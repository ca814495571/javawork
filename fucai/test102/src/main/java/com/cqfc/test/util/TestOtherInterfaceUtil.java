package com.cqfc.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

public class TestOtherInterfaceUtil {
   
    private Map<String,String>map = null;
    private List<Map>list = new ArrayList<Map>();
	
    
	public List<Map> getList() {
		return list;
	}
	
	


	@SuppressWarnings("unused")
 	public void ReadAndWrite(String direct,int num) {
		
		
	
		 FileInputStream in = null;
	     HSSFWorkbook workbook = null;
		 HSSFSheet sheet = null;
		 HSSFCell cell = null;
		 String value = null;
		
		
		
		try {

					in = new FileInputStream(direct);
				    workbook = new HSSFWorkbook(in);
				    for(int m=0;m<num;m++){
				    
				    sheet = workbook.getSheetAt(m);
				    int row = sheet.getLastRowNum();
				    HSSFRow hrow = sheet.getRow(0);
				  
				    
			
			
			for (int i = 1; i <= row; i++) {
				
				 HSSFRow irow = sheet.getRow(i);
				 int column = irow.getLastCellNum();
					 map =  new HashMap<String,String>();
					for (int j = 0; j < column; j++) {
					
                       cell = irow.getCell(j);
                       if(cell==null) continue;
                       value = this.getCellValueToString(cell);
                       if(value==null || value.trim().equals("")) break;
                     
                       else  if (hrow.getCell(j).getStringCellValue()
								.contains("transcode")) {
                    	   
                    	   map.put("transcode", value);
					
							continue;
						}
					
                        else if (hrow.getCell(j).getStringCellValue()
                    		   .contains("partnerid")) {
                        	
                        	map.put("partnerid", value);
                    	  
                    	   continue;
                       }
                        else  if (hrow.getCell(j).getStringCellValue()
                    		   .contains("gameid")) {
                        	
                        	map.put("gameid", value);
                    	   
                    	   continue;
                       }
                       else	if (hrow.getCell(j).getStringCellValue()
								.contains("sendMsessage")) {
                    	    
							map.put("sendMsessage", value);
							continue;
						}

						


					}
                     
					list.add(map);
					
					
					 
				}
				
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

	
	public String getCellValueToString(HSSFCell cell )
	  {  
		
		switch(cell.getCellType()) 
		  {
		case Cell.CELL_TYPE_BLANK:
			return null;
			
		case Cell.CELL_TYPE_NUMERIC:
			return  String.valueOf(cell.getNumericCellValue());
			
		case Cell.CELL_TYPE_ERROR:
			
			return null;
			
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		
			
		case Cell.CELL_TYPE_FORMULA:
			return null;
			
		case Cell.CELL_TYPE_BOOLEAN:
			return null;
		
		  }
		
		
		return null;
	  }


    public  static void main(String []args){
    	
    	TestOtherInterfaceUtil tu  =  new TestOtherInterfaceUtil();
    	tu.ReadAndWrite("D:/otherInterface.xls", 1);
    	Map map = null;
    	List list = tu.getList();
    	for(int i=0;i<list.size();i++)
    	  {
    		
    	   map =(Map) list.get(i);
    	   System.out.println( map.get("transcode"));
    	   System.out.println( map.get("partnerid"));
    	   System.out.println( map.get("gameid"));
    	   System.out.println(  map.get("sendMsessage"));
    	 
    	  }
    
    	
    	
    	
    }




}
