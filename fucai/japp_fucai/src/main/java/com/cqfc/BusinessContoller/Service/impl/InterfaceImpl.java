package com.cqfc.BusinessContoller.Service.impl;

import org.springframework.stereotype.Service;

import com.cqfc.BusinessContoller.protocol.BusinessContollerService;
import com.cqfc.BusinessContoller.protocol.Message;


@Service("InterfaceImpl")
public class InterfaceImpl implements BusinessContollerService.Iface {

	 @Override
	    public int onMessage(Message msg) {
		 	System.out.println(msg.msg);
	        return 0;
	    }
}
