package com.cqfc.util;

/**
 * 渠道商常量定义
 * 
 * @author liwh
 */
public class PartnerConstant {
	/**
	 * 渠道商类型：1B2B合作商 2B2B2C合作商 3行业合作商
	 * 
	 */
	public enum PartnerType {
		B2B(1, "B2B合作商"), B2B2C(2, "B2B2C合作商"), USERPARTNER(3, "行业合作商");

		private final Integer value;
		private final String text;

		private PartnerType(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static PartnerType getEnum(Integer value) {
			if (value != null) {
				PartnerType[] values = PartnerType.values();
				for (PartnerType val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 渠道商状态 1正常 2锁定
	 * 
	 */
	public enum PartnerState {
		NORMAL(1, "正常"), LOCK(2, "锁定");

		private final Integer value;
		private final String text;

		private PartnerState(Integer value, String text) {
			this.value = value;
			this.text = text;
		}

		public Integer getValue() {
			return value;
		}

		public String getText() {
			return text;
		}

		public static PartnerState getEnum(Integer value) {
			if (value != null) {
				PartnerState[] values = PartnerState.values();
				for (PartnerState val : values) {
					if (val.getValue().intValue() == value.intValue()) {
						return val;
					}
				}
			}
			return null;
		}
	}
}
