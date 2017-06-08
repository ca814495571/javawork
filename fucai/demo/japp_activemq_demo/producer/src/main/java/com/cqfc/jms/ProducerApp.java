package com.cqfc.jms;

import com.cqfc.jms.producer.ActivemqProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: giantspider@126.com
 */

public class ProducerApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");

        ActivemqProducer producer = (ActivemqProducer)applicationContext.getBean("activemqProducer");
        System.out.println("message has been sent by producer");
        producer.send("hello, fucai!");
    }
}
