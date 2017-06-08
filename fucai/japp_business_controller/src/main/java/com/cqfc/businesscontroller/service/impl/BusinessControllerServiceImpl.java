package com.cqfc.businesscontroller.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.businesscontroller.task.TransactionMsgProcessor101;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor102;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor103;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor105;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor106;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor107;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor108;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor109;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor110;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor111;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor112;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor116;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor117;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor118;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor119;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor121;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor124;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor130;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor131;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor132;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor133;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor134;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor140;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor141;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor148;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor149;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor151;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor156;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor157;
import com.cqfc.businesscontroller.task.TransactionMsgProcessor205;
import com.cqfc.businesscontroller.util.OrderOperateUtil;
import com.cqfc.order.AppendTaskOrderService;
import com.cqfc.order.OrderService;
import com.cqfc.order.datacenter.OrderMemcacheUtil;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.order.model.UpdateSportOrderStatus;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.OrderNoUtil;
import com.cqfc.order.util.OrderPayUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.businesscontroller.BusinessControllerService;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.OrderMsg;
import com.cqfc.protocol.businesscontroller.PrintMatch;
import com.cqfc.protocol.businesscontroller.SportDetail;
import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.businesscontroller.VoteTicket;
import com.cqfc.protocol.lotteryissue.IssueSport;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetivePlay;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.ticketwinning.service.impl.TicketWinningServiceImpl;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.IssueUtil;
import com.cqfc.util.LotteryPlayTypeConstants;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.PartnerConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SportIssueConstant;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader704;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * @author: giantspider@126.com
 */
@Service
public class BusinessControllerServiceImpl implements BusinessControllerService.Iface {

	@Resource(name = "threadPoolTaskExecutor")
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Autowired
	private AppendTaskOrderService appendTaskOrderService;

	@Override
	public TransResponse ProcessMessage(Message message) throws TException {

		TransResponse response = new TransResponse();

		// processing message...
		switch (message.getTransCode()) {
		case 101:
			response = TransactionMsgProcessor101.process(message);
			break;
		case 102:
			response = TransactionMsgProcessor102.process(message);
			break;
		case 103:
			response = TransactionMsgProcessor103.process(message);
			break;
		case 106:
			response = TransactionMsgProcessor106.process(message);
			break;
		case 107:
			response = TransactionMsgProcessor107.process(message);
			break;
		case 108:
			response = TransactionMsgProcessor108.process(message);
			break;
		case 109:
			response = TransactionMsgProcessor109.process(message);
			break;
		case 110:
			response = TransactionMsgProcessor110.process(message);
			break;
		case 111:
			response = TransactionMsgProcessor111.process(message);
			break;
		case 112:
			response = TransactionMsgProcessor112.process(message);
			break;
		case 116:
			response = TransactionMsgProcessor116.process(message);
			break;
		case 117:
			response = TransactionMsgProcessor117.process(message);
			break;
		case 118:
			response = TransactionMsgProcessor118.process(message);
			break;
		case 119:
			response = TransactionMsgProcessor119.process(message);
			break;
		case 121:
			response = TransactionMsgProcessor121.process(message);
			break;
		case 124:
			response = TransactionMsgProcessor124.process(message);
			break;
		case 130:
			response = TransactionMsgProcessor130.process(message);
			break;
		case 131:
			response = TransactionMsgProcessor131.process(message);
			break;
		case 132:
			response = TransactionMsgProcessor132.process(message);
			break;
		case 133:
			response = TransactionMsgProcessor133.process(message);
			break;
		case 134:
			response = TransactionMsgProcessor134.process(message);
			break;
		case 140:
			response = TransactionMsgProcessor140.process(message);
			break;
		case 141:
			response = TransactionMsgProcessor141.process(message);
			break;
		case 148:
			response = TransactionMsgProcessor148.process(message);
			break;
		case 149:
			response = TransactionMsgProcessor149.process(message);
			break;
		case 151:
			response = TransactionMsgProcessor151.process(message);
			break;
		case 156:
			response = TransactionMsgProcessor156.process(message);
			break;
		case 157:
			response = TransactionMsgProcessor157.process(message);
			break;
		default:
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
			response.setData("XML格式化错误");
		}
		return response;
	}

	@Override
	public int getQueueSize() throws TException {
		return threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size();
	}

	@Override
	public boolean isTicketSuccess(String orderNoStr, int resultNumber) throws TException {
		try {
			long orderNo = Long.valueOf(orderNoStr);
			Log.fucaibiz.info("出票回调,orderNo=%d,resultValue=%d(1失败 2成功 3出票中 4订单不存在)", orderNo, resultNumber);
			if (resultNumber == TicketIssueConstant.SEND_TICKET_RESULT_PROCESSING) {
				return false;
			}
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			OrderService orderService = ctx.getBean("orderService", OrderService.class);

			Order order = orderService.findOrderByOrderNo(orderNo);
			if (null == order || "".equals(order)) {
				Log.fucaibiz.error("出票回调,查询订单信息不存在,orderNo=%d", orderNo);
				return false;
			}
			String ticketId = order.getTradeId();
			int orderType = order.getOrderType();
			String partnerId = order.getPartnerId();
			int currentStatus = order.getOrderStatus();

			if (resultNumber == TicketIssueConstant.SEND_TICKET_RESULT_NOEXIST_ORDER) {
				printAgainAfterCallback(order, orderService);
				return false;
			}

			if (resultNumber != TicketIssueConstant.SEND_TICKET_RESULT_FAIL
					&& resultNumber != TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS) {
				return false;
			}
			int orderStatus = resultNumber == TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS ? Order.OrderStatus.HASTICKET_WAITLOTTERY
					.getValue() : Order.OrderStatus.DRAWER_FAILURE.getValue();

			if (orderStatus <= currentStatus) {
				Log.fucaibiz.info("出票回调订单状态小于等于当前订单状态,orderNo=%d,currentStatus=%d,updateStatus=%d", orderNo,
						currentStatus, orderStatus);
				return false;
			}

			boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, orderStatus, ticketId);
			Log.fucaibiz.info("出票回调,判断批更新MAP中是否存在,orderNo=%d,orderStatus=%d,isExist=%b", orderNo, orderStatus,
					mapIsExist);

			if (!mapIsExist) {
				int queueValue = BatchUtil.updateStatusAddQueue(orderNo, orderStatus, ticketId);

				int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
				if (queueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
					Log.fucaibiz.info("出票回调加入更新订单状态队列成功,orderNo=%d,orderStatus=%d", orderNo, orderStatus);
					operateValue = BatchUtil.statusRoundResult(orderNo, orderStatus);
				} else {
					int tempValue = orderService.updateOrderStatus(orderNo, orderStatus);
					operateValue = tempValue == 1 ? BatchConstant.BATCH_OPERATE_SUCCESS
							: BatchConstant.BATCH_OPERATE_FAILED;
					Log.fucaibiz.info("出票回调加入更新订单状态队列失败,直接调用更新接口,orderNo=%d,orderStatus=%d,returnValue=%d", orderNo,
							orderStatus, operateValue);
				}
				Log.fucaibiz.info("出票回调更新订单状态,orderNo=%d,orderStatus=%d,operateValue=%d", orderNo, orderStatus,
						operateValue);

				if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
					order.setOrderStatus(orderStatus);

					ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount",
							partnerId);
					boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

					if (resultNumber == TicketIssueConstant.SEND_TICKET_RESULT_FAIL) {
						failTicketUpdateOrder(order, isPartnerAccount, orderService);
					}

					int finalStatus = order.getOrderStatus();
					if (finalStatus == Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
							|| finalStatus == Order.OrderStatus.REFUND_SUCCESS.getValue()
							|| finalStatus == Order.OrderStatus.ORDER_CANCEL.getValue()) {

						int syncValue = OrderOperateUtil.syncToGlobal(order, isPartnerAccount);
						Log.fucaibiz.info("同步订单完成,orderNo=%d,syncValue=%d(0未同步 1成功 2失败)", orderNo, syncValue);

						boolean isSyncMapExist = BatchUtil.isSyncMapExist(orderNo, syncValue, ticketId);
						Log.fucaibiz.info("批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b", orderNo,
								syncValue, isSyncMapExist);
						if (!isSyncMapExist) {
							int syncQueueValue = BatchUtil.syncAddQueue(orderNo, syncValue, ticketId);
							if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
								int syncIsSuccess = BatchUtil.syncRoundResult(orderNo);
								if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
									order.setIsSyncSuccess(syncValue);
									Log.fucaibiz.debug("通知合作商处理结果,orderNo=%d,orderStatus=%d", orderNo, finalStatus);
									OrderOperateUtil.notifyPartner(order);
								}
							}
						}

						if (orderType == Order.OrderType.AFTER_ORDER.getValue()) {
							boolean isSuccessPrint = resultNumber == TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS ? true
									: false;
							Log.fucaibiz.debug("追号订单出票回调修改追号明细,orderNo=%d,isOutTicketSuccess=%b", orderNo,
									isSuccessPrint);
							TransactionProcessor.dynamicInvoke("appendTask", "updateDetailAfterPrint", orderNo,
									isSuccessPrint);
						}
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("出票回调处理发生异常,orderNo=" + orderNoStr, e);
			return false;
		}
		return true;
	}

	/**
	 * 回调返回订单不存在,再次请求出票
	 * 
	 * @param order
	 * @param orderService
	 */
	private void printAgainAfterCallback(Order order, OrderService orderService) {
		long orderNo = order.getOrderNo();
		try {
			String ticketId = order.getTradeId();
			Log.fucaibiz.info("出票回调返回订单不存在,再次请求出票.orderNo=%d", orderNo);
			ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
					EnsapOutTicketChekOrder.ensapOutTicketOrder(order));
			if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("再次请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
				return;
			}
			ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
			if (sendTicket.getStatusCode() == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
				Log.fucaibiz.info("再次请求出票提交到线程池成功,orderNo=%d", orderNo);
				if (order.getOrderStatus() == Order.OrderStatus.HAS_PAYMENT.getValue()) {
					// 当前订单状态为'已付款',则更新订单状态为'出票中'
					int inTicket = Order.OrderStatus.IN_TICKET.getValue();
					boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, inTicket, ticketId);
					if (!mapIsExist) {
						int queueValue = BatchUtil.updateStatusAddQueue(orderNo, inTicket, ticketId);
						Log.fucaibiz.info("回调,更新订单为'出票中'加入队列,orderNo=%d,queueValue=%d(1成功 2失败)", orderNo, queueValue);
						if (queueValue == BatchConstant.BATCH_ADD_QUEUE_FAILED) {
							int returnValue = orderService.updateOrderStatus(orderNo, inTicket);
							Log.fucaibiz.info("回调,更新'出票中'加入队列失败,进行单条更新,orderNo=%d,orderStatus=%d,returnValue=%d",
									orderNo, inTicket, returnValue);
						}
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("出票回调返回订单不存在,再次请求出票发生异常,ordeNo=" + orderNo, e);
		}
	}

	/**
	 * 出票失败更新订单信息
	 * 
	 * @param order
	 * @param isPartnerAccount
	 * @param orderService
	 * @return
	 */
	private void failTicketUpdateOrder(Order order, boolean isPartnerAccount, OrderService orderService) {
		long orderNo = order.getOrderNo();
		try {
			int orderType = order.getOrderType();
			long userId = order.getUserId();
			long totalMoney = order.getTotalAmount();
			String ticketId = order.getTradeId();
			String partnerId = order.getPartnerId();
			String paySerialNumber = order.getPaySerialNumber();
			String refundSerialNumber = OrderConstant.ORDER_REFUNDSERIANUMBER_PREFIX + orderNo;

			// 更新订单状态为'退款中'(待考虑是否可以省略此更新...)
			int refundingStatus = Order.OrderStatus.REFUNDING.getValue();
			int refunding = orderService.updateOrderStatus(orderNo, refundingStatus);
			Log.fucaibiz.info("出票失败,订单状态更新成退款中,开始进行退款,orderNo=%d,returnValue=%d", orderNo, refunding);
			if (refunding <= 0) {
				return;
			}
			order.setOrderStatus(refundingStatus);

			// 退款
			ReturnMessage refundMsg = null;
			if (isPartnerAccount) {
				refundMsg = TransactionProcessor.dynamicInvoke("partnerAccount", "modifyRefund", partnerId, totalMoney,
						paySerialNumber, refundSerialNumber);
			} else {
				if (orderType == Order.OrderType.AFTER_ORDER.getValue()) {
					ReturnMessage appendMsg = TransactionProcessor.dynamicInvoke("appendTask",
							"getRefundSerialNumberByOrderNo", orderNo, String.valueOf(userId));
					String freezeSerialNumber = (String) appendMsg.getObj();
					refundSerialNumber = OrderConstant.APPEND_REFUNDFREEZESERIANUMBER_PREFIX + orderNo;

					refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "refundFreezeMoney", userId,
							totalMoney, freezeSerialNumber, refundSerialNumber);
				} else {
					refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "modifyRefund", userId,
							paySerialNumber, refundSerialNumber, totalMoney);
				}
			}
			int refundValue = (Integer) refundMsg.getObj();
			Log.fucaibiz.info("出票失败,退款完成,orderNo=%d,refundValue=%d", orderNo, refundValue);

			if (refundValue > 0 || refundValue == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
				// 更新订单状态'退款成功'.
				int refundStatus = Order.OrderStatus.REFUND_SUCCESS.getValue();
				boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, refundStatus, ticketId);
				Log.fucaibiz.info("出票回调,更新订单状态'退款成功',批更新MAP,orderNo=%d,isExist=%b", orderNo, mapIsExist);

				if (!mapIsExist) {
					int refundQueueValue = BatchUtil.updateStatusAddQueue(orderNo, refundStatus, ticketId);

					int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
					if (refundQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						Log.fucaibiz.info("出票回调加入更新订单状态队列成功,orderNo=%d,orderStatus=%d", orderNo, refundStatus);
						// 轮循等待更新结果
						operateValue = BatchUtil.statusRoundResult(orderNo, refundStatus);
					} else {
						int refundSucess = orderService.updateOrderStatus(orderNo, refundStatus);

						operateValue = refundSucess == 1 ? BatchConstant.BATCH_OPERATE_SUCCESS
								: BatchConstant.BATCH_OPERATE_FAILED;
						Log.fucaibiz.info("出票回调加入更新订单状态队列失败,直接调用更新接口,orderNo=%d,orderStatus=%d,returnValue=%d",
								orderNo, refundStatus, operateValue);
					}
					Log.fucaibiz.info("出票失败退款成功,更新订单状态,orderNo=%d,orderStatus=%d,returnValue=%d", orderNo,
							refundStatus, operateValue);
					if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
						order.setOrderStatus(Order.OrderStatus.REFUND_SUCCESS.getValue());
					}
				}
			}
		} catch (Exception e) {
			Log.error("出票回调,出票失败更新订单信息发生异常,orderNo=" + orderNo, e);
			Log.fucaibiz.error("出票回调,出票失败更新订单信息发生异常,orderNo=" + orderNo, e);
		}
	}

	/**
	 * 追号明细创建订单（定时任务）
	 * 
	 * @param appendTaskDetail
	 * @return
	 * @throws TException
	 */
	@Override
	public int createAppendOrder(AppendTaskDetail appendTaskDetail) throws TException {
		int isSuccess = 0;
		try {
			isSuccess = appendTaskOrderService.createAppendTaskOrder(appendTaskDetail);
			Log.run.debug("追号明细创建订单(定时),appendTaskId=%s,detailId=%d,returnValue=%d",
					appendTaskDetail.getAppendTaskId(), appendTaskDetail.getDetailId(), isSuccess);
		} catch (Exception e) {
			ScanLog.run.error("追号明细创建订单", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public TransResponse createOrderProcess(OrderMsg orderMsg) throws TException {
		TransResponse response = null;
		try {
			String lotteryId = orderMsg.getLotteryId();
			int lotteryType = OrderUtil.getLotteryCategory(lotteryId);
			if (lotteryType == OrderStatus.LotteryType.NUMBER_GAME.getType()) {
				response = createDigitalOrder(orderMsg);
			} else if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
				response = createSportOrder(orderMsg);
			} else {
				Log.fucaibiz.error("104投注彩种错误,既不属于数字彩,也不属于竞技彩,lotteryId=%s", lotteryId);
				response = responseBack704(0, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}
		} catch (Exception e) {
			Log.fucaibiz.error("104投注createOrder发生异常", e);
			response = responseBack704(0, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
		}
		return response;
	}

	/**
	 * 创建竞技彩订单
	 * 
	 * @param orderMsg
	 * @return
	 */
	private TransResponse createSportOrder(OrderMsg orderMsg) {
		TransResponse response = null;
		String partnerTradeId = "";
		long orderNo = 0;
		try {
			String partnerId = orderMsg.getPartnerId();
			VoteTicket voteTicket = orderMsg.getTicketList().get(0);
			partnerTradeId = voteTicket.getTicketId();
			Log.fucaibiz.info("竞技彩订单进入处理流程,ticketId=%s", partnerTradeId);

			long userId = orderMsg.getUserId();
			String realName = orderMsg.getRealName();
			String mobile = orderMsg.getPhone();
			String cardNo = orderMsg.getIdCard();
			String lotteryId = orderMsg.getLotteryId();

			// 1.由partnerId、ticketId从memcache获取orderNo,确定是否已创建,已存在则返回重复订单
			long tempOrderNo = OrderMemcacheUtil.getOrderNoFromMemcache(partnerId, partnerTradeId);
			if (tempOrderNo > 0) {
				Log.fucaibiz.info("竞技彩,该订单在createOrderMap或memcache中已存在,则重复订单,orderNo=%d", orderNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_REPEAT_ORDER, "重复订单");
			}

			// 2.获取系统唯一ID,组合成orderNo
			orderNo = OrderNoUtil.getOrderNo(partnerTradeId);
			if (orderNo <= 0) {
				Log.fucaibiz.error("竞技彩,orderNo生成失败,partnerId=%s,ticketId=%s,orderNo=%d", partnerId, partnerTradeId,
						orderNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}

			// 3.判断订单是否在创建map或memcache中.存在则返回重复订单
			if (BatchUtil.isSportCreateMapExist(orderNo) || !OrderMemcacheUtil.verifyOrderIsNotInMemcache(orderNo)) {
				Log.fucaibiz.info("竞技彩,该订单在createOrderMap或memcache中已存在,则重复订单,orderNo=%d", orderNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_REPEAT_ORDER, "重复订单");
			}

			// 4.校验渠道商类型,用户ID
			ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "findPartnerForCheck", partnerId);
			if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.error("竞技彩,校验渠道商类型发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId, partnerTradeId,
						partnerMsg.getMsg());
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR,
						"合作商id不存在或者参数与xml文件中的不一致");
			}
			LotteryPartner partner = (LotteryPartner) partnerMsg.getObj();
			boolean isPartnerAccount = partner.getPartnerType() == PartnerConstant.PartnerType.USERPARTNER.getValue() ? false
					: true;
			boolean checkUserId = true;
			Log.fucaibiz.debug("竞技彩,校验合作商类型,partnerId=%s,ticketId=%s,isPartner=%b", partnerId, partnerTradeId,
					isPartnerAccount);
			if (!isPartnerAccount) {
				ReturnMessage userMsg = TransactionProcessor.dynamicInvoke("userAccount", "checkUserExist", userId);
				if (!userMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.error("竞技彩,校验用户是否存在发生异常,partnerId=%s,userId=%d,ticketId=%s", partnerId, userId,
							partnerTradeId);
					return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
				} else {
					boolean userIsExist = (Boolean) userMsg.getObj();
					if (!userIsExist) {
						Log.fucaibiz.error("竞技彩,用户USERID不存在,partnerId=%s,orderUserId=%d,ticketId=%s", partnerId,
								userId, partnerTradeId);
						checkUserId = false;
					}
				}
			} else if (partner.getUserId() != userId) {
				Log.fucaibiz.error("竞技彩,渠道商USERID不一致,partnerId=%s,partnerUserId=%d,orderUserId=%d,ticketId=%s",
						partnerId, partner.getUserId(), userId, partnerTradeId);
				checkUserId = false;
			}
			if (!checkUserId) {
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_USERID_ERROR, "用户id错误");
			}

			// 5.期号信息校验
			String issueNo = voteTicket.getIssueNo();
			String playType = voteTicket.getPlayType();
			long orderMoney = MoneyUtil.toCent(Long.valueOf(voteTicket.getMoney()));
			String orderContent = voteTicket.getBall();
			int multiple = voteTicket.getMultiple();

			int lotteryType = OrderUtil.getJcCategoryDetail(lotteryId);

			Log.fucaibiz.info("竞技彩,订单处理中,ticketId=%s,partnerId=%s,isPartnerAccount=%b,lotteryId=%s,issueNo=%s",
					partnerTradeId, partnerId, isPartnerAccount, lotteryId, issueNo);

			if (lotteryType == OrderStatus.LotteryType.JJBD_GAME.getType()
					|| lotteryType == OrderStatus.LotteryType.JJBDSFGG_GAME.getType()) {
				if (null == issueNo || "".equals(issueNo)) {
					Log.fucaibiz.info("竞技彩,期号不能为空,ticketId=%s,partnerId=%s,lotteryId=%s,issueNo=%s", partnerTradeId,
							partnerId, lotteryId, issueNo);
					return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO, "不存在的期次");
				}
				ReturnMessage issueSportMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findIssueSport",
						IssueConstant.MATCHPLAY_BEIDAN_ALL, issueNo);
				IssueSport issueSport = (IssueSport) issueSportMsg.getObj();
				if (null == issueSport || "".equals(issueSport)) {
					Log.fucaibiz.info("竞技彩不存在的期次,ticketId=%s,partnerId=%s,lotteryId=%s,issueNo=%s", partnerTradeId,
							partnerId, lotteryId, issueNo);
					return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO, "不存在的期次");
				}
			} else {
				issueNo = IssueConstant.SPORT_ISSUE_CONSTANT;
			}

			// 6.拆投注内容进行校验,拆分投注内容,校验投注金额
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			TicketWinningServiceImpl ticketService = ctx.getBean("ticketWinningServiceImpl",
					TicketWinningServiceImpl.class);
			List<SportDetail> detailList = null;
			BallCountReturnMessage ballCountMessage = null;
			try {
				if (lotteryType == OrderStatus.LotteryType.JJBD_GAME.getType()
						|| lotteryType == OrderStatus.LotteryType.JJBDSFGG_GAME.getType()) {
					ballCountMessage = ticketService.calBDBallCount(lotteryId, orderContent, playType);
				} else if (lotteryType == OrderStatus.LotteryType.JJZC_GAME.getType()
						|| lotteryType == OrderStatus.LotteryType.JJLC_GAME.getType()) {
					ballCountMessage = ticketService.calJCBallCount(lotteryId, orderContent, playType);
				}
				detailList = ticketService.dismantleOrderContent(orderContent, lotteryId);
			} catch (Exception e) {
				Log.fucaibiz.error("竞技彩,校验投注内容发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId, partnerTradeId, e);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}
			if (!ballCountMessage.getStatusCode().equals(ConstantsUtil.STATUS_CODE_SUCCESS)) {
				Log.fucaibiz.error("竞技彩,投注号码格式错误,partnerId=%s,ticketId=%s,orderContent=%s", partnerId, partnerTradeId,
						orderContent);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR, "投注号码格式错误");
			}
			int ballNum = ballCountMessage.getCount();
			if (null == detailList || detailList.size() == 0) {
				Log.fucaibiz.error("竞技彩,投注内容不正确,partnerId=%s,ticketId=%s,orderContent=%s", partnerId, partnerTradeId,
						orderContent);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR, "投注号码格式错误");
			}
			long total = MoneyUtil.toCent(ballNum * multiple * 2);
			if (orderMoney != total || multiple <= 0) {
				Log.fucaibiz.error(
						"竞技彩,投注金额与拆单后计算的金额不一致,partnerId=%s,ticketId=%s,orderMoney=%d,realMoney=%d,multiple=%d",
						partnerId, partnerTradeId, orderMoney, total, multiple);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_BETTINGAMOUNT_ERROR, "投注金额错误");
			}

			// 7.计算出订单截止时间、开奖时间,校验赛事状态
			String playTypeStr = OrderUtil.getSportPlayType(playType);
			String closeTime = "2080-01-01 00:00:00";
			String deadlineTime = "2010-01-01 00:00:00";
			int checkValue = 0;
			List<SportOrderDetail> orderDetailList = new ArrayList<SportOrderDetail>();
			int matchType = 0;
			int len = detailList.size();
			for (int i = 0; i < len; i++) {
				SportDetail sportDetail = detailList.get(i);
				String transferId = sportDetail.getTransferId();
				Log.fucaibiz.debug("104投注赛事信息,transferId=%s", transferId);

				ReturnMessage matchCompetiveMsg = null;
				// 如果投注内容为混合过关
				if (lotteryId.equals(IssueConstant.SportLotteryType.JCLQ_HHGG.getValue())
						|| lotteryId.equals(IssueConstant.SportLotteryType.JCZQ_HHGG.getValue())) {
					matchCompetiveMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "getMatchCompetive",
							issueNo, transferId);
				} else {
					if (lotteryType == OrderStatus.LotteryType.JJBD_GAME.getType()
							|| lotteryType == OrderStatus.LotteryType.JJBDSFGG_GAME.getType()) {
						int type = lotteryType == OrderStatus.LotteryType.JJBDSFGG_GAME.getType() ? SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG
								.getValue() : SportIssueConstant.CompetitionMatchType.BEIDAN.getValue();
						transferId = IssueUtil.getBeiDanTransferId(issueNo, transferId, type);
						Log.fucaibiz.debug("104投注北单transferId=%s", transferId);
					}
					matchCompetiveMsg = TransactionProcessor.dynamicInvoke("lotteryIssue",
							"getMatchCompetiveByLotteryId", issueNo, transferId, lotteryId);
				}
				MatchCompetive matchCompetive = (MatchCompetive) matchCompetiveMsg.getObj();
				if (null == matchCompetive || "".equals(matchCompetive)) {
					checkValue = -1;
					break;
				}
				int type = matchCompetive.getMatchType();
				if (i == 0) {
					matchType = type;
				}
				// 判断赛事是否为同一类型
				if (matchType != type) {
					checkValue = -2;
					break;
				}
				String bettingDeadline = matchCompetive.getBettingDeadline();
				// 计算投注内容中最早截止的赛事时间
				if (DateUtil.compareDateTime(closeTime, bettingDeadline) < 0) {
					deadlineTime = bettingDeadline;
					closeTime = DateUtil.addDateMinut(bettingDeadline, "MINUTE", -5);
				}
				if (!lotteryId.equals(IssueConstant.SportLotteryType.JCLQ_HHGG.getValue())
						&& !lotteryId.equals(IssueConstant.SportLotteryType.JCZQ_HHGG.getValue())) {
					// 判断赛事状态(竞足竞篮赛事混合过关不判断状态)
					int matchStatus = 0;
					List<MatchCompetivePlay> playList = matchCompetive.getMatchCompetivePlayList();
					if (playList.size() != 1) {
						checkValue = -1;
					} else {
						MatchCompetivePlay matchCompetivePlay = playList.get(0);
						if (matchCompetivePlay.getLotteryId().equals(lotteryId)) {
							if (playTypeStr.equals("DANGUAN")) {
								matchStatus = matchCompetivePlay.getDgGdSaleStatus();
								if (matchStatus != SportIssueConstant.DgGdSaleStatus.IN_SALE.getValue()) {
									checkValue = -1;
									break;
								}
							} else {
								matchStatus = matchCompetivePlay.getGgSaleStatus();
								if (matchStatus != SportIssueConstant.GgSaleStatus.IN_SALE.getValue()) {
									checkValue = -1;
									break;
								}
							}
						}
					}
					if (checkValue == -1) {
						break;
					}
				}
				SportOrderDetail detail = new SportOrderDetail();
				detail.setOrderNo(orderNo);
				detail.setTransferId(sportDetail.getTransferId());
				detail.setOrderContent(sportDetail.getOrderContent());
				detail.setMatchNo(matchCompetive.getMatchNo());
				detail.setMatchId(matchCompetive.getMatchId());
				orderDetailList.add(detail);
			}
			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
			// 8.校验赛事信息是否在销售
			if (checkValue == -1 || DateUtil.compareDateTime(deadlineTime, currentTime) >= 0) {
				Log.fucaibiz.error("竞技彩,投注内容有赛事停止销售,partnerId=%s,ticketId=%s,orderContent=%s", partnerId,
						partnerTradeId, orderContent);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_STOP_SAIL, "销售期已过 停止销售");
			}
			if (checkValue == -2) {
				Log.fucaibiz.error("竞技彩,投注内容有赛事类型不一致,partnerId=%s,ticketId=%s,orderContent=%s", partnerId,
						partnerTradeId, orderContent);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR, "投注号码格式错误");
			}

			// 9.封装订单信息
			String planId = orderMsg.getPartnerId();
			String province = orderMsg.getProvince();
			String paySerialNumber = OrderConstant.ORDER_PAYSERIANUMBER_PREFIX + orderNo;
			SportOrder order = new SportOrder();
			order.setOrderNo(orderNo);
			order.setLotteryId(lotteryId);
			order.setPartnerId(partnerId);
			order.setUserId(userId);
			order.setIssueNo(issueNo);
			order.setOrderType(Order.OrderType.DIRECT_ORDER.getValue());
			order.setOrderStatus(Order.OrderStatus.WAIT_PAYMENT.getValue());
			order.setTotalAmount(orderMoney);
			order.setOrderContent(orderContent);
			order.setMultiple(multiple);
			order.setPlayType(playType);
			order.setPaySerialNumber(paySerialNumber);
			order.setTradeId(partnerTradeId);
			order.setRealName(realName);
			order.setCardNo(cardNo);
			order.setMobile(mobile);
			order.setTicketNum(ballNum);
			order.setPlanId(planId);
			order.setPrintProvince(province);
			order.setCloseTime(closeTime);
			order.setSportOrderDetailList(orderDetailList);

			int queueValue = BatchUtil.createSportOrderAddQueue(order);
			Log.fucaibiz.info("竞技彩,104放入批量创建订单队列,orderNo=%d,queueValue=%d", orderNo, queueValue);
			if (queueValue == BatchConstant.BATCH_ADD_QUEUE_FAILED) {
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}

			int operateValue = BatchUtil.createSportRoundResult(orderNo);

			String statusCode = "";
			String msg = "";
			if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
				statusCode = ConstantsUtil.STATUS_CODE_WAIT_TRADE;
				msg = "等待交易";
			} else if (operateValue == BatchConstant.BATCH_OPERATE_CREATE_REPEAT) {
				statusCode = ConstantsUtil.STATUS_CODE_REPEAT_ORDER;
				msg = "重复订单";
			} else {
				statusCode = ConstantsUtil.STATUS_CODE_SYSTEM_ERROR;
				msg = "系统错误";
			}
			Log.fucaibiz.info("订单入库完成,704返回,orderNo=%d,statusCode=%s,msg=%s", orderNo, statusCode, msg);
			response = responseBack704(orderNo, orderMsg, statusCode, msg);

			// 10.订单创建完成,放入支付线程池
			if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
				OrderPayUtil.submitSportOrderToPayThreadPool(order, isPartnerAccount);
			}
		} catch (Exception e) {
			Log.fucaibiz.info("竞技彩,104投注创建竞技彩发生异常", e);
			response = responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
		}
		return response;
	}

	/**
	 * 创建数字彩订单
	 * 
	 * @param orderMsg
	 * @return
	 */
	private TransResponse createDigitalOrder(OrderMsg orderMsg) {
		TransResponse response = null;
		String partnerTradeId = "";
		long orderNo = 0;
		try {
			String partnerId = orderMsg.getPartnerId();
			VoteTicket voteTicket = orderMsg.getTicketList().get(0);
			partnerTradeId = voteTicket.getTicketId();
			Log.fucaibiz.info("订单进入处理流程,ticketId=%s", partnerTradeId);

			long userId = orderMsg.getUserId();
			String realName = orderMsg.getRealName();
			String mobile = orderMsg.getPhone();
			String cardNo = orderMsg.getIdCard();
			String lotteryId = orderMsg.getLotteryId();

			long tempOrderNo = OrderMemcacheUtil.getOrderNoFromMemcache(partnerId, partnerTradeId);
			if (tempOrderNo > 0) {
				Log.fucaibiz.info("该订单在createOrderMap或memcache中已存在,则重复订单,orderNo=%d", orderNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_REPEAT_ORDER, "重复订单");
			}
			orderNo = OrderNoUtil.getOrderNo(partnerTradeId);

			if (orderNo <= 0) {
				Log.fucaibiz.error("orderNo生成失败,partnerId=%s,ticketId=%s,orderNo=%d", partnerId, partnerTradeId,
						orderNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}

			if (BatchUtil.isCreateMapExist(orderNo) || !OrderMemcacheUtil.verifyOrderIsNotInMemcache(orderNo)) {
				Log.fucaibiz.info("该订单在createOrderMap或memcache中已存在,则重复订单,orderNo=%d", orderNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_REPEAT_ORDER, "重复订单");
			}

			ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "findPartnerForCheck", partnerId);
			if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.error("校验渠道商类型发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId, partnerTradeId,
						partnerMsg.getMsg());
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR,
						"合作商id不存在或者参数与xml文件中的不一致");
			}
			LotteryPartner partner = (LotteryPartner) partnerMsg.getObj();
			boolean isPartnerAccount = partner.getPartnerType() == PartnerConstant.PartnerType.USERPARTNER.getValue() ? false
					: true;
			boolean checkUserId = true;
			Log.fucaibiz.debug("校验合作商类型,partnerId=%s,ticketId=%s,isPartner=%b", partnerId, partnerTradeId,
					isPartnerAccount);

			if (!isPartnerAccount) {
				ReturnMessage userMsg = TransactionProcessor.dynamicInvoke("userAccount", "checkUserExist", userId);
				if (!userMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.error("校验用户是否存在发生异常,partnerId=%s,userId=%d,ticketId=%s", partnerId, userId,
							partnerTradeId);
					return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
				} else {
					boolean userIsExist = (Boolean) userMsg.getObj();
					if (!userIsExist) {
						Log.fucaibiz.error("用户USERID不存在,partnerId=%s,orderUserId=%d,ticketId=%s", partnerId, userId,
								partnerTradeId);
						checkUserId = false;
					}
				}
			} else if (partner.getUserId() != userId) {
				Log.fucaibiz.error("渠道商USERID不一致,partnerId=%s,partnerUserId=%d,orderUserId=%d,ticketId=%s", partnerId,
						partner.getUserId(), userId, partnerTradeId);
				checkUserId = false;
			}
			if (!checkUserId) {
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_USERID_ERROR, "用户id错误");
			}
			String issueNo = voteTicket.getIssueNo();
			String playType = voteTicket.getPlayType();
			long orderMoney = MoneyUtil.toCent(Long.valueOf(voteTicket.getMoney()));
			String orderContent = voteTicket.getBall();
			int multiple = voteTicket.getMultiple();

			if (null == issueNo || "".equals(issueNo)) {
				Log.fucaibiz.info("期号不能为空,ticketId=%s,partnerId=%s,lotteryId=%s,issueNo=%s", partnerTradeId, partnerId,
						lotteryId, issueNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO, "不存在的期次");
			}

			Log.fucaibiz.info("订单处理中,ticketId=%s,partnerId=%s,isPartnerAccount=%b,lotteryId=%s,issueNo=%s",
					partnerTradeId, partnerId, isPartnerAccount, lotteryId, issueNo);

			String playTypeStr = OrderUtil.checkPlayType(lotteryId, playType);
			boolean ballNumFlag = true;
			int perBallNum = orderContent.split("#").length;
			if (!"SINGLE".equals(playTypeStr)) {
				if (perBallNum > 1) {
					ballNumFlag = false;
				}
			} else if (perBallNum > 5) {
				ballNumFlag = false;
			}
			if (!ballNumFlag) {
				Log.fucaibiz.error("超过最大注数,partnerId=%s,ticketId=%s,orderContent=%s", partnerId, partnerTradeId,
						orderContent);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_OVER_LARGESTNUM, "超过最大注数");
			}

			if (multiple > 99) {
				Log.fucaibiz.error("超过最大允许倍数,partnerId=%s,ticketId=%s,orderContent=%s,multiple=%d", partnerId,
						partnerTradeId, orderContent, multiple);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_OVER_MAXMULTIPLE, "超过最大允许倍数");
			}
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			TicketWinningServiceImpl ticketService = ctx.getBean("ticketWinningServiceImpl",
					TicketWinningServiceImpl.class);

			BallCountReturnMessage ticketReturn = null;
			try {
				ticketReturn = ticketService.calBallCount(lotteryId, playType, orderContent);
			} catch (Exception e) {
				Log.fucaibiz.error("投注内容拆单发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId, partnerTradeId, e);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}
			if (!ticketReturn.getStatusCode().equals(ConstantsUtil.STATUS_CODE_SUCCESS)) {
				Log.fucaibiz.error("投注号码格式错误,partnerId=%s,ticketId=%s,orderContent=%s", partnerId, partnerTradeId,
						orderContent);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR, "投注号码格式错误");
			}
			int ballNum = ticketReturn.getCount();
			int perTicketMoney = 2;

			if (lotteryId.equals(IssueConstant.LOTTERYID_DLT)
					&& (playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_DANSHI_ZHUIJIA)
							|| playType.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_ZHUIJIA) || playType
								.equals(LotteryPlayTypeConstants.PLAYTYPE_DLT_FUSHI_DANTUO_ZHUIJIA))) {
				perTicketMoney = 3;
			}

			long total = MoneyUtil.toCent(ballNum * multiple * perTicketMoney);
			if (orderMoney != total || multiple <= 0) {
				Log.fucaibiz.error("投注金额与拆单后计算的金额不一致,partnerId=%s,ticketId=%s,orderMoney=%d,realMoney=%d,multiple=%d",
						partnerId, partnerTradeId, orderMoney, total, multiple);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_BETTINGAMOUNT_ERROR, "投注金额错误");
			}

			ReturnMessage issueMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId,
					issueNo);
			LotteryIssue issue = (LotteryIssue) issueMsg.getObj();
			if (null == issue || "".equals(issue)) {
				Log.fucaibiz.info("期次不存在,ticketId=%s,partnerId=%s,lotteryId=%s,issueNo=%s", partnerTradeId, partnerId,
						lotteryId, issueNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO, "不存在的期次");
			}
			String beginTime = issue.getBeginTime();
			String endTime = "SINGLE".equals(playTypeStr) ? issue.getSingleEndTime() : issue.getCompoundEndTime();
			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
			if (DateUtil.compareDateTime(currentTime, beginTime) > 0) {
				Log.fucaibiz.error("投注期号不在销售时间范围内,该期还未开始销售,partnerId=%s,ticketId=%s,lotteryId=%s,issueNo=%s",
						partnerId, partnerTradeId, lotteryId, issueNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_ISSUE_ERROR, "期次错误");
			} else if (DateUtil.compareDateTime(endTime, currentTime) > 0) {
				Log.fucaibiz.error("投注期号不在销售时间范围内,销售期已过 停止销售,partnerId=%s,ticketId=%s,lotteryId=%s,issueNo=%s",
						partnerId, partnerTradeId, lotteryId, issueNo);
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_STOP_SAIL, "销售期已过 停止销售");
			}

			String paySerialNumber = OrderConstant.ORDER_PAYSERIANUMBER_PREFIX + orderNo;

			Order order = new Order();
			order.setLotteryId(lotteryId);
			order.setPartnerId(partnerId);
			order.setUserId(userId);
			order.setIssueNo(issueNo);
			order.setOrderNo(orderNo);
			order.setOrderType(Order.OrderType.DIRECT_ORDER.getValue());
			order.setOrderStatus(Order.OrderStatus.WAIT_PAYMENT.getValue());
			order.setTotalAmount(orderMoney);
			order.setOrderContent(orderContent);
			order.setMultiple(multiple);
			order.setPlayType(playType);
			order.setRealName(realName);
			order.setCardNo(cardNo);
			order.setMobile(mobile);
			order.setPaySerialNumber(paySerialNumber);
			order.setTradeId(partnerTradeId);
			order.setTicketNum(ballNum);

			int queueValue = BatchUtil.createOrderAddQueue(order);

			Log.fucaibiz.info("104放入批量创建订单队列,orderNo=%d,queueValue=%d", orderNo, queueValue);

			if (queueValue == BatchConstant.BATCH_ADD_QUEUE_FAILED) {
				return responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
			}

			int operateValue = BatchUtil.createRoundResult(orderNo);

			String statusCode = "";
			String msg = "";
			if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
				statusCode = ConstantsUtil.STATUS_CODE_WAIT_TRADE;
				msg = "等待交易";
			} else if (operateValue == BatchConstant.BATCH_OPERATE_CREATE_REPEAT) {
				statusCode = ConstantsUtil.STATUS_CODE_REPEAT_ORDER;
				msg = "重复订单";
			} else {
				statusCode = ConstantsUtil.STATUS_CODE_SYSTEM_ERROR;
				msg = "系统错误";
			}
			Log.fucaibiz.info("订单入库完成,704返回,orderNo=%d,statusCode=%s,msg=%s", orderNo, statusCode, msg);

			response = responseBack704(orderNo, orderMsg, statusCode, msg);

			if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
				String printBeginTime = issue.getPrintBeginTime();
				String printEndTime = issue.getPrintEndTime();
				OrderPayUtil.submitOrderToPayThreadPool(order, isPartnerAccount, printBeginTime, printEndTime);
			}
		} catch (Exception e) {
			Log.fucaibiz.info("104投注创建数字彩发生异常", e);
			response = responseBack704(orderNo, orderMsg, ConstantsUtil.STATUS_CODE_SYSTEM_ERROR, "系统错误");
		} finally {
			BatchConstant.createOrderMap.remove(orderNo);
		}
		return response;
	}

	/**
	 * 104投注接口的直投返回接口(704)
	 * 
	 * @param orderNo
	 * @param orderMsg
	 * @param statusCode
	 * @param msg
	 * @return
	 */
	private TransResponse responseBack704(long orderNo, OrderMsg orderMsg, String statusCode, String msg) {
		TransResponse response = new TransResponse();
		try {
			com.cqfc.xmlparser.transactionmsg704.Msg msg704 = new com.cqfc.xmlparser.transactionmsg704.Msg();
			com.cqfc.xmlparser.transactionmsg704.Headtype headtype = new com.cqfc.xmlparser.transactionmsg704.Headtype();
			String partnerId = orderMsg.getPartnerId();
			headtype.setPartnerid(partnerId);
			headtype.setTranscode("704");
			headtype.setVersion(orderMsg.getVersion());
			headtype.setTime(DateUtil.getCurrentDateTime());
			msg704.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg704.Body body = new com.cqfc.xmlparser.transactionmsg704.Body();
			com.cqfc.xmlparser.transactionmsg704.Ticketordertype ticketordertype704 = new com.cqfc.xmlparser.transactionmsg704.Ticketordertype();
			com.cqfc.xmlparser.transactionmsg704.Tickets tickets = new com.cqfc.xmlparser.transactionmsg704.Tickets();

			List<com.cqfc.xmlparser.transactionmsg704.Ticket> ticketList = tickets.getTicket();
			com.cqfc.xmlparser.transactionmsg704.Ticket ticket = new com.cqfc.xmlparser.transactionmsg704.Ticket();
			VoteTicket voteTicket = orderMsg.getTicketList().get(0);
			String ticketId = voteTicket.getTicketId();
			ticket.setId(ticketId);
			ticket.setIssue(voteTicket.getIssueNo());
			ticket.setMoney(String.valueOf(voteTicket.getMoney()));
			ticket.setMultiple(String.valueOf(voteTicket.getMultiple()));
			ticket.setPlaytype(voteTicket.getPlayType());
			ticket.setMsg(msg);
			ticket.setStatuscode(statusCode);
			ticket.setPalmid(orderNo + "");
			ticketList.add(ticket);
			Log.fucaibiz.info("704 response,partnerId=%s,ticketId=%s,statusCode=%s,msg=%s", partnerId, ticketId,
					statusCode, msg);
			ticketordertype704.setTickets(tickets);

			ticketordertype704.setGameid(orderMsg.getLotteryId());
			ticketordertype704.setTicketsnum(String.valueOf(orderMsg.getTicketsNum()));
			ticketordertype704.setTotalmoney(String.valueOf(orderMsg.getTotalMoney()));

			body.setTicketorder(ticketordertype704);
			msg704.setBody(body);

			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setData(TransactionMsgLoader704.msgToXml(msg704));

			response.setResponseTransCode("704");
		} catch (Exception e) {
			Log.fucaibiz.error("投注订单拼接704返回值发生异常", e);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setResponseTransCode("704");
		}
		return response;
	}

	@Override
	public TransResponse findOrderProcess(Message message) throws TException {
		return TransactionMsgProcessor105.process(message);
	}

	@Override
	public TransResponse batchFindOrderProcess(Message message) throws TException {
		return TransactionMsgProcessor205.process(message);
	}

	/**
	 * 竞技彩订单出票回调接口
	 * 
	 * @param sportPrint
	 * @return
	 * @throws TException
	 */
	@Override
	public boolean sportOrderAfterPrint(SportPrint sportPrint) throws TException {
		// TODO Auto-generated method stub
		try {
			sportPrint.setPrintTime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date()));
			long orderNo = sportPrint.getOrderNo();
			// 1失败 2成功 3出票中 4订单不存在
			int printStatus = sportPrint.getPrintStatus();

			Log.fucaibiz.info("竞技彩,出票回调,orderNo=%d,resultValue=%d(1失败 2成功 3出票中 4订单不存在)", orderNo, printStatus);
			if (printStatus == TicketIssueConstant.SEND_TICKET_RESULT_PROCESSING) {
				return false;
			}
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			OrderService orderService = ctx.getBean("orderService", OrderService.class);

			SportOrder order = orderService.findSportOrderByOrderNo(orderNo);
			if (null == order || "".equals(order)) {
				Log.fucaibiz.error("竞技彩,出票回调,查询订单信息不存在,orderNo=%d", orderNo);
				return false;
			}
			String ticketId = order.getTradeId();
			String partnerId = order.getPartnerId();
			int currentStatus = order.getOrderStatus();
			String lotteryId = order.getLotteryId();

			// 出票回调返回订单不存在,再次请求出票
			if (printStatus == TicketIssueConstant.SEND_TICKET_RESULT_NOEXIST_ORDER) {
				sportPrintAgainAfterCallback(order);
				return false;
			}
			if (printStatus != TicketIssueConstant.SEND_TICKET_RESULT_FAIL
					&& printStatus != TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS) {
				return false;
			}
			int orderStatus = printStatus == TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS ? SportOrder.OrderStatus.HASTICKET_WAITLOTTERY
					.getValue() : SportOrder.OrderStatus.DRAWER_FAILURE.getValue();

			if (orderStatus <= currentStatus) {
				Log.fucaibiz.info("竞技彩,出票回调订单状态小于等于当前订单状态,orderNo=%d,currentStatus=%d,updateStatus=%d", orderNo,
						currentStatus, orderStatus);
				return false;
			}

			// 校验更新订单状态map是否存在
			boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, orderStatus, ticketId);
			Log.fucaibiz.info("竞技彩,出票回调,判断批更新MAP中是否存在,orderNo=%d,orderStatus=%d,isExist=%b", orderNo, orderStatus,
					mapIsExist);

			if (!mapIsExist) {
				// 加入更新订单状态批处理队列
				int queueValue = 0;
				int lotteryType = OrderUtil.getJcCategoryDetail(lotteryId);

				if (orderStatus == SportOrder.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
						&& (lotteryType == OrderStatus.LotteryType.JJZC_GAME.getType() || lotteryType == OrderStatus.LotteryType.JJLC_GAME
								.getType())) {
					UpdateSportOrderStatus updateObj = new UpdateSportOrderStatus();
					updateObj.setOrderNo(orderNo);
					updateObj.setOrderStatus(orderStatus);
					updateObj.setPrintTime(sportPrint.getPrintTime());
					updateObj.setTicketNo(sportPrint.getTicketNo());
					updateObj.setIsBackReq(false);
					updateObj.setTradeId(ticketId);
					updateObj.setLotteryType(lotteryType);
					updateObj.setMatchList(sportPrint.getMatchList());
					queueValue = BatchUtil.updateSportStatusTicketAddQueue(updateObj);
				} else {
					queueValue = BatchUtil.updateSportStatusAddQueue(orderNo, orderStatus, ticketId);
				}

				int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
				if (queueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
					Log.fucaibiz.info("竞技彩,出票回调加入更新订单状态队列成功,orderNo=%d,orderStatus=%d", orderNo, orderStatus);
					// 轮询更新结果
					operateValue = BatchUtil.sportStatusRoundResult(orderNo, orderStatus);
				} else {
					Log.fucaibiz.info("竞技彩,出票回调加入更新订单状态队列失败,orderNo=%d,orderStatus=%d,returnValue=%d", orderNo,
							orderStatus, operateValue);
					return false;
				}
				Log.fucaibiz.info("出票回调更新订单状态,orderNo=%d,orderStatus=%d,operateValue=%d", orderNo, orderStatus,
						operateValue);

				if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
					order.setOrderStatus(orderStatus);

					ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount",
							partnerId);
					boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

					if (printStatus == TicketIssueConstant.SEND_TICKET_RESULT_FAIL) {
						// 回调返回出票失败,更新订单信息
						failTicketSportUpdateOrder(order, isPartnerAccount);
					}

					int finalStatus = order.getOrderStatus();
					if (finalStatus == Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
							|| finalStatus == Order.OrderStatus.REFUND_SUCCESS.getValue()
							|| finalStatus == Order.OrderStatus.ORDER_CANCEL.getValue()) {

						if (finalStatus == Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
								&& (lotteryType == OrderStatus.LotteryType.JJZC_GAME.getType() || lotteryType == OrderStatus.LotteryType.JJLC_GAME
										.getType())) {
							String ticketNo = sportPrint.getTicketNo();
							String printTime = sportPrint.getPrintTime();
							order.setTicketNo(ticketNo);
							order.setPrintTime(printTime);
							for (SportOrderDetail detail : order.getSportOrderDetailList()) {
								String transferId = detail.getTransferId();
								PrintMatch match = null;
								for (PrintMatch printMatch : sportPrint.getMatchList()) {
									if (transferId.equals(printMatch.getTransferId())) {
										match = printMatch;
									}
								}
								if (null != match) {
									detail.setSp(match.getSp());
									detail.setRq(match.getRq());
								}
							}
						}
						// 订单状态完结,同步订单
						int syncValue = OrderOperateUtil.syncSportToGlobal(order, isPartnerAccount);
						Log.fucaibiz.info("竞技彩,同步订单完成,orderNo=%d,syncValue=%d(0未同步 1成功 2失败)", orderNo, syncValue);

						boolean isSyncMapExist = BatchUtil.isSyncSportMapExist(orderNo, syncValue, ticketId);
						Log.fucaibiz.info("竞技彩,批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b",
								orderNo, syncValue, isSyncMapExist);
						if (!isSyncMapExist) {
							int syncQueueValue = BatchUtil.syncSportAddQueue(orderNo, syncValue, ticketId);
							if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
								int syncIsSuccess = BatchUtil.syncSportRoundResult(orderNo);
								if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
									order.setIsSyncSuccess(syncValue);
									Log.fucaibiz.debug("竞技彩,通知合作商处理结果,orderNo=%d,orderStatus=%d", orderNo, finalStatus);
									OrderOperateUtil.notifySportPartner(order);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,出票回调处理发生异常,orderNo=" + sportPrint.getOrderNo(), e);
			return false;
		}
		return true;
	}

	/**
	 * 竞技彩出票回调返回出票中,再次请求出票
	 * 
	 * @param order
	 */
	private void sportPrintAgainAfterCallback(SportOrder order) {
		long orderNo = order.getOrderNo();
		try {
			String ticketId = order.getTradeId();
			Log.fucaibiz.info("竞技彩,出票回调返回订单不存在,再次请求出票.orderNo=%d", orderNo);
			ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
					EnsapOutTicketChekOrder.ensapSportOutTicketOrder(order));
			if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("竞技彩,再次请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
				return;
			}
			ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
			if (sendTicket.getStatusCode() == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
				Log.fucaibiz.info("竞技彩,再次请求出票提交到线程池成功,orderNo=%d", orderNo);
				if (order.getOrderStatus() == Order.OrderStatus.HAS_PAYMENT.getValue()) {
					// 当前订单状态为'已付款',则更新订单状态为'出票中'
					int inTicket = Order.OrderStatus.IN_TICKET.getValue();
					boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, inTicket, ticketId);
					if (!mapIsExist) {
						int queueValue = BatchUtil.updateSportStatusAddQueue(orderNo, inTicket, ticketId);
						Log.fucaibiz.info("竞技彩,回调,更新订单为'出票中'加入队列,orderNo=%d,queueValue=%d(1成功 2失败)", orderNo,
								queueValue);
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,出票回调返回订单不存在,再次请求出票发生异常,ordeNo=" + orderNo, e);
		}
	}

	/**
	 * 竞技彩出票失败处理订单信息
	 * 
	 * @param order
	 * @param isPartnerAccount
	 */
	private void failTicketSportUpdateOrder(SportOrder order, boolean isPartnerAccount) {
		long orderNo = order.getOrderNo();
		try {
			int orderType = order.getOrderType();
			long userId = order.getUserId();
			long totalMoney = order.getTotalAmount();
			String ticketId = order.getTradeId();
			String partnerId = order.getPartnerId();
			String paySerialNumber = order.getPaySerialNumber();
			String refundSerialNumber = OrderConstant.ORDER_REFUNDSERIANUMBER_PREFIX + orderNo;

			// 退款
			ReturnMessage refundMsg = null;
			if (isPartnerAccount) {
				refundMsg = TransactionProcessor.dynamicInvoke("partnerAccount", "modifyRefund", partnerId, totalMoney,
						paySerialNumber, refundSerialNumber);
			} else {
				if (orderType == SportOrder.OrderType.AFTER_ORDER.getValue()) {
					ReturnMessage appendMsg = TransactionProcessor.dynamicInvoke("appendTask",
							"getRefundSerialNumberByOrderNo", orderNo, String.valueOf(userId));
					String freezeSerialNumber = (String) appendMsg.getObj();
					refundSerialNumber = OrderConstant.APPEND_REFUNDFREEZESERIANUMBER_PREFIX + orderNo;

					refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "refundFreezeMoney", userId,
							totalMoney, freezeSerialNumber, refundSerialNumber);
				} else {
					refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "modifyRefund", userId,
							paySerialNumber, refundSerialNumber, totalMoney);
				}
			}
			int refundValue = (Integer) refundMsg.getObj();
			Log.fucaibiz.info("竞技彩,出票失败,退款完成,orderNo=%d,refundValue=%d", orderNo, refundValue);

			if (refundValue > 0 || refundValue == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
				// 更新订单状态'退款成功'.
				int refundStatus = SportOrder.OrderStatus.REFUND_SUCCESS.getValue();
				boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, refundStatus, ticketId);
				Log.fucaibiz.info("竞技彩,出票回调,更新订单状态'退款成功',批更新MAP,orderNo=%d,isExist=%b", orderNo, mapIsExist);

				if (!mapIsExist) {
					int refundQueueValue = BatchUtil.updateSportStatusAddQueue(orderNo, refundStatus, ticketId);

					int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
					if (refundQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						Log.fucaibiz.info("竞技彩,出票回调加入更新订单状态队列成功,orderNo=%d,orderStatus=%d", orderNo, refundStatus);
						operateValue = BatchUtil.sportStatusRoundResult(orderNo, refundStatus);
					}
					Log.fucaibiz.info("竞技彩,出票失败退款成功,更新订单状态,orderNo=%d,orderStatus=%d,returnValue=%d", orderNo,
							refundStatus, operateValue);
					if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
						order.setOrderStatus(SportOrder.OrderStatus.REFUND_SUCCESS.getValue());
					}
				}
			}
		} catch (Exception e) {
			Log.error("竞技彩,出票回调,出票失败更新订单信息发生异常,orderNo=" + orderNo, e);
			Log.fucaibiz.error("竞技彩,出票回调,出票失败更新订单信息发生异常,orderNo=" + orderNo, e);
		}
	}
}
