package com.cqfc.UserInfo.testClient;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.UserInfo.protocol.BusinessContollerService;
import com.cqfc.UserInfo.protocol.Message;


public class onMessageTest {
	public static void main(String[] args) {
        try {
        	Message msg = new Message();
        	msg.partnerID  = 10000;
        	msg.transCode = 101;
        	msg.msg = "test.....................";
            TTransport transport;
            transport = new TSocket("localhost",10010);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            BusinessContollerService.Client javaClient = new BusinessContollerService.Client(protocol);
            System.out.println(javaClient.onMessage(msg));
            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
