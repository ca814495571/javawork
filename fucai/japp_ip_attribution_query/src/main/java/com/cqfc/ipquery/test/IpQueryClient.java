package com.cqfc.ipquery.test;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.ipquery.IpQueryService;

public class IpQueryClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10020);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			IpQueryService.Client ipquery = new IpQueryService.Client(
					protocol);
			long start = System.currentTimeMillis();
			ipquery.parseIp2DB();
//			System.out.println(ipquery.queryIpAttribution("182.254.212.183"));
			System.out.println("cost " + (System.currentTimeMillis()-start) + "ms");
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
