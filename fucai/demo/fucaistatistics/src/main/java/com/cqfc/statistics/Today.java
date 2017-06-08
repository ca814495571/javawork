package com.cqfc.statistics;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import com.cqfc.statistics.common.ApplicationContextProvider;
import com.cqfc.statistics.serviceImpl.StationService;
import com.cqfc.statistics.serviceImpl.StatisticsService;
import com.cqfc.statistics.serviceImpl.UserActionService;

/**
 * @author: giantspider@126.com
 */

public class Today {

	private StatisticsService getStatisticsServiceContext() {
		ApplicationContext ctx = ApplicationContextProvider
				.getApplicationContext();
		return ctx.getBean("statisticsService", StatisticsService.class);
	}

	private UserActionService getUserActionServiceContext() {
		ApplicationContext ctx = ApplicationContextProvider
				.getApplicationContext();
		return ctx.getBean("userActionService", UserActionService.class);
	}

	private StationService getStationServiceContext() {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        return ctx.getBean("stationService", StationService.class);
    }
	
//	public void t_stat() {
//		Date currentDate = new Date();
//		getStatisticsServiceContext().t_stat(currentDate, currentDate);
//	}
//
//	public void t_stat_lottery() {
//		Date currentDate = new Date();
//		getStatisticsServiceContext().t_stat_lottery(currentDate, currentDate);
//	}
//
//
	public void t_user() {
		Date currentDate = new Date();
		getStatisticsServiceContext().t_user(currentDate, currentDate);
	}

	public void t_deal() {
		Date currentDate = new Date();
		getStatisticsServiceContext().t_deal(currentDate, currentDate);
	}
	
	
	public void t_stat() {
		Date currentDate = new Date();
		getStationServiceContext().stationStatics(currentDate, currentDate);
	}

	public void t_stat_lottery() {
		Date currentDate = new Date();
		getStationServiceContext().stationLotteryStatics(currentDate, currentDate);
	}


	public void t_stat_user() {
		Date currentDate = new Date();
		getStationServiceContext().stationUserStatics(currentDate, currentDate);
	}

	public void t_stat_deal() {
		Date currentDate = new Date();
		getStationServiceContext().stationDealStatics(currentDate, currentDate);
	}
	

	public void t_user_action() {
		Date currentDate = new Date();
		getUserActionServiceContext().userAction(currentDate,0);
	}

}
