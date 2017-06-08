package com.cqfc.partner.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.DailyReportCountMapper;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.PcDailyReport;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class DailyReportCountDao {

	@Autowired
	private DailyReportCountMapper dailyReportCountMapper;

	public int insert(DailySaleAndCharge dailySaleAndCharge) throws Exception {

		int flag = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;

		try {
			flag = dailyReportCountMapper.insert(dailySaleAndCharge);
		} catch (Exception e) {
			Log.run.error(e);
			Log.error("添加日报统计信息数据库异常",e);
			throw e;
		}
		return flag;

	}

	public PcDailyReport getDailyReportByWhere(DailySaleAndCharge dailySaleAndCharge,int pageNum,int pageSize) {
		PcDailyReport pcDailyReport = new PcDailyReport();
		List<DailySaleAndCharge> dailyAwardCounts = new ArrayList<DailySaleAndCharge>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" 1=1 ");
			
			if(StringUtils.isNotBlank(dailySaleAndCharge.getPartnerId())){
				sb.append(" and partnerId = '");
				sb.append(dailySaleAndCharge.getPartnerId());
				sb.append("'");
			}
			
			if(StringUtils.isNotBlank(dailySaleAndCharge.getCountTime())){
				sb.append(" and countTime = '");
				sb.append(dailySaleAndCharge.getCountTime());
				sb.append("'");
			}
			
			if(StringUtils.isNotBlank(dailySaleAndCharge.getLotteryId())){
				sb.append(" and lotteryId = '");
				sb.append(dailySaleAndCharge.getLotteryId());
				sb.append("'");
			}
			
			
			
			int totalNum = dailyReportCountMapper.getDailyReportByWhereSum(sb.toString());
			sb.append(" order by countTime desc ");
			
			if (pageNum != 0 && pageSize != 0) {

				sb.append(" limit ");
				sb.append((pageNum - 1) * pageSize);
				sb.append(",");
				sb.append(pageSize);

			}
			
			dailyAwardCounts = dailyReportCountMapper.getDailyReportByWhere(sb.toString());
			pcDailyReport.setTotalNum(totalNum);
			pcDailyReport.setDailySaleAndCharges(dailyAwardCounts);
		} catch (Exception e) {
			Log.run.error("数据库出现异常", e);
			Log.error("数据库出现异常", e);
		}
		return pcDailyReport;
	}

}
