package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.CheckIssueTask;

public class ZSTestCheckIssue {
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
			 	 "<msg>"+
			 	 "<head time=\"1411210471613\" version=\"1.0\" partnerid=\"25000016\" transcode=\"105\"/>"+
			 	 "<body>"+
			 	 "<queryticket userid=\"2500000016\" issue=\"2014300\" gameid=\"D3\" id=\"150#00860088#801818-80050\"/>"+
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
