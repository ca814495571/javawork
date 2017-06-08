package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.ticketissue.util.ParameterUtils;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader000;
import com.cqfc.xmlparser.TransactionMsgLoader108;
import com.cqfc.xmlparser.transactionmsg108.Body;
import com.cqfc.xmlparser.transactionmsg108.Headtype;
import com.cqfc.xmlparser.transactionmsg108.Msg;
import com.cqfc.xmlparser.transactionmsg108.Querytype;




public class LotteryDrawResultTask108 {
	/**
	 * 获取福彩开奖结果
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String findLotteryDrawResult() {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		boolean flag = true;
		int status = 0;
		String transcode = "108";
		try {
			
			String serverUrl=ParameterUtils.getParameterValue("server_url");
			String private_secret_name=ParameterUtils.getParameterValue("private_secret_name");
			String key_store_pass=ParameterUtils.getParameterValue("key_store_pass");
			String alias=ParameterUtils.getParameterValue("alias");
			String partner_id=ParameterUtils.getParameterValue("partner_id");
			String version=ParameterUtils.getParameterValue("version");
			String userId=ParameterUtils.getParameterValue("user_id");

			Msg msg108 = new Msg();
			Headtype head108 = new Headtype();
			head108.setPartnerid(partner_id);
			head108.setTranscode(transcode);
			head108.setVersion(version);
			head108.setTime(DateUtil.getCurrentDateTime());
			Body body108 = new Body();
			Querytype queryType108 = new Querytype();
			queryType108.setUserid(userId);
			body108.setUseraccount(queryType108);
			msg108.setHead(head108);
			msg108.setBody(body108);
			String xmlMsg = TransactionMsgLoader108.msgToXml(msg108);

			System.out.println("xmlMsg -->" + xmlMsg);
			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", partner_id);
			method.setParameter("msg", xmlMsg);
			method.setParameter("key", Digit.sig((transcode+xmlMsg).getBytes("utf-8"),
					 key_store_pass, alias, ClassPathUtil.getClassPathInputStream(private_secret_name)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);

			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR))
					+ TicketIssueConstant.RESPONSE_PROCESS_STR;
			com.cqfc.xmlparser.transactionmsg000.Msg msg000 = TransactionMsgLoader000.xmlToMsg(result);
			if (TicketIssueConstant.TRANSCODE000.equals(msg000.getHead().getTranscode())) {
				if(result.contains(ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST)){
					System.out.println("sadfdasfsd");
				}
			}
		} catch (HttpException e) {
			flag = false;
		} catch (IOException e) {
			flag = false;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);

				} catch (Exception e) {
					e.printStackTrace();

				}
				client = null;
			}
			System.out.println(new StringBuilder().append(",返回的消息体内容:")
					.append(result).toString());

		}

		return result;
	}
}
