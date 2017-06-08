package com.cqfc.order.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class MQUtil {
	public static ConcurrentMap<String, Boolean> orderScanMap = new ConcurrentHashMap<String, Boolean>();

	public static boolean isOrderScanMapExist(int type, String lotteryId, String issueNo) {
		boolean returnValue = true;
		try {
			String mapKey = getMapKey(type, lotteryId, issueNo);
			Boolean obj = orderScanMap.putIfAbsent(mapKey, true);
			if (null == obj) {
				returnValue = false;
			}
		} catch (Exception e) {
			ScanLog.scan.error("判断MQ消息订单orderScanMap是否存在,发生异常,type=" + type + ",lotteryId=" + lotteryId + ",issueNo="
					+ issueNo, e);
		}
		return returnValue;
	}

	public static void removeOrderScanMap(int type, String lotteryId, String issueNo) {
		try {
			String mapKey = getMapKey(type, lotteryId, issueNo);
			orderScanMap.remove(mapKey);
		} catch (Exception e) {
			ScanLog.scan.error("删除MQ消息orderScanMap发生异常,type=" + type + ",lotteryId=" + lotteryId + ",issueNo="
					+ issueNo, e);
		}
	}

	private static String getMapKey(int type, String lotteryId, String issueNo) {
		return type + "@" + lotteryId + "@" + issueNo;
	}
}
