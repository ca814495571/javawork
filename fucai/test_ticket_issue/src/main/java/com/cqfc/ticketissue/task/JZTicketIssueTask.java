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
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;
import com.jami.util.Log;

public class JZTicketIssueTask {
	
	public static void ticketIssue(String msg) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		int status = 0;

		try {	
//			String serverUrl=ParameterUtils.getParameterValue("server_url");
//			String private_secret_name=ParameterUtils.getParameterValue("private_secret_name");
//			String key_store_pass=ParameterUtils.getParameterValue("key_store_pass");
//			String alias=ParameterUtils.getParameterValue("alias");
//			String partner_id=ParameterUtils.getParameterValue("partner_id");
			
//			String serverUrl= "http://182.254.212.183/jweb_access/verification/verify";
//			String private_secret_name= "19_private";
//			String key_store_pass="123456";
//			String alias="cqfc";
//			String partner_id="00860066";
			
//			String serverUrl= "http://119.84.60.81:8080/greatwallweb/main";
//			String private_secret_name= "19_private";
//			String key_store_pass="123456";
//			String alias="cqfc";
//			String partner_id="00860066";
//			
			
			String serverUrl= "http://119.84.60.81:8080/greatwallweb/main";
			String private_secret_name= "tencentcerts";
			String key_store_pass="TencentAdmin4Fucai";
			String alias="tencent";
			String partner_id="21000021";
			
//			partner.jizong.serverUrl=http://119.84.60.81:8080/greatwallweb/main
//			partner.jizong.sendServerUrl=http://10.247.68.25:16014/jweb_out_ticket/out/ticket
//			partner.jizong.queryServerUrl=http://10.247.68.25:16014/jweb_out_ticket/out/result
//			partner.jizong.testServerUrl=http://10.247.68.220:18080/greatwallweb/main
//			partner.jizong.statisServerUrl=http://10.247.68.25:16014/jweb_out_ticket/out/statistics103
//			partner.jizong.alias=tencent
//			partner.jizong.keyStorePass=TencentAdmin4Fucai
//			partner.jizong.privateSecretName=tencentcerts
//			partner.jizong.partnerId=21000021
//			partner.jizong.userId=2100000021
//			partner.jizong.realName=\u5409\u7965\u5E73
//			partner.jizong.idCard=460007198211070057
//			partner.jizong.phone=18523057877
//			partner.jizong.version=1.0
//			partner.jizong.province=cq
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
			System.out.println("xmlMsg -->" + msg);
			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));	
		
		} catch (HttpException e) {
			Log.run.debug("出票请求结果(HttpException)："+e);
		} catch (IOException e) {
			Log.run.debug("出票请求结果(IOException)："+e);
		} catch (Exception e) {
			Log.run.debug("出票请求结果(Exception)："+e);
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
			System.out.println("出票请求结果(result)："+result);
			Log.run.debug("出票请求结果(result)："+result);
		}
		
	}
}
