package com.cqfc.partner.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.DailyAwardCount;
import com.jami.common.BaseMapper;

public interface DailyAwardCountMapper extends BaseMapper {

	@Insert("replace into t_partner_daily_award_count (lotteryId,partnerId,"
			+ "awardPrizeMoney,countTime,createTime) "
			+ "values (#{lotteryId},#{partnerId},#{awardPrizeMoney},"
			+ "#{countTime},now())")
	public int insert(DailyAwardCount dailyAwardCount);

	@Select("select partnerId,lotteryId,sum(winningAmount*multiple) as awardPrizeMoney  from  ${tableName}  where  ${where} ")
	public List<DailyAwardCount> getDailyAwardByCountTime(
			@Param("where") String countTime,
			@Param("tableName") String tableName);

	@Select("select sum(awardPrizeMoney) from t_partner_daily_award_count where countTime=#{day}")
	public Long getTotalAwardPrize(@Param("day") String day);


	@Select("select count(1)  from  t_partner_daily_award_count  where ${where} ")
	public int  getDailyAwardByWhereSum(@Param("where") String where);
	
	@Select("select *  from  t_partner_daily_award_count  where ${where} ")
	public List<DailyAwardCount>  getDailyAwardByWhere(@Param("where") String where);
}
