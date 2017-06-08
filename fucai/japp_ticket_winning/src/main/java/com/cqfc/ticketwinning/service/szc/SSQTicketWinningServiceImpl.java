package com.cqfc.ticketwinning.service.szc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cqfc.ticketwinning.model.Prize;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.ticketwinning.util.CombinationUtil;
import com.cqfc.ticketwinning.util.PrizeAmount;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryPlayTypeConstants;

public class SSQTicketWinningServiceImpl implements ITicketWinningService{
	
	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_SINGLE)) {//双色球单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String blueBallContent = "";
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//红球不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用一个冒号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				
				if (balls.length != 2) {
					return false;
				}
				String[] redBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(redBalls)) {
					return false;
				}				
				
				int[] redBallsInt = TicketWinningUtil.str2IntArr(redBalls);				
				if (redBallsInt.length != 6) {
					return false;
				}				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(redBallsInt, 1, 33)) {
					return false;
				}
			
				blueBallContent = balls[1];
	
				if (blueBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length != 1){
					return false;
				}	
				if (Integer.valueOf(blueBallContent) < 10 && !blueBallContent.startsWith("0")){
					return false;
				}
				int blueBallInt = Integer.parseInt(blueBallContent);
				if (blueBallInt < 1 || blueBallInt > 16) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_COMPOUND)) {//双色球复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);

			//最多1注
			if (ballContents.length > 1) {
				return false;
			} 
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			//用一个冒号分隔
			if (balls.length != 2) {
				return false;
			}
			
			//红球蓝球不能重复且升序
			String redBallContent = balls[0];
			String blueBallContent = balls[1];
			
			String[] redBalls = redBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] blueBalls = blueBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(redBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(blueBalls)) {
				return false;
			}
			int[] redBallsInt = TicketWinningUtil.str2IntArr(redBalls);
			int[] blueBallsInt = TicketWinningUtil.str2IntArr(blueBalls);
			
			if (redBallsInt.length < 6 || redBallsInt.length > 33) {
				return false;
			}
			
			if (blueBallsInt.length < 1 || blueBallsInt.length > 16) {
				return false;
			}
			
			if (redBallsInt.length == 6 && blueBallsInt.length == 1) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(redBallsInt, 1, 33)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(blueBallsInt, 1, 16)) {
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_BILE_DRAG)) {//双色球胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String braveBallContent = "";
			String dragBallContent = "";
			String blueBallContent = "";
			
			//最多1注
			if (ballContents.length > 1) {
				flag = false;
			} 
			
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			//用两个冒号分隔
			if (balls.length != 3) {
				return false;
			}
			//胆码，拖码，蓝球不能重复且升序
			braveBallContent = balls[0];
			dragBallContent = balls[1];
			blueBallContent = balls[2];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] blueBalls = blueBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(blueBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			int[] blueBallsInt = TicketWinningUtil.str2IntArr(blueBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 33)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 33)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(blueBallsInt, 1, 16)) {
				return false;
			}
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen < 1 || braveLen > 5) {
				return false;
			}
			
			if (dragLen < (6 - braveLen) || dragLen > (33 - braveLen)){
				return false;
			}
			
			if (braveLen + dragLen == 6) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		}
		return flag;
	}

	/**
	 * 计算一张彩票中奖金额 
	 * @param orderContent  一张彩票投注内容
	 * @param playType      玩法
	 * @return
	 */
	@Override
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		Prize rePrize = new Prize();
		Prize prize = null;
		for (int i = 0, len = balls.length; i < len; i++) {
			prize = calSSQWinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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
	
	/**
	 * 计算投注总注数
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public int calBallCounts(String orderContent, String playType){
		int sum = 0;
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			sum += calSSQBallCount(ballContents[i], playType);
		}
		
		return sum;
	}
	
	private static int calSSQBallCount(String ballContent, String playType){
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_SINGLE)) {//双色球单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_COMPOUND)) {//双色球复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int redBallCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int blueBallCount = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(redBallCount, 6) * blueBallCount;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_BILE_DRAG)) {//双色球胆拖
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int braveBallCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int blueBallCount = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 6 - braveBallCount) * blueBallCount;
		}
		
		return count;
	}
	
	/**
	 * 计算每注详细内容
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public List<String> listTicketDetails(String orderContent, String playType){
		List<String> details = new ArrayList<String>();
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			details.addAll(listSSQTicketDetail(ballContents[i], playType));
		}
		
		return details;
	}
	
	private static List<String> listSSQTicketDetail(String ballContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_SINGLE)) {//双色球单式
			details.add(ballContent);				
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_COMPOUND)) {//双色球复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO); 
			String[] redBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] blueBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			int blueBallCount = blueBalls.length;
			
			List<String> combBalls = CombinationUtil.combine(redBalls, 6, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < combBalls.size(); i++){
				for(int j = 0; j < blueBallCount; j++){
					details.add(combBalls.get(i) + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + blueBalls[j]);
				}
			}
			combBalls.clear();
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_BILE_DRAG)) {//双色球胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String braveBallDetail = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] blueBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int blueBallCount = blueBalls.length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 6 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				for(int j = 0; j < blueBallCount; j++){
					details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i) + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + blueBalls[j]);
				}
			}
			combBalls.clear();
		}
		
		return details;
	}

	/**
	 * 根据玩法计算一注彩票中奖金额
	 * 
	 * @param ballContent
	 *            一注彩票投注内容
	 * @param playType
	 *            玩法
	 * @return
	 */
	private static Prize calSSQWinningTicketPrize(String ballContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_SINGLE)) {//双色球单式
			prizeLevel = calSSQWinningTicketPrizeLevel(ballContent, winningBallContent);
			if (prizeLevel > 0) {
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_COMPOUND)) {//双色球复式
			List<String> combBalls = listSSQTicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calSSQWinningTicketPrizeLevel(combBalls.get(i), winningBallContent);
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSQ_BILE_DRAG)) {//双色球胆拖
			List<String> combBalls = listSSQTicketDetail(ballContent, playType);
			
			for (int i = 0; i < combBalls.size(); i++) {
				prizeLevel = calSSQWinningTicketPrizeLevel(combBalls.get(i), winningBallContent);
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
	 * 计算一注双色球彩票中奖金额，彩票内容格式(01,02,03,04,05,06:07)
	 * 
	 * @param orderBallContent
	 *            投注彩票内容
	 * @return
	 */
	private static int calSSQWinningTicketPrizeLevel(String ballContent, String winningBallContent) {
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String blueBall = balls[1];
		String[] redBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String winningRedBallContent = winningBalls[0];
		String winningBlueBall = winningBalls[1];

		int redBallWinningCount = 0;
		int blueBallWinningCount = 0;
		for (int i = 0; i < redBalls.length; i++) {
			if (winningRedBallContent.contains(redBalls[i])) {
				redBallWinningCount++;
			}
		}

		if (blueBall.equals(winningBlueBall)) {
			blueBallWinningCount = 1;
		}

		return calSSQWinningPrizeLevel(redBallWinningCount, blueBallWinningCount);
	}

	/**
	 * 根据红球中奖个数和篮球中奖个数计算奖金
	 * 
	 * @param redBallWinningCount
	 *            红球中奖个数
	 * @param blueBallWinningCount
	 *            篮球中奖个数
	 * @return
	 */
	private static int calSSQWinningPrizeLevel(int redBallWinningCount,
			int blueBallWinningCount) {
		if (blueBallWinningCount == 0 && redBallWinningCount == 0) {       // 未中奖
			return 0;
		} else if (blueBallWinningCount == 1 && redBallWinningCount < 3) { // 六等奖
			return PrizeAmount.SSQ_SIXTH_PRIZE_LEVEL;
		} else if ((blueBallWinningCount == 1 && redBallWinningCount == 3)
				|| (blueBallWinningCount == 0 && redBallWinningCount == 4)) { // 五等奖
			return PrizeAmount.SSQ_FIFTH_PRIZE_LEVEL;
		} else if ((blueBallWinningCount == 1 && redBallWinningCount == 4)
				||  (blueBallWinningCount == 0 && redBallWinningCount == 5)) { // 四等奖
			return PrizeAmount.SSQ_FOURTH_PRIZE_LEVEL;
		} else if (blueBallWinningCount == 1 && redBallWinningCount == 5) { // 三等奖
			return PrizeAmount.SSQ_THIRD_PRIZE_LEVEL;						
		} else if (blueBallWinningCount == 0 && redBallWinningCount == 6) { // 二等奖
			return PrizeAmount.SSQ_SECOND_PRIZE_LEVEL;						
		} else if (blueBallWinningCount == 1 && redBallWinningCount == 6) { // 一等奖
			return PrizeAmount.SSQ_FIRST_PRIZE_LEVEL;
		}

		return 0;
	}
}
