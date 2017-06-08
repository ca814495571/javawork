package com.cqfc.common.zookeeper;

import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.watcher.DBKeyValueWatcher;
import com.cqfc.common.zookeeper.watcher.ReceiveSvcWatcher;
import com.cqfc.common.zookeeper.watcher.RegisterSvcWatcher;
import com.jami.util.Log;

/**
 * 初始化ZooKeeper连接bean，用于web工程同步idl服务配置
 * 
 * @author HowKeyond
 * 
 */
public class InitZooKeeperBean implements InitializingBean, DisposableBean {

	private String port;
	private String service;
	private boolean hasDb = false;

	public void start() {
		// 启动ZooKeeper连接,监听配置文件
		Watcher receiveWatcher = new ReceiveSvcWatcher();
		ZooKeeperManager.connectZookeeper();
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_CLUSTER_PATH,
				WatcherType.Child, receiveWatcher);
		ZooKeeperManager.addWatcher(ReceiveSvcWatcher.RECEIVE_IDLCONFIG_PATH,
				WatcherType.Child, receiveWatcher);
		if (hasDb) {
			DBKeyValueWatcher dbWatcher = new DBKeyValueWatcher();
			ZooKeeperManager.addWatcher(
					DBKeyValueWatcher.RECEIVE_DBCONFIG_PATH, WatcherType.Child,
					dbWatcher);
		}
	}

	public void stop() {
		try {
			ZooKeeperManager.getZooKeeper().close();
		} catch (InterruptedException e) {
			Log.run.error(e.getMessage(), e);
		}
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return true;
	}

	private void initAfterStart() {
		if (StringUtils.isEmpty(port) || StringUtils.isEmpty(service)) {
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
	public void destroy() throws Exception {
		stop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		start();
		initAfterStart();
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setHasDb(boolean hasDb) {
		this.hasDb = hasDb;
	}

}
