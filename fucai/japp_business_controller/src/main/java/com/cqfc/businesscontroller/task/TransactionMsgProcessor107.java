package com.cqfc.businesscontroller.task;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserRecharge;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader107;
import com.cqfc.xmlparser.TransactionMsgLoader707;
import com.cqfc.xmlparser.transactionmsg107.Body;
import com.cqfc.xmlparser.transactionmsg107.Headtype;
import com.cqfc.xmlparser.transactionmsg107.Msg;
import com.jami.util.Log;

public class TransactionMsgProcessor107 {

	private static String retrunCode = "707";

	/**
	 * 查询用户的充值结果信息
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理107消息");

		TransResponse response = new TransResponse();

		String xml107 = message.getTransMsg();

		Msg msg107 = TransactionMsgLoader107.xmlToMsg(xml107);

		Headtype head107 = msg107.getHead();
		Body body107 = msg107.getBody();
		com.cqfc.xmlparser.transactionmsg107.Querytype queryType107 = body107
				.getChargeuporder();

		String partnerId = head107.getPartnerid();
		String version = head107.getVersion();
		String money = queryType107.getMoney();
		String chargeId = queryType107.getChargeid();

		com.cqfc.xmlparser.transactionmsg707.Msg msg707 = new com.cqfc.xmlparser.transactionmsg707.Msg();
		com.cqfc.xmlparser.transactionmsg707.Headtype head707 = new com.cqfc.xmlparser.transactionmsg707.Headtype();
		com.cqfc.xmlparser.transactionmsg707.Body body707 = new com.cqfc.xmlparser.transactionmsg707.Body();
		com.cqfc.xmlparser.transactionmsg707.Querytype queryType707 = new com.cqfc.xmlparser.transactionmsg707.Querytype();
		String xml707 = "";
		UserRecharge userRecharge = null;

		com.cqfc.processor.ReturnMessage retMsg;

		Log.fucaibiz
				.info("调用partnerAccount中的findPartnerRecharge方法,参数:partnerId="
						+ partnerId + ",chargeId=" + chargeId);
		retMsg = TransactionProcessor.dynamicInvoke("userAccount",
				"findUserRecharge", partnerId, chargeId);

		String statusCode = retMsg.getStatusCode();

		Log.fucaibiz.info("返回的状态码：" + statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)
				&& retMsg.getObj() != null) {

			userRecharge = (UserRecharge) retMsg.getObj();
			money = MoneyUtil.toYuanStr(userRecharge.getRechargeAmount());

			if (userRecharge != null) {

				queryType707.setChargeid(chargeId);
				queryType707.setMoney(money);
				queryType707.setMsg("充值成功");
				queryType707.setStatus("2");

			}

		} else if (ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(statusCode)) {

			response.setStatusCode(ConstantsUtil.STATUS_CODE_RECHARGEORDER_NOTEXIST);
			response.setData("不存在的充值定单");
			return response;

		} else {
			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			Log.fucaibiz.error("返回的错误消息" + retMsg.getMsg());
			return response;
		}

		body707.setChargeuporder(queryType707);

		head707.setPartnerid(partnerId);
		head707.setTime(DateUtil.getCurrentDateTime());
		head707.setVersion(version);
		head707.setTranscode(retrunCode);

		msg707.setHead(head707);
		msg707.setBody(body707);
		xml707 = TransactionMsgLoader707.msgToXml(msg707);
		response.setData(xml707);
		response.setResponseTransCode(retrunCode);
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		Log.fucaibiz.info("707消息生成成功");
		return response;
	}
}
