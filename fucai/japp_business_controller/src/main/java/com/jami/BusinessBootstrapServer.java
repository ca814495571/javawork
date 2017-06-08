package com.jami;

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
import com.cqfc.order.datacenter.OrderBuffer;
import com.cqfc.order.datacenter.SportOrderBuffer;
import com.cqfc.order.service.impl.OrderServiceImpl;
import com.cqfc.protocol.businesscontroller.BusinessControllerService;
import com.cqfc.protocol.businesscontroller.BusinessControllerService.Iface;
import com.cqfc.util.ParameterUtils;

@Component
public class BusinessBootstrapServer {

	@Resource
	BusinessControllerService.Iface businessControllerService;

	private static ApplicationContext applicationContext = null;

	private static InitZooKeeperBean zookeeper;

	public static void main(String[] args) {
		zookeeper = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		ServiceModel model = new ServiceModel();
		model.setServerIP(ServerUtil.getLocalNetWorkIp());
		int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
		model.setPort(port);
		model.setSetNo(ParameterUtils.getParameterValue("setNo"));
		model.setIdlList(ServerUtil
				.getIdlList(BusinessControllerService.Iface.class));
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);

		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		BusinessBootstrapServer server = (BusinessBootstrapServer) applicationContext
				.getBean("businessBootstrapServer");
		OrderBuffer.initListQueue();
		SportOrderBuffer.initSportListQueue();
		OrderServiceImpl orderService = (OrderServiceImpl) applicationContext.getBean("orderServiceImpl");
		orderService.initOrderTask(applicationContext);
		server.start();
	}

	private void start() {
		System.out.println("starting BusinessBootstrapServer ...");
		BusinessControllerService.Processor<Iface> processor = new BusinessControllerService.Processor<Iface>(
				this.businessControllerService);
		try {
			int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(transport);
			serverArgs.processor(processor);
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
