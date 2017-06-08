package com.cqfc.ticketwinning.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.ticketwinning.WinningAmountReturnMessage;
import com.cqfc.ticketwinning.model.WinningOrder;
import com.cqfc.ticketwinning.service.IWinningOrderService;
import com.cqfc.ticketwinning.service.impl.TicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.impl.WinningOrderServiceImpl;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.OrderUtil;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

public class CalPrizeTask implements Runnable {
	private CountDownLatch latch;
	private int startNum;
	private int endNum;
	private String lotteryId;
	private String issueNo;
	private String winningBallContent;
	private Map<Integer, Long> prizeLevelMap;
	private AtomicBoolean isException;
	private ApplicationContext applicationContext;

	public CalPrizeTask(CountDownLatch latch, int startNum, int endNum,
			String lotteryId, String issueNo, String winningBallContent,
			Map<Integer, Long> prizeLevelMap, AtomicBoolean isException) {
		this.latch = latch;
		this.startNum = startNum;
		this.endNum = endNum;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.winningBallContent = winningBallContent;
		this.prizeLevelMap = prizeLevelMap;
		this.isException = isException;
	}

	@Override
	public void run() {
		/**
		 * 1.从临时表中读取order信息 2.调用过关算奖程序接口 3.将中奖结果写入中奖结果表中
		 */
		ScanLog.scan.debug(
				"TicketWinningTask(lotteryId=%s, issueNo=%s)", lotteryId, issueNo);
		applicationContext = ApplicationContextProvider.getApplicationContext();
		TicketWinningServiceImpl ticketWinningService = applicationContext
				.getBean("ticketWinningServiceImpl",
						TicketWinningServiceImpl.class);
		IWinningOrderService orderService = applicationContext.getBean(
				"winningOrderServiceImpl", WinningOrderServiceImpl.class);
		int pageSize = TicketWinningConstantsUtil.ORDER_TABLE_PAGE_SIZE;
		int count = 0;
		List<WinningOrder> list = null;
		WinningAmountReturnMessage warm = null;	
		
		try {
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.ORDER_TEMP_DBNAME);
			while (true) {
				count = endNum - startNum;
				if (count <= 0) {
					break;
				}
				if (count > pageSize) {
					count = pageSize;
				}
				list = orderService.getOrderListOfWaittinglottery(lotteryId,
						issueNo, DBSourceUtil.ORDER_TEMP_TABLENAME, startNum, count);
				ScanLog.scan
				.info("TicketWinningTask(lotteryId=%s, issueNo=%s, cursor=%d, listLen=%d, count=%d)",
						lotteryId, issueNo, startNum, list.size(), count);
				if (list == null || list.size() == 0) {
					break;
				}
				if (isException.get()) {
					return;
				}
				for (WinningOrder order : list) {
					warm = ticketWinningService.calTicketWinningAmount(
							order.getLotteryId(), order.getPlayType(),
							order.getOrderContent(), order.getIssueNo(),
							winningBallContent, prizeLevelMap);
					order.setPrize(warm.isIsPrize());
					order.setWinningAmount(warm.getAmount());
					ScanLog.scan
					.info("TicketWinningTask(lotteryId=%s, issueNo=%s, orderNo=%s, isPrize=%b, winningAmount=%d, orderContent=%s, playType=%s)",
							lotteryId, issueNo, order.getOrderNo(), warm.isIsPrize(),
							warm.getAmount(), order.getOrderContent(), order.getPlayType());							
					
				}
				// 将中奖的记录写入中奖结果表
				if(null != list && list.size() > 0){
					createWinningOrder(list);
				}
				startNum += pageSize;
			}
		} catch (TException e) {
			Log.run.error(
					"TicketWinningTask(lotteryId=%s, issueNo=%s, exception=%s)",
					 lotteryId, issueNo, e);
			isException.set(Boolean.TRUE);
		} finally {
			this.latch.countDown();
		}

	}

	private void createWinningOrder(List<WinningOrder> orders) {
		WinningOrder order = null;
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
				if (order.isPrize()) {
					psLog.setString(1, order.getPartnerId());
					psLog.setLong(2, order.getUserId());
					psLog.setString(3, order.getOrderNo());
					psLog.setString(4, order.getOrderType());
					psLog.setString(5, TicketWinningConstantsUtil.PARTNET_ORDER_DB_NAME_PREFIX + OrderUtil.getDbNameByOrderNo(order.getOrderNo()));
					psLog.setString(6, TicketWinningConstantsUtil.PARTNER_ORDER_TABLE_NAME_PREFIX + OrderUtil.getTableNameByOrderNo(order.getOrderNo()));
					psLog.setString(7, lotteryId);
					psLog.setString(8, issueNo);
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

	private void closeConnection(Connection connection, boolean autoCommit) {
		try {
			if (connection != null) {
				connection.setAutoCommit(autoCommit);
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("closeConnection", e);
		}
	}
}
