package com.cqfc.order.task;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;

import com.cqfc.businesscontroller.util.OrderOperateUtil;
import com.cqfc.order.OrderService;
import com.cqfc.order.model.Order;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.OrderDynamicUtil;
import com.cqfc.order.util.ScanUtil;
import com.cqfc.order.util.TaskDbGenerator;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.util.CancelOrderConstant;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.TicketIssueConstant;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * lottery_issue触发,出票(请求出票/查询出票)、撤单、同步订单
 * 
 * @author liwh
 */
public class OrderStatusTask {

	public static void taskProcess(String dbName, int type, String lotteryId, String issueNo,
			AtomicBoolean atomicBoolean) {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		OrderService orderService = ctx.getBean("orderService", OrderService.class);

		try {
			for (int i = 0; i < OrderConstant.PER_DATASOURCE_TABLE_NUM; i++) {
				if (!atomicBoolean.get()) {
					break;
				}
				if (type == OrderConstant.ORDER_PRINT_CHECK) {
					String mapKey = ScanUtil.getBreakMapKey(type, lotteryId, issueNo);
					if (ScanUtil.printScanMap.get(mapKey)) {
						Log.fucaibiz.debug("出票结束时间已到,主动中止正在出票的扫描线程,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
						atomicBoolean.set(false);
						break;
					}
				}
				String tableName = TaskDbGenerator.getTableName(i);
				int currentPage = 1;
				int pageSize = 2000;
				while (true) {
					TaskDbGenerator.setDynamicDataSource(OrderDynamicUtil.SLAVE, dbName);
					List<Order> dealList = orderService.getOrderList(tableName, lotteryId, issueNo, type, currentPage,
							pageSize);
					int size = dealList.size();
					if (null == dealList || "".equals(dealList) || size < 1) {
						break;
					} else {
						ScanLog.scan.debug("订单扫描deal,type=%d,lotteryId=%s,issueNo=%s,dbName=%s,tableName=%s,size=%d",
								type, lotteryId, issueNo, dbName, tableName, size);
						operateTryAgain(dealList, type, atomicBoolean);
					}
					currentPage++;
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("订单扫描发生异常,dbName=%s,scanType=%d,lotteryId=%s,issueNo=%s,errorMsg=%s", dbName, type,
					lotteryId, issueNo, e);
			if (type == OrderConstant.ORDER_CANCEL_CHECK) {
				atomicBoolean.set(false);
			}
		}
	}

	private static void operateTryAgain(List<Order> dealList, int type, AtomicBoolean atomicBoolean) {
		if (type == OrderConstant.ORDER_PRINT_CHECK) {
			printTryAgain(dealList);
		}
		if (type == OrderConstant.ORDER_CANCEL_CHECK) {
			cancelOrderAfterPrintEnd(dealList, atomicBoolean);
		}
		if (type == OrderConstant.ORDER_SYNC_CHECK) {
			syncOrderToGlobalDataBase(dealList, atomicBoolean);
		}
	}

	/**
	 * 订单出票扫描（订单状态：已付款、出票中）
	 * 
	 * @param dealOrderList
	 */
	private static void printTryAgain(List<Order> dealOrderList) {
		try {
			for (Order order : dealOrderList) {
				long orderNo = order.getOrderNo();
				int orderStatus = order.getOrderStatus();

				if (orderStatus == Order.OrderStatus.HAS_PAYMENT.getValue()) {
					Log.fucaibiz.info("扫描出票,订单状态为已付款,再次请求出票.orderNo=%d", orderNo);
					ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
							EnsapOutTicketChekOrder.ensapOutTicketOrder(order));
					if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.info("扫描出票,再次请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
						continue;
					}
					ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
					if (sendTicket.getStatusCode() == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
						Log.fucaibiz.info("扫描出票,再次请求出票提交到线程池成功,orderNo=%d", orderNo);
						int inTicket = Order.OrderStatus.IN_TICKET.getValue();
						String ticketId = order.getTradeId();
						// 判断map中是否存在更新状态出票中.
						boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, inTicket, ticketId);
						if (!mapIsExist) {
							int queueValue = BatchUtil.updateStatusAddQueue(orderNo, inTicket, ticketId);
							Log.fucaibiz.info("扫描出票,更新订单为'出票中'加入队列,orderNo=%d,queueValue=%d(1成功 2失败)", orderNo,
									queueValue);
						}
					}
				}

				if (orderStatus == Order.OrderStatus.IN_TICKET.getValue()) {
					Log.fucaibiz.info("扫描出票,查询出票,orderNo=%d", orderNo);
					ReturnMessage ticketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "checkTicket",
							EnsapOutTicketChekOrder.ensapOutTicketOrder(order));
					if (!ticketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.info("订单扫描任务,查询出票情况发生异常,orderNo=%d,errorMsg=%s", orderNo, ticketMsg.getMsg());
						continue;
					}
					ResultMessage queryTicket = (ResultMessage) ticketMsg.getObj();
					int queryValue = queryTicket.getStatusCode();
					Log.fucaibiz.info("查询出票请求已发送,orderNo=%d,returnValue=%d(1 成功 0 失败)", orderNo, queryValue);
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("business订单扫描再次发起出票请求（订单状态：已付款、出票中）发生异常", e);
			Log.error("business订单扫描再次发起出票请求（订单状态：已付款、出票中）发生异常", e);
		}
	}

	/**
	 * 撤单(超过出票截止时间后,未付款、已付款、出票中、出票失败的订单都做撤单处理)
	 * 
	 * @param dealList
	 */
	private static void cancelOrderAfterPrintEnd(List<Order> dealList, AtomicBoolean atomicBoolean) {
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			OrderService orderService = ctx.getBean("orderService", OrderService.class);
			for (Order order : dealList) {
				long orderNo = order.getOrderNo();
				long totalMoney = order.getTotalAmount();
				long userId = order.getUserId();
				String paySerialNumber = order.getPaySerialNumber();
				String partnerId = order.getPartnerId();
				int orderType = order.getOrderType();
				int orderStatus = order.getOrderStatus();
				String ticketId = order.getTradeId();

				Log.fucaibiz.info("订单进入撤单队列,orderNo=%d", orderNo);

				int updateStatus = orderStatus == Order.OrderStatus.WAIT_PAYMENT.getValue() ? Order.OrderStatus.ORDER_CANCEL
						.getValue() : Order.OrderStatus.REFUND_SUCCESS.getValue();

				// 更新订单状态退款成功
				boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, updateStatus, ticketId);
				Log.fucaibiz.info("撤单更新订单状态,校验MAP,orderNo=%d,orderStatus=%d,isExist=%b", orderNo, updateStatus,
						mapIsExist);

				if (!mapIsExist) {
					int queueValue = BatchUtil.updateStatusAddQueue(orderNo, updateStatus, ticketId);
					// 更新订单状态的值
					int operateValue = BatchConstant.BATCH_OPERATE_FAILED;

					if (queueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						// 轮循等待更新结果
						operateValue = BatchUtil.statusRoundResult(orderNo, updateStatus);
					} else {
						int tempValue = orderService.updateOrderStatus(orderNo, updateStatus);
						operateValue = tempValue == 1 ? BatchConstant.BATCH_OPERATE_SUCCESS
								: BatchConstant.BATCH_OPERATE_FAILED;
						Log.fucaibiz.info("撤单加入更新订单状态队列失败,直接调用更新接口,orderNo=%d,orderStatus=%d,returnValue=%d", orderNo,
								updateStatus, operateValue);
					}

					Log.fucaibiz.info("撤单操作,更新订单状态结束,orderNo=%d,updateStatus=%d,returnValue=%d", orderNo, updateStatus,
							operateValue);

					if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
						// 撤单订单转移,并主动通知合作商
						int createCancelValue = createCancelOrder(order);
						Log.fucaibiz.info("撤单操作转移订单完成,orderNo=%d,createCancelValue=%d", orderNo, createCancelValue);
						if (createCancelValue != 1) {
							atomicBoolean.set(false);
							break;
						}
						order.setOrderStatus(updateStatus);
						OrderOperateUtil.notifyPartner(order);

						if (orderStatus != Order.OrderStatus.WAIT_PAYMENT.getValue()) {
							// 退款
							ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner",
									"isPartnerAccount", partnerId);
							if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
								Log.fucaibiz.debug("撤单操作校验渠道商类型发生异常,orderNo=%d,partnerId=%s,errorMsg=%s", orderNo,
										partnerId, partnerMsg.getMsg());
							} else {
								boolean isPartnerAccount = (Boolean) partnerMsg.getObj();
								int isRefundSuccess = refundOperate(orderNo, partnerId, userId, paySerialNumber,
										totalMoney, orderType, isPartnerAccount);
								Log.fucaibiz.info("撤单操作退款结束,orderNo=%d,returnValue=%d", orderNo, isRefundSuccess);

							}
						}
					} else {
						atomicBoolean.set(false);
						break;
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("business订单扫描撤单(超过出票截止时间后,已付款、出票中、出票失败的订单都做撤单处理)发生异常", e);
			Log.error("business订单扫描撤单(超过出票截止时间后,已付款、出票中、出票失败的订单都做撤单处理)发生异常", e);
			atomicBoolean.set(false);
		}
	}

	/**
	 * 期号状态变成已撤单后,将同步到全局数据库失败的订单进行再次同步
	 * 
	 * @param dealOrderList
	 */
	private static void syncOrderToGlobalDataBase(List<Order> dealOrderList, AtomicBoolean atomicBoolean) {
		try {
			for (Order order : dealOrderList) {
				String partnerId = order.getPartnerId();
				long orderNo = order.getOrderNo();
				int orderStatus = order.getOrderStatus();
				ScanLog.scan.debug("issue scan sync order,orderNo=%d", orderNo);
				// 校验合作商类型
				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					ScanLog.scan.error("同步订单时,校验渠道商类型发生异常,partnerId=%s,errorMsg=%s", partnerId, partnerMsg.getMsg());
				} else {
					boolean isPartnerAccount = (Boolean) partnerMsg.getObj();
					// 同步订单数据到全局数据库
					int syncValue = OrderOperateUtil.syncToGlobal(order, isPartnerAccount);
					if (syncValue != Order.OrderSync.SYNC_SUCCESS.getValue()) {
						ScanLog.scan.debug("issueScan order atomicBoolean fasle,orderNo=%d,syncValue=%d", orderNo,
								syncValue);
						atomicBoolean.set(false);
					} else {
						ScanLog.scan.debug("同步订单完成,orderNo=%d,syncValue=%d(0未同步 1成功 2失败)", orderNo, syncValue);
						// 批量更新订单同步状态.
						String ticketId = order.getTradeId();
						boolean isSyncMapExist = BatchUtil.isSyncMapExist(orderNo, syncValue, ticketId);
						Log.fucaibiz.info("同步扫描,批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b",
								orderNo, syncValue, isSyncMapExist);
						if (!isSyncMapExist) {
							int syncQueueValue = BatchUtil.syncAddQueue(orderNo, syncValue, ticketId);
							if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
								// 轮循等待更新同步结果
								int syncIsSuccess = BatchUtil.syncRoundResult(orderNo);
								if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
									order.setIsSyncSuccess(syncValue);
									ScanLog.scan.debug("通知合作商,orderNo=%d,orderStatus=%d", orderNo, orderStatus);
									OrderOperateUtil.notifyPartner(order);
								}
								ScanLog.scan.debug("issueScan同步订单完成,orderNo=%d,syncValue=%d,orderSyncValue=%d",
										orderNo, syncValue, syncIsSuccess);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("business订单扫描 撤单期号状态变成已撤单后,将同步到全局数据库失败的订单进行再次同步 发生异常", e);
		}
	}

	/**
	 * 订单退款操作
	 * 
	 * @param orderNo
	 * @param userId
	 * @param paySerialNumber
	 * @param totalMoney
	 * @param orderType
	 * @param isPartnerAccount
	 * @return
	 */
	private static int refundOperate(long orderNo, String partnerId, long userId, String paySerialNumber,
			long totalMoney, int orderType, boolean isPartnerAccount) {
		int isRefundSuccess = 0;
		try {
			String refundSerialNumber = OrderConstant.ORDER_REFUNDSERIANUMBER_PREFIX + orderNo;
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
			isRefundSuccess = (Integer) refundMsg.getObj();
		} catch (Exception e) {
			ScanLog.scan.error("退款操作发生异常,orderNo=%d,errorMsg=%s", orderNo, e);
			return 0;
		}
		return isRefundSuccess;
	}

	/**
	 * 创建撤单订单
	 * 
	 * @param order
	 * @return
	 */
	private static int createCancelOrder(Order order) {
		int isSuccess = 0;
		try {
			int orderStatus = order.getOrderStatus();
			int outTicketStatus = orderStatus == Order.OrderStatus.WAIT_PAYMENT.getValue() ? CancelOrderConstant.OutTicketStatus.WAIT_PAYMENT
					.getValue() : CancelOrderConstant.OutTicketStatus.IN_TICKET.getValue();
			long orderNo = order.getOrderNo();
			CancelOrder cancelOrder = new CancelOrder();
			cancelOrder.setOrderNo(String.valueOf(orderNo));
			cancelOrder.setPartnerId(order.getPartnerId());
			cancelOrder.setUserId(order.getUserId());
			cancelOrder.setLotteryId(order.getLotteryId());
			cancelOrder.setIssueNo(order.getIssueNo());
			cancelOrder.setOutTicketStatus(outTicketStatus);
			cancelOrder.setTotalAmount(order.getTotalAmount());
			cancelOrder.setOrderContent(order.getOrderContent());
			cancelOrder.setPlayType(order.getPlayType());
			cancelOrder.setMultiple(order.getMultiple());

			ReturnMessage cancelMsg = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_CANCEL_ORDER,
					"createCancelOrder", cancelOrder);
			isSuccess = (Integer) cancelMsg.getObj();
			ScanLog.scan.info("撤单后创建撤单信息,orderNo=%d,returnValue=%d", orderNo, isSuccess);
		} catch (Exception e) {
			ScanLog.scan.error("撤单后创建撤单信息发生异常,orderNo=%d,errorMsg=%s", order.getOrderNo(), e);
			return 0;
		}
		return isSuccess;
	}

}
