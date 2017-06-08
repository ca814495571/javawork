package com.cqfc.cancelorder.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.CancelOrderService;
import com.cqfc.util.CancelOrderConstant;

public class CancelOrderClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10017);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			CancelOrderService.Client cancelOrderService = new CancelOrderService.Client(protocol);

			CancelOrder cancelOrder = new CancelOrder();
			cancelOrder.setOrderNo("test_orderNo_001");
			cancelOrder.setPartnerId("001");
			cancelOrder.setUserId(2);
			cancelOrder.setOutTicketStatus(CancelOrderConstant.OutTicketStatus.IN_TICKET.getValue());
			cancelOrder.setTotalAmount(200);
			cancelOrder.setOrderContent("01,02,03,04,05,06:12");
			cancelOrder.setPlayType("0");
			cancelOrder.setMultiple(1);
			cancelOrder.setLotteryId("SSQ");
			cancelOrder.setIssueNo("2014113");

			System.out.println(cancelOrderService.createCancelOrder(cancelOrder));

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
