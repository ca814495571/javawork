package com.cqfc.lottery.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryIssueService;

public class LotteryIssueClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10006);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			LotteryIssueService.Client lotteryIssue = new LotteryIssueService.Client(protocol);

			LotteryIssue issue = new LotteryIssue();
			issue.setLotteryId("QLC");
			issue.setIssueNo("2014137");
			issue.setDrawTime("2014-11-21 21:30:00");
			issue.setBeginTime("2014-11-19 19:45:00");
			issue.setSingleEndTime("2014-11-21 19:30:00");
			issue.setCompoundEndTime("2014-11-21 19:30:00");
			issue.setSingleTogetherEndTime("2014-11-21 19:30:00");
			issue.setCompoundTogetherEndTime("2014-11-21 19:30:00");
			issue.setSingleUploadEndTime("2014-11-21 19:30:00");
			issue.setPrintBeginTime("2014-11-20 00:00:10");
			issue.setPrintEndTime("2014-11-21 19:40:00");
			issue.setOfficialBeginTime("2014-11-19 19:45:00");
			issue.setOfficialEndTime("2014-11-21 19:30:00");
			// System.out.println(lotteryIssue.addOrUpdateLotteryItem(lotteryItem));
			// System.out.println(lotteryIssue.getLotteryItemList());
			// System.out.println(lotteryIssue.findLotteryItem(lotteryItem));
			// System.out.println(lotteryIssue.isExistLotteryId("SSQxs"));

			// System.out.println(lotteryIssue.getLotteryIssueList(null,1,5));
			// System.out.println(lotteryIssue.deleteIssueNo("FC3D",
			// "2014030"));
			// System.out.println(lotteryIssue.findLotteryIssue("QLC",
			// "2014104"));
			// System.out.println(lotteryIssue.findLotteryDrawResult("SSQ",
			// "2014001"));
			// System.out.println(lotteryIssue.getCreateMinTime("SSQ"));
			// System.out.println(lotteryIssue.updateLotteryIssueState("SSQ","2014001",2));

			System.out.println(lotteryIssue.updateLotteryIssue(issue));
			// System.out.println(lotteryIssue.getLotteryIssueList(issue, 1,
			// 10));
			// System.out.println(lotteryIssue.findLotteryIssue("QLC",
			// "2014134"));
			// System.out.println(lotteryIssue.createLotteryIssue("2014-09-07","2014-09-07","SSC"));
			// System.out.println(lotteryIssue.updateLotteryIssueState(7546,
			// 2));
			// System.out.println(lotteryIssue.createLotteryIssue("2014-01-02","2014-01-04","QLC"));
			// System.out.println(lotteryIssue.createLotteryIssue("2014-01-02","2014-01-02","FC3D"));
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException
					&& ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
				System.out.println("The result of function is NULL");
			}
		}
	}
}
