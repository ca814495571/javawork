package com.cqfc.ticketwinning.factory;

import com.cqfc.ticketwinning.service.AbstrartJCTicketWinningService;
import com.cqfc.ticketwinning.service.impl.JCBDTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.jl.JCLQHHGGTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.jz.JCZQHHGGTicketWinningServiceImpl;
import com.cqfc.util.LotteryType;

public class JCTicketWinningServiceFactory {
	private static final JCZQHHGGTicketWinningServiceImpl jzhhggService = new JCZQHHGGTicketWinningServiceImpl();
	private static final JCLQHHGGTicketWinningServiceImpl jlhhggService = new JCLQHHGGTicketWinningServiceImpl();
	private static final JCBDTicketWinningServiceImpl jcbdService = new JCBDTicketWinningServiceImpl();
	
	/**
	 * 彩种对象实例
	 * 
	 * @param lotteryId
	 * @return
	 */
	public static AbstrartJCTicketWinningService getJCTicketWinningServiceInstance(String lotteryId){
		if (lotteryId.equals(LotteryType.JCZQHHGG.getText())) {
			return jzhhggService;
		} else if (lotteryId.equals(LotteryType.JCLQHHGG.getText())) {
			return jlhhggService;
		} 
			
		return jcbdService;
	}
}
