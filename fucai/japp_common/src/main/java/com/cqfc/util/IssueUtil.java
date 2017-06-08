package com.cqfc.util;

import com.cqfc.util.DateUtil;
import com.cqfc.util.SportIssueConstant;
import com.jami.util.Log;

/**
 * @author liwh
 */
public class IssueUtil {

	/**
	 * transferId(13位)：赛事类型（1位,1足球 2篮球）+日期（8位,yyyyMMdd）+周几（1位,周日0--周六6）+赛事编号（3位）
	 * 
	 * @param matchDate
	 * @param matchNo
	 * @param matchType
	 * @return
	 */
	public static String getTransferId(String matchDate, String matchNo, int matchType) {
		String transferId = "";
		try {
			int len = matchNo.length();
			String type = "";
			if (matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue()) {
				type = "1";
			} else if (matchType == SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue()) {
				type = "2";
			}
			String dateTime = matchDate.replace("-", "");
			String weekValue = getWeekValue(matchNo.substring(0, 2));
			String dateNo = matchNo.substring(len - 3, len);
			transferId = type + dateTime + weekValue + dateNo;
			if (transferId.length() != 13) {
				transferId = "";
			}
		} catch (Exception e) {
			Log.run.error("获取转换赛事ID发生异常,matchNo=" + matchNo, e);
		}
		return transferId;
	}

	/**
	 * 北单赛事transferId(10位):赛事类型(1位,3北单 4北单胜负过关)+北单期号(6位)+场次号(3位)
	 * 
	 * @param issueNo
	 * @param matchNo
	 * @param matchType
	 * @return
	 */
	public static String getBeiDanTransferId(String issueNo, String matchNo, int matchType) {
		String transferId = "";
		try {
			String type = "";
			if (matchType == SportIssueConstant.CompetitionMatchType.BEIDAN.getValue()) {
				type = "3";
			} else if (matchType == SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue()) {
				type = "4";
			}
			int len = matchNo.length();
			matchNo = len == 1 ? "00" + matchNo : (len == 2 ? "0" + matchNo : matchNo);
			transferId = type + issueNo + matchNo;
			if (transferId.length() != 10) {
				transferId = "";
			}
		} catch (Exception e) {
			Log.run.error("获取北单转换赛事ID发生异常,issueNo=" + issueNo + ",matchNo=" + matchNo + ",matchType=" + matchType, e);
		}
		return transferId;
	}

	/**
	 * 拆分北单transferId(格式:type+issueNo+matchNo)
	 * 
	 * @param transferId
	 * @return
	 */
	public static String splitBeiDanTransferId(String transferId) {
		String returnValue = "";
		try {
			String type = transferId.substring(0, 1);
			String issueNo = transferId.substring(1, 7);
			int matchNo = Integer.valueOf(transferId.substring(7, 10));
			returnValue = type + "," + issueNo + "," + matchNo;
		} catch (Exception e) {
			Log.run.error("拆分北单transferId发生异常,transferId=" + transferId, e);
		}
		return returnValue;
	}

	private static String getWeekValue(String week) {
		String weekValue = "";
		try {
			if (week.equals("周一")) {
				weekValue = "1";
			} else if (week.equals("周二")) {
				weekValue = "2";
			} else if (week.equals("周三")) {
				weekValue = "3";
			} else if (week.equals("周四")) {
				weekValue = "4";
			} else if (week.equals("周五")) {
				weekValue = "5";
			} else if (week.equals("周六")) {
				weekValue = "6";
			} else if (week.equals("周日")) {
				weekValue = "0";
			}
		} catch (Exception e) {
			Log.run.error("获取转换赛事ID获取周几的value发生异常,week=" + week, e);
		}
		return weekValue;
	}

	/**
	 * 计算赛事的出票截止时间
	 * 
	 * @param matchTime
	 * @return
	 */
	public static String getPrintDeadlineTime(String matchTime) {
		String printDeadlineTime = "";
		try {
			String newTime = DateUtil.addDateMinut(matchTime, "HOUR", -12);
			String date = newTime.substring(0, 10);

			String week = DateUtil.getWeekOfDate(DateUtil.convertStringToDate("yyyy-MM-dd", date));
			String beginTime = date + " 09:00:00";
			String endTime = date + " 24:00:00";
			if (week.equals("Saturday") || week.equals("Sunday")) {
				endTime = DateUtil.addDateMinut(newTime, "DAY", 1).substring(0, 10) + " 01:00:00";
			}
			if (DateUtil.compareDateTime(beginTime, matchTime) > 0 && DateUtil.compareDateTime(matchTime, endTime) > 0) {
				printDeadlineTime = DateUtil.addDateMinut(matchTime, "MINUTE", -30);
			} else {
				printDeadlineTime = DateUtil.addDateMinut(endTime, "MINUTE", -30);
			}
		} catch (Exception e) {
			Log.run.error("计算赛事出票截止时间发生异常", e);
		}
		return printDeadlineTime;
	}

	public static void main(String args[]) {
		// System.out.println(getTransferId("2015-03-27 10:26:15", "周四009", 1));
		// System.out.println(getPrintDeadlineTime("2015-03-30 03:26:15"));
		System.out.println(splitBeiDanTransferId("3150504031"));
	}
}
