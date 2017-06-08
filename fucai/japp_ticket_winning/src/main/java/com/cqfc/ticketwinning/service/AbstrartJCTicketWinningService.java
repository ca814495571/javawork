package com.cqfc.ticketwinning.service;

import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;



public abstract class AbstrartJCTicketWinningService {
	
	/**
	 * 计算竞彩比赛中奖赔率
	 * 
	 * @param orderContent
	 * @param winningContent
	 * @param odds
	 * @return
	 */
	public int calWinOdds(String orderContent, String winningContent,
			String odds) {
		int winOdds = 0;

		if (isTicketPrize(orderContent, winningContent)) {
			winOdds = TicketWinningUtil.computeWinOdds(odds, winningContent);
		}

		return winOdds;
	}
	
	/**
	 * 计算北单比赛中奖赔率
	 * @param orderContent
	 * @param winningContent
	 * @param sp
	 * @return
	 */
	public int calBDWinOdds(String orderContent, String winningContent,
			String sp) {
		int winOdds = 0;

		if (isTicketPrize(orderContent, winningContent)) {
			winOdds = (int)(Double.valueOf(sp) * 100);
		}

		return winOdds;
	}

	/**
	 * 计算是否中奖
	 * 
	 * @param orderContent  
	 * @param winningContent 
	 * @return
	 */
	public boolean isTicketPrize(String orderContent, String winningContent){
		boolean isPrize = false;		
		String ballContent = orderContent.substring(1, orderContent.length() - 1);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);		
		
		for(int i = 0, len = balls.length; i < len; i++){
			if(balls[i].equals(winningContent)){
				isPrize = true;
				break;
			}
		}
		
		return isPrize;
	}
}
