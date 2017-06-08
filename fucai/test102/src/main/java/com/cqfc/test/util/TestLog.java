package com.cqfc.test.util;

import com.jami.util.Log;
import com.jami.util.LotteryLogger;

public class TestLog extends Log {

	public static LotteryLogger ticketId = LotteryLogger.getLogger("ticketId");
	public static LotteryLogger id = LotteryLogger.getLogger("id");
	
	public static LotteryLogger fail = LotteryLogger.getLogger("fail");
	public static LotteryLogger notExist = LotteryLogger.getLogger("notExist");
	public static LotteryLogger inTransaction = LotteryLogger.getLogger("inTransaction");
	public static LotteryLogger waitTaransaction = LotteryLogger.getLogger("waitTaransaction");
	public static LotteryLogger success = LotteryLogger.getLogger("success");
	public static LotteryLogger other = LotteryLogger.getLogger("other");
	public static LotteryLogger touzhu = LotteryLogger.getLogger("touzhu");
	
	
	
}
