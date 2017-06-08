package com.cqfc.businesscontroller.task;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader112;
import com.cqfc.xmlparser.TransactionMsgLoader711;
import com.cqfc.xmlparser.transactionmsg711.Msg;
import com.cqfc.xmlparser.transactionmsg711.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor112 {


	private static String retrunCode = "711";

	/**
	 * 彩金查询接口，为合作商提供彩金赠送的交易结果信息，
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理112消息,与111返回的消息体一样");
		
		TransResponse response = new TransResponse();

		String xml112 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg112.Msg msg112 = TransactionMsgLoader112
				.xmlToMsg(xml112);
		com.cqfc.xmlparser.transactionmsg112.Headtype head112 = msg112
				.getHead();
		com.cqfc.xmlparser.transactionmsg112.Body body112 = msg112.getBody();
		com.cqfc.xmlparser.transactionmsg112.Querytype queryType112 = body112
				.getGiftinfo();

		String partnerId = head112.getPartnerid();
		String version = head112.getVersion();
		// 该彩金交易在合作商的彩金id，不能重复
		String giftId = queryType112.getGiftid();

		com.cqfc.xmlparser.transactionmsg711.Msg msg711 = new Msg();
		com.cqfc.xmlparser.transactionmsg711.Body body711 = new com.cqfc.xmlparser.transactionmsg711.Body();
		com.cqfc.xmlparser.transactionmsg711.Headtype head711 = new com.cqfc.xmlparser.transactionmsg711.Headtype();
		com.cqfc.xmlparser.transactionmsg711.Querytype bodyInfo711 = new Querytype();

		UserHandsel userHandsel = null;
		ReturnMessage retMsg = null;
		
		Log.fucaibiz.info("调用userAccount中的findUserHandselByPartnerId方法,参数giftId="+giftId);
		
		retMsg = TransactionProcessor.dynamicInvoke("userAccount",
				"findUserHandselByPartnerId",partnerId,giftId);

		String statusCode = retMsg.getStatusCode();

		Log.fucaibiz.info("返回的状态码statusCode="+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)&&retMsg.getObj()!=null) {

			userHandsel = (UserHandsel) retMsg.getObj();

			if (userHandsel != null) {

				bodyInfo711.setActivityid(userHandsel.getActivityId());
				bodyInfo711.setGiftid(userHandsel.getPartnerHandselId());
				bodyInfo711.setGiftmoney(MoneyUtil
						.toYuanStr(userHandsel.getPresentAmount()));
				bodyInfo711.setUserid(String.valueOf(userHandsel.getUserId()));
				bodyInfo711.setPalmgiftid(userHandsel.getSerialNumber());

				bodyInfo711
						.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);
				bodyInfo711.setMsg("赠送成功");

			} else {

				bodyInfo711.setStatuscode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				bodyInfo711.setMsg("赠送失败");
			}

		} else {

			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			
			Log.fucaibiz.error("返回错误msg:"+retMsg.getMsg());
			return response;
		}

		head711.setTime(DateUtil.getCurrentDateTime());
		head711.setPartnerid(partnerId);
		head711.setVersion(version);
		head711.setTranscode(retrunCode);
		msg711.setHead(head711);

		body711.setGiftinfo(bodyInfo711);
		msg711.setBody(body711);


		response.setData(TransactionMsgLoader711.msgToXml(msg711));
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setResponseTransCode(retrunCode);
		
		Log.fucaibiz.info("712消息体生成成功(与711一样)");
		return response;

	}
}
