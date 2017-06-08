package com.cqfc.accessback.task;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.accessback.util.ConstUtil;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.TicketIssueConstant;
import com.jami.util.Log;

public class AccessBackTask implements Runnable {

	private LotteryPartner partner;
	private String msg;

	public AccessBackTask(LotteryPartner partner, String msg) {
		this.partner = partner;
		this.msg = msg;
	}

	@Override
	public void run() {
		PostMethod method = null;
		HttpClient client = null;
		int status = 0;
		String cbUrl = partner.getCallbackUrl();
		String partnerId = partner.getPartnerId();
		try {
			if(null != cbUrl && !"".equals(cbUrl)){
				Log.run.info("sendAccessBackMessage(url: %s,partnerId: %s, msg: %s)", cbUrl, partnerId, msg);
				client = new HttpClient();
				method = new PostMethod(getUrlRandom(cbUrl));
				method.getParams().setContentCharset("utf-8");	
				HttpConnectionManagerParams managerParams = client
						.getHttpConnectionManager().getParams();
				method.setParameter("transcode", TicketIssueConstant.TRANSCODE605);
				method.setParameter("partnerid", partnerId);
				method.setParameter("msg", msg);
				method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE605 + msg).getBytes("utf-8"),
						partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey())));
				Log.run.info("dig key=%s", Digit.sig((TicketIssueConstant.TRANSCODE605 + msg).getBytes("utf-8"),
						partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey())));
				checkPartnerAddParameter(partnerId, method);
				// 设置恢复策略，在发生异常时候将自动重试3次
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler(3, true));
				managerParams.setConnectionTimeout(10000);
				managerParams.setSoTimeout(10000);
				status = client.executeMethod(method);
				int count = 0;
				while (status != HttpStatus.SC_OK && count < 3) {
					client.executeMethod(method);
					count++;
				}
				method.getResponseBodyAsString();
			}
		} catch (HttpException e) {
			Log.run.error("accessBackTask(HttpException) : ", e);
		} catch (IOException e) {
			Log.run.error("accessBackTask(IOException) : ", e);
		} catch (Exception e) {
			Log.run.error("accessBackTask(Exception) : ",  e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);
				} catch (Exception e) {
					Log.run.error("accessBackTaskClient(Exception) : ",  e);
				}
				client = null;
			}
		}

	}
	
	private void checkPartnerAddParameter(String partnerId, PostMethod method) {
		if(partnerId.equals(ConstUtil.PARTNERID_TENCENT)){
			method.setParameter("c", ConstUtil.TENCENT_CB_C);
			method.setParameter("m", ConstUtil.TENCENT_CB_M);
		}
		
	}

	/**
	 * 随机获取一个回调url
	 * @param cbUrl
	 * @return
	 */
	private String getUrlRandom(String cbUrl){
		String[] cbUrlSplit = cbUrl.split(",");
		if(cbUrlSplit.length == 1){
			return cbUrl;
		}
		int randomNum = new Random().nextInt(cbUrlSplit.length);
		
		return cbUrlSplit[randomNum];
	}
}
