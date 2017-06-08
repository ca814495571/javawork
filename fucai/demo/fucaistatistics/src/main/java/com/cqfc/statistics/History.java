package com.cqfc.statistics;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cqfc.statistics.common.ApplicationContextProvider;
import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.serviceImpl.StationService;
import com.cqfc.statistics.serviceImpl.StatisticsService;
import com.cqfc.statistics.serviceImpl.UserActionService;

/**
 * @author: giantspider@126.com
 */
@Component
public class History {

    private static final Logger logger = LoggerFactory.getLogger(History.class);

    private static Date startDate;
    private static Date endDate;
    private static boolean overwrite = false;
    static {
        Properties p = CommonUtil.getProperties("statistics.properties");
        startDate = CommonUtil.parseStatisticsTime(p.getProperty("startDate"));
        endDate = CommonUtil.parseStatisticsTime(p.getProperty("endDate"));
        overwrite = Boolean.valueOf(p.getProperty("overwrite"));
    }


    public static boolean isOverwrite() {
		return overwrite;
	}

	public static void setOverwrite(boolean overwrite) {
		History.overwrite = overwrite;
	}

	private StationService getStationServiceContext() {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        return ctx.getBean("stationService", StationService.class);
    }
	
	private UserActionService getUserActionServiceContext() {
        ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
        return ctx.getBean("userActionService", UserActionService.class);
    }
	
	private StatisticsService getStatisticsServiceContext() {
		ApplicationContext ctx = ApplicationContextProvider
				.getApplicationContext();
		return ctx.getBean("statisticsService", StatisticsService.class);
	}

    public void t_stat() {
    	
    	if(endDate == null){
    		endDate = new Date();
    	}
        getStationServiceContext().stationStatics(startDate, endDate);
    }

    public void t_stat_lottery() {
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getStationServiceContext().stationLotteryStatics(startDate, endDate);
    }

    public void t_country() {
    	
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getStationServiceContext().countryStatics(startDate, endDate);
    }

    public void t_country_lottery() {
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getStationServiceContext().countryLotteryStatics(startDate, endDate);
    }
    public void t_stat_user() {
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getStationServiceContext().stationUserStatics(startDate, endDate);
    }
    
    
    public void t_user() {
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getStatisticsServiceContext().t_user(startDate, endDate);
    }
    
    public void t_stat_deal() {
    	if(endDate == null){
    		endDate = new Date();
    	}
        getStationServiceContext().stationDealStatics(startDate, endDate);
    }


    
    /**
     * 用户购彩
     */
    public void t_user_buy(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userBuy",startDate, endDate);
    }
    
    
    /**
     * 用户充值
     */
    public void t_user_recharge(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userRecharge", startDate,endDate);
    }
    
    
    /**
     * 用户支付
     */
    public void t_user_pay(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userPay",startDate, endDate);
    }
    
    /**
     * 用户基本信息
     */
    public void t_user_info(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userInfo",startDate, endDate);
    }
    
    /**
     * 用户账户金额
     */
    public void t_user_finance(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userFinance",startDate, endDate);
    }
    
    /**
     * 用户账号
     */
    public void t_user_account(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userAccount",startDate, endDate);
    }
    
    /**
     * 活动
     */
    public void t_activity(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory("userBindStat",startDate, endDate);
    }
    
    /**
     * 用户所有配置的操作
     */
    public void t_user_action(){
    	if(endDate == null){
    		endDate = new Date();
    	}
    	getUserActionServiceContext().userActionHistory(startDate, endDate);
    }
    
    
    
    
    public static void main(String[] args) {
       
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/history.spring.xml", History.class);
        History history = applicationContext.getBean("history", History.class);
     
        try{
    	  if (args.length==0) {
    		  history.t_stat_user();
          } else if (args.length==1) {                //multiple methods seperated by comma
              List<String> targetList = Arrays.asList(args[0].split(","));
              if (targetList.contains("t_stat"))                history.t_stat();
              if (targetList.contains("t_stat_lottery"))        history.t_stat_lottery();
              if (targetList.contains("t_stat_user"))            history.t_stat_user();
              if (targetList.contains("t_stat_deal"))    history.t_stat_deal();
              if (targetList.contains("t_country"))            history.t_country();
              if (targetList.contains("t_country_lottery"))    history.t_country_lottery();
              if (targetList.contains("t_user_buy"))      history.t_user_buy();
              if (targetList.contains("t_user_recharge"))      history.t_user_recharge();
              if (targetList.contains("t_user_pay"))      history.t_user_pay();
              if (targetList.contains("t_user_info"))  history.t_user_info();
              if (targetList.contains("t_user_account"))  history.t_user_account();
              if (targetList.contains("t_user_finance"))   history.t_user_finance();
              if (targetList.contains("t_user_action")) history.t_user_action();
              if (targetList.contains("t_activity")) history.t_activity();
          }
	} catch (Exception e) {
		logger.error(e.toString());
	}
    }
}
