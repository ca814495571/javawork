package com.cqfc.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.cqfc.test.util.Test104And105Util;
import com.cqfc.test.util.TestLog;

public class Test105 implements Runnable{
	private long m=0;
	private long n=0;
	
      public Test105(long m,long n){
	   this.m =m;
	   this.n=n;
	
     }	
	


    
	@Override
 	  public void run() {
		

		 Test104And105Util  t = new Test104And105Util();
         String ticketId = null;
		 FileReader in = null;
		 FileInputStream inPut = null;
		 FileOutputStream out = null;
         BufferedReader  br = null;
         boolean flag = true;
         long i = 0;
     	 Properties prop  = new Properties();
     	 String str = null;
     	 
         
       	try {   
       		
       		
       		   if(flag){
       			out = new FileOutputStream("D:/testlog/data.properties",true); 
				inPut = new FileInputStream("D:/testlog/data.properties");
				prop.load(inPut);
				if(prop.get(Thread.currentThread().getName())!=null){
					i=Integer.parseInt((String) prop.get(Thread.currentThread().getName()));
					prop.clear();
				  }
				
       		   }
       		      
       		
				in = new FileReader("D:/testlog/id.log");
				br = new BufferedReader(in); 
			   
			     String partnerid ="99999999";
			    // String partnerid ="00860003";
				  while((str= br.readLine())!=null)
			         {
			        	 
					     i++;
					     if(i<m) continue;
					  
					/* try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	*/
					  
					      
					  
					      ticketId=str;
					     
					      String str105 = t.send105(ticketId,partnerid);
			        	 
			        		if(t.isSuccess(str105))
							{
								 
								 TestLog.success.debug("105请求返回的 message,msg=%s", str105);
								
							}
							
							
							else if(t.isInTransaction(str105))
				 			{   
								TestLog.inTransaction.debug("id=%s", ticketId.toLowerCase());
				 				TestLog.inTransaction.debug("105请求返回的 message,msg=%s", str105);
				 				
				 			}
				 			
				 			
							else if(t.isNotExist(str105))
				 			{
				 				
				 				TestLog.notExist.debug("id=%s", ticketId.toLowerCase());
				 				TestLog.notExist.debug("105请求返回的 message,msg=%s", str105);
				 				
				 			}
				 			
							else if(t.isFail(str105))
				 			{
				 				
				 				TestLog.fail.debug("id=%s", ticketId.toLowerCase());
				 				TestLog.fail.debug("105请求返回的 message,msg=%s", str105);
				 				
				 			}
				 			
							else if(t.isWaitTransaction(str105))
				 			{
								
				 				TestLog.waitTaransaction.debug("id=%s", ticketId.toLowerCase());
				 				TestLog.waitTaransaction.debug("105请求返回的 message,msg=%s", str105);
				 				
				 			}
							
							
							
							else
								
							{

								TestLog.other.debug("id=%s",ticketId.toLowerCase());
								TestLog.other.debug("105请求返回的 message,msg=%s", str105);
	
							 }
							
							
			        	 
			        	     System.out.println(str105);
			        	     
			        	     if(flag){
			        	    	 prop.setProperty(Thread.currentThread().getName(), String.valueOf(i));
			        	    	 if(Thread.currentThread().getName().equals("first")){
			        	    		 System.out.println(i);
			        	    	 }
			        	    	
			        	    	 prop.store(out, String.valueOf(i));	
					        		}
			        	    	 
			        	    	
			        	 
			        	 if(i>=n)  break;
			        	 
			         }
       	
       	} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}finally{
				
				
				  if(br!=null)
				    {
					  try {
						br.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					  
				    }
				  
				  
				  if(inPut!=null){
					  
					  
					  try {
						inPut.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  }
				
				if(out!=null){
					
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
          
       	 
       	 
       	 
    
        
        
        
      
        
        
        
	
		
		
		
	
	}
	
	
	
	
	
	  public static void main(String []args){
		 
		  Test105  t001 = new Test105(1,10000);
          Thread  t1  = new Thread(t001);
          t1.setName("first");
          t1.start();
	     
          Test105  t002 = new Test105(10001,20000);
          Thread  t2  = new Thread(t002);
          t2.start();
          t2.setName("second");
          
          Test105  t003 = new Test105(20001,30000);
          Thread  t3  = new Thread(t003);
          t3.start();
          t3.setName("there");
          
          Test105  t004 = new Test105(30001,40000);
          Thread  t4  = new Thread(t004);
          t4.start();
          t4.setName("four");
          
          Test105  t005 = new Test105(40001,50000);
          Thread  t5  = new Thread(t005);
          t5.start();
          t5.setName("five");
          
          Test105  t006 = new Test105(50001,60000);
          Thread  t6  = new Thread(t006);
          t6.start();
          t6.setName("six");
          
          Test105  t007 = new Test105(60001,70000);
          Thread  t7  = new Thread(t007);
          t7.start();
          t7.setName("seven");
          
          Test105  t008 = new Test105(70001,80000);
          Thread  t8  = new Thread(t008);
          t8.start();
          t8.setName("eight");
          
          Test105  t009 = new Test105(80001,90000);
          Thread  t9  = new Thread(t009);
          t9.start();
          t9.setName("nine");
          
          Test105  t010 = new Test105(90001,100000);
          Thread  t10  = new Thread(t010);
          t10.start();
          t10.setName("ten");
          
          
          Test105  t011 = new Test105(100001,110000);
          Thread  t11  = new Thread(t011);
          t11.start();
          
          Test105  t012 = new Test105(110001,120000);
          Thread  t12  = new Thread(t012);
          t12.start();
          
          
          Test105  t013 = new Test105(120001,130000);
          Thread  t13  = new Thread(t013);
          t13.start();
          
          Test105  t014 = new Test105(130001,140000);
          Thread  t14  = new Thread(t014);
          t14.start();
          
          Test105  t015 = new Test105(140001,150000);
          Thread  t15  = new Thread(t015);
          t15.start();
          
          Test105  t016 = new Test105(150001,160000);
          Thread  t16  = new Thread(t016);
          t16.start();
          
          Test105  t017 = new Test105(160001,170000);
          Thread  t17  = new Thread(t017);
          t17.start();
          
          Test105  t018 = new Test105(170001,180000);
          Thread  t18  = new Thread(t018);
          t18.start();
          
          Test105  t019 = new Test105(180001,190000);
          Thread  t19  = new Thread(t019);
          t19.start();
          
          Test105  t020 = new Test105(190001,200000);
          Thread  t20  = new Thread(t020);
          t20.start();
          
          
          Test105  t021 = new Test105(200001,210000);
          Thread  t21  = new Thread(t021);
          t21.start();
          
          Test105  t022 = new Test105(210001,220000);
          Thread  t22  = new Thread(t022);
          t22.start();
          
          Test105  t023 = new Test105(220001,230000);
          Thread  t23  = new Thread(t023);
          t23.start();
          
          Test105  t024 = new Test105(230001,240000);
          Thread  t24  = new Thread(t024);
          t24.start();
          
          
          Test105  t025 = new Test105(240001,250000);
          Thread  t25  = new Thread(t025);
          t25.start();
          
          Test105  t026 = new Test105(250001,260000);
          Thread  t26  = new Thread(t026);
          t26.start();
          
          Test105  t027 = new Test105(260001,270000);
          Thread  t27  = new Thread(t027);
          t27.start();
          
          Test105  t028 = new Test105(270001,280000);
          Thread  t28  = new Thread(t028);
          t28.start();
          
          Test105  t029 = new Test105(280001,290000);
          Thread  t29  = new Thread(t029);
          t29.start();
          
          Test105  t030 = new Test105(290001,300000);
          Thread  t30  = new Thread(t030);
          t30.start();
          
          
          
          
          
     
          
          
		 
	 }
	
  
	
	

}
