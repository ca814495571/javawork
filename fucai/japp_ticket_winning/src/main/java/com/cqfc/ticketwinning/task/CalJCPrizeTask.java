package com.cqfc.ticketwinning.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.factory.JCTicketWinningServiceFactory;
import com.cqfc.ticketwinning.model.JCTempOrder;
import com.cqfc.ticketwinning.model.OrderNoCount;
import com.cqfc.ticketwinning.model.WinningOrder.OrderStatus;
import com.cqfc.ticketwinning.service.AbstrartJCTicketWinningService;
import com.cqfc.ticketwinning.service.IJCTempOrderService;
import com.cqfc.ticketwinning.service.IWinningOrderService;
import com.cqfc.ticketwinning.service.impl.JCTempOrderServiceImpl;
import com.cqfc.ticketwinning.service.impl.WinningOrderServiceImpl;
import com.cqfc.ticketwinning.util.MXNUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryType;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

public class CalJCPrizeTask implements Runnable {
	
	private CountDownLatch latch;
	private int startNum;
	private int endNum;
	private String transferId;
	private String winningContent;
	private AtomicBoolean isException;
	private ApplicationContext applicationContext;

	public CalJCPrizeTask(CountDownLatch latch, int startNum, int endNum,
			String transferId, String winningContent, AtomicBoolean isException) {
		this.latch = latch;
		this.startNum = startNum;
		this.endNum = endNum;
		this.transferId = transferId;
		this.winningContent = winningContent;
		this.isException = isException;
	}
	
	@Override
	public void run() {
		ScanLog.scan.debug("jcPrizeTask(transferId=%s)", transferId);
		applicationContext = ApplicationContextProvider.getApplicationContext();
		IJCTempOrderService orderService = applicationContext.getBean(
				"JCTempOrderServiceImpl", JCTempOrderServiceImpl.class);
		IWinningOrderService winningOrderService = applicationContext.getBean(
				"winningOrderServiceImpl", WinningOrderServiceImpl.class);
		AbstrartJCTicketWinningService ticketService = null;
		int pageSize = TicketWinningConstantsUtil.ORDER_TABLE_PAGE_SIZE;
		int count = 0;
		List<JCTempOrder> list = null;
		List<JCTempOrder> jcTempOrders = null;
		List<Integer> winOddsList = null;
		List<WinningOrderInfo> winningOrderInfos = new ArrayList<WinningOrderInfo>();
		WinningOrderInfo winningOrderInfo = null;
		int winOdds = 0;
		int totalOdds = 0;
		int m = 0;
		int n = 0;
		int[] playType = null;
		String tempWinningContent = "";
		
		try {
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.ORDER_TEMP_DBNAME);
			Map<String, String> winningContentMap = TicketWinningUtil.getWinningContentMap(winningContent);
			while (true) {
				count = endNum - startNum;
				if (count <= 0) {
					break;
				}
				if (count > pageSize) {
					count = pageSize;
				}
				list = orderService.getJCTempOrderList(transferId, DBSourceUtil.JC_ORDER_TEMP_TABLENAME, startNum, count);
				ScanLog.scan.info("jcPrizeTask(transferId=%s, cursor=%d, listLen=%d, count=%d)", transferId, startNum, list.size(), count);
				if (list == null || list.size() == 0) {
					break;
				}
				if (isException.get()) {
					return;
				}
				for (JCTempOrder order : list) {	
					// 计算该场比赛中奖情况
					String lotteryId = order.getLotteryId();
					ticketService = JCTicketWinningServiceFactory.getJCTicketWinningServiceInstance(order.getLotteryId());
					if (!(lotteryId.equals(LotteryType.JCZQHHGG.getText()) || lotteryId.equals(LotteryType.JCLQHHGG.getText()))) {
						tempWinningContent = winningContentMap.get(lotteryId);
					}
					else{
						tempWinningContent = winningContent;
					}
					
					winOdds = ticketService.calWinOdds(order.getContent(), tempWinningContent, order.getOdds());
					order.setWinOdds(winOdds);	
					ScanLog.scan.info("jcPrizeTask(lotteryId=%s, transferId=%s, orderNo=%s, winOdds=%d, orderContent=%s)",
							order.getLotteryId(), transferId, order.getOrderNo(), winOdds,
							order.getContent());
				}
				// 将中奖赔率写回竞彩临时表
				if (null != list && list.size() > 0) {
					updateJCTempOrder(list);
				}				
				// 判断相同orderNo其它赛事是否计算过,取出都计算过的orderNo
				for (JCTempOrder order : list) {
					OrderNoCount orderNoCount = orderService.getOrderNoCountAndWinOddsCount(order.getOrderNo(), DBSourceUtil.JC_ORDER_TEMP_TABLENAME);
					
					if (orderNoCount != null && orderNoCount.getWinOddsCount() != null && orderNoCount.getOrderNoCount() == orderNoCount.getWinOddsCount()) {						
						jcTempOrders = orderService.getOrderListByOrderNo(order.getOrderNo(), DBSourceUtil.JC_ORDER_TEMP_TABLENAME);
						winOddsList = new ArrayList<Integer>();
						totalOdds = 0;
						for (JCTempOrder jcTempOrder : jcTempOrders) {
							winOddsList.add(jcTempOrder.getWinOdds());
						}
						// 从主表查询串法情况
						winningOrderInfo = winningOrderService.getOrderByOrderNo(order.getOrderNo(), DBSourceUtil.ORDER_TEMP_TABLENAME);
						String[] playTypeSplit = winningOrderInfo.getPlayType().split(TicketWinningConstantsUtil.SEPARATOR_CHENG);
						m = Integer.valueOf(playTypeSplit[0]);
						n = Integer.valueOf(playTypeSplit[1]);
						playType = MXNUtil.mxn(m, n);
						// 组合计算中奖情况
						for (int i = 0, len = playType.length; i < len; i++) {
							if (m > playType[i]) {
								break;
							}
							totalOdds += TicketWinningUtil.computeMXNWinOdds(winOddsList, m);							
						}
						if (totalOdds > 0) {
							winningOrderInfo.setOrderStatus(OrderStatus.WAIT_AWARD.getValue());
						}
						else{
							winningOrderInfo.setOrderStatus(OrderStatus.NOT_WINNING.getValue());
						}
			
						winningOrderInfo.setWinningAmount(totalOdds * 2);				
						winningOrderInfos.add(winningOrderInfo);							
					}
				}
				if (null != winningOrderInfos && winningOrderInfos.size() > 0) {
					// 将中奖的记录写入中奖结果表
					createWinningOrder(winningOrderInfos);
					// 更改全局数据库中订单
					updatePartnerJCOrderStatus(winningOrderInfos);
				}
				
				startNum += pageSize;
			}
		} catch (Exception e) {
			Log.run.error("jcPrizeTask(transferId=%s, Exception=%s)",
					 transferId, e);
			isException.set(Boolean.TRUE);
		} finally {
			this.latch.countDown();
		}
		
	}

	private void updateJCTempOrder(List<JCTempOrder> orders) {
		JCTempOrder order = null;
		Connection connection = null;
		boolean autoCommit = true;
		DataSource dataSource = applicationContext.getBean(DBSourceUtil.MASTER + DBSourceUtil
				.WINNING_RESULT_DBNAME,
				PooledDataSource.class);

		try {
			if (connection == null) {
				connection = dataSource.getConnection();
				autoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}
			PreparedStatement psLog = connection.prepareStatement("UPDATE t_temp_jc_detail SET winOdds = ?, matchStatus = ? WHERE transferId = ? AND orderNo = ?");
			for (int i = 0, size = orders.size(); i < size; i++) {
				order = orders.get(i);
				psLog.setInt(1, order.getWinOdds());
				psLog.setInt(2, TicketWinningConstantsUtil.MATCH_STATUS_HAS_PRIZE);
				psLog.setString(3, order.getTransferId());
				psLog.setString(4, order.getOrderNo());
				psLog.addBatch();
			}
			psLog.executeBatch();
			connection.commit();
			psLog.close();
		} catch (Exception e) {
			Log.run.error(
					"batch insert to database failed(createWinningOrder):",
					e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				Log.run.error("connection rollback(createWinningOrder):", e);
			}
		} finally {
			closeConnection(connection, autoCommit);
			connection = null;
		}
	}

	private void closeConnection(Connection connection, boolean autoCommit) {
		try {
			if (connection != null) {
				connection.setAutoCommit(autoCommit);
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("closeConnectionException: ", e);
		}
	}
	
	
	private void createWinningOrder(List<WinningOrderInfo> orders) {
		WinningOrderInfo order = null;
		Connection connection = null;
		boolean autoCommit = true;
		DataSource dataSource = applicationContext.getBean(DBSourceUtil.MASTER + DBSourceUtil
				.WINNING_RESULT_DBNAME,
				PooledDataSource.class);

		try {
			if (connection == null) {
				connection = dataSource.getConnection();
				autoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}
			PreparedStatement psLog = connection
					.prepareStatement("replace into t_lottery_winning_result(partnerId,userId,orderNo,orderType,dbName,tableName,lotteryId,issueNo,playType,multiple,orderContent,winningAmount,sendPrizeState,createTime) values(?,?,?,?,?,?,?,?,?,?,?,?,1,now())");
			for (int i = 0, size = orders.size(); i < size; i++) {
				order = orders.get(i);
				// 判断是否中奖
				if (order.getWinningAmount() > 0) {
					psLog.setString(1, order.getPartnerId());
					psLog.setLong(2, order.getUserId());
					psLog.setString(3, order.getOrderNo());
					psLog.setString(4, String.valueOf(order.getOrderType()));
					psLog.setString(5, "");
					psLog.setString(6, "");
					psLog.setString(7, order.getLotteryId());
					psLog.setString(8, order.getIssueNo());
					psLog.setString(9, order.getPlayType());
					psLog.setInt(10, order.getMultiple());
					psLog.setString(11, order.getOrderContent());
					psLog.setLong(12, order.getWinningAmount());
					psLog.addBatch();
				}
			}
			psLog.executeBatch();
			connection.commit();
			psLog.close();
		} catch (Exception e) {
			Log.run.error(
					"batch insert to database failed(createWinningOrder):",
					e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				Log.run.error("connection rollback(createWinningOrder):", e);
			}
		} finally {
			closeConnection(connection, autoCommit);
			connection = null;
		}
	}
	
	
	private void updatePartnerJCOrderStatus(List<WinningOrderInfo> winningOrderInfos){
		ReturnMessage msg = TransactionProcessor.dynamicInvoke("partnerOrder",
				"updateWinResultInfo", winningOrderInfos);
		
		if(msg == null || msg.getObj() == null){
			Log.run.error("updatePartnerJCOrderStatus fail");
		}
	}

}
