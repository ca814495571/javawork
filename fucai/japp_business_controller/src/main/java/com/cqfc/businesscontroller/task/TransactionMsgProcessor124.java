package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.appendtask.AppendTaskDetail;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.userorder.Order;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.xmlparser.TransactionMsgLoader124;
import com.cqfc.xmlparser.TransactionMsgLoader724;
import com.cqfc.xmlparser.transactionmsg124.Headtype;
import com.cqfc.xmlparser.transactionmsg124.Msg;
import com.cqfc.xmlparser.transactionmsg124.Querytype;
import com.cqfc.xmlparser.transactionmsg724.Bookperissueorder;
import com.cqfc.xmlparser.transactionmsg724.Perissueordertype;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class TransactionMsgProcessor124 {

	public static final long BIG_PRIZE_AMOUNT = 1000000;

	/**
	 * 追号订单查询接口，供合作商进行追号订单的查询(124)
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		TransResponse response = new TransResponse();
		String respCode = "724";
		// 请求数据
		String xmlstr = message.getTransMsg();
		Msg requestMsg = TransactionMsgLoader124.xmlToMsg(xmlstr);

		Headtype head = requestMsg.getHead();
		Querytype queryBook = requestMsg.getBody().getQuerybook();

		String partnerId = head.getPartnerid();
		String partnerTradeId = queryBook.getId();
		String appendTaskId = partnerId + "#" + partnerTradeId;

		try {
			// 查询追号信息
			ReturnMessage appendTaskMsg = TransactionProcessor.dynamicInvoke("appendTask", "findAppendTaskById",
					appendTaskId);
			if (!appendTaskMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.info("查询追号信息发生异常,partnerId=%s,appendTaskId=%s,errorMsg=%s", partnerId, appendTaskId,
						appendTaskMsg.getMsg());
			}
			AppendTask appendTask = (AppendTask) appendTaskMsg.getObj();

			if (null == appendTask || "".equals(appendTask)) {
				// 查询追号信息失败
				Log.fucaibiz.info("查询追号信息失败,追号订单ID:" + appendTaskId + "不存在.");
				response.setResponseTransCode(ConstantsUtil.STATUS_CODE_CHASEORDER_NOTEXIST);
				response.setData("追号订单不存在");
				response.setResponseTransCode(respCode);
				return response;
			}

			// 返回追号订单结果
			com.cqfc.xmlparser.transactionmsg724.Msg msg724 = new com.cqfc.xmlparser.transactionmsg724.Msg();

			com.cqfc.xmlparser.transactionmsg724.Headtype headtype = new com.cqfc.xmlparser.transactionmsg724.Headtype();
			headtype.setTranscode(respCode);
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg724.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg724.Body reqBody = new com.cqfc.xmlparser.transactionmsg724.Body();
			com.cqfc.xmlparser.transactionmsg724.Querytype query = new com.cqfc.xmlparser.transactionmsg724.Querytype();
			com.cqfc.xmlparser.transactionmsg724.Bookorder bookOrder = new com.cqfc.xmlparser.transactionmsg724.Bookorder();

			bookOrder.setBorderid(appendTaskId);
			bookOrder.setUserid(String.valueOf(appendTask.getUserId()));
			bookOrder.setMoney(MoneyUtil.toYuanStr(appendTask.getAppendTotalMoney()));
			bookOrder.setStatus(String.valueOf(appendTask.getAppendStatus()));
			bookOrder.setPlaytype(String.valueOf(appendTask.getPlayType()));
			bookOrder.setStoptype(String.valueOf(appendTask.getStopFlag()));
			bookOrder.setPrizemoney(String.valueOf(appendTask.getWinningTotalMoney()));
			bookOrder.setStake(String.valueOf(appendTask.getPerNoteNumber()));
			bookOrder.setCreatetime(appendTask.getCreateTime());
			bookOrder.setPartnerid(appendTask.getPartnerId());
			bookOrder.setStartissue(appendTask.getBeginIssueNo());
			bookOrder.setCurrentissue(appendTask.getNewAppendIssueNo()); // 当前期次(待处理)
			bookOrder.setIssuenum(String.valueOf(appendTask.getAppendQuantity()));
			bookOrder.setGameid(appendTask.getLotteryId());
			bookOrder.setPid(partnerTradeId);
			bookOrder.setAnteball(appendTask.getBall());
			bookOrder.setCancelnum(String.valueOf(appendTask.getCancelNum()));
			bookOrder.setCancelmoney(MoneyUtil.toYuanStr(appendTask.getCancelMoney()));
			bookOrder.setFinishednum(String.valueOf(appendTask.getFinishedNum()));
			bookOrder.setFinishedsum(MoneyUtil.toYuanStr(appendTask.getFinishedMoney()));

			Perissueordertype perissueorder = new Perissueordertype();
			List<Bookperissueorder> appendOrderList = perissueorder.getBookperissueorder();
			for (AppendTaskDetail detail : appendTask.getAppendTaskDetailList()) {
				Bookperissueorder appendOrder = new Bookperissueorder();
				appendOrder.setPerissueid(String.valueOf(detail.getDetailId())); // 每一期的追号ID(待处理)
				appendOrder.setBorderId(detail.getAppendTaskId());
				appendOrder.setGameId(detail.getLotteryId());
				appendOrder.setIssueNo(detail.getIssueNo());
				appendOrder.setMultiple(String.valueOf(detail.getMultiple()));
				appendOrder.setStatus(String.valueOf(detail.getAppendDetailStatus()));
				appendOrder.setMoney(MoneyUtil.toYuanStr(detail.getTotalMoney()));

				String orderNo = detail.getOrderNo();
				String chileNum = "0";
				String prizeType = "0";
				String prizeStatus = "0";
				String prizeMoney = "0";
				if (null != orderNo && !"".equals(orderNo)) {
					ReturnMessage msg = TransactionProcessor.dynamicInvoke("userOrder", "getUserOrderByUserId",
							detail.getUserId(), orderNo);
					Order order = (Order) msg.getObj();
					if (null != order && !"".equals(order)) {
						int orderStatus = order.getOrderStatus();
						long winPrizeMoney = order.getWinPrizeMoney();
						chileNum = String.valueOf(order.getStakeNum());
						prizeMoney = MoneyUtil.toYuanStr(order.getWinPrizeMoney());
						prizeType = winPrizeMoney > BIG_PRIZE_AMOUNT ? "2" : (winPrizeMoney > 0 ? "1" : "0");
						prizeStatus = orderStatus == OrderStatus.NOT_WIN_PRIZE ? "1"
								: (orderStatus == OrderStatus.WAIT_TO_AWARD_PRIZE ? "2"
										: (orderStatus == OrderStatus.FINISH_AWARD_PRIZE ? "3" : "0"));
					}
				}
				appendOrder.setChileNum(chileNum);
				appendOrder.setPrizeType(prizeType);
				appendOrder.setPrizestatus(prizeStatus);
				appendOrder.setPrizeMoney(prizeMoney);

				appendOrderList.add(appendOrder);
			}
			bookOrder.setPerissueorder(perissueorder);
			query.setBookorder(bookOrder);
			query.setId(partnerTradeId);
			reqBody.setQuerybook(query);
			msg724.setBody(reqBody);

			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setData(TransactionMsgLoader724.msgToXml(msg724));
			response.setResponseTransCode(respCode);

		} catch (Exception e) {
			Log.fucaibiz.error("追号订单查询发生异常,partnerId=%s,partnerTradeId=%s,errorMsg=%s", partnerId, partnerTradeId, e);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("系统错误，即由未捕获的异常导致的错误");
			response.setResponseTransCode(respCode);
		}
		return response;
	}

}
