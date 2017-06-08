package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.xmlparser.TransactionMsgLoader121;
import com.cqfc.xmlparser.TransactionMsgLoader621;
import com.cqfc.xmlparser.transactionmsg121.Headtype;
import com.cqfc.xmlparser.transactionmsg121.Msg;
import com.cqfc.xmlparser.transactionmsg121.Querytype;
import com.cqfc.xmlparser.transactionmsg621.Flowissues;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class TransactionMsgProcessor121 {

	/**
	 * 停追合作商某个追号单的某几期(121)
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String respCode = "621";
		// 请求数据
		String xmlstr = message.getTransMsg();
		Msg requestMsg = TransactionMsgLoader121.xmlToMsg(xmlstr);

		Headtype head = requestMsg.getHead();
		Querytype flowOrder = requestMsg.getBody().getFloworder();

		String partnerId = head.getPartnerid();
		String partnerTradeId = flowOrder.getId();
		String lotteryId = flowOrder.getGameid();
		String appendTaskId = flowOrder.getPalmflowid();
		List<String> issueNoList = flowOrder.getIssues().getIssue();

		try {
			// 修改追号明细状态(停追)
			ReturnMessage stopAppendTaskMsg = TransactionProcessor.dynamicInvoke("appendTask", "stopAppendTask",
					appendTaskId, issueNoList);
			if (!stopAppendTaskMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("修改追号明细状态发生异常,渠道ID：" + partnerId + ",平台追号ID：" + appendTaskId + ",异常信息:"
						+ stopAppendTaskMsg.getMsg());
				response.setStatusCode(stopAppendTaskMsg.getStatusCode());
				response.setData(stopAppendTaskMsg.getMsg());
				response.setResponseTransCode(respCode);
				return response;
			}
			List<AppendTaskDetail> stopList = (List<AppendTaskDetail>) stopAppendTaskMsg.getObj();

			if (null == stopList || (issueNoList.size() > 0 && issueNoList.size() != stopList.size())) {
				Log.fucaibiz.info("停追失败,渠道ID：" + partnerId + ",平台追号ID：" + appendTaskId);
				// 停止追号失败
				response.setStatusCode(ConstantsUtil.STATUS_CODE_STOPCHASE_ERROR);
				response.setData("停追期号发生错误");
				response.setResponseTransCode(respCode);
				return response;
			}
			// 获取当期追号任务状态
			ReturnMessage AppendTaskMsg = TransactionProcessor.dynamicInvoke("appendTask", "findAppendTaskById",
					appendTaskId);
			AppendTask appendTask = (AppendTask) AppendTaskMsg.getObj();
			int appendStatus = appendTask.getAppendStatus();
			String appendStatusMsg = appendStatus == 2 ? "追号完成" : "追号进行中";

			long userId = stopList.get(0).getUserId();
			long refundFreezeMoney = 0; // 停追总金额
			String stopIssueNo = ""; // 停追期号字符串(用于日志)
			String serialNumberDetailId = ""; // 追号退款流水
			for (AppendTaskDetail detail : stopList) {
				refundFreezeMoney += detail.getTotalMoney();
				stopIssueNo += detail.getIssueNo() + ",";
				serialNumberDetailId += String.valueOf(detail.getDetailId()) + ",";
			}
			// 退还冻结金额流水号
			String serialNumber = OrderConstant.APPEND_REFUNDFREEZESERIANUMBER_PREFIX + appendTaskId + ","
					+ serialNumberDetailId.substring(0, serialNumberDetailId.length() - 1);
			// 退还冻结金额给用户
			ReturnMessage refundFreezeMsg = TransactionProcessor.dynamicInvoke("userAccount", "refundFreezeMoney",
					userId, refundFreezeMoney, appendTask.getFreezeSerialNumber(), serialNumber);
			if (!refundFreezeMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("退还冻结金额给用户发生异常,用户ID：" + userId + ",平台追号ID：" + appendTaskId + ",异常信息:"
						+ refundFreezeMsg.getMsg());
			}
			int refundFreeze = (Integer) refundFreezeMsg.getObj();

			if (refundFreeze <= 0) {
				// 退还冻结金额失败
				Log.fucaibiz.info("退还冻结金额失败,渠道ID：" + partnerId + ",平台追号ID：" + appendTaskId);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_REFUNDMONEY_ERROR);
				response.setData("退款异常");
				response.setResponseTransCode(respCode);
				return response;
			}
			Log.fucaibiz.info("停追成功,退还冻结金额成功,平台追号ID：" + appendTaskId + ",停追期号：" + stopIssueNo + "退还冻结金额(分):"
					+ refundFreezeMoney + ",冻结流水号：" + appendTask.getFreezeSerialNumber());
			// 返回停追反馈结果
			com.cqfc.xmlparser.transactionmsg621.Msg msg621 = new com.cqfc.xmlparser.transactionmsg621.Msg();

			com.cqfc.xmlparser.transactionmsg621.Headtype headtype = new com.cqfc.xmlparser.transactionmsg621.Headtype();
			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg621.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg621.Body reqBody = new com.cqfc.xmlparser.transactionmsg621.Body();
			com.cqfc.xmlparser.transactionmsg621.Querytype stopAppend = new com.cqfc.xmlparser.transactionmsg621.Querytype();

			stopAppend.setCancelmoney(MoneyUtil.toYuanStr(refundFreezeMoney));
			stopAppend.setGameid(lotteryId);
			stopAppend.setId(partnerTradeId);
			stopAppend.setFlowstatus(String.valueOf(appendStatus));
			stopAppend.setFlowMsg(appendStatusMsg);

			List<Flowissues> issueList = stopAppend.getFlowissues();
			for (AppendTaskDetail appendDetail : stopList) {
				com.cqfc.xmlparser.transactionmsg621.Flowissues issue = new com.cqfc.xmlparser.transactionmsg621.Flowissues();
				issue.setIssue(appendDetail.getIssueNo());
				issue.setIssuecancelmoney(MoneyUtil.toYuanStr(appendDetail.getTotalMoney()));
				issueList.add(issue);
			}
			reqBody.setReport(stopAppend);
			msg621.setBody(reqBody);

			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setData(TransactionMsgLoader621.msgToXml(msg621));
			response.setResponseTransCode(respCode);
		} catch (Exception e) {
			Log.fucaibiz.error("停追发生异常,partnerId：" + partnerId + ",partnerTradeId：" + partnerTradeId, e);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setResponseTransCode(respCode);
		}
		return response;
	}

}
