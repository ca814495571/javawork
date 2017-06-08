package com.cqfc.order.util;

/**
 * @author liwh
 */
public class OrderDynamicUtil {

	public static final String MASTER = "master";

	public static final String SLAVE = "slave";

	/**
	 * 数字彩DB前缀
	 */
	public static final String PRE_DBNAME = "fucai_order_";

	/**
	 * 数字彩表前缀
	 */
	public static final String PRE_TABLENAME = "t_lottery_order_";

	/**
	 * 数字彩批量写DB选择源
	 */
	public static final String BATCH_MASTER_BEAN = "master_order_";

	/**
	 * 竞技彩批量写DB选择源
	 */
	public static final String BATCH_COMPETITION_MASTER_BEAN = "master_competition_order_";

	/**
	 * 竞技彩DB前缀
	 */
	public static final String PRE_COMPETITION_DBNAME = "fucai_competition_order_";

	/**
	 * 竞技彩主表前缀
	 */
	public static final String PRE_COMPETITION_TABLENAME_MAIN = "t_lottery_order_sport_";

	/**
	 * 竞技彩细表前缀
	 */
	public static final String PRE_COMPETITION_TABLENAME_DETAIL = "t_lottery_order_sport_detail_";

}
