package com.cqfc.user.order.task;

import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.user.order.service.IUserOrderService;
import com.cqfc.util.Pair;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.CountLog;
import com.jami.util.DataSourceContextHolder;
import com.jami.util.DataSourceUtil;
/**
 * 单独的线程去执行恢复queue中的order到数据库中
 * @author admin
 *
 */
public class StartQueueToDbTask implements Runnable{
	private List<Pair<File,BlockingQueue>> pairs;
	
	public StartQueueToDbTask(List<Pair<File,BlockingQueue>> pairs){
		this.pairs = pairs;
	}
	
	@Override
	public void run() {
		String dbName = "";
		RecoverOrderIndex index = null;
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		IUserOrderService userOrderService = applicationContext.getBean("userOrderServiceImpl", IUserOrderService.class);
		
		int flag = userOrderService.queueToDb(pairs);
		
		if(flag == 1){
			System.out.println("恢复日志文件中的订单入库成功...");
			for (Pair p : pairs) {
			//	DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE+DataSourceUtil.TempOrderDbName);
				File f = (File) p.first();
				dbName = f.getName().split("\\.")[0];
		//		index = userOrderService.getIndexByDbName(dbName);
				//将同步订单记录最后一次入库的锁打开,否则无法修改订单成功入库的最后一个订单号
		//		if(index!=null){
					DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.TempOrderDbName);
					flag = userOrderService.updateFlag(dbName, 0);
					if(flag != 1){
						
						CountLog.run.fatal(dbName+"修改标识为可修改失败,需要检查并重启。");
				}
	//			}
				
			}
		}else{
			System.out.println("恢复日志文件中的订单入库失败...");
		}
		
	}
	

}
