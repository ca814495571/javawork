package com.cqfc.user.order.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.userorder.Order;
import com.cqfc.protocol.userorder.UserOrderService;

public class UserOrderClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10089);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			UserOrderService.Client userOrderService = new UserOrderService.Client(
					protocol);
			Order order = new Order();
			
			order.setCardNo("123");
			
			order.setOrderId(123123123);
			order.setLotteryId("ssq");
			order.setPartnerId("123123");
			order.setUserId(213123);
			order.setIssueNo("2014001");
			order.setOrderNo("");
			order.setOrderStatus(1);
			order.setTotalAmount(123123);
			order.setPrizeAfterTax(123123);
			order.setMultiple(2);
			order.setOrderContent("123");
			order.setPlayType("WUXUANYI");
			order.setPaySerialNumber("123123123");
			order.setRealName("test");
			order.setMobile("213123123");
			order.setCreateTime("123123");
			order.setLastUpdateTime("123123");
			order.setOrderType(1);
			
			userOrderService.addUserOrder(order);
			System.out.println(userOrderService.getUserOrder(order, 1, 5));
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
