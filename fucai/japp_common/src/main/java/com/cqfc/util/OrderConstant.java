package com.cqfc.util;

/**
 * 定时器触发事件订单相关常量定义
 * 
 * @author liwh
 */
public class OrderConstant {
	// 库数量
	public static final int DATASOURCE_NUM = 10;

	// 每个库表的数量
	public static final int PER_DATASOURCE_TABLE_NUM = 100;

	// 未付款订单扫描（订单状态为未付款）
	public static final int ORDER_PAYMENT_CHECK = 1;

	// 出票扫描(订单状态为已付款、出票中)
	public static final int ORDER_PRINT_CHECK = 2;

	// 期号状态更新为已出票后，进行撤单操作
	public static final int ORDER_CANCEL_CHECK = 3;

	// 期号状态更新为已撤单后，进行未同步到全局数据库的订单进行同步扫描
	public static final int ORDER_SYNC_CHECK = 4;

	// 期号状态更新为已派奖后，且订单创建超过5天，已同步到全局数据库，则删除订单
	public static final int ORDER_DELETE_CHECK = 5;

	// 触发退款订单扫描(扫描订单状态为出票失败、退款中,再加上时间控制)
	public static final int ORDER_REFUND_CHECK = 6;

	// 投注订单支付流水号前缀
	public static final String ORDER_PAYSERIANUMBER_PREFIX = "BP";

	// 投注订单退款流水号前缀
	public static final String ORDER_REFUNDSERIANUMBER_PREFIX = "BR";

	// 追号任务冻结金额流水号前缀
	public static final String APPEND_FREEZESERIANUMBER_PREFIX = "AF";

	// 追号任务扣除冻结金额流水号前缀
	public static final String APPEND_DEDUCTFREEZESERIANUMBER_PREFIX = "AD";

	// 追号任务退还冻结金额流水号前缀
	public static final String APPEND_REFUNDFREEZESERIANUMBER_PREFIX = "AR";

	// 派奖流水号前缀
	public static final String SEND_PRIZE__PREFIX = "SP";

}
