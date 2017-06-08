package com.cqfc.ticketwinning.util;

import java.util.concurrent.ConcurrentHashMap;


public class BDZJQSAndBQCAndSXDSMXNUtil {
	
	private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	
	static {
		map.put("1X1", "1");
		map.put("2X1", "2");
		map.put("2X3", "1,2");
		map.put("3X1", "3");
		map.put("3X4", "2,3");
		map.put("3X7", "1,2,3");
		map.put("4X1", "4");
		map.put("4X5", "3,4");
		map.put("4X11", "2,3,4");
		map.put("4X15", "1,2,3,4");
		map.put("5X1", "5");
		map.put("5X6", "4,5");
		map.put("5X16", "3,4,5");
		map.put("5X26", "2,3,4,5");
		map.put("5X31", "1,2,3,4,5");
		map.put("6X1", "6");
		map.put("6X7", "5,6");
		map.put("6X22", "4,5,6");
		map.put("6X42", "3,4,5,6");
		map.put("6X57", "2,3,4,5,6");
		map.put("6X63", "1,2,3,4,5,6");	
	}
	
	/**
	 * M串N组合表
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static int[] mxn(int m, int n){
		int[] retArr = null;
		
		String value = map.get(m + TicketWinningConstantsUtil.SEPARATOR_CHENG + n);
		
		retArr = TicketWinningUtil.str2IntArr(value.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
		
		return retArr;
	}
	
	public static boolean isPlayTypeExist(String playType){
		if(map.get(playType) != null){
			return true;
		}
		
		return false;
	}
}