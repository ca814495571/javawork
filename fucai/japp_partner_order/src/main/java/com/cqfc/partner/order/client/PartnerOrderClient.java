package com.cqfc.partner.order.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.partner.order.service.impl.PartnerOrderServiceImpl;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.PartnerOrderService;

public class PartnerOrderClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10088);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			PartnerOrderService.Client partnerOrderService = new PartnerOrderService.Client(
					protocol);

			Order order = new Order();
//			order.setOrderId(1);
//			order.setCardNo("123");
//			order.setLotteryId("SSC");
//			order.setPartnerId("00860001");
//			order.setUserId(3);
//			order.setIssueNo("2014001");
//			order.setOrderNo("001#00860001#110");
//			order.setOrderStatus(10);
//			order.setTotalAmount(100000);
//			order.setWinPrizeMoney(10000);
//			order.setPrizeAfterTax(0);
//			order.setMultiple(1);
//			order.setOrderContent("01,02,04,14,12");
//			order.setPlayType("WUXUANYI");
//			order.setPaySerialNumber("1123123");
//			order.setRealName("test");
//			order.setMobile("213123123");
//			order.setCreateTime("2014-08-22 ");
//			order.setOrderType(1);
//			order.setTradeId("110");

			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
			
			PartnerOrderServiceImpl orderService = ctx.getBean("partnerOrderServiceImpl", PartnerOrderServiceImpl.class);
			orderService.partnerDailySaleCount("2014-8-22");
			
		//	order = partnerOrderService.getPartnerOrderByOrderNo("001#00860001#1");
	//		partnerOrderService.getPartnerOrderByWhere(order);
			//			partnerOrderService.addPartnerOrder(order);
	///	System.out.println(partnerOrderService.updatePartnerOrder(order));	
			
			
			
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
