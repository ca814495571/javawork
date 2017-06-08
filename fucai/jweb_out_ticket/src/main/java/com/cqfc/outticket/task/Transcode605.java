package com.cqfc.outticket.task;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.outticket.util.ParameterUtils;
import com.jami.util.Log;

public class Transcode605 {
	
	public static void ticketCallBack(String msg) {
		
		PostMethod method = null;
		HttpClient client = null;

		String callbackUrl = ParameterUtils.getParameterValue("callback_server_url");
		String partnerId = ParameterUtils.getParameterValue("partner_id");

		try {
			client = new HttpClient();
			method = new PostMethod(callbackUrl);
			Log.run.info("ticketCallBack请求出票URL,serverUrl=%s", callbackUrl);
			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", partnerId);
			method.setParameter("transcode", "605");
			method.setParameter("msg", msg);
			method.setParameter("key", "");
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			client.executeMethod(method);
		} catch (Exception e) {
			Log.run.error("ticketCallBack发生异常, msg: %s, exception: %s", msg, e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);

				} catch (Exception e) {
					e.printStackTrace();
				}
				client = null;
			}
		}
	}
}
