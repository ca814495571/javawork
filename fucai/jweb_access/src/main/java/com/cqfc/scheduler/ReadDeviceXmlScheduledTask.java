package com.cqfc.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.util.DeviceHelper;

@EnableScheduling
@Service
public class ReadDeviceXmlScheduledTask {
    
    /**
     * 每隔一个小时读取device.xml文件，一旦发现更改，立即更新list
     */
//    @Scheduled(cron = "0 0 * * * *")
    public void task3() {
        DeviceHelper.initDevices();
    }
}
