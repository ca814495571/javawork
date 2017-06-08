package com.cqfc.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.cqfc.test.util.Test104And105Util;
import com.cqfc.test.util.TestLog;

public class QueryTicketFucai implements Runnable {

	private long m = 0;
	private long n = 0;

	public QueryTicketFucai(long m, long n) {
		this.m = m;
		this.n = n;
	}

	@Override
	public void run() {
		Test104And105Util t = new Test104And105Util();

		QueryTicketUtil qtu = new QueryTicketUtil();
		String ticketId = null;
		FileReader in = null;
		FileInputStream inPut = null;
		FileOutputStream out = null;
		BufferedReader br = null;
		boolean flag = true;
		long i = 0;
		Properties prop = new Properties();
		String str = null;

		try {
			if (flag) {
				out = new FileOutputStream("D:/testlog/data.properties", true);
				inPut = new FileInputStream("D:/testlog/data.properties");
				prop.load(inPut);
				if (prop.get(Thread.currentThread().getName()) != null) {
					i = Integer.parseInt((String) prop.get(Thread.currentThread().getName()));
					prop.clear();
				}
			}

			in = new FileReader("D:/testlog/201502603063.txt");
			br = new BufferedReader(in);

			while ((str = br.readLine()) != null) {
				i++;
				if (i < m) {
					continue;
				}
				String[] orderList = str.split("\\\t");

				ticketId = orderList[0];

				String str105 = qtu.Query(ticketId);

				if (t.isSuccess(str105)) {
					TestLog.success.debug("105请求返回的 message,msg=%s", str105);
				} else if (t.isInTransaction(str105)) {
					TestLog.inTransaction.debug("id=%s", ticketId.toLowerCase());
					TestLog.inTransaction.debug("105请求返回的 message,msg=%s", str105);
				} else if (t.isNotExist(str105)) {
					TestLog.notExist.debug("id=%s", ticketId.toLowerCase());
					TestLog.notExist.debug("105请求返回的 message,msg=%s", str105);
				} else if (t.isFail(str105)) {
					TestLog.fail.debug("id=%s", ticketId.toLowerCase());
					TestLog.fail.debug("105请求返回的 message,msg=%s", str105);
				} else if (t.isWaitTransaction(str105)) {
					TestLog.waitTaransaction.debug("id=%s", ticketId.toLowerCase());
					TestLog.waitTaransaction.debug("105请求返回的 message,msg=%s", str105);
				} else {
					TestLog.other.debug("id=%s", ticketId.toLowerCase());
					TestLog.other.debug("105请求返回的 message,msg=%s", str105);
				}
				System.out.println(str105);
				if (flag) {
					prop.setProperty(Thread.currentThread().getName(), String.valueOf(i));
					if (Thread.currentThread().getName().equals("first")) {
						System.out.println(i);
					}
					prop.store(out, String.valueOf(i));
				}
				if (i >= n) {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inPut != null) {
				try {
					inPut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	public static void main(String[] args) {
		new Thread(new QueryTicketFucai(1, 20000)).start();
		new Thread(new QueryTicketFucai(20001, 40000)).start();
		new Thread(new QueryTicketFucai(40001, 60000)).start();
		new Thread(new QueryTicketFucai(60001, 80000)).start();
		new Thread(new QueryTicketFucai(80001, 100000)).start();
		new Thread(new QueryTicketFucai(100001, 120000)).start();
		new Thread(new QueryTicketFucai(120001, 140000)).start();
		new Thread(new QueryTicketFucai(140001, 160000)).start();
		new Thread(new QueryTicketFucai(160001, 190000)).start();
	}

}
