package com.cqfc.ticketwinning.service.szc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cqfc.ticketwinning.model.Prize;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.ticketwinning.util.PrizeAmount;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryPlayTypeConstants;

public class QXCTicketWinningServiceImpl implements ITicketWinningService{

	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;

		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_DANSHI)) {// 七星彩单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String ballContent = "";

			if (ballContents.length > 5) {
				return false;
			}

			for (int i = 0; i < ballContents.length; i++) {
				ballContent = ballContents[i];
				
				if(ballContent.length() != 13){
					return false;
				}
				
				String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
				
				if (ballsInt.length != 7) {
					return false;
				}
				
				if (!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_FUSHI)) {// 七星彩复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);

			if (ballContents.length > 1) {
				return false;
			} 
			if((orderContent.length() - 13) % 2 != 0){
				return false;
			}
			
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 7) {
				return false;
			}
			for (int i = 0; i < balls.length; i++) {
				String ballContent = balls[i];
				String[] ballSplit = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(ballSplit)) {
					return false;
				}
				int[] ballsInt = TicketWinningUtil.str2IntArr(ballSplit);
				
				if (ballsInt.length < 1) {
					return false;
				}
					
				if (!TicketWinningUtil.isBallContentASCAndInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} 
		return flag;
	}

	@Override
	public Prize calTicketWinningAmount(String orderContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		Prize rePrize = new Prize();
		Prize prize = null;
		for(int i = 0, len = balls.length; i< len; i++){
			prize = calQXCWinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
			if(prize.isPrize()){
				if(prize.getPrizeAmount() == 0){
					rePrize.setPrize(true);
					rePrize.setPrizeAmount(0);
					break;
				}
				else{
					rePrize.setPrize(true);
					rePrize.setPrizeAmount(rePrize.getPrizeAmount() + prize.getPrizeAmount());
				}
			}
		}
		return rePrize;
	}

	private Prize calQXCWinningTicketPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_DANSHI)){//七乐彩单式
			prizeLevel = calQXCWinningTicketPrizeLevel(ballContent, winningBallContent);
			if (prizeLevel > 0) {
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_FUSHI)){//七乐彩复式			
			List<String> combBalls = listQXCTicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calQXCWinningTicketPrizeLevel(combBalls.get(i), winningBallContent);
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

	private int calQXCWinningTicketPrizeLevel(String orderBallContent,
			String winningBallContent) {

		int count = 0;
		int tempCount = 0;
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO); 
		String[] orderBalls = orderBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO); 
	
		if(winningBalls[0].equals(orderBalls[0])){
			count++;
		}
		
		for(int i = 1; i < winningBalls.length; i++){
			if(winningBalls[i-1].equals(orderBalls[i-1]) && winningBalls[i].equals(orderBalls[i])){
				if(count == 0){
					count++;
				}
				count++;
			}
			else{
				if(count >= 2){
					tempCount = count;
					count = 0;
				}
			}
		}
		
		if(tempCount > count){
			count = tempCount;
		}
		
		return calQXCWinningPrizeLevel(count);
	}

	private int calQXCWinningPrizeLevel(int winningCount) {
		if(winningCount < 2){
			return 0;
		}
		else if(winningCount == 2){
			return PrizeAmount.QXC_SIXTH_PRIZE_LEVEL;
		}
		else if(winningCount == 3){
			return PrizeAmount.QXC_FIFTH_PRIZE_LEVEL;
		}
		else if(winningCount == 4){
			return PrizeAmount.QXC_FOURTH_PRIZE_LEVEL;
		}
		else if(winningCount == 5){
			return PrizeAmount.QXC_THIRD_PRIZE_LEVEL;
		}
		else if(winningCount == 6){
			return PrizeAmount.QXC_SECOND_PRIZE_LEVEL;
		}
		else if(winningCount == 7){
			return PrizeAmount.QXC_FIRST_PRIZE_LEVEL;
		}
		
		return 0;
	}

	@Override
	public int calBallCounts(String orderContent, String playType) {
		int sum = 0;
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			sum += calQXCBallCount(ballContents[i], playType);
		}
		
		return sum;
	}

	private int calQXCBallCount(String ballContent, String playType) {
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_DANSHI)) {// 七星彩单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_FUSHI)) {// 七星彩复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = 1;
			for(int i = 0; i < balls.length; i++){
				String[] ballSplit = balls[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				ballCount *= ballSplit.length;
			}
			count = ballCount;
		} 
		
		return count;
	}

	@Override
	public List<String> listTicketDetails(String orderContent, String playType) {
		List<String> details = new ArrayList<String>();
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			details.addAll(listQXCTicketDetail(ballContents[i], playType));
		}
		
		return details;
	}

	private List<String> listQXCTicketDetail(String ballContent,
			String playType) {
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_DANSHI)) {// 七星彩单式
			details.add(ballContent);				
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QXC_FUSHI)) {// 七星彩复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			String[] geBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] qianBalls = balls[3].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] wanBalls = balls[4].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] shiWanBalls = balls[5].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] baiWanBalls = balls[6].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			String geDetail = "";
			String shiDetail = "";
			String baiDetail = "";
			String qianDetail = "";
			String wanDetail = "";
			String shiWanDetail = "";
			for(int ge = 0, geLen = geBalls.length; ge < geLen; ge++){
				geDetail = geBalls[ge] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
				for(int shi = 0, shiLen = shiBalls.length; shi < shiLen; shi++){
					shiDetail = geDetail + shiBalls[shi] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
					for(int bai = 0, baiLen = baiBalls.length; bai < baiLen; bai++){
						baiDetail = shiDetail + baiBalls[bai] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
						for(int qian = 0, qianLen = qianBalls.length; qian < qianLen; qian++){
							qianDetail = baiDetail + qianBalls[qian] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
							for(int wan = 0, wanLen = wanBalls.length; wan < wanLen; wan++){
								wanDetail = qianDetail + wanBalls[wan] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
								for(int shiWan = 0, shiWanLen = shiWanBalls.length; shiWan < shiWanLen; shiWan++){
									shiWanDetail = wanDetail + shiWanBalls[shiWan] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
									for(int baiWan = 0, baiWanLen = baiWanBalls.length; baiWan < baiWanLen; baiWan++){										
										details.add(shiWanDetail + baiWanBalls[baiWan]);
									}
								}
							}
						}
					}
				}
			}
		} 
		
		return details;
	}

}
