package com.cqfc.lottery.schedule.issuestate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.SwitchUtil;
import com.jami.util.DbGenerator;
import com.jami.util.ScanLog;

/**
 * 获取开奖结果操作 (job条件： 1.期开奖时间已到 2.期信息无开奖结果)
 * 
 * @author liwh
 */
@EnableScheduling
@Service
public class GetDrawResultScanJob {

	@Autowired
	private ILotteryIssueService lotteryIssueService;

	@Scheduled(cron = "0/30 * * * * ?")
	public void execute() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		ScanLog.scan.info("--------获取开奖结果操作 开始------------");
		try {
			int currentPage = 1;
			int pageSize = 2000;
			while (true) {
				List<LotteryIssue> lotteryIssueList = lotteryIssueService.getDrawIssueList(currentPage, pageSize);

				if (lotteryIssueList != null && lotteryIssueList.size() > 0) {
					for (LotteryIssue lotteryIssue : lotteryIssueList) {
						String lotteryId = lotteryIssue.getLotteryId();
						String issueNo = lotteryIssue.getIssueNo();
						// 体彩数字彩、老足彩暂时不主动拉取开奖结果,等待抓取程序定时推送
						if (lotteryId.equals(IssueConstant.LOTTERYID_ZJSYXW)
								|| lotteryId.equals(IssueConstant.LOTTERYID_DLT)
								|| lotteryId.equals(IssueConstant.LOTTERYID_PLS)
								|| lotteryId.equals(IssueConstant.LOTTERYID_PLW)
								|| lotteryId.equals(IssueConstant.LOTTERYID_QXC)
								|| lotteryId.equals(IssueConstant.SportLotteryType.LZC_SF.getValue())
								|| lotteryId.equals(IssueConstant.SportLotteryType.LZC_6BQC.getValue())
								|| lotteryId.equals(IssueConstant.SportLotteryType.LZC_4JQS.getValue())) {
							continue;
						}
						ScanLog.scan.debug("开奖结果get(send),lotteryId=%s,issueNo=%s", lotteryId, issueNo);
						ReturnMessage msg = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_TICKET_ISSUE,
								"findLotteryDrawResult", lotteryId, issueNo);
						ScanLog.scan.debug("开奖结果get(back),lotteryId=%s,issueNo=%s,statusCode=%s,msg=%s", lotteryId,
								issueNo, msg.getStatusCode(), msg.getMsg());
						if (msg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
							LotteryDrawResult draw = (LotteryDrawResult) msg.getObj();
							if (null != draw && !"".equals(draw)) {
								DbGenerator.setDynamicMasterSource();
								int isSuccess = lotteryIssueService.addLotteryDrawResult(draw);
								ScanLog.scan.info("开奖结果入库,lotteryId=%s,issueNo=%s,drawNo=%s,returnValue=%d", lotteryId,
										issueNo, draw.getDrawResult(), isSuccess);
							}
						}
					}
				} else {
					break;
				}
				currentPage++;
			}
		} catch (Exception e) {
			ScanLog.scan.error("获取开奖结果操作发生异常", e);
		}
		ScanLog.scan.info("--------获取开奖结果操作结束------------");
	}
}
