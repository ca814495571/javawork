package com.cqfc.ticketissue.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.cqfc.ticketissue.model.TempOrder;
import com.cqfc.ticketissue.testClient.DataCenter;
import com.jami.util.Log;

public class ReadOrderFile {
	
	public static void getTempOrderList() throws IOException{
		File file = new File("D:\\fucai\\test_ticket_issue\\src\\main\\resources\\201502603063.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		int lineCount = 0;
		while ((line = br.readLine()) != null) {
			lineCount++;
			TempOrder to = new TempOrder();
			String[] lineSpr = line.split("\\s");
//			System.out.println(lineSpr.length);
//			System.out.println(Arrays.toString(lineSpr));
			if (lineSpr.length != 5){
				Log.run.error("line @@ " + lineCount +" @@ " + line);
				continue;
			}
			try {
				to.setId(lineSpr[0]);
				to.setMoney(Long.valueOf(lineSpr[4].trim()));
				to.setBall(lineSpr[3]);
				to.setMultiple(Long.valueOf(lineSpr[1].trim()));
				to.setPlayType(Long.valueOf(lineSpr[2].trim()));
				DataCenter.add(to);
				System.out.println(to);
			} catch (Exception e) {
				Log.run.error("@@ " + line);
			}
		}
		br.close();		
		System.out.println("process line count " + lineCount);
	}
	
	public static void main(String[] args) throws IOException {
		getTempOrderList();
	}
}
