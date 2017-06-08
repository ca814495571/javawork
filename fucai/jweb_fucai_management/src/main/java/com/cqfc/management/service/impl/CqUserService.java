package com.cqfc.management.service.impl;

import org.springframework.stereotype.Service;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.service.ICqUserService;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.ticketissue.UserAccountInfo;

@Service
public class CqUserService implements ICqUserService{

	@Override
	public PcResultObj getCqUserAccountInfo(String paramType,String value) {

		PcResultObj pcResultObj = new PcResultObj(); 
		
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke("ticketIssue", "getUserAccountInfo", paramType, value);
		
		if(returnMessage.getObj() != null){			
			UserAccountInfo userAccountInfo = (UserAccountInfo) returnMessage.getObj();
			pcResultObj.setEntity(userAccountInfo);
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("查询成功");
			
		}else{			
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("查询失败");
		}
		return pcResultObj;
	}
}
