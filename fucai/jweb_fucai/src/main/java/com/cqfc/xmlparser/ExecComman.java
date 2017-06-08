package com.cqfc.xmlparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExecComman {

	public static String execCommand(String command) {
		Runtime runtime = Runtime.getRuntime();
		String errorMSG = "";

		try {
			String[] args = new String[] { "cmd", "/c", command };
			// String[] args = new String[]{"sh","-?/c",command};

			Process pro = runtime.exec(args);
			// Process pro = runtime.exec("c://///////.exe");

			InputStream in = pro.getErrorStream();
			InputStreamReader isr = new InputStreamReader(in);

			BufferedReader br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null) {
				errorMSG += line + "\n";
				System.out.println(errorMSG);
			}

			// 检查命令是否失败
			try {
				if (pro.waitFor() != 0) {
					System.err.println("exit value:" + pro.exitValue());
				}
			} catch (InterruptedException e) {
				System.err.println();
				e.printStackTrace();

			}

		} catch (IOException e) {
			System.out.println("error Message:" + e.getMessage());
			e.printStackTrace();
		} finally {
			return errorMSG;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	/*	File file = new File("E://fucai/jweb_fucai/src/main/resources");
		File[] files = file.listFiles();
		List<String> list = new ArrayList<String>();
		for (File f : files) {
			list.add(f.getName().replaceAll("[^\\d]+", ""));
		}

		for (String str : list) {
			if (str != null && str.length() == 3) {
				//System.out.println(execCommand("xjc -p com.cqfc.xmlparser.transactionmsg"
							//	+ str + " -d ../java transdef" + str + ".xsd"));
				System.out.println(str);
			}
		}*/
		
		String str = "import com.cqfc.xmlparser.transactionmsg.Msg;";
		System.out.println(str.matches("(.*)com.cqfc.xmlparser.transactionmsg(.*)"));

		// System.out.println(execCommand("xjc -p com.cqfc.xmlparser.transactionmsg101 -d ../java transdef101.xsd"));

	}

}
