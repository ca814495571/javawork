package com.cqfc.test;
import java.util.List;
import java.util.Map;

import com.cqfc.test.util.ReadAndWriteExcle;
import com.cqfc.test.util.SendMessage;
import com.cqfc.test.util.TestOtherInterfaceUtil;


public class TestOther {
	
	public static void main(String []args) throws InterruptedException{
		
		TestOtherInterfaceUtil tu  =  new TestOtherInterfaceUtil();
    	tu.ReadAndWrite("D:/otherInterface.xls", 1);
    	SendMessage sm = new SendMessage();
		
    	Map map = null;
    	List list = tu.getList();
    	for(int i=0;i<list.size();i++)
    	  {
    		
    	   map =(Map) list.get(i);
    	   String transcode = (String) map.get("transcode");
    	   String partnerid  =  (String) map.get("partnerid");
    	   String gameid =   (String) map.get("gameid");
    	   String sendMsessage =  (String) map.get("sendMsessage");
    	  System.out.println(sm.send(sendMsessage, transcode, partnerid)); 
    	 
    	  }
    
		
	
		
		
	}
	

}
