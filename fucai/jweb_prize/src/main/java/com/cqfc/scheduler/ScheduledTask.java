package com.cqfc.scheduler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.prize.service.Iservice.ITempPrizeService;
import com.cqfc.util.DateUtil;
import com.jami.util.Log;


@EnableScheduling
@Service
public class ScheduledTask {

	@Autowired
	private ITempPrizeService tempPrizeService;
	
	
	@Scheduled(cron = "0 0 3  * * ?")
	public void staticsDailyReport(){
		
		String countTime = DateUtil.getDateTime("yyyy-MM-dd", new Date());
		
		boolean flag = false;
		int i=1;
		
		while(true){
		
			flag = tempPrizeService.dailyReportCount(countTime);
			if(!flag){
				Log.run.warn("时间--" + countTime + "生成日报失败"+i+"次");
				
				if(i == 3){
					Log.error("时间--" + countTime + "生成日报失败"+i+"次");
					break;
				}
				try {
					i++;
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					Log.run.error(e);
				}
			}else{
				Log.run.info("时间--" + countTime + "生成日报成功!!");
				break;
			}
		}
	}
	
	@Scheduled(cron = "0 0 4  * * ?")
	public void cleanTempPrize(){
		
		String time = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", DateUtil.getOffsetDate(new Date(), -5));
		int flag = 1;
		int i=1;
		while(true){
			try {
			flag = tempPrizeService.cleanTemp(time);
			if(flag== -100){
					
					Log.run.warn("清理前五天临时表数据失败"+i+"次");
					Thread.sleep(60000);
					if(i == 3){
						Log.error("清理前五天临时表数据失败"+i+"次");
						break;
					}
					i++;
					
			}else if(flag == 0){
				
				Log.run.info("清理前五天临时表数据成功!");
				break;
			}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.run.error(e);
			}
			
		}
		
		
	}
    
    
    
    
    
}
