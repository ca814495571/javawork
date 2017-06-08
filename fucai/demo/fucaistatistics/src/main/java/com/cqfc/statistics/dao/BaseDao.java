package com.cqfc.statistics.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cqfc.statistics.dao.baseDao.IBaseDao;
import com.cqfc.statistics.dao.baseDao.TemplateBase;
import com.cqfc.statistics.model.Branch;
import com.cqfc.statistics.model.Center;
import com.cqfc.statistics.model.Country;
import com.cqfc.statistics.model.Stat;
import com.cqfc.statistics.serviceImpl.CqfcService;

@Repository
public class BaseDao extends TemplateBase implements IBaseDao{
	
	private static final Logger logger = LoggerFactory
			.getLogger(CqfcService.class);

	
	private static Map<String, Stat> stationMap = null;
	private long lastOpTime = 0L;

	/**
	 *  获取所有投注站信息
	 * @return
	 */
	@Override
	public Map<String, Stat> getStationInfo() {
		long delta = System.currentTimeMillis() - lastOpTime;
		if ((stationMap != null && delta < 14400 * 1000))
			return stationMap;
		lastOpTime = System.currentTimeMillis();
		stationMap = new HashMap<String, Stat>();
		String queryBetStation = "select distinct station_code, parent_code, station_name,address,station_type,business_type from t_lottery_betting_station where station_type = 2 order by station_code asc";
		List<Map<String, Object>> rows = cqfcFinanceTemplate
				.queryForList(queryBetStation);
		Stat station = null;
		for (Map<String, Object> row : rows) {
			station = new Stat();
			station.setBet_station((String) row.get("station_code") == null ? ""
					: (String) row.get("station_code"));
			station.setParentCode((String) row.get("parent_code") == null ? ""
					: (String) row.get("parent_code"));
			station.setStationName((String) row.get("station_name") == null ? ""
					: (String) row.get("station_name"));
			station.setAddress((String) row.get("address") == null ? ""
					: (String) row.get("address"));
			station.setStation_type((Integer) row.get("station_type") == null ? CONST_TWO
					: (Integer) row.get("station_type"));
			station.setBusiness_type((Integer) row.get("business_type") == null ? CONST_ZORE
					: (Integer) row.get("business_type"));
			stationMap.put((String) row.get("station_code"), station);
		}
		station = new Stat();
		station.setBet_station(VIRTUAL_STATION);
		station.setParentCode(VIRTUAL_STATION);
		station.setStationName(WEB_USER);
		station.setBusiness_type(CONST_ONE);
		station.setStation_type(CONST_TWO);
		station.setAddress("");
		stationMap.put(VIRTUAL_STATION, station); // 添加没有绑定投注站的编码0
		logger.info("total bet stations: " + stationMap.size());
		return stationMap;
	}

	
	private long getCountryLastOpTime = 0L;
	private static Map<String, Country> countryMap = null;
	/**
	 * 获取所有区县信息
	 * @return
	 */
	@Override
	public Map<String, Country> getCountryInfo(){
		long delta = System.currentTimeMillis() - getCountryLastOpTime;
		if((countryMap != null && delta < 14400 * 1000)) return countryMap;
		String queryCountry = "select distinct station_code,station_name,parent_code from t_lottery_betting_station where station_type = 4 order by station_code asc";
		countryMap = new HashMap<String, Country>();
		List<Map<String, Object>> rows = cqfcFinanceTemplate.queryForList(queryCountry);
		
		Country country = null;
		for(Map<String, Object> row : rows){
			country = new Country();
			country.setCountry_code((String) row.get("station_code"));
			country.setCountry_name((String) row.get("station_name"));
			country.setParentCode((String) row.get("parent_code"));
			countryMap.put((String) row.get("station_code"), country);
		}
		getCountryLastOpTime = System.currentTimeMillis();
		logger.info("total country: " + countryMap.size());
		return countryMap;
	}
	
	private long getBranchLastOpTime = 0L;
	private static Map<String, Branch> branchMap = null;
	/**
	 * 获取所有分中心信息
	 * @return
	 */
	@Override
	public Map<String, Branch> getBranchInfo() {
		long delta = System.currentTimeMillis() - getBranchLastOpTime;
		if ((branchMap != null && delta < 14400 * 1000))
			return branchMap;
		String queryBranch = "select distinct station_code,station_name,business_type from t_lottery_betting_station where station_type = 1 order by station_code asc";
		branchMap = new HashMap<String, Branch>();
		List<Map<String, Object>> rows = cqfcFinanceTemplate
				.queryForList(queryBranch);
		Branch branch = null;
		for (Map<String, Object> row : rows) {
			branch = new Branch();
			branch.setBranchCode((String) row.get("station_code"));
			branch.setBranchName((String) row.get("station_name"));
			branch.setBusiness_type((Integer) row.get("business_type"));
			branchMap.put((String) row.get("station_code"), branch);
		}
		branch = new Branch();
		branch.setBranchCode(VIRTUAL_STATION);
		branch.setBranchName(WEB_USER);
		branch.setBusiness_type(CONST_ONE);
		branchMap.put(VIRTUAL_STATION, branch);
		getBranchLastOpTime = System.currentTimeMillis();
		// IS_SELECT_BRANCH = false;
		logger.info("total branches: " + branchMap.size());
		return branchMap;
	}

	private long getCenterLastOpTime = 0L;
	private static Map<String, Center> centerMap = null;
	/**
	 * 获取所有中心信息
	 * @return
	 */
	@Override
	public Map<String, Center> getCenterInfo() {
		long delta = System.currentTimeMillis() - getCenterLastOpTime;
		if ((centerMap != null && delta < 14400 * 1000))
			return centerMap;
		String queryBranch = "select distinct station_code,station_name from t_lottery_betting_station where station_type = 0 order by station_code asc";
		centerMap = new HashMap<String, Center>();
		List<Map<String, Object>> rows = cqfcFinanceTemplate
				.queryForList(queryBranch);
		Center center = null;
		for (Map<String, Object> row : rows) {
			center = new Center();
			center.setCenterCode((String) row.get("station_code"));
			center.setCenterName((String) row.get("station_name"));
			centerMap.put((String) row.get("station_code"), center);
		}
		getCenterLastOpTime = System.currentTimeMillis();
		logger.info("total center: " + centerMap.size());
		return centerMap;
	}

	private long getLotteryItemLastOpTime = 0L;
	private static Map<String, String> lotteryItemMap = null;
	/**
	 * 获取所有彩种信息
	 * @return
	 */
	@Override
	public Map<String, String> getLotteryItemInfo() {
		
		long delta = System.currentTimeMillis() - getLotteryItemLastOpTime;
		
		if (lotteryItemMap != null && delta < 14400 * 1000)
			return lotteryItemMap;
		lotteryItemMap = new HashMap<String, String>();
		String queryLottery = "select Flottery_id, Flottery_name from t_lottery_item";
		for (Map<String, Object> lotteryItem : cqfcDbTemplate
				.queryForList(queryLottery)) {
			lotteryItemMap.put((String) lotteryItem.get("Flottery_id"),
					(String) lotteryItem.get("Flottery_name"));
		}
		getLotteryItemLastOpTime = System.currentTimeMillis();
		logger.info("total lottery:" + lotteryItemMap.size());
		return lotteryItemMap;
	}
	
	

}
