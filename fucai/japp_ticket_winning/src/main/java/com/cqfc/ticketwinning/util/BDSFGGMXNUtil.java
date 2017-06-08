package com.cqfc.ticketwinning.util;

import java.util.concurrent.ConcurrentHashMap;


public class BDSFGGMXNUtil {
	
	private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
	
	static {
		map.put("3X1", "3");
		map.put("4X1", "4");
		map.put("4X5", "3,4");
		map.put("5X1", "5");
		map.put("5X6", "4,5");
		map.put("5X16", "3,4,5");
		map.put("6X1", "6");
		map.put("6X7", "5,6");
		map.put("6X22", "4,5,6");
		map.put("6X42", "3,4,5,6");
		map.put("7X1", "7");
		map.put("8X1", "8");
		map.put("9X1", "9");
		map.put("10X1", "10");
		map.put("11X1", "11");
		map.put("12X1", "12");
		map.put("13X1", "13");
		map.put("14X1", "14");
		map.put("15X1", "15");		
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
