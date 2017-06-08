package com.cqfc.convertor;

import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.protocol.ticketissue.OutTicketOrder;

public interface IConvertor {
	String convert4out(String ticket);
	String convert4out(OutTicketOrder ticket);
	String convert4In(String content);
	void sendTicket(OutTicketOrder ticket, FucaiPartnerInfo partnerInfo);
	String queryTicket(OutTicketOrder ticket, FucaiPartnerInfo partnerInfo);
	void checkTicket(OutTicketOrder ticket, FucaiPartnerInfo partnerInfo);
}
