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
public class PartnerDailyEncashCountTask {

	
	@Autowired
	private IPartnerOrderService partnerOrderService;
	
	@Scheduled(cron = "0 20 1 * * ?")
	public void executeDailyEncashCount() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		String countTime = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.getOffsetDate(new Date(), -1));
		
		
		Log.run.info("(暂时只有3类型有提现功能)3类型用户合作商日提现记录统计start...");
		int flag = -1;
		int i=1;
		while(i < 4 && flag!=1){
			
			flag  = partnerOrderService.partnerDailyEncashCount(countTime);
			if(flag != 1){
				Log.run.warn("3类型用户合作商日提现记录统计失败"+i+"次");
			
				if(i == 3){
					Log.error("3类型用户合作商日提现记录统计失败"+i+"次");
				}
				
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					Log.run.error(e);
				}
			}else{
				Log.run.info("3类型用户合作商日提现记录统计成功!!");
				break;
			}
			i++;
		}
		
	}
}
