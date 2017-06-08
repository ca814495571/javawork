package com.cqfc.util;

/**
 * 全局订单库订单状态常量
 */
public class OrderStatus {
	/**
	 * 待付款
	 */
//	public final static int WATIT_TO_PAY = 1;
	/**
	 * 已付款
	 */
//	public final static int FINISH_PAY = 2;
	/**
	 * 出票中
	 */
//	public final static int IN_THE_TICKET = 3;
	/**
	 * 已出票待开奖
	 */
	public final static int FINISH_TICKET_WAIT_PRIZE = 4;
	/**
	 * 出票失败
	 */
//	public final static int FAIL_TO_TICKET = 5;
	/**
	 * 未中奖
	 */
	public final static int NOT_WIN_PRIZE = 6;
	/**
	 * 待领奖
	 */
	public final static int WAIT_TO_AWARD_PRIZE = 7;
	/**
	 * 已领奖
	 */
	public final static int FINISH_AWARD_PRIZE = 8;
	/**
	 * 退款中
	 */
//	public final static int RETURNING_MONGEY = 9;
	/**
	 * 退款成功
	 */
	public final static int SUC_RETURNING_MONGEY = 10;
	/**
	 * 订单取消
	 */
	public final static int CANCEL_ORDER = 11;

	/**
	 * 算奖中
	 */
	public final static int CALCULATTING_PRIZE = 12;

	
	public enum OrderType{
		
		DIERCT_BUY(1,"直投"),
		APPEND_BUY(2,"追号");
		
		public Integer key;
		public String desc;
		OrderType(Integer key,String desc){
			this.key = key;
			this.desc = desc;
		}
		public Integer getKey() {
			return key;
		}
		public String getDesc() {
			return desc;
		}
		
	}
	

	
	public static final String  JC_CODE = "JCDetail";
	/**
	 * 彩票种类
	 */
	public static enum LotteryType {



		JJZC_GAME(1, "竞足"),

		JJLC_GAME(2, "竞蓝"),

		JJBD_GAME(3, "北单"),

		JJBDSFGG_GAME(4, "北单胜负过关"),

		JJLZC_GAME(5, "老足彩"),

		JJAYC_GAME(6, "奥运彩"),
		
		NUMBER_GAME(10, "数字彩"),

		SPORTS_GAME(20, "竞技彩");
		
		public int type;

		public String desc;

		private LotteryType(int type, String desc) {
			this.type = type;
			this.desc = desc;
		}

		public int getType() {
			return type;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(OrderType.APPEND_BUY.getDesc());
	}
}
