package com.cqfc.businesscontroller.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;

import com.cqfc.order.AppendTaskOrderService;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.ticketwinning.service.impl.TicketWinningServiceImpl;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.xmlparser.TransactionMsgLoader110;
import com.cqfc.xmlparser.TransactionMsgLoader710;
import com.cqfc.xmlparser.transactionmsg110.Flowissue;
import com.cqfc.xmlparser.transactionmsg110.Flowissues;
import com.cqfc.xmlparser.transactionmsg110.Headtype;
import com.cqfc.xmlparser.transactionmsg110.Msg;
import com.cqfc.xmlparser.transactionmsg110.Querytype;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class TransactionMsgProcessor110 {

	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String respCode = "710";

		String xmlstr = message.getTransMsg();
		Msg requestMsg = TransactionMsgLoader110.xmlToMsg(xmlstr);

		Headtype head = requestMsg.getHead();
		String partnerId = head.getPartnerid();

		Querytype flowOrder = requestMsg.getBody().getFloworder();

		String partnerTradeId = flowOrder.getId();
		try {
			long totalMoney = MoneyUtil.toCent(Long.valueOf(flowOrder.getTotalmoney()));
			int totalIssue = Integer.valueOf(flowOrder.getTotalissue());
			String playType = flowOrder.getPlaytype();
			String lotteryId = flowOrder.getGameid();
			int stopFlag = Integer.valueOf(flowOrder.getStopflag());
			long userId = Long.valueOf(flowOrder.getUserid());
			String ball = flowOrder.getBall();

			Flowissues flowIssues = flowOrder.getFlowissues();
			List<Flowissue> flowIssueList = flowIssues.getFlowissue();

			if (flowIssueList.isEmpty() || flowIssueList.size() < 2) {
				response.setStatusCode(ConstantsUtil.STATUS_CODE_OVER_LARGESTNUM);
				response.setData("追期信息,必须出现2-10次");
				response.setResponseTransCode(respCode);
				return response;
			}

			ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "isPartnerAccount", partnerId);
			if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("追号任务,校验渠道商类型发生异常,partnerId=%s,errorMsg=%d", partnerId, partnerMsg.getMsg());
			}
			boolean isPartnerAccount = (Boolean) partnerMsg.getObj();

			if (isPartnerAccount) {
				Log.fucaibiz.info("追号任务,不支持接入商类型,只有平台用户才可以进行追号投注,partnerId=%s,partnerTradeId=%s", partnerId,
						partnerTradeId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTSUPPORT_PORTAL);
				response.setData("不支持的接入商类型");
				response.setResponseTransCode(respCode);
				return response;
			}
			// 校验投注内容与总金额是否一致
			TicketWinningServiceImpl ticketService = new TicketWinningServiceImpl();
			BallCountReturnMessage ticketReturn = null;
			try {
				ticketReturn = ticketService.calBallCount(lotteryId, playType, ball);
			} catch (TException e) {
				Log.fucaibiz.info("追号任务,校验投注内容发生异常,partnerId=%s,partnerTradeId=%s,errorMsg=%s", partnerId,
						partnerTradeId, e);
			}
			// 每次注数
			int ballNum = ticketReturn.getCount();
			if (ballNum == 0) {
				Log.fucaibiz.info("追号任务,投注号码格式错误,partnerId=%s,userId=%d,partnerTradeId=%s", partnerId, userId,
						partnerTradeId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
				response.setData("投注号码格式错误");
				response.setResponseTransCode(respCode);
				return response;
			}
			// 总投注注数
			int totalBallNum = 0;
			for (Flowissue flowissue : flowIssueList) {
				int multiple = Integer.valueOf(flowissue.getMultiple());
				totalBallNum += ballNum * multiple;
			}
			if (totalMoney != MoneyUtil.toCent(totalBallNum * 2)) {
				Log.fucaibiz.info("追号任务,投注金额错误,partnerId=%s,userId=%d,partnerTradeId=%s", partnerId, userId,
						partnerTradeId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_BETTINGAMOUNT_ERROR);
				response.setData("投注金额错误");
				response.setResponseTransCode(respCode);
				return response;
			}

			// 生成追号任务ID
			String appendTaskId = partnerId + "#" + partnerTradeId;

			// 对追号的期号进行排序（升序）
			Collections.sort(flowIssueList, new Comparator<Flowissue>() {
				public int compare(Flowissue arg0, Flowissue arg1) {
					return arg0.getIssue().compareTo(arg1.getIssue());
				}
			});
			// 校验最小期号是否可以生成追号任务
			String smallIssueNo = flowIssueList.get(0).getIssue();
			ReturnMessage issueMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId,
					smallIssueNo);
			LotteryIssue issue = (LotteryIssue) issueMsg.getObj();
			String endTime = "SINGLE".equals(OrderUtil.checkPlayType(lotteryId, playType)) ? issue.getSingleEndTime()
					: issue.getCompoundEndTime();
			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());

			if (DateUtil.compareDateTime(currentTime, endTime) < 0) {
				Log.fucaibiz.info("追号任务,最小期号：" + smallIssueNo
						+ "销售期已过,无法创建追号任务,partnerId=%s,userId=%d,partnerTradeId=%s", partnerId, userId, partnerTradeId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_STOP_SAIL);
				response.setData("销售期已过 停止销售");
				response.setResponseTransCode(respCode);
				return response;
			}
			// 封装追号任务明细
			List<AppendTaskDetail> appendTaskDetailList = new ArrayList<AppendTaskDetail>();
			for (Flowissue flowIssue : flowIssueList) {
				String currentIssueNo = flowIssue.getIssue();
				int currentMultiple = Integer.valueOf(flowIssue.getMultiple());

				AppendTaskDetail appendTaskDetail = new AppendTaskDetail();
				appendTaskDetail.setAppendTaskId(appendTaskId);
				appendTaskDetail.setPartnerId(partnerId);
				appendTaskDetail.setIssueNo(currentIssueNo);
				appendTaskDetail.setLotteryId(lotteryId);
				appendTaskDetail.setUserId(userId);
				appendTaskDetail.setPlayType(playType);
				appendTaskDetail.setTotalMoney(MoneyUtil.toCent(ballNum * currentMultiple * 2));
				appendTaskDetail.setMultiple(currentMultiple);
				appendTaskDetail.setNoteNumber(ballNum);
				appendTaskDetail.setBall(ball);

				appendTaskDetailList.add(appendTaskDetail);
			}
			// 冻结用户账户资金流水号
			String serialNumber = OrderConstant.APPEND_FREEZESERIANUMBER_PREFIX + appendTaskId;
			// 创建追号任务（包括追号明细）
			AppendTask appendTask = new AppendTask();
			appendTask.setAppendTaskId(appendTaskId);
			appendTask.setPartnerId(partnerId);
			appendTask.setLotteryId(lotteryId);
			appendTask.setBall(ball);
			appendTask.setBeginIssueNo(smallIssueNo);
			appendTask.setAppendQuantity(totalIssue);
			appendTask.setRemainingQuantity(totalIssue);
			appendTask.setStopFlag(stopFlag);
			appendTask.setAppendTotalMoney(totalMoney);
			appendTask.setPerNoteNumber(ballNum);
			appendTask.setUserId(userId);
			appendTask.setPlayType(playType);
			appendTask.setFreezeSerialNumber(serialNumber);
			appendTask.setAppendTaskDetailList(appendTaskDetailList);

			ReturnMessage appendTaskMsg = TransactionProcessor.dynamicInvoke("appendTask", "addAppendTask", appendTask);
			int isAppendTaskSuccess = (Integer) appendTaskMsg.getObj();
			if (isAppendTaskSuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
				Log.fucaibiz.info("追号任务,交易号已存在,不能重复创建,,partnerId=%s,userId=%d,partnerTradeId=%s", partnerId, userId,
						partnerTradeId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_REPEAT_ORDER);
				response.setData("该交易号已创建追号投注订单,不能重复创建");
				response.setResponseTransCode(respCode);
				return response;
			}
			if (isAppendTaskSuccess <= 0) {
				Log.fucaibiz.info("追号任务,追号失败,,partnerId=%s,userId=%d,partnerTradeId=%s", partnerId, userId,
						partnerTradeId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_RRADE_FAIL);
				response.setData("交易失败");
				response.setResponseTransCode(respCode);
				return response;
			}
			Log.fucaibiz.info("追号任务创建成功,appendTaskId=%s", appendTaskId);
			// 冻结用户账户资金
			ReturnMessage userAccountMsg = TransactionProcessor.dynamicInvoke("userAccount", "freezeUserAccount",
					userId, totalMoney, serialNumber);
			if (!userAccountMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("追号任务,冻结用户账户资金发生异常,partnerId=%s,errorMsg=%s", partnerId, userAccountMsg.getMsg());
			}
			int isfreezeUserAccount = (Integer) userAccountMsg.getObj();
			if (isfreezeUserAccount <= 0) {
				// 追号冻结金额失败,取消追号订单.
				Log.fucaibiz.info("追号任务,冻结金额失败,partnerId=%s,freezeMoney=%d,serialNumber=%s", partnerId, totalMoney,
						serialNumber);

				ReturnMessage cancelMsg = TransactionProcessor.dynamicInvoke("appendTask", "cancelAppendTask",
						appendTaskId);
				if (!cancelMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.info("追号冻结金额失败,取消追号订单发生异常,appendTaskId=%s,userId=%d,errorMsg=%s", appendTaskId,
							userId, cancelMsg.getMsg());
				}
				int cancelValue = (Integer) cancelMsg.getObj();
				if (cancelValue > 0) {
					Log.fucaibiz.info("追号冻结金额失败,取消追号订单成功,appendTaskId=%s,userId=%d", appendTaskId, userId);
				}
				response.setStatusCode(ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR);
				response.setData("扣款异常或者帐号钱数不足,追号任务已取消.");
				response.setResponseTransCode(respCode);
				return response;
			}

			com.cqfc.xmlparser.transactionmsg710.Msg msg710 = new com.cqfc.xmlparser.transactionmsg710.Msg();

			com.cqfc.xmlparser.transactionmsg710.Headtype headtype = new com.cqfc.xmlparser.transactionmsg710.Headtype();
			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg710.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg710.Body reqBody = new com.cqfc.xmlparser.transactionmsg710.Body();
			com.cqfc.xmlparser.transactionmsg710.Querytype appendOrder = new com.cqfc.xmlparser.transactionmsg710.Querytype();
			appendOrder.setId(partnerTradeId);
			appendOrder.setPalmflowid(appendTaskId);
			appendOrder.setStatuscode(ConstantsUtil.STATUS_CODE_TRADING);
			appendOrder.setMsg("交易中");
			appendOrder.setTotalmoney(MoneyUtil.toYuanStr(totalMoney));
			reqBody.setReport(appendOrder);
			msg710.setBody(reqBody);

			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setData(TransactionMsgLoader710.msgToXml(msg710));
			response.setResponseTransCode(respCode);

			try {
				ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
				AppendTaskOrderService appendTaskOrderService = ctx.getBean("appendTaskOrderService",
						AppendTaskOrderService.class);
				// 查询追号明细
				ReturnMessage appendTaskDetailMsg = TransactionProcessor.dynamicInvoke("appendTask",
						"findMinAppendTaskDetail", partnerId, partnerTradeId);
				AppendTaskDetail minDetail = (AppendTaskDetail) appendTaskDetailMsg.getObj();
				if (null != minDetail) {
					// 尝试创建最小期号订单(如果当前时间不在投注时间范围内,创建失败)
					Log.fucaibiz.info("追号任务创建成功,尝试创建最小期号订单,彩种：" + minDetail.getLotteryId() + ",期号:"
							+ minDetail.getIssueNo());
					appendTaskOrderService.createAppendTaskOrder(minDetail);
				}
			} catch (Exception e) {
				Log.fucaibiz.info("追号任务创建成功,尝试创建最小期号订单发生异常");
				return response;
			}
		} catch (Exception e) {
			Log.fucaibiz.error("追号任务创建成功发生异常,partnerId：" + partnerId + ",partnerTradeId：" + partnerTradeId, e);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setResponseTransCode(respCode);
		}
		return response;
	}

}
