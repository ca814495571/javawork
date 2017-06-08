package com.cqfc.prize.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.prize.dao.mapper.TempPrizeMapper;
import com.cqfc.prize.model.DailyReport;
import com.jami.util.Log;

@Repository
public class TempPrizeDao {

	@Autowired
	private TempPrizeMapper tempPrizeMapper;
	
	public List<DailyReport> dailyReportCount(String currentDay) {
		
		List<DailyReport> dailyReports =null;
		try {
			
			StringBuffer sbf = new StringBuffer();
			
			sbf.append("where createTime>'");
			sbf.append(currentDay+" 00:00:00'");
			sbf.append(" and createTime <='");
			sbf.append(currentDay+" 23:59:59'");

			dailyReports = tempPrizeMapper.dailyReportCount(sbf.toString());
			
			
		} catch (Exception e) {
			Log.run.error("dailyReportCount exception:" +e);
		}
		
		return dailyReports;
	}


	public int cleanTemp(String time) {
		
		int flag = 0;
		try {
			
			flag = tempPrizeMapper.deleteTempPrize(time);
			
		} catch (Exception e) {
			Log.run.error("cleanTemp exception:" +e);
			flag = -100;
		}
		
		return flag;
	}
	
	
}
