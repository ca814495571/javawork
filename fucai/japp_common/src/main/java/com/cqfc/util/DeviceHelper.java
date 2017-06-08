package com.cqfc.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.cqfc.xmlparser.devices.Device;
import com.cqfc.xmlparser.devices.Devices;
import com.cqfc.xmlparser.devices.loader.TransactionDevicesLoader;

public class DeviceHelper {

	private static List<Device> list = new ArrayList<Device>();

	private static Map<String, Device> map = new HashMap<String, Device>();
	private static Map<String, Map<Integer, Device>> deviceMap = new HashMap<String, Map<Integer, Device>>();

	private static int ticketBusinessNum = 0;
	private static int queryTicketBusinessNum = 0;
	private static int otherBusinessNum = 0;

	/**
	 * 获取各模块的个数
	 * 
	 * @param moduleName
	 *            模块名
	 * @param moduleType
	 *            模块类型
	 * @return
	 */
	public static int getModuleNum(String moduleName, int moduleType) {
		getDevices();
		if (ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER.equals(moduleName)) {
			if (ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS == moduleType) {
				return ticketBusinessNum;
			} else if (ConstantsUtil.DEVICE_TYPE_QUERY_TICKET_BUSINESS == moduleType) {
				return queryTicketBusinessNum;
			} else {
				return otherBusinessNum;
			}
		}
		Map<Integer, Device> tmpMap = deviceMap.get(moduleName);
		return tmpMap == null ? 0 : tmpMap.size();
	}

	/**
	 * 获取模块名(或typeName)相应的id
	 * 
	 * @param moduleName
	 * @return
	 */
	public static List<String> getDeviceIds(String moduleName) {
		getDevices();
		List<String> ids = new ArrayList<String>();
		for (Device device : list) {
			if (device.getTypeName().equals(moduleName)) {
				ids.add(device.getId());
			}
		}

		return ids;
	}

	/**
	 * 初始化，读取devices.xml文件，将内容放入list和map
	 */
	public static void initDevices() {
		InputStream inputStream = null;
		try {
			String env = System.getProperty("cfg.env");
			if (StringUtils.isEmpty(env)) {
				env = "local";
			}
			inputStream = DeviceHelper.class.getClassLoader().getResourceAsStream("devices_" + env + ".xml");
			Devices devices = TransactionDevicesLoader.loadDevices(inputStream);
			list.clear();
			list.addAll(devices.getDevice());
			updateDeviceMap(list);
			// String xml = TransactionDevicesLoader.devices2xml(devices);
			// Log.run.debug("device=%s",xml);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据模块类型和下标生businessController的下标
	 * 
	 * @param moduleType
	 *            模块类型
	 * @param index
	 *            下标
	 * @return
	 */
	public static int getBusinessIndex(int moduleType, int index) {
		int businessIndex = moduleType * ConstantsUtil.MAX_BUSINESSCONTROLLER_NUM;
		return businessIndex + index;
	}

	private static void updateDeviceMap(List<Device> list) {
		ticketBusinessNum = 0;
		queryTicketBusinessNum = 0;
		otherBusinessNum = 0;
		String typeName = null;
		for (Device d : list) {
			typeName = d.getTypeName();
			map.put(d.getId(), d);
			if (!deviceMap.containsKey(typeName)) {
				deviceMap.put(typeName, new HashMap<Integer, Device>());
			}
			int deviceIndex = 0;
			if (d.getType() == ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS) {
				deviceIndex = getBusinessIndex(d.getType(), d.getServiceIndex());
				// ticketBusinessMap.put(d.getServiceIndex(), d);
				ticketBusinessNum++;
			} else if (d.getType() == ConstantsUtil.DEVICE_TYPE_QUERY_TICKET_BUSINESS) {
				deviceIndex = getBusinessIndex(d.getType(), d.getServiceIndex());
				queryTicketBusinessNum++;
			} else if (d.getType() == ConstantsUtil.DEVICE_TYPE_OTHER_BUSINESS) {
				deviceIndex = getBusinessIndex(d.getType(), d.getServiceIndex());
				// otherBusinessMap.put(d.getServiceIndex(), d);
				otherBusinessNum++;
			} else {
				deviceIndex = d.getServiceIndex();
			}
			deviceMap.get(typeName).put(deviceIndex, d);

		}
	}

	/**
	 * 获取devices
	 * 
	 * @return
	 */
	public static List<Device> getDevices() {
		synchronized (list) {
			if (list.size() == 0) {
				initDevices();
			}
		}
		return list;
	}

	/**
	 * 获取queueSize最小的device(1.如果device的typeName一致且其queueSize等于0，则立即返回;2.
	 * queueSize不能大于最大的size)
	 * 
	 * @param type
	 * @return
	 */
	public static Device getMinQueueSizeDevice(String typeName, int index) {
		getDevices();
		Device device = new Device();
		int minQueueSize = Integer.MAX_VALUE;
		Map<Integer, Device> tmpMap = deviceMap.get(typeName);
		if ((index == ConstantsUtil.ANY) || !(tmpMap.containsKey(index))) {
			for (Device d : tmpMap.values()) {
				if (d.getQueueSize() == 0) {
					return d;
				} else {
					if (d.getQueueSize() <= minQueueSize && d.getQueueSize() <= d.getThresholdSize()) {
						device = d;
						minQueueSize = device.getQueueSize();
					}
				}
			}
		} else {
			return tmpMap.get(index);
		}
		return device;
	}

	/**
	 * 根据id更新queueSize
	 * 
	 * @param id
	 * @param queueSize
	 */
	public static void updateDevice(int id, int queueSize) {
		/*
		 * if(map.get(id) != null){ map.get(id).setQueueSize(queueSize); }
		 */
	}

	/**
	 * 主动通知，更新list和map
	 * 
	 * @param notifyMap
	 */
	public static void notifyDevices(Map<String, Integer> notifyMap) {
		/*
		 * Set<Integer> ids = notifyMap.keySet(); for(int id : ids){
		 * updateDevice(id, notifyMap.get(id)); }
		 */
	}

}
