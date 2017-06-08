package com.cqfc.user.order.task;

import java.io.File;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.userorder.Order;
import com.cqfc.util.Pair;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.CountLog;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;

/**
 * StartQueueToDbTask的执行体
 * 
 * @author admin
 *
 */
public class RecoverOrderFromQueueTask implements Runnable {

	private Pair<File, BlockingQueue> pair;
	private CountDownLatch countDownLatch;
	private AtomicBoolean atomicBoolean;
	private static final int MAX_NUM = 500;

	public RecoverOrderFromQueueTask(Pair<File, BlockingQueue> pair,
			CountDownLatch countDownLatch, AtomicBoolean atomicBoolean) {
		this.pair = pair;
		this.countDownLatch = countDownLatch;
		this.atomicBoolean = atomicBoolean;
	}

	@Override
	public void run() {

		DataSource dataSource = null;
		Connection conn = null;
		Statement statement = null;
		List<Order> lists = new ArrayList<Order>();

		// 临时库的数据连接
		Connection tempConn = null;
		Statement tempStatement = null;
		DataSource tempDataSource = null;
		String tempMasterDbName = DataSourceUtil.MASTER
				+ DataSourceUtil.TempOrderDbName;
		String tempTableName = "";
		int waitNum = 0 ;
		int doneNum = 0;
		try {

			BlockingQueue queue = pair.second();
			waitNum = queue.size();
			System.out.println( pair.first().getName()+waitNum);
			while (queue.size() > 0) {

				try {

					queue.drainTo(lists, MAX_NUM);
					String fileName = pair.first().getName().split("\\.")[0];

					ApplicationContext applicationContext = ApplicationContextProvider
							.getApplicationContext();

					dataSource = applicationContext.getBean(
							DataSourceUtil.MASTER + fileName,
							PooledDataSource.class);
					conn = dataSource.getConnection();
					statement = conn.createStatement();
					tempDataSource = applicationContext.getBean(
							tempMasterDbName, PooledDataSource.class);
					tempConn = tempDataSource.getConnection();
					tempStatement = tempConn.createStatement();
					tempTableName = DataSourceUtil.TempOrderTableName;
					conn.setAutoCommit(false);
					tempConn.setAutoCommit(false);
					if (!atomicBoolean.get()) {
						return;
					}

					for (Order order : lists) {

						statement.addBatch(getSql(order,
								DataSourceUtil.getTableName(String
										.valueOf(order.getUserId()))));
						

						order.setExt(fileName
								+ "#"
								+ DataSourceUtil.getTableName(String
										.valueOf(order.getUserId())));
						
						
						tempStatement.addBatch(getSql(order, tempTableName));
					}

					
					try {

						statement.executeBatch();
					} catch (Exception e) {
						CountLog.addUserOrder.warn(fileName
								+ "同步订单时，批量插入全局订单数据库出现异常", e);
						CountLog.run.warn(fileName
								+ "同步订单时，批量插入全局订单数据库出现异常", e);
						try {

							conn.rollback();
							DataSourceUtil.closeConn(conn, statement);
							conn = dataSource.getConnection();
							statement = conn.createStatement();
							conn.setAutoCommit(true);
							for (Order order : lists) {

								statement.execute(getSql(order, DataSourceUtil
										.getTableName(String.valueOf(order
												.getUserId()))));
							}
							CountLog.run.info("逐个插入全局订单数据库成功!");
						} catch (Exception e2) {
							CountLog.addUserOrder.error(fileName
									+ "同步订单时，逐个插入全局订单数据库出现异常,需及时处理", e2);
							CountLog.run.error(fileName
									+ "同步订单时，逐个插入全局订单数据库出现异常,需及时处理", e2);
							atomicBoolean.set(false);
							return;
						}
					}
				
					try {

						tempStatement.executeBatch();
					} catch (BatchUpdateException e) {
						
						CountLog.run.warn(e.getUpdateCounts().length+":"+lists.get(e.getUpdateCounts().length-1));
						CountLog.addUserOrder.warn(fileName
								+ "同步订单时，批量插入临时订单数据库出现异常", e);
						CountLog.run.warn(fileName
								+ "同步订单时，批量插入临时订单数据库出现异常", e);
						try {

							tempConn.rollback();
							DataSourceUtil.closeConn(tempConn, tempStatement);
							tempConn = tempDataSource.getConnection();
							tempStatement = tempConn.createStatement();
							tempConn.setAutoCommit(true);
							for (Order order : lists) {
								order.setExt(fileName
										+ "#"
										+ DataSourceUtil.getTableName(String.valueOf(order
												.getUserId())));
								tempStatement.execute(getSql(order, tempTableName));
							}
							CountLog.run.info("逐个插入临时订单数据库成功!");
						} catch (Exception e2) {
							CountLog.addUserOrder.error(fileName
									+ "同步订单时，逐个插入临时订单数据库出现异常,需及时处理", e2);
							CountLog.run.error(fileName
									+ "同步订单时，逐个插入临时订单数据库出现异常,需及时处理", e2);
							atomicBoolean.set(false);
							return;
						}
					}
				
					
					if (!conn.getAutoCommit()) {
						conn.commit();
					}
					if (!tempConn.getAutoCommit()) {
						tempConn.commit();
					}
					conn.setAutoCommit(true);
					tempConn.setAutoCommit(true);
					doneNum += lists.size(); 
					lists.clear();
				} catch (Exception e) {
					Log.run.error(pair.first().getName()+"日志文件恢复出现异常",e);
					atomicBoolean.set(false);
					return;
				} finally {
					
					DataSourceUtil.closeConn(tempConn, tempStatement);
					DataSourceUtil.closeConn(conn, statement);
				}
			}

		} catch (Exception e) {

			CountLog.addUserOrder.error(pair.first().getName() + "恢复日志异常", e);
			atomicBoolean.set(false);
		} finally {
			Log.run.info(pair.first().getName()+"文件待恢复的订单数量:"+waitNum+"----已恢复订单数量:"+doneNum);
			System.out.println(pair.first().getName()+"文件待恢复的订单数量:"+waitNum+"----已恢复订单数量:"+doneNum);
			countDownLatch.countDown();
		}
	}

	public String getSql(Order order, String tableName) {

		return " replace into "
				+ tableName
				+ " (lotteryId,partnerId,userId,issueNo,orderNo,"
				+ "orderStatus,totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,"
				+ "multiple,playType,paySerialNumber,realName,cardNo,mobile,createTime,ext,"
				+ "orderType,tradeId) values ('" + order.getLotteryId() + "','"
				+ order.getPartnerId() + "'," + order.getUserId() + ",'"
				+ order.getIssueNo() + "','" + order.getOrderNo() + "',"
				+ order.getOrderStatus() + "," + order.getTotalAmount() + ","
				+ order.getWinPrizeMoney() + "," + order.getPrizeAfterTax()
				+ ",'" + order.getOrderContent() + "'," + order.getStakeNum()
				+ "," + order.getMultiple() + ",'" + order.getPlayType()
				+ "','" + order.getPaySerialNumber() + "','"
				+ order.getRealName() + "','" + order.getCardNo() + "','"
				+ order.getMobile() + "','" + order.getCreateTime() + "','"
				+ order.getExt() + "'," + order.getOrderType() + ",'"
				+ order.getTradeId() + "')";

	}

}
