package com.cqfc.access.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;

public class TestCode133 {

	
	/**
	 * 服务器接入地址
	 */
	public final static String server_url = "http://127.0.0.1:8080/jweb_access/verification/verify";

	/**
	 * 签名代理名 （alias)
	 */
	public final static String alias = "cqfc";
	/**
	 * 签名密码 （keystorepass)
	 */
	public final static String key_store_pass = "123456";
	/**
	 * 接入渠道的id
	 */
	public final static String partner_id = "00860001";
	
	/**
	 * 当前期号
	 */
	public static String issue_no = "2013022";

	public final static String PARM_TIME = "time";
	public final static String PARM_PARNERID = "partnerid";
	public final static String PARM_KEY = "key";
	public final static String PARM_MSG = "msg";
	public final static String PARM_VERSION = "version";
	public final static String PARM_TRANSCODE = "transcode";

	public final static String DATE_FORMAT = "yyyyMMDDHHmmSS";
	public final static String DEFAULT_VERSION = "1.0";
	public final static String CONVERT_TYPE = "http";
	
	/**
	 * 获取新期方法
	 */
	public void getIssue() {

		String transcode = "133";

		String xml111 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='133' partnerid='00860001' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "         <prizePasswd   oldpasswd=''  userid='28'  newpasswd='23123'/>\n"
				+ "      </body>\n" + "</msg>";

		this.send(xml111, transcode);

	}
	
	
	public boolean send(String msg, String transcode) {
		PostMethod method = null;
		HttpClient client = null;
		long time = System.currentTimeMillis();
		String result = null;
		boolean flag = true;
		int status = 0;

		try {
			String key = Digit.sig((transcode + msg).getBytes("utf-8"),
					key_store_pass, alias, ClassPathUtil.getClassPathInputStream("19_private"));
			client = new HttpClient();
			method = new PostMethod(server_url);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter(PARM_VERSION, "1.0");
			method.setParameter(PARM_PARNERID, partner_id);
			method.setParameter(PARM_MSG, msg);
			method.setParameter(PARM_KEY, key);
			method.setParameter(PARM_TRANSCODE, transcode);
			method.setParameter(PARM_TIME, "200911120000");
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			

		} catch (HttpException e) {

			flag = false;
		} catch (IOException e) {

			flag = false;
		} catch (Exception e) {

			flag = false;
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

			System.out.println(new StringBuilder().append("111返回的消息体内容711:")
					.append(result).toString());

		}
		return flag;

	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {

		TestCode133 test107 = new TestCode133();
		
		test107.getIssue();
		
	}
	
	
	
}
