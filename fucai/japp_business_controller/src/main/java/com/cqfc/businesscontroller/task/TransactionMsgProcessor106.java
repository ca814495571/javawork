package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserInfoData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader106;
import com.cqfc.xmlparser.TransactionMsgLoader706;
import com.cqfc.xmlparser.transactionmsg106.Msg;
import com.cqfc.xmlparser.transactionmsg706.Account;
import com.cqfc.xmlparser.transactionmsg706.Body;
import com.cqfc.xmlparser.transactionmsg706.Headtype;
import com.cqfc.xmlparser.transactionmsg706.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor106 {

	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String responseTransCode = "706";
		//请求数据
		String xmlstr = message.getTransMsg();
		//请求消息对象
		Msg msg106 = TransactionMsgLoader106.xmlToMsg(xmlstr);
		com.cqfc.xmlparser.transactionmsg706.Msg msg706 = new com.cqfc.xmlparser.transactionmsg706.Msg();
		String partnerId = msg106.getHead().getPartnerid();
		String value = msg106.getBody().getQueryuser().getValue();
		String name = msg106.getBody().getQueryuser().getParmtype();

		// 判断传入的参数是userId、电话号码、身份证还是渠道userId
		UserInfo userInfo = new UserInfo();
		if (name != null && !"".equals(name)) {
			if (name.equals("userid")) {
				userInfo.setUserId(Integer.valueOf(value));
			}
			else if (name.equals("puserkey")) {
				userInfo.setPartnerId(message.getPartnerId());
				userInfo.setPartnerUserId(value);
			}
			else if (name.equals("phone")) {
				userInfo.setMobile(value);
			}
			else if (name.equals("idcardno")) {
				userInfo.setCardType(1);
				userInfo.setCardNo(value);
			}
			else{
				Log.fucaibiz.error("TransactionMsgProcessor106: 参数与xml文件中的不一致, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
				response.setData("合作商id不存在或者参数与xml文件中的不一致");
				return response;
			}
		}
		else {
			Log.fucaibiz.error("TransactionMsgProcessor106: 参数为空, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_PARAM_NULL);
			response.setData("参数为空");
			return response;
		}

			
		UserInfoData userInfoData = null;
		/**
		 * dynamicInvoke参数约定
		 * 1.第一个参数为模块名,使用驼峰命名规则（第一个单词第一个字母小写，后面单词首字母大写），比如businessControll,lotteryIssue,partnerAccount等
		 * 2.第二个参数为方法名,为调用模块中的方法名称。比如getUserInfoById,getUserInfoList等
		 * 3.其余参数为需要调用方法的参数，比如调用getUserInfoById需要传入userId,调用getUserInfoList需要传入userInfo,currentPage,pageSize
		 */
		ReturnMessage retMsg = TransactionProcessor.dynamicInvoke("userAccount", "getUserInfoList", userInfo, 1, 1);
		if(retMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)){
			userInfoData = (UserInfoData) retMsg.getObj();
		}
		else{
			Log.fucaibiz.error("TransactionMsgProcessor106: %s", retMsg.getMsg());
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			response.setData("系统错误");
			return response;
		}
		
		UserInfo retUserInfo = null;
		if (null != userInfoData.resultList
				&& userInfoData.resultList.size() > 0) {
			retUserInfo = userInfoData.resultList.get(0);
			if(!partnerId.equals(retUserInfo.getPartnerId())){
				Log.fucaibiz.error("TransactionMsgProcessor106: 该用户不属于该合作商, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
				response.setData("用户不存在");
				return response;
			}
		}
		else {
			Log.fucaibiz.error("TransactionMsgProcessor106: 用户不存在, msg=%s", xmlstr);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			return response;
		}

		// 拼装706交易对象，生成xml，返回
		Headtype headtype = new Headtype();
		headtype.setPartnerid(msg106.getHead().getPartnerid());
		headtype.setTranscode(responseTransCode);
		headtype.setVersion("1.0");
		headtype.setTime(DateUtil.getCurrentDateTime());
		msg706.setHead(headtype);
		Body body = new Body();
		Querytype querytype = new Querytype();
		querytype.setUserid(String.valueOf(retUserInfo.getUserId()));
		querytype.setIdcardno(retUserInfo.getCardNo());
		querytype.setRealname(retUserInfo.getUserName());
		querytype.setPuserkey(retUserInfo.getPartnerUserId());
		querytype.setPhone(retUserInfo.getMobile());
		querytype.setStatus(String.valueOf(retUserInfo.getUserAccount()
				.getState()));
		Account account = new Account();
		account.setCash(MoneyUtil.toYuanStr(retUserInfo.getUserAccount()
				.getUsableAmount()));
		account.setForzencash(MoneyUtil.toYuanStr(retUserInfo.getUserAccount()
				.getFreezeAmount()));
		List<UserHandsel> userHandsel = retUserInfo.getUserHandselList();
		long handselSum = 0;
		for (UserHandsel handsel : userHandsel) {
			handselSum += handsel.getUsableAmount();
		}
		account.setGiftcash(MoneyUtil.toYuanStr(handselSum));
		querytype.setAccount(account);
		body.setUser(querytype);
		msg706.setBody(body);

		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setData(TransactionMsgLoader706.msgToXml(msg706));
		response.setResponseTransCode(responseTransCode);
		Log.fucaibiz.debug("TransactionMsgProcessor106: success, msg=%s", xmlstr);
		return response;
	}
}
