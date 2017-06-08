package com.cqfc.order.util;

import com.cqfc.order.datacenter.OrderBuffer;
import com.cqfc.order.datacenter.SportOrderBuffer;
import com.cqfc.order.model.CreateOrderMsg;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.UpdateOrderStatus;
import com.cqfc.order.model.UpdateSportOrderStatus;
import com.cqfc.order.model.UpdateSyncStatus;
import com.cqfc.util.OrderConstant;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class BatchUtil {

	public static int getDbIndex(String tradeId) {
		return Integer.valueOf(Math.abs(tradeId.hashCode() / OrderConstant.PER_DATASOURCE_TABLE_NUM
				% OrderConstant.DATASOURCE_NUM));
	}

	public static String getMasterDbBean(int index) {
		return OrderDynamicUtil.BATCH_MASTER_BEAN + "0" + index;
	}

	public static String getOrderStatusMapKey(long orderNo, int orderStatus) {
		return orderNo + "@" + orderStatus;
	}

	public static int createOrderAddQueue(Order order) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		long orderNo = order.getOrderNo();
		try {
			if (OrderBuffer.addOrderToListQueue(order) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("创建订单加入队列异常,orderNo=" + orderNo, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.createOrderMap.remove(orderNo);
			}
		}
		return operateValue;
	}

	public static int createRoundResult(long orderNo) {
		int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
		// long beginTime = System.currentTimeMillis();
		try {
			if (BatchConstant.createOrderMap.containsKey(orderNo)) {
				CreateOrderMsg orderMsg = BatchConstant.createOrderMap.get(orderNo);
				synchronized (orderMsg) {
					orderMsg.wait(2000);
					if (!orderMsg.getIsBackReq()) {
						orderMsg.wait();
					}
				}
				if (orderMsg.getIsBackReq()) {
					operateValue = orderMsg.getOperateIdentifier();
				}
			}
			// long endTime = System.currentTimeMillis();
			// Log.fucaibiz.info("create round time,orderNo=%d,time=%d",
			// orderNo, (endTime - beginTime));
		} catch (Exception e) {
			Log.fucaibiz.error("轮循创建订单批处理返回结果发生异常,orderNo=" + orderNo, e);
		} finally {
			BatchConstant.createOrderMap.remove(orderNo);
		}
		return operateValue;
	}

	public static int updateStatusAddQueue(long orderNo, int orderStatus, String ticketId) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		String statusMapKey = getOrderStatusMapKey(orderNo, orderStatus);
		try {
			UpdateOrderStatus statusObj = createStatusMapObj(orderNo, orderStatus, ticketId);
			if (OrderBuffer.addOrderToStatusQueue(statusObj) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("更新订单状态加入队列异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.orderStatusMap.remove(statusMapKey);
			}
		}
		return operateValue;
	}

	public static int statusRoundResult(long orderNo, int orderStatus) {
		// long beginTime = System.currentTimeMillis();
		int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
		String statusMapKey = getOrderStatusMapKey(orderNo, orderStatus);
		try {
			if (BatchConstant.orderStatusMap.containsKey(statusMapKey)) {
				UpdateOrderStatus statusMsg = BatchConstant.orderStatusMap.get(statusMapKey);
				synchronized (statusMsg) {
					statusMsg.wait(2000);
					if (!statusMsg.getIsBackReq()) {
						statusMsg.wait();
					}
				}
				if (statusMsg.getIsBackReq()) {
					operateValue = statusMsg.getOperateIdentifier();
				}
			}
			// long endTime = System.currentTimeMillis();
			// Log.fucaibiz.info("update round time,orderNo=%d,status=%d,time=%d",
			// orderNo, orderStatus,
			// (endTime - beginTime));
		} catch (Exception e) {
			Log.fucaibiz.error("轮循订单状态批处理返回结果发生异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		} finally {
			BatchConstant.orderStatusMap.remove(statusMapKey);
		}
		return operateValue;
	}

	public static int syncAddQueue(long orderNo, int sync, String ticketId) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			UpdateSyncStatus syncObj = createSyncMapObj(orderNo, sync, ticketId);
			if (OrderBuffer.addOrderToSyncQueue(syncObj) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("更新订单同步状态加入队列异常,orderNo=" + orderNo + ",sync=" + sync, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.orderSyncMap.remove(orderNo);
			}
		}
		return operateValue;
	}

	public static int syncRoundResult(long orderNo) {
		int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
		// long beginTime = System.currentTimeMillis();
		try {
			if (BatchConstant.orderSyncMap.containsKey(orderNo)) {
				UpdateSyncStatus syncMsg = BatchConstant.orderSyncMap.get(orderNo);
				synchronized (syncMsg) {
					syncMsg.wait(2000);
					if (!syncMsg.getIsBackReq()) {
						syncMsg.wait();
					}
				}
				if (syncMsg.getIsBackReq()) {
					operateValue = syncMsg.getOperateIdentifier();
				}
			}
			// long endTime = System.currentTimeMillis();
			// Log.fucaibiz.info("sync round time,orderNo=%d,time=%d",
			// orderNo,(endTime - beginTime));
		} catch (Exception e) {
			Log.fucaibiz.error("轮循订单同步状态批处理返回结果发生异常,orderNo=" + orderNo, e);
		} finally {
			BatchConstant.orderSyncMap.remove(orderNo);
		}
		return operateValue;
	}

	public static boolean isStatusMapExist(long orderNo, int orderStatus, String ticketId) {
		boolean returnValue = true;
		try {
			String mapKey = BatchUtil.getOrderStatusMapKey(orderNo, orderStatus);
			UpdateOrderStatus statusObj = BatchUtil.createStatusMapObj(orderNo, orderStatus, ticketId);
			UpdateOrderStatus obj = BatchConstant.orderStatusMap.putIfAbsent(mapKey, statusObj);
			if (null == obj) {
				// 返回null,说明map中不存在,但是此时已经将值放入map中.
				returnValue = false;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("判断订单状态map是否存在发生异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		}
		return returnValue;
	}

	public static boolean isCreateMapExist(long orderNo) {
		boolean returnValue = true;
		try {
			CreateOrderMsg createOrderMsg = new CreateOrderMsg();
			createOrderMsg.setIsBackReq(false);
			CreateOrderMsg obj = BatchConstant.createOrderMap.putIfAbsent(orderNo, createOrderMsg);
			if (null == obj) {
				// 返回null,说明map中不存在,但是此时已经将值放入map中.
				returnValue = false;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("判断订单创建map是否存在,发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	public static boolean isSyncMapExist(long orderNo, int sync, String ticketId) {
		boolean returnValue = true;
		try {
			UpdateSyncStatus syncObj = createSyncMapObj(orderNo, sync, ticketId);
			UpdateSyncStatus obj = BatchConstant.orderSyncMap.putIfAbsent(orderNo, syncObj);
			if (null == obj) {
				// 返回null,说明map中不存在,但是此时已经将值放入map中.
				returnValue = false;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("判断订单同步map是否存在,发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	private static UpdateOrderStatus createStatusMapObj(long orderNo, int orderStatus, String ticketId) {
		UpdateOrderStatus updateOrderStatus = new UpdateOrderStatus();
		try {
			updateOrderStatus.setOrderNo(orderNo);
			updateOrderStatus.setOrderStatus(orderStatus);
			updateOrderStatus.setTradeId(ticketId);
			updateOrderStatus.setIsBackReq(false);
		} catch (Exception e) {
			Log.fucaibiz.error("更新订单状态封装MAP的VALUE值发生异常,orderNo=" + orderNo, e);
		}
		return updateOrderStatus;
	}

	private static UpdateSyncStatus createSyncMapObj(long orderNo, int sync, String ticketId) {
		UpdateSyncStatus updateSyncStatus = new UpdateSyncStatus();
		try {
			updateSyncStatus.setOrderNo(orderNo);
			updateSyncStatus.setSync(sync);
			updateSyncStatus.setTradeId(ticketId);
			updateSyncStatus.setIsBackReq(false);
		} catch (Exception e) {
			Log.fucaibiz.error("更新同步状态封装MAP的VALUE值发生异常,orderNo=" + orderNo, e);
		}
		return updateSyncStatus;
	}

	/**
	 * 获取竞技彩DB bean
	 * 
	 * @param index
	 * @return
	 */
	public static String getSportMasterDbBean(int index) {
		return OrderDynamicUtil.BATCH_COMPETITION_MASTER_BEAN + "0" + index;
	}

	/**
	 * 判断竞技彩批创建map是否存在该订单号
	 * 
	 * @param orderNo
	 * @return
	 */
	public static boolean isSportCreateMapExist(long orderNo) {
		boolean returnValue = true;
		try {
			CreateOrderMsg createOrderMsg = new CreateOrderMsg();
			createOrderMsg.setIsBackReq(false);
			CreateOrderMsg obj = BatchConstant.createSportOrderMap.putIfAbsent(orderNo, createOrderMsg);
			if (null == obj) {
				// 返回null,说明map中不存在,但是此时已经将值放入map中.
				returnValue = false;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩判断订单创建map是否存在,发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	/**
	 * 竞技彩创建订单加入批队列
	 * 
	 * @param order
	 * @return
	 */
	public static int createSportOrderAddQueue(SportOrder order) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		long orderNo = order.getOrderNo();
		try {
			if (SportOrderBuffer.addOrderToListQueue(order) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,创建订单加入队列异常,orderNo=" + orderNo, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.createSportOrderMap.remove(orderNo);
			}
		}
		return operateValue;
	}

	/**
	 * 竞技彩订单创建结果轮询
	 * 
	 * @param orderNo
	 * @return
	 */
	public static int createSportRoundResult(long orderNo) {
		int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
		try {
			if (BatchConstant.createSportOrderMap.containsKey(orderNo)) {
				CreateOrderMsg orderMsg = BatchConstant.createSportOrderMap.get(orderNo);
				synchronized (orderMsg) {
					orderMsg.wait(2000);
					if (!orderMsg.getIsBackReq()) {
						orderMsg.wait();
					}
				}
				if (orderMsg.getIsBackReq()) {
					operateValue = orderMsg.getOperateIdentifier();
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,轮询创建订单批处理返回结果发生异常,orderNo=" + orderNo, e);
		} finally {
			BatchConstant.createSportOrderMap.remove(orderNo);
		}
		return operateValue;
	}

	/**
	 * 竞技彩状态批修改map判断
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @param ticketId
	 * @return
	 */
	public static boolean isSportStatusMapExist(long orderNo, int orderStatus, String ticketId) {
		boolean returnValue = true;
		try {
			String mapKey = getOrderStatusMapKey(orderNo, orderStatus);
			UpdateSportOrderStatus statusObj = createSportStatusMapObj(orderNo, orderStatus, ticketId);
			UpdateSportOrderStatus obj = BatchConstant.sportOrderStatusMap.putIfAbsent(mapKey, statusObj);
			if (null == obj) {
				// 返回null,说明map中不存在,但是此时已经将值放入map中.
				returnValue = false;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("判断订单状态map是否存在发生异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		}
		return returnValue;
	}

	/**
	 * 封装竞技彩订单状态批修改对象
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @param ticketId
	 * @return
	 */
	private static UpdateSportOrderStatus createSportStatusMapObj(long orderNo, int orderStatus, String ticketId) {
		UpdateSportOrderStatus updateStatus = new UpdateSportOrderStatus();
		try {
			updateStatus.setOrderNo(orderNo);
			updateStatus.setOrderStatus(orderStatus);
			updateStatus.setTradeId(ticketId);
			updateStatus.setIsBackReq(false);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,更新订单状态封装MAP的VALUE值发生异常,orderNo=" + orderNo, e);
		}
		return updateStatus;
	}

	/**
	 * 批更新竞技彩状态加入队列
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @param ticketId
	 * @return
	 */
	public static int updateSportStatusAddQueue(long orderNo, int orderStatus, String ticketId) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		String statusMapKey = getOrderStatusMapKey(orderNo, orderStatus);
		try {
			UpdateSportOrderStatus statusObj = createSportStatusMapObj(orderNo, orderStatus, ticketId);
			if (SportOrderBuffer.addOrderToSportStatusQueue(statusObj) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,更新订单状态加入队列异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.sportOrderStatusMap.remove(statusMapKey);
			}
		}
		return operateValue;
	}

	/**
	 * 竞技彩,出票成功加入批更新队列
	 * 
	 * @param updateObj
	 * @return
	 */
	public static int updateSportStatusTicketAddQueue(UpdateSportOrderStatus updateObj) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		long orderNo = updateObj.getOrderNo();
		int orderStatus = updateObj.getOrderStatus();
		String statusMapKey = getOrderStatusMapKey(orderNo, orderStatus);
		try {
			if (SportOrderBuffer.addOrderToSportStatusQueue(updateObj) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,更新订单状态加入队列异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.sportOrderStatusMap.remove(statusMapKey);
			}
		}
		return operateValue;
	}

	/**
	 * 竞技彩订单状态批修改轮询
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @return
	 */
	public static int sportStatusRoundResult(long orderNo, int orderStatus) {
		int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
		String statusMapKey = getOrderStatusMapKey(orderNo, orderStatus);
		try {
			if (BatchConstant.sportOrderStatusMap.containsKey(statusMapKey)) {
				UpdateSportOrderStatus statusMsg = BatchConstant.sportOrderStatusMap.get(statusMapKey);
				synchronized (statusMsg) {
					statusMsg.wait(2000);
					if (!statusMsg.getIsBackReq()) {
						statusMsg.wait();
					}
				}
				if (statusMsg.getIsBackReq()) {
					operateValue = statusMsg.getOperateIdentifier();
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,轮询订单状态批处理返回结果发生异常,orderNo=" + orderNo + ",orderStatus=" + orderStatus, e);
		} finally {
			BatchConstant.sportOrderStatusMap.remove(statusMapKey);
		}
		return operateValue;
	}

	/**
	 * 竞技彩批同步订单map判断
	 * 
	 * @param orderNo
	 * @param sync
	 * @param ticketId
	 * @return
	 */
	public static boolean isSyncSportMapExist(long orderNo, int sync, String ticketId) {
		boolean returnValue = true;
		try {
			UpdateSyncStatus syncObj = createSyncMapObj(orderNo, sync, ticketId);
			UpdateSyncStatus obj = BatchConstant.sportOrderSyncMap.putIfAbsent(orderNo, syncObj);
			if (null == obj) {
				// 返回null,说明map中不存在,但是此时已经将值放入map中.
				returnValue = false;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,判断订单同步map是否存在,发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	/**
	 * 批同步竞技彩同步状态修改加入队列
	 * 
	 * @param orderNo
	 * @param sync
	 * @param ticketId
	 * @return
	 */
	public static int syncSportAddQueue(long orderNo, int sync, String ticketId) {
		int operateValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			UpdateSyncStatus syncObj = createSyncMapObj(orderNo, sync, ticketId);
			if (SportOrderBuffer.addSportOrderToSyncQueue(syncObj) == BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				operateValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,更新订单同步状态加入队列异常,orderNo=" + orderNo + ",sync=" + sync, e);
		} finally {
			if (operateValue != BatchConstant.BATCH_ADD_QUEUE_SUCCESS) {
				BatchConstant.sportOrderSyncMap.remove(orderNo);
			}
		}
		return operateValue;
	}

	/**
	 * 竞技彩同步批更新轮询
	 * 
	 * @param orderNo
	 * @return
	 */
	public static int syncSportRoundResult(long orderNo) {
		int operateValue = BatchConstant.BATCH_OPERATE_FAILED;
		try {
			if (BatchConstant.sportOrderSyncMap.containsKey(orderNo)) {
				UpdateSyncStatus syncMsg = BatchConstant.sportOrderSyncMap.get(orderNo);
				synchronized (syncMsg) {
					syncMsg.wait(2000);
					if (!syncMsg.getIsBackReq()) {
						syncMsg.wait();
					}
				}
				if (syncMsg.getIsBackReq()) {
					operateValue = syncMsg.getOperateIdentifier();
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,轮询订单同步状态批处理返回结果发生异常,orderNo=" + orderNo, e);
		} finally {
			BatchConstant.sportOrderSyncMap.remove(orderNo);
		}
		return operateValue;
	}

}
