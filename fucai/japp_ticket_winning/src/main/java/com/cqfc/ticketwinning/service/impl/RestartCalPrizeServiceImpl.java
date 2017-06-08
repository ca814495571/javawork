package com.cqfc.ticketwinning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.ticketwinning.WinningAmountReturnMessage;
import com.cqfc.ticketwinning.model.Winning;
import com.cqfc.ticketwinning.util.LotteryDrawResultUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.IssueUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;

@Service
public class RestartCalPrizeServiceImpl {
	private ApplicationContext applicationContext;
	
	public void restartCalPrizeAll(String lotteryId, String issueNo){
		Log.fucaibiz.info("restartCalPrizeAll start, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
		
		if(OrderUtil.getLotteryCategory(lotteryId) == OrderStatus.LotteryType.NUMBER_GAME.getType()){
			restartSZCalPrizeAll(lotteryId, issueNo);
		}
		else{
			if(lotteryId.equals("BD")){
				restartCalBDPrizeAll(issueNo);
			}
			else{
				restartCalJCPrizeAll(issueNo);
			}
		}
	}
	/**
	 * 全部重新算奖1.删除中奖结果表中数据 2.重新算奖(发生情况，算错，漏算等)
	 * @param lotteryId
	 * @param issueNo
	 */
	public void restartSZCalPrizeAll(String lotteryId, String issueNo){
		Log.fucaibiz.info("restartSZCalPrizeAll start, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
		try {
			applicationContext = ApplicationContextProvider.getApplicationContext();
			WinningServiceImpl winningService = applicationContext.getBean(
					"winningServiceImpl", WinningServiceImpl.class);
			CalPrizeServiceImpl calPrizeService = applicationContext.getBean("calPrizeServiceImpl",
					CalPrizeServiceImpl.class);
			DBSourceUtil.setMasterDataSourceType(DBSourceUtil
					.WINNING_RESULT_DBNAME);
			int isSuccess = winningService.deleteWinningRecordsByLotteryIdAndIssueNo(lotteryId, issueNo, DBSourceUtil.WINNING_RESULT_TABLENAME);
			Log.run.debug("restartCalPrizeAll, lotteryId=%s, issueNo=%s, isSuccess=%d", lotteryId, issueNo, isSuccess);
			if(isSuccess >= 0){
				calPrizeService.calPrize(lotteryId, issueNo, true);
			}
			else{
				Log.run.error("restartSZCalPrizeAll error, lotteryId=%s, issueNo=%s, isSuccess=%d", lotteryId, issueNo, isSuccess);
			}
		} catch (Exception e) {
			Log.run.error("restartSZCalPrizeAll exception, lotteryId=%s, issueNo=%s, exception=%s", lotteryId, issueNo, e);
		}		
	}
	
	
	/**
	 * 部分重新算奖,仅算中奖结果表中中奖金额为0的数据(发生情况，开奖公告获取大奖奖金为0)
	 * @param lotteryId
	 * @param issueNo
	 */
	public void restartCalPrizePart(String lotteryId, String issueNo){
		Log.fucaibiz.info("restartCalPrizePart(lotteryId: %s, issueNo: %s)", lotteryId, issueNo);
		try {
			applicationContext = ApplicationContextProvider.getApplicationContext();
			WinningServiceImpl winningService = applicationContext.getBean(
					"winningServiceImpl", WinningServiceImpl.class);
			TicketWinningServiceImpl ticketWinningService = applicationContext
					.getBean("ticketWinningServiceImpl", TicketWinningServiceImpl.class);
			//获取开奖公告
			LotteryDrawResult lotteryDrawResult = LotteryDrawResultUtil.getLotteryDrawResult(lotteryId, issueNo);
			//开奖结果
			String winningBallContent = lotteryDrawResult.getDrawResult();
			winningBallContent = LotteryDrawResultUtil.convertWinningBallContentFormat(lotteryId, winningBallContent);
			//开奖奖级
			List<LotteryDrawLevel> prizeLevelList = lotteryDrawResult.getLotteryDrawLevelList();
			Map<Integer, Long> prizeLevelMap = new HashMap<Integer, Long>();
			for(LotteryDrawLevel level : prizeLevelList){
				prizeLevelMap.put(level.getLevel(), level.getMoney());
			}
			List<Winning> winningList = winningService.getWinningAmountZeroList(lotteryId, issueNo, DBSourceUtil.WINNING_RESULT_TABLENAME);
			WinningAmountReturnMessage warm = null;
		
			for(Winning winning : winningList){	
				warm = ticketWinningService.calTicketWinningAmount(
							winning.getLotteryId(), winning.getPlayType(),
							winning.getOrderContent(), winning.getIssueNo(),
							winningBallContent, prizeLevelMap);
				winning.setWinningAmount(warm.getAmount());
			}
			DBSourceUtil.setMasterDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
			for(Winning order : winningList){
				winningService.updateWinningAmount(order.getWinningAmount(), order.getId(), DBSourceUtil.WINNING_RESULT_TABLENAME);
			}
			ActivemqProducer activemqProducer = applicationContext.getBean("activemqProducer", ActivemqProducer.class);
			ActivemqSendObject sendObject = new ActivemqSendObject();
			sendObject.setLotteryId(lotteryId);
			sendObject.setIssueNo(issueNo);
			activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_WINNING_COUNT_COMPLETED_METHODID);
		} catch (Exception e) {
			Log.run.error("restartCalPrizePart(lotteryId=%s, issueNo=%s, errorMsg=%s)", lotteryId, issueNo, e);
			Log.error("restartCalPrizePart(lotteryId=%s, issueNo=%s, errorMsg=%s)", lotteryId, issueNo, e);
		}
	}
	
	/**
	 * 竞彩全部重新算奖
	 * @param transferId
	 */
	public void restartCalJCPrizeAll(String transferId){
		String issueNo = IssueConstant.SPORT_ISSUE_CONSTANT;
		Log.fucaibiz.info("restartCalJCPrizeAll start, transferId=%s, issueNo=%s", transferId, issueNo);
		try {
			applicationContext = ApplicationContextProvider.getApplicationContext();
			WinningServiceImpl winningService = applicationContext.getBean(
					"winningServiceImpl", WinningServiceImpl.class);
			CalJCPrizeServiceImpl calJCPrizeService = applicationContext.getBean("calJCPrizeServiceImpl",
					CalJCPrizeServiceImpl.class);
			DBSourceUtil.setMasterDataSourceType(DBSourceUtil
					.WINNING_RESULT_DBNAME);
			int isSuccess = winningService.deleteJCWinningRecordsByTransferId(transferId, DBSourceUtil.WINNING_RESULT_TABLENAME, DBSourceUtil.JC_ORDER_TEMP_TABLENAME);
			Log.run.debug("restartCalJCPrizeAll, transferId=%s, isSuccess=%d", transferId, isSuccess);
			if(isSuccess >= 0){
				calJCPrizeService.calJCPrize(transferId, issueNo, true);
			}
			else{
				Log.run.error("restartCalJCPrizeAll error, transferId=%s, isSuccess=%d", transferId, isSuccess);
			}
		} catch (Exception e) {
			Log.run.error("restartCalJCPrizeAll exception, transferId=%s, exception=%s", transferId, e);
		}

	}
	
	
	/**
	 * 北单全部重新算奖
	 * @param transferId
	 */
	public void restartCalBDPrizeAll(String transferId){
		String[] transferIdSplit = IssueUtil.splitBeiDanTransferId(transferId).split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		int matchType = Integer.valueOf(transferIdSplit[0]);
		String wareIssue = transferIdSplit[1];
		String matchNo = transferIdSplit[2];
		Log.fucaibiz.info("restartCalBDPrizeAll start, wareIssue=%s, matchNo=%s, matchType=%d", wareIssue, matchNo, matchType);
		try {
			applicationContext = ApplicationContextProvider.getApplicationContext();
			WinningServiceImpl winningService = applicationContext.getBean(
					"winningServiceImpl", WinningServiceImpl.class);
			CalBDPrizeServiceImpl calBDPrizeService = applicationContext.getBean("calBDPrizeServiceImpl",
					CalBDPrizeServiceImpl.class);
			DBSourceUtil.setMasterDataSourceType(DBSourceUtil
					.WINNING_RESULT_DBNAME);
			int isSuccess = winningService.deleteBDWinningRecordsByWareIssueAndMatchNoAndMatchType(wareIssue, matchNo, matchType, DBSourceUtil.WINNING_RESULT_TABLENAME, DBSourceUtil.JC_ORDER_TEMP_TABLENAME);
			Log.run.debug("restartCalJCPrizeAll,  wareIssue=%s, matchNo=%s, matchType=%d, isSuccess=%d", wareIssue, matchNo, matchType, isSuccess);
			if(isSuccess >= 0){
				calBDPrizeService.calBDPrize(wareIssue, matchNo, matchType, true);
			}
			else{
				Log.run.error("restartCalJCPrizeAll error, wareIssue=%s, matchNo=%s, matchType=%d, isSuccess=%d", wareIssue, matchNo, matchType, isSuccess);
			}
		} catch (Exception e) {
			Log.run.error("restartCalJCPrizeAll exception, wareIssue=%s, matchNo=%s, matchType=%d, exception=%s", wareIssue, matchNo, matchType, e);
		}

	}
}
