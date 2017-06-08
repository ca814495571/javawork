package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.QueryTicketTask;
import com.jami.util.Log;

public class TestCheckIssue {
	public static void main(String[] args) {
//		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
//	 	 "<msg>"+
//	 	 "<head time=\"201409221403\" version=\"1.0\" partnerid=\"42\" transcode=\"105\"/>"+
//	 	 "<body>"+
//	 	 "<queryticket userid=\"2100000021\" issue=\"2014111\" gameid=\"SSQ\" id=\"150#00860001#2014-0922-10008\"/>"+
//	 	 "</body>"+
//		  "</msg>";
//		QueryTicketTask.queryTicketChuPiao(msg);
		Log.run.error("test lotteryId=%s, exception=%s", "ssq", new NullPointerException());
	}
}
