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

public class ZCSFC14CTickwetWinningServiceImpl implements ITicketWinningService{
	
	/**
	 * 校验投注内容
	 * @param orderContent 1-0-0-3-3-3-3-1-3-0-0-3-1-0
	 * @return
	 */
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;
		Set<String> contentSet = LZCContentSetUtil.getSetContentByLotteryId(LotteryType.ZCSFC.getText());
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_DANSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			if(ballContents.length > 3){
				return false;
			}
			for(int i = 0, ballContentLen = ballContents.length; i < ballContentLen; i++){
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
				
				if(balls.length != 14){
					return false;
				}
				
				for(int j = 0, len = balls.length; j < len; j++){
					if(balls[j].length() != 1){
						return false;
					}
					if(!contentSet.contains(balls[i])){
						return false;
					}
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_FUSHI)){
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			
			if(balls.length != 14){
				return false;
			}
			if(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length < 2){
				return false;
			}
			for(int i = 0, len = balls.length; i < len; i++){
				String[] ballsSplit = balls[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(ballsSplit)){
					return false;
				}
								
				for(int j = 0, lenInt = ballsSplit.length; j < lenInt; j++){
					if(!contentSet.contains(ballsSplit[j])){
						return false;
					}
				}
			}
		}
		
		return flag;
	}	
	
	/**
	 * 计算投注总注数
	 * @param orderContent 1-0-0-3-3-3-3-1-3-0-0-3-1-0
	 * @return
	 */
	public int calBallCounts(String orderContent, String playType) {
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		int sum = 0;
		for(int i = 0; i< ballContents.length; i++){
			sum += calZCSFC14CBallCount(ballContents[i], playType);
		}
		
		return sum;
	}
	
	private int calZCSFC14CBallCount(String orderContent, String playType){
		int count = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_DANSHI)){
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_FUSHI)){
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
			details.addAll(listZCSFC14CTicketDetail(ballContents[i], playType));
		}
		
		return details;
	}
	
	private List<String> listZCSFC14CTicketDetail(String orderContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_DANSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			for(int i = 0, len = ballContents.length; i < len; i++){
				details.add(ballContents[i]);				
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_FUSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			for(int i = 0, len = ballContents.length; i < len; i++){
				details = TicketWinningUtil.calContentList(details, ballContents[i]);
			}
		}		
		
		return details;
	}
	
	/**
	 * 计算是否中奖
	 * @param orderContent 1-0-0-3-3-3-3-1-3-0-0-3-1-0
	 * @param winningContent 1
	 * @return
	 */
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningContent, Map<Integer, Long> prizeAmountLevelMap){
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_DANSHI)){
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			for(int i = 0, len = ballContents.length; i < len; i++){
				prizeLevel = calSFCWinningTicketPrize(ballContents[i], winningContent);	
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_LZC_14_FUSHI)){
			List<String> ballContents = listTicketDetails(orderContent, playType);
			
			for(String ballContent : ballContents){
				prizeLevel = calSFCWinningTicketPrize(ballContent, winningContent);	
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


	private int calSFCWinningTicketPrize(String ballContent, String winningContent) {
		int prizeLevel = 0;
		ballContent = ballContent.replaceAll(TicketWinningConstantsUtil.SEPARATOR_DOUHAO, TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
		String[] winningBalls = winningContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		int winningCount = 0;
		int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
		int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBalls);
		
		for(int i = 0, len = balls.length; i < len; i++){
			if(ballsInt[i] == winningBallsInt[i]){
				winningCount++;
			}
		}
		
		if(winningCount == 13){
			prizeLevel = PrizeAmount.ZC_SFC14C_SECOND_PRIZE_LEVEL;
		}
		else if(winningCount == 14){
			prizeLevel = PrizeAmount.ZC_SFC14C_FIRST_PRIZE_LEVEL;
		}
		return prizeLevel;
	}
	
}
