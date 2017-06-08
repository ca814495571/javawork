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

public class PL3TicketWinningServiceImpl implements ITicketWinningService{

	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;

		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN)) {// 排列三直选
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			
			for (int i = 0; i < ballContents.length; i++) {				
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (balls.length != 3) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_FUSHI)) {// 排列三直选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if (ballContents.length > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] ballSplit = null;
			if (balls.length != 3) {
				return false;
			}
			if (orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length == 1) {
				return false;
			}
			for (int i = 0; i < balls.length; i++) {				
				ballSplit = balls[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(ballSplit)) {
					return false;
				}
				int[] ballsInt = TicketWinningUtil.str2IntArr(ballSplit);
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE)) {// 排列三直选组合
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			
			
			if (ballContents.length > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 3){
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 0, 9)) {
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_HEZHI)){// 排列三直选和值
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);

			if (ballContents.length > 1) {
				return false;
			}
			
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(orderContent)) {
				return false;
			}
			
			if (Integer.valueOf(orderContent) < 0 || Integer.valueOf(orderContent) > 27) {
				return false;
			}			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE_DANTUO)){// 排列三直选组合胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);

			if (ballContents.length > 1) {
				return false;
			}
			
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			//用一个冒号分隔
			if (balls.length != 2) {
				return false;
			}
			
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentInRange(braveBallsInt, 0, 9)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentInRange(dragBallsInt, 0, 9)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen < 1 || braveLen > 2) {
				return false;
			}
			
			if (dragLen < (3 - braveLen)){
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
		}else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUXUAN_DANSHI)){// 排列三组选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			
			for(int i = 0; i < ballContents.length; i++){	
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (ballsInt.length != 3){
					return false;
				}
				if (!TicketWinningUtil.isBallContentASCAndInRange(ballsInt, 0, 9)){
					return false;
				}
				if ((ballsInt[0] == ballsInt[1]) && (ballsInt[1] == ballsInt[2]) && (ballsInt[0] == ballsInt[2])){
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_FUSHI)){// 排列三组三复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			for(int i = 0; i < ballContents.length; i++){
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (balls.length < 2) {
					return false;
				}
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_DANTUO)){// 排列三组三胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			
			
			if(ballContents.length > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			//用一个冒号分隔
			if (balls.length != 2) {
				return false;
			}
			
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentInRange(braveBallsInt, 0, 9)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 0, 9)) {
				return false;
			}

			if (braveBallsInt.length != 1) {
				return false;
			}	
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_FUSHI)){// 排列三组六复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			for(int i = 0; i < ballContents.length; i++){
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				
				if (ballsInt.length < 4 || ballsInt.length > 10) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_DANTUO)){// 排列三组六胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			
			
			if(ballContents.length > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			//用一个冒号分隔
			if (balls.length != 2) {
				return false;
			}
			
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 0, 9)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 0, 9)) {
				return false;
			}

			if (braveBallsInt.length < 1 || braveBallsInt.length > 2) {
				return false;
			}
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_DANSHI)){// 排列三组选和值单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			
			if (len > 1) {
				return false;
			}
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(orderContent)) {
				return false;
			}
			if (Integer.valueOf(orderContent) < 1 || Integer.valueOf(orderContent) > 26) {
				return false;
			}		
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_FUSHI)){// 排列三组选和值复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			
			if (len > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			if (balls.length < 2) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 26)) {
				return false;
			}	
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_DANSHI)){// 排列六组选和值单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			
			if (len > 1) {
				return false;
			}
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(orderContent)) {
				return false;
			}
			if (Integer.valueOf(orderContent) < 3 || Integer.valueOf(orderContent) > 24) {
				return false;
			}		
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_FUSHI)){// 排列六组选和值复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			
			if (len > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			if (balls.length < 2) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 3, 24)) {
				return false;
			}	
		} 
		
		return flag;
	}
	
	@Override
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		Prize rePrize = new Prize();
		Prize prize = null;
		for(int i = 0, len = balls.length; i< len; i++){
			prize = calPL3WinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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


	public Prize calPL3WinningTicketPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN)) {// 排列三直选
			prizeLevel = calPL3ZhiXuan(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_FUSHI)) {// 排列三直选复式
			List<String> combBalls = listPL3TicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calPL3ZhiXuan(combBalls.get(i), winningBallContent);
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE)) {// 排列三直选组合
			List<String> combBalls = listPL3TicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calPL3ZhiXuan(combBalls.get(i), winningBallContent);
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_HEZHI)){// 排列三直选和值		
			prizeLevel = calPL3ZhiXuanHeZhi(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);				
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE_DANTUO)){// 排列三直选组合胆拖
			List<String> combBalls = listPL3TicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calPL3ZhiXuan(combBalls.get(i), winningBallContent);
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUXUAN_DANSHI)){// 排列三组选单式
			//注意：组三单式和组六单式玩法类型都是组选单式，也可以相互混投
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if((balls[0].equals(balls[1])) || (balls[1].equals(balls[2])) || (balls[0].equals(balls[2]))){//组三单式
				prizeLevel = calPL3Group3(ballContent, winningBallContent);
				if(prizeLevel > 0){
					prize.setPrize(true);
					prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
				}
			}
			else{
				String sortContent = TicketWinningUtil.sortBallContent(winningBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if(ballContent.equals(sortContent)){
					prize.setPrize(true);
					prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.PL3_ZULIU_LEVEL));
				}
			}			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_FUSHI)){// 排列三组三复式
			prize = calZuSanPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_DANTUO)){// 排列三组三胆拖
			prize = calZuSanPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_FUSHI)){// 排列三组六复式
			prize = calZuLiuPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_DANTUO)){// 排列三组六胆拖
			prize = calZuLiuPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_DANSHI)){// 排列三组三和值单式
			prize = calZuSanPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_FUSHI)){// 排列三组三和值复式
			prize = calZuSanPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_DANSHI)){// 排列三组六和值单式
			prize = calZuLiuPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_FUSHI)){// 排列三组六和值复式
			prize = calZuLiuPrize(ballContent, playType, winningBallContent, prizeAmountLevelMap);
		} 
		return prize;
	}
	
	private Prize calZuSanPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		List<String> combBalls = listPL3TicketDetail(ballContent, playType);

		for (int i = 0, size = combBalls.size(); i < size; i++) {
			prizeLevel = calPL3Group3(combBalls.get(i), winningBallContent);
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
		return prize;
	}
	
	private Prize calZuLiuPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		List<String> combBalls = listPL3TicketDetail(ballContent, playType);

		for (int i = 0, size = combBalls.size(); i < size; i++) {
			prizeLevel = calPL3ZuLiu(combBalls.get(i), winningBallContent);
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
		return prize;
	}

	@Override
	public int calBallCounts(String orderContent, String playType) {
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		int sum = 0;
		for(int i = 0; i< ballContents.length; i++){
			sum += calPL3BallCount(ballContents[i], playType);
		}
		
		return sum;
	}

	private int calPL3BallCount(String orderContent, String playType) {
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN)) {// 排列三直选
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_FUSHI)) {// 排列三直选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			int baiBallsCount = ballContents[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int shiBallsCount = ballContents[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int geBallsCount = ballContents[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = baiBallsCount * shiBallsCount * geBallsCount;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE)) {// 排列三直选组合
			int ballCount = 0;
			
			ballCount = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;			
			count = TicketWinningUtil.getCombNum(ballCount, ballCount - 3) * 6;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_HEZHI)){// 排列三直选和值
			count = TicketWinningUtil.getPL3ZhiXuanHeZhi(Integer.valueOf(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE_DANTUO)){// 排列三直选组合胆拖
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int braveBallLen = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallLen = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallLen, 3 - braveBallLen) * 6;			
		}else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUXUAN_DANSHI)){// 排列三组选单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_FUSHI)){// 排列三组三复式
			int ballCount = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 2) * 2;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_DANTUO)){// 排列三组三胆拖
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int dragBallLen = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = dragBallLen * 2;
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_FUSHI)){// 排列三组六复式
			int ballCount = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 3);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_DANTUO)){// 排列三组六胆拖
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int braveBallLen = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallLen = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallLen, 3 - braveBallLen);	
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_DANSHI)){// 排列三组三和值单式			
			count = TicketWinningUtil.getPL3ZuSanHeZhi(Integer.valueOf(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_FUSHI)){// 排列三组三和值复式
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, len = balls.length; i < len; i++){
				count += TicketWinningUtil.getPL3ZuSanHeZhi(Integer.valueOf(balls[i]));
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_DANSHI)){// 排列三组六和值单式			
			count = TicketWinningUtil.getPL3ZuLiuHeZhi(Integer.valueOf(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_FUSHI)){// 排列三组六和值复式
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, len = balls.length; i < len; i++){
				count += TicketWinningUtil.getPL3ZuLiuHeZhi(Integer.valueOf(balls[i]));
			}
		} 
		return count;
	}
	
	@Override
	public List<String> listTicketDetails(String orderContent, String playType) {
		List<String> details = new ArrayList<String>();
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			details.addAll(listPL3TicketDetail(ballContents[i], playType));
		}
		
		return details;
	}


	private List<String> listPL3TicketDetail(String ballContent, String playType) {
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN)) {// 排列三直选
			details.add(ballContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_FUSHI)) {// 排列三直选
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] baiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] geBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, baiLen = baiBalls.length; i < baiLen; i++){
				for(int j = 0, shiLen = shiBalls.length; j < shiLen; j++){
					for(int k = 0, geLen = geBalls.length; k < geLen; k++){
						details.add(baiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + shiBalls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[k]);
					}
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE)) {// 排列三直选组合
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);

			List<String> combs = CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			for(String comb : combs){
				String[] combBalls = comb.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				details.add(comb);
				details.add(combBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[1]);
				details.add(combBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[2]);
				details.add(combBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0]);
				details.add(combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[1]);
				details.add(combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0]);
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_HEZHI)){// 排列三直选和值
			details.addAll(TicketWinningUtil.getPL3ZhiXuanZuHe(Integer.valueOf(ballContent)));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZHIXUAN_ZUHE_DANTUO)){// 排列三直选组合胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] braveBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			List<String> combDragBalls = CombinationUtil.combine(dragBalls, 3-braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			List<String> combs = new ArrayList<String>();
			String content = "";
			
			for(int i = 0; i < combDragBalls.size(); i++){
				if(braveBalls.length == 1){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combDragBalls.get(i);
				}
				else if(braveBalls.length == 2){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + braveBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combDragBalls.get(i); 
				}
				else{
					content = combDragBalls.get(i);
				}
				
				combs.add(content);
			}
			
			for(String comb : combs){
				String[] combBalls = comb.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				details.add(comb);
				details.add(combBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[1]);
				details.add(combBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[2]);
				details.add(combBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0]);
				details.add(combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[1]);
				details.add(combBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combBalls[0]);
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUXUAN_DANSHI)){// 排列三组选单式
			details.add(ballContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_FUSHI)){// 排列三组三复式
			details.addAll(TicketWinningUtil.getGroup3Compound(ballContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU3_DANTUO)){// 排列三组三胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String braveBallContent = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, dragLen = dragBalls.length; i < dragLen; i++){
				details.add(sortPL3Ball(braveBallContent+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+braveBallContent+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+dragBalls[i]));
				details.add(sortPL3Ball(braveBallContent+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+dragBalls[i]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+dragBalls[i]));
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_FUSHI)){// 排列三组六复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			details.addAll(CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZU6_DANTUO)){// 排列三组六胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] braveBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if(braveBalls.length == 1){
				List<String> combDragBalls = CombinationUtil.combine(dragBalls, 2, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				for(int i = 0, combLen = combDragBalls.size(); i < combLen; i++){
					details.add(sortPL3Ball(braveBalls[0]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+combDragBalls.get(i)));
				}
			}
			else{
				for(int j = 0, dragLen = dragBalls.length; j < dragLen; j++){
					details.add(sortPL3Ball(braveBalls[0]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+braveBalls[1]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+dragBalls[j]));
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_DANSHI)){// 排列三组三和值单式
			details.addAll(TicketWinningUtil.getGroup3AndValue(ballContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZUSAN_HEZHI_FUSHI)){// 排列三组三和值复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0, len = balls.length; i < len; i++){
				details.addAll(TicketWinningUtil.getGroup3AndValue(balls[i]));
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_DANSHI)){// 排列三组六和值单式
			details.addAll(TicketWinningUtil.getPL3ZuLiuHeZhiDetails(ballContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_PL3_ZULIU_HEZHI_FUSHI)){// 排列三组六和值复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0, len = balls.length; i < len; i++){
				details.addAll(TicketWinningUtil.getPL3ZuLiuHeZhiDetails(balls[i]));
			}
		} 
		return details;
	}
	
	/**
	 * 计算一注排列3直/单选彩票中奖奖级，彩票内容格式(1:2:3)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calPL3ZhiXuan(String orderBallContent, String winningBallContent){
		winningBallContent = winningBallContent.replace(",", ":");
		if(orderBallContent.equals(winningBallContent)){
			return PrizeAmount.PL3_ZHIXUAN_LEVEL;
		}
		
		return 0;		
	}
	
	/**
	 * 计算一注排列3直/单选彩票中奖奖级，彩票内容格式(9)
	 * @param orderBallContent
	 * @param winningBallContent
	 * @return
	 */
	private static int calPL3ZhiXuanHeZhi(String orderBallContent, String winningBallContent){
		String[] winngBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		int sum = Integer.valueOf(winngBalls[0]) + Integer.valueOf(winngBalls[1]) + Integer.valueOf(winngBalls[2]);
		
		if(Integer.valueOf(orderBallContent) == sum){
			return PrizeAmount.PL3_ZHIXUAN_LEVEL;
		}
		
		return 0;
	}
	
	/**
	 * 计算一注排列3组三彩票中奖奖级，彩票内容格式(1:2:2)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calPL3Group3(String orderBallContent, String winningBallContent){
		winningBallContent = winningBallContent.replace(",", ":");
		String winningContent = sortPL3Ball(winningBallContent);
		orderBallContent = sortPL3Ball(orderBallContent);
		String[] winningBalls = winningContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		if(!(winningBalls[0].equals(winningBalls[1])  || winningBalls[1].equals(winningBalls[2]))){
			return 0;
		}
		else{
			if(orderBallContent.equals(winningContent)){
				return PrizeAmount.PL3_ZUSAN_LEVEL;
			}
		}
		
		return 0;		
	}
	
	/**
	 * 计算一注排列3组六彩票中奖奖级，彩票内容格式(1:2:3)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calPL3ZuLiu(String orderBallContent, String winningBallContent){
		winningBallContent = winningBallContent.replace(",", ":");
		String winningContent = sortPL3Ball(winningBallContent);
		orderBallContent = sortPL3Ball(orderBallContent);
		String[] winningBalls = winningContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		if(winningBalls[0].equals(winningBalls[1]) || winningBalls[1].equals(winningBalls[2]) || winningBalls[0].equals(winningBalls[2])){
			return 0;
		}
		else{
			if(orderBallContent.equals(winningContent)){
				return PrizeAmount.PL3_ZULIU_LEVEL;
			}
		}
		
		return 0;		
	}
	
	/**
	 * 从小到大排序
	 * @param ballContent
	 * @return
	 */
	private static String sortPL3Ball(String ballContent){
		return TicketWinningUtil.sortBallContent(ballContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
	}
	
	/**
	 * 判断奖级
	 * @param prizeLevel
	 * @param prize
	 */
	private static void checkPrizeLevel(int prizeLevel, Prize prize, Map<Integer, Long> prizeAmountLevelMap){
		if(prizeLevel > 0){
			prize.setPrize(true);
			prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
		}
		else{
			prize.setPrize(false);
			prize.setPrizeAmount(0);	
		}
	}

}
