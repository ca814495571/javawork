package com.cqfc.businesscontroller.util;

import com.cqfc.util.IssueConstant;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.UserAccountConstant;


public class DataUtil {

	//判断字符串是否为数字（带小数点和负号会返回false）
	public static boolean isNumeric(String s){
		
		for (int i = s.length() ; --i>=0;) {
			
			int chr = s.charAt(i);
			
			if(chr < 48 || chr > 57){
				
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 根据期号状态按文档状态显示
	 * 	0为预销售
		1为销售中
		2为已结期
		3为已获得开奖公告
		4 为已经兑奖完毕
	 */
	public static String transferState(int state){
		
		if(IssueConstant.ISSUE_STATUS_NOT_SELL == state){
			
			return "0";
		}
		
		if(IssueConstant.ISSUE_STATUS_SELLING == state){
			return "1";
		}
		
		if(IssueConstant.ISSUE_STATUS_END_SELL == state || IssueConstant.ISSUE_STATUS_AEGIS == state || IssueConstant.ISSUE_STATUS_PRINT ==state
				|| IssueConstant.ISSUE_STATUS_CANCEL == state || IssueConstant.ISSUE_STATUS_TRANSFER == state ){
			
			return "2";
		}
		if(IssueConstant.ISSUE_STATUS_DRAW==state || IssueConstant.ISSUE_STATUS_PASSING ==state||IssueConstant.ISSUE_STATUS_PASS==state||IssueConstant.ISSUE_STATUS_CHECK_PASS==state
				|| IssueConstant.ISSUE_STATUS_CALING ==state|| IssueConstant.ISSUE_STATUS_HASCAL_WAITAUDIT==state||IssueConstant.ISSUE_STATUS_AUDITTING==state
				||IssueConstant.ISSUE_STATUS_AUDITTING==state||IssueConstant.ISSUE_STATUS_CAN_SEND==state||IssueConstant.ISSUE_STATUS_SENDING==state){
			
			return "3";
		}
		
		if(IssueConstant.ISSUE_STATUS_SENDPRIZE_ONLINE == state){
			
			return "4";
		}
		
		return null;
	}
	
	/**
	 * 根据订单状态判断中奖情况
	 * @param orderStatus
	 * @return
	 */
	public static String getPrizeStatus(int orderStatus) {
		// 中奖状态:0未开奖1未中奖 2中奖
		// 订单状态：//订单状态：1待付款 2已付款 3出票中 4已出票待开奖 
		//5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功 11订单取消 12 算奖中 13 已派奖
		
		if (
				 orderStatus == OrderStatus.FINISH_TICKET_WAIT_PRIZE 
				|| orderStatus == OrderStatus.SUC_RETURNING_MONGEY || orderStatus == OrderStatus.CANCEL_ORDER|| orderStatus == OrderStatus.CALCULATTING_PRIZE) {

			return "0";
		}
		if (orderStatus == OrderStatus.NOT_WIN_PRIZE) {

			return "1";
		}
		if (orderStatus == OrderStatus.WAIT_TO_AWARD_PRIZE || orderStatus == OrderStatus.FINISH_AWARD_PRIZE) {

			return "2";
		}
		return "";

	}
	
	
	/**
	 * 根据订单状态确定文档接口显示的订单交易状态
	 * @param orderStatus
	 * @return
	 */
	public static String getTradeStatus(int orderStatus) {
		// 订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败
		// 6未中奖 7待领奖 8已领奖 9退款中 10退款成功 11订单取消



		if (orderStatus == OrderStatus.FINISH_TICKET_WAIT_PRIZE || orderStatus == OrderStatus.NOT_WIN_PRIZE || orderStatus == OrderStatus.WAIT_TO_AWARD_PRIZE
				|| orderStatus == OrderStatus.FINISH_AWARD_PRIZE|| orderStatus == OrderStatus.CALCULATTING_PRIZE) {

			return "2";
		}

		if ( orderStatus == OrderStatus.SUC_RETURNING_MONGEY
				|| orderStatus == OrderStatus.CANCEL_ORDER) {

			return "3";
		}

		return "";

	}
	
	/**
	 * 获取订单状态转换
	 * @param orderType
	 * @return
	 */
	public static String changeOrderType(String orderType){
		
		//0 直投
		//1 追号
		if("1".equals(orderType)){
			
			return "0";
		}
		
		if("2".equals(orderType)){
			return "1";
		}
		
		return "0";
		
	}
	
	
	/**
	 * 提现接口查询获取的状态
	 * @param auditState
	 * @return
	 */
	public static String getAuditState(int auditState){
		
		//0 : 待处理
		//1 : 交易中
		//2 : 成功
		//3 : 失败
		//4 : 审核未通过  
		//5 : 审核通过
		
		if(auditState == UserAccountConstant.WithdrawAuditState.NOT_AUDIT.getValue()){
			return "0";
		}
		if(auditState == UserAccountConstant.WithdrawAuditState.AUDIT_PASS.getValue()){
			return "5";
		}
		if(auditState == UserAccountConstant.WithdrawAuditState.AUDIT_NOPASS.getValue()){
			return "4";
		}
	
		return "0";
		
	}
	
	
}
