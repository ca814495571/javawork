package com.cqfc.ticketwinning.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.ticketwinning.TicketWinningService;

public class TicketWinningClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 100091);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			TicketWinningService.Client ticketWinning = new TicketWinningService.Client(
					protocol);
			
			
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException 
                    && ((TApplicationException) e).getType() ==   
                                 TApplicationException.MISSING_RESULT) { 
                System.out.println("The result of function is NULL"); 
            }
		}
	}
}
