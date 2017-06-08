package com.cqfc.util.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class FileGenerateTools {
	static void generateRepeatTable() throws IOException {
		File sqlDir = new File(FileGenerateTools.class.getResource("/").getPath(), "../../src/main/sql");
		String sqlPath = sqlDir.getCanonicalPath();
		String srcFile = sqlPath + "\\t_lottery_user.sql";
		String descFile = sqlPath + "\\t_lottery_user_all.sql";
		File descF = new File(descFile);
		descF.delete();
		BufferedReader in= new BufferedReader(new FileReader(srcFile));
		FileOutputStream out = new FileOutputStream(descFile, true);
		String readLine = "";
		String totalStr = "";
		try {
			while (in.ready()){
				readLine = in.readLine();
				totalStr += readLine + System.getProperty("line.separator");
			}
			String outStr = null;
			String index = null;
			
			for(int i=0;i<100;i++){
				index = getIndex(i);
				outStr = totalStr.replaceAll("\\{idx\\}", index);
//				outStr = outStr.replaceAll("\\{index\\}", "" + (i+1));
				out.write(outStr.getBytes());
			}
			System.out.println("生成sql文件成功，sqlFile=" + descFile);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			in.close();
			out.close();
		}
	}
	private static String getIndex(int i){
		String index = (i<10) ? "0" + i : "" + i;
		return index;
	}
	static void generateAlterTable(){
		int dbNum = 10;
		int tableNum = 100;
		String db_prefix = "fucai_user_account_";
		String table_prefix = "t_lottery_user_info_";
		for(int i=0; i<dbNum;i++){
			for(int j=0;j<tableNum; j++){
				System.out.println("alter table " + db_prefix + getIndex(i) + "." + table_prefix + getIndex(j) + " change prizePassword prizePassword varchar(64) null default '';");
			}
		}
	}
	public static void main(String[] args) throws IOException {
//		FileGenerateTools.generateRepeatTable();
		FileGenerateTools.generateAlterTable();
	}
}
