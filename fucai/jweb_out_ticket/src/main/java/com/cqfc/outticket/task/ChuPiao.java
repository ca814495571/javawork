package com.cqfc.outticket.task;

import java.util.List;
import java.util.Random;

import org.springframework.context.ApplicationContext;

import com.cqfc.outticket.model.OutTicket;
import com.cqfc.outticket.service.OutTicketService;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.LotteryUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.xmlparser.TransactionMsgLoader104;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.transactionmsg104.Ticket;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

public class ChuPiao implements Runnable {
	private String msg;

	public ChuPiao(String msg) {
		this.msg = msg;
	}

	@Override
	public void run() {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		OutTicketService service = applicationContext.getBean("outTicketService", OutTicketService.class);
		OutTicket outTicket = new OutTicket();
		int resultNum = 2;
		outTicket.setResultNum(resultNum);
		String orderNo = null;
		
		try{
			com.cqfc.xmlparser.transactionmsg104.Msg reqMsg = TransactionMsgLoader104.xmlToMsg(msg);
			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = new com.cqfc.xmlparser.transactionmsg605.Msg();
			com.cqfc.xmlparser.transactionmsg605.Headtype headtype = new com.cqfc.xmlparser.transactionmsg605.Headtype();
			headtype.setTranscode("605");
			headtype.setPartnerid(reqMsg.getHead().getPartnerid());
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg605.setHead(headtype);
			com.cqfc.xmlparser.transactionmsg605.Body reqBody = new com.cqfc.xmlparser.transactionmsg605.Body();
			com.cqfc.xmlparser.transactionmsg605.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg605.Querytype();
			List<Ticket> list = reqMsg.getBody().getTicketorder().getTickets().getTicket();
			String lotteryId = reqMsg.getBody().getTicketorder().getGameid();
	
			int flag = 0;
			for (Ticket ticket : list) {
				orderNo = ticket.getId();
				outTicket.setOrderNo(orderNo);
				outTicket.setLotteryId(lotteryId);
				outTicket.setIssueNo(LotteryUtil.convertIssueNo2System(lotteryId, ticket.getIssue()));
				outTicket.setOrderMoney(Long.valueOf(ticket.getMoney()) * 100);
				outTicket.setMutiple(Integer.valueOf(ticket.getMultiple()));
				outTicket.setOrderContentOdds(ticket.getBall());
				outTicket.setPlayType(ticket.getPlaytype());
				flag = service.addOutTicket(outTicket);
				if (flag == 1) {
					ticketResult.setPalmid(orderNo);
					ticketResult.setGameid(reqMsg.getBody().getTicketorder().getGameid());
					ticketResult.setMultiple(ticket.getMultiple());
					ticketResult.setIssue(ticket.getIssue());
					ticketResult.setPlaytype(ticket.getPlaytype());
					ticketResult.setMoney(ticket.getMoney());
					ticketResult.setOrgserial(orderNo);
					if (OrderUtil.getJcCategoryDetail(lotteryId)  == OrderStatus.LotteryType.JJZC_GAME.getType() 
					   || OrderUtil.getJcCategoryDetail(lotteryId)  == OrderStatus.LotteryType.JJLC_GAME.getType()) {
						ticketResult.setOdds(ticket.getBall());						
					}
					
					if (OrderUtil.getLotteryCategory(lotteryId) == OrderStatus.LotteryType.NUMBER_GAME.getType()){
						Random random = new Random();
						int value = random.nextInt(10);
						if (value < 2) {
							ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_RRADE_FAIL);
							ticketResult.setMsg("交易失败");
						} else {
							ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);
							ticketResult.setMsg("交易成功");
						}
					} else {
						ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);
						ticketResult.setMsg("交易成功");						
					}				
	
					ticketResult.setId(ticket.getId());
					reqBody.setTicketresult(ticketResult);
					msg605.setBody(reqBody);
					String resultXmlStr = TransactionMsgLoader605.msgToXml(msg605);
					Transcode605.ticketCallBack(resultXmlStr);
				} else {
					Log.run.error("", "插入数据库发生异常");
				}
			}
		}
		catch (Exception e) {
			Log.run.error("ChuPiao exception=%s", e);
		}
		
	}
}
