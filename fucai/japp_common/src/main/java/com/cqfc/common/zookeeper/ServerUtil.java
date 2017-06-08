package com.cqfc.common.zookeeper;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.jami.util.Log;

/**
 * 服务器相关工具类
 * @author HowKeyond
 *
 */
public class ServerUtil {
	public static final String SEPERATOR_OF_CLASS_METHOD = "_";
	/**
	 * 将服务器字符串配置，解析成列表，servers 格式为 ip:port,ip:port,.....
	 * @param servers
	 * @return
	 */
	public static List<Server> parseServerList(String servers){
		List<Server> serverList = new ArrayList<Server>();
		try{
			String[] strArray = servers.split(",");
			for(String str:strArray){
				String[] tmpArray = str.split(":");
				Server server = new Server();
				server.setHost(tmpArray[0]);
				server.setPort(Integer.parseInt(tmpArray[1]));
				serverList.add(server);
			}
		}catch(Exception e){
			Log.run.error("服务器配置解析错误！");
			Log.run.error(e.getMessage(), e);
			return null;
		}
		return serverList;
	}
	public static List<String> getIdlList(Class <?> clazz){
		Method[] methods = clazz.getMethods();
		String[] pkgs = clazz.getCanonicalName().split("\\.");
		String className = pkgs[pkgs.length-2];
		List<String> idls = new ArrayList<String>();
		for(int i=0;i<methods.length; i++){
			idls.add(className + "_" + methods[i].getName());
		}
		return idls;
	}
	/**
     * 获取本机的网络IP
     */
    public static String getLocalNetWorkIp() {
    	String localIp = null;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {// 遍历所有的网卡
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual() || ni.isPointToPoint()) {
                    continue;
                }
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress address = addresss.nextElement();
                    if (address instanceof Inet4Address 
                    		&& (address.getHostAddress().startsWith("10.")||address.getHostAddress().startsWith("192."))) {// 这里暂时只获取ipv4地址
                    	localIp = address.getHostAddress();
                        break;
                    }
                }
                if (localIp != null) {
                    break;
                }
            }

        } catch (Exception e) {
        	Log.run.error(e.getMessage(), e);
        }
        if (localIp == null) {
        	Log.run.error("Cannot get ");
        	localIp = "127.0.0.1";
        }
        return localIp;
    }
    
    public static void main(String[] args){
    	System.out.println(getLocalNetWorkIp());
    }
}
