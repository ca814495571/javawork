package com.cqfc.ticketissue.task;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.FucaiPartnerInfoUtil;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.Log;

public class TestHttpClientTask implements Runnable{
	
	@Override
	public void run() {
		PostMethod method = null;
		HttpClient client = null;

		try {
			FucaiPartnerInfo partnerInfo = FucaiPartnerInfoUtil.selectFucaiPartnerInfo("123456789");
			String webPartnerId = partnerInfo.getPartnerId();
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();
			serverUrl = "http://10.247.68.25:16014/jweb_out_ticket/out/ticket";

			Log.run.info("checkTicket请求出票URL,serverUrl=%s", serverUrl);

			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", webPartnerId);
			method.setParameter("transcode", TicketIssueConstant.TRANSCODE105);
			method.setParameter("msg", "");
			method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE105 + "").getBytes("utf-8"),
					keyStorePass, alias, ClassPathUtil.getClassPathInputStream(privateSecretName)));
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
			managerParams.setConnectionTimeout(20000);
			managerParams.setSoTimeout(20000);
			client.executeMethod(method);
			new String(method.getResponseBodyAsString().getBytes("utf-8"));

		
		} catch (Exception e) {
			Log.run.error("checkTicket发生异常", e);
			Log.error("checkTicket发生异常", e);
		} finally {
			closeHttp(method, client);
		}
	}

	private void closeHttp(PostMethod method, HttpClient client) {
		if (null != method) {
			method.releaseConnection();
		}
		if (null != client) {
			try {
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);
			} catch (Exception e) {
				Log.run.error("checkTicket关闭http连接发生异常", e);
			}
			client = null;
		}
	}
}
