package com.cqfc.partner.order.dataCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.OrderDetail;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.jami.util.CountLog;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;
import com.jami.util.LotteryLogger;
import com.jami.util.OrderLogger;
import com.jami.util.PartnerOrderLogUtil;

public class PartnerOrderBuffer {

	private static final int MAX_QUEUE_SIZE = 50000;

	private static final List<BlockingQueue<Order>> listQueue = new ArrayList<BlockingQueue<Order>>();

	private static List<BlockingQueue<WinningOrderInfo>> winResultUpdateQueue = new ArrayList<BlockingQueue<WinningOrderInfo>>();
	
	public static int addOrder(Order order) {
		// 写日志
		
		boolean b = false;
		
		try {

			LotteryLogger logger = OrderLogger.getDynamicLogger(DataSourceUtil.getDbName(order.getTradeId()));
			logger.info(PartnerOrderLogUtil.order2Str(order));
			
			if(OrderUtil.getLotteryCategory(order.getLotteryId()) == OrderStatus.LotteryType.SPORTS_GAME.type && order.getOrderDetails()!=null){
				//竞技彩详情列表写日志
				for (OrderDetail orderDetail :order.getOrderDetails()) {
					logger.info(PartnerOrderLogUtil.orderDetail2Str(orderDetail));
				}
			}
			
			b  = listQueue.get(DataSourceUtil.getDbCode(order.getTradeId()))
					.offer(order);
			if (b) {
				return 1;
			} else {
				return -1;
			}
			
		} catch (Exception e) {
			Log.run.error("订单同步到全局库出现异常"+order,e);
			return -1;
		}
		// 加入对应数据库编号的队列中


	}
	
	
	public static int updateBatchWinResult(List<WinningOrderInfo> winningOrderInfos){
		
		for (WinningOrderInfo order :winningOrderInfos) {
			
			try {
				winResultUpdateQueue.get(DataSourceUtil.getDbCode(order.getTradeId())).put(order);
			} catch (InterruptedException e) {
				Log.run.error("竞彩订单修改中奖金额和状态时出现异常"+order,e);
			}
		}
		
		return 1;
	}
	
	

	/**
	 * 初始化队列，只初始化一次
	 */

	public static void initListQueue() {

		BlockingQueue<Order> queue = null;
		BlockingQueue<WinningOrderInfo> queueJc = null;
		if (listQueue.size() == 0) {
			// 订单同步队列
			for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {
				queue = new LinkedBlockingQueue<Order>(MAX_QUEUE_SIZE);
				listQueue.add(queue);
			}
		}
		
		if (winResultUpdateQueue.isEmpty()) {
			// 竞彩算奖之后修改订单中奖金额以及状态队列
			for (int i = 0; i < DataSourceUtil.DB_NUM; i++) {
				queueJc = new LinkedBlockingQueue<WinningOrderInfo>(MAX_QUEUE_SIZE);
				winResultUpdateQueue.add(queueJc);
			}
		}

	}

	public static List<BlockingQueue<Order>> getListQueue() {
		return listQueue;
	}
	
	public static List<BlockingQueue<WinningOrderInfo>> getWinResultUpdateQueue() {
		return winResultUpdateQueue;
	}

}
