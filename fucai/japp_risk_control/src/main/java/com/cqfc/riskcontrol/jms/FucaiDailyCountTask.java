package com.cqfc.riskcontrol.jms;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.riskcontrol.StatisticDataByDay;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.ticketissue.FucaiCount;
import com.cqfc.riskcontrol.dao.RiskControlDao;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

public class FucaiDailyCountTask  implements Runnable{

	private ScheduledExecutorService threadPool;
	private RiskControlDao riskControlDao;
	private String day;
	private int retryTime;

	public FucaiDailyCountTask(ScheduledExecutorService threadPool,
			RiskControlDao riskControlDao, String day, int retryTime) {

		this.threadPool = threadPool;
		this.riskControlDao = riskControlDao;
		this.day = day;
		this.retryTime = retryTime;
	}

	@Override
	public void run() {
		StatisticDataByDay dataByDay = new StatisticDataByDay();
		dataByDay.setDay(day);
		ReturnMessage message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_TICKET_ISSUE,
				"getFucaiCountByDay", day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			FucaiCount statinfo = (FucaiCount)message.getObj();
			if (statinfo.getTotalBuy() == ServiceStatusCodeUtil.STATUS_LIST_IS_NULL) {
				if(retryTime > 50){
					Log.run.error("retry 50 times and cannot get fucai count, day=%s",day);
					return;
				}
				threadPool.schedule(new FucaiDailyCountTask(threadPool, riskControlDao, day, retryTime + 1), 5, TimeUnit.MINUTES);
				return;
			}
			dataByDay.setFucaiTotalBuy(statinfo.getTotalBuy());
			Log.run.debug("get fucai total winning by day,day=%s, money=%d", day, statinfo.getTotalWinning());
			riskControlDao.updateFucaiCountByDay(dataByDay);
		} else {
			Log.run.error("get statistic data from ticket issue by day failed.");
		}
		
	}

}
