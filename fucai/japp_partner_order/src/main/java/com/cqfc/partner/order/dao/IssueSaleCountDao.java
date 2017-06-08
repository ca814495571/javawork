package com.cqfc.partner.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.IssueSaleCountMapper;
import com.cqfc.protocol.partnerorder.IssueRiskCount;
import com.cqfc.protocol.partnerorder.IssueSaleCount;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class IssueSaleCountDao {

	@Autowired
	private IssueSaleCountMapper issueSaleCountMapper;

	public int insert(IssueSaleCount issueSaleCount) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			flag = issueSaleCountMapper.insert(issueSaleCount);
		} catch (Exception e) {
			Log.run.error("添加期销量统计记录数据库异常" ,e);
			Log.error("添加期销量统计记录数据库异常" ,e);
			throw e;
		}
		return flag;

	}

	public List<IssueSaleCount> getIssueSaleCount(String lotteryId,
			String issueNo, String tableName) {

		List<IssueSaleCount> issueSaleCounts = new ArrayList<IssueSaleCount>();
		try {

			issueSaleCounts = issueSaleCountMapper.getIssueSaleCount(lotteryId,
					issueNo, tableName);
		} catch (Exception e) {
			Log.run.error("添加彩种期号销量统计记录数据库异常" ,e);
			Log.error("添加彩种期号销量统计记录数据库异常" ,e);
		}
		return issueSaleCounts;
	}

	public IssueRiskCount getIssueRiskCount(String lotteryId, String issueNo) {
		
		IssueRiskCount issueRiskCount = new IssueRiskCount();
		
		try {
			
			issueRiskCount =  issueSaleCountMapper.getIssueRiskCount(lotteryId, issueNo);
		} catch (Exception e) {
			
			Log.run.error("获取期销量风控统计数据库异常" ,e);
			Log.error("获取期销量风控统计数据库异常" ,e);
		}
		return issueRiskCount;
	}

}
