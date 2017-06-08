package com.cqfc.useraccount.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cqfc.useraccount.service.IUserAccountService;
import com.cqfc.util.SwitchUtil;

@Component
public class HandselStateUpdateTask {

	@Autowired
	private IUserAccountService userAccountService;
	
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void execute() {
		if(!SwitchUtil.timerIsOpen()){
			return;
		}
		userAccountService.updateHandselState();
	}
}
