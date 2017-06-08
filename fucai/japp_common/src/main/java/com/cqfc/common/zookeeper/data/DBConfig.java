package com.cqfc.common.zookeeper.data;

import java.util.List;

import com.cqfc.common.zookeeper.Server;

/**
 * 接收zookeeper注册的db服务的数据格式
 * 
 * 1. db服务注册在zookeeper的/cqfc/config/dbconfig节点下面，节点名称为数据库名称
 * 2. 节点数据格式为josn格式 {dbName:"cqfcdb",masterDB:{host:ip,port:port},slaveDB:[{host:ip,port:port},{host:ip,port:port},….]}
 * @author HowKeyond
 *
 */
public class DBConfig {

	// DB名称
	private String dbName;
	// DB主库
	private Server masterDB;
	// DB副库列表
	private List<Server> slaveDBList;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public Server getMasterDB() {
		return masterDB;
	}

	public void setMasterDB(Server masterDB) {
		this.masterDB = masterDB;
	}

	public List<Server> getSlaveDBList() {
		return slaveDBList;
	}

	public void setSlaveDBList(List<Server> slaveDBList) {
		this.slaveDBList = slaveDBList;
	}

}
