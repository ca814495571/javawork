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
import com.cqfc.order.model.UpdateOrderStatus;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.DbGenerator;
import com.cqfc.util.ConstantsUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class UpdateOrderStatusSqlExecuteTask implements Runnable {

	private static final List<BlockingQueue<UpdateOrderStatus>> statusQueueList = OrderBuffer.getStatusListQueue();

	private static final int MAX_NUM = 500;

	private DataSource dataSource;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int index;

	private BlockingQueue<UpdateOrderStatus> statusQueue;

	public UpdateOrderStatusSqlExecuteTask(ApplicationContext applicationContext, AtomicBoolean running, int index) {
		this.applicationContext = applicationContext;
		this.running = running;
		this.index = index;
		this.statusQueue = statusQueueList.get(index);
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
			List<UpdateOrderStatus> list = new ArrayList<UpdateOrderStatus>();
			while (running.get()) {
				if (statusQueue.size() > 0) {
					statusQueue.drainTo(list, MAX_NUM);
				} else {
					Thread.sleep(10);
				}
				if (list.size() > 0) {
					// int listSize = list.size();
					// long beginTime = System.currentTimeMillis();
					try {
						Collections.sort(list, new Comparator<UpdateOrderStatus>() {
							public int compare(UpdateOrderStatus arg0, UpdateOrderStatus arg1) {
								return String.valueOf(arg0.getOrderNo()).compareTo(String.valueOf(arg1.getOrderNo()));
							}
						});

						connection = dataSource.getConnection();
						connection.setAutoCommit(false);
						statement = connection.createStatement();
						for (UpdateOrderStatus updateOrderStatus : list) {
							String tableName = DbGenerator.getOrderTableName(updateOrderStatus.getTradeId());
							statement.addBatch(getUpdateOrderSql(updateOrderStatus, tableName));
							Log.fucaibiz.info("批量更新订单状态,orderNo=%d,dbName=%s,tableName=%s",
									updateOrderStatus.getOrderNo(), masterDbBean, tableName);
						}
						try {
							int[] batchResult = statement.executeBatch();
							connection.commit();

							long orderNo = 0;
							try {
								for (int i = 0, len = list.size(); i < len; i++) {
									UpdateOrderStatus updateOrderStatus = list.get(i);

									orderNo = updateOrderStatus.getOrderNo();
									int orderStatus = updateOrderStatus.getOrderStatus();
									int returnValue = batchResult[i];

									Log.fucaibiz.info("订单状态批量更新返回值,orderNo=%d,orderStatus=%d,returnValue=%d", orderNo,
											orderStatus, returnValue);
									int operatValue = BatchConstant.BATCH_OPERATE_FAILED;
									if (returnValue > 0) {
										operatValue = BatchConstant.BATCH_OPERATE_SUCCESS;
										OrderMemcacheUtil.updateStatusMemcache(orderNo, orderStatus);
									}
									updateStatusMap(orderNo, orderStatus, operatValue);
									Log.fucaibiz.info("订单状态批量更新完成,orderNo=%d,orderStatus=%d,operatValue=%d", orderNo,
											orderStatus, operatValue);
								}
							} catch (Exception e) {
								Log.fucaibiz.error("订单状态批更新之后,处理订单信息发生异常,orderNo=" + orderNo, e);
							}
						} catch (Exception e1) {
							Log.fucaibiz.error("批量更新订单状态异常,进行逐条更新操作,masterDbBean=" + masterDbBean, e1);
							long orderNo = 0;
							int orderStatus = 0;
							try {
								connection.rollback();
								closeConnection(connection, statement);

								connection = dataSource.getConnection();
								statement = connection.createStatement();
								connection.setAutoCommit(true);

								for (UpdateOrderStatus updateOrderStatus : list) {
									orderNo = updateOrderStatus.getOrderNo();
									orderStatus = updateOrderStatus.getOrderStatus();
									String tableName = DbGenerator.getOrderTableName(updateOrderStatus.getTradeId());
									int operateValue = statement.executeUpdate(getUpdateOrderSql(updateOrderStatus,
											tableName));
									Log.fucaibiz.info("逐条更新订单状态完成,orderNo=%d,orderStatus=%d,operateValue=%d", orderNo,
											orderStatus, operateValue);
									if (operateValue > 0) {
										OrderMemcacheUtil.updateStatusMemcache(orderNo, orderStatus);
									}
									int operateIdentifier = operateValue > 0 ? BatchConstant.BATCH_OPERATE_SUCCESS
											: BatchConstant.BATCH_OPERATE_FAILED;
									updateStatusMap(orderNo, orderStatus, operateIdentifier);
								}
							} catch (Exception e2) {
								updateStatusMap(orderNo, orderStatus, BatchConstant.BATCH_OPERATE_FAILED);
								Log.fucaibiz.error("逐条更新订单发生异常", e2);
								// stop();
								// return;
							}
						}
					} catch (Exception e) {
						Log.fucaibiz.error("update order status,batch written to the database error", e);
					} finally {
						list.clear();
						closeConnection(connection, statement);
						connection = null;
					}
					// long endTime = System.currentTimeMillis();
					// Log.fucaibiz.info("batch update status list,size=%d,totalTime=%d",
					// listSize, (endTime - beginTime));
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.debug("batch update order status error", e);
		}
	}

	/**
	 * 订单状态变更前置条件： ①.1待付款-->2已付款; ②.2已付款-->3出票中; ③.2已付款、3出票中-->4已出票待开奖;
	 * ④.2已付款、3出票中-->5出票失败; ⑤.2已付款、3出票中、5出票失败-->9退款中;
	 * ⑥.2已付款、3出票中、5出票失败、9退款中-->10退款成功; ⑦.1待付款-->11订单取消
	 * 
	 * @param updateOrderStatus
	 * @param tableName
	 * @return
	 */
	private String getUpdateOrderSql(UpdateOrderStatus updateOrderStatus, String tableName) {
		long orderNo = 0;
		String sql = "";
		try {
			orderNo = updateOrderStatus.getOrderNo();
			int status = updateOrderStatus.getOrderStatus();
			StringBuffer statusIn = new StringBuffer();
			if (status == Order.OrderStatus.HAS_PAYMENT.getValue()
					|| status == Order.OrderStatus.ORDER_CANCEL.getValue()) {
				statusIn.append(Order.OrderStatus.WAIT_PAYMENT.getValue());
			} else if (status == Order.OrderStatus.IN_TICKET.getValue()) {
				statusIn.append(Order.OrderStatus.HAS_PAYMENT.getValue());
			} else if (status == Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
					|| status == Order.OrderStatus.DRAWER_FAILURE.getValue()) {
				statusIn.append(Order.OrderStatus.HAS_PAYMENT.getValue() + "," + Order.OrderStatus.IN_TICKET.getValue());
			} else if (status == Order.OrderStatus.REFUNDING.getValue()) {
				statusIn.append(Order.OrderStatus.HAS_PAYMENT.getValue() + "," + Order.OrderStatus.IN_TICKET.getValue()
						+ "," + Order.OrderStatus.DRAWER_FAILURE.getValue());
			} else if (status == Order.OrderStatus.REFUND_SUCCESS.getValue()) {
				statusIn.append(Order.OrderStatus.HAS_PAYMENT.getValue() + "," + Order.OrderStatus.IN_TICKET.getValue()
						+ "," + Order.OrderStatus.DRAWER_FAILURE.getValue() + ","
						+ Order.OrderStatus.REFUNDING.getValue());
			}
			if (status == Order.OrderStatus.ORDER_CANCEL.getValue()) {
				sql = "update " + tableName + " set orderStatus=" + status + ",errCodeStatus='"
						+ ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR + "',errCodeRemark='扣款异常或者帐号钱数不足' where orderNo="
						+ orderNo + " and orderStatus in (" + statusIn.toString() + ");";
			} else {
				sql = "update " + tableName + " set orderStatus=" + status + " where orderNo=" + orderNo
						+ " and orderStatus in (" + statusIn.toString() + ");";
			}
			Log.fucaibiz.info("批更新订单状态拼接SQL,orderNo=%d,sql=%s", orderNo, sql);
		} catch (Exception e) {
			Log.fucaibiz.error("批更新订单状态拼接SQL发生异常,orderNo=" + orderNo, e);
		}
		return sql;
	}

	private void updateStatusMap(long orderNo, int orderStatus, int operateValue) {
		String mapKey = BatchUtil.getOrderStatusMapKey(orderNo, orderStatus);
		Log.fucaibiz.debug("update status map,orderNo=%d,orderStatus=%d,value=%d", orderNo, orderStatus, operateValue);
		try {
			if (orderStatus == Order.OrderStatus.HAS_PAYMENT.getValue()
					|| orderStatus == Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
					|| orderStatus == Order.OrderStatus.DRAWER_FAILURE.getValue()
					|| orderStatus == Order.OrderStatus.REFUND_SUCCESS.getValue()
					|| orderStatus == Order.OrderStatus.ORDER_CANCEL.getValue()) {
				UpdateOrderStatus statusMsg = BatchConstant.orderStatusMap.get(mapKey);
				statusMsg.setOperateIdentifier(operateValue);
				statusMsg.setIsBackReq(true);
				synchronized (statusMsg) {
					statusMsg.notify();
				}
			} else {
				Log.fucaibiz.debug("orderStatusMap delete,orderNo=%d,orderStatus=%d,value=%d", orderNo, orderStatus,
						operateValue);
				BatchConstant.orderStatusMap.remove(mapKey);
			}
		} catch (Exception e) {
			Log.fucaibiz.error("处理更新订单状态MAP发生异常,orderNo=" + orderNo, e);
		}
	}

	private void closeConnection(Connection connection, Statement statement) {
		try {
			if (null != statement) {
				statement.close();
			}
			if (null != connection) {
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("batch operate order connection close error", e);
		}
	}
}
