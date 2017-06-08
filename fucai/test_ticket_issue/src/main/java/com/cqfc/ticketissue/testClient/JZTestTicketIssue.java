package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.JZTicketIssueTask;
import com.cqfc.ticketissue.task.TicketIssueTask;

public class JZTestTicketIssue {
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='21000021' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='D3' ticketsnum='1' totalmoney='2' province='cq' machine='182.254.213.183'>\n"
				+ "          	<user userid='2100000021' realname='吉祥平' idcard='460007198211070057' phone='15523157877'/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='080#00880099#201412261152' multiple='1' issue='2015061' playtype='0' money='2'>\n"
				+ "          			<ball>3:9:2</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
		
		JZTicketIssueTask.ticketIssue(msg);
	}
	
}
