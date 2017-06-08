package com.cqfc.ticketwinning.service.impl;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.ticketwinning.service.IJCTempOrderService;
import com.cqfc.ticketwinning.task.CalBDPrizeTask;
import com.cqfc.ticketwinning.util.LotteryDrawResultUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.Configuration;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.IssueUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;

@Service
public class CalBDPrizeServiceImpl {	
	
	/**
	 * 北单算奖
	 * @param wareIssue
	 * @param matchNo
	 * @param matchType
	 * @param isRestartCalPrize
	 * @return
	 */
	public int calBDPrize(String wareIssue, String matchNo, int matchType, boolean isRestartCalPrize){
		Log.fucaibiz.info("calBDPrize(wareIssue=%s, matchNo=%s, matchType=%d, isRestartCalPrize=%b)", wareIssue, matchNo, matchType, isRestartCalPrize);
		int returnValue = 1;
		try{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor calBDPrizeThreadPoolTaskExecutor = applicationContext.getBean("calJCPrizeThreadPoolTaskExecutor", ThreadPoolTaskExecutor.class);
			IJCTempOrderService orderService = applicationContext.getBean("JCTempOrderServiceImpl", JCTempOrderServiceImpl.class);
			
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.ORDER_TEMP_DBNAME);
			int totalCount = orderService.getBDTempOrderCount(wareIssue, matchNo, matchType, DBSourceUtil.JC_ORDER_TEMP_TABLENAME);
			int count = (totalCount % TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT == 0) ? (totalCount / TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT) : (totalCount / TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT + 1);
			
			//任务计数器
			CountDownLatch latch = new CountDownLatch(TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT);
			AtomicBoolean isException = new AtomicBoolean(Boolean.FALSE);

			//获取开奖公告
			String winningContent = "";
			String spContent = "";
			List<MatchCompetiveResult> matchCompetiveResults = LotteryDrawResultUtil.getBDMatchCompetiveResultList(wareIssue, matchNo, matchType).getMatchCompetiveResultList();
			for(MatchCompetiveResult mResult : matchCompetiveResults){
				winningContent += mResult.getLotteryId() + TicketWinningConstantsUtil.SEPARATOR_JINGHAO + mResult.getDrawResult() + TicketWinningConstantsUtil.SEPARATOR_AT;
				spContent += mResult.getLotteryId() + TicketWinningConstantsUtil.SEPARATOR_JINGHAO + mResult.getSp() + TicketWinningConstantsUtil.SEPARATOR_AT;
			}
			if(!winningContent.equals("")){
				winningContent = winningContent.substring(0, winningContent.length() - 1);				
			}
			else{
				return -1;
			}
			if(!spContent.equals("")){
				spContent = spContent.substring(0, spContent.length() - 1);				
			}
			else{
				return -1;
			}
			
			for(int i = 0; i < TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT; i++){
				calBDPrizeThreadPoolTaskExecutor.submit(new CalBDPrizeTask(latch, i * count, (i+1) * count, wareIssue, matchNo, matchType, winningContent, spContent, isException));
				if(i == TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT - 1){
					calBDPrizeThreadPoolTaskExecutor.submit(new CalBDPrizeTask(latch, i * count, totalCount, wareIssue, matchNo, matchType, winningContent, spContent, isException));				
				}
			}			
			
			//等待算奖完成
			latch.await();
			if(isException.get()){
				Log.run.error("calBDPrize(complete): exception");
				return -1;
			}
			Log.fucaibiz.info("calBDPrize(complete: success, wareIssue=%s, matchNo=%s, matchType=%d)", wareIssue, matchNo, matchType);
			
			//算奖完成，写数据库并通知MQ
			String transferId = IssueUtil.getBeiDanTransferId(wareIssue, matchNo, matchType);
			ActivemqProducer activemqProducer = applicationContext.getBean("activemqProducer", ActivemqProducer.class);
			if(!isRestartCalPrize){
				LotteryTaskComplete taskComplete = new LotteryTaskComplete();
				taskComplete.setLotteryId(transferId);
				taskComplete.setIssueNo(wareIssue);
				taskComplete.setTaskType(IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);
				taskComplete.setSetNo(Configuration.getConfigValue("setNo"));	//待处理...

				ActivemqSendObject sendObject = new ActivemqSendObject();
				sendObject.setLotteryId(transferId);
				sendObject.setIssueNo(wareIssue);
				sendObject.setObj(taskComplete);
				activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
				
				Log.fucaibiz.info("calBDPrize(createLotteryTaskComplete):  success, transferId=%s, wareIssue=%s", transferId, wareIssue);			
			}
			ActivemqSendObject sendObject = new ActivemqSendObject();
			sendObject.setLotteryId(transferId);
			sendObject.setIssueNo(wareIssue);
			activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_WINNING_COUNT_COMPLETED_METHODID);
		} catch (Exception e) {
			Log.run.error("calBDPrize(complete): false, wareIssue=%s, matchNo=%s, matchType=%d, Exception: %s", wareIssue, matchNo, matchType, e);
			Log.error("calBDPrize(complete): false, wareIssue=%s, matchNo=%s, matchType=%d, Exception: %s", wareIssue, matchNo, matchType, e);
			return -1;
		}
		return returnValue;
	}	

}
