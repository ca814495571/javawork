package com.cqfc.access.task;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.transactionmsg605.Querytype;
import com.jami.util.Log;

public class TicketIssueCallbackTask implements Runnable {
	private String msg;

	public TicketIssueCallbackTask(String msg) {
		super();
		this.msg = msg;
	}

	@Override
	public void run() {
		String orderNo = "";
		String statusCode = "";
		int resultNum = 0;
		try {
			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = TransactionMsgLoader605.xmlToMsg(msg);
			if (TicketIssueConstant.TRANSCODE605.equals(msg605.getHead().getTranscode())) {
				Querytype ticketresult = msg605.getBody().getTicketresult();
				orderNo = ticketresult.getId();
				statusCode = ticketresult.getStatuscode();
				if (statusCode.equals(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS)) {
					resultNum = TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS;
				} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_RRADE_FAIL)) {
					resultNum = TicketIssueConstant.SEND_TICKET_RESULT_FAIL;
				}
				Log.run.debug("ticketIssueCallBack business,orderNo=%s,resultCode=%s,result=%d", orderNo, statusCode,
						resultNum);
				if (resultNum > 0) {
					String gameid = ticketresult.getGameid();
					int lotteryType = OrderUtil.getLotteryCategory(gameid);
					if (lotteryType == OrderStatus.LotteryType.NUMBER_GAME.getType()) {
						TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "isTicketSuccess",
								orderNo, resultNum);
					} else if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
						SportPrint success = OrderUtil.convertMsg2result(msg605, resultNum);					
						TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "sportOrderAfterPrint",
								success);
					} else {
						Log.fucaibiz.error("105回调彩种错误,既不属于数字彩,也不属于竞技彩,lotteryId=%s", gameid);
					}
				}
			}
		} catch (Exception e) {
			Log.run.error("ticketIssueCallBack(exception), msg: %s, exception: %s", msg, e);
		}
	}
}
