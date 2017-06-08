package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.ticketissue.util.ChongQingTicketIssueConst;
import com.cqfc.ticketissue.util.LotteryUtil;
import com.cqfc.ticketissue.util.TencentTicketIssueConst;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader102;
import com.cqfc.xmlparser.transactionmsg102.Body;
import com.cqfc.xmlparser.transactionmsg102.Headtype;
import com.cqfc.xmlparser.transactionmsg102.Msg;
import com.cqfc.xmlparser.transactionmsg102.Querytype;

public class LotteryDrawResultTask102 {
	
	/**
	 * 获取福彩开奖结果
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String findLotteryDrawResult(String lotteryId, String issueNo) {
		PostMethod method = null;
		HttpClient client = null;
		String result = null;
		boolean flag = true;
		int status = 0;
		String transcode = "102";
		issueNo = LotteryUtil.convertIssueNo(lotteryId, issueNo);
		try {

			Msg msg102 = new Msg();
			Headtype head102 = new Headtype();
			head102.setPartnerid(ChongQingTicketIssueConst.PARTNERID);
			head102.setTranscode(transcode);
			head102.setVersion(ChongQingTicketIssueConst.VERSION);
			head102.setTime(DateUtil.getCurrentDateTime());
			Body body102 = new Body();
			Querytype queryType102 = new Querytype();
			queryType102.setGameid(lotteryId);
			queryType102.setIssue(issueNo);
			queryType102.setPrivance("");
			body102.setQueryprizenotice(queryType102);
			msg102.setHead(head102);
			msg102.setBody(body102);
			String xmlMsg = TransactionMsgLoader102.msgToXml(msg102);
			
			System.out.println("xmlMsg -->" + xmlMsg);
			client = new HttpClient();
			method = new PostMethod(ChongQingTicketIssueConst.SERVER_URL);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", ChongQingTicketIssueConst.PARTNERID);
			method.setParameter("msg", xmlMsg);
			method.setParameter("key", Digit.sig((transcode+xmlMsg).getBytes("utf-8"),
					ChongQingTicketIssueConst.KEY_STORE_PASS, ChongQingTicketIssueConst.ALIALS, ClassPathUtil.getClassPathInputStream(ChongQingTicketIssueConst.PRIVATE_KEY)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
			
			/**
			 * 去掉返回信息后面带有的多余内容,比如去掉如下信息中最后的 “   error”
			 * <?xml version="1.0" encoding="UTF-8"?>
			 * <msg><head transcode="702" partnerid="21000021" version="1.0" time="20140828103612"/><body>
			 * <prizeinfo gameid="SSQ" issue="2014098" prizeball="17,31,29,20,13,2,7" status="4" prizepool="392834215" province="cq">
			 * <levelinfos><levelinfo level="1" name="一等奖" money="8502424" count="2"/>
			 * <levelinfo level="2" name="二等奖" money="284287" count="10"/>
			 * <levelinfo level="3" name="三等奖" money="3000" count="91"/>
			 * <levelinfo level="4" name="四等奖" money="200" count="3977"/>
			 * <levelinfo level="5" name="五等奖" money="10" count="68881"/>
			 * <levelinfo level="6" name="六等奖" money="5" count="617383"/>
			 * </levelinfos>
			 * <saleinfos>
			 * <saleinfo type="2" typename="cq" money="18522134"/>
			 * </saleinfos>
			 * </prizeinfo>
			 * </body>
			 * </msg>	error
			 */
			
			result = result.substring(0, result.lastIndexOf(TicketIssueConstant.RESPONSE_PROCESS_STR))+TicketIssueConstant.RESPONSE_PROCESS_STR;
			
		} catch (HttpException e) {
			flag = false;
		} catch (IOException e) {
			flag = false;
		} catch (Exception e) {
			flag = false;
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
			System.out.println(new StringBuilder().append(",返回的消息体内容:")
					.append(result).toString());

		}

		return result;
	}
}
