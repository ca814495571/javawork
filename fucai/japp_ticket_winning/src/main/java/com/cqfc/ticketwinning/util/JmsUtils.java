package com.cqfc.ticketwinning.util;

/**
 * @author liwh
 */
public class JmsUtils {

	// 触发算奖接口
	public static final int OPERATE_CALPRIZE = 1;

	// 触发将算奖结果更新到订单表前,先修改期号状态为已算奖审核中
	public static final int OPERATE_UPDATEORDER = 2;

	// 触发派奖接口
	public static final int OPERATE_SENDPRIZE = 3;

}
