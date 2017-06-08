package com.cqfc.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class testSchedule {
 @Scheduled(cron="* 08 14 * * ?")
  public void schedule(){
	  
	    Test104 test1   = new  Test104();
		Thread  t1 = new Thread(test1);
		t1.start();
		 
		
		Test104 test2   = new  Test104();
		Thread  t2 = new Thread(test2);
		t2.start();
		
		Test104 test3   = new  Test104();
		Thread  t3 = new Thread(test3);
		t3.start();
		
		
		Test104 test4   = new  Test104();
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
		
		
	  
  }
	
	
}
