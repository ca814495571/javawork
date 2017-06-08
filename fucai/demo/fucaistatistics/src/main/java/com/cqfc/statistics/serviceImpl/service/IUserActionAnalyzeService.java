package com.cqfc.statistics.serviceImpl.service;

import java.io.File;

public interface IUserActionAnalyzeService {

	
	/**
	 * 分析20150115签到活动数据入库
	 */
	public void statistics_0115();
	
	
	/**
	 * 分析至今用户使用真实现金购买彩票的总次数,总金额以及各个彩种的比例
	 */
	public void statics_1_19();
	
	/**
	 * 投票活动手机机型比例,数量统计
	 */
	public void activityTpPhoneAnalyze();
	
	
	/**
	 * 分析CGI日志
	 * 
	 * @param file
	 */
	public void readTpLog(File file) ;
}
