package com.cqfc.order;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqfc.order.datacenter.OrderMemcacheUtil;
import com.cqfc.order.model.Order;
import com.cqfc.order.util.OrderNoUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader610;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class AppendTaskOrderService {

	@Resource
	private OrderService orderService;

	/**
	 * 追号明细生成追号订单
	 * 
	 * @param appendTaskDetail
	 * @return
	 */
	public int createAppendTaskOrder(AppendTaskDetail appendTaskDetail) {

		String appendTaskId = appendTaskDetail.getAppendTaskId();
		String partnerId = appendTaskDetail.getPartnerId();
		String lotteryId = appendTaskDetail.getLotteryId();
		String issueNo = appendTaskDetail.getIssueNo();
		String playType = appendTaskDetail.getPlayType();
		String partnerTradeId = appendTaskId.split("#")[1];
		long minTotalMoney = appendTaskDetail.getTotalMoney();
		long detailId = appendTaskDetail.getDetailId();
		long userId = appendTaskDetail.getUserId();
		String ball = appendTaskDetail.getBall();
		int minMultiple = appendTaskDetail.getMultiple();
		int ticketNum = appendTaskDetail.getNoteNumber();

		// 校验期号是否可以创建订单
		ReturnMessage issueMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId,
				issueNo);
		LotteryIssue issue = (LotteryIssue) issueMsg.getObj();
		String beginTime = issue.getBeginTime();
		String endTime = "SINGLE".equals(OrderUtil.checkPlayType(lotteryId, playType)) ? issue.getSingleEndTime()
				: issue.getCompoundEndTime();
		String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
		// 校验当前时间是否在投注时间范围
		if (DateUtil.compareDateTime(currentTime, endTime) <= 0
				|| DateUtil.compareDateTime(beginTime, currentTime) <= 0) {
			return -1;
		}
		// 合作商交易ID#追号明细ID
		String appendPartnerTradeId = partnerTradeId + "#" + detailId;
		// 追号订单编号格式：setNo#渠道ID#合作商交易ID#追号明细ID
		String ticketId = ParameterUtils.getParameterValue("setNo") + "#" + partnerId + "#" + appendPartnerTradeId;

		long tempOrderNo = OrderMemcacheUtil.getOrderNoFromMemcache(partnerId, ticketId);
		if (tempOrderNo > 0) {
			Log.fucaibiz.error("该明细已追号,lotteryId=%s,issueNo=%s,appendTaskId=%s,detailId=%d", lotteryId, issueNo,
					appendTaskId, detailId);
			return -1;
		}
		long orderNo = OrderNoUtil.getOrderNo(ticketId);

		// 支付流水号
		String paySerialNumber = OrderConstant.APPEND_DEDUCTFREEZESERIANUMBER_PREFIX + orderNo;

		Order order = new Order();
		order.setLotteryId(lotteryId);
		order.setPartnerId(partnerId);
		order.setUserId(userId);
		order.setIssueNo(issueNo);
		order.setOrderNo(orderNo);
		order.setOrderType(Order.OrderType.AFTER_ORDER.getValue());
		order.setOrderStatus(Order.OrderStatus.WAIT_PAYMENT.getValue());
		order.setTotalAmount(minTotalMoney);
		order.setOrderContent(ball);
		order.setMultiple(minMultiple);
		order.setPlayType(playType);
		order.setPaySerialNumber(paySerialNumber);
		order.setTradeId(ticketId);
		order.setTicketNum(ticketNum);
		order.setRealName("");
		order.setCardNo("");
		order.setMobile("");

		int isSuccess = orderService.createOrder(order);

		if (isSuccess == 1 || isSuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
			// 更新追号任务明细状态等信息
			TransactionProcessor.dynamicInvoke("appendTask", "updateAppendAfterOrder", detailId, ticketId);
		}

		if (isSuccess > 0) {
			Log.fucaibiz.info("追号明细生成追号订单成功,orderNo=%d,lotteryId=%s,issueNo=%s,appendTaskId=%s", orderNo, lotteryId,
					issueNo, appendTaskId);
			// 追号订单付款
			ReturnMessage payMsg = TransactionProcessor.dynamicInvoke("userAccount", "deductFreezeMoney", userId,
					minTotalMoney, paySerialNumber);
			int isPaySuccess = (Integer) payMsg.getObj();
			Log.fucaibiz.info("追号订单支付结束,orderNo=%d,lotteryId=%s,issueNo=%s,appendTaskId=%s,returnValue=%d", orderNo,
					lotteryId, issueNo, appendTaskId, isPaySuccess);
			if (isPaySuccess > 0) {
				orderService.updateOrderStatus(orderNo, Order.OrderStatus.HAS_PAYMENT.getValue());

				String printBeginTime = issue.getPrintBeginTime();
				String printEndTime = issue.getPrintEndTime();
				if (DateUtil.compareDateTime(printBeginTime, currentTime) >= 0
						&& DateUtil.compareDateTime(currentTime, printEndTime) > 0) {
					// 判断是否在出票时间范围,创建订单后再请求出票
					ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
							EnsapOutTicketChekOrder.ensapOutTicketOrder(order));
					if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.info("追号订单请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
					}
					ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
					if (sendTicket.getStatusCode() == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
						Log.fucaibiz.info("追号订单出票请求成功,状态为出票中...orderNo=%d", orderNo);
						orderService.updateOrderStatus(orderNo, Order.OrderStatus.IN_TICKET.getValue());
					}
				}
			}
			// 返回结果
			String respCode = "610";
			com.cqfc.xmlparser.transactionmsg610.Msg msg610 = new com.cqfc.xmlparser.transactionmsg610.Msg();

			com.cqfc.xmlparser.transactionmsg610.Headtype headtype = new com.cqfc.xmlparser.transactionmsg610.Headtype();
			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(String.valueOf(System.currentTimeMillis()));
			headtype.setVersion("1.0");
			msg610.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg610.Body reqBody = new com.cqfc.xmlparser.transactionmsg610.Body();
			com.cqfc.xmlparser.transactionmsg610.Querytype appendOrder = new com.cqfc.xmlparser.transactionmsg610.Querytype();
			appendOrder.setId(partnerTradeId);
			appendOrder.setPalmflowid(String.valueOf(orderNo));
			appendOrder.setStatuscode(ConstantsUtil.STATUS_CODE_TRADING);
			appendOrder.setMsg("交易中");
			appendOrder.setSuccmoney(MoneyUtil.toYuanStr(minTotalMoney));
			appendOrder.setFailmoney("0");
			reqBody.setReport(appendOrder);
			msg610.setBody(reqBody);

			String backXml = TransactionMsgLoader610.msgToXml(msg610);
			// 主动通知合作商处理结果
			TransactionProcessor.dynamicInvoke("accessBack", "sendAccessBackMessage", partnerId, backXml);
		}
		return isSuccess;
	}

}
