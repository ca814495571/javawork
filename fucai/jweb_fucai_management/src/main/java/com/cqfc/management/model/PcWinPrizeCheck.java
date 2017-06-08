package com.cqfc.management.model;

import java.io.Serializable;
import java.util.List;

public class PcWinPrizeCheck implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6857339765710970827L;
		private List<Object> winPrizeChecks ;
		private String lotteryId ;
		private String issueNo;
		private String prizeResult;
		private Integer status;//state值对应：1：中奖审核未审核13 2：中奖审核通过15、16、17 3： 派奖审核未审核 15  4：派奖审核通过17
		public PcWinPrizeCheck() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
		public PcWinPrizeCheck(List<Object> winPrizeChecks, String lotteryId,
				String issueNo, String prizeResult, Integer status) {
			super();
			this.winPrizeChecks = winPrizeChecks;
			this.lotteryId = lotteryId;
			this.issueNo = issueNo;
			this.prizeResult = prizeResult;
			this.status = status;
		}

		public List<Object> getWinPrizeChecks() {
			return winPrizeChecks;
		}

		public void setWinPrizeChecks(List<Object> winPrizeChecks) {
			this.winPrizeChecks = winPrizeChecks;
		}

		public String getLotteryId() {
			return lotteryId;
		}
		public void setLotteryId(String lotteryId) {
			this.lotteryId = lotteryId;
		}
		public String getIssueNo() {
			return issueNo;
		}
		public void setIssueNo(String issueNo) {
			this.issueNo = issueNo;
		}
		public String getPrizeResult() {
			return prizeResult;
		}
		public void setPrizeResult(String prizeResult) {
			this.prizeResult = prizeResult;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		
}
