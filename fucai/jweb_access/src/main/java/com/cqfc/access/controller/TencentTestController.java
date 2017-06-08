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
import com.cqfc.util.Configuration;
import com.cqfc.util.Digit;

@Controller
@RequestMapping("/tencent")
public class TencentTestController {
	/**
	 * 鉴权 验证 分发
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
		int status = 0;
		try {
			String serverUrl=Configuration.getConfigValue("server_url");
			String private_secret_name=Configuration.getConfigValue("private_secret_name");
			String key_store_pass=Configuration.getConfigValue("key_store_pass");
			String alias=Configuration.getConfigValue("alias");
			String partner_id=Configuration.getConfigValue("partner_id");
			
			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", partner_id);
			method.setParameter("msg", msg);
			method.setParameter("key", Digit.sig((transcode+msg).getBytes("utf-8"),
					key_store_pass, alias, ClassPathUtil.getClassPathInputStream(private_secret_name)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			result = result.substring(0, result.lastIndexOf("</msg>"))+"</msg>";
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
