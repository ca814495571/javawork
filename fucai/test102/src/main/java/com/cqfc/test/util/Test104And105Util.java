package com.cqfc.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cqfc.xmlparser.TransactionMsgLoader104;
import com.cqfc.xmlparser.transactionmsg104.Ticket;

public class Test104And105Util {
	//send104(String ticketId,String gameid,String multiple,String issue,String playtype,int money,String ball)
		private HSSFWorkbook workbook = null;
		private HSSFSheet sheet = null;
		private FileInputStream in = null;
	    private FileOutputStream out = null;
	    private List<Map> list = new ArrayList<Map>();
	   
	    public List<Map> getList() {
			return list;
		}



		private Map<String,String> map = null;
	    
	    private String ticketId = null;
	    private String gameid =null;
	    private String multiple = null;
	    private String issue = null;
	    private String playtype = null;
	    private String money =null;
	    private String ball = null;
	    
		@SuppressWarnings("unused")
		public void readExcle(String direct) {

			try {       
				        
						HSSFCell cell = null;
						HSSFCell resultcell = null;
						in = new FileInputStream(direct);
					    workbook = new HSSFWorkbook(in);
					   
					    
					    sheet = workbook.getSheetAt(0);
					    int row = sheet.getLastRowNum();
					    HSSFRow hrow = sheet.getRow(0);
					    int hnum = hrow.getLastCellNum();
					   
					    
					loop: 
				for (int i = 1; i <= row; i++) {
					
					 HSSFRow irow = sheet.getRow(i);
					 int column = irow.getLastCellNum();
					 
					 map = new HashMap<String,String>();
					 
					 
					
						for (int j = 0; j < column; j++) {
							
							
                          
							if ( null == irow.getCell(0)) {
								break loop;
							   } 
							else if (irow.getCell(0).getStringCellValue().trim()
									.equals("")) {
								break loop;
							   }

							else if ( null == irow.getCell(j)) {
								break ;
							   } 
							else if (irow.getCell(j).getStringCellValue().trim()
									.equals("")) {
								break ;
							   }
								
							else if (hrow.getCell(j).getStringCellValue()
									.contains("gameid")) {
								cell = irow.getCell(j);
								if(null!=cell){
									gameid = cell.getStringCellValue();
									map.put("gameid", gameid);
									
								}
								
								continue;
							}
							
							else if (hrow.getCell(j).getStringCellValue()
									.contains("multiple")) {
								cell = irow.getCell(j);
								
								if(null!=cell){
									multiple = cell.getStringCellValue();
									map.put("multiple", multiple);
									
								}
								
								continue;
							}
							/*else if (hrow.getCell(j).getStringCellValue()
									.contains("issue")) {
								cell = irow.getCell(j);
								if(null!=cell){
									issue = cell.getStringCellValue();
									map.put("issue", issue);
								}
								
								continue;
							}
							*/
							else if (hrow.getCell(j).getStringCellValue()
									.contains("playtype")) {
								cell = irow.getCell(j);
								if(null!=cell){
									playtype = cell.getStringCellValue();
									map.put("playtype", playtype);
								}
								
								continue;
							}
							
							else if (hrow.getCell(j).getStringCellValue()
									.contains("money")) {
								cell = irow.getCell(j);
								if(null!=cell){
									money = cell.getStringCellValue();
									map.put("money", money);
								}
								
								continue;
							}
							
							else if (hrow.getCell(j).getStringCellValue()
									.contains("ball")) {
								cell = irow.getCell(j);
								if(null!=cell){
									ball = cell.getStringCellValue();
									map.put("ball", ball);
								}
								
								continue;
							}
							
				
							}
						
						//issue = this.subStringIssue(this.queryIssue(gameid));
						//map.put("issue", issue);
						list.add(map);
		
						}
	                     
					
						
						
					

					
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} finally {

				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}

				}

			}

		}

	
		
		public String send104(String ticketId,int i,String partnerid,String userId ){
			   
			      
			    	 map = list.get(i);
			    	 issue = this.subStringIssue(this.queryIssue(map.get("gameid")));
			    	 map.put("issue", issue);
			    	 //System.out.println(issue);
			    	 gameid = map.get("gameid");
	         	     multiple = map.get("multiple");
	         	    // issue = map.get("issue");
	         	     playtype = map.get("playtype");
	         	     money = map.get("money");
	         	     ball = map.get("ball");
	         	    
	         	     if(gameid.equals("SSQ"))
	         	     {
	         	    	 ball =this.randdomData();
	         	    	 money = "2";
	         	    	 playtype ="0";
	         	     }
	         	     
	       
			String strXml1 ="<?xml version='1.0' encoding='UTF-8' ?>\n"
					+ "<msg>\n"
					+ "<head transcode='104' partnerid=\'"+partnerid+"\' version='1.0' time='201408211432' />\n"
					+ "  <body>\n"
					+ "   <ticketorder gameid=\'"+gameid+"\' ticketsnum='1' totalmoney=\'"+money+"\' province='sz' machine='127.0.0.1'>\n"
					+ "        <user userid=\'"+userId+"\' realname='testuser1' idcard='123456789123456789' phone='12345678911' />\n" 
					+ "     <tickets>\n"
					+ "             <ticket id=\'"+ticketId+"\' multiple=\'"+multiple+"\' issue=\'"+issue+"\' playtype=\'"+playtype+"\' money=\'"+money+"\'>\n"
					+ "                   <ball>"+ball+"</ball>\n"
					+ "             </ticket>\n"
					+ "     </tickets>\n"
					+ "  </ticketorder>\n"
					+ "  </body>\n"
					+"</msg>";
			
			
			/*String strXml ="<?xml version='1.0' encoding='UTF-8' ?>\n"
					+ "<msg>\n"
					+ "<head transcode='104' partnerid=\'"+partnerid+"\' version='1.0' time='201408211432' />\n"
					+ "  <body>\n"
					+ "   <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='10' province='sz' machine='127.0.0.1'>\n"
					+ "        <user userid='0' realname='testuser1' idcard='123456789123456789' phone='12345678911' />\n" 
					+ "     <tickets>\n"
					+ "             <ticket id=\'"+ticketId+"\' multiple='1' issue='' playtype='0' money='10'>\n"
					+ "                   <ball>01,02,19,23,27,29:05#04,17,23,27,28,32:03#04,17,23,27,28,32:02#04,17,23,27,28,32:01#04,17,23,27,28,32:04</ball>\n"
					+ "             </ticket>\n"
					+ "     </tickets>\n"
					+ "  </ticketorder>\n"
					+ "  </body>\n"
					+"</msg>";*/
			
			
			
			if(issue==null)  return null;
 			//SendMessage sm  = new  SendMessage();
 			
 			 Send sm  = new  Send();
			//System.out.println(strXml1);
			
			return sm.send(strXml1, "104",partnerid );
			
			
			
			
			

		}

		
		
		public String  send104(List<Map>list){
			
            	
			  SendMessage sm  = new  SendMessage();
			    for(int i=0;i<list.size();i++){
			    	map = list.get(i);
			    	String  str = this.replaceTicketId((String)map.get("sendMessage"),map);
			    	System.out.println(str);
			        str =  sm.send( str, "104", (String)map.get("partnerid"));
			    	System.out.println(i);
			    	map.put("return104", str);
			    	System.out.println(str);
			    	
			    }
				  
				 /*  for(Map map:list){
				         System.out.println(map.get("sendMessage"));
				         return  sm.send((String)map.get("sendMessage"), "104", (String)map.get("partnerid"));	
				        
			          }
			*/
			  
			return null;
			
			
		}
		
		
		public String  send105(String ticketId,int i){
			  map = list.get(i);
       		String issue=  map.get("issue");
       		if(issue == null) 
       			           return null;
			
			String strXml =
					 "<?xml version='1.0' encoding='UTF-8' ?>\n"
					+"<msg>\n"
					+"   <head transcode='105' partnerid='00860003' version='1.0' time='201408211432' />\n"
					+"    <body>\n"
					+"        <queryticket id=\'"+ticketId+"\' gameid=\'"+map.get("gameid")+"\' issue=\'"+map.get("issue")+"\' userid='0' />\n"
					+"     </body>\n"
					+"</msg>\n";

		
		SendMessage sm  = new  SendMessage();
		
		
		return sm.send(strXml, "105","00860003" );
		}
		
		
		
		
		
		public String  send105(String ticketId){
			
			
			String strXml =
					"<?xml version='1.0' encoding='UTF-8' ?>\n"
							+"<msg>\n"
							+"   <head transcode='105' partnerid='00000001' version='1.0' time='201408211432' />\n"
							+"    <body>\n"
							+"        <queryticket id=\'"+ticketId+"\' gameid='SSC' issue='2014102' userid='2' />\n"
							+"     </body>\n"
							+"</msg>\n";
			
			
			SendMessage sm  = new  SendMessage();
		
			
			
			return sm.send(strXml, "105","00000001" );
			
		}
		
		
		
		
		
		public String  send105(String ticketId,String partnerid){
			
			
			String strXml =
					"<?xml version='1.0' encoding='UTF-8' ?>\n"
							+"<msg>\n"
							+"   <head transcode='105' partnerid=\'"+partnerid+"\' version='1.0' time='201408211432' />\n"
							+"    <body>\n"
							+"        <queryticket id=\'"+ticketId+"\' gameid='SSC' issue='2014102' userid='10' />\n"
							+"     </body>\n"
							+"</msg>\n";
			
			
			SendMessage sm  = new  SendMessage();
			//Send sm  = new  Send();
			
			
			return sm.send(strXml, "105",partnerid);
		}
		
		
	
		
	    public void testMap(){
		     System.out.println(list.size());
		for (int i=0;i<list.size();i++){
			
			 map  = list.get(i);
			
	    	 gameid = map.get("gameid");
    	     multiple = map.get("multiple");
    	     issue = map.get("issue");
    	     playtype = map.get("playtype");
    	     money = map.get("money");
    	     ball = map.get("ball");
    	     System.out.println("gameid="+gameid);
    	     System.out.println("multiple="+multiple);
    	     System.out.println("issue="+issue);
    	     System.out.println("playtype="+playtype);
    	     System.out.println("money="+money);
    	     System.out.println("ball="+ball);
			
			
		      }
		
		
	}
	
	    
	    
         
	    public String queryIssue(String gameid){
		   
		   String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
					+ "<msg>\n"
					+ "      <head transcode='101' partnerid='99999999' version='1.0' time='200911120000'/>\n"
					+ "      <body>\n"
					+ "          <queryissue gameid=\'"+gameid+"\' issueno='' province='tj'/>\n"
					+ "      </body>\n" + "</msg>";
		   //System.out.println(msg);
		   
		  SendMessage sm  =  new  SendMessage();
		  String str =  sm.send(msg, "101", "99999999");
		  //System.out.println(str);
		  return  str;
		  
		   
	   }
	    
	    
	    
	    public String subStringIssue(String msg){
	        
	    	if(msg==null) return null;
	 		String regex = "issue=.[A-Za-z0-9]+.";   //issue="2014108"
	 	
	 		Pattern p = Pattern.compile(regex);
	 	    Matcher m = p.matcher(msg);
	 	    int startIndex = 0;
	 	    int endIndex  =  0;
	 	   
	 	    while(m.find()){
	 	   	  
	 	       startIndex = m.start();
	 	       endIndex = m.end();
	 	        
	 	    } 
	 	   
	 		String str = msg.substring(startIndex, endIndex);
	 		
	 		
	 	    
	 		//if(str==null) System.out.println(msg);
	 		String [] s =  str.split("=");
	 		//if(s==null) System.out.println(msg);
	 		if(s.length>=2){
	 			       
	 					String ss = s[1].substring(1, s[1].length()-1);
	 					
	 					return ss;
	 				
	 					
	 		}
	 		
	       
	 		return null;
	    	
	    	
	    }
	   
	    
	    
	  
	    
	    public boolean  isInTransaction (String str){
		  
		  
		  if(str==null) return false;
			String regex ="statuscode=\"0001\"";
			return str.contains(regex);
			
	  }   
	    
	    
        public boolean  isNotExist (String str){
		  
		  
		  if(str==null) return false;
			String regex = "<error msg=\"订单不存在\"";
			return str.contains(regex);
			
	  }     
	    
        public boolean  isSuccess (String str){
  		  
  		  
  		  if(str==null) return false;
  			String regex = "statuscode=\"0002\"";
  			return str.contains(regex);
  			
  	  }   
  	    
        public boolean  isFail (String str){
    		  
    		  
    		  if(str==null) return false;
    			String regex = "statuscode=\"0003\"";
    			return str.contains(regex);
    			
    	  }   
        
        
        
        public boolean  isWaitTransaction (String str){
    		  
    		  
    		  if(str==null) return false;
    			String regex = "statuscode=\"0000\"";
    			return str.contains(regex);
    			
    	  }   
      
        
     
        public  void  touZhu(String direct,int ii){
        	


			try {       
				        
						HSSFCell cell = null;
						HSSFCell resultcell = null;
						in = new FileInputStream(direct);
					    workbook = new HSSFWorkbook(in);
					   
					    
					    sheet = workbook.getSheetAt(ii);
					    int row = sheet.getLastRowNum();
					    HSSFRow hrow = sheet.getRow(0);
					    int hnum = hrow.getLastCellNum();
					   
					    
					loop: 
				for (int i = 1; i <= row; i++) {
					
					 HSSFRow irow = sheet.getRow(i);
					 int column = irow.getLastCellNum();
					 //System.out.println("column="+column);
					 
					 map = new HashMap();
					 
					 
					
						for (int j = 0; j < column; j++) {
							
							
                          
							if ( null == irow.getCell(0)) {
								break loop;
							   } 
							else if (irow.getCell(0).getStringCellValue().trim()
									.equals("")) {
								break loop;
							   }

							else if ( null == irow.getCell(j)) {
								break ;
							   } 
							else if (irow.getCell(j).getStringCellValue().trim()
									.equals("")) {
								break ;
							   }
							else if (null==hrow.getCell(j)) {
								break ;
							}
								
							else if (hrow.getCell(j).getStringCellValue()
									.contains("sendMessage")) {
								cell = irow.getCell(j);
								if(null!=cell){
									map.put("sendMessage", cell.getStringCellValue());
						            //System.out.println( cell.getStringCellValue());
								}
								
								continue;
							}
							
							
							
							
							
							else if (hrow.getCell(j).getStringCellValue()
									.contains("purpose")) {
								cell = irow.getCell(j);
								if(null!=cell){
									map.put("purpose", cell.getStringCellValue());
									//System.out.println( cell.getStringCellValue());
								}
								
								continue;
							}
							
					
							
				
							}
						
						String partnerid= this.subStringPartnerid(map.get("sendMessage"));
						map.put("partnerid", partnerid);
						list.add(map);
		
						}
	                     
					
						
						
					

					
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} finally {

				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}

				}

			}

		
        	
        	
        }
        
       
        public String  subStringPartnerid(String msg){
    	   

	        
	    	if(msg==null) return null;
	 		String regex = "partnerid=.[A-Za-z0-9]+.";   //issue="2014108"
	 	    
	 		Pattern p = Pattern.compile(regex);
	 	    Matcher m = p.matcher(msg);
	 	    int startIndex = 0;
	 	    int endIndex  =  0;
	 	   
	 	    while(m.find()){
	 	   	  
	 	       startIndex = m.start();
	 	       endIndex = m.end();
	 	        
	 	    } 
	 	   
	 		String str = msg.substring(startIndex, endIndex);
	 		
	 		
	 	    
	 		//if(str==null) System.out.println(msg);
	 		String [] s =  str.split("=");
	 		//if(s==null) System.out.println(msg);
	 		if(s.length>=2){
	 			       
	 					String ss = s[1].substring(1, s[1].length()-1);
	 					
	 					return ss;
	 				
	 					
	 		}
	 		
	       
	 		return null;
	    	
	    	
	    
    	 
    	   
       } 
        
     
        public String  replaceTicketId(String str,Map map){
        	
    	   UUID u =null;
    	   List<String>id  =  new ArrayList<String>();
    	   
          com.cqfc.xmlparser.transactionmsg104.Msg  req104  =  TransactionMsgLoader104.xmlToMsg(str);
          com.cqfc.xmlparser.transactionmsg104.Msg  rep104  = req104;
          String  gameId = rep104.getBody().getTicketorder().getGameid();
    	  List<Ticket>list = rep104.getBody().getTicketorder().getTickets().getTicket();
    	  
    	  
    	   for(Ticket ticket:list){
    		   
    		   u = UUID.randomUUID();
    		   ticket.setId(u.toString());
    		   //System.out.println(this.subStringIssue(this.queryIssue(gameId)));
    		   ticket.setIssue(this.subStringIssue(this.queryIssue(gameId)));
    		   id.add(u.toString());
    	   }
    	  
    	  map.put("idList", id);
    	 return TransactionMsgLoader104.msgToXml(rep104); 
    	   
    	   
       }
        
        
        
        
        public  String randdomData() {
    		Integer[]k = new Integer [6];
    		Random  r = new Random();
    		HashSet<Integer> set = new HashSet<Integer>();
    		StringBuilder  sb  = new StringBuilder();
    		 String s = null;
    		while(set.size() < 7){
    		    set.add(r.nextInt(33) + 1);
    		}
    		
    	     k =  set.toArray(new Integer[0]);
    		
    		
    	        
    			for(int i=0;i<6;i++){
    				
    				 for(int j =0;j<5;j++)
    			   {
    				if( k[j] >k[j+1])
    				 {
    					int max= k[j];
    				     k[j] = k[j+1];
    				     k[j+1] =max; 
    				}
    				
    			  }
    		 }
    			
    	
    			
    			
    			for(int m =0;m<6;m++){
    				 
    				 
    				 if(k[m]<10){
    					s =  "0"+k[m];
    				 }else
    					s = String.valueOf(k[m]);
    				
    				 
    				if(m==5)
    				  sb = sb.append(s);
    				    else
    			      sb = sb.append(s+",");
    					
    			    
    			}		
    			
    			
    			int  i =  r.nextInt(16)+1; 
    			 if(i<10){
    					s =  "0"+i;
    				 }else
    					s = String.valueOf(i);
    		  sb = sb.append(":"+s);
    			
    		return sb.toString();
    	
    	}	
    			     
        
        
	    
}
