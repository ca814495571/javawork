package com.cqfc.businesscontroller.task;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader109;
import com.cqfc.xmlparser.TransactionMsgLoader709;
import com.cqfc.xmlparser.transactionmsg109.Body;
import com.cqfc.xmlparser.transactionmsg109.Headtype;
import com.cqfc.xmlparser.transactionmsg709.Msg;
import com.cqfc.xmlparser.transactionmsg709.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor109 {


	private static String retrunCode = "709";

	/**
	 * 109接口查询合作商当前资金数据
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理109消息");
		
		TransResponse response = new TransResponse();

		String xml109 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg109.Msg msg109 = TransactionMsgLoader109
				.xmlToMsg(xml109);

		Headtype head109 = msg109.getHead();
		Body body109 = msg109.getBody();
		com.cqfc.xmlparser.transactionmsg109.Querytype queryType109 = body109
				.getPartneraccount();

		String version = head109.getVersion();

		String partnerId = queryType109.getPartnerid();
		if ( partnerId != null&&!head109.getPartnerid().equals(partnerId) ) {

			response.setStatusCode(ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
			response.setData("合作商id不存在或者参数与xml文件中的不一致");
			Log.fucaibiz.error("返回的状态错误信息:"+partnerId);
			return response;
		}

		com.cqfc.xmlparser.transactionmsg709.Msg msg709 = new Msg();
		com.cqfc.xmlparser.transactionmsg709.Headtype head709 = new com.cqfc.xmlparser.transactionmsg709.Headtype();
		com.cqfc.xmlparser.transactionmsg709.Body body709 = new com.cqfc.xmlparser.transactionmsg709.Body();
		Querytype querytype709 = new Querytype();
		PartnerAccount partnerAccount = null;

		ReturnMessage retMsg;
		
		Log.fucaibiz.info("调用partnerAccount中findPartnerAccountByPartnerId方法，参数partnerId="+partnerId);
		
		retMsg = TransactionProcessor.dynamicInvoke("partnerAccount",
				"findPartnerAccountByPartnerId", partnerId);

		String statusCode = retMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode) && retMsg.getObj()!=null) {

			partnerAccount = (PartnerAccount) retMsg.getObj();

		} else {

			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			Log.fucaibiz.error("返回的状态错误信息:"+retMsg.getMsg());
			return response;
		}

		querytype709.setCashbalance(MoneyUtil.toYuanStr(partnerAccount
				.getUsableAmount()));
		querytype709.setCreditlimit(MoneyUtil.toYuanStr(partnerAccount
				.getCreditLimit()));

		body709.setPartneraccount(querytype709);

		head709.setPartnerid(partnerId);
		head709.setTime(DateUtil.getCurrentDateTime());
		head709.setVersion(version);
		head709.setTranscode(retrunCode);

		msg709.setBody(body709);
		msg709.setHead(head709);


		response.setData(TransactionMsgLoader709.msgToXml(msg709));
		response.setResponseTransCode(retrunCode);
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		Log.fucaibiz.info("返回709消息体成功");
		return response;
	}
}
