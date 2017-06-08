package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.lotteryissue.IssueSport;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetivePlay;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.LotteryType;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.SportIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader101;
import com.cqfc.xmlparser.TransactionMsgLoader701;
import com.cqfc.xmlparser.TransactionMsgLoader762;
import com.cqfc.xmlparser.transactionmsg101.Msg;
import com.cqfc.xmlparser.transactionmsg101.Querytype;
import com.cqfc.xmlparser.transactionmsg701.Body;
import com.cqfc.xmlparser.transactionmsg701.Headtype;
import com.cqfc.xmlparser.transactionmsg762.Pv;
import com.jami.util.Log;

public class TransactionMsgProcessor101 {

	/**
	 * 该接口是彩票期次信息的交易接口，该接口包含了彩种期次、交易的开始时间和 结束时间，以及平台系统接收订单的最后时间和期次状态，当前期次状态。
	 * 如果该彩种已经获得开奖公告，返回开奖号码。
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理101发来的消息");

		TransResponse response = new TransResponse();
		String xml101 = message.getTransMsg();
		Msg msg101 = TransactionMsgLoader101.xmlToMsg(xml101);

		com.cqfc.xmlparser.transactionmsg101.Headtype head101 = msg101.getHead();
		com.cqfc.xmlparser.transactionmsg101.Body body101 = msg101.getBody();
		Querytype queryType101 = body101.getQueryissue();

		String lotteryId = queryType101.getGameid();
		String issueNo = queryType101.getIssueno();
		// ==================================以上是101相关数据处理，下面是701数据处理========================
	
		//数字彩 
		if(OrderUtil.getLotteryCategory(lotteryId) == OrderStatus.LotteryType.NUMBER_GAME.getType() && OrderStatus.LotteryType.JJLZC_GAME.getType()!=OrderUtil.getJcCategoryDetail(lotteryId)){
			return get701Response(lotteryId, issueNo, head101);
		}else
		//竞彩
		if(OrderUtil.getLotteryCategory(lotteryId) == OrderStatus.LotteryType.SPORTS_GAME.getType()||IssueConstant.MATCHPLAY_BEIDAN_ALL.equals(lotteryId)
				||OrderStatus.LotteryType.JJLZC_GAME.getType()==OrderUtil.getJcCategoryDetail(lotteryId)){
			return get762Response(lotteryId, issueNo, head101);
		}
//		if(OrderUtil.getJcCategoryDetail(lotteryId) == OrderStatus.LotteryType.JJLZC_GAME.getType()){
//			return get761Response(lotteryId, issueNo, head101);
//		}
		else{
			response.setResponseTransCode(ConstantsUtil.STATUS_CODE_ISSUE_ERROR);
			response.setData("彩种或者期次不存在");
		}
		return response;
	}
	
	
	public static TransResponse get701Response(String lotteryId,String issueNo,com.cqfc.xmlparser.transactionmsg101.Headtype head101){
		TransResponse response = new TransResponse();

		Headtype head701 = new Headtype();
		Body body701 = new Body();
		com.cqfc.xmlparser.transactionmsg701.Msg msg701 = new com.cqfc.xmlparser.transactionmsg701.Msg();
		com.cqfc.xmlparser.transactionmsg701.Querytype quertType701 = new com.cqfc.xmlparser.transactionmsg701.Querytype();
		LotteryIssue lotteryResult = null;
		ReturnMessage retMsg = null;

		Log.fucaibiz.info("开始调用lotteryIssue模块findLotteryIssue方法,partnerId=" + head101.getPartnerid() + "lotteryId="
				+ lotteryId + ",issueNo=" + issueNo);
		
		retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId, issueNo);

		String statusCode = retMsg.getStatusCode();
		Log.fucaibiz.info("返回状态码为:" + statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode) && retMsg.getObj() != null) {

			lotteryResult = (LotteryIssue) retMsg.getObj();
			Log.fucaibiz.info("查询的彩种期号信息" + lotteryResult);
		} else {
			
			if(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(statusCode)){
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO);
				response.setData("不存在的期次");
				
			}else{
				response.setStatusCode(statusCode);
				response.setData(retMsg.getMsg());
				Log.fucaibiz.error("返回状态码为:" + statusCode + ",返回消息:" + retMsg.getMsg());
			}
			
			return response;
		}

		quertType701.setIssue(lotteryResult.getIssueNo());
		quertType701.setGameid(lotteryResult.getLotteryId());
		quertType701.setPrizeball(lotteryResult.getDrawResult());
		try {
			quertType701.setStarttime(DateUtil.formatStringFour(lotteryResult.getOfficialBeginTime()));
			quertType701.setEndtime(DateUtil.formatStringFour(lotteryResult.getOfficialEndTime()));
			quertType701.setPalmtime(DateUtil.formatStringFour(lotteryResult.getCompoundEndTime()));
			quertType701.setPrizetime(DateUtil.formatStringFour(lotteryResult.getDrawTime()));
			quertType701.setUnionendtime(DateUtil.formatStringFour(lotteryResult.getCompoundTogetherEndTime()));
			// 0为预销售 1
			// 1为销售中 2
			// 2为已结期 3 4 5 6 7
			// 3为已获得开奖公告 8 11 12 13 14 15 16
			// 4 为已经兑奖完毕 17

		} catch (ParseException e) {
			quertType701.setStarttime("");
			quertType701.setEndtime("");
			quertType701.setPalmtime("");
			quertType701.setPrizetime("");
			quertType701.setUnionendtime("");
			Log.fucaibiz.error("时间格式不正确", e);
		}
		String status = DataUtil.transferState(lotteryResult.getState());
		if (null != lotteryResult.getDrawResult() && !"".equals(lotteryResult.getDrawResult())
				&& lotteryResult.getState() != IssueConstant.ISSUE_STATUS_SENDPRIZE_ONLINE) {
			status = "3";
		}
		quertType701.setStatus(status);
		body701.setIssueinfo(quertType701);

		head701.setPartnerid(head101.getPartnerid());
		head701.setTime(DateUtil.getCurrentDateTime());
		head701.setTranscode("701");
		head701.setVersion(head101.getVersion());

		msg701.setBody(body701);
		msg701.setHead(head701);

		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setData(TransactionMsgLoader701.msgToXml(msg701));
		response.setResponseTransCode("701");
		Log.fucaibiz.info("701消息体返回成功");

		return response;
	}
	
	
	public static TransResponse get762Response(String lotteryId,String issueNo,com.cqfc.xmlparser.transactionmsg101.Headtype head101){
		TransResponse response = new TransResponse();

		com.cqfc.xmlparser.transactionmsg762.Headtype head762 =  new com.cqfc.xmlparser.transactionmsg762.Headtype();
		com.cqfc.xmlparser.transactionmsg762.Body body762 = new com.cqfc.xmlparser.transactionmsg762.Body();
		com.cqfc.xmlparser.transactionmsg762.Msg msg762 = new com.cqfc.xmlparser.transactionmsg762.Msg();
		com.cqfc.xmlparser.transactionmsg762.Matchs matchs =  new com.cqfc.xmlparser.transactionmsg762.Matchs();
		List<com.cqfc.xmlparser.transactionmsg762.Match> match = matchs.getMatch();
		com.cqfc.xmlparser.transactionmsg762.Match match762 = null;
		com.cqfc.xmlparser.transactionmsg762.Pv pv  = null;
//		LotteryIssue lotteryResult = null;
		ReturnMessage retMsg = null;
		List<MatchCompetivePlay> competivePlays = null;
		List<com.cqfc.xmlparser.transactionmsg762.Pv> pvs = null;
	
		//竞彩（老足彩表结构不同独立出来）
		if(OrderUtil.getLotteryCategory(lotteryId)==OrderStatus.LotteryType.SPORTS_GAME.getType() ||IssueConstant.MATCHPLAY_BEIDAN_ALL.equals(lotteryId)){
			//北单需要查询期号表
			if(OrderUtil.getJcCategoryDetail(lotteryId)==OrderStatus.LotteryType.JJBD_GAME.getType()||IssueConstant.MATCHPLAY_BEIDAN_ALL.equals(lotteryId)
					||LotteryType.BDSFGG.getText().equals(lotteryId)){
				
				retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findIssueSport", IssueConstant.MATCHPLAY_BEIDAN_ALL,issueNo);
				
				if(retMsg.getObj()!=null){
					IssueSport issueSport =  (IssueSport) retMsg.getObj();
					//matchs.setLotteryid(lotteryId);
					matchs.setIssue(issueSport.getWareIssue());
					try {
						matchs.setIssueendtime(DateUtil.formatStringFour(issueSport.getEndSellTime()));
						matchs.setIssuestarttime(DateUtil.formatStringFour(issueSport.getBeginSellTime()));
					} catch (Exception e) {
						matchs.setIssueendtime("");
						matchs.setIssuestarttime("");
						Log.fucaibiz.error("时间格式不正确", e);
					}
				}else{
					
					response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO);
					response.setData("不存在的期次");
					return response;
				}
			}else{
				matchs.setIssue(IssueConstant.SPORT_ISSUE_CONSTANT);
			}
			
			//JZJL  BD 调用统一接口
			retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "getMatchCompetiveListByMatchType", lotteryId);
	
			String statusCode = retMsg.getStatusCode();
			List<MatchCompetive> matchCompetives = new ArrayList<MatchCompetive>();
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode) && retMsg.getObj() != null) {
	
				matchCompetives = (List<MatchCompetive>) retMsg.getObj();
			} else {
				
				if(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(statusCode)){
					response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO);
					response.setData("未查询到数据");
					
				}else{
					
					response.setStatusCode(statusCode);
					response.setData(retMsg.getMsg());
					Log.fucaibiz.error("返回状态码为:" + statusCode + ",返回消息:" + retMsg.getMsg());
				}
				
				return response;
			}
			
			for (MatchCompetive matchCompetive :matchCompetives) {
				
				competivePlays = matchCompetive.getMatchCompetivePlayList();
				if(competivePlays == null || competivePlays.size()<1){
					 continue;
				}
				
				match762 = new com.cqfc.xmlparser.transactionmsg762.Match();
				
				if(matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue() || 
						matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue()) match762.setMatchid(matchCompetive.getTransferId());
				else if (matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.BEIDAN.getValue() ||
						matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue()) match762.setMatchid(matchCompetive.getMatchNo());
				else match762.setMatchid("");
				match762.setGameno(matchCompetive.getMatchNo());
				match762.setGamename(matchCompetive.getMatchName().trim());
				match762.setHomename(matchCompetive.getHomeTeam().trim());
				match762.setGuestname(matchCompetive.getGuestTeam().trim());
				try {
					match762.setGametime(DateUtil.formatStringFour(matchCompetive.getMatchBeginTime()));
					match762.setEndtime(DateUtil.formatStringFour(matchCompetive.getBettingDeadline()));
				} catch (ParseException e) {
					match762.setGametime("");
					match762.setEndtime("");
					Log.fucaibiz.error("时间格式不正确", e);
				}
				pvs = match762.getPv();
	
				
				for (MatchCompetivePlay matchCompetivePlay : matchCompetive.getMatchCompetivePlayList()) {
					
					pv = new Pv() ;
					pv.setLotteryid(matchCompetivePlay.getLotteryId());
					pv.setGgrq(matchCompetivePlay.getGgRq());
					pv.setDgrq(matchCompetivePlay.getDgRq());
					pv.setGgpv(matchCompetivePlay.getGgPv());
					pv.setDgpv(matchCompetivePlay.getDgPv());
					
					pv.setGgsalestatus(String.valueOf(matchCompetivePlay.getGgSaleStatus()));
					pv.setDgsalestatus(String.valueOf(matchCompetivePlay.getDgSaleStatus()));
					pv.setDggdsalestatus(String.valueOf(matchCompetivePlay.getDgGdSaleStatus()));
					pvs.add(pv);
				}
				match.add(match762);
			}
			
		}else{
			
			//老足彩查询期号表
			retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId,issueNo);
			
			if(retMsg.getObj()!=null){
				LotteryIssue lotteryIssue =  (LotteryIssue) retMsg.getObj();
				matchs.setLotteryid(lotteryId);
				matchs.setIssue(lotteryIssue.getIssueNo());
				try {
					matchs.setDrawtime(DateUtil.formatStringFour(lotteryIssue.getDrawTime()));
					matchs.setIssueendtime(DateUtil.formatStringFour(lotteryIssue.getCompoundEndTime()));
					matchs.setIssuestarttime(DateUtil.formatStringFour(lotteryIssue.getBeginTime()));
				} catch (Exception e) {
					matchs.setDrawtime("");
					matchs.setIssueendtime("");
					matchs.setIssuestarttime("");
					Log.fucaibiz.error("时间格式不正确", e);
				}
			}else{
				
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO);
				response.setData("不存在的期次");
				return response;
			}
			//老足彩查询赛事
			retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "getMatchFootballList", lotteryId,matchs.getIssue());
	
			String statusCode = retMsg.getStatusCode();
			List<MatchFootball> matchFootballs = new ArrayList<MatchFootball>();
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode) && retMsg.getObj() != null) {
	
				matchFootballs = (List<MatchFootball>) retMsg.getObj();
			} else {
				
				if(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(statusCode)){
					response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO);
					response.setData("未查询到数据");
					
				}else{
					response.setStatusCode(statusCode);
					response.setData(retMsg.getMsg());
					Log.fucaibiz.error("返回状态码为:" + statusCode + ",返回消息:" + retMsg.getMsg());
				}
				return response;
			}
			
			for (MatchFootball matchFootball : matchFootballs) {
				
				match762 = new com.cqfc.xmlparser.transactionmsg762.Match();
				match762.setMatchid(matchFootball.getMatchNo());
				match762.setGameno(matchFootball.getMatchNo());
				match762.setGamename(matchFootball.getMatchName().trim());
				match762.setHomename(matchFootball.getHomeTeam().trim());
				match762.setGuestname(matchFootball.getGuestTeam().trim());
				match762.setSp(matchFootball.getSp());
				try {
					match762.setGametime(DateUtil.formatStringFour(matchFootball.getMatchBeginTime()));
					
				} catch (ParseException e) {
					match762.setGametime("");
					match762.setEndtime("");
					Log.fucaibiz.error("时间格式不正确", e);
				}
				match.add(match762);
			}
		}
		
		body762.setMatchs(matchs);

		head762.setPartnerid(head101.getPartnerid());
		head762.setTime(DateUtil.getCurrentDateTime());
		head762.setTranscode("762");
		head762.setVersion(head101.getVersion());

		msg762.setBody(body762);
		msg762.setHead(head762);

		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setData(TransactionMsgLoader762.msgToXml(msg762));
		response.setResponseTransCode("762");
		Log.fucaibiz.info("762消息体返回成功");

		return response;
	}

}
