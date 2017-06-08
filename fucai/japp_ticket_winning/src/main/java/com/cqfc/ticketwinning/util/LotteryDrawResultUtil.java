package com.cqfc.ticketwinning.util;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.util.IssueUtil;
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
	 * 获取竞彩开奖内容
	 * @param lotteryId
	 * @param transferId
	 * @return
	 */
	public static MatchCompetiveResult getMatchCompetiveResult(String lotteryId, String transferId){
		ReturnMessage msg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"getMatchCompetiveResult", transferId, lotteryId);
		
		return (MatchCompetiveResult)msg.getObj();
		
	}
	
	/**
	 * 获取竞彩开奖内容
	 * @param transferId
	 * @return
	 */
	public static MatchCompetive getMatchCompetiveResultList(String transferId, String issueNo){
		ReturnMessage msg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"getMatchCompetiveResultList", issueNo, transferId);
		
		return (MatchCompetive)msg.getObj();
		
	}
	
	/**
	 * 获取北单开奖结果
	 * @param wareIssue
	 * @param matchNo
	 * @return
	 */
	public static MatchCompetive getBDMatchCompetiveResultList(
			String wareIssue, String matchNo, int matchType) {
		String transferId = IssueUtil.getBeiDanTransferId(wareIssue, matchNo, matchType);
		ReturnMessage msg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"getMatchCompetiveResultList", wareIssue, transferId);
		
		return (MatchCompetive)msg.getObj();
	}
	
	/**
	 * 转换成投注格式
	 * @param lotteryId
	 * @param winningBallContent
	 * @return
	 */
	public static String convertWinningBallContentFormat(String lotteryId, String winningBallContent){
		if(lotteryId.equals(LotteryType.XYNC.getText())){
			winningBallContent = winningBallContent.replace(",", "|");
		}
		else if(lotteryId.equals(LotteryType.SSC.getText())){
			winningBallContent = winningBallContent.replace(",", ":");
		}		
		else if(lotteryId.equals(LotteryType.QLC.getText())){
			winningBallContent = addZero2WinningBallContent(winningBallContent);
		}
		else if(lotteryId.equals(LotteryType.FC3D.getText())){
			winningBallContent = winningBallContent.replace(",", ":");
		}
		else if(lotteryId.equals(LotteryType.SSQ.getText())){
			winningBallContent = addZero2WinningBallContent(winningBallContent);
		    winningBallContent = winningBallContent.substring(0, winningBallContent.lastIndexOf(",")) + 
				  ":" + winningBallContent.substring(winningBallContent.lastIndexOf(",")+1);			
		}
		
		return winningBallContent;
	}
	
	/**
	 * 判断开奖号码是否符合规则
	 * @param lotteryId
	 * @param winningBallContent
	 * @return
	 */
	public static boolean isWinningBallNumsRight(String lotteryId, String winningBallContent){
		boolean flag = false;
		
		if(lotteryId.equals(LotteryType.SSC.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_SSC)){
			flag = true;	
		}
		if(lotteryId.equals(LotteryType.ZJSYXW.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_ZJSYXW)){
			flag = true;	
		}
		else if(lotteryId.equals(LotteryType.XYNC.getText())  && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_XYNC)){
			flag = true;	
		}
		else if(lotteryId.equals(LotteryType.FC3D.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_FC3D)){
			flag = true;	
		}
		else if(lotteryId.equals(LotteryType.QLC.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_QLC)){
			flag = true;	
		}	
		else if(lotteryId.equals(LotteryType.SSQ.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_SSQ)){
			flag = true;		
		}
		else if(lotteryId.equals(LotteryType.PLS.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_PLS)){
			flag = true;		
		}
		else if(lotteryId.equals(LotteryType.PLW.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_PLW)){
			flag = true;		
		}
		else if(lotteryId.equals(LotteryType.QXC.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_QXC)){
			flag = true;		
		}
		else if(lotteryId.equals(LotteryType.DLT.getText())){
			flag = true;		
		}
		else if(lotteryId.equals(LotteryType.ZCSFC.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_LZCSFC)){
			flag = true;
		}
		else if(lotteryId.equals(LotteryType.ZCRX9.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_LZCR9)){
			flag = true;
		}
		else if(lotteryId.equals(LotteryType.ZC4CJQ.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_LZC4JQS)){
			flag = true;
		}
		else if(lotteryId.equals(LotteryType.ZC6CBQC.getText()) && (winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == TicketWinningConstantsUtil.NUM_LZC6BQC)){
			flag = true;
		}
		
		
		return flag;
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
			if((Integer.valueOf(balls[i]) < 10) && !balls[i].contains("0")){
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
