package com.cqfc.outticket.util;


public class MockUtil {
	
	/**
	 * 生成随机赔率
	 * @return
	 */
	public static int generateRandomOdds(){
		int odds = 0;
		
		odds = (int)(Math.random()* 100) + 100;
		
		return odds;
	}
	
}
