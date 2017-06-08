package com.cqfc.ticketwinning.util;

public interface PrizeAmount {
	
	//双色球奖级
	public static final int SSQ_SIXTH_PRIZE_LEVEL  = 6;
	public static final int SSQ_FIFTH_PRIZE_LEVEL  = 5;
	public static final int SSQ_FOURTH_PRIZE_LEVEL  = 4;
	public static final int SSQ_THIRD_PRIZE_LEVEL = 3;
	public static final int SSQ_SECOND_PRIZE_LEVEL = 2;
	public static final int SSQ_FIRST_PRIZE_LEVEL = 1;
	
	//大乐透奖级
	public static final int DLT_FIRST_PRIZE_LEVEL = 1;
	public static final int DLT_FIRST_PRIZE_APPEND_LEVEL = 2;
	public static final int DLT_SECOND_PRIZE_LEVEL = 3;
	public static final int DLT_SECOND_PRIZE_APPEND_LEVEL = 4;
	public static final int DLT_THIRD_PRIZE_LEVEL = 5;
	public static final int DLT_THIRD_PRIZE_APPEND_LEVEL = 6;
	public static final int DLT_FOURTH_PRIZE_LEVEL  = 7;
	public static final int DLT_FOURTH_PRIZE_APPEND_LEVEL  = 8;
	public static final int DLT_FIFTH_PRIZE_LEVEL  = 9;
	public static final int DLT_FIFTH_PRIZE_APPEND_LEVEL  = 10;
	public static final int DLT_SIXTH_PRIZE_LEVEL = 11;
	
	
	//七乐彩奖级
	public static final int QLC_SEVENTH_PRIZE_LEVEL  = 7;
	public static final int QLC_SIXTH_PRIZE_LEVEL  = 6;
	public static final int QLC_FIFTH_PRIZE_LEVEL  = 5;
	public static final int QLC_FOURTH_PRIZE_LEVEL  = 4;
	public static final int QLC_THIRD_PRIZE_LEVEL = 3;
	public static final int QLC_SECOND_PRIZE_LEVEL = 2;
	public static final int QLC_FIRST_PRIZE_LEVEL = 1;
	
	//福彩3D奖级
	//单选
	public static final int FC3D_RADIO_PRIZE_LEVEL = 1;
	//组三
	public static final int FC3D_GROUP3_PRIZE_LEVEL = 2;
	//组六
	public static final int FC3D_GROUP6_PRIZE_LEVEL = 3;	
	//1D
	public static final int FC3D_1D_PRIZE_LEVEL = 4;
	//2D
	public static final int FC3D_2D_PRIZE_LEVEL = 5;
	//和数0或27
	public static final int FC3D_ANDVALUE_0OR27_PRIZE_LEVEL = 6;
	//和数1或26
	public static final int FC3D_ANDVALUE_1OR26_PRIZE_LEVEL = 7;
	//和数2或25
	public static final int FC3D_ANDVALUE_2OR25_PRIZE_LEVEL = 8;
	//和数3或24
	public static final int FC3D_ANDVALUE_3OR24_PRIZE_LEVEL = 9;
	//和数4或23
	public static final int FC3D_ANDVALUE_4OR23_PRIZE_LEVEL = 10;
	//和数5或22
	public static final int FC3D_ANDVALUE_5OR22_PRIZE_LEVEL = 11;
	//和数6或21
	public static final int FC3D_ANDVALUE_6OR21_PRIZE_LEVEL = 12;
	//和数7或20
	public static final int FC3D_ANDVALUE_7OR20_PRIZE_LEVEL = 13;
	//和数8或19
	public static final int FC3D_ANDVALUE_8OR19_PRIZE_LEVEL = 14;
	//和数9或18
	public static final int FC3D_ANDVALUE_9OR18_PRIZE_LEVEL = 15;
	//和数10或17
	public static final int FC3D_ANDVALUE_10OR17_PRIZE_LEVEL = 16;
	//和数11或16
	public static final int FC3D_ANDVALUE_11OR16_PRIZE_LEVEL = 17;
	//和数12或15
	public static final int FC3D_ANDVALUE_12OR15_PRIZE_LEVEL = 18;
	//和数13或14
	public static final int FC3D_ANDVALUE_13OR14_PRIZE_LEVEL = 19;
	//通选一等奖
	public static final int FC3D_COMMON_FIRST_PRIZE_LEVEL = 20;
	//通选二等奖
	public static final int FC3D_COMMON_SECOND_PRIZE_LEVEL = 21;
	//猜1D中1
	public static final int FC3D_GUESS1D_1_PRIZE_LEVEL = 22;
	//猜1D中2
	public static final int FC3D_GUESS1D_2_PRIZE_LEVEL = 23;
	//猜1D中3
	public static final int FC3D_GUESS1D_3_PRIZE_LEVEL = 24;
	//猜2D两同号
	public static final int FC3D_GUESS2D_2_SAME_PRIZE_LEVEL = 25;
	//猜2D两不同号
	public static final int FC3D_GUESS2D_2_DIFF_PRIZE_LEVEL = 26;
	//包选3全中
	public static final int FC3D_ALL_3_PRIZE_LEVEL = 27;
	//包选3组中
	public static final int FC3D_ALL_GROUP_3_PRIZE_LEVEL = 28;
	//包选6全中
	public static final int FC3D_ALL_6_PRIZE_LEVEL = 29;
	//包选6组中
	public static final int FC3D_ALL_GROUP_6_PRIZE_LEVEL = 30;
	//猜大小
	public static final int FC3D_GUESS_BIG_SMALL_PRIZE_LEVEL = 31;
	//猜三同
	public static final int FC3D_GUESS_3_SAME_PRIZE_LEVEL = 32;
	//拖拉机
	public static final int FC3D_TACTOR_PRIZE_LEVEL = 33;
	//猜奇偶
	public static final int FC3D_GUESS_ODD_EVEN_PRIZE_LEVEL = 34;
	
	//幸运农场奖级
	//果蔬单选(选一数投)
	public static final int XYNC_CHOOSEONE_SHOTS_PRIZE_LEVEL = 1;
	//动物单选(选一红投)
	public static final int XYNC_CHOOSEONE_RED_PRIZE_LEVEL = 2;
	//背靠背(选二连组中二)
	public static final int XYNC_CHOOSE_TWO_UNORDER_PRIZE_LEVEL = 4;
	//连连中(选二连直中二)
	public static final int XYNC_CHOOSE_TWO_ORDER_PRIZE_LEVEL = 5;
	//幸运二(选二任选中二)
	public static final int XYNC_CHOOSE_TWO_ANY_PRIZE_LEVEL = 3;
	//幸运三(选三任选中三)
	public static final int XYNC_CHOOSE_THREE_ANY_PRIZE_LEVEL = 6;
	//幸运四(选四任选中四)
	public static final int XYNC_CHOOSE_FOUR_ANY_PRIZE_LEVEL = 9;
	//幸运五(选五任选中五)
	public static final int XYNC_CHOOSE_FIVE_ANY_PRIZE_LEVEL = 10;
	//三连中(选三前直中三)
	public static final int XYNC_CHOOSETHREE_STRAIGHT_PRIZE_LEVEL = 8;
	//三全中(选三前组中三)
	public static final int XYNC_CHOOSETHREE_ALL_PRIZE_LEVEL = 7;

	
	
	//时时彩奖金
	//单式一星
	public static final long SSC_RADIO_ONESTAR_PRIZE = 1000;
	//单式二星
	public static final long SSC_RADIO_TWOSTAR_PRIZE = 10000;
	//单式三星
	public static final long SSC_RADIO_THREESTAR_PRIZE = 100000;
	//单式四星
	public static final long SSC_RADIO_FOURSTAR_PRIZE = 1000000;
	//单式五星
	public static final long SSC_RADIO_FIVESTAR_PRIZE = 10000000;
	//二星组选
	public static final long SSC_GROUP_TWOSTAR_PRIZE = 5000;
	//二星组选(对子)
	public static final long SSC_GROUP_TWOSTAR_DUIZI_PRIZE = 10000;
	//大小单双
	public static final long SSC_SIZE_ODDEVEN_PRIZE = 400;
	//三星组三
	public static final long SSC_GROUP_THREE_PRIZE = 32000;
	//三星组六
	public static final long SSC_GROUP_SIX_PRIZE = 16000;
	//三星组选包点豹子
	public static final long SSC_GROUP_THREE_BAOZI_PRIZE = 100000;
	//五星通选全中
	public static final long SSC_CHOOSE_FIVESTAR_PRIZE = 2000000;
	//五星通选中三
	public static final long SSC_CHOOSE_THREESTAR_PRIZE = 20000;
	//五星通选中二
	public static final long SSC_CHOOSE_TWOSTAR_PRIZE = 2000;	
	
	
	//时时彩奖金
	//单式一星
	public static final int SSC_RADIO_ONESTAR_PRIZE_LEVEL = 4;
	//单式二星
	public static final int SSC_RADIO_TWOSTAR_PRIZE_LEVEL = 3;
	//单式三星
	public static final int SSC_RADIO_THREESTAR_PRIZE_LEVEL = 2;
	//单式四星
	public static final int SSC_RADIO_FOURSTAR_PRIZE_LEVEL = 1000000;
	//单式五星
	public static final int SSC_RADIO_FIVESTAR_PRIZE_LEVEL = 1;
	//二星组选
	public static final int SSC_GROUP_TWOSTAR_PRIZE_LEVEL = 6;
	//二星组选(对子)
	public static final int SSC_GROUP_TWOSTAR_DUIZI_PRIZE_LEVEL = 10000;
	//大小单双
	public static final int SSC_SIZE_ODDEVEN_PRIZE_LEVEL = 5;
	//三星组三
	public static final int SSC_GROUP_THREE_PRIZE_LEVEL = 10;
	//三星组六
	public static final int SSC_GROUP_SIX_PRIZE_LEVEL = 11;
	//三星组选包点豹子
	public static final int SSC_GROUP_THREE_BAOZI_PRIZE_LEVEL = 100000;
	//五星通选全中
	public static final int SSC_CHOOSE_FIVESTAR_PRIZE_LEVEL = 7;
	//五星通选中三
	public static final int SSC_CHOOSE_THREESTAR_PRIZE_LEVEL = 8;
	//五星通选中二
	public static final int SSC_CHOOSE_TWOSTAR_PRIZE_LEVEL = 9;	
	
	
	//七星彩奖级
	public static final int QXC_SIXTH_PRIZE_LEVEL  = 6;
	public static final int QXC_FIFTH_PRIZE_LEVEL  = 5;
	public static final int QXC_FOURTH_PRIZE_LEVEL  = 4;
	public static final int QXC_THIRD_PRIZE_LEVEL = 3;
	public static final int QXC_SECOND_PRIZE_LEVEL = 2;
	public static final int QXC_FIRST_PRIZE_LEVEL = 1;
	
	//排列3奖级
	//排列三直选
	public static final int PL3_ZHIXUAN_LEVEL = 1;
	//排列三组三
	public static final int PL3_ZUSAN_LEVEL = 2;
	//排列三组六
	public static final int PL3_ZULIU_LEVEL = 3;
	
	//排列5奖级
	//排列五直选
	public static final int PL5_FIRST_PRIZE_LEVEL = 1;
	
	//11选5
	//任选二
	public static final int SYX5_REN_XUAN2_LEVEL = 1;
	//任选三
	public static final int SYX5_REN_XUAN3_LEVEL = 2;
	//任选四
	public static final int SYX5_REN_XUAN4_LEVEL = 3;
	//任选五
	public static final int SYX5_REN_XUAN5_LEVEL = 4;
	//任选六
	public static final int SYX5_REN_XUAN6_LEVEL = 5;
	//任选七
	public static final int SYX5_REN_XUAN7_LEVEL = 6;
	//任选八
	public static final int SYX5_REN_XUAN8_LEVEL = 7;
	//前一直选
	public static final int SYX5_ZHI_XUAN1_LEVEL = 8;
	//前二直选
	public static final int SYX5_ZHI_XUAN2_LEVEL = 9;
	//前二组选
	public static final int SYX5_ZU_XUAN2_LEVEL = 10;
	//前三直选
	public static final int SYX5_ZHI_XUAN3_LEVEL = 11;
	//前三组选
	public static final int SYX5_ZU_XUAN3_LEVEL = 12;
	
	
	//老足彩
	//胜负彩14场一等奖
	public static final int ZC_SFC14C_FIRST_PRIZE_LEVEL = 1;
	//胜负彩14场二等奖
	public static final int ZC_SFC14C_SECOND_PRIZE_LEVEL = 2;
	//任选9场一等奖
	public static final int ZC_RX9C_FIRST_PRIZE_LEVEL = 3;
	//6场半全场一等奖
	public static final int ZC_6CBQC_FIRST_PRIZE_LEVEL = 1;
	//4场进球彩一等奖
	public static final int ZC_4CJQC_FIRST_PRIZE_LEVEL = 1;
	
}
