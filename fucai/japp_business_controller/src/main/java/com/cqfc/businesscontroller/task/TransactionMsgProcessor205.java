package com.cqfc.businesscontroller.task;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.businesscontroller.util.OrderOperateUtil;
import com.cqfc.order.OrderService;
import com.cqfc.order.datacenter.OrderMemcacheUtil;
import com.cqfc.order.model.Order;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader205;
import com.cqfc.xmlparser.TransactionMsgLoader755;
import com.cqfc.xmlparser.transactionmsg205.Body;
import com.cqfc.xmlparser.transactionmsg205.Headtype;
import com.cqfc.xmlparser.transactionmsg205.Msg;
import com.cqfc.xmlparser.transactionmsg205.Querytype;
import com.cqfc.xmlparser.transactionmsg205.Ticket;
import com.cqfc.xmlparser.transactionmsg205.Tickets;
import com.cqfc.xmlparser.transactionmsg755.Ticketbatchres;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class TransactionMsgProcessor205 {

	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String respCode = "755";
		// 请求数据
		String xmlstr = message.getTransMsg();
		Msg reqMsg = TransactionMsgLoader205.xmlToMsg(xmlstr);
		// head信息
		Headtype head = reqMsg.getHead();
		String partnerId = head.getPartnerid();
		// body信息
		Body body = reqMsg.getBody();

		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			OrderService orderService = ctx.getBean("orderService", OrderService.class);

			Querytype queryType = body.getTicketorder();
			Tickets tickets = queryType.getTickets();
			List<Ticket> ticketList = tickets.getTicket();

			com.cqfc.xmlparser.transactionmsg755.Msg msg755 = new com.cqfc.xmlparser.transactionmsg755.Msg();
			com.cqfc.xmlparser.transactionmsg755.Headtype headtype = new com.cqfc.xmlparser.transactionmsg755.Headtype();
			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg755.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg755.Body reqBody = new com.cqfc.xmlparser.transactionmsg755.Body();
			com.cqfc.xmlparser.transactionmsg755.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg755.Querytype();
			com.cqfc.xmlparser.transactionmsg755.Tickets respTickets = new com.cqfc.xmlparser.transactionmsg755.Tickets();

			List<Ticketbatchres> respTicket = respTickets.getTicketbatchres();
			int size = Integer.valueOf(queryType.getSize());
			if (size > 100) {
				response.setStatusCode(ConstantsUtil.STATUS_CODE_OVER_LARGESTNUM);
				response.setData("超过最大注数");
				response.setResponseTransCode(respCode);
				return response;
			}
			if (size != ticketList.size()) {
				response.setStatusCode(ConstantsUtil.STATUS_CODE_TOTALORDER_NOTACTUAL);
				response.setData("订单总数与实际不匹配");
				response.setResponseTransCode(respCode);
				return response;
			}
			for (Ticket ticket : ticketList) {
				String partnerTradeId = ticket.getId();
				String issue = ticket.getIssue();
				String game = ticket.getGameid();

				// 1.由ticketId、partnerId从memcache获取orderNo
				long tempOrderNo = OrderMemcacheUtil.getOrderNoFromMemcache(partnerId, partnerTradeId);
				long orderNo = 0;
				Order order = null;
				if (tempOrderNo > 0) {
					order = orderService.findOrderByOrderNo(tempOrderNo);
					orderNo = tempOrderNo;
				} else {
					// 2.由ticketId、partnerId从db中获取order
					order = orderService.findOrderByParams(partnerId, partnerTradeId);
					orderNo = null != order ? order.getOrderNo() : 0;
				}

				Ticketbatchres batch = new Ticketbatchres();
				if (null == order || "".equals(order)) {
					Log.run.debug("205 find not exist,orderNo=%s", orderNo);
					batch.setId(partnerTradeId);
					batch.setPalmid(String.valueOf(orderNo));
					batch.setMultiple("0");
					batch.setGameid(game);
					batch.setIssue(issue);
					batch.setPlaytype("0");
					batch.setMoney("0");
					batch.setStatuscode(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST);
					batch.setMsg("订单不存在");
					respTicket.add(batch);
					continue;
				}
				Log.run.debug("205 find,orderNo=%s,orderStatus=%d", orderNo, order.getOrderStatus());
				String errCodeStatus = order.getErrCodeStatus();
				String errCodeRemark = order.getErrCodeRemark();
				String resultStr = "";
				if (null != errCodeStatus && !"".equals(errCodeStatus) && null != errCodeRemark
						&& !"".equals(errCodeRemark)) {
					resultStr = errCodeStatus + "#" + errCodeRemark;
				} else {
					resultStr = OrderOperateUtil.getStatusCode(order.getOrderStatus());
				}
				String[] status = resultStr.split("#");

				batch.setId(partnerTradeId);
				batch.setPalmid(String.valueOf(orderNo));
				batch.setMultiple(String.valueOf(order.getMultiple()));
				batch.setGameid(order.getLotteryId());
				batch.setIssue(order.getIssueNo());
				batch.setPlaytype(order.getPlayType());
				batch.setMoney(MoneyUtil.toYuanStr(order.getTotalAmount()));
				batch.setStatuscode(status[0]);
				batch.setMsg(status[1]);
				respTicket.add(batch);
			}
			ticketResult.setTickets(respTickets);
			reqBody.setTicketorder(ticketResult);
			msg755.setBody(reqBody);

			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setData(TransactionMsgLoader755.msgToXml(msg755));
			response.setResponseTransCode(respCode);

		} catch (Exception e) {
			Log.fucaibiz.error("205查询订单,系统错误,即由未捕获的异常导致的错误,partnerId:" + partnerId, e);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setResponseTransCode(respCode);
			return response;
		}
		return response;
	}

}
