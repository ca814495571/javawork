package com.cqfc.ticketissue.task;

import com.cqfc.convertor.IConvertor;
import com.cqfc.convertor.ILotteryFactory;
import com.cqfc.convertor.LotteryConvertFactory;
import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.protocol.ticketissue.OutTicketOrder;
import com.cqfc.util.FucaiPartnerInfoUtil;

public class QueryTicketTask {

	public static String queryTicketChuPiao(OutTicketOrder outTicketOrder) {
		FucaiPartnerInfo partnerInfo = FucaiPartnerInfoUtil
				.selectFucaiPartnerInfo(outTicketOrder.getPartnerId());
		String result = "";
		ILotteryFactory lotteryFactory = LotteryConvertFactory
				.getLotteryFactory(partnerInfo.getPartnerType());
		IConvertor convertor = lotteryFactory.getConvertor(outTicketOrder
				.getLotteryId());
		result = convertor.queryTicket(outTicketOrder, partnerInfo);

		return result;
	}
}
