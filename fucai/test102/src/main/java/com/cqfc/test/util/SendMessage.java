package com.cqfc.test.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;

public class SendMessage {
	public final static String server_url = "http://182.254.212.164/jweb_access/verification/verify";
	public final static String private_scret_url = SendMessage.getClassPath() + "19_private";
	public final static String alias = "cqfc";
	public final static String key_store_pass = "123456";
	public final static String PARM_TIME = "time";
	public final static String PARM_PARNERID = "partnerid";
	public final static String PARM_KEY = "key";
	public final static String PARM_MSG = "msg";
	public final static String PARM_VERSION = "version";
	public final static String PARM_TRANSCODE = "transcode";
	public final static String DATE_FORMAT = "yyyyMMDDHHmmSS";
	public final static String DEFAULT_VERSION = "1.0";
	public final static String CONVERT_TYPE = "http";

	public String send(String msg, String transcode, String partner_id) {
		PostMethod method = null;
		HttpClient client = null;
		String str = null;
		String result = null;
		@SuppressWarnings("unused")
		int status = 0;
		try {
			String key = Digit.sig((transcode + msg).getBytes("utf-8"), key_store_pass, alias,
					ClassPathUtil.getClassPathInputStream("19_private"));
			client = new HttpClient();
			method = new PostMethod(server_url);
			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter(PARM_VERSION, "1.0");
			method.setParameter(PARM_PARNERID, partner_id);
			method.setParameter(PARM_MSG, msg);
			method.setParameter(PARM_KEY, key);
			method.setParameter(PARM_TRANSCODE, transcode);
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
			managerParams.setConnectionTimeout(500000000);
			managerParams.setSoTimeout(500000000);
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			str = new StringBuilder().append(transcode + "返回的消息体内容:").append(result).toString();
		} catch (HttpException e) {
			System.out.println("HttpException");
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception");
		} finally {
			if (method != null && client != null) {
				method.releaseConnection();
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);
				method = null;
				client = null;
			}
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				client = null;
			}
		}
		return str;
	}
	/**
	 * 得到当前工程的classPath
	 * 
	 * @return
	 */
	public static String getClassPath() {
		return ClassPathUtil.class.getResource("/").getPath();
	}

}
