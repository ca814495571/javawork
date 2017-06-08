package com.cqfc.prize.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cqfc.prize.service.Iservice.IDailyReportService;
import com.cqfc.prize.service.Iservice.IPrizeService;
import com.cqfc.prize.service.Iservice.ITempPrizeService;
import com.cqfc.util.DateUtil;

@RunWith(SpringJUnit4ClassRunner.class) //指定测试用例的运行器 这里是指定了Junit4  
@ContextConfiguration(locations={"file:src/main/webapp/WEB-INF/springmvc-dispatcher-servlet.xml"})
public class UnitTest extends AbstractJUnit4SpringContextTests{

	
	@Autowired
	private IPrizeService prizeService;
	
	
	@Autowired
	private ITempPrizeService tempPrizeService;
	
	@Autowired
	private IDailyReportService dailyReportService;
	
	@Test
	public void test(){
		
//			for (int i = 0; i < 20; i++) {
//				Prize prize = new Prize();
//				prize.setUserId("1");
//				prize.setTicketId(""+i);
//				prize.setWinAmount(1000);
//				prize.setStatus(1);
//				if(i%2==0){
//					prize.setStatus(2);
//				}
//				
//				System.out.println(prizeService.insert(prize)
//						);
//			}
		String countTime = DateUtil.getDateTime("yyyy-MM-dd", new Date());
		System.out.println(tempPrizeService.cleanTemp("2015-07-16"));
		
			
	}
	
}
