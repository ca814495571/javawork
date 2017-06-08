package com.cqfc.ticketissue.task;

import com.cqfc.convertor.IConvertor;
import com.cqfc.convertor.ILotteryFactory;
import com.cqfc.convertor.LotteryConvertFactory;
import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.protocol.ticketissue.OutTicketOrder;
import com.cqfc.ticketissue.util.TicketUtil;
import com.cqfc.util.FucaiPartnerInfoUtil;

public class SendTicketTask implements Runnable {
	private OutTicketOrder outTicketOrder;

	public SendTicketTask(OutTicketOrder outTicketOrder) {
		this.outTicketOrder = outTicketOrder;
	}

	@Override
	public void run() {
		FucaiPartnerInfo partnerInfo = FucaiPartnerInfoUtil.selectFucaiPartnerInfo(outTicketOrder.getPartnerId());	
		try {
			ILotteryFactory lotteryFactory = LotteryConvertFactory.getLotteryFactory(partnerInfo.getPartnerType());
			IConvertor convertor = lotteryFactory.getConvertor(outTicketOrder.getLotteryId());
			convertor.sendTicket(outTicketOrder, partnerInfo);
		} finally {
			TicketUtil.sendTicketMap.remove(outTicketOrder.getOrderNo());
		}
	}
}
