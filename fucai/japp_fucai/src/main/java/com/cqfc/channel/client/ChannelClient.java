package com.cqfc.channel.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.channel.protocol.ChannelCooperation;
import com.cqfc.channel.protocol.ChannelCooperationService;

public class ChannelClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10001);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			ChannelCooperationService.Client javaClient = new ChannelCooperationService.Client(
					protocol);
			
//			ChannelCooperation channelCooperation = new ChannelCooperation();
//			channelCooperation.setChannelName("insert");
//			channelCooperation.setChannelKey("insert_key");
//			channelCooperation.setStatus((short)1);
//			channelCooperation.setBalanceAlarm(1234);
//			channelCooperation.setAccountType(0);
//			channelCooperation.setAccountBalance(123);
//			channelCooperation.setMinBalanceAlarm("123");
//			channelCooperation.setExtension3("zxcv");
//			channelCooperation.setChannelID("333");
//			channelCooperation.setChannelAccountID("000");
//			channelCooperation.setCredit(666);
//			channelCooperation.setRemark("asdfasdf");
//			channelCooperation.setPassword("zcbzxcmnmjkjk");
//			System.out.println(javaClient.addChannelCooperation(channelCooperation));
			
			ChannelCooperation channelCooperation = new ChannelCooperation();
//			channelCooperation.setChannelName("xcz");
			System.out.println(javaClient.getChannelCooperationList(channelCooperation, 1, 5));
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
	}
}
