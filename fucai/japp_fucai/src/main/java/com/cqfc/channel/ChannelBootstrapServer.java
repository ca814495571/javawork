package com.cqfc.channel;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cqfc.channel.protocol.ChannelCooperationService;

@Component
public class ChannelBootstrapServer {

	@Resource
	ChannelCooperationService.Iface channelCooperationService;

	private static ApplicationContext applicationContext = null;

	public static void main(String[] args) {
		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		ChannelBootstrapServer server = (ChannelBootstrapServer) applicationContext
				.getBean("channelBootstrapServer");
		server.start();
	}

	private void start() {
		System.out.println("starting channelBootstrapServer ...");
		ChannelCooperationService.Processor p = new ChannelCooperationService.Processor(
				this.channelCooperationService);
		try {
			TServerTransport transport = new TServerSocket(10001);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(
					transport);
			serverArgs.processor(p);
			serverArgs.minWorkerThreads(4);
			serverArgs.maxWorkerThreads(400);
			TThreadPoolServer server = new TThreadPoolServer(serverArgs);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}
