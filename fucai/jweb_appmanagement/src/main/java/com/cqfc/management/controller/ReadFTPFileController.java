package com.cqfc.management.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cqfc.management.service.IReadFTPFileService;
import com.cqfc.management.util.dateUtils.DateUtils;

@Controller
@RequestMapping("/ftp")
public class ReadFTPFileController {

	@Autowired
	private IReadFTPFileService readFTPService;

	@RequestMapping("/insert/userInfo")
	public void insertUserInfo() {

	//	readFTPService.insertUserInfo("d:/userinfo2014052920.txt");

	}

	@RequestMapping("/insert/planInfo")
	public void insertLotteryPlan() {

	//	readFTPService.insertPlanInfo("d:/planinfo2014052920.txt");

	}

	@RequestMapping("/analyze/userInfo")
	public void analyzeUserInfo() throws ParseException {

		readFTPService.analyzeUserInfo(DateUtils.stringToDateTwo("2014-5-11"));

	}

	@RequestMapping("/analyze/planInfo")
	public void analyzePlanInfo() throws ParseException {

		readFTPService.analyzePlanInfo(DateUtils.stringToDateTwo("2014-2-28"));

	}
	
	
	@RequestMapping("/transfer")
	public void ftpDateTransfer(){
		
		readFTPService.ftpDateTransfer();
	}
	
	
	
	
}
