package com.cqfc.user.order.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.protocol.userorder.Order;
import com.cqfc.protocol.userorder.PcUserOrder;
import com.cqfc.user.order.dao.RecoveryIndexDao;
import com.cqfc.user.order.dao.UserOrderDao;
import com.cqfc.user.order.dataCache.UserOrderBuffer;
import com.cqfc.user.order.service.IUserOrderService;
import com.cqfc.user.order.task.AddOrderExcSqlTask;
import com.cqfc.user.order.task.AddOrderToQueueTask;
import com.cqfc.user.order.task.RecoverOrderFromQueueTask;
import com.cqfc.util.Pair;
import com.jami.util.CountLog;
import com.jami.util.DataSourceContextHolder;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;
import com.jami.util.OrderLogger;
import com.jami.util.UserOrderLogUtil;

@Service
public class UserOrderServiceImpl implements IUserOrderService {

	@Autowired
	UserOrderDao userOrderDao;

	@Autowired
	RecoveryIndexDao recoveryIndexDao;

	/**
	 * 合作商的缓存key(ConstantsUtil.MODULENAME_PARTNER_ORDER+唯一值)
	 */
	@Autowired
	private MemcachedClient memcachedClient;

	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	ThreadPoolTaskExecutor addOrderTaskExecutor;

	/**
	 * 返回值 -1 数据库异常 1 添加成功 -8 用户Id不存在 -100 违反唯一性约束
	 * 
	 */
	@Override
	public int addUserOrder(Order userOrder) {
		return UserOrderBuffer.addOrder(userOrder);
	}

	@Override
	public Order getUserOrderByUserId(long userId, String orderNo) {

		Log.run.debug("根据用户ID和订单编号查询用户订单");
		if (userId == 0 || "".equals(userId)) {

			Log.run.debug("用户Id不存在");
			return null;
		}
		Order order = null;

		String tableName = DataSourceUtil.getTableName(String.valueOf(userId));
		String dbName = DataSourceUtil.SLAVE
				+ DataSourceUtil.getDbName(String.valueOf(userId));

		DataSourceContextHolder.setDataSourceType(dbName);

		order = new Order();

		order.setUserId(userId);
		order.setOrderNo(orderNo);
		List<Order> orders = userOrderDao.getUserOrderByWhere(order, tableName);

		if (orders.size() > 0) {
			Log.run.debug("根据用户ID和订单编号查询用户订单成功...");
			return orders.get(0);
		}

		return null;
	}

	@Override
	public PcUserOrder getUserOrder(Order order, int pageNum, int pageSize) {

		Log.run.debug("根据多条件分页查询用户订单...");
		if (pageNum < 1) {
			pageNum = 1;
		}

		if (pageSize > 50) {
			pageSize = 50;
		}

		String tableName = DataSourceUtil.getTableName(String.valueOf(order
				.getUserId()));
		String dbName = DataSourceUtil.SLAVE
				+ DataSourceUtil.getDbName(String.valueOf(order.getUserId()));

		DataSourceContextHolder.setDataSourceType(dbName);
		return userOrderDao.getUserOrder(order, pageNum, pageSize, tableName);
	}

	@Override
	public Order getUserPrizeStatus(long userId, String partnerId,
			String orderNo, String ticketId) {
		Log.run.debug("根据用户ID、合作商id、订单编号、交易号查询用户订单");
		if (userId == 0) {
			return null;
		}
		Order order = null;
		List<Order> orders = new ArrayList<Order>();
		String userIdTmep = String.valueOf(userId);

		String tableName = DataSourceUtil.getTableName(userIdTmep);

		String dbName = DataSourceUtil.SLAVE
				+ DataSourceUtil.getDbName(userIdTmep);

		DataSourceContextHolder.setDataSourceType(dbName);
		if (orderNo != null && !"".equals(orderNo)) {

			order = new Order();
			order.setPartnerId(partnerId);
			order.setUserId(userId);
			order.setOrderNo(orderNo);
			order.setTradeId(ticketId);
			orders = userOrderDao.getUserOrderByWhere(order, tableName);

			if (orders.size() > 0) {
				Log.run.debug("根据用户ID、合作商id、订单编号、交易号查询用户订单成功..");

				return orders.get(0);
			}

		} else {

			order = new Order();
			order.setPartnerId(partnerId);
			order.setUserId(userId);
			order.setTradeId(ticketId);

			order = userOrderDao.getMaxIssueNoOrder(order, tableName);
			return order;
		}
		return null;
	}


	

	@Override
	public void initAddOrderTask(ApplicationContext applicationContext) {
		AtomicBoolean running = new AtomicBoolean(true);
		
		for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {

			addOrderTaskExecutor.submit(new AddOrderExcSqlTask(
					applicationContext, running, i));
		}

	}

	@Override
	public int logOrderToQueue(List<Pair<File, BlockingQueue>> pairs) {

		try {
			
			UserOrderBuffer.initListQueue();
			List<BlockingQueue<Order>> queues= UserOrderBuffer.getListQueue();
			File file = null;
			Pair<File, BlockingQueue> pair = null;
			for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {

				file = new File(OrderLogger.BACKFILE_PREFIX
						+ DataSourceUtil.getDateSourceName(i)
						+ OrderLogger.LOG_FILE_PATH_SUBBIX);
				Log.run.info("日志文件路径:"+file.getAbsolutePath());
				if (!file.exists() || file.isDirectory()) {
					continue;
				}
				// 将每一个日志文件的需要回复的订单数据放入到队列中
				pair = new Pair<File, BlockingQueue>(file, queues.get(i));
				pairs.add(pair);
			}

			// true 标示正常
			AtomicBoolean atomicBoolean = new AtomicBoolean(true);
			CountDownLatch countDownLatch = new CountDownLatch(pairs.size());
			for (Pair<File, BlockingQueue> p : pairs) {
				threadPoolTaskExecutor.submit(new AddOrderToQueueTask(p,
						countDownLatch, atomicBoolean));

			}
			countDownLatch.await();

			if (!atomicBoolean.get()) {

				CountLog.run.fatal("恢复订单日志将订单放入队列中失败...");
				return -1;
			}
		} catch (InterruptedException e) {
			CountLog.run.error("计数器调用await()异常", e);
			return -1;
		}

		return 1;
	}

	@Override
	public int queueToDb(List<Pair<File, BlockingQueue>> pairs) {

		AtomicBoolean atomicBoolean = new AtomicBoolean(true);
		CountDownLatch countDownLatch = new CountDownLatch(pairs.size());
		for (Pair p : pairs) {

			threadPoolTaskExecutor.submit(new RecoverOrderFromQueueTask(p,
					countDownLatch, atomicBoolean));
		}

		try {
			countDownLatch.await();
			if (!atomicBoolean.get()) {
				CountLog.run.fatal("恢复订单日志,将队列中的订单批量入库失败..");
				return -1;
			}
		} catch (InterruptedException e) {
			CountLog.run.error("计数器调用await()异常", e);
			return -1;
		}

		return 1;
	}

	@Override
	public RecoverOrderIndex getIndexByDbName(String dbName) {

		RecoverOrderIndex recoverOrderIndex = null;
		try {

			recoverOrderIndex = recoveryIndexDao.getIndexByDbName(dbName);
		} catch (Exception e) {
			CountLog.run.error("数据库异常", e);
		}
		return recoverOrderIndex;
	}

	@Override
	public int updateFlag(String dbName, int flag) {
		return recoveryIndexDao.updateFlag(dbName, flag);
	}

	@Override
	public int addOrderToQueue(Pair<File, BlockingQueue> pair, String orderNo) {

		File file = pair.first();
		BlockingQueue queue = pair.second();

		// 标示是否从日志中找到订单号
		boolean b = false;
		b = backOrderByOrderNo(file, queue, orderNo);

		if (!b) {

			String fileName = file.getName();
			file = getLastUpdateFile(fileName);

			// 找到.log.1 .log.2......文件中最后一个被修改的文件进行恢复
			b = backOrderByOrderNo(file, queue, orderNo);
		}

		if (b) {
			return 1;
		} else {
			Log.run.error(file.getName() + "没有找到orderNo:" + orderNo);
			return -1;
		}
	}

	/**
	 * 找到最后修改的文件
	 * 
	 * @param fileName
	 * @return
	 */
	public File getLastUpdateFile(String fileName) {
		File file = null;
		try {
			file = new File(OrderLogger.BACKFILE_PREFIX);
			long lastTime = 0;
			long lastTimeTemp = 0;
			File[] files = file.listFiles();

			List<File> filesTocompare = new ArrayList<File>();
			for (File f : files) {

				if (f.getName().startsWith(fileName) && f.getName().length()>fileName.length()) {
					filesTocompare.add(f);
				}
			}
			if(filesTocompare.size()>0){
				file = filesTocompare.get(0);
				for (File f : filesTocompare) {
					lastTimeTemp = f.lastModified();
					if (lastTimeTemp > lastTime) {
						lastTime = lastTimeTemp;
						file = f;
					}
				}
				
			}
			
		} catch (Exception e) {
			Log.run.error("寻找最后修改的日志文件出现异常" + fileName);
		}

		return file;
	}

	public boolean backOrderByOrderNo(File file, BlockingQueue queue,
			String orderNo) {

		RandomAccessFile rf = null;
		Order order = null;
		boolean b = false;
		try {
			rf = new RandomAccessFile(file, "r");
			long len = rf.length();

			if (len == 0) {
				return true;
			}
			long start = rf.getFilePointer();
			long end = start + len - 2;
			String line = "";
			rf.seek(end);
			int c = -1;
			String orderNoInLog;
			while (end > start) {

				c = rf.read();
				if (c == '\n' || end == 1) {

					line = rf.readLine();
					if (line != null && !"".equals(line)) {

						line = new String(line.getBytes("ISO-8859-1"), "utf-8");

						orderNoInLog = line.split(UserOrderLogUtil.SEPERATOR)[5];

						if (orderNoInLog != null
								&& orderNoInLog.equals(orderNo)) {
							b = true;
							CountLog.run.info(file.getAbsolutePath()
									+ "滚动的日志文件中找到orderNo:" + orderNo);
							break;
						}

						order = UserOrderLogUtil.convertStr2Log(line);
						queue.offer(order);
					}
				}
				end--;
				rf.seek(end);
			}
			
		}catch (Exception e){
			Log.run.error("订单日志恢复时出现异常", e);
		} finally {
			try {
				rf.close();
			} catch (IOException e) {
				Log.run.error("订单日志恢复时关闭IO流出异常,文件名称" + file.getName(), e);
			}
		}

		return b;
	}

	

}
