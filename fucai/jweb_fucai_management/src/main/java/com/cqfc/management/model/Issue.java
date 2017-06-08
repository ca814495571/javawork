package com.cqfc.management.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class Issue implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5439310893079106860L;
	  public Integer issueId; // required
	  public String lotteryId; // required
	  public String issueNo; // required
	  public String drawResult; // required
	  public String state; // required
	  public String drawTime; // required
	  public String beginTime; // required
	  public String singleEndTime; // required
	  public String compoundEndTime; // required
	  public String singleTogetherEndTime; // required
	  public String compoundTogetherEndTime; // required
	  public String singleUploadEndTime; // required
	  public String printBeginTime; // required
	  public String printEndTime; // required
	  public String officialBeginTime; // required
	  public String officialEndTime; // required
	  public String prizePool; // required
	  public String ext; // required
	  public String createTime; // required
	  public String lastUpdateTime; // required
	
	
	public Issue(Integer issueId, String lotteryId, String issueNo,
			String drawResult, String state, String drawTime, String beginTime,
			String singleEndTime, String compoundEndTime,
			String singleTogetherEndTime, String compoundTogetherEndTime,
			String singleUploadEndTime, String printBeginTime,
			String printEndTime, String officialBeginTime,
			String officialEndTime, String prizePool, String ext,
			String createTime, String lastUpdateTime) {
		super();
		this.issueId = issueId;
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
		this.drawResult = drawResult;
		this.state = state;
		this.drawTime = drawTime;
		this.beginTime = beginTime;
		this.singleEndTime = singleEndTime;
		this.compoundEndTime = compoundEndTime;
		this.singleTogetherEndTime = singleTogetherEndTime;
		this.compoundTogetherEndTime = compoundTogetherEndTime;
		this.singleUploadEndTime = singleUploadEndTime;
		this.printBeginTime = printBeginTime;
		this.printEndTime = printEndTime;
		this.officialBeginTime = officialBeginTime;
		this.officialEndTime = officialEndTime;
		this.prizePool = prizePool;
		this.ext = ext;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
	}
	public Issue() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getIssueId() {
		return issueId;
	}
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getDrawResult() {
		return drawResult;
	}
	public void setDrawResult(String drawResult) {
		this.drawResult = drawResult;
	}
	public String getPrizePool() {
		return prizePool;
	}
	public void setPrizePool(String prizePool) {
		this.prizePool = prizePool;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDrawTime() {
		return drawTime;
	}
	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}
	public String getOfficialBeginTime() {
		return officialBeginTime;
	}
	public void setOfficialBeginTime(String officialBeginTime) {
		this.officialBeginTime = officialBeginTime;
	}
	public String getOfficialEndTime() {
		return officialEndTime;
	}
	public void setOfficialEndTime(String officialEndTime) {
		this.officialEndTime = officialEndTime;
	}
	public String getPrintBeginTime() {
		return printBeginTime;
	}
	public void setPrintBeginTime(String printBeginTime) {
		this.printBeginTime = printBeginTime;
	}
	public String getPrintEndTime() {
		return printEndTime;
	}
	public void setPrintEndTime(String printEndTime) {
		this.printEndTime = printEndTime;
	}
	public String getCompoundEndTime() {
		return compoundEndTime;
	}
	public void setCompoundEndTime(String compoundEndTime) {
		this.compoundEndTime = compoundEndTime;
	}
	public String getCompoundTogetherEndTime() {
		return compoundTogetherEndTime;
	}
	public void setCompoundTogetherEndTime(String compoundTogetherEndTime) {
		this.compoundTogetherEndTime = compoundTogetherEndTime;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getSingleEndTime() {
		return singleEndTime;
	}
	public void setSingleEndTime(String singleEndTime) {
		this.singleEndTime = singleEndTime;
	}
	public String getSingleTogetherEndTime() {
		return singleTogetherEndTime;
	}
	public void setSingleTogetherEndTime(String singleTogetherEndTime) {
		this.singleTogetherEndTime = singleTogetherEndTime;
	}
	public String getSingleUploadEndTime() {
		return singleUploadEndTime;
	}
	public void setSingleUploadEndTime(String singleUploadEndTime) {
		this.singleUploadEndTime = singleUploadEndTime;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(StringUtils.isNotBlank(this.lotteryId)){
			sb.append(" 彩种: ");
			sb.append(this.lotteryId);
		}
		
		
		if(StringUtils.isNotBlank(this.issueNo)){
			sb.append(" 期号: ");
			sb.append(this.issueNo);
		}
		
		return sb.toString();
	}
	
	
	
	
}
