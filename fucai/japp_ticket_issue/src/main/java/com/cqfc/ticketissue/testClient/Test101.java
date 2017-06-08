package com.cqfc.ticketissue.testClient;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.ticketissue.TicketIssueService;

public class Test101 {
	public static void main(String[] args) {		
		TTransport transport = null;
		try {
			transport = new TSocket("localhost", 10066);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			TicketIssueService.Client ticketIssue = new TicketIssueService.Client(
					protocol);
			System.out.println(ticketIssue.getFucaiCount("SSQ", "2014120"));
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException
					&& ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
				System.out.println("The result of function is NULL");
			}
		}finally{
			if(transport != null){
				transport.close();
			}
		}
	}
}
