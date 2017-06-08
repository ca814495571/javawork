package com.cqfc.partner.order.service.impl;

import java.util.List;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.partner.order.service.IPartnerOrderService;
import com.cqfc.protocol.partnerorder.DailyRiskCount;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.IssueRiskCount;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.protocol.partnerorder.LotteryDaySale;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.PartnerOrderService;
import com.cqfc.protocol.partnerorder.PcDailyReport;
import com.cqfc.protocol.partnerorder.PcDaySaleDetails;
import com.cqfc.protocol.partnerorder.PcLotteryIssueSale;
import com.cqfc.protocol.partnerorder.PcPartnerOrder;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.jami.util.Log;

@Service
public class PartnerOrderHandler implements PartnerOrderService.Iface {

	@Autowired
	IPartnerOrderService partnerOrderService;

	@Override
	public int addPartnerOrder(Order order) throws TException {
//		return 1;
		return partnerOrderService.addPartnerOrder(order);
	}


	
	@Override
	public List<IssueSaleAndReward> getIssueSaleAndReward(String partnerId,
			String lotteryId, String issueNo) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.getIssueSaleAndReward(partnerId, lotteryId, issueNo);
	}

	@Override
	public List<DailySaleAndCharge> getDailySaleAndCharge(String partnerId,
			String countTime) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.getDailySaleAndChargeByWhere(partnerId, countTime);
	}

	@Override
	public List<LotteryIssueSale> getAllLotteryIssueSale(String lotteryId,
			String issueNo) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.getAllLotteryIssueSale(lotteryId, issueNo);
	}


	@Override
	public PcLotteryIssueSale getLotteryIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale, int pageNum, int pageSize)
			throws TException {
		return partnerOrderService.getLotteryIssueSaleByWhere(lotteryIssueSale,
				pageNum, pageSize);
	}

	@Override
	public List<LotteryDaySale> getLotteryDaySaleByWhere(
			LotteryDaySale lotteryDaySale, int pageNum, int pageSize)
			throws TException {
		return partnerOrderService.getLotteryDaySaleByWhere(lotteryDaySale,
				pageNum, pageSize);
	}

	@Override
	public List<LotteryIssueSale> getIssueSaleAndRewardByGroup(
			String partnerId, String lotteryId, String issueNo)
			throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.getIssueSaleAndRewardByGroup(partnerId, lotteryId, issueNo);
	}


	@Override
	public DailyRiskCount getDailyRiskCount(String day) throws TException {
		try {
			return partnerOrderService.getDailyRiskCount(day);
		} catch (Exception e) {
			Log.run.debug("", e);
		}
		return null;
	}


	@Override
	public IssueRiskCount getIssueRiskCount(String lotteryId, String issueNo)
			throws TException {
		try {
			return partnerOrderService.getIssueRiskCount(lotteryId, issueNo);
		} catch (Exception e) {
			Log.run.debug("", e);
		}
		return null;
	}


	@Override
	public int partnerIssueSaleCount(String lotteryId, String issueNo)
			throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.partnerIssueSaleCount(lotteryId, issueNo);
	}


	@Override
	public int partnerIssueRewardCount(String lotteryId, String issueNo)
			throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.partnerIssueRewardCount(lotteryId, issueNo);
	}


	@Override
	public int partnerDailySaleCount(String countTime) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.partnerDailySaleCount(countTime);
	}


	@Override
	public int partnerDailyAwardCount(String countTime) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.partnerDailyAwardCount(countTime);
	}


	@Override
	public int partnerDailyChargeCount(String countTime) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.partnerDailyChargeCount(countTime);
	}


	@Override
	public int partnerDailyEncashCount(String countTime) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.partnerDailyEncashCount(countTime);
	}


	@Override
	public long getTotalTicknumByDay(String date) throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.getTotalTicknumByDay(date);
	}




	@Override
	public PcPartnerOrder getPartnerOrderByWhere(Order order, int pageNum, int pageSize)
			throws TException {
		// TODO Auto-generated method stub
		return partnerOrderService.getPartnerOrderByWhere(order, pageNum, pageSize);
	}



	@Override
	public List<LotteryIssueSale> getCurrentIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale, String fromTime, String toTime)
			throws TException {

		
		return partnerOrderService.getCurrentIssueSaleByWhere( lotteryIssueSale,  fromTime,  toTime);
	}



	@Override
	public PcDailyReport getDailyReportByWhere(
			DailySaleAndCharge dailySaleAndCharge, int pageNum, int pageSize)
			throws TException {
		
		return partnerOrderService.getDailyReportByWhere(dailySaleAndCharge, pageNum, pageSize);
	}



	@Override
	public int partnerDailyReport(String countTime) throws TException {
		return partnerOrderService.partnerDailyReport(countTime);
	}



	@Override
	public int updateWinResultInfo(List<WinningOrderInfo> winningOrderInfos)
			throws TException {
		return partnerOrderService.updateWinResultInfo(winningOrderInfos);
	}



	@Override
	public PcDaySaleDetails getDailySaleDetails(
			DailySaleAndCharge dailySaleAndCharge, int pageNum, int pageSize)
			throws TException {
		return partnerOrderService.getDailySaleDetails(dailySaleAndCharge, pageNum, pageSize);
	}


}
