package com.cqfc.order.datacenter;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.order.model.CreateOrderMsg;
import com.cqfc.order.model.Order;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.DbGenerator;
import com.cqfc.util.DateUtil;
import com.jami.util.Log;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * @author liwh
 */
public class CreateOrderSqlExecuteTask implements Runnable {

	private static final List<BlockingQueue<Order>> createQueueList = OrderBuffer.getCreateListQueue();

	private static final int MAX_NUM = 500;

	private DataSource dataSource;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int index;

	private BlockingQueue<Order> orderQueue;

	public CreateOrderSqlExecuteTask(ApplicationContext applicationContext, AtomicBoolean running, int index) {
		this.applicationContext = applicationContext;
		this.running = running;
		this.index = index;
		this.orderQueue = createQueueList.get(index);
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
			List<Order> list = new ArrayList<Order>();
			while (running.get()) {
				if (orderQueue.size() > 0) {
					orderQueue.drainTo(list, MAX_NUM);
				} else {
					Thread.sleep(10);
				}
				if (list.size() > 0) {
					// int listSize = list.size();
					// long beginTime = System.currentTimeMillis();
					try {
						Collections.sort(list, new Comparator<Order>() {
							public int compare(Order arg0, Order arg1) {
								return String.valueOf(arg0.getOrderNo()).compareTo(String.valueOf(arg1.getOrderNo()));
							}
						});

						connection = dataSource.getConnection();
						connection.setAutoCommit(false);
						statement = connection.createStatement();
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());

						Map<String, List<Order>> countTable = new HashMap<String, List<Order>>();
						for (Order order : list) {
							String table = DbGenerator.getOrderTableName(order.getTradeId());
							if (countTable.containsKey(table)) {
								countTable.get(table).add(order);
							} else {
								List<Order> countList = new ArrayList<Order>();
								countList.add(order);
								countTable.put(table, countList);
							}
						}

						List<List<Order>> totalList = new ArrayList<List<Order>>();
						for (String tableStr : countTable.keySet()) {
							totalList.add(countTable.get(tableStr));
						}

						int totalListSize = totalList.size();
						for (int i = 0; i < totalListSize; i++) {
							List<Order> tempList = totalList.get(i);
							for (Order order : tempList) {
								order.setCreateTime(currentTime);
							}
							String tableName = DbGenerator.getOrderTableName(tempList.get(0).getTradeId());
							String sql = getBatchCreateOrderSql(tempList, tableName);
							statement.addBatch(sql);
						}

						try {
							int[] batchResult = statement.executeBatch();
							connection.commit();

							for (int i = 0; i < totalListSize; i++) {
								List<Order> tempList = totalList.get(i);
								int returnValue = batchResult[i];

								int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
								if (returnValue == tempList.size()) {
									operateValue = BatchConstant.BATCH_OPERATE_SUCCESS;
								}
								for (Order order : tempList) {
									long orderNo = order.getOrderNo();
									Log.fucaibiz.info("批创建处理结果,orderNo=%d,operateValue=%d", orderNo, operateValue);
									OrderMemcacheUtil.createOrderMemcache(order);
									updateCreateMap(orderNo, operateValue);
								}
							}
						} catch (Exception e1) {
							Log.fucaibiz.error("批量创建订单异常,进行逐条创建操作,masterDbBean=" + masterDbBean, e1);
							long orderNo = 0;
							try {
								connection.rollback();
								closeConnection(connection, statement);

								connection = dataSource.getConnection();
								statement = connection.createStatement();
								connection.setAutoCommit(true);

								for (Order order : list) {
									orderNo = order.getOrderNo();
									String tableName = DbGenerator.getOrderTableName(order.getTradeId());
									int operateValue = statement.executeUpdate(getCreateOrderSql(order, tableName));
									Log.fucaibiz.info("逐条写入完成,orderNo=%d,operateValue=%d", orderNo, operateValue);
									if (operateValue > 0) {
										OrderMemcacheUtil.createOrderMemcache(order);
									}

									int operateIdentifier = operateValue > 0 ? BatchConstant.BATCH_OPERATE_SUCCESS
											: BatchConstant.BATCH_OPERATE_FAILED;
									updateCreateMap(orderNo, operateIdentifier);
								}
							} catch (MySQLIntegrityConstraintViolationException e2) {
								updateCreateMap(orderNo, BatchConstant.BATCH_OPERATE_CREATE_REPEAT);
								Log.error("逐条创建投注订单发生唯一键冲突,orderNo=" + orderNo, e2);
								Log.fucaibiz.error("逐条创建投注订单发生唯一键冲突,orderNo=" + orderNo, e2);
							} catch (Exception e2) {
								updateCreateMap(orderNo, BatchConstant.BATCH_OPERATE_FAILED);
								Log.error("逐条创建订单发生异常,orderNo=" + orderNo, e2);
								Log.fucaibiz.error("逐条创建订单发生异常,orderNo=" + orderNo, e2);
								// stop();
								// return;
							}
						}
					} catch (Exception e) {
						Log.fucaibiz.error("create order,batch written to the database error", e);
					} finally {
						list.clear();
						closeConnection(connection, statement);
						connection = null;
					}
					// long endTime = System.currentTimeMillis();
					// Log.fucaibiz.info("batch create order list,size=%d,totalTime=%d",
					// listSize, (endTime - beginTime));
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("batch create order error", e);
		}
	}

	private void updateCreateMap(long orderNo, int operateValue) {
		try {
			if (BatchConstant.createOrderMap.containsKey(orderNo)) {
				BatchConstant.createOrderMap.get(orderNo).setOperateIdentifier(operateValue);
				BatchConstant.createOrderMap.get(orderNo).setIsBackReq(true);
				CreateOrderMsg orderMsg = BatchConstant.createOrderMap.get(orderNo);
				synchronized (orderMsg) {
					orderMsg.notify();
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("批创建订单之后处理createMap发生异常,orderNo=" + orderNo, e);
		}
	}

	private String getCreateOrderSql(Order order, String tableName) {
		StringBuffer sql = new StringBuffer();
		try {
			sql.append("insert into ");
			sql.append(tableName);
			sql.append("(orderNo,lotteryId,partnerId,userId,issueNo,orderType,orderStatus,totalAmount,");
			sql.append("orderContent,multiple,playType,paySerialNumber,tradeId,realName,cardNo,mobile,");
			sql.append("ticketNum,createTime,isSyncSuccess) values");
			sql.append("(" + order.getOrderNo() + ",'" + order.getLotteryId() + "','" + order.getPartnerId() + "',"
					+ order.getUserId() + ",'" + order.getIssueNo() + "'," + order.getOrderType() + ","
					+ order.getOrderStatus() + "," + order.getTotalAmount() + ",'" + order.getOrderContent() + "',"
					+ order.getMultiple() + ",'" + order.getPlayType() + "','" + order.getPaySerialNumber() + "','"
					+ order.getTradeId() + "','" + order.getRealName() + "','" + order.getCardNo() + "','"
					+ order.getMobile() + "'," + order.getTicketNum() + ",'" + order.getCreateTime() + "',1);");
			Log.fucaibiz.info("创建订单拼装SQL,orderNo=%d,sql=%s", order.getOrderNo(), sql.toString());
		} catch (Exception e) {
			Log.fucaibiz.error("创建订单拼接SQL发生异常,orderNo=" + order.getOrderNo(), e);
		}
		return sql.toString();
	}

	private String getBatchCreateOrderSql(List<Order> claList, String tableName) {
		String sql = "";
		try {
			String temp = "";
			for (Order order : claList) {
				temp += "(" + order.getOrderNo() + ",'" + order.getLotteryId() + "','" + order.getPartnerId() + "',"
						+ order.getUserId() + ",'" + order.getIssueNo() + "'," + order.getOrderType() + ","
						+ order.getOrderStatus() + "," + order.getTotalAmount() + ",'" + order.getOrderContent() + "',"
						+ order.getMultiple() + ",'" + order.getPlayType() + "','" + order.getPaySerialNumber() + "','"
						+ order.getTradeId() + "','" + order.getRealName() + "','" + order.getCardNo() + "','"
						+ order.getMobile() + "'," + order.getTicketNum() + ",'" + order.getCreateTime() + "',1)" + ",";
			}
			sql = "insert into " + tableName
					+ "(orderNo,lotteryId,partnerId,userId,issueNo,orderType,orderStatus,totalAmount,"
					+ "orderContent,multiple,playType,paySerialNumber,tradeId,realName,cardNo,mobile,"
					+ "ticketNum,createTime,isSyncSuccess) values" + temp.substring(0, temp.length() - 1);
			Log.fucaibiz.info("批创建订单拼装SQL,sql=%s", sql);
		} catch (Exception e) {
			Log.fucaibiz.error("批创建订单拼接SQL发生异常", e);
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
			Log.run.debug("createQueueList connection close error", e);
		}
	}
}
