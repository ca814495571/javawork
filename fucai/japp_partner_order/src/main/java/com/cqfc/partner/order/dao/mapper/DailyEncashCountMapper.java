package com.cqfc.partner.order.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.DailyEncashCount;
import com.jami.common.BaseMapper;

public interface DailyEncashCountMapper extends BaseMapper {

	@Insert("replace into t_partner_daily_encash_count (partnerId,encashTotalMoney,"
			+ "countTime,createTime) "
			+ "values (#{partnerId},#{encashTotalMoney},#{countTime},now())")
	public int insert(DailyEncashCount dailyChargeCount);

	@Select("select sum(encashTotalMoney) from t_partner_daily_encash_count where countTime=#{day}")
	public Long getTotalWithdraw(@Param("day") String day);

}
