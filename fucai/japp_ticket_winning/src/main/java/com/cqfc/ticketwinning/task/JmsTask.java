package com.cqfc.ticketwinning.task;

import java.util.concurrent.ConcurrentMap;

import org.springframework.context.ApplicationContext;

import com.cqfc.ticketwinning.service.impl.CalBDPrizeServiceImpl;
import com.cqfc.ticketwinning.service.impl.CalJCPrizeServiceImpl;
import com.cqfc.ticketwinning.service.impl.CalPrizeServiceImpl;
import com.cqfc.ticketwinning.service.impl.SendPrizeServiceImpl;
import com.cqfc.ticketwinning.service.impl.UpdateOrderStatusServiceImpl;
import com.cqfc.ticketwinning.util.JmsUtils;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.IssueUtil;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
public class JmsTask implements Runnable {

	private int type;
	private String lotteryId;
	private String issueNo;
	private String transferId;
	private ConcurrentMap<String, Boolean> winningThreadMap;

	public JmsTask(int type, String lotteryId, String issueNo, String transferId,
			ConcurrentMap<String, Boolean> winningThreadMap) {
		this.type = type;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.transferId = transferId;
		this.winningThreadMap = winningThreadMap;
	}

	@Override
	public void run() {
		try {
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			if (type == JmsUtils.OPERATE_CALPRIZE) {

				if (lotteryId.equals(IssueConstant.CAL_PRIZE_SPORT_LOTTERYID)) {
					// 竞足竞篮算奖
					CalJCPrizeServiceImpl calPrizeService = ctx.getBean("calJCPrizeServiceImpl",
							CalJCPrizeServiceImpl.class);
					ScanLog.scan.debug("sport match prize,issueNo=%s,transferId=%s", issueNo, transferId);
					calPrizeService.calJCPrize(transferId, issueNo, false);
				} else if (lotteryId.equals(IssueConstant.CAL_PRIZE_BD_LOTTERYID)) {
					// 北单算奖
					CalBDPrizeServiceImpl calPrizeService = ctx.getBean("calBDPrizeServiceImpl",
							CalBDPrizeServiceImpl.class);
					String[] ids = IssueUtil.splitBeiDanTransferId(transferId).split(",");
					int matchType = Integer.valueOf(ids[0]);
					String wareIssue = ids[1];
					String matchNo = ids[2];
					ScanLog.scan.debug("bd match prize,wareIssue=%s,matchNo=%s,matchType=%d", wareIssue, matchNo,
							matchType);
					calPrizeService.calBDPrize(wareIssue, matchNo, matchType, false);
				} else {
					// 数字彩、包括老足彩算奖
					CalPrizeServiceImpl calPrizeService = ctx.getBean("calPrizeServiceImpl", CalPrizeServiceImpl.class);
					calPrizeService.calPrize(lotteryId, issueNo, false);
				}
			}
			if (type == JmsUtils.OPERATE_UPDATEORDER) {
				UpdateOrderStatusServiceImpl updateOrderStatusService = ctx.getBean("updateOrderStatusServiceImpl",
						UpdateOrderStatusServiceImpl.class);
				updateOrderStatusService.updateOrderStatus(lotteryId, issueNo);
			}
			if (type == JmsUtils.OPERATE_SENDPRIZE) {
				SendPrizeServiceImpl sendPrizeService = ctx.getBean("sendPrizeServiceImpl", SendPrizeServiceImpl.class);
				sendPrizeService.sendPrize(lotteryId, issueNo);
			}
		} catch (Exception e) {
			ScanLog.scan.error("tiketwinning订单数据发生异常, type=%d, lotteryId=%s, issueNo=%s, exception=%s", type,
					lotteryId, issueNo, e.toString());
		} finally {
			String threadKey = type + "#" + lotteryId + "#" + issueNo + "#" + transferId;
			winningThreadMap.remove(threadKey);
		}
	}

}
