package com.cqfc.management.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.UserCountDao;
import com.cqfc.management.model.UserCount;
import com.cqfc.management.service.IUserCountService;
import com.cqfc.management.util.dateUtils.DateUtils;

@Service
public class UserCountServiceImpl implements IUserCountService {

	@Autowired
	private UserCountDao userCountDao;

	@Override
	public int insertUserCount(UserCount userCount) {

		return userCountDao.insertUserCount(userCount);
	}

	@Override
	public List<UserCount> getUserCountByWhereAnd(UserCount userCount) {
		return userCountDao.getUserCountByWhereAnd(userCount);
	}

	@Override
	public List<UserCount> getpreDayUserCount(UserCount userCount ,Date date) {
		 
		UserCount uc = new UserCount();
		uc.setStationId(userCount.getStationId());
		uc.setCountTime(DateUtils.getPreDay(date));
		return getUserCountByWhereAnd(uc);
	}

	@Override
	public int getTotalNum(int stationId , String date){
		
		return userCountDao.getTotalNum(stationId, date);
		
	}

	@Override
	public int deleteAll(String date) {
		return userCountDao.deleteAll(date);
	}
	
}
