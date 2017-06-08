package com.jami.util;

public class CountLog extends Log{
	
	
	/**
	 * 期销量，兑奖,日销量、兑奖、提现、充值统计日志
	 */
	public static LotteryLogger count = LotteryLogger.getLogger("count");
	
	/**
	 * 用户订单添加日志
	 */
	public static LotteryLogger userOrder = LotteryLogger.getLogger("userOrder");
	
	/**
	 * 合作商批量添加入库日志，出错时从此日志中查询挂掉线程附近各个线程最早的订单时间与userOrder日志中的时间进行匹配，尽量将时间向前检索(10分钟)，保证数据完整性
	 */
	public static LotteryLogger addUserOrder = LotteryLogger.getLogger("addUserOrder");
}
