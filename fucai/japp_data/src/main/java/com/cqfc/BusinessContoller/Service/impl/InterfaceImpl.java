package com.cqfc.BusinessContoller.Service.impl;

import java.util.concurrent.RejectedExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.BusinessContoller.protocol.BusinessContollerService;
import com.cqfc.BusinessContoller.protocol.Message;
import com.cqfc.BusinessContoller.Service.QueueService;


@Service("InterfaceImpl")
public class InterfaceImpl implements BusinessContollerService.Iface {

	@Autowired
    private QueueService queueService;
	
	 @Override
	    public int onMessage(Message msg) {
		 	System.out.println(msg.msg + ":receive");
		 	int ret  = 0;
		 	
		 	//消息处理
	        switch( msg.getTransCode())
	        {
	        case 101:
	        	break;
	        }
	        
	        
	        try {
	            QueueTask queueTask = new QueueTask(msg);
	            queueService.submit(queueTask);
	            ret = 0;
	        } catch (RejectedExecutionException e) {
	            ret = -1;
	        }
	        
	        return ret;
	    }
}
