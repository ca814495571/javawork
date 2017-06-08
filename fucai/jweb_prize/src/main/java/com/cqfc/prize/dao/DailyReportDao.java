package com.cqfc.prize.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.prize.dao.mapper.DailyReportMapper;
import com.cqfc.prize.model.DailyReport;
import com.jami.util.Log;

@Repository
public class DailyReportDao {

	@Autowired
	private DailyReportMapper dailyReportMapper;
	
	
	public int insertDailyReport(DailyReport dailyReport) {
		
		int flag = 0 ;
		try {
			
			flag = dailyReportMapper.insert(dailyReport);
		} catch (Exception e) {
			Log.run.error("insertDailyReport exception:"+e);
			return 0;
		}
		
		return flag;
	}
	
	
	
	public List<DailyReport> getDailyReportByCountTime(String countTime) {
		
		List<DailyReport> dailyReports = new ArrayList<DailyReport>();
		
		try {
			
			dailyReports = dailyReportMapper.getDailyReport(countTime);
		} catch (Exception e) {
			Log.run.error("getDailyReportByCountTime exception:"+e);
		}
		
		return dailyReports;
	}
	
	
	
}
