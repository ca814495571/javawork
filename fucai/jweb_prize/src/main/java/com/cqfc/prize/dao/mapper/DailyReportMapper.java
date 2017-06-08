package com.cqfc.prize.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.prize.model.DailyReport;

public interface DailyReportMapper extends BaseMapper{

	
	@Insert("replace into t_daily_report (status,totalAmount,totalMoney,countTime,createTime) values (#{status},#{totalAmount},#{totalMoney},#{countTime},now())")
	public int insert(DailyReport dailyReport);
	
	@Select("select * from t_daily_report where countTime=#{countTime}")
	public List<DailyReport> getDailyReport(@Param("countTime")String countTime);

}
