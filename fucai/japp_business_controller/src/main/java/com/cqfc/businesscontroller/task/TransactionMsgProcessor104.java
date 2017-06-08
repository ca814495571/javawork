package com.cqfc.businesscontroller.task;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.order.OrderService;
import com.cqfc.order.model.Order;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.protocol.ticketwinning.BallCountReturnMessage;
import com.cqfc.ticketwinning.service.impl.TicketWinningServiceImpl;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderConstant;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.PartnerConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader104;
import com.cqfc.xmlparser.transactionmsg104.Headtype;
import com.cqfc.xmlparser.transactionmsg104.Msg;
import com.cqfc.xmlparser.transactionmsg104.Ticket;
import com.cqfc.xmlparser.transactionmsg104.Ticketordertype;
import com.cqfc.xmlparser.transactionmsg104.Tickets;
import com.cqfc.xmlparser.transactionmsg104.User;
import com.cqfc.xmlparser.util.ErrorMsgXmlHelper;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.EnsapOutTicketChekOrder;
import com.jami.util.Log;

/**
 * 此类暂时废弃
 * 
 * @author liwh
 */
public class TransactionMsgProcessor104 {

	public static void process(Message message) {
		String errorMsg = "";
		// 请求数据
		String xmlstr = message.getTransMsg();

		Msg requestMsg = TransactionMsgLoader104.xmlToMsg(xmlstr);
		// head信息
		Headtype head = requestMsg.getHead();
		Ticketordertype ticketOrder = requestMsg.getBody().getTicketorder();

		String partnerId = head.getPartnerid();
		String partnerTradeId = "";
		try {
			Tickets tickets = ticketOrder.getTickets();
			List<Ticket> ticketList = tickets.getTicket();
			partnerTradeId = ticketList.get(0).getId();

			User user = ticketOrder.getUser();
			long userId = null != user.getUserid() && !"".equals(user.getUserid()) ? Long.valueOf(user.getUserid())
					.longValue() : 0;
			String realName = user.getRealname();
			String mobile = user.getPhone();
			String cardNo = user.getIdcard();

			String lotteryId = ticketOrder.getGameid();
			// 校验渠道商类型
			ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke("partner", "findPartnerForCheck", partnerId);
			if (!partnerMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
				Log.fucaibiz.error("校验渠道商类型发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId, partnerTradeId,
						partnerMsg.getMsg());
				errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "合作商id不存在或者参数与xml文件中的不一致",
						ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
			} else {
				LotteryPartner partner = (LotteryPartner) partnerMsg.getObj();
				boolean isPartnerAccount = partner.getPartnerType() == PartnerConstant.PartnerType.USERPARTNER
						.getValue() ? false : true;
				boolean checkUserId = true;
				Log.fucaibiz.debug("校验合作商类型,partnerId=%s,ticketId=%s,isPartner=%b", partnerId, partnerTradeId,
						isPartnerAccount);
				if (!isPartnerAccount) {
					ReturnMessage userMsg = TransactionProcessor.dynamicInvoke("userAccount", "checkUserExist", userId);
					if (!userMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
						Log.fucaibiz.error("校验用户是否存在发生异常,partnerId=%s,userId=%d,ticketId=%s", partnerId, userId,
								partnerTradeId);
						errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "系统错误，即由未捕获的异常导致的错误",
								ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
					} else {
						boolean userIsExist = (Boolean) userMsg.getObj();
						if (!userIsExist) {
							Log.fucaibiz.error("用户USERID不存在,partnerId=%s,orderUserId=%d,ticketId=%s", partnerId,
									userId, partnerTradeId);
							checkUserId = false;
						}
					}
				} else if (partner.getUserId() != userId) {
					Log.fucaibiz.error("渠道商USERID不一致,partnerId=%s,partnerUserId=%d,orderUserId=%d,ticketId=%s",
							partnerId, partner.getUserId(), userId, partnerTradeId);
					checkUserId = false;
				}
				if (!checkUserId) {
					errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "用户id错误",
							ConstantsUtil.STATUS_CODE_USERID_ERROR);
				}
				if ("".equals(errorMsg)) {
					ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
					OrderService orderService = ctx.getBean("orderService", OrderService.class);
					TicketWinningServiceImpl ticketService = ctx.getBean("ticketWinningServiceImpl",
							TicketWinningServiceImpl.class);
					for (Ticket ticket : ticketList) {
						String issueNo = ticket.getIssue();
						String playType = ticket.getPlaytype();
						long orderMoney = MoneyUtil.toCent(Long.valueOf(ticket.getMoney()));
						String orderContent = ticket.getBall();
						int multiple = Integer.valueOf(ticket.getMultiple()).intValue();

						Log.fucaibiz.info("订单处理中,ticketId=%s,partnerId=%s,isPartnerAccount=%b,lotteryId=%s,issueNo=%s",
								partnerTradeId, partnerId, isPartnerAccount, lotteryId, issueNo);

						String playTypeStr = OrderUtil.checkPlayType(lotteryId, playType);

						boolean ballNumFlag = true;
						int perBallNum = orderContent.split("#").length;
						if (!"SINGLE".equals(playTypeStr)) {
							if (perBallNum > 1) {
								ballNumFlag = false;
							}
						} else if (perBallNum > 5) {
							ballNumFlag = false;
						}
						if (!ballNumFlag) {
							Log.fucaibiz.error("超过最大注数,partnerId=%s,ticketId=%s,orderContent=%s", partnerId,
									partnerTradeId, orderContent);
							errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "超过最大注数",
									ConstantsUtil.STATUS_CODE_OVER_LARGESTNUM);
							break;
						}
						if (multiple > 99) {
							Log.fucaibiz.error("超过最大允许倍数,partnerId=%s,ticketId=%s,orderContent=%s,multiple=%d",
									partnerId, partnerTradeId, orderContent, multiple);
							errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "超过最大允许倍数",
									ConstantsUtil.STATUS_CODE_OVER_MAXMULTIPLE);
							break;
						}
						// 投注内容拆单
						BallCountReturnMessage ticketReturn = null;
						try {
							ticketReturn = ticketService.calBallCount(lotteryId, playType, orderContent);
						} catch (Exception e) {
							Log.fucaibiz.error("投注内容拆单发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId,
									partnerTradeId, e);
						}
						if (!ticketReturn.getStatusCode().equals(ConstantsUtil.STATUS_CODE_SUCCESS)) {
							Log.fucaibiz.error("投注号码格式错误,partnerId=%s,ticketId=%s,orderContent=%s", partnerId,
									partnerTradeId, orderContent);
							errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "投注号码格式错误",
									ConstantsUtil.STATUS_CODE_NUMBERFORMAT_ERROR);
							break;
						}
						int ballNum = ticketReturn.getCount();
						long total = MoneyUtil.toCent(ballNum * multiple * 2);
						// 校验投注内容与总金额是否一致
						if (orderMoney != total || multiple <= 0) {
							Log.fucaibiz.error(
									"投注金额与拆单后计算的金额不一致,partnerId=%s,ticketId=%s,orderMoney=%d,realMoney=%d,multiple=%d",
									partnerId, partnerTradeId, orderMoney, total, multiple);
							errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "投注金额错误",
									ConstantsUtil.STATUS_CODE_BETTINGAMOUNT_ERROR);
							break;
						}
						// 校验期号是否可以投注
						ReturnMessage issueMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue",
								lotteryId, issueNo);
						if (!issueMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
							Log.fucaibiz.error("校验期号是否可以投注发生异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId,
									partnerTradeId, issueMsg.getMsg());
							break;
						}
						LotteryIssue issue = (LotteryIssue) issueMsg.getObj();
						String beginTime = issue.getBeginTime();
						// 获取投注截止时间
						String endTime = "SINGLE".equals(playTypeStr) ? issue.getSingleEndTime() : issue
								.getCompoundEndTime();
						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());

						if (DateUtil.compareDateTime(currentTime, endTime) < 0
								|| DateUtil.compareDateTime(beginTime, currentTime) < 0) {
							Log.fucaibiz.error("投注期不在销售时间范围内,partnerId=%s,ticketId=%s,lotteryId=%s,issueNo=%s",
									partnerId, partnerTradeId, lotteryId, issueNo);
							errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "销售期已过 停止销售",
									ConstantsUtil.STATUS_CODE_STOP_SAIL);
							break;
						}
						// 订单编号待续...
						long orderNo = 0;
						// 支付流水号
						String paySerialNumber = OrderConstant.ORDER_PAYSERIANUMBER_PREFIX + orderNo;

						Order order = new Order();
						order.setLotteryId(lotteryId);
						order.setPartnerId(partnerId);
						order.setUserId(userId);
						order.setIssueNo(issueNo);
						order.setOrderNo(orderNo);
						order.setOrderType(Order.OrderType.DIRECT_ORDER.getValue());
						order.setOrderStatus(Order.OrderStatus.WAIT_PAYMENT.getValue());
						order.setTotalAmount(orderMoney);
						order.setOrderContent(orderContent);
						order.setMultiple(multiple);
						order.setPlayType(playType);
						order.setRealName(realName);
						order.setCardNo(cardNo);
						order.setMobile(mobile);
						order.setPaySerialNumber(paySerialNumber);
						order.setTradeId(partnerTradeId);
						order.setTicketNum(ballNum);
						// 创建投注订单
						int isSuccess = orderService.createOrder(order);
						Log.fucaibiz.info("投注订单创建完成,orderNo=%d,returnValue=%d", orderNo, isSuccess);
						if (isSuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
							Log.fucaibiz.error("订单创建失败,交易号已存在.partnerId=%s,ticketId=%s", partnerId, partnerTradeId);
							errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "重复订单",
									ConstantsUtil.STATUS_CODE_REPEAT_ORDER);
							break;
						}
						if (isSuccess > 0) {
							// 支付
							ReturnMessage payMsg = null;
							if (!isPartnerAccount) {
								payMsg = TransactionProcessor.dynamicInvoke("userAccount", "payUserAccount", userId,
										paySerialNumber, orderMoney);
							} else {
								payMsg = TransactionProcessor.dynamicInvoke("partnerAccount", "payPartnerAccount",
										partnerId, orderMoney, paySerialNumber);
							}
							if (!payMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
								Log.fucaibiz.error("订单支付发生异常,orderNo=%d,ticketId=%s,errorMsg=%s", orderNo,
										partnerTradeId, payMsg.getMsg());
								break;
							}
							int isPaySuccess = (Integer) payMsg.getObj();

							Log.fucaibiz.info("订单支付完成,orderNo=%d,paySerialNumber=%s,payTotalMoney=%d,returnValue=%d",
									orderNo, paySerialNumber, orderMoney, isPaySuccess);
							if (isPaySuccess > 0 || isPaySuccess == ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST) {
								// 更新订单状态为已支付状态
								int isPayStatus = orderService.updateOrderStatus(orderNo,
										Order.OrderStatus.HAS_PAYMENT.getValue());
								if (isPayStatus > 0) {
									Log.fucaibiz.info("更新订单状态'已付款'成功,orderNo=%d", orderNo);
									String printBeginTime = issue.getPrintBeginTime();
									String printEndTime = issue.getPrintEndTime();
									currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
									if (DateUtil.compareDateTime(printBeginTime, currentTime) >= 0
											&& DateUtil.compareDateTime(currentTime, printEndTime) > 0) {
										Log.fucaibiz.info("该订单在出票时间范围内,orderNo=%d", orderNo);

										ReturnMessage sendTicketMsg = TransactionProcessor.dynamicInvoke("ticketIssue",
												"sendTicket", EnsapOutTicketChekOrder.ensapOutTicketOrder(order));
										if (!sendTicketMsg.getStatusCode().equals(
												ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
											Log.fucaibiz.error("请求出票发生异常,orderNo=%d,errorMsg=%s", orderNo,
													sendTicketMsg.getMsg());
										} else {
											ResultMessage sendTicket = (ResultMessage) sendTicketMsg.getObj();
											if (sendTicket.getStatusCode() == TicketIssueConstant.STATUS_SEND_TICKET_OK) {
												// 更新订单状态为出票中
												// orderService.updateOrderStatus(orderNo,
												// Order.OrderStatus.IN_TICKET.getValue());
												Log.fucaibiz.info("订单请求出票,提交到线程池成功,orderNo=%d", orderNo);
											}
										}
									} else {
										Log.fucaibiz.info("该订单不在出票时间范围内,所以暂不请求出票,orderNo=%d", orderNo);
									}
								}
							} else {
								// 支付失败,取消订单
								int cancelIsSuccess = orderService.updateOrderStatusAndRemark(orderNo,
										Order.OrderStatus.ORDER_CANCEL.getValue(),
										ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR, "扣款异常或者帐号钱数不足");
								if (cancelIsSuccess > 0) {
									Log.fucaibiz.error("支付时金额发生错误,订单已取消,orderNo=%d,paySerialNumber=%s,returnValue=%d",
											orderNo, paySerialNumber, isPaySuccess);
									errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "扣款异常或者帐号钱数不足",
											ConstantsUtil.STATUS_CODE_DEDUCTMONEY_ERROR);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log.fucaibiz.error("投注订单创建失败,系统错误,即由未捕获的异常导致的错误,partnerId=%,ticketId=%s,errorMsg=%s", partnerId,
					partnerTradeId, e);
			errorMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerId, "系统错误，即由未捕获的异常导致的错误",
					ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
		} finally {
			if (!"".equals(errorMsg)) {
				Log.fucaibiz.error("投注单发生异常,通知合作商,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId, partnerTradeId,
						errorMsg);
				ReturnMessage backMsg = TransactionProcessor.dynamicInvoke("accessBack", "sendAccessBackMessage",
						partnerId, errorMsg);
				if (!backMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)) {
					Log.fucaibiz.error("主动通知合作商'投注错误'发生连接异常,partnerId=%s,ticketId=%s,errorMsg=%s", partnerId,
							partnerTradeId, backMsg.getMsg());
				}
			}
		}
	}

}
