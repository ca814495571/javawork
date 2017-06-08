package com.cqfc.access.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;

@Controller
@RequestMapping("/lottery")
public class RestartCalPrizeController {
	
	@RequestMapping(value="/all")
	public String save(){				
		return "all";
	}
	
	@RequestMapping(value="/part")
	public String part(){				
		return "part";
	}
	
	
	/**
	 * 全部重新算奖
	 */
	@RequestMapping(value = "/allCal")
	@ResponseBody
	public String allCal(String lotteryId, String issueNo){
		ReturnMessage retCountMsg = TransactionProcessor.dynamicInvoke("ticketWinning", "restartCalPrizeAll", lotteryId, issueNo);
		
		return "success";
	}
	
	
	/**
	 * 部分重新算奖
	 */
	@RequestMapping(value = "/partCal")
	@ResponseBody
	public String partCal(String lotteryId, String issueNo){
		ReturnMessage retCountMsg = TransactionProcessor.dynamicInvoke("ticketWinning", "restartCalPrizePart", lotteryId, issueNo);
		
		return "success";
	}
}
