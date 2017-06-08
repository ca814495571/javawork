package com.cqfc.util;

/**
 * @author liwh
 * 
 */
public class UserAccountConstant {

	/**
	 * 用户账户流水日志类型枚举
	 * 
	 */
	public enum UserAccountLogType {

		RECHARGE(1, "充值"), PAYMENT(2, "支付"), WITHDRAW(3, "提现"), REFUND(4, "退款"), PRIZE(5, "派奖"), HANDSEL_PRESENT(6,
				"彩金赠送"), HANDSEL_FAILURE(7, "彩金失效"), FREEZE_AMOUNT(8, "冻结金额"), USERPREAPPLY(9, "用户预存款");

		private final Integer value;
		private final String text;

		private UserAccountLogType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserAccountLogType getEnum(Integer value) {
			if (value != null) {
				UserAccountLogType[] values = UserAccountLogType.values();
				for (UserAccountLogType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
		
		public static String  getLogTypeName(Integer value){
			
			if (value != null) {
				UserAccountLogType[] values = UserAccountLogType.values();
				for (UserAccountLogType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val.getText();
					}
				}
			}
			return null;
			
			
		}
		
	}

	/**
	 * 用户账户状态枚举
	 * 
	 */
	public enum UserAccountState {

		NORMAL(1, "正常"), FREEZE(2, "冻结");

		private final Integer value;
		private final String text;

		private UserAccountState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserAccountState getEnum(Integer value) {
			if (value != null) {
				UserAccountState[] values = UserAccountState.values();
				for (UserAccountState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 帐号类型
	 * 
	 */
	public enum UserAccountType {

		QQ(1, "qq"), WX(2, "微信");

		private final Integer value;
		private final String text;

		private UserAccountType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserAccountType getEnum(Integer value) {
			if (value != null) {
				UserAccountType[] values = UserAccountType.values();
				for (UserAccountType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}
	
	/**
	 * 注册终端
	 */
	public enum UserRegisterTerminal {

		MOBILE(1, "手机"), PC(2, "PC");

		private final Integer value;
		private final String text;

		private UserRegisterTerminal(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserRegisterTerminal getEnum(Integer value) {
			if (value != null) {
				UserRegisterTerminal[] values = UserRegisterTerminal.values();
				for (UserRegisterTerminal val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 注册终端
	 */
	public enum UserCardType {

		IDENTIFICATION_CARD (1, "身份证"), PASSPORT(2, "护照"), OFFICER_CARD(3, "军官证"), HK_0R_TW_COMPATRIOT_CERTIFICATE(4, "港台同胞证");

		private final Integer value;
		private final String text;

		private UserCardType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserCardType getEnum(Integer value) {
			if (value != null) {
				UserCardType[] values = UserCardType.values();
				for (UserCardType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}
	/**
	 * 彩金状态枚举
	 * 
	 */
	public enum UserHandselState {

		VALID(1, "有效"), INVALID(2, "无效");

		private final Integer value;
		private final String text;

		private UserHandselState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserHandselState getEnum(Integer value) {
			if (value != null) {
				UserHandselState[] values = UserHandselState.values();
				for (UserHandselState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 提现审核状态枚举
	 * 
	 */
	public enum WithdrawAuditState {

		NOT_AUDIT(1, "未审核"), AUDIT_PASS(2, "审核通过"), AUDIT_NOPASS(3, "审核不通过");

		private final Integer value;
		private final String text;

		private WithdrawAuditState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static WithdrawAuditState getEnum(Integer value) {
			if (value != null) {
				WithdrawAuditState[] values = WithdrawAuditState.values();
				for (WithdrawAuditState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 预存款审核状态枚举
	 * 
	 */
	public enum UserPreApplyAuditState {

		NOT_AUDIT(1, "未审核"), AUDIT_PASS(2, "审核通过"), AUDIT_NOPASS(3, "审核不通过");

		private final Integer value;
		private final String text;

		private UserPreApplyAuditState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static UserPreApplyAuditState getEnum(Integer value) {
			if (value != null) {
				UserPreApplyAuditState[] values = UserPreApplyAuditState.values();
				for (UserPreApplyAuditState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}
}
