package com.cqfc.order.model;

import java.io.Serializable;
import java.util.List;

import com.cqfc.protocol.businesscontroller.PrintMatch;

/**
 * @author liwh
 */
public class UpdateSportOrderStatus implements Serializable {

	private static final long serialVersionUID = -7946837578036768837L;

	private long orderNo;

	private int orderStatus;

	private String tradeId;

	private Boolean isBackReq;

	private int operateIdentifier;

	private String ticketNo;

	private String printTime;

	private int lotteryType;

	private List<PrintMatch> matchList;

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Boolean getIsBackReq() {
		return isBackReq;
	}

	public void setIsBackReq(Boolean isBackReq) {
		this.isBackReq = isBackReq;
	}

	public int getOperateIdentifier() {
		return operateIdentifier;
	}

	public void setOperateIdentifier(int operateIdentifier) {
		this.operateIdentifier = operateIdentifier;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	public List<PrintMatch> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<PrintMatch> matchList) {
		this.matchList = matchList;
	}

	public int getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(int lotteryType) {
		this.lotteryType = lotteryType;
	}

}
