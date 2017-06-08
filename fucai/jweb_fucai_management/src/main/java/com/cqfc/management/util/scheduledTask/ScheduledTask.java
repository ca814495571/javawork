package com.cqfc.management.util.scheduledTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.ReturnData;
import com.cqfc.protocol.ticketissue.FucaiCount;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ParameterUtils;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;

/**
 * @author: giantspider@126.com
 */

@EnableScheduling
@Service
public class ScheduledTask {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss");

	private static boolean checkAwardPrize = false;

	private static boolean checkWinPrize = false;

	private static LotteryIssue lotteryIssueWin = new LotteryIssue();

	private static LotteryIssue lotteryIssueAward = new LotteryIssue();

	
	private static String autoCheck_lotteryId = ParameterUtils.getParameterValue("autoCheck_lotteryId");
	

	public void reportCurrentTime(String scheduledType, String taskName) {
		System.out.println("[" + scheduledType + "] " + taskName
				+ " got executed at " + dateFormat.format(new Date()));
	}

	// 格式: [秒] [分] [小时] [日] [月] [周] [年]* 表示所有值. 例如:在分的字段上设置 "*",表示每一分钟都会触发。?
	// 表示不指定值。
	@Scheduled(cron = "0 0/1 * * * ?")
	public void checkWinPrize() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		try {
			long winningPrizeMoney = 0;
			String issueNo = "";
			String lotteryId = "";
			if (checkWinPrize) {
				lotteryIssueWin.setState(1);
				// 获取状态为待算奖的彩种
				com.cqfc.processor.ReturnMessage message = TransactionProcessor
						.dynamicInvoke("lotteryIssue",
								"getLotteryIssueByParam", lotteryIssueWin, 1,
								100000);

				if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
						.getStatusCode())) {
					ReturnData returnData = (ReturnData) message.getObj();
					List<LotteryIssue> lotteryIssues = returnData
							.getResultList();

					for (LotteryIssue lotteryIssueTemp : lotteryIssues) {
						lotteryId = lotteryIssueTemp.getLotteryId();
						issueNo = lotteryIssueTemp.getIssueNo();
						if (ifAutoCheckLotteryId(lotteryId)) {

							// 获取本平台中奖总金额
							message = TransactionProcessor.dynamicInvoke(
									"ticketWinning",
									"getTotalWinningMoneyByGame",
									lotteryId,
									issueNo);
							if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
									.equals(message.getStatusCode())) {
								winningPrizeMoney = (Long) message.getObj();
								Log.run.info("中奖审核平台中奖金额, lotteryId=%s, issueNo=%s, winningPrizeMoney=%d", lotteryId, issueNo, winningPrizeMoney);
								// 获取福彩统计中奖总金额
								message = TransactionProcessor
										.dynamicInvoke("ticketIssue",
												"getFucaiCount",
												lotteryId,
												issueNo);
								if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
										.equals(message.getStatusCode())) {
									FucaiCount fucaiCount = (FucaiCount) message
											.getObj();
									Log.run.info("中奖审核福彩中奖金额, lotteryId=%s, issueNo=%s, fucaiWinningMoney=%d", lotteryId, issueNo, fucaiCount
											.getTotalWinning());
									if (winningPrizeMoney == fucaiCount
											.getTotalWinning()) {
										message = TransactionProcessor
												.dynamicInvoke(
														"lotteryIssue",
														"updateLotteryIssueStateByParam",
														lotteryId,
														issueNo,
														IssueConstant.ISSUE_STATUS_AUDITTING);
										if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
												.equals(message
														.getStatusCode())
												&& (Integer) message
														.getObj() == 1) {
											Log.run.info("中奖审核自动审核成功, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
										} else {
											Log.run.info("中奖审核自动审核失败, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
										}
									} else {
										Log.run.info("中奖审核自动审核中奖金额和福彩给回金额不一致, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
									}
								} else {
									Log.run.info("中奖审核自动审核103获取福彩统计中奖金额错误, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
								}
							}

						}

					}
				}
			}
		} catch (Exception e) {
			Log.run.error("中奖自动审核异常", e);
		}

	}

	@Scheduled(cron = "0 0/1 * * * ?")
	public void checkAwardPrize() {
		if (!SwitchUtil.timerIsOpen()) {
			return;
		}
		try {
			String issueNo = "";
			String lotteryId = "";
			if (checkAwardPrize) {
				lotteryIssueAward.setState(3);
				com.cqfc.processor.ReturnMessage message = TransactionProcessor
						.dynamicInvoke("lotteryIssue",
								"getLotteryIssueByParam", lotteryIssueAward, 1,
								100000);

				if (message.getObj() != null) {

					ReturnData returnData = (ReturnData) message.getObj();
					List<LotteryIssue> lotteryIssues = returnData
							.getResultList();

					for (LotteryIssue lotteryIssueTemp : lotteryIssues) {
						lotteryId = lotteryIssueTemp.getLotteryId();
						issueNo = lotteryIssueTemp.getIssueNo();
						if (ifAutoCheckLotteryId(lotteryId)) {

							message = TransactionProcessor.dynamicInvoke(
									"lotteryIssue",
									"updateLotteryIssueStateByParam",
									lotteryId,
									issueNo,
									IssueConstant.ISSUE_STATUS_SENDING);
							if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
									.equals(message.getStatusCode())
									&& (Integer) message.getObj() == 1) {
								Log.run.info("派奖审核自动审核成功, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
							} else {
								Log.run.info("派奖审核自动审核失败, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
							}

						}

					}

				}

			}

		} catch (Exception e) {
			Log.run.error("派奖自动审核异常", e);
		}

	}

	public static boolean getCheckAwardPrize() {
		return checkAwardPrize;
	}

	public static void setCheckAwardPrize(boolean checkAwardPrize) {
		ScheduledTask.checkAwardPrize = checkAwardPrize;
	}

	public static boolean getCheckWinPrize() {
		return checkWinPrize;
	}

	public static void setCheckWinPrize(boolean checkWinPrize) {
		ScheduledTask.checkWinPrize = checkWinPrize;
	}
	
	/**
	 * 读取属性文件的彩种判断是否需要自动审核
	 * @param lotteryId
	 * @return
	 */
	public static boolean ifAutoCheckLotteryId(String lotteryId){
		
	try {
		
		String [] lotteryIds = autoCheck_lotteryId.split(",");
		
		for (String lotteryIdTemp : lotteryIds) {
			
			if(lotteryId.equals(lotteryIdTemp)){
				
				return true;
			}
			
		}
	} catch (Exception e) {
		Log.run.warn("自动审核彩种参数没有进行配置");
		return false;
	}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(ifAutoCheckLotteryId("SSQ"));
	}

}
