package com.cqfc.businesscontroller.task;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.xmlparser.TransactionMsgLoader133;
import com.cqfc.xmlparser.TransactionMsgLoader733;
import com.cqfc.xmlparser.transactionmsg733.Body;
import com.jami.util.Log;

public class TransactionMsgProcessor133 {

	private static String returnCode = "733";

	/**
	 * 用户帐户兑奖密码修改接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理133消息体");
		
		TransResponse response = new TransResponse();

		String xml133 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg133.Msg msg133 = TransactionMsgLoader133
				.xmlToMsg(xml133);

		com.cqfc.xmlparser.transactionmsg133.Body body133 = msg133.getBody();
		com.cqfc.xmlparser.transactionmsg133.Headtype head133 = msg133
				.getHead();
		com.cqfc.xmlparser.transactionmsg133.Querytype querytype133 = body133
				.getPrizePasswd();

		String partnerId = head133.getPartnerid();
		String version = head133.getVersion();

		String userId = querytype133.getUserid();

		String oldPassword = querytype133.getOldpasswd();
		String newPassword = querytype133.getNewpasswd();

		com.cqfc.xmlparser.transactionmsg733.Msg msg733 = new com.cqfc.xmlparser.transactionmsg733.Msg();
		com.cqfc.xmlparser.transactionmsg733.Body body733 = new Body();
		com.cqfc.xmlparser.transactionmsg733.Headtype head733 = new com.cqfc.xmlparser.transactionmsg733.Headtype();

		head733.setTime(DateUtil.getCurrentDateTime());
		head733.setPartnerid(partnerId);
		head733.setTranscode(returnCode);
		head733.setVersion(version);

		long userIdTemp = 0;
		
		try {
			
			userIdTemp = Long.parseLong(userId);
		} catch (Exception e) {
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			response.setData("非法数字");
			Log.fucaibiz.error("数据格式不正确"+userId,e);
			return response;
		}
		
		if(!CheckUser.validateUser(userIdTemp, partnerId)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			Log.fucaibiz.error(partnerId+"合作商没有该用户"+userIdTemp);
			return response;
		}
		
		com.cqfc.xmlparser.transactionmsg733.Querytype querytype733 = new com.cqfc.xmlparser.transactionmsg733.Querytype();

		Log.fucaibiz.info("调用userAccount中的updatePrizePassword方法,参数：userIdTemp="+userIdTemp+",oldPassword="+oldPassword+",newPassword="+newPassword);
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("userAccount", "updatePrizePassword", userIdTemp,
						oldPassword, newPassword);

		String statusCode = reMsg.getStatusCode();

		Log.fucaibiz.info("返回的状态码："+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			int flag = (Integer) reMsg.getObj();

			if (flag == 1) {
				querytype733.setStatus("2");
				querytype733.setMsg("修改成功");

			} else if (flag == -2) {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
				response.setData("用户id不存在");
				Log.fucaibiz.info("用户id不存在");
				return response;
				
			} else {
				
				querytype733.setStatus("3");
				querytype733.setMsg("修改失败,请检查原密码是否正确");
				Log.fucaibiz.info("修改失败,请检查原密码是否正确");
			}

			querytype733.setUserid(userId);
			body733.setPrizepasswd(querytype733);
			msg733.setBody(body733);
			msg733.setHead(head733);
			response.setData(TransactionMsgLoader733.msgToXml(msg733));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		
		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("错误码的消息体："+reMsg.getMsg());
			return response;
		}

		Log.fucaibiz.info("733消息生成成功");
		return response;
	}

}
