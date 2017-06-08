package com.cqfc.partner.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.jami.common.BaseMapper;

public interface DailyReportCountMapper extends BaseMapper {

	@Insert("replace into t_partner_daily_report_count (partnerId,lotteryId,totalAmount,awardAmount,afterAmount,"
			+ "countTime,createTime) "
			+ "values (#{partnerId},#{lotteryId},#{totalMoney},#{awardPrizeMoney},#{chargeTotalMoney},#{countTime},now())")
	public int insert(DailySaleAndCharge dailySaleAndCharge);

	@Select("select count(*) from t_partner_daily_report_count where ${where}")
	public int getDailyReportByWhereSum(@Param("where") String where);
	
	@Select("select partnerId,lotteryId,countTime,totalAmount as totalMoney,awardAmount as awardPrizeMoney,afterAmount as chargeTotalMoney from t_partner_daily_report_count where ${where}")
	public List<DailySaleAndCharge> getDailyReportByWhere(@Param("where") String where);

}
