package com.cqfc.prize.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.prize.model.DailyReport;

public interface TempPrizeMapper extends BaseMapper{

	
	@Delete("delete from t_temp_prize where createTime<#{time} limit 2000")
	public int deleteTempPrize(@Param("time")String time);
	
	
	@Select("select status,count(*) as totalAmount,sum(winAmount) as totalMoney  from t_temp_prize ${where} group by status")
	public List<DailyReport> dailyReportCount(@Param("where")String where);
	
	
	
}
