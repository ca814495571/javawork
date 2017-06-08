package com.cqfc.user.order.dataCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cqfc.protocol.userorder.Order;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;
import com.jami.util.LotteryLogger;
import com.jami.util.OrderLogger;
import com.jami.util.UserOrderLogUtil;

public class UserOrderBuffer {

	private static final int MAX_QUEUE_SIZE = 50000;

	private static final List<BlockingQueue<Order>> listQueue = new ArrayList<BlockingQueue<Order>>();

	public static int addOrder(Order order) {
		// 写日志
		String userId = String.valueOf(order.getUserId());
		boolean b ;
		try {
			if(order.getExt() == null){
				order.setExt("");
			}
			
			b = listQueue.get(DataSourceUtil.getDbCode(userId))
					.offer(order);
			if (b) {
				
				LotteryLogger logger = OrderLogger.getDynamicLogger(DataSourceUtil.getDbName(userId));
				logger.info(UserOrderLogUtil.convertLog2Str(order));
				
				return 1;
			} else {
				return -1;
			}

		} catch (Exception e) {
			Log.run.error("订单同步到全局时出现异常"+order,e);
			return -1;
		}
		
	
	}

	/**
	 * 初始化队列，只初始化一次
	 */

	public static void initListQueue() {

		BlockingQueue<Order> queue = null;

		if (listQueue.size() == 0) {
			for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {
				queue = new LinkedBlockingQueue<Order>(MAX_QUEUE_SIZE);
				listQueue.add(queue);
			}
		}

	}

	public static List<BlockingQueue<Order>> getListQueue() {
		return listQueue;
	}

}
