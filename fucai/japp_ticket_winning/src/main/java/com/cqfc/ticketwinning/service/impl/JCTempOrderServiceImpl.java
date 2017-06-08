package com.cqfc.ticketwinning.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqfc.ticketwinning.dao.JCTempOrderDao;
import com.cqfc.ticketwinning.model.JCTempOrder;
import com.cqfc.ticketwinning.model.OrderNoCount;
import com.cqfc.ticketwinning.service.IJCTempOrderService;

@Service
public class JCTempOrderServiceImpl implements IJCTempOrderService{

	@Resource
	JCTempOrderDao jCTempOrderDao;
	
	/**
	 * 查询竞彩临时订单表
	 * 
	 * @param transferId
	 * @param tableName
	 * @param cursor
	 * @param pageSize
	 * @return
	 */
	@Override
	public List<JCTempOrder> getJCTempOrderList(String transferId,
			String tableName, long cursor, int pageSize) {
		
		return jCTempOrderDao.getJCTempOrderList(transferId, tableName, cursor, pageSize);
	}

	/**
	 * 计算竞彩总记录数(仅计算订单状态为已出票待开奖的订单)
	 * 
	 * @param transferId
	 * @param tableName
	 * @return
	 */
	@Override
	public int getJCTempOrderCount(String transferId, String tableName) {

		return jCTempOrderDao.countTotalSizeOfJCTempOrder(transferId, tableName);
	}
	
	
	/**
	 * 更新竞彩中奖赔率
	 * 
	 * @param transferId
	 * @param orderNo
	 * @param winOdds
	 * @param tableName
	 * @return
	 */
	@Override
	public int updateJCTempOrderWinOdds(String transferId, String orderNo, String winOdds, String tableName){
		
		return jCTempOrderDao.updateJCTempOrderWinOdds(transferId, orderNo, winOdds, tableName);
	}
	
	
	/**
	 * 查询竞彩临时订单表订单号相同的订单数
	 * 
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	@Override
	public OrderNoCount getOrderNoCountAndWinOddsCount(String orderNo, String tableName){
		
		return jCTempOrderDao.getOrderNoCountAndWinOddsCount(orderNo, tableName);
	}
	
	
	/**
	 * 查询竞彩临时订单表订单号相同的订单
	 * 
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	@Override
	public List<JCTempOrder> getOrderListByOrderNo(String orderNo, String tableName){
		
		return jCTempOrderDao.getOrderListByOrderNo(orderNo, tableName);
	}
	
	
	/**
	 * 计算北单总记录数
	 * 
	 * @param wareIssue
	 * @param matchNo
	 * @param tableName
	 * @return
	 */
	@Override
	public int getBDTempOrderCount(String wareIssue, String matchNo, int matchType, String tableName) {

		return jCTempOrderDao.countTotalSizeOfBDTempOrder(wareIssue, matchNo, matchType, tableName);
	}

	@Override
	public List<JCTempOrder> getBDTempOrderList(String wareIssue,
			String matchNo, int matchType, String tableName, int cursor,
			int pageSize) {
		
		return jCTempOrderDao.getBDTempOrderList(wareIssue, matchNo, matchType, tableName, cursor, pageSize);
	}
	
}
