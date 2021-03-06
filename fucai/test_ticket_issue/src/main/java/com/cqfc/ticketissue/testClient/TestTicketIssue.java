package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.TicketIssueTask;

public class TestTicketIssue {
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='21000021' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='CQSSC' ticketsnum='1' totalmoney='2' province='cq' machine='182.254.213.183'>\n"
				+ "          	<user userid='2100000021' realname='吉祥平' idcard='460007198211070057' phone='18523057877'/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='080#21000021#2014103006923' multiple='1' issue='141104102' playtype='60' money='2'>\n"
				+ "          			<ball>5:4</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
		
		TicketIssueTask.ticketIssue(msg);
	}
}
