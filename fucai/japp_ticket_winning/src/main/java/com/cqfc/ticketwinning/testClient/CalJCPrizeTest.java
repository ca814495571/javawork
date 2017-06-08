package com.cqfc.ticketwinning.testClient;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.common.zookeeper.InitZooKeeperBean;
import com.cqfc.ticketwinning.service.impl.CalJCPrizeServiceImpl;

public class CalJCPrizeTest {
	public static void main(String[] args) throws TException {
		InitZooKeeperBean zookeeper = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		CalJCPrizeServiceImpl taskService = applicationContext.getBean("calJCPrizeServiceImpl", CalJCPrizeServiceImpl.class);
		taskService.calJCPrize(args[0], args[1], false);
	}
}
