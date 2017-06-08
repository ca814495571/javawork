package com.cqfc.statistics.dao.baseDao;

import java.util.Date;
import java.util.Map;

import com.cqfc.statistics.model.Branch;
import com.cqfc.statistics.model.Center;
import com.cqfc.statistics.model.Country;
import com.cqfc.statistics.model.Stat;

public interface IStationDao {

	void stationUserStatics(Date time ,Stat station);
	
	void stationDealStatics(Date time , Stat station);
	
	void stationStatics(Date time , Stat station);
	
	void stationLotteryStatics(Date time , Stat station,Map<String, String> map);
	
	void countryLotteryStatics(Date time , Country station);
	
	void countryStatics(Date time , Country station);
	
	void branchLotteryStatics(Date time , Branch station);
	
	void branchStatics(Date time , Branch station);
	
	void centerLotteryStatics(Date time , Center station);
	
	void centerStatics(Date time , Center station);
}
