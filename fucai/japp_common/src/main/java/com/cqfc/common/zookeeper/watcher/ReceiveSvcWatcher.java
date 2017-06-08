package com.cqfc.common.zookeeper.watcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.codehaus.jackson.map.ObjectMapper;

import com.cqfc.common.zookeeper.Server;
import com.cqfc.common.zookeeper.ServerCache;
import com.cqfc.common.zookeeper.ServerUtil;
import com.cqfc.common.zookeeper.ZooKeeperManager;
import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.data.ServiceModelUtil;
import com.jami.util.Log;

/**
 * 服务接收监听器 负责接收zookeeper通知，更新idl服务
 * 
 * @author HowKeyond
 *
 */
public class ReceiveSvcWatcher implements WatcherOpeartion,Watcher {

	// 监听动态服务器集群节点
	public static final String RECEIVE_CLUSTER_PATH = "/cqfc/clusterServers";
    // 监听配置节点
	public static final String RECEIVE_IDLCONFIG_PATH = "/cqfc/config/idlServices";
	// 人工配置的idl服务列表，读取/cqfc/config/idlServices节点下面的数据
	private static Map<String, List<Server>> configIdlMap = new HashMap<String, List<Server>>();
	// 动态集群上报服务，读取/cqfc/clusterServers节点下面的数据
	private static Map<String, List<Server>> clusterIdlMap = new HashMap<String, List<Server>>();
	
	@Override
	public void process(WatchedEvent event) {
        // 只负责观察子节点变化事件
		if(event.getType() == EventType.NodeChildrenChanged){
			Log.run.info("Receive Watcher  EventType.NodeChildrenChanged "+event.getPath());
			if(event.getPath().equals(RECEIVE_CLUSTER_PATH)){
				readClusterServices();
				updateServices();
			}else if(event.getPath().equals(RECEIVE_IDLCONFIG_PATH)){
				readConfigServices();
				updateServices();
			}
		}
		
	}

	/**
	 * 读取动态集群上报服务/cqfc/clusterServers节点下面的数据
	 */
	private void readClusterServices(){
		ZooKeeper zooKeeper = ZooKeeperManager.getZooKeeper();
		if(zooKeeper != null){
			try {
				// 从zookeeper中心获取/cqfc/clusterServers节点下面的idl服务列表
				List<String> childList = zooKeeper.getChildren(RECEIVE_CLUSTER_PATH, this);
				Log.run.info("childList "+childList);
				// 循环获取个节点数据
				// 将从zookeeper中心获取/cqfc/clusterServers节点下面的idl服务列表进行数据解析
				List<ServiceModel> serviceModelList = new ArrayList<ServiceModel>();
				for(String nodeName:childList){
					byte[] nodeData = zooKeeper.getData(RECEIVE_CLUSTER_PATH+"/"+nodeName, false, null);
					String serviceJson = new String(nodeData);
					ServiceModel serviceModel = ServiceModelUtil.parseServiceJson(serviceJson);
					serviceModelList.add(serviceModel);
				}
				// 将解析后的数据，转换成以idl服务为key，idl对应的服务器，port集合为value
				Map<String, List<Server>> serverMap = ServiceModelUtil.convertToServerMap(serviceModelList);
				Log.run.info("updateClusterServices "+serverMap);
				clusterIdlMap = serverMap;
			} catch (Exception e) {
				Log.run.error(e.getMessage(), e);
			}
		}
	}
	
	/** 
	 * 读取人工配置的idl服务列表/cqfc/config/idlServices节点下面的数据
	 */
	private void readConfigServices(){
		ZooKeeper zooKeeper = ZooKeeperManager.getZooKeeper();
		if(zooKeeper != null){
			try {
				// 从zookeeper中心获取/cqfc/clusterServers节点下面的idl服务列表
				List<String> childList = zooKeeper.getChildren(RECEIVE_IDLCONFIG_PATH, this);
				Log.run.info("childList "+childList);
				// 循环获取个节点数据
				// 将从zookeeper中心获取/cqfc/clusterServers节点下面的idl服务列表进行数据解析
				// 将解析后的数据格式为Map,idl服务为key，idl对应的服务器，port集合为value
				Map<String, List<Server>> serverMap = new HashMap<String, List<Server>>();
				for(String nodeName:childList){
					byte[] nodeData = zooKeeper.getData(RECEIVE_IDLCONFIG_PATH+"/"+nodeName, false, null);
					String ipPorts = new String(nodeData);
					List<Server> serverList =  ServerUtil.parseServerList(ipPorts);
					serverMap.put(nodeName, serverList);
					if (serverList == null){
						Log.run.warn("ipPorts format error,nodeName=%s, ipPorts=%s", nodeName, ipPorts);
					}
				}
				Log.run.info("updateConfigServices "+serverMap);
				configIdlMap = serverMap;
			} catch (Exception e) {
				Log.run.error(e.getMessage(), e);
			}
		}
	}
	
	/** 
	 * 更新可用的idl服务列表， /cqfc/config/idlServices 和/cqfc/clusterServers 节点下面idl服务交集
	 */
	private void updateServices() {
		Map<String, List<Server>> serverMap = new HashMap<String, List<Server>>();
		Set<String> idlKeySet = clusterIdlMap.keySet();
		// 将两个map中的idl服务比较，取交集，将idl下面的服务器配置比较，取交集
		for(String idlKey:idlKeySet){
			if(configIdlMap.containsKey(idlKey)){
				List<Server> clusterServerList = clusterIdlMap.get(idlKey);
				List<Server> configServerList = configIdlMap.get(idlKey);
				List<Server> serverList = null;
				for(Server clusterServer:clusterServerList){
					for(Server configServer:configServerList){
						if(clusterServer.getHost().equals(configServer.getHost())
								&& clusterServer.getPort() == configServer.getPort()){
							if(serverList == null){
								serverList = new ArrayList<Server>();
							}
							serverList.add(clusterServer);
						}
					}
				}
				if(serverList != null){
					serverMap.put(idlKey, serverList);
				}
			}
		}
		
		try{
//			ObjectMapper objMapper = new ObjectMapper();
//			String idlString = objMapper.writeValueAsString(serverMap);
//			Log.run.info("serverMap:"+idlString);  //日志太多，测试环境频繁重启，每次都会有这个日志打出，导致日志刷屏
		}catch(Exception e){
			Log.run.error(e.getMessage(), e);
		}

		ServerCache.setServerMap(serverMap);
	}

	@Override
	public void firstInvoker() {
		Log.run.info("Receive firstInvoker");
		readConfigServices();
		readClusterServices();
		updateServices();
	}
}
