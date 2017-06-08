package com.cqfc.statistics.serviceImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.common.DateIterator;
import com.cqfc.statistics.common.IConstantUtil;
import com.cqfc.statistics.dao.BaseDao;
import com.cqfc.statistics.dao.StationDao;
import com.cqfc.statistics.model.Branch;
import com.cqfc.statistics.model.Center;
import com.cqfc.statistics.model.Country;
import com.cqfc.statistics.model.Stat;
import com.cqfc.statistics.serviceImpl.service.IStationStaticsService;

@Service
public class StationService implements IStationStaticsService,IConstantUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(StatisticsService.class);

	@Autowired
	private StationDao stationDao;

	@Autowired
	private BaseDao baseDao;


	@Override
	public void stationStatics(Date dateFrom, Date dateTo) {

		logger.info("[start] [t_stat] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Map<String, Stat> stationsMap = baseDao.getStationInfo();
		Iterator<Date> iterator = new DateIterator(dateFrom, dateTo);

		while (iterator.hasNext()) {

			Date date = iterator.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s bet_station=%s",
						"[t_stat]", CommonUtil.formatDate(date), stationCode));
				stationDao.stationStatics(date,
						stationsMap.get(stationCode));
			}
		}

		logger.info("[end] [t_stat] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		countryStatics(dateFrom, dateTo);
	}

	@Override
	public void countryStatics(Date dateFrom, Date dateTo) {

		logger.info("[start] [t_country] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));

		Map<String, Country> stationsMap = baseDao.getCountryInfo();

		Iterator<Date> i = new DateIterator(dateFrom, dateTo);
		while (i.hasNext()) {
			Date date = i.next();
			for (String stationCode : stationsMap.keySet()) {
				// yyyy-MM-dd
				logger.info(String.format("%22s date=%s bet_station=%s",
						"[t_country]", CommonUtil.formatDate(date), stationCode));
				stationDao.countryStatics(date, stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_country] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		branchStatics(dateFrom, dateTo);
	}

	@Override
	public void branchStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_branch] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Map<String, Branch> stationsMap = baseDao.getBranchInfo();
		Iterator<Date> i = new DateIterator(dateFrom, dateTo);
		while (i.hasNext()) {
			Date date = i.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s bet_station=%s",
						"[t_branch]", CommonUtil.formatDate(date), stationCode));
				stationDao.branchStatics(date, stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_branch] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		centerStatics(dateFrom, dateTo);

	}

	@Override
	public void centerStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_center] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Map<String, Center> stationsMap = baseDao.getCenterInfo();
		Iterator<Date> i = new DateIterator(dateFrom, dateTo);
		while (i.hasNext()) {
			Date date = i.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s bet_station=%s",
						"[t_center]", CommonUtil.formatDate(date), stationCode));
				stationDao.centerStatics(date, stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_center] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));

	}

	@Override
	public void stationLotteryStatics(Date dateFrom, Date dateTo) {

		logger.info("[start] [t_stat_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Map<String, Stat> stationsMap = baseDao.getStationInfo();
		Map<String, String> lotteryItem = baseDao.getLotteryItemInfo();
		Iterator<Date> iterator = new DateIterator(dateFrom, dateTo);

		while (iterator.hasNext()) {

			Date date = iterator.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s bet_station=%s",
						"[t_stat_lottery]", CommonUtil.formatDate(date),
						stationCode));
				stationDao.stationLotteryStatics(date,
						stationsMap.get(stationCode), lotteryItem);
			}
		}

		logger.info("[end] [t_stat_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		countryLotteryStatics(dateFrom, dateTo);
	}

	@Override
	public void countryLotteryStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_country_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Iterator<Date> i = new DateIterator(dateFrom, dateTo);
		Map<String, Country> stationsMap = baseDao.getCountryInfo();
		while (i.hasNext()) {
			Date date = i.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s  bet_station=%s",
						"[t_country_lottery]", CommonUtil.formatDate(date),
						stationCode));
				stationDao.countryLotteryStatics(date,
						stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_country_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		branchLotteryStatics(dateFrom, dateTo);

	}

	@Override
	public void branchLotteryStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_branch_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Iterator<Date> i = new DateIterator(dateFrom, dateTo);
		Map<String, Branch> stationsMap = baseDao.getBranchInfo();
		while (i.hasNext()) {
			Date date = i.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s  bet_station=%s",
						"[t_branch_lottery]", CommonUtil.formatDate(date),
						stationCode));

				stationDao.branchLotteryStatics(date,
						stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_branch_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		centerLotteryStatics(dateFrom, dateTo);

	}

	@Override
	public void centerLotteryStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_center_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Iterator<Date> i = new DateIterator(dateFrom, dateTo);
		Map<String, Center> stationsMap = baseDao.getCenterInfo();
		while (i.hasNext()) {
			Date date = i.next();
			for (String stationCode : stationsMap.keySet()) {
				logger.info(String.format("%22s date=%s  bet_station=%s",
						"[t_center_lottery]", CommonUtil.formatDate(date),
						stationCode));

				stationDao.centerLotteryStatics(date,
						stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_center_lottery] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));

	}

	@Override
	public void stationUserStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_stat_user] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Map<String, Stat> stationsMap = baseDao.getStationInfo();

		Iterator<Date> iterator = new DateIterator(dateFrom, dateTo);
		while (iterator.hasNext()) {
			Date date = iterator.next();
			for (String stationCode : stationsMap.keySet()) {
				
				if(VIRTUAL_STATION.equals(stationCode)){
					continue;
				}
				logger.info(String.format("%22s   date=%s bet_station=%s",
						"[t_stat_user]",CommonUtil.formatDate(date),stationCode));
				stationDao.stationUserStatics(date,
						stationsMap.get(stationCode));
			}
		}
		logger.info("[end] [t_stat_user] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));

	}

	@Override
	public void stationDealStatics(Date dateFrom, Date dateTo) {
		logger.info("[start] [t_stat_deal] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		Map<String, Stat> stationsMap = baseDao.getStationInfo();
		Iterator<Date> iterator = new DateIterator(dateFrom, dateTo);

		while (iterator.hasNext()) {
			Date date = iterator.next();
			for (String stationCode : stationsMap.keySet()) {
				
				if(VIRTUAL_STATION.equals(stationCode)){
					continue;
				}
				logger.info(String.format("%22s date=%s bet_station=%s",
						"[t_stat_deal]", CommonUtil.formatDate(date), stationCode));
				stationDao.stationDealStatics(date,
						stationsMap.get(stationCode));
			}
		}

		logger.info("[end] [t_stat_deal] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));

	}
}
