package com.cqfc.partner.order.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.DailyChargeCount;
import com.jami.common.BaseMapper;

public interface DailyChargeCountMapper extends BaseMapper {

	@Insert("replace into t_partner_daily_charge_count (partnerId,chargeTotalMoney,"
			+ "countTime,createTime) "
			+ "values (#{partnerId},#{chargeTotalMoney},#{countTime},now())")
	public int insert(DailyChargeCount dailyChargeCount);

	@Select("select sum(chargeTotalMoney) from t_partner_daily_charge_count where countTime=#{day}")
	public Long getTotalRecharge(@Param("day") String day);

}
