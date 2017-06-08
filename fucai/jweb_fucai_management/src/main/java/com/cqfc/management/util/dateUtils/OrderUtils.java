package com.cqfc.management.util.dateUtils;

import java.util.HashMap;
import java.util.Map;

import com.cqfc.management.model.OrderInfo;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;

public class OrderUtils {

	
	public static Map<String,LotteryPartner> maps =  new HashMap<String, LotteryPartner>();
	
	
	public static final long bigPrizeMoney = 1000000;
	
	public static OrderInfo packPartnerOrder(Order partnerOrder){
		OrderInfo orderInfo = new OrderInfo();
		
		
		orderInfo.setContent(partnerOrder.getOrderContent());
		orderInfo.setCreateTime(DateUtil.getSubstringDateTime(partnerOrder.getCreateTime()));
		orderInfo.setIssueNo(partnerOrder.getIssueNo());
		orderInfo.setLotteryId(partnerOrder.getLotteryId());
		orderInfo.setMultiple(partnerOrder.getMultiple());
		orderInfo.setOrderNo(partnerOrder.getOrderNo());
		orderInfo.setOrderType(partnerOrder.getOrderType());
		orderInfo.setUserId(String.valueOf(partnerOrder.getUserId()));
		orderInfo.setPartnerId(partnerOrder.getPartnerId());
		orderInfo.setPrizeMoney(MoneyUtil.toYuanStr(partnerOrder.getWinPrizeMoney()));
		
		if(partnerOrder.getWinPrizeMoney() > bigPrizeMoney){
			
			orderInfo.setAfterPrizeMoney(MoneyUtil.toYuanStr(Math.round(partnerOrder.getWinPrizeMoney()*0.8)));
		}else{
			
			orderInfo.setAfterPrizeMoney(MoneyUtil.toYuanStr(partnerOrder.getWinPrizeMoney()));
		}
		
		com.cqfc.processor.ReturnMessage message = TransactionProcessor.dynamicInvoke("partner",
				"findLotteryPartnerById", partnerOrder.getPartnerId());

		if (message.getObj() != null) {
			LotteryPartner lotteryPartner = (LotteryPartner) message.getObj();
			orderInfo.setPartnerType(lotteryPartner.getPartnerType());
			orderInfo.setPartnerName(lotteryPartner.getPartnerName());
		}

		orderInfo.setPlayType(partnerOrder.getPlayType());
		orderInfo.setStakeNum(partnerOrder.getStakeNum());
		orderInfo.setStatus(partnerOrder.getOrderStatus());
		orderInfo.setTicketId(partnerOrder.getTradeId());
		orderInfo.setTotalMoney(MoneyUtil.toYuanStr(partnerOrder
				.getTotalAmount()));
		
		
		return orderInfo;
	}
	
	
	
	public static OrderInfo packUserOrder(com.cqfc.protocol.userorder.Order userOrder,String userName){
		
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setContent(userOrder.getOrderContent());
		orderInfo.setCreateTime(DateUtil.getSubstringDateTime(userOrder.getCreateTime()));
		orderInfo.setIssueNo(userOrder.getIssueNo());
		orderInfo.setLotteryId(userOrder.getLotteryId());
		orderInfo.setMultiple(userOrder.getMultiple());
		orderInfo.setOrderNo(userOrder.getOrderNo());
		orderInfo.setOrderType(userOrder.getOrderType());

		orderInfo.setPartnerId(userOrder.getPartnerId());
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor.dynamicInvoke("partner",
				"findLotteryPartnerById", userOrder.getPartnerId());
		if (reMsg.getObj() != null) {
			LotteryPartner lotteryPartner = (LotteryPartner) reMsg.getObj();
			orderInfo.setPartnerType(lotteryPartner.getPartnerType());
			orderInfo.setPartnerName(lotteryPartner.getPartnerName());
		}
		orderInfo.setPlayType(userOrder.getPlayType());
		orderInfo.setStakeNum(userOrder.getStakeNum());
		orderInfo.setStatus(userOrder.getOrderStatus());
		orderInfo.setTicketId(userOrder.getTradeId());
		orderInfo.setTotalMoney(MoneyUtil.toYuanStr(userOrder
				.getTotalAmount()));
		orderInfo.setUserId(String.valueOf(userOrder.getUserId()));
		orderInfo.setUserName(userName);
		
		return orderInfo;
	}
	
}
