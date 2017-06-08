package com.cqfc.ticketissue.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.jami.util.Log;

public class WriteResultInfo {
	private final static String RIGHT_FILE_NAME = "D://20150306_ssq_success.txt";
	private final static String ERROR_FILE_NAME = "D://20150306_ssq_error.txt";
	private final static String RIGHT_FLAG = "statuscode";
	private static FileWriter rfw = null;
	private static FileWriter efw = null;
	private static BufferedWriter rbr = null;
	private static BufferedWriter ebr = null;

	static {
		try {
			rfw = new FileWriter(RIGHT_FILE_NAME, true);
			efw = new FileWriter(ERROR_FILE_NAME, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		rbr = new BufferedWriter(rfw);
		ebr = new BufferedWriter(efw);
	}

	public static void writeResult(int status, String result, String msg) {
		try {
			if (status == 200) {
				if (result != null && !"".equals(result)
						&& result.contains(RIGHT_FLAG)) {
					rbr.write("msg: " + msg);
					rbr.newLine();
					rbr.write("out ticket result: " + result);
					rbr.newLine();
				} else {
					ebr.write("msg: " + msg);
					ebr.newLine();
					ebr.write("out ticket result: " + result);
					ebr.newLine();
					System.out.println("out ticket result: " + result);
				}
			} else {
				ebr.write("out ticket result: " + result);
				ebr.newLine();
				System.out.println("out ticket result(status): " + status
						+ ", msg: " + msg);
			}
		} catch (Exception e) {
			System.out.println("out ticket result(Exception): " + msg + e);
			Log.run.error("out ticket result(Exception), msg: %s, exception: ",
					msg, e);
		} finally {
			if (rbr != null) {
				try {
					rbr.flush();
					rbr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ebr != null) {
				try {
					ebr.flush();
					ebr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		writeResult(200, "test success sdfas dafd", "xml");
	}
}
