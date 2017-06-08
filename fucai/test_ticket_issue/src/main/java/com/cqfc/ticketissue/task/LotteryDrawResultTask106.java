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
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.xmlparser.TransactionMsgLoader106;
import com.cqfc.xmlparser.transactionmsg106.Body;
import com.cqfc.xmlparser.transactionmsg106.Headtype;
import com.cqfc.xmlparser.transactionmsg106.Msg;
import com.cqfc.xmlparser.transactionmsg106.Querytype;




public class LotteryDrawResultTask106 {
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
		String transcode = "106";
		try {
			String serverUrl=ParameterUtils.getParameterValue("server_url");
			String private_secret_name=ParameterUtils.getParameterValue("private_secret_name");
			String key_store_pass=ParameterUtils.getParameterValue("key_store_pass");
			String alias=ParameterUtils.getParameterValue("alias");
			String partner_id=ParameterUtils.getParameterValue("partner_id");
			String version=ParameterUtils.getParameterValue("version");
			String userId=ParameterUtils.getParameterValue("user_id");
			
			Msg msg106 = new Msg();
			Headtype head106 = new Headtype();
			head106.setPartnerid(partner_id);
			head106.setTranscode(transcode);
			head106.setVersion(version);
			head106.setTime(DateUtil.getCurrentDateTime());
			Body body106 = new Body();
			Querytype queryType106 = new Querytype();
			queryType106.setParmtype("userid");
			queryType106.setValue(userId);
			body106.setQueryuser(queryType106);
			msg106.setHead(head106);
			msg106.setBody(body106);
			String xmlMsg = TransactionMsgLoader106.msgToXml(msg106);

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
