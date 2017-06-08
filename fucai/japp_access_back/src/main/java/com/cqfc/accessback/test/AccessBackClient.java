package com.cqfc.accessback.test;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.accessback.AccessBackService;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader605;

public class AccessBackClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10090);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			AccessBackService.Client accessBack = new AccessBackService.Client(
					protocol);

			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = new com.cqfc.xmlparser.transactionmsg605.Msg();
			com.cqfc.xmlparser.transactionmsg605.Headtype headtype = new com.cqfc.xmlparser.transactionmsg605.Headtype();
			headtype.setTranscode("605");
			headtype.setPartnerid("0086001");
			headtype.setTime(String.valueOf(System.currentTimeMillis()));
			headtype.setVersion("1.0");
			msg605.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg605.Body reqBody = new com.cqfc.xmlparser.transactionmsg605.Body();
			com.cqfc.xmlparser.transactionmsg605.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg605.Querytype();
			ticketResult.setId("jkfdsljdsls");
			ticketResult.setPalmid("kjljkl");
			ticketResult.setGameid("SSQ");
			ticketResult.setMultiple(String.valueOf(2));
			ticketResult.setIssue("2014152");
			ticketResult.setPlaytype("");
			ticketResult.setMoney(MoneyUtil.toYuanStr(400));
			ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);
//			ticketResult.setStatuscode(ConstantsUtil.STATUS_CODE_RRADE_FAIL);
			ticketResult.setMsg("d");
			reqBody.setTicketresult(ticketResult);
			msg605.setBody(reqBody);
//			System.out.println(accessBack.sendAccessBackMessage("00860010",
//					TransactionMsgLoader605.msgToXml(msg605)));
			
			System.out.println(accessBack.getTicknumRecord("CQKL10", "20141020064"));
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException
					&& ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
				System.out.println("The result of function is NULL");
			}
		}
	}
}
