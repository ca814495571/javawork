package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.TicketIssueTask;

public class ZSTestTicketIssue {
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='25000016' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='CQSSC' ticketsnum='1' totalmoney='2' province='cq' machine='182.254.213.183'>\n"
				+ "          	<user userid='2500000016' realname='张剑' idcard='06029250-0' phone='13828820191'/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='150#00860088#801818-80052' multiple='1' issue='141104074' playtype='10' money='2'>\n"
				+ "          			<ball>1</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
		
		TicketIssueTask.ticketIssue(msg);
	}
	
}
