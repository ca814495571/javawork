package com.cqfc.ticketwinning.factory;

import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.ticketwinning.service.lzc.ZC4CJQCTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.lzc.ZC6CBQCTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.lzc.ZCRX9CTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.lzc.ZCSFC14CTickwetWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.DLTTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.FC3DTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.PL3TicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.PL5TicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.QLCTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.QXCTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.SSCTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.SSQTicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.SYX5TicketWinningServiceImpl;
import com.cqfc.ticketwinning.service.szc.XYNCTicketWinningServiceImpl;
import com.cqfc.util.LotteryType;

public class TicketWinningServiceFactory {
	private static final SSQTicketWinningServiceImpl ssqService = new SSQTicketWinningServiceImpl();
	private static final QLCTicketWinningServiceImpl qlcService = new QLCTicketWinningServiceImpl();
	private static final FC3DTicketWinningServiceImpl fc3dService = new FC3DTicketWinningServiceImpl();
	private static final XYNCTicketWinningServiceImpl xyncService = new XYNCTicketWinningServiceImpl();
	private static final SSCTicketWinningServiceImpl sscService = new SSCTicketWinningServiceImpl();
	private static final DLTTicketWinningServiceImpl dltService = new DLTTicketWinningServiceImpl();
	private static final QXCTicketWinningServiceImpl qxcService = new QXCTicketWinningServiceImpl();
	private static final PL3TicketWinningServiceImpl pl3Service = new PL3TicketWinningServiceImpl();
	private static final PL5TicketWinningServiceImpl pl5Service = new PL5TicketWinningServiceImpl();
	private static final SYX5TicketWinningServiceImpl syx5Service = new SYX5TicketWinningServiceImpl();
	private static final ZCSFC14CTickwetWinningServiceImpl zcsfcService = new ZCSFC14CTickwetWinningServiceImpl();
	private static final ZCRX9CTicketWinningServiceImpl zcrx9Service = new ZCRX9CTicketWinningServiceImpl();
	private static final ZC6CBQCTicketWinningServiceImpl zc6cbqcService = new ZC6CBQCTicketWinningServiceImpl();
	private static final ZC4CJQCTicketWinningServiceImpl zc4cjqcService = new ZC4CJQCTicketWinningServiceImpl();
	
	/**
	 * 彩种对象实例
	 * @param lotteryId
	 * @return
	 */
	public static ITicketWinningService getTicketServiceInstance(String lotteryId){
		if(lotteryId.equals(LotteryType.SSQ.getText())){
			return ssqService;
		}
		else if(lotteryId.equals(LotteryType.QLC.getText())){
			return qlcService;
		}
		else if(lotteryId.equals(LotteryType.FC3D.getText())){
			return fc3dService;
		}
		else if(lotteryId.equals(LotteryType.XYNC.getText())){
			return xyncService;
		}
		else if(lotteryId.equals(LotteryType.SSC.getText())){
			return sscService;
		}
		else if(lotteryId.equals(LotteryType.DLT.getText())){
			return dltService;
		}
		else if(lotteryId.equals(LotteryType.QXC.getText())){
			return qxcService;
		}
		else if(lotteryId.equals(LotteryType.PLS.getText())){
			return pl3Service;
		}
		else if(lotteryId.equals(LotteryType.PLW.getText())){
			return pl5Service;
		}
		else if(lotteryId.equals(LotteryType.ZJSYXW.getText())){
			return syx5Service;
		}
		else if(lotteryId.equals(LotteryType.ZCSFC.getText())){
			return zcsfcService;
		}
		else if(lotteryId.equals(LotteryType.ZCRX9.getText())){
			return zcrx9Service;
		}
		else if(lotteryId.equals(LotteryType.ZC6CBQC.getText())){
			return zc6cbqcService;
		}
		else if(lotteryId.equals(LotteryType.ZC4CJQ.getText())){
			return zc4cjqcService;
		}
		return null;
	}
}
