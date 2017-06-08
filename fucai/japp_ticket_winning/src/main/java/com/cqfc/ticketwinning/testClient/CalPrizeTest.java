package com.cqfc.ticketwinning.testClient;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.ticketwinning.service.impl.CalPrizeServiceImpl;

public class CalPrizeTest {
	public static void main(String[] args) throws TException {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		CalPrizeServiceImpl taskService = applicationContext.getBean("calPrizeServiceImpl", CalPrizeServiceImpl.class);
		taskService.calPrize(args[0], args[1], false);
	}
}
