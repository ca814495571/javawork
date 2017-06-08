package com.cqfc.order.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.jami.util.Log;

/**
 * @author liwh
 */
public class ScanUtil {

	public static ConcurrentMap<String, Boolean> printScanMap = new ConcurrentHashMap<String, Boolean>();

	public static String getBreakMapKey(int type, String lotteryId, String issueNo) {
		String mapKey = "";
		try {
			mapKey = type + "@" + lotteryId + "@" + issueNo;
		} catch (Exception e) {
			Log.run.error("拼接mapKey发生异常", e);
		}
		return mapKey;
	}

	public static boolean isPrintScanMapExist(int type, String lotteryId, String issueNo) {
		boolean returnValue = true;
		try {
			String mapKey = getBreakMapKey(type, lotteryId, issueNo);
			Boolean obj = printScanMap.putIfAbsent(mapKey, false);
			if (null == obj) {
				returnValue = false;
			}
		} catch (Exception e) {
			Log.run.error("判断出票请求是否在扫描MAP发生异常,lotteryId=" + lotteryId + ",issueNo=" + issueNo, e);
		}
		return returnValue;
	}

}
