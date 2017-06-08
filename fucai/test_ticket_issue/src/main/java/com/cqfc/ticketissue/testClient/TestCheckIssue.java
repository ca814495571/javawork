package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.CheckIssueTask;

public class TestCheckIssue {
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
			 	 "<msg>"+
			 	 "<head time=\"1411210471613\" version=\"1.0\" partnerid=\"21000021\" transcode=\"105\"/>"+
			 	 "<body>"+
			 	 "<queryticket userid=\"2100000021\" issue=\"2014131\" gameid=\"CQSSC\" id=\"080#00880099#20143001760\"/>"+
			 	 "</body>"+
				  "</msg>";
	  CheckIssueTask.checkTicketChuPiao(msg);
//		String msg000 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
//						"<msg><head transcode=\"000\" partnerid=\"42\" version=\"1.0\" time=\"20140922124733\"/><body><msg errorCode=\"9015\" msg=\"订单不存在\"/></body></msg>";
//		com.cqfc.xmlparser.checkissueresult.Msg msg705 = CheckIssueResultLoader.xmlToMsg(msg000);
//		if("705".equals(msg705.getHead().getTranscode())){
//			System.out.println("fdsaf");
//		}
	}
}
