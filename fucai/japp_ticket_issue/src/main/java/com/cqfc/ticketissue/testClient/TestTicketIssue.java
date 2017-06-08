package com.cqfc.ticketissue.testClient;


public class TestTicketIssue {
	public static void main(String[] args) {
		String orderNo = "";
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='42' version='1.0' time='201409221403'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='2' province='cq' machine='182.245.213.183'>\n"
				+ "          	<user phone=\"\" idcard=\"\" realname=\"\" userid=\"4200000001\"/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='150#00860001#2014-0922-10008' multiple='1' issue='2014054' playtype='0' money='2'>\n"
				+ "          			<ball>01,02,03,04,05,06:13</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
		
//		TestTicketIssueTask task = new TestTicketIssueTask(orderNo, msg);
//		Thread thread = new Thread(task);
//		thread.run();
	}
}
