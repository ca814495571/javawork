package com.cqfc.user.order.task;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.userorder.Order;
import com.cqfc.user.order.dataCache.UserOrderBuffer;
import com.jami.util.CountLog;
import com.jami.util.DataSourceUtil;

public class AddOrderExcSqlTask implements Runnable {

	private static final List<BlockingQueue<Order>> listQueue = UserOrderBuffer
			.getListQueue();

	private BlockingQueue<Order> queue;

	private static final int MAX_NUM = 500;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int num;

	public AddOrderExcSqlTask(ApplicationContext applicationContext,
			AtomicBoolean running, int num) {

		this.queue = listQueue.get(num);
		this.num = num;
		this.applicationContext = applicationContext;
		this.running = running;

	}

	public void stop() {
		running.set(false);
	}

	@Override
	public void run() {

		String dbName = DataSourceUtil.getDateSourceName(num);
		String masterDbName = DataSourceUtil.MASTER + dbName;
		DataSource dataSource = null;
		DataSource tempDataSource = null;

		List<Order> list = new ArrayList<Order>();
		// 全局库的数据连接
		Connection conn = null;
		Statement statement = null;
		// 临时库的数据连接
		Connection tempConn = null;
		Statement tempStatement = null;
		String tempMasterDbName = DataSourceUtil.MASTER
				+ DataSourceUtil.TempOrderDbName;
		String tempTableName = "";
		Order lastOrder = null;
		int b;
		try {

			dataSource = applicationContext.getBean(masterDbName,
					PooledDataSource.class);
			tempDataSource = applicationContext.getBean(tempMasterDbName,
					PooledDataSource.class);
			while (running.get()) {

				if (queue.size() == 0) {
					try {
						Thread.sleep(5000);

					} catch (InterruptedException e) {
						CountLog.addUserOrder.error(Thread.currentThread()
								.getName() + ",sleep异常", e);
						CountLog.run.error(Thread.currentThread().getName()
								+ ",sleep异常", e);
					}
					continue;
				}
				queue.drainTo(list, MAX_NUM);

				try {

					conn = dataSource.getConnection();
					statement = conn.createStatement();
					conn.setAutoCommit(false);


					tempConn = tempDataSource.getConnection();
					tempStatement = tempConn.createStatement();
					tempConn.setAutoCommit(false);
					tempTableName = DataSourceUtil.TempOrderTableName;

					for (Order order : list) {

						if (!running.get()) {
							return;
						}
						statement.addBatch(getSql(order,
								DataSourceUtil.getTableName(String
										.valueOf(order.getUserId()))));

						order.setExt(dbName
								+ "#"
								+ DataSourceUtil.getTableName(String
										.valueOf(order.getUserId())));
						
						tempStatement.addBatch(getSql(order, tempTableName));

					}

					try {

						statement.executeBatch();
						conn.commit();
						conn.setAutoCommit(true);
					} catch (Exception e) {
						CountLog.addUserOrder.warn(masterDbName
								+ "同步订单时，批量插入全局订单数据库出现异常", e);
						CountLog.run.warn(masterDbName
								+ "同步订单时，批量插入全局订单数据库出现异常", e);
						try {

							conn.rollback();
							DataSourceUtil.closeConn(conn, statement);
							conn = dataSource.getConnection();
							statement = conn.createStatement();
							conn.setAutoCommit(true);
							for (Order order : list) {

								statement.execute(getSql(order, DataSourceUtil
										.getTableName(String.valueOf(order
												.getUserId()))));
							}
							CountLog.run.info("逐个插入全局数据库成功!");
						} catch (Exception e2) {
							CountLog.addUserOrder.error(masterDbName
									+ "同步订单时，逐个插入全局数据库出现异常,需及时处理", e2);
							CountLog.run.error(masterDbName
									+ "同步订单时，逐个插入全局数据库出现异常,需及时处理", e2);
							running.set(false);
							return;
						}
					}

					try {

						tempStatement.executeBatch();
						tempConn.commit();
						tempConn.setAutoCommit(true);
					} catch (Exception e) {

						CountLog.addUserOrder.warn(tempMasterDbName
								+ "同步订单时，批量插入订单临时数据库出现异常", e);
						CountLog.run.warn(tempMasterDbName
								+ "同步订单时，批量插入订单临时数据库出现异常", e);
						try {
							tempConn.rollback();
							DataSourceUtil.closeConn(tempConn, tempStatement);
							tempConn = tempDataSource.getConnection();
							tempStatement = tempConn.createStatement();
							tempConn.setAutoCommit(true);
							tempTableName = DataSourceUtil.TempOrderTableName;
							for (Order order : list) {
								order.setExt(dbName
										+ "#"
										+ DataSourceUtil.getTableName(String
												.valueOf(order.getUserId())));
								tempStatement.execute(getSql(order,
										tempTableName));
								
							}
							CountLog.run.info("逐个插入全局数据库成功!");
						} catch (Exception e2) {
							conn.rollback();
							CountLog.addUserOrder.error(tempMasterDbName
									+ "同步订单时，逐个插入订单临时数据库出现异常,需及时处理", e);
							CountLog.run.error(tempMasterDbName
									+ "同步订单时，逐个插入订单临时数据库出现异常,需及时处理", e);
							running.set(false);
							return;
						}

					}

					CountLog.addUserOrder.info(masterDbName
							+ "成功批处理完成,处理了" + list.size() + "个订单");

				} catch (Exception e) {

					CountLog.addUserOrder.error("系统异常", e);
					CountLog.run.error("系统异常", e);
					running.set(false);
					return;

				} finally {
					try {

						if (running.get()) {

							lastOrder = list.get(list.size() - 1);
							b = tempStatement.executeUpdate("update "
									+ DataSourceUtil.RecoveryIndexTable
									+ " set orderNo='" + lastOrder.getOrderNo()
									+ "' where dbName='" + dbName
									+ "'");
							if (b != 1) {

								tempStatement.execute("insert into "
										+ DataSourceUtil.RecoveryIndexTable
										+ " (dbName,orderNo,createTime) "
										+ " values ('" + dbName + "','"
										+ lastOrder.getOrderNo() + "', now())");
							}

						}
					} catch (Exception e2) {
						CountLog.addUserOrder.warn("修改索引表异常", e2);
					}
					list.clear();
					DataSourceUtil.closeConn(conn, statement);
					DataSourceUtil.closeConn(tempConn, tempStatement);
				}

			}
		} catch (Exception e) {
			CountLog.addUserOrder.error("系统异常", e);
			CountLog.run.error("系统异常", e);
			running.set(false);
			return;
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
