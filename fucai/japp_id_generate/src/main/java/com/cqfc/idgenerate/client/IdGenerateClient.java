package com.cqfc.idgenerate.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.idgenerate.IdGenerateService;

public class IdGenerateClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10040);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			IdGenerateService.Client idGenerateService = new IdGenerateService.Client(protocol);
			for (int i = 0; i < 10; i++) {
				System.out.println(idGenerateService.idGen("orderNo"));
			}
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
