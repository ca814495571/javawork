package com.cqfc.order.datacenter;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.order.model.CreateOrderMsg;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.DbGenerator;
import com.cqfc.util.DateUtil;
import com.jami.util.Log;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * @author liwh
 */
public class SportOrderCreateTask implements Runnable {

	private static final List<BlockingQueue<SportOrder>> createSportQueueList = SportOrderBuffer.getCreateListQueue();

	private static final int MAX_NUM = 300;

	private DataSource dataSource;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int index;

	private BlockingQueue<SportOrder> orderQueue;

	public SportOrderCreateTask(ApplicationContext applicationContext, AtomicBoolean running, int index) {
		this.applicationContext = applicationContext;
		this.running = running;
		this.index = index;
		this.orderQueue = createSportQueueList.get(index);
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
			List<SportOrder> list = new ArrayList<SportOrder>();
			while (running.get()) {
				if (orderQueue.size() > 0) {
					orderQueue.drainTo(list, MAX_NUM);
				} else {
					Thread.sleep(10);
				}
				if (list.size() > 0) {
					try {
						Collections.sort(list, new Comparator<SportOrder>() {
							public int compare(SportOrder arg0, SportOrder arg1) {
								return String.valueOf(arg0.getOrderNo()).compareTo(String.valueOf(arg1.getOrderNo()));
							}
						});

						connection = dataSource.getConnection();
						connection.setAutoCommit(false);
						statement = connection.createStatement();
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						for (SportOrder order : list) {
							String ticketId = order.getTradeId();
							String tableNameMain = DbGenerator.getSportOrderTableNameMain(ticketId);
							String tableNameDetail = DbGenerator.getSportOrderTableNameDetail(ticketId);
							order.setCreateTime(currentTime);
							statement.addBatch(getCreateOrderMainSql(order, tableNameMain));
							statement.addBatch(getCreateOrderDetailSql(order, tableNameDetail));
							Log.fucaibiz.info("竞技彩,批量创建订单,orderNo=%d,dbName=%s,mainTable=%s,detailTable=%s",
									order.getOrderNo(), masterDbBean, tableNameMain, tableNameDetail);
						}
						try {
							int[] batchResult = statement.executeBatch();
							connection.commit();

							for (int i = 0, len = list.size(); i < len; i++) {
								SportOrder order = list.get(i);
								long orderNo = order.getOrderNo();
								int mainValue = batchResult[i * 2];
								int detailValue = batchResult[i * 2 + 1];
								int detailSize = order.getSportOrderDetailList().size();
								Log.fucaibiz.info("竞技彩,批量创建订单完成,orderNo=%d,mainValue=%d,detailValue=%d", orderNo,
										mainValue, detailValue);

								int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
								if (mainValue == 1 && detailValue == detailSize) {
									operateValue = BatchConstant.BATCH_OPERATE_SUCCESS;
									OrderMemcacheUtil.createSportOrderMemcache(order);
								}
								updateCreateMap(orderNo, operateValue);
							}
						} catch (Exception e1) {
							Log.fucaibiz.error("竞技彩,批量创建订单异常,进行逐条创建操作,masterDbBean=" + masterDbBean, e1);
							long orderNo = 0;
							try {
								connection.rollback();
								closeConnection(connection, statement);

								connection = dataSource.getConnection();
								statement = connection.createStatement();
								connection.setAutoCommit(true);

								for (SportOrder order : list) {
									orderNo = order.getOrderNo();
									String ticketId = order.getTradeId();
									String tableNameMain = DbGenerator.getSportOrderTableNameMain(ticketId);
									String tableNameDetail = DbGenerator.getSportOrderTableNameDetail(ticketId);
									String mainSql = getCreateOrderMainSql(order, tableNameMain);
									String detailSql = getCreateOrderDetailSql(order, tableNameDetail);
									int mainValue = statement.executeUpdate(mainSql);
									int detailValue = statement.executeUpdate(detailSql);
									Log.fucaibiz.info("竞技彩,逐条写入完成,orderNo=%d,mainValue=%d,detailValue=%d", orderNo,
											mainValue, detailValue);
									int operateIdentifier = BatchConstant.BATCH_OPERATE_FAILED;
									if (mainValue == 1 && detailValue == order.getSportOrderDetailList().size()) {
										operateIdentifier = BatchConstant.BATCH_OPERATE_SUCCESS;
										OrderMemcacheUtil.createSportOrderMemcache(order);
									}
									updateCreateMap(orderNo, operateIdentifier);
								}
							} catch (MySQLIntegrityConstraintViolationException e2) {
								updateCreateMap(orderNo, BatchConstant.BATCH_OPERATE_CREATE_REPEAT);
								Log.error("竞技彩,逐条创建竞技彩订单发生唯一键冲突,orderNo=" + orderNo, e2);
								Log.fucaibiz.error("竞技彩,逐条创建竞技彩订单发生唯一键冲突,orderNo=" + orderNo, e2);
							} catch (Exception e2) {
								updateCreateMap(orderNo, BatchConstant.BATCH_OPERATE_FAILED);
								Log.error("竞技彩,逐条创建竞技彩订单发生异常,orderNo=" + orderNo, e2);
								Log.fucaibiz.error("竞技彩,逐条创建竞技彩订单发生异常,orderNo=" + orderNo, e2);
							}
						}
					} catch (Exception e) {
						Log.fucaibiz.error("create order,batch written to the database error", e);
					} finally {
						list.clear();
						closeConnection(connection, statement);
						connection = null;
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("batch create order error", e);
		}
	}

	private void updateCreateMap(long orderNo, int operateValue) {
		try {
			if (BatchConstant.createSportOrderMap.containsKey(orderNo)) {
				BatchConstant.createSportOrderMap.get(orderNo).setOperateIdentifier(operateValue);
				BatchConstant.createSportOrderMap.get(orderNo).setIsBackReq(true);
				CreateOrderMsg orderMsg = BatchConstant.createSportOrderMap.get(orderNo);
				synchronized (orderMsg) {
					orderMsg.notify();
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,批创建订单之后处理createSportOrderMap发生异常,orderNo=" + orderNo, e);
		}
	}

	private String getCreateOrderMainSql(SportOrder order, String tableNameMain) {
		String sql = "";
		try {
			sql = "insert into " + tableNameMain
					+ "(orderNo,lotteryId,partnerId,userId,issueNo,orderType,orderStatus,totalAmount,"
					+ "orderContent,multiple,playType,paySerialNumber,tradeId,realName,cardNo,mobile,"
					+ "ticketNum,planId,printProvince,closeTime,createTime) values(" + order.getOrderNo() + ",'"
					+ order.getLotteryId() + "','" + order.getPartnerId() + "'," + order.getUserId() + ",'"
					+ order.getIssueNo() + "'," + order.getOrderType() + "," + order.getOrderStatus() + ","
					+ order.getTotalAmount() + ",'" + order.getOrderContent() + "'," + order.getMultiple() + ",'"
					+ order.getPlayType() + "','" + order.getPaySerialNumber() + "','" + order.getTradeId() + "','"
					+ order.getRealName() + "','" + order.getCardNo() + "','" + order.getMobile() + "',"
					+ order.getTicketNum() + ",'" + order.getPlanId() + "','" + order.getPrintProvince() + "','"
					+ order.getCloseTime() + "','" + order.getCreateTime() + "');";
			Log.fucaibiz.info("竞技彩,创建订单主表SQL,orderNo=%d,sql=%s", order.getOrderNo(), sql);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,创建订单主表SQL发生异常,orderNo=" + order.getOrderNo(), e);
		}
		return sql;
	}

	private String getCreateOrderDetailSql(SportOrder order, String tableNameDetail) {
		String sql = "";
		try {
			sql += "insert into " + tableNameDetail
					+ "(orderNo,matchId,transferId,matchNo,orderContent,createTime) values";
			String temp = "";
			for (SportOrderDetail detail : order.getSportOrderDetailList()) {
				temp += "(" + detail.getOrderNo() + ",'" + detail.getMatchId() + "','" + detail.getTransferId() + "','"
						+ detail.getMatchNo() + "','" + detail.getOrderContent() + "','" + order.getCreateTime()
						+ "'),";
			}
			sql += temp.substring(0, temp.length() - 1) + ";";
			Log.fucaibiz.info("竞技彩,创建订单细表SQL,orderNo=%d,sql=%s", order.getOrderNo(), sql);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,创建订单细表SQL发生异常,orderNo=" + order.getOrderNo(), e);
		}
		return sql;
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
			Log.run.debug("竞技彩,createQueueList connection close error", e);
		}
	}
}
