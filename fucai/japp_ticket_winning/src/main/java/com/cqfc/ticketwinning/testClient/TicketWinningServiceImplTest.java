package com.cqfc.ticketwinning.testClient;

import org.apache.thrift.TException;

import com.cqfc.ticketwinning.service.impl.TicketWinningServiceImpl;


public class TicketWinningServiceImplTest {
	public static void main(String[] args) throws TException {
		//只测试投注格式是否合法，测试中奖金额，总注数和投注内容参见具体的彩种Test
		TicketWinningServiceImpl service = new TicketWinningServiceImpl();

		
//SSQ		
//		String orderContentSingle = "01,02,19,23,27,29:10#04,17,23,27,28,32:03";
//		String orderContentCompound = "03,07,13,14,23,27,29:09,14";
//		String orderContentBileDrag = "01,02,19,23:05,16,18,20,29:11,15";
//		
//		//当前开奖号码为01,02,19,23,27,29:10
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSQ", "0", orderContentSingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSQ", "1", orderContentCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSQ", "2", orderContentBileDrag));


		
		
//QLC		
//		String orderContentSingle = "01,02,03,04,05,06,07#01,02,03,04,05,06,07";
//		String orderContentCompound = "01,02,03,04,05,06";
//		String orderContentBileDrag = "09,12,23,24:15,16,27,29";
//		
//		//当前开奖号码为01,02,03,04,05,06,07,08 
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("QLC", "0", orderContentSingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("QLC", "1", orderContentCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("QLC", "2", orderContentBileDrag));

		
		
		
		
//FC3D		
//		String orderContentRadioSingle = "9:3:1#2:9:4#3:9:2";
//		String orderContentRadioCompound = "1,3,9:3,4,7:1,5";
//		String orderContentRadioPackageNo = "3,9";
//		String orderContentRadioAndValue = "13";
//		String orderContentGroup3Single = "4:5:5#2:7:7#1:3:3";
//		String orderContentGroup3PackageNo = "1,3,4,5";
//		String orderContentGroup6Single = "5:6:7#1:5:8#1:3:7";
//		String orderContentGroup6PackageNo = "1,3,5,6,7,9";
//		String orderContentGroup6AndValue = "12";
//		String orderContentGroup3AndValue = "17";
		
		//当前开奖号码为7:3:1
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "0", orderContentRadioSingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "6", orderContentRadioCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "11", orderContentRadioPackageNo));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "10", orderContentRadioAndValue));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "5", orderContentGroup3Single));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "7", orderContentGroup3PackageNo));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "5", orderContentGroup6Single));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "8", orderContentGroup6PackageNo));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "19", orderContentGroup6AndValue));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("FC3D", "18", orderContentGroup3AndValue));
		
//		String orderContentChooseOneShot = "1";
//		String orderContentChooseOneCompound = "2|11|18";
//		String orderContentChooseOneRed = "20";
//		String orderContentChooseTwoAnySingle = "1|2";
//		String orderContentChooseTwoAnyCompound = "1|2|3|4|5|6|7";
//		String orderContentChooseTwoAnyBrave = "1-2|3|4|5|6";
//		String orderContentChooseTwoGroup = "1|2";
//		String orderContentChooseTwoCompound = "1|2|3|4|5|6|7";
//		String orderContentChooseTwoBrave = "1-2|3|4|5|6";
//		String orderContentChooseTwoStraight = "1|2";
//		String orderContentChooseThreeAnySingle = "1|2|3";
//		String orderContentChooseThreeAnyCompound = "1|2|3|4";
//		String orderContentChooseThreeAnyBrave = "1-2|3|4";
//		String orderContentChooseThreeGroup = "1|2|3";
//		String orderContentChooseThreeCompound = "1|2|3|4|5|6|7";
//		String orderContentChooseThreeBrave = "1-2|3|4|5|6";
//		String orderContentChooseThreeStraight = "1|2|3";
//		String orderContentChooseFourAnySingle = "1|2|3|4";
//		String orderContentChooseFourAnyCompound = "1|2|3|4|5";
//		String orderContentChooseFourAnyBrave = "1-2|3|4|5";
//		String orderContentChooseFiveAnySingle = "1|2|3|4|5";
//		String orderContentChooseFiveAnyCompound = "1|2|3|4|5|6";
//		String orderContentChooseFiveAnyBrave = "1-2|3|4|5|6";
//		
//		//当前开奖号码为1|2|3|4|5|6|7|8
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "10", orderContentChooseOneShot));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "11", orderContentChooseOneCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "12", orderContentChooseOneRed));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "20", orderContentChooseTwoAnySingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "21", orderContentChooseTwoAnyCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "22", orderContentChooseTwoAnyBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "23", orderContentChooseTwoGroup));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "24", orderContentChooseTwoCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "25", orderContentChooseTwoBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "26", orderContentChooseTwoStraight));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "30", orderContentChooseThreeAnySingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "31", orderContentChooseThreeAnyCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "32", orderContentChooseThreeAnyBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "33", orderContentChooseThreeGroup));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "34", orderContentChooseThreeCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "35", orderContentChooseThreeBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "36", orderContentChooseThreeStraight));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "40", orderContentChooseFourAnySingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "41", orderContentChooseFourAnyCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "42", orderContentChooseFourAnyBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "50", orderContentChooseFiveAnySingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "51", orderContentChooseFiveAnyCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("XYNC", "52", orderContentChooseFiveAnyBrave));

		
		
		
//SSC		
//		String orderContentRadioOneStar = "5";
//		String orderContentRadioTwoStar = "4:5";
//		String orderContentRadioThreeStar = "3:4:5";
//		String orderContentRadioFiveStar = "1:2:3:4:5";
//		String orderContentCompoundTwoStar = "4:5";
//		String orderContentCompoundThreeStar = "3:4:5";
//		String orderContentCompoundFiveStar = "1:2:3:4:5";
//		String orderContentGroupTwoStar = "2:3:4-3:4:5";
//		String orderContentGroupThreeStar = "1:2:3-2:3:4-3:4:5";
//		String orderContentGroupFiveStar = "1:2-2:3-3:4:5-4:6:7-5:7:8:9";
//		String orderContentGroupTwoStarSingle = "5:4";
//		String orderContentGroupTwoStarCompound = "2:4:5:8";
//		String orderContentGroupingTwoStar = "2:3:4-3:4:5";
//		String orderContentGroupTwoStarBuns = "2";
//		String orderContentGroupTwoStarBrave = "5";
//		String orderContentTwoStarBuns = "9";
//		String orderContentThreeStarBuns = "12";
//		String orderContentSizeOddEven = "1:2";
//		String orderContentFiveStarChoose = "1:2:3:4:5";
//		String orderContentThreeStarGroup3 = "2:2:3";
//		String orderContentThreeStarGroup6 = "4:5:3";
//		String orderContentThreeStarGroup3Compound = "4:5:3";
//		String orderContentThreeStarGroup6Compound = "1:3:4:5:6";
//		String orderContentThreeStarGroupBuns = "12";
//		String orderContentThreeStarGroupBrave = "5:4";
//		String orderContentThreeStarGroupDirect = "1:2:3:4:5";
		
		//当前开奖号码为1:2:3:4:5
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "10", orderContentRadioOneStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "20", orderContentRadioTwoStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "30", orderContentRadioThreeStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "50", orderContentRadioFiveStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "21", orderContentCompoundTwoStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "31", orderContentCompoundThreeStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "51", orderContentCompoundFiveStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "22", orderContentGroupTwoStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "32", orderContentGroupThreeStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "52", orderContentGroupFiveStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "23", orderContentGroupTwoStarSingle));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "24", orderContentGroupTwoStarCompound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "25", orderContentGroupingTwoStar));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "26", orderContentGroupTwoStarBuns));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "27", orderContentGroupTwoStarBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "28", orderContentTwoStarBuns));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "37", orderContentThreeStarBuns));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "60", orderContentSizeOddEven));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "53", orderContentFiveStarChoose));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "38", orderContentThreeStarGroup3));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "39", orderContentThreeStarGroup6));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "40", orderContentThreeStarGroup3Compound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "33", orderContentThreeStarGroup6Compound));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "34", orderContentThreeStarGroupBuns));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "35", orderContentThreeStarGroupBrave));
//		System.out.println("恭喜，您的中奖金额为：" + service.calTicketWinningAmount("SSC", "36", orderContentThreeStarGroupDirect));
	}
	
	
}
