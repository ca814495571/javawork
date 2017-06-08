package com.cqfc.ticketwinning.factory;

import com.cqfc.ticketwinning.service.AbstrartJCTicketWinningService;
import com.cqfc.ticketwinning.service.impl.JCBDTicketWinningServiceImpl;

public class BDTicketWinningServiceFactory {
	private static final JCBDTicketWinningServiceImpl jcbdService = new JCBDTicketWinningServiceImpl();
	
	/**
	 * 彩种对象实例
	 * 
	 * @param lotteryId
	 * @return
	 */
	public static AbstrartJCTicketWinningService getBDTicketWinningServiceInstance(String lotteryId){

		return jcbdService;
	}
}
