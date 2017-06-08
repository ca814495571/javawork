package com.cqfc.util;

/**
 * 竞技彩状态枚举定义类
 * 
 * @author liwh
 */
public class SportIssueConstant {

	/**
	 * 赛事玩法单关固定销售状态
	 * 
	 */
	public enum DgGdSaleStatus {
		IN_SALE(1, "开售"), STOP_SALE(2, "停售");

		private final Integer value;
		private final String text;

		private DgGdSaleStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static DgGdSaleStatus getEnum(Integer value) {
			if (value != null) {
				DgGdSaleStatus[] values = DgGdSaleStatus.values();
				for (DgGdSaleStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 赛事玩法单关浮动销售状态
	 * 
	 */
	public enum DgFdSaleStatus {
		IN_SALE(1, "开售"), STOP_SALE(2, "停售");

		private final Integer value;
		private final String text;

		private DgFdSaleStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static DgFdSaleStatus getEnum(Integer value) {
			if (value != null) {
				DgFdSaleStatus[] values = DgFdSaleStatus.values();
				for (DgFdSaleStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 赛事玩法过关销售状态
	 * 
	 */
	public enum GgSaleStatus {
		IN_SALE(1, "开售"), STOP_SALE(2, "停售");

		private final Integer value;
		private final String text;

		private GgSaleStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static GgSaleStatus getEnum(Integer value) {
			if (value != null) {
				GgSaleStatus[] values = GgSaleStatus.values();
				for (GgSaleStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 竞足竞篮赛事类型枚举
	 * 
	 */
	public enum CompetitionMatchType {
		FOOTBALL(1, "足球"), BASKETBALL(2, "篮球"), BEIDAN(3, "北单"), BEIDAN_SFGG(4, "北单胜负过关");

		private final Integer value;
		private final String text;

		private CompetitionMatchType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static CompetitionMatchType getEnum(Integer value) {
			if (value != null) {
				CompetitionMatchType[] values = CompetitionMatchType.values();
				for (CompetitionMatchType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 竞足竞篮赛事是否允许修改枚举
	 * 
	 */
	public enum CompetiveMatchIsAllowModify {
		MODIFY_ALLOW(1, "允许"), MODIFY_NOTALLOW(2, "不允许");

		private final Integer value;
		private final String text;

		private CompetiveMatchIsAllowModify(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static CompetiveMatchIsAllowModify getEnum(Integer value) {
			if (value != null) {
				CompetiveMatchIsAllowModify[] values = CompetiveMatchIsAllowModify.values();
				for (CompetiveMatchIsAllowModify val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 赛事状态枚举
	 * 
	 */
	public enum CompetiveMatchStatus {
		MATCH_DELAY(-3, "比赛延期"), MATCH_CANCEL(-2, "比赛取消"), MATCH_SUSPEND_SALES(-1, "比赛暂停销售"), MATCH_NOT_START(0,
				"比赛未开始"), MATCH_IN_SALES(1, "比赛销售中"), MATCH_HAS_DRAW(2, "比赛已开奖"), MATCH_IN_CALING(3, "赛事算奖中"), MATCH_HAS_CAL(
				4, "赛事已算奖");

		private final Integer value;
		private final String text;

		private CompetiveMatchStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static CompetiveMatchStatus getEnum(Integer value) {
			if (value != null) {
				CompetiveMatchStatus[] values = CompetiveMatchStatus.values();
				for (CompetiveMatchStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 竞技彩期号状态
	 * 
	 */
	public enum SportIssueStatus {
		NOT_SALE(0, "未销售"), IN_SALE(1, "销售中"), STOP_SALE(2, "已停售");

		private final Integer value;
		private final String text;

		private SportIssueStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static SportIssueStatus getEnum(Integer value) {
			if (value != null) {
				SportIssueStatus[] values = SportIssueStatus.values();
				for (SportIssueStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}
}
