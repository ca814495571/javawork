package com.cqfc.partner.order.schedual;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.partner.order.service.IPartnerOrderService;
import com.cqfc.util.DateUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;

@EnableScheduling
@Service
public class CleanTempOrderTask {

	@Autowired
	private IPartnerOrderService partnerOrderService;
	
	
	@Scheduled(cron = "0 0 4  * * ?")
	public void cleanTempOrder(){
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		Log.run.info("开始清理前五天的临时数字彩订单start.");
		String time = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", DateUtil.getOffsetDate(new Date(), -5));
		int flag = 1;
		int i=1;
		while(true){
			try {
			flag = partnerOrderService.cleanTempOrder(time,OrderStatus.LotteryType.NUMBER_GAME.getType());
			Log.run.info("清理前五天的临时数字彩订单数量:"+flag+"");
			if(flag== -100){
					
					Log.run.warn("清理前五天的临时数字彩订单失败"+i+"次");
					Thread.sleep(60000);
					if(i == 3){
						Log.error("清理前五天的临时数字彩订单失败"+i+"次");
						break;
					}
					i++;
					
			}else if(flag == 0){
				
				Log.run.info("清理前五天的临时数字彩订单成功!");
				break;
			}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.run.error(e);
			}
			
		}
	}
	
	@Scheduled(cron = "0 30 4  * * ?")
	public void cleanTempJcOrder(){
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		Log.run.info("开始清理前十天的临时竞技彩订单start.");
		String time = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", DateUtil.getOffsetDate(new Date(), -10));
		int flag = 1;
		int i=1;
		while(true){
			try {
			flag = partnerOrderService.cleanTempOrder(time,OrderStatus.LotteryType.SPORTS_GAME.getType());
			Log.run.info("清理前十天的临时竞技彩订单数量:"+flag+"");
			if(flag== -100){
					
					Log.run.warn("清理前十天的临时竞技彩订单失败"+i+"次");
					Thread.sleep(60000);
					if(i == 3){
						Log.error("清理前十天的临时竞技彩订单失败"+i+"次");
						break;
					}
					i++;
					
			}else if(flag == 0){
				
				Log.run.info("清理前十天的临时竞技彩订单成功!");
				break;
			}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.run.error(e);
			}
			
		}
	}
	
	@Scheduled(cron = "0 10 4  * * ?")
	public void cleanTempJcOrderDetail(){
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		Log.run.info("开始清理前十天的临时竞技彩订单详情start.");
		String time = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", DateUtil.getOffsetDate(new Date(), -10));
		int flag = 1;
		int i=1;
		while(true){
			try {
			flag = partnerOrderService.cleanTempJcOrderDetail(time);
			Log.run.info("清理前十天的临时竞技彩订单数量:"+flag+"");
			if(flag== -100){
					
					Log.run.warn("清理前十天的临时竞技彩订单失败"+i+"次");
					Thread.sleep(60000);
					if(i == 3){
						Log.error("清理前十天的临时竞技彩订单失败"+i+"次");
						break;
					}
					i++;
					
			}else if(flag == 0){
				
				Log.run.info("清理前十天的临时订单成功!");
				break;
			}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Log.run.error(e);
			}
			
		}
	}
}
