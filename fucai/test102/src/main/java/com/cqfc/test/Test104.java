package com.cqfc.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.cqfc.test.util.Test104And105Util;
import com.cqfc.test.util.TestLog;

public class Test104 implements Runnable{

	public static void main(String[] args) throws InterruptedException {
		
		
		Test104 test1   = new  Test104();
		Thread  t1 = new Thread(test1);
		t1.start();
		 
		
	/*	Test104 test2   = new  Test104();
		Thread  t2 = new Thread(test2);
		t2.start();
		
		Test104 test3   = new  Test104();
		Thread  t3 = new Thread(test3);
		t3.start();*/
		
		
	/*	Test104 test4   = new  Test104();
		Thread  t4 = new Thread(test4);
		t4.start();
	
		Test104 test5   = new  Test104();
		Thread  t5 = new Thread(test5);
		t5.start();
		
		
		Test104 test6   = new  Test104();
		Thread  t6 = new Thread(test6);
		t6.start();
		
		Test104 test7   = new  Test104();
		Thread  t7 = new Thread(test7);
		t7.start();
		
		Test104 test8   = new  Test104();
		Thread  t8 = new Thread(test8);
		t8.start();
		
		Test104 test9   = new  Test104();
		Thread  t9 = new Thread(test9);
		t9.start();
		
		Test104 test10   = new  Test104();
		Thread  t10 = new Thread(test10);
		t10.start();
		
		
		
		Test104 test11   = new  Test104();
		Thread  t11 = new Thread(test11);
		t11.start();
		
		
		Test104 test12   = new  Test104();
		Thread  t12 = new Thread(test12);
		t12.start();
		
		Test104 test13   = new  Test104();
		Thread  t13 = new Thread(test13);
		t13.start();
		
		Test104 test14   = new  Test104();
		Thread  t14 = new Thread(test14);
		t14.start();
		
		Test104 test15   = new  Test104();
		Thread  t15 = new Thread(test15);
		t15.start();
		
		
		
		Test104 test16   = new  Test104();
		Thread  t16 = new Thread(test16);
		t16.start();
		
		
		Test104 test17   = new  Test104();
		Thread  t17 = new Thread(test17);
		t17.start();
		
		
		
		Test104 test18   = new  Test104();
		Thread  t18 = new Thread(test18);
		t18.start();
		
		
		
		Test104 test19   = new  Test104();
		Thread  t19 = new Thread(test19);
		t19.start();
		
		
		
		Test104 test20   = new  Test104();
		Thread  t20 = new Thread(test20);
		t20.start();
		*/
	}

	@SuppressWarnings("unused")
	@Override
	public void run() {
		
		Map<String,String>map = new HashMap<String, String>();
		Test104And105Util  t = new Test104And105Util();
		t.readExcle("d:/104.xls");
		UUID u = null;
		String str = null;
		String partnerid ="99999999";
	    String userId = "0";
		/* String partnerid ="00860003";
	     String userId ="2";*/
		
	//while(true){
			
			
			for(int m =0;m<64;m++){
				
			
			for(int i=0;i<1;i++)
              {

					u = UUID.randomUUID();
					
					str = t.send104(u.toString(), m,partnerid,userId);
                   
					if (str == null)
						continue;
					TestLog.ticketId
							.debug(" 104请求返回的 message,transcode=%s,partnerid=%s,msg=%s",
									"104", "partnerid", str);

					// map.put(String.valueOf(i), u.toString());
					TestLog.id.debug("%s", u.toString().toLowerCase());
					
					System.out.println(str);
					
					/*try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					*/
					
					

				/*	String ticketId = u.toString();

					String str105 = t.send105(ticketId);

					if (t.isSuccess(str105)) {

						TestLog.success
								.debug("105请求返回的 message,msg=%s", str105);

					}

					else if (t.isInTransaction(str105)) {
						TestLog.inTransaction.debug("id=%s",
								ticketId.toLowerCase());
						TestLog.inTransaction.debug("105请求返回的 message,msg=%s",
								str105);

					}

					else if (t.isNotExist(str105)) {

						TestLog.notExist.debug("id=%s", ticketId.toLowerCase());
						TestLog.notExist.debug("105请求返回的 message,msg=%s",
								str105);

					}

					else if (t.isFail(str105)) {

						TestLog.fail.debug("id=%s", ticketId.toLowerCase());
						TestLog.fail.debug("105请求返回的 message,msg=%s", str105);

					}

					else if (t.isWaitTransaction(str105)) {

						TestLog.waitTaransaction.debug("id=%s",
								ticketId.toLowerCase());
						TestLog.waitTaransaction.debug(
								"105请求返回的 message,msg=%s", str105);

					}

					else

					{

						TestLog.other.debug("id=%s", ticketId.toLowerCase());
						TestLog.other.debug("105请求返回的 message,msg=%s", str105);

					}

					
					System.out.println(str105);
*/
	         }
			                 
			
			
			
		
		
		  }
			
			
       //  }
	
		
			
			
		}
		
		
	
	
		
	
	}