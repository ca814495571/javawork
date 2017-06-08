package com.cqfc.convertor;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpStatus;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.protocol.ticketissue.OutTicketOrder;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.LotteryUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.ReqPrintXmlUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader000;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.TransactionMsgLoader704;
import com.cqfc.xmlparser.transactionmsg605.Querytype;
import com.jami.util.Log;

public class AbstractConvertor implements IConvertor {

	@Override
	public String convert4out(String ticket) {
		return ticket;
	}

	@Override
	public String convert4out(OutTicketOrder ticket) {
		return ticket.getOrderContent();
	}

	@Override
	public String convert4In(String content) {
		return content;
	}

	@Override
	public void sendTicket(OutTicketOrder outTicket, FucaiPartnerInfo partnerInfo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		int status = 0;
		int count = 1;

		String orderNo = outTicket.getOrderNo();
		try {
			outTicket.setOrderContent(convert4out(outTicket.getOrderContent()));
			String msg = ReqPrintXmlUtil.orderOutTicketToXml(outTicket, partnerInfo);
			Log.run.debug("sendTicket拼接出票请求104,orderNo=%s,msg=%s", orderNo, msg);
			String webPartnerId = partnerInfo.getPartnerId();
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();

			Log.run.info("sendTicket请求出票URL,orderNo=%s,serverUrl=%s", orderNo, serverUrl);

			boolean requestOk = false;
			while (!requestOk) {
				try {
					client = new HttpClient();
					method = new PostMethod(serverUrl);

					method.getParams().setContentCharset("utf-8");
					client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
					method.setParameter("partnerid", webPartnerId);
					method.setParameter("transcode", TicketIssueConstant.TRANSCODE104);
					method.setParameter("msg", msg);
					method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE104 + msg).getBytes("utf-8"),
							keyStorePass, alias, ClassPathUtil.getClassPathInputStream(privateSecretName)));
					HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

					managerParams.setConnectionTimeout(10000);
					managerParams.setSoTimeout(10000);
					status = client.executeMethod(method);
					if (status == HttpStatus.SC_OK) {
						requestOk = true;
					} else {
						closeHttp(method, client);
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					closeHttp(method, client);
					Thread.sleep(5000);
					Log.run.error("请求中心出票发生异常,orderNo=" + orderNo, e);
					Log.error("请求中心出票发生异常,orderNo=" + orderNo, e);
				}
				count++;
				if (count > 2) {
					break;
				}
			}
			if (count > 2 && !requestOk) {
				Log.run.debug("sendTicket连续2次请求出票都连接失败,orderNo=%s", orderNo);
				return;
			}
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			int callBackType = 0;
			if (null != result && !"".equals(result)) {
				result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR)
						+ TicketIssueConstant.RESPONSE_PROCESS_STR_LENGTH);
				Log.run.debug("sendTicket出票请求返回704,orderNo=%s,resultXml=%s", orderNo, result);

				String statusCode = "";
				com.cqfc.xmlparser.transactionmsg704.Msg msg704 = TransactionMsgLoader704.xmlToMsg(result);

				if (TicketIssueConstant.TRANSCODE704.equals(msg704.getHead().getTranscode())) {
					com.cqfc.xmlparser.transactionmsg704.Ticket ticket = msg704.getBody().getTicketorder().getTickets()
							.getTicket().get(0);
					statusCode = ticket.getStatuscode();
					if (statusCode.equals(ConstantsUtil.STATUS_CODE_ISSUE_ERROR)
							|| statusCode.equals(ConstantsUtil.STATUS_CODE_ISSUENO_ERROR)) {

						Log.run.debug("sendTicket出票请求返回期次错误,orderNo=%s,statusCode=%s", orderNo, statusCode);
						String lotteryId = msg704.getBody().getTicketorder().getGameid();
						String issueNo = ticket.getIssue();
						ReturnMessage issueMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue",
								lotteryId, LotteryUtil.convertIssueNo2System(lotteryId, issueNo));
						LotteryIssue issue = (LotteryIssue) issueMsg.getObj();
						String printEndTime = issue.getPrintEndTime();

						String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
						if (DateUtil.compareDateTime(currentTime, printEndTime) <= 0) {
							Log.run.debug("sendTicket704返回期次错误,而且当前时间大于出票截止时间,所以直接通知出票失败,orderNo=%s", orderNo);
							callBackType = TicketIssueConstant.SEND_TICKET_RESULT_FAIL;
						}
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_WAIT_TRADE)
							|| statusCode.equals(ConstantsUtil.STATUS_CODE_TRADING)
							|| statusCode.equals(ConstantsUtil.STATUS_CODE_REPEAT_ORDER)) {
						Log.run.debug("sendTicket704返回结果正常,orderNo=%s,statusCode=%s", orderNo, statusCode);
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_SYSTEM_ERROR)) {
						Log.run.debug("sendTicket704返回系统错误,订单不取消,等待订单定时器处理,orderNo=%s,statusCode=%s", orderNo,
								statusCode);
					} else {
						callBackType = TicketIssueConstant.SEND_TICKET_RESULT_FAIL;
						Log.run.debug("sendTicket704返回出票异常,直接通知出票失败,orderNo=%s,statusCode=%s", orderNo, statusCode);
					}

				} else {
					Log.run.debug("sendTicket返回结果不是704消息体,直接通知出票失败,orderNo=%s", orderNo);
					callBackType = TicketIssueConstant.SEND_TICKET_RESULT_FAIL;
				}
			} else {
				Log.run.debug("sendTicket返回结果为null,等待扫描任务再次发送出票请求,orderNo=%s,resultXml=%s", orderNo, result);
			}

			if (callBackType == TicketIssueConstant.SEND_TICKET_RESULT_FAIL) {
				int lotteryType = OrderUtil.getLotteryCategory(outTicket.getLotteryId());
				if (lotteryType == OrderStatus.LotteryType.NUMBER_GAME.getType()) {
					TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "isTicketSuccess",
							orderNo, callBackType);
				} else if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
					SportPrint failed = new SportPrint();
					failed.setOrderNo(Long.parseLong(orderNo));
					failed.setPrintStatus(TicketIssueConstant.SEND_TICKET_RESULT_FAIL);
					TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "sportOrderAfterPrint",
							failed);
				} else {
					Log.fucaibiz.error("104投注彩种错误,既不属于数字彩,也不属于竞技彩,lotteryId=%s", outTicket.getLotteryId());
				}
			}
		} catch (Exception e) {
			Log.error("sendTicket发生异常,orderNo=" + orderNo, e);
			Log.run.error("sendTicket发生异常,orderNo=" + orderNo, e);
		} finally {
			closeHttp(method, client);
		}
	}

	private void closeHttp(PostMethod method, HttpClient client) {
		if (method != null)
			method.releaseConnection();
		if (client != null) {
			try {
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);
			} catch (Exception e) {
				Log.run.error("sendTicket关闭http连接发生异常", e);
			}
			client = null;
		}
	}

	@Override
	public String queryTicket(OutTicketOrder outTicket,
			FucaiPartnerInfo partnerInfo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;

		try {	
			String msg = ReqPrintXmlUtil.orderQueryTicketToXml(outTicket, partnerInfo);
			String webPartnerId = partnerInfo.getPartnerId();
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();
			
			Log.run.info("queryTicket serverUrl=%s, msg=%s", serverUrl, msg);		
			
			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", webPartnerId);
			method.setParameter("transcode", TicketIssueConstant.TRANSCODE105);
			method.setParameter("msg", msg);
			method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE105+msg).getBytes("utf-8"),
					keyStorePass, alias, ClassPathUtil.getClassPathInputStream(privateSecretName)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			if(result != null && !result.equals("")){
				result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR)+TicketIssueConstant.RESPONSE_PROCESS_STR_LENGTH);
			}
		} catch (HttpException e) {
			Log.run.error("queryTicket(HttpException=%s)", e);
		} catch (IOException e) {
			Log.run.error("queryTicket(IOException=%s)", e);
		} catch (Exception e) {
			Log.run.error("queryTicket(Exception=%s)", e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);

				} catch (Exception e) {
					Log.run.error("queryTicketClient(Exception=%s)", e);
				}
				client = null;
			}
			Log.run.debug("queryTicket(result=%s)", result);
		}
		
		return result;
	}

	@Override
	public void checkTicket(OutTicketOrder outTicket, FucaiPartnerInfo partnerInfo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;

		String orderNo = outTicket.getOrderNo();
		try {
			String msg = ReqPrintXmlUtil.orderQueryTicketToXml(outTicket, partnerInfo);
			Log.run.debug("checkTicket拼接查询出票105,orderNo=%s,msg=%s", orderNo, msg);
			String webPartnerId = partnerInfo.getPartnerId();
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();

			Log.run.info("checkTicket请求出票URL,orderNo=%s,serverUrl=%s", orderNo, serverUrl);

			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", webPartnerId);
			method.setParameter("transcode", TicketIssueConstant.TRANSCODE105);
			method.setParameter("msg", msg);
			method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE105 + msg).getBytes("utf-8"),
					keyStorePass, alias, ClassPathUtil.getClassPathInputStream(privateSecretName)));
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
			managerParams.setConnectionTimeout(20000);
			managerParams.setSoTimeout(20000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));

			if (result != null && !result.equals("")) {
				result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR)
						+ TicketIssueConstant.RESPONSE_PROCESS_STR_LENGTH);
				Log.run.debug("checkTicket请求出票返回605结果,orderNo=%s,xml=%s", orderNo, result);
				dealReturnXml(result, outTicket.getIssueNo(), orderNo, outTicket.getLotteryId());
			}
		} catch (Exception e) {
			Log.run.error("checkTicket发生异常,orderNo=" + orderNo, e);
			Log.error("checkTicket发生异常,orderNo=" + orderNo, e);
		} finally {
			closeHttp(method, client);
		}
	}


	private void dealReturnXml(String xml, String issueNo, String orderNo, String lotteryId) {
		try {
			com.cqfc.xmlparser.transactionmsg605.Msg msg605 = TransactionMsgLoader605.xmlToMsg(xml);
			String statusCode = "";
			int ticketStatus = 0;
			if (TicketIssueConstant.TRANSCODE605.equals(msg605.getHead().getTranscode())) {
				String queryIssueNo = msg605.getBody().getTicketresult().getIssue();
				if (queryIssueNo.equals(issueNo)) {
					statusCode = msg605.getBody().getTicketresult().getStatuscode();
					if (statusCode.equals(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS)) {
						ticketStatus = TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS;
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_RRADE_FAIL)) {
						ticketStatus = TicketIssueConstant.SEND_TICKET_RESULT_FAIL;
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_WAIT_TRADE)) {
						ticketStatus = TicketIssueConstant.SEND_TICKET_RESULT_PROCESSING;
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST)) {
						ticketStatus = TicketIssueConstant.SEND_TICKET_RESULT_NOEXIST_ORDER;
					}
				} else {
					ticketStatus = TicketIssueConstant.SEND_TICKET_RESULT_FAIL;
				}
			} else {
				com.cqfc.xmlparser.transactionmsg000.Msg msg000 = TransactionMsgLoader000.xmlToMsg(xml);
				if (TicketIssueConstant.TRANSCODE000.equals(msg000.getHead().getTranscode())) {
					if (xml.contains(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST)) {
						ticketStatus = TicketIssueConstant.SEND_TICKET_RESULT_NOEXIST_ORDER;
					}
				}
			}
			Log.run.debug("checkTicket返回结果,orderNo=%s,ticketStatus=%d,returnStatusCode=%s", orderNo, ticketStatus,
					statusCode);
			if (ticketStatus > 0) {
				int lotteryType = OrderUtil.getLotteryCategory(lotteryId);
				if (lotteryType == OrderStatus.LotteryType.NUMBER_GAME.getType()) {
					TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "isTicketSuccess",
							orderNo, ticketStatus);
				} else if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
					SportPrint success = OrderUtil.convertMsg2result(msg605, ticketStatus);					
					TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, "sportOrderAfterPrint",
							success);
				} else {
					Log.fucaibiz.error("105查询彩种错误,既不属于数字彩,也不属于竞技彩,lotteryId=%s", lotteryId);
				}
			}
		} catch (Exception e) {
			Log.run.error("checkTicket处理返回结果发生异常,orderNo=" + orderNo, e);
		}
	}

}
