package com.cqfc.businesscontroller.task;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.xmlparser.TransactionMsgLoader118;
import com.cqfc.xmlparser.TransactionMsgLoader718;
import com.cqfc.xmlparser.transactionmsg118.Msg;
import com.cqfc.xmlparser.transactionmsg118.Querytype;
import com.cqfc.xmlparser.transactionmsg718.Body;
import com.cqfc.xmlparser.transactionmsg718.Headtype;
import com.jami.util.Log;

public class TransactionMsgProcessor118 {

	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String responseTransCode = "718";
		String xmlstr = message.getTransMsg();
		Msg msg118 = TransactionMsgLoader118.xmlToMsg(xmlstr);
		com.cqfc.xmlparser.transactionmsg718.Msg msg718 = new com.cqfc.xmlparser.transactionmsg718.Msg();		
		Querytype bindUser = msg118.getBody().getBinduser();
		String puserkey = bindUser.getPuserkey();
		String realName = bindUser.getRealname();
		String phone = bindUser.getPhone();
		String cardNo = bindUser.getIdcardno();
		if(realName == null || "".equals(realName)){
			Log.fucaibiz.error("TransactionMsgProcessor118: 用户的真实姓名不能为空, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		if(puserkey == null || "".equals(puserkey)){
			Log.fucaibiz.error("TransactionMsgProcessor118: 用户在合作商平台的唯一身份标识不能为空, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		if(phone == null || "".equals(phone)){
			Log.fucaibiz.error("TransactionMsgProcessor118: 电话号码不能为空, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		if(cardNo == null || "".equals(cardNo)){
			Log.fucaibiz.error("TransactionMsgProcessor118: 身份证号码不能为空, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		if(phone.length() > 16 || cardNo.length() > 20){
			Log.fucaibiz.error("TransactionMsgProcessor118: 电话号码或身份证过长, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		
		UserInfo userInfo = new UserInfo();
		userInfo.setPartnerId(message.getPartnerId());
		userInfo.setUserName(realName);
		userInfo.setCardType(1);
		userInfo.setCardNo(cardNo);
		userInfo.setPartnerUserId(puserkey);
		userInfo.setMobile(phone);

	
		long userId;
		/**
		 * dynamicInvoke参数约定
		 * 1.第一个参数为模块名,使用驼峰命名规则（第一个单词第一个字母小写，后面单词首字母大写），比如businessControll,lotteryIssue,partnerAccount等
		 * 2.第二个参数为方法名,为调用模块中的方法名称。比如getUserInfoById,getUserInfoList等
		 * 3.其余参数为需要调用方法的参数，比如调用getUserInfoById需要传入userId,调用getUserInfoList需要传入userInfo,currentPage,pageSize
		 */
		ReturnMessage retMsg;		
		retMsg = TransactionProcessor.dynamicInvoke("partner", "verifyCanBindUser", message.getPartnerId());
		if(retMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)){
			if(!(Boolean) retMsg.getObj()){
				Log.fucaibiz.error("TransactionMsgProcessor118: 不支持的接入商类型,只有partnerId类型为2或者3，才能创建用户, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTSUPPORT_PORTAL);
				response.setData("不支持的接入商类型");
				return response;
			}
		}
		else{
			Log.fucaibiz.error("TransactionMsgProcessor118: %s", retMsg.getMsg());
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		retMsg = TransactionProcessor.dynamicInvoke("userAccount", "createUserInfo", userInfo);
		if(retMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)){
			userId = (Long) retMsg.getObj();
			if(userId == ServiceStatusCodeUtil.STATUS_CODE_USER_ISEXIST){
				Log.fucaibiz.error("TransactionMsgProcessor118: 用户已存在, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_REPEAT_USER);
				response.setData("重复的用户");
				return response;
			}
			else if(userId == ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR){
				Log.fucaibiz.error("TransactionMsgProcessor118: 操作数据库失败, msg=%s", xmlstr);
				Log.error("TransactionMsgProcessor118: 操作数据库失败, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_DB_ERROR);
				response.setData("操作数据库失败");
				return response;
			}
			else if(userId <= 0){
				Log.fucaibiz.error("TransactionMsgProcessor118: 系统错误, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
				response.setData("系统错误");
				return response;
			}
		}
		else{
			Log.fucaibiz.error("TransactionMsgProcessor118: %s", retMsg.getMsg());
			Log.error("TransactionMsgProcessor118: %s", retMsg.getMsg());
			response.setStatusCode(retMsg.getStatusCode());
			response.setData(retMsg.getMsg());
			return response;
		}
		// 拼装718交易对象，生成xml，返回
		Headtype headtype = new Headtype();
		headtype.setPartnerid(msg118.getHead().getPartnerid());
		headtype.setTranscode(responseTransCode);
		headtype.setVersion("1.0");
		headtype.setTime(DateUtil.getCurrentDateTime());
		msg718.setHead(headtype);
		Body body = new Body();
		com.cqfc.xmlparser.transactionmsg718.Querytype querytype718 = new com.cqfc.xmlparser.transactionmsg718.Querytype();
		querytype718.setUserid(String.valueOf(userId));
		body.setBindinfo(querytype718);
		msg718.setBody(body);

		// 校验拼装的对象
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setData(TransactionMsgLoader718.msgToXml(msg718));
		response.setResponseTransCode(responseTransCode);
		Log.fucaibiz.debug("TransactionMsgProcessor118: success");
		return response;
	}
}
