package com.cqfc.ticketwinning.testClient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.common.zookeeper.InitZooKeeperBean;
import com.cqfc.ticketwinning.service.impl.RestartCalPrizeServiceImpl;

public class RestartCalJCPrizeTest {
	public static void main(String[] args) {
		InitZooKeeperBean zookeeper = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		RestartCalPrizeServiceImpl taskService = applicationContext.getBean("restartCalPrizeServiceImpl", RestartCalPrizeServiceImpl.class);
		taskService.restartCalJCPrizeAll(args[0]);
	}
}
