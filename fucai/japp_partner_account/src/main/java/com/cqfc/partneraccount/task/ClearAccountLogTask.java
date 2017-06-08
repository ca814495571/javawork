package com.cqfc.partneraccount.task;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cqfc.partneraccount.dao.PartnerAccountDao;
import com.cqfc.util.DateUtil;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;
@Component
public class ClearAccountLogTask {
	@Resource
	private PartnerAccountDao partnerAccountDao;
	
	@Scheduled(cron = "0 30 1 * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		Log.run.info("开始清理30天的流水日志.");
		String time = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", DateUtil.getOffsetDate(new Date(), -30));
		int num = 1;
		int i=1;
		while(true){
			try {
			num = partnerAccountDao.clearAccountLog(time);
			Log.run.info("清理前30天的流水日志:"+num+"");
			if(num== -100){
					
					Log.run.info("清理前30天的流水日志失败"+i+"次");
					Thread.sleep(60000);
					i++;
					if(i == 4){
						break;
					}
					
			}else if(num == 0){
				
				Log.run.info("30天的流水日志成功!");
				break;
			}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.run.error(e);
			}
			
		}
	}
}
