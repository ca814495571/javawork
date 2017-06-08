package com.cqfc.ticketwinning.testClient;

import com.cqfc.ticketwinning.factory.TicketWinningServiceFactory;
import com.cqfc.ticketwinning.service.ITicketWinningService;
import com.cqfc.util.LotteryType;

public class FC3DTicketWinningServiceImplTest {
	public static void main(String[] args) {
		/** 
			// 福彩3D单选单式
			public final static String PLAYTYPE_FC3D_RADIO_SINGLE = "0";
			
			格式：9:3:1#2:9:4#3:9:2百十个位顺序，以“:”分隔，各组号码间用“#”分隔。最多五注。按位全部相同，中单选奖(1000元)。
			
			// 福彩3D单选复式
			public final static String PLAYTYPE_FC3D_RADIO_COMPOUND = "6";
			
			格式：1,3:4,7:5 C21*C21*C11=4注(145,175,345,375) 百位、十位、个位间用“:”分隔，号码间用“，”分隔(升序排列)。
			
			// 福彩3D单选包号
			public final static String PLAYTYPE_FC3D_RADIO_PACKAGENO = "11";
			
			格式：1,2 C21*C21*C21=8注(百位1,2,十位1,2,个位1,2) 个位、十位和百位分别包选中号码，号码和号码间用“，”分隔(升序排列)。
			
			// 福彩3D单选和值
			public final static String PLAYTYPE_FC3D_RADIO_ANDVALUE = "10";
			
			格式：1(001,010,100)3注 (0-27) 0(000),27(999) 百位、十位、个位相加和，单个号码无特殊格式区分。
			
			// 福彩3D组三单式
			public final static String PLAYTYPE_FC3D_GROUP3_SINGLE = "5";
			
			格式：4:5:5#2:7:7 4:5:5(455,554,545)1注 号码之间以“:”分隔（升序排列）。注与注之间使用#分隔。最多五注。可以与组六单式混投。
 			开奖号码有两位相同，且投注号码与开奖号码相同，顺序可不同，中组选三奖(320元)。
			
			// 福彩3D组三包号
			public final static String PLAYTYPE_FC3D_GROUP3_PACKAGENO = "7";
			
			格式：2,3 (223,233)2注 号码和号码间用“，”分隔。
			
			// 福彩3D组六单式
			public final static String PLAYTYPE_FC3D_GROUP6_SINGLE = "5";
			
			格式：5:6:7#1:5:8 5:6:7(567,576,657,675,756,765) 号码之间以“:”分隔（升序排列）。注与注之间使用#分隔。最多五注。可以与组三单式混投。
			开奖号码百十个位都不相同，且投注号码与开奖号码相同，顺序可不同，中组选六奖(160元)。
			
			// 福彩3D组六包号
			public final static String PLAYTYPE_FC3D_GROUP6_PACKAGENO = "8";
			
			格式：3,5,6,7,9(4位以上) 共C53=10注 号码和号码间用“，”分隔(升序排列)
			
			// 福彩3D组六和值
			public final static String PLAYTYPE_FC3D_GROUP6_ANDVALUE = "19";
			
			格式：20(1-26) 1(001),26(899) 百位、十位、个位相加和，单个号码无特殊格式区分。
			
			// 福彩3D组三和值
			public final static String PLAYTYPE_FC3D_GROUP3_ANDVALUE = "18";
			
			格式：20(1-26) 1(001),26(899) 百位、十位、个位相加和，单个号码无特殊格式区分。
		**/
		
		String orderContentRadioSingle = "9:3:1#2:9:4#3:9:2";
		String orderContentRadioCompound = "1,3,9:3,4,7:1,5";
		String orderContentRadioPackageNo = "3,9";
		String orderContentRadioAndValue = "13";
		String orderContentGroup3Single = "4:5:5#2:7:7#1:3:3";
		String orderContentGroup3PackageNo = "1,3,4,5";
		String orderContentGroup6Single = "5:6:7#1:5:8#1:3:7";
		String orderContentGroup6PackageNo = "1,3,5,6,7,9";
		String orderContentGroup6AndValue = "12";
		String orderContentGroup3AndValue = "11";
		ITicketWinningService ticketService = TicketWinningServiceFactory.getTicketServiceInstance(LotteryType.FC3D.getText());
	
		//当前开奖号码为7:3:1
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioSingle, "0"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioCompound, "6"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioPackageNo, "11"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentRadioAndValue, "10"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroup3Single, "5"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroup3PackageNo, "7"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroup6Single, "5"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroup6PackageNo, "8"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroup6AndValue, "19"));
//		System.out.println("恭喜，您的中奖金额为：" + ticketService.calTicketWinningAmount(orderContentGroup3AndValue, "18"));
		
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioSingle, "0"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioCompound, "6"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioPackageNo, "11"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentRadioAndValue, "10"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroup3Single, "5"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroup3PackageNo, "7"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroup6Single, "5"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroup6PackageNo, "8"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroup6AndValue, "19"));
		System.out.println("投注的注数为：" + ticketService.calBallCounts(orderContentGroup3AndValue, "18"));
		
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioSingle, "0"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioCompound, "6"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioPackageNo, "11"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentRadioAndValue, "10"));
		System.out.println("投注的内容的长度：" + ticketService.listTicketDetails(orderContentRadioAndValue, "10").size());
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroup3Single, "5"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroup3PackageNo, "7"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroup6Single, "5"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroup6PackageNo, "8"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroup6AndValue, "19"));
		System.out.println("投注的内容为：" + ticketService.listTicketDetails(orderContentGroup3AndValue, "18"));
		
	}
}
