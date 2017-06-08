package com.cqfc.management.service;

import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PartnerCharge;
import com.cqfc.management.model.PartnerDaySale;
import com.cqfc.management.model.PartnerInfo;
import com.cqfc.management.model.PartnerIssueCount;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;

public interface IPartnerService {

	
	
	
	/**
	 * 添加一个合作商信息
	 */
	PcResultObj addPartnerInfo(PartnerInfo partnerInfo);
	
	
	/**
	 * 获取合作商信息
	 * @param id
	 * @return
	 */
	PcResultObj getPartnerInfo(LotteryPartner partnerInfo,int pageNum,int pageSize);
	
	
	/**
	 * 修改合作商信息
	 * @param id
	 * @return
	 */
	PcResultObj updatePartnerById(PartnerInfo partnerInfo);
	
	
	/**
	 * 查询单个合作商详细信息
	 * @param id
	 * @return
	 */
	PcResultObj getPartnerById(PartnerInfo partnerInfo);
	
	/**
	 * 根据条件获取所有合作商列表信息
	 * @return
	 */
//	PcResultObj getPartners(PartnerInfo partnerInfo,int pageNum,int pageSize);
	
	
	/**
	 * 根据合作商id 彩种 期号 查询销售量
	 * @param partnerId
	 * @return
	 */
	PcResultObj getSaleByIssue(PartnerIssueCount partnerIssueCount ,int pageNum ,int pageSize);
	
	
	/**
	 * 根据条件查询合作商日销售量
	 */
	PcResultObj getSaleByDay(PartnerDaySale partnerDaySale,int pageNum ,int pageSize);
	
	
	
	
	/**
	 * 合作商充值
	 * @param status
	 * @return
	 */
	PcResultObj addPartnerRecharge(PartnerCharge partnerCharge);
	
	
	
	/**
	 * 修改合作商冻结状态
	 * @param status
	 * @return
	 */
	PcResultObj updatePartnerState(PartnerInfo partnerInfo);
	
	/**
	 * 根据条件查询合作商账户信息
	 * @return
	 */
//	PcResultObj getAccountInfoByWhere(AccountInfo partnerAccount);
	
	
	/**
	 * 期销售统计
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	PcResultObj issueSaleCount(String lotteryId, String issueNo);
	
	/**
	 * 期兑奖统计
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	PcResultObj issueRewardCount(String lotteryId, String issueNo);
	
	/**
	 * 日销量统计
	 * @param countTime
	 * @return
	 */
	PcResultObj dailySaleCount(String countTime);
	
	/**
	 * 日提现统计
	 * @param countTime
	 * @return
	 */
	PcResultObj dailyEncashCount(String countTime);
	
	/**
	 * 日兑奖统计
	 * @param countTime
	 * @return
	 */
	PcResultObj dailyRewardCount(String countTime);
	
	/**
	 * 日充值统计
	 * @param countTime
	 * @return
	 */
	PcResultObj dailyChargeCount(String countTime);
	
	
	/**
	 * 日报统计
	 * @param countTime
	 * @return
	 */
	PcResultObj dailyReport(String countTime);
	
	
	/**
	 * 根据条件查询合作商流水日志信息
	 */
//	PcResultObj getLogInfoByWhere(AccountLog partnerAccountLog ,String fromTime, String toTime ,int pageNum ,int pageSize);
	
	
	/**
	 * 根据合作商id判断合作商类型 0 （1，2合作商类型） 1（3用户类型） -1（不存在）
	 */
	int getPartnerType(String partnerId);
	
	
	
	/**
	 * 根据条件查询订单
	 * @param orderInfo
	 * @return
	 */
	public PcResultObj getOrderByWhere(OrderInfo orderInfo);
	
	
	
	/**
	 * 后台合作商历史期号销量查询
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcResultObj getHistoryIssueCount(PartnerIssueCount partnerIssueCount,int pageNum,int pageSize);
	
	/**
	 * 当前期销量查询
	 * @param partnerIssueCount
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public PcResultObj getCurrentIssueCount(PartnerIssueCount partnerIssueCount,String fromTime,String toTime);
	/**
	 * 日报查询
	 * @param dailySaleAndCharge
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcResultObj getDailyReport(DailySaleAndCharge dailySaleAndCharge,int pageNum,int pageSize);
	/**
	 * 中奖订单查询
	 * @param orderInfo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcResultObj getOrder(OrderInfo orderInfo,int pageNum,int pageSize);

	/**
	 * 日销量详情列表
	 * @param partnerId
	 * @param countTime
	 * @return
	 */
	public PcResultObj getDaySaleDetails(DailySaleAndCharge dailySaleAndCharge,int pageNum,int pageSize);
	
	
}
