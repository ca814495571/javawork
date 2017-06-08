package com.cqfc.ticketwinning.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.businesscontroller.SportDetail;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.protocol.ticketwinning.TicketDetailReturnMessage;
import com.cqfc.protocol.ticketwinning.WinningAmountReturnMessage;
import com.cqfc.ticketwinning.factory.TicketWinningServiceFactory;
import com.cqfc.ticketwinning.model.Prize;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.ticketwinning.util.BDContentSetUtil;
import com.cqfc.ticketwinning.util.JCContentSetUtil;
import com.cqfc.ticketwinning.util.MXNUtil;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.ticketwinning.util.TicketWinningUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.LotteryType;
import com.jami.util.Log;



@Service
public class TicketWinningServiceImpl{

	/**
	 * 校验传入投注内容是否合法
	 */
	private boolean validateOrderContent(String lotteryId, String playType,
			String orderContent) throws TException {
		boolean flag = false;
		
		try {
			if(orderContent == null || "".equals(orderContent)){
				return flag;
			}
			
			ITicketWinningService ticketService = TicketWinningServiceFactory.getTicketServiceInstance(lotteryId);
			flag = ticketService.validateContent(orderContent, playType);			
		} catch(Exception e){
			Log.run.error("validateOrderContent exception: (lotteryId: %s, playType: %s,  orderContent: %s)", lotteryId, playType, orderContent);
		} 
		
		return flag;
	}

	/**
	 * 	计算投注订单中奖总金额
	 */
	public WinningAmountReturnMessage calTicketWinningAmount(String lotteryId,
			String playType, String orderContent, String issueNo, String winningBallContent, Map<Integer, Long> prizeLevelMap)
			throws TException {
		WinningAmountReturnMessage msg = new WinningAmountReturnMessage();
		ITicketWinningService ticketService = null;
		
		try{
			if(validateOrderContent(lotteryId, playType, orderContent)){
				ticketService = TicketWinningServiceFactory.getTicketServiceInstance(lotteryId);
				Prize prize = ticketService.calTicketWinningAmount(orderContent, playType, winningBallContent, prizeLevelMap);
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				msg.setMsg("success");
				msg.setIsPrize(prize.isPrize());
				msg.setAmount(prize.getPrizeAmount());
			}
			else{
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
				msg.setMsg("投注号码格式错误");
				Log.run.error("calTicketWinningAmount: 投注号码格式错误, (lotteryId: %s, playType: %s, issueNo: %s, orderContent: %s)", lotteryId, playType, issueNo, orderContent);
			}
		}
		catch(Exception e){
			msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
			msg.setMsg("投注号码格式错误");
			Log.run.error("calTicketWinningAmount: 投注号码格式错误, (lotteryId: %s, playType: %s, issueNo: %s, orderContent: %s)", lotteryId, playType, issueNo, orderContent);
			return msg;
		} 
					
		return msg;		
	}

	/**
	 * 	计算投注订单总注数
	 */
	public BallCountReturnMessage calBallCount(String lotteryId,
			String playType, String orderContent)
			throws TException {
		Log.run.info("calBallCount(lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
		BallCountReturnMessage msg = new BallCountReturnMessage();
		ITicketWinningService ticketService = null;
		int sum = 0;
		
		try{
			if(validateOrderContent(lotteryId, playType, orderContent)){
				ticketService = TicketWinningServiceFactory.getTicketServiceInstance(lotteryId);
				sum = ticketService.calBallCounts(orderContent, playType);
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				msg.setMsg("success");
				msg.setCount(sum);
			}
			else{
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
				msg.setMsg("投注号码格式错误");
				Log.run.error("calBallCount: 投注号码格式错误, (lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
			}
		}
		catch(Exception e){
			msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
			msg.setMsg("投注号码格式错误");
			Log.run.error("calBallCount: 投注号码格式错误, (lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
			return msg;
		} 
	
		return msg;
	}

	/**
	 * 计算投注订单每注详细内容
	 */
	public TicketDetailReturnMessage calTicketDetail(String lotteryId,
			String playType, String orderContent)
			throws TException {
		Log.run.info("calTicketDetail(lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
		TicketDetailReturnMessage msg = new TicketDetailReturnMessage();
		ITicketWinningService ticketService = null;
		List<String> details = new ArrayList<String>();
		
		try{
			if(validateOrderContent(lotteryId, playType, orderContent)){
				ticketService = TicketWinningServiceFactory.getTicketServiceInstance(lotteryId);
				details = ticketService.listTicketDetails(orderContent, playType);
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				msg.setMsg("success");
				msg.setDetails(details);
			}
			else{
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
				msg.setMsg("投注号码格式错误");
				Log.run.error("calTicketDetail: 投注号码格式错误, (lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
			}
		}
		catch(Exception e){
			msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
			msg.setMsg("投注号码格式错误");
			Log.run.error("calTicketDetail: 投注号码格式错误, (lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
			return msg;
		} 
		return msg;
	}
	
	/**
	 * 校验竞彩传入投注内容是否合法
	 */
	public boolean validateJCOrderContent(String lotteryId, String orderContent, String playType) {
		try {
			if(orderContent == null || "".equals(orderContent)){
				return false;
			}			
			
			String[] content = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_PIE);
			String[] playTypeSplit = playType.split(TicketWinningConstantsUtil.SEPARATOR_CHENG);
			int contentLen = content.length;
			
			if(contentLen != Integer.valueOf(playTypeSplit[0])){
				return false;
			}
			
			if(!MXNUtil.isPlayTypeExist(playType)){
				return false;
			}
			
			if(contentLen == 1 && (lotteryId.equals(LotteryType.JCZQHHGG.getText()) || lotteryId.equals(LotteryType.JCLQHHGG.getText()))){
				return false;
			}
			
			Set<String> jcContentSet = JCContentSetUtil.getSetContentByLotteryId(lotteryId);
			String[] match = null;
			String matchContent = null;
			String[] matchContentSplit = null;
			for(int i = 0; i < contentLen; i++){
				match = content[i].split(TicketWinningConstantsUtil.SEPARATOR_BOLANG);
				if(match.length != TicketWinningConstantsUtil.JC_MATCH_SPLIT_LEN){
					return false;
				}
				
				if(!(match[1].startsWith(TicketWinningConstantsUtil.JC_MATCH_CONTENT_LEFT) && 
						match[1].endsWith(TicketWinningConstantsUtil.JC_MATCH_CONTENT_RIGHT))){
					return false;
				}
				
				matchContent = match[1].substring(1, match[1].length() - 1);
				matchContentSplit = matchContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if(matchContent.equals("") || matchContent.length() == 0){
					return false;
				}
				if(!TicketWinningUtil.isBallContentRepeat(matchContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
					return false;
				}
				for(int j = 0, len = matchContentSplit.length; j < len; j++){
					if(!jcContentSet.contains(matchContentSplit[j])){
						return false;
					}
				}
				
			}				
		} catch(Exception e){
			Log.run.error("validateJCOrderContent exception: (lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
		} 
		
		return true;
	}
	
	
	/**
	 * 校验北单传入投注内容是否合法
	 */
	public boolean validateBDOrderContent(String lotteryId, String orderContent, String playType) {
		try {
			if(orderContent == null || "".equals(orderContent)){
				return false;
			}	
			String[] content = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_PIE);
			String[] playTypeSplit = playType.split(TicketWinningConstantsUtil.SEPARATOR_CHENG);
			int contentLen = content.length;
			
			if(contentLen != Integer.valueOf(playTypeSplit[0])){
				return false;
			}
			
			if(!TicketWinningUtil.isBDPlayTypeExist(lotteryId, playType)){
				return false;
			}
			
			Set<String> bdContentSet = BDContentSetUtil.getSetContentByLotteryId(lotteryId);
			String[] match = null;
			String matchContent = null;
			String[] matchContentSplit = null;
			for(int i = 0; i < contentLen; i++){
				match = content[i].split(TicketWinningConstantsUtil.SEPARATOR_BOLANG);
				if(match.length != TicketWinningConstantsUtil.JC_MATCH_SPLIT_LEN){
					return false;
				}
				
				if(!(match[1].startsWith(TicketWinningConstantsUtil.JC_MATCH_CONTENT_LEFT) && 
						match[1].endsWith(TicketWinningConstantsUtil.JC_MATCH_CONTENT_RIGHT))){
					return false;
				}
				
				matchContent = match[1].substring(1, match[1].length() - 1);
				matchContentSplit = matchContent.split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO);
				
				if(matchContent.equals("") || matchContent.length() == 0){
					return false;
				}
				if(!TicketWinningUtil.isBallContentRepeat(matchContent, TicketWinningConstantsUtil.SEPARATOR_DOUHAO)){
					return false;
				}
				for(int j = 0, len = matchContentSplit.length; j < len; j++){
					if(!bdContentSet.contains(matchContentSplit[j])){
						return false;
					}
				}
				
			}				
		} catch(Exception e){
			Log.run.error("validateBDOrderContent exception: (lotteryId: %s, playType: %s, orderContent: %s)", lotteryId, playType, orderContent);
		} 
		
		return true;
	}
	
	/**
	 * 	计算竞彩投注订单总注数
	 */
	public BallCountReturnMessage calJCBallCount(String lotteryId, String orderContent, String playType)
			throws TException {
		Log.run.info("calJCBallCount(lotteryId: %s, orderContent: %s, playType: %s)", lotteryId, orderContent, playType);
		BallCountReturnMessage msg = new BallCountReturnMessage();
		int count = 0;
		
		try{
			if(validateJCOrderContent(lotteryId, orderContent, playType)){				
				String[] matchContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_PIE);
				
				List<Integer> ballCounts = new ArrayList<Integer>();
				for(int i = 0, len = matchContents.length; i < len; i++){
					String[] match = matchContents[i].split(TicketWinningConstantsUtil.SEPARATOR_BOLANG);
					ballCounts.add(match[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length);
				}
				
				int[] ballInts = new int[ballCounts.size()];
				for(int j = 0; j < ballCounts.size(); j++){
					ballInts[j] = ballCounts.get(j);
				}
				
				String[] playTypeSplit = playType.split(TicketWinningConstantsUtil.SEPARATOR_CHENG);
				int m = Integer.valueOf(playTypeSplit[0]);
				int n = Integer.valueOf(playTypeSplit[1]);
				
				if(n == 1){
					count = TicketWinningUtil.getArrMul(ballInts, m);
				}
				else{
					int[] balls = MXNUtil.mxn(m, n);
					for(int k = 0, len = balls.length; k < len; k++){
						count += TicketWinningUtil.getArrMul(ballInts, balls[k]);
					}
				}
				
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				msg.setMsg("success");
				msg.setCount(count);
			}
			else{
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
				msg.setMsg("投注号码格式错误");
				Log.run.error("calJCBallCount: 投注号码格式错误, (lotteryId: %s, orderContent: %s, playType: %s)", lotteryId, orderContent, playType);
			}
		}
		catch(Exception e){
			msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
			msg.setMsg("投注号码格式错误");
			Log.run.error("calJCBallCount: 投注号码格式错误, (lotteryId: %s, orderContent: %s, playType: %s)", lotteryId, orderContent, playType);
			return msg;
		} 
	
		return msg;
	}
	
	/**
	 * 	计算北单投注订单总注数
	 */
	public BallCountReturnMessage calBDBallCount(String lotteryId, String orderContent, String playType)
			throws TException {
		Log.run.info("calBDBallCount(lotteryId: %s, orderContent: %s, playType: %s)", lotteryId, orderContent, playType);
		BallCountReturnMessage msg = new BallCountReturnMessage();
		int count = 0;
		
		try{
			if(validateBDOrderContent(lotteryId, orderContent, playType)){				
				String[] matchContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_PIE);
				
				List<Integer> ballCounts = new ArrayList<Integer>();
				for(int i = 0, len = matchContents.length; i < len; i++){
					String[] match = matchContents[i].split(TicketWinningConstantsUtil.SEPARATOR_BOLANG);
					ballCounts.add(match[1].split(TicketWinningConstantsUtil.SEPARATOR_DOUHAO).length);
				}
				
				int[] ballInts = new int[ballCounts.size()];
				for(int j = 0; j < ballCounts.size(); j++){
					ballInts[j] = ballCounts.get(j);
				}
				
				String[] playTypeSplit = playType.split(TicketWinningConstantsUtil.SEPARATOR_CHENG);
				int m = Integer.valueOf(playTypeSplit[0]);
				int n = Integer.valueOf(playTypeSplit[1]);
				
				if(n == 1){
					count = TicketWinningUtil.getArrMul(ballInts, m);
				}
				else{
					int[] balls = TicketWinningUtil.getBallsMXNByLotteryId(lotteryId, m, n);
					for(int k = 0, len = balls.length; k < len; k++){
						count += TicketWinningUtil.getArrMul(ballInts, balls[k]);
					}
				}
				
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				msg.setMsg("success");
				msg.setCount(count);
			}
			else{
				msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
				msg.setMsg("投注号码格式错误");
				Log.run.error("calBDBallCount: 投注号码格式错误, (lotteryId: %s, orderContent: %s, playType: %s)", lotteryId, orderContent, playType);
			}
		}
		catch(Exception e){
			msg.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
			msg.setMsg("投注号码格式错误");
			Log.run.error("calBDBallCount: 投注号码格式错误, (lotteryId: %s, orderContent: %s, playType: %s)", lotteryId, orderContent, playType);
			return msg;
		} 
	
		return msg;
	}
	
	/**
	 * 拆解竞彩北单投注赛事
	 */
	public List<SportDetail> dismantleOrderContent(String orderContent, String lotteryId){
		List<SportDetail> list = new ArrayList<SportDetail>();
		Set<String> transferIdSet = new HashSet<String>();
		
		try {						
			String[] matchContents = orderContent.split(TicketWinningConstantsUtil.SEPARATOR_PIE);
			int matchLen = matchContents.length;
			SportDetail order = null;
			
			if(lotteryId.equals(LotteryType.JCZQJQS.getText()) && matchLen > 6){
				return null;
			}
			else if(lotteryId.equals(LotteryType.JCZQBF.getText()) && matchLen > 4){
				return null;
			}
			else if(lotteryId.equals(LotteryType.JCZQBQC.getText()) && matchLen > 4){
				return null;
			}
			else if(lotteryId.equals(LotteryType.JCLQSFC.getText()) && matchLen > 4){
				return null;
			}
			for(int i = 0; i < matchLen; i++){
				String[] matchSplit = matchContents[i].split(TicketWinningConstantsUtil.SEPARATOR_BOLANG);
				order = new SportDetail();
				order.setTransferId(matchSplit[0]);
				order.setOrderContent(matchSplit[1]);
				
				if(lotteryId.equals(LotteryType.JCZQHHGG.getText())){
					if(matchSplit[1].contains(TicketWinningConstantsUtil.JZ_BF) || matchSplit[1].contains(TicketWinningConstantsUtil.JZ_BQCSPF)){
						if(matchLen > 4){
							return null;
						}
					}
					else if(matchSplit[1].contains(TicketWinningConstantsUtil.JZ_BF)){
						if(matchLen > 6){
							return null;
						}
					}					
				}
				else if(lotteryId.equals(LotteryType.JCLQHHGG.getText())){
					if(matchSplit[1].contains(TicketWinningConstantsUtil.JL_SFC)){
						if(matchLen > 4){
							return null;
						}
					}
				}
				transferIdSet.add(matchSplit[0]);
				list.add(order);
			}
			
			if(matchContents.length != transferIdSet.size()){
				return null;
			}
		} catch(Exception e){
			Log.run.error("dismantleOrderContent exception: (orderContent: %s)", orderContent);
			return null;
		} 
		
		return list;
	}

}
