package com.cqfc.order.service;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;

/**
 * @author liwh
 */
public interface IOrderService {

	/**
	 * 创建投注订单
	 * 
	 * @param order
	 * @return
	 */
	public int createOrder(Order order);

	/**
	 * 根据订单编号查询投注订单
	 * 
	 * @param orderNo
	 * @return
	 */
	public Order findOrderByOrderNo(long orderNo);

	/**
	 * 更新订单状态
	 * 
	 * @param orderId
	 *            订单ID
	 * @param orderStatus
	 *            订单状态：1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功
	 *            11订单取消
	 * @return
	 */
	public int updateOrderStatus(long orderNo, int orderStatus);

	/**
	 * 更新订单同步状态
	 * 
	 * @param orderNo
	 *            订单编号
	 * @param syncValue
	 *            是否同步到全局数据库 0未同步 1同步成功 2同步失败
	 * @return
	 */
	public int updateOrderSync(long orderNo, int syncValue);

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
			int pageSize);

	/**
	 * 期号状态更新为已派奖后，且订单创建超过5天，已同步到全局数据库，则删除订单(定时任务)
	 * 
	 * @param tableName
	 * @param deleteTime
	 * @return
	 */
	public int deleteOrder(String tableName, String deleteTime);

	/**
	 * business扫描订单记录
	 * 
	 * @param tableName
	 * @param type
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<Order> getOrderListScan(String tableName, int type, int currentPage, int pageSize);

	/**
	 * 更新订单状态(错误状态码)
	 * 
	 * @param orderNo
	 * @param orderStatus
	 * @param errorCode
	 * @param errorRemark
	 * @return
	 */
	public int updateOrderStatusAndRemark(long orderNo, int orderStatus, String errorCode, String errorRemark);

	/**
	 * 启动订单批处理任务
	 * 
	 * @param applicationContext
	 */
	public void initOrderTask(ApplicationContext applicationContext);

	/**
	 * 查询订单信息
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @return
	 */
	public Order findOrderByParams(String partnerId, String tradeId);

	/**
	 * 查询竞技彩订单
	 * 
	 * @param orderNo
	 * @return
	 */
	public SportOrder findSportOrderByOrderNo(long orderNo);

	/**
	 * 根据参数查询竞技彩订单
	 * 
	 * @param partnerId
	 * @param tradeId
	 * @return
	 */
	public SportOrder findSportOrderByParams(String partnerId, String tradeId);

	/**
	 * 竞技彩获取订单列表
	 * 
	 * @param mainTableName
	 * @param detailTableName
	 * @param type
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<SportOrder> getSportOrderListScan(String mainTableName, String detailTableName, int type,
			int currentPage, int pageSize);

	/**
	 * 删除竞技彩订单
	 * 
	 * @param mainTableName
	 * @param detailTableName
	 * @param deleteTime
	 * @return
	 */
	public int deleteSportOrder(String mainTableName, String detailTableName, String deleteTime);
}
