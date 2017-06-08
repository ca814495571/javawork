package com.cqfc.test.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jami.util.Log;



public class ReadAndWriteExcle {
    //private Map<String,String>map  = new HashMap<String, String>();
	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	private SendMessage sendMsg = null;
	private String transcode = null;
	private String partner_id = null;
	private String issue_no = null;
	private String resultTranscode = null;
	private String returnTranscode = null;
    //private int flag=1;
	
	@SuppressWarnings("unused")
	public void ReadAndWrite(String direct,int num) {

		FileInputStream in = null;
		FileOutputStream out = null;
		String str = null;
		String newStr = null;

		sendMsg = new SendMessage();
		try {

					in = new FileInputStream(direct);
				    workbook = new HSSFWorkbook(in);
				    for(int m=0;m<num;m++){
				    
				    sheet = workbook.getSheetAt(m);
				    int row = sheet.getLastRowNum();
				    HSSFRow hrow = sheet.getRow(0);
				    int hnum = hrow.getLastCellNum();
				    
				    int n = 0;
				    for (int k = 0; k <= hnum; k++) {
				    	if (null == hrow.getCell(k)) {
				    		break;
				    	}
				    	
				    	if (hrow.getCell(k).getStringCellValue().trim().equals("")) {
				    		break;
				    	}
				    	
				    	if (hrow.getCell(k).getStringCellValue()
				    			.contains("sendMsessage")) {
				    		n = k;
				    	}
				    	
				    }
				    

			    HSSFRow firstRow = sheet.getRow(1);
				str = firstRow.getCell(n).getStringCellValue();
				HSSFCell cell = null;
				HSSFCell resultcell = null;
				loop: 
			for (int i = 1; i <= row; i++) {
				
				 HSSFRow irow = sheet.getRow(i);
				 int column = irow.getLastCellNum();
				
					for (int j = 0; j < column; j++) {
						

						if ( null == irow.getCell(0)) {
							break loop;
						   } 
						if (irow.getCell(0).getStringCellValue().trim()
								.equals("")) {
							break loop;
						   }

						if (hrow.getCell(j).getStringCellValue()
								.contains("receiveMsessage")) {
							     cell = irow.getCell(j);
							   if (cell == null) {
								   cell = irow.createCell(j);
							   }
							   continue;
						    }
							

						if (hrow.getCell(j).getStringCellValue()
								.contains("result")) {
							   resultcell = irow.getCell(j);
							   if (resultcell == null) {
								   resultcell = irow.createCell(j);
							        }
							   break;
						    }
							
							
								
								
							

						if (null == irow.getCell(j)) {
							break;
						}

						if (irow.getCell(j).getStringCellValue().trim()
								.equals("")) {
							break;
						}
						
						if (hrow.getCell(j).getStringCellValue()
								.equals("transcode")) {
							
							transcode = irow.getCell(j).getStringCellValue();
						
							continue;
						}
						if (hrow.getCell(j).getStringCellValue()
								.equals("returnTranscode")) {
							
							returnTranscode = irow.getCell(j).getStringCellValue();
							
							continue;
						}
						
						

						if (irow.getCell(j).getStringCellValue()
								.contains("<?xml version")) {
							continue;
						}

						else if (hrow.getCell(j).getStringCellValue()
								.contains("receiveMsessage")) {
							cell = irow.getCell(j);

							if (cell == null) {
								cell = irow.createCell(j);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);

							}
						}

						else {

							str = this.replace(str, hrow.getCell(j)
									.getStringCellValue()
									+ "="
									+ "'"
									+ irow.getCell(j).getStringCellValue()
									+ "'");

						}

					}
                     
					System.out.println(str);
					
					
					 String value = sendMsg.send(str,transcode,partner_id);
					
					  
					 System.out.println(value);
					 
					 if(value!=null  &&  !"".equals(value.trim()))
					 { 
						 
						 cell.setCellValue(value);
						 resultTranscode = resultTranscode(value);
						 
						 
					 }
					 

					 if(resultTranscode!=null && resultTranscode.equals(returnTranscode))
					   {
						  resultcell.setCellValue("ok");
						 
					   }
					 
					  else  resultcell.setCellValue("no");
					 
					 
					 
				}
				
				    }

				out = new FileOutputStream("D:/otherInterface001.xls");

				workbook.write(out);
				out.close();
		         //System.out.println(str);

			

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

				if (out != null) {

					try {
						out.close();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				}

			}

		}

	}

	
	public String replace(String str, String... args) {
		String newStr = str;
		for (String s : args) {

			if (str.contains("partnerid") && s.contains("partnerid")) {

				String regex = "partnerid=\\'[A-Za-z0-9]+\\'";
				newStr = newStr.replaceAll(regex, s);
		        String [] ss = s.split("=");
		        String sss =  ss[1].substring(1, ss[1].length()-1);
				partner_id = sss;
			}
				
				


			else if (str.contains("gameid") && s.contains("gameid")) {
				String regex = "gameid=\\'[A-Za-z0-9]+\\'";
				newStr = newStr.replaceAll(regex, s);
			}
			
			
			

			else if (str.contains("issueno") && s.contains("issueno")) {
				String regex = "issueno=\\'[A-Za-z0-9]+\\'";
				newStr = newStr.replaceAll(regex, s);
				String [] ss = s.split("=");
				String sss =  ss[1].substring(1, ss[1].length()-1);
				issue_no = sss;
			}
				

			
			else if (str.contains("province") && s.contains("province")) {
				String regex = "province=\\'[A-Za-z0-9]+\\'";
				newStr = newStr.replaceAll(regex, s);
			}
			
			
			
			/*else if (str.contains("id") && s.contains("id")) {
				if(flag<=10){
					
					String regex = "<ticket id=\\'.+\\'";
					String id="<ticket id="+"\'"+map.get( String.valueOf(flag))+"\'";
					newStr = newStr.replaceFirst(regex, id);
					++flag;
					
				  }
				
				
			}*/
			
			
			

		}

		return newStr;
	}
	

	public String resultTranscode(String msg){
		 
	    if(msg==null) return null;
		String regex = "transcode=.[A-Za-z0-9]+.";
		
		Pattern p = Pattern.compile(regex);
	    Matcher m = p.matcher(msg);
	    int startIndex = 0;
	    int endIndex  =  0;
	 
	    while(m.find()){
	        startIndex = m.start();
	        endIndex = m.end();
	        
	    } 
	   
		String str = msg.substring(startIndex, endIndex);
		String [] s =  str.split("=");
		if(s.length>=2){
			
					String ss = s[1].substring(1, s[1].length()-1);
					return ss;
		}
		
      
		return null;
	}

	
	
	public String send104(String ticketId){
		/*String strXml1 ="<?xml version='1.0' encoding='UTF-8' ?>\n"
				+ "<msg>"
				+ "<head transcode='104' partnerid='00860000002' version='1.0' time='201408211432' />\n"
				+ "  <body>\n"
				+ "   <ticketorder gameid=\'"+gameid+"\' ticketsnum='1' totalmoney='2' province='sz' machine='127.0.0.1'>\n"
				+ "        <user userid='0' realname='testuser1' idcard='123456789123456789' phone='12345678911' />\n" 
				+ "     <tickets>\n"
				+ "             <ticket id=\'"+ticketId+"\' multiple=\'"+multiple+"\' issue=\'"+issue+"\' playtype=\'"+playtype+"\' money=\'"+money+"\'>\n"
				+ "                   <ball>"+ball+"</ball>\n"
				+ "             </ticket>\n"
				+ "     </tickets>\n"
				+ "  </ticketorder>\n"
				+ "  </body>\n"
				+"</msg>";*/
		
		
		
		String strXml2 ="<?xml version='1.0' encoding='UTF-8' ?>\n"
				+ "<msg>"
				+ "<head transcode='104' partnerid='00860000002' version='1.0' time='201408211432' />\n"
				+ "  <body>\n"
				+ "   <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='2' province='sz' machine='127.0.0.1'>\n"
				+ "        <user userid='0' realname='testuser1' idcard='123456789123456789' phone='12345678911' />\n" 
				+ "     <tickets>\n"
				+ "             <ticket id=\'"+ticketId+"\' multiple='1' issue='2014107' playtype='1' money='336'>\n"
				+ "                   <ball>03,07,13,14,19,22,25,28,30:09,14</ball>\n"
				+ "             </ticket>\n"
				+ "     </tickets>\n"
				+ "  </ticketorder>\n"
				+ "  </body>\n"
				+"</msg>";
		
		
		/*String strXml3 ="<?xml version='1.0' encoding='UTF-8' ?>\n"
				+ "<msg>"
				+ "<head transcode='104' partnerid='00860000002' version='1.0' time='201408211432' />\n"
				+ "  <body>\n"
				+ "   <ticketorder gameid='SSQ' ticketsnum='1' totalmoney='2' province='sz' machine='127.0.0.1'>\n"
				+ "        <user userid='0' realname='testuser1' idcard='123456789123456789' phone='12345678911' />\n" 
				+ "     <tickets>\n"
				+ "             <ticket id=\'"+ticketId+"\' multiple='1' issue='2014107' playtype='2' money='40'>\n"
				+ "                   <ball>04,09,13,22:05,16,18,20,29:11,15 </ball>\n"
				+ "             </ticket>\n"
				+ "     </tickets>\n"
				+ "  </ticketorder>\n"
				+ "  </body>\n"
				+"</msg>";*/
		
		
		/*String regex = "<ticket id=\\'.+\\' ";
		String id="<ticket id="+"\'"+ticketId+"\'";
		String msg = strXml.replaceFirst(regex, id);*/
		SendMessage sm  = new  SendMessage();
		//System.out.println(strXml);
		
		
		
		   return sm.send(strXml2, "104","00860000002" );
		
		   
		

	}

	
	
	public String  send105(String ticketId){
		
	/*	
		<?xml version="1.0" encoding="UTF-8" ?>
		<msg>
		    <head transcode='105' partnerid='00860000001' version='1.0' time='201408211432' />
		    <body>
		        <queryticket id='SSQ20141051540' gameid='SSQ' issue='2014101' userid='44' />
		     </body>
		</msg>*/

		
		
		
		String strXml =
				 "<?xml version='1.0' encoding='UTF-8' ?>\n"
				+"<msg>\n"
				+"   <head transcode='105' partnerid='00860000002' version='1.0' time='201408211432' />\n"
				+"    <body>\n"
				+"        <queryticket id=\'"+ticketId+"\' gameid='SSQ' issue='2014107' userid='0' />\n"
				+"     </body>\n"
				+"</msg>\n";

	/*String regex = "<queryticket id=\\'.+\\'";
	String id="<queryticket id="+"\'"+ticketId+"\'";
	String msg = strXml.replaceFirst(regex, id);*/
	//System.out.println(msg);
	SendMessage sm  = new  SendMessage();
	
	
	return sm.send(strXml, "105","00860000002" );
	}
	
	
}
