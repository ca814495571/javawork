package com.cqfc.ticketwinning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.ticketwinning.WinningAmountStat;
import com.cqfc.protocol.ticketwinning.WinningNumStat;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.model.Winning;
import com.jami.common.BaseMapper;

public interface WinningMapper extends BaseMapper {
	
	/**
	 * 查询投注中奖结果	
	 * @param conditions
	 * @return
	 */
	@Select("select * from  ${tableName} where ${conditions}")
	public List<Winning> getWinningList(@Param("conditions")String conditions, @Param("tableName") String tableName);
	
	/**
	 * 查询中奖订单信息
	 * @param conditions
	 * @return
	 */
	@Select("select * from  ${tableName} where ${conditions}")
	public List<WinningOrderInfo> getWinningOrderList(@Param("conditions")String conditions, @Param("tableName") String tableName);
	
	/**
	 * 查询投注中奖结果总数
	 * @return
	 */
	@Select("select count(*) from  ${tableName} where ${conditions}")
	public int countTotalSize(@Param("conditions")String conditions, @Param("tableName") String tableName);
	
	/**
	 * 更新派奖状态
	 * @return
	 */
	@Update("update ${tableName} set sendPrizeState = #{sendPrizeState} where ${conditions}")
	public int updateSendPrizeState(@Param("sendPrizeState")long sendPrizeState, @Param("conditions")String conditions, @Param("tableName") String tableName);
	
	/**
	 * 统计中奖金额
	 * @param conditions
	 * @return
	 */
	@Select("select lotteryId, issueNo, partnerId, sum(case when winningAmount > 0 then winningAmount * multiple else  0 end) as sum from  ${tableName} where ${conditions}")
	public List<WinningAmountStat> getWinningAmountStat(@Param("conditions")String conditions, @Param("tableName") String tableName);
	
	/**
	 * 统计中奖大小奖个数与金额
	 * @param conditions
	 * @return
	 */
	@Select("select lotteryId, issueNo, partnerId, orderType, sum(case when winningAmount * multiple > ${bigPrizeAmount} then winningAmount * multiple else 0 end) as bigPrizeMoney, sum(case when winningAmount * multiple > ${bigPrizeAmount} then 1 else 0 end) as bigPrizeNum, sum(case when winningAmount * multiple <= ${bigPrizeAmount} then winningAmount * multiple else 0 end) as smallPrizeMoney, sum(case when winningAmount * multiple <= ${bigPrizeAmount} then 1 else 0 end) as smallPrizeNum from  ${tableName} where ${conditions}")
	public List<WinningNumStat> getWinningNumStat(@Param("conditions")String conditions, @Param("bigPrizeAmount") long bigPrizeAmount, @Param("tableName") String tableName);
	
	/**
	 * 删除中奖结果记录
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	@Delete("delete from ${tableName} where lotteryId = #{lotteryId} and issueNo = #{issueNo}")
	public int deleteWinningRecordsByLotteryIdAndIssueNo(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo, @Param("tableName") String tableName);
	
	
	/**
	 * 删除竞彩中奖结果记录
	 * @param transferId
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	@Delete("delete from ${tableName} where orderNo in (select orderNo from ${jcTableName} where transferId = #{transferId})")
	public int deleteJCWinningRecordsByorderNo(@Param("transferId") String transferId, @Param("tableName") String tableName, @Param("jcTableName") String jcTableName);
	
	
	/**
	 * 查询投注中奖结果	
	 * @param conditions
	 * @return
	 */
	@Select("select * from  ${tableName} where ${conditions}")
	public List<Winning> findWinningAmountZeroList(@Param("conditions")String conditions, @Param("tableName") String tableName);
	
	/**
	 * 更新中奖金额
	 * @return
	 */
	@Update("update ${tableName} set winningAmount = ${winningAmount} where ${conditions}")
	public int updateWinningAmount(@Param("winningAmount")long winningAmount, @Param("conditions")String conditions, @Param("tableName") String tableName);

	@Select("select sum(winningAmount * multiple) from  ${tableName} where lotteryId = #{lotteryId} and issueNo = #{issueNo}")
	public Long getTotalWinningMoneyByGame(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo, @Param("tableName") String tableName);
	
	@Select("select sum(winningAmount * multiple) from  ${tableName} where createTime >= #{startTime} and createTime <= #{endTime}")
	public Long getTotalWinningMoneyByDay(@Param("startTime") String start, @Param("endTime") String end, @Param("tableName") String tableName);

	@Delete("delete from ${tableName} where orderNo in (select orderNo from ${jcTableName} where issueNo = #{issueNo} and matchNo = #{matchNo} and matchType = #{matchType})")
	public int deleteBDWinningRecordsByorderNo(@Param("issueNo") String issueNo, @Param("matchNo") String matchNo, @Param("matchType") int matchType, @Param("tableName") String tableName, @Param("jcTableName") String jcTableName);
}
