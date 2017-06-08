package com.cqfc.partneraccount;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.cqfc.common.zookeeper.InitZooKeeperBean;
import com.cqfc.common.zookeeper.ServerUtil;
import com.cqfc.common.zookeeper.WatcherType;
import com.cqfc.common.zookeeper.ZooKeeperManager;
import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.watcher.ReceiveSvcWatcher;
import com.cqfc.common.zookeeper.watcher.RegisterSvcWatcher;
import com.cqfc.partneraccount.task.InitAccountTask;
import com.cqfc.partneraccount.task.SqlExecuteTask;
import com.cqfc.protocol.partneraccount.PartnerAccountService;
import com.cqfc.util.ParameterUtils;

@Component
public class PartnerAccountBootstrapServer {

	@Resource
	PartnerAccountService.Iface partnerAccountHandler;

	private static ApplicationContext applicationContext = null;

	private static InitZooKeeperBean zookeeper;

	public static void main(String[] args) {
		zookeeper = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
		ServiceModel model = new ServiceModel();
		model.setServerIP(ServerUtil.getLocalNetWorkIp());
		model.setPort(port);
		model.setSetNo(ParameterUtils.getParameterValue("setNo"));
		model.setIdlList(ServerUtil
				.getIdlList(PartnerAccountService.Iface.class));
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);

		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		PartnerAccountBootstrapServer server = (PartnerAccountBootstrapServer) applicationContext
				.getBean("partnerAccountBootstrapServer");
		server.start();
	}

	private void start() {
		System.out.println("starting partnerAccountBootstrapServer ...");
		PartnerAccountService.Processor partnerAccount = new PartnerAccountService.Processor(this.partnerAccountHandler);
		try {
			ThreadPoolTaskExecutor threadPoolTaskExecutor = applicationContext.getBean("threadPoolTaskExecutor",ThreadPoolTaskExecutor.class);
			threadPoolTaskExecutor.submit(new InitAccountTask(applicationContext));
			SqlExecuteTask task = new SqlExecuteTask(applicationContext);
			threadPoolTaskExecutor.submit(task);
			int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(transport);
			serverArgs.processor(partnerAccount);
			serverArgs.minWorkerThreads(4);
			serverArgs.maxWorkerThreads(400);
			TThreadPoolServer server = new TThreadPoolServer(serverArgs);
			server.serve();
			zookeeper.stop();
			task.stop();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}

}
