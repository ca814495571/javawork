package com.cqfc.xmlparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GenerateTransactionMsgJavaFile {
	
	public static void generateFile(String transcode){
		File inputFile = new File("E://fucai/jweb_fucai/src/main/java/com/cqfc/xmlparser/TransactionMsgLoader.java");
		File outputFile = new File("E://fucai/jweb_fucai/src/main/java/com/cqfc/xmlparser/TransactionMsgLoader"+transcode+".java");
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile));
			BufferedReader buffer = new BufferedReader(reader);
			
			String line = "";
			String content = "";
			
			while((line = buffer.readLine()) != null){
				if(line.matches("(.*)com.cqfc.xmlparser.transactionmsg(.*)")){
					line = line.replaceAll("transactionmsg", "transactionmsg"+transcode);
				}	
				else if(line.matches("(.*)TransactionMsgLoader(.*)")){
					line = line.replaceAll("TransactionMsgLoader", "TransactionMsgLoader"+transcode);
				}
				content += line + "\n";
			}
			reader.close();
			buffer.close();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(content);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		File file = new File("E://fucai/jweb_fucai/src/main/resources");
		File[] files = file.listFiles();
		for (File f : files) {
			generateFile(f.getName().replaceAll("[^\\d]+", ""));
		}
		
		
	}
}
