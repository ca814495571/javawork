package com.cqfc.user.order.service;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.protocol.userorder.Order;
import com.cqfc.protocol.userorder.PcUserOrder;
import com.cqfc.util.Pair;

public interface IUserOrderService {

	
	
	/**
	 * 添加用户订单
	 * @param order
	 * @return
	 */
	public int addUserOrder(Order order);
	/**
	 * 根据用户id查询订单信息
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public Order getUserOrderByUserId(long userId, String orderNo);

	/**
	 * 根据用户id，订单类型，彩种查询订单信息
	 * @param order
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PcUserOrder getUserOrder(Order order, int pageNum, int pageSize);
	
	/**
	 * 根据用户Id，合作商Id,流水号ID(平台orderNo)，pid(ticketId)查询订单中奖状态
	 * @param userId
	 * @param partnerId
	 * @param orderNo
	 * @param ticketId
	 * @return
	 */
	public Order getUserPrizeStatus(long userId, String partnerId,
			String orderNo, String ticketId);
	
	
	/**
	 * 初始化添加同步订单线程任务
	 */
	public void initAddOrderTask(ApplicationContext applicationContext);
	
	
	/**
	 * 重启应用start（）,之前将日志中的order放入队列中
	 * @return
	 */
	public int logOrderToQueue(List<Pair<File, BlockingQueue>> pairs);
	/**
	 * 重启应用start（）,之后从队列中中恢复订单数据到数据库
	 * @return
	 */
	public int queueToDb(List<Pair<File, BlockingQueue>> pairs);
	
	
	/**
	 * 根据库名查找日志恢复表中的最后一条入库数据
	 * @param dbName
	 * @return
	 */
	public RecoverOrderIndex getIndexByDbName(String dbName);
	
	/**
	 * 修改日志恢复表中的标示符
	 * @param dbName
	 * @param flag
	 * @return
	 */
	public int updateFlag(String dbName,int flag);
	
	
	
	/**
	 * 根据订单号和日志文件恢复将需要恢复的数据放入队列中
	 */
	public int addOrderToQueue(Pair<File, BlockingQueue> pair,String orderNo);
}
