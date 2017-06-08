package com.cqfc.util;

import java.util.List;

import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.protocol.ticketissue.OutTicketOrder;
import com.cqfc.xmlparser.TransactionMsgLoader104;
import com.cqfc.xmlparser.TransactionMsgLoader105;
import com.cqfc.xmlparser.transactionmsg104.Body;
import com.cqfc.xmlparser.transactionmsg104.Headtype;
import com.cqfc.xmlparser.transactionmsg104.Msg;
import com.cqfc.xmlparser.transactionmsg104.Ticket;
import com.cqfc.xmlparser.transactionmsg104.Ticketordertype;
import com.cqfc.xmlparser.transactionmsg104.Tickets;
import com.cqfc.xmlparser.transactionmsg104.User;
import com.jami.util.Log;

/**
 * 出票请求,order转换xml工具类
 * 
 * @author liwh
 */
public class ReqPrintXmlUtil {	

//	public static final String partnerId = "25000016";
//
//	public static final String userId = "2500000016";
//
//	public static final String idCard = "06029250-0";
//
//	public static final String realName = "张剑";
//
//	public static final String phone = "13828820191";

	public static String orderOutTicketToXml(OutTicketOrder outTicketOrder, FucaiPartnerInfo partnerInfo) {
		String partnerId = partnerInfo.getPartnerId();
		String userId = partnerInfo.getUserId();
		String idCard = partnerInfo.getIdCard();
		String realName = partnerInfo.getRealName();
		String phone = partnerInfo.getPhone();
		
		String xml = "";
		try {
			Msg msg = new Msg();
			Headtype headType = new Headtype();

			headType.setPartnerid(partnerId);
			headType.setTranscode("104");
			headType.setVersion("1.0");
			headType.setTime(DateUtil.getCurrentDateTime());

			Body body = new Body();
			Ticketordertype ticketOrder = new Ticketordertype();

			String money = String.valueOf(outTicketOrder.getTotalMoney() / 100);

			Tickets tickets = new Tickets();
			List<Ticket> ticketList = tickets.getTicket();
			Ticket ticket = new Ticket();
			ticket.setBall(outTicketOrder.getOrderContent());
			ticket.setId(outTicketOrder.getOrderNo());
			ticket.setIssue(LotteryUtil.convertIssueNo(outTicketOrder.getLotteryId(), outTicketOrder.getIssueNo()));
			ticket.setMoney(money);
			ticket.setMultiple(String.valueOf(outTicketOrder.getMultiple()));
			ticket.setPlaytype(outTicketOrder.getPlayType());
			ticketList.add(ticket);

			User user = new User();
			user.setIdcard(idCard);
			user.setPhone(phone);
			user.setRealname(realName);
			user.setUserid(userId);

			ticketOrder.setGameid(outTicketOrder.getLotteryId());
			ticketOrder.setMachine("182.245.213.183");
			ticketOrder.setProvince("cq");
			ticketOrder.setTickets(tickets);
			ticketOrder.setTicketsnum("1");
			ticketOrder.setTotalmoney(money);
			ticketOrder.setUser(user);

			body.setTicketorder(ticketOrder);

			msg.setHead(headType);
			msg.setBody(body);
			xml = TransactionMsgLoader104.msgToXml(msg);
		} catch (Exception e) {
			Log.run.error("out ticket,order converted to xml error,orderNo=" + outTicketOrder.getOrderNo(), e);
			return xml;
		}
		return xml;
	}

	public static String orderQueryTicketToXml(OutTicketOrder outTicketOrder, FucaiPartnerInfo partnerInfo) {
		String partnerId = partnerInfo.getPartnerId();
		String userId = partnerInfo.getUserId();
		
		String xml = "";
		try {
			com.cqfc.xmlparser.transactionmsg105.Msg msg = new com.cqfc.xmlparser.transactionmsg105.Msg();
			com.cqfc.xmlparser.transactionmsg105.Headtype headtype = new com.cqfc.xmlparser.transactionmsg105.Headtype();
			headtype.setTranscode("105");
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");

			com.cqfc.xmlparser.transactionmsg105.Body body = new com.cqfc.xmlparser.transactionmsg105.Body();
			com.cqfc.xmlparser.transactionmsg105.Querytype queryType = new com.cqfc.xmlparser.transactionmsg105.Querytype();
			queryType.setGameid(outTicketOrder.getLotteryId());
			queryType.setId(outTicketOrder.getOrderNo());
			queryType.setIssue(LotteryUtil.convertIssueNo(outTicketOrder.getLotteryId(), outTicketOrder.getIssueNo()));
			queryType.setUserid(userId);
			body.setQueryticket(queryType);

			msg.setHead(headtype);
			msg.setBody(body);

			xml = TransactionMsgLoader105.msgToXml(msg);
		} catch (Exception e) {
			Log.run.error("query ticket,order converted to xml error,orderNo=" + outTicketOrder.getOrderNo(), e);
			return xml;
		}
		return xml;
	}

}
