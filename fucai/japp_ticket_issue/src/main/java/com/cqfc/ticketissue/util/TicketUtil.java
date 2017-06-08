package com.cqfc.ticketissue.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jami.util.Log;

/**
 * @author liwh
 */
public class TicketUtil {

	public static ConcurrentMap<String, Boolean> sendTicketMap = new ConcurrentHashMap<String, Boolean>();

	public static ConcurrentMap<String, Boolean> checkTicketMap = new ConcurrentHashMap<String, Boolean>();

	public static boolean isSendTicketMapExist(String orderNo) {
		boolean returnValue = true;
		try {
			Boolean mapValue = sendTicketMap.putIfAbsent(orderNo, true);
			if (null == mapValue) {
				returnValue = false;
			}
		} catch (Exception e) {
			Log.run.error("校验请求出票MAP是否存在该订单发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	public static boolean isCheckTicketMapExist(String orderNo) {
		boolean returnValue = true;
		try {
			Boolean mapValue = checkTicketMap.putIfAbsent(orderNo, true);
			if (null == mapValue) {
				returnValue = false;
			}
		} catch (Exception e) {
			Log.run.error("校验查询出票MAP是否存在该订单发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}
}
