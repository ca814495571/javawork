package com.cqfc.ticketwinning.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqfc.protocol.ticketwinning.ReturnData;
import com.cqfc.protocol.ticketwinning.WinningAmountStat;
import com.cqfc.protocol.ticketwinning.WinningAmountStatData;
import com.cqfc.protocol.ticketwinning.WinningNumStat;
import com.cqfc.protocol.ticketwinning.WinningNumStatData;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.dao.WinningDao;
import com.cqfc.ticketwinning.model.Winning;
import com.cqfc.ticketwinning.service.IWinningService;

@Service
public class WinningServiceImpl implements IWinningService{
	@Resource
	private WinningDao winningDao;
	
	/**
	 * 查询投注中奖结果
	 * @param conditions
	 * @return
	 */
	@Override
	public List<Winning> getWinningList(String lotteryId, String issueNo, int currentPage, int pageSize, String tableName){
		return winningDao.getWinningList(lotteryId, issueNo, currentPage, pageSize, tableName);
	}
	
	/**
	 * 查询投注中奖订单
	 * @param conditions
	 * @return
	 */
	@Override
	public ReturnData getWinningOrderList(WinningOrderInfo winningOrderInfo, int currentPage, int pageSize, String tableName){
		return winningDao.getWinningOrderList(winningOrderInfo, currentPage, pageSize, tableName);
	}
	
	/**
	 * 查询未派奖记录
	 * @param conditions
	 * @return
	 */
	@Override
	public List<Winning> getUnSendPrizeWinningList(String lotteryId, String issueNo, int currentPage, int pageSize, String tableName){
		return winningDao.getUnSendPrizeWinningList(lotteryId, issueNo, currentPage, pageSize, tableName);
	}
	
	/**
	 * 根据lotteryId和issueNo查询投注中奖结果
	 * @param conditions
	 * @return
	 */
	@Override
	public List<Winning> getWinningAmountZeroList(String lotteryId, String issueNo, String tableName){
		return winningDao.getWinningAmountZeroList(lotteryId, issueNo, tableName);
	}
	
	/**
	 * 查询投注中奖结果总数
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	@Override
	public int countTotalSize(String lotteryId, String issueNo, String tableName){		
		return winningDao.countTotalSize(lotteryId, issueNo, tableName);
	}
	
	/**
	 * 统计中奖金额
	 * @param winningSum
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */	
	@Override
	public WinningAmountStatData getWinningAmountStat(List<WinningAmountStat> winningAmountStatList, int currentPage, int pageSize, String tableName){
		return winningDao.getWinningAmountStat(winningAmountStatList, currentPage, pageSize, tableName);
	}
	
	/**
	 * 统计中奖大小奖个数与金额
	 * @param winningNumStatList
	 * @param currentPage
	 * @param pageSize
	 * @param tableName
	 * @return
	 */
	@Override
	public WinningNumStatData getWinningNumStat(WinningNumStat winningNumStat, int currentPage, int pageSize, String tableName){
		return winningDao.getWinningNumStat(winningNumStat, currentPage, pageSize, tableName);
	}
	
	/**
	 * 更新中奖订单派奖状态
	 * @param id
	 * @param sendPrizeState
	 * @param tableName
	 * @return
	 */
	@Override
	public int updateSendPrizeState(long id, int sendPrizeState, String tableName){
		return winningDao.updateSendPrizeState(id, sendPrizeState, tableName);
	}
	
	/**
	 * 删除中奖结果记录
	 * @param tableName
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@Override
	public int deleteWinningRecordsByLotteryIdAndIssueNo(String lotteryId, String issueNo, String tableName){
		return winningDao.deleteWinningRecordsByLotteryIdAndIssueNo(lotteryId, issueNo, tableName);
	}
	
	/**
	 * 删除竞彩中奖结果记录
	 * @param transferId
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	@Override
	public int deleteJCWinningRecordsByTransferId(String transferId, String tableName, String jcTableName){
		return winningDao.deleteJCWinningRecordsByTransferId(transferId, tableName, jcTableName);
	}
	
	/**
	 * 更新中奖金额
	 * @param winningAmount
	 * @param conditions
	 * @param tableName
	 * @return
	 */
	@Override
	public int updateWinningAmount(long winningAmount, long winningId, String tableName){
		return winningDao.updateWinningAmount(winningAmount, winningId, tableName);
	}
	
	public long getTotalWinningMoneyByGame(String lotteryId, String issueNo) {
		return winningDao.getTotalWinningMoneyByGame(lotteryId, issueNo);
	}

	public long getTotalWinningMoneyByDay(String start, String end) {
		return winningDao.getTotalWinningMoneyByDay(start, end);
	}
	
	/**
	 * 删除北单中奖纪录
	 * @param wareIssue
	 * @param matchNo
	 * @param matchType
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	@Override
	public int deleteBDWinningRecordsByWareIssueAndMatchNoAndMatchType(
			String wareIssue, String matchNo, int matchType,
			String tableName, String jcTableName) {
		return winningDao.deleteBDWinningRecordsByWareIssueAndMatchNoAndMatchType(wareIssue, matchNo, matchType, tableName, jcTableName);
	}
}
