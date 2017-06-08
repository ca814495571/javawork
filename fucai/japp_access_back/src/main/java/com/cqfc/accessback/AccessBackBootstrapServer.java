package com.cqfc.accessback;

import javax.annotation.Resource;

import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.cqfc.accessback.record.InitRecordTask;
import com.cqfc.common.zookeeper.InitZooKeeperBean;
import com.cqfc.common.zookeeper.ServerUtil;
import com.cqfc.common.zookeeper.WatcherType;
import com.cqfc.common.zookeeper.ZooKeeperManager;
import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.watcher.ReceiveSvcWatcher;
import com.cqfc.common.zookeeper.watcher.RegisterSvcWatcher;
import com.cqfc.protocol.accessback.AccessBackService;
import com.cqfc.util.ParameterUtils;

@Component
public class AccessBackBootstrapServer {
	@Resource
	AccessBackService.Iface accessBackService;

    private static ApplicationContext applicationContext = null;

	private static InitZooKeeperBean zookeeper = null;

    public static void main(String[] args) {
		int port = Integer.valueOf(ParameterUtils.getParameterValue("SERVER_PORT"));
		zookeeper  = new InitZooKeeperBean();
		zookeeper.setHasDb(true);
		zookeeper.start();
		ServiceModel model = new ServiceModel();
		model.setServerIP(ServerUtil.getLocalNetWorkIp());
		model.setPort(port);
		model.setSetNo(ParameterUtils.getParameterValue("setNo"));
		model.setIdlList(ServerUtil
				.getIdlList(AccessBackService.Iface.class));
		RegisterSvcWatcher svcWatcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, svcWatcher);
		
        applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        AccessBackBootstrapServer server = (AccessBackBootstrapServer)applicationContext.getBean("accessBackBootstrapServer");
        server.start();
    }

    private void start() {
        System.out.println("starting AccessBootstrapServer ...");
        AccessBackService.Processor p = new AccessBackService.Processor(this.accessBackService);
        try {
			ThreadPoolTaskExecutor threadPoolTaskExecutor = applicationContext.getBean("threadPoolTaskExecutor",ThreadPoolTaskExecutor.class);
			InitRecordTask task = new InitRecordTask();
			threadPoolTaskExecutor.submit(task);
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
