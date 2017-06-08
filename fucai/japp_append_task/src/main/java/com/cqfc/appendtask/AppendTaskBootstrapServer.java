package com.cqfc.appendtask;

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
import com.cqfc.protocol.appendtask.AppendTaskService;
import com.cqfc.protocol.appendtask.AppendTaskService.Iface;
import com.cqfc.util.ParameterUtils;

@Component
public class AppendTaskBootstrapServer {

	@Resource
	AppendTaskService.Iface appendTaskHandler;

	private static InitZooKeeperBean zookeeper;

	private static ApplicationContext applicationContext = null;

	public static void main(String[] args) {
		int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
		zookeeper = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		ServiceModel model = new ServiceModel();
		model.setServerIP(ServerUtil.getLocalNetWorkIp());
		model.setPort(port);
		model.setSetNo(ParameterUtils.getParameterValue("setNo"));
		model.setIdlList(ServerUtil
				.getIdlList(AppendTaskService.Iface.class));
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);

		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		AppendTaskBootstrapServer server = (AppendTaskBootstrapServer) applicationContext
				.getBean("appendTaskBootstrapServer");
		server.start();
	}

	private void start() {
		System.out.println("starting appendTaskBootstrapServer ...");
		AppendTaskService.Processor<Iface> appendTask = new AppendTaskService.Processor<Iface>(this.appendTaskHandler);
		try {
			int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(transport);
			serverArgs.processor(appendTask);
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
