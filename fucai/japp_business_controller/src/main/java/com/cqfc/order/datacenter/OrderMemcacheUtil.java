package com.cqfc.order.datacenter;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.context.ApplicationContext;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.order.model.UpdateSportOrderStatus;
import com.cqfc.protocol.businesscontroller.PrintMatch;
import com.cqfc.util.ConstantsUtil;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class OrderMemcacheUtil {

	/**
	 * 数字彩订单创建后加入memcache
	 * 
	 * @param order
	 * @return
	 */
	public static boolean createOrderMemcache(Order order) {
		boolean operateValue = false;
		long orderNo = order.getOrderNo();
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			operateValue = memcachedClient.set(memKey, 0, order);
			String orderNoMemKey = getOrderNoMemKey(order.getPartnerId(), order.getTradeId());
			boolean orderValue = memcachedClient.set(orderNoMemKey, 0, orderNo);
			Log.fucaibiz
					.debug("after create order add memcache,orderNo=%d,orderMemKey=%s,operateValue=%b,orderNoMemKey=%s,orderNoValue=%b",
							orderNo, memKey, operateValue, orderNoMemKey, orderValue);
		} catch (Exception e) {
			Log.fucaibiz.error("订单创建后,加入memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	/**
	 * 竞技彩订单创建后加入memcache
	 * 
	 * @param order
	 * @return
	 */
	public static boolean createSportOrderMemcache(SportOrder order) {
		boolean operateValue = false;
		long orderNo = order.getOrderNo();
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			operateValue = memcachedClient.set(memKey, 0, order);
			String orderNoMemKey = getOrderNoMemKey(order.getPartnerId(), order.getTradeId());
			boolean orderValue = memcachedClient.set(orderNoMemKey, 0, orderNo);
			Log.fucaibiz.debug(
					"竞技彩加入memcache,orderNo=%d,orderMemKey=%s,operateValue=%b,orderNoMemKey=%s,orderNoValue=%b",
					orderNo, memKey, operateValue, orderNoMemKey, orderValue);
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩订单创建后,加入memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	/**
	 * 数字彩更新订单状态后更新memcache
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @return
	 */
	public static boolean updateStatusMemcache(long orderNo, int orderStatus) {
		boolean operateValue = false;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			GetsResponse<Order> result = memcachedClient.gets(memKey);
			if (null != result && !"".equals(result)) {
				Order order = result.getValue();
				if (null != order && !"".equals(order) && order.getOrderStatus() < orderStatus) {
					long cas = result.getCas();
					order.setOrderStatus(orderStatus);
					if (orderStatus == Order.OrderStatus.ORDER_CANCEL.getValue()) {
						order.setErrCodeStatus(ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR);
						order.setErrCodeRemark("扣款异常或者帐号钱数不足");
					}
					boolean memResponse = memcachedClient.cas(memKey, 0, order, cas);
					Log.fucaibiz.debug("memcache cas,orderNo=%d,cas=%s,orderStatus=%d,memResponse=%b", orderNo, cas,
							orderStatus, memResponse);
					if (!memResponse) {
						boolean memDelete = memcachedClient.delete(memKey);
						Log.fucaibiz.debug("memcache delete,orderNo=%d,cas=%s,memDeleteValue=%b,memResponse=%b",
								orderNo, cas, memDelete, memResponse);
					} else {
						operateValue = true;
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.debug("memcache更新订单状态异常,删除memcache,orderNo=%d,deleteValue=%b", orderNo,
					deleteOrderMemcache(orderNo));
			Log.fucaibiz.error("订单状态更新后,更新memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	/**
	 * 竞技彩订单状态修改后更新memcache
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @return
	 */
	public static boolean updateSportStatusMemcache(long orderNo, int orderStatus) {
		boolean operateValue = false;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			GetsResponse<SportOrder> result = memcachedClient.gets(memKey);
			if (null != result && !"".equals(result)) {
				SportOrder order = result.getValue();
				if (null != order && !"".equals(order) && order.getOrderStatus() < orderStatus) {
					long cas = result.getCas();
					order.setOrderStatus(orderStatus);
					if (orderStatus == Order.OrderStatus.ORDER_CANCEL.getValue()) {
						order.setErrCodeStatus(ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR);
						order.setErrCodeRemark("扣款异常或者帐号钱数不足");
					}
					boolean memResponse = memcachedClient.cas(memKey, 0, order, cas);
					Log.fucaibiz.debug("竞技彩,memcache cas,orderNo=%d,cas=%s,orderStatus=%d,memResponse=%b", orderNo,
							cas, orderStatus, memResponse);
					if (!memResponse) {
						boolean memDelete = memcachedClient.delete(memKey);
						Log.fucaibiz.debug("竞技彩,memcache delete,orderNo=%d,cas=%s,memDeleteValue=%b,memResponse=%b",
								orderNo, cas, memDelete, memResponse);
					} else {
						operateValue = true;
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.debug("竞技彩,memcache更新订单状态异常,删除memcache,orderNo=%d,deleteValue=%b", orderNo,
					deleteOrderMemcache(orderNo));
			Log.fucaibiz.error("竞技彩,订单状态更新后,更新memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	/**
	 * 竞技彩出票成功更新memcache
	 * 
	 * @param updateOrderStatus
	 * @return
	 */
	public static boolean updateSportPrintSuccessMemcache(UpdateSportOrderStatus updateOrderStatus) {
		boolean operateValue = false;
		long orderNo = updateOrderStatus.getOrderNo();
		int orderStatus = updateOrderStatus.getOrderStatus();
		try {

			String ticketNo = updateOrderStatus.getTicketNo();
			String printTime = updateOrderStatus.getPrintTime();

			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			GetsResponse<SportOrder> result = memcachedClient.gets(memKey);
			if (null != result && !"".equals(result)) {
				SportOrder order = result.getValue();
				if (null != order && !"".equals(order) && order.getOrderStatus() < orderStatus) {
					long cas = result.getCas();
					order.setOrderStatus(orderStatus);
					order.setTicketNo(ticketNo);
					order.setPrintTime(printTime);
					for (SportOrderDetail detail : order.getSportOrderDetailList()) {
						String transferId = detail.getTransferId();
						PrintMatch match = null;
						for (PrintMatch printMatch : updateOrderStatus.getMatchList()) {
							if (transferId.equals(printMatch.getTransferId())) {
								match = printMatch;
							}
						}
						if (null != match) {
							detail.setSp(match.getSp());
							detail.setRq(match.getRq());
						}
					}
					boolean memResponse = memcachedClient.cas(memKey, 0, order, cas);
					Log.fucaibiz.debug("竞技彩出票成功,memcache cas,orderNo=%d,cas=%s,orderStatus=%d,memResponse=%b", orderNo,
							cas, orderStatus, memResponse);
					if (!memResponse) {
						boolean memDelete = memcachedClient.delete(memKey);
						Log.fucaibiz.debug(
								"竞技彩出票成功,memcache delete,orderNo=%d,cas=%s,memDeleteValue=%b,memResponse=%b", orderNo,
								cas, memDelete, memResponse);
					} else {
						operateValue = true;
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.debug("竞技彩出票成功,memcache更新订单状态异常,删除memcache,orderNo=%d,deleteValue=%b", orderNo,
					deleteOrderMemcache(orderNo));
			Log.fucaibiz.error("竞技彩出票成功,订单状态更新后,更新memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	public static boolean verifyOrderIsNotInMemcache(long orderNo) {
		boolean operateValue = false;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			Order order = memcachedClient.get(memKey);
			if (null == order || "".equals(order)) {
				operateValue = true;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("校验memcache中是否存在该订单发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	/**
	 * 更新数字彩订单同步状态memcache
	 * 
	 * @param orderNo
	 * @param sync
	 * @return
	 */
	public static boolean updateOrderSyncMemcache(long orderNo, int sync) {
		boolean operateValue = false;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			Order order = memcachedClient.get(memKey);
			if (null != order && !"".equals(order)) {
				order.setIsSyncSuccess(sync);
				boolean memUpdate = memcachedClient.set(memKey, 0, order);
				if (memUpdate) {
					operateValue = true;
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("更新订单同步状态memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	/**
	 * 更新竞技彩订单同步状态memcache
	 * 
	 * @param orderNo
	 * @param sync
	 * @return
	 */
	public static boolean updateSportOrderSyncMemcache(long orderNo, int sync) {
		boolean operateValue = false;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			SportOrder order = memcachedClient.get(memKey);
			if (null != order && !"".equals(order)) {
				order.setIsSyncSuccess(sync);
				boolean memUpdate = memcachedClient.set(memKey, 0, order);
				if (memUpdate) {
					operateValue = true;
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,更新订单同步状态memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	public static long getOrderNoFromMemcache(String partnerId, String ticketId) {
		long orderNo = 0;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String orderNoMemKey = getOrderNoMemKey(partnerId, ticketId);
			Long tempOrderNo = memcachedClient.get(orderNoMemKey);
			Log.fucaibiz.debug("get orderNo from memcache,orderNoMemKey=" + orderNoMemKey + ",orderNo=" + tempOrderNo);
			if (null != tempOrderNo && !"".equals(tempOrderNo)) {
				orderNo = tempOrderNo;
			} else {
				orderNo = 0;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("从memcache中获取订单编号发生异常,ticketId=" + ticketId, e);
		}
		return orderNo;
	}

	public static boolean deleteOrderMemcache(long orderNo) {
		boolean operateValue = false;
		try {
			MemcachedClient memcachedClient = getMemcachedClientBean();
			String memKey = orderMemKey(orderNo);
			operateValue = memcachedClient.delete(memKey);
		} catch (Exception e) {
			Log.fucaibiz.error("删除订单memcache发生异常,orderNo=" + orderNo, e);
		}
		return operateValue;
	}

	private static MemcachedClient getMemcachedClientBean() {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		return ctx.getBean("memcachedClient", MemcachedClient.class);
	}

	public static String orderMemKey(long orderNo) {
		return ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER + orderNo;
	}

	public static String getOrderNoMemKey(String partnerId, String tradeId) {
		return partnerId + "@" + tradeId;
	}
}
