package com.cqfc.util;

/**
 * @author liwh
 */
public class AppendTaskConstant {

	// 库数量
	public static final int DATASOURCE_NUM = 10;

	// 每个库表的数量
	public static final int PER_DATASOURCE_TABLE_NUM = 100;

	/**
	 * 追号详情状态枚举（0等待交易 1交易中 2交易成功 3交易失败）
	 * 
	 */
	public enum DetailStatus {

		WAIT_TRADE(0, "等待交易"), TRADING(1, "交易中"), TRADE_SUCCESS(2, "交易成功"), TRADE_FAILURE(3, "交易失败"), TRADE_CANCEL(4,
				"交易取消");

		private final Integer value;
		private final String text;

		private DetailStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static DetailStatus getEnum(Integer value) {
			if (value != null) {
				DetailStatus[] values = DetailStatus.values();
				for (DetailStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 追号任务状态枚举（1追号正常 2追号完成 3追号取消）
	 * 
	 */
	public enum AppendTaskStatus {

		APPEND_NORMAL(1, "追号正常"), APPEND_COMPLETE(2, "追号完成"), APPEND_CANCEL(3, "追号取消");

		private final Integer value;
		private final String text;

		private AppendTaskStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static AppendTaskStatus getEnum(Integer value) {
			if (value != null) {
				AppendTaskStatus[] values = AppendTaskStatus.values();
				for (AppendTaskStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 追号终止条件枚举（0不停追 1中任意奖停追 2中大奖停追）
	 * 
	 */
	public enum StopAppendType {
		NOT_STOP(0, "不停追"), ANYPRIZE_STOP(1, "中任意奖停追"), BIGPRIZE_STOP(2, "中大奖停追");

		private final Integer value;
		private final String text;

		private StopAppendType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static StopAppendType getEnum(Integer value) {
			if (value != null) {
				StopAppendType[] values = StopAppendType.values();
				for (StopAppendType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 订单中奖等级枚举（1小奖 2大奖）
	 * 
	 */
	public enum OrderPrizeLevel {
		SMALLPRIZE(1, "小奖"), BIGPRIZE(2, "大奖");

		private final Integer value;
		private final String text;

		private OrderPrizeLevel(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static OrderPrizeLevel getEnum(Integer value) {
			if (value != null) {
				OrderPrizeLevel[] values = OrderPrizeLevel.values();
				for (OrderPrizeLevel val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

}
