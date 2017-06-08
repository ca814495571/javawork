package com.cqfc.ticketwinning.service.jz;

import java.util.Map;

import com.cqfc.ticketwinning.service.AbstrartJCTicketWinningService;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryType;

public class JCZQHHGGTicketWinningServiceImpl extends AbstrartJCTicketWinningService {
		
	
	/**
	 * 计算比赛中奖赔率
	 * @param orderContent [SPF#1,BF#0:0]
	 * @param winningContent 
	 * @param odds
	 * @return
	 */
	@Override
	public int calWinOdds(String orderContent, String winningContent, String odds){
		int winOdds = 0;	
		String ballContent = orderContent.substring(1, orderContent.length() - 1);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] ballSplit = null;
		String lotteryNameTemp = "";
		String ball = "";
		Map<String, String> winningContentMap = TicketWinningUtil.getWinningContentMap(winningContent);
		
		for(int i = 0, len = balls.length; i < len; i++){
			ballSplit = balls[i].split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			lotteryNameTemp = ballSplit[0];
			ball = ballSplit[1];
			if(lotteryNameTemp.equals(TicketWinningConstantsUtil.JZ_SPF)){
				if(isTicketBallPrize(ball, winningContentMap.get(LotteryType.JCZQSPF.getText()))){
					winOdds += 	TicketWinningUtil.computeWinOdds(odds, ballContent);	
				}
			}
			else if(lotteryNameTemp.equals(TicketWinningConstantsUtil.JZ_RQSPF)){
				if(isTicketBallPrize(ball, winningContentMap.get(LotteryType.JCZQRQSPF.getText()))){
					winOdds += 	TicketWinningUtil.computeWinOdds(odds, ballContent);				
				}
			}
			else if(lotteryNameTemp.equals(TicketWinningConstantsUtil.JZ_BF)){
				if(isTicketBallPrize(ball, winningContentMap.get(LotteryType.JCZQBF.getText()))){
					winOdds += 	TicketWinningUtil.computeWinOdds(odds, ballContent);				
				}
			}
			else if(lotteryNameTemp.equals(TicketWinningConstantsUtil.JZ_BQCSPF)){
				if(isTicketBallPrize(ball, winningContentMap.get(LotteryType.JCZQBQC.getText()))){
					winOdds += 	TicketWinningUtil.computeWinOdds(odds, ballContent);				
				}
			}
			else if(lotteryNameTemp.equals(TicketWinningConstantsUtil.JZ_ZJQS)){
				if(isTicketBallPrize(ball, winningContentMap.get(LotteryType.JCZQJQS.getText()))){
					winOdds += 	TicketWinningUtil.computeWinOdds(odds, ballContent);				
				}
			}
		}			
		
		return winOdds;
	}
	
	private boolean isTicketBallPrize(String orderContent, String winningContent){
		boolean isPrize = false;				
		if(orderContent.equals(winningContent)){
			isPrize = true;
		}
		
		return isPrize;
	}
}
