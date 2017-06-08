package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserCount;

public interface UserCountMapper extends BaseMapper {
	/*
	 * private int userTotalNum ; private int userDailyAddNum ; private String
	 * countTime ; private int year ; private int month ; private int stationId
	 * ; private String lastTime;
	 */
	/**
	 * 
	 * @param userCount
	 * @return
	 */
	@Insert("insert into t_user_count "
			+ "(userTotalNum,userDailyAddNum,countTime,year,month,stationId,lastTime,enterTag) "
			+ "values (#{userTotalNum},#{userDailyAddNum},now(),#{year},#{month},"
			+ "#{stationId},#{lastTime},#{enterTag})")
	public int insertUserCount(UserCount userCount);


	@Select("select *  from t_user_count where ${where}")
	public List<UserCount> getUserCountByWhereAnd(@Param("where") String where);
	
	@Select("select *  from  t_user_count t where stationId = #{param1.id} and countTime between #{1} and #{2} ")
	public List<UserCount> getUserCountByTime(
			StationInfo stationInfo,  String fromDate ,String toDate);
	
	@Select("select *  from  t_user_count t where stationId = #{param1.id} and countTime between #{1} and #{2} limit #{3},#{4} ")
	public List<UserCount> getUserCountByDate(
			StationInfo stationInfo,  String fromDate ,String toDate ,int preNum , int pageSize);


	@Select("select count(*)  from  t_user_count t where stationId = #{param1.id} and countTime between #{1} and #{2}  ")
	public int getRecordTotal(
			StationInfo stationInfo,  String fromDate,String toDate );




}
