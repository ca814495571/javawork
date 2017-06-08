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

public class PL5TicketWinningServiceImpl implements ITicketWinningService{

	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;
		
		try {
			if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_DANSHI)) {// 排列五单式
				String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
				String ballContent = "";
	
				if (ballContents.length > 5) {
					return false;
				}
	
				for (int i = 0; i < ballContents.length; i++) {
					ballContent = ballContents[i];
					
					String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
					if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
						return false;
					}
					int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
					
					if (ballsInt.length != 5) {
						return false;
					}
					
					if (!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)) {
						return false;
					}
				}
			} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_FUSHI)) {// 排列五复式
				String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
	
				if (ballContents.length > 1) {
					return false;
				} 
				
				String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (balls.length != 5) {
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
		}
		catch (Exception e) {
			flag = false;
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
			prize = calPL5WinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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

	private Prize calPL5WinningTicketPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_DANSHI)){// 排列五单式
			prizeLevel = calPL5WinningTicketPrizeLevel(ballContent, winningBallContent);
			if (prizeLevel > 0) {
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_FUSHI)){// 排列五复式		
			List<String> combBalls = listPL5TicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calPL5WinningTicketPrizeLevel(combBalls.get(i), winningBallContent);
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

	private int calPL5WinningTicketPrizeLevel(String orderBallContent,
			String winningBallContent) {
		winningBallContent = winningBallContent.replace(",", ":");
		if(orderBallContent.equals(winningBallContent)){
			return PrizeAmount.PL5_FIRST_PRIZE_LEVEL;
		}
		
		return 0;

	}

	@Override
	public int calBallCounts(String orderContent, String playType) {
		int sum = 0;
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			sum += calPL5BallCount(ballContents[i], playType);
		}
		
		return sum;
	}

	private int calPL5BallCount(String ballContent, String playType) {
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_DANSHI)) {// 排列五单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_FUSHI)) {// 排列五复式
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
			details.addAll(listPL5TicketDetail(ballContents[i], playType));
		}
		
		return details;
	}

	private List<String> listPL5TicketDetail(String ballContent,
			String playType) {
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_DANSHI)) {// 排列五单式
			details.add(ballContent);				
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL5_FUSHI)) {// 排列五复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);

			String[] geBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] qianBalls = balls[3].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] wanBalls = balls[4].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			String geDetail = "";
			String shiDetail = "";
			String baiDetail = "";
			String qianDetail = "";
			for(int ge = 0, geLen = geBalls.length; ge < geLen; ge++){
				geDetail = geBalls[ge] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
				for(int shi = 0, shiLen = shiBalls.length; shi < shiLen; shi++){
					shiDetail = geDetail + shiBalls[shi] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
					for(int bai = 0, baiLen = baiBalls.length; bai < baiLen; bai++){
						baiDetail = shiDetail + baiBalls[bai] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
						for(int qian = 0, qianLen = qianBalls.length; qian < qianLen; qian++){
							qianDetail = baiDetail + qianBalls[qian] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
							for(int wan = 0, wanLen = wanBalls.length; wan < wanLen; wan++){
								details.add(qianDetail + wanBalls[wan]);
							}
						}
					}
				}
			}
			
		} 
		
		return details;
	}

}
