package com.cqfc.partner.order;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

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
import com.cqfc.partner.order.service.IPartnerOrderService;
import com.cqfc.protocol.partnerorder.PartnerOrderService;
import com.cqfc.util.Pair;
import com.cqfc.util.ParameterUtils;
import com.jami.util.Log;



@Component
public class PartnerOrderBootstrapServer {

	@Resource
	PartnerOrderService.Iface partnerOrderService;
	
	private static ApplicationContext applicationContext = null;

	private static InitZooKeeperBean zookeeper;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		PartnerOrderBootstrapServer.applicationContext = applicationContext;
	}

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
				.getIdlList(PartnerOrderService.Iface.class));
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);

		applicationContext = new ClassPathXmlApplicationContext("spring.xml");
		PartnerOrderBootstrapServer server = (PartnerOrderBootstrapServer) applicationContext
				.getBean("partnerOrderBootstrapServer");
		
		IPartnerOrderService partnerOrderService = applicationContext.getBean("partnerOrderServiceImpl", IPartnerOrderService.class);
		
		List<Pair<File, BlockingQueue>> pairs = new ArrayList<Pair<File,BlockingQueue>>();
		//扫描日志文件,将文件中没有入库的订单放入队列中
		int flag =partnerOrderService.logOrderToQueue(pairs);
		if(flag == 1){
			System.out.println("日志文件未入库的订单加入队列成功...");
			Log.run.info("所有日志文件未入库的订单加入队列成功...");
			//启动同步批量处理订单的任务
			partnerOrderService.initAddOrderTask(applicationContext);
			partnerOrderService.initUpdateWinResultTask(applicationContext);
			server.start();

		}else{
			System.out.println("日志文件未入库的订单加入队列失败...");
			Log.run.info("日志文件未入库的订单加入队列失败...");
		}
	}

	private void start() {
		System.out.println("starting partnerOrderBootstrapServer ...");
		Log.run.info("starting partnerOrderBootstrapServer ...");
		PartnerOrderService.Processor partnerOrder = new PartnerOrderService.Processor(
				this.partnerOrderService);
		try {
			int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
			TServerTransport transport = new TServerSocket(port);
			TThreadPoolServer.Args serverArgs = new TThreadPoolServer.Args(
					transport);
			serverArgs.processor(partnerOrder);
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
