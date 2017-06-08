package com.cqfc.access.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DeviceHelper;
import com.cqfc.xmlparser.devices.Device;

public class TestDeviceHelper {

	public static void main(String[] args) {
		Device minQueueSizeDevice = DeviceHelper.getMinQueueSizeDevice(ConstantsUtil.DEVICE_TYPE_BUSINESS_CONTROLLER, ConstantsUtil.ANY);
		List<Device> list = DeviceHelper.getDevices();
		System.out.println("id -->" + minQueueSizeDevice.getId()
				+ "; queueSize --->" + minQueueSizeDevice.getQueueSize() + "; threshholdSize --->" + minQueueSizeDevice.getThresholdSize());
		int ret = 5;
		DeviceHelper.updateDevice(minQueueSizeDevice.getId(), ret);

		for (Device d : list) {
			System.out.println("id -->" + d.getId() + "; queueSize --->"
					+ d.getQueueSize());
		}

		Map<Integer, Integer> notifyMap = new HashMap<Integer, Integer>();
		notifyMap.put(1, 10);
		notifyMap.put(2, 15);
		notifyMap.put(23, 15);
		DeviceHelper.notifyDevices(notifyMap);

		for (Device d : list) {
			System.out.println("id -->" + d.getId() + "; queueSize --->"
					+ d.getQueueSize());
		}
		minQueueSizeDevice = DeviceHelper.getMinQueueSizeDevice(ConstantsUtil.DEVICE_TYPE_BUSINESS_CONTROLLER, ConstantsUtil.ANY);
		System.out.println("id -->" + minQueueSizeDevice.getId()
				+ "; queueSize --->" + minQueueSizeDevice.getQueueSize());
		minQueueSizeDevice = DeviceHelper.getMinQueueSizeDevice(ConstantsUtil.DEVICE_TYPE_BUSINESS_CONTROLLER, ConstantsUtil.ANY);
		System.out.println("id -->" + minQueueSizeDevice.getId()
				+ "; queueSize --->" + minQueueSizeDevice.getQueueSize());
	}
}
