package com.cqfc.common.zookeeper.watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.codehaus.jackson.map.ObjectMapper;

import com.cqfc.common.zookeeper.DBConfigCache;
import com.cqfc.common.zookeeper.ZooKeeperManager;
import com.cqfc.common.zookeeper.data.DBConfig;
import com.cqfc.common.zookeeper.data.ServiceModelUtil;
import com.jami.util.Log;

/**
 * 服务接收监听器 负责接收zookeeper通知，更新db配置
 * 
 * @author HowKeyond
 *
 */
public class DBConfigWatcher implements WatcherOpeartion,Watcher {

	// 监听DB配置节点
	public static final String RECEIVE_DBCONFIG_PATH = "/cqfc/config/dbconfig";
	
	
	@Override
	public void process(WatchedEvent event) {
        // 只负责观察子节点变化事件
		if(event.getType() == EventType.NodeChildrenChanged){
			Log.run.info("DbConfig Watcher  EventType.NodeChildrenChanged "+event.getPath());
			if(event.getPath().equals(RECEIVE_DBCONFIG_PATH)){
				updateDBConfig();
			}
		}
	}

	/**
	 * 读取动态集群上报服务/cqfc/clusterServers节点下面的数据
	 */
	private void updateDBConfig(){
		ZooKeeper zooKeeper = ZooKeeperManager.getZooKeeper();
		if(zooKeeper != null){
			try {
				// 从zookeeper中心获取/cqfc/config/dbconfig节点下面的idl服务列表
				List<String> childList = zooKeeper.getChildren(RECEIVE_DBCONFIG_PATH, this);
				Log.run.info("db childList "+childList);
				
				// 人工配置的db服务列表，读取/cqfc/config/dbconfig节点下面的数据,进行数据解析
				Map<String, DBConfig> dbConfigMap = new HashMap<String, DBConfig>();
				// 循环获取个节点数据
				for(String nodeName:childList){
					byte[] nodeData = zooKeeper.getData(RECEIVE_DBCONFIG_PATH+"/"+nodeName, false, null);
					String dbConfigJson = new String(nodeData);
					DBConfig dbConfig = ServiceModelUtil.parseDBConfigJson(dbConfigJson);
					dbConfigMap.put(nodeName, dbConfig);
				}
				
				try{
					ObjectMapper objMapper = new ObjectMapper();
					String dbConfigStr = objMapper.writeValueAsString(dbConfigMap);
					Log.run.info("dbConfigMap:"+dbConfigStr);
				}catch(Exception e){
					Log.run.error(e.getMessage(), e);
				}
				
				DBConfigCache.setDBConfigMap(dbConfigMap);
			} catch (Exception e) {
				Log.run.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void firstInvoker() {
		Log.run.info("DBConfigWatcher firstInvoker");
		updateDBConfig();
	}
}
