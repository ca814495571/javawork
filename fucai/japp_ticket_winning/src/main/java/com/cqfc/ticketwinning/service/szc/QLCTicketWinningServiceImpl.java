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

public class QLCTicketWinningServiceImpl implements ITicketWinningService{
	
	/**
	 * 校验内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;

		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_SINGLE)) {//七乐彩单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String ballContent = "";

			if (ballContents.length > 5) {
				return false;
			}

			for (int i = 0; i < ballContents.length; i++) {
				ballContent = ballContents[i];
				
				String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
				
				if (ballsInt.length != 7) {
					return false;
				}
				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 30)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_COMPOUND)) {//七乐彩复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);

			if (ballContents.length > 1) {
				return false;
			} 
			
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if (ballsInt.length < 8 || ballsInt.length > 16) {
				return false;
			}
				
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 30)) {
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_BILE_DRAG)) {//七乐彩胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String braveBallContent = null;
			String dragBallContent = null;
			//投注注数只能一注
			if (ballContents.length > 1) {
				flag = false;
			}
			//用一个冒号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2){
				return false;
			}		
			
			braveBallContent = balls[0];
			dragBallContent = balls[1];	
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			int[] braveInts = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragInts = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (braveInts.length < 1 || braveInts.length > 6) {
				return false;
			}
			
			if (dragInts.length < 1 || dragInts.length > 29) {
				return false;
			}
			
			if ((braveInts.length + dragInts.length) < 8 || (braveInts.length + dragInts.length) > 30) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveInts, 1, 30)){
				return false;
			}
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragInts, 1, 30)){
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
	 * @param orderContent
	 *            一张彩票投注内容
	 * @param playType
	 *            玩法
	 * @return
	 */
	@Override
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		Prize rePrize = new Prize();
		Prize prize = null;
		for(int i = 0, len = balls.length; i< len; i++){
			prize = calQLCWinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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
			sum += calQLCBallCount(ballContents[i], playType);
		}
		
		return sum;
	}
	
	private static int calQLCBallCount(String ballContent, String playType){
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_SINGLE)) {//七乐彩单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_COMPOUND)) {//七乐彩复式
			int ballCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 7);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_BILE_DRAG)) {//七乐彩胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 7 - braveBallCount);
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
			details.addAll(listQLCTicketDetail(ballContents[i], playType));
		}
		
		return details;
	}
	
	private static List<String> listQLCTicketDetail(String ballContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_SINGLE)) {//七乐彩单式
			details.add(ballContent);				
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_COMPOUND)) {//七乐彩复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 7, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_BILE_DRAG)) {//七乐彩胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 7 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();
		}
		
		return details;
	}
	
	/**
	 * 根据玩法计算一注彩票中奖金额
	 * @param ballContent 一注彩票投注内容
	 * @param playType 玩法
	 * @return
	 */
	private static Prize calQLCWinningTicketPrize(String ballContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_SINGLE)){//七乐彩单式
			prizeLevel = calQLCWinningTicketPrizeLevel(ballContent, winningBallContent);
			if (prizeLevel > 0) {
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_COMPOUND)){//七乐彩复式			
			List<String> combBalls = listQLCTicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calQLCWinningTicketPrizeLevel(combBalls.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_QLC_BILE_DRAG)){//七乐彩胆拖			
			List<String> combBalls = listQLCTicketDetail(ballContent, playType);
			
			for (int i = 0; i < combBalls.size(); i++) {
				prizeLevel = calQLCWinningTicketPrizeLevel(combBalls.get(i), winningBallContent);
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
	 * 计算一注双色球彩票中奖奖级，彩票内容格式(01,02,03,04,05,06,07)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calQLCWinningTicketPrizeLevel(String orderBallContent, String winningBallContent){	
		String winningBaseBallContent = winningBallContent.substring(0, winningBallContent.lastIndexOf(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
		String winningSpecBall = winningBallContent.substring(winningBallContent.lastIndexOf(TicketWinningConstantsUtil.SEPARATOR_DOUHAO)+1);
		String unWinngingBaseBallContent = "";
		String[] orderBalls = orderBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO); 
		int length = orderBalls.length;
		int baseBallWinningCount = 0;
		int specBallWinningCount = 0;
		
		for(int i = 0; i < length; i++){
			if(winningBaseBallContent.contains(orderBalls[i])){
				baseBallWinningCount++;
			}
			else{
				unWinngingBaseBallContent += orderBalls[i]+TicketWinningConstantsUtil.SEPARATOR_DOUHAO;
			}
		}
		
		if(unWinngingBaseBallContent.contains(winningSpecBall)){
			specBallWinningCount = 1;
		}		
		
		return calQLCWinningPrizeLevel(baseBallWinningCount, specBallWinningCount);
	}
	
	/**
	 * 根据基本(红)球中奖个数和特殊(蓝)球中奖个数计算奖金
	 * @param baseBallWinningCount
	 * @param specBallWinningCount
	 * @return
	 */
	private static int calQLCWinningPrizeLevel(int baseBallWinningCount, int specBallWinningCount){
		
		if(baseBallWinningCount < 4){
			return 0;
		}
		else if(baseBallWinningCount == 4 && specBallWinningCount == 0){
			return PrizeAmount.QLC_SEVENTH_PRIZE_LEVEL;
		}
		else if(baseBallWinningCount == 4 && specBallWinningCount == 1){
			return PrizeAmount.QLC_SIXTH_PRIZE_LEVEL;
		}
		else if(baseBallWinningCount == 5 && specBallWinningCount == 0){
			return PrizeAmount.QLC_FIFTH_PRIZE_LEVEL;
		}
		else if(baseBallWinningCount == 5 && specBallWinningCount == 1){
			return PrizeAmount.QLC_FOURTH_PRIZE_LEVEL;
		}
		else if(baseBallWinningCount == 6 && specBallWinningCount == 0){
			return PrizeAmount.QLC_THIRD_PRIZE_LEVEL;
		}
		else if(baseBallWinningCount == 6 && specBallWinningCount == 1){
			return PrizeAmount.QLC_SECOND_PRIZE_LEVEL;
		}
		else if(baseBallWinningCount == 7){
			return PrizeAmount.QLC_FIRST_PRIZE_LEVEL;
		}
		
		return 0;
	}
	
	
}
