package com.cqfc.test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;

import com.cqfc.test.util.SendMessage;
import com.cqfc.test.util.Test104And105Util;


public class Test50 {

	public static void main(String[] args) {
		 Test104And105Util  t = new Test104And105Util();
         
		 FileInputStream in = null;
        
       
         
       	try {   
				in = new FileInputStream("D:/testlog/id001.log");
				
			    int i = 0;
			    byte [] buf = new byte[1024*20];
			
			    i = in.read(buf);
			    
			     String  str = new String(buf);
			     str  =  str.trim();
			     SendMessage sm = new SendMessage();
			    System.out.println(sm.send(str, "104", "00000001")); 
			      
			   
			    
       	} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}finally{
				
				
				  if(in!=null)
				    {
					  try {
						in.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					  
				    }
				
				
			}
          
       	 
       	 
       	 
    
        
        
        
      
        
        
        

	}

}
