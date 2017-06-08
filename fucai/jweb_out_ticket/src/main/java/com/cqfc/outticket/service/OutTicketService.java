package com.cqfc.outticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.outticket.dao.OutTicketDao;
import com.cqfc.outticket.model.OutTicket;
import com.cqfc.outticket.model.Statis;
import com.jami.util.Log;

@Service
public class OutTicketService {

	@Autowired
	private OutTicketDao outTicketDao;
	
	public int addOutTicket(OutTicket outTicket) {
		int flag = 0;
		try {
			flag = outTicketDao.addOutTicket(outTicket);
		} catch (Exception e) {
			Log.run.debug("out ticket,orderNo=%s,exception=%s", outTicket.getOrderNo(), e);
		}
		return flag;
	}

	public OutTicket findOrderStaByOrderNo(String orderNo) {
		return outTicketDao.findOrderStaByOrderNo(orderNo);
	}
	
	
	public Statis statistics103(String lotteryId, String issueNo, String tableName){
		return outTicketDao.statisOrder(lotteryId, issueNo, tableName);
	}

}
