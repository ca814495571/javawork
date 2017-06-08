package com.cqfc.ticketwinning.testClient;

import com.cqfc.ticketwinning.factory.TicketWinningServiceFactory;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.util.LotteryType;


public class SSCTicketWinningServiceImplTest {
	public static void main(String[] args) {
		/**
		  	// 时时彩单式一星
			public final static String PLAYTYPE_SSC_RADIO_ONESTAR = "10";
			// 时时彩单式二星
			public final static String PLAYTYPE_SSC_RADIO_TWOSTAR = "20";
			// 时时彩单式三星
			public final static String PLAYTYPE_SSC_RADIO_THREESTAR = "30";
			// 时时彩单式五星
			public final static String PLAYTYPE_SSC_RADIO_FIVESTAR = "50";
			// 时时彩复选二星
			public final static String PLAYTYPE_SSC_COMPOUND_TWOSTAR = "21";
			// 时时彩复选三星
			public final static String PLAYTYPE_SSC_COMPOUND_THREESTAR = "31";
			// 时时彩复选五星
			public final static String PLAYTYPE_SSC_COMPOUND_FIVESTAR = "51";
			// 时时彩组合二星
			public final static String PLAYTYPE_SSC_GROUP_TWOSTAR = "22";
			// 时时彩组合三星
			public final static String PLAYTYPE_SSC_GROUP_THREESTAR = "32";
			// 时时彩组合五星
			public final static String PLAYTYPE_SSC_GROUP_FIVESTAR = "52";
			// 时时彩二星组选单式
			public final static String PLAYTYPE_SSC_TWOSTAR_GROUP_SINGLE = "23";
			// 时时彩二星组选复式
			public final static String PLAYTYPE_SSC_TWOSTAR_GROUP_COMPOUND = "24";
			// 时时彩二星组选分组
			public final static String PLAYTYPE_SSC_TWOSTAR_GROUPING = "25";
			// 时时彩二星组选包点
			public final static String PLAYTYPE_SSC_TWOSTAR_GROUP_BUNS = "26";
			// 时时彩二星组选包胆
			public final static String PLAYTYPE_SSC_TWOSTAR_GROUP_BRAVE = "27";
			// 时时彩二星包点
			public final static String PLAYTYPE_SSC_TWOSTAR_BUNS = "28";
			// 时时彩三星包点
			public final static String PLAYTYPE_SSC_THREESTAR_BUNS = "37";
			// 时时彩猜大小单双
			public final static String PLAYTYPE_SSC_SIZE_ODDEVEN = "60";
			// 时时彩五星通选
			public final static String PLAYTYPE_SSC_FIVESTAR_CHOOSE = "53";
			// 时时彩三星组三
			public final static String PLAYTYPE_SSC_THREESTAR_GROUP3 = "38";
			// 时时彩三星组六
			public final static String PLAYTYPE_SSC_THREESTAR_GROUP6 = "39";
			// 时时彩三星组三复式
			public final static String PLAYTYPE_SSC_THREESTAR_GROUP3_COMPOUND = "43";
			// 时时彩三星组六复式
			public final static String PLAYTYPE_SSC_THREESTAR_GROUP6_COMPOUND = "33";
			// 时时彩三星组选包点
			public final static String PLAYTYPE_SSC_THREESTAR_GROUP_BUNS = "34";
			// 时时彩三星组选包胆
			public final static String PLAYTYPE_SSC_THREESTAR_GROUP_BRAVE = "35";
			// 时时彩三星直选组合复式
			public final static String PLAYTYPE_SSC_THREESTAR_DIRECT_GROUP_COMPOUND = "36";
		 */
		
		String orderContentRadioOneStar = "5";
		String orderContentRadioTwoStar = "4:5";
		String orderContentRadioThreeStar = "3:4:5";
		String orderContentRadioFiveStar = "1:2:3:4:5";
		String orderContentCompoundTwoStar = "4:5";
		String orderContentCompoundThreeStar = "3:4:5";
		String orderContentCompoundFiveStar = "1:2:3:4:5";
		String orderContentGroupTwoStar = "2:3:4-3:4:5";
		String orderContentGroupThreeStar = "1:2:3-2:3:4-3:4:5";
		String orderContentGroupFiveStar = "1:2-2:3-3:4:5-4:6:7-5:7:8:9";
		String orderContentGroupTwoStarSingle = "5:4";
		String orderContentGroupTwoStarCompound = "2:4:5:8";
		String orderContentGroupingTwoStar = "2:3:4-3:4:5";
		String orderContentGroupTwoStarBuns = "2";
		String orderContentGroupTwoStarBrave = "5";
		String orderContentTwoStarBuns = "9";
		String orderContentThreeStarBuns = "12";
		String orderContentSizeOddEven = "1:2";
		String orderContentFiveStarChoose = "1:2:3:4:5";
		String orderContentThreeStarGroup3 = "2:2:3";
		String orderContentThreeStarGroup6 = "4:5:3";
		String orderContentThreeStarGroup3Compound = "4:5:3";
		String orderContentThreeStarGroup6Compound = "1:3:4:5:6";
		String orderContentThreeStarGroupBuns = "12";
		String orderContentThreeStarGroupBrave = "5:4";
		String orderContentThreeStarGroupDirect = "1:2:3:4:5";
		ITicketWinningService ticketService = TicketWinningServiceFactory.getTicketServiceInstance(LotteryType.SSC.getText());
		
		//当前开奖号码为1:2:3:4:5
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioOneStar, "10"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioTwoStar, "20"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioThreeStar, "30"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioFiveStar, "50"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentCompoundTwoStar, "21"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentCompoundThreeStar, "31"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentCompoundFiveStar, "51"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupTwoStar, "22"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupThreeStar, "32"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupFiveStar, "52"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupTwoStarSingle, "23"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupTwoStarCompound, "24"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupingTwoStar, "25"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupTwoStarBuns, "26"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroupTwoStarBrave, "27"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentTwoStarBuns, "28"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarBuns, "37"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentSizeOddEven, "60"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentFiveStarChoose, "53"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroup3, "38"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroup6, "39"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroup3Compound, "40"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroup6Compound, "33"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroupBuns, "34"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroupBrave, "35"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentThreeStarGroupDirect, "36"));
		
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioOneStar, "10"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioTwoStar, "20"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioThreeStar, "30"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioFiveStar, "50"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentCompoundTwoStar, "21"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentCompoundThreeStar, "31"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentCompoundFiveStar, "51"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupTwoStar, "22"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupThreeStar, "32"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupFiveStar, "52"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupTwoStarSingle, "23"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupTwoStarCompound, "24"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupingTwoStar, "25"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupTwoStarBuns, "26"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroupTwoStarBrave, "27"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentTwoStarBuns, "28"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarBuns, "37"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentSizeOddEven, "60"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentFiveStarChoose, "53"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroup3, "38"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroup6, "39"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroup3Compound, "40"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroup6Compound, "33"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroupBuns, "34"));
//		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroupBrave, "35"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentThreeStarGroupDirect, "36"));
		
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioOneStar, "10"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioTwoStar, "20"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioThreeStar, "30"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioFiveStar, "50"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentCompoundTwoStar, "21"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentCompoundThreeStar, "31"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentCompoundFiveStar, "51"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupTwoStar, "22"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupThreeStar, "32"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupFiveStar, "52"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupTwoStarSingle, "23"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupTwoStarCompound, "24"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupingTwoStar, "25"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupTwoStarBuns, "26"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroupTwoStarBrave, "27"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentTwoStarBuns, "28"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarBuns, "37"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentSizeOddEven, "60"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentFiveStarChoose, "53"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroup3, "38"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroup6, "39"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroup3Compound, "40"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroup6Compound, "33"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroupBuns, "34"));
//		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroupBrave, "35"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentThreeStarGroupDirect, "36"));
	}
}
