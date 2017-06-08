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

public class SSCTicketWinningServiceImpl implements ITicketWinningService{
	
	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType){
		boolean flag = true;
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		if(ballContents.length > 5){
			return false;
		}
		
		for(int i = 0; i < ballContents.length; i++){
			flag = validateBallContent(ballContents[i], playType);
			if(!flag){
				return flag;
			}
		}
		
		return flag;
	}
	
	private static boolean validateBallContent(String ballContent, String playType){
		boolean flag = true;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_ONESTAR)){//单式一星
			if(TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContent)){
				return false;
			}
			if(Integer.valueOf(ballContent) < 0 || Integer.valueOf(ballContent) > 9){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_TWOSTAR)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_TWOSTAR)){//单式二星 复选二星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 2){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_THREESTAR)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_THREESTAR)){//单式三星 复选三星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 3){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_FIVESTAR) 
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_FIVESTAR)){//单式五星 复选五星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 5){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_TWOSTAR)){//组合二星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			if(balls.length != 2){
				return false;
			}
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(shiBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(geBalls)){
				return false;
			}
			int[] shiBallsInt = TicketWinningUtil.str2IntArr(shiBalls);
			int[] geBallsInt = TicketWinningUtil.str2IntArr(geBalls);
			
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(shiBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(geBallsInt, 0, 9)){
				return false;
			}		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_THREESTAR)){//组合三星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			if(balls.length != 3){
				return false;
			}			
			
			String[] baiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(baiBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(shiBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(geBalls)){
				return false;
			}			
			int[] baiBallsInt = TicketWinningUtil.str2IntArr(baiBalls);
			int[] shiBallsInt = TicketWinningUtil.str2IntArr(shiBalls);
			int[] geBallsInt = TicketWinningUtil.str2IntArr(geBalls);
			
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(baiBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(shiBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(geBallsInt, 0, 9)){
				return false;
			}	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_FIVESTAR)){//组合五星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			if(balls.length != 5){
				return false;
			}
			
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[3].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[4].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(wanBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(qianBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(baiBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(shiBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(geBalls)){
				return false;
			}
			int[] wanBallsInt = TicketWinningUtil.str2IntArr(wanBalls);
			int[] qianBallsInt = TicketWinningUtil.str2IntArr(qianBalls);
			int[] baiBallsInt = TicketWinningUtil.str2IntArr(baiBalls);
			int[] shiBallsInt = TicketWinningUtil.str2IntArr(shiBalls);
			int[] geBallsInt = TicketWinningUtil.str2IntArr(geBalls);
			
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(wanBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(qianBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(baiBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(shiBallsInt, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(geBallsInt, 0, 9)){
				return false;
			}
			if((wanBallsInt.length + qianBallsInt.length + baiBallsInt.length + shiBallsInt.length + geBallsInt.length) > 35){
				return false;
			}
		}		
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE)){//二星组选单式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 2){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_COMPOUND)){//二星组选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length < 3 || ballsInt.length > 6){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUPING)){//二星组选分组		
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			if(balls.length != 2){
				return false;
			}
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(shiBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(geBalls)){
				return false;
			}
			int[] shiBallContent = TicketWinningUtil.str2IntArr(shiBalls);
			int[] geBallContent = TicketWinningUtil.str2IntArr(geBalls);
			
			if(shiBallContent.length < 1 || shiBallContent.length > 10){
				return false;
			}
			if(geBallContent.length < 1 || geBallContent.length > 10){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(shiBallContent, 0, 9)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(geBallContent, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BUNS)){//二星组选包点
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContent)){
				return false;
			}
			if (Integer.valueOf(ballContent) < 0 || Integer.valueOf(ballContent) > 18) {
				return false;
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BRAVE)){//二星组选包胆
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContent)){
				return false;
			}
			if (Integer.valueOf(ballContent) < 0 || Integer.valueOf(ballContent) > 9) {
				return false;
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_BUNS)){//二星包点
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContent)){
				return false;
			}
			if (Integer.valueOf(ballContent) < 0 || Integer.valueOf(ballContent) > 18) {
				return false;
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_BUNS)){//三星包点
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContent)){
				return false;
			}
			if (Integer.valueOf(ballContent) < 0 || Integer.valueOf(ballContent) > 27) {
				return false;
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_SIZE_ODDEVEN)){//猜大小单双
			if(!TicketWinningUtil.isSSCSizeOddEvenBallNum(ballContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_FIVESTAR_CHOOSE)){//五星通选
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 5){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3)){//三星组三
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 3){
				return false;
			}
			if(!((ballsInt[0] == ballsInt[1]) || (ballsInt[1] == ballsInt[2]) || (ballsInt[0] == ballsInt[2]))){
				return false;
			}
			if((ballsInt[0] == ballsInt[1]) && (ballsInt[1] == ballsInt[2])){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6)){//三星组六
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length != 3){
				return false;
			}
			if((ballsInt[0] == ballsInt[1]) || (ballsInt[1] == ballsInt[2]) || (ballsInt[0] == ballsInt[2])){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3_COMPOUND)){//三星组三复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length < 2 || ballsInt.length > 10){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6_COMPOUND)){//三星组六复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length < 4 || ballsInt.length > 10){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BUNS)){//三星组选包点
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContent)){
				return false;
			}
			if (Integer.valueOf(ballContent) < 0 || Integer.valueOf(ballContent) > 27) {
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BRAVE)){//三星组选包胆
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length < 1 || ballsInt.length > 2){
				return false;
			}
			if(!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_DIRECT_GROUP_COMPOUND)){//三星直选组合复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
			if(ballsInt.length < 3 || ballsInt.length > 10){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 0, 9)){
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
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
//		prizeAmountLevelMap = prizeLevelMap;
		prizeAmountLevelMap.put(PrizeAmount.SSC_RADIO_FOURSTAR_PRIZE_LEVEL, (long)PrizeAmount.SSC_RADIO_FOURSTAR_PRIZE_LEVEL);
		prizeAmountLevelMap.put(PrizeAmount.SSC_GROUP_TWOSTAR_DUIZI_PRIZE_LEVEL, (long)PrizeAmount.SSC_GROUP_TWOSTAR_DUIZI_PRIZE_LEVEL);
		prizeAmountLevelMap.put(PrizeAmount.SSC_GROUP_THREE_BAOZI_PRIZE_LEVEL, (long)PrizeAmount.SSC_GROUP_THREE_BAOZI_PRIZE_LEVEL);
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		Prize rePrize = new Prize();
		Prize prize = null;
		for(int i = 0, len = ballContents.length; i< len; i++){
			prize = calSSCWinningTicket(ballContents[i], playType, winningBallContent, prizeAmountLevelMap);
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
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		int sum = 0;
		for(int i = 0; i< ballContents.length; i++){
			sum += calSSCBallCount(ballContents[i], playType);
		}
		
		return sum;
	}
	
	private static int calSSCBallCount(String ballContent, String playType){
		int count = 0;

		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_ONESTAR)){//单式一星
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_TWOSTAR)){//单式二星
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_THREESTAR)){//单式三星
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_FIVESTAR)){//单式五星
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_TWOSTAR)){//复选二星
			count = 2;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_THREESTAR)){//复选三星
			count = 3;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_FIVESTAR)){//复选五星
			count = 4;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_TWOSTAR)){//组合二星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = shiBalls.length * geBalls.length;			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_THREESTAR)){//组合三星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] baiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = baiBalls.length * shiBalls.length * geBalls.length;		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_FIVESTAR)){//组合五星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[3].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[4].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = wanBalls.length * qianBalls.length * baiBalls.length * shiBalls.length * geBalls.length;		
		}		
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE)){//二星组选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_COMPOUND)){//二星组选复式
			List<String> combList = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO), 2, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = combList.size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUPING)){//二星组选分组
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = shiBalls.length * geBalls.length;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BUNS)){//二星组选包点
			count = TicketWinningUtil.getSSCTwoStarGroupAndValue(Integer.valueOf(ballContent));
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BRAVE)){//二星组选包胆
			count = 10;
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_BUNS)){//二星包点
			count = TicketWinningUtil.getSSCTwoStarAndValue(Integer.valueOf(ballContent));
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_BUNS)){//三星包点
			count = TicketWinningUtil.getSSCThreeStarAndValue(Integer.valueOf(ballContent));
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_SIZE_ODDEVEN)){//猜大小单双
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_FIVESTAR_CHOOSE)){//五星通选
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3)){//三星组三
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6)){//三星组六
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3_COMPOUND)){//三星组三复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = balls.length * (balls.length - 1);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6_COMPOUND)){//三星组六复式
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO), 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			count = combBalls.size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BUNS)){//三星组选包点
			count = TicketWinningUtil.getSSCThreeStarGroupAndValue(Integer.valueOf(ballContent));
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BRAVE)){//三星组选包胆
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			if(balls.length == 1){
				count = 55;
			}
			else{
				count = 10;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_DIRECT_GROUP_COMPOUND)){//三星直选组合复式
			int length = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO).length;
			
			count = length * (length - 1) * (length -2);
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
			details.addAll(listSSCTicketDetail(ballContents[i], playType));
		}		
				
		return details;
	}
	
	private static List<String> listSSCTicketDetail(String ballContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_ONESTAR)){//单式一星
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_TWOSTAR)){//单式二星
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_THREESTAR)){//单式三星
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_FIVESTAR)){//单式五星
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_TWOSTAR)){//复选二星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			details.add(ballContent);
			details.add(balls[1]);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_THREESTAR)){//复选三星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			details.add(ballContent);
			details.add(balls[1]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+balls[2]);
			details.add(balls[2]);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_FIVESTAR)){//复选五星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			details.add(ballContent);
			details.add(balls[2]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+balls[3]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+balls[4]);
			details.add(balls[3]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+balls[4]);
			details.add(balls[4]);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_TWOSTAR)){//组合二星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < shiBalls.length; i++){
				for(int j = 0; j < geBalls.length; j++){
					details.add(shiBalls[i]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+geBalls[j]);
				}
			}			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_THREESTAR)){//组合三星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] baiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < baiBalls.length; i++){
				for(int j = 0; j < shiBalls.length; j++){
					for(int k = 0; k < geBalls.length; k++){
						details.add(baiBalls[i]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+shiBalls[j]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+geBalls[k]);
					}
				}
			}	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_FIVESTAR)){//组合五星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[3].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[4].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < wanBalls.length; i++){
				for(int j = 0; j < qianBalls.length; j++){
					for(int k = 0; k < baiBalls.length; k++){
						for(int m = 0; m < shiBalls.length; m++){
							for(int n = 0; n < geBalls.length; n++){
								details.add(wanBalls[i]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+qianBalls[j]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+baiBalls[k]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+shiBalls[m]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+geBalls[n]);
							}
						}	
					}
				}
			}
		}		
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE)){//二星组选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_COMPOUND)){//二星组选复式
			details.addAll(CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO), 2, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));

		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUPING)){//二星组选分组
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < shiBalls.length; i++){
				for(int j = 0; j < geBalls.length; j++){
					details.add(shiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[j]);
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BUNS)){//二星组选包点
			int sum = Integer.valueOf(ballContent);
			
			for(int i = 0; i < 10; i++){
				for(int j = i; j < 10; j++){
					if(i + j == sum){
						details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j);
					}
				}
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BRAVE)){//二星组选包胆
			int num = Integer.valueOf(ballContent);
			
			for(int i = 0; i < 10; i++){
				details.add(num + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + i);
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_BUNS)){//二星包点
			int sum = Integer.valueOf(ballContent);
			
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 10; j++){
					if(i + j == sum){
						details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j);
					}
				}
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_BUNS)){//三星包点
			int sum = Integer.valueOf(ballContent);
			
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 10; j++){
					for(int k = 0; k < 10; k++){
						if(i + j + k == sum){
							details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + k);
						}
					}
				}
			}
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_SIZE_ODDEVEN)){//猜大小单双
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_FIVESTAR_CHOOSE)){//五星通选
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3)){//三星组三
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6)){//三星组六
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3_COMPOUND)){//三星组三复式
			details.addAll(TicketWinningUtil.getGroup3Compound(ballContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6_COMPOUND)){//三星组六复式
			details.addAll(CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO), 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BUNS)){//三星组选包点
			int sum = Integer.valueOf(ballContent);
			
			for(int i = 0; i < 10; i++){
				for(int j = i; j < 10; j++){
					for(int k = j;  k < 10; k++){
						if(i + j + k == sum){
							details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + k);							
						}
					}
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BRAVE)){//三星组选包胆
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			if(balls.length == 1){
				for(int i = 0; i < 10; i++){
					int j = i;
					for(; j < 10; j++){
						details.add(ballContent + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j);
					}
				}
			}
			else{
				for(int i = 0; i < 10; i++){
					details.add(ballContent + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + i);
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_DIRECT_GROUP_COMPOUND)){//三星直选组合复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int length = balls.length;
			
			for(int i = 0; i < length; i++){
				for(int j = 0; j < length; j++){
					for(int k = 0; k < length; k++){
						if((balls[i] != balls[j]) && (balls[j] != balls[k]) && (balls[k] != balls[i])){
							details.add(balls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[k]);
						}
					}
				}
			}
		}
		
		return details;
	}
	
	/**
	 * 根据玩法计算一注彩票中奖金额
	 * @param ballContent 一注彩票投注内容
	 * @param playType 玩法
	 * @return
	 */
	private static Prize calSSCWinningTicket(String ballContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_ONESTAR)){//单式一星
			prizeLevel = calSSCRadio(ballContent, 1, PrizeAmount.SSC_RADIO_ONESTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_TWOSTAR)){//单式二星
			prizeLevel = calSSCRadio(ballContent, 2, PrizeAmount.SSC_RADIO_TWOSTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_THREESTAR)){//单式三星
			prizeLevel = calSSCRadio(ballContent, 3, PrizeAmount.SSC_RADIO_THREESTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_RADIO_FIVESTAR)){//单式五星
			prizeLevel = calSSCRadio(ballContent, 5, PrizeAmount.SSC_RADIO_FIVESTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_TWOSTAR)){//复选二星
			prize = calSSCCompound(ballContent, 2, winningBallContent, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_THREESTAR)){//复选三星
			prize = calSSCCompound(ballContent, 3, winningBallContent, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_COMPOUND_FIVESTAR)){//复选五星
			prize = calSSCCompound(ballContent, 5, winningBallContent, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_TWOSTAR)){//组合二星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < shiBalls.length; i++){
				for(int j = 0; j < geBalls.length; j++){
					prizeLevel = calSSCRadio(shiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[j], 2, PrizeAmount.SSC_RADIO_TWOSTAR_PRIZE_LEVEL, winningBallContent);
					prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
					if(prizeLevel > 0){
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
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_THREESTAR)){//组合三星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] baiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < baiBalls.length; i++){
				for(int j = 0; j < shiBalls.length; j++){
					for(int k = 0; k < geBalls.length; k++){
						prizeLevel = calSSCRadio(baiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + shiBalls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[k], 3, PrizeAmount.SSC_RADIO_THREESTAR_PRIZE_LEVEL, winningBallContent);
						prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
						if(prizeLevel > 0){
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
			}	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_GROUP_FIVESTAR)){//组合五星
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] shiBalls = balls[3].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[4].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < wanBalls.length; i++){
				for(int j = 0; j < qianBalls.length; j++){
					for(int k = 0; k < baiBalls.length; k++){
						for(int m = 0; m < shiBalls.length; m++){
							for(int n = 0; n < geBalls.length; n++){
								prizeLevel = calSSCRadio(wanBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + qianBalls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + baiBalls[k] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + shiBalls[m] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[n], 5, PrizeAmount.SSC_RADIO_FIVESTAR_PRIZE_LEVEL, winningBallContent);
								prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
								if(prizeLevel > 0){
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
					}
				}
			}
		}		
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE)){//二星组选单式
			prizeLevel = calSSCRadio(ballContent, 2, PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
			if(prizeLevel == 0){
				prizeLevel = calSSCRadio(TicketWinningUtil.reverseString(ballContent), 2, PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL, winningBallContent);
				checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);				
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_COMPOUND)){//二星组选复式
			List<String> combList = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO), 2, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			for(int i = 0; i < combList.size(); i++){
				prizeLevel = calSSCRadio(combList.get(i), 2, PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL, winningBallContent);				
				if(prizeLevel == 0){
					prizeLevel = calSSCRadio(TicketWinningUtil.reverseString(combList.get(i)), 2, PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL, winningBallContent);
				}
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if(prizeLevel > 0){
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUPING)){//二星组选分组
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String[] shiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] geBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			for(int i = 0; i < shiBalls.length; i++){
				for(int j = 0; j < geBalls.length; j++){
					if(shiBalls[i].equals(geBalls[j])){
						prizeLevel = calSSCRadio(shiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[j], 2, PrizeAmount.SSC_GROUP_TWOSTAR_DUIZI_PRIZE_LEVEL, winningBallContent);	
					}
					else{
						prizeLevel = calSSCRadio(shiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[j], 2, PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL, winningBallContent);
						if(prizeLevel == 0){
							prizeLevel = calSSCRadio(geBalls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + shiBalls[i], 2, PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL, winningBallContent);
						}
					}
					prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
					if(prizeLevel > 0){
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
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BUNS)){//二星组选包点
			prizeLevel = calSSCBuns(ballContent, 2, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_GROUP_BRAVE)){//二星组选包胆
			prizeLevel = calSSCTwoBrave(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_TWOSTAR_BUNS)){//二星包点
			prizeLevel = calSSCAndValue(ballContent, 2, PrizeAmount.SSC_RADIO_TWOSTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_BUNS)){//三星包点
			prizeLevel = calSSCAndValue(ballContent, 3, PrizeAmount.SSC_RADIO_THREESTAR_PRIZE_LEVEL, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}	
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_SIZE_ODDEVEN)){//猜大小单双
			prizeLevel = calSSCSizeOddEven(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_FIVESTAR_CHOOSE)){//五星通选
			prizeAmount = calSSCFiveChoose(ballContent, winningBallContent, prizeAmountLevelMap);
			if(prizeAmount > 0){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmount);
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3)){//三星组三
			prizeLevel = calSSCGroup(ballContent, true, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6)){//三星组六
			prizeLevel = calSSCGroup(ballContent, false, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP3_COMPOUND)){//三星组三复式
			List<String> list = TicketWinningUtil.getGroup3Compound(ballContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			for(int i = 0; i < list.size(); i++){
				prizeLevel = calSSCGroup(list.get(i), true, winningBallContent);
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if(prizeLevel > 0){
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP6_COMPOUND)){//三星组六复式
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO), 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = calSSCGroup(combBalls.get(i), false, winningBallContent);
				prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
				if(prizeLevel > 0){
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BUNS)){//三星组选包点
			prizeLevel = calSSCThreeGroupBuns(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_GROUP_BRAVE)){//三星组选包胆
			prizeLevel = calSSCThreeGroupBrave(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_SSC_THREESTAR_DIRECT_GROUP_COMPOUND)){//三星直选组合复式
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			if((winningBallsInt[2] == winningBallsInt[3]) || (winningBallsInt[3] == winningBallsInt[4]) || (winningBallsInt[2] == winningBallsInt[4])){
				return prize;
			}
			int count = 0;
			for(int i = 0; i < 3; i++){
				for(int j = 0, size = ballsInt.length; j < size; j++){
					if(ballsInt[j] == winningBallsInt[i+2]){
						count++;
					}
				}
			}
			if(count == 3){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.SSC_RADIO_THREESTAR_PRIZE_LEVEL));
			}
		}	
		
		return prize;
	}
	
	/**
	 * 单选
	 * @param ballContent
	 * @param ballCount
	 * @param prizeAmount
	 * @return
	 */
	private static int calSSCRadio(String ballContent, int ballCount, int prizeLevel, String winningBallContent){

		if(winningBallContent.indexOf(ballContent) == -1){
			return 0;
		}
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBalls);
		int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
		int start = winningBallsInt.length - ballCount;
		int count = 0;
		
		for(int i = 0; i < ballCount; i++){
			if(winningBallsInt[start+i] == ballsInt[i]){
				count++;
			}
		}
		
		if(ballCount == count){
			return prizeLevel;
		}		
		
		return 0;
	}
	
	/**
	 * 复选
	 * @param ballContent
	 * @param ballCount
	 * @return
	 */
	private static Prize calSSCCompound(String ballContent, int ballCount, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){	
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		Prize prize = new Prize();
		int prizeLevel = 0;
		long prizeAmount = 0L;
		String content = "";
		for(int i = 1; i <= ballCount; i++){
			if(i == 1){
				prizeLevel = PrizeAmount.SSC_RADIO_ONESTAR_PRIZE_LEVEL;
				content = balls[ballCount - 1];
			}
			else if(i == 2){
				prizeLevel = PrizeAmount.SSC_RADIO_TWOSTAR_PRIZE_LEVEL;
				content = balls[ballCount - 2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[ballCount - 1];
			}
			else if(i == 4){
				continue;
			}
			else if(i == 3){
				prizeLevel = PrizeAmount.SSC_RADIO_THREESTAR_PRIZE_LEVEL;
				content = balls[ballCount - 3] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[ballCount - 2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[ballCount - 1];
			}
			else if(i == 5){
				prizeLevel = PrizeAmount.SSC_RADIO_FIVESTAR_PRIZE_LEVEL;
				content = balls[ballCount - 5] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO +  balls[ballCount - 4] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[ballCount - 3] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[ballCount - 2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[ballCount - 1];
			}
			
			prizeLevel = calSSCRadio(content, i, prizeLevel, winningBallContent);
			prizeAmount = prizeAmountLevelMap.containsKey(prizeLevel) ? prizeAmountLevelMap.get(prizeLevel) : 0;
			if(prizeLevel > 0){
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
	
	/**
	 * 五星通选
	 * @param ballContent
	 * @return
	 */
	private static long calSSCFiveChoose(String ballContent, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		long sum = 0;
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String threeWinningBefBall = winningBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + winningBalls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + winningBalls[2];
		String threeWinningAftBall = winningBalls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + winningBalls[3] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + winningBalls[4];
		String twoWinningBefBall = winningBalls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + winningBalls[1] ;
		String twoWinningAftBall = winningBalls[3] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + winningBalls[4];
		String threeBefBall = balls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[1] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[2];
		String threeAftBall = balls[2] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[3] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[4];
		String twoBefBall = balls[0] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[1] ;
		String twoAftBall = balls[3] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[4];
		
		if(twoBefBall.equals(twoWinningBefBall)){
			sum += prizeAmountLevelMap.get(PrizeAmount.SSC_CHOOSE_TWOSTAR_PRIZE_LEVEL);
		}
		if(twoAftBall.equals(twoWinningAftBall)){
			sum += prizeAmountLevelMap.get(PrizeAmount.SSC_CHOOSE_TWOSTAR_PRIZE_LEVEL);
		}
		if(threeBefBall.equals(threeWinningBefBall)){
			sum += prizeAmountLevelMap.get(PrizeAmount.SSC_CHOOSE_THREESTAR_PRIZE_LEVEL);
		}
		if(threeAftBall.equals(threeWinningAftBall)){
			sum += prizeAmountLevelMap.get(PrizeAmount.SSC_CHOOSE_THREESTAR_PRIZE_LEVEL);
		}
		if(winningBallContent.equals(ballContent)){
			sum += prizeAmountLevelMap.get(PrizeAmount.SSC_CHOOSE_FIVESTAR_PRIZE_LEVEL);
		}
		
		return sum;
	}
	
	/**
	 * 包点
	 * @param ballContent
	 * @param ballCount
	 * @return
	 */
	private static int calSSCBuns(String ballContent, int ballCount, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);

		int start = winningBalls.length - ballCount;
		int winningSum = 0;
		
		for(int i = 0; i < ballCount; i++){
			winningSum += Integer.valueOf(winningBalls[start+i]);
		}
		
		if(Integer.valueOf(ballContent) == winningSum){
			if(winningBalls[3].equals(winningBalls[4])){
				return PrizeAmount.SSC_GROUP_TWOSTAR_DUIZI_PRIZE_LEVEL;
			}
			else{
				return PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL;
			}
		}	
		
		return 0;
	}
	
	/**
	 * 两星组选包胆
	 * @param ballContent
	 * @return
	 */
	private static int calSSCTwoBrave(String ballContent, String winningBallContent){
	
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		if(winningBalls[3].equals(ballContent) || winningBalls[4].equals(ballContent)){
			if(winningBalls[4].equals(winningBalls[3])){
				return PrizeAmount.SSC_GROUP_TWOSTAR_DUIZI_PRIZE_LEVEL;
			}
			else{
				return PrizeAmount.SSC_GROUP_TWOSTAR_PRIZE_LEVEL;
			}
		}
		
		return 0;
	}
	
	/**
	 * 和值
	 * @param ballContent
	 * @param ballCount
	 * @param prizeAmount
	 * @return
	 */
	private static int calSSCAndValue(String ballContent, int ballCount, int prizeLevel, String winningBallContent){
		int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		int start = winningBallsInt.length - ballCount;
		int winningSum = 0;
		
		for(int i = 0; i < ballCount; i++){
			winningSum += winningBallsInt[start + i];
		}
		
		if(Integer.valueOf(ballContent) == winningSum){
			return prizeLevel;
		}		
		
		return 0;
	}
	
	
	/**
	 * 大小双单
	 * @param ballContent
	 * @return
	 */
	private static int calSSCSizeOddEven(String ballContent, String winningBallContent){		
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		int winningShiBall = Integer.valueOf(winningBalls[3]);
		int winningGeBall = Integer.valueOf(winningBalls[4]);
		String shiResult = "";
		String geResult = "";
		String shiBall = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
		String geBall = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1];
		
		if(winningShiBall < 5){
			shiResult += "1";
		}
		else{
			shiResult += "2";
		}
		if(winningShiBall % 2 == 0){
			shiResult += "4";
		}
		else{
			shiResult += "5";
		}
		if(winningGeBall < 5){
			geResult += "1";
		}
		else{
			geResult += "2";
		}
		if(winningGeBall % 2 == 0){
			geResult += "4";
		}
		else{
			geResult += "5";
		}
		if((shiResult.indexOf(shiBall) != -1) && (geResult.indexOf(geBall) != -1)){
			return PrizeAmount.SSC_SIZE_ODDEVEN_PRIZE_LEVEL;
		}		
		
		return 0;
	}
	
	/**
	 * 组选
	 * @param ballContent
	 * @param isGroupThree
	 * @return
	 */
	private static int calSSCGroup(String ballContent, boolean isGroupThree, String winningBallContent){
		String orderWinningThreeBallContent = getSortWinningThreeBallContent(winningBallContent);
		String orderBallContent = TicketWinningUtil.sortBallContent(ballContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		if(orderWinningThreeBallContent.equals(orderBallContent)){
			if(isGroupThree){
				return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;				
			}
			else{
				return PrizeAmount.SSC_GROUP_SIX_PRIZE_LEVEL;
			}
		}		
		
		return 0;
	}	
	
	/**
	 * 三星组选包点
	 * @param ballContent
	 * @return
	 */
	private static int calSSCThreeGroupBuns(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);		
		int winningSum = 0;
		
		for(int i = 0; i < 3; i++){
			winningSum += Integer.valueOf(winningBalls[2+i]);
		}
		
		if(winningSum == Integer.valueOf(ballContent)){
			if(winningBalls[2].equals(winningBalls[3]) && winningBalls[3].equals(winningBalls[4])){
				return PrizeAmount.SSC_GROUP_THREE_BAOZI_PRIZE_LEVEL;
			}
			else if(winningBalls[2].equals(winningBalls[3]) || winningBalls[3].equals(winningBalls[4])){
				return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;
			}
			else{
				return PrizeAmount.SSC_GROUP_SIX_PRIZE_LEVEL;
			}
		}
		
		return 0;
	}
	
	/**
	 * 三星组选包胆
	 * @param ballContent
	 * @return
	 */
	public static int calSSCThreeGroupBrave(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);	
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String[] tempArr = {winningBalls[2], winningBalls[3], winningBalls[4]};
		int[] winningThreeBallsInt = TicketWinningUtil.str2IntArr(tempArr);
		int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			
		if((winningThreeBallsInt[0] == ballsInt[0]) || (winningThreeBallsInt[1] == ballsInt[0]) || (winningThreeBallsInt[2] == ballsInt[0])){
			if(ballsInt.length == 1){
				if((winningThreeBallsInt[0] == winningThreeBallsInt[1]) && (winningThreeBallsInt[1] == winningThreeBallsInt[2])){					
					return PrizeAmount.SSC_GROUP_THREE_BAOZI_PRIZE_LEVEL;
				}
				else if((winningThreeBallsInt[0] == winningThreeBallsInt[1]) || (winningThreeBallsInt[1] == winningThreeBallsInt[2]) || (winningThreeBallsInt[0] == winningThreeBallsInt[2])){
					return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;
				}
				else{
					return PrizeAmount.SSC_GROUP_SIX_PRIZE_LEVEL;
				}
			}
			else if(balls.length == 2 && (winningThreeBallsInt[0] == ballsInt[1]) || (winningThreeBallsInt[1] == ballsInt[2]) || (winningThreeBallsInt[2] == ballsInt[2])){
				if(ballsInt[0] == ballsInt[1]){
					if((winningThreeBallsInt[0] == winningThreeBallsInt[1]) && (winningThreeBallsInt[1] == winningThreeBallsInt[2])){
						return PrizeAmount.SSC_GROUP_THREE_BAOZI_PRIZE_LEVEL;
					}
					else if((winningThreeBallsInt[0] == winningThreeBallsInt[1]) && (winningThreeBallsInt[0] == ballsInt[0])){						
						return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;
					}
					else if((winningThreeBallsInt[1] == winningThreeBallsInt[2]) && (winningThreeBallsInt[1] == ballsInt[0])){						
						return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;
					}
					else if((winningThreeBallsInt[0] == winningThreeBallsInt[2]) && (winningThreeBallsInt[0] == ballsInt[0])){						
						return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;
					}
				}
				else{
					if((winningThreeBallsInt[0] == winningThreeBallsInt[1]) || (winningThreeBallsInt[1] == winningThreeBallsInt[2]) || (winningThreeBallsInt[0] == winningThreeBallsInt[2])){
						return PrizeAmount.SSC_GROUP_THREE_PRIZE_LEVEL;
					}
					else{
						return PrizeAmount.SSC_GROUP_SIX_PRIZE_LEVEL;						
					}
				}
			}
		}
		
		return 0;
	}
	
	private static String getSortWinningThreeBallContent(String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String winningThreeBallContent = winningBalls[2]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+winningBalls[3]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+winningBalls[4];
		
		return TicketWinningUtil.sortBallContent(winningThreeBallContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
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
