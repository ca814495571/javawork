package com.cqfc.management.util.propertiesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ReadProperties {

	
	
	public Map<String, String> readProperties(String fileName){
		
		Map<String, String> map = new HashMap<String, String>();
		
		Properties props =  new Properties();
		
		//String path = this.getClass().getClassLoader().getResource(".").getPath(); 
		//path = path.substring(1)+fileName;
		InputStream  in = this.getClass().getResourceAsStream("/"+fileName );     
		
		try {
			
			
			props.load(in);
			Set set = props.keySet();     
			Iterator it = set.iterator();   
			
		    while (it.hasNext()) {     
		          String key = (String) it.next();   
		          
		          map.put(key, props.getProperty(key));
		        }     

			
		} catch (IOException e) {
			e.printStackTrace();
		}     
		
		
		return map ;
	}
	
	
	public static void main(String[] args) {
		 System.out.println(new ReadProperties().readProperties("conf.properties").get("ftpDownLoad"));
		 
		 
		 
		// System.out.println(new ReadProperties().getClass().getResource("/conf.properties").getPath());
	
	
	
	}
	
	
	
	
}
