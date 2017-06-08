package com.cqfc.cancelorder.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.cancelorder.dao.mapper.CancelOrderMapper;
import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.ReturnData;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.cancelorder.SportCancelOrderDetail;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class CancelOrderDao {

	@Autowired
	private CancelOrderMapper cancelOrderMapper;

	/**
	 * 创建撤单订单
	 * 
	 * @param cancelOrder
	 * @return
	 */
	public int createCancelOrder(CancelOrder cancelOrder) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderMapper.createCancelOrder(cancelOrder);
		} catch (DuplicateKeyException e) {
			Log.run.error("创建撤单订单发生唯一键冲突,订单编号：" + cancelOrder.getOrderNo(), e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error("创建撤单订单发生异常,orderNo=" + cancelOrder.getOrderNo(), e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 获取撤单订单列表
	 * 
	 * @param outTicketStatus
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<CancelOrder> getCancelOrderListForScan(int outTicketStatus, int currentPage, int pageSize) {
		List<CancelOrder> cancelOrderList = null;
		try {
			StringBuffer conditions = new StringBuffer();
			conditions.append(" outTicketStatus=" + outTicketStatus);
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			cancelOrderList = cancelOrderMapper.getCancelOrderListForScan(conditions.toString());
		} catch (Exception e) {
			Log.run.error("获取撤单订单列表发生异常", e);
			return null;
		}
		return cancelOrderList;
	}

	/**
	 * 更新撤单订单出票状态
	 * 
	 * @param orderNo
	 * @param outTicketStatus
	 * @return
	 */
	public int updateOutTicketStatus(String orderNo, int outTicketStatus) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderMapper.updateOutTicketStatus(orderNo, outTicketStatus);
		} catch (Exception e) {
			Log.run.error("更新撤单订单出票状态发生异常,orderNo=" + orderNo + ",outTicketStatus=" + outTicketStatus, e);
			return 0;
		}
		return returnValue;
	}

	public long getSuccessTicketCancelOrder(String lotteryId, String issueNo) {
		return cancelOrderMapper.getSuccessTicketCancelOrder(lotteryId, issueNo);
	}

	/**
	 * 查询转移单
	 * 
	 * @param partnerId
	 * @param lotteryId
	 * @param issueNo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public ReturnData getCancelOrder(long userId, String orderNo, String partnerId, String lotteryId, String issueNo,
			int currentPage, int pageSize) {
		ReturnData returnData = new ReturnData();
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		if (userId != 0) {
			conditions.append("and userId ='" + userId + "'");
		}
		if (StringUtils.isNotBlank(orderNo)) {
			conditions.append("and orderNo ='" + orderNo + "'");
		}
		if (StringUtils.isNotBlank(partnerId)) {
			conditions.append("and partnerId ='" + partnerId + "'");
		}
		if (StringUtils.isNotBlank(lotteryId)) {
			conditions.append("and lotteryId ='" + lotteryId + "'");
		}
		if (StringUtils.isNotBlank(issueNo)) {
			conditions.append("and issueNo ='" + issueNo + "'");
		}
		int totalSize = countTotalSize(conditions.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		conditions.append(" order by issueNo desc ");
		if (totalPage >= currentPage) {
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
		}
		List<CancelOrder> list = cancelOrderMapper.getCancelOrderList(conditions.toString());
		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算转移单总记录数
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	public int countTotalSize(String conditions) {

		return cancelOrderMapper.countTotalSize(conditions);
	}

	/**
	 * 创建竞技彩转移订单主表
	 * 
	 * @param sportCancelOrder
	 * @return
	 */
	public int createSportCancelOrder(SportCancelOrder sportCancelOrder) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderMapper.createSportCancelOrder(sportCancelOrder);
		} catch (DuplicateKeyException e) {
			Log.run.error("创建竞技彩转移订单主表发生唯一键冲突,orderNo=" + sportCancelOrder.getOrderNo(), e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error("创建竞技彩转移订单主表发生异常,orderNo=" + sportCancelOrder.getOrderNo(), e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 创建竞技彩转移订单细表
	 * 
	 * @param sportCancelOrderDetail
	 * @return
	 */
	public int createSportCancelOrderDetail(SportCancelOrderDetail sportCancelOrderDetail) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderMapper.createSportCancelOrderDetail(sportCancelOrderDetail);
		} catch (DuplicateKeyException e) {
			Log.run.error("创建竞技彩转移订单细表发生唯一键冲突,orderNo=" + sportCancelOrderDetail.getOrderNo(), e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error("创建竞技彩转移订单细表发生异常,orderNo=" + sportCancelOrderDetail.getOrderNo(), e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 获取竞技彩转移订单list
	 * 
	 * @param outTicketStatus
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<SportCancelOrder> getSportCancelOrderListForScan(int outTicketStatus, int currentPage, int pageSize) {
		List<SportCancelOrder> cancelOrderList = null;
		try {
			StringBuffer conditions = new StringBuffer();
			conditions.append(" outTicketStatus=" + outTicketStatus);
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			cancelOrderList = cancelOrderMapper.getSportCancelOrderListForScan(conditions.toString());
		} catch (Exception e) {
			Log.run.error("获取竞技彩转移订单list发生异常", e);
		}
		return cancelOrderList;
	}

	/**
	 * 获取竞技彩订单细表list
	 * 
	 * @param orderNo
	 * @return
	 */
	public List<SportCancelOrderDetail> getSportOrderDetail(long orderNo) {
		List<SportCancelOrderDetail> detailList = null;
		try {
			detailList = cancelOrderMapper.getSportOrderDetail(orderNo);
		} catch (Exception e) {
			Log.run.error("获取竞技彩订单细表list发生异常,orderNo=" + orderNo, e);
		}
		return detailList;
	}

	/**
	 * 更新竞技彩订单主表出票信息
	 * 
	 * @param orderNo
	 * @param outTicketStatus
	 * @param ticketNo
	 * @param printTime
	 * @return
	 */
	public int updateSportCancelOrderMain(long orderNo, int outTicketStatus, String ticketNo, String printTime) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderMapper.updateSportCancelOrderMain(orderNo, outTicketStatus, ticketNo, printTime);
		} catch (Exception e) {
			Log.run.error("更新竞技彩订单主表出票信息发生异常,orderNo=" + orderNo, e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 更新竞技彩订单细表出票信息
	 * 
	 * @param orderNo
	 * @param transferId
	 * @param rq
	 * @param sp
	 * @return
	 */
	public int updateSportCancelOrderDetail(long orderNo, String transferId, String rq, String sp) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderMapper.updateSportCancelOrderDetail(orderNo, transferId, rq, sp);
		} catch (Exception e) {
			Log.run.error("更新竞技彩订单主表出票信息发生异常,orderNo=" + orderNo, e);
			return returnValue;
		}
		return returnValue;
	}
}
