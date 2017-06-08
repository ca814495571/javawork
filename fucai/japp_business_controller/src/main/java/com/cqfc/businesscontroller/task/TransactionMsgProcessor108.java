package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader108;
import com.cqfc.xmlparser.TransactionMsgLoader708;
import com.cqfc.xmlparser.transactionmsg108.Msg;
import com.cqfc.xmlparser.transactionmsg708.Body;
import com.cqfc.xmlparser.transactionmsg708.Headtype;
import com.cqfc.xmlparser.transactionmsg708.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor108 {

	public static TransResponse process(Message message) {
		TransResponse response = new TransResponse();
		String responseTransCode = "708";
		// 请求数据
		String xmlstr = message.getTransMsg();
		// 请求消息对象
		Msg msg108 = TransactionMsgLoader108.xmlToMsg(xmlstr);
		com.cqfc.xmlparser.transactionmsg708.Msg msg708 = new com.cqfc.xmlparser.transactionmsg708.Msg();

		long userId = Long.valueOf(msg108.getBody().getUseraccount()
				.getUserid());
		String partnerId = msg108.getHead().getPartnerid();

		// 查询
		UserInfo userInfo = null;
		/**
		 * dynamicInvoke参数约定
		 * 1.第一个参数为模块名,使用驼峰命名规则（第一个单词第一个字母小写，后面单词首字母大写），比如businessControll
		 * ,lotteryIssue,partnerAccount等
		 * 2.第二个参数为方法名,为调用模块中的方法名称。比如getUserInfoById,getUserInfoList等
		 * 3.其余参数为需要调用方法的参数
		 * ，比如调用getUserInfoById需要传入userId,调用getUserInfoList需要传入userInfo
		 * ,currentPage,pageSize
		 */
		ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
				"userAccount", "findUserInfoById", userId);
		if (retMsg.getStatusCode().equals(
				ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
			userInfo = (UserInfo) retMsg.getObj();
			if (!userInfo.getPartnerId().equals(partnerId)) {
				Log.fucaibiz.error("TransactionMsgProcessor108: 该用户不属于该合作商, msg=%s", xmlstr);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
				response.setData("用户不存在");
				return response;
			}
		}
		else {
			Log.fucaibiz.error("TransactionMsgProcessor108: %s", retMsg.getMsg());
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			return response;
		}

		// 拼装708交易对象，生成xml，返回
		Headtype headtype = new Headtype();
		headtype.setPartnerid(msg108.getHead().getPartnerid());
		headtype.setTranscode(responseTransCode);
		headtype.setVersion("1.0");
		headtype.setTime(DateUtil.getCurrentDateTime());
		msg708.setHead(headtype);
		Body body = new Body();
		Querytype querytype = new Querytype();
		querytype.setUserid(String.valueOf(userId));
		querytype.setCashbalance(MoneyUtil.toYuanStr(userInfo
				.getUserAccount().getUsableAmount()));
		querytype.setStatus(String.valueOf(userInfo.getUserAccount()
				.getState()));
		List<UserHandsel> userHandsel = userInfo.getUserHandselList();
		long handselSum = 0;
		for (UserHandsel handsel : userHandsel) {
			handselSum += handsel.getUsableAmount();
		}
		querytype.setGiftbalance(MoneyUtil
				.toYuanStr(handselSum));
		body.setUseraccount(querytype);
		msg708.setBody(body);

		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setData(TransactionMsgLoader708.msgToXml(msg708));
		response.setResponseTransCode(responseTransCode);
		Log.fucaibiz.debug("TransactionMsgProcessor108: success, msg=%s", xmlstr);
		return response;
	}
}
