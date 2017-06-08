package com.cqfc.ticketissue.task;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.jami.util.Log;

public class ZJSYXWSnatchDrawResultTask {
	private final static String SNATCH_SERVER_URL = "http://10.247.68.220:18081/win/get115.asp?t=";
	private final static String SEPRARE_RESULT = "\\$\\$\\$";
	
	/**
	 * 获取开奖结果
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String findLotteryDrawResult(String issueNo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;

		try {
			
			String serverUrl = SNATCH_SERVER_URL + System.currentTimeMillis();
			client = new HttpClient();
			method = new PostMethod(serverUrl);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout(40000);
			managerParams.setSoTimeout(400000);
			client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			
			Log.run.debug("findLotteryDrawResult(result: %s, issueNo: %s)", result, issueNo);
			if(result.indexOf(issueNo.substring(2)) == -1){
				return "";
			}
			
			String[] resultSplit = result.split(SEPRARE_RESULT);
			
			result  = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<msg><head transcode=\"702\" partnerid=\"21000021\" version=\"1.0\" time=\"20150415155055\"/>"+
					"<body><prizeinfo gameid=\"ZJSYXW\" issue=\"20"+resultSplit[3]+"\" prizeball=\""+resultSplit[4].replace(" ", ",")+"\" status=\"4\" prizepool=\"445522068\" province=\"cq\"><levelinfos>"+
					"<levelinfo level=\"1\" name=\"任选二\" money=\"6\" count=\"0\"/>"+
					"<levelinfo level=\"2\" name=\"任选三\" money=\"19\" count=\"0\"/>"+
					"<levelinfo level=\"3\" name=\"任选四\" money=\"78\" count=\"0\"/>"+
					"<levelinfo level=\"4\" name=\"任选五\" money=\"540\" count=\"0\"/>"+
					"<levelinfo level=\"5\" name=\"任选六\" money=\"90\" count=\"0\"/>"+
					"<levelinfo level=\"6\" name=\"任选七\" money=\"26\" count=\"0\"/>"+
					"<levelinfo level=\"7\" name=\"任选八\" money=\"9\" count=\"0\"/>"+
					"<levelinfo level=\"8\" name=\"前一直选\" money=\"13\" count=\"0\"/>"+
					"<levelinfo level=\"9\" name=\"前二直选\" money=\"130\" count=\"0\"/>"+
					"<levelinfo level=\"10\" name=\"前二组选\" money=\"65\" count=\"0\"/>"+
					"<levelinfo level=\"11\" name=\"前三直选\" money=\"1170\" count=\"0\"/>"+
					"<levelinfo level=\"11\" name=\"前三组选\" money=\"195\" count=\"0\"/>"+
					"</levelinfos><saleinfos><saleinfo type=\"2\" typename=\"cq\" money=\"0\"/></saleinfos></prizeinfo></body></msg>";
		} catch (Exception e) {
			Log.run.error("findLotteryDrawResult(lotteryId: %s, issueNo: %s, Exception: %s)", "ZJSYXW", issueNo, e);
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {
				try {
					((SimpleHttpConnectionManager) client.getHttpConnectionManager()).closeIdleConnections(0);

				} catch (Exception e) {
					Log.run.error("findLotteryDrawResultClient(lotteryId: %s, issueNo: %s, Exception): ", "ZJSYXW", issueNo, e);
				}
				client = null;
			}
			Log.run.debug("findLotteryDrawResult(lotteryId: %s, issueNo: %s, result: %s)", "ZJSYXW", issueNo, result);
		}

		return result;
	}
	public static void main(String[] args) {
		findLotteryDrawResult("");
	}
}
