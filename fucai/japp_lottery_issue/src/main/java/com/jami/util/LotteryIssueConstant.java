package com.jami.util;

/**
 * @author liwh
 */
public class LotteryIssueConstant {

	/**
	 * 扫描任务状态 1完成 2处理中
	 * 
	 */
	public enum TaskStatus {
		COMPLETE(1, "完成"), DEALING(2, "处理中");

		private final Integer value;
		private final String text;

		private TaskStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static TaskStatus getEnum(Integer value) {
			if (value != null) {
				TaskStatus[] values = TaskStatus.values();
				for (TaskStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

}
