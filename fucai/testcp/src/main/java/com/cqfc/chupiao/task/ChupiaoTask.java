package com.cqfc.chupiao.task;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.chupiao.dao.ChupiaoDao;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.chupiao.Chupiao;
import com.cqfc.protocol.chupiao.ResultMessage;
import com.jami.common.ApplicationContextProvider;

public class ChupiaoTask implements Runnable {
	
	private String orderNo;
	public ChupiaoTask(String orderNo) {
		this.orderNo = orderNo;
	}	
	
	@Override
	public void run() {
		ResultMessage msg = new ResultMessage();
		int resultNum = 0;
		Chupiao chupiao=new Chupiao();
		chupiao.setOrderNo(orderNo);
		float a=(float) Math.random();
		if(a<0.1){
			resultNum = 0;
			msg.setStatusCode(0);
		    msg.setMsg("fail"); 
		    chupiao.setReasult("0");
		}
		if(0.1<a&&a<0.2){
			resultNum = 1;
			msg.setStatusCode(2);
		    msg.setMsg("chupiaozhong"); 
		    chupiao.setReasult("2");
		}
		if(a>0.2){
			resultNum = 1;
			msg.setStatusCode(1);
		    msg.setMsg("success"); 
		    chupiao.setReasult("1");
		}	
		
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		ChupiaoDao chupiaoDao = applicationContext.getBean("chupiaoDao", ChupiaoDao.class);
	    chupiaoDao.addChupiao(chupiao);
		TransactionProcessor.dynamicInvoke("ticketIssue", "notifyTicket", orderNo, resultNum);
	}
}
