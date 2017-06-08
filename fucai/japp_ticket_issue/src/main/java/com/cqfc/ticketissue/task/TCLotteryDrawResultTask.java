package com.cqfc.ticketissue.task;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.ticketissue.SnatchDrawResultService;
import com.cqfc.util.LotteryUtil;
import com.jami.util.Log;

public class TCLotteryDrawResultTask {
	/**
	 * 获取福彩开奖结果
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public static String snatchLotteryDrawResult(String lotteryId, String issueNo) {
		String result = "";
		try {
			TTransport transport = null;
			transport = new TSocket("10.247.68.220", 9091);
	
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			SnatchDrawResultService.Client client = new SnatchDrawResultService.Client(protocol);
			issueNo = LotteryUtil.convertIssueNo(lotteryId, issueNo);
			result = new String(client.snatchDrawResult(lotteryId, issueNo).getBytes("utf-8"));
	        transport.close();
		} catch (TTransportException e) {
			Log.run.error("snatchLotteryDrawResult(lotteryId: %s, issueNo: %s, TTransportException: %s)", lotteryId, issueNo, e);
		} catch (Exception e) {
			Log.run.error("snatchLotteryDrawResult(lotteryId: %s, issueNo: %s, Exception: %s)", lotteryId, issueNo, e);
		}
		Log.run.debug("snatchLotteryDrawResult(lotteryId: %s, issueNo: %s, result: %s)", lotteryId, issueNo, result);
		return result;
	
	}

	public static void main(String[] args) {
		System.out.println(snatchLotteryDrawResult("DLT", "2015030"));
	}
}
