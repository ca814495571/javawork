package com.cqfc.partner.order.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.partnerorder.IssueRiskCount;
import com.cqfc.protocol.partnerorder.IssueSaleCount;
import com.jami.common.BaseMapper;

public interface IssueSaleCountMapper extends BaseMapper {

	@Insert("replace into t_lottery_issue_sale_count (issueNo,lotteryId,partnerId,"
			+ "sucNum,sucMoney,failNum,failMoney,orderType,createTime) "
			+ "values (#{issueNo},#{lotteryId},#{partnerId},#{sucNum},#{sucMoney},"
			+ "#{failNum},#{failMoney},#{orderType},now())")
	public int insert(IssueSaleCount issueSaleCount);

	@Select("select partnerId,lotteryId,issueNo,orderType,sum(case when orderStatus in (4,6,7,8,12) then 1 else 0 end) as sucNum,"
			+ " sum(case when orderStatus in (4,6,7,8,12) then totalAmount else 0 end) as sucMoney,sum(case when orderStatus in (10,11) then 1 else 0 end) as failNum,"
			+ " sum(case when orderStatus in (10,11) then totalAmount else 0 end) as failMoney  from  ${tableName} "
			+ " where lotteryId=#{lotteryId} and issueNo= #{issueNo} group by partnerId,lotteryId,issueNo,orderType ")
	public List<IssueSaleCount> getIssueSaleCount(
			@Param("lotteryId") String lotteryId,
			@Param("issueNo") String issueNo,
			@Param("tableName") String tableName);

	@Select("select lotteryId,issueNo,sum(sucNum) as successNum, sum(sucMoney) as successMoney, sum(failNum) as failNum, sum(failMoney) as failMoney from t_lottery_issue_sale_count "
			+ "where lotteryId=#{lotteryId} and issueNo= #{issueNo} group by lotteryId,issueNo")
	public IssueRiskCount getIssueRiskCount(
			@Param("lotteryId") String lotteryId,
			@Param("issueNo") String issueNo);

}
