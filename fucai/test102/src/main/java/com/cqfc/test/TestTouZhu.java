package com.cqfc.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cqfc.test.util.Test104And105Util;
import com.cqfc.test.util.TestLog;

public class TestTouZhu {

	public static void main(String[] args) {
		Test104And105Util t = new Test104And105Util();
		t.touZhu("d:/touzhu.xls",0);
		
		t.send104(t.getList());
        List <Map> list =  t.getList();
        
        
       /* try {
			Thread.sleep(1000*60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        
        
        
        
        for(Map map:list)
          {
        	List<String>list105return = new ArrayList<String>(); 
            List <String>idList  =   (List) map.get("idList");
            String partnerid = (String) map.get("partnerid");
            
             for(String str:idList)
                {
            	   //System.out.println(str);
            	 String  return105  = t.send105(str,partnerid);
            	 list105return.add(return105);
            	 System.out.println(return105);
            	 
                }
            
        	map.put("list105return",list105return);
          }
        
        for(Map map:list){
        	
         String purpose = (String) map.get("purpose");
         String sendMessage = (String) map.get("sendMessage");
         String return104 = (String) map.get("return104");
         TestLog.touzhu.debug("测试目的 %s",purpose );
         TestLog.touzhu.debug("104请求内容 %s",sendMessage );
         TestLog.touzhu.debug("104返回%s",return104 );
         
         List <String>list105return = (List) map.get("list105return");
         
         for(String str:list105return){
        	 TestLog.touzhu.debug("105返回%s",str); 
        	
         }
       	
        	
        	
        }
        
        
        
	}

}
