package com.cqfc.common.zookeeper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import com.jami.util.Log;

public class ZookeeperClient {
	// 监听配置节点
	public static final String RECEIVE_IDLCONFIG_PATH = "/cqfc/config/idlServices";
	public static final String RECEIVE_DBCONFIG_PATH = "/cqfc/config/dbconfig";
	// zookeeper连接相关配置
	public static final String ZOOKEEPER_IDL_ROUTE_FILE = "idl_route_${cfg.env}.properties";
	public static final String ZOOKEEPER_DB_ROUTE_FILE = "db_route_${cfg.env}.properties";

	public static void registIdlConfig(String service) {
		try {
			String env = System.getProperty("cfg.env");
			if (StringUtils.isEmpty(env)) {
				env = "dev";
				System.out
						.println("cfg.env parameter not found, use 'dev' as default.");
			}
			String idlRoute = System.getProperty("cfg.idlRoute");
			if (StringUtils.isEmpty(idlRoute)) {
				idlRoute = ZOOKEEPER_IDL_ROUTE_FILE.replace("${cfg.env}", env);
				System.out.println("cfg.idlRoute parameter not found, use file "
						+ idlRoute + " as default.");
			}
			Resource resource = new ClassPathResource(idlRoute);
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			ZooKeeper zooKeeper = ZooKeeperManager.getZooKeeper();
			Set<String> idlSet = new HashSet<String>();
			try {
				List<String> childList = zooKeeper.getChildren(
						RECEIVE_IDLCONFIG_PATH, false);
				idlSet.addAll(childList);
			} catch (Exception e) {
				Stat stat = zooKeeper.exists("/cqfc/config", false);
				if (stat == null) {
					zooKeeper.create("/cqfc/config", "config path".getBytes(),
							Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				zooKeeper.create(RECEIVE_IDLCONFIG_PATH,
						"idl config path".getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
			}
			// Stat stat = zooKeeper.exists(RECEIVE_IDLCONFIG_PATH, true);
			// if(stat == null){
			// stat = zooKeeper.exists("/cqfc/config", false);
			// if(stat == null){
			// zooKeeper.create("/cqfc/config", "idl config path".getBytes(),
			// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			// }
			// zooKeeper.create(RECEIVE_IDLCONFIG_PATH,
			// "idl config idl path".getBytes(), Ids.OPEN_ACL_UNSAFE,
			// CreateMode.PERSISTENT);
			// }
			Map<String, String> newNotes = new HashMap<String, String>();

			String nodeName = null;
			for (Entry<Object, Object> entry : props.entrySet()) {
				if (service != null) {
					if (!entry.getKey().toString().startsWith(service)) {
						continue;
					}
				}
				nodeName = entry.getKey().toString();
				String path = RECEIVE_IDLCONFIG_PATH + "/" + nodeName;
				// stat = zooKeeper.exists(path, true);
				if (idlSet.contains(nodeName)) {
					// 存在,则更新数据
					Stat stat = zooKeeper.setData(path, entry.getValue()
							.toString().getBytes(), -1);
					Log.run.info("register idl  exist " + stat);
				} else {
					newNotes.put(nodeName, entry.getValue().toString());
				}
			}

			// 如果没有结点新增，则删除最后一个结点再新增，从而触发结点变更事件
			if ((newNotes.size()) == 0 && (nodeName != null)) {
				zooKeeper.delete(RECEIVE_IDLCONFIG_PATH + "/" + nodeName, -1);
				newNotes.put(nodeName, props.getProperty(nodeName));
			}
			for (Entry<String, String> entry : newNotes.entrySet()) {
				// 不存在，则注册一个
				String pt = zooKeeper.create(RECEIVE_IDLCONFIG_PATH + "/"
						+ entry.getKey(), entry.getValue().getBytes(),
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				Log.run.info("register idl not exist " + pt);
			}
			System.out.println("regist idl config success. idl size="
					+ props.size());
		} catch (Exception e) {
			System.out.println("注册zookeper idl结点失败!");
			System.out.println(e.getMessage());
		}
	}

	public static void registDbConfig() {
		try {
			String env = System.getProperty("cfg.env");
			if (StringUtils.isEmpty(env)) {
				env = "dev";
				System.out
						.println("cfg.env parameter not found, use 'dev' as default.");
			}
			String dbRoute = System.getProperty("cfg.dbRoute");
			if (StringUtils.isEmpty(dbRoute)) {
				dbRoute = ZOOKEEPER_DB_ROUTE_FILE.replace("${cfg.env}", env);
				System.out.println("cfg.dbRoute parameter not found, use file "
						+ dbRoute + " as default.");
			}
			Resource resource = new ClassPathResource(dbRoute);
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			ZooKeeper zooKeeper = ZooKeeperManager.getZooKeeper();
			Set<String> dbSet = new HashSet<String>();
			try {
				List<String> childList = zooKeeper.getChildren(
						RECEIVE_DBCONFIG_PATH, false);
				dbSet.addAll(childList);
			} catch (Exception e) {
				Stat stat = zooKeeper.exists("/cqfc/config", false);
				if (stat == null) {
					zooKeeper.create("/cqfc/config", "config path".getBytes(),
							Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}
				zooKeeper.create(RECEIVE_DBCONFIG_PATH,
						"db config path".getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.PERSISTENT);
			}
			// Stat stat = zooKeeper.exists(RECEIVE_DBCONFIG_PATH, true);
			// if(stat == null){
			// stat = zooKeeper.exists("/cqfc/config", false);
			// if(stat == null){
			// zooKeeper.create("/cqfc/config", "idl config path".getBytes(),
			// Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			// }
			// zooKeeper.create(RECEIVE_DBCONFIG_PATH,
			// "idl config idl path".getBytes(), Ids.OPEN_ACL_UNSAFE,
			// CreateMode.PERSISTENT);
			// }
			Map<String, String> newNotes = new HashMap<String, String>();
			String nodeName = null;
			for (Entry<Object, Object> entry : props.entrySet()) {
				nodeName = entry.getKey().toString();
				String path = RECEIVE_DBCONFIG_PATH + "/" + nodeName;
				// stat = zooKeeper.exists(path, true);
				if (dbSet.contains(nodeName)) {
					// 存在,则更新数据
					Stat stat = zooKeeper.setData(path, entry.getValue()
							.toString().getBytes(), -1);
					Log.run.info("register db  exist " + stat);
				} else {
					newNotes.put(nodeName, entry.getValue().toString());
				}
			}

			// 如果没有结点新增，则删除最后一个结点再新增，从而触发结点变更事件
			if ((newNotes.size()) == 0 && (nodeName != null)) {
				zooKeeper.delete(RECEIVE_DBCONFIG_PATH  + "/" + nodeName, -1);
				newNotes.put(nodeName, props.getProperty(nodeName));
			}
			for (Entry<String, String> entry : newNotes.entrySet()) {
				// 不存在，则注册一个
				String pt = zooKeeper.create(RECEIVE_DBCONFIG_PATH + "/"
						+ entry.getKey(), entry.getValue().getBytes(),
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				Log.run.info("register db not exist " + pt);
			}
			System.out.println("regist db config success. db node size="
					+ props.size());
		} catch (Exception e) {
			System.out.println("注册zookeper db结点失败!");
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		String type = "idl";
		if(args.length > 0){
			type = args[0];
		}
		InitZooKeeperBean zookeeper = new InitZooKeeperBean();
		zookeeper.start();
		if (type.equals("idl")) {
			registIdlConfig(null);
		} else if (type.equals("db")) {
			registDbConfig();
		}else{
			System.out.println("regist type error, must be 'idl' or 'db'.");
		}
		zookeeper.stop();
	}
}
