package com.cqfc.access.util;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.util.LotteryType;

public class LotteryDrawResultUtil {
	
	/**
	 * 获取开奖内容
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static LotteryDrawResult getLotteryDrawResult(String lotteryId, String issueNo){
		ReturnMessage msg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"findLotteryDrawResult", lotteryId, issueNo);
		return (LotteryDrawResult)msg.getObj();
	}
	
	/**
	 * 转换成投注格式
	 * @param lotteryId
	 * @param winningBallContent
	 * @return
	 */
	public static String convertWinningBallContentFormat(String lotteryId, String winningBallContent){
		if(lotteryId.equals(LotteryType.SSQ.getText())){
			winningBallContent = addZero2WinningBallContent(winningBallContent);
		    winningBallContent = winningBallContent.substring(0, winningBallContent.lastIndexOf(",")) + 
				  ":" + winningBallContent.substring(winningBallContent.lastIndexOf(",")+1);
			
		}
		else if(lotteryId.equals(LotteryType.QLC.getText())){
			winningBallContent = addZero2WinningBallContent(winningBallContent);
		}
		else if(lotteryId.equals(LotteryType.FC3D.getText())){
			winningBallContent = winningBallContent.replace(",", ":");
		}
		else if(lotteryId.equals(LotteryType.XYNC.getText())){
			winningBallContent = winningBallContent.replace(",", "|");
		}
		else if(lotteryId.equals(LotteryType.SSC.getText())){
			winningBallContent = winningBallContent.replace(",", ":");
		}
		
		return winningBallContent;
	}
	
	/**
	 * 给小于10的中奖结果添加0
	 * @param winningBallContent
	 * @return
	 */
	public static String addZero2WinningBallContent(String winningBallContent){
		String[] balls = winningBallContent.split(",");
		String ret = "";
		for(int i = 0, size = balls.length; i < size; i++){
			if(Integer.valueOf(balls[i]) < 10){
				balls[i] = "0" + balls[i];
			}
			if( i != size -1){
				ret += balls[i] + ",";				
			}
			else{
				ret += balls[i];
			}
		}
		
		return ret;
	}
}
