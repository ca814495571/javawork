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
import com.cqfc.util.LotteryUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader102;
import com.cqfc.xmlparser.transactionmsg102.Body;
import com.cqfc.xmlparser.transactionmsg102.Headtype;
import com.cqfc.xmlparser.transactionmsg102.Msg;
import com.cqfc.xmlparser.transactionmsg102.Querytype;
import com.jami.util.Log;

public class LotteryDrawResultTask {
	/**
	 * 获取福彩开奖结果
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String findLotteryDrawResult(String lotteryId, String issueNo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;

		issueNo = LotteryUtil.convertIssueNo(lotteryId, issueNo);
		try {
			FucaiPartnerInfo partnerInfo = FucaiPartnerInfoUtil.selectDefaultFucaiPartnerInfo();
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();
			String partnerId = partnerInfo.getPartnerId();
			String version = partnerInfo.getVersion();
			String province = partnerInfo.getProvince();
			
			Log.run.debug("102 serverUrl: %s, partnerId: %s",  
					serverUrl, partnerId);

			Msg msg102 = new Msg();
			Headtype head102 = new Headtype();
			head102.setPartnerid(partnerId);
			head102.setTranscode(TicketIssueConstant.TRANSCODE102);
			head102.setVersion(version);
			head102.setTime(DateUtil.getCurrentDateTime());
			Body body102 = new Body();
			Querytype queryType102 = new Querytype();
			queryType102.setGameid(lotteryId);
			queryType102.setIssue(issueNo);
			queryType102.setPrivance(province);
			body102.setQueryprizenotice(queryType102);
			msg102.setHead(head102);
			msg102.setBody(body102);
			String xmlMsg = TransactionMsgLoader102.msgToXml(msg102);

			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", TicketIssueConstant.TRANSCODE102);
			method.setParameter("partnerid", partnerId);
			method.setParameter("msg", xmlMsg);
			method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE102 + xmlMsg).getBytes("utf-8"),
					keyStorePass, alias, ClassPathUtil.getClassPathInputStream(privateSecretName)));
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(40000);
			managerParams.setSoTimeout(40000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			if (result != null && !result.equals("")) {
				result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR) + TicketIssueConstant.RESPONSE_PROCESS_STR_LENGTH)
						;
			}
		} catch (HttpException e) {
			Log.run.error("findLotteryDrawResult(lotteryId: %s, issueNo: %s, HttpException: %s)", lotteryId, issueNo, e);
		} catch (IOException e) {
			Log.run.error("findLotteryDrawResult(lotteryId: %s, issueNo: %s, IOException: %s)", lotteryId, issueNo, e);
		} catch (Exception e) {
			Log.run.error("findLotteryDrawResult(lotteryId: %s, issueNo: %s, Exception: %s)", lotteryId, issueNo, e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);

				} catch (Exception e) {
					Log.run.error("findLotteryDrawResultClient(Exception)：", e);
				}
				client = null;
			}
			Log.run.debug("findLotteryDrawResult(lotteryId: %s, issueNo: %s, result: %s)", lotteryId, issueNo, result);
		}

		return result;
	}
	public static void main(String[] args) {
		if(args.length<2){
			System.out.println("need lotteryId and issueNo");
			return;
		}
		long now = System.currentTimeMillis();
		System.out.println(findLotteryDrawResult(args[0], args[1]));
		System.out.println("cost " + (System.currentTimeMillis() - now) + "ms");
	}
}
