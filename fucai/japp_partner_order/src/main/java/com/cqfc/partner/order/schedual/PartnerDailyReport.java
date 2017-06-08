package com.cqfc.partner.order.schedual;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.partner.order.service.IPartnerOrderService;
import com.cqfc.util.DateUtil;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;
@EnableScheduling
@Service
public class PartnerDailyReport {

	

	public static String countTime = "";
	
	@Autowired
	private IPartnerOrderService partnerOrderService;
	//格式: [秒] [分] [小时] [日] [月] [周] [年]* 表示所有值. 例如:在分的字段上设置 "*",表示每一分钟都会触发。? 表示不指定值。使用的场景为不需要关心当前设置这个字段的值
	@Scheduled(cron = "0 30 6 * * ?")
	public void executeDailySaleCount() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		int flag = -1;
		int i=1;
		
		countTime = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.getOffsetDate(new Date(), -1));
		while(i < 4 && flag!=1){
		
			flag = partnerOrderService.partnerDailyReport(countTime);
			if(flag != 1){
				Log.run.warn("时间--" + countTime + "生成日报失败"+i+"次");
				
				if(i ==3){
					Log.error("时间--" + countTime + "生成日报失败"+i+"次");
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					Log.run.error(e);
				}
			}else{
				Log.run.info("时间--" + countTime + "生成日报成功!!");
				break;
			}
			i++;
		}
	}
}
