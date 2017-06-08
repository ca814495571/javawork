package com.cqfc.businesscontroller.task;

import com.cqfc.order.model.SportOrder;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;

/**
 * 竞技彩订单支付
 * 
 * @author liwh
 */
public class PaySportOrderProcessor {

	public static void process(SportOrder order, boolean isPartnerAccount) {
		long orderNo = order.getOrderNo();
		long userId = order.getUserId();
		String paySerialNumber = order.getPaySerialNumber();
		long orderMoney = order.getTotalAmount();
		String partnerId = order.getPartnerId();
		String ticketId = order.getTradeId();

		try {
			// 支付
			ReturnMessage payMsg = null;
			if (!isPartnerAccount) {
				payMsg = TransactionProcessor.dynamicInvoke("userAccount", "payUserAccount", userId, paySerialNumber,
						orderMoney);
			} else {
				payMsg = TransactionProcessor.dynamicInvoke("partnerAccount", "payPartnerAccount", partnerId,
						orderMoney, paySerialNumber);
			}
			if (!payMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.error("竞技彩,订单支付发生异常,orderNo=%d,ticketId=%s,errorMsg=%s", orderNo, ticketId,
						payMsg.getMsg());
				return;
			}

			int isPaySuccess = (Integer) payMsg.getObj();
			Log.fucaibiz.info("竞技彩,订单支付完成,orderNo=%d,paySerialNumber=%s,payTotalMoney=%d,returnValue=%d", orderNo,
					paySerialNumber, orderMoney, isPaySuccess);

			if (isPaySuccess > 0 || isPaySuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {

				int orderStatus = SportOrder.OrderStatus.HAS_PAYMENT.getValue();

				boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, orderStatus, ticketId);
				Log.fucaibiz.info("竞技彩,支付后批更新map是否存在,orderNo=%d,isExist=%b", orderNo, mapIsExist);
				if (mapIsExist) {
					return;
				}

				// 更新订单状态为'已付款'
				int hasPayQueueValue = BatchUtil.updateSportStatusAddQueue(orderNo, orderStatus, ticketId);

				int updateHasPay = BatchConstant.BATCH_OPERATE_FAILED;
				if (hasPayQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
					updateHasPay = BatchUtil.sportStatusRoundResult(orderNo, orderStatus);
				}

				Log.fucaibiz.info("竞技彩,批更新订单状态'已付款'返回结果,orderNo=%d,returnValue=%d", orderNo, updateHasPay);
				if (updateHasPay == BatchConstant.BATCH_OPERATE_SUCCESS) {
					// 发送出票请求,出票xml拼接
					ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
							EnsapOutTicketChekOrder.ensapSportOutTicketOrder(order));

					if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.error("竞技彩,请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
						return;
					}
					ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
					int sendTicketValue = sendTicket.getStatusCode();

					// int sendTicketValue =
					// TicketIssueConstant.STATUS_SEND_TICKET_OK;

					if (sendTicketValue == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
						Log.fucaibiz.info("竞技彩,订单请求出票成功,即将更新订单状态为'出票中',orderNo=%d", orderNo);
						int inTicketStatus = SportOrder.OrderStatus.IN_TICKET.getValue();
						boolean inTicketMapIsExist = BatchUtil.isSportStatusMapExist(orderNo, inTicketStatus, ticketId);
						Log.fucaibiz.info("竞技彩,批量更新订单状态'出票中',map是否存在,orderNo=%d,isExist=%b", orderNo,
								inTicketMapIsExist);
						if (inTicketMapIsExist) {
							return;
						}
						int queueValue = BatchUtil.updateSportStatusAddQueue(orderNo, inTicketStatus, ticketId);
						Log.fucaibiz.info("竞技彩,更新订单为'出票中'加入队列,orderNo=%d,queueValue=%d(1成功 2失败)", orderNo, queueValue);
					}
				}
			} else {
				// 支付失败,取消订单
				int cancelStatus = SportOrder.OrderStatus.ORDER_CANCEL.getValue();
				boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, cancelStatus, ticketId);
				Log.fucaibiz.info("竞技彩,支付失败取消订单更新map是否存在,orderNo=%d,isExist=%b", orderNo, mapIsExist);
				if (mapIsExist) {
					return;
				}
				int cancelQueueValue = BatchUtil.updateSportStatusAddQueue(orderNo, cancelStatus, ticketId);
				Log.fucaibiz.info("竞技彩,扣款异常或者帐号钱数不足取消订单,加入批队列,orderNo=%d,orderStatus=%d,queueValue=%d", orderNo,
						cancelStatus, cancelQueueValue);
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,投注订单支付线程池处理异常,即由未捕获的异常导致的错误,partnerId=%,ticketId=%s,errorMsg=%s", partnerId,
					ticketId, e);
		}
	}

}
