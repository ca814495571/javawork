package com.jami.util;

import com.cqfc.protocol.userorder.Order;



/**
 * 渠道商订单日志
 * @author HowKeyond
 *
 */
public class UserOrderLogUtil {
	public static final String SEPERATOR = "@_";
	private static final Integer FIELDS_NUM = 22;

	public static String convertLog2Str(Order order) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(SEPERATOR).append(order.getLotteryId());
		sb.append(SEPERATOR).append(order.getPartnerId());
		sb.append(SEPERATOR).append(order.getUserId());
		sb.append(SEPERATOR).append(order.getIssueNo());
		sb.append(SEPERATOR).append(order.getOrderNo());
		sb.append(SEPERATOR).append(order.getOrderStatus());
		sb.append(SEPERATOR).append(order.getTotalAmount());
		sb.append(SEPERATOR).append(order.getWinPrizeMoney());
		sb.append(SEPERATOR).append(order.getPrizeAfterTax());
		sb.append(SEPERATOR).append(order.getOrderContent());
		sb.append(SEPERATOR).append(order.getStakeNum());
		sb.append(SEPERATOR).append(order.getMultiple());
		sb.append(SEPERATOR).append(order.getPlayType());
		sb.append(SEPERATOR).append(order.getPaySerialNumber());
		sb.append(SEPERATOR).append(order.getRealName());
		sb.append(SEPERATOR).append(order.getCardNo());
		sb.append(SEPERATOR).append(order.getMobile());
		sb.append(SEPERATOR).append(order.getCreateTime());
		sb.append(SEPERATOR).append(order.getExt());
		sb.append(SEPERATOR).append(order.getOrderType());
		sb.append(SEPERATOR).append(order.getTradeId());
		return sb.toString();
	}

	public static Order convertStr2Log(String str) {
		Order order = new Order();
		String[] fields = str.split(SEPERATOR);
		if (fields.length == FIELDS_NUM) {
			order.setLotteryId(fields[1]);
			order.setPartnerId(fields[2]);
			order.setUserId(Long.parseLong(fields[3]));
			order.setIssueNo((fields[4]));
			order.setOrderNo(fields[5]);
			order.setOrderStatus(Integer.parseInt(fields[6]));
			order.setTotalAmount(Long.parseLong(fields[7]));
			order.setWinPrizeMoney(Long.parseLong(fields[8]));
			order.setPrizeAfterTax(Long.parseLong(fields[9]));
			order.setOrderContent((fields[10]));
			order.setStakeNum(Integer.parseInt(fields[11]));
			order.setMultiple(Integer.parseInt(fields[12]));
			order.setPlayType(fields[13]);
			order.setPaySerialNumber(fields[14]);
			order.setRealName(fields[15]);
			order.setCardNo(fields[16]);
			order.setMobile(fields[17]);
			order.setCreateTime(fields[18]);
			order.setExt(fields[19]);
			order.setOrderType(Integer.parseInt(fields[20]));
			order.setTradeId(fields[21]);
		}
		return order;
	}

		
}
