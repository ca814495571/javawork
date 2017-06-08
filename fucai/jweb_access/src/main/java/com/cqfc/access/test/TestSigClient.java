package com.cqfc.access.test;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.Digit;

public class TestSigClient {
	/**
	 * 服务器接入地址
	 */
	public final static String server_url = "http://127.0.0.1:8080/jweb_access/verification/verify";
	/**
	 * 私钥地址
	 */
//	public final static String private_scret_url = ClassPathUtil.getClassPath() + "19_private";
	/**
	 * 签名代理名 （alias)
	 */
	public final static String alias = "cqfc";
	/**
	 * 签名密码 （keystorepass)
	 */
	public final static String key_store_pass = "123456";
	/**
	 * 接入渠道的id
	 */
	public final static String partner_id = "00860001";

	/**
	 * 当前期号
	 */
	public static String issue_no = "2014076";

	public final static String PARM_TIME = "time";
	public final static String PARM_PARNERID = "partnerid";
	public final static String PARM_KEY = "key";
	public final static String PARM_MSG = "msg";
	public final static String PARM_VERSION = "version";
	public final static String PARM_TRANSCODE = "transcode";

	public final static String DATE_FORMAT = "yyyyMMDDHHmmSS";
	public final static String DEFAULT_VERSION = "1.0";
	public final static String CONVERT_TYPE = "http";

	/**
	 * 获取新期方法
	 */
	public void getIssue() {

		String transcode = "101";

		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='101' partnerid='00860001' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <queryissue gameid='SSQ' issueno='2009001' province='tj'/>\n"
				+ "      </body>\n" + "</msg>";

		this.send(msg, transcode);

	}
	
	/**
	 * 获取用户信息
	 */
	public void getUserInfo() {

		String transcode = "106";

		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='106' partnerid='00860001' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <queryuser parmtype='userid' value='3'/>\n"
				+ "      </body>\n" + "</msg>";

		this.send(msg, transcode);

	}

	public void voteTicket() {

		String transcode = "104";
		/*String msg = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><msg><head transcode ='104'  partnerid='"
				+ partner_id
				+ "'  version='1.0' time='200911120000'/>"
				+ " <body>  <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='2' province=''> <user userid='3'"
				+ " realname='sha' idcard='37010419801122999' phone='13911550999'/> <tickets>"
				+ "<ticket id='"
				+ String.valueOf(System.currentTimeMillis())
				+ "' multiple='1' issue='"
				+ issue_no
				+ "' playtype='0' money='2'>"
				+ "<ball>01,02,03,04,05,06:07</ball></ticket> </tickets>  </ticketorder> </body></msg>"*/;
		/*String msg = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<msg><head transcode='104' partnerid='00860001' version='1.0' time='200911120000'/>"
				+ "<body><ticketorder gameid='SSQ' ticketsnum='1' totalmoney='1' province='cq' machine='127.0.0.1'>"
				+ "<user userid='3' realname='test' idcard='12222222111112' phone='13800000000'/>"
				+ "<tickets><ticket id='123-12321-1231200' multiple='1' issue='2014076' playtype='0' money='1'>"
				+ "<ball>01,03,05,06,07,28:16</ball></ticket>"
				+ "</tickets></ticketorder></body></msg>";*/
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='00860002' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='1' province='tj' machine='127.0.0.1'>\n"
				+ "          	<user userid='' realname='TEST' idcard='440111111111212121' phone='13855555555'/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='12221-11102-122118' multiple='1' issue='2014078' playtype='0' money='1'>\n"
				+ "          			<ball>01,02,03,04,05,06:13</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
		this.send(msg, transcode);
	}
	
	public void findOrderStatus() {
		
		String transcode = "105";
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='105' partnerid='00860002' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <queryticket id='12221-11102-122908' gameid='SSQ' issue='2014078' userid=''/>\n"
				+ "      </body>\n" + "</msg>";
		this.send(msg, transcode);
	}
	public void createAppendTask() {
		
		String transcode = "110";
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='110' partnerid='00860001' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <floworder id='6669-1000-1236' totalmoney='6' totalissue='3' playtype='0' gameid='SSQ' stopflag='2' userid='3' ball='06,07,08,09,10,11:10'>\n"
				+ "      		<flowissues>"
				+ "      			<flowissue issue='2014082' multiple='1' />"
				+ "      			<flowissue issue='2014080' multiple='1' />"
				+ "      			<flowissue issue='2014081' multiple='1' />"
				+ "      		</flowissues>"
				+ "      	</floworder>"
				+ "      </body>\n" + "</msg>";
		this.send(msg, transcode);
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 * @param partnerId
	 * @param transcode
	 * @param key
	 * @return
	 */
	public boolean send(String msg, String transcode) {
		PostMethod method = null;
		HttpClient client = null;
		long time = System.currentTimeMillis();
		String result = null;
		boolean flag = true;
		int status = 0;

		try {
			String key = Digit.sig((transcode + msg).getBytes("utf-8"),
					key_store_pass, alias, ClassPathUtil.getClassPathInputStream("19_private"));
			client = new HttpClient();
			method = new PostMethod(server_url);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter(PARM_VERSION, "1.0");
			method.setParameter(PARM_PARNERID, partner_id);
			method.setParameter(PARM_MSG, msg);
			method.setParameter(PARM_KEY, key);
			method.setParameter(PARM_TRANSCODE, transcode);
			method.setParameter(PARM_TIME, "200911120000");
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
			

		} catch (HttpException e) {

			flag = false;
		} catch (IOException e) {

			flag = false;
		} catch (Exception e) {

			flag = false;
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
		return flag;

	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		TestSigClient manager = new TestSigClient();
		
		// 查询期号
		//manager.getIssue();
		
		// 获取用户信息
		manager.getUserInfo();

		// 投注
//		manager.voteTicket();
//		manager.findOrderStatus();
		
//		manager.createAppendTask();
	}
}
