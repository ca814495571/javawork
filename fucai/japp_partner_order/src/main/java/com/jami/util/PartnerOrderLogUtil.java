package com.jami.util;

import org.apache.commons.lang3.StringUtils;

import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.OrderDetail;
import com.cqfc.util.OrderStatus;

/**
 * 渠道商订单日志
 * 
 * @author an
 *
 */
public class PartnerOrderLogUtil {
	public static final String SEPERATOR = "@_";
	public static final Integer ORDER_FIELDS_NUM = 28;
	public static final Integer ORDERDETAL_FIELDS_NUM = 12;
	public static String order2Str(Order order) {
		initOrder(order);
		StringBuffer sb = new StringBuffer();
		sb.append(SEPERATOR).append(order.getLotteryId()==null?"":order.getLotteryId());
		sb.append(SEPERATOR).append(order.getPartnerId()==null?"":order.getPartnerId());
		sb.append(SEPERATOR).append(order.getUserId());
		sb.append(SEPERATOR).append(order.getIssueNo()==null?"":order.getIssueNo());
		sb.append(SEPERATOR).append(order.getOrderNo()==null?"":order.getOrderNo());
		sb.append(SEPERATOR).append(order.getOrderStatus());
		sb.append(SEPERATOR).append(order.getTotalAmount());
		sb.append(SEPERATOR).append(order.getWinPrizeMoney());
		sb.append(SEPERATOR).append(order.getPrizeAfterTax());
		sb.append(SEPERATOR).append(order.getOrderContent()==null?"":order.getOrderContent());
		sb.append(SEPERATOR).append(order.getStakeNum());
		sb.append(SEPERATOR).append(order.getMultiple());
		sb.append(SEPERATOR).append(order.getPlayType()==null?"":order.getPlayType());
		sb.append(SEPERATOR).append(order.getPaySerialNumber()==null?"":order.getPaySerialNumber());
		sb.append(SEPERATOR).append(order.getRealName()==null?"":order.getRealName());
		sb.append(SEPERATOR).append(order.getCardNo()==null?"":order.getCardNo());
		sb.append(SEPERATOR).append(order.getMobile()==null?"":order.getMobile());
		sb.append(SEPERATOR).append(order.getCreateTime()==null?"":order.getCreateTime());
		sb.append(SEPERATOR).append(order.getExt()==null?"":order.getExt());
		sb.append(SEPERATOR).append(order.getOrderType());
		sb.append(SEPERATOR).append(order.getTradeId()==null?"":order.getTradeId());
		sb.append(SEPERATOR).append(order.getPlanId()==null?"":order.getPlanId());
		sb.append(SEPERATOR).append(order.getProvince());
		sb.append(SEPERATOR).append(order.getLotteryMark()==null?"":order.getLotteryMark());
		sb.append(SEPERATOR).append(order.getEndTime());
		sb.append(SEPERATOR).append(order.getTicketTime());
		sb.append(SEPERATOR).append(order.getDrawTime());
		return sb.toString();
	}

	public static Order str2Order(String str) {
		Order order = new Order();
		String[] fields = str.split(SEPERATOR);
		if (fields.length == ORDER_FIELDS_NUM) {
			order.setLotteryId((fields[1]));
			order.setPartnerId((fields[2]));
			order.setUserId(Long.parseLong(fields[3]));
			order.setIssueNo(((fields[4])));
			order.setOrderNo((fields[5]));
			order.setOrderStatus(Integer.parseInt(fields[6]));
			order.setTotalAmount(Long.parseLong(fields[7]));
			order.setWinPrizeMoney(Long.parseLong(fields[8]));
			order.setPrizeAfterTax(Long.parseLong(fields[9]));
			order.setOrderContent(((fields[10])));
			order.setStakeNum(Integer.parseInt(fields[11]));
			order.setMultiple(Integer.parseInt(fields[12]));
			order.setPlayType((fields[13]));
			order.setPaySerialNumber((fields[14]));
			order.setRealName((fields[15]));
			order.setCardNo((fields[16]));
			order.setMobile((fields[17]));
			order.setCreateTime((fields[18]));
			order.setExt((fields[19]));
			order.setOrderType(Integer.parseInt(fields[20]));
			order.setTradeId((fields[21]));
			order.setPlanId((fields[22]));
			order.setProvince((fields[23]));
			order.setLotteryMark((fields[24]));
			order.setEndTime((fields[25]));
			order.setTicketTime((fields[26]));
			order.setDrawTime((fields[27]));
		}
		return order;
	}

	public static String orderDetail2Str(OrderDetail orderDetail) {
		initOrderDetail(orderDetail);
		StringBuffer sb = new StringBuffer();
		sb.append(SEPERATOR).append(orderDetail.getOrderNo());
		sb.append(SEPERATOR).append(orderDetail.getTransferId()==null?"":orderDetail.getTransferId());
		sb.append(SEPERATOR).append(orderDetail.getMatchNo()==null?"":orderDetail.getMatchNo());
		sb.append(SEPERATOR).append(orderDetail.getRq());
		sb.append(SEPERATOR).append(orderDetail.getOdds());
		sb.append(SEPERATOR).append(orderDetail.getContent()==null?"":orderDetail.getContent());
		sb.append(SEPERATOR).append(orderDetail.getMatchStatus());
		sb.append(SEPERATOR).append(orderDetail.getLotteryId()==null?"":orderDetail.getLotteryId());
		sb.append(SEPERATOR).append(orderDetail.getIssueNo()==null?"":orderDetail.getIssueNo());
		sb.append(SEPERATOR).append(orderDetail.getMatchType());
		//竞彩详情
		sb.append(SEPERATOR).append(OrderStatus.JC_CODE);
		
		return sb.toString();
	}

	public static OrderDetail str2OrderDetail(String str) {
		OrderDetail orderDetail = new OrderDetail();
		String[] fields = str.split(SEPERATOR);
		if (fields.length == ORDERDETAL_FIELDS_NUM) {
			orderDetail.setOrderNo((fields[1]));
			orderDetail.setTransferId((fields[2]));
			orderDetail.setMatchNo((fields[3]));
			orderDetail.setRq((fields[4]));
			orderDetail.setOdds((fields[5]));
			orderDetail.setContent((fields[6]));
			orderDetail.setMatchStatus(Integer.parseInt(fields[7]));
			orderDetail.setLotteryId(fields[8]);
			orderDetail.setIssueNo(fields[9]);
			orderDetail.setMatchType(Integer.parseInt(fields[10]));
		}
		return orderDetail;
	}

	public  static void initOrder(Order order){
		if(StringUtils.isBlank(order.getDrawTime())) order.setDrawTime("0000-00-00 00:00:00") ;
		if(StringUtils.isBlank(order.getEndTime())) order.setEndTime("0000-00-00 00:00:00") ;
		if(StringUtils.isBlank(order.getTicketTime())) order.setTicketTime("0000-00-00 00:00:00") ;
		if(StringUtils.isBlank(order.getCreateTime())) order.setCreateTime("0000-00-00 00:00:00") ;
		if(StringUtils.isBlank(order.getProvince())) order.setProvince("") ;
		if(StringUtils.isBlank(order.getPlanId())) order.setPlanId("") ;
		if(StringUtils.isBlank(order.getLotteryMark())) order.setLotteryMark("") ;
	}
	
	public static void initOrderDetail(OrderDetail orderDetail){
		
		if(StringUtils.isBlank(orderDetail.getRq())) orderDetail.setRq("");
		if(StringUtils.isBlank(orderDetail.getOdds()))	orderDetail.setOdds("");
		
	}
	public static void main(String[] args) {
	
	}

}
