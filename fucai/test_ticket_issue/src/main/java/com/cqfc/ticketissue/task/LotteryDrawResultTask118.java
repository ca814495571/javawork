package com.cqfc.ticketissue.task;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.ticketissue.util.ChongQingTicketIssueConst;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.Digit;
import com.cqfc.xmlparser.TransactionMsgLoader118;
import com.cqfc.xmlparser.transactionmsg118.Body;
import com.cqfc.xmlparser.transactionmsg118.Headtype;
import com.cqfc.xmlparser.transactionmsg118.Msg;
import com.cqfc.xmlparser.transactionmsg118.Querytype;





public class LotteryDrawResultTask118 {
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
		String transcode = "118";
		try {

			Msg msg118 = new Msg();
			Headtype head118 = new Headtype();
			head118.setPartnerid(ChongQingTicketIssueConst.PARTNERID);
			head118.setTranscode(transcode);
			head118.setVersion(ChongQingTicketIssueConst.VERSION);
			head118.setTime(DateUtil.getCurrentDateTime());
			Body body118 = new Body();
			Querytype queryType118 = new Querytype();
			queryType118.setIdcardno("460007198211070057");
			queryType118.setPhone("15523157877");
			queryType118.setPuserkey("21000022");
			queryType118.setRealname("阿骆");
			body118.setBinduser(queryType118);
			msg118.setHead(head118);
			msg118.setBody(body118);
			String xmlMsg = TransactionMsgLoader118.msgToXml(msg118);

			
			client = new HttpClient();
			method = new PostMethod(ChongQingTicketIssueConst.SERVER_URL);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(
					HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			method.setParameter("transcode", transcode);
			method.setParameter("partnerid", ChongQingTicketIssueConst.PARTNERID);
			method.setParameter("msg", xmlMsg);
			method.setParameter("key", Digit.sig((transcode+xmlMsg).getBytes("utf-8"),
					ChongQingTicketIssueConst.KEY_STORE_PASS, ChongQingTicketIssueConst.ALIALS, ClassPathUtil.getClassPathInputStream(ConstantsUtil.FUCAI_PRIVATE_SCRECT_NAME)));
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes(
					"utf-8"));
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
