package com.cqfc.ticketwinning.service;

import java.util.List;

import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.model.WinningOrder;


public interface IWinningOrderService {
	
	/**
	 * 查询订单列表(仅查询订单状态为已出票待开奖的订单)
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @param cursor
	 * @param pageSize
	 * @return
	 */
	public List<WinningOrder> getOrderListOfWaittinglottery(String lotteryId, String issueNo, String tableName, long cursor, int pageSize);	
	
	/**
	 * 计算彩种期号总记录数(仅计算订单状态为已出票待开奖的订单)
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	public int countTotalSizeOfWaittinglottery(String lotteryId, String issueNo, String tableName);
	
	/**
	 * 更新订单状态和中奖金额
	 * @param orderStatus
	 * @param winningAmout
	 * @param orderId
	 * @param tableName
	 * @return
	 */
	public int updateWinningOrderByOrderNo(int orderStatus, long winningAmout, String orderNo, String tableName);
	
	/**
	 * 更新订单状态和税后中奖金额
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	public int updateUnWinningOrder(String lotteryId, String issueNo, String tableName, int pageSize);
	
	/**
	 * 计算未中奖的记录数
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	public int getUnWinningOrderCount(String lotteryId, String issueNo, String tableName);
	
	/**
	 * 查询订单总数
	 * @param tableName
	 * @return
	 */
	public int getOrderCount(String lotteryId, String issueNo, String tableName);
	
	
	/**
	 * 根据orderNo查找订单信息
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public WinningOrderInfo getOrderByOrderNo(String orderNo, String tableName);
}
