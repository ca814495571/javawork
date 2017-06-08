package com.cqfc.appendtask.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.appendtask.AppendTaskService;

public class AppendTaskClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10013);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			AppendTaskService.Client appendTask = new AppendTaskService.Client(protocol);
//			System.out.println(appendTask.findMinAppendTaskDetail("00860001", "2014-0731-3002"));
			List<String> l = new ArrayList<String>();
			l.add("2014095");
//			System.out.println(appendTask.stopAppendTask("00860001#2014-0814-90011", l));
//			System.out.println(appendTask.getRefundSerialNumberByOrderNo("001#0087002#201409169004#10","4"));
			System.out.println(appendTask.updateAppendAfterOrderPrize("001#0087002#201409169004#10",1,7));

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
