package com.cqfc.ticketwinning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.ticketwinning.service.IWinningOrderService;
import com.cqfc.ticketwinning.task.CalPrizeTask;
import com.cqfc.ticketwinning.util.LotteryDrawResultUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.Configuration;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.LotteryType;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;

@Service
public class CalPrizeServiceImpl {

	/**
	 * 数字彩算奖
	 * @param lotteryId 
	 * @param issueNo
	 */
	public int calPrize(String lotteryId, String issueNo, boolean isRestartCalPrize) {
		Log.fucaibiz.info("calPrize(lotteryId=%s, issueNo=%s, isRestartCalPrize=%b)", lotteryId, issueNo, isRestartCalPrize);
		int returnValue = 1;
		
		try{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor calPrizeThreadPoolTaskExecutor = applicationContext.getBean("calPrizeThreadPoolTaskExecutor", ThreadPoolTaskExecutor.class);
			IWinningOrderService orderService = applicationContext.getBean("winningOrderServiceImpl", WinningOrderServiceImpl.class);
			
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.ORDER_TEMP_DBNAME);
			int totalCount = orderService.getOrderCount(lotteryId, issueNo, DBSourceUtil.ORDER_TEMP_TABLENAME);
			int count = (totalCount % TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT == 0) ? (totalCount / TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT) : (totalCount / TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT + 1);
			//任务计数器
			CountDownLatch latch = new CountDownLatch(TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT);
			AtomicBoolean isException = new AtomicBoolean(Boolean.FALSE);
			//获取开奖公告
			LotteryDrawResult lotteryDrawResult = LotteryDrawResultUtil.getLotteryDrawResult(lotteryId, issueNo);
			//开奖结果
			String winningBallContent = lotteryDrawResult.getDrawResult();
			//如果双色球开奖号码为8个，则截取前7个
			if(lotteryId.equals(LotteryType.SSQ.getText()) && winningBallContent != null && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == 8)){
				winningBallContent = winningBallContent.substring(0, winningBallContent.lastIndexOf(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				Log.run.debug("calprize SSQ, issueNo %s, winningBallContent: %s", issueNo, winningBallContent);
			}
			//判断开奖号码个数是否符合规则
			if(!LotteryDrawResultUtil.isWinningBallNumsRight(lotteryId, winningBallContent)){
				Log.run.error("calPrize winning ball nums error, lotteryId: %s, issueNo: %s, winningBallContent: %s",  lotteryId, issueNo, winningBallContent);
				Log.error("calPrize winning ball nums error, lotteryId: %s, issueNo: %s, winningBallContent: %s",  lotteryId, issueNo, winningBallContent);
				return -1;
			}
			winningBallContent = LotteryDrawResultUtil.convertWinningBallContentFormat(lotteryId, winningBallContent);
			//开奖奖级
			List<LotteryDrawLevel> prizeLevelList = lotteryDrawResult.getLotteryDrawLevelList();
			Map<Integer, Long> prizeLevelMap = new HashMap<Integer, Long>();
			for(LotteryDrawLevel level : prizeLevelList){
				prizeLevelMap.put(level.getLevel(), level.getMoney());
			}
			
			for(int i = 0; i < TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT; i++){
				calPrizeThreadPoolTaskExecutor.submit(new CalPrizeTask(latch, i * count, (i+1) * count, lotteryId, issueNo, winningBallContent, prizeLevelMap, isException));
				if(i == TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT - 1){
					calPrizeThreadPoolTaskExecutor.submit(new CalPrizeTask(latch, i * count, totalCount, lotteryId, issueNo, winningBallContent, prizeLevelMap, isException));				
				}
			} 
			
			//等待算奖完成
			latch.await();
			Log.run.debug("calPrize: lotteryId=%s, issueNo=%s", lotteryId, issueNo);
			if(isException.get()){
				Log.run.error("calPrize(complete):  exception");
				return -1;
			}
			Log.fucaibiz.info("calPrize(complete: success, lotteryId=%s, issueNo=%s)", lotteryId, issueNo);
			//算奖完成，写数据库并通知MQ
			ActivemqProducer activemqProducer = applicationContext.getBean("activemqProducer", ActivemqProducer.class);
			if(!isRestartCalPrize){
				LotteryTaskComplete taskComplete = new LotteryTaskComplete();
				taskComplete.setLotteryId(lotteryId);
				taskComplete.setIssueNo(issueNo);
				taskComplete.setTaskType(IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);
				taskComplete.setSetNo(Configuration.getConfigValue("setNo"));	//待处理...

				ActivemqSendObject sendObject = new ActivemqSendObject();
				sendObject.setLotteryId(lotteryId);
				sendObject.setIssueNo(issueNo);
				sendObject.setObj(taskComplete);
				activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
				
				Log.fucaibiz.info("calPrize(createLotteryTaskComplete):  success, lotteryId=%s, issueNo=%s", lotteryId, issueNo);			
			}
			ActivemqSendObject sendObject = new ActivemqSendObject();
			sendObject.setLotteryId(lotteryId);
			sendObject.setIssueNo(issueNo);
			activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_WINNING_COUNT_COMPLETED_METHODID);
		} catch (Exception e) {
			Log.run.error("calPrize(complete): false, lotteryId=%s, issueNo=%s, Exception: %s", lotteryId, issueNo, e);
			Log.error("calPrize(complete): false, lotteryId=%s, issueNo=%s, Exception: %s", lotteryId, issueNo, e);
			return -1;
		}
		return returnValue;
	}
}
