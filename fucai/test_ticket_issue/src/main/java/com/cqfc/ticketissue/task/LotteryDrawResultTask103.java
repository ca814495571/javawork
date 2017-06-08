package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.ticketissue.util.ChongQingTicketIssueConst;
import com.cqfc.ticketissue.util.ParameterUtils;
import com.cqfc.ticketissue.util.TencentTicketIssueConst;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Configuration;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.xmlparser.TransactionMsgLoader103;
import com.cqfc.xmlparser.transactionmsg103.Body;
import com.cqfc.xmlparser.transactionmsg103.Headtype;
import com.cqfc.xmlparser.transactionmsg103.Msg;
import com.cqfc.xmlparser.transactionmsg103.Querytype;



public class LotteryDrawResultTask103 {
	/**
	 * 获取福彩开奖结果
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String findLotteryDrawResult(String lotteryId, String issueNo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		boolean flag = true;
		int status = 0;
		String transcode = "103";
		try {
			String serverUrl=ParameterUtils.getParameterValue("server_url");
			String private_secret_name=ParameterUtils.getParameterValue("private_secret_name");
			String key_store_pass=ParameterUtils.getParameterValue("key_store_pass");
			String alias=ParameterUtils.getParameterValue("alias");
			String partner_id=ParameterUtils.getParameterValue("partner_id");
			
			Msg msg103 = new Msg();
			Headtype head103 = new Headtype();
			head103.setPartnerid(partner_id);
			head103.setTranscode(transcode);
			head103.setVersion("1.0");
			head103.setTime(DateUtil.getCurrentDateTime());
			Body body103 = new Body();
			Querytype queryType103 = new Querytype();
			queryType103.setGameid(lotteryId);
			queryType103.setIssue(issueNo);
			body103.setQueryprizenotice(queryType103);
			msg103.setHead(head103);
			msg103.setBody(body103);
			String xmlMsg = TransactionMsgLoader103.msgToXml(msg103);

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

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
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
	
	
	public static void main(String[] args) {
		findLotteryDrawResult("SSQ", "2014102");
	}
}
