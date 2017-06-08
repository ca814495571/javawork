package com.cqfc.ticketwinning.service.lzc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cqfc.ticketwinning.model.Prize;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.ticketwinning.util.LZCContentSetUtil;
import com.cqfc.ticketwinning.util.PrizeAmount;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryPlayTypeConstants;
import com.cqfc.util.LotteryType;

public class ZCRX9CTicketWinningServiceImpl implements ITicketWinningService{
	/**
	 * 校验投注内容
	 * @param orderContent *-*-1-3-1-*-3-3-*-1-1-1-0-*
	 * @return
	 */
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;
		Set<String> contentSet = LZCContentSetUtil.getSetContentByLotteryId(LotteryType.ZCRX9.getText());
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_DANSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			if(ballContents.length > 3){
				return false;
			}
			for(int i = 0, ballContentLen = ballContents.length; i < ballContentLen; i++){
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
				
				if(balls.length != 14){
					return false;
				}
				
				int countStar = 0;
				int countBall = 0;
				
				for(int j = 0, len = balls.length; j < len; j++){
					if(balls[j].equals(TicketWinningConstantsUtil.SEPARATOR_XINGHAO)){
						countStar++;
					}
					else if(contentSet.contains(balls[j])){
						countBall++;
					}
				}
				
				if(countStar != 5){
					return false;
				}
				if(countBall != 9){
					return false;
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_FUSHI)){
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			int countStar = 0;
			int countBall = 0;
			
			if(balls.length != 14){
				return false;
			}
			if(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length < 2){
				return false;
			}
			for(int i = 0, len = balls.length; i < len; i++){
				if(balls[i].length() != 1){
					String[] ballsSplit = balls[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
					for(int j = 0, lenSplit = ballsSplit.length; j < lenSplit; j++){
						if(!contentSet.contains(ballsSplit[j])){
							return false;
						}
					}
					countBall++;
				}
				else if(balls[i].equals(TicketWinningConstantsUtil.SEPARATOR_XINGHAO)){
					countStar++;
				}
				else{
					if(contentSet.contains(balls[i])){
						countBall++;
					}
				}
			}
			if(countStar != 5){
				return false;
			}
			if(countBall != 9){
				return false;
			}			
		}
		
		return flag;
	}
	
	/**
	 * 计算投注总注数
	 * @param orderContent *-*-1-3-1-*-3-3-*-1-1-1-0-*
	 * @return
	 */
	public int calBallCounts(String orderContent, String playType) {
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		int sum = 0;
		for(int i = 0; i< ballContents.length; i++){
			sum += calZCRX9CBallCount(ballContents[i], playType);
		}
		
		return sum;
	}
	
	private int calZCRX9CBallCount(String orderContent, String playType){
		int count = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_DANSHI)){
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_FUSHI)){
			count = listTicketDetails(orderContent, playType).size();
		}
		return count;
	}
	
	/**
	 * 计算每注详细内容
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	public List<String> listTicketDetails(String orderContent, String playType) {
		List<String> details = new ArrayList<String>();
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			details.addAll(listZCRX9CTicketDetail(ballContents[i], playType));
		}
		
		return details;
	}
	
	private List<String> listZCRX9CTicketDetail(String orderContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_DANSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			for(int i = 0, len = ballContents.length; i < len; i++){
				details.add(ballContents[i]);				
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_FUSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			for(int i = 0, len = ballContents.length; i < len; i++){
				details = TicketWinningUtil.calContentList(details, ballContents[i]);
			}
		}		
		
		return details;
	}
	
	/**
	 * 计算是否中奖
	 * @param orderContent *-*-1-3-1-*-3-3-*-1-1-1-0-*
	 * @param winningContent 1
	 * @return
	 */
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningContent, Map<Integer, Long> prizeAmountLevelMap){
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_DANSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			for(int i = 0, len = ballContents.length; i < len; i++){
				prizeLevel = calRX9WinningTicketPrize(ballContents[i], winningContent);	
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if (prizeLevel > 0) {
					if(prizeAmount == 0){
						prize.setPrize(true);
						prize.setPrizeAmount(prizeAmount);
						break;						
					}
					else{
						prize.setPrize(true);
						prize.setPrizeAmount(prize.getPrizeAmount() + prizeAmount);
					}
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_9_FUSHI)){
			List<String> ballContents = listTicketDetails(orderContent, playType);
			
			for(String ballContent : ballContents){
				prizeLevel = calRX9WinningTicketPrize(ballContent, winningContent);	
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if (prizeLevel > 0) {
					if(prizeAmount == 0){
						prize.setPrize(true);
						prize.setPrizeAmount(prizeAmount);
						break;						
					}
					else{
						prize.setPrize(true);
						prize.setPrizeAmount(prize.getPrizeAmount() + prizeAmount);
					}
				}
			}
		}
		
		return prize;
	}

	/**
	 * 计算中奖情况
	 * @param ballContent
	 * @param winningContent
	 * @return
	 */
	private int calRX9WinningTicketPrize(String ballContent, String winningContent) {
		int prizeLevel = 0;
		ballContent = ballContent.replaceAll(TicketWinningConstantsUtil.SEPARATOR_DOUHAO, TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
		String[] winningBalls = winningContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		int winningCount = 0;
		
		for(int i = 0, len = balls.length; i < len; i++){
			if(balls[i].equals(winningBalls[i])){
				winningCount++;
			}
		}
		
		if(winningCount == 9){
			prizeLevel = PrizeAmount.ZC_RX9C_FIRST_PRIZE_LEVEL;
		}

		return prizeLevel;
	}
}
