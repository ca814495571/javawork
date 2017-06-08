package com.cqfc.util;

import com.cqfc.protocol.partneraccount.PartnerAccountLog;

/**
 * 渠道商日志流水号
 * @author HowKeyond
 *
 */
public class PartnerAccountLogUtil {
	private static final String SEPERATOR=",";
	private static final Integer FIELDS_NUM=8;
	public static String convertLog2Str(PartnerAccountLog log) {
		StringBuffer sb = new StringBuffer();
		sb.append(SEPERATOR).append(log.getPartnerId());
		sb.append(SEPERATOR).append(log.getSerialNumber());
		sb.append(SEPERATOR).append(log.getState());
		sb.append(SEPERATOR).append(log.getTotalAmount());
		sb.append(SEPERATOR).append(log.getAccountAmount());
		sb.append(SEPERATOR).append(log.getRemainAmount());
		sb.append(SEPERATOR).append(log.getRemark());
		return sb.toString();
	}
	
	public static PartnerAccountLog convertStr2Log(String str){
		PartnerAccountLog log = new PartnerAccountLog();
		String[] fields = str.split(SEPERATOR);
		if(fields.length == FIELDS_NUM){
			log.setPartnerId(fields[1]);
			log.setSerialNumber(fields[2]);
			log.setState(Integer.parseInt(fields[3]));
			log.setTotalAmount(Long.parseLong(fields[4]));
			log.setAccountAmount(Long.parseLong(fields[5]));
			log.setRemainAmount(Long.parseLong(fields[6]));
			log.setRemark(fields[7]);
		}
		return log;
	}
}
