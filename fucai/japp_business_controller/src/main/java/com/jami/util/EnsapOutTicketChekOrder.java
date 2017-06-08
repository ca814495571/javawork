package com.jami.util;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.protocol.ticketissue.OutTicketOrder;

/**
 * @author liwh
 */
public class EnsapOutTicketChekOrder {

	/**
	 * order转为OutTicketOrder
	 * 
	 * @param order
	 * @return
	 */
	public static OutTicketOrder ensapOutTicketOrder(Order order) {
		OutTicketOrder outTicketOrder = new OutTicketOrder();
		try {
			outTicketOrder.setIssueNo(order.getIssueNo());
			outTicketOrder.setLotteryId(order.getLotteryId());
			outTicketOrder.setMultiple(order.getMultiple());
			outTicketOrder.setOrderContent(order.getOrderContent());
			outTicketOrder.setOrderNo(String.valueOf(order.getOrderNo()));
			outTicketOrder.setPlayType(order.getPlayType());
			outTicketOrder.setTotalMoney(order.getTotalAmount());
			outTicketOrder.setPartnerId(order.getPartnerId());
		} catch (Exception e) {
			ScanLog.scan.error("order转为OutTicketOrder发生异常,orderNo=" + order.getOrderNo(), e);
		}
		return outTicketOrder;
	}

	/**
	 * sportOrder转为OutTicketOrder
	 * 
	 * @param order
	 * @return
	 */
	public static OutTicketOrder ensapSportOutTicketOrder(SportOrder order) {
		OutTicketOrder outTicketOrder = new OutTicketOrder();
		try {
			outTicketOrder.setIssueNo(order.getIssueNo());
			outTicketOrder.setLotteryId(order.getLotteryId());
			outTicketOrder.setMultiple(order.getMultiple());
			outTicketOrder.setOrderContent(order.getOrderContent());
			outTicketOrder.setOrderNo(String.valueOf(order.getOrderNo()));
			outTicketOrder.setPlayType(order.getPlayType());
			outTicketOrder.setTotalMoney(order.getTotalAmount());
			outTicketOrder.setPartnerId(order.getPartnerId());
			outTicketOrder.setBatchId("");
			outTicketOrder.setWareId("");
		} catch (Exception e) {
			ScanLog.scan.error("竞技彩SportOrder转为OutTicketOrder发生异常,orderNo=" + order.getOrderNo(), e);
		}
		return outTicketOrder;
	}

}
