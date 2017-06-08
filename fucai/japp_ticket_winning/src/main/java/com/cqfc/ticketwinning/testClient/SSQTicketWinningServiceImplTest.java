package com.cqfc.ticketwinning.testClient;

import com.cqfc.ticketwinning.factory.TicketWinningServiceFactory;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.util.LotteryType;

public class SSQTicketWinningServiceImplTest {
	public static void main(String[] args) {
		/** 
			// 双色球单式
			public final static String PLAYTYPE_SSQ_SINGLE = "0";
			
			格式：01,02,19,23,27,29:10#04,17,23,27,28,32:03 最多5注，注与注之间用#分隔，红球与篮球间用冒号分隔，号码球与号码球之间用逗号分隔，号码都为2位，小于10的前一位用零补齐,红球号码需要升序排列。
			
			// 双色球复式
			public final static String PLAYTYPE_SSQ_COMPOUND = "1";
			
			格式：03,07,13,14,19,22,25,28,30:09,14 共C96*C21=168注最多1注,红球与篮球之间冒号分隔,号码间逗号分隔，号码都为2位，小于10的前一位用零补齐红球和篮球需要分别升序排列。
			
			// 双色球胆拖
			public final static String PLAYTYPE_SSQ_BILE_DRAG = "2";
			
			格式：04,09,13,22:05,16,18,20,29:11,15 共C52*C21=20注胆码和拖码中不包含重复号码最多1注,胆码在前,拖码中间,蓝球最后,并且都按升序排列,胆码,拖码和蓝球间用分号隔开，号码间用逗号隔开，号码都为2位，小于10的前一位用零补齐。
		**/
		
		String orderContentSingle = "01,02,19,23,27,29:10#04,17,23,27,28,32:03";
		String orderContentCompound = "03,07,13,14,19,22,28:09,10,14";
		String orderContentBileDrag = "01,02,19,23:05,16,18,20,29:11,15";
		ITicketWinningService ticketService = TicketWinningServiceFactory.getTicketServiceInstance(LotteryType.SSQ.getText());
		
		//当前开奖号码为01,02,19,23,27,29:10
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentSingle, "0"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentCompound, "1"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentBileDrag, "2"));
//		
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentSingle, "0"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentCompound, "1"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentBileDrag, "2"));
		
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentSingle, "0"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentCompound, "1"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentBileDrag, "2"));
	}
}
