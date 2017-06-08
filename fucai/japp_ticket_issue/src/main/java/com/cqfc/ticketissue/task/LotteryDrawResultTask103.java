package com.cqfc.ticketissue.task;

import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.FucaiPartnerInfoUtil;
import com.cqfc.util.LotteryUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader103;
import com.cqfc.xmlparser.TransactionMsgLoader703;
import com.cqfc.xmlparser.transactionmsg103.Body;
import com.cqfc.xmlparser.transactionmsg103.Headtype;
import com.cqfc.xmlparser.transactionmsg103.Msg;
import com.cqfc.xmlparser.transactionmsg103.Querytype;
import com.jami.util.Log;



public class LotteryDrawResultTask103 {

	/**
	 * 获取销量统计数据
	 * @param lotteryId
	 * @param issueNo
	 */
	public static com.cqfc.xmlparser.transactionmsg703.Querytype getFucaiCount(String lotteryId, String issueNo, FucaiPartnerInfo partnerInfo) {
		try {
			String xml = getFucaiCountResult(lotteryId, issueNo, partnerInfo);
			if (xml.indexOf("transcode=\"703\"") < 0) {
				Log.run.error("get fucai count by issue failed, msg=%s", xml);
				return null;
			}
			com.cqfc.xmlparser.transactionmsg703.Msg msg = TransactionMsgLoader703.xmlToMsg(xml);
			com.cqfc.xmlparser.transactionmsg703.Querytype statinfo = msg.getBody().getStatinfo();
			return statinfo;
		} catch (Exception e) {
			Log.run.error(e.toString());
		}
		return null;
	}
	/**
	 * 获取销量统计数据
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String getFucaiCountResult(String lotteryId, String issueNo, FucaiPartnerInfo partnerInfo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		int status = 0;
		int count = 1;

		try {
			issueNo = LotteryUtil.convertIssueNo(lotteryId, issueNo);
			String serverUrl = partnerInfo.getServerUrl();
			String privateSecretName = partnerInfo.getPrivateSecretKey();
			String keyStorePass = partnerInfo.getKeyStorePass();
			String alias = partnerInfo.getAliasKey();
			String partnerId = partnerInfo.getPartnerId();
			String version = partnerInfo.getVersion();
			
			Log.run.debug("103 serverUrl: %s, partnerId: %s",  
					serverUrl, partnerId);
			
			boolean requestOk = false;
			while (!requestOk) { 
				try{
					Msg msg103 = new Msg();
					Headtype head103 = new Headtype();
					head103.setPartnerid(partnerId);
					head103.setTranscode(TicketIssueConstant.TRANSCODE103);
					head103.setVersion(version);
					head103.setTime(DateUtil.getCurrentDateTime());
					Body body103 = new Body();
					Querytype queryType103 = new Querytype();
					queryType103.setGameid(lotteryId);
					queryType103.setIssue(issueNo);
					body103.setQueryprizenotice(queryType103);
					msg103.setHead(head103);
					msg103.setBody(body103);
					String xmlMsg = TransactionMsgLoader103.msgToXml(msg103);		
					
					client = new HttpClient();
					method = new PostMethod(serverUrl);
		
					method.getParams().setContentCharset("utf-8");
					client.getParams().setParameter(
							HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
					method.setParameter("transcode", TicketIssueConstant.TRANSCODE103);
					method.setParameter("partnerid", partnerId);
					method.setParameter("msg", xmlMsg);
					method.setParameter("key", Digit.sig((TicketIssueConstant.TRANSCODE103+xmlMsg).getBytes("utf-8"),
							keyStorePass, alias, ClassPathUtil.getClassPathInputStream(privateSecretName)));
					HttpConnectionManagerParams managerParams = client
							.getHttpConnectionManager().getParams();
		
					managerParams.setConnectionTimeout(100000);
					managerParams.setSoTimeout(100000);
					status = client.executeMethod(method);
					if (status == HttpStatus.SC_OK) {
						requestOk = true;
					} else {
						closeHttp(method, client);
						Thread.sleep(60000);
					}
				} catch (Exception e) {
					closeHttp(method, client);
					Thread.sleep(5000);
					Log.run.error("103统计发生异常,lotteryId=%s,issueNo=%s,partnerId=%s,exception=%s", lotteryId, issueNo, partnerInfo.getPartnerId(), e);
				}
				count++;
				if (count > 2) {
					break;
				}
		    }
			if (count > 2 && !requestOk) {
				Log.run.debug("103统计连续2次请求都连接失败,lotteryId=%s,issueNo=%s,partnerId=%s", lotteryId, issueNo, partnerInfo.getPartnerId());
				return "";
			}
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			if (null != result && !"".equals(result)) {
				result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR)
						+ TicketIssueConstant.RESPONSE_PROCESS_STR_LENGTH);
			}
		} catch (Exception e) {
			Log.run.error("103统计发生异常,lotteryId=%s,issueNo=%s,partnerId=%s,exception=%s", lotteryId, issueNo, partnerInfo.getPartnerId(), e);
		} finally {
			closeHttp(method, client);
		}

		return result;
	}
	
	private static void closeHttp(PostMethod method, HttpClient client) {
		if (method != null)
			method.releaseConnection();
		if (client != null) {
			try {
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);
			} catch (Exception e) {
				Log.run.error("sendTicket关闭http连接发生异常", e);
			}
			client = null;
		}
	}
	
	public static void main(String[] args) {
		if(args.length<2){
			System.out.println("need lotteryId and issueNo");
			return;
		}
		long now = System.currentTimeMillis();
		List<FucaiPartnerInfo> partnerInfoList = FucaiPartnerInfoUtil.getFucaiPartnerInfoList();
		for (FucaiPartnerInfo partnerInfo : partnerInfoList) {
			System.out.println(getFucaiCountResult(args[0], args[1], partnerInfo));			
		}
		System.out.println("cost " + (System.currentTimeMillis() - now) + "ms");
	}
}
