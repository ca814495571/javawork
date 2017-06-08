package com.cqfc.access.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.Log;

@Controller
@RequestMapping("/chongqing")
public class ChongQingTestTask {
	
	/**
	 * 请求
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/verify", produces = { "text/xml;charset=UTF-8" })
	@ResponseBody
	public String verify(String transcode, String msg) throws FileNotFoundException {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;		
		
		try {
			client = new HttpClient();
			method = new PostMethod(TicketIssueConstant.CHONGQING_FUCAI_SERVER_URL);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", TicketIssueConstant.CHONGQING_PARTNERID);
			method.setParameter("msg", msg);
			method.setParameter("key", Digit.sig((transcode+msg).getBytes("utf-8"),
					TicketIssueConstant.CHONGQING_KEY_STORE_PASS, TicketIssueConstant.CHONGQING_ALIALS, ClassPathUtil.getClassPathInputStream(TicketIssueConstant.CHONGQING_PRIVATE_SCRECT_NAME)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(100000);
			managerParams.setSoTimeout(100000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			result = result.substring(0, result.lastIndexOf("</msg>"))+"</msg>";
		} catch (HttpException e) {
			Log.run.error("ChongQingTestTask(HttpException: %s)", e);
		} catch (IOException e) {
			Log.run.error("ChongQingTestTask(IOException: %s)",  e);
		} catch (Exception e) {
			Log.run.error("ChongQingTestTask(Exception: %s)",  e);
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

		return result;
	}
}
