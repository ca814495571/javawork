package com.cqfc.accessback.record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.cqfc.util.ConstantsUtil;
import com.jami.util.Log;
import com.jami.util.RecordLogger;

/**
 * 缓存中只保留近两期的数据，如果有第三期则丢弃
 * 
 * @author HowKeyond
 * 
 */
public class ResultRecord {
	private final static String SEPERATOR = ",";
	private final static String OBJECT_SEPERATOR = "@";
	// 如果超过30秒没有记日志，且状态有变化（主要是长时间没有订单回调时，期信息已入库，但日志中的状态还记的是未入库），则再记一条日志
	private final static int INTERVAL_TIME = 30000;
	private Map<String, Record> map = new ConcurrentHashMap<String, Record>();
	private Queue<Record> waitForUpdate = new LinkedBlockingQueue<Record>();
	private String currentIssue = "";
	private String preIssue = "";
	private long lastLogTime = 0;
	private boolean lastUpdateState;

	public void addResult(String gameId, String issue, String statusCode) {
		boolean status;
		if (ConstantsUtil.STATUS_CODE_TRADE_SUCCESS.equals(statusCode)) {
			status = true;
		} else if (ConstantsUtil.STATUS_CODE_RRADE_FAIL.equals(statusCode)) {
			status = false;
		} else {
			return;
		}
		if (currentIssue.isEmpty()) {
			addRecord(gameId, issue, status, true);
			return;
		}
		int compareTo = currentIssue.compareTo(issue);
		if (compareTo < 0) {
			addRecord(gameId, issue, status, true);
			return;
		}

		if (compareTo == 0) {
			updateStatus(map.get(issue), status);
			return;
		}

		if (preIssue.isEmpty()) {
			addRecord(gameId, issue, status, false);
			return;
		}
		compareTo = preIssue.compareTo(issue);
		if (compareTo < 0) {
			addRecord(gameId, issue, status, false);
			return;
		}

		if (compareTo == 0) {
			updateStatus(map.get(issue), status);
			return;
		}

		Record record = new Record(gameId, issue);
		updateStatus(record, status);
		record.isNew = false;
		waitForUpdate.add(record);

	}

	private void updateStatus(Record record, boolean statusCode) {
		if (record == null) {
			Log.run.error("record not found");
			return;
		}
		synchronized (map) {
			if (statusCode) {
				record.successNum++;
			} else {
				record.failedNum++;
			}
			record.hasUpdate = false;
		}
	}

	private void addRecord(String gameId, String issue, boolean statusCode,
			boolean add2last) {
		Record removeRecord = map.remove(preIssue);
		if (removeRecord != null && !removeRecord.hasUpdate) {
			waitForUpdate.add(removeRecord);
		}
		if (add2last) {
			preIssue = currentIssue;
			currentIssue = issue;
		} else {
			preIssue = issue;
		}
		Record record = new Record(gameId, issue);
		map.put(issue, record);
		updateStatus(record, statusCode);
	}

	public List<Record> getUpdateRecords() {
		List<Record> resultList = new ArrayList<ResultRecord.Record>();
		if (map.size() == 0) {
			return resultList;
		}
		this.waitForUpdate.removeAll(resultList);
		resultList.addAll(map.values());
		if ((map.get(currentIssue).hasUpdate != lastUpdateState)
				&& ((System.currentTimeMillis() - lastLogTime) > INTERVAL_TIME)) {
			RecordLogger.getDynamicLogger(map.get(currentIssue).gameId).info(
					toLog());
		}
		return resultList;
	}

	private void getLogStr(Record record, StringBuffer sb) {
		if (record == null)
			return;
		sb.append(record.gameId).append(SEPERATOR).append(record.issue)
				.append(SEPERATOR).append(record.successNum).append(SEPERATOR)
				.append(record.failedNum).append(SEPERATOR)
				.append(record.hasUpdate).append(SEPERATOR)
				.append(record.isNew);
	}

	public String toLog() {
		StringBuffer sb = new StringBuffer();
		sb.append(OBJECT_SEPERATOR);
		if (!preIssue.isEmpty()) {
			getLogStr(map.get(preIssue), sb);
			sb.append(OBJECT_SEPERATOR);
		}
		getLogStr(map.get(currentIssue), sb);
		lastLogTime = System.currentTimeMillis();
		lastUpdateState = map.get(currentIssue).hasUpdate;
		return sb.toString();
	}

	private static Record toRecord(String recordStr) throws Exception {
		String[] values = recordStr.trim().split(SEPERATOR);
		if (values.length != 6) {
			throw new Exception("recordStr format error.");
		}
		Record record = new Record(values[0], values[1]);
		record.successNum = Integer.parseInt(values[2]);
		record.failedNum = Integer.parseInt(values[3]);
		record.hasUpdate = Boolean.parseBoolean(values[4]);
		record.isNew = Boolean.parseBoolean(values[5]);
		return record;
	}

	public void init(String logStr) {
		if (logStr == null || logStr.isEmpty()) {
			return;
		}
		String[] records = logStr.split(OBJECT_SEPERATOR);
		if (records.length < 2) {
			return;
		}
		try {
			if (records.length == 3) {
				Record record = toRecord(records[1]);
				preIssue = record.issue;
				map.put(preIssue, record);
				record = toRecord(records[2]);
				currentIssue = record.issue;
				map.put(currentIssue, record);
			} else {
				Record record = toRecord(records[1]);
				currentIssue = record.issue;
				map.put(currentIssue, record);
			}
		} catch (Exception e) {
			Log.run.error("init records failed");
		}
	}

	public static String getUpdateSql() {
		return "update t_lottery_ticket_num set successNum=  ?, failedNum= ? where gameId=? and issue=? and setNo=?";
	}

	public static String getInsertSql() {
		return "insert into t_lottery_ticket_num(gameId,issue,setNo,successNum,failedNum) values(?,?,?,?,?) on duplicate key update successNum=successNum+?,failedNum=failedNum+?";
	}
	public static String getFirstTimeInsertSql() {
		return "insert into t_lottery_ticket_num(gameId,issue,setNo,successNum,failedNum) values(?,?,?,?,?) on duplicate key update successNum=?,failedNum=?";
	}

	public static void afterUpdate(Record record) {
		record.isNew = false;
		record.hasUpdate = true;
	}

	public void afterUpdate() {
		for (Record record : map.values()) {
			afterUpdate(record);
		}
	}

	static class Record {
		String gameId = "";
		String issue = "";
		int successNum = 0;
		int failedNum = 0;
		boolean hasUpdate = false;
		boolean isNew = true;

		public Record(String gameId, String issue) {
			this.gameId = gameId;
			this.issue = issue;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("gameId=").append(gameId).append(",issue =")
					.append(issue).append(",successNum =").append(successNum)
					.append(",failedNum =").append(failedNum)
					.append(",hasUpdate =").append(hasUpdate)
					.append(",isNew =").append(isNew);
			return sb.toString();
		}
	}

	public static void main(String[] args) throws Exception {
		Record r = toRecord("SSQ,2014152,1,0,false,true");
		System.out.println(r.toString());
	}
}
