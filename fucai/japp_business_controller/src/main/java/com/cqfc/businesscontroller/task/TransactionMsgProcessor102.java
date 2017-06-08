package com.cqfc.businesscontroller.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.IssueUtil;
import com.cqfc.util.LotteryType;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.SportIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader102;
import com.cqfc.xmlparser.TransactionMsgLoader702;
import com.cqfc.xmlparser.TransactionMsgLoader763;
import com.cqfc.xmlparser.transactionmsg702.Body;
import com.cqfc.xmlparser.transactionmsg702.Headtype;
import com.cqfc.xmlparser.transactionmsg702.Levelinfo;
import com.cqfc.xmlparser.transactionmsg702.Levelinfos;
import com.cqfc.xmlparser.transactionmsg702.Msg;
import com.cqfc.xmlparser.transactionmsg702.Querytype;
import com.cqfc.xmlparser.transactionmsg702.Saleinfo;
import com.cqfc.xmlparser.transactionmsg702.Saleinfos;
import com.cqfc.xmlparser.transactionmsg763.Match;
import com.cqfc.xmlparser.transactionmsg763.Matchresults;
import com.cqfc.xmlparser.transactionmsg763.Result;
import com.jami.util.Log;

public class TransactionMsgProcessor102 {


	/**
	 * 102接口提供对某彩种某期次的开奖公告信息的查询， 开奖公告主要是某彩种某期的各奖等的全国或地方中奖信息如一等奖的
	 * 中奖金额数量等。对于地方性彩种无需添加省份字段，对于全国彩种 如果要查询全国的开奖公告统计也无需给省份字段赋值。3D虽然为全国彩种，
	 * 但3D为地方结算彩种，所以无法返回全国开奖信息，只能返回指定省份的 开奖公告信息，如果3D的102请求未设置省份字段，则返回默认平台默认省份信息。
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String transMsg = message.getTransMsg();
		
		com.cqfc.xmlparser.transactionmsg102.Msg msg102 = TransactionMsgLoader102.xmlToMsg(transMsg);
		com.cqfc.xmlparser.transactionmsg102.Headtype head102 = msg102.getHead();
		com.cqfc.xmlparser.transactionmsg102.Querytype queryType102 = msg102.getBody().getQueryprizenotice();

		String partnerId = head102.getPartnerid();
		String lotteryId = queryType102.getGameid();
		String issueNo = queryType102.getIssue();
		String province = queryType102.getPrivance();
		String version = head102.getVersion();
		String matchid = queryType102.getMatchid();
		
		if(OrderUtil.getLotteryCategory(lotteryId) == OrderStatus.LotteryType.NUMBER_GAME.getType()){
			return get702Response(partnerId,  lotteryId, issueNo,province,version);
		}else
		
		if(OrderUtil.getLotteryCategory(lotteryId) == OrderStatus.LotteryType.SPORTS_GAME.getType()||IssueConstant.MATCHPLAY_BEIDAN_ALL.equals(lotteryId)){
			return get763Response(partnerId, lotteryId, issueNo,matchid,version);
		}else{
			response.setData("未查询到数据");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
		}

		return response;
	}

	public static TransResponse get702Response(String partnerId,
			String lotteryId, String issueNo, String province, String version) {

		TransResponse response = new TransResponse();
		com.cqfc.xmlparser.transactionmsg702.Msg msg702 = new Msg();
		Headtype head702 = new Headtype();
		Body body702 = new Body();
		Querytype querytype702 = new Querytype();

		Levelinfos levelinfos702 = new Levelinfos();
		LotteryDrawLevel lotteryDrawLevel = null;

		Saleinfos saleinfos702 = new Saleinfos();
		LotteryDrawResult drawResult = null;
		try {
			Log.fucaibiz
					.info("102调用lotteryIssue中findLotteryDrawResult,partnerId=%s,lotteryId=%s,issueNo=%s",
							partnerId, lotteryId, issueNo);
			ReturnMessage reMsg = TransactionProcessor
					.dynamicInvoke("lotteryIssue", "findLotteryDrawResult",
							lotteryId, issueNo);

			if (null != reMsg.getObj()
					&& ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(reMsg
							.getStatusCode())) {
				drawResult = (LotteryDrawResult) reMsg.getObj();
			} else if (ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(reMsg
					.getStatusCode())) {
				Log.fucaibiz.debug("102findLotteryDrawResult不存在开奖信息,msg=%s",
						reMsg.getMsg());
				response.setData("不存在获奖信息");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NO_WINNINGMSG);
				return response;
			} else {
				Log.fucaibiz.debug("102findLotteryDrawResult发生异常,msg=%s",
						reMsg.getMsg());
				response.setData(reMsg.getMsg());
				response.setStatusCode(reMsg.getStatusCode());
				return response;
			}
			// 期号信息
			LotteryIssue lotteryResult = drawResult.getLotteryIssue();
			String prizePool = MoneyUtil
					.toYuanStr(lotteryResult.getPrizePool());
			// 奖级信息
			List<LotteryDrawLevel> lotteryDrawLevels = drawResult
					.getLotteryDrawLevelList();

			List<Levelinfo> levelinfo702s = levelinfos702.getLevelinfo();
			for (int i = 0; i < lotteryDrawLevels.size(); i++) {
				lotteryDrawLevel = lotteryDrawLevels.get(i);
				Levelinfo levelinfo702 = new Levelinfo();

				levelinfo702.setLevel(String.valueOf(lotteryDrawLevel
						.getLevel()));
				levelinfo702.setMoney(MoneyUtil.toYuanStr(lotteryDrawLevel
						.getMoney()));
				levelinfo702.setCount(String.valueOf(lotteryDrawLevel
						.getTotalCount()));
				levelinfo702.setName(lotteryDrawLevel.getLevelName());

				levelinfo702s.add(levelinfo702);
			}
			Log.fucaibiz.info(
					"102开奖公告信息获取成功,partnerId=%s,lotteryId=%s,issueNo=%s",
					partnerId, lotteryId, issueNo);

			Saleinfo saleinfo702 = new Saleinfo();
			saleinfo702.setMoney(MoneyUtil.toYuanStr(lotteryResult
					.getSalesVolume()));
			saleinfo702.setType("1"); // 腾讯那边约定这里type设置成1
			saleinfo702.setTypename("cq");
			List<Saleinfo> saleinfo702s = saleinfos702.getSaleinfo();
			saleinfo702s.add(saleinfo702);

			querytype702.setLevelinfos(levelinfos702);
			querytype702.setSaleinfos(saleinfos702);
			querytype702.setGameid(drawResult.getLotteryId());
			querytype702.setIssue(drawResult.getIssueNo());
			querytype702.setPrizepool(prizePool);
			querytype702.setPrizeball(drawResult.getDrawResult());
			querytype702.setProvince(province);
			String status = DataUtil.transferState(lotteryResult.getState());
			if (null != lotteryResult.getDrawResult()
					&& !"".equals(lotteryResult.getDrawResult())
					&& lotteryResult.getState() != IssueConstant.ISSUE_STATUS_SENDPRIZE_ONLINE) {
				status = "3";
			}
			querytype702.setStatus(status);

			body702.setPrizeinfo(querytype702);

			head702.setPartnerid(partnerId);
			head702.setTime(DateUtil.getCurrentDateTime());
			head702.setTranscode("702");
			head702.setVersion(version);

			msg702.setHead(head702);
			msg702.setBody(body702);

			response.setData(TransactionMsgLoader702.msgToXml(msg702));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode("702");
			Log.fucaibiz.debug(
					"702消息返回成功,partnerId=%s,lotteryId=%s,issueNo=%s",
					partnerId, lotteryId, issueNo);
		} catch (Exception e) {
			Log.fucaibiz.error("102查询发生异常,partnerId=" + partnerId
					+ ",lotteryId=" + lotteryId + ",issueNo=" + issueNo, e);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			return response;
		}
		return response;
	}
	
	
	public static TransResponse get763Response(String partnerId,
			String lotteryId, String issueNo ,String matchId,String version) {

		TransResponse response = new TransResponse();
		com.cqfc.xmlparser.transactionmsg763.Msg msg763 = new com.cqfc.xmlparser.transactionmsg763.Msg();
		com.cqfc.xmlparser.transactionmsg763.Headtype head763 = new com.cqfc.xmlparser.transactionmsg763.Headtype();
		com.cqfc.xmlparser.transactionmsg763.Body body763 = new com.cqfc.xmlparser.transactionmsg763.Body();
		com.cqfc.xmlparser.transactionmsg763.Matchresults matchresults = new Matchresults();

		List<Match> matchs = matchresults.getMatch();

		Match match = null;
		Result result = null;
		
		MatchCompetive matchCompetive = null;
		List<MatchCompetiveResult> competiveResults = new ArrayList<MatchCompetiveResult>();
		ReturnMessage reMsg = null;
		if(OrderUtil.getJcCategoryDetail(lotteryId) == OrderStatus.LotteryType.JJZC_GAME.getType() ||
				OrderUtil.getJcCategoryDetail(lotteryId) == OrderStatus.LotteryType.JJLC_GAME.getType()){
			
			issueNo = IssueConstant.SPORT_ISSUE_CONSTANT;
			if(LotteryType.JCZQHHGG.getText().equals(lotteryId) || LotteryType.JCLQHHGG.getText().equals(lotteryId)){
				
				reMsg = TransactionProcessor
						.dynamicInvoke("lotteryIssue", "getMatchCompetiveResultList",
								issueNo,matchId);
				
			}else{
				reMsg = TransactionProcessor
						.dynamicInvoke("lotteryIssue", "getMatchCompetiveResult",
								issueNo,lotteryId,matchId);
			}
			
		}else{
				if(IssueConstant.MATCHPLAY_BEIDAN_ALL.equals(lotteryId)){
					
					matchId = IssueUtil.getBeiDanTransferId(issueNo, matchId, OrderStatus.LotteryType.JJBD_GAME.getType());
				
					reMsg = TransactionProcessor
							.dynamicInvoke("lotteryIssue", "getMatchCompetiveResultList",
									issueNo,matchId);
				}else{
					matchId = IssueUtil.getBeiDanTransferId(issueNo, matchId, OrderUtil.getJcCategoryDetail(lotteryId));
					reMsg = TransactionProcessor
							.dynamicInvoke("lotteryIssue", "getMatchCompetiveResult",
									issueNo,lotteryId,matchId);
				}
		}
		
		
		try {

			if (null != reMsg.getObj()
					&& ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(reMsg
							.getStatusCode())) {
				matchCompetive = (MatchCompetive) reMsg.getObj();
				competiveResults = matchCompetive.getMatchCompetiveResultList();
				match = new Match();
				List<Result> results = match.getResult();
				
				if(matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue() || 
						matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue()) match.setMatchid(matchCompetive.getTransferId());
				else if (matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.BEIDAN.getValue() ||
						matchCompetive.getMatchType() == SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue()) match.setMatchid(matchCompetive.getMatchNo());
				else match.setMatchid("");
//				match.setMatchid(matchCompetive.getTransferId());
				match.setGamename(matchCompetive.getMatchName().trim());
				match.setGameno(matchCompetive.getMatchNo());
				match.setHomename(matchCompetive.getHomeTeam().trim());
				match.setGuestname(matchCompetive.getGuestTeam().trim());
				
				matchs.add(match);
				match.setBf(matchCompetive.getDrawResult());
				if(StringUtils.isEmpty(match.getBf())||competiveResults.size()<1){
					Log.fucaibiz.debug("102getMatchCompetiveResultList不存在开奖信息,msg=%s",
							reMsg.getMsg());
					response.setData("不存在获奖信息");
					response.setStatusCode(ConstantsUtil.STATUS_CODE_NO_WINNINGMSG);
					return response;
				}
				for (MatchCompetiveResult matchCompetiveResult : competiveResults) {
					result = new Result();
					result.setDgpv(matchCompetiveResult.getSp());
					result.setLotteryid(matchCompetiveResult.getLotteryId());
					result.setResult(matchCompetiveResult.getDrawResult());
					result.setRq(matchCompetiveResult.getRq());
					results.add(result);
				}
			} else if (ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(reMsg
					.getStatusCode())) {
				Log.fucaibiz.debug("102getMatchCompetiveResultList不存在开奖信息,msg=%s",
						reMsg.getMsg());
				response.setData("不存在获奖信息");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NO_WINNINGMSG);
				return response;
			} else {
				Log.fucaibiz.debug("102findJcDrawResult发生异常,msg=%s",
						reMsg.getMsg());
				response.setData(reMsg.getMsg());
				response.setStatusCode(reMsg.getStatusCode());
				return response;
			}
			// 期号信息
		
				
			Log.fucaibiz.info(
					"102开奖公告信息获取成功,partnerId=%s,lotteryId=%s,matchId=%s",
					partnerId, lotteryId, matchId);

			matchresults.setIssue(issueNo);
//			matchresults.setGameid(lotteryId);
			body763.setMatchresults(matchresults);

			head763.setPartnerid(partnerId);
			head763.setTime(DateUtil.getCurrentDateTime());
			head763.setTranscode("763");
			head763.setVersion(version);

			msg763.setHead(head763);
			msg763.setBody(body763);

			response.setData(TransactionMsgLoader763.msgToXml(msg763));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode("763");
			Log.fucaibiz.debug(
					"763消息返回成功,partnerId=%s,lotteryId=%s,matchId=%s",
					partnerId, lotteryId, matchId);
		} catch (Exception e) {
			Log.fucaibiz.error("102查询发生异常,partnerId=" + partnerId
					+ ",lotteryId=" + lotteryId + ",matchId=" + matchId, e);
			Log.run.error("102查询发生异常,partnerId=" + partnerId
					+ ",lotteryId=" + lotteryId + ",matchId=" + matchId, e);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			return response;
		}
		return response;
	}
}
