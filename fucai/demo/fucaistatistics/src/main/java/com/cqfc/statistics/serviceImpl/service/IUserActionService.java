package com.cqfc.statistics.serviceImpl.service;

import java.util.Date;

public interface IUserActionService {
		
	
	
	
	/**
	 * 清理用户购彩订单,充值..半年前的数据
	 * @param time
	 */
	public void cleanUserActionTemp(String time);
	
	/**
	 * 手动添加用户操作行为的数据
	 * @param actionName
	 * @param dateFrom
	 * @param dateTo
	 */
	public void  userActionHistory(String actionName,Date dateFrom,Date dateTo);
}
