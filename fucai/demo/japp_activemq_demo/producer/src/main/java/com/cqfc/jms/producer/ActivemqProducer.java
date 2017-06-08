package com.cqfc.jms.producer;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;


/**
 * @author: giantspider@126.com
 */

@Component
public class ActivemqProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(final String msg) {
        //使用JMSTemplate可以很简单的实现发送消息
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

}
