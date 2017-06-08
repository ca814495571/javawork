package com.cqfc.businesscontroller.task;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IdWorker;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader116;
import com.cqfc.xmlparser.TransactionMsgLoader716;
import com.cqfc.xmlparser.transactionmsg716.Body;
import com.jami.util.Log;

public class TransactionMsgProcessor116 {

	private static String returnCode = "716";

	/**
	 * 提现接口申请
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		
		Log.fucaibiz.info("处理116消息");
		
		TransResponse response = new TransResponse();

		String xml116 = message.getTransMsg();
		
		com.cqfc.xmlparser.transactionmsg116.Msg msg116 = TransactionMsgLoader116.xmlToMsg(xml116); 
		
		com.cqfc.xmlparser.transactionmsg116.Body body116 = msg116.getBody();
		com.cqfc.xmlparser.transactionmsg116.Headtype head116 = msg116.getHead();
		com.cqfc.xmlparser.transactionmsg116.Querytype querytype116 = body116.getWithdraw();
		
		String headPartnerId = head116.getPartnerid();
		String version = head116.getVersion();
		
		String branch = querytype116.getBranch();
		String bankName = querytype116.getBankname();
		String cardNo = querytype116.getCardno();
		String cardUserName = querytype116.getCardusername();
		String id = querytype116.getId();
		String money = querytype116.getMoney();
		String userId = querytype116.getUserid();
		
		
		com.cqfc.xmlparser.transactionmsg716.Msg msg716= new com.cqfc.xmlparser.transactionmsg716.Msg();
		com.cqfc.xmlparser.transactionmsg716.Body body716= new Body();
		com.cqfc.xmlparser.transactionmsg716.Headtype head716= new com.cqfc.xmlparser.transactionmsg716.Headtype();
		
		head716.setTime(DateUtil.getCurrentDateTime());
		head716.setPartnerid(headPartnerId);
		head716.setTranscode(returnCode);
		head716.setVersion(version);
		
		com.cqfc.xmlparser.transactionmsg716.Querytype querytype716= new com.cqfc.xmlparser.transactionmsg716.Querytype();
		
		WithdrawApply withdrawApply = new WithdrawApply();
		WithdrawAccount withdrawAccount = new WithdrawAccount(); 
		
		withdrawAccount.setRealName(cardUserName);
		withdrawAccount.setBankName(bankName);
		withdrawAccount.setAccountAddress(branch);
		withdrawAccount.setAccountNo(cardNo);
		
		withdrawApply.setRealName(cardUserName);
		withdrawApply.setPartnerId(headPartnerId);
		withdrawApply.setPartnerApplyId(id);
		String platformid ="";
		long userIdTemp = 0;
		try {
			withdrawApply.setWithdrawAmount(MoneyUtil.toCent(Double.parseDouble(money)));
			platformid = IdWorker.getSerialNumber();
			userIdTemp = Long.parseLong(userId);
			
		} catch (Exception e) {
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			response.setData("非法数字");
			Log.fucaibiz.error("流水号生成失败或者提现金额、用户id格式有错",e);
			return response;
		}
		
		if(!CheckUser.validateUser(userIdTemp, headPartnerId)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			Log.fucaibiz.error(headPartnerId+"合作商没有该用户"+userIdTemp);
			return response;
			
		}
		
		
		withdrawAccount.setUserId(userIdTemp);
		withdrawApply.setUserId(userIdTemp);
		withdrawApply.setSerialNumber(platformid);
		withdrawApply.setWithdrawAccount(withdrawAccount);
		//i32 createWithdrawApply(1:WithdrawApply withdrawApply);
		
		Log.fucaibiz.info("调用userAccount中的createWithdrawApply方法,参数withdrawApply="+withdrawApply);
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor.dynamicInvoke("userAccount", 
				"createWithdrawApply",withdrawApply);
		
		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码"+statusCode);
		if(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)&&reMsg.getObj()!=null){
			
			int flag  = (Integer) reMsg.getObj();
			
			if(flag ==1){
				
				querytype716.setUserid(userId);
				querytype716.setId(id);
				querytype716.setMoney(money);
				querytype716.setPlatformid(platformid);
				
				
			}else{
				
				response.setStatusCode(ConstantsUtil.STATUS_CODE_RRADE_FAIL);
				response.setData("提现申请失败,请检查id是否重复或者该账户余额不足");
				Log.fucaibiz.error("提现申请失败");
				return response;
				
			}
			
			body716.setWithdraw(querytype716);
			msg716.setBody(body716);
			msg716.setHead(head716);
			response.setData(TransactionMsgLoader716.msgToXml(msg716));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		}else{
			
			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("错误消息"+reMsg.getMsg());
			return response;
		}
		
		Log.fucaibiz.info("716消息生成成功");
		return response;
	}
	
	

}
