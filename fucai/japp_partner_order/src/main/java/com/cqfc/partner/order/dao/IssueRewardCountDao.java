package com.cqfc.partner.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.IssueRewardCountMapper;
import com.cqfc.protocol.partnerorder.IssueRewardCount;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class IssueRewardCountDao {

	@Autowired
	IssueRewardCountMapper issueRewardCountMapper;

	public int insert(IssueRewardCount issueRewardCount) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = issueRewardCountMapper.insert(issueRewardCount);

		} catch (Exception e) {
			Log.run.error("添加期中奖统计记录数据库异常",e);
			Log.error("添加期中奖统计记录数据库异常",e);
			throw e;
		}
		return flag;
	}

}
