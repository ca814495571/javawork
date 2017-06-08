package com.cqfc.xmlparser.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cqfc.xmlparser.TransactionMsgLoader000;
import com.cqfc.xmlparser.transactionmsg000.Body;
import com.cqfc.xmlparser.transactionmsg000.Headtype;
import com.cqfc.xmlparser.transactionmsg000.Msg;
import com.cqfc.xmlparser.transactionmsg000.Querytype;

public class ErrorMsgXmlHelper {
	
	/**
	 * 构造错误返回
	 * @param partnerId
	 * @param errorMsg
	 * @param errorCode
	 * @return
	 */
	public static String getErrorMsgXml(String partnerId, String errorMsg, String errorCode){
		Msg msg = new Msg();
		Headtype headtype = new Headtype();
		headtype.setPartnerid(partnerId);
		headtype.setTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		headtype.setTranscode("000");
		headtype.setVersion("1.0");
		Body body = new Body();
		Querytype querytype = new Querytype();
		querytype.setErrorcode(errorCode);
		querytype.setMsg(errorMsg);
		body.setError(querytype);
		msg.setHead(headtype);
		msg.setBody(body);
		return TransactionMsgLoader000.msgToXml(msg);
	}
}
