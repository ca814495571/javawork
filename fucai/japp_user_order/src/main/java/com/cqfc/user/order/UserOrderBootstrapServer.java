package com.cqfc.user.order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cqfc.protocol.userorder.UserOrderService;
import com.cqfc.user.order.service.IUserOrderService;
import com.cqfc.util.Pair;
import com.cqfc.util.ParameterUtils;
import com.jami.util.Log;


@Component
public class UserOrderBootstrapServer {

	@Autowired
	UserOrderService.Iface userOrderService;
	
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
				.getIdlList(UserOrderService.Iface.class));
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);

		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		UserOrderBootstrapServer server = (UserOrderBootstrapServer) applicationContext
				.getBean("userOrderBootstrapServer");
		
		
		IUserOrderService userOrderService = applicationContext.getBean("userOrderServiceImpl", IUserOrderService.class);
		
		List<Pair<File, BlockingQueue>> pairs = new ArrayList<Pair<File,BlockingQueue>>();
		//扫描日志文件,将文件中没有入库的订单放入队列中
		int flag =userOrderService.logOrderToQueue(pairs);
		if(flag == 1){
			Log.run.info("日志文件未入库的订单加入队列成功......");
			System.out.println("日志文件未入库的订单加入队列成功......");
			//启动同步批量处理订单的任务
			userOrderService.initAddOrderTask(applicationContext);
			
	//		new Thread(new StartQueueToDbTask(pairs)).start() ;
			server.start();

		}else{
			Log.run.info("日志文件未入库的订单加入队列失败...");
			System.out.println("日志文件未入库的订单加入队列失败...");
		}
	}

	private void start() {
		Log.run.info("starting userOrderBootstrapServer ...");
		System.out.println("starting userOrderBootstrapServer ...");
		//关联处理器与userOrder 服务的实现
		UserOrderService.Processor userOrder = new UserOrderService.Processor(
				this.userOrderService);
		try {
			int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(
					transport);
			serverArgs.processor(userOrder);
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
