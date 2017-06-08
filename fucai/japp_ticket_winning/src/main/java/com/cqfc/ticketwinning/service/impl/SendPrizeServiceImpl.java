package com.cqfc.ticketwinning.service.impl;

import java.util.concurrent.RejectedExecutionException;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.ticketwinning.task.SendPrizeTask;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

@Service
public class SendPrizeServiceImpl {
	
	/**
	 * 派奖
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public void sendPrize(String lotteryId, String issueNo) {
		try{
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor threadPoolTaskExecutor = applicationContext.getBean("threadPoolTaskExecutor", ThreadPoolTaskExecutor.class);
			SendPrizeTask task = new SendPrizeTask(lotteryId, issueNo);
			threadPoolTaskExecutor.submit(task);
		} catch (RejectedExecutionException e) {		
			Log.run.error("派奖请求线程池已满,lotteryId=%s, issueNo=%s, exception=%s", lotteryId, issueNo, e);
		} catch (Exception e) {
			Log.run.error("派奖请求submit线程池发生异常,lotteryId=%s, issueNo=%s, exception=%s", lotteryId, issueNo, e);
		}
	}
}
