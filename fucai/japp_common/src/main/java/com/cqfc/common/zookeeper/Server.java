package com.cqfc.common.zookeeper;

/**
 * 服务器信息
 * @author HowKeyond
 *
 */
public class Server {

	/**
	 * 服务器ip
	 */
	private String host;
	
	/**
	 * 服务器端口
	 */
	private int port;
	
	/**
	 * 机器编码
	 */
	private String setNo = "";
	
	/**
	 * 超时时间
	 */
	private int timeOut = 3000; //默认3s

    public Server() {
    }

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public String getSetNo() {
		return setNo;
	}

	public void setSetNo(String setNo) {
		this.setNo = setNo;
	}

}
