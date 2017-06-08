package com.cqfc.ticketwinning.service.szc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cqfc.ticketwinning.model.Prize;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.ticketwinning.util.CombinationUtil;
import com.cqfc.ticketwinning.util.PrizeAmount;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.LotteryPlayTypeConstants;

public class XYNCTicketWinningServiceImpl implements ITicketWinningService{
	
	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;

		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_SHOTS)) {//选一数投(植物单选)
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 10){
				return false;
			}
			for (int i = 0, len = ballContents.length; i < len; i++) {
				if(TicketWinningUtil.isHasZeroPrefixLessThanTen(ballContents[i])){
					return false;
				}
				int ball = Integer.valueOf(ballContents[i]);
				
				if(ball < 1 || ball > 18){
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_COMPOUND)) {//选一数投复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			int[] ballsInt = null;
			
			if(ballContents.length > 1){
				return false;
			}
			for (int i = 0; i < ballContents.length; i++) {
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
				if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				
				if(!TicketWinningUtil.isBallContentInRange(ballsInt, 1, 18)){
					return false;
				}
				if(ballsInt.length < 2 || ballsInt.length > 18){
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_RED)) {//选一红投(动物单选)
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 10){
				return false;
			}
			for (int i = 0; i < ballContents.length; i++) {
				int ball = Integer.valueOf(ballContents[i]);
				if(!((ball == 19) || (ball == 20))){
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_GROUP) 
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_STRAIGHT)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_SINGLE)) {//选二连组(背靠背) 选二连直 选二任选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length != 2){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_COMPOUND)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_COMPOUND)) {//选二连组复式  选二任选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length < 3 || ballsInt.length > 20){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_BRAVE)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_BRAVE)) {//选二连组胆拖 选二任选胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			
			if(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO).length != 2){
				return false;
			}
			
			String braveBallContent = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[0];
			String dragBallContent = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[1];
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)){
				return false;
			}
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if(braveBallsInt.length != 1){
				return false;
			}
			if(braveBallsInt[0] < 1 || braveBallsInt[0] > 20){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(dragBallsInt, 1, 20)){
				return false;
			}
			if(TicketWinningUtil.isTwoArrContainSameElement(braveBallsInt, dragBallsInt)){
				return false;
			}
		} else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_GROUP)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_STRAIGHT)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_SINGLE)){//选三前组(三全中) 选三前直(三连中) 选三任选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length != 3){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}		
		} else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_COMPOUND)
			|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_COMPOUND)){//选三前组复式	选三任选复式		
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length < 4 || ballsInt.length > 20){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}
		} else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_BRAVE)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_BRAVE)){//选三前组胆拖 选三连组胆拖 
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			
			if(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO).length != 2){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)){
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if(!((braveBallsInt.length == 1) || (braveBallsInt.length == 2))){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(braveBallsInt, 1, 20)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(dragBallsInt, 1, 20)){
				return false;
			}
			if(TicketWinningUtil.isTwoArrContainSameElement(braveBallsInt, dragBallsInt)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_SINGLE)){//选四任选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length != 4){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_COMPOUND)){//选四任选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length < 5 || ballsInt.length > 20){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_BRAVE)){//选四连组胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			
			if(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO).length != 2){
				return false;
			}
			String braveBallContent = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[0];
			String dragBallContent = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[1];
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)){
				return false;
			}
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if(!TicketWinningUtil.isBallCount(braveBallsInt.length, 1, 3)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(braveBallsInt, 1, 20)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(dragBallsInt, 1, 20)){
				return false;
			}
			if(TicketWinningUtil.isTwoArrContainSameElement(braveBallsInt, dragBallsInt)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_SINGLE)){//选五任选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}	
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length != 5){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_COMPOUND)){//选五任选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);
			if(ballsInt.length < 6 || ballsInt.length > 20){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 20)){
				return false;
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_BRAVE)){//选五连组胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			if(ballContents.length > 1){
				return false;
			}
			
			if(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO).length != 2){
				return false;
			}
			String braveBallContent = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[0];
			String dragBallContent = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[1];
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(braveBalls)){
				return false;
			}
			if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(dragBalls)){
				return false;
			}
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if(!TicketWinningUtil.isBallCount(braveBallsInt.length, 1, 4)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(braveBallsInt, 1, 20)){
				return false;
			}
			if(!TicketWinningUtil.isBallContentNoRepeatAndInRange(dragBallsInt, 1, 20)){
				return false;
			}
			if(TicketWinningUtil.isTwoArrContainSameElement(braveBallsInt, dragBallsInt)){
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
		String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		Prize rePrize = new Prize();
		Prize prize = null;
		for(int i = 0, len = balls.length; i< len; i++){
			prize = calXYNCWinningTicket(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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
			sum += calXYNCBallCount(ballContents[i], playType);
		}
		return sum;
	}
	
	private static int calXYNCBallCount(String ballContent, String playType){
		int count = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_SHOTS)){//选一数投(植物单选)
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_COMPOUND)){//选一数投复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			count = balls.length;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_RED)){//选一红投(动物单选)
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_GROUP)){//选二连组(背靠背)
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_STRAIGHT)){//选二连直(连连中)
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_COMPOUND)){//选二连组复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			count = combBalls.size();		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_BRAVE)){//选二连组胆拖
			count = chooseAnyBraveCount(ballContent, 2);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_GROUP)){//选三前组(三全中)
			count = 1;			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_STRAIGHT)){//选三前直(三连中)
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_COMPOUND)){//选三前组复式			
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 3, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			count = combBalls.size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_BRAVE)){//选三前组胆拖
			String[] braveBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			List<String> combDragBalls = CombinationUtil.combine(dragBalls, 3-braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			count = combDragBalls.size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_SINGLE)){//选二任选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_COMPOUND)){//选二任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			count = combBalls.size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_BRAVE)){//选二任选胆拖
			count = chooseAnyBraveCount(ballContent, 2);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_SINGLE)){//选三任选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_COMPOUND)){//选三任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			count = combBalls.size();		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_BRAVE)){//选三连组胆拖
			count = chooseAnyBraveCount(ballContent, 3);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_SINGLE)){//选四任选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_COMPOUND)){//选四任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 4, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			count = combBalls.size();		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_BRAVE)){//选四连组胆拖
			count = chooseAnyBraveCount(ballContent, 4);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_SINGLE)){//选五任选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_COMPOUND)){//选五任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 5, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			count = combBalls.size();	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_BRAVE)){//选五连组胆拖
			count = chooseAnyBraveCount(ballContent, 5);
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
			details.addAll(listXYNCTicketDetail(ballContents[i], playType));
		}	
		return details;
	}
	
	private static List<String> listXYNCTicketDetail(String ballContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_SHOTS)){//选一数投(植物单选)
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_COMPOUND)){//选一数投复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			details.addAll(Arrays.asList(balls));
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_RED)){//选一红投(动物单选)
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_GROUP)){//选二连组(背靠背)
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_STRAIGHT)){//选二连直(连连中)
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_COMPOUND)){//选二连组复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_BRAVE)){//选二连组胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String braveBall = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			for(int i = 0; i < dragBalls.length; i++){
				details.add(braveBall+TicketWinningConstantsUtil.SEPARATOR_SHUXIAN+dragBalls[i]);
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_GROUP)){//选三前组(三全中)
			details.add(ballContent);			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_STRAIGHT)){//选三前直(三连中)
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_COMPOUND)){//选三前组复式			
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 3, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_BRAVE)){//选三前组胆拖
			String[] braveBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			List<String> combDragBalls = CombinationUtil.combine(dragBalls, 3-braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			String content = "";
			
			for(int i = 0; i < combDragBalls.size(); i++){
				if(braveBalls.length == 1){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + combDragBalls.get(i);
				}
				else if(braveBalls.length == 2){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + braveBalls[1] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + combDragBalls.get(i); 
				}
				else{
					content = combDragBalls.get(i);
				}
				
				details.add(content);
			}	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_SINGLE)){//选二任选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_COMPOUND)){//选二任选复式
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 2, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_BRAVE)){//选二任选胆拖
			details.addAll(chooseAnyBraveDetail(ballContent, 2));
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_SINGLE)){//选三任选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_COMPOUND)){//选三任选复式
			List<String> combBalls = CombinationUtil.combine( ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 3, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_BRAVE)){//选三连组胆拖
			details.addAll(chooseAnyBraveDetail(ballContent, 3));
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_SINGLE)){//选四任选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_COMPOUND)){//选四任选复式
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 4, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_BRAVE)){//选四连组胆拖
			details.addAll(chooseAnyBraveDetail(ballContent, 4));
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_SINGLE)){//选五任选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_COMPOUND)){//选五任选复式
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 5, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_BRAVE)){//选五连组胆拖
			details.addAll(chooseAnyBraveDetail(ballContent, 5));
		}
		return details;
	}
	/**
	 * 根据玩法计算一注彩票中奖金额
	 * @param ballContent 一注彩票投注内容
	 * @param playType 玩法
	 * @return
	 */
	private static Prize calXYNCWinningTicket(String ballContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_SHOTS)){//选一数投(植物单选)
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if(ballContent.equals(winningBalls[0])){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.XYNC_CHOOSEONE_SHOTS_PRIZE_LEVEL));
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_COMPOUND)){//选一数投复式
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			for(int i = 0; i < balls.length; i++){
				if(balls[i].equals(winningBalls[0])){
					prize.setPrize(true);
					prizeAmount = prizeAmountLevelMap.get(PrizeAmount.XYNC_CHOOSEONE_SHOTS_PRIZE_LEVEL);
					if(prizeAmount == 0){
						prize.setPrizeAmount(prizeAmount);
						return prize;
					}
					else{
						prize.setPrizeAmount(prize.getPrizeAmount() + prizeAmount);
					}
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEONE_RED)){//选一红投(动物单选)
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			if("19".equals(winningBalls[0]) || "20".equals(winningBalls[0])){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.XYNC_CHOOSEONE_RED_PRIZE_LEVEL));
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_GROUP)){//选二连组(背靠背)
			prizeLevel = chooseTwoUnOrder(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_STRAIGHT)){//选二连直(连连中)
			prizeLevel = chooseTwoOrder(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_COMPOUND)){//选二连组复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseTwoUnOrder(combBalls.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWO_BRAVE)){//选二连组胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String braveBall = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			for(int i = 0; i < dragBalls.length; i++){
				prizeLevel = chooseTwoUnOrder(braveBall+TicketWinningConstantsUtil.SEPARATOR_SHUXIAN+dragBalls[i], winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_GROUP)){//选三前组(三全中)
			prizeLevel = chooseThreeGroup(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_STRAIGHT)){//选三前直(三连中)
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			int count = 0;
			for(int i = 0; i < 3; i++){
				if(winningBalls[i].equals(balls[i])){
					count++;
				}
			}
			
			if(count == 3){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.XYNC_CHOOSETHREE_STRAIGHT_PRIZE_LEVEL));
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_COMPOUND)){//选三前组复式			
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), 3, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseThreeGroup(combBalls.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREE_BRAVE)){//选三前组胆拖
			String[] braveBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			List<String> combDragBalls = CombinationUtil.combine(dragBalls, 3-braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			String content = "";
			
			for(int i = 0; i < combDragBalls.size(); i++){
				if(braveBalls.length == 1){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + combDragBalls.get(i);
				}
				else if(braveBalls.length == 2){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + braveBalls[1] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + combDragBalls.get(i); 
				}
				else{
					content = combDragBalls.get(i);
				}
				
				prizeLevel = chooseThreeGroup(content, winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_SINGLE)){//选二任选单式
			prizeLevel = chooseAnyUnOrder(ballContent, 2, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_COMPOUND)){//选二任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseAnyUnOrder(combBalls.get(i), 2, winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETWOANY_BRAVE)){//选二任选胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			String braveBall = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			
			for(int i = 0; i < dragBalls.length; i++){
				prizeLevel = chooseAnyUnOrder(braveBall+TicketWinningConstantsUtil.SEPARATOR_SHUXIAN+dragBalls[i], 2, winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_SINGLE)){//选三任选单式
			prizeLevel = chooseAnyUnOrder(ballContent, 3, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_COMPOUND)){//选三任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseAnyUnOrder(combBalls.get(i), 3, winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSETHREEANY_BRAVE)){//选三连组胆拖
			prize = chooseAnyBrave(ballContent, 3, winningBallContent, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_SINGLE)){//选四任选单式
			prizeLevel = chooseAnyUnOrder(ballContent, 4, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_COMPOUND)){//选四任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 4, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseAnyUnOrder(combBalls.get(i), 4, winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFOURANY_BRAVE)){//选四连组胆拖
			prize = chooseAnyBrave(ballContent, 4, winningBallContent, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_SINGLE)){//选五任选单式
			prizeLevel = chooseAnyUnOrder(ballContent, 5, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_COMPOUND)){//选五任选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			List<String> combBalls = CombinationUtil.combine(balls, 5, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseAnyUnOrder(combBalls.get(i), 5, winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_XYNC_CHOOSEFIVEANY_BRAVE)){//选五连组胆拖
			prize = chooseAnyBrave(ballContent, 5, winningBallContent, prizeAmountLevelMap);
		}

		return prize;
	}
	
	/**
	 * 任选
	 * @param ballContent
	 * @param ballNum
	 * @return
	 */
	private static int chooseAnyUnOrder(String ballContent, int ballNum, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		
		int prizeLevel = 0;
		int count = 0;
		for(int i = 0; i < winningBalls.length; i++){
			for(int j = 0; j < ballNum; j++){
				if(winningBalls[i].equals(balls[j])){
					count++;
				}
			}
		}
		
		if(count == ballNum){
			switch(ballNum){
				case 2:
					prizeLevel = PrizeAmount.XYNC_CHOOSE_TWO_ANY_PRIZE_LEVEL;
					break;
				case 3:
					prizeLevel = PrizeAmount.XYNC_CHOOSE_THREE_ANY_PRIZE_LEVEL;
					break;
				case 4:
					prizeLevel = PrizeAmount.XYNC_CHOOSE_FOUR_ANY_PRIZE_LEVEL;
					break;
				case 5:
					prizeLevel = PrizeAmount.XYNC_CHOOSE_FIVE_ANY_PRIZE_LEVEL;
					break;
				default:
					break;
			}
		}
		return prizeLevel;
	}
	
	/**
	 * 选二连组
	 * @param ballContent
	 * @return
	 */
	private static int chooseTwoUnOrder(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		
		for(int i = 0; i < winningBalls.length; i++){
			if(balls[0].equals(winningBalls[i])){
				if( i != 0){
					if(balls[1].equals(winningBalls[i-1])){
						return PrizeAmount.XYNC_CHOOSE_TWO_UNORDER_PRIZE_LEVEL;
					}
				}
				if(i != winningBalls.length - 1){
					if(balls[1].equals(winningBalls[i+1])){
						return PrizeAmount.XYNC_CHOOSE_TWO_UNORDER_PRIZE_LEVEL;
					}
				}
			}
		}
		return 0;
	}
	
	/**
	 * 选二连直
	 * @param ballContent
	 * @return
	 */
	private static int chooseTwoOrder(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		
		for(int i = 0; i < winningBalls.length - 1; i++){
			if(balls[0].equals(winningBalls[i]) && balls[1].equals(winningBalls[i+1])){
				return PrizeAmount.XYNC_CHOOSE_TWO_ORDER_PRIZE_LEVEL;
			}
		}
		return 0;
	}
	
	/**
	 * 选三
	 * @param ballContent
	 * @return
	 */
	private static int chooseThreeGroup(String ballContent, String winningBallContent){		
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		String winningThreeBallContent = winningBalls[0] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + winningBalls[1] + TicketWinningConstantsUtil.SEPARATOR_SHUXIAN + winningBalls[2];
		String orderBall = TicketWinningUtil.sortBallContent(ballContent, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		
		String orderWinningBall = TicketWinningUtil.sortBallContent(winningThreeBallContent, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
		if(orderBall.equals(orderWinningBall)){
			return PrizeAmount.XYNC_CHOOSETHREE_ALL_PRIZE_LEVEL;
		}	
		
		return 0;
	}
	
	/**
	 * 任选胆拖
	 * @param ballContent
	 * @param ballCount
	 * @return
	 */
	private static Prize chooseAnyBrave(String ballContent, int ballCount, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap){
		Prize prize = new Prize();
		int prizeLevel = 0;
		long prizeAmount = 0L;
		List<String> combBalls = null;
		String[] balls = null;
		String braveBall = "";
		String[] braveBalls = null;
		String[] dragBalls = null;
		
		if(ballContent.indexOf(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO) == -1){
			combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), ballCount, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseAnyUnOrder(combBalls.get(i), ballCount, winningBallContent);
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
		else{
			balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			braveBall = balls[0];
			braveBalls = braveBall.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			combBalls = CombinationUtil.combine(dragBalls, ballCount - braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseAnyUnOrder(braveBall+TicketWinningConstantsUtil.SEPARATOR_SHUXIAN+combBalls.get(i), ballCount, winningBallContent);
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
			
		return prize;
	}
	
	private static int chooseAnyBraveCount(String ballContent, int ballCount){
		List<String> combBalls = null;
		String[] balls = null;
		String braveBall = "";
		String[] braveBalls = null;
		String[] dragBalls = null;
		
		if(ballContent.indexOf(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO) == -1){
			combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), ballCount, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
		}
		else{
			balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			braveBall = balls[0];
			braveBalls = braveBall.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			combBalls = CombinationUtil.combine(dragBalls, ballCount - braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
		}
		return combBalls.size();
	}
	
	private static List<String> chooseAnyBraveDetail(String ballContent, int ballCount){
		List<String> details = new ArrayList<String>();		
		List<String> combBalls = null;
		String[] balls = null;
		String braveBall = "";
		String[] braveBalls = null;
		String[] dragBalls = null;
		
		if(ballContent.indexOf(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO) == -1){
			combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX), ballCount, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			details.addAll(combBalls);
		}
		else{
			balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_POZHEHAO);
			braveBall = balls[0];
			braveBalls = braveBall.split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_SHUXIAN_REGX);
			combBalls = CombinationUtil.combine(dragBalls, ballCount - braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_SHUXIAN);
			
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBall+TicketWinningConstantsUtil.SEPARATOR_SHUXIAN+combBalls.get(i));
			}
		}
		return details;
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
