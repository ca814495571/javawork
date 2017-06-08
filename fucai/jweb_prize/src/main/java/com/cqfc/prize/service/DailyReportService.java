package com.cqfc.prize.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.prize.dao.DailyReportDao;
import com.cqfc.prize.model.DailyReport;
import com.cqfc.prize.service.Iservice.IDailyReportService;
import com.cqfc.prize.util.DataSourceContextHolder;
import com.cqfc.prize.util.DataSourceUtil;
@Service
public class DailyReportService implements IDailyReportService{

	@Autowired
	private DailyReportDao dailyReportDao;
	
	@Override
	public boolean insertDailyReport(DailyReport dailyReport) {
		
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.DB_NAME_TEMP_PRIZE);
		
		int flag = dailyReportDao.insertDailyReport(dailyReport);
		
		if(flag == 1){
			return true;
		}
		
		return false;
	}

	@Override
	public List<DailyReport> getDailyReport(String currentDay) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.DB_NAME_TEMP_PRIZE);
	//	DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE+DataSourceUtil.DB_NAME_TEMP_PRIZE);
		
		List<DailyReport> dailyReports =  dailyReportDao.getDailyReportByCountTime(currentDay);
		
		return dailyReports;
	}



}
