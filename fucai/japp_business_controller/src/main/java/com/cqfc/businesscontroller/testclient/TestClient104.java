
package com.cqfc.businesscontroller.testclient;

import com.cqfc.businesscontroller.task.TransactionMsgProcessor104;
import com.cqfc.protocol.businesscontroller.Message;

public class TestClient104 {
	
	public static void main(String[] args) {
		String xml104 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<msg>\n"
				+ "      <head transcode='104' partnerid='00860001' version='1.0' time='200911120000'/>\n"
				+ "      <body>\n"
				+ "          <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='1' province='tj' machine='127.0.0.1'>\n"
				+ "          	<user userid='3' realname='' idcard='' phone=''/>\n"
				+ "          	<tickets>\n"
				+ "          		<ticket id='12221-11102-122302' multiple='1' issue='2014077' playtype='0' money='1'>\n"
				+ "          			<ball>01,02,03,04,05,06:13</ball>\n"
				+ "          		</ticket>\n"
				+ "          	</tickets>\n"
				+ "          </ticketorder>\n"
				+ "      </body>\n" + "</msg>";
        Message message104 = new Message();
        message104.setTransCode(104);
        message104.setTransMsg(xml104);
        message104.setPartnerId("00860001");
//        TransactionMsgProcessor104 tmp104 = new TransactionMsgProcessor104();
//		tmp104.process(message104);
        TransactionMsgProcessor104.process(message104);
	}
}
