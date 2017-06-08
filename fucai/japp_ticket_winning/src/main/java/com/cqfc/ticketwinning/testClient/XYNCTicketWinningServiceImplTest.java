package com.cqfc.ticketwinning.testClient;

import com.cqfc.ticketwinning.factory.TicketWinningServiceFactory;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.util.LotteryType;


public class XYNCTicketWinningServiceImplTest {
	public static void main(String[] args) {
		/**
			// 幸运农场选一数投
			public final static String PLAYTYPE_XYNC_CHOOSEONE_SHOTS = "10";
			// 幸运农场选一数投复式
			public final static String PLAYTYPE_XYNC_CHOOSEONE_COMPOUND = "11";
			// 幸运农场选一红投
			public final static String PLAYTYPE_XYNC_CHOOSEONE_RED = "12";
			// 幸运农场选二任选单式
			public final static String PLAYTYPE_XYNC_CHOOSETWOANY_SINGLE = "20";
			// 幸运农场选二任选复式
			public final static String PLAYTYPE_XYNC_CHOOSETWOANY_COMPOUND = "21";
			// 幸运农场选二任选胆拖
			public final static String PLAYTYPE_XYNC_CHOOSETWOANY_BRAVE = "22";
			// 幸运农场选二连组
			public final static String PLAYTYPE_XYNC_CHOOSETWO_GROUP = "23";
			// 幸运农场选二连组复式
			public final static String PLAYTYPE_XYNC_CHOOSETWO_COMPOUND = "24";
			// 幸运农场选二连组胆拖
			public final static String PLAYTYPE_XYNC_CHOOSETWO_BRAVE = "25";
			// 幸运农场选二连直
			public final static String PLAYTYPE_XYNC_CHOOSETWO_STRAIGHT = "26";
			// 幸运农场选三任选单式
			public final static String PLAYTYPE_XYNC_CHOOSETHREEANY_SINGLE = "30";
			// 幸运农场选三任选复式
			public final static String PLAYTYPE_XYNC_CHOOSETHREEANY_COMPOUND = "31";
			// 幸运农场选三任选胆拖
			public final static String PLAYTYPE_XYNC_CHOOSETHREEANY_BRAVE = "32";
			// 幸运农场选三前组
			public final static String PLAYTYPE_XYNC_CHOOSETHREE_GROUP = "33";
			// 幸运农场选三前组复式
			public final static String PLAYTYPE_XYNC_CHOOSETHREE_COMPOUND = "34";
			// 幸运农场选三前组胆拖
			public final static String PLAYTYPE_XYNC_CHOOSETHREE_BRAVE = "35";
			// 幸运农场选三前直
			public final static String PLAYTYPE_XYNC_CHOOSETHREE_STRAIGHT = "36";
			// 幸运农场选四任选单式
			public final static String PLAYTYPE_XYNC_CHOOSEFOURANY_SINGLE = "40";
			// 幸运农场选四任选复式
			public final static String PLAYTYPE_XYNC_CHOOSEFOURANY_COMPOUND = "41";
			// 幸运农场选四任选胆拖
			public final static String PLAYTYPE_XYNC_CHOOSEFOURANY_BRAVE = "42";
			// 幸运农场选五任选单式
			public final static String PLAYTYPE_XYNC_CHOOSEFIVEANY_SINGLE = "50";
			// 幸运农场选五任选复式
			public final static String PLAYTYPE_XYNC_CHOOSEFIVEANY_COMPOUND = "51";
			// 幸运农场选五任选胆拖
			public final static String PLAYTYPE_XYNC_CHOOSEFIVEANY_BRAVE = "52";
		 */
		String orderContentChooseOneShot = "1";
		String orderContentChooseOneCompound = "2|11|18";
		String orderContentChooseOneRed = "17";
		String orderContentChooseTwoAnySingle = "1|2";
		String orderContentChooseTwoAnyCompound = "1|2|3|4|5|6|7";
		String orderContentChooseTwoAnyBrave = "1-2|3|4|5|6";
		String orderContentChooseTwoGroup = "1|2";
		String orderContentChooseTwoCompound = "1|2|3|4|5|6|7";
		String orderContentChooseTwoBrave = "1-2|3|4|5|6";
		String orderContentChooseTwoStraight = "1|2";
		String orderContentChooseThreeAnySingle = "1|2|3";
		String orderContentChooseThreeAnyCompound = "1|2|3|4";
		String orderContentChooseThreeAnyBrave = "1-2|3|4";
		String orderContentChooseThreeGroup = "1|2|3";
		String orderContentChooseThreeCompound = "1|2|3|4|5|6|7";
		String orderContentChooseThreeBrave = "1-2|3|4|5|6";
		String orderContentChooseThreeStraight = "1|2|3";
		String orderContentChooseFourAnySingle = "1|2|3|4";
		String orderContentChooseFourAnyCompound = "1|2|3|4|5";
		String orderContentChooseFourAnyBrave = "1-2|3|4|5";
		String orderContentChooseFiveAnySingle = "1|2|3|4|5";
		String orderContentChooseFiveAnyCompound = "1|2|3|4|5|6";
		String orderContentChooseFiveAnyBrave = "1-2|3|4|5|6";
		ITicketWinningService ticketService = TicketWinningServiceFactory.getTicketServiceInstance(LotteryType.XYNC.getText());
		
		//当前开奖号码为1|2|3|4|5|6|7|8
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseOneShot, "10"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseOneCompound, "11"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseOneRed, "12"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoAnySingle, "20"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoAnyCompound, "21"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoAnyBrave, "22"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoGroup, "23"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoCompound, "24"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoBrave, "25"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseTwoStraight, "26"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeAnySingle, "30"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeAnyCompound, "31"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeAnyBrave, "32"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeGroup, "33"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeCompound, "34"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeBrave, "35"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseThreeStraight, "36"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseFourAnySingle, "40"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseFourAnyCompound, "41"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseFourAnyBrave, "42"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseFiveAnySingle, "50"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseFiveAnyCompound, "51"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentChooseFiveAnyBrave, "52"));
		
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseOneShot, "10"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseOneCompound, "11"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseOneRed, "12"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoAnySingle, "20"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoAnyCompound, "21"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoAnyBrave, "22"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoGroup, "23"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoCompound, "24"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoBrave, "25"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseTwoStraight, "26"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeAnySingle, "30"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeAnyCompound, "31"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeAnyBrave, "32"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeGroup, "33"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeCompound, "34"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeBrave, "35"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseThreeStraight, "36"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseFourAnySingle, "40"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseFourAnyCompound, "41"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseFourAnyBrave, "42"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseFiveAnySingle, "50"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseFiveAnyCompound, "51"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentChooseFiveAnyBrave, "52"));
		
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseOneShot, "10"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseOneCompound, "11"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseOneRed, "12"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoAnySingle, "20"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoAnyCompound, "21"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoAnyBrave, "22"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoGroup, "23"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoCompound, "24"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoBrave, "25"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseTwoStraight, "26"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeAnySingle, "30"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeAnyCompound, "31"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeAnyBrave, "32"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeGroup, "33"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeCompound, "34"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeBrave, "35"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseThreeStraight, "36"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseFourAnySingle, "40"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseFourAnyCompound, "41"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseFourAnyBrave, "42"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseFiveAnySingle, "50"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseFiveAnyCompound, "51"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentChooseFiveAnyBrave, "52"));
	}
}
