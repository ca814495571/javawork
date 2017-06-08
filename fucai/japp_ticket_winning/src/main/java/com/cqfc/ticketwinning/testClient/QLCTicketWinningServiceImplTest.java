package com.cqfc.ticketwinning.testClient;

import com.cqfc.ticketwinning.factory.TicketWinningServiceFactory;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.util.LotteryType;

public class QLCTicketWinningServiceImplTest {
	public static void main(String[] args) {
		/** 
			// 七乐彩单式
			public final static String PLAYTYPE_QLC_SINGLE = "0";
			
			格式：01,02,03,04,05,06,07#01,02,03,04,05,06,07 从01—30共30个号码中选择7个号码组合为一注投注号码，每组号码升序排列，顺序不限号与号之间用”,”分隔(升序排列)，注与注之间以”#”分隔。最多五注
			
			// 七乐彩复式
			public final static String PLAYTYPE_QLC_COMPOUND = "1";
			
			格式：01,02,03,04,05,06,07,08,10 从01—30共30个号码中选择8－16个号码，将每7个号码的组合方式都作为一注投注号码的多注投注，最多1注 ,号码间逗号分隔，号码都为2位，小于10的前一位用零补齐,每组号码必须大于7个并且不多于16个
			
			// 七乐彩胆拖
			public final static String PLAYTYPE_QLC_BILE_DRAG = "2";
			
			格式：09,12,23,24:15,16,27,29,30 共C53=10注最多1注,胆码,拖码间用半角冒号隔开，号码间用,隔开号码都为2位，小于10的前一位用零补齐胆码和托码不能包含重复号码,胆码托码个数之和必须大于7胆码1-6个托码1-29个
		**/
		
		String orderContentSingle = "01,02,03,04,05,06,07#01,02,03,04,05,06,07";
		String orderContentCompound = "01,02,03,04,05,06,07,08";
		String orderContentBileDrag = "09,12,23,24:15,16,27,29";
		ITicketWinningService ticketService = TicketWinningServiceFactory.getTicketServiceInstance(LotteryType.QLC.getText());
		//当前开奖号码为01,02,03,04,05,06,07,08 
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentSingle, "0"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentCompound, "1"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentBileDrag, "2"));
		
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentSingle, "0"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentCompound, "1"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentBileDrag, "2"));
		
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentSingle, "0"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentCompound, "1"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentBileDrag, "2"));
	}
}
