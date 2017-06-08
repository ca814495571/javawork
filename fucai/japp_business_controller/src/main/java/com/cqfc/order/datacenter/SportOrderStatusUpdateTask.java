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

import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.UpdateSportOrderStatus;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.DbGenerator;
import com.cqfc.protocol.businesscontroller.PrintMatch;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderStatus;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class SportOrderStatusUpdateTask implements Runnable {

	private static final List<BlockingQueue<UpdateSportOrderStatus>> statusQueueList = SportOrderBuffer
			.getStatusListQueue();

	private static final int MAX_NUM = 500;

	private DataSource dataSource;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int index;

	private BlockingQueue<UpdateSportOrderStatus> statusQueue;

	public SportOrderStatusUpdateTask(ApplicationContext applicationContext, AtomicBoolean running, int index) {
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
			String masterDbBean = BatchUtil.getSportMasterDbBean(index);
			dataSource = applicationContext.getBean(masterDbBean, PooledDataSource.class);
			List<UpdateSportOrderStatus> list = new ArrayList<UpdateSportOrderStatus>();
			while (running.get()) {
				if (statusQueue.size() > 0) {
					statusQueue.drainTo(list, MAX_NUM);
				} else {
					Thread.sleep(10);
				}
				if (list.size() > 0) {
					try {
						Collections.sort(list, new Comparator<UpdateSportOrderStatus>() {
							public int compare(UpdateSportOrderStatus arg0, UpdateSportOrderStatus arg1) {
								return String.valueOf(arg0.getOrderNo()).compareTo(String.valueOf(arg1.getOrderNo()));
							}
						});

						connection = dataSource.getConnection();
						connection.setAutoCommit(false);
						statement = connection.createStatement();
						for (UpdateSportOrderStatus updateOrderStatus : list) {
							String mainTableName = DbGenerator.getSportOrderTableNameMain(updateOrderStatus
									.getTradeId());
							int lotteryType = updateOrderStatus.getLotteryType();
							if (updateOrderStatus.getOrderStatus() == SportOrder.OrderStatus.HASTICKET_WAITLOTTERY
									.getValue()
									&& (lotteryType == OrderStatus.LotteryType.JJZC_GAME.getType() || lotteryType == OrderStatus.LotteryType.JJLC_GAME
											.getType())) {
								String tableDetailName = DbGenerator.getSportOrderTableNameDetail(updateOrderStatus
										.getTradeId());
								String Mainsql = getPrintUpdateMainSql(updateOrderStatus, mainTableName);
								statement.addBatch(Mainsql);
								for (PrintMatch printMatch : updateOrderStatus.getMatchList()) {
									String detailSql = "update " + tableDetailName + " set rq='" + printMatch.getRq()
											+ "',sp='" + printMatch.getSp() + "' where orderNo="
											+ updateOrderStatus.getOrderNo() + " and transferId='"
											+ printMatch.getTransferId() + "'";
									Log.fucaibiz.info("order detail print success,sql=" + detailSql);
									statement.addBatch(detailSql);
								}
							} else {
								String sql = getUpdateOrderSql(updateOrderStatus, mainTableName);
								statement.addBatch(sql);
							}
							Log.fucaibiz.info("竞技彩,批量更新订单状态,orderNo=%d,dbName=%s,tableName=%s",
									updateOrderStatus.getOrderNo(), masterDbBean, mainTableName);
						}
						try {
							int[] batchResult = statement.executeBatch();
							connection.commit();

							long orderNo = 0;
							try {
								int loop = 0;
								for (int i = 0, len = list.size(); i < len; i++) {
									UpdateSportOrderStatus updateOrderStatus = list.get(i);

									orderNo = updateOrderStatus.getOrderNo();
									int orderStatus = updateOrderStatus.getOrderStatus();
									int returnValue = batchResult[loop];

									Log.fucaibiz.info("竞技彩,订单状态批量更新返回值,orderNo=%d,orderStatus=%d,returnValue=%d",
											orderNo, orderStatus, returnValue);

									int operatValue = BatchConstant.BATCH_OPERATE_FAILED;

									int lotteryType = updateOrderStatus.getLotteryType();
									if (updateOrderStatus.getOrderStatus() == SportOrder.OrderStatus.HASTICKET_WAITLOTTERY
											.getValue()
											&& (lotteryType == OrderStatus.LotteryType.JJZC_GAME.getType() || lotteryType == OrderStatus.LotteryType.JJLC_GAME
													.getType())) {
										boolean detailFlag = true;
										int detailSize = updateOrderStatus.getMatchList().size();
										for (int z = 0; z < detailSize; z++) {
											loop++;
											if (batchResult[loop] <= 0) {
												detailFlag = false;
												break;
											}
										}
										if (returnValue > 0 && detailFlag) {
											// 出票成功,更新memcache,未完,待续...
											operatValue = BatchConstant.BATCH_OPERATE_SUCCESS;
											OrderMemcacheUtil.updateSportPrintSuccessMemcache(updateOrderStatus);
										}
									} else if (returnValue > 0) {
										operatValue = BatchConstant.BATCH_OPERATE_SUCCESS;
										OrderMemcacheUtil.updateSportStatusMemcache(orderNo, orderStatus);
									}
									updateStatusMap(orderNo, orderStatus, operatValue);
									Log.fucaibiz.info("竞技彩,订单状态批量更新完成,orderNo=%d,orderStatus=%d,operatValue=%d",
											orderNo, orderStatus, operatValue);
									loop++;
								}
							} catch (Exception e) {
								Log.fucaibiz.error("竞技彩,订单状态批更新之后,处理订单信息发生异常,orderNo=" + orderNo, e);
							}
						} catch (Exception e1) {
							Log.fucaibiz.error("竞技彩,批量更新订单状态异常,进行逐条更新操作,masterDbBean=" + masterDbBean, e1);
							long orderNo = 0;
							int orderStatus = 0;
							try {
								connection.rollback();
								closeConnection(connection, statement);

								connection = dataSource.getConnection();
								statement = connection.createStatement();
								connection.setAutoCommit(true);

								for (UpdateSportOrderStatus updateOrderStatus : list) {
									orderNo = updateOrderStatus.getOrderNo();
									orderStatus = updateOrderStatus.getOrderStatus();
									String tradeId = updateOrderStatus.getTradeId();
									String mainTableName = DbGenerator.getSportOrderTableNameMain(tradeId);
									int lotteryType = updateOrderStatus.getLotteryType();

									int operateIdentifier = BatchConstant.BATCH_OPERATE_FAILED;
									if (orderStatus == SportOrder.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
											&& (lotteryType == OrderStatus.LotteryType.JJZC_GAME.getType() || lotteryType == OrderStatus.LotteryType.JJLC_GAME
													.getType())) {
										String tableDetailName = DbGenerator.getSportOrderTableNameDetail(tradeId);
										String mainSql = getPrintUpdateMainSql(updateOrderStatus, mainTableName);
										int mainValue = statement.executeUpdate(mainSql);
										boolean flag = true;
										for (PrintMatch printMatch : updateOrderStatus.getMatchList()) {
											String detailSql = "update " + tableDetailName + " set rq='"
													+ printMatch.getRq() + "',sp='" + printMatch.getSp()
													+ "' where orderNo=" + orderNo + " and transferId='"
													+ printMatch.getTransferId() + "'";
											int detailValue = statement.executeUpdate(detailSql);
											if (detailValue <= 0) {
												flag = false;
												break;
											}
										}
										if (mainValue > 0 && flag) {
											operateIdentifier = BatchConstant.BATCH_OPERATE_SUCCESS;
											// 出票成功,更新memcache,未完,待续...
											OrderMemcacheUtil.updateSportPrintSuccessMemcache(updateOrderStatus);
										}
									} else {
										String sql = getUpdateOrderSql(updateOrderStatus, mainTableName);
										int operateValue = statement.executeUpdate(sql);
										if (operateValue > 0) {
											operateIdentifier = BatchConstant.BATCH_OPERATE_SUCCESS;
											OrderMemcacheUtil.updateSportStatusMemcache(orderNo, orderStatus);
										}
									}
									Log.fucaibiz.info("竞技彩,逐条更新订单状态完成,orderNo=%d,orderStatus=%d,operateIdentifier=%d",
											orderNo, orderStatus, operateIdentifier);
									updateStatusMap(orderNo, orderStatus, operateIdentifier);
								}
							} catch (Exception e2) {
								updateStatusMap(orderNo, orderStatus, BatchConstant.BATCH_OPERATE_FAILED);
								Log.fucaibiz.error("竞技彩,逐条更新订单发生异常", e2);
							}
						}
					} catch (Exception e) {
						Log.fucaibiz.error("update order status,batch written to the database error", e);
					} finally {
						list.clear();
						closeConnection(connection, statement);
						connection = null;
					}
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
	private String getUpdateOrderSql(UpdateSportOrderStatus updateOrderStatus, String tableName) {
		long orderNo = 0;
		String sql = "";
		try {
			orderNo = updateOrderStatus.getOrderNo();
			int status = updateOrderStatus.getOrderStatus();
			StringBuffer statusIn = new StringBuffer();
			if (status == SportOrder.OrderStatus.HAS_PAYMENT.getValue()
					|| status == SportOrder.OrderStatus.ORDER_CANCEL.getValue()) {
				statusIn.append(SportOrder.OrderStatus.WAIT_PAYMENT.getValue());
			} else if (status == SportOrder.OrderStatus.IN_TICKET.getValue()) {
				statusIn.append(SportOrder.OrderStatus.HAS_PAYMENT.getValue());
			} else if (status == SportOrder.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
					|| status == SportOrder.OrderStatus.DRAWER_FAILURE.getValue()) {
				statusIn.append(SportOrder.OrderStatus.HAS_PAYMENT.getValue() + ","
						+ SportOrder.OrderStatus.IN_TICKET.getValue());
			} else if (status == SportOrder.OrderStatus.REFUNDING.getValue()) {
				statusIn.append(SportOrder.OrderStatus.HAS_PAYMENT.getValue() + ","
						+ SportOrder.OrderStatus.IN_TICKET.getValue() + ","
						+ SportOrder.OrderStatus.DRAWER_FAILURE.getValue());
			} else if (status == SportOrder.OrderStatus.REFUND_SUCCESS.getValue()) {
				statusIn.append(SportOrder.OrderStatus.HAS_PAYMENT.getValue() + ","
						+ SportOrder.OrderStatus.IN_TICKET.getValue() + ","
						+ SportOrder.OrderStatus.DRAWER_FAILURE.getValue() + ","
						+ SportOrder.OrderStatus.REFUNDING.getValue());
			}
			if (status == SportOrder.OrderStatus.ORDER_CANCEL.getValue()) {
				sql = "update " + tableName + " set orderStatus=" + status + ",errCodeStatus='"
						+ ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR + "',errCodeRemark='扣款异常或者帐号钱数不足' where orderNo="
						+ orderNo + " and orderStatus in (" + statusIn.toString() + ");";
			} else {
				sql = "update " + tableName + " set orderStatus=" + status + " where orderNo=" + orderNo
						+ " and orderStatus in (" + statusIn.toString() + ");";
			}
			Log.fucaibiz.info("竞技彩,批更新订单状态拼接SQL,orderNo=%d,sql=%s", orderNo, sql);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,批更新订单状态拼接SQL发生异常,orderNo=" + orderNo, e);
		}
		return sql;
	}

	private String getPrintUpdateMainSql(UpdateSportOrderStatus updateOrderStatus, String tableMainName) {
		long orderNo = 0;
		String sql = "";
		try {
			orderNo = updateOrderStatus.getOrderNo();
			int status = updateOrderStatus.getOrderStatus();
			String ticketNo = updateOrderStatus.getTicketNo();
			String printTime = updateOrderStatus.getPrintTime();
			// String printTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",
			// new Date());

			String conditions = SportOrder.OrderStatus.HAS_PAYMENT.getValue() + ","
					+ SportOrder.OrderStatus.IN_TICKET.getValue();

			sql = "update " + tableMainName + " set orderStatus=" + status + ",ticketNo='" + ticketNo + "',printTime='"
					+ printTime + "' where orderNo=" + orderNo + " and orderStatus in (" + conditions + ");";
			Log.fucaibiz.info("竞技彩,出票成功,批更新订单状态拼接SQL,orderNo=%d,sql=%s", orderNo, sql);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,出票成功,批更新订单状态拼接SQL发生异常,orderNo=" + orderNo, e);
		}
		return sql;
	}

	private void updateStatusMap(long orderNo, int orderStatus, int operateValue) {
		String mapKey = BatchUtil.getOrderStatusMapKey(orderNo, orderStatus);
		Log.fucaibiz.debug("竞技彩,update status map,orderNo=%d,orderStatus=%d,value=%d", orderNo, orderStatus,
				operateValue);
		try {
			if (orderStatus == SportOrder.OrderStatus.HAS_PAYMENT.getValue()
					|| orderStatus == SportOrder.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
					|| orderStatus == SportOrder.OrderStatus.DRAWER_FAILURE.getValue()
					|| orderStatus == SportOrder.OrderStatus.REFUND_SUCCESS.getValue()
					|| orderStatus == SportOrder.OrderStatus.ORDER_CANCEL.getValue()) {
				UpdateSportOrderStatus statusMsg = BatchConstant.sportOrderStatusMap.get(mapKey);
				statusMsg.setOperateIdentifier(operateValue);
				statusMsg.setIsBackReq(true);
				synchronized (statusMsg) {
					statusMsg.notify();
				}
			} else {
				Log.fucaibiz.debug("竞技彩,orderStatusMap delete,orderNo=%d,orderStatus=%d,value=%d", orderNo,
						orderStatus, operateValue);
				BatchConstant.sportOrderStatusMap.remove(mapKey);
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,处理更新订单状态MAP发生异常,orderNo=" + orderNo, e);
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
