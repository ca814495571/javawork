package com.cqfc.businesscontroller.util;

import java.util.ArrayList;
import java.util.List;

import com.cqfc.order.model.Order;
import com.cqfc.order.model.SportOrder;
import com.cqfc.order.model.SportOrderDetail;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.partnerorder.OrderDetail;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.jami.util.Log;

/**
 * 投注订单相关操作类
 * 
 * @author liwh
 */
public class OrderOperateUtil {

	/**
	 * 获取订单交易状态码
	 * 
	 * @param orderStatus
	 * @return
	 */
	public static String getStatusCode(int orderStatus) {
		String str = "";
		if (orderStatus == Order.OrderStatus.WAIT_PAYMENT.getValue()) {
			str = "0000#等待交易";
		} else if (orderStatus == Order.OrderStatus.HAS_PAYMENT.getValue()
				|| orderStatus == Order.OrderStatus.IN_TICKET.getValue()) {
			str = "0001#交易中";
		} else if (orderStatus == Order.OrderStatus.DRAWER_FAILURE.getValue()
				|| orderStatus == Order.OrderStatus.REFUNDING.getValue()
				|| orderStatus == Order.OrderStatus.REFUND_SUCCESS.getValue()
				|| orderStatus == Order.OrderStatus.ORDER_CANCEL.getValue()) {
			str = "0003#交易失败";
		} else {
			str = "0002#交易成功";
		}
		return str;
	}

	/**
	 * 同步订单数据到全局数据库
	 * 
	 * @param order
	 * @param isPartnerAccount
	 * @return
	 */
	public static int syncToGlobal(Order order, boolean isPartnerAccount) {
		int syncValue = Order.OrderSync.SYNC_FAILURE.getValue();
		long orderNo = order.getOrderNo();
		try {
			com.cqfc.protocol.partnerorder.Order partnerOrder = null;
			com.cqfc.protocol.userorder.Order userOrder = null;
			if (isPartnerAccount) {
				partnerOrder = new com.cqfc.protocol.partnerorder.Order();
				partnerOrder.setLotteryId(order.getLotteryId());
				partnerOrder.setPartnerId(order.getPartnerId());
				partnerOrder.setUserId(order.getUserId());
				partnerOrder.setIssueNo(order.getIssueNo());
				partnerOrder.setOrderType(order.getOrderType());
				partnerOrder.setOrderNo(String.valueOf(orderNo));
				partnerOrder.setOrderStatus(order.getOrderStatus());
				partnerOrder.setTotalAmount(order.getTotalAmount());
				partnerOrder.setOrderContent(order.getOrderContent());
				partnerOrder.setMultiple(order.getMultiple());
				partnerOrder.setPlayType(order.getPlayType());
				partnerOrder.setPaySerialNumber(order.getPaySerialNumber());
				partnerOrder.setRealName(order.getRealName());
				partnerOrder.setCardNo(order.getCardNo());
				partnerOrder.setMobile(order.getMobile());
				partnerOrder.setCreateTime(order.getCreateTime());
				partnerOrder.setTradeId(order.getTradeId());
				partnerOrder.setStakeNum(order.getTicketNum());
				partnerOrder.setExt("");
			} else {
				userOrder = new com.cqfc.protocol.userorder.Order();
				userOrder.setLotteryId(order.getLotteryId());
				userOrder.setPartnerId(order.getPartnerId());
				userOrder.setUserId(order.getUserId());
				userOrder.setIssueNo(order.getIssueNo());
				userOrder.setOrderType(order.getOrderType());
				userOrder.setOrderNo(String.valueOf(orderNo));
				userOrder.setOrderStatus(order.getOrderStatus());
				userOrder.setTotalAmount(order.getTotalAmount());
				userOrder.setOrderContent(order.getOrderContent());
				userOrder.setMultiple(order.getMultiple());
				userOrder.setPlayType(order.getPlayType());
				userOrder.setPaySerialNumber(order.getPaySerialNumber());
				userOrder.setRealName(order.getRealName());
				userOrder.setCardNo(order.getCardNo());
				userOrder.setMobile(order.getMobile());
				userOrder.setCreateTime(order.getCreateTime());
				userOrder.setTradeId(order.getTradeId());
				userOrder.setStakeNum(order.getTicketNum());
				userOrder.setExt("");
			}
			for (int i = 1; i < 4; i++) {
				Log.fucaibiz.debug("第%d次请求同步订单到全局数据库,orderNo=%d,isPartner=%b", i, orderNo, isPartnerAccount);
				ReturnMessage sycOrderMsg = null;
				if (isPartnerAccount) {
					sycOrderMsg = TransactionProcessor.dynamicInvoke("partnerOrder", "addPartnerOrder", partnerOrder);
				} else {
					sycOrderMsg = TransactionProcessor.dynamicInvoke("userOrder", "addUserOrder", userOrder);
				}
				if (!sycOrderMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.debug("第%d次请求同步订单发生异常,orderNo=%d,errorMsg=%s", i, orderNo, sycOrderMsg.getMsg());
				}
				int isSycSuccess = (Integer) sycOrderMsg.getObj();
				if (isSycSuccess == 1) {
					syncValue = Order.OrderSync.SYNC_SUCCESS.getValue();
					break;
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("同步订单发生异常,orderNo=%d,errorMsg=%s", order.getOrderNo(), e);
		}
		return syncValue;
	}

	/**
	 * 竞技彩同步订单到全局数据库
	 * 
	 * @param order
	 * @param isPartnerAccount
	 * @return
	 */
	public static int syncSportToGlobal(SportOrder order, boolean isPartnerAccount) {
		int syncValue = SportOrder.OrderSync.SYNC_FAILURE.getValue();
		long orderNo = order.getOrderNo();
		try {
			com.cqfc.protocol.partnerorder.Order partnerOrder = null;
			com.cqfc.protocol.userorder.Order userOrder = null;
			if (isPartnerAccount) {
				partnerOrder = new com.cqfc.protocol.partnerorder.Order();
				partnerOrder.setLotteryId(order.getLotteryId());
				partnerOrder.setPartnerId(order.getPartnerId());
				partnerOrder.setUserId(order.getUserId());
				partnerOrder.setIssueNo(order.getIssueNo());
				partnerOrder.setOrderType(order.getOrderType());
				partnerOrder.setOrderNo(String.valueOf(orderNo));
				partnerOrder.setOrderStatus(order.getOrderStatus());
				partnerOrder.setTotalAmount(order.getTotalAmount());
				partnerOrder.setOrderContent(order.getOrderContent());
				partnerOrder.setMultiple(order.getMultiple());
				partnerOrder.setPlayType(order.getPlayType());
				partnerOrder.setPaySerialNumber(order.getPaySerialNumber());
				partnerOrder.setRealName(order.getRealName());
				partnerOrder.setCardNo(order.getCardNo());
				partnerOrder.setMobile(order.getMobile());
				partnerOrder.setCreateTime(order.getCreateTime());
				partnerOrder.setTradeId(order.getTradeId());
				partnerOrder.setStakeNum(order.getTicketNum());
				partnerOrder.setPlanId(order.getPlanId());
				partnerOrder.setEndTime(order.getCloseTime());
				partnerOrder.setTicketTime(order.getPrintTime());
				partnerOrder.setProvince(order.getPrintProvince());
				partnerOrder.setLotteryMark(order.getTicketNo());
				List<OrderDetail> detailList = new ArrayList<OrderDetail>();
				StringBuffer ext = new StringBuffer();
				for (SportOrderDetail detail : order.getSportOrderDetailList()) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrderNo(String.valueOf(detail.getOrderNo()));
					orderDetail.setTransferId(detail.getTransferId());
					orderDetail.setMatchNo(detail.getMatchNo());
					orderDetail.setRq(detail.getRq());
					orderDetail.setContent(detail.getOrderContent());
					orderDetail.setOdds(detail.getSp());
					orderDetail.setMatchStatus(detail.getMatchStatus());
					orderDetail.setLotteryId(order.getLotteryId());
					orderDetail.setIssueNo(order.getIssueNo());
					orderDetail.setMatchType(OrderUtil.getJcCategoryDetail(order.getLotteryId()));
					orderDetail.setCreateTime(detail.getCreateTime());
					ext.append(detail.getOrderContent()).append("~").append(detail.getTransferId()).append("~")
							.append(detail.getMatchNo()).append("~").append(detail.getRq()).append("~")
							.append(detail.getSp()).append("~").append(detail.getMatchStatus()).append("/");

					Log.fucaibiz.debug("sync order test,orderNo=%d,transferId=%s,rq=%s,sp=%s,createTime=%s",
							detail.getOrderNo(), detail.getTransferId(), detail.getRq(), detail.getSp(),
							detail.getCreateTime());
					detailList.add(orderDetail);
				}
				partnerOrder.setExt(ext.toString());
				partnerOrder.setOrderDetails(detailList);
			} else {
				userOrder = new com.cqfc.protocol.userorder.Order();
				userOrder.setLotteryId(order.getLotteryId());
				userOrder.setPartnerId(order.getPartnerId());
				userOrder.setUserId(order.getUserId());
				userOrder.setIssueNo(order.getIssueNo());
				userOrder.setOrderType(order.getOrderType());
				userOrder.setOrderNo(String.valueOf(orderNo));
				userOrder.setOrderStatus(order.getOrderStatus());
				userOrder.setTotalAmount(order.getTotalAmount());
				userOrder.setOrderContent(order.getOrderContent());
				userOrder.setMultiple(order.getMultiple());
				userOrder.setPlayType(order.getPlayType());
				userOrder.setPaySerialNumber(order.getPaySerialNumber());
				userOrder.setRealName(order.getRealName());
				userOrder.setCardNo(order.getCardNo());
				userOrder.setMobile(order.getMobile());
				userOrder.setCreateTime(order.getCreateTime());
				userOrder.setTradeId(order.getTradeId());
				userOrder.setStakeNum(order.getTicketNum());
				userOrder.setExt("");
			}
			for (int i = 1; i < 4; i++) {
				Log.fucaibiz.debug("竞技彩,第%d次请求同步订单到全局数据库,orderNo=%d,isPartner=%b", i, orderNo, isPartnerAccount);
				ReturnMessage sycOrderMsg = null;
				if (isPartnerAccount) {
					sycOrderMsg = TransactionProcessor.dynamicInvoke("partnerOrder", "addPartnerOrder", partnerOrder);
				} else {
					sycOrderMsg = TransactionProcessor.dynamicInvoke("userOrder", "addUserOrder", userOrder);
				}
				if (!sycOrderMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.debug("竞技彩,第%d次请求同步订单发生异常,orderNo=%d,errorMsg=%s", i, orderNo, sycOrderMsg.getMsg());
				}
				int isSycSuccess = (Integer) sycOrderMsg.getObj();
				if (isSycSuccess == 1) {
					syncValue = SportOrder.OrderSync.SYNC_SUCCESS.getValue();
					break;
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,同步订单发生异常,orderNo=%d,errorMsg=%s", order.getOrderNo(), e);
		}
		return syncValue;
	}

	/**
	 * 主动通知合作商投注订单处理结果
	 * 
	 * @param order
	 */
	public static void notifyPartner(Order order) {
		String partnerId = order.getPartnerId();
		long orderNo = order.getOrderNo();
		try {
			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = new com.cqfc.xmlparser.transactionmsg605.Msg();

			com.cqfc.xmlparser.transactionmsg605.Headtype headtype = new com.cqfc.xmlparser.transactionmsg605.Headtype();
			headtype.setTranscode("605");
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg605.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg605.Body reqBody = new com.cqfc.xmlparser.transactionmsg605.Body();
			com.cqfc.xmlparser.transactionmsg605.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg605.Querytype();

			String[] status = getStatusCode(order.getOrderStatus()).split("#");
			String statusCode = status[0];
			String msgStr = status[1];

			ticketResult.setId(order.getTradeId());
			ticketResult.setPalmid(String.valueOf(orderNo));
			ticketResult.setGameid(order.getLotteryId());
			ticketResult.setMultiple(String.valueOf(order.getMultiple()));
			ticketResult.setIssue(order.getIssueNo());
			ticketResult.setPlaytype(order.getPlayType());
			ticketResult.setMoney(MoneyUtil.toYuanStr(order.getTotalAmount()));
			ticketResult.setStatuscode(statusCode);
			ticketResult.setMsg(msgStr);
			reqBody.setTicketresult(ticketResult);
			msg605.setBody(reqBody);

			String backXml = TransactionMsgLoader605.msgToXml(msg605);

			ReturnMessage msg = TransactionProcessor.dynamicInvoke("accessBack", "sendAccessBackMessage", partnerId,
					backXml);
			Log.fucaibiz.debug("通知合作商订单处理结果,partnerId=%s,orderNo=%d,statuscode=%s,msg=%s,returnCode=%s", partnerId,
					orderNo, statusCode, msgStr, msg.getStatusCode());
		} catch (Exception e) {
			Log.fucaibiz.error("通知合作商订单处理结果发生异常,orderNo=" + orderNo, e);
		}
	}

	/**
	 * 竞技彩,主动通知合作商投注订单处理结果
	 * 
	 * @param order
	 */
	public static void notifySportPartner(SportOrder order) {
		String partnerId = order.getPartnerId();
		long orderNo = order.getOrderNo();
		try {
			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = new com.cqfc.xmlparser.transactionmsg605.Msg();

			com.cqfc.xmlparser.transactionmsg605.Headtype headtype = new com.cqfc.xmlparser.transactionmsg605.Headtype();
			headtype.setTranscode("605");
			headtype.setPartnerid(partnerId);
			headtype.setTime(DateUtil.getCurrentDateTime());
			headtype.setVersion("1.0");
			msg605.setHead(headtype);

			com.cqfc.xmlparser.transactionmsg605.Body reqBody = new com.cqfc.xmlparser.transactionmsg605.Body();
			com.cqfc.xmlparser.transactionmsg605.Querytype ticketResult = new com.cqfc.xmlparser.transactionmsg605.Querytype();

			String[] status = getStatusCode(order.getOrderStatus()).split("#");
			String statusCode = status[0];
			String msgStr = status[1];

			String lotteryId = order.getLotteryId();
			ticketResult.setId(order.getTradeId());
			ticketResult.setPalmid(String.valueOf(orderNo));
			ticketResult.setGameid(lotteryId);
			ticketResult.setMultiple(String.valueOf(order.getMultiple()));
			ticketResult.setIssue(order.getIssueNo());
			ticketResult.setPlaytype(order.getPlayType());
			ticketResult.setMoney(MoneyUtil.toYuanStr(order.getTotalAmount()));
			ticketResult.setStatuscode(statusCode);
			ticketResult.setMsg(msgStr);

			int lotteryDetaiType = OrderUtil.getJcCategoryDetail(lotteryId);
			if (lotteryDetaiType == OrderStatus.LotteryType.JJZC_GAME.getType()
					|| lotteryDetaiType == OrderStatus.LotteryType.JJLC_GAME.getType()) {
				String odds = "";
				if (statusCode.equals(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS)) {
					for (SportOrderDetail detail : order.getSportOrderDetailList()) {
//						odds += detail.getTransferId() + "(" + detail.getRq() + ")~[" + detail.getSp() + "]~0/";
						odds += detail.getTransferId() + "~[" + detail.getSp() + "]~0/";
					}
					odds = odds.substring(0, odds.length() - 1);
				}
				ticketResult.setOdds(odds);
			}

			reqBody.setTicketresult(ticketResult);
			msg605.setBody(reqBody);

			String backXml = TransactionMsgLoader605.msgToXml(msg605);

			ReturnMessage msg = TransactionProcessor.dynamicInvoke("accessBack", "sendAccessBackMessage", partnerId,
					backXml);
			Log.fucaibiz.debug("竞技彩,通知合作商订单处理结果,partnerId=%s,orderNo=%d,statuscode=%s,msg=%s,returnCode=%s", partnerId,
					orderNo, statusCode, msgStr, msg.getStatusCode());
		} catch (Exception e) {
			Log.fucaibiz.error("竞技彩,通知合作商订单处理结果发生异常,orderNo=" + orderNo, e);
		}
	}

}
