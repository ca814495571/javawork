package com.cqfc.management.util.scheduledTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.management.service.IFuserService;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;

@EnableScheduling
@Service
public class ClearOperateLogTask {
	
	@Autowired
	private IFuserService fuserService;
	
	
	@Scheduled(cron = "0 0 0 1 1/3 ?")//从1月开始每隔三个月(1,4,7,10)的1号0点执行
	public void clearOperateLog(){
		if(!SwitchUtil.timerIsOpen()){
			return;
		}
		Log.run.info("开始清理90天前的操作日志");
		
		int flag = 1;
		int i =1;
		while(true){
			try {
				flag = fuserService.deleteOperate();
				Log.run.info("清理90天前的操作日志:"+flag+"");
				if(flag == -100){
					Thread.sleep(60000);
					i++;
					if(i==4){
						break;
					}
				}else if(flag==0){
					Log.run.info("清理90天前的操作日志成功:"+flag);
					break;
				}
			} catch (InterruptedException e) {
				Log.run.error(e);
			}
		}
	}
}
