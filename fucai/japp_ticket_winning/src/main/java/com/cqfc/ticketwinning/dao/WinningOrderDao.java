package com.cqfc.ticketwinning.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.dao.mapper.WinningOrderMapper;
import com.cqfc.ticketwinning.model.WinningOrder;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class WinningOrderDao {
	@Autowired
	WinningOrderMapper winningOrderMapper;
	
	/**
	 * 查询订单列表(仅查询订单状态为已出票待开奖的订单)
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<WinningOrder> getOrderListOfWaittinglottery(String lotteryId, String issueNo, String tableName, long cursor, int pageSize){
		List<WinningOrder> winningOrderList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try{
			// 搜索参数
			if(null != lotteryId && !"".equals(lotteryId)){
				conditions.append(" and lotteryId='" + lotteryId + "'");
			}
			if(null != issueNo && !"".equals(issueNo)){
				conditions.append(" and issueNo='" +issueNo + "'");
			}

			conditions.append(" and orderStatus = " + OrderStatus.FINISH_TICKET_WAIT_PRIZE + " limit " + cursor + "," + pageSize);
			
			winningOrderList = winningOrderMapper.getOrderList(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("getOrderListOfWaittinglotteryDao(exception=%s)", e);
			return null;
		}	
	
		return winningOrderList;
	}
	
	/**
	 * 计算彩种期号总记录数(仅计算订单状态为已出票待开奖的订单)
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	public int countTotalSizeOfWaittinglottery(String lotteryId, String issueNo, String tableName) {
		int count = 0;
		StringBuffer conditions = new StringBuffer(" 1=1 ");
		
		try{
			if(null != lotteryId && !"".equals(lotteryId)){
				conditions.append(" and lotteryId='" + lotteryId + "'");
			}
			if(null != issueNo && !"".equals(issueNo)){
				conditions.append(" and issueNo='" +issueNo + "'");
			}
			conditions.append(" and orderStatus = " + OrderStatus.FINISH_TICKET_WAIT_PRIZE);
			count = winningOrderMapper.countTotalSize(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("countTotalSizeOfWaittinglotteryDao(exception=%s)", e);
			return -1;
		}	
	
		return count;
	}
	
	/**
	 * 更新订单状态和中奖金额
	 * @param orderStatus
	 * @param winningAmout
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public int updateWinningOrderByOrderNo(int orderStatus, long winningAmout, String orderNo, String tableName){
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try{
			returnValue = winningOrderMapper.updateWinningOrderByOrderNo(orderStatus, winningAmout, orderNo, tableName);
		}catch (DuplicateKeyException e) {
			Log.run.error("updateWinningOrderByOrderNoDao(orderNo=%s, tableName=%s, duplicateKeyException=%s)", orderNo, tableName, e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		}catch (Exception e) {
			Log.run.error("updateWinningOrderByOrderNoDao(orderNo=%s, tableName=%s, exception=%s)", orderNo, tableName, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}
	
	/**
	 * 更新订单状态和税后中奖金额
	 * @param orderStatus
	 * @param winningAmout
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public int updateUnWinningOrder(String lotteryId, String issueNo, String tableName, int pageSize) throws DaoLevelException{
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		StringBuffer conditions = new StringBuffer(" 1=1 ");
		try{
			if(null != lotteryId && !"".equals(lotteryId)){
				conditions.append(" and lotteryId='" + lotteryId + "'");
			}
			if(null != issueNo && !"".equals(issueNo)){
				conditions.append(" and issueNo='" +issueNo + "'");
			}
			conditions.append(" and orderStatus = " + OrderStatus.FINISH_TICKET_WAIT_PRIZE + " limit " + pageSize);
			returnValue = winningOrderMapper.updateUnWinningOrder(WinningOrder.OrderStatus.NOT_WINNING.getValue(), 0, conditions.toString(), tableName);
		}catch (DuplicateKeyException e) {
			Log.run.error("updateUnWinningOrderDao(lotteryId=%s, issueNo=%s, tableName=%s, duplicateKeyException=%s)", lotteryId, issueNo, tableName, e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		}catch (Exception e) {
			Log.run.error("updateUnWinningOrderDao(lotteryId=%s, issueNo=%s, tableName=%s, exception=%s)", lotteryId, issueNo, tableName, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}
	
	/**
	 * 计算未中奖的记录数
	 * @param lotteryId
	 * @param issueNo
	 * @param tableName
	 * @return
	 */
	public int getUnWinningOrderCount(String lotteryId, String issueNo, String tableName){
		int count = 0;
		StringBuffer conditions = new StringBuffer(" 1=1 ");
		try{
			if(null != lotteryId && !"".equals(lotteryId)){
				conditions.append(" and lotteryId='" + lotteryId + "'");
			}
			if(null != issueNo && !"".equals(issueNo)){
				conditions.append(" and issueNo='" +issueNo + "'");
			}
			conditions.append(" and orderStatus = " + OrderStatus.FINISH_TICKET_WAIT_PRIZE);
			count = winningOrderMapper.countTotalSize(conditions.toString(), tableName);
		}catch (Exception e) {
			Log.run.error("getUnWinningOrderCountDao(lotteryId=%s, issueNo=%s, tableName=%s, exception=%s)", lotteryId, issueNo, tableName, e);
			return -1;
		}
		return count;
	}
	
	/**
	 * 查询订单总数
	 * @param tableName
	 * @return
	 */
	public int getOrderCount(String lotteryId, String issueNo, String tableName){
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try{
			returnValue = winningOrderMapper.getOrderCount(lotteryId, issueNo, tableName);
		}catch (Exception e) {
			Log.run.error("getOrderCountDao(lotteryId=%s, issueNo=%s, tableName=%s, exception=%s)", lotteryId, issueNo, tableName, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}
	
	/**
	 * 根据orderNo查找订单信息
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public WinningOrderInfo getOrderByOrderNo(String orderNo, String tableName){
		WinningOrderInfo order = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try{
			// 搜索参数
			if(null != orderNo && !"".equals(orderNo)){
				conditions.append(" and orderNo='" + orderNo + "'");
			}

			conditions.append(" and orderStatus = " + OrderStatus.FINISH_TICKET_WAIT_PRIZE);
			
			order = winningOrderMapper.getOrderByOrderNo(orderNo, tableName);
		} catch (Exception e) {
			Log.run.error("getOrderByOrderNo(exception=%s)", e);
			return null;
		}	
	
		return order;
	}
}
