package com.cqfc.ticketwinning.service;

import java.util.List;

import com.cqfc.protocol.ticketwinning.ReturnData;
import com.cqfc.protocol.ticketwinning.WinningAmountStat;
import com.cqfc.protocol.ticketwinning.WinningAmountStatData;
import com.cqfc.protocol.ticketwinning.WinningNumStat;
import com.cqfc.protocol.ticketwinning.WinningNumStatData;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.model.Winning;


public interface IWinningService {	
	
	/**
	 * 查询投注中奖结果
	 * @param lotteryId
	 * @param issueNo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<Winning> getWinningList(String lotteryId, String issueNo, int currentPage, int pageSize, String tableName);
	
	/**
	 * 查询投注中奖订单
	 * @param winningOrderInfo
	 * @param currentPage
	 * @param pageSize
	 * @param tableName
	 * @return
	 */
	public ReturnData getWinningOrderList(WinningOrderInfo winningOrderInfo, int currentPage, int pageSize, String tableName);
	
	/**
	 * 查询未派奖记录
	 * @param lotteryId
	 * @param issueNo
	 * @param currentPage
	 * @param pageSize
	 * @param tableName
	 * @return
	 */
	public List<Winning> getUnSendPrizeWinningList(String lotteryId, String issueNo, int currentPage, int pageSize, String tableName);
	
	/**
	 * 根据lotteryId和issueNo查询投注中奖结果
	 * @param conditions
	 * @return
	 */
	public List<Winning> getWinningAmountZeroList(String lotteryId, String issueNo, String tableName);
	
	/**
	 * 查询投注中奖结果总数
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int countTotalSize(String lotteryId, String issueNo, String tableName);
	
	/**
	 * 统计中奖金额
	 * @param winningSum
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */	
	public WinningAmountStatData getWinningAmountStat(List<WinningAmountStat> winningAmountStatList, int currentPage, int pageSize, String tableName);
	
	/**
	 * 统计中奖大小奖个数与金额
	 * @param winningNumStatList
	 * @param currentPage
	 * @param pageSize
	 * @param tableName
	 * @return
	 */
	public WinningNumStatData getWinningNumStat(WinningNumStat winningNumStat, int currentPage, int pageSize, String tableName);
	
	/**
	 * 更新中奖订单派奖状态
	 * @param id
	 * @param sendPrizeState
	 * @param tableName
	 * @return
	 */
	public int updateSendPrizeState(long id, int sendPrizeState, String tableName);
	
	/**
	 * 删除中奖结果记录
	 * @param tableName
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int deleteWinningRecordsByLotteryIdAndIssueNo(String lotteryId, String issueNo, String tableName);
	
	/**
	 * 删除竞彩中奖结果记录
	 * @param transferId
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	public int deleteJCWinningRecordsByTransferId(String transferId, String tableName, String jcTableName);
	
	/**
	 * 更新中奖金额
	 * @param winningAmount
	 * @param conditions
	 * @param tableName
	 * @return
	 */
	public int updateWinningAmount(long winningAmount, long winningId, String tableName);
	
	/**
	 * 删除北单中奖纪录
	 * @param wareIssue
	 * @param matchNo
	 * @param matchType
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	public int deleteBDWinningRecordsByWareIssueAndMatchNoAndMatchType(
			String wareIssue, String matchNo, int matchType,
			String tableName, String jcTableName);
}
