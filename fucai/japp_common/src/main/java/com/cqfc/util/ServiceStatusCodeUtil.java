package com.cqfc.util;

/**
 * @author liwh
 * 
 */
public class ServiceStatusCodeUtil {

	/**
	 * 操作成功
	 */
	public static final int STATUS_CODE_OPERATE_SUCCESS = 1;

	/**
	 * 数据库错误
	 */
	public static final int STATUS_CODE_DB_ERROR = -1;

	/**
	 * 用户不存在
	 */
	public static final int STATUS_CODE_USER_NOTEXIST = -2;

	/**
	 * 提现申请记录不存在
	 */
	public static final int STATUS_CODE_WITHDRAWAPPLY_NOTEXIST = -3;

	/**
	 * 提现申请记录已审核
	 */
	public static final int STATUS_CODE_WITHDRAWAPPLY_ISAUDIT = -4;

	/**
	 * 支付流水日志不存在
	 */
	public static final int STATUS_CODE_PAYACCOUNTLOG_NOTEXIST = -5;

	/**
	 * 退款金额已超过该支付订单支付总额
	 */
	public static final int STATUS_CODE_REFUND_OVERPAY = -6;

	/**
	 * 插入数据库的数据已存在（违反唯一约束）
	 */
	public static final int STATUS_CODE_INSERT_ISEXIST = -100;

	/**
	 * 订单号不存在
	 */
	public static final int STATUS_CODE_ORDERNO_NOEXIST = -7;

	/**
	 * 用户已存在
	 */
	public static final int STATUS_CODE_USER_ISEXIST = -8;

	/**
	 * 查询列表为空
	 */
	public static final int STATUS_LIST_IS_NULL = -9;

	/**
	 * 提现帐号错误
	 */
	public static final int STATUS_CODE_WITHDRAWACCOUNT_ERROR = -10;

	/**
	 * 用户预存款申请已审核过
	 */
	public static final int STATUS_CODE_USERPREAPPLY_ISAUDIT = -11;

	/**
	 * 渠道账户预存款申请已审核过
	 */
	public static final int STATUS_CODE_PARTNERPREAPPLY_ISAUDIT = -12;

	/**
	 * 帐户资金不足
	 */
	public static final int STATUS_CODE_ACCOUNT_MONEY_NOT_ENOUGH = -13;
	
	/**
	 * 帐户资金被冻结
	 */
	public static final int STATUS_CODE_ACCOUNT_BE_FREEZED = -14;
}
