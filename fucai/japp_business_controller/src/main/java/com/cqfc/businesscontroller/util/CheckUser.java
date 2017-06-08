package com.cqfc.businesscontroller.util;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.util.ConstantsUtil;

public class CheckUser {

	
	/**
	 * 3类型用户校验用户是否属于该合作商的
	 * @param userId
	 * @return
	 */
	public static  boolean  validateUser(long userId,String partnerId){
		
		boolean flag = false;
		
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_USER_ACCOUNT, "findUserInfoById", userId);
		
		if(returnMessage.getObj()!=null){
			
			UserInfo user = (UserInfo) returnMessage.getObj();
			
			if(user.getPartnerId()!=null && partnerId.equals(user.getPartnerId()) ){
				
				flag = true;
			}
			
		}
		
		return flag;
	}
	
}
