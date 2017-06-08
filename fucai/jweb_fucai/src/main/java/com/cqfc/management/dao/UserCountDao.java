package com.cqfc.management.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.UserCountMapper;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserCount;

@Repository
public class UserCountDao {

	@Autowired
	private UserCountMapper userCountMapper;

	/**
	 * 插入记录
	 * 
	 * @param UserCount
	 * @return
	 */
	public int insertUserCount(UserCount userCount) {

		filterNullColumn(userCount);
		return userCountMapper.insertUserCount(userCount);
	}

	/**
	 * 查询语句（条件格式为 ：1=1 and fieldName1 = value1 and fieldName2 = value2 .....）
	 * 
	 * @param userCount
	 * @return
	 */
	public List<UserCount> getUserCountByWhereAnd(UserCount userCount) {

		List<UserCount> userCounts = new ArrayList<UserCount>();
		filterNullColumn(userCount);

		StringBuffer where = new StringBuffer(" 1=1");

		if (!"".equals(userCount.getUserTotalNum())
				&& userCount.getUserTotalNum() != 0) {
			where.append(" and userTotalNum = '");
			where.append(userCount.getUserTotalNum());
			where.append("'");
		}
		if (!"".equals(userCount.getUserDailyAddNum())
				&& userCount.getUserDailyAddNum() != 0) {
			where.append(" and userDailyAddNum = '");
			where.append(userCount.getUserDailyAddNum());
			where.append("'");
		}
		if (!"".equals(userCount.getCountTime())
				&& userCount.getCountTime() != null
				&& !"0000-00-00".equals(userCount.getCountTime())) {
			where.append(" and countTime = '");
			where.append(userCount.getCountTime());
			where.append("'");
		}
		if (!"".equals(userCount.getYear()) && userCount.getYear() != 0) {
			where.append(" and year = '");
			where.append(userCount.getYear());
			where.append("'");
		}
		if (!"".equals(userCount.getMonth()) && userCount.getMonth() != 0) {
			where.append(" and month = '");
			where.append(userCount.getMonth());
			where.append("'");
		}
		if (!"".equals(userCount.getStationId())
				&& userCount.getStationId() != 0) {
			where.append(" and stationId = '");
			where.append(userCount.getStationId());
			where.append("'");
		}

		System.out.println(where.toString());
		userCounts = userCountMapper.getUserCountByWhereAnd(where.toString());
		// return where.toString();
		return userCounts;
	}

	/**
	 * 过滤掉null值的字段设置默认值
	 * 
	 * @param UserCount
	 */
	public void filterNullColumn(UserCount UserCount) {
		if (UserCount.getCountTime() == null) {
			UserCount.setCountTime("");
		}

	}
	
	
	public List<UserCount> getUserCountByDate(
			StationInfo stationInfo, String fromDate ,String toDate) {

		return userCountMapper.getUserCountByTime(stationInfo, fromDate,toDate);
	}
	
	public List<UserCount> getUserCountByDate(
			StationInfo stationInfo, String fromDate ,String toDate , int pageNum,int pageSize) {

		return userCountMapper.getUserCountByDate(stationInfo, fromDate,toDate,pageSize*(pageNum-1),pageSize);
	}
	
	public int getRecordTotal(StationInfo stationInfo, String fromDate ,String toDate){
		
		return userCountMapper.getRecordTotal(stationInfo, fromDate, toDate);
		
	}
}
