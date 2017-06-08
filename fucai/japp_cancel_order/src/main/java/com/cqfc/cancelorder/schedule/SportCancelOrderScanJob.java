package com.cqfc.cancelorder.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.cancelorder.service.ICancelOrderService;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.ticketissue.OutTicketOrder;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.util.CancelOrderConstant;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.SwitchUtil;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.DbGenerator;
import com.jami.util.ScanLog;

/**
 * 竞技彩转移订单查询出票情况
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class SportCancelOrderScanJob {

	@Resource
	private ICancelOrderService cancelOrderService;

	@Scheduled(cron = "0 0/10 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------查询撤单订单出票情况操作开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			DbGenerator.setCancelDynamicDataSource(DbGenerator.SLAVE);
			while (true) {
				List<SportCancelOrder> cancelOrderList = cancelOrderService.getSportCancelOrderListForScan(
						CancelOrderConstant.OutTicketStatus.IN_TICKET.getValue(), currentPage, pageSize);
				if (cancelOrderList != null && cancelOrderList.size() > 0) {
					DbGenerator.setCancelDynamicDataSource(DbGenerator.MASTER);
					for (SportCancelOrder cancelOrder : cancelOrderList) {
						long orderNo = cancelOrder.getOrderNo();
						ScanLog.scan.debug("竞技彩查询转移订单出票情况,orderNo=%d", orderNo);
						ReturnMessage checkTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue", "queryTicket",
								ensapOutTicketOrder(cancelOrder));
						if (!checkTicketMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
							ScanLog.scan.error("竞技彩查询转移订单出票情况发生异常,orderNo=%d,errorMsg=%s", orderNo,
									checkTicketMsg.getMsg());
						} else {
							ResultMessage sendTicket = (ResultMessage) checkTicketMsg.getObj();
							int checkTicket = sendTicket.getStatusCode();
							if (checkTicket != TicketIssueConstant.SEND_TICKET_RESULT_PROCESSING) {
								int outTicketStatus = checkTicket == TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS ? CancelOrderConstant.OutTicketStatus.HASTICKET_WAITLOTTERY
										.getValue() : CancelOrderConstant.OutTicketStatus.DRAWER_FAILURE.getValue();
								int ticketingIsSuccess = cancelOrderService.updateSportCancelOrder(outTicketStatus,
										sendTicket.getSportPrint());
								ScanLog.scan.debug("竞技彩转移订单出票状态更新为'"
										+ CancelOrderConstant.OutTicketStatus.getEnum(outTicketStatus).getText()
										+ "',orderNo=" + orderNo + ",returnValue=" + ticketingIsSuccess);
							}
						}
					}
					DbGenerator.setCancelDynamicDataSource(DbGenerator.SLAVE);
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("查询撤单订单出票情况发生异常", e);
		}
		ScanLog.scan.info("--------查询撤单订单出票情况操作结束------------");
	}

	private OutTicketOrder ensapOutTicketOrder(SportCancelOrder cancelOrder) {
		OutTicketOrder outTicketOrder = new OutTicketOrder();
		try {
			outTicketOrder.setIssueNo(cancelOrder.getIssueNo());
			outTicketOrder.setLotteryId(cancelOrder.getLotteryId());
			outTicketOrder.setMultiple(cancelOrder.getMultiple());
			outTicketOrder.setOrderContent(cancelOrder.getOrderContent());
			outTicketOrder.setOrderNo(String.valueOf(cancelOrder.getOrderNo()));
			outTicketOrder.setPlayType(cancelOrder.getPlayType());
			outTicketOrder.setTotalMoney(cancelOrder.getTotalAmount());
			outTicketOrder.setPartnerId(cancelOrder.getPartnerId());
			outTicketOrder.setBatchId("");
			outTicketOrder.setWareId("");
		} catch (Exception e) {
			ScanLog.scan.error("创建撤单订单封装对象发生异常,orderNo=" + cancelOrder.getOrderNo(), e);
		}
		return outTicketOrder;
	}
}
