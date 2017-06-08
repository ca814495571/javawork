package com.cqfc.common.zookeeper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback.ACLCallback;
import org.apache.zookeeper.AsyncCallback.Children2Callback;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import com.cqfc.common.zookeeper.watcher.WatcherOpeartion;
import com.jami.util.Log;

/**
 * ZooKeeper连接服务管理
 * @author HowKeyond
 *
 */
public class ZooKeeperManager {

	// ZooKeeper对象
	private static ZooKeeper zooKeeper;
	// ZooKeeper连接字符串
	private static String connectString;
	// session超时时间
	private static int sessionTimeout = 3000;
	private static CountDownLatch latch;
	private static SessionWatcher sessionWatcher;
	private static Map<String, Set<Watcher>> nodeWatches = new HashMap<String, Set<Watcher>>();
	private static Map<String, Set<Watcher>> existWatches = new HashMap<String, Set<Watcher>>();
	private static Map<String, Set<Watcher>> childWatches = new HashMap<String, Set<Watcher>>();
	// 重庆福彩zookeeper根节点
	public static final String ZOOKEEPER_ROOT_PATH = "/cqfc";
	// zookeeper节点,存储动态集群服务列表
	public static final String REGISTER_CLUSTER_PATH = "/cqfc/clusterServers";
	// zookeeper连接相关配置
    public static final String ZOOKEEPER_CONFIG_FILE = "zkcfg_${cfg.env}.properties";	
	
	/**
	 * 读取项目classes目录下的cfg_${cfg.env}.properties
	 * 文件中的 connectString 和 sessionTimeout 配置参数
	 */
	private static void readZooKeeperConfig() {
		try{
			String env = System.getProperty("cfg.env");
			if(StringUtils.isEmpty(env)){
				env = System.getenv("cfg.env");
			}
			if (StringUtils.isEmpty(env)){
				env = "local";
			}
			String zkConfFile = ZOOKEEPER_CONFIG_FILE.replace("${cfg.env}", env);
			Resource resource = new ClassPathResource(zkConfFile);
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			connectString = props.getProperty("zookeeperConnectString");
			sessionTimeout = Integer.parseInt(props.getProperty("zookeeperSessionTimeout"));
		}catch(Exception e){
			Log.run.error("读取zookeper连接配置失败!");
			Log.run.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 初始化连接Zookeeper
	 */
	public static void connectZookeeper() {
		// 读取connectString和sessionTimeout配置
		readZooKeeperConfig();
		// 连接ZooKeeper服务
		reconnectZookeeper(); 
	}
	
	/**
	 * 重新连接ZooKeeper服务
	 */
	private static void reconnectZookeeper() {
		if(zooKeeper == null){
			synchronized(ZooKeeperManager.class){
				if(zooKeeper == null){
					try{
						latch = new CountDownLatch(1);
						sessionWatcher = new SessionWatcher();
						zooKeeper = new ZooKeeper(connectString, sessionTimeout, sessionWatcher);
						// 同步等待zookeeper连接上
						latch.await();
						Log.run.info("zooKeeper is created");
						// 判断根节点是否存在，如果不存在，自动创建一个
						Stat stat = zooKeeper.exists(ZOOKEEPER_ROOT_PATH, false);
						if(stat == null){
							zooKeeper.create(ZOOKEEPER_ROOT_PATH, "root path".getBytes(),  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
						}
						stat = zooKeeper.exists(REGISTER_CLUSTER_PATH, false);
						if(stat == null){
							zooKeeper.create(REGISTER_CLUSTER_PATH, "idl service path".getBytes(),  Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
						}
					}catch(Exception e){
						Log.run.error("连接ZooKeeper产生异常");
						Log.run.error(e.getMessage(), e);
					}
				}
			}
		}else{
			Log.run.info("ZooKeeper 已经初始化，不能重复初始化！");
		}
	}
	
	/**
	 * 添加Watcher
	 * @param watchPath
	 * @param watchType
	 * @param watcher
	 */
	public static void addWatcher(String watchPath, WatcherType watchType,Watcher watcher){
		switch(watchType){
		case Exist :
			addExistWatcher(watchPath, watcher);
			break;
		case Node :
			addNodeWatcher(watchPath, watcher);
			break;
		case Child :
			addChildWatcher(watchPath, watcher);
			break;
		}
	}
	
	/**
	 * 添加Exist Watcher
	 * @param watchPath
	 * @param watchType
	 * @param watcher
	 */
	private static void addExistWatcher(String watchPath, Watcher watcher){
		Set<Watcher> watcherSet = existWatches.get(watchPath);
		if(watcherSet == null){
			watcherSet = new HashSet<Watcher>();
			existWatches.put(watchPath, watcherSet);
		}
		watcherSet.add(watcher);
		try{
			zooKeeper.exists(watchPath, watcher);
			if(watcher instanceof WatcherOpeartion){
				WatcherOpeartion op = (WatcherOpeartion)watcher;
				op.firstInvoker();
			}
		}catch(KeeperException e){
			Log.run.error(e.getMessage(), e);
		}catch(InterruptedException e){
			Log.run.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 添加Data Watcher
	 * @param watchPath
	 * @param watchType
	 * @param watcher
	 */
	private static void addNodeWatcher(String watchPath, Watcher watcher){
		Set<Watcher> watcherSet = nodeWatches.get(watchPath);
		if(watcherSet == null){
			watcherSet = new HashSet<Watcher>();
			nodeWatches.put(watchPath, watcherSet);
		}
		watcherSet.add(watcher);
		try{
			zooKeeper.getData(watchPath, watcher, null);
			if(watcher instanceof WatcherOpeartion){
				WatcherOpeartion op = (WatcherOpeartion)watcher;
				op.firstInvoker();
			}
		}catch(KeeperException e){
			Log.run.error(e.getMessage(), e);
		}catch(InterruptedException e){
			Log.run.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 添加Data Watcher
	 * @param watchPath
	 * @param watchType
	 * @param watcher
	 */
	private static void addChildWatcher(String watchPath, Watcher watcher){
		Set<Watcher> watcherSet = childWatches.get(watchPath);
		if(watcherSet == null){
			watcherSet = new HashSet<Watcher>();
			childWatches.put(watchPath, watcherSet);
		}
		watcherSet.add(watcher);
		try{
			zooKeeper.getChildren(watchPath, watcher);
			if(watcher instanceof WatcherOpeartion){
				WatcherOpeartion op = (WatcherOpeartion)watcher;
				op.firstInvoker();
			}
		}catch(KeeperException e){
			Log.run.error(e.getMessage(), e);
		}catch(InterruptedException e){
			Log.run.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 注册公共Watcher服务
	 */
    private static void registerWatcher(){
		registerExistWatcher();
		registerNodeWatcher();
		registerChildWatcher();
	}
	
	/**
	 * 注册Exist Watcher
	 */
	private static void registerExistWatcher(){
		Set<String> existKeySet = existWatches.keySet();
		for(String path:existKeySet) {
			Set<Watcher> watcherSet = existWatches.get(path);
			for(Watcher watcher:watcherSet){
				try{
					zooKeeper.exists(path, watcher);
					if(watcher instanceof WatcherOpeartion){
						WatcherOpeartion op = (WatcherOpeartion)watcher;
						op.firstInvoker();
					}
				}catch(KeeperException e){
					Log.run.error(e.getMessage(), e);
				}catch(InterruptedException e){
					Log.run.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 注册Data Watcher
	 */
	private static void registerNodeWatcher(){
		Set<String> dataKeySet = nodeWatches.keySet();
		for(String path:dataKeySet) {
			Set<Watcher> watcherSet = nodeWatches.get(path);
			for(Watcher watcher:watcherSet){
				try{
					zooKeeper.getData(path, watcher, null);
					if(watcher instanceof WatcherOpeartion){
						WatcherOpeartion op = (WatcherOpeartion)watcher;
						op.firstInvoker();
					}
				}catch(KeeperException e){
					Log.run.error(e.getMessage(), e);
				}catch(InterruptedException e){
					Log.run.error(e.getMessage(), e);
				}
			}
		}	
	}
	
	/**
	 * 注册Child Watcher
	 */
	private static void registerChildWatcher(){
		Set<String> childKeySet = childWatches.keySet();
		for(String path:childKeySet) {
			Set<Watcher> watcherSet = childWatches.get(path);
			for(Watcher watcher:watcherSet){
				try{
					zooKeeper.getChildren(path, watcher);
					if(watcher instanceof WatcherOpeartion){
						WatcherOpeartion op = (WatcherOpeartion)watcher;
						op.firstInvoker();
					}
				}catch(KeeperException e){
					Log.run.error(e.getMessage(), e);
				}catch(InterruptedException e){
					Log.run.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 关闭ZooKeeper连接
	 */
	public static void close(){
		if(zooKeeper != null){
			try{
				zooKeeper.close();
			}catch(InterruptedException e){
				Log.run.error(e.getMessage(), e);
			}
			zooKeeper = null;
		}		
	}
	
	static class SessionWatcher implements Watcher{
		@Override
		public void process(WatchedEvent event) {
			KeeperState keeperState = event.getState(); // 事件类型
			EventType eventType = event.getType();
			Log.run.info("RootWatcher KeeperState :"+keeperState);
			Log.run.info("RootWatcher EventType :"+eventType);
			// 处理连接成功事件
			if (KeeperState.SyncConnected == keeperState) {
				// 成功连接上ZK服务器
				if (EventType.None == eventType) {
					Log.run.info("SessionWatcher zookeeper connect is ok!");
					if(latch != null){
						latch.countDown();
					}
					registerWatcher();
				}
			}else if(KeeperState.Expired == keeperState){
				// 处理session过期事件
				Log.run.info("RootWatcher Expired :"+keeperState);
				// 关闭ZooKeeper连接
				close();
				// 重新创建ZooKeeper连接
				reconnectZookeeper();
			}else if(KeeperState.AuthFailed == keeperState){
				Log.run.info("RootWatcher AuthFailed :"+keeperState);
			}else if(KeeperState.Disconnected == keeperState){
				Log.run.info("RootWatcher Disconnected :"+keeperState);
			}else if(KeeperState.ConnectedReadOnly == keeperState){
				Log.run.info("RootWatcher ConnectedReadOnly :"+keeperState);
			}else if(KeeperState.SaslAuthenticated == keeperState){
				Log.run.info("RootWatcher SaslAuthenticated :"+keeperState);
			}
		}
	};
    
	public static ZooKeeper getZooKeeper(){
		return zooKeeper;
	}
	
	public static String create(String path, byte[] data, List<ACL> acl, CreateMode createMode)throws KeeperException, InterruptedException {
		return zooKeeper.create(path, data, acl, createMode);
	}
	
    public static void create(String path, byte[] data, List<ACL> acl, CreateMode createMode, StringCallback cb, Object ctx) {
    	zooKeeper.create(path, data, acl, createMode, cb, ctx);
    }
    
    public static void delete(String path, int version) throws InterruptedException, KeeperException {
    	zooKeeper.delete(path, version);
    }
    
    public static void delete(String path, int version, VoidCallback cb, Object ctx) {
    	zooKeeper.delete(path, version, cb, ctx);
    }
    
    public static Stat exists(String path, Watcher watcher) throws KeeperException, InterruptedException {
    	return zooKeeper.exists(path, watcher);
    }
    
    public static Stat exists(String path, boolean watch) throws KeeperException, InterruptedException {
    	return zooKeeper.exists(path, watch);
    }
    
    public static void exists(String path, Watcher watcher, StatCallback cb, Object ctx) {
    	zooKeeper.exists(path, watcher, cb, ctx);
    }
    
    public static void exists(String path, boolean watch, StatCallback cb, Object ctx) {
    	zooKeeper.exists(path, watch, cb, ctx);
    }
    
    public static List<ACL> getACL(String path, Stat stat) throws KeeperException, InterruptedException {
    	return zooKeeper.getACL(path, stat);
    }
    
    public static void getACL(String path, Stat stat, ACLCallback cb, Object ctx) {
    	zooKeeper.getACL(path, stat, cb, ctx);
    }
    
    public static List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException {
    	return zooKeeper.getChildren(path, watch);
    }
    
    public static List<String> getChildren(String path, Watcher watcher) throws KeeperException, InterruptedException {
    	return zooKeeper.getChildren(path, watcher);
    }
    
    public static List<String> getChildren(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException {
    	return zooKeeper.getChildren(path, watch, stat);
    }
    
    public static List<String> getChildren(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException {
    	return zooKeeper.getChildren(path, watcher, stat);
    }
    
    public static void getChildren(String path, boolean watch, Children2Callback cb, Object ctx) {
    	zooKeeper.getChildren(path, watch, cb, ctx);
    }
    
    public static void getChildren(String path, boolean watch, ChildrenCallback cb, Object ctx) {
    	zooKeeper.getChildren(path, watch, cb, ctx);
    }
    
    public static void getChildren(String path, Watcher watcher, Children2Callback cb, Object ctx) {
    	zooKeeper.getChildren(path, watcher, cb, ctx);
    }
    
    public static void getChildren(String path, Watcher watcher, ChildrenCallback cb, Object ctx) {
    	zooKeeper.getChildren(path, watcher, cb, ctx);
    }
    
    public static byte[] getData(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException {
    	return zooKeeper.getData(path, watcher, stat);
    }
    
    public static byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException {
    	return zooKeeper.getData(path, watch, stat);
    }
    
    public static void getData(String path, boolean watch, DataCallback cb, Object ctx) {
    	zooKeeper.getData(path, watch, cb, ctx);
    }
    
    public static void getData(String path, Watcher watcher, DataCallback cb, Object ctx) {
    	zooKeeper.getData(path, watcher, cb, ctx);
    }
    
    public static long getSessionId() {
    	return zooKeeper.getSessionId();
    }
    
    public static States getState() {
    	return zooKeeper.getState();
    } 
    
    public static  Stat setACL(String path, List<ACL> acl, int version) throws KeeperException, InterruptedException  {
    	return zooKeeper.setACL(path, acl, version);
    } 
    
    public static void setACL(String path, List<ACL> acl, int version, StatCallback cb, Object ctx)  {
    	zooKeeper.setACL(path, acl, version, cb, ctx);
    } 
    
    public static Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException  {
    	return zooKeeper.setData(path, data, version);
    } 
    
    public static void setData(String path, byte[] data, int version, StatCallback cb, Object ctx) {
    	zooKeeper.setData(path, data, version, cb, ctx);
    } 
}
