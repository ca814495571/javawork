package com.cqfc.common.zookeeper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.util.StringUtils;

import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.watcher.ReceiveSvcWatcher;
import com.cqfc.common.zookeeper.watcher.RegisterSvcWatcher;

public class InitZooKeeperListener implements ServletContextListener {

	private InitZooKeeperBean zookeeper;
	
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		zookeeper = new InitZooKeeperBean();
		String hasDb = arg0.getServletContext().getInitParameter("hasDb");
		if (!StringUtils.isEmpty(hasDb) && Boolean.parseBoolean(hasDb)){
			zookeeper.setHasDb(true);
		}
		zookeeper.start();
		String port = arg0.getServletContext().getInitParameter("port");
		String service = arg0.getServletContext().getInitParameter("idlService");
		if(StringUtils.isEmpty(port) || StringUtils.isEmpty(service)){
			return;
		}
		ServiceModel model = new ServiceModel();
		model.setServerIP(ServerUtil.getLocalNetWorkIp());
		model.setPort(Integer.parseInt(port));
		List<String> list = new ArrayList<String>();
		list.add(service);
		model.setIdlList(list);
		RegisterSvcWatcher watcher = new RegisterSvcWatcher(model);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, watcher);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (zookeeper != null) {
			zookeeper.stop();
		}
	}

}
