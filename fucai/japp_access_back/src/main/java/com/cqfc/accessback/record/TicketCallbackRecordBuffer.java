package com.cqfc.accessback.record;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.transactionmsg605.Msg;
import com.cqfc.xmlparser.transactionmsg605.Querytype;
import com.jami.util.RecordLogger;

public class TicketCallbackRecordBuffer {
	private static final Map<String, ResultRecord> map = new HashMap<String, ResultRecord>();

	public static void addRecord(String xml) {
		if (xml.indexOf("transcode=\"605\"") < 0) {
			return;
		}
		Msg msg = TransactionMsgLoader605.xmlToMsg(xml);
		Querytype ticketResult = msg.getBody().getTicketresult();
		String gameId = ticketResult.getGameid();
		String issue = ticketResult.getIssue();
		String statusCode = ticketResult.getStatuscode();
		if (!map.containsKey(gameId)) {
			map.put(gameId, new ResultRecord());
		}
		map.get(gameId).addResult(gameId, issue, statusCode);
		RecordLogger.getDynamicLogger(gameId).info(map.get(gameId).toLog());
	}

	public static Collection<ResultRecord> getResultRecords() {
		return map.values();
	}
	
	public static void addLog(String gameId, String logStr){
		ResultRecord record = new ResultRecord();
		record.init(logStr);
		map.put(gameId, record);
	}

	public static void afterUpdate() {
		for(ResultRecord record: map.values()){
			record.afterUpdate();
		}
	}
}
