package com.cqfc.businesscontroller.task;

import java.util.Date;

import com.cqfc.order.model.Order;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class PayOrderProcessor {

	public static void process(Order order, boolean isPartnerAccount, String printBeginTime, String printEndTime) {
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
				Log.fucaibiz.error("订单支付发生异常,orderNo=%d,ticketId=%s,errorMsg=%s", orderNo, ticketId, payMsg.getMsg());
				return;
			}

			int isPaySuccess = (Integer) payMsg.getObj();
			Log.fucaibiz.info("订单支付完成,orderNo=%d,paySerialNumber=%s,payTotalMoney=%d,returnValue=%d", orderNo,
					paySerialNumber, orderMoney, isPaySuccess);

			if (isPaySuccess > 0 || isPaySuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {

				int orderStatus = Order.OrderStatus.HAS_PAYMENT.getValue();

				boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, orderStatus, ticketId);
				Log.fucaibiz.info("支付后批更新map是否存在,orderNo=%d,isExist=%b", orderNo, mapIsExist);
				if (mapIsExist) {
					return;
				}

				// 更新订单状态为'已付款'
				int hasPayQueueValue = BatchUtil.updateStatusAddQueue(orderNo, orderStatus, ticketId);

				int updateHasPay = BatchConstant.BATCH_OPERATE_FAILED;
				if (hasPayQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
					updateHasPay = BatchUtil.statusRoundResult(orderNo, orderStatus);
				}

				Log.fucaibiz.info("批更新订单状态'已付款'返回结果,orderNo=%d,returnValue=%d", orderNo, updateHasPay);
				if (updateHasPay == BatchConstant.BATCH_OPERATE_SUCCESS) {
					// 更新订单状态'已付款'成功,进行出票请求.
					String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
					if (DateUtil.compareDateTime(printBeginTime, currentTime) >= 0
							&& DateUtil.compareDateTime(currentTime, printEndTime) > 0) {

						Log.fucaibiz.info("订单在出票时间范围内,请求出票,orderNo=%d", orderNo);

						ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
								EnsapOutTicketChekOrder.ensapOutTicketOrder(order));

						if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
							Log.fucaibiz.error("请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
							return;
						}
						ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();

						int sendTicketValue = sendTicket.getStatusCode();

						if (sendTicketValue == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
							Log.fucaibiz.info("订单请求出票成功,即将更新订单状态为'出票中',orderNo=%d", orderNo);

							int inTicketStatus = Order.OrderStatus.IN_TICKET.getValue();

							boolean inTicketMapIsExist = BatchUtil.isStatusMapExist(orderNo, inTicketStatus, ticketId);
							Log.fucaibiz.info("批量更新订单状态'出票中',map是否存在,orderNo=%d,isExist=%b", orderNo,
									inTicketMapIsExist);
							if (inTicketMapIsExist) {
								return;
							}
							int queueValue = BatchUtil.updateStatusAddQueue(orderNo, inTicketStatus, ticketId);
							Log.fucaibiz.info("更新订单为'出票中'加入队列,orderNo=%d,queueValue=%d(1成功 2失败)", orderNo, queueValue);
						}
					} else {
						Log.fucaibiz.info("该订单不在出票时间范围内,所以暂不请求出票,orderNo=%d", orderNo);
					}
				}
			} else {
				// 支付失败,取消订单
				int cancelStatus = Order.OrderStatus.ORDER_CANCEL.getValue();
				boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, cancelStatus, ticketId);
				Log.fucaibiz.info("支付失败取消订单更新map是否存在,orderNo=%d,isExist=%b", orderNo, mapIsExist);
				if (mapIsExist) {
					return;
				}
				int cancelQueueValue = BatchUtil.updateStatusAddQueue(orderNo, cancelStatus, ticketId);
				Log.fucaibiz.info("扣款异常或者帐号钱数不足取消订单,加入批队列,orderNo=%d,orderStatus=%d,queueValue=%d", orderNo,
						cancelStatus, cancelQueueValue);
			}
		} catch (Exception e) {
			Log.fucaibiz.error("投注订单支付线程池处理异常,即由未捕获的异常导致的错误,partnerId=%,ticketId=%s,errorMsg=%s", partnerId, ticketId,
					e);
		}
	}

}
