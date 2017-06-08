package com.cqfc.order.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.businesscontroller.util.OrderOperateUtil;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.order.service.IOrderService;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.OrderDynamicUtil;
import com.cqfc.order.util.OrderPayUtil;
import com.cqfc.order.util.TaskDbGenerator;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.cancelorder.SportCancelOrderDetail;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.util.CancelOrderConstant;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.TicketIssueConstant;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * 竞技彩支付、同步订单、退款、出票
 * 
 * @author liwh
 */
public class SportOrderScanTask {

	public static void taskProcess(String dbName, int type) {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		IOrderService orderService = ctx.getBean("orderServiceImpl", IOrderService.class);
		try {
			for (int i = 0; i < OrderConstant.PER_DATASOURCE_TABLE_NUM; i++) {
				String mainTableName = TaskDbGenerator.getSportMainTableName(i);
				String detailTableName = TaskDbGenerator.getSportDetailTableName(i);
				int currentPage = 1;
				int pageSize = 2000;
				while (true) {
					TaskDbGenerator.setDynamicDataSource(OrderDynamicUtil.SLAVE, dbName);
					List<SportOrder> dealList = orderService.getSportOrderListScan(mainTableName, detailTableName,
							type, currentPage, pageSize);
					if (null != dealList && dealList.size() > 0) {
						operateTryAgain(dealList, type);
						Log.run.debug("竞技彩扫描,type=%d,dbName=%s,mainTableName=%s,listSize=%d", type, dbName,
								mainTableName, dealList.size());
					} else {
						break;
					}
					currentPage++;
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("竞技彩,订单扫描发生异常,dbName=" + dbName + ",type=" + type, e);
		}
	}

	private static void operateTryAgain(List<SportOrder> dealOrderList, int type) {
		if (type == OrderConstant.ORDER_PAYMENT_CHECK) {
			payMentTryAgain(dealOrderList);
		}
		if (type == OrderConstant.ORDER_PRINT_CHECK) {
			printTryAgain(dealOrderList);
		}
		if (type == OrderConstant.ORDER_CANCEL_CHECK) {
			cancelSportOrder(dealOrderList);
		}
		if (type == OrderConstant.ORDER_SYNC_CHECK) {
			syncOrderToGlobalDataBase(dealOrderList);
		}
		if (type == OrderConstant.ORDER_REFUND_CHECK) {
			refundOrder(dealOrderList);
		}
	}

	/**
	 * 订单出票扫描（订单状态：已付款、出票中）
	 * 
	 * @param dealOrderList
	 */
	private static void printTryAgain(List<SportOrder> dealOrderList) {
		try {
			for (SportOrder order : dealOrderList) {
				long orderNo = order.getOrderNo();
				int orderStatus = order.getOrderStatus();

				if (orderStatus == SportOrder.OrderStatus.HAS_PAYMENT.getValue()) {
					Log.fucaibiz.info("竞技彩扫描出票,订单状态为已付款,再次请求出票.orderNo=%d", orderNo);
					ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "sendTicket",
							EnsapOutTicketChekOrder.ensapSportOutTicketOrder(order));
					if (!sendTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.info("竞技彩扫描出票,再次请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo, sendTicketMsg.getMsg());
						continue;
					}
					ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
					if (sendTicket.getStatusCode() == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
						Log.fucaibiz.info("竞技彩扫描出票,再次请求出票提交到线程池成功,orderNo=%d", orderNo);
						int inTicket = Order.OrderStatus.IN_TICKET.getValue();
						String ticketId = order.getTradeId();
						// 判断map中是否存在更新状态出票中.
						boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, inTicket, ticketId);
						if (!mapIsExist) {
							int queueValue = BatchUtil.updateSportStatusAddQueue(orderNo, inTicket, ticketId);
							Log.fucaibiz.info("竞技彩扫描出票,更新订单为'出票中'加入队列,orderNo=%d,queueValue=%d(1成功 2失败)", orderNo,
									queueValue);
						}
					}
				}

				if (orderStatus == Order.OrderStatus.IN_TICKET.getValue()) {
					Log.fucaibiz.info("竞技彩扫描出票,查询出票,orderNo=%d", orderNo);
					ReturnMessage ticketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "checkTicket",
							EnsapOutTicketChekOrder.ensapSportOutTicketOrder(order));
					if (!ticketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.info("竞技彩订单扫描任务,查询出票情况发生异常,orderNo=%d,errorMsg=%s", orderNo, ticketMsg.getMsg());
						continue;
					}
					ResultMessage queryTicket = (ResultMessage) ticketMsg.getObj();
					int queryValue = queryTicket.getStatusCode();
					Log.fucaibiz.info("竞技彩查询出票请求已发送,orderNo=%d,returnValue=%d(1 成功 0 失败)", orderNo, queryValue);
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩订单扫描再次发起出票请求（订单状态：已付款、出票中）发生异常", e);
			Log.error("竞技彩订单扫描再次发起出票请求（订单状态：已付款、出票中）发生异常", e);
		}
	}

	/**
	 * 再次发起扣款请求（订单状态：未付款）
	 * 
	 * @param dealOrderList
	 */
	private static void payMentTryAgain(List<SportOrder> dealOrderList) {
		try {
			for (SportOrder order : dealOrderList) {
				long orderNo = order.getOrderNo();
				String partnerId = order.getPartnerId();

				Log.fucaibiz.debug("竞技彩,扫描未支付订单到支付线程池,orderNo=%d", orderNo);

				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

				OrderPayUtil.submitSportOrderToPayThreadPool(order, isPartnerAccount);
			}
		} catch (Exception e) {
			ScanLog.scan.error("竞技彩,订单扫描再次发起扣款请求（订单状态：未付款）发生异常", e);
		}
	}

	/**
	 * 订单同步扫描
	 * 
	 * @param dealOrderList
	 */
	private static void syncOrderToGlobalDataBase(List<SportOrder> dealOrderList) {
		try {
			for (SportOrder order : dealOrderList) {
				String partnerId = order.getPartnerId();
				long orderNo = order.getOrderNo();
				int orderStatus = order.getOrderStatus();

				if (BatchConstant.sportOrderSyncMap.containsKey(orderNo)) {
					continue;
				}

				// 校验合作商类型
				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					ScanLog.scan.debug("竞技彩同步订单时,校验渠道商类型发生异常,partnerId=%s,errorMsg=%s", partnerId, partnerMsg.getMsg());
					continue;
				}
				boolean isPartnerAccount = (Boolean) partnerMsg.getObj();
				// 同步订单数据到全局数据库
				int syncValue = OrderOperateUtil.syncSportToGlobal(order, isPartnerAccount);
				if (syncValue == SportOrder.OrderSync.SYNC_SUCCESS.getValue()) {
					ScanLog.scan.debug("竞技彩,同步订单完成,orderNo=%d,syncValue=%d(0未同步 1成功 2失败)", orderNo, syncValue);
					// 批量更新订单同步状态.
					String ticketId = order.getTradeId();

					boolean isSyncMapExist = BatchUtil.isSyncSportMapExist(orderNo, syncValue, ticketId);
					Log.fucaibiz.info("竞技彩,同步扫描,批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b",
							orderNo, syncValue, isSyncMapExist);
					if (isSyncMapExist) {
						continue;
					}

					int syncQueueValue = BatchUtil.syncSportAddQueue(orderNo, syncValue, ticketId);
					if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						// 轮循等待更新同步结果
						int syncIsSuccess = BatchUtil.syncSportRoundResult(orderNo);
						ScanLog.scan.debug("竞技彩,扫描同步订单完成,orderNo=%d,syncValue=%d,orderSyncValue=%d", orderNo,
								syncValue, syncIsSuccess);
						if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
							order.setIsSyncSuccess(syncValue);
							ScanLog.scan.debug("竞技彩,通知合作商,orderNo=%d,orderStatus=%d", orderNo, orderStatus);
							OrderOperateUtil.notifySportPartner(order);
						}
					}
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("竞技彩,扫描将同步到全局数据库失败的订单进行再次同步发生异常", e);
		}
	}

	/**
	 * 订单退款扫描
	 * 
	 * @param dealOrderList
	 */
	private static void refundOrder(List<SportOrder> dealOrderList) {
		try {
			for (SportOrder order : dealOrderList) {
				String partnerId = order.getPartnerId();
				long orderNo = order.getOrderNo();
				String paySerialNumber = order.getPaySerialNumber();
				long totalMoney = order.getTotalAmount();
				long userId = order.getUserId();
				int orderType = order.getOrderType();
				String ticketId = order.getTradeId();

				int updateStatus = SportOrder.OrderStatus.REFUND_SUCCESS.getValue();

				// 更新订单状态退款成功
				boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, updateStatus, ticketId);
				Log.fucaibiz.info("竞技彩订单退款扫描,校验MAP,orderNo=%d,orderStatus=%d,isExist=%b", orderNo, updateStatus,
						mapIsExist);
				if (mapIsExist) {
					continue;
				}

				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.debug("竞技彩订单退款时,校验渠道商类型发生异常,渠道ID：" + partnerId + ",异常信息:" + partnerMsg.getMsg());
					continue;
				}
				boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

				int isRefundSuccess = refundOperate(orderNo, partnerId, userId, paySerialNumber, totalMoney, orderType,
						isPartnerAccount);

				Log.fucaibiz.debug("竞技彩退款扫描对订单进行退款操作完成,orderNo=%d,returnValue=%d", orderNo, isRefundSuccess);

				if (isRefundSuccess > 0 || isRefundSuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
					int queueValue = BatchUtil.updateSportStatusAddQueue(orderNo, updateStatus, ticketId);
					// 更新订单状态的值
					int operateValue = BatchConstant.BATCH_OPERATE_FAILED;

					if (queueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						// 轮询等待更新结果
						operateValue = BatchUtil.sportStatusRoundResult(orderNo, updateStatus);
					}

					Log.fucaibiz.info("竞技彩退款扫描,更新订单状态结束,orderNo=%d,updateStatus=%d,returnValue=%d", orderNo,
							updateStatus, operateValue);

					if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
						// 更新订单状态'退款成功',进行同步订单操作.
						order.setOrderStatus(Order.OrderStatus.REFUND_SUCCESS.getValue());
						int syncValue = OrderOperateUtil.syncSportToGlobal(order, isPartnerAccount);
						// 批量更新订单同步状态.
						boolean isSyncMapExist = BatchUtil.isSyncSportMapExist(orderNo, syncValue, ticketId);
						Log.fucaibiz.info("竞技彩退款扫描,批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b",
								orderNo, syncValue, isSyncMapExist);
						if (!isSyncMapExist) {
							int syncQueueValue = BatchUtil.syncSportAddQueue(orderNo, syncValue, ticketId);
							if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
								// 轮询等待更新同步结果
								int syncIsSuccess = BatchUtil.syncSportRoundResult(orderNo);
								if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
									order.setIsSyncSuccess(syncValue);
									Log.fucaibiz.debug("竞技彩通知合作商,orderNo=%d,orderStatus=%d", orderNo,
											order.getOrderStatus());
									OrderOperateUtil.notifySportPartner(order);
								}
								Log.fucaibiz.debug("竞技彩同步订单扫描完成,orderNo=%d,syncValue=%d,orderSyncValue=%d", orderNo,
										syncValue, syncIsSuccess);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("business订单退款发生异常", e);
		}
	}

	/**
	 * 订单退款操作
	 * 
	 * @param orderNo
	 * @param partnerId
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
				refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "modifyRefund", userId, paySerialNumber,
						refundSerialNumber, totalMoney);
			}
			isRefundSuccess = (Integer) refundMsg.getObj();
		} catch (Exception e) {
			ScanLog.scan.error("退款操作发生异常,orderNo=" + orderNo, e);
			return 0;
		}
		return isRefundSuccess;
	}

	/**
	 * 到订单的出票截止时间还未出票的订单,进入撤单流程
	 * 
	 * @param dealList
	 */
	private static void cancelSportOrder(List<SportOrder> dealList) {
		try {
			for (SportOrder order : dealList) {
				long orderNo = order.getOrderNo();
				long totalMoney = order.getTotalAmount();
				long userId = order.getUserId();
				String paySerialNumber = order.getPaySerialNumber();
				String partnerId = order.getPartnerId();
				int orderType = order.getOrderType();
				int orderStatus = order.getOrderStatus();
				String ticketId = order.getTradeId();

				Log.fucaibiz.info("竞技彩订单进入撤单队列,orderNo=%d", orderNo);

				int updateStatus = orderStatus == SportOrder.OrderStatus.WAIT_PAYMENT.getValue() ? SportOrder.OrderStatus.ORDER_CANCEL
						.getValue() : SportOrder.OrderStatus.REFUND_SUCCESS.getValue();

				// 更新订单状态退款成功
				boolean mapIsExist = BatchUtil.isSportStatusMapExist(orderNo, updateStatus, ticketId);
				Log.fucaibiz.info("竞技彩撤单更新订单状态,校验MAP,orderNo=%d,orderStatus=%d,isExist=%b", orderNo, updateStatus,
						mapIsExist);

				if (!mapIsExist) {
					int queueValue = BatchUtil.updateSportStatusAddQueue(orderNo, updateStatus, ticketId);
					// 更新订单状态的值
					int operateValue = BatchConstant.BATCH_OPERATE_FAILED;

					if (queueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						// 轮询等待更新结果
						operateValue = BatchUtil.sportStatusRoundResult(orderNo, updateStatus);
					}

					Log.fucaibiz.info("竞技彩撤单操作,更新订单状态结束,orderNo=%d,updateStatus=%d,returnValue=%d", orderNo,
							updateStatus, operateValue);

					if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
						// 撤单订单转移,并主动通知合作商
						int createCancelValue = createSportCancelOrder(order);
						Log.fucaibiz.info("竞技彩撤单操作转移订单完成,orderNo=%d,createCancelValue=%d", orderNo, createCancelValue);
						order.setOrderStatus(updateStatus);
						OrderOperateUtil.notifySportPartner(order);

						if (orderStatus != SportOrder.OrderStatus.WAIT_PAYMENT.getValue()) {
							// 退款
							ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner",
									"isPartnerAccount", partnerId);
							if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
								Log.fucaibiz.debug("竞技彩撤单操作校验渠道商类型发生异常,orderNo=%d,partnerId=%s,errorMsg=%s", orderNo,
										partnerId, partnerMsg.getMsg());
							} else {
								boolean isPartnerAccount = (Boolean) partnerMsg.getObj();
								int isRefundSuccess = refundOperate(orderNo, partnerId, userId, paySerialNumber,
										totalMoney, orderType, isPartnerAccount);
								Log.fucaibiz.info("竞技彩撤单操作退款结束,orderNo=%d,returnValue=%d", orderNo, isRefundSuccess);

							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩订单扫描撤单发生异常", e);
			Log.error("竞技彩订单扫描撤单发生异常", e);
		}
	}

	/**
	 * 竞技彩创建转移订单
	 * 
	 * @param order
	 * @return
	 */
	private static int createSportCancelOrder(SportOrder order) {
		int isSuccess = 0;
		try {
			int orderStatus = order.getOrderStatus();
			int outTicketStatus = orderStatus == SportOrder.OrderStatus.WAIT_PAYMENT.getValue() ? CancelOrderConstant.OutTicketStatus.WAIT_PAYMENT
					.getValue() : CancelOrderConstant.OutTicketStatus.IN_TICKET.getValue();
			long orderNo = order.getOrderNo();
			SportCancelOrder cancelOrder = new SportCancelOrder();
			cancelOrder.setOrderNo(orderNo);
			cancelOrder.setPartnerId(order.getPartnerId());
			cancelOrder.setUserId(order.getUserId());
			cancelOrder.setLotteryId(order.getLotteryId());
			cancelOrder.setIssueNo(order.getIssueNo());
			cancelOrder.setOutTicketStatus(outTicketStatus);
			cancelOrder.setTotalAmount(order.getTotalAmount());
			cancelOrder.setOrderContent(order.getOrderContent());
			cancelOrder.setPlayType(order.getPlayType());
			cancelOrder.setMultiple(order.getMultiple());
			cancelOrder.setTradeId(order.getTradeId());
			cancelOrder.setPlanId(order.getPlanId());
			cancelOrder.setPrintProvince(order.getPrintProvince());

			List<SportCancelOrderDetail> detailList = new ArrayList<SportCancelOrderDetail>();
			for (SportOrderDetail detail : order.getSportOrderDetailList()) {
				SportCancelOrderDetail cancelDetail = new SportCancelOrderDetail();
				cancelDetail.setOrderNo(orderNo);
				cancelDetail.setMatchId(detail.getMatchId());
				cancelDetail.setTransferId(detail.getTransferId());
				cancelDetail.setMatchNo(detail.getMatchNo());
				cancelDetail.setOrderContent(detail.getOrderContent());
				cancelDetail.setMatchStatus(detail.getMatchStatus());
				detailList.add(cancelDetail);
			}
			cancelOrder.setDetailList(detailList);

			ReturnMessage cancelMsg = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_CANCEL_ORDER,
					"createSportCancelOrder", cancelOrder);
			isSuccess = (Integer) cancelMsg.getObj();
			Log.fucaibiz.info("竞技彩撤单后创建撤单信息,orderNo=%d,returnValue=%d", orderNo, isSuccess);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩撤单后创建撤单信息发生异常,orderNo=%d,errorMsg=%s", order.getOrderNo(), e);
			return 0;
		}
		return isSuccess;
	}

}
