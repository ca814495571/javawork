package com.cqfc.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.service.IOrderService;
import com.cqfc.order.util.DbGenerator;
import com.cqfc.order.util.OrderDynamicUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class OrderService {

	@Resource(name = "orderServiceImpl")
	private IOrderService orderService;

	/**
	 * 创建投注订单
	 * 
	 * @param order
	 * @return
	 */
	public int createOrder(Order order) {
		int returnValue = 0;
		try {
			long orderNo = order.getOrderNo();
			String dbName = getDbName(OrderDynamicUtil.MASTER, orderNo);
			setDynamicDataSource(dbName);
			returnValue = orderService.createOrder(order);
			Log.run.debug("创建投注订单,orderNo=%d,dbName=%s,returnValue=%d", orderNo, dbName, returnValue);
		} catch (Exception e) {
			Log.run.error("创建投注订单发生异常", e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 根据订单编号查询投注订单
	 * 
	 * @param orderNo
	 *            订单编号
	 * @return
	 */
	public Order findOrderByOrderNo(long orderNo) {
		Order order = null;
		try {
			String dbName = getDbName(OrderDynamicUtil.SLAVE, orderNo);
			setDynamicDataSource(dbName);
			order = orderService.findOrderByOrderNo(orderNo);
			Log.run.debug("查询投注订单,orderNo=%d,dbName=%s", orderNo, dbName);
		} catch (Exception e) {
			Log.run.error("查询投注订单发生异常", e);
			return order;
		}
		return order;
	}

	/**
	 * 更新订单状态
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param orderStatus
	 *            订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功
	 *            11订单取消
	 * @return
	 */
	public int updateOrderStatus(long orderNo, int orderStatus) {
		int returnValue = 0;
		try {
			String dbName = getDbName(OrderDynamicUtil.MASTER, orderNo);
			setDynamicDataSource(dbName);
			returnValue = orderService.updateOrderStatus(orderNo, orderStatus);
			Log.run.debug("更新订单状态,orderNo=%d,dbName=%s,orderStatus=%d,returnValue=%d", orderNo, dbName, orderStatus,
					returnValue);
		} catch (Exception e) {
			Log.run.error("更新订单状态发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 更新订单是否同步到全局数据库
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param syncValue
	 *            是否同步到全局数据库 0未同步 1同步成功 2同步失败
	 * @return
	 */
	public int updateOrderSync(long orderNo, int syncValue) {
		int returnValue = 0;
		try {
			String dbName = getDbName(OrderDynamicUtil.MASTER, orderNo);
			setDynamicDataSource(dbName);
			returnValue = orderService.updateOrderSync(orderNo, syncValue);
			Log.run.debug("更新订单同步状态,orderNo=%d,dbName=%s,syncValue=%d,returnValue=%d", orderNo, dbName, syncValue,
					returnValue);
		} catch (Exception e) {
			Log.run.error("更新订单同步状态发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 扫描任务获取订单列表
	 * 
	 * @param tableName
	 *            表名
	 * @param lotteryId
	 *            彩种ID
	 * @param issueNo
	 *            期号
	 * @param type
	 *            扫描类型
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public List<Order> getOrderList(String tableName, String lotteryId, String issueNo, int type, int currentPage,
			int pageSize) {
		List<Order> orderList = null;
		orderList = orderService.getOrderList(tableName, lotteryId, issueNo, type, currentPage, pageSize);
		return orderList;
	}

	/**
	 * 期号状态更新为已派奖后，且订单创建超过5天，已同步到全局数据库，则删除订单(定时任务)
	 * 
	 * @param tableName
	 * @param deleteTime
	 * @return
	 */
	public int deleteOrder(String tableName, String deleteTime) {
		int returnValue = 0;
		try {
			returnValue = orderService.deleteOrder(tableName, deleteTime);
		} catch (Exception e) {
			Log.run.error("删除订单发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 更新订单状态
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param orderStatus
	 *            订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功
	 *            11订单取消
	 * @param errorCode
	 *            异常状态码
	 * @param errorRemark
	 *            异常备注
	 * @return
	 */
	public int updateOrderStatusAndRemark(long orderNo, int orderStatus, String errorCode, String errorRemark) {
		int returnValue = 0;
		try {
			String dbName = getDbName(OrderDynamicUtil.MASTER, orderNo);
			setDynamicDataSource(dbName);
			returnValue = orderService.updateOrderStatusAndRemark(orderNo, orderStatus, errorCode, errorRemark);
			Log.run.debug("支付失败取消订单,更新订单状态,orderNo=%d,dbName=%s,orderStatus=%d,errorRemark=%s,returnValue=%d", orderNo,
					dbName, orderStatus, errorRemark, returnValue);
		} catch (Exception e) {
			Log.run.error("支付失败取消订单,更新订单状态发生异常", e);
		}
		return returnValue;
	}

	public Order findOrderByParams(String partnerId, String tradeId) {
		Order order = null;
		try {
			String dbName = OrderDynamicUtil.SLAVE + "_" + DbGenerator.getDbName(OrderDynamicUtil.PRE_DBNAME, tradeId);
			setDynamicDataSource(dbName);
			order = orderService.findOrderByParams(partnerId, tradeId);
			Log.run.debug("查询投注订单,partnerId=%s,tradeId=%s,dbName=%s", partnerId, tradeId, dbName);
		} catch (Exception e) {
			Log.run.error("查询投注订单发生异常", e);
			return order;
		}
		return order;
	}

	private void setDynamicDataSource(String dbName) {
		DbGenerator.setOrderDynamicDataSource(dbName);
	}

	private String getDbName(String masterSlave, long orderNo) {
		String temp = String.valueOf(orderNo);
		int len = temp.length();
		// 目前10库,库标识为2位
		String dbIndex = temp.substring(len - 5, len - 3);
		return masterSlave + "_" + OrderDynamicUtil.PRE_DBNAME + dbIndex;
	}

	/**
	 * 竞技彩获取DBName
	 * 
	 * @param masterSlave
	 * @param orderNo
	 * @return
	 */
	private String getSportDbName(String masterSlave, long orderNo) {
		String temp = String.valueOf(orderNo);
		int len = temp.length();
		// 目前10库,库标识为2位
		String dbIndex = temp.substring(len - 5, len - 3);
		return masterSlave + "_" + OrderDynamicUtil.PRE_COMPETITION_DBNAME + dbIndex;
	}

	/**
	 * 根据订单编号查询竞技彩订单
	 * 
	 * @param orderNo
	 * @return
	 */
	public SportOrder findSportOrderByOrderNo(long orderNo) {
		SportOrder order = null;
		try {
			String dbName = getSportDbName(OrderDynamicUtil.SLAVE, orderNo);
			setDynamicDataSource(dbName);
			order = orderService.findSportOrderByOrderNo(orderNo);
			Log.run.debug("竞技彩,查询投注订单,orderNo=%d,dbName=%s", orderNo, dbName);
		} catch (Exception e) {
			Log.run.error("竞技彩,查询投注订单发生异常", e);
			return null;
		}
		return order;
	}

	/**
	 * 根据parnterId、tradeId查询竞技彩订单
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @return
	 */
	public SportOrder findSportOrderByParam(String partnerId, String tradeId) {
		SportOrder order = null;
		try {
			String dbIndex = DbGenerator.getDbName(OrderDynamicUtil.PRE_COMPETITION_DBNAME, tradeId);
			String dbName = OrderDynamicUtil.SLAVE + "_" + dbIndex;
			setDynamicDataSource(dbName);
			order = orderService.findSportOrderByParams(partnerId, tradeId);
			Log.run.debug("竞技彩查询投注订单,partnerId=%s,tradeId=%s,dbName=%s", partnerId, tradeId, dbName);
		} catch (Exception e) {
			Log.run.error("竞技彩查询投注订单发生异常", e);
			return null;
		}
		return order;
	}

	/**
	 * 删除竞技彩订单
	 * 
	 * @param mainTableName
	 * @param detailTableName
	 * @param deleteTime
	 * @return
	 */
	public int deleteSportOrder(String mainTableName, String detailTableName, String deleteTime) {
		int returnValue = 0;
		try {
			returnValue = orderService.deleteSportOrder(mainTableName, detailTableName, deleteTime);
		} catch (Exception e) {
			Log.run.error("删除竞技彩订单发生异常", e);
		}
		return returnValue;
	}

}
