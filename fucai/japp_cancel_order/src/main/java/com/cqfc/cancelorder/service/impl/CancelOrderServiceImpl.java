package com.cqfc.cancelorder.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.cancelorder.dao.CancelOrderDao;
import com.cqfc.cancelorder.service.ICancelOrderService;
import com.cqfc.protocol.businesscontroller.PrintMatch;
import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.ReturnData;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.cancelorder.SportCancelOrderDetail;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
@Service
public class CancelOrderServiceImpl implements ICancelOrderService {

	@Resource
	private CancelOrderDao cancelOrderDao;

	@Override
	public int createCancelOrder(CancelOrder cancelOrder) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = cancelOrderDao.createCancelOrder(cancelOrder);
			Log.run.debug("创建撤单订单,orderNo=%s,returnValue=%d", cancelOrder.getOrderNo(), returnValue);
		} catch (Exception e) {
			Log.run.error("创建撤单订单发生异常,orderNo=" + cancelOrder.getOrderNo(), e);
		}
		return returnValue;
	}

	@Override
	public List<CancelOrder> getCancelOrderListForScan(int outTicketStatus, int currentPage, int pageSize) {
		List<CancelOrder> cancelOrderList = null;
		try {
			cancelOrderList = cancelOrderDao.getCancelOrderListForScan(outTicketStatus, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("获取撤单订单列表发生异常,outTicketStatus=" + outTicketStatus, e);
			return null;
		}
		return cancelOrderList;
	}

	@Override
	public int updateOutTicketStatus(String orderNo, int outTicketStatus) {
		int returnValue = 0;
		try {
			returnValue = cancelOrderDao.updateOutTicketStatus(orderNo, outTicketStatus);
			ScanLog.run.debug("更新撤单订单出票状态,orderNo=%s,outTicketStatus=%d,returnValue=%d", orderNo, outTicketStatus,
					returnValue);
		} catch (Exception e) {
			Log.run.error("更新撤单订单出票状态发生异常,orderNo=" + orderNo + ",outTicketStatus=" + outTicketStatus, e);
			return 0;
		}
		return returnValue;
	}

	@Override
	public long getSuccessTicketCancelOrder(String lotteryId, String issueNo) {
		return cancelOrderDao.getSuccessTicketCancelOrder(lotteryId, issueNo);
	}

	@Override
	public ReturnData getCancelOrder(CancelOrder cancelOrder, int currentPage, int pageSize) {
		ReturnData returnData = null;
		try {
			returnData = cancelOrderDao.getCancelOrder(cancelOrder.getUserId(), cancelOrder.getOrderNo(),
					cancelOrder.getPartnerId(), cancelOrder.getLotteryId(), cancelOrder.getIssueNo(), currentPage,
					pageSize);
		} catch (Exception e) {
			Log.run.error("获取转移单列表发生异常 ，lotteryId =" + cancelOrder.getLotteryId(), e);
		}
		return returnData;
	}

	@Override
	@Transactional
	public int createSportCancelOrder(SportCancelOrder sportCancelOrder) {
		// TODO Auto-generated method stub
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = cancelOrderDao.createSportCancelOrder(sportCancelOrder);
			for (SportCancelOrderDetail detail : sportCancelOrder.getDetailList()) {
				int detailValue = cancelOrderDao.createSportCancelOrderDetail(detail);
				if (detailValue <= 0) {
					throw new Exception("创建竞技彩转移订单细表发生异常");
				}
			}
			Log.run.debug("创建竞技彩转移订单,orderNo=%d,returnValue=%d", sportCancelOrder.getOrderNo(), returnValue);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("创建竞技彩转移订单发生异常,orderNo=" + sportCancelOrder.getOrderNo(), e);
		}
		return returnValue;
	}

	@Override
	public List<SportCancelOrder> getSportCancelOrderListForScan(int outTicketStatus, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		List<SportCancelOrder> cancelOrderList = null;
		try {
			cancelOrderList = cancelOrderDao.getSportCancelOrderListForScan(outTicketStatus, currentPage, pageSize);
			for (SportCancelOrder order : cancelOrderList) {
				order.setDetailList(cancelOrderDao.getSportOrderDetail(order.getOrderNo()));
			}
		} catch (Exception e) {
			Log.run.error("获取竞技彩转移订单list发生异常,outTicketStatus=" + outTicketStatus, e);
		}
		return cancelOrderList;
	}

	@Override
	@Transactional
	public int updateSportCancelOrder(int outTicketStatus, SportPrint sportPrint) {
		// TODO Auto-generated method stub
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			long orderNo = sportPrint.getOrderNo();
			String ticketNo = sportPrint.getTicketNo();
			String printTime = sportPrint.getPrintTime();
			returnValue = cancelOrderDao.updateSportCancelOrderMain(orderNo, outTicketStatus, ticketNo, printTime);

			List<PrintMatch> matchList = sportPrint.getMatchList();
			for (PrintMatch printMatch : matchList) {
				String transferId = printMatch.getTransferId();
				String sp = printMatch.getSp();
				String rq = printMatch.getRq();
				int detailValue = cancelOrderDao.updateSportCancelOrderDetail(orderNo, transferId, rq, sp);
				if (detailValue <= 0) {
					throw new Exception("更新竞技彩转移订单细表出票信息发生异常");
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("更新竞技彩转移订单出票状态发生异常", e);
			return 0;
		}
		return returnValue;
	}
}
