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
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * 追号任务创建订单扫描 (job条件：期号状态销售中,扫描追号明细表,创建追号任务订单)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class AppendCreateOrderJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0/1 * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------扫描创建追号任务订单开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getLotteryIssueListByState(
						IssueConstant.ISSUE_STATUS_SELLING, currentPage, pageSize);
				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {

					ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
					ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);
					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						String printBeginTime = lotteryIssue.getPrintBeginTime();
						String printEndTime = lotteryIssue.getPrintEndTime();

						if (DateUtil.compareDateTime(printBeginTime, currentTime) >= 0
								&& DateUtil.compareDateTime(currentTime, printEndTime) > 0) {
							String lotteryId = lotteryIssue.getLotteryId();
							String issueNo = lotteryIssue.getIssueNo();

							ActivemqSendObject sendObject = new ActivemqSendObject();
							sendObject.setLotteryId(lotteryId);
							sendObject.setIssueNo(issueNo);
							activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_APPENDSCANORDER_METHODID);
							ScanLog.scan.info("触发追号创建订单操作,放入ACTIVEMQ队列,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("扫描创建追号任务订单发生异常", e);
		}
		ScanLog.scan.info("--------扫描创建追号任务订单结束------------");
	}
}
