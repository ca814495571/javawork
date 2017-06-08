package com.cqfc.statistics.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.serviceImpl.service.IUserActionService;

@EnableScheduling
@Component
public class CleanUserActionTemp {

	
	@Autowired
	IUserActionService userActionService; 
	
	
	@Scheduled(cron="30 0 3 * * ?" )
	public void cleanUserActionTemp(){
		
		userActionService.cleanUserActionTemp(CommonUtil.getOffsetMonth(-6));
		
	}

}
