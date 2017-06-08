package com.cqfc.util;

public class LotteryUtil {
	
	private static final String ISSUENO_PREFIX = "20";
	
	/**
	 * 系统转换成对应福彩中心的期号
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String convertIssueNo(String lotteryId, String issueNo){
	    if(lotteryId.equals(LotteryType.SSC.getText()) || lotteryId.equals(LotteryType.XYNC.getText()) || lotteryId.equals(LotteryType.DLT.getText())
	    		 || lotteryId.equals(LotteryType.QXC.getText())  || lotteryId.equals(LotteryType.PLS.getText())  || lotteryId.equals(LotteryType.PLW.getText())){
			issueNo = issueNo.substring(2);
		}
		
		return issueNo;
	}
	
	
	/**
	 * 福彩中心的期号转换成对应系统期号
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String convertIssueNo2System(String lotteryId, String issueNo){
		if(lotteryId.equals(LotteryType.SSC.getText()) || lotteryId.equals(LotteryType.XYNC.getText()) || lotteryId.equals(LotteryType.DLT.getText())
	    		 || lotteryId.equals(LotteryType.QXC.getText())  || lotteryId.equals(LotteryType.PLS.getText())  || lotteryId.equals(LotteryType.PLW.getText())){
			issueNo = ISSUENO_PREFIX + issueNo;
		}
		return issueNo;
	}
	
	
	
}
