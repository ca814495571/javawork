package com.cqfc.common.zookeeper.watcher;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.cqfc.common.zookeeper.ZooKeeperManager;
import com.cqfc.common.zookeeper.data.ServiceModel;
import com.cqfc.common.zookeeper.data.ServiceModelUtil;
import com.jami.util.Log;

/**
 * 服务注册监听器
 * 负责向zookeeper注册自己的idl服务
 * @author HowKeyond
 *
 */
public class RegisterSvcWatcher implements WatcherOpeartion, Watcher{

	// 监听动态服务器集群节点
	public static final String REGISTER_CLUSTER_PATH = "/cqfc/clusterServers";
	private ServiceModel serviceModel;
	
	public RegisterSvcWatcher(ServiceModel serviceModel) {
		this.serviceModel = serviceModel;
	}
	
	@Override
	public void process(WatchedEvent event) {		
		// 只处理被观察节点被删除事件，同时ZooKeeper处于连接状态,其他断开连接和session过期事件统一由SessionWatcher处理
		if(event.getState() == KeeperState.SyncConnected 
				&& event.getType() == EventType.NodeDeleted){
			Log.run.info("Register Watcher EventType:"+event.getType()+" EventPath:"+event.getPath());
			registerService();
		}
	}
	
	/**
	 * 向zookeeper注册idl服务
	 * 
	 */
	private void registerService(){
		ZooKeeper zooKeeper = ZooKeeperManager.getZooKeeper();
		if(zooKeeper != null){
			String serviceJson = ServiceModelUtil.builderServiceJson(this.serviceModel);
			try{
				zooKeeper = ZooKeeperManager.getZooKeeper();
				String path = REGISTER_CLUSTER_PATH+"/service_"+serviceModel.getServerIP()+":"+serviceModel.getPort();
				Stat stat = zooKeeper.exists(path, false);
				if(stat == null){
					// 不存在，则注册一个
					String pt = zooKeeper.create(path, serviceJson.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
					Log.run.info("registerService not exist "+pt);
				}else{
					// 存在,则更新数据
					Log.run.info("registerService  exist "+stat);
					stat = zooKeeper.setData(path, serviceJson.getBytes(), -1);
				}
				// 注册RegisterSvcWatcher,只观察自己注册的节点变化
				zooKeeper.exists(path, this);
			}catch(KeeperException e){
				Log.run.error(e.getMessage(), e);
			}catch(InterruptedException e){
				Log.run.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void firstInvoker() {
		Log.run.info("Register firstInvoker");
		registerService();
	}
}
