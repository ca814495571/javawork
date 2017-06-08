package com.cqfc.prize.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.prize.dao.DailyReportDao;
import com.cqfc.prize.dao.TempPrizeDao;
import com.cqfc.prize.model.DailyReport;
import com.cqfc.prize.service.Iservice.ITempPrizeService;
import com.cqfc.prize.util.DataSourceContextHolder;
import com.cqfc.prize.util.DataSourceUtil;
@Service
public class TempPrizeService implements ITempPrizeService {

	@Autowired
	private TempPrizeDao tempPrizeDao;
	
	@Autowired
	private DailyReportDao dailyReportDao;

	@Override
	public int cleanTemp(String time) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.DB_NAME_TEMP_PRIZE);
		int flag = tempPrizeDao.cleanTemp(time);
		return flag;
	}



	@Override
	public boolean dailyReportCount(String currentDay) {
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.DB_NAME_TEMP_PRIZE);
		List<DailyReport> dailyReports = tempPrizeDao.dailyReportCount(currentDay);
		int flag = 0;
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.DB_NAME_TEMP_PRIZE);
		for (DailyReport dailyReport:dailyReports) {
			dailyReport.setCountTime(currentDay);
			flag = dailyReportDao.insertDailyReport(dailyReport);
			
			if(flag == 0){
				return false;
			}
		}
		return true;
	}

	
}
