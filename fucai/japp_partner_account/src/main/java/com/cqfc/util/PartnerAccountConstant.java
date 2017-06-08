package com.cqfc.util;

/**
 * @author liwh
 */
public class PartnerAccountConstant {

	/**
	 * 渠道账户流水日志类型枚举
	 * 
	 */
	public enum PartnerAccountLogType {

		RECHARGE(1, "充值"), PAYMENT(2, "支付"), REFUND(4, "退款"), PRIZE(5, "派奖"), ROLLBACK(6,"重复支付或重付退款回滚"), USERPREAPPLY(9, "用户预存款");

		private final Integer value;
		private final String text;

		private PartnerAccountLogType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static PartnerAccountLogType getEnum(Integer value) {
			if (value != null) {
				PartnerAccountLogType[] values = PartnerAccountLogType.values();
				for (PartnerAccountLogType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 账户状态枚举
	 * 
	 */
	public enum PartnerAccountState {

		NORMAL(1, "正常"), FREEZE(2, "冻结");

		private final Integer value;
		private final String text;

		private PartnerAccountState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static PartnerAccountState getEnum(Integer value) {
			if (value != null) {
				PartnerAccountState[] values = PartnerAccountState.values();
				for (PartnerAccountState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 渠道账户预存款申请状态
	 * 
	 */
	public enum PartnerPreApplyState {

		NOT_AUDIT(0, "未审核"), AUDIT_PASS(1, "审核通过"), AUDIT_NOPASS(2, "审核不通过");

		private final Integer value;
		private final String text;

		private PartnerPreApplyState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static PartnerPreApplyState getEnum(Integer value) {
			if (value != null) {
				PartnerPreApplyState[] values = PartnerPreApplyState.values();
				for (PartnerPreApplyState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}
}
