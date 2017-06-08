package com.cqfc.ticketwinning.testClient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cqfc.ticketwinning.service.impl.UpdateOrderStatusServiceImpl;

public class UpdateOrderStatusTest {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring.xml");
		UpdateOrderStatusServiceImpl updateOrderService = applicationContext.getBean("updateOrderStatusServiceImpl", UpdateOrderStatusServiceImpl.class);
		
		updateOrderService.updateOrderStatus("SSQ", "2014001");
	}
}
