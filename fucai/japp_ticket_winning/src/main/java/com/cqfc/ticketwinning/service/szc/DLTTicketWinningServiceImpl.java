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

public class DLTTicketWinningServiceImpl implements ITicketWinningService{

	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI) 
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA)) {//大乐透单式 和 单式追加
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//前区不能重复且升序
			for (int i = 0, len = ballContents.length; i < len; i++) {
				//用一个冒号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				
				if (balls.length != 2) {
					return false;
				}
				if(balls[0].length() != 14){
					return false;
				}
				if(balls[1].length() != 5){
					return false;
				}
				String[] preBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(preBalls)) {
					return false;
				}				
				
				int[] preBallsInt = TicketWinningUtil.str2IntArr(preBalls);				
				if (preBallsInt.length != 5) {
					return false;
				}				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(preBallsInt, 1, 35)) {
					return false;
				}
			
				String[] sufBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);	
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(sufBalls)) {
					return false;
				}				
				
				int[] sufBallsInt = TicketWinningUtil.str2IntArr(sufBalls);				
				if (sufBallsInt.length != 2) {
					return false;
				}				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(sufBallsInt, 1, 12)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_ZHUIJIA)) {//大乐透复式 和 复式追加
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
			
			//前区蓝球不能重复且升序
			String preBallContent = balls[0];
			String sufBallContent = balls[1];
			
			if((preBallContent.length() - 14) % 3 != 0){
				return false;
			}
			if((sufBallContent.length() - 5) % 3 != 0){
				return false;
			}
			String[] preBalls = preBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] sufBalls = sufBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(preBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(sufBalls)) {
				return false;
			}
			int[] preBallsInt = TicketWinningUtil.str2IntArr(preBalls);
			int[] sufBallsInt = TicketWinningUtil.str2IntArr(sufBalls);
			
			if (preBallsInt.length < 5 || preBallsInt.length > 35) {
				return false;
			}
			
			if (sufBallsInt.length < 2 || sufBallsInt.length > 12) {
				return false;
			}
			
			if (preBallsInt.length == 5 && sufBallsInt.length == 2) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(preBallsInt, 1, 35)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(sufBallsInt, 1, 12)) {
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO_ZHUIJIA)) {//大乐透复式胆拖 和 大乐透复式胆拖追加 
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String preBallContent = "";
			String sufBallContent = "";
			String preBraveBallContent = "";
			String preDragBallContent = "";
			String sufBraveBallContent = "";
			String sufDragBallContent = "";
			
			//最多1注
			if (ballContents.length > 1) {
				flag = false;
			} 
			
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			//用冒号分隔前区后区
			if (balls.length != 2) {
				return false;
			}
			preBallContent = balls[0];
			sufBallContent = balls[1];
			
			String[] preBalls = preBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] sufBalls = sufBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(preBalls.length < 2 && sufBalls.length < 2){
				return false;
			}
			
			//胆码，拖码不能重复且升序
			if(preBalls.length == 2){
				preBraveBallContent = preBalls[0];
				preDragBallContent = preBalls[1];
				
				if(!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(preBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO))) {
					return false;
				}
				if(!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(preDragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO))) {
					return false;
				}
				int[] preBraveBallsInt = TicketWinningUtil.str2IntArr(preBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				int[] preDragBallsInt = TicketWinningUtil.str2IntArr(preDragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				
				if (preBraveBallsInt.length < 1 || preBraveBallsInt.length > 4) {
					return false;
				}
				if ((preBraveBallsInt.length + preDragBallsInt.length) < 6) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(preBraveBallsInt, 1, 35)) {
					return false;
				}
				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(preDragBallsInt, 1, 35)) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentRepeat(preBraveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + preDragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
					return false;
				}
			}
			else{
				preBraveBallContent = preBalls[0];
				
				if(!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(preBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO))) {
					return false;
				}
				int[] preBraveBallsInt = TicketWinningUtil.str2IntArr(preBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				if (preBraveBallsInt.length != 5) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(preBraveBallsInt, 1, 35)) {
					return false;
				}
			}
			
			if(sufBalls.length == 2){
				sufBraveBallContent = sufBalls[0];
				sufDragBallContent = sufBalls[1];
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(sufBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO))) {
					return false;
				}
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(sufDragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO))) {
					return false;
				}
				int[] sufBraveBallsInt = TicketWinningUtil.str2IntArr(sufBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				int[] sufDragBallsInt = TicketWinningUtil.str2IntArr(sufDragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				
				if (sufBraveBallsInt.length != 1) {
					return false;
				}
				if ((sufBraveBallsInt.length + sufDragBallsInt.length) < 3) {
					return false;
				} 
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(sufBraveBallsInt, 1, 12)) {
					return false;
				}
				
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(sufDragBallsInt, 1, 12)) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentRepeat(sufBraveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + sufDragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
					return false;
				}
			}
			else{
				sufBraveBallContent = sufBalls[0];
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(sufBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO))) {
					return false;
				}
				int[] sufBraveBallsInt = TicketWinningUtil.str2IntArr(sufBraveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO));
				if (sufBraveBallsInt.length != 2){
					return false;
				}
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(sufBraveBallsInt, 1, 12)) {
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
			prize = calDLTWinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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

	private Prize calDLTWinningTicketPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI) 
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA)) {//大乐透单式 和 单式追加
			prizeLevel = calDLTWinningTicketPrizeLevel(ballContent, winningBallContent, playType);
			if (prizeLevel > 0) {
				prize.setPrize(true);
				prize.setPrizeAmount(getPrizeAmount(playType, prizeLevel, prizeAmountLevelMap));		
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_ZHUIJIA)) {//大乐透复式 和 复式追加
			List<String> combBalls = listDLTTicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calDLTWinningTicketPrizeLevel(combBalls.get(i), winningBallContent, playType);
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if (prizeLevel > 0) {
					if(prizeAmount == 0){
						prize.setPrize(true);
						prize.setPrizeAmount(getPrizeAmount(playType, prizeLevel, prizeAmountLevelMap));
						break;						
					}
					else{
						prize.setPrize(true);
						prize.setPrizeAmount(prize.getPrizeAmount() + getPrizeAmount(playType, prizeLevel, prizeAmountLevelMap));
					}
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO_ZHUIJIA)) {//大乐透复式胆拖 和 大乐透复式胆拖追加 
			List<String> combBalls = listDLTTicketDetail(ballContent, playType);

			for (int i = 0, size = combBalls.size(); i < size; i++) {
				prizeLevel = calDLTWinningTicketPrizeLevel(combBalls.get(i), winningBallContent, playType);
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if (prizeLevel > 0) {
					if(prizeAmount == 0){
						prize.setPrize(true);
						prize.setPrizeAmount(getPrizeAmount(playType, prizeLevel, prizeAmountLevelMap));
						break;						
					}
					else{
						prize.setPrize(true);
						prize.setPrizeAmount(prize.getPrizeAmount() + getPrizeAmount(playType, prizeLevel, prizeAmountLevelMap));
					}
				}
			}
		}
		return prize;
	}


	private int calDLTWinningTicketPrizeLevel(String ballContent,
			String winningBallContent, String playType) {
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String[] sufBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] preBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String winningPreBallContent = winningBalls[0];
		String winningSufBallContent = winningBalls[1];

		int preBallWinningCount = 0;
		int sufBallWinningCount = 0;
		for (int i = 0; i < preBalls.length; i++) {
			if (winningPreBallContent.contains(preBalls[i])) {
				preBallWinningCount++;
			}
		}
		
		for (int j = 0; j < sufBalls.length; j++) {
			if (winningSufBallContent.contains(sufBalls[j])) {
				sufBallWinningCount++;
			}
		}	

		return calDLTWinningPrizeLevel(preBallWinningCount, sufBallWinningCount, playType);
	}

	private int calDLTWinningPrizeLevel(int preBallWinningCount,
			int sufBallWinningCount, String playType) {
		
		if (sufBallWinningCount == 0 && preBallWinningCount == 0) { // 未中奖
			return 0;
		} else if ((preBallWinningCount == 3 && sufBallWinningCount == 0) || (preBallWinningCount == 1 && sufBallWinningCount == 2) 
				|| (preBallWinningCount == 2 && sufBallWinningCount == 1) || (preBallWinningCount == 0 && sufBallWinningCount == 2)) { // 六等奖
			return PrizeAmount.DLT_SIXTH_PRIZE_LEVEL;
		} else if ((sufBallWinningCount == 2 && preBallWinningCount == 2)
				|| (sufBallWinningCount == 1 && preBallWinningCount == 3)
				|| (sufBallWinningCount == 0 && preBallWinningCount == 4)) { // 五等奖
			if (isAppend(playType)){
				return PrizeAmount.DLT_FIFTH_PRIZE_APPEND_LEVEL;
			}
			return PrizeAmount.DLT_FIFTH_PRIZE_LEVEL;
		} else if ((sufBallWinningCount == 1 && preBallWinningCount == 4)
				|| (sufBallWinningCount == 2 && preBallWinningCount == 3)) { // 四等奖
			if (isAppend(playType)){
				return PrizeAmount.DLT_FOURTH_PRIZE_APPEND_LEVEL;
			}
			return PrizeAmount.DLT_FOURTH_PRIZE_LEVEL;
		} else if ((sufBallWinningCount == 0 && preBallWinningCount == 5)
				|| (sufBallWinningCount == 2 && preBallWinningCount == 4)) { // 三等奖
			if (isAppend(playType)){
				return PrizeAmount.DLT_THIRD_PRIZE_APPEND_LEVEL;
			}
			return PrizeAmount.DLT_THIRD_PRIZE_LEVEL;						
		} else if (sufBallWinningCount == 1 && preBallWinningCount == 5) { // 二等奖
			if (isAppend(playType)){
				return PrizeAmount.DLT_SECOND_PRIZE_APPEND_LEVEL;
			}
			return PrizeAmount.DLT_SECOND_PRIZE_LEVEL;						
		} else if (sufBallWinningCount == 2 && preBallWinningCount == 5) { // 一等奖
			if (isAppend(playType)){
				return PrizeAmount.DLT_FIRST_PRIZE_APPEND_LEVEL;
			}
			return PrizeAmount.DLT_FIRST_PRIZE_LEVEL;
		}

		return 0;
	}
	
	private boolean isAppend(String playType){
		boolean flag = false;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_ZHUIJIA)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO_ZHUIJIA)){
			flag = true;
		}
		
		return flag;
	}
	
	private long getPrizeAmount(String playType, int prizeLevel, Map<Integer, Long> prizeAmountLevelMap){
		long prizeAmount = 0L;
		
		if(!isAppend(playType)){
			return prizeAmountLevelMap.get(prizeLevel);
		}
		else{
			if(prizeLevel == PrizeAmount.DLT_FIFTH_PRIZE_APPEND_LEVEL){
				prizeAmount = prizeAmountLevelMap.get(prizeLevel) + prizeAmountLevelMap.get(PrizeAmount.DLT_FIFTH_PRIZE_LEVEL);
			}
			else if(prizeLevel == PrizeAmount.DLT_FOURTH_PRIZE_APPEND_LEVEL){
				prizeAmount = prizeAmountLevelMap.get(prizeLevel) + prizeAmountLevelMap.get(PrizeAmount.DLT_FOURTH_PRIZE_LEVEL);
			}
			else if(prizeLevel == PrizeAmount.DLT_THIRD_PRIZE_APPEND_LEVEL){
				prizeAmount = prizeAmountLevelMap.get(prizeLevel) + prizeAmountLevelMap.get(PrizeAmount.DLT_THIRD_PRIZE_LEVEL);
			}
			else if(prizeLevel == PrizeAmount.DLT_SECOND_PRIZE_APPEND_LEVEL){
				prizeAmount = prizeAmountLevelMap.get(prizeLevel) + prizeAmountLevelMap.get(PrizeAmount.DLT_SECOND_PRIZE_LEVEL);
			}
			else if(prizeLevel == PrizeAmount.DLT_FIRST_PRIZE_APPEND_LEVEL){
				prizeAmount = prizeAmountLevelMap.get(prizeLevel) + prizeAmountLevelMap.get(PrizeAmount.DLT_FIRST_PRIZE_LEVEL);
			}				
		}
		return prizeAmount;
	}

	@Override
	public int calBallCounts(String orderContent, String playType) {
		int sum = 0;
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			sum += calDLTBallCount(ballContents[i], playType);
		}
		
		return sum;
	}

	private int calDLTBallCount(String ballContent, String playType) {
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI) 
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA)) {//大乐透单式 和 单式追加
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_ZHUIJIA)) {//大乐透复式 和 复式追加
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int preBallCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int sufBallCount = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(preBallCount, 5) * TicketWinningUtil.getCombNum(sufBallCount, 2);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO_ZHUIJIA)) {//大乐透复式胆拖 和 追加
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			String[] preBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] sufBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			int preCount = 1;
			int sufCount = 1;
			
			if(preBalls.length == 2){
				int preBaveBallCount = preBalls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
				int preDragBallCount = preBalls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
				
				preCount = TicketWinningUtil.getCombNum(preDragBallCount, 5 - preBaveBallCount);
			}
			
			if(sufBalls.length == 2){
				sufCount = sufBalls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			}
			
			count = preCount * sufCount;
		}
		
		return count;
	}

	@Override
	public List<String> listTicketDetails(String orderContent, String playType) {
		List<String> details = new ArrayList<String>();
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			details.addAll(listDLTTicketDetail(ballContents[i], playType));
		}
		
		return details;
	}

	private List<String> listDLTTicketDetail(String ballContent,
			String playType) {
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI) 
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA)) {//大乐透单式 和 单式追加
			details.add(ballContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_ZHUIJIA)) {//大乐透复式 和 复式追加
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO); 
			String[] preBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] sufBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			List<String> combPreBalls = CombinationUtil.combine(preBalls, 5, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			List<String> combSufBalls = CombinationUtil.combine(sufBalls, 2, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, preSize = combPreBalls.size(); i < preSize; i++){
				for(int j = 0, sufSize = combSufBalls.size(); j < sufSize; j++){
					details.add(combPreBalls.get(i) + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + combSufBalls.get(j));
				}
			}
			combPreBalls.clear();
			combSufBalls.clear();
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO_ZHUIJIA)) {//大乐透复式胆拖 和 大乐透复式胆拖追加 
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			String preBallContent = balls[0];
			String sufBallContent = balls[1];
			
			List<String> preDetails = new ArrayList<String>();
			List<String> sufDetails = new ArrayList<String>();
			
			String[] preBalls = preBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] sufBalls = sufBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(preBalls.length == 2){
				String preBraveBallDetail = preBalls[0];
				String[] preDragBalls = preBalls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				int preBraveBallCount = preBraveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
				
				List<String> dragDetails =  CombinationUtil.combine(preDragBalls, 5 - preBraveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				for(int i = 0, len = dragDetails.size(); i < len; i++){
					preDetails.add(preBraveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragDetails.get(i));
				}
			}
			else{
				preDetails.add(preBallContent);
			}
			
			if(sufBalls.length == 2){
				String sufBraveBallDetail = sufBalls[0];
				String[] sufDragBalls = sufBalls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				for(int j = 0, dragLen = sufDragBalls.length; j < dragLen; j++){
					sufDetails.add(sufBraveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + sufDragBalls[j]); 
				}
			}
			else{
				sufDetails.add(sufBallContent);
			}
			
			for(String preDetail : preDetails){
				for(String sufDetail : sufDetails){
					details.add(preDetail + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + sufDetail); 
				}
			}
		}
		
		return details;
	}

}
