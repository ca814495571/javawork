package com.cqfc.lottery.schedule.issuestate;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.DateUtil;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 触发出票中订单扫描事件 (job条件： 1.期状态为销售中 2.时间在出票时间范围内)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class TicketPrintJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------扫描出票中订单开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			List<LotteryIssue> lotteryIssueList = lotteryIssueService.getCanPrintIssueList(currentPage, pageSize);

			if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
				for (LotteryIssue lotteryIssue : lotteryIssueList) {
					String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
					String printBeginTime = lotteryIssue.getPrintBeginTime();
					String printEndTime = lotteryIssue.getPrintEndTime();

					if (DateUtil.compareDateTime(printBeginTime, currentTime) >= 0
							&& DateUtil.compareDateTime(currentTime, printEndTime) > 0) {
						ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
						ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();

						ActivemqSendObject sendObject = new ActivemqSendObject();
						sendObject.setLotteryId(lotteryId);
						sendObject.setIssueNo(issueNo);
						activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_ORDERPRINT_METHODID);
						ScanLog.scan.info("scan ticketPrint put ACTIVEMQ queue,lotteryId=%s,issueNo=%s", lotteryId,
								issueNo);
					}
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("扫描出票中订单发生异常", e);
		}
		ScanLog.scan.info("--------扫描出票中订单结束------------");
	}
}
