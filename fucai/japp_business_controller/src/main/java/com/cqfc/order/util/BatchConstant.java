package com.cqfc.order.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.cqfc.order.model.CreateOrderMsg;
import com.cqfc.order.model.UpdateOrderStatus;
import com.cqfc.order.model.UpdateSportOrderStatus;
import com.cqfc.order.model.UpdateSyncStatus;

/**
 * @author liwh
 */
public class BatchConstant {

	/**
	 * 创建订单批处理map
	 */
	public static ConcurrentMap<Long, CreateOrderMsg> createOrderMap = new ConcurrentHashMap<Long, CreateOrderMsg>();

	/**
	 * 订单状态批处理map
	 */
	public static ConcurrentMap<String, UpdateOrderStatus> orderStatusMap = new ConcurrentHashMap<String, UpdateOrderStatus>();

	/**
	 * 订单同步状态批处理map
	 */
	public static ConcurrentMap<Long, UpdateSyncStatus> orderSyncMap = new ConcurrentHashMap<Long, UpdateSyncStatus>();

	/**
	 * 加入批处理队列成功
	 */
	public static final int BATCH_ADD_QUEUE_SUCCESS = 1;

	/**
	 * 加入批处理队列失败
	 */
	public static final int BATCH_ADD_QUEUE_FAILED = 2;

	/**
	 * 批处理操作成功
	 */
	public static final int BATCH_OPERATE_SUCCESS = 1;

	/**
	 * 批处理操作失败
	 */
	public static final int BATCH_OPERATE_FAILED = 2;

	/**
	 * 批处理创建订单重复
	 */
	public static final int BATCH_OPERATE_CREATE_REPEAT = 3;

	/**
	 * 创建竞技彩订单批处理map
	 */
	public static ConcurrentMap<Long, CreateOrderMsg> createSportOrderMap = new ConcurrentHashMap<Long, CreateOrderMsg>();

	/**
	 * 竞技彩订单状态批处理map
	 */
	public static ConcurrentMap<String, UpdateSportOrderStatus> sportOrderStatusMap = new ConcurrentHashMap<String, UpdateSportOrderStatus>();

	/**
	 * 竞技彩订单同步状态批处理map
	 */
	public static ConcurrentMap<Long, UpdateSyncStatus> sportOrderSyncMap = new ConcurrentHashMap<Long, UpdateSyncStatus>();

}
