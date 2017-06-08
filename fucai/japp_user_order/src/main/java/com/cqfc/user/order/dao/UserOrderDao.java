package com.cqfc.user.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.protocol.userorder.Order;
import com.cqfc.protocol.userorder.PcUserOrder;
import com.cqfc.user.order.dao.mapper.UserOrderMapper;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.DataSourceContextHolder;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;

@Repository
public class UserOrderDao {

	@Autowired
	private UserOrderMapper userOrderMapper;

	public int addUserOrder(Order userOrder, String tableName) {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.getDbName(String.valueOf(userOrder.getUserId())));
		try {
			if(userOrder.getExt()==null){
				userOrder.setExt("");
			}
			flag = userOrderMapper.addUserOrder(userOrder, tableName);
			
		} catch (DuplicateKeyException e) {
			Log.run.error(e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error(e);
			return flag;
		}
		return flag;
	}

	
	public PcUserOrder getUserOrder(Order order, int pageNum, int pageSize,
			String tableName) {

		PcUserOrder pcUserOrder = new PcUserOrder();
		long userId = order.getUserId();
		int orderType = order.getOrderType();
		String lotteryId = order.getLotteryId();

		StringBuffer condition = new StringBuffer(" 1=1 ");

		if (!"".equals(userId) && userId != 0) {

			condition.append(" and userId = '");
			condition.append(userId);
			condition.append("'");
		}

		if (!"".equals(orderType) && orderType != 0) {

			condition.append(" and orderType = '");
			condition.append(orderType);
			condition.append("'");
		}

		if (lotteryId != null && !"".equals(lotteryId)) {

			condition.append(" and lotteryId = '");
			condition.append(lotteryId);
			condition.append("'");
		}
		
		if (StringUtils.isNotBlank(order.getTradeId())) {

			condition.append(" and tradeId = '");
			condition.append(order.getTradeId());
			condition.append("'");
		}
		
		if (StringUtils.isNotBlank(order.getOrderNo())) {

			condition.append(" and orderNo = '");
			condition.append(order.getOrderNo());
			condition.append("'");
		}

		
		
		condition.append(" order by createTime desc ");
		int totalNum = 0;
		try {

			totalNum = userOrderMapper.getOrderTotal(condition.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error(e);
			totalNum = -1;
		}

		if (pageNum != 0 && pageSize != 0) {

			condition.append(" limit ");
			condition.append((pageNum - 1) * pageSize);
			condition.append(",");
			condition.append(pageSize);
		}

		List<Order> orders = new ArrayList<Order>();

		try {
			orders = userOrderMapper.getUserOrder(condition.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error("根据条件查询用户订单数据库异常",e);
			Log.error("根据条件查询用户订单数据库异常",e);
		}

		pcUserOrder.setCurrentPage(pageNum);
		pcUserOrder.setPageSize(pageSize);
		pcUserOrder.setTotalSize(totalNum);
		pcUserOrder.setUserOrders(orders);
		Log.run.debug("根据多条件分页查询用户订单成功...");
		return pcUserOrder;
	}

	public int updateUserOrder(Order order, String tableName) {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = userOrderMapper.updateUserOrder(order, tableName);
			Log.run.debug("修改用户订单成功");
		} catch (DuplicateKeyException e) {
			Log.run.error(e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error(e);
			return flag;
		}
		return flag;

	}

	public List<Order> getUserOrderByWhere(Order order, String tableName) {

		StringBuffer sb = new StringBuffer();
		List<Order> orders = new ArrayList<Order>();
		try {
			
			sb.append(" 1=1 ");

			if (order.getUserId() != 0) {

				sb.append(" and userId ='");
				sb.append(order.getUserId());
				sb.append("'");
			}

			if (order.getPartnerId() != null && !"".equals(order.getPartnerId())) {

				sb.append(" and partnerId ='");
				sb.append(order.getPartnerId());
				sb.append("'");

			}

			if (order.getOrderNo() != null && !"".equals(order.getOrderNo())) {

				sb.append(" and orderNo ='");
				sb.append(order.getOrderNo());
				sb.append("'");

			}
			
			if(order.getLotteryId() != null && !"".equals(order.getLotteryId())){
				
				sb.append(" and lotteryId = '");
				sb.append(order.getLotteryId());
				sb.append("'");
				
			}
			
			if (order.getIssueNo() != null && !"".equals(order.getIssueNo())) {

				sb.append(" and issueNo ='");
				sb.append(order.getIssueNo());
				sb.append("'");

			}

			if (order.getTradeId() != null && !"".equals(order.getTradeId())) {

				sb.append(" and tradeId ='");
				sb.append(order.getTradeId());
				sb.append("'");

			}
			orders =  userOrderMapper.getOrderBywhere(sb.toString(), tableName);
			
			
		} catch (Exception e) {
			Log.run.error("根据条件查询用户订单数据库异常", e);
			Log.error("根据条件查询用户订单数据库异常", e);
		}
		return orders;
	}

	public Order getMaxIssueNoOrder(Order order, String tableName) {

		
		StringBuffer sb = new StringBuffer();

		sb.append(" 1=1 ");

		if (order.getUserId() != 0) {

			sb.append(" and userId ='");
			sb.append(order.getUserId());
			sb.append("'");
		}

		if (order.getPartnerId() != null && !"".equals(order.getPartnerId())) {

			sb.append(" and partnerId ='");
			sb.append(order.getPartnerId());
			sb.append("'");

		}
		if (order.getTradeId() != null && !"".equals(order.getTradeId())) {

			sb.append(" and tradeId ='");
			sb.append(order.getTradeId());
			sb.append("'");

		}

		sb.append(" order by issueNo  desc limit 1");
		

		try {
			order = userOrderMapper.getMaxIsserNoOrder(sb.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("获取期号最大的用户订单时数据库异常",e);
			Log.error("获取期号最大的用户订单时数据库异常",e);
			order = null;
		}
		
		return order;
	}

}
