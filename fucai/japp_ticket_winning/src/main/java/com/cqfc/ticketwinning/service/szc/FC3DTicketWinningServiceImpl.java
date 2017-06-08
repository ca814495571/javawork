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

public class FC3DTicketWinningServiceImpl implements ITicketWinningService{
	
	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;

		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_SINGLE)) {//单选单式
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_COMPOUND)) {//单选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			
			String geBallContent = "";
			String shiBallContent = "";
			String baiBallContent = "";
			
			if (ballContents.length > 1) {
				return false;
			}
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			if (balls.length != 3) {
				return false;
			}
			baiBallContent = balls[0];
			shiBallContent = balls[1];
			geBallContent = balls[2];
			
			String[] baiBalls = baiBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] shiBalls = shiBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] geBalls = geBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(baiBalls)) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(shiBalls)) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(geBalls)) {
				return false;
			}
			
			int[] baiBallsInt = TicketWinningUtil.str2IntArr(baiBalls);
			int[] shiBallsInt = TicketWinningUtil.str2IntArr(shiBalls);
			int[] geBallsInt = TicketWinningUtil.str2IntArr(geBalls);
			
			if (baiBallsInt.length < 1 || baiBallsInt.length > 10) {
				return false;
			}
			if (shiBallsInt.length < 1 || shiBallsInt.length > 10) {
				return false;
			}
			if (geBallsInt.length < 1 || geBallsInt.length > 10) {
				return false;
			}
			if ((baiBallsInt.length + shiBallsInt.length + geBallsInt.length) == 3){
				return false;
			}
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(baiBallsInt, 0, 9)) {
				return false;
			}
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(shiBallsInt, 0, 9)) {
				return false;
			}
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(geBallsInt, 0, 9)) {
				return false;
			}		
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_PACKAGENO)){//单选包号
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			for(int i = 0; i < ballContents.length; i++){
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				if(TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)){
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 0, 9)) {
					return false;
				}	
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_ANDVALUE)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ANDVALUE)){//单选和值
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_SINGLE)){//组三单式，组六单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			
			for(int i = 0; i < ballContents.length; i++){	
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_PACKAGENO)){//组三包号
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
				if (!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_ANDVALUE)){//组三和值
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);			

			if (ballContents.length > 1) {
				return false;
			}
			if (TicketWinningUtil.isHasZeroPrefixLessThanTen(orderContent)) {
				return false;
			}
			if (Integer.valueOf(orderContent) < 1 || Integer.valueOf(orderContent) > 26) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_PACKAGENO)){//组六包号
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
				if (!TicketWinningUtil.isBallContentASCAndInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_ANDVALUE)){//组六和值
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
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_1D)){//1D投注
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			String sortBallContent = "";
			String[] balls = null;
			int[] ballsInt = null;
			
			if (len > 5) {
				return false;
			}
			for (int i = 0; i < len; i++) {
				sortBallContent = TicketWinningUtil.sortBallContent(ballContents[i], TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				balls = sortBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isHasZeroPrefixLessThanTen(balls[0])) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if ((ballsInt[0] < 0) || (ballsInt[0] > 9) || (ballsInt[1] != 255) || (ballsInt[2] != 255)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_2D)){//2D投注
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			String sortBallContent = "";
			String[] balls = null;
			int[] ballsInt = null;
			
			if (len > 5) {
				return false;
			}
			for (int i = 0; i < len; i++) {
				sortBallContent = TicketWinningUtil.sortBallContent(ballContents[i], TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				balls = sortBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isHasZeroPrefixLessThanTen(balls[0])) {
					return false;
				}
				if (TicketWinningUtil.isHasZeroPrefixLessThanTen(balls[1])) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if ((ballsInt[0] < 0) || (ballsInt[0] > 9) || (ballsInt[1] < 0) || (ballsInt[1] > 9) || (ballsInt[2] != 255)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_CHOOSE)){//通选
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			String[] balls = null;
			int[] ballsInt = null;

			if (len > 5) {
				return false;
			}
			
			for (int i = 0; i < len; i++) {				
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (balls.length != 3) {
					return false;
				}
				if (!TicketWinningUtil.isBallContentInRange(ballsInt, 0, 9)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_1D)){//猜1D
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;

			if (len > 5) {
				return false;
			}
			if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(ballContents)) {
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContents);
			for (int i = 0; i < len; i++) {
				if (ballsInt[i] < 0 || ballsInt[i] > 9) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_2D)){//猜2D
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			String[] balls = null;
			int[] ballsInt = null;

			if (len > 5) {
				return false;
			}
			
			for (int i = 0; i < len; i++) {
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (ballsInt.length != 2) {
					 return false;
				}
				if (ballsInt[0] < 0 || ballsInt[0] > 9) {
					return false;
				}
				if (ballsInt[1] < 0 || ballsInt[1] > 9) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_THREE)){//包选3
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			
			for(int i = 0; i < ballContents.length; i++){
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (ballsInt.length != 3){
					return false;
				}
				if (!((ballsInt[0] == ballsInt[1]) || (ballsInt[1] == ballsInt[2]) || (ballsInt[0] == ballsInt[2]))) {
					return false;
				}
				if ((ballsInt[0] == ballsInt[1]) && (ballsInt[1] == ballsInt[2])) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_SIX)){//包选6
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			String[] balls = null;
			int[] ballsInt = null;
			
			if (ballContents.length > 5) {
				return false;
			}
			
			for(int i = 0; i < ballContents.length; i++){	
				balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if (TicketWinningUtil.isAnyHasZeroPrefixLessThanTen(balls)) {
					return false;
				}
				ballsInt = TicketWinningUtil.str2IntArr(balls);
				if (ballsInt.length != 3){
					return false;
				}
				if ((ballsInt[0] == ballsInt[1]) || (ballsInt[1] == ballsInt[2]) || (ballsInt[0] == ballsInt[2])) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_BIG_SMALL)){//猜大小
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			
			if (len > 5) {
				return false;
			}
			for (int i = 0; i < len; i++) {
				if (!(ballContents[i].equals("1") || ballContents[i].equals("2"))) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_THREE_SAME_ALL)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_TRACTOR)){//三同号全包 拖拉机
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;

			if (len > 1) {
				return false;
			}
			int[] ballsInt = TicketWinningUtil.str2IntArr(orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			if (ballsInt[0] != 255 || ballsInt[1] != 255 || ballsInt[2] != 255) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_ODD_EVEN)){//猜奇偶
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			int len = ballContents.length;
			
			if (len > 5) {
				return false;
			}
			for(int i = 0; i < len; i++){
				if (!(ballContents[i].equals("4") || ballContents[i].equals("5"))) {
					return false;
				}
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
			prize = calFC3DWinningTicket(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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
			sum += calFC3DBallCount(ballContents[i], playType);
		}
		
		return sum;
	}

	private static int calFC3DBallCount(String orderContent, String playType){
		int count = 0;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_SINGLE)) {//单选单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_COMPOUND)) {//单选复式	
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int baiBallCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int shiBallCount = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int geBallCount = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;			
			
			count = baiBallCount * shiBallCount * geBallCount;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_PACKAGENO)) {//单选包号
			int ballCount = 0;
			
			ballCount = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;			
			count = (int)Math.pow(ballCount, 3);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_ANDVALUE)){//单选和值
		    count = TicketWinningUtil.getFC3DRadioAndValue(Integer.valueOf(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_SINGLE)){//组三单式，组六单式
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_PACKAGENO)){//组三包号
			int ballCount = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 2) * 2;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_ANDVALUE)){//组三和值
		    count = TicketWinningUtil.getFC3DGroup3AndValue(Integer.valueOf(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_PACKAGENO)){//组六包号
			int ballCount = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 3);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_ANDVALUE)){//组六和值
			count = TicketWinningUtil.getFC3DGroup6AndValue(Integer.valueOf(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_1D)){//1D投注
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_2D)){//2D投注
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ANDVALUE)){//和数投注
			count = 1;	
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_CHOOSE)){//通选
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_1D)){//猜1D
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_2D)){//猜2D
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_THREE)){//包选3
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_SIX)){//包选6
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_BIG_SMALL)){//猜大小
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_THREE_SAME_ALL)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_TRACTOR)){//三同号全包 拖拉机
			count = 1;
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_ODD_EVEN)){//猜奇偶
			count = 1;
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
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		List<String> details = new ArrayList<String>();
		
		for(int i = 0; i< ballContents.length; i++){
			details.addAll(listFC3DTicketDetail(ballContents[i], playType));
		}
		
		return details;
		
	}


	private static List<String> listFC3DTicketDetail(String orderContent, String playType){
		List<String> details = new ArrayList<String>();
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_SINGLE)) {//单选单式
			details.add(orderContent);				
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_COMPOUND)) {//单选复式
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] baiBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] shiBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] geBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < baiBalls.length; i++){
				for(int j = 0; j < shiBalls.length; j++){
					for(int k = 0; k < geBalls.length; k++){
						details.add(baiBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + shiBalls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + geBalls[k]);
					}
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_PACKAGENO)) {//单选包号
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < balls.length; i++){
				for(int j = 0; j < balls.length; j++){
					for(int k = 0; k < balls.length; k++){
						details.add(balls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + balls[k]);							
					}
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_ANDVALUE)){//单选和值
			details.addAll(TicketWinningUtil.getFC3DRadioAndValueDetail(Integer.valueOf(orderContent)));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_SINGLE)){//组三单式，组六单式
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_PACKAGENO)){//组三包号
			details.addAll(TicketWinningUtil.getGroup3Compound(orderContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_ANDVALUE)){//组三和值
			details.addAll(TicketWinningUtil.getGroup3AndValue(orderContent));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_PACKAGENO)){//组六包号
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			details.addAll(CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_ANDVALUE)){//组六和值
			int sum = Integer.valueOf(orderContent);
			
			for(int i = 0; i < 10; i++){
				for(int j = i + 1; j < 10; j++){
					for(int k = j + 1; k < 10; k++){
						if((i + j + k) == sum){
							details.add(i + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + j + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + k);
						}
					}
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_1D)){//1D投注
			details.add(orderContent);	
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_2D)){//2D投注
			details.add(orderContent);	
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ANDVALUE)){//和数投注
			details.add(orderContent);	
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_CHOOSE)){//通选
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_1D)){//猜1D
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_2D)){//猜2D
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_THREE)){//包选3
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_SIX)){//包选6
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_BIG_SMALL)){//猜大小
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_THREE_SAME_ALL)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_TRACTOR)){//三同号全包 拖拉机
			details.add(orderContent);
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_ODD_EVEN)){//猜奇偶
			details.add(orderContent);
		}
		
		return details;
	}
	
	/**
	 * 根据玩法计算一注彩票中奖金额
	 * @param ballContent 一注彩票投注内容
	 * @param playType 玩法
	 * @return
	 */
	private static Prize calFC3DWinningTicket(String ballContent, String playType, String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_SINGLE)){//单选单式
			prizeLevel = calFC3DRadio(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_COMPOUND)){//单选复式
			List<String> list = listFC3DTicketDetail(ballContent, playType);
			
			for(int i = 0, len = list.size(); i < len; i++){
				prizeLevel = calFC3DRadio(list.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_PACKAGENO)){//单选包号
			List<String> list = listFC3DTicketDetail(ballContent, playType);
			
			for(int i = 0, len = list.size(); i < len; i++){
				prizeLevel = calFC3DRadio(list.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_RADIO_ANDVALUE)){//单选和值
			prizeLevel = calFC3DRadioAndValue(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_SINGLE)){//组三单式，组六单式
			//注意：组三单式和组六单式玩法类型都是5，也可以相互混投
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			if((balls[0].equals(balls[1])) || (balls[1].equals(balls[2])) || (balls[0].equals(balls[2]))){//组三单式
				prizeLevel = calFC3DGroup3(ballContent, winningBallContent);
				if(prizeLevel > 0){
					prize.setPrize(true);
					prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
				}
			}
			else{
				String sortContent = TicketWinningUtil.sortBallContent(winningBallContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				if(ballContent.equals(sortContent)){
					prize.setPrize(true);
					prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.FC3D_GROUP6_PRIZE_LEVEL));
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_PACKAGENO)){//组三包号
			List<String> ballList = combBallContent(ballContent);
			for(int i = 0, len = ballList.size(); i < len; i++){
				prizeLevel = calFC3DGroup3(ballList.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP3_ANDVALUE)){//组三和值
			prizeLevel = calAndValue(ballContent, PrizeAmount.FC3D_GROUP3_PRIZE_LEVEL, true, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_PACKAGENO)){//组六包号
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			List<String> combs = CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String winningContent = sortFC3DBall(winningBallContent);
			
			for(int i = 0; i < combs.size(); i++){
				if(winningContent.equals(combs.get(i))){
					prize.setPrize(true);
					prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.FC3D_GROUP6_PRIZE_LEVEL));
					break;
				}
			}
			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GROUP6_ANDVALUE)){//组六和值
			prizeLevel = calAndValue(ballContent, PrizeAmount.FC3D_GROUP6_PRIZE_LEVEL, false, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_1D)){//1D投注
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));

			if((ballsInt[0] == winningBallsInt[0]) || (ballsInt[1] == winningBallsInt[1]) || (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_1D_PRIZE_LEVEL;
				checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
			}
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_2D)){//2D投注
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[1])){
				prizeLevel = PrizeAmount.FC3D_2D_PRIZE_LEVEL;
			}
			else if((ballsInt[1] == winningBallsInt[1]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_2D_PRIZE_LEVEL;
			}
			else if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_2D_PRIZE_LEVEL;
			}
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ANDVALUE)){//和数投注
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int winningBallsSum = winningBallsInt[0] + winningBallsInt[1] + winningBallsInt[2];
			int value = Integer.valueOf(ballContent);
			
			if(value == winningBallsSum){
				if(value == 0 || value == 27){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_0OR27_PRIZE_LEVEL;
				}
				else if(value == 1 || value == 26){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_1OR26_PRIZE_LEVEL;
				}
				else if(value == 2 || value == 25){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_2OR25_PRIZE_LEVEL;
				}
				else if(value == 3 || value == 24){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_3OR24_PRIZE_LEVEL;
				}
				else if(value == 4 || value == 23){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_4OR23_PRIZE_LEVEL;
				}
				else if(value == 5 || value == 22){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_5OR22_PRIZE_LEVEL;
				}
				else if(value == 6 || value == 21){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_6OR21_PRIZE_LEVEL;
				}
				else if(value == 7 || value == 20){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_7OR20_PRIZE_LEVEL;
				}
				else if(value == 8 || value == 19){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_8OR19_PRIZE_LEVEL;
				}
				else if(value == 9 || value == 18){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_9OR18_PRIZE_LEVEL;
				}
				else if(value == 10 || value == 17){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_10OR17_PRIZE_LEVEL;
				}
				else if(value == 11 || value == 16){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_11OR16_PRIZE_LEVEL;
				}
				else if(value == 12 || value == 15){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_12OR15_PRIZE_LEVEL;
				}
				else if(value == 13 || value == 14){
					prizeLevel = PrizeAmount.FC3D_ANDVALUE_13OR14_PRIZE_LEVEL;
				}
			}
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_CHOOSE)){//通选
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[1]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_COMMON_FIRST_PRIZE_LEVEL;
			}
			else if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[1])){
				prizeLevel = PrizeAmount.FC3D_COMMON_SECOND_PRIZE_LEVEL;
			}
			else if((ballsInt[1] == winningBallsInt[1]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_COMMON_SECOND_PRIZE_LEVEL;
			}
			else if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_COMMON_SECOND_PRIZE_LEVEL;
			}
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_1D)){//猜1D
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int value = Integer.valueOf(ballContent);
			
			if(value == winningBallsInt[0]){
				prizeLevel = PrizeAmount.FC3D_GUESS1D_1_PRIZE_LEVEL;
				if(value == winningBallsInt[1]){
					prizeLevel = PrizeAmount.FC3D_GUESS1D_2_PRIZE_LEVEL;
					if(value == winningBallsInt[2]){
						prizeLevel = PrizeAmount.FC3D_GUESS1D_3_PRIZE_LEVEL;
					}
				}
			}
			else if(value == winningBallsInt[1]){
				prizeLevel = PrizeAmount.FC3D_GUESS1D_1_PRIZE_LEVEL;
				if(value == winningBallsInt[2]){
					prizeLevel = PrizeAmount.FC3D_GUESS1D_2_PRIZE_LEVEL;
				}
			}
			else if(value == winningBallsInt[2]){
				prizeLevel = PrizeAmount.FC3D_GUESS1D_1_PRIZE_LEVEL;
			}
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_2D)){//猜2D
			String winningSortBallContent = TicketWinningUtil.sortBallContent(winningBallContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String sortBallContent = TicketWinningUtil.sortBallContent(ballContent, TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningSortBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] ballsInt = TicketWinningUtil.str2IntArr(sortBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			
		    if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[1])){
		    	if(ballsInt[0] == ballsInt[1]){
		    		prizeLevel = PrizeAmount.FC3D_GUESS2D_2_SAME_PRIZE_LEVEL;		    		
		    	}
		    	else{
		    		prizeLevel = PrizeAmount.FC3D_GUESS2D_2_DIFF_PRIZE_LEVEL;
		    	}
		    }
		    else if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[2])){
		    	if(ballsInt[0] == ballsInt[1]){
		    		prizeLevel = PrizeAmount.FC3D_GUESS2D_2_SAME_PRIZE_LEVEL;		    		
		    	}
		    	else{
		    		prizeLevel = PrizeAmount.FC3D_GUESS2D_2_DIFF_PRIZE_LEVEL;
		    	}
		    }
		    else if((ballsInt[0] == winningBallsInt[1]) && (ballsInt[1] == winningBallsInt[2])){
		    	if(ballsInt[0] == ballsInt[1]){
		    		prizeLevel = PrizeAmount.FC3D_GUESS2D_2_SAME_PRIZE_LEVEL;		    		
		    	}
		    	else{
		    		prizeLevel = PrizeAmount.FC3D_GUESS2D_2_DIFF_PRIZE_LEVEL;
		    	}
		    }
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);			
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_THREE)){//包选3
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[1]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_ALL_3_PRIZE_LEVEL;
				checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
			}
			else{
				String winningSortContent = sortFC3DBall(winningBallContent);
				String ballSortContent = sortFC3DBall(ballContent);
		
				if(winningSortContent.equals(ballSortContent)){
					prizeLevel = PrizeAmount.FC3D_ALL_GROUP_3_PRIZE_LEVEL;
					checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
				}
			}
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_ALL_CHOOSE_SIX)){//包选6
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int[] ballsInt = TicketWinningUtil.str2IntArr(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			if((ballsInt[0] == winningBallsInt[0]) && (ballsInt[1] == winningBallsInt[1]) && (ballsInt[2] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_ALL_6_PRIZE_LEVEL;
				checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
			}
			else{
				String winningSortContent = sortFC3DBall(winningBallContent);
				String ballSortContent = sortFC3DBall(ballContent);
		
				if(winningSortContent.equals(ballSortContent)){
					prizeLevel = PrizeAmount.FC3D_ALL_GROUP_6_PRIZE_LEVEL;
					checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
				}
			}
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_BIG_SMALL)){//猜大小
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int winningBallsSum = winningBallsInt[0] + winningBallsInt[1] + winningBallsInt[2];
			int value = Integer.valueOf(ballContent);
			
			if(value == 1 && winningBallsSum >= 0 && winningBallsSum <= 8){
				prizeLevel = PrizeAmount.FC3D_GUESS_BIG_SMALL_PRIZE_LEVEL;
			}
			else if(value == 2 && winningBallsSum >= 19 && winningBallsSum <= 27){
				prizeLevel = PrizeAmount.FC3D_GUESS_BIG_SMALL_PRIZE_LEVEL;
			}
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		} 
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_THREE_SAME_ALL)){//三同号全包
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			if((winningBallsInt[0] == winningBallsInt[1]) && (winningBallsInt[1] == winningBallsInt[2])){
				prizeLevel = PrizeAmount.FC3D_GUESS_3_SAME_PRIZE_LEVEL;
				checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
			}
		}
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_TRACTOR)){//拖拉机
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			
			if(((winningBallsInt[1] - winningBallsInt[0] == 1) && (winningBallsInt[2] - winningBallsInt[1] == 1)) || ((winningBallsInt[0] - winningBallsInt[1] == 1) && (winningBallsInt[1] - winningBallsInt[2] == 1))){
				prizeLevel = PrizeAmount.FC3D_TACTOR_PRIZE_LEVEL;
				checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
			}
		}
		else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_FC3D_GUESS_ODD_EVEN)){//猜奇偶
			int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
			int value = Integer.valueOf(ballContent);
			
			if((value == 4) && (winningBallsInt[0] % 2 == 0) && (winningBallsInt[1] % 2 == 0) && (winningBallsInt[2] % 2 == 0)){
				prizeLevel = PrizeAmount.FC3D_GUESS_ODD_EVEN_PRIZE_LEVEL;
			}
			else if((value == 5) && (winningBallsInt[0] % 2 != 0) && (winningBallsInt[1] % 2 != 0) && (winningBallsInt[2] % 2 != 0)){
				prizeLevel = PrizeAmount.FC3D_GUESS_ODD_EVEN_PRIZE_LEVEL;
			}
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		return prize;
	}

	/**
	 * 计算一注福彩3D直/单选彩票中奖金额，彩票内容格式(1:2:3)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calFC3DRadio(String orderBallContent, String winningBallContent){
		if(orderBallContent.equals(winningBallContent)){
			return PrizeAmount.FC3D_RADIO_PRIZE_LEVEL;
		}
		
		return 0;		
	}
	
	/**
	 * 计算一注福彩3D直/单选和值彩票中奖金额，彩票内容格式(1)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calFC3DRadioAndValue(String orderBallContent, String winningBallContent){
		int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		int winningBallSum = 0;
		for(int i = 0, len = winningBallsInt.length; i < len; i++){
			winningBallSum += winningBallsInt[i];
		}
		if(Integer.valueOf(orderBallContent) == winningBallSum){
			return PrizeAmount.FC3D_RADIO_PRIZE_LEVEL;
		}
		
		return 0;		
	}
	
	/**
	 * 计算一注福彩3D组三彩票中奖金额，彩票内容格式(1:2:2)
	 * @param orderBallContent 投注彩票内容
	 * @return
	 */
	private static int calFC3DGroup3(String orderBallContent, String winningBallContent){
		String winningContent = sortFC3DBall(winningBallContent);
		String[] winningBalls = winningContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		if(!(winningBalls[0].equals(winningBalls[1])  || winningBalls[1].equals(winningBalls[2]))){
			return 0;
		}
		else{
			if(orderBallContent.equals(winningContent)){
				return PrizeAmount.FC3D_GROUP3_PRIZE_LEVEL;
			}
		}
		
		return 0;		
	}
	
	/**
	 * 计算组三或组六和值奖金
	 * @param ballContent
	 * @param prizeAmount
	 * @param isGroup3
	 * @return
	 */
	private static int calAndValue(String ballContent, int prizeLevel, boolean isGroup3, String winningBallContent){
		int[] winningBallsInt = TicketWinningUtil.str2IntArr(winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO));
		if(isGroup3 && !((winningBallsInt[0] == winningBallsInt[1]) || (winningBallsInt[1] == winningBallsInt[2]) || (winningBallsInt[0] == winningBallsInt[2]))){
			return 0;
		}
		int sum = 0;
		for(int i = 0, len = winningBallsInt.length; i < len; i++){
			sum += winningBallsInt[i];
		}
		if(Integer.valueOf(ballContent) == sum){
			return prizeLevel;
		}	
		
		return 0;
	}
	
	/**
	 * 从小到大排序
	 * @param ballContent
	 * @return
	 */
	private static String sortFC3DBall(String ballContent){
		String retStr = "";
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		for(int i = 0; i < balls.length; i++){
			for(int j = 0; j < i; j++){
				if(Integer.valueOf(balls[j]) > Integer.valueOf(balls[i])){
					String temp = balls[j];
					balls[j] = balls[i];
					balls[i] = temp;
				}
			}
		}
		int k = 0;
		for(; k < balls.length - 1; k++){
			retStr += balls[k] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO;
		}
		retStr += balls[k];
		return retStr;
	}
	
	/**
	 * 组合投注内容
	 * @param ballContent
	 * @return
	 */
	private static List<String> combBallContent(String ballContent){
		List<String> details = new ArrayList<String>();
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		List<String> combs = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String combContent = "";
		String[] combBalls = null;
		for(int i = 0; i < combs.size(); i++){
			combContent = combs.get(i);
			combBalls = combContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			details.add(combBalls[0]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+combBalls[0]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+combBalls[1]);
			details.add(combBalls[0]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+combBalls[1]+TicketWinningConstantsUtil.SEPARATOR_MAOHAO+combBalls[1]);
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
