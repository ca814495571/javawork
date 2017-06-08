package com.cqfc.ticketwinning.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.dao.WinningOrderDao;
import com.cqfc.ticketwinning.model.WinningOrder;
import com.cqfc.ticketwinning.service.IWinningOrderService;

@Service
public class WinningOrderServiceImpl implements IWinningOrderService{
	
	@Resource
	WinningOrderDao winningOrderDao;
	
	/**
	 * 查询订单列表(仅查询订单状态为已出票待开奖的订单)
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @param cursor
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<WinningOrder> getOrderListOfWaittinglottery(String lotteryId, String issueNo, String tableName, long cursor, int pageSize){
		return winningOrderDao.getOrderListOfWaittinglottery(lotteryId, issueNo, tableName, cursor, pageSize);
	}
	
	/**
	 * 计算彩种期号总记录数(仅计算订单状态为已出票待开奖的订单)
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	@Override
	public int countTotalSizeOfWaittinglottery(String lotteryId, String issueNo, String tableName) {
		return winningOrderDao.countTotalSizeOfWaittinglottery(lotteryId, issueNo, tableName);
	}
	
	/**
	 * 更新订单状态和中奖金额
	 * @param orderStatus
	 * @param winningAmout
	 * @param orderId
	 * @param tableName
	 * @return
	 */
	@Override
	@Transactional
	public int updateWinningOrderByOrderNo(int orderStatus, long winningAmout, String orderNo, String tableName){
		return winningOrderDao.updateWinningOrderByOrderNo(orderStatus, winningAmout, orderNo, tableName);
	}
	
	/**
	 * 更新订单状态和税后中奖金额
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	@Override
	@Transactional
	public int updateUnWinningOrder(String lotteryId, String issueNo, String tableName, int pageSize){
		return winningOrderDao.updateUnWinningOrder(lotteryId, issueNo, tableName, pageSize);
	}
	
	/**
	 * 计算未中奖的记录数
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	public int getUnWinningOrderCount(String lotteryId, String issueNo, String tableName){
		return winningOrderDao.getUnWinningOrderCount(lotteryId, issueNo, tableName);
	}
	
	/**
	 * 查询订单总数
	 * @param tableName
	 * @return
	 */
	@Override
	public int getOrderCount(String lotteryId, String issueNo, String tableName){
		return winningOrderDao.getOrderCount(lotteryId, issueNo, tableName);
	}
	
	/**
	 * 根据orderNo查找订单信息
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	@Override
	public WinningOrderInfo getOrderByOrderNo(String orderNo, String tableName){
		
		return winningOrderDao.getOrderByOrderNo(orderNo, tableName);
	}
}
