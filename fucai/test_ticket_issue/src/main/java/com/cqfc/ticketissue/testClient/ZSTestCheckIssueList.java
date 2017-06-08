package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.model.TempOrder;
import com.cqfc.ticketissue.task.ZSCheckIssueTaskList;
import com.cqfc.ticketissue.util.ReadOrderFile;

class ProcessThread2 extends Thread{
	@Override
	public void run() {
		while(!DataCenter.isEnd()){
			TempOrder order = DataCenter.get();
			if(order == null){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
//			String msg = ZSTestTicketIssueList.get104Content(order);
//			ZSTicketIssueTaskList.ticketIssue(msg);
			String msg = ZSTestCheckIssueList.get105Content(order);
			ZSCheckIssueTaskList.ticketIssue(msg);
		}
	}
}
public class ZSTestCheckIssueList {

	public static String get105Content(TempOrder to){
//		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//				+ "<msg>\n"
//				+ "      <head transcode='104' partnerid='00860066' version='1.0' time='"+DateUtil.getCurrentDateTime()+"'/>\n"
//				+ "      <body>\n"
//				+ "          <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='"+ to.getMoney() +"' province='cq' machine='182.254.213.183'>\n"
//				+ "          	<user userid='0' realname='张剑' idcard='06029250-0' phone='13828820191'/>\n"
//				+ "          	<tickets>\n"
//				+ "          		<ticket id='"+to.getId()+"' multiple='"+to.getMultiple()+"' issue='2015026' playtype='"+to.getPlayType()+"' money='"+ to.getMoney()  +"'>\n"
//				+ "          			<ball>"+to.getBall()+"</ball>\n"
//				+ "          		</ticket>\n"
//				+ "          	</tickets>\n"
//				+ "          </ticketorder>\n"
//				+ "      </body>\n" + "</msg>";
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
			 	 "<msg>"+
			 	 "<head time=\"1411210471613\" version=\"1.0\" partnerid=\"25000016\" transcode=\"105\"/>"+
			 	 "<body>"+
			 	 "<queryticket userid=\"2500000016\" issue=\"2015026\" gameid=\"SSQ\" id=\"080#00860066#"+to.getId()+"\"/>"+
			 	 "</body>"+
				  "</msg>";
		
		return msg;
		
	}
	public static void main(String[] args) throws Exception {
//		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//				+ "<msg>\n"
//				+ "      <head transcode='104' partnerid='25000016' version='1.0' time='200911120000'/>\n"
//				+ "      <body>\n"
//				+ "          <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='2' province='cq' machine='182.254.213.183'>\n"
//				+ "          	<user userid='2500000016' realname='张剑' idcard='06029250-0' phone='13828820191'/>\n"
//				+ "          	<tickets>\n"
//				+ "          		<ticket id='080#00880099#test201412261157' multiple='1' issue='2014152' playtype='0' money='2'>\n"
//				+ "          			<ball>01,02,19,23,27,29:10</ball>\n"
//				+ "          		</ticket>\n"
//				+ "          	</tickets>\n"
//				+ "          </ticketorder>\n"
//				+ "      </body>\n" + "</msg>";
		
//		TicketIssueTask.ticketIssue(msg);
		for(int i=0; i<6; i++){
			ProcessThread2 t = new ProcessThread2();
			t.setDaemon(false);
			t.start();
		}
		
		ReadOrderFile.getTempOrderList();
		do{
			Thread.sleep(5000);
		}while(true);
	}
	
}
