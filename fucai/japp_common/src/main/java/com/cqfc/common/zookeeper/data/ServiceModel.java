package com.cqfc.common.zookeeper.data;

import java.util.List;

/**
 * 向zookeeper注册的idl服务的数据格式
 * 
 * 1. idl服务注册在zookeeper的/cqfc/nameservice节点下面,节点名称格式为：service_ip:port
 * 2. 数据格式为josn格式 {serverIp：xxx.xxx.xxx.xxx,port：xxx,idlList:[idl1,idl2,.....]}
 * 
 * @author HowKeyond
 *
 */
public class ServiceModel {

	private String serverIP;
	private int port;
	private String setNo;
	private List<String> idlList;
	
	public String getServerIP() {
		return serverIP;
	}
	
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public List<String> getIdlList() {
		return idlList;
	}
	
	public void setIdlList(List<String> idlList) {
		this.idlList = idlList;
	}

	public String getSetNo() {
		return setNo;
	}

	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}

}
