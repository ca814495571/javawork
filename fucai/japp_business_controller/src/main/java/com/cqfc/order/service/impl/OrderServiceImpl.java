package com.cqfc.order.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.order.dao.OrderDao;
import com.cqfc.order.datacenter.CreateOrderSqlExecuteTask;
import com.cqfc.order.datacenter.SportOrderCreateTask;
import com.cqfc.order.datacenter.SportOrderStatusUpdateTask;
import com.cqfc.order.datacenter.SportSyncStatusUpdateTask;
import com.cqfc.order.datacenter.UpdateOrderStatusSqlExecuteTask;
import com.cqfc.order.datacenter.UpdateSyncStatusSqlExecuteTask;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.order.service.IOrderService;
import com.cqfc.order.util.DbGenerator;
import com.cqfc.order.util.OrderDynamicUtil;
import com.cqfc.util.OrderConstant;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderDao orderDao;

	@Resource(name = "batchOrderTaskExecutor")
	private ThreadPoolTaskExecutor batchOrderTaskExecutor;

	@Override
	public int createOrder(Order order) {
		int isSuccess = 0;
		long orderNo = order.getOrderNo();
		try {
			String tableName = getTableName(orderNo);
			Log.run.debug("创建订单,orderNo=%d,tableName=%s", orderNo, tableName);
			isSuccess = orderDao.createOrder(order, tableName);
		} catch (Exception e) {
			Log.run.error("创建订单发生异常,orderNo=" + orderNo, e);
			return isSuccess;
		}
		return isSuccess;
	}

	@Override
	public Order findOrderByOrderNo(long orderNo) {
		Order order = null;
		try {
			String tableName = getTableName(orderNo);
			Log.run.debug("查询订单,orderNo=%d,tableName=%s", orderNo, tableName);
			order = orderDao.findOrderByOrderNo(orderNo, tableName);
		} catch (Exception e) {
			Log.run.error("查询订单发生异常,orderNo=" + orderNo, e);
			return null;
		}
		return order;
	}

	@Override
	public int updateOrderStatus(long orderNo, int orderStatus) {
		int isSuccess = 0;
		try {
			String tableName = getTableName(orderNo);
			Log.run.debug("更新订单状态,orderNo=%d,tableName=%s,orderStatus=%d", orderNo, tableName, orderStatus);
			isSuccess = orderDao.modifyOrderStatus(orderNo, orderStatus, tableName, null, null);
		} catch (Exception e) {
			Log.run.error("更新订单状态发生异常,orderNo=" + orderNo, e);
			return isSuccess;
		}
		return isSuccess;
	}

	@Override
	public int updateOrderSync(long orderNo, int syncValue) {
		int isSuccess = 0;
		try {
			String tableName = getTableName(orderNo);
			Log.run.debug("更新订单同步状态,orderNo=%d,tableName=%s,syncValue=%d", orderNo, tableName, syncValue);
			isSuccess = orderDao.updateOrderSync(orderNo, syncValue, tableName);
		} catch (Exception e) {
			Log.run.error("更新订单同步状态发生异常,orderNo=" + orderNo, e);
			return isSuccess;
		}
		return isSuccess;
	}

	@Override
	public List<Order> getOrderList(String tableName, String lotteryId, String issueNo, int type, int currentPage,
			int pageSize) {
		List<Order> orderList = null;
		try {
			orderList = orderDao.getOrderList(tableName, lotteryId, issueNo, type, currentPage, pageSize);
		} catch (Exception e) {
			ScanLog.run.error("扫描任务获取订单列表,tableName=" + tableName + ",scanType=" + type
					+ "(1未付款订单,2出票,3撤单,4订单同步,5删除订单,6退款)", e);
			return null;
		}
		return orderList;
	}

	@Override
	public int deleteOrder(String tableName, String deleteTime) {
		int isSuccess = 0;
		try {
			isSuccess = orderDao.deleteOrder(tableName, deleteTime);
		} catch (Exception e) {
			Log.run.error("删除订单发生异常,tableName=" + tableName, e);
			return isSuccess;
		}
		return isSuccess;
	}

	@Override
	public List<Order> getOrderListScan(String tableName, int type, int currentPage, int pageSize) {
		List<Order> orderList = null;
		try {
			orderList = orderDao.getOrderListScan(tableName, type, currentPage, pageSize);
		} catch (Exception e) {
			ScanLog.run.error("business扫描订单列表,tableName=" + tableName + ",scanType=" + type
					+ "(1未付款订单,2出票,3撤单,4订单同步,5删除订单,6退款)", e);
			return null;
		}
		return orderList;
	}

	@Override
	public int updateOrderStatusAndRemark(long orderNo, int orderStatus, String errorCode, String errorRemark) {
		int isSuccess = 0;
		try {
			String tableName = getTableName(orderNo);
			Log.run.debug("更新订单状态（错误状态码）,orderNo=%d,tableName=%s,orderStatus=%d,errorCode=%s,errorRemark=%s", orderNo,
					tableName, orderStatus, errorCode, errorRemark);
			isSuccess = orderDao.modifyOrderStatus(orderNo, orderStatus, tableName, errorCode, errorRemark);
		} catch (Exception e) {
			Log.run.error("更新订单状态发生异常,orderNo=" + orderNo, e);
			return isSuccess;
		}
		return isSuccess;
	}

	/**
	 * 数字彩订单获取获取订单表名
	 * 
	 * @param orderNo
	 * @return
	 */
	private String getTableName(long orderNo) {
		String temp = String.valueOf(orderNo);
		int len = temp.length();
		// 目前每库100张表,所以表标识为2位（预留为3位）
		String tableIndex = temp.substring(len - 2);
		return OrderDynamicUtil.PRE_TABLENAME + tableIndex;
	}

	@Override
	public void initOrderTask(ApplicationContext applicationContext) {
		AtomicBoolean running = new AtomicBoolean(true);
		for (int i = 0; i < OrderConstant.DATASOURCE_NUM; i++) {
			batchOrderTaskExecutor.submit(new CreateOrderSqlExecuteTask(applicationContext, running, i));
			batchOrderTaskExecutor.submit(new UpdateOrderStatusSqlExecuteTask(applicationContext, running, i));
			batchOrderTaskExecutor.submit(new UpdateSyncStatusSqlExecuteTask(applicationContext, running, i));
			batchOrderTaskExecutor.submit(new SportOrderCreateTask(applicationContext, running, i));
			batchOrderTaskExecutor.submit(new SportOrderStatusUpdateTask(applicationContext, running, i));
			batchOrderTaskExecutor.submit(new SportSyncStatusUpdateTask(applicationContext, running, i));
		}
	}

	@Override
	public Order findOrderByParams(String partnerId, String tradeId) {
		// TODO Auto-generated method stub
		Order order = null;
		try {
			String tableName = DbGenerator.getTableName(OrderDynamicUtil.PRE_TABLENAME, tradeId);
			Log.run.debug("查询订单,partnerId=%s,tradeId=%s,tableName=%s", partnerId, tradeId, tableName);
			order = orderDao.findOrderByParams(partnerId, tradeId, tableName);
		} catch (Exception e) {
			Log.run.error("查询订单发生异常,partnerId=" + partnerId + ",tradeId=" + tradeId, e);
			return null;
		}
		return order;
	}

	/**
	 * 竞技彩订单获取表名
	 * 
	 * @param orderNo
	 * @return
	 */
	private String getSportTableName(long orderNo, String mainOrDetail) {
		String temp = String.valueOf(orderNo);
		int len = temp.length();
		// 目前每库100张表,所以表标识为2位（预留为3位）
		String tableIndex = temp.substring(len - 2);
		return mainOrDetail + tableIndex;
	}

	@Override
	public SportOrder findSportOrderByOrderNo(long orderNo) {
		SportOrder order = null;
		try {
			String mainTableName = getSportTableName(orderNo, OrderDynamicUtil.PRE_COMPETITION_TABLENAME_MAIN);
			String detailTableName = getSportTableName(orderNo, OrderDynamicUtil.PRE_COMPETITION_TABLENAME_DETAIL);
			Log.run.debug("竞技彩,查询订单,orderNo=%d,mainTableName=%s", orderNo, mainTableName);
			order = orderDao.findSportOrderByOrderNo(orderNo, mainTableName, detailTableName);
		} catch (Exception e) {
			Log.run.error("竞技彩,查询订单发生异常,orderNo=" + orderNo, e);
			return null;
		}
		return order;
	}

	@Override
	public List<SportOrder> getSportOrderListScan(String mainTableName, String detailTableName, int type,
			int currentPage, int pageSize) {
		List<SportOrder> orderList = null;
		try {
			orderList = orderDao.getSportOrderListScan(mainTableName, detailTableName, type, currentPage, pageSize);
			if (type == OrderConstant.ORDER_REFUND_CHECK || type == OrderConstant.ORDER_SYNC_CHECK
					|| type == OrderConstant.ORDER_CANCEL_CHECK) {
				for (SportOrder sportOrder : orderList) {
					long orderNo = sportOrder.getOrderNo();
					List<SportOrderDetail> detailList = orderDao.getSportOrderDetailListByOrderNo(orderNo,
							detailTableName);
					sportOrder.setSportOrderDetailList(detailList);
				}
			}
		} catch (Exception e) {
			ScanLog.run.error("竞技彩,business扫描订单列表,tableName=" + mainTableName + ",scanType=" + type
					+ "(1未付款订单,2出票,3撤单,4订单同步,5删除订单,6退款)", e);
			return null;
		}
		return orderList;
	}

	@Override
	public int deleteSportOrder(String mainTableName, String detailTableName, String deleteTime) {
		int isSuccess = 0;
		try {
			isSuccess = orderDao.deleteSportOrder(mainTableName, detailTableName, deleteTime);
		} catch (Exception e) {
			Log.run.error("删除竞技彩订单发生异常,mainTableName=" + mainTableName, e);
			return isSuccess;
		}
		return isSuccess;
	}

	@Override
	public SportOrder findSportOrderByParams(String partnerId, String tradeId) {
		// TODO Auto-generated method stub
		SportOrder order = null;
		try {
			String mainTableName = DbGenerator.getSportOrderTableNameMain(tradeId);
			String detailTableName = DbGenerator.getSportOrderTableNameDetail(tradeId);
			Log.run.debug("竞技彩查询订单,partnerId=%s,tradeId=%s,mainTableName=%s", partnerId, tradeId, mainTableName);
			order = orderDao.findSportOrderByParams(partnerId, tradeId, mainTableName, detailTableName);
		} catch (Exception e) {
			Log.run.error("竞技彩查询订单发生异常", e);
			return null;
		}
		return order;
	}

}
