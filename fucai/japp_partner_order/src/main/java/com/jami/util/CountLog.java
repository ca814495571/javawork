package com.jami.util;


public class CountLog extends Log{
	
	/**
	 * 统计操作日志
	 */
	public static LotteryLogger count = LotteryLogger.getLogger("count");
	
	
	/**
	 * 合作商批量添加入库日志，出错时从此日志中查询挂掉线程附近各个线程最早的订单时间与partnerOrder日志中的时间进行匹配，尽量将时间向前检索，保证数据完整性
	 */
	public static LotteryLogger addPartnerOrder = LotteryLogger.getLogger("addPartnerOrder");
}
