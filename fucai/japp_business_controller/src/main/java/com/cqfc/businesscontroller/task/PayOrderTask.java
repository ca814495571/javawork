package com.cqfc.businesscontroller.task;

import com.cqfc.order.model.Order;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class PayOrderTask implements Runnable {

	private Order order;
	private boolean isPartnerAccount;
	private String printBeginTime;
	private String printEndTime;

	public PayOrderTask(Order order, boolean isPartnerAccount, String printBeginTime, String printEndTime) {
		this.order = order;
		this.isPartnerAccount = isPartnerAccount;
		this.printBeginTime = printBeginTime;
		this.printEndTime = printEndTime;
	}

	@Override
	public void run() {
		try {
			PayOrderProcessor.process(order, isPartnerAccount, printBeginTime, printEndTime);
		} catch (Exception e) {
			Log.fucaibiz.error("投注订单支付线程池异常,orderNo=" + order.getOrderNo(), e);
		}
	}

}
