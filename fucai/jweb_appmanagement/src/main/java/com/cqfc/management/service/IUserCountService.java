package com.cqfc.management.service;

import java.util.Date;
import java.util.List;

import com.cqfc.management.model.UserCount;

public interface IUserCountService {
	/**
	 * 插入用户统计数据
	 * @param userCount
	 * @return
	 */
	public int insertUserCount(UserCount userCount);
	
	/**
	 * @param userCount
	 * @return
	 */
	public List<UserCount> getUserCountByWhereAnd(UserCount userCount);
	
	
	/**
	 * 获取前一天的数据
	 * @param userCount
	 * @param date
	 * @return
	 */
	public List<UserCount> getpreDayUserCount(UserCount userCount , Date date) ;

	
	
	
	public int getTotalNum(int stationId , String date);
	
	
	public int deleteAll(String date);
}
