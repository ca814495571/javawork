package com.cqfc.ticketwinning.testClient;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.ticketwinning.TicketWinningService;

public class TestStatistic {
	public static void main(String[] args) {
		TTransport transport = null;
		try {
			transport = new TSocket("127.0.0.1", 10091);
//			transport = new TSocket("127.0.0.1", 10090);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			TicketWinningService.Client ticketWinning = new TicketWinningService.Client(
					protocol);
			long moneyByGame = ticketWinning.getTotalWinningMoneyByGame(
					"CQSSC", "2014091906011");
			System.out.println(moneyByGame);
//			AccessBackService.Client client = new AccessBackService.Client(protocol);
//			TicknumRecord record = client.getTicknumRecord("SSQ", "2014157");
//			System.out.println("record,sucnum=" + record.getSuccessNum() + ",failnum=" + record.getFailNum());
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException
					&& ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
				System.out.println("The result of function is NULL");
			}
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}
}
