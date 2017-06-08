package com.cqfc.common.zookeeper.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cqfc.common.zookeeper.Server;
import com.jami.util.Log;

/**
 * 负责组装和解析向zookeeper注册的idl服务数据格式
 * 1. idl服务注册在zookeeper的/cqfc/nameservice节点下面
 * 2. 数据格式为josn格式 {serverIp：xxx.xxx.xxx.xxx,port：xxx,idlList:[idl1,idl2,.....]}
 * @author HowKeyond
 *
 */
public class ServiceModelUtil {

	/**
	 * 将idl服务组装成json格式
	 * @return
	 */
	public static String builderServiceJson(ServiceModel serviceModel) {
		return JSONObject.toJSONString(serviceModel);
	}
	
	/**
	 * 将idl配置服务解析成对象
	 * @return
	 */
	public static ServiceModel parseServiceJson(String serviceJson) {
		ServiceModel serviceModel = null;
		try{
			serviceModel = JSONObject.parseObject(serviceJson, ServiceModel.class);
		}catch(Exception e){
			Log.run.error(e.getMessage(), e);
		}
		return serviceModel;
	}
	
	/**
	 * 将Server，下面挂idl服务List集合，转换成功idl为key，Server集合为Value的map
	 * @param serviceModelList
	 * @return
	 */
	public static Map<String,List<Server>> convertToServerMap(List<ServiceModel> serviceModelList){
		Map<String, List<Server>> serverMap = new HashMap<String, List<Server>>();
		for(ServiceModel serviceModel:serviceModelList){
			Server server = new Server(serviceModel.getServerIP(),serviceModel.getPort());
			server.setSetNo(serviceModel.getSetNo());
			List<String> idlList = serviceModel.getIdlList();
			if(idlList != null){
				for(String idl:idlList){
					List<Server> serverList = serverMap.get(idl);
					if(serverList == null){
						serverList = new ArrayList<Server>();
						serverMap.put(idl, serverList);
					}
					if(!serverList.contains(server)){
						serverList.add(server);
					}
				}
			}
		}
		return serverMap;
	}
	
	/**
	 * 将db配置服务组解析成DBConfig对象
	 * @return
	 */
	public static DBConfig parseDBConfigJson(String dbConfigJson) {
		DBConfig config = new DBConfig();
		config = JSONObject.parseObject(dbConfigJson, DBConfig.class);
		return config;
	}
	
	public static void main(String[] args)throws Exception{
		System.out.println("--------builderServiceJson test---------");
		ServiceModel serviceModel = new ServiceModel();
		serviceModel.setServerIP("192.168.254.212");
		serviceModel.setPort(88888);
		List<String> idlList= new ArrayList<String>();
		idlList.add(String.class.getName());
		idlList.add(Long.class.getName());
		idlList.add(Double.class.getName());
		serviceModel.setIdlList(idlList);
		String serviceJson = builderServiceJson(serviceModel);
		System.out.println(serviceJson);
		
		System.out.println("--------parseServiceJson test---------");
		ServiceModel serviceModel2 = parseServiceJson(serviceJson);
		System.out.println("serviceIP:"+serviceModel2.getServerIP());
		System.out.println("port:"+serviceModel2.getPort());
		System.out.println("idlList:");
		for(String str:serviceModel2.getIdlList()){
			System.out.println(str);
		}
		
		System.out.println("--------convertToServerMap test---------");
		List<ServiceModel> modelList =new ArrayList<ServiceModel>();
		for(int i=0;i<10;i++){
			ServiceModel model = new ServiceModel();
			model.setServerIP("192.168.254.21"+i);
			model.setPort(88880+i);
			List<String> idlLs= new ArrayList<String>();
			idlLs.add(String.class.getName());
			idlLs.add(Long.class.getName());
			idlLs.add(Double.class.getName());
			model.setIdlList(idlLs);
			modelList.add(model);
		}
		Map<String,List<Server>> map = convertToServerMap(modelList);
		for(String str:map.keySet()){
			System.out.println("------------idl "+str);
			List<Server> list = map.get(str);
			for(Server serv:list){
				System.out.println(serv.getHost()+" : "+serv.getPort());
			}
		}
		String dbConfigJson = "{dbName:\"cqfcdb\",masterDB:{host:\"192.168.1.1\",port:3306},slaveDBList:[{host:\"192.168.1.2\",port:3306},{host:\"192.168.1.3\",port:3306}]}";
		DBConfig dbConfig = parseDBConfigJson(dbConfigJson);
		System.out.println(dbConfig);
	}
}
