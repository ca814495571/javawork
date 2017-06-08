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

public class CheckIssueTask {	
	public static String checkTicketChuPiao(String msg){
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		boolean flag = true;
		int status = 0;

		try {
			String serverUrl=ParameterUtils.getParameterValue("server_url");
			String private_secret_name=ParameterUtils.getParameterValue("private_secret_name");
			String key_store_pass=ParameterUtils.getParameterValue("key_store_pass");
			String alias=ParameterUtils.getParameterValue("alias");
			String partner_id=ParameterUtils.getParameterValue("partner_id");
			
			client = new HttpClient();
			method = new PostMethod(serverUrl);
			
			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("partnerid", partner_id);
			method.setParameter("transcode", ChongQingTicketIssueConst.TRANSCODE105);
			method.setParameter("msg", msg);
			method.setParameter("key", Digit.sig((ChongQingTicketIssueConst.TRANSCODE105+msg).getBytes("utf-8"),
					key_store_pass, alias, ClassPathUtil.getClassPathInputStream(private_secret_name)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();
			
			System.out.println("xmlMsg -->" + msg);
			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			
		} catch (HttpException e) {
			System.out.println("查询出票请求(HttpException)："+e);
			flag = false;
		} catch (IOException e) {
			System.out.println("查询出票请求(IOException)："+e);
			flag = false;
		} catch (Exception e) {
			System.out.println("查询出票请求(Exception)："+e);
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
			System.out.println("查询出票请求(result)："+result);
		}
		
		return result;
	}
}
