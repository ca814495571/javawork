package com.cqfc.ticketwinning.util;

public class TicketWinningConstantsUtil {

	// 分隔符
	// 逗号 ,
	public final static String SEPARATOR_DOUHAO = ",";
	// 冒号 :
	public final static String SEPARATOR_MAOHAO = ":";
	// 破折号 -
	public final static String SEPARATOR_POZHEHAO = "-";
	// 井号 #
	public final static String SEPARATOR_JINGHAO = "#";
	// 竖线 |
	public final static String SEPARATOR_SHUXIAN = "|";
	// 竖线转义 |
	public final static String SEPARATOR_SHUXIAN_REGX = "\\|";
	// 美元 $
	public final static String SEPARATOR_MEIYUAN = "$";
	// 美元转义 $
	public final static String SEPARATOR_MEIYUAN_REGX = "\\$";
	// 波浪 ~
	public final static String SEPARATOR_BOLANG = "~";
	// 撇 /
	public final static String SEPARATOR_PIE = "/";
	// 乘 X(大写字母x)
	public final static String SEPARATOR_CHENG = "X";	
	// @
	public final static String SEPARATOR_AT = "@";
	// *
	public final static String SEPARATOR_XINGHAO = "*"; 
	
	// 彩种内容
	// 双色球红球
	public final static String CONTENT_SSQ_RED = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33";
	// 双色球篮球
	public final static String CONTENT_SSQ_BLUE = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16";
	// 七乐彩
	public final static String CONTENT_QLC = "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30";
	// 福彩3D
	public final static String CONTENT_FC3D = "0,1,2,3,4,5,6,7,8,9";
	// 幸运农场(植物+动物)
	public final static String CONTENT_XYNC = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
	// 幸运农场(植物)
	public final static String CONTENT_XYNC_PLANT = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18";
	// 时时彩
	public final static String CONTENT_SSC = "0,1,2,3,4,5,6,7,8,9";
	public final static String CONTENT_SSC_SIZE_ODDEVEN = "1,2,4,5";	
	
	// 分页大小
	public final static int ORDER_TABLE_PAGE_SIZE = 1000;
	// DB中表数量
	public final static int ORDER_DB_TABLE_SIZE = 100;
	// 合作商DB前缀
	public final static String PARTNET_ORDER_DB_NAME_PREFIX = "fucai_partner_order_";
	// 用户DB前缀
	public final static String USER_ORDER_DB_NAME_PREFIX = "fucai_user_order_";
	// 合作商DB中表前缀
	public final static String PARTNER_ORDER_TABLE_NAME_PREFIX = "t_partner_order_";
	// 用户DB中表前缀
	public final static String USER_ORDER_TABLE_NAME_PREFIX = "t_user_order_";
	
	// 大奖金额（派奖金额上限）
	public final static long BIG_PRIZE_AMOUNT = 1000000; 
	
	// 算奖分隔次数
	public final static int CAL_PRIZE_SPERATE_COUNT = 10;	
	
	// 竞彩赛事Id长度
	public final static int JC_MATCHID_LEN = 13;
	// 竞彩赛事分隔个数
	public final static int JC_MATCH_SPLIT_LEN = 3;
	
	// 竞彩赛事投注内容左分隔
	public final static String JC_MATCH_CONTENT_LEFT = "[";	
	// 竞彩赛事投注内容右分隔
	public final static String JC_MATCH_CONTENT_RIGHT = "]";
	
	
	// 竞彩赛事状态已算奖
	public final static int MATCH_STATUS_HAS_PRIZE = 1;
	
	// 双色球开奖号码个数
	public final static int NUM_SSQ = 7;
	// 七乐彩开奖号码个数
	public final static int NUM_QLC = 8;
	// 福彩3D开奖号码个数
	public final static int NUM_FC3D = 3;
	// 时时彩开奖号码个数
	public final static int NUM_SSC = 5;
	// 幸运农场开奖号码个数
	public final static int NUM_XYNC = 8;
	// 大乐透开奖号码个数
	public final static int NUM_DLT = 7;
	// 七星彩开奖号码个数
	public final static int NUM_QXC = 7;
	// 排列三开奖号码个数
	public final static int NUM_PLS = 3;
	// 排列五开奖号码个数
	public final static int NUM_PLW = 5;
	// 浙江11选5开奖号码个数
	public final static int NUM_ZJSYXW = 5;
	// 老足彩14场胜负彩开奖号码个数
	public final static int NUM_LZCSFC = 14;
	// 老足彩任选9开奖号码个数
	public final static int NUM_LZCR9 = 14;
	// 老足彩4场进球彩开奖号码个数
	public final static int NUM_LZC4JQS = 8;
	// 老足彩6场半全场开奖号码个数
	public final static int NUM_LZC6BQC= 12;
	
	
	//竞足竞篮混合过关字母
	public final static String JZ_SPF = "SPF";
	public final static String JZ_RQSPF = "RQSPF";
	public final static String JZ_BF = "BF";
	public final static String JZ_ZJQS = "JQS";
	public final static String JZ_BQCSPF = "BQC";
	public final static String JL_SF = "SF";
	public final static String JL_RFSF = "RFSF";
	public final static String JL_SFC = "SFC";
	public final static String JL_DXF = "DXF";
	
}
