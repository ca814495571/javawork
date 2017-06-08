package com.cqfc.access.controller;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.access.util.ConstantsUtil;
import com.cqfc.access.util.HttpClientUtil;
import com.cqfc.access.util.MD5Utils;
import com.cqfc.access.util.ParameterUtils;



@Controller
@RequestMapping(value="/test")
public class TranscodeController306 {
	
	@RequestMapping(value="/transcode306")
	@ResponseBody
	public String save() throws UnknownHostException, UnsupportedEncodingException{		
		String serverUrl = ParameterUtils.getParameterValue("serverUrl");
		String channelID = ParameterUtils.getParameterValue("channelID");
		String key = ParameterUtils.getParameterValue("key");		
		String sign = MD5Utils.getMD5(channelID + key);
		String result = "";
		
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		CloseableHttpResponse  response = null;
		
		try{
			httpClient = HttpClientUtil.getHttpClient();
			httpPost = new HttpPost(serverUrl); 
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(ConstantsUtil.HTTP_SOCKET_TIME_OUT)
					.setConnectTimeout(ConstantsUtil.HTTP_CONNECTION_TIME_OUT)
					.setConnectionRequestTimeout(ConstantsUtil.HTTP_CONNECTION_REQUEST_TIME_OUT).build();
			
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); 
			// 绑定到请求 Entry		           
			formParams.add(new BasicNameValuePair("transcode", ConstantsUtil.TRANSCODE306));
			formParams.add(new BasicNameValuePair("ChannelID", channelID));
			formParams.add(new BasicNameValuePair("Sign", sign));
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
			httpPost.setConfig(requestConfig);
			
			response = httpClient.execute(httpPost);
			result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
		}

		return result;
	}
}
