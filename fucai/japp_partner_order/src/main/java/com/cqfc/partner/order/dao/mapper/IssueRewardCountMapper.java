package com.cqfc.partner.order.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.IssueRewardCount;
import com.jami.common.BaseMapper;

public interface IssueRewardCountMapper extends BaseMapper {

	@Insert("replace into t_lottery_issue_reward_count (partnerId,lotteryId,"
			+ "issueNo,smallPrizeNum,bigPrizeNum,smallPrizeMoney,bigPrizeMoney,orderType,createTime) "
			+ "values (#{partnerId},#{lotteryId},#{issueNo},#{smallPrizeNum},#{bigPrizeNum},#{smallPrizeMoney},"
			+ "#{bigPrizeMoney},#{orderType},now())")
	public int insert(IssueRewardCount issueRewardCount);

	
	
	@Select("select sum(smallPrizeNum) as smallPrizeNum,sum(smallPrizeMoney) as smallPrizeMoney,sum(bigPrizeNum) as bigPrizeNum,sum(bigPrizeMoney) as bigPrizeMoney from t_lottery_issue_reward_count where partnerId =#{0} and lotteryId = #{1} and issueNo =#{2} group by partnerId,lotteryId,issueNo")
	public IssueRewardCount getIssueRewardGroup(String partnerId,String lotteryId,String issueNo);
	
	
	@Select("select * from t_lottery_issue_reward_count where partnerId =#{0} and lotteryId = #{1} and issueNo =#{2} and orderType =#{3}")
	public IssueRewardCount getIssueReward(String partnerId,String lotteryId,String issueNo,String orderType);
}
