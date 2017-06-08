package com.cqfc.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.access.model.LotteryPrizeResultModel;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.util.ConstantsUtil;

@Controller
@RequestMapping("/lottery")
public class JCLotteryPrizeController {
	
	@RequestMapping(value="/jcSave")
	public String save(){				
		return "jcPrize";
	}
	
	@RequestMapping(value="/calJCPrize")
	public String calJCPrize(){				
		return "calJCPrize";
	}
	
	/**
	 * 算奖
	 */
	@RequestMapping(value = "/jcPrize")
	@ResponseBody
	public LotteryPrizeResultModel prize(String lotteryId, String playType, String ballContent) {
		ReturnMessage retCountMsg = TransactionProcessor.dynamicInvoke("ticketWinning", "calJCBallCount", lotteryId, playType, ballContent);
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
		
		return model;
	}
	
	
	/**
	 * 算奖
	 */
	@RequestMapping(value = "/jcCalPrize")
	@ResponseBody
	public String jcCalPrize(String transferId) {
		TransactionProcessor.dynamicInvoke("ticketWinning", "calJCPrize", transferId, "false");
		
		
		return "正在算奖,请稍后...";
	}
}
