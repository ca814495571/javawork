package com.cqfc.ticketwinning.testClient;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.ticketwinning.service.impl.SendPrizeServiceImpl;

public class SendPrizeServiceImplTest {
	public static void main(String[] args) throws TException {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		SendPrizeServiceImpl sendPrizeService = applicationContext.getBean("sendPrizeServiceImpl", SendPrizeServiceImpl.class);
		
		sendPrizeService.sendPrize("SSQ", "2014001");
	}
}
