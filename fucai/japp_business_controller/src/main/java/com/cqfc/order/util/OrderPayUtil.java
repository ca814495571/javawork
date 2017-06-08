package com.cqfc.order.util;

import java.util.concurrent.RejectedExecutionException;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.cqfc.businesscontroller.task.PayOrderTask;
import com.cqfc.businesscontroller.task.PaySportOrderTask;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

/**
 * 支付线程池操作类,数字彩订单和竞技彩订单支付放入同一个线程池中
 * 
 * @author liwh
 */
public class OrderPayUtil {

	/**
	 * 数字彩订单放入支付线程池
	 * 
	 * @param order
	 * @param isPartnerAccount
	 * @param printBeginTime
	 * @param printEndTime
	 */
	public static void submitOrderToPayThreadPool(Order order, boolean isPartnerAccount, String printBeginTime,
			String printEndTime) {
		long orderNo = 0;
		try {
			orderNo = order.getOrderNo();
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor threadPoolTaskExecutor = ctx.getBean("threadPoolTaskExecutor",
					ThreadPoolTaskExecutor.class);
			threadPoolTaskExecutor.submit(new PayOrderTask(order, isPartnerAccount, printBeginTime, printEndTime));
			Log.fucaibiz.info("支付线程池,size=%d,orderNo=%d", threadPoolTaskExecutor.getThreadPoolExecutor().getQueue()
					.size(), orderNo);
		} catch (RejectedExecutionException e) {
			Log.fucaibiz.error("订单支付线程池已满,orderNo=" + orderNo, e);
		} catch (Exception e) {
			Log.fucaibiz.error("投注订单submit支付线程池发生异常,orderNo=" + orderNo, e);
		}
	}

	/**
	 * 竞技彩订单放入支付线程池
	 * 
	 * @param order
	 * @param isPartnerAccount
	 */
	public static void submitSportOrderToPayThreadPool(SportOrder order, boolean isPartnerAccount) {
		long orderNo = 0;
		try {
			orderNo = order.getOrderNo();
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			ThreadPoolTaskExecutor threadPoolTaskExecutor = ctx.getBean("threadPoolTaskExecutor",
					ThreadPoolTaskExecutor.class);
			threadPoolTaskExecutor.submit(new PaySportOrderTask(order, isPartnerAccount));
			Log.fucaibiz.info("支付线程池,size=%d,orderNo=%d", threadPoolTaskExecutor.getThreadPoolExecutor().getQueue()
					.size(), orderNo);
		} catch (RejectedExecutionException e) {
			Log.fucaibiz.error("订单支付线程池已满,orderNo=" + orderNo, e);
		} catch (Exception e) {
			Log.fucaibiz.error("投注订单submit支付线程池发生异常,orderNo=" + orderNo, e);
		}
	}
}
