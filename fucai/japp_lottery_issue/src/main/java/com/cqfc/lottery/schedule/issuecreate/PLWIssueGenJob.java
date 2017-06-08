package com.cqfc.lottery.schedule.issuecreate;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.DbGenerator;
import com.jami.util.ScanLog;

/**
 * 排列五期号创建：如果能创建期号的开始时间 - 当前时间 < 6,则一次创建7天的期号
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class PLWIssueGenJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0 0 04 * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("-------------排列五期号创建定时任务开始-------------");
		try {
			LotteryIssue lotteryIssue = lotteryIssueService.getMaxLotteryIssue(IssueConstant.LOTTERYID_PLW);
			if (null != lotteryIssue) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date drawTime = simpleDateFormat.parse(lotteryIssue.getDrawTime());
				Date date = DateUtil.getOffsetDate(drawTime, 1);

				String createIssueBeginTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", date);
				String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());

				int offset = DateUtil.getTimeDifference(currentTime, createIssueBeginTime);
				int createDays = 6;
				if (offset < createDays) {
					Date loopDate = drawTime;
					for (int i = 0; i < 6; i++) {
						loopDate = DateUtil.getOffsetDate(loopDate, 1);
						String str = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", loopDate);
						if (DateUtil.checkIsHoliday(str)) {
							createDays += 1;
						}
					}
					String endTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",
							DateUtil.getOffsetDate(date, createDays)).substring(0, 10);
					createIssueBeginTime = createIssueBeginTime.substring(0, 10);
					DbGenerator.setDynamicMasterSource();
					lotteryIssueService.createLotteryIssue(createIssueBeginTime, endTime, IssueConstant.LOTTERYID_PLW);
					ScanLog.scan.info("PLW create,beginTime=%s,endTime=%s", createIssueBeginTime, endTime);
				}
			}
		} catch (Exception e) {
			ScanLog.scan.error("排列五期号创建定时任务发生异常", e);
		}
		ScanLog.scan.info("-------------排列五期号创建定时任务结束--------------");
	}

}
