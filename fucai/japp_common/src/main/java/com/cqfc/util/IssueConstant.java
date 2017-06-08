package com.cqfc.util;

/**
 * @author liwh
 * 
 */
public class IssueConstant {

	// 双色球
	public static final String LOTTERYID_SSQ = "SSQ";
	// 时时彩
	public static final String LOTTERYID_SSC = "CQSSC";
	// 幸运农场
	public static final String LOTTERYID_XYNC = "CQKL10";
	// 七乐彩
	public static final String LOTTERYID_QLC = "QLC";
	// 福彩3D
	public static final String LOTTERYID_FC3D = "D3";
	// 大乐透
	public static final String LOTTERYID_DLT = "DLT";
	// 七星彩
	public static final String LOTTERYID_QXC = "QXC";
	// 排列3
	public static final String LOTTERYID_PLS = "PLS";
	// 排列5
	public static final String LOTTERYID_PLW = "PLW";
	// 浙江11选5
	public static final String LOTTERYID_ZJSYXW = "ZJSYXW";

	// 未销售
	public final static int ISSUE_STATUS_NOT_SELL = 1;
	// 销售中
	public final static int ISSUE_STATUS_SELLING = 2;
	// 销售截止
	public final static int ISSUE_STATUS_END_SELL = 3;
	// 保底状态（ 暂时没有合买，用不上保底这个状态，预留）
	public final static int ISSUE_STATUS_AEGIS = 4;
	// 已出票
	public final static int ISSUE_STATUS_PRINT = 5;
	// 已撤单
	public final static int ISSUE_STATUS_CANCEL = 6;
	// 已转移
	public final static int ISSUE_STATUS_TRANSFER = 7;
	// 已开奖
	public final static int ISSUE_STATUS_DRAW = 8;
	// 过关中
	public final static int ISSUE_STATUS_PASSING = 9;
	// 已过关
	public final static int ISSUE_STATUS_PASS = 10;
	// 过关已审核
	public final static int ISSUE_STATUS_CHECK_PASS = 11;
	// 算奖中
	public final static int ISSUE_STATUS_CALING = 12;
	// 已算奖待审核
	public final static int ISSUE_STATUS_HASCAL_WAITAUDIT = 13;
	// 已算奖审核中
	public final static int ISSUE_STATUS_AUDITTING = 14;
	// 算奖已审核
	public final static int ISSUE_STATUS_CAN_SEND = 15;
	// 派奖中
	public final static int ISSUE_STATUS_SENDING = 16;
	// 已派奖
	public final static int ISSUE_STATUS_SENDPRIZE_ONLINE = 17;

	// 扫描任务完成记录表 -- 6已撤单
	public final static int TASK_COMPLETE_CANCEL = 6;
	// 扫描任务完成记录表 -- 13已算奖待审核
	public final static int TASK_COMPLETE_HASCAL_WAITAUDIT = 13;
	// 扫描任务完成记录表 -- 15算奖已审核(算奖结果表记录已更新到订单表中)
	public final static int TASK_COMPLETE_CAN_SEND = 15;
	// 扫描任务完成记录表 --17已派奖
	public final static int TASK_COMPLETE_SENDPRIZE = 17;
	// 销量统计任务完成记录表 --20渠道、用户销量统计完成
	public final static int TASK_COMPLETE_PARTNER_USER_STATISTICS = 20;
	// 统计合作商中奖信息任务完成记录表 --22统计合作商中奖信息
	public final static int TASK_COMPLETE_PARTNER_WINNINGCAL = 22;
	// 扫描任务完成记录表 --23订单同步完成
	public final static int TASK_COMPLETE_SYNC_ORDER = 23;

	/**
	 * 竞技彩彩种枚举
	 * 
	 */
	public enum SportLotteryType {
		BEIDAN_SF("BDSF", "单场胜负过关"), 
		BEIDAN_XBC("BDXBC", "单场下半场比分"), 
		BEIDAN_SPF("BDSPF", "单场胜平负"), 
		BEIDAN_SXDS("BDSXDS", "单场上下单双"), 
		BEIDAN_BQC("BDBQC", "单场半全场"), 
		BEIDAN_JQS("BDJQS", "单场进球数"), 
		BEIDAN_BF("BDBF", "单场正确比分"), 
		LZC_SF("ZCSF", "足彩胜负彩14场"), 
		LZC_R9("ZCR9", "足彩任选9场"), 
		LZC_6BQC("ZC6BQC", "足彩6场半全场"), 
		LZC_4JQS("ZC4JQS", "足彩4场进球彩"), 
		JCZQ_SPF("JZSPF", "竞彩足球胜平负"), 
		JCZQ_BF("JZBF", "竞彩足球比分"), 
		JCZQ_JQS("JZJQS", "竞彩足球总进球数"), 
		JCZQ_BQC("JZBQC", "竞彩足球半全场胜平负"), 
		JCZQ_HHGG("JZHHGG", "竞彩足球混合过关"), 
		JCZQ_RQSPF("JZRQSPF", "竞彩足球让球胜平负"), 
		GYJ_JC("GYJJC", "冠亚军竞猜"), 
		JCLQ_SF("JLSF", "竞彩篮球胜负"), 
		JCLQ_RFSF("JLRFSF", "竞彩篮球让分胜负"), 
		JCLQ_SFC("JLSFC", "竞彩篮球胜分差"), 
		JCLQ_DXF("JLDXF", "竞彩篮球大小分"), 
		JCLQ_HHGG("JLHHGG", "竞彩篮球混合过关");

		private final String value;
		private final String text;

		private SportLotteryType(String value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static SportLotteryType getEnum(String value) {
			if (value != null) {
				SportLotteryType[] values = SportLotteryType.values();
				for (SportLotteryType val : values) {
					if (val.getValue().equals(value)) {
						return val;
					}
				}
			}
			return null;
		}
	}

	// 竞足竞篮期号常量
	public final static String SPORT_ISSUE_CONSTANT = "20110501";
	// 北单赛事获取所有玩法标识常量
	public final static String MATCHPLAY_BEIDAN_ALL = "BD";
	// 竞足竞篮算奖彩种标识
	public final static String CAL_PRIZE_SPORT_LOTTERYID = "SPORT_JZJL";
	// 北单算奖彩种标识
	public final static String CAL_PRIZE_BD_LOTTERYID = "SPORT_BD";

}
