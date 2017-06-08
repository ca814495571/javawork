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

public class SYX5TicketWinningServiceImpl implements ITicketWinningService{

	/**
	 *  校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	@Override
	public boolean validateContent(String orderContent, String playType) {
		boolean flag = true;
		
		if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANSHI)
		   || playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANSHI)){ // 11选5任选二单式 11选5前二组选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 2) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_FUSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_FUSHI)){ // 11选5任选二复式 11选5前二组选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 3 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANTUO)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANTUO)){ // 11选5任选二胆拖 11选5前二组选胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen != 1) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANSHI)){ // 11选5任选三单式 11选5前三组选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 3) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_FUSHI)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_FUSHI)){ // 11选5任选三复式 11选5前三组选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 4 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANTUO)
				|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANTUO)){ // 11选5任选三胆拖 11选5前三组选胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen > 2 ) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			if ((braveLen + dragLen) == 3) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANSHI)){ // 11选5任选四单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 4) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_FUSHI)){ // 11选5任选四复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 5 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANTUO)){ // 11选5任选四胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen > 3) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			if ((braveLen + dragLen) == 4) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANSHI)){ // 11选5任选五单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 5) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_FUSHI)){ // 11选5任选五复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 6 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANTUO)){ // 11选5任选五胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen > 5) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			if ((braveLen + dragLen) == 5) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANSHI)){ // 11选5任选六单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 6) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_FUSHI)){ // 11选5任选六复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 7 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANTUO)){ // 11选5任选六胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen > 5) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			if ((braveLen + dragLen) == 6) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANSHI)){ // 11选5任选七单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 7) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_FUSHI)){ // 11选5任选七复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 8 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANTUO)){ // 11选5任选七胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen > 6) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			if ((braveLen + dragLen) == 7) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANSHI)){ // 11选5任选八单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 8) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_FUSHI)){ // 11选5任选八复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 8 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANTUO)){ // 11选5任选八胆拖
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String braveBallContent = balls[0];
			String dragBallContent = balls[1];
			
			String[] braveBalls = braveBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = dragBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(braveBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(dragBalls)) {
				return false;
			}
			
			int[] braveBallsInt = TicketWinningUtil.str2IntArr(braveBalls);
			int[] dragBallsInt = TicketWinningUtil.str2IntArr(dragBalls);
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(braveBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(dragBallsInt, 1, 11)) {
				return false;
			}
			
			int braveLen = braveBallsInt.length;
			int dragLen = dragBallsInt.length;
			if (braveLen > 7) {
				return false;
			}
			
			if (dragLen < 2 || dragLen > (11 - braveLen)) {
				return false;
			}
			
			if ((braveLen + dragLen) == 8) {
				return false;
			}
			
			//胆码和拖码中不包含重复号码
			if (!TicketWinningUtil.isBallContentRepeat(braveBallContent + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_DANSHI)){ // 11选5前一直选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 1) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_FUSHI)){ // 11选5前一直选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			//用逗号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if (balls.length < 2 || balls.length > 11) {
				return false;
			}
			
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
				return false;
			}				
			
			int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(ballsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_DANSHI)){ // 11选5前二直选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			//不能重复且升序
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				
				if (balls.length != 2) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		}  else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_FUSHI)){ // 11选5前二直选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}			

			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 2) {
				return false;
			}
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if ((wanBalls.length + qianBalls.length) < 3) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(wanBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(qianBalls)) {
				return false;
			}
			
			int[] wanBallsInt = TicketWinningUtil.str2IntArr(wanBalls);				
			int[] qianBallsInt = TicketWinningUtil.str2IntArr(qianBalls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(wanBallsInt, 1, 11)) {
				return false;
			}
			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(qianBallsInt, 1, 11)) {
				return false;
			}
			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANSHI)){ // 11选5前三组选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}
			
			for (int i = 0; i < ballContents.length; i++) {
				//用逗号分隔
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if (balls.length != 3) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
			
				if (!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_DANSHI)){ // 11选5前三直选单式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多5注
			if (ballContents.length > 5) {
				return false;
			}			
			
			for(int i = 0, len = ballContents.length; i < len; i++){				
				String[] balls = ballContents[i].split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
				
				if (balls.length != 3) {
					return false;
				}
				
				if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(balls)) {
					return false;
				}				
				
				int[] ballsInt = TicketWinningUtil.str2IntArr(balls);				
				
				if (!TicketWinningUtil.isBallContentNoRepeatAndInRange(ballsInt, 1, 11)) {
					return false;
				}
			}			
		} else if (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_FUSHI)){ // 11选5前三直选复式
			String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
			
			//最多1注
			if (ballContents.length > 1) {
				return false;
			}	
			
			//用冒号分隔
			String[] balls = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			if (balls.length != 3) {
				return false;
			}
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if ((wanBalls.length + qianBalls.length + baiBalls.length) < 4) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(wanBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(qianBalls)) {
				return false;
			}
			if (!TicketWinningUtil.isAllHasZeroPrefixLessThanTen(baiBalls)) {
				return false;
			}
			
			int[] wanBallsInt = TicketWinningUtil.str2IntArr(wanBalls);				
			int[] qianBallsInt = TicketWinningUtil.str2IntArr(qianBalls);				
			int[] baiBallsInt = TicketWinningUtil.str2IntArr(baiBalls);				
		
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(wanBallsInt, 1, 11)) {
				return false;
			}			
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(qianBallsInt, 1, 11)) {
				return false;
			}
			if (!TicketWinningUtil.isBallContentNoRepeatAndASCAndInRange(baiBallsInt, 1, 11)) {
				return false;
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
		for (int i = 0, len = balls.length; i < len; i++) {
			prize = calSYX5WinningTicketPrize(balls[i], playType, winningBallContent, prizeAmountLevelMap);
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

	private Prize calSYX5WinningTicketPrize(String ballContent, String playType,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap) {
		Prize prize = new Prize();
		long prizeAmount = 0L;
		int prizeLevel = 0;
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANSHI)){// 11选5任选二单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 2);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_FUSHI)){// 11选5任选二复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 2);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANTUO)){// 11选5任选二胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 2);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANSHI)){// 11选5任选三单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 3);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_FUSHI)){// 11选5任选三复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 3);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANTUO)){// 11选5任选三胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 3);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANSHI)){// 11选5任选四单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 4);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_FUSHI)){// 11选5任选四复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 4);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANTUO)){// 11选5任选四胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 4);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANSHI)){// 11选5任选五单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 5);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_FUSHI)){// 11选5任选五复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 5);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANTUO)){// 11选5任选五胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 5);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANSHI)){// 11选5任选六单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 6);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_FUSHI)){// 11选5任选六复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 6);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANTUO)){// 11选5任选六胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 6);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANSHI)){// 11选5任选七单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 7);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_FUSHI)){// 11选5任选七复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 7);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANTUO)){// 11选5任选七胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 7);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANSHI)){// 11选5任选八单式
			prize = calDanShi(ballContent, winningBallContent, prizeAmountLevelMap,	prize, 8);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_FUSHI)){// 11选5任选八复式
			prize = calFuShi(ballContent, winningBallContent, prizeAmountLevelMap, 8);	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANTUO)){// 11选5任选八胆拖
			prize = calBraveDragPrize(ballContent, winningBallContent,	prizeAmountLevelMap, 8);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_DANSHI)){// 11选5前一直选单式
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			if(ballContent.equals(winningBalls[0])){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.SYX5_ZHI_XUAN1_LEVEL));
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_FUSHI)){// 11选5前一直选复式
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < balls.length; i++){
				if(balls[i].equals(winningBalls[0])){
					prize.setPrize(true);
					prizeAmount = prizeAmountLevelMap.get(PrizeAmount.SYX5_ZHI_XUAN1_LEVEL);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_DANSHI)){// 11选5前二直选单式
			prizeLevel = chooseTwoOrder(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_FUSHI)){// 11选5前二直选复式
			List<String> combBalls = listTicketDetails(ballContent, playType);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseTwoOrder(combBalls.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANSHI)){// 11选5前二组选单式
			prizeLevel = chooseTwoUnOrder(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_FUSHI)){// 11选5前二组选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANTUO)){// 11选5前二组选胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String braveBall = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < dragBalls.length; i++){
				prizeLevel = chooseTwoUnOrder(braveBall+TicketWinningConstantsUtil.SEPARATOR_DOUHAO+dragBalls[i], winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_DANSHI)){// 11选5前三直选单式
			String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			
			int count = 0;
			for(int i = 0; i < 3; i++){
				if(winningBalls[i].equals(balls[i])){
					count++;
				}
			}
			
			if(count == 3){
				prize.setPrize(true);
				prize.setPrizeAmount(prizeAmountLevelMap.get(PrizeAmount.SYX5_ZHI_XUAN3_LEVEL));
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_FUSHI)){// 11选5前三直选复式
			List<String> combBalls = listSYX5TicketDetail(ballContent, playType);
			
			for(int i = 0; i < combBalls.size(); i++){
				prizeLevel = chooseThreeOrder(combBalls.get(i), winningBallContent);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANSHI)){// 11选5前三组选单式
			prizeLevel = chooseThreeGroup(ballContent, winningBallContent);
			checkPrizeLevel(prizeLevel, prize, prizeAmountLevelMap);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_FUSHI)){// 11选5前三组选复式
			List<String> combBalls = CombinationUtil.combine(ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO), 3, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
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
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANTUO)){// 11选5前三组选胆拖
			String[] braveBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			List<String> combDragBalls = CombinationUtil.combine(dragBalls, 3-braveBalls.length, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String content = "";
			
			for(int i = 0; i < combDragBalls.size(); i++){
				if(braveBalls.length == 1){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + combDragBalls.get(i);
				}
				else if(braveBalls.length == 2){
					content = braveBalls[0] + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + braveBalls[1] + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + combDragBalls.get(i); 
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
		return prize;
	}

	private Prize calDanShi(String ballContent, String winningBallContent,
			Map<Integer, Long> prizeAmountLevelMap, Prize prize, int ballCount) {
		int prizeLevel;
		prizeLevel = chooseAnyUnOrder(ballContent, ballCount, winningBallContent);
		if (prizeLevel > 0) {
			prize.setPrize(true);
			prize.setPrizeAmount(prizeAmountLevelMap.get(prizeLevel));		
		}
		
		return prize;
	}

	private Prize calFuShi(String ballContent, String winningBallContent,
			Map<Integer, Long> prizeAmountLevelMap, int ballCount) {
		Prize prize = new Prize();
		long prizeAmount;
		int prizeLevel;
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		List<String> combBalls = CombinationUtil.combine(balls, 8, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		for(int i = 0; i < combBalls.size(); i++){
			prizeLevel = chooseAnyUnOrder(combBalls.get(i), 8, winningBallContent);
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

	private Prize calBraveDragPrize(String ballContent,
			String winningBallContent, Map<Integer, Long> prizeAmountLevelMap, int ballCount) {
		Prize prize = new Prize();
		long prizeAmount;
		int prizeLevel;
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		String braveBall = balls[0];
		String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		List<String> combBalls = CombinationUtil.combine(dragBalls, ballCount - braveBall.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		for(int i = 0, len = combBalls.size(); i < len; i++){
			prizeLevel = chooseAnyUnOrder(braveBall+TicketWinningConstantsUtil.SEPARATOR_DOUHAO+combBalls.get(i), ballCount, winningBallContent);
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
	 * 任选
	 * @param ballContent
	 * @param ballNum
	 * @return
	 */
	private int chooseAnyUnOrder(String ballContent, int ballNum, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);		
		
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
					prizeLevel = PrizeAmount.SYX5_REN_XUAN2_LEVEL;
					break;
				case 3:
					prizeLevel = PrizeAmount.SYX5_REN_XUAN3_LEVEL;
					break;
				case 4:
					prizeLevel = PrizeAmount.SYX5_REN_XUAN4_LEVEL;
					break;
				case 5:
					prizeLevel = PrizeAmount.SYX5_REN_XUAN5_LEVEL;
					break;				
				default:
					break;
			}
		}
		
		if(count == 5 && ballNum == 6){
			prizeLevel = PrizeAmount.SYX5_REN_XUAN6_LEVEL;
		}
		else if(count == 5 && ballNum == 7){
			prizeLevel = PrizeAmount.SYX5_REN_XUAN7_LEVEL;
		}
		else if(count == 5 && ballNum == 8){
			prizeLevel = PrizeAmount.SYX5_REN_XUAN8_LEVEL;
		}
		
		return prizeLevel;
	}
	
	/**
	 * 前二直选
	 * @param ballContent
	 * @return
	 */
	private int chooseTwoOrder(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
		
		
		if(balls[0].equals(winningBalls[0]) && balls[1].equals(winningBalls[1])){
			return PrizeAmount.SYX5_ZHI_XUAN2_LEVEL;
		}
		
		return 0;
	}
	
	/**
	 * 前三直选
	 * @param ballContent
	 * @return
	 */
	private int chooseThreeOrder(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);		
		
		if(balls[0].equals(winningBalls[0]) && balls[1].equals(winningBalls[1]) && balls[2].equals(winningBalls[2])){
			return PrizeAmount.SYX5_ZHI_XUAN3_LEVEL;
		}
		
		return 0;
	}
	
	/**
	 * 前二组选
	 * @param ballContent
	 * @return
	 */
	private int chooseTwoUnOrder(String ballContent, String winningBallContent){
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		
		if(balls[0].equals(winningBalls[0]) && balls[1].equals(winningBalls[1])){
			return PrizeAmount.SYX5_ZU_XUAN2_LEVEL;
		}
		else if(balls[0].equals(winningBalls[1]) && balls[1].equals(winningBalls[0])){
			return PrizeAmount.SYX5_ZU_XUAN2_LEVEL;
		}
	
		return 0;
	}
	
	/**
	 * 前三组选
	 * @param ballContent
	 * @return
	 */
	private int chooseThreeGroup(String ballContent, String winningBallContent){		
		String[] winningBalls = winningBallContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		String winningThreeBallContent = winningBalls[0] + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + winningBalls[1] + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + winningBalls[2];
		
		String winningBallSort = TicketWinningUtil.sortBallContent(winningThreeBallContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
		if(ballContent.equals(winningBallSort)){
			return PrizeAmount.SYX5_ZU_XUAN3_LEVEL;
		}	
		
		return 0;
	}

	@Override
	public int calBallCounts(String orderContent, String playType) {
		int sum = 0;
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			sum += calSYX5BallCount(ballContents[i], playType);
		}
		
		return sum;
	}

	private int calSYX5BallCount(String ballContent, String playType) {
		int count = 0;
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANSHI)){// 11选5任选二单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_FUSHI)){// 11选5任选二复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 2);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANTUO)){// 11选5任选二胆拖
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			count = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANSHI)){// 11选5任选三单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_FUSHI)){// 11选5任选三复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 3);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANTUO)){// 11选5任选三胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 3 - braveBallCount);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANSHI)){// 11选5任选四单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_FUSHI)){// 11选5任选四复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 4);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANTUO)){// 11选5任选四胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 4 - braveBallCount);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANSHI)){// 11选5任选五单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_FUSHI)){// 11选5任选五复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 5);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANTUO)){// 11选5任选五胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 5 - braveBallCount);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANSHI)){// 11选5任选六单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_FUSHI)){// 11选5任选六复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 6);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANTUO)){// 11选5任选六胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 6 - braveBallCount);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANSHI)){// 11选5任选七单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_FUSHI)){// 11选5任选七复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 7);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANTUO)){// 11选5任选七胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 7 - braveBallCount);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANSHI)){// 11选5任选八单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_FUSHI)){// 11选5任选八复式
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			int ballCount = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(ballCount, 8);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANTUO)){// 11选5任选八胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 8 - braveBallCount);		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_DANSHI)){// 11选5前一直选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_FUSHI)){// 11选5前一直选复式
			count =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_DANSHI)){// 11选5前二直选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_FUSHI)){// 11选5前二直选复式
		    count = listTicketDetails(ballContent, playType).size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANSHI)){// 11选5前二组选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_FUSHI)){// 11选5前二组选复式
			int ballCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			count = TicketWinningUtil.getCombNum(ballCount, 2);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANTUO)){// 11选5前二组选胆拖
			String[] balls =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			count = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;	
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_DANSHI)){// 11选5前三直选单式
			count = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO).length;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_FUSHI)){// 11选5前三直选复式
			count = listTicketDetails(ballContent, playType).size();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANSHI)){// 11选5前三组选单式
			count = 1;
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_FUSHI)){// 11选5前三组选复式
			int ballCount =  ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			count = TicketWinningUtil.getCombNum(ballCount, 3);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANTUO)){// 11选5前三组选胆拖
			int braveBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			int dragBallCount = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			count = TicketWinningUtil.getCombNum(dragBallCount, 3 - braveBallCount);
		}
		return count;
	}

	@Override
	public List<String> listTicketDetails(String orderContent, String playType) {
		List<String> details = new ArrayList<String>();
		String[] ballContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_JINGHAO);
		
		for(int i = 0; i < ballContents.length; i++){
			details.addAll(listSYX5TicketDetail(ballContents[i], playType));
		}
		
		return details;
	}

	private List<String> listSYX5TicketDetail(String ballContent,
			String playType) {
		List<String> details = new ArrayList<String>();
		
		if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANSHI)){// 11选5任选二单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_FUSHI)){// 11选5任选二复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN2_DANTUO)){// 11选5任选二胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String braveBallDetail = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < dragBalls.length; i++){				
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBalls[i]);
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANSHI)){// 11选5任选三单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_FUSHI)){// 11选5任选三复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN3_DANTUO)){// 11选5任选三胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 3 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANSHI)){// 11选5任选四单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_FUSHI)){// 11选5任选四复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 4, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN4_DANTUO)){// 11选5任选四胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 4 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANSHI)){// 11选5任选五单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_FUSHI)){// 11选5任选五复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 5, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN5_DANTUO)){// 11选5任选五胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 5 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANSHI)){// 11选5任选六单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_FUSHI)){// 11选5任选六复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 6, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN6_DANTUO)){// 11选5任选六胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 6 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANSHI)){// 11选5任选七单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_FUSHI)){// 11选5任选七复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 7, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN7_DANTUO)){// 11选5任选七胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 7 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();		
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANSHI)){// 11选5任选八单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_FUSHI)){// 11选5任选八复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 8, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_XUAN8_DANTUO)){// 11选5任选八胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 8 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();			
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_DANSHI)){// 11选5前一直选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANYI_ZHIXUAN_FUSHI)){// 11选5前一直选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, len = balls.length; i < len; i++){
				details.add(balls[i]);
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_DANSHI)){// 11选5前二直选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZHIXUAN_FUSHI)){// 11选5前二直选复式			
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, wanLen = wanBalls.length; i < wanLen; i++){
				for(int j = 0, qianLen = qianBalls.length; j < qianLen; j++){
					if(!wanBalls[i].equals(qianBalls[j])){
						details.add(wanBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + qianBalls[j]);						
					}
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANSHI)){// 11选5前二组选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_FUSHI)){// 11选5前二组选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 2, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANER_ZUXUAN_DANTUO)){// 11选5前二组选胆拖
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String braveBallDetail = balls[0];
			String[] dragBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0; i < dragBalls.length; i++){				
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO + dragBalls[i]);
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_DANSHI)){// 11选5前三直选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZHIXUAN_FUSHI)){// 11选5前三直选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO);
			String[] wanBalls = balls[0].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] qianBalls = balls[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			String[] baiBalls = balls[2].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			for(int i = 0, wanLen = wanBalls.length; i < wanLen; i++){
				for(int j = 0, qianLen = qianBalls.length; j < qianLen; j++){
					for(int k = 0, baiLen = baiBalls.length; k < baiLen; k++){
						if(!wanBalls[i].equals(qianBalls[j]) && !wanBalls[i].equals(baiBalls[k]) && !qianBalls[j].equals(baiBalls[k])){
							details.add(wanBalls[i] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + qianBalls[j] + TicketWinningConstantsUtil.SEPARATOR_MAOHAO + baiBalls[k]);						
						}
					}
				}
			}
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANSHI)){// 11选5前三组选单式
			details.add(ballContent);
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_FUSHI)){// 11选5前三组选复式
			String[] balls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);			
			List<String> combBalls = CombinationUtil.combine(balls, 3, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			details.addAll(combBalls);
			combBalls.clear();
		}
		else if(playType.equals(LotteryPlayTypeConstants.PLAYTYPE_11X5_QIANSAN_ZUXUAN_DANTUO)){// 11选5前三组选胆拖
			String braveBallDetail = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[0];
			String[] dragBalls = ballContent.split(TicketWinningConstantsUtil.SEPARATOR_MAOHAO)[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			
			int braveBallCount = braveBallDetail.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length;
			
			List<String> combBalls = CombinationUtil.combine(dragBalls, 3 - braveBallCount, TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
			for(int i = 0; i < combBalls.size(); i++){
				details.add(braveBallDetail + TicketWinningConstantsUtil.SEPARATOR_DOUHAO +combBalls.get(i));
			}
			combBalls.clear();	
		}
		return details;
	}
	
	/**
	 * 判断奖级
	 * @param prizeLevel
	 * @param prize
	 */
	private void checkPrizeLevel(int prizeLevel, Prize prize, Map<Integer, Long> prizeAmountLevelMap){
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
