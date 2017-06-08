package com.cqfc.outticket.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.outticket.util.MockConstansUtil;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.Log;

public class Transcode106 {
	public static String fetchUserAccountInfo(String msg, String key, String partnerid,
			String transcode){
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		try {			
			client = new HttpClient();
			method = new PostMethod(MockConstansUtil.CHONG_QIN_CENTER_SERVER_URL);
			Log.run.info("fetchUserAccountInfo,serverUrl=%s,transcode=%s,partnerid=%s,msg=%s", MockConstansUtil.CHONG_QIN_CENTER_SERVER_URL, transcode, partnerid, msg);
			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", partnerid);
			method.setParameter("msg", msg);
			method.setParameter("key", key);
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
			Log.run.error("fetchUserAccountInfo(HttpException)：", e);
		} catch (IOException e) {
			Log.run.error("fetchUserAccountInfo(IOException)：", e);
		} catch (Exception e) {
			Log.run.error("fetchUserAccountInfo(Exception)：", e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);

				} catch (Exception e) {
					Log.run.error("fetchUserAccountInfo(Exception)：",
							e);
				}
				client = null;
			}
			Log.run.debug(
					"fetchUserAccountInfo(msg: %s, result: %s)",
					msg, result);
		}

		return result;
	}
}
