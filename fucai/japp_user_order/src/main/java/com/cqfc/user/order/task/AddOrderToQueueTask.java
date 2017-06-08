package com.cqfc.user.order.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;

import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.protocol.userorder.Order;
import com.cqfc.user.order.service.IUserOrderService;
import com.cqfc.util.Pair;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.CountLog;
import com.jami.util.DataSourceContextHolder;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;

public class AddOrderToQueueTask implements Runnable{

	private Pair<File,BlockingQueue> pair;
	
	private CountDownLatch count;
	
	private AtomicBoolean atomicBoolean; 
	
	public  AddOrderToQueueTask(Pair<File,BlockingQueue> pair,CountDownLatch count,AtomicBoolean atomicBoolean) {
		
		this.pair = pair;
		this.count = count;
		this.atomicBoolean = atomicBoolean;
	}
	
	
	@Override
	public void run() {
		
		
		String dbName = "";
		
		try {
			
			ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
			IUserOrderService userOrderService = applicationContext.getBean("userOrderServiceImpl", IUserOrderService.class);
			
			File file = pair.first();
			dbName = file.getName().split("\\.")[0];
			
			DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE+DataSourceUtil.TempOrderDbName);
			RecoverOrderIndex index = userOrderService.getIndexByDbName(dbName);
			
			
			if(index!=null){
				
				String orderNo = index.getOrderNo();

				
				int flag = userOrderService.addOrderToQueue(pair, orderNo);
				
				if(flag ==1 ){
					CountLog.run.info("文件"+file.getName()+"日志文件订单入队列成功,订单数量："+pair.second().size());
					reverseQueue(pair.second());
				}else{
					CountLog.run.fatal("文件"+file.getName()+"日志文件订单入队列失败");
					atomicBoolean.set(false);
					return ;
				}
				
			}
			
			
		} catch (Exception e) {
			Log.run.error("日志恢复时,将日志中的信息放入队列出现异常",e);
			atomicBoolean.set(false);
			return ;
			
		}finally{
			
			count.countDown();
		}
		
	}

	
	public static void reverseQueue(BlockingQueue<Order> queue){
		
		List<Order> list =  new ArrayList<Order>();
		queue.drainTo(list);
		
		for (int i = list.size()-1; i >=0 ; i--) {
			
			queue.offer(list.get(i));
			
		}
	}
}
