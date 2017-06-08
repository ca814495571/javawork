package com.cqfc.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;

public class QueryTicketUtil {

	public String Query(String ticketId) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<msg>"
				+ "<head time=\"1411210471613\" version=\"1.0\" partnerid=\"25000016\" transcode=\"105\"/>" + "<body>"
				+ "<queryticket userid=\"2500000016\" issue=\"2015026\" gameid=\"SSQ\" id=\"080#00860066#" + ticketId
				+ "\"/>" + "</body>" + "</msg>";
//		System.out.println(msg);
		return queryTicket(msg, ticketId);
	}

	public String queryTicket(String msg, String ticketId) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;

		try {
			String serverUrl = "http://119.84.60.81:8080/greatwallweb/main";
			String private_secret_name = "cqfccerts";
			String key_store_pass = "greatCQFC6confidential$";
			String alias = "cqfc";
			String partner_id = "25000016";

			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", partner_id);
			method.setParameter("transcode", "105");
			method.setParameter("msg", msg);
			method.setParameter(
					"key",
					Digit.sig(("105" + msg).getBytes("utf-8"), key_store_pass, alias,
							ClassPathUtil.getClassPathInputStream(private_secret_name)));
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
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
		return result;
	}

}
