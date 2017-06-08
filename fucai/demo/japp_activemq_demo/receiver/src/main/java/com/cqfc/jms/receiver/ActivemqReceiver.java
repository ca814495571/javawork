package com.cqfc.jms.receiver;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author: giantspider@126.com
 */

//@Component
public class ActivemqReceiver implements MessageListener {

    public void onMessage(Message message) {
        TextMessage textMsg = (TextMessage) message;
        try {
            System.out.println("=> receive message: " + textMsg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
