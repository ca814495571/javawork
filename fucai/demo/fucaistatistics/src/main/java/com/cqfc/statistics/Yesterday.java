package com.cqfc.statistics;

import java.util.Date;

import org.springframework.context.ApplicationContext;

import com.cqfc.statistics.common.ApplicationContextProvider;
import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.serviceImpl.StationService;
import com.cqfc.statistics.serviceImpl.StatisticsService;
import com.cqfc.statistics.serviceImpl.UserActionService;

/**
 * @author: giantspider@126.com
 */
public class Yesterday {

    private StatisticsService getStatisticsServiceContext() {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        return ctx.getBean("statisticsService", StatisticsService.class);
    }
    
    private UserActionService getUserActionServiceContext() {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        return ctx.getBean("userActionService", UserActionService.class);
    }

	private StationService getStationServiceContext() {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        return ctx.getBean("stationService", StationService.class);
    }
	
//    public void t_stat() {
//        Date yesterday = CommonUtil.getYesterday();
//        getStatisticsServiceContext().t_stat(yesterday, yesterday);
//    }
//
//    public void t_stat_lottery() {
//        Date yesterday = CommonUtil.getYesterday();
//        getStatisticsServiceContext().t_stat_lottery(yesterday, yesterday);
//    }
//
    public void t_user() {
        Date yesterday = CommonUtil.getYesterday();
        getStatisticsServiceContext().t_user(yesterday, yesterday);
    }

    public void t_deal() {
        Date yesterday = CommonUtil.getYesterday();
        getStatisticsServiceContext().t_deal(yesterday, yesterday);
    }
//
//    public void t_stat_sum() {
//        Date yesterday = CommonUtil.getYesterday();
//        getStatisticsServiceContext().t_stat_sum(yesterday, yesterday);
//    }
//
//    public void t_stat_lottery_sum() {
//        Date yesterday = CommonUtil.getYesterday();
//        getStatisticsServiceContext().t_stat_lottery_sum(yesterday, yesterday);
//    }
    
    
	public void t_stat() {
		Date yesterday = CommonUtil.getYesterday();
		getStationServiceContext().stationStatics(yesterday, yesterday);
	}

	public void t_stat_lottery() {
		Date yesterday = CommonUtil.getYesterday();
		getStationServiceContext().stationLotteryStatics(yesterday, yesterday);
	}


	public void t_stat_user() {
		Date yesterday = CommonUtil.getYesterday();
		getStationServiceContext().stationUserStatics(yesterday, yesterday);
	}

	public void t_stat_deal() {
		Date yesterday = CommonUtil.getYesterday();
		getStationServiceContext().stationDealStatics(yesterday, yesterday);
	}

//	public void t_user_action() {
//		Date yesterday = CommonUtil.getYesterday();
//		getUserActionServiceContext().userAction(yesterday,1);
//	}
    
}
