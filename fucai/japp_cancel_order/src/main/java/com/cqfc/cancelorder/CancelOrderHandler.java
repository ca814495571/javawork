package com.cqfc.cancelorder;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.cancelorder.service.ICancelOrderService;
import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.CancelOrderService;
import com.cqfc.protocol.cancelorder.ReturnData;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.jami.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class CancelOrderHandler implements CancelOrderService.Iface {

	@Resource
	private ICancelOrderService cancelOrderService;

	/**
	 * 创建撤单订单
	 * 
	 * @param cancelOrder
	 * @return
	 * @throws TException
	 */
	@Override
	public int createCancelOrder(CancelOrder cancelOrder) throws TException {
		int returnValue = 0;
		try {
			DbGenerator.setCancelDynamicDataSource(DbGenerator.MASTER);
			returnValue = cancelOrderService.createCancelOrder(cancelOrder);
			Log.run.debug("create cancel order,orderNo=%s,returnValue=%d", cancelOrder.getOrderNo(), returnValue);
		} catch (Exception e) {
			Log.run.error("创建撤单订单发生异常,orderNo=" + cancelOrder.getOrderNo(), e);
			return 0;
		}
		return returnValue;
	}

	@Override
	public long getSuccessTicketCancelOrder(String lotteryId, String issueNo) throws TException {
		DbGenerator.setCancelDynamicDataSource(DbGenerator.SLAVE);
		return cancelOrderService.getSuccessTicketCancelOrder(lotteryId, issueNo);
	}

	@Override
	public ReturnData getCancelOrder(CancelOrder cancelOrder, int currentPage, int pageSize) throws TException {

		DbGenerator.setCancelDynamicDataSource(DbGenerator.SLAVE);
		ReturnData returnData = null;
		try {
			returnData = cancelOrderService.getCancelOrder(cancelOrder, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("查询迁移单发生异常 ，lotteryId= " + cancelOrder.getLotteryId(), e);
		}
		return returnData;
	}

	/**
	 * 创建竞技彩转移订单
	 * 
	 * @param sportCancelOrder
	 * @return
	 * @throws TException
	 */
	@Override
	public int createSportCancelOrder(SportCancelOrder sportCancelOrder) throws TException {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			DbGenerator.setCancelDynamicDataSource(DbGenerator.MASTER);
			returnValue = cancelOrderService.createSportCancelOrder(sportCancelOrder);
			Log.run.debug("create sport cancel order,orderNo=%d,returnValue=%d", sportCancelOrder.getOrderNo(),
					returnValue);
		} catch (Exception e) {
			Log.run.error("创建竞技彩转移订单发生异常,orderNo=" + sportCancelOrder.getOrderNo(), e);
			return 0;
		}
		return returnValue;
	}
}
