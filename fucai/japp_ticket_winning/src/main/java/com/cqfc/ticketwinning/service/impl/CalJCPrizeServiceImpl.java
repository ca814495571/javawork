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
import com.cqfc.ticketwinning.task.CalJCPrizeTask;
import com.cqfc.ticketwinning.util.LotteryDrawResultUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.Configuration;
import com.cqfc.util.IssueConstant;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;

@Service
public class CalJCPrizeServiceImpl {	
	
	/**
	 * 竞彩算奖
	 * @param transferId
	 * @param isRestartCalPrize
	 * @return
	 */
	public int calJCPrize(String transferId, String issueNo, boolean isRestartCalPrize){
		Log.fucaibiz.info("calJCPrize(transferId=%s, issueNo=%s, isRestartCalPrize=%b)", transferId, issueNo, isRestartCalPrize);
		int returnValue = 1;
		try{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor calJCPrizeThreadPoolTaskExecutor = applicationContext.getBean("calJCPrizeThreadPoolTaskExecutor", ThreadPoolTaskExecutor.class);
			IJCTempOrderService orderService = applicationContext.getBean("JCTempOrderServiceImpl", JCTempOrderServiceImpl.class);
			
			DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.ORDER_TEMP_DBNAME);
			int totalCount = orderService.getJCTempOrderCount(transferId, DBSourceUtil.JC_ORDER_TEMP_TABLENAME);
			int count = (totalCount % TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT == 0) ? (totalCount / TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT) : (totalCount / TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT + 1);
			
			//任务计数器
			CountDownLatch latch = new CountDownLatch(TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT);
			AtomicBoolean isException = new AtomicBoolean(Boolean.FALSE);

			//获取开奖公告
			String winningContent = "";
			List<MatchCompetiveResult> matchCompetiveResults = LotteryDrawResultUtil.getMatchCompetiveResultList(transferId, issueNo).getMatchCompetiveResultList();
			for(MatchCompetiveResult mResult : matchCompetiveResults){
				winningContent += mResult.getLotteryId() + TicketWinningConstantsUtil.SEPARATOR_JINGHAO + mResult.getDrawResult() + TicketWinningConstantsUtil.SEPARATOR_AT;
			}
			if(!winningContent.equals("")){
				winningContent = winningContent.substring(0, winningContent.length() - 1);				
			}
			else{
				return -1;
			}
			
			for(int i = 0; i < TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT; i++){
				calJCPrizeThreadPoolTaskExecutor.submit(new CalJCPrizeTask(latch, i * count, (i+1) * count, transferId, winningContent, isException));
				if(i == TicketWinningConstantsUtil.CAL_PRIZE_SPERATE_COUNT - 1){
					calJCPrizeThreadPoolTaskExecutor.submit(new CalJCPrizeTask(latch, i * count, totalCount, transferId, winningContent, isException));				
				}
			}			
			
			//等待算奖完成
			latch.await();
			if(isException.get()){
				Log.run.error("calJCPrize(complete): exception");
				return -1;
			}
			Log.fucaibiz.info("calJCPrize(complete: success, transferId=%s, issueNo=%s)", transferId, issueNo);
			
			//算奖完成，写数据库并通知MQ
			ActivemqProducer activemqProducer = applicationContext.getBean("activemqProducer", ActivemqProducer.class);
			if(!isRestartCalPrize){
				LotteryTaskComplete taskComplete = new LotteryTaskComplete();
				taskComplete.setLotteryId(transferId);
				taskComplete.setIssueNo(issueNo);
				taskComplete.setTaskType(IssueConstant.TASK_COMPLETE_HASCAL_WAITAUDIT);
				taskComplete.setSetNo(Configuration.getConfigValue("setNo"));	//待处理...

				ActivemqSendObject sendObject = new ActivemqSendObject();
				sendObject.setLotteryId(transferId);
				sendObject.setIssueNo(issueNo);
				sendObject.setObj(taskComplete);
				activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
				
				Log.fucaibiz.info("calJCPrize(createLotteryTaskComplete):  success, transferId=%s, issueNo=%s", transferId, issueNo);			
			}
			ActivemqSendObject sendObject = new ActivemqSendObject();
			sendObject.setLotteryId(transferId);
			sendObject.setIssueNo(issueNo);
			activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_WINNING_COUNT_COMPLETED_METHODID);
		} catch (Exception e) {
			Log.run.error("calJCPrize(complete): false, transferId=%s, Exception: %s", transferId, e);
			Log.error("calJCPrize(complete): false, transferId=%s, Exception: %s", transferId, e);
			return -1;
		}
		return returnValue;
	}	

}
