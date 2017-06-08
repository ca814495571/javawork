package com.cqfc.ticketissue.testClient;

import com.cqfc.ticketissue.task.LotteryDrawResultTask;

public class TestDrawResult {
	public static void main(String[] args) {
		LotteryDrawResultTask.findLotteryDrawResult("SSQ", "2014111");
	}
}
