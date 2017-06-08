package com.cqfc.order.datacenter;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.UpdateSyncStatus;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class UpdateSyncStatusSqlExecuteTask implements Runnable {

	private static final List<BlockingQueue<UpdateSyncStatus>> syncListQueue = OrderBuffer.getSyncListQueue();

	private static final int MAX_NUM = 300;

	private DataSource dataSource;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int index;

	private BlockingQueue<UpdateSyncStatus> syncQueue;

	public UpdateSyncStatusSqlExecuteTask(ApplicationContext applicationContext, AtomicBoolean running, int index) {
		this.applicationContext = applicationContext;
		this.running = running;
		this.index = index;
		this.syncQueue = syncListQueue.get(index);
	}

	public void stop() {
		running.set(false);
	}

	public void run() {
		Connection connection = null;
		Statement statement = null;
		try {
			String masterDbBean = BatchUtil.getMasterDbBean(index);
			dataSource = applicationContext.getBean(masterDbBean, PooledDataSource.class);
			List<UpdateSyncStatus> list = new ArrayList<UpdateSyncStatus>();
			while (running.get()) {
				if (syncQueue.size() == 0) {
					Thread.sleep(10);
				}
				syncQueue.drainTo(list, MAX_NUM);
				if (list.size() > 0) {
					try {
						Collections.sort(list, new Comparator<UpdateSyncStatus>() {
							public int compare(UpdateSyncStatus arg0, UpdateSyncStatus arg1) {
								return String.valueOf(arg0.getOrderNo()).compareTo(String.valueOf(arg1.getOrderNo()));
							}
						});

						connection = dataSource.getConnection();
						connection.setAutoCommit(false);
						statement = connection.createStatement();
						for (UpdateSyncStatus updateSyncStatus : list) {
							String tableName = DbGenerator.getOrderTableName(updateSyncStatus.getTradeId());
							statement.addBatch(getUpdateSyncSql(updateSyncStatus, tableName));
							Log.fucaibiz.info("批量更新订单同步状态,orderNo=%d,dbName=%s,tableName=%s",
									updateSyncStatus.getOrderNo(), masterDbBean, tableName);
						}
						try {
							int[] batchResult = statement.executeBatch();
							connection.commit();

							for (int i = 0, len = list.size(); i < len; i++) {
								UpdateSyncStatus updateSyncStatus = list.get(i);
								long orderNo = updateSyncStatus.getOrderNo();
								int sync = updateSyncStatus.getSync();

								int returnValue = batchResult[i];

								Log.fucaibiz.info("批量更新订单同步状态成功,orderNo=%d,syncStatus=%d,returnValue=%d", orderNo,
										sync, returnValue);

								if (returnValue > 0) {
									OrderMemcacheUtil.updateOrderSyncMemcache(orderNo, sync);
								}

								updateSyncMap(orderNo, BatchConstant.BATCH_OPERATE_SUCCESS);
							}
						} catch (Exception e1) {
							Log.fucaibiz.error("批量更新订单同步状态异常,进行逐条更新操作", e1);
							long orderNo = 0;
							try {
								connection.rollback();
								closeConnection(connection, statement);

								connection = dataSource.getConnection();
								statement = connection.createStatement();
								connection.setAutoCommit(true);

								for (UpdateSyncStatus updateSyncStatus : list) {
									orderNo = updateSyncStatus.getOrderNo();
									int sync = updateSyncStatus.getSync();
									String tableName = DbGenerator.getOrderTableName(updateSyncStatus.getTradeId());
									boolean operateValue = statement.execute(getUpdateSyncSql(updateSyncStatus,
											tableName));
									Log.fucaibiz.info("逐条更新订单同步状态完成,orderNo=%d,syncValue=%d,operateValue=%b", orderNo,
											sync, operateValue);
									int operate = operateValue ? BatchConstant.BATCH_OPERATE_SUCCESS
											: BatchConstant.BATCH_OPERATE_FAILED;
									if (operateValue) {
										OrderMemcacheUtil.updateOrderSyncMemcache(orderNo, sync);
									}
									updateSyncMap(orderNo, operate);
								}
							} catch (Exception e2) {
								updateSyncMap(orderNo, BatchConstant.BATCH_OPERATE_FAILED);
								Log.fucaibiz.error("逐条更新订单同步状态发生异常", e2);
								// stop();
								// return;
							}
						}
					} catch (Exception e) {
						Log.fucaibiz.error("update order sync,batch written to the database error", e);
					} finally {
						list.clear();
						closeConnection(connection, statement);
						connection = null;
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.debug("batch update order sync error", e);
		}
	}

	private String getUpdateSyncSql(UpdateSyncStatus updateSyncStatus, String tableName) {
		long orderNo = 0;
		String sql = "";
		try {
			orderNo = updateSyncStatus.getOrderNo();
			int sync = updateSyncStatus.getSync();
			String statusIn = Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue() + ","
					+ Order.OrderStatus.REFUND_SUCCESS.getValue() + "," + Order.OrderStatus.ORDER_CANCEL.getValue();
			sql = "update " + tableName + " set isSyncSuccess=" + sync + " where orderNo=" + orderNo
					+ " and orderStatus in (" + statusIn + ")";
			Log.fucaibiz.info("批更新订单同步状态拼接SQL,orderNo=%d,sql=%s", orderNo, sql);
		} catch (Exception e) {
			Log.fucaibiz.error("批更新订单同步状态拼接SQL发生异常,orderNo=" + orderNo, e);
		}
		return sql;
	}

	private void updateSyncMap(long orderNo, int operateValue) {
		try {
			BatchConstant.orderSyncMap.get(orderNo).setOperateIdentifier(operateValue);
			BatchConstant.orderSyncMap.get(orderNo).setIsBackReq(true);
			UpdateSyncStatus syncMsg = BatchConstant.orderSyncMap.get(orderNo);
			synchronized (syncMsg) {
				syncMsg.notify();
			}
		} catch (Exception e) {
			Log.fucaibiz.error("批更新订单同步状态后更新MAP发生异常,orderNo=" + orderNo, e);
		}
	}

	private void closeConnection(Connection connection, Statement statement) {
		try {
			if (null != statement) {
				statement.close();
			}
			if (null != connection) {
				connection.setAutoCommit(true);
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("batch operate order connection close error", e);
		}
	}
}
