package com.cqfc.ticketissue.util;

public class LotteryUtil {
	/**
	 * 转换成对应福彩中心的彩种
	 * @param lotteryId
	 * @return
	 */
	public static String convertLotteryId(String lotteryId){
		if(lotteryId.equals("FC3D")){
			lotteryId = "D3";
		}
		else if(lotteryId.equals("SSC")){
			lotteryId = "CQSSC";
		}
		else if(lotteryId.equals("XYNC")){
			lotteryId = "CQKL10";
		}
		
		return lotteryId;
	}
	
	/**
	 * 转换成对应福彩中心的期号
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String convertIssueNo(String lotteryId, String issueNo){
	    if(lotteryId.equals("CQSSC")){
			issueNo = issueNo.substring(2);
		}
		else if(lotteryId.equals("CQKL10")){
			issueNo = issueNo.substring(2);
		}
		return issueNo;
	}
	
}
