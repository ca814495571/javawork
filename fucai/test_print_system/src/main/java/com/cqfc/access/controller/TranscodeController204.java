package com.cqfc.access.controller;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import org.apache.http.Consts;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.access.model.Model204;
import com.cqfc.access.util.ConstantsUtil;
import com.cqfc.access.util.DateUtil;
import com.cqfc.access.util.HttpClientUtil;
import com.cqfc.access.util.ParameterUtils;



@Controller
@RequestMapping(value="/test")
public class TranscodeController204 {
	
	
	@RequestMapping(value="/transcode204")
	@ResponseBody
	public String save(Model204 model204) throws UnknownHostException, UnsupportedEncodingException{		
		String serverUrl204 = ParameterUtils.getParameterValue("serverUrl204");
		
		String result = "";
		
		CloseableHttpClient httpClient = null;
		HttpGet httpGet = null;
		CloseableHttpResponse  response = null;
		
		try{
			String lotteryId = model204.getLotteryID();
			String date = model204.getDate();
			if(date == null || "".equals(date)){
				date = DateUtil.getDateyyyyMMdd();
			}
			String fileName = lotteryId + ConstantsUtil.SEPARATOR_XIAHUAXIAN + date;
		
			httpClient = HttpClientUtil.getHttpClient();
			httpGet = new HttpGet(serverUrl204 + fileName); 
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(ConstantsUtil.HTTP_SOCKET_TIME_OUT)
					.setConnectTimeout(ConstantsUtil.HTTP_CONNECTION_TIME_OUT)
					.setConnectionRequestTimeout(ConstantsUtil.HTTP_CONNECTION_REQUEST_TIME_OUT).build();
			
			httpGet.setConfig(requestConfig);
			
			response = httpClient.execute(httpGet);
			result = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpGet.releaseConnection();
		}

		return result;
	}
}
