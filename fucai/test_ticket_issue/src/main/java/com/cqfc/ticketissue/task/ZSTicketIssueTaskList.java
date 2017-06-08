package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.ticketissue.util.ChongQingTicketIssueConst;
import com.cqfc.ticketissue.util.WriteResultInfo;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;
import com.jami.util.Log;

public class ZSTicketIssueTaskList {
	
	public static void ticketIssue(String msg) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		int status = 0;	
		String RIGHT_FLAG = "statuscode=\"0001\"";
		
		try {	
//			String serverUrl=ParameterUtils.getParameterValue("server_url");
//			String private_secret_name=ParameterUtils.getParameterValue("private_secret_name");
//			String key_store_pass=ParameterUtils.getParameterValue("key_store_pass");
//			String alias=ParameterUtils.getParameterValue("alias");
//			String partner_id=ParameterUtils.getParameterValue("partner_id");
			
			String serverUrl= "http://182.254.212.183/jweb_access/verification/verify";
			String private_secret_name= "19_private";
			String key_store_pass="123456";
			String alias="cqfc";
			String partner_id="00860066";
			
			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", partner_id);
			method.setParameter("transcode", ChongQingTicketIssueConst.TRANSCODE104);
			method.setParameter("msg", msg);
			method.setParameter("key", Digit.sig((ChongQingTicketIssueConst.TRANSCODE104+msg).getBytes("utf-8"),
					key_store_pass, alias, ClassPathUtil.getClassPathInputStream(private_secret_name)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();
			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));	
			
//		    WriteResultInfo.writeResult(status, result, msg);
			if (status == 200) {
				if (result != null && !"".equals(result)
						&& result.contains(RIGHT_FLAG)) {
					Log.fucaibiz.debug("msg: " + msg);					
					Log.fucaibiz.debug("out ticket result: " + result);
				} else {
					Log.run.error("msg: " + msg);					
					Log.run.error("out ticket result: " + result);			
					System.out.println("out ticket result: " + result);
				}
			} else {
				Log.run.error("out ticket result: " + result);
				System.out.println("out ticket result(status): " + status
						+ ", msg: " + msg);
			}
		} catch (HttpException e) {
			System.out.println("out ticket result(HttpException): " + msg + e);
			Log.run.error("out ticket result(HttpException), msg: %s, exception: ", msg, e);
		} catch (IOException e) {
			System.out.println("out ticket result(IOException): " + msg + e);
			Log.run.error("out ticket result(IOException), msg: %s, exception: ", msg, e);
		} catch (Exception e) {
			System.out.println("out ticket result(Exception): " + msg + e);
			Log.run.error("out ticket result(Exception), msg: %s, exception: ", msg, e);
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
		}
		
	}
}
