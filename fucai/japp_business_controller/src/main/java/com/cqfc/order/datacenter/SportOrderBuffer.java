package com.cqfc.order.datacenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.UpdateSportOrderStatus;
import com.cqfc.order.model.UpdateSyncStatus;
import com.cqfc.order.util.BatchConstant;
import com.cqfc.order.util.BatchUtil;
import com.cqfc.util.OrderConstant;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class SportOrderBuffer {

	private static final int MAX_QUEUE_SIZE = 30000;

	private static final List<BlockingQueue<SportOrder>> createSportListQueue = new ArrayList<BlockingQueue<SportOrder>>();

	private static final List<BlockingQueue<UpdateSportOrderStatus>> statusListQueue = new ArrayList<BlockingQueue<UpdateSportOrderStatus>>();

	private static final List<BlockingQueue<UpdateSyncStatus>> syncListQueue = new ArrayList<BlockingQueue<UpdateSyncStatus>>();

	public static int addOrderToListQueue(SportOrder sportOrder) {
		int returnValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			if (createSportListQueue.get(BatchUtil.getDbIndex(sportOrder.getTradeId())).add(sportOrder)) {
				returnValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			Log.run.error("竞技彩,订单创建加入批队列异常,orderNo=" + sportOrder.getOrderNo(), e);
		}
		return returnValue;
	}

	public static int addOrderToSportStatusQueue(UpdateSportOrderStatus updateSportOrderStatus) {
		int returnValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			if (statusListQueue.get(BatchUtil.getDbIndex(updateSportOrderStatus.getTradeId())).offer(
					updateSportOrderStatus)) {
				returnValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			long orderNo = updateSportOrderStatus.getOrderNo();
			Log.run.error("竞技彩,订单状态更新加入批队列异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	public static int addSportOrderToSyncQueue(UpdateSyncStatus updateSyncStatus) {
		int returnValue = BatchConstant.BATCH_ADD_QUEUE_FAILED;
		try {
			if (syncListQueue.get(BatchUtil.getDbIndex(updateSyncStatus.getTradeId())).offer(updateSyncStatus)) {
				returnValue = BatchConstant.BATCH_ADD_QUEUE_SUCCESS;
			}
		} catch (Exception e) {
			long orderNo = updateSyncStatus.getOrderNo();
			Log.run.error("竞技彩,同步订单更新同步状态发生异常,orderNo=" + orderNo, e);
		}
		return returnValue;
	}

	public static void initSportListQueue() {
		for (int i = 0; i < OrderConstant.DATASOURCE_NUM; i++) {
			createSportListQueue.add(new LinkedBlockingQueue<SportOrder>(MAX_QUEUE_SIZE));
			statusListQueue.add(new LinkedBlockingQueue<UpdateSportOrderStatus>(MAX_QUEUE_SIZE));
			syncListQueue.add(new LinkedBlockingQueue<UpdateSyncStatus>(MAX_QUEUE_SIZE));
		}
	}

	public static List<BlockingQueue<SportOrder>> getCreateListQueue() {
		return createSportListQueue;
	}

	public static List<BlockingQueue<UpdateSportOrderStatus>> getStatusListQueue() {
		return statusListQueue;
	}

	public static List<BlockingQueue<UpdateSyncStatus>> getSyncListQueue() {
		return syncListQueue;
	}

}
