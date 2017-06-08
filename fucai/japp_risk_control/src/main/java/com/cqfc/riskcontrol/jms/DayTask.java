package com.cqfc.riskcontrol.jms;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partnerorder.DailyRiskCount;
import com.cqfc.protocol.riskcontrol.StatisticDataByDay;
import com.cqfc.protocol.ticketissue.FucaiCount;
import com.cqfc.protocol.useraccount.UserHandselCount;
import com.cqfc.riskcontrol.dao.RiskControlDao;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;

@Component
public class DayTask {
	@Resource
	private RiskControlDao riskControlDao;
	
	@Resource(name = "threadPoolTaskExecutor")
	private ScheduledExecutorService threadPoolTaskExecutor;
	
	@Scheduled(cron = "1 0 0 * * ?")
	public void executeInit() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		String yestoday = DateUtil.getDateTime("yyyy-MM-dd",
				DateUtil.getOffsetDate(new Date(), -1));
		String today = DateUtil.getDateTime("yyyy-MM-dd", new Date());
		riskControlDao.createTickStatisticByDay(yestoday);
		TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_USER_ACCOUNT, "initHandselCount",
				today);
	}

	public static void countData(RiskControlDao riskControlDao, String day){
		StatisticDataByDay dataByDay = riskControlDao
				.getStatisticDataByDay(day);
		if (dataByDay == null) {
			dataByDay = new StatisticDataByDay();
			dataByDay.setDay(day);
			riskControlDao.createTickStatisticByDay(day);
		}
		ReturnMessage message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_PARTNER_ORDER, "getDailyRiskCount",
				day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			DailyRiskCount dailyCount = (DailyRiskCount) message.getObj();
			dataByDay.setTotalRecharge(dailyCount.getRechargeTotalMoney());
			dataByDay.setTotalWithdraw(dailyCount.getWithdrawTotalMoney());
			dataByDay.setTotalWinningMoney(dailyCount.getAwardPrizeMoney());
		} else {
			Log.run.error("get statistic data from partner order failed.");
		}
		
		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_PARTNER_ACCOUNT, "totalAccountMoney");
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			
			Long totalPartnerAccount = (Long) message.getObj();
			if (totalPartnerAccount < 0) {
				Log.run.error("get partner account failed.");
			} else {
				dataByDay.setTotalPartnerAccount(totalPartnerAccount);
			}
		} else {
			Log.run.error("get total partner account from user account failed.");
		}
		long totalPaylogNum = 0;
		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_PARTNER_ACCOUNT, "totalPaylogNum", day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			
			Long totalPartnerPaylog = (Long) message.getObj();
			if (totalPartnerPaylog < 0) {
				Log.run.error("get partner pay log failed.");
			} else {
				totalPaylogNum += totalPartnerPaylog;
			}
		} else {
			Log.run.error("get total partner pay log from user account failed.");
		}	
		
		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_USER_ACCOUNT, "totalAccountMoney");
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			
			Long totalUserAccount = (Long) message.getObj();
			if (totalUserAccount < 0) {
				Log.run.error("get total user account failed.");
			} else {
				dataByDay.setTotalUserAccount(totalUserAccount);
			}
		} else {
			Log.run.error("get total user account from user account failed.");
		}

		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_USER_ACCOUNT, "totalPaylogNum", day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			
			Long totalUserPaylog = (Long) message.getObj();
			if (totalUserPaylog < 0) {
				Log.run.error("get user pay log failed.");
			} else {
				totalPaylogNum += totalUserPaylog;
			}
		} else {
			Log.run.error("get total user pay log from user account failed.");
		}
		
		dataByDay.setPaylogNum(totalPaylogNum);
		
		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_USER_ACCOUNT, "getUserHandselCount",
				day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			UserHandselCount dailyCount = (UserHandselCount) message.getObj();
			dataByDay.setTotalHansel(dailyCount.getTotalMoney());
			dataByDay.setInvalidHansel(dailyCount.getTotalInvalidMoney());
		} else {
			Log.run.error("get statistic data from user account failed.");
		}
		
		
		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_PARTNER_ORDER, "getTotalTicknumByDay",
				day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {

			Long totalTicketNum = (Long) message.getObj();
			dataByDay.setTotalTicketNum(totalTicketNum);
		} else {
			Log.run.error("get statistic data from user account failed.");
		}
		

		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_TICKET_WINNING,
				"getTotalWinningMoneyByDay", day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			Long totalWinningMoney = (Long) message.getObj();
			dataByDay.setTotalWinningMoney(totalWinningMoney);
			Log.run.debug("get fucai total winning by day,day=%s, money=%d", day, totalWinningMoney);
		} else {
			Log.run.error("get statistic data from ticket winning failed.");
		}
		
		message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_TICKET_ISSUE,
				"getFucaiCountByDay", day);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			FucaiCount statinfo = (FucaiCount)message.getObj();
			dataByDay.setFucaiTotalBuy(statinfo.getTotalBuy());
			Log.run.debug("get fucai total winning by day,day=%s, money=%d", day, statinfo.getTotalWinning());
		} else {
			Log.run.error("get statistic data from ticket issue failed.");
		}
		
			
		Log.run.debug(
				"before update statistic by day, TotalRecharge=%d, TotalWithdraw=%d, TotalWinningMoney=%d, TotalHansel=%d, InvalidHansel=%d, FucaiTotalBuy=%d, PaylogNum=%d",
				dataByDay.getTotalPartnerAccount(), dataByDay.getTotalUserAccount(),
				dataByDay.getTotalRecharge(), dataByDay.getTotalWithdraw(),
				dataByDay.getTotalWinningMoney(), dataByDay.getTotalHansel(),
				dataByDay.getInvalidHansel(), dataByDay.getFucaiTotalBuy(),
				dataByDay.getTotalTicketNum(), dataByDay.getPaylogNum());
		riskControlDao.updateStatisticDataByDay(dataByDay);
	}
	@Scheduled(cron = "0 30 1 * * ?")
	public void executeCount() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		String yestoday = DateUtil.getDateTime("yyyy-MM-dd",
				DateUtil.getOffsetDate(new Date(), -1));
		Log.run.debug("start to statistic data, day=%s", yestoday);
		countData(riskControlDao, yestoday);
		StatisticDataByDay dataByDay = new StatisticDataByDay();
		dataByDay.setDay(yestoday);
		ReturnMessage message = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_TICKET_ISSUE,
				"getFucaiCountByDay", yestoday);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			FucaiCount statinfo = (FucaiCount)message.getObj();
			if (statinfo.getTotalBuy() == ServiceStatusCodeUtil.STATUS_LIST_IS_NULL) {
				threadPoolTaskExecutor.schedule(new FucaiDailyCountTask(threadPoolTaskExecutor, riskControlDao, yestoday, 1), 5, TimeUnit.MINUTES);
				return;
			}
			dataByDay.setFucaiTotalBuy(statinfo.getTotalBuy());
			Log.run.debug("get fucai total winning by day,day=%s, money=%d", yestoday, statinfo.getTotalWinning());
		} else {
			Log.run.error("get statistic data from ticket issue by day failed.");
		}
		
	}
}
