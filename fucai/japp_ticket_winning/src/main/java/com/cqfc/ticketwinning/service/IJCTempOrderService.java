package com.cqfc.ticketwinning.service;

import java.util.List;

import com.cqfc.ticketwinning.model.JCTempOrder;
import com.cqfc.ticketwinning.model.OrderNoCount;

public interface IJCTempOrderService {
	
	/**
	 * 查询竞彩临时订单表
	 * @param transferId
	 * @param tableName
	 * @param cursor
	 * @param pageSize
	 * @return
	 */
	public List<JCTempOrder> getJCTempOrderList(String transferId, String tableName, long cursor, int pageSize);	
	
	/**
	 * 查询竞彩临时订单表记录数
	 * @param transferId
	 * @param tableName
	 * @return
	 */
	public int getJCTempOrderCount(String transferId, String tableName);
	
	/**
	 * 更新竞彩中奖赔率
	 * @param transferId
	 * @param orderNo
	 * @param winOdds
	 * @param tableName
	 * @return
	 */
	public int updateJCTempOrderWinOdds(String transferId, String orderNo, String winOdds, String tableName);
	
	/**
	 * 查询竞彩临时订单表订单号相同的订单数
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public OrderNoCount getOrderNoCountAndWinOddsCount(String orderNo, String tableName);
	
	/**
	 * 查询竞彩临时订单表订单号相同的订单
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public List<JCTempOrder> getOrderListByOrderNo(String orderNo, String tableName);
	
	/**
	 * 查询竞彩临时订单表记录数
	 * @param wareIssue
	 * @param matchNo
	 * @param tableName
	 * @return
	 */
	public int getBDTempOrderCount(String wareIssue, String matchNo, int matchType,
			String tableName);

	/**
	 * 查询北单临时订单表记录数
	 * @param wareIssue
	 * @param matchNo
	 * @param jcOrderTempTablename
	 * @param startNum
	 * @param pageSize
	 * @return
	 */
	public List<JCTempOrder> getBDTempOrderList(String wareIssue,
			String matchNo, int matchType, String jcOrderTempTablename, int startNum,
			int pageSize);
}
