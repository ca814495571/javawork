package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.LotteryPlan;

public interface LotteryPlanMapper extends BaseMapper {

	@Insert("insert into t_lottery_plan (userId ,planId,lotteryId,lotteryName,totalAmount,createTime,charOne,charTwo,extInfo) "
			+ "values (#{userId},#{planId},#{lotteryId},#{lotteryName},#{totalAmount},#{createTime},#{charOne},#{charTwo},#{extInfo})")
	public int insert(LotteryPlan lotteryPlan);

	@Select("select * from t_lottery_plan ")
	public List<LotteryPlan> getAllLotteryPlans();

	@Select("select ifnull(sum(totalAmount),0) from t_lottery_plan where "
			+ "charOne = #{0} and createTime= #{1} and lotteryName = #{2}")
	public int getLotterySaleByType(String enterTag, String Date,
			String lotteyType);

	@Select("select * from t_lottery_plan where planId = #{planId}")
	public List<LotteryPlan> getByPlanId(LotteryPlan lotteryPlan);
}
