package com.cqfc.ticketwinning.testClient;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.common.zookeeper.InitZooKeeperBean;
import com.cqfc.ticketwinning.service.impl.CalBDPrizeServiceImpl;

public class CalBDPrizeTest {
	public static void main(String[] args) throws TException {
		InitZooKeeperBean zookeeper = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		CalBDPrizeServiceImpl taskService = applicationContext.getBean("calBDPrizeServiceImpl", CalBDPrizeServiceImpl.class);
		taskService.calBDPrize(args[0], args[1], Integer.valueOf(args[2]), false);
	}
}
