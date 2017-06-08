package com.cqfc.back.controller;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/dealAcessBack")
public class DealAccessBackControl {
	@RequestMapping("/save")
	public void saveXml(String xml) throws IOException{
		 BufferedWriter  bf=null;
		//System.out.println(ClassPathUtil.class.getResource("/").getPath());
		 System.out.println(xml);
		
	    try {
			  
			       Writer  out  = new FileWriter("D://log.txt",true);
			   
			        bf  = new  BufferedWriter(out);
			      
			        bf.write(new Date().toLocaleString());
			        bf.newLine();
			        bf.write(xml);
			        
			        bf.newLine();
			       
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}finally{
			if(bf!=null){
				bf.close();
				
			}
			
			
		}
		 
	}

}
