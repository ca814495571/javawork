package com.cqfc.order.schedule.digital;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.order.OrderService;
import com.cqfc.order.util.OrderDynamicUtil;
import com.cqfc.order.util.TaskDbGenerator;
import com.cqfc.util.DateUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 删除订单扫描
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class DeleteOrderScanJob {

	@Scheduled(cron = "0 0 04 * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("------------删除订单扫描操作开始------------");
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			OrderService orderService = ctx.getBean("orderService", OrderService.class);

			String currentTime = DateUtil.getDateTime("yyyy-MM-dd", new Date()) + " 00:00:00";
			String deleteTime = DateUtil.addDateMinut(currentTime, "DAY", -5);

			ScanLog.scan.debug("delete order,deleteTime=%s", deleteTime);
			for (int i = 0; i < OrderConstant.DATASOURCE_NUM; i++) {
				String dbName = TaskDbGenerator.getDbName(i);
				TaskDbGenerator.setDynamicDataSource(OrderDynamicUtil.MASTER, dbName);
				for (int j = 0; j < OrderConstant.PER_DATASOURCE_TABLE_NUM; j++) {
					String tableName = TaskDbGenerator.getTableName(j);
					int returnValue = orderService.deleteOrder(tableName, deleteTime);
					ScanLog.scan.debug("delete order,dbName=%s,tableName=%s,returnValue=%d", dbName, tableName,
							returnValue);
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("删除订单扫描发生异常", e);
		}
		ScanLog.scan.info("------------删除订单扫描操作结束------------");
	}
}
