package com.cqfc.statistics.dao.baseDao;

import java.util.Map;

import com.cqfc.statistics.model.Branch;
import com.cqfc.statistics.model.Center;
import com.cqfc.statistics.model.Country;
import com.cqfc.statistics.model.Stat;

public interface IBaseDao {

	Map<String, Stat> getStationInfo();

	Map<String, Country> getCountryInfo();

	Map<String, Branch> getBranchInfo();

	Map<String, Center> getCenterInfo();

	Map<String, String> getLotteryItemInfo();

}
