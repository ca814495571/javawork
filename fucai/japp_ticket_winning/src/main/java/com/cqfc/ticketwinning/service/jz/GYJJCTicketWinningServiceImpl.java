package com.cqfc.ticketwinning.service.jz;

import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;

public class GYJJCTicketWinningServiceImpl {
	
	/**
	 * 校验投注内容
	 * @param orderContent 01,02,03
	 * @return
	 */
	public boolean validateContent(String orderContent) {
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		if(balls.length < 1){
			return false;
		}
		
		if(!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)){
			return false;
		}
		
		
		return true;
	}
	
	
	/**
	 * 计算投注总注数
	 * @param orderContent 01,02,03
	 * @return
	 */
	public int calBallCounts(String orderContent){
		int count = 0;
		
		count = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
		
		return count;
	}
	
	/**
	 * 计算是否中奖
	 * @param orderContent 01,02,03
	 * @param winningContent
	 * @return
	 */
	public boolean isTicketPrize(String orderContent, String winningContent){
		boolean isPrize = false;
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);		
		
		for(int i = 0, len = balls.length; i < len; i++){
			if(balls[i].equals(winningContent)){
				isPrize = true;
				break;
			}
		}
		
		return isPrize;
	}
	
}
