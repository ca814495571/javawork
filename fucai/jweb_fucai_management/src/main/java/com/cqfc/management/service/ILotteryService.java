package com.cqfc.management.service;

import com.cqfc.management.model.CancelOrderInfo;
import com.cqfc.management.model.Issue;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.WinPrizeCheck;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;

public interface ILotteryService {

	
	
	/**
	 * 根据id修改彩种期号的状态
	 * @param issueId
	 * @param status
	 * @return
	 */
	PcResultObj updateIssueStatus(int issueId , int status);
	
	
	
	/**
	 * 根据条件查询中奖总金额（中将审核查询）
	 * @return
	 */
	PcResultObj getWinningCheck(WinPrizeCheck winPrizeCheck,int pageNum,int pageSize);
	
	/**
	 * 根据条件查询中奖总金额（派奖审核查询）
	 * @return
	 */
	PcResultObj getAwardCheck(WinPrizeCheck winPrizeCheck,int pageNum,int pageSize);
	
	
	/**
	 * 审核中奖(修改期号表状态)
	 * @return
	 */
	PcResultObj checkWinPrize(WinPrizeCheck winPrizeCheck);
	
	
	/**
	 * 审核派奖(修改期号表状态)
	 * @return
	 */
	PcResultObj checkAwardPrize(WinPrizeCheck winPrizeCheck);
	
	
	/**
	 * 根据条件查询订单信息
	 * @return
	 */
	PcResultObj getOrderByWhere(OrderInfo orderInfo,int pageNum ,int pageSize);
	
	/**
	 * 根据条件查询中奖订单信息
	 * @return
	 */
	PcResultObj getWinningOrderByWhere(WinningOrderInfo winningOrderInfo, int pageNum, int pageSize);
	
	
	/**
	 * 根据条件查询彩种期号
	 * @return
	 */
	PcResultObj getLotteryIssue(Issue issue,int pageNum,int pageSize);
	
	/**
	 * 根据彩种期号查询详情
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	PcResultObj findLotteryIssue(String lotteryId,String issueNo);
	/**
	 * 删除期号
	 * @param orderInfo
	 * @return
	 */
	PcResultObj deleteLotteryIssue(Issue issue);
	
	/**
	 * 修改彩种期号
	 * @param issue
	 * @return
	 */
	PcResultObj updateLotteryIssue(Issue issue);
	
	/**
	 * 设置自动审核中奖
	 * @return
	 */
	PcResultObj ifAutoCheckWinPrize(boolean flag);
	
	/**
	 * 设置自动审核派奖
	 * @return
	 */
	PcResultObj ifAutoCheckAwardPrize(boolean flag);
	
	
	/**
	 * 查询彩种期号是否存在
	 * @param lotteryIssue
	 * @return
	 */
	public LotteryIssue getLotteryIssue(LotteryIssue lotteryIssue);
	

	
	
	/**
	 *  查询转移单
	 * @param lotteryIssue
	 * @param issueNo
	 * @return
	 */
	PcResultObj getCancelOrder(CancelOrderInfo canelorderInfo, int currentPage, int pageSize);

	/**
	 * 重新全部算奖
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public Integer restartAllPrize(String lotteryId, String issueNo);
	
	/**
	 * 重新部分算奖
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public Integer restartPartPrize(String lotteryId, String issueNo);
	
	/**
	 * 获取赛事列表
	 * @param competive
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcResultObj getJcMacth(MatchCompetive competive,int pageNum,int pageSize);

	/**
	 * 修改赛事信息
	 * @param competive
	 * @return
	 */
	public PcResultObj updateJcMatch(MatchCompetive competive);
	
	/**
	 * 删除单个赛事信息
	 * @param competive
	 * @return
	 */
	public PcResultObj delJcMatch(MatchCompetive competive);
	
	/**
	 * 查询单个赛事信息
	 * @param competive
	 * @return
	 */
	public PcResultObj getJcMatchById(MatchCompetive competive);
	
	
	/**
	 * 获取单个赛事开奖结果
	 * @param competive
	 * @return
	 */
	public PcResultObj getJcMatchResultById(MatchCompetive competive,String lotteryId);
	
	/**
	 * 创建或者修改赛事结果
	 * @param competive
	 * @return
	 */
	public PcResultObj createOrUpdateJcMatchResult(MatchCompetiveResult competiveResult);
	
	
	/**
	 * 查看老足彩赛事列表
	 * @param matchFootball
	 * @return
	 */
	public PcResultObj getLzcMactches(MatchFootball matchFootball);
	
	
	/**
	 * 获取单个老足彩赛事信息
	 * @param matchFootball
	 * @return
	 */
	public PcResultObj getFootballMatchById(MatchFootball matchFootball);
	
	/**
	 * 修改老足彩赛事信息
	 * @param matchFootball
	 * @return
	 */
	public PcResultObj udpateLzcMactche(MatchFootball matchFootball);
	
	/**
	 * 分页获取竞彩转移单
	 * @param sportCancelOrder
	 * @return
	 */
	public PcResultObj getJcCancelOrder(SportCancelOrder sportCancelOrder,int pageNum ,int pageSize);
}
