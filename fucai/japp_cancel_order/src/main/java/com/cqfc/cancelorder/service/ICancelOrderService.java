package com.cqfc.cancelorder.service;

import java.util.List;

import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.ReturnData;
import com.cqfc.protocol.cancelorder.SportCancelOrder;

/**
 * @author liwh
 */
public interface ICancelOrderService {

	/**
	 * 创建撤单订单
	 * 
	 * @param cancelOrder
	 * @return
	 */
	public int createCancelOrder(CancelOrder cancelOrder);

	/**
	 * 获取撤单订单列表
	 * 
	 * @param outTicketStatus
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<CancelOrder> getCancelOrderListForScan(int outTicketStatus, int currentPage, int pageSize);

	/**
	 * 更新撤单订单出票状态
	 * 
	 * @param orderNo
	 * @param outTicketStatus
	 *            3出票中 4出票成功 5出票失败
	 * @return
	 */
	public int updateOutTicketStatus(String orderNo, int outTicketStatus);

	/**
	 * 获取转移但出票成功的票数
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public long getSuccessTicketCancelOrder(String lotteryId, String issueNo);

	/**
	 * 分页获取转移单列表
	 * 
	 * @param partnerId
	 * @param lotteryId
	 * @param issueNo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public ReturnData getCancelOrder(CancelOrder cancelOrder, int currentPage, int pageSize);

	/**
	 * 创建竞技彩转移订单
	 * 
	 * @param sportCancelOrder
	 * @return
	 */
	public int createSportCancelOrder(SportCancelOrder sportCancelOrder);

	/**
	 * 获取竞技彩转移订单list
	 * 
	 * @param outTicketStatus
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<SportCancelOrder> getSportCancelOrderListForScan(int outTicketStatus, int currentPage, int pageSize);

	/**
	 * 更新竞技彩转移订单出票状态
	 * 
	 * @param outTicketStatus
	 * @param sportPrint
	 * @return
	 */
	public int updateSportCancelOrder(int outTicketStatus, SportPrint sportPrint);
}
