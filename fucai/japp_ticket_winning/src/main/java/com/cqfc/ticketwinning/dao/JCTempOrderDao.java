package com.cqfc.ticketwinning.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.ticketwinning.dao.mapper.JCTempOrderMapper;
import com.cqfc.ticketwinning.model.JCTempOrder;
import com.cqfc.ticketwinning.model.OrderNoCount;
import com.jami.util.Log;

@Repository
public class JCTempOrderDao {
	
	@Autowired
	JCTempOrderMapper jCTempOrderMapper;
	
	/**
	 * 查询竞彩临时订单表
	 * @param transferId
	 * @param tableName
	 * @param cursor
	 * @param pageSize
	 * @return
	 */
	public List<JCTempOrder> getJCTempOrderList(String transferId, String tableName, long cursor, int pageSize){
		List<JCTempOrder> orderList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try{
			// 搜索参数
			if(null != transferId && !"".equals(transferId)){
				conditions.append(" and transferId='" + transferId + "'");
			}

			conditions.append(" limit " + cursor + "," + pageSize);
			
			orderList = jCTempOrderMapper.getOrderList(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("getJCTempOrderList(exception=%s)", e);
			return null;
		}	
	
		return orderList;
	}
	
	
	/**
	 * 查询竞彩临时订单表记录数
	 * @param transferId
	 * @param tableName
	 * @return
	 */
	public int countTotalSizeOfJCTempOrder(String transferId, String tableName) {
		int count = 0;
		StringBuffer conditions = new StringBuffer(" 1=1 ");
		
		try{
			if(null != transferId && !"".equals(transferId)){
				conditions.append(" and transferId='" + transferId + "'");
			}

			count = jCTempOrderMapper.countTotalSize(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("countTotalSizeOfJCTempOrder(exception=%s)", e);
			return -1;
		}	
	
		return count;
	}
	
	/**
	 * 更新竞彩中奖赔率
	 * @param transferId
	 * @param orderNo
	 * @param winOdds
	 * @param tableName
	 * @return
	 */
	public int updateJCTempOrderWinOdds(String transferId, String orderNo, String winOdds, String tableName){
		int retValue = 0;
		int matchStatus = 1;
		
		try{
			retValue = jCTempOrderMapper.updateJCTempOrderWinOdds(transferId, orderNo, winOdds, matchStatus, tableName);
		} catch (Exception e) {
			Log.run.error("updateJCTempOrderWinOdds(exception=%s)", e);
			return -1;
		}
		
		return retValue;
	}
	
	
	/**
	 * 查询竞彩临时订单表订单号相同的订单数和中奖赔率数
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public OrderNoCount getOrderNoCountAndWinOddsCount(String orderNo, String tableName){
		OrderNoCount count = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try{
			// 搜索参数
			if(null != orderNo && !"".equals(orderNo)){
				conditions.append(" and orderNo='" + orderNo + "'");
			}
			
			count = jCTempOrderMapper.getOrderNoCountAndWinOddsCount(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("getOrderNoCountAndWinOddsCount(exception=%s)", e);
			return null;
		}	
	
		return count;
	}
	
	
	/**
	 * 查询竞彩临时订单表订单号相同的订单
	 * @param orderNo
	 * @param tableName
	 * @return
	 */
	public List<JCTempOrder> getOrderListByOrderNo(String orderNo, String tableName){
		List<JCTempOrder> list = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try{
			// 搜索参数
			if(null != orderNo && !"".equals(orderNo)){
				conditions.append(" and orderNo='" + orderNo + "'");
			}
			
			list = jCTempOrderMapper.getOrderList(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("getOrderNoCountAndWinOddsCount(exception=%s)", e);
			return null;
		}	
	
		return list;
	}

	/**
	 * 查询北单算奖记录数
	 * @param wareIssue
	 * @param matchNo
	 * @param tableName
	 * @return
	 */
	public int countTotalSizeOfBDTempOrder(String wareIssue, String matchNo, Integer matchType,
			String tableName) {
		int count = 0;
		StringBuffer conditions = new StringBuffer(" 1=1 ");
		
		try{
			if(null != wareIssue && !"".equals(wareIssue)){
				conditions.append(" and issueNo ='" + wareIssue + "'");
			}
			if(null != matchNo && !"".equals(matchNo)){
				conditions.append(" and matchNo ='" + matchNo + "'");
			}
			if(null != matchType){
				conditions.append(" and matchType ='" + matchType + "'");
			}

			count = jCTempOrderMapper.countTotalSize(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("countTotalSizeOfBDTempOrder(exception=%s)", e);
			return -1;
		}	
	
		return count;
	}


	public List<JCTempOrder> getBDTempOrderList(String wareIssue,
			String matchNo, Integer matchType, String tableName, int cursor, int pageSize) {
		List<JCTempOrder> orderList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try{
			// 搜索参数
			if(null != wareIssue && !"".equals(wareIssue)){
				conditions.append(" and issueNo='" + wareIssue + "'");
			}
			if(null != matchNo && !"".equals(matchNo)){
				conditions.append(" and matchNo='" + matchNo + "'");
			}
			if(null != matchType){
				conditions.append(" and matchType='" + matchType + "'");
			}

			conditions.append(" limit " + cursor + "," + pageSize);
			
			orderList = jCTempOrderMapper.getOrderList(conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("getJCTempOrderList(exception=%s)", e);
			return null;
		}	
	
		return orderList;
	}
}
