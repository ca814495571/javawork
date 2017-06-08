package com.cqfc.ticketissue.task;

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.FucaiPartnerInfoUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader141;
import com.cqfc.xmlparser.TransactionMsgLoader741content;
import com.cqfc.xmlparser.transactionmsg141.Body;
import com.cqfc.xmlparser.transactionmsg141.Headtype;
import com.cqfc.xmlparser.transactionmsg141.Msg;
import com.cqfc.xmlparser.transactionmsg141.Querytype;
import com.cqfc.xmlparser.transactionmsg741content.Partnerdatebeans;
import com.jami.util.Log;

public class LotteryDrawResultTask141 {
	private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private static final String PARTNER_DATE_START = "<partnerdatebean>";
	private static final String PARTNER_DATE_END = "</partnerdatebean>";
	
	/**
	 * 获取销量统计数据
	 * 
	 * @param lotteryId
	 * @param issueNo
	 */
	public static Partnerdatebeans getFucaiCount(
			String date, FucaiPartnerInfo partnerInfo) {
		try {
			String xml = getFucaiCountResult(date, partnerInfo);
			if (xml.indexOf("transcode=\"741\"") < 0) {
				Log.run.error("get fucai count by day failed, msg=%s", xml);
				return null;
			}

			int start = xml.indexOf(PARTNER_DATE_START);
			int end = xml.lastIndexOf(PARTNER_DATE_END);
			if((start == -1) || (end == -1)){
				return null;
			}
			xml = XML_HEAD + xml.substring(start + PARTNER_DATE_START.length(), end);
			Partnerdatebeans statinfo = TransactionMsgLoader741content.xmlToMsg(xml);
			return statinfo;
		} catch (Exception e) {
			Log.run.error(e.toString());
		}
		return null;
	}

	/**
	 * 获取福彩开奖结果
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String getFucaiCountResult(String date, FucaiPartnerInfo partnerInfo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;

		try {
			String dateWithoutSep = date.replace("-", "");
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();
			String partnerId = partnerInfo.getPartnerId();
			String version = partnerInfo.getVersion();
			
			Log.run.debug("141 serverUrl: %s, partnerId: %s",  
					serverUrl, partnerId);
			
			Msg msg141 = new Msg();
			Headtype head141 = new Headtype();
			head141.setPartnerid(partnerId);
			head141.setTranscode(TicketIssueConstant.TRANSCODE141);
			head141.setVersion(version);
			head141.setTime(DateUtil.getCurrentDateTime());
			Body body141 = new Body();
			Querytype queryType141 = new Querytype();
			queryType141.setDate(dateWithoutSep);
			;
			body141.setPartnerDateQuery(queryType141);
			;
			msg141.setHead(head141);
			msg141.setBody(body141);
			String xmlMsg = TransactionMsgLoader141.msgToXml(msg141);

			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", TicketIssueConstant.TRANSCODE141);
			method.setParameter("partnerid", partnerId);
			method.setParameter("msg", xmlMsg);
			method.setParameter("key", Digit.sig(
					(TicketIssueConstant.TRANSCODE141 + xmlMsg)
							.getBytes("utf-8"), keyStorePass, alias,
					ClassPathUtil.getClassPathInputStream(privateSecretName)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
		} catch (Exception e) {
			Log.run.error(e.toString());
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);

				} catch (Exception e) {
					Log.run.debug("", e);
				}
				client = null;
			}
		}

		return result.substring(0,
				result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR)
						+ TicketIssueConstant.RESPONSE_PROCESS_STR_LENGTH);
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("need one param day");
			return;
		}
		long now = System.currentTimeMillis();
		List<FucaiPartnerInfo> partnerInfoList = FucaiPartnerInfoUtil.getFucaiPartnerInfoList();
		for (FucaiPartnerInfo partnerInfo : partnerInfoList) {
			System.out.println(getFucaiCountResult(args[0], partnerInfo));
		}
		System.out.println("cost " + (System.currentTimeMillis() - now) + "ms");
	}
}
