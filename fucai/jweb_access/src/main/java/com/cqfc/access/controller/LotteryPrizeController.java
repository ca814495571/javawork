package com.cqfc.access.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.access.model.LotteryPrizeResultModel;
import com.cqfc.access.util.LotteryDrawResultUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.protocol.ticketwinning.TicketDetailReturnMessage;
import com.cqfc.protocol.ticketwinning.WinningAmountReturnMessage;
import com.cqfc.util.ConstantsUtil;

@Controller
@RequestMapping("/lottery")
public class LotteryPrizeController {
	
	@RequestMapping(value="/save")
	public String save(){				
		return "prize";
	}
	
	/**
	 * 算奖
	 */
	@RequestMapping(value = "/prize")
	@ResponseBody
	public LotteryPrizeResultModel prize(String lotteryId, String issueNo, String ballContent, String playType) {
		ReturnMessage retCountMsg = TransactionProcessor.dynamicInvoke("ticketWinning", "calBallCount", lotteryId, playType, ballContent);
		int ballCount = retCountMsg.getObj() != null ? ((BallCountReturnMessage)retCountMsg.getObj()).getCount() : 0;
		LotteryPrizeResultModel model = new LotteryPrizeResultModel();
		model.setBallCount(ballCount);
		if(retCountMsg.getObj() == null){
			model.setDetail(retCountMsg.getMsg());
			return model;
		}
		if(((BallCountReturnMessage)retCountMsg.getObj()).getStatusCode().equals(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR)){
			model.setDetail(((BallCountReturnMessage)retCountMsg.getObj()).getMsg());
			return model;
		}
		ReturnMessage retDetailMsg = TransactionProcessor.dynamicInvoke("ticketWinning", "calTicketDetail", lotteryId, playType, ballContent);
		ReturnMessage retAmountMsg = TransactionProcessor.dynamicInvoke("ticketWinning", "calTicketWinningAmount", lotteryId, playType, ballContent, issueNo);
		if(retAmountMsg.getObj() == null || retAmountMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR)){
			model.setDetail(retAmountMsg.getMsg());
			return model;
		}
		List<String>  detailList = ((TicketDetailReturnMessage) retDetailMsg.getObj()).getDetails();
		String detail = detailList != null ? detailList.toString() : ((TicketDetailReturnMessage)retDetailMsg.getObj()).getMsg();
		model.setDetail(detail.replaceAll(",\\s", " @ "));
		
		if(detailList != null && detailList.size() == ballCount){
			model.setCheckDetailCount("Yes");
		}
		else{
			model.setCheckDetailCount("No");
		}
		
		Long amount = retAmountMsg.getObj() != null ? ((WinningAmountReturnMessage) retAmountMsg.getObj()).getAmount() : 0;
		model.setAmount(amount / 100);
		
		//获取开奖公告
		LotteryDrawResult lotteryDrawResult = LotteryDrawResultUtil.getLotteryDrawResult(lotteryId, issueNo);
		//开奖结果
		String winningBallContent = lotteryDrawResult.getDrawResult();
		winningBallContent = LotteryDrawResultUtil.convertWinningBallContentFormat(lotteryId, winningBallContent);
		List<LotteryDrawLevel> prizeLevelList = lotteryDrawResult.getLotteryDrawLevelList();
		//开奖奖级
		Map<String, Long> prizeLevelMap = new HashMap<String, Long>();
		for(LotteryDrawLevel level : prizeLevelList){
			prizeLevelMap.put(level.getLevelName(), level.getMoney() /100);
		}
		model.setWinningBallContent(winningBallContent);
		model.setPrize(prizeLevelMap.toString());
		
		return model;
	}
}
