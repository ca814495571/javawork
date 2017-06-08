package com.cqfc.util;

/**
 * @author liwh
 */
public class CancelOrderConstant {

	/**
	 * 撤单订单出票状态枚举
	 * 
	 */
	public enum OutTicketStatus {
		WAIT_PAYMENT(1, "未付款"), IN_TICKET(3, "出票中"), HASTICKET_WAITLOTTERY(4, "已出票待开奖"), DRAWER_FAILURE(5, "出票失败");

		private final Integer value;
		private final String text;

		private OutTicketStatus(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static OutTicketStatus getEnum(Integer value) {
			if (value != null) {
				OutTicketStatus[] values = OutTicketStatus.values();
				for (OutTicketStatus val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

}
