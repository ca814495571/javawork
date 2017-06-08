package com.cqfc.partner.order.task;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.partner.order.dataCache.PartnerOrderBuffer;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.OrderDetail;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.ParameterUtils;
import com.jami.util.CountLog;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;

public class AddOrderExcSqlTask implements Runnable {

	private static final List<BlockingQueue<Order>> listQueue = PartnerOrderBuffer
			.getListQueue();

	
	protected static final String setNo = ParameterUtils.getParameterValue("setNo");
	
	
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
		String tempTableName = DataSourceUtil.TempOrderTableName;
		String jcTempTableName = DataSourceUtil.TempJcDetailTableName;
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
						CountLog.addPartnerOrder.error(Thread.currentThread()
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
					for (Order order : list) {

						if (!running.get()) {
							return;
						}
//						CountLog.addPartnerOrder.debug(order);
//						Log.run.info(order);
						if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.NUMBER_GAME.type){
							
							statement.addBatch(getNumInsertSql(order,
									DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId())));

							order.setExt(dbName
									+ "#"
									+ DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId()));
							tempStatement.addBatch(getNumTempInsertSql(order, tempTableName));
						}else
						//竞技彩的订单详情
						if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.SPORTS_GAME.type){
							
							
							statement.addBatch(getSportInsertSql(order,
									DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId())));

							tempStatement.addBatch(getSportTempInsertSql(order, tempTableName));
							if(order.getOrderDetails()!=null&&order.getOrderDetails().size()>0){
								for (OrderDetail orderDetail :order.getOrderDetails()) {
							//		statement.addBatch(getOrderDetailSql(orderDetail, DataSourceUtil.getTableName(DataSourceUtil.ORDERDETAIL_TABLENAME,order.getTradeId())));
									tempStatement.addBatch(getOrderDetailSql(orderDetail,jcTempTableName));
								}
							}
							
						}						
					}

					try {

						statement.executeBatch();
						conn.commit();
						conn.setAutoCommit(true);
					} catch (Exception e) {
						CountLog.addPartnerOrder.warn(masterDbName
								+ "同步订单时，批量插入全局订单数据库出现异常", e);
						CountLog.run.warn(masterDbName
								+ "同步订单时，批量插入全局订单数据库出现异常", e);
						Order orderTemp = null;
						try {

							conn.rollback();
							DataSourceUtil.closeConn(conn, statement);
							conn = dataSource.getConnection();
							statement = conn.createStatement();
							conn.setAutoCommit(true);
							for (Order order : list) {
								
								try {
									if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.NUMBER_GAME.type){
											
											statement.execute(getNumInsertSql(order,
													DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId())));
									}else
									//竞技彩的订单详情
									if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.SPORTS_GAME.type){
										
										statement.execute(getSportInsertSql(order,
												DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId())));
									}				
								} catch (Exception e2) {
									CountLog.addPartnerOrder.error(masterDbName
											+ "逐个插入订单全局数据库出现异常,需及时处理,order:"+order, e2);
									CountLog.run.error(masterDbName
											+ "逐个插入订单全局数据库出现异常,需及时处理,order:"+order, e2);
									Log.error(masterDbName
											+ "逐个插入订单全局数据库出现异常,需及时处理,order:"+order, e2);
								}
							}
							CountLog.run.info("逐个插入全局数据库成功!");
						} catch (Exception e2) {
							CountLog.addPartnerOrder.error(masterDbName
									+ "同步订单时，逐个插入订单全局数据库出现异常,需及时处理", e2);
							CountLog.run.error(masterDbName
									+ "同步订单时，逐个插入订单全局数据库出现异常,需及时处理", e2);
							Log.error(masterDbName
									+ "同步订单时，逐个插入订单全局数据库出现异常,需及时处理", e2);
						//	running.set(false);
						//	return;
						}
					}

					try {
						
						tempStatement.executeBatch();
						tempConn.commit();
						tempConn.setAutoCommit(true);
					} catch (Exception e) {

						CountLog.addPartnerOrder.warn(tempMasterDbName
								+ "同步订单时，批量插入临时数据库出现异常", e);
						CountLog.run.warn(tempMasterDbName
								+ "同步订单时，批量插入临时数据库出现异常", e);
						try {
							tempConn.rollback();
							DataSourceUtil.closeConn(tempConn, tempStatement);
							tempConn = tempDataSource.getConnection();
							tempStatement = tempConn.createStatement();
							tempConn.setAutoCommit(true);
							tempTableName = DataSourceUtil.TempOrderTableName;
							for (Order order : list) {
								
								try{
									
									if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.NUMBER_GAME.type){
										
										order.setExt(dbName
												+ "#"
												+ DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId()));
										tempStatement.execute(getNumTempInsertSql(order, tempTableName));
										
									}else
									//竞技彩的订单详情
									if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.SPORTS_GAME.type){
										
										tempStatement.execute(getSportTempInsertSql(order, tempTableName));
										if(order.getOrderDetails()!=null&&order.getOrderDetails().size()>0){
											
											for (OrderDetail orderDetail :order.getOrderDetails()) {
												tempStatement.execute(getOrderDetailSql(orderDetail,jcTempTableName));
											}
										}
										
									}				
								} catch (Exception e2) {
									CountLog.addPartnerOrder.error(masterDbName
											+ "订单临时库逐个插入出现异常,需及时处理,order:"+order, e2);
									CountLog.run.error(masterDbName
											+ "订单临时库逐个插入出现异常,需及时处理,order:"+order, e2);
									Log.error(masterDbName
											+ "订单临时库逐个插入出现异常,需及时处理,order:"+order, e2);
								}
							}
							
							CountLog.run.info("逐个插入临时订单数据库成功!");
						} catch (Exception e2) {
							CountLog.addPartnerOrder.fatal(tempMasterDbName
									+ "同步订单时，逐个插入临时数据库出现异常,需及时处理", e2);
							CountLog.run.fatal(tempMasterDbName
									+ "同步订单时，逐个插入临时数据库出现异常,需及时处理", e2);
							Log.error(tempMasterDbName
									+ "同步订单时，逐个插入临时数据库出现异常,需及时处理", e2);
//							running.set(false);
//							return;
						}

					}
					
					CountLog.addPartnerOrder.debug(masterDbName
							+ "批处理了" + list.size() + "个订单");

				} catch (Exception e) {

					CountLog.addPartnerOrder.error("系统异常,需及时处理", e);
					CountLog.run.error("系统异常,需及时处理", e);
//					running.set(false);
//					return;

				} finally {
					try {

						if (running.get()) {

							lastOrder = list.get(list.size() - 1);
							b = tempStatement.executeUpdate("update "
									+ DataSourceUtil.RecoveryIndexTable
									+ " set orderNo='" + lastOrder.getOrderNo()
									+ "' where dbName='" + dbName+"' and setNo='"+setNo+"'");
							if (b != 1) {

								tempStatement.execute("insert into "
										+ DataSourceUtil.RecoveryIndexTable
										+ " (dbName,orderNo,createTime,setNo) "
										+ " values ('" + dbName + "','"
										+ lastOrder.getOrderNo() + "', now(),'"+setNo+"')");
							}

						}
					} catch (Exception e2) {
						CountLog.addPartnerOrder.error("修改索引表异常", e2);
						Log.error("修改索引表异常", e2);
					}
					list.clear();
					DataSourceUtil.closeConn(conn, statement);
					DataSourceUtil.closeConn(tempConn, tempStatement);
				}

			}
		} catch (Exception e) {
			CountLog.addPartnerOrder.error("系统异常,需及时处理", e);
			CountLog.run.error("系统异常,需及时处理", e);
		}
	}

	public String getNumInsertSql(Order order, String tableName) {

		return " replace into "
				+ tableName
				+ " (lotteryId,partnerId,userId,issueNo,orderNo,"
				+ "orderStatus,totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,"
				+ "multiple,playType,paySerialNumber,realName,cardNo,mobile,createTime,"
				+ "orderType,tradeId,ext,planId,province,lotteryMark,ticketTime) values ('" + order.getLotteryId() + "','"
				+ order.getPartnerId() + "'," + order.getUserId() + ",'"
				+ order.getIssueNo() + "','" + order.getOrderNo() + "',"
				+ order.getOrderStatus() + "," + order.getTotalAmount() + ","
				+ order.getWinPrizeMoney() + "," + order.getPrizeAfterTax()
				+ ",'" + order.getOrderContent() + "'," + order.getStakeNum()
				+ "," + order.getMultiple() + ",'" + order.getPlayType()
				+ "','" + order.getPaySerialNumber() + "','"
				+ order.getRealName() + "','" + order.getCardNo() + "','"
				+ order.getMobile() + "','" + order.getCreateTime() + "',"
				+ order.getOrderType() + ",'"
				+ order.getTradeId() +"','"+order.getExt()+  "','"
				+order.getPlanId()+"','"
				+order.getProvince()+"','"
				+order.getLotteryMark()+"','"
				+order.getTicketTime()
				+"')";
	}
	
	
	public String getNumTempInsertSql(Order order, String tableName) {

		return " replace into "
				+ tableName
				+ " (lotteryId,partnerId,userId,issueNo,orderNo,"
				+ "orderStatus,totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,"
				+ "multiple,playType,paySerialNumber,realName,cardNo,mobile,createTime,"
				+ "orderType,tradeId,ext,planId,province,lotteryMark,ticketTime,lotteryType) values ('" + order.getLotteryId() + "','"
				+ order.getPartnerId() + "'," + order.getUserId() + ",'"
				+ order.getIssueNo() + "','" + order.getOrderNo() + "',"
				+ order.getOrderStatus() + "," + order.getTotalAmount() + ","
				+ order.getWinPrizeMoney() + "," + order.getPrizeAfterTax()
				+ ",'" + order.getOrderContent() + "'," + order.getStakeNum()
				+ "," + order.getMultiple() + ",'" + order.getPlayType()
				+ "','" + order.getPaySerialNumber() + "','"
				+ order.getRealName() + "','" + order.getCardNo() + "','"
				+ order.getMobile() + "','" + order.getCreateTime() + "',"
				+ order.getOrderType() + ",'"
				+ order.getTradeId() +"','"+order.getExt()+  "','"
				+order.getPlanId()+"','"
				+order.getProvince()+"','"
				+order.getLotteryMark()+"','"
				+order.getTicketTime()+"',"+OrderStatus.LotteryType.NUMBER_GAME.getType()
				+")";
	}
	
	public String getSportInsertSql(Order order, String tableName) {

		return " replace into "
				+ tableName
				+ " (lotteryId,partnerId,userId,issueNo,orderNo,"
				+ "orderStatus,totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,"
				+ "multiple,playType,paySerialNumber,realName,cardNo,mobile,createTime,"
				+ "orderType,tradeId,ext,planId,province,lotteryMark,ticketTime,endTime) values ('" + order.getLotteryId() + "','"
				+ order.getPartnerId() + "'," + order.getUserId() + ",'"
				+ order.getIssueNo() + "','" + order.getOrderNo() + "',"
				+ order.getOrderStatus() + "," + order.getTotalAmount() + ","
				+ order.getWinPrizeMoney() + "," + order.getPrizeAfterTax()
				+ ",'" + order.getOrderContent() + "'," + order.getStakeNum()
				+ "," + order.getMultiple() + ",'" + order.getPlayType()
				+ "','" + order.getPaySerialNumber() + "','"
				+ order.getRealName() + "','" + order.getCardNo() + "','"
				+ order.getMobile() + "','" + order.getCreateTime() + "',"
				+ order.getOrderType() + ",'"
				+ order.getTradeId() +"','"+order.getExt()+  "','"
				+order.getPlanId()+"','"
				+order.getProvince()+"','"
				+order.getLotteryMark()+"','"
				+order.getTicketTime()+"','"
				+order.getEndTime()
				+"')";
	}
	
	public String getSportTempInsertSql(Order order, String tableName) {

		return " replace into "
				+ tableName
				+ " (lotteryId,partnerId,userId,issueNo,orderNo,"
				+ "orderStatus,totalAmount,winPrizeMoney,prizeAfterTax,orderContent,stakeNum,"
				+ "multiple,playType,paySerialNumber,realName,cardNo,mobile,createTime,"
				+ "orderType,tradeId,ext,planId,province,lotteryMark,ticketTime,endTime,lotteryType) values ('" + order.getLotteryId() + "','"
				+ order.getPartnerId() + "'," + order.getUserId() + ",'"
				+ order.getIssueNo() + "','" + order.getOrderNo() + "',"
				+ order.getOrderStatus() + "," + order.getTotalAmount() + ","
				+ order.getWinPrizeMoney() + "," + order.getPrizeAfterTax()
				+ ",'" + order.getOrderContent() + "'," + order.getStakeNum()
				+ "," + order.getMultiple() + ",'" + order.getPlayType()
				+ "','" + order.getPaySerialNumber() + "','"
				+ order.getRealName() + "','" + order.getCardNo() + "','"
				+ order.getMobile() + "','" + order.getCreateTime() + "',"
				+ order.getOrderType() + ",'"
				+ order.getTradeId() +"','"+order.getExt()+  "','"
				+order.getPlanId()+"','"
				+order.getProvince()+"','"
				+order.getLotteryMark()+"','"
				+order.getTicketTime()+"','"
				+order.getEndTime()+"',"
				+OrderStatus.LotteryType.SPORTS_GAME.getType()
				+")";
	}
	
	public String getOrderDetailSql(OrderDetail orderDetail, String tableName) {

		return " replace into "
				+ tableName
				+ " (orderNo,transferId,matchNo,rq,content,odds,matchStatus,createTime,lotteryId,issueNo,matchType) values ('" + orderDetail.getOrderNo() + "','"
				+orderDetail.getTransferId()+"','"
				+orderDetail.getMatchNo()+"','"
				+orderDetail.getRq()+"','"
				+orderDetail.getContent()+"','"
				+orderDetail.getOdds()+"',"
				+orderDetail.getMatchStatus()+",now(),'"
				+orderDetail.getLotteryId()+"','"
				+orderDetail.getIssueNo()+"',"
				+orderDetail.getMatchType()
				+")";
	}
}
