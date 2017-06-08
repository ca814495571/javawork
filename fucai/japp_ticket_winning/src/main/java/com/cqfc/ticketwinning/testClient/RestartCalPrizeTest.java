package com.cqfc.ticketwinning.testClient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.ticketwinning.service.impl.RestartCalPrizeServiceImpl;

public class RestartCalPrizeTest {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		RestartCalPrizeServiceImpl taskService = applicationContext.getBean("restartCalPrizeServiceImpl", RestartCalPrizeServiceImpl.class);
		taskService.restartCalPrizeAll("D3", "2014278");
		taskService.restartCalPrizeAll("CQSSC ", "20141013085");
		taskService.restartCalPrizeAll("CQSSC", "  20141013087");
		taskService.restartCalPrizeAll("CQSSC", "20141013084");
		taskService.restartCalPrizeAll("CQSSC", " 20141013086");
		taskService.restartCalPrizeAll("CQSSC", " 20141013086");
		taskService.restartCalPrizeAll("CQKL10", " 20141013078");
	}
}
