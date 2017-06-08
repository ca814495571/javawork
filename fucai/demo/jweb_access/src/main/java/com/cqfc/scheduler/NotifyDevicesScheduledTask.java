package com.cqfc.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.businesscontroller.BusinessControllerService;
import com.cqfc.util.DeviceHelper;
import com.cqfc.xmlparser.devices.Device;
import com.jami.util.Log;

@EnableScheduling
@Service
public class NotifyDevicesScheduledTask {

	/**
	 * 流程： 1.取得各个Device目前的queueSize 2.通知DeviceHelper更新queueSize
	 **/
	public void execute() {
		Map<Integer, Integer> queueSizeMap = new HashMap<Integer, Integer>();
		int queueSize = 0;
		List<Device> devices = DeviceHelper.getDevices();

		for (Device d : devices) {
			queueSize = getQueueSizeFromClient(d);
			queueSizeMap.put(d.getId(), queueSize);
		}

		DeviceHelper.notifyDevices(queueSizeMap);
	}

	private int getQueueSizeFromClient(Device device) {
		int queueSize = 0;
		TTransport transport = null;
		
		try {
			transport = new TSocket(device.getIp(), device.getPort());
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);

			BusinessControllerService.Client javaClient = new BusinessControllerService.Client(
					protocol);
			queueSize = javaClient.getQueueSize();
		} catch (TException e) {
			Log.run.error("getQueueSizeFromClient have an Exception.error=%s", e.toString());
			Log.run.debug("", e);
		}  finally{
		   //关闭TTransport
		   transport.close();
		}	

		return queueSize;
	}

	/**
	 * 每隔十分钟，查询Set的queueSize,主动更新devices
	 */
	//@Scheduled(cron = "0 0/10 * * * *")
	public void task3() {
		Log.run.debug("getQueueSizeFromClient");
		execute();
	}
}
