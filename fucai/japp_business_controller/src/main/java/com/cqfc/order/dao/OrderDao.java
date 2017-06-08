package com.cqfc.order.dao;

import java.util.Date;
import java.util.List;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.order.dao.mapper.OrderMapper;
import com.cqfc.order.datacenter.OrderMemcacheUtil;
import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class OrderDao {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private MemcachedClient memcachedClient;

	/**
	 * 创建投注订单
	 * 
	 * @param order
	 * @return
	 */
	public int createOrder(Order order, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
			order.setCreateTime(currentTime);
			returnValue = orderMapper.createOrder(order, tableName);
			if (returnValue == 1) {
				// 加入缓存
				long orderNo = order.getOrderNo();
				String memKey = ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER + orderNo;
				memcachedClient.set(memKey, 0, order);
			}
		} catch (DuplicateKeyException e) {
			Log.error("创建投注订单发生唯一键冲突,订单编号：" + order.getOrderNo() + ",异常信息：", e);
			Log.run.error("创建投注订单发生唯一键冲突,订单编号：" + order.getOrderNo() + ",异常信息：", e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.error("创建投注订单发生异常,订单编号：" + order.getOrderNo() + ",异常信息：", e);
			Log.run.error("创建投注订单发生异常,订单编号：" + order.getOrderNo() + ",异常信息：", e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 根据订单编号查询订单信息
	 * 
	 * @param orderNo
	 *            订单编号
	 * @return
	 */
	public Order findOrderByOrderNo(long orderNo, String tableName) {
		Order order = null;
		try {
			String memKey = ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER + orderNo;
			order = memcachedClient.get(memKey);
			Log.run.debug("find order from memcache,orderNo=%d,order=%s", orderNo, order);
			if (order == null || "".equals(order)) {
				order = orderMapper.findOrderByOrderNo(orderNo, tableName);
				if (null != order) {
					memcachedClient.set(memKey, 0, order);
				}
			}
		} catch (Exception e) {
			Log.error("根据订单编号" + orderNo + "查询订单信息发生异常：", e);
			Log.run.error("根据订单编号" + orderNo + "查询订单信息发生异常：", e);
			return null;
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
	public int modifyOrderStatus(long orderNo, int orderStatus, String tableName, String errorCode, String errorRemark) {
		int isSuccess = 0;
		String memKey = ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER + orderNo;
		try {
			if (null != errorCode && !"".equals(errorCode)) {
				isSuccess = orderMapper.modifyOrderStatusAndRemark(orderNo, orderStatus, tableName, errorCode,
						errorRemark);
				if (isSuccess > 0) {
					memcachedClient.delete(memKey);
				}
			} else {
				if (orderStatus == Order.OrderStatus.DRAWER_FAILURE.getValue()
						|| orderStatus == Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()) {
					isSuccess = orderMapper.modifyTicketResult(orderNo, orderStatus, tableName);
				} else {
					isSuccess = orderMapper.modifyOrderStatus(orderNo, orderStatus, tableName);
				}
				if (isSuccess > 0) {
					// 更新缓存
					GetsResponse<Order> result = memcachedClient.gets(memKey);
					if (null != result && !"".equals(result)) {
						Order order = result.getValue();
						if (null != order && !"".equals(order) && order.getOrderStatus() < orderStatus) {
							long cas = result.getCas();
							order.setOrderStatus(orderStatus);
							boolean memResponse = memcachedClient.cas(memKey, 0, order, cas);
							Log.run.debug("memcache cas,orderNo=%d,cas=%s,orderStatus=%d,memResponse=%b", orderNo, cas,
									orderStatus, memResponse);
							if (!memResponse) {
								boolean memDelete = memcachedClient.delete(memKey);
								Log.run.debug("memcache delete,orderNo=%d,cas=%s,memDeleteValue=%b,memResponse=%b",
										orderNo, cas, memDelete, memResponse);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			try {
				memcachedClient.delete(memKey);
			} catch (Exception e1) {
				Log.error("更新订单状态发生异常时,删除memcache发生异常,orderNo：" + orderNo + ",orderStatus:" + orderStatus + ",异常信息：",
						e1);
				Log.run.error("更新订单状态发生异常时,删除memcache发生异常,orderNo：" + orderNo + ",orderStatus:" + orderStatus
						+ ",异常信息：", e1);
			}
			Log.error("更新订单状态发生异常,orderNo：" + orderNo + ",orderStatus:" + orderStatus + ",异常信息：", e);
			Log.run.error("更新订单状态发生异常,orderNo：" + orderNo + ",orderStatus:" + orderStatus + ",异常信息：", e);
		}
		return isSuccess;
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
	public int updateOrderSync(long orderNo, int syncValue, String tableName) {
		int isSuccess = 0;
		try {
			isSuccess = orderMapper.updateOrderSync(orderNo, syncValue, tableName);
			if (isSuccess == 1) {
				// 更新缓存
				String memKey = ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER + orderNo;
				Order order = memcachedClient.get(memKey);
				if (null != order && !"".equals(order)) {
					order.setIsSyncSuccess(syncValue);
					memcachedClient.set(memKey, 0, order);
				}
			}
		} catch (Exception e) {
			Log.error("更新订单是否同步状态发生异常,订单编号：" + orderNo + ",异常信息：", e);
			Log.run.error("更新订单是否同步状态发生异常,订单编号：" + orderNo + ",异常信息：", e);
		}
		return isSuccess;
	}

	/**
	 * 扫描订单数据,分页获取list
	 * 
	 * @param tableName
	 *            表名
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public List<Order> getOrderList(String tableName, String lotteryId, String issueNo, int type, int currentPage,
			int pageSize) {
		StringBuffer conditions = new StringBuffer();
		conditions.append(" lotteryId='" + lotteryId + "'");
		conditions.append(" and issueNo='" + issueNo + "'");

		if (type == OrderConstant.ORDER_PRINT_CHECK) {
			String currentTime = DateUtil.addDateMinut(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date()),
					"MINUTE", -1);
			conditions.append(" and (orderStatus=" + Order.OrderStatus.HAS_PAYMENT.getValue() + " or orderStatus="
					+ Order.OrderStatus.IN_TICKET.getValue() + ")");
			conditions.append(" and lastUpdateTime < '" + currentTime + "'");
		}
		if (type == OrderConstant.ORDER_CANCEL_CHECK) {
			conditions.append(" and (orderStatus=" + Order.OrderStatus.WAIT_PAYMENT.getValue() + " or orderStatus="
					+ Order.OrderStatus.HAS_PAYMENT.getValue() + " or orderStatus="
					+ Order.OrderStatus.IN_TICKET.getValue() + " or orderStatus="
					+ Order.OrderStatus.DRAWER_FAILURE.getValue() + ")");
		}
		if (type == OrderConstant.ORDER_SYNC_CHECK) {
			conditions.append(" and isSyncSuccess !=" + Order.OrderSync.SYNC_SUCCESS.getValue());
			conditions.append(" and (orderStatus=" + Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
					+ " or orderStatus=" + Order.OrderStatus.REFUND_SUCCESS.getValue() + " or orderStatus="
					+ Order.OrderStatus.ORDER_CANCEL.getValue() + ")");
		}
		conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
		return orderMapper.getOrderList(tableName, conditions.toString());
	}

	/**
	 * 期号状态更新为已派奖后，且订单创建超过5天，已同步到全局数据库，则删除订单
	 * 
	 * @param tableName
	 * @param deleteTime
	 * @return
	 */
	public int deleteOrder(String tableName, String deleteTime) {
		int returnValue = 0;
		try {
			returnValue = orderMapper.deleteOrder(tableName, deleteTime, Order.OrderSync.SYNC_SUCCESS.getValue());
		} catch (Exception e) {
			Log.error("删除订单发生异常", e);
			Log.run.error("删除订单发生异常", e);
		}
		return returnValue;
	}

	public List<Order> getOrderListScan(String tableName, int type, int currentPage, int pageSize) {
		StringBuffer conditions = new StringBuffer();
		conditions.append(" 1=1");

		String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
		currentTime = DateUtil.addDateMinut(currentTime, "MINUTE", -1);

		if (type == OrderConstant.ORDER_PAYMENT_CHECK) {
			conditions.append(" and orderStatus=" + Order.OrderStatus.WAIT_PAYMENT.getValue()
					+ " and lastUpdateTime < '" + currentTime + "'");
		}
		if (type == OrderConstant.ORDER_SYNC_CHECK) {
			conditions.append(" and isSyncSuccess !=" + Order.OrderSync.SYNC_SUCCESS.getValue());
			conditions.append(" and (orderStatus=" + Order.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
					+ " or orderStatus=" + Order.OrderStatus.REFUND_SUCCESS.getValue() + " or orderStatus="
					+ Order.OrderStatus.ORDER_CANCEL.getValue() + ") and lastUpdateTime < '" + currentTime + "'");
		}
		if (type == OrderConstant.ORDER_REFUND_CHECK) {
			conditions.append(" and (orderStatus=" + Order.OrderStatus.DRAWER_FAILURE.getValue() + " or orderStatus="
					+ Order.OrderStatus.REFUNDING.getValue() + ") and lastUpdateTime < '" + currentTime + "'");
		}
		conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
		return orderMapper.getOrderList(tableName, conditions.toString());
	}

	public Order findOrderByParams(String partnerId, String tradeId, String tableName) {
		Order order = null;
		try {
			List<Order> orderList = orderMapper.findOrderByParams(partnerId, tradeId, tableName);
			String orderNoMemKey = OrderMemcacheUtil.getOrderNoMemKey(partnerId, tradeId);
			if (null != orderList && orderList.size() > 0) {
				order = orderList.get(0);
				String orderMemKey = OrderMemcacheUtil.orderMemKey(order.getOrderNo());
				memcachedClient.set(orderNoMemKey, 0, order.getOrderNo());
				memcachedClient.set(orderMemKey, 0, order);
			}
		} catch (Exception e) {
			Log.error("根据partnerId=" + partnerId + ",tradeId=" + tradeId + "查询订单信息发生异常：", e);
			Log.run.error("根据partnerId=" + partnerId + ",tradeId=" + tradeId + "查询订单信息发生异常：", e);
			return null;
		}
		return order;
	}

	/**
	 * 查询竞技彩订单
	 * 
	 * @param orderNo
	 * @param mainTableName
	 * @param detailTableName
	 * @return
	 */
	public SportOrder findSportOrderByOrderNo(long orderNo, String mainTableName, String detailTableName) {
		SportOrder order = null;
		try {
			String memKey = ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER + orderNo;
			order = memcachedClient.get(memKey);
			Log.run.debug("find order from memcache,orderNo=%d,order=%s", orderNo, order);
			if (order == null || "".equals(order)) {
				order = orderMapper.findSportOrderByOrderNo(orderNo, mainTableName);
				if (null != order) {
					List<SportOrderDetail> detailList = orderMapper.findSportOrderDetailByOrderNo(orderNo,
							detailTableName);
					order.setSportOrderDetailList(detailList);
					memcachedClient.set(memKey, 0, order);
				}
			}
		} catch (Exception e) {
			Log.error("查询竞技彩订单发生异常,orderNo=" + orderNo, e);
			Log.run.error("查询竞技彩订单发生异常,orderNo=" + orderNo, e);
			return null;
		}
		return order;
	}

	/**
	 * 竞技彩定时扫描任务,获取符合条件的订单列表
	 * 
	 * @param mainTableName
	 * @param detailTableName
	 * @param type
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<SportOrder> getSportOrderListScan(String mainTableName, String detailTableName, int type,
			int currentPage, int pageSize) {
		List<SportOrder> sportOrderList = null;
		try {
			StringBuffer conditions = new StringBuffer();
			conditions.append(" 1=1");

			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());

			String afterOneMinute = DateUtil.addDateMinut(currentTime, "MINUTE", -1);

			if (type == OrderConstant.ORDER_PAYMENT_CHECK) {
				conditions.append(" and orderStatus=" + SportOrder.OrderStatus.WAIT_PAYMENT.getValue()
						+ " and lastUpdateTime<'" + afterOneMinute + "'");
			}
			if (type == OrderConstant.ORDER_PRINT_CHECK) {
				conditions.append(" and (orderStatus=" + Order.OrderStatus.HAS_PAYMENT.getValue() + " or orderStatus="
						+ Order.OrderStatus.IN_TICKET.getValue() + ")");
				conditions.append(" and lastUpdateTime<'" + afterOneMinute + "'");
				currentTime = DateUtil.addDateMinut(currentTime, "MINUTE", 1);
				conditions.append(" and closeTime>'" + currentTime + "'");
			}
			if (type == OrderConstant.ORDER_CANCEL_CHECK) {
				conditions.append(" and closeTime<='" + currentTime + "'");
				conditions.append(" and orderStatus in(" + SportOrder.OrderStatus.WAIT_PAYMENT.getValue() + ","
						+ SportOrder.OrderStatus.HAS_PAYMENT.getValue() + ","
						+ SportOrder.OrderStatus.IN_TICKET.getValue() + ","
						+ SportOrder.OrderStatus.DRAWER_FAILURE.getValue() + ")");
			}
			if (type == OrderConstant.ORDER_SYNC_CHECK) {
				conditions.append(" and isSyncSuccess!=" + SportOrder.OrderSync.SYNC_SUCCESS.getValue());
				conditions.append(" and (orderStatus=" + SportOrder.OrderStatus.HASTICKET_WAITLOTTERY.getValue()
						+ " or orderStatus=" + SportOrder.OrderStatus.REFUND_SUCCESS.getValue() + " or orderStatus="
						+ SportOrder.OrderStatus.ORDER_CANCEL.getValue() + ") and lastUpdateTime<'" + afterOneMinute
						+ "'");
			}
			if (type == OrderConstant.ORDER_REFUND_CHECK) {
				conditions.append(" and (orderStatus=" + SportOrder.OrderStatus.DRAWER_FAILURE.getValue()
						+ " or orderStatus=" + SportOrder.OrderStatus.REFUNDING.getValue() + ") and lastUpdateTime<'"
						+ afterOneMinute + "'");
			}
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			// Log.run.debug("竞技彩订单扫描type=%d,sql=%s", type,
			// conditions.toString());
			sportOrderList = orderMapper.getSportOrderList(mainTableName, conditions.toString());
		} catch (Exception e) {
			Log.run.error("竞技彩定时扫描任务,获取符合条件的订单列表发生异常,type=" + type, e);
		}
		return sportOrderList;
	}

	/**
	 * 竞技彩根据订单编号查询细表list
	 * 
	 * @param orderNo
	 * @param detailTableName
	 * @return
	 */
	public List<SportOrderDetail> getSportOrderDetailListByOrderNo(long orderNo, String detailTableName) {
		List<SportOrderDetail> detailList = null;
		try {
			detailList = orderMapper.findSportOrderDetailByOrderNo(orderNo, detailTableName);
		} catch (Exception e) {
			Log.run.error("竞技彩根据订单编号查询细表list发生异常", e);
		}
		return detailList;
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
			returnValue = orderMapper.deleteSportOrder(mainTableName, detailTableName, deleteTime,
					SportOrder.OrderSync.SYNC_SUCCESS.getValue());
		} catch (Exception e) {
			Log.error("删除竞技彩订单发生异常", e);
			Log.run.error("删除竞技彩订单发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 根据参数查询竞技彩订单
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @param mainTableName
	 * @param detailTableName
	 * @return
	 */
	public SportOrder findSportOrderByParams(String partnerId, String tradeId, String mainTableName,
			String detailTableName) {
		SportOrder order = null;
		try {
			order = orderMapper.findSportOrderByParams(partnerId, tradeId, mainTableName);
			if (null != order && !"".equals(order)) {
				long orderNo = order.getOrderNo();
				List<SportOrderDetail> detailList = orderMapper.findSportOrderDetailByOrderNo(orderNo, detailTableName);
				order.setSportOrderDetailList(detailList);
			}
		} catch (Exception e) {
			Log.error("查询竞技彩订单发生异常", e);
			Log.run.error("查询竞技彩订单发生异常", e);
			return null;
		}
		return order;
	}

}
