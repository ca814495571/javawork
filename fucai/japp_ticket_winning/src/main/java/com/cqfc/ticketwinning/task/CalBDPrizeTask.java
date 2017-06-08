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
import com.cqfc.ticketwinning.factory.BDTicketWinningServiceFactory;
import com.cqfc.ticketwinning.model.JCTempOrder;
import com.cqfc.ticketwinning.model.OrderNoCount;
import com.cqfc.ticketwinning.model.WinningOrder.OrderStatus;
import com.cqfc.ticketwinning.service.AbstrartJCTicketWinningService;
import com.cqfc.ticketwinning.service.IJCTempOrderService;
import com.cqfc.ticketwinning.service.IWinningOrderService;
import com.cqfc.ticketwinning.service.impl.JCTempOrderServiceImpl;
import com.cqfc.ticketwinning.service.impl.WinningOrderServiceImpl;
import com.cqfc.ticketwinning.util.BDBFMXNUtil;
import com.cqfc.ticketwinning.util.BDSFGGMXNUtil;
import com.cqfc.ticketwinning.util.BDSPFMXNUtil;
import com.cqfc.ticketwinning.util.BDZJQSAndBQCAndSXDSMXNUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryType;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

public class CalBDPrizeTask implements Runnable {
	
	private CountDownLatch latch;
	private int startNum;
	private int endNum;
	private String wareIssue;
	private String matchNo;
	private int matchType;
	private String winningContent;
	private String spContent;
	private AtomicBoolean isException;
	private ApplicationContext applicationContext;

	public CalBDPrizeTask(CountDownLatch latch, int startNum, int endNum,
			String wareIssue, String matchNo, int matchType, String winningContent, String spContent, 
			AtomicBoolean isException) {
		this.latch = latch;
		this.startNum = startNum;
		this.endNum = endNum;
		this.wareIssue = wareIssue;
		this.matchNo = matchNo;
		this.matchType = matchType;
		this.winningContent = winningContent;
		this.spContent = spContent;
		this.isException = isException;
	}
	
	@Override
	public void run() {
		ScanLog.scan.debug("bdPrizeTask(wareIssue=%s, matchNo=%s, matchType=%d)", wareIssue, matchNo, matchType);
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
		String tempSpContent = "";
		
		try {
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.ORDER_TEMP_DBNAME);
			Map<String, String> winningContentMap = TicketWinningUtil.getWinningContentMap(winningContent);
			Map<String, String> spContentMap = TicketWinningUtil.getWinningContentMap(spContent);
			while (true) {
				count = endNum - startNum;
				if (count <= 0) {
					break;
				}
				if (count > pageSize) {
					count = pageSize;
				}
				list = orderService.getBDTempOrderList(wareIssue, matchNo, matchType, DBSourceUtil.JC_ORDER_TEMP_TABLENAME, startNum, count);
				ScanLog.scan.info("bdPrizeTask(wareIssue=%s, matchNo=%s, matchType=%d, pecursor=%d, listLen=%d, count=%d)", wareIssue, matchNo, matchType, startNum, list.size(), count);
				if (list == null || list.size() == 0) {
					break;
				}
				if (isException.get()) {
					return;
				}
				for (JCTempOrder order : list) {	
					// 计算该场比赛中奖情况
					String lotteryId = order.getLotteryId();
					ticketService = BDTicketWinningServiceFactory.getBDTicketWinningServiceInstance(order.getLotteryId());
					tempWinningContent = winningContentMap.get(lotteryId);		
					tempSpContent = spContentMap.get(lotteryId);					
					
					winOdds = ticketService.calBDWinOdds(order.getContent(), tempWinningContent, tempSpContent);
					order.setWinOdds(winOdds);	
					order.setOdds(tempSpContent);
					ScanLog.scan.info("bdPrizeTask(lotteryId=%s, wareIssue=%s, matchNo=%s, matchType=%d, orderNo=%s, winOdds=%d, orderContent=%s)",
							order.getLotteryId(), wareIssue, matchNo, matchType, order.getOrderNo(), winOdds,
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
						if (winningOrderInfo.getLotteryId().equals(LotteryType.BDBF.getText())){
							playType = BDBFMXNUtil.mxn(m, n);
						}
						else if (winningOrderInfo.getLotteryId().equals(LotteryType.BDSFGG.getText())){
							playType = BDSFGGMXNUtil.mxn(m, n);
						}
						else if (winningOrderInfo.getLotteryId().equals(LotteryType.BDSPF.getText())){
							playType = BDSPFMXNUtil.mxn(m, n);
						}
						else {
							playType = BDZJQSAndBQCAndSXDSMXNUtil.mxn(m, n);
						}
						
						// 组合计算中奖情况
						for (int i = 0, len = playType.length; i < len; i++) {
							if (m > playType[i]) {
								break;
							}
							totalOdds += TicketWinningUtil.computeMXNWinOdds(winOddsList, m);							
						}
						if (totalOdds > 0 && (totalOdds != 100 && m != 1)) {
							winningOrderInfo.setOrderStatus(OrderStatus.WAIT_AWARD.getValue());
							winningOrderInfo.setWinningAmount(totalOdds * 2 * 65 / 100);
						}
						else if (totalOdds == 100 && m == 1) {
							winningOrderInfo.setOrderStatus(OrderStatus.WAIT_AWARD.getValue());
							winningOrderInfo.setWinningAmount(totalOdds * 2);
						}
						else{
							winningOrderInfo.setOrderStatus(OrderStatus.NOT_WINNING.getValue());
							winningOrderInfo.setWinningAmount(totalOdds * 2 * 65 / 100);
						}
						winningOrderInfos.add(winningOrderInfo);
										
											
					}
				}
				if (null != winningOrderInfos && winningOrderInfos.size() > 0) {
					// 将中奖的记录写入中奖结果表
					createWinningOrder(winningOrderInfos);
					// 更改全局数据库中订单
					updatePartnerBDOrderStatus(winningOrderInfos);
				}
				
				startNum += pageSize;
			}
		} catch (Exception e) {
			Log.run.error("bdPrizeTask(wareIssue=%s, Exception=%s)",
					 wareIssue, e);
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
			PreparedStatement psLog = connection.prepareStatement("UPDATE t_temp_jc_detail SET winOdds = ?, matchStatus = ?, odds = ? WHERE orderNo = ? AND matchNo = ?");
			for (int i = 0, size = orders.size(); i < size; i++) {
				order = orders.get(i);
				psLog.setInt(1, order.getWinOdds());
				psLog.setInt(2, TicketWinningConstantsUtil.MATCH_STATUS_HAS_PRIZE);
				psLog.setString(3, order.getOdds());
				psLog.setString(4, order.getOrderNo());
				psLog.setString(5, order.getMatchNo());
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
	
	
	private void updatePartnerBDOrderStatus(List<WinningOrderInfo> winningOrderInfos){
		ReturnMessage msg = TransactionProcessor.dynamicInvoke("partnerOrder",
				"updateWinResultInfo", winningOrderInfos);
		
		if(msg == null || msg.getObj() == null){
			Log.run.error("updatePartnerBDOrderStatus fail");
		}		
	}

}
