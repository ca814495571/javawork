package com.cqfc.util;

/**
 * activemq消息类型定义
 * 
 * @author liwh
 * 
 */
public interface ActivemqMethodUtil {

	// 触发扫描追号明细创建订单
	public static final String MQ_APPENDSCANORDER_METHODID = "10001";

	// 追号明细发送到bussiness请求创建订单
	public static final String MQ_APPENDCREATEORDER_METHODID = "10002";

	// 未付款订单扫描（订单状态为未付款）
	public static final String MQ_ORDERWAITPAYMENT_METHODID = "10003";

	// 出票扫描(订单状态为已付款、出票中)
	public static final String MQ_ORDERPRINT_METHODID = "10004";

	// 撤单扫描(期状态更新为已出票后)
	public static final String MQ_ORDERCANCEL_METHODID = "10005";

	// 未同步订单扫描(期状态更新为已撤单后)
	public static final String MQ_ORDERSYNC_METHODID = "10006";

	// 触发算奖事件
	public static final String MQ_CALPRIZESCAN_METHODID = "10007";

	// 扫描任务完成，将结果写入任务结果表,MQ消息接收由lotteryIssue.
	public static final String MQ_TASKCOMPLETE_METHODID = "10008";

	// 算奖结果更新到订单表
	public static final String MQ_CALAFTERUPDATEORDER_METHODID = "10009";

	// 触发派奖接口
	public static final String MQ_SENDPRIZE_METHODID = "10010";

	// 触发删除订单
	public static final String MQ_ORDERDELETE_METHODID = "10011";

	// 触发订单退款
	public static final String MQ_ORDERREFUND_METHODID = "10012";

	// 触发销量统计,渠道商、用户(期状态更新为已撤单后)
	public static final String MQ_PARTNERANDUSER_SALCOUNT_METHODID = "10013";

	// 触发统计合作商兑奖信息(期状态更新为已派奖后)
	public static final String MQ_PARTNER_CHANGEPRIZE_METHODID = "10015";

	// 销量统计结束事件
	public static final String MQ_SALECOUNT_COMPLETED_METHODID = "10016";
	// 算奖结束事件
	public static final String MQ_WINNING_COUNT_COMPLETED_METHODID = "10017";
	
	// 更改合作商信息
	public static final String MQ_PARTNERINFO_CHANGE_METHODID = "10018";
}
