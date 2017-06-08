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

import com.cqfc.access.model.ModelHighLotteryPrizeResult;
import com.cqfc.access.util.ConstantsUtil;
import com.cqfc.access.util.HttpClientUtil;
import com.cqfc.access.util.ParameterUtils;

@Controller
@RequestMapping(value="/test")
public class HighLotteryPrizeResult {
	@RequestMapping(value="/transcodegpczj")
	public String savePersonUI(){				
		return "highLotteryPrizeReuslt";
	}
	
	@RequestMapping(value="/requestgpczj")
	@ResponseBody
	public String save(ModelHighLotteryPrizeResult model) throws UnknownHostException, UnsupportedEncodingException{		
		String serverUrl = ParameterUtils.getParameterValue("serverUrl");
		String channelID = ParameterUtils.getParameterValue("channelID");
		
		String result = "";
		
		CloseableHttpClient httpClient = null;
		HttpGet httpGet = null;
		CloseableHttpResponse  response = null;
		
		try{	
			String lotteryId = model.getLotteryID();
			String date = model.getDate();
			String wareIssue = model.getWareIssue();
			
			httpClient = HttpClientUtil.getHttpClient();
			httpGet = new HttpGet(serverUrl + lotteryId + ConstantsUtil.SEPARATOR_XIEXIAN + channelID + date + ConstantsUtil.SEPARATOR_XIEXIAN + wareIssue + ".txt"); 
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
