package com.cqfc.util;

import java.io.Serializable;

/**
 * MQ发送消息对象
 * 
 * @author liwh
 * 
 */
public class ActivemqSendObject implements Serializable {

	private static final long serialVersionUID = -5880838986689700446L;

	/**
	 * 期号
	 */
	private String issueNo;

	/**
	 * 竞足竞篮北单赛事转换ID
	 */
	private String transferId;

	/**
	 * 彩种ID
	 */
	private String lotteryId;

	/**
	 * 封装对象
	 */
	private Object obj;

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(String lotteryId) {
		this.lotteryId = lotteryId;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}

}
