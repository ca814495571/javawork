package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
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
import com.cqfc.xmlparser.TransactionMsgLoader106;
import com.cqfc.xmlparser.transactionmsg106.Body;
import com.cqfc.xmlparser.transactionmsg106.Headtype;
import com.cqfc.xmlparser.transactionmsg106.Msg;
import com.cqfc.xmlparser.transactionmsg106.Querytype;
import com.jami.util.Log;

public class LotteryDrawResultTask106 {

	/**
	 * 查询用户信息
	 * 
	 * @return
	 */
	public static String findUserInfo106(String parmType, String value) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		String transcode = "106";
		try {
			FucaiPartnerInfo partnerInfo = FucaiPartnerInfoUtil.selectFucaiPartnerInfoByUserId(value);
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();
			String partnerId = partnerInfo.getPartnerId();
			String version = partnerInfo.getVersion();
			
			Log.run.debug("106 serverUrl: %s, partnerId: %s",  
					serverUrl, partnerId);
			
			Msg msg106 = new Msg();
			Headtype head106 = new Headtype();
			head106.setPartnerid(partnerId);
			head106.setTranscode(transcode);
			head106.setVersion(version);
			head106.setTime(DateUtil.getCurrentDateTime());
			Body body106 = new Body();
			Querytype queryType106 = new Querytype();
			queryType106.setParmtype(parmType);
			queryType106.setValue(value);
			body106.setQueryuser(queryType106);
			msg106.setHead(head106);
			msg106.setBody(body106);
			String xmlMsg = TransactionMsgLoader106.msgToXml(msg106);

			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", partnerId);
			method.setParameter("msg", xmlMsg);
			method.setParameter("key", Digit.sig(
					(transcode + xmlMsg).getBytes("utf-8"), keyStorePass,
					alias,
					ClassPathUtil.getClassPathInputStream(privateSecretName)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			if (result != null && !result.equals("")) {
				result = result.substring(0, result
						.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR))
						+ TicketIssueConstant.RESPONSE_PROCESS_STR;
			}
		} catch (HttpException e) {
			Log.run.error("findLotteryDrawResult106(HttpException)：", e);
		} catch (IOException e) {
			Log.run.error("findLotteryDrawResult106(IOException)：", e);
		} catch (Exception e) {
			Log.run.error("findLotteryDrawResult106(Exception)：", e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);

				} catch (Exception e) {
					Log.run.error("findLotteryDrawResultClient106(Exception)：",
							e);
				}
				client = null;
			}
			Log.run.debug(
					"findLotteryDrawResult106(parmType: %s, value: %s, result: %s)",
					parmType, value, result);
		}

		return result;
	}
}
