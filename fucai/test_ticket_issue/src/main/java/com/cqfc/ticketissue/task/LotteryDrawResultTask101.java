package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.ticketissue.util.ChongQingTicketIssueConst;
import com.cqfc.ticketissue.util.TencentTicketIssueConst;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Configuration;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.xmlparser.TransactionMsgLoader101;
import com.cqfc.xmlparser.transactionmsg101.Body;
import com.cqfc.xmlparser.transactionmsg101.Headtype;
import com.cqfc.xmlparser.transactionmsg101.Msg;
import com.cqfc.xmlparser.transactionmsg101.Querytype;


public class LotteryDrawResultTask101 {
	
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
		String transcode = "101";
		try {
			String serverUrl=Configuration.getConfigValue("server_url");
			String private_secret_name=Configuration.getConfigValue("private_secret_name");
			String key_store_pass=Configuration.getConfigValue("key_store_pass");
			String alias=Configuration.getConfigValue("alias");
			String partner_id=Configuration.getConfigValue("partner_id");
			String version=Configuration.getConfigValue("version");
			String province=Configuration.getConfigValue("province");
			
			Msg msg101 = new Msg();
			Headtype head101 = new Headtype();
			head101.setPartnerid(partner_id);
			head101.setTranscode(transcode);
			head101.setVersion(version);
			head101.setTime(DateUtil.getCurrentDateTime());
			Body body101 = new Body();
			Querytype queryType101 = new Querytype();
			queryType101.setGameid(lotteryId);
			queryType101.setIssueno(issueNo);
			queryType101.setProvince("");;
			body101.setQueryissue(queryType101);
			msg101.setHead(head101);
			msg101.setBody(body101);
			String xmlMsg = TransactionMsgLoader101.msgToXml(msg101);

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
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
		} catch (HttpException e) {
		} catch (IOException e) {
		} catch (Exception e) {
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
