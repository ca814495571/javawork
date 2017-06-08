package com.cqfc.statistics.common;

public interface IConstantUtil {

	public static final int CONST_ZORE = 0;
	
	public static final int CONST_ONE = 1;
	
	public static final int CONST_TWO = 2;
	
	public static final int CONST_THREE = 3;
	
	public static final int CONST_FOURE = 4;
	
	public static final int CONST_FIVE = 5;
	
	public static final int CONST_SIX = 6;
	
	public static final int CONST_SEVEN = 7;
	
	public static final int CONST_EIGHT = 8;
	
	public static final int CONST_NINE = 9;
	
	public static final int CONST_TEN = 10;
	
	/**
	 * 没有绑定投注站的编码
	 */
	public static final String VIRTUAL_STATION = "0";

	/**
	 * 没有绑定投注站的名称
	 */
	public static final String WEB_USER = "网络用户";

	public static final Integer AMOUNT_1000 = new Integer(1000);

	public static final Integer AMOUNT_500 = new Integer(500);

	

	/**
	 *操作类型
	 */
	public static enum ActionType{
		
		USER_BUY (CONST_ONE,"购彩"),
		
		USER_RECHARGE (CONST_TWO,"充值"),
		
		USER_WITHDRAW (CONST_THREE,"提现"),
		
		USER_APPEND(CONST_FOURE,"追号"),
		
		USER_PAY (CONST_FIVE,"支付"),
		
		USER_INFO (CONST_SIX,"用户基本信息"),
		
		USER_FINANCE (CONST_SEVEN,"用户账户金额信息"),
		
		USER_ACCOUNT (CONST_EIGHT,"用户账号信息"),
		
		USER_BIND (CONST_NINE,"用户账号绑定信息"),
		
		USER_ACTIVITY(CONST_TEN,"活动")
		;
		public int type;
		
		public String desc;
		
		private ActionType(int type,String desc){
			this.type = type;
			this.desc = desc;
		}
		
		public int getType(){
			return type;
		}
		
		public String getDesc(){
			return desc;
		}
		
	    public static String getDesc(int type) {
            for (ActionType actionType : ActionType.values()) {
                if (actionType.getType() == type) {
                    return actionType.desc;
                }
            }
            return null;
        }
	}
	
	/**
	 * 活动类型
	 *
	 */
	public static enum ActivityType{
		
		ACTIVITY_CHECKIN (CONST_ONE,"签到"),
		
		ACTIVITY_PRESENT (CONST_TWO,"赠彩"),
		
		ACTIVITY_RECIEVE (CONST_THREE,"获彩"),
		
		ACTIVITY_RETURN(CONST_FOURE,"返现"),
		;
		public int type;
		
		public String desc;
		
		private ActivityType(int type,String desc){
			this.type = type;
			this.desc = desc;
		}
		
		public int getType(){
			return type;
		}
		
		public String getDesc(){
			return desc;
		}
		
	    public static String getDesc(int type) {
            for (ActionType actionType : ActionType.values()) {
                if (actionType.getType() == type) {
                    return actionType.desc;
                }
            }
            return null;
        }
	}
	
	/**
	 * 
	 *入库唯一键约束异常处理
	 */
	public static enum duplicateKeyExceptionType{
		
		NO_DEAL (CONST_ONE,"不作处理"),
		
		REPLACEINTO (CONST_TWO,"替换"),
		
		UPDATE (CONST_TWO,"修改"),
		;
		public int type;
		
		public String desc;
		
		private duplicateKeyExceptionType(int type,String desc){
			this.type = type;
			this.desc = desc;
		}
		
		public int getType(){
			return type;
		}
		
		public String getDesc(){
			return desc;
		}
	}
	
	
}
