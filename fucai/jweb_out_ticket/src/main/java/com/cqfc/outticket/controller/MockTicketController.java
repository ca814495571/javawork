package com.cqfc.outticket.controller;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.outticket.model.OutTicket;
import com.cqfc.outticket.model.Statis;
import com.cqfc.outticket.service.OutTicketService;
import com.cqfc.outticket.task.ChuPiao;
import com.cqfc.outticket.task.Transcode102;
import com.cqfc.outticket.task.Transcode106;
import com.cqfc.outticket.util.MockConstansUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.LotteryUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.xmlparser.TransactionMsgLoader103;
import com.cqfc.xmlparser.TransactionMsgLoader104;
import com.cqfc.xmlparser.TransactionMsgLoader105;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.TransactionMsgLoader703;
import com.cqfc.xmlparser.TransactionMsgLoader704;
import com.cqfc.xmlparser.transactionmsg104.Ticketordertype;
import com.cqfc.xmlparser.transactionmsg104.Tickets;
import com.cqfc.xmlparser.transactionmsg105.Body;
import com.cqfc.xmlparser.transactionmsg105.Headtype;
import com.cqfc.xmlparser.transactionmsg105.Msg;
import com.cqfc.xmlparser.transactionmsg105.Querytype;
import com.cqfc.xmlparser.transactionmsg703.Stat;
import com.jami.util.Log;

@Controller
@RequestMapping("/mock")
public class MockTicketController {
	@Resource
	private OutTicketService service;
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	
	@RequestMapping(value = "/process", produces = { "text/plain;charset=UTF-8" })
	@ResponseBody
	public String process(String msg, String key, String partnerid, String transcode){
		String ret = "";
		Log.run.info("mock message,transcode=%s,partnerid=%s,msg=%s", transcode, partnerid, msg);
		if (msg == null || "".equals(msg)) {
			return "";
		}
		
		if(transcode != null && !"".equals(transcode)){
			if(transcode.equals(MockConstansUtil.TRANSCODE_104)){
				ret = outTicket(msg, key, partnerid, transcode);
			}
			else if(transcode.equals(MockConstansUtil.TRANSCODE_105)){
				ret = findOrderStaByNo(msg, key, partnerid, transcode);
			}
			else if(transcode.equals(MockConstansUtil.TRANSCODE_103)){
				ret = statistics103(msg, key, partnerid, transcode);
			}
			else if(transcode.equals(MockConstansUtil.TRANSCODE_102)){
				ret = fetchDrawResult(msg, key, partnerid, transcode);
			}
			else if(transcode.equals(MockConstansUtil.TRANSCODE_106)){
				ret = fetchUserAccountInfo(msg, key, partnerid, transcode);
			}
			else{
				return "";
			}
		}
		else{
			return "";
		}
		
		return ret;
	}

	private String fetchUserAccountInfo(String msg, String key,
			String partnerid, String transcode) {
		
		return Transcode106.fetchUserAccountInfo(msg, key, partnerid, transcode);
	}

	private String fetchDrawResult(String msg, String key, String partnerid,
			String transcode) {

		return Transcode102.fetchDrawResult(msg, key, partnerid, transcode);
	}
	
	

	public String outTicket(String msg, String key, String partnerid, String transcode) {
		String ticketId = null;
		try {
			com.cqfc.xmlparser.transactionmsg704.Msg msg704 = new com.cqfc.xmlparser.transactionmsg704.Msg();
			com.cqfc.xmlparser.transactionmsg104.Msg requestMsg = TransactionMsgLoader104.xmlToMsg(msg);
			com.cqfc.xmlparser.transactionmsg704.Headtype headtype = new com.cqfc.xmlparser.transactionmsg704.Headtype();
			headtype.setPartnerid(requestMsg.getHead().getPartnerid());
			headtype.setTranscode("704");
			headtype.setVersion(requestMsg.getHead().getVersion());
			headtype.setTime(requestMsg.getHead().getTime());
			msg704.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg704.Body body = new com.cqfc.xmlparser.transactionmsg704.Body();
			com.cqfc.xmlparser.transactionmsg704.Ticketordertype ticketordertype704 = new com.cqfc.xmlparser.transactionmsg704.Ticketordertype();
			com.cqfc.xmlparser.transactionmsg704.Tickets tickets = new com.cqfc.xmlparser.transactionmsg704.Tickets();

			List<com.cqfc.xmlparser.transactionmsg704.Ticket> ticketList = tickets.getTicket();
			Tickets reqTickets = requestMsg.getBody().getTicketorder().getTickets();

			for (com.cqfc.xmlparser.transactionmsg104.Ticket reqTicket : reqTickets.getTicket()) {
				com.cqfc.xmlparser.transactionmsg704.Ticket ticket = new com.cqfc.xmlparser.transactionmsg704.Ticket();
				ticketId = reqTicket.getId();
				ticket.setId(reqTicket.getId());
				ticket.setIssue(reqTicket.getIssue());
				ticket.setMoney(reqTicket.getMoney());
				ticket.setMultiple(reqTicket.getMultiple());
				ticket.setPlaytype(reqTicket.getPlaytype());
				ticket.setMsg("交易中");
				ticket.setStatuscode(ConstantsUtil.STATUS_CODE_TRADING);
				ticketList.add(ticket);
			}

			ticketordertype704.setTickets(tickets);
			Ticketordertype reqTicketorder = requestMsg.getBody().getTicketorder();
			ticketordertype704.setGameid(reqTicketorder.getGameid());
			ticketordertype704.setTicketsnum(reqTicketorder.getTicketsnum());
			ticketordertype704.setTotalmoney(reqTicketorder.getTotalmoney());
			body.setTicketorder(ticketordertype704);
			msg704.setBody(body);
			String xmlStr = TransactionMsgLoader704.msgToXml(msg704);

			ChuPiao cp = new ChuPiao(msg);
			threadPoolTaskExecutor.submit(cp);
			Log.run.debug("t_out_ticket submit success, ticketId=%s", ticketId);
			return xmlStr;
		} catch (RejectedExecutionException e) {
			Log.run.error("t_out_ticket threadpool full, ticketId=%s, exception=%e", ticketId, e);
		} catch (Exception e) {
			Log.run.error("t_out_ticket submit fail, ticketId=%s, exception=%e", ticketId, e);
		}
		return "";
	}
	
	public String findOrderStaByNo(String msg, String key, String partnerid, String transcode) {
		if (msg == null || "".equals(msg)) {
			return "";
		}
		try {
			String resultXmlStr = null;

			String respCode = "605";
			Msg reqMsg = TransactionMsgLoader105.xmlToMsg(msg);
			Headtype head = reqMsg.getHead();
			String partnerId = head.getPartnerid();
			Body body = reqMsg.getBody();
			Querytype queryType = body.getQueryticket();
			String partnerTradeId = queryType.getId();

			String orderNo = queryType.getId();
			String lotteryId = queryType.getGameid();
			String issueNo = queryType.getIssue();
			Log.run.debug("findOrderStaByNo,orderNo=%s", orderNo);
			OutTicket ot = service.findOrderStaByOrderNo(orderNo);

			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = new com.cqfc.xmlparser.transactionmsg605.Msg();
			com.cqfc.xmlparser.transactionmsg605.Headtype headtype = new com.cqfc.xmlparser.transactionmsg605.Headtype();

			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(String.valueOf(System.currentTimeMillis()));
			headtype.setVersion("1.0");

			com.cqfc.xmlparser.transactionmsg605.Body reqBody = new com.cqfc.xmlparser.transactionmsg605.Body();
			com.cqfc.xmlparser.transactionmsg605.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg605.Querytype();
			ticketResult.setId(partnerTradeId);
			ticketResult.setPalmid(orderNo);
			ticketResult.setGameid(lotteryId);
			ticketResult.setIssue(issueNo);
			ticketResult.setOrgserial(orderNo);
			
			if (ot != null && !"".equals(ot)) {
				ticketResult.setPlaytype(ot.getPlayType());
				if (OrderUtil.getJcCategoryDetail(lotteryId)  == OrderStatus.LotteryType.JJZC_GAME.getType() 
						   || OrderUtil.getJcCategoryDetail(lotteryId)  == OrderStatus.LotteryType.JJLC_GAME.getType()) {
					ticketResult.setOdds(ot.getOrderContentOdds());
				}
				ticketResult.setMultiple(String.valueOf(ot.getMutiple()));
				ticketResult.setMoney(String.valueOf(ot.getOrderMoney() / 100));
				ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);
				ticketResult.setMsg("交易成功");
				Log.run.debug("findOrderStaByNo交易成功,orderNo=%s", ot.getOrderNo());
			} else {
				ticketResult.setPlaytype("");
				ticketResult.setMultiple("");
				ticketResult.setMoney("");
				ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST);
				ticketResult.setMsg("订单不存在");
				Log.run.debug("findOrderStaByNo订单不存在,orderNo=%s", orderNo);
			}

			reqBody.setTicketresult(ticketResult);
			msg605.setHead(headtype);
			msg605.setBody(reqBody);
			resultXmlStr = TransactionMsgLoader605.msgToXml(msg605);
			return resultXmlStr;
		} catch (Exception e) {
			Log.run.error("findOrderStaByNo, exception=%s", e);
		}
		return "";
	}

	public String statistics103(String msg, String key, String partnerid, String transcode) {
		Log.run.debug("statistics103 msg: %s", msg);
		String result = "";
		if (msg == null || "".equals(msg)) {
			return result;
		}

		com.cqfc.xmlparser.transactionmsg103.Msg msg103 = TransactionMsgLoader103.xmlToMsg(msg);
		String partnerId = msg103.getHead().getPartnerid();
		String lotteryId = msg103.getBody().getQueryprizenotice().getGameid();
		String issueNo = msg103.getBody().getQueryprizenotice().getIssue();
		String tableName = "t_out_ticket";
		issueNo = LotteryUtil.convertIssueNo2System(lotteryId, issueNo);
		Statis statis = service.statistics103(lotteryId, issueNo, tableName);

		com.cqfc.xmlparser.transactionmsg703.Msg msg703 = new com.cqfc.xmlparser.transactionmsg703.Msg();
		com.cqfc.xmlparser.transactionmsg703.Headtype head703 = new com.cqfc.xmlparser.transactionmsg703.Headtype();
		com.cqfc.xmlparser.transactionmsg703.Querytype querytype703 = new com.cqfc.xmlparser.transactionmsg703.Querytype();
		com.cqfc.xmlparser.transactionmsg703.Body body703 = new com.cqfc.xmlparser.transactionmsg703.Body();
		com.cqfc.xmlparser.transactionmsg703.Stats stats703 = new com.cqfc.xmlparser.transactionmsg703.Stats();

		head703.setPartnerid(partnerId);
		head703.setTime(DateUtil.getCurrentDateTime());
		head703.setVersion("1.0");
		head703.setTranscode("703");

		List<Stat> stats = stats703.getStat();
		Stat stat = null;

		long totalSale = 0;
		long totalBouns = 0;

		ReturnMessage message = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_TICKET_WINNING,
				"getTotalWinningMoneyByGame", lotteryId, issueNo);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message.getStatusCode())) {
			totalBouns = (Long) message.getObj();
		}

		stat = new Stat();

		stat.setSucmoney(MoneyUtil.toYuanStr(statis.getTotalMoney()));
		stat.setSucnum(String.valueOf(statis.getTotalSize()));

		stat.setFailmoney(MoneyUtil.toYuanStr(0));
		stat.setFailnum(String.valueOf(0));

		stat.setBigprizebonus(MoneyUtil.toYuanStr(0));
		stat.setBigprizenum(String.valueOf(0));

		stat.setSmallprize(MoneyUtil.toYuanStr(0));
		stat.setSmallprizenum(String.valueOf(0));

		stat.setType(String.valueOf(1));

		stats.add(stat);

		querytype703.setTotalbouns(String.valueOf(totalBouns / 100));
		querytype703.setTotalsale(MoneyUtil.toYuanStr(totalSale));
		querytype703.setGameid(lotteryId);
		querytype703.setIssue(LotteryUtil.convertIssueNo(lotteryId, issueNo));
		querytype703.setStats(stats703);
		body703.setStatinfo(querytype703);
		msg703.setBody(body703);
		msg703.setHead(head703);

		result = TransactionMsgLoader703.msgToXml(msg703);
		Log.run.debug("statistics103 result: %s", result);
		return result;
	}
}
