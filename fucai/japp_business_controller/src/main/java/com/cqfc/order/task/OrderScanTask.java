package com.cqfc.order.task;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.businesscontroller.util.OrderOperateUtil;
import com.cqfc.order.OrderService;
import com.cqfc.order.model.Order;
import com.cqfc.order.service.IOrderService;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.order.util.OrderDynamicUtil;
import com.cqfc.order.util.OrderPayUtil;
import com.cqfc.order.util.TaskDbGenerator;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * 支付、同步订单、退款
 * 
 * @author liwh
 */
public class OrderScanTask {

	public static void taskProcess(String dbName, int type) {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		IOrderService orderService = ctx.getBean("orderServiceImpl", IOrderService.class);
		try {
			for (int i = 0; i < OrderConstant.PER_DATASOURCE_TABLE_NUM; i++) {
				String tableName = TaskDbGenerator.getTableName(i);
				int currentPage = 1;
				int pageSize = 2000;
				while (true) {
					TaskDbGenerator.setDynamicDataSource(OrderDynamicUtil.SLAVE, dbName);
					List<Order> dealList = orderService.getOrderListScan(tableName, type, currentPage, pageSize);
					if (null == dealList || "".equals(dealList) || dealList.size() < 1) {
						break;
					} else {
						operateTryAgain(dealList, type);
					}
					currentPage++;
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("订单扫描发生异常,dbName=" + dbName + ",type=" + type, e);
		}
	}

	private static void operateTryAgain(List<Order> dealOrderList, int type) {
		if (type == OrderConstant.ORDER_PAYMENT_CHECK) {
			payMentTryAgain(dealOrderList);
		}
		if (type == OrderConstant.ORDER_SYNC_CHECK) {
			syncOrderToGlobalDataBase(dealOrderList);
		}
		if (type == OrderConstant.ORDER_REFUND_CHECK) {
			refundOrder(dealOrderList);
		}
	}

	/**
	 * 再次发起扣款请求（订单状态：未付款）
	 * 
	 * @param dealOrderList
	 */
	private static void payMentTryAgain(List<Order> dealOrderList) {
		try {
			for (Order order : dealOrderList) {
				long orderNo = order.getOrderNo();
				String partnerId = order.getPartnerId();
				String lotteryId = order.getLotteryId();
				String issueNo = order.getIssueNo();

				Log.fucaibiz.debug("扫描未支付订单到支付线程池,orderNo=%d", orderNo);

				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

				ReturnMessage issueMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue",
						lotteryId, issueNo);
				LotteryIssue issue = (LotteryIssue) issueMsg.getObj();
				String printBeginTime = issue.getPrintBeginTime();
				String printEndTime = issue.getPrintEndTime();
				OrderPayUtil.submitOrderToPayThreadPool(order, isPartnerAccount, printBeginTime, printEndTime);
			}
		} catch (Exception e) {
			ScanLog.scan.error("business订单扫描再次发起扣款请求（订单状态：未付款）发生异常", e);
		}
	}

	/**
	 * 订单同步扫描
	 * 
	 * @param dealOrderList
	 */
	private static void syncOrderToGlobalDataBase(List<Order> dealOrderList) {
		try {
			for (Order order : dealOrderList) {
				String partnerId = order.getPartnerId();
				long orderNo = order.getOrderNo();
				int orderStatus = order.getOrderStatus();

				if (BatchConstant.orderSyncMap.containsKey(orderNo)) {
					continue;
				}

				// 校验合作商类型
				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					ScanLog.scan.debug("同步订单时,校验渠道商类型发生异常,partnerId=%s,errorMsg=%s", partnerId, partnerMsg.getMsg());
					continue;
				}
				boolean isPartnerAccount = (Boolean) partnerMsg.getObj();
				// 同步订单数据到全局数据库
				int syncValue = OrderOperateUtil.syncToGlobal(order, isPartnerAccount);
				if (syncValue == Order.OrderSync.SYNC_SUCCESS.getValue()) {
					ScanLog.scan.debug("同步订单完成,orderNo=%d,syncValue=%d(0未同步 1成功 2失败)", orderNo, syncValue);
					// 批量更新订单同步状态.
					String ticketId = order.getTradeId();

					boolean isSyncMapExist = BatchUtil.isSyncMapExist(orderNo, syncValue, ticketId);
					Log.fucaibiz.info("同步扫描,批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b",
							orderNo, syncValue, isSyncMapExist);
					if (isSyncMapExist) {
						continue;
					}

					int syncQueueValue = BatchUtil.syncAddQueue(orderNo, syncValue, ticketId);
					if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
						// 轮循等待更新同步结果
						int syncIsSuccess = BatchUtil.syncRoundResult(orderNo);
						ScanLog.scan.debug("issueScan同步订单完成,orderNo=%d,syncValue=%d,orderSyncValue=%d", orderNo,
								syncValue, syncIsSuccess);
						if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
							order.setIsSyncSuccess(syncValue);
							ScanLog.scan.debug("通知合作商,orderNo=%d,orderStatus=%d", orderNo, orderStatus);
							OrderOperateUtil.notifyPartner(order);
						}
					}
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("business订单扫描撤单期号状态变成已撤单后,将同步到全局数据库失败的订单进行再次同步发生异常", e);
		}
	}

	/**
	 * 订单退款扫描
	 * 
	 * @param dealOrderList
	 */
	private static void refundOrder(List<Order> dealOrderList) {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		OrderService orderService = ctx.getBean("orderService", OrderService.class);

		try {
			for (Order order : dealOrderList) {
				String partnerId = order.getPartnerId();
				long orderNo = order.getOrderNo();
				String paySerialNumber = order.getPaySerialNumber();
				long totalMoney = order.getTotalAmount();
				long userId = order.getUserId();
				int orderType = order.getOrderType();

				int updateStatus = Order.OrderStatus.REFUND_SUCCESS.getValue();

				// 更新订单状态退款成功
				String statusMapKey = BatchUtil.getOrderStatusMapKey(orderNo, updateStatus);
				boolean isStatusMap = BatchConstant.orderStatusMap.containsKey(statusMapKey);
				Log.fucaibiz.info("订单退款扫描,校验MAP,orderNo=%d,orderStatus=%d,isExist=%b", orderNo, updateStatus,
						isStatusMap);
				if (isStatusMap) {
					continue;
				}

				ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
				if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.debug("订单退款时,校验渠道商类型发生异常,渠道ID：" + partnerId + ",异常信息:" + partnerMsg.getMsg());
					continue;
				}
				boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

				int isRefundSuccess = refundOperate(orderNo, partnerId, userId, paySerialNumber, totalMoney, orderType,
						isPartnerAccount);

				Log.fucaibiz.debug("退款扫描对订单进行退款操作完成,orderNo=%d,returnValue=%d", orderNo, isRefundSuccess);

				if (isRefundSuccess > 0 || isRefundSuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
					// 批量更新订单状态'退款成功'
					String ticketId = order.getTradeId();
					boolean mapIsExist = BatchUtil.isStatusMapExist(orderNo, updateStatus, ticketId);
					Log.fucaibiz.info("退款扫描,批更新'退款成功',map是否存在,orderNo=%d,isExist=%b", orderNo, mapIsExist);
					if (mapIsExist) {
						continue;
					}
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
						Log.fucaibiz.info("退款扫描,更新订单状态加队列失败,直接调用更新接口,orderNo=%d,orderStatus=%d,returnValue=%d",
								orderNo, updateStatus, operateValue);
					}

					Log.fucaibiz.info("退款扫描,更新订单状态结束,orderNo=%d,updateStatus=%d,returnValue=%d", orderNo, updateStatus,
							operateValue);

					if (operateValue == BatchConstant.BATCH_OPERATE_SUCCESS) {
						// 更新订单状态'退款成功',进行同步订单操作.
						order.setOrderStatus(Order.OrderStatus.REFUND_SUCCESS.getValue());
						int syncValue = OrderOperateUtil.syncToGlobal(order, isPartnerAccount);
						// 批量更新订单同步状态.
						boolean isSyncMapExist = BatchUtil.isSyncMapExist(orderNo, syncValue, ticketId);
						Log.fucaibiz.info("退款扫描,批更新订单同步状态map是否存在,orderNo=%d,syncValue=%d(0未同步 1成功 2失败),isExist=%b",
								orderNo, syncValue, isSyncMapExist);
						if (!isSyncMapExist) {
							int syncQueueValue = BatchUtil.syncAddQueue(orderNo, syncValue, ticketId);
							if (syncQueueValue == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
								// 轮循等待更新同步结果
								int syncIsSuccess = BatchUtil.syncRoundResult(orderNo);
								if (syncIsSuccess == BatchConstant.BATCH_OPERATE_SUCCESS) {
									order.setIsSyncSuccess(syncValue);
									Log.fucaibiz.debug("通知合作商,orderNo=%d,orderStatus=%d", orderNo,
											order.getOrderStatus());
									OrderOperateUtil.notifyPartner(order);
								}
								Log.fucaibiz.debug("issueScan同步订单完成,orderNo=%d,syncValue=%d,orderSyncValue=%d",
										orderNo, syncValue, syncIsSuccess);
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
				if (orderType == Order.OrderType.AFTER_ORDER.getValue()) {
					ScanLog.scan.debug("追号订单退款开始,orderNo=" + orderNo + ",userId=" + userId);
					ReturnMessage appendMsg = TransactionProcessor.dynamicInvoke("appendTask",
							"getRefundSerialNumberByOrderNo", orderNo, String.valueOf(userId));
					String freezeSerialNumber = (String) appendMsg.getObj();
					refundSerialNumber = OrderConstant.APPEND_REFUNDFREEZESERIANUMBER_PREFIX + orderNo;

					ScanLog.scan.debug("追号订单,freezeSerialNumber=" + freezeSerialNumber + ",orderNo=" + orderNo
							+ ",status=" + appendMsg.getStatusCode());
					refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "refundFreezeMoney", userId,
							totalMoney, freezeSerialNumber, refundSerialNumber);
				} else {
					refundMsg = TransactionProcessor.dynamicInvoke("userAccount", "modifyRefund", userId,
							paySerialNumber, refundSerialNumber, totalMoney);
				}
			}
			isRefundSuccess = (Integer) refundMsg.getObj();
		} catch (Exception e) {
			ScanLog.scan.error("退款操作发生异常,orderNo=" + orderNo, e);
			return 0;
		}
		return isRefundSuccess;
	}

}
