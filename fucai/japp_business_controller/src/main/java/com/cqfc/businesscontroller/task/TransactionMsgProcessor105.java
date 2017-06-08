package com.cqfc.businesscontroller.task;

import org.springframework.context.ApplicationContext;

import com.cqfc.businesscontroller.util.OrderOperateUtil;
import com.cqfc.order.OrderService;
import com.cqfc.order.datacenter.OrderMemcacheUtil;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.xmlparser.TransactionMsgLoader105;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.transactionmsg105.Body;
import com.cqfc.xmlparser.transactionmsg105.Headtype;
import com.cqfc.xmlparser.transactionmsg105.Msg;
import com.cqfc.xmlparser.transactionmsg105.Querytype;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class TransactionMsgProcessor105 {

	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String respCode = "605";
		// 请求数据
		String xmlstr = message.getTransMsg();
		Msg reqMsg = TransactionMsgLoader105.xmlToMsg(xmlstr);
		// head信息
		Headtype head = reqMsg.getHead();
		String partnerId = head.getPartnerid();
		// body信息
		Body body = reqMsg.getBody();
		Querytype queryType = body.getQueryticket();
		String partnerTradeId = queryType.getId();
		String gameId = queryType.getGameid();
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			OrderService orderService = ctx.getBean("orderService", OrderService.class);

			// 1.由ticketId、partnerId从memcache获取orderNo
			long tempOrderNo = OrderMemcacheUtil.getOrderNoFromMemcache(partnerId, partnerTradeId);

			Log.fucaibiz.debug("查询订单信息,从memcache获取orderNo,partnerId=%s,partnerTradeId=%s,orderNo=%d", partnerId,
					partnerTradeId, tempOrderNo);
			long orderNo = 0;

			String errCodeStatus = "";
			String errCodeRemark = "";
			int orderStatus = 0;
			String lotteryId = "";
			int multiple = 0;
			String issueNo = "";
			String playType = "";
			long totalAmount = 0;
			String odds = "";
			int lotteryDetaiType = 0;

			int lotteryType = OrderUtil.getLotteryCategory(gameId);
			if (lotteryType == OrderStatus.LotteryType.NUMBER_GAME.getType()) {
				Order order = null;
				if (tempOrderNo > 0) {
					order = orderService.findOrderByOrderNo(tempOrderNo);
					orderNo = tempOrderNo;
				} else {
					// 2.由ticketId、partnerId从db中获取order
					order = orderService.findOrderByParams(partnerId, partnerTradeId);
					orderNo = null != order ? order.getOrderNo() : 0;
				}
				if (null == order || "".equals(order)) {
					response.setStatusCode(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST);
					response.setData("订单不存在");
					response.setResponseTransCode(respCode);
					return response;
				}
				errCodeStatus = order.getErrCodeStatus();
				errCodeRemark = order.getErrCodeRemark();
				orderStatus = order.getOrderStatus();
				lotteryId = order.getLotteryId();
				multiple = order.getMultiple();
				issueNo = order.getIssueNo();
				playType = order.getPlayType();
				totalAmount = order.getTotalAmount();
			} else if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
				SportOrder order = null;
				if (tempOrderNo > 0) {
					order = orderService.findSportOrderByOrderNo(tempOrderNo);
					orderNo = tempOrderNo;
				} else {
					// 2.由ticketId、partnerId从db中获取order
					order = orderService.findSportOrderByParam(partnerId, partnerTradeId);
					orderNo = null != order ? order.getOrderNo() : 0;
				}
				if (null == order || "".equals(order)) {
					response.setStatusCode(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST);
					response.setData("订单不存在");
					response.setResponseTransCode(respCode);
					return response;
				}
				errCodeStatus = order.getErrCodeStatus();
				errCodeRemark = order.getErrCodeRemark();
				orderStatus = order.getOrderStatus();
				lotteryId = order.getLotteryId();
				multiple = order.getMultiple();
				issueNo = order.getIssueNo();
				playType = order.getPlayType();
				totalAmount = order.getTotalAmount();
				lotteryDetaiType = OrderUtil.getJcCategoryDetail(lotteryId);
				if (lotteryDetaiType == OrderStatus.LotteryType.JJZC_GAME.getType()
						|| lotteryDetaiType == OrderStatus.LotteryType.JJLC_GAME.getType()) {
					for (SportOrderDetail detail : order.getSportOrderDetailList()) {
//						odds += detail.getTransferId() + "(" + detail.getRq() + ")~[" + detail.getSp() + "]~0/";
						odds += detail.getTransferId() + "~[" + detail.getSp() + "]~0/";
					}
					odds = odds.substring(0, odds.length() - 1);
				}
			}

			if (orderStatus == 0) {
				response.setStatusCode(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST);
				response.setData("订单不存在");
				response.setResponseTransCode(respCode);
				return response;
			}

			String resultStr = "";
			if (null != errCodeStatus && !"".equals(errCodeStatus) && null != errCodeRemark
					&& !"".equals(errCodeRemark)) {
				resultStr = errCodeStatus + "#" + errCodeRemark;
			} else {
				resultStr = OrderOperateUtil.getStatusCode(orderStatus);
			}
			String[] status = resultStr.split("#");

			Log.run.debug("105 find,orderNo=%d,orderStatus=%d", orderNo, orderStatus);

			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = new com.cqfc.xmlparser.transactionmsg605.Msg();
			com.cqfc.xmlparser.transactionmsg605.Headtype headtype = new com.cqfc.xmlparser.transactionmsg605.Headtype();
			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg605.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg605.Body reqBody = new com.cqfc.xmlparser.transactionmsg605.Body();
			com.cqfc.xmlparser.transactionmsg605.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg605.Querytype();

			String statusCode = status[0];
			ticketResult.setId(partnerTradeId);
			ticketResult.setPalmid(String.valueOf(orderNo));
			ticketResult.setGameid(lotteryId);
			ticketResult.setMultiple(String.valueOf(multiple));
			ticketResult.setIssue(issueNo);
			ticketResult.setPlaytype(playType);
			ticketResult.setMoney(MoneyUtil.toYuanStr(totalAmount));
			ticketResult.setStatuscode(statusCode);
			ticketResult.setMsg(status[1]);
			if (!statusCode.equals(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS)) {
				odds = "";
			}
			if (lotteryDetaiType == OrderStatus.LotteryType.JJZC_GAME.getType()
					|| lotteryDetaiType == OrderStatus.LotteryType.JJLC_GAME.getType()) {
				ticketResult.setOdds(odds);
			}
			reqBody.setTicketresult(ticketResult);
			msg605.setBody(reqBody);

			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setData(TransactionMsgLoader605.msgToXml(msg605));
			response.setResponseTransCode(respCode);

		} catch (Exception e) {
			Log.fucaibiz.error("105查询订单,系统错误,即由未捕获的异常导致的错误,partnerId=" + partnerId + ",ticketId=" + partnerTradeId, e);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setResponseTransCode(respCode);
			return response;
		}
		return response;
	}

}
