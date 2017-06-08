package com.cqfc.ticketwinning;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.protocol.ticketwinning.ReturnData;
import com.cqfc.protocol.ticketwinning.TicketDetailReturnMessage;
import com.cqfc.protocol.ticketwinning.TicketWinningService;
import com.cqfc.protocol.ticketwinning.WinningAmountReturnMessage;
import com.cqfc.protocol.ticketwinning.WinningAmountStat;
import com.cqfc.protocol.ticketwinning.WinningAmountStatData;
import com.cqfc.protocol.ticketwinning.WinningNumStat;
import com.cqfc.protocol.ticketwinning.WinningNumStatData;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.service.impl.RestartCalPrizeServiceImpl;
import com.cqfc.ticketwinning.service.impl.TicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.impl.WinningServiceImpl;
import com.cqfc.ticketwinning.util.LotteryDrawResultUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.LotteryType;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;

@Service
public class TicketWinningHandler implements TicketWinningService.Iface {

	@Autowired
	TicketWinningServiceImpl ticketWinningServiceImpl;

	@Autowired
	WinningServiceImpl winningServiceImpl;

	@Autowired
	RestartCalPrizeServiceImpl restartCalPrizeServiceImpl;

	/**
	 * 计算投注订单中奖总金额
	 */
	@Override
	public WinningAmountReturnMessage calTicketWinningAmount(String lotteryId,
			String playType, String orderContent, String issueNo)
			throws TException {

		// 获取开奖公告
		LotteryDrawResult lotteryDrawResult = LotteryDrawResultUtil
				.getLotteryDrawResult(lotteryId, issueNo);
		// 开奖结果
		String winningBallContent = lotteryDrawResult.getDrawResult();
		//如果双色球开奖号码为8个，则截取前7个
		if(lotteryId.equals(LotteryType.SSQ.getText()) && winningBallContent != null && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == 8)){
			winningBallContent = winningBallContent.substring(0, winningBallContent.lastIndexOf(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
			Log.run.debug("calTicketWinningAmount SSQ, issueNo %s, winningBallContent: %s", issueNo, winningBallContent);
		}
		winningBallContent = LotteryDrawResultUtil
				.convertWinningBallContentFormat(lotteryId, winningBallContent);
		List<LotteryDrawLevel> prizeLevelList = lotteryDrawResult
				.getLotteryDrawLevelList();
		// 开奖奖级
		Map<Integer, Long> prizeLevelMap = new HashMap<Integer, Long>();
		for (LotteryDrawLevel level : prizeLevelList) {
			prizeLevelMap.put(level.getLevel(), level.getMoney());
		}

		return ticketWinningServiceImpl.calTicketWinningAmount(lotteryId,
				playType, orderContent, issueNo, winningBallContent,
				prizeLevelMap);
	}

	/**
	 * 计算投注订单总注数
	 */
	@Override
	public BallCountReturnMessage calBallCount(String lotteryId,
			String playType, String orderContent) throws TException {

		return ticketWinningServiceImpl.calBallCount(lotteryId, playType,
				orderContent);
	}

	/**
	 * 计算投注订单每注详细内容
	 */
	@Override
	public TicketDetailReturnMessage calTicketDetail(String lotteryId,
			String playType, String orderContent) throws TException {

		return ticketWinningServiceImpl.calTicketDetail(lotteryId, playType,
				orderContent);
	}

	/**
	 * 统计中奖金额
	 */
	@Override
	public WinningAmountStatData getWinningAmountStat(
			List<WinningAmountStat> winningAmountStatList, int currentPage,
			int pageSize) throws TException {
		DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);

		return winningServiceImpl.getWinningAmountStat(winningAmountStatList,
				currentPage, pageSize, DBSourceUtil.WINNING_RESULT_TABLENAME);
	}

	/**
	 * 统计中奖大小奖个数与金额
	 */
	@Override
	public WinningNumStatData getWinningNumStat(WinningNumStat winningNumStat,
			int currentPage, int pageSize) throws TException {
		DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);

		return winningServiceImpl.getWinningNumStat(winningNumStat,
				currentPage, pageSize, DBSourceUtil.WINNING_RESULT_TABLENAME);
	}

	/**
	 * 全部重新算奖
	 */
	@Override
	public int restartCalPrizeAll(String lotteryId, String issueNo)
			throws TException {
		restartCalPrizeServiceImpl.restartCalPrizeAll(lotteryId, issueNo);
		return 0;
	}

	/**
	 * 部分重新算奖
	 */
	@Override
	public int restartCalPrizePart(String lotteryId, String issueNo)
			throws TException {
		restartCalPrizeServiceImpl.restartCalPrizePart(lotteryId, issueNo);
		return 0;
	}

	@Override
	public long getTotalWinningMoneyByGame(String lotteryId, String issueNo)
			throws TException {
		try {
			DBSourceUtil
					.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
			return winningServiceImpl.getTotalWinningMoneyByGame(lotteryId,
					issueNo);
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("getTotalWinningMoneyByGame failed");
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}

	@Override
	public long getTotalWinningMoneyByDay(String date) throws TException {
		try {
			DBSourceUtil
					.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
			String startTime = date + " 00:00:00";
			String endTime = date + " 23:59:59";
			return winningServiceImpl.getTotalWinningMoneyByDay(startTime,
					endTime);
		} catch (Exception e) {
			Log.run.debug("", e);
			Log.run.error("getTotalWinningMoneyByGame failed");
			return ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		}
	}
	
	/**
	 * 查询中奖订单信息
	 */
	@Override
	public ReturnData getWinningOrderList(WinningOrderInfo winningOrderInfo,
			int currentPage, int pageSize) throws TException {
		ReturnData returnData = null;
		try {
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
			returnData = winningServiceImpl.getWinningOrderList(winningOrderInfo, currentPage, pageSize, DBSourceUtil.WINNING_RESULT_TABLENAME);
		} catch (Exception e) {
			Log.run.error("查询中奖订单信息发生异常", e);
			return null;
		}
		return returnData;
	}
	
	/**
	 * 计算竞彩投注注数
	 */
	@Override
	public BallCountReturnMessage calJCBallCount(String lotteryId,
			String playType, String orderContent) throws TException {
		
		return ticketWinningServiceImpl.calJCBallCount(lotteryId, orderContent, playType);
	}

}
