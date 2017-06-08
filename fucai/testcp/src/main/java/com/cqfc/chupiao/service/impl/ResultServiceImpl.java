package com.cqfc.chupiao.service.impl;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.chupiao.dao.ChupiaoDao;
import com.cqfc.chupiao.task.ChupiaoTask;
import com.cqfc.protocol.chupiao.ChupiaoService;
import com.cqfc.protocol.chupiao.ResultMessage;
import com.jami.util.Log;

@Service
public class ResultServiceImpl  implements ChupiaoService.Iface{
	@Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@Autowired
	private ChupiaoDao chupiaoDao;
	
	@Override
	public ResultMessage orderTicket(String content, String orderNo)
			throws TException {
		ResultMessage message = new ResultMessage();
	    threadPoolTaskExecutor.submit(new ChupiaoTask(orderNo));    
	    
	   //1 接受成功，0 接受失败 
	    message.setStatusCode(1);
	    message.setMsg("success");
		return message;
	}

	@Override
	public ResultMessage orderTicketResult(String orderNo) throws TException {
		// TODO Auto-generated method stub
		Log.run.info("orderTicketReasult(orderNo): %s", orderNo);
		ResultMessage msg = new ResultMessage();
		int count = chupiaoDao.findChupiaoByOrderNo(orderNo);

		if(count == 1){
			msg.setStatusCode(1);
		    msg.setMsg("success");   
		}
		else{
			msg.setStatusCode(0);
		    msg.setMsg("fail");   
		}
		
		Log.run.info("orderTicketReasult(resultCallback): success");
		return msg;
	}

}
