package com.cqfc.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class CalculatePrize {
	
    private  String  lotteryId =null; 
	private  String  issueNo = null;
	private  String  playType = null;
	private  String  ballContent = null;
	private  String  winMoney = null;
	private  String  exceptWinMoney = null;
	private  String  result = null;
	private  String  checkDetailCount = null;
	private  Map<String,String> map = null;
	private  List<Map> list = new ArrayList<Map>();
	
	public String sendMessage(int i){
		String server_url = "http://182.254.212.183:8080/jweb_access/lottery/prize";
		String str =null;
		PostMethod method = null;
		HttpClient client = null;
		long time = System.currentTimeMillis();
		String result = null;
		boolean flag = true;
		int status = 0;
		map = list.get(i);
		
		 lotteryId = map.get("lotteryId");
		 issueNo = map.get("issueNo");
		 playType = map.get("playType");
	     ballContent = map.get("ballContent");
	    
	  

		try {
			
			client = new HttpClient();
			method = new PostMethod(server_url);

			method.getParams().setContentCharset("utf-8");
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			
			method.setParameter("lotteryId", lotteryId);
			method.setParameter("issueNo", issueNo);
			method.setParameter("playType", playType);
			method.setParameter("ballContent", ballContent);
			
			HttpConnectionManagerParams managerParams = client
					.getHttpConnectionManager().getParams();

			managerParams.setConnectionTimeout((100000));
			managerParams.setSoTimeout((100000));
			status = client.executeMethod(method);
			result = new String(method.getResponseBodyAsString().getBytes("utf-8"));
		
		} catch (HttpException e) {

			flag = false;
		} catch (IOException e) {

			flag = false;
		} catch (Exception e) {

			flag = false;
		} finally {
			if (method != null)
				method.releaseConnection();
			if (client != null) {

				try {

					((SimpleHttpConnectionManager) client
							.getHttpConnectionManager())
							.closeIdleConnections(0);

				} catch (Exception e) {
					e.printStackTrace();

				}

				client = null;

			}

			str =new StringBuilder()
					.append(result).toString();

		}
		return str;

		
		
	}

	
	
	public List<Map> getList() {
		return list;
	}



	public void  makeUpData (String direct,int r){
		 HSSFWorkbook workbook = null;
		 HSSFSheet sheet = null;
		 FileInputStream in = null;
	     FileOutputStream out = null;
	     String value = null;
	    
	     
	    

		try {       
			        
					HSSFCell cell = null;
					HSSFCell resultcell = null;
					in = new FileInputStream(direct);
				    workbook = new HSSFWorkbook(in);
				   
				    
				    sheet = workbook.getSheetAt(r);
				    int row = sheet.getLastRowNum();
				    HSSFRow hrow = sheet.getRow(0);
				    int hnum = hrow.getLastCellNum();
				   
				    
			
			for (int i = 1; i <= row; i++) {
				
				 HSSFRow irow = sheet.getRow(i);
				 int column = irow.getLastCellNum();
				 
				 map = new HashMap<String,String>();
				 
				 
				 
				
					for (int j = 0; j < column; j++) {
					
				    	cell = irow.getCell(j);
				    	//if(cell == null) break;
				    	if(hrow.getCell(j)==null) break;
				      switch(cell.getCellType())
				         {
				      case Cell.CELL_TYPE_BLANK :
				    	
				    	 // break ;
				      case Cell.CELL_TYPE_NUMERIC:
				    	  value = String.valueOf(cell.getNumericCellValue());
				    	  System.out.println(value);
				    	  break;
				      case Cell.CELL_TYPE_STRING:
				    	  value = cell.getStringCellValue();
				    	  System.out.println(value);
				    	
				 
				         }		
                     
						/*if ( null == irow.getCell(0)) {
							break loop;
						   } 
						else if (irow.getCell(0).getStringCellValue().trim()
								.equals("")) {
							break loop;
						   }*/

						/*else if ( null == irow.getCell(j)) {
							break ;
						   } 
						else if (irow.getCell(j).getStringCellValue().trim()
								.equals("")) {
							break ;
						   }*/
							
				         
					    if (hrow.getCell(j).getStringCellValue()
								.contains("lotteryId")) {
							
							
								lotteryId =value;
								map.put("lotteryId", lotteryId);
								
						
							
							continue;
						}
						
						else if (hrow.getCell(j).getStringCellValue()
								.contains("issueNo")) {
							
							
								issueNo = value;
								map.put("issueNo", issueNo);
								
						
							
							continue;
						}
						
						else if (hrow.getCell(j).getStringCellValue()
								.contains("playType")) {
							
								playType = value;
								map.put("playType", playType);
							
							
							continue;
						}
						
						else if (hrow.getCell(j).getStringCellValue()
								.contains("ballContent")) {
							
								ballContent = value;
								map.put("ballContent", ballContent);
							
							
							continue;
						}
						else if (hrow.getCell(j).getStringCellValue()
								.contains("exceptWinMoney")) {
						        if(cell.getCellType()==Cell.CELL_TYPE_BLANK)
						        continue;
								exceptWinMoney = value;
								map.put("exceptWinMoney", exceptWinMoney);
					
							
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
	
	
	
	
	public  Map<String,String> analysisData(int i){
		//System.out.println(list.size());
		if(i>list.size()-1)  return null;
		map = list.get(i);
		
	    String  str =  this.sendMessage(i);
	    //System.out.println(str);
	    String [] s  =  str.split(",");
	    
		   
	   
	    for (String ss:s)
	       {
	    	
	    	if(ss.contains("checkDetailCount") )
	    	  {
	    		 
	    		  String [] aa = ss.split(":");
	    		  
	    		  if(aa.length>=2)
	    		    {
	    			  
	    			  checkDetailCount = aa[1].substring(1, aa[1].length()-1);
	    		    }
	    		  map.put("checkDetailCount", checkDetailCount);
	    			  
	    	  }
	    			  
	    	 
	    	
	    	
	    	
	    	else if(ss.contains("amount")) 
	    	    {
	    		  String [] bb = ss.split(":");
	    		  if(bb.length>=2)
	    		    {
	    			  winMoney = bb[1];
	    			 // System.out.println(bb[1]);
	    			 
	    		    }
	    		
	    		 map.put("winMoney", winMoney);
	    	    }
	    		
	    		
	       }
	    
	  
	    
	    if(map.get("exceptWinMoney")!=null && map.get("exceptWinMoney").equals(map.get("winMoney")))
	    	 map.put("result", "ok");
	    
	    if(map.get("exceptWinMoney")!=null && !map.get("exceptWinMoney").equals(map.get("winMoney")))
	    	map.put("result", "no");
	    
	   if (map.get("winMoney").equals("null"))
	    {
	    	map.put("result", "格式不正确");
	    
	    }
	    	
	
	  //  else  map.put("result", "no");
	    
	return map;
		
	}
	
	
	
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void  writeDate(List list){
		

		 HSSFWorkbook workbook = null;
		 HSSFSheet sheet = null;
		 FileInputStream in = null;
	     FileOutputStream out = null;
	     HSSFCell cell = null;
		 HSSFCell resultcell = null;
	     workbook = new HSSFWorkbook();
	    

		try {       
			        
					
				
					
				    sheet = workbook.createSheet();
				    HSSFRow hrow = sheet.createRow(0);
				    for(int k=0;k<=6;k++){
				    	
				    	cell = hrow.createCell(k);
				    	cell.setCellType(Cell.CELL_TYPE_STRING);
				    	if(k==0) cell.setCellValue("lotteryId");
				    	else if(k==1) cell.setCellValue("issueNo");
				    	else if(k==2) cell.setCellValue("playType");
				    	else if(k==3) cell.setCellValue("ballContent");
				    	else if(k==4) cell.setCellValue("winMoney");
				    	else if(k==5) cell.setCellValue("exceptWinMoney");
				    	else if(k==6) cell.setCellValue("result");
 				    }
				   
				    
				   
				    
				
			for (int i = 1; i <= list.size(); i++) {
				
				 HSSFRow irow = sheet.createRow(i);
				 int column = 6;
				 
				 map = (Map<String, String>) list.get(i-1);
				 
				 
				
					for (int j = 0; j <= column; j++) {
						
						
						if (hrow.getCell(j).getStringCellValue()
								.contains("lotteryId")) {
							
							cell =  irow.createCell(j);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue((String)map.get("lotteryId"));
							
							continue;
						}
					  if (hrow.getCell(j).getStringCellValue()
								.contains("issueNo")) {
							
							cell =  irow.createCell(j);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue((String)map.get("issueNo"));
							
							continue;
						}
						
						else if (hrow.getCell(j).getStringCellValue()
								.contains("playType")) {
							
							 cell =  irow.createCell(j);
							 cell.setCellType(Cell.CELL_TYPE_STRING);
							 cell.setCellValue((String)map.get("playType"));
							
							continue;
						}
						
						else if (hrow.getCell(j).getStringCellValue()
								.contains("ballContent")) {
							
							 cell =  irow.createCell(j);
							 cell.setCellType(Cell.CELL_TYPE_STRING);
							 cell.setCellValue((String)map.get("ballContent"));
					
							continue;
						}
						else if (hrow.getCell(j).getStringCellValue()
								.contains("winMoney")) {
							
							cell =  irow.createCell(j);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue((String)map.get("winMoney"));
							
							continue;
						}
						else if (hrow.getCell(j).getStringCellValue()
								.contains("exceptWinMoney")) {
							
							cell =  irow.createCell(j);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue((String)map.get("exceptWinMoney"));
						
							continue;
						}
						else if (hrow.getCell(j).getStringCellValue()
								.contains("result")) {
							
							cell =  irow.createCell(j);
							cell.setCellType(Cell.CELL_TYPE_STRING);
							cell.setCellValue((String)map.get("result"));
							
							continue;
						}
						
			
					}
            	
					
			}
			
		
				    out = new FileOutputStream("D:/calculatePrize001.xls");

					workbook.write(out);
					out.close();

				
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
	
	
	
	
	
	
	

}
