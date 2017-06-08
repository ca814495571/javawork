package com.cqfc.ticketissue;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cqfc.common.zookeeper.InitZooKeeperBean;
import com.cqfc.common.zookeeper.ServerUtil;
import com.cqfc.common.zookeeper.WatcherType;
import com.cqfc.common.zookeeper.ZooKeeperManager;
import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.watcher.ReceiveSvcWatcher;
import com.cqfc.common.zookeeper.watcher.RegisterSvcWatcher;
import com.cqfc.protocol.ticketissue.TicketIssueService;
import com.cqfc.protocol.ticketissue.TicketIssueService.Iface;
import com.cqfc.util.ParameterUtils;

@Component
public class TicketIssueBootstrapServer {
	@Resource
	TicketIssueService.Iface ticketIssueService;

	private static ApplicationContext applicationContext = null;

	private static InitZooKeeperBean zookeeper;

	public static void main(String[] args) {
		zookeeper = new InitZooKeeperBean();
		zookeeper.start();
		int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
		ServiceModel model = new ServiceModel();
		model.setServerIP(ServerUtil.getLocalNetWorkIp());
		model.setPort(port);
		model.setSetNo(ParameterUtils.getParameterValue("setNo"));
		model.setIdlList(ServerUtil
				.getIdlList(TicketIssueService.Iface.class));
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);

		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		TicketIssueBootstrapServer server = (TicketIssueBootstrapServer) applicationContext
				.getBean("ticketIssueBootstrapServer");
		server.start();
	}

	private void start() {
		System.out.println("starting TicketIssueBootstrapServer ...");
		TicketIssueService.Processor<Iface> p = new TicketIssueService.Processor<Iface>(this.ticketIssueService);
		try {
			int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(transport);
			serverArgs.processor(p);
			serverArgs.minWorkerThreads(4);
			serverArgs.maxWorkerThreads(400);
			TThreadPoolServer server = new TThreadPoolServer(serverArgs);
			server.serve();
			zookeeper.stop();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}
