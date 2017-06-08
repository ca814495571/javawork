package com.jami.common;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.cqfc.util.ActivemqSendObject;

/**
 * @author: HowKeyond
 */
public class ActivemqProducer {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void send(final ActivemqSendObject obj, final String methodId) {
		// 使用JMSTemplate可以很简单的实现发送消息
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage message = session.createObjectMessage();
				message.setObject(obj);
				message.setStringProperty("methodId", methodId);
				return message;
			}
		});
	}

}
