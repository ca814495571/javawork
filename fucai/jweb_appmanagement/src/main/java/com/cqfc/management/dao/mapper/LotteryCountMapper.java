package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.StationInfo;

public interface LotteryCountMapper extends BaseMapper {
	/*
	 * private String lotteryType ; private int lotteryDailyNum ; private int
	 * lotteryMonthNum ; private String countTime ; private int year; private
	 * int month ; private int stationId ; private String lastTime ;
	 */
	/**
	 * 
	 * @param LotteryCount
	 * @return
	 */
	@Insert("insert into t_lottery_count "
			+ "(lotteryType,lotteryDailyNum,lotteryMonthNum,countTime,year,month,stationId ,enterTag)"
			+ "values (#{lotteryType},#{lotteryDailyNum},#{lotteryMonthNum},#{countTime},#{year},"
			+ "#{month},#{stationId},#{enterTag})")
	public int insertLotteryCount(LotteryCount lotteryCount);


	@Select("select *  from t_lottery_count where ${where}")
	public List<LotteryCount> getLotteryCountByWhereAnd(
			@Param("where") String where);
			
			
//	@Select("select year ,month,sum(lotteryDailyNum) as saleNum ,lotteryType   from  t_lottery_count t where stationId = #{param1.id} and year = #{1}  group by month,lotteryType")
	
	@Select("select *  from  t_lottery_count t where stationId = #{param1.id} and countTime between #{1} and #{2} ")
	public List<LotteryCount> getLotteryCountByTime(
			StationInfo stationInfo,  String fromDate ,String toDate);
	
	@Select("select *  from  t_lottery_count t where stationId = #{param1.id} and countTime between #{1} and #{2} limit #{3},#{4}")
	public List<LotteryCount> getLotteryCountByDate(
			StationInfo stationInfo,  String fromDate ,String toDate ,int preSize ,int pageSize);
	
	@Select("select ifnull(count(*),0) from  t_lottery_count t where stationId = #{param1.id} and countTime between #{1} and #{2} ")
	public int getRecordTotal(StationInfo stationInfo,  String fromDate ,String toDate);
	
	@Select("select ifnull(sum(lotteryDailyNum),0) from t_lottery_count where  stationId = #{param1.id} and lotteryType= #{1} and countTime between #{2} and #{3} ")
	public int getTotalSaleNum(StationInfo stationInfo,String lotteryType, String fromDate ,String toDate);

	@Delete("delete from t_lottery_count where countTime = #{0}")
	public int deleteAll(String date);

}

