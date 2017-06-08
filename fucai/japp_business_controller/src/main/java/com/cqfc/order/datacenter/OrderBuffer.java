package com.cqfc.order.datacenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.UpdateOrderStatus;
import com.cqfc.order.model.UpdateSyncStatus;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.util.OrderConstant;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class OrderBuffer {

	private static final int MAX_QUEUE_SIZE = 50000;

	private static final List<BlockingQueue<Order>> createListQueue = new ArrayList<BlockingQueue<Order>>();

	private static final List<BlockingQueue<UpdateOrderStatus>> statusListQueue = new ArrayList<BlockingQueue<UpdateOrderStatus>>();

	private static final List<BlockingQueue<UpdateSyncStatus>> syncListQueue = new ArrayList<BlockingQueue<UpdateSyncStatus>>();

	public static int addOrderToListQueue(Order order) {
		int returnValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			if (createListQueue.get(BatchUtil.getDbIndex(order.getTradeId())).add(order)) {
				returnValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.run.error("create order,to join the createListQueue error,orderNo=" + order.getOrderNo(), e);
		}
		return returnValue;
	}

	public static int addOrderToStatusQueue(UpdateOrderStatus updateOrderStatus) {
		int returnValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			if (statusListQueue.get(BatchUtil.getDbIndex(updateOrderStatus.getTradeId())).offer(updateOrderStatus)) {
				returnValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			long orderNo = updateOrderStatus.getOrderNo();
			Log.run.error("update order status,to join the statusListQueue error,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	public static int addOrderToSyncQueue(UpdateSyncStatus updateSyncStatus) {
		int returnValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			if (syncListQueue.get(BatchUtil.getDbIndex(updateSyncStatus.getTradeId())).offer(updateSyncStatus)) {
				returnValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			long orderNo = updateSyncStatus.getOrderNo();
			Log.run.error("update order sync,to join the syncListQueue error,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	public static void initListQueue() {
		for (int i = 0; i < OrderConstant.DATASOURCE_NUM; i++) {
			createListQueue.add(new LinkedBlockingQueue<Order>(MAX_QUEUE_SIZE));
			statusListQueue.add(new LinkedBlockingQueue<UpdateOrderStatus>(MAX_QUEUE_SIZE));
			syncListQueue.add(new LinkedBlockingQueue<UpdateSyncStatus>(MAX_QUEUE_SIZE));
		}
	}

	public static List<BlockingQueue<Order>> getCreateListQueue() {
		return createListQueue;
	}

	public static List<BlockingQueue<UpdateOrderStatus>> getStatusListQueue() {
		return statusListQueue;
	}

	public static List<BlockingQueue<UpdateSyncStatus>> getSyncListQueue() {
		return syncListQueue;
	}

}
