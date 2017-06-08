package com.cqfc.test;



import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoadSpringConf {

	public static void main(String[] args) {
	   
		ApplicationContext  tx  = new ClassPathXmlApplicationContext("spring.xml");
		
	}


	
}
