package com.cqfc.order.schedule.sport;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.util.OrderConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.ScanLog;

/**
 * 触发竞技彩未同步订单扫描事件
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class SportSyncScanJob {

	public ConcurrentMap<Integer, Boolean> scanInitMap = new ConcurrentHashMap<Integer, Boolean>();

	@Scheduled(cron = "0 0/1 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("------------竞技彩扫描未同步订单开始------------");
		try {
			int type = OrderConstant.ORDER_SYNC_CHECK;
			Boolean mapReturnValue = scanInitMap.putIfAbsent(type, true);
			if (null == mapReturnValue) {
				SportScanInit.putScan(scanInitMap, type);
			}
		} catch (Exception e) {
			ScanLog.scan.error("竞技彩扫描未同步订单发生异常", e);
		}
		ScanLog.scan.info("------------竞技彩扫描未同步订单结束------------");
	}
}
