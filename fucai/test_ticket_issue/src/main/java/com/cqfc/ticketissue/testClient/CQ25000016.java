package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.TicketIssueTask;

public class CQ25000016 {
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='25000016' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='2' province='cq' machine='182.254.213.183'>\n"
				+ "          	<user userid='2500000016' realname='张剑' idcard='06029250-0' phone='13828820191'/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='080#00880099#201412261153' multiple='1' issue='2015027' playtype='0' money='2'>\n"
				+ "          			<ball>01,02,19,23,27,29:10</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
		
		TicketIssueTask.ticketIssue(msg);
	}
	
}
