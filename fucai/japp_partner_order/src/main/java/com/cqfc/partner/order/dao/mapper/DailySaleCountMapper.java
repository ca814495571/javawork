package com.cqfc.partner.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.DailySaleCount;
import com.jami.common.BaseMapper;

public interface DailySaleCountMapper extends BaseMapper {

	@Insert("replace into t_partner_daily_sale_count (lotteryId,partnerId,"
			+ "totalMoney,countTime,createTime) "
			+ "values (#{lotteryId},#{partnerId},#{totalMoney},"
			+ "#{countTime},now())")
	public int insert(DailySaleCount dailySaleCount);


	@Select("select partnerId,lotteryId,sum(case when orderStatus in (4,6,7,8,12) then totalAmount  else 0 end) as totalMoney  from  ${tableName}  where ${where} ")
	public List<DailySaleCount>  getDailySaleByCountTime(@Param("where") String countTime,@Param("tableName")String tableName);

	@Select("select sum(totalMoney) from t_partner_daily_sale_count  where countTime=#{day}")
	public Long getTotalSale(@Param("day") String day);
	
	
	@Select("select count(*)  from  t_partner_daily_sale_count t1 where ${where} ")
	public int  getDailySaleByWhereSum(@Param("where") String where);
	
	
	@Select("select t1.partnerId,t1.lotteryId,t1.countTime,t1.totalMoney,t2.awardPrizeMoney from t_partner_daily_sale_count t1 left join t_partner_daily_award_count t2 on t1.partnerId = t2.partnerId and t1.lotteryId = t2.lotteryId and t1.countTime = t2.countTime where ${where} ")
	public List<DailySaleAndCharge>  getDailySaleByWhere(@Param("where") String where);
	

}
