package com.cqfc.businesscontroller.task;

import com.cqfc.order.model.SportOrder;
import com.jami.util.Log;

/**
 * 竞技彩订单支付
 * 
 * @author liwh
 */
public class PaySportOrderTask implements Runnable {

	private SportOrder order;
	private boolean isPartnerAccount;

	public PaySportOrderTask(SportOrder order, boolean isPartnerAccount) {
		this.order = order;
		this.isPartnerAccount = isPartnerAccount;
	}

	@Override
	public void run() {
		try {
			PaySportOrderProcessor.process(order, isPartnerAccount);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,订单支付线程池异常,orderNo=" + order.getOrderNo(), e);
		}
	}

}
