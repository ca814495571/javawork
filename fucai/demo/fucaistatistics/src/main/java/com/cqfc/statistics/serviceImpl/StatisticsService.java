package com.cqfc.statistics.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.common.DateIterator;
import com.cqfc.statistics.model.ActivityScancodeSource;
import com.cqfc.statistics.model.Branch;
import com.cqfc.statistics.model.BranchLottery;
import com.cqfc.statistics.model.BranchLotterySum;
import com.cqfc.statistics.model.BranchSum;
import com.cqfc.statistics.model.Center;
import com.cqfc.statistics.model.CenterLottery;
import com.cqfc.statistics.model.CenterLotterySum;
import com.cqfc.statistics.model.CenterSum;
import com.cqfc.statistics.model.Country;
import com.cqfc.statistics.model.CountryLottery;
import com.cqfc.statistics.model.Deal;
import com.cqfc.statistics.model.Stat;
import com.cqfc.statistics.model.StatDeal;
import com.cqfc.statistics.model.StatLottery;
import com.cqfc.statistics.model.StatLotterySum;
import com.cqfc.statistics.model.StatSum;
import com.cqfc.statistics.model.StatTpActivity;
import com.cqfc.statistics.model.StatTpActivityInfo;
import com.cqfc.statistics.model.StatTpActivitySum;
import com.cqfc.statistics.model.StatUser;
import com.cqfc.statistics.model.User;

/**
 * @author: giantspider@126.com
 */

@Service
public class StatisticsService {

	private static final Logger logger = LoggerFactory
			.getLogger(StatisticsService.class);

	@Autowired
	CqfcService cqfcService;

	@Autowired
	RdbService rdbService;

	public void t_stat(Date startDate, Date endDate) {
		logger.info("[start] [t_stat] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));

		Iterator<Date> i = new DateIterator(startDate, endDate);
		while (i.hasNext()) {
			Date date = i.next();

			Set<String> stations = cqfcService.getStationInfo().keySet();
			for (String station : stations) {

				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isStatCalculated(station, time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_stat]", CommonUtil.formatDate(date), station));
					Stat stat = cqfcService.getStat(station, date);
					rdbService.insertStat(stat);
				}
			}
		}

		logger.info("[end] [t_stat] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_country(startDate, endDate);

	}

	public void t_stat_lottery(Date startDate, Date endDate) {
		logger.info("[start] [t_stat_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		Set<String> stations = cqfcService.getStationInfo().keySet();
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : stations) {
				for (Map.Entry<String, String> entry : cqfcService
						.getLotteryItemInfo().entrySet()) {
					String lotterId = entry.getKey();

					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService.isStatLotteryCalculated(
							station, entry.getValue(), time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_stat_lottery]",
								CommonUtil.formatDate(date), lotterId, station));
						StatLottery statLottery = cqfcService.getStatLottery(
								station, lotterId, date);
						rdbService.insertStatLottery(statLottery);
					}
				}
			}
		}

		logger.info("[end] [t_stat_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_country_lottery(startDate, endDate);
	}

	public void t_user(Date startDate, Date endDate) {
		logger.info("[start] [t_user] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = new Date();
		for (String station : cqfcService.getStationInfo().keySet()) {
			if (CqfcService.ISNOT_STATION.equals(station)) {
				continue;
			}
			logger.info(String.format(
					"%22s date=%s startDate=%s bet_station=%s", "[t_user]",
					CommonUtil.formatDate(currentDate),
					CommonUtil.formatDate(startDate), station));
			User user = cqfcService.getUser(station, startDate, currentDate);
			rdbService.insertUser(user);
		}
		logger.info("[end] [t_user] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	public void t_stat_user() {
		Date currentDate = new Date();
		logger.info("[start] [t_stat_user] Date="
				+ CommonUtil.formatDate(currentDate));
		for (String station : cqfcService.getStationInfo().keySet()) {
			if (CqfcService.ISNOT_STATION.equals(station)) {
				continue;
			}
			logger.info(String.format("%22s date=%s  bet_station=%s",
					"[t_stat_user]", CommonUtil.formatDate(currentDate),
					station));
			List<StatUser> users = cqfcService.getStatUser(station);
			System.out.println(station + ":" + users.size());

			rdbService.insertStatUser(users);
		}
		logger.info("[end] [t_stat_user] Date="
				+ CommonUtil.formatDate(currentDate));
	}

	public void t_deal(Date startDate, Date endDate) {
		logger.info("[start] [t_deal] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		for (String station : cqfcService.getStationInfo().keySet()) {
			if (CqfcService.ISNOT_STATION.equals(station)) {
				continue;
			}
			Iterator<Date> i = new DateIterator(startDate, currentDate);
			while (i.hasNext()) {
				Date date = i.next();
				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isDealCalculated(station, time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_deal]", CommonUtil.formatDate(date), station));
					Deal deal = cqfcService.getDeal(station, date);
					rdbService.insertDeal(deal);
				}
			}
		}
		logger.info("[end] [t_deal] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	// 统计绑定投注站的下单信息
	public void t_stat_deal(Date startDate, Date endDate) {
		logger.info("[start] [t_stat_deal] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));

		Date currentDate = endDate;
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			long time = CommonUtil.getRdbTime(date);
			for (String station : cqfcService.getStationInfo().keySet()) {
				if (CqfcService.ISNOT_STATION.equals(station)) {
					continue;
				}

				boolean calculated = rdbService.isStatDealCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_stat_deal]", CommonUtil.formatDate(date),
							station));
					List<StatDeal> statDealList = new ArrayList<StatDeal>();
					statDealList = cqfcService.getStatDeal(station, date);
					rdbService.insertStatDeal(statDealList);

				}
			}
		}
		logger.info("[end] [t_deal] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	public void t_stat_sum(Date startDate, Date endDate) {
		logger.info("[start] [t_stat_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		Set<String> stations = cqfcService.getStationInfo().keySet();
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : stations) {

				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isStatSumCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_stat_sum]", CommonUtil.formatDate(date),
							station));
					StatSum statSum = cqfcService.getStatSum(station, date);
					rdbService.insertStatSum(statSum);
				}
			}
		}

		t_branch_sum(startDate, endDate);
		logger.info("[end] [t_stat_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	public void t_stat_lottery_sum(Date startDate, Date endDate) {
		logger.info("[start] [t_stat_lottery_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		Set<String> stations = cqfcService.getStationInfo().keySet();
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : stations) {
				for (Map.Entry<String, String> entry : cqfcService
						.getLotteryItemInfo().entrySet()) {
					String lotterId = entry.getKey();

					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService.isStatLotterySumCalculated(
							station, entry.getValue(), time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_stat_lottery_sum]",
								CommonUtil.formatDate(date), lotterId, station));
						StatLotterySum statLotterySum = cqfcService
								.getStatLotterySum(station, lotterId, date);
						rdbService.insertStatLotterySum(statLotterySum);
					}
				}
			}
		}
		t_branch_lottery_sum(startDate, endDate);
		logger.info("[end] [t_stat_lottery_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	/**
	 * 区域统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_country(Date startDate, Date endDate) {
		logger.info("[start] [t_country] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		for (String station : cqfcService.getCountryInfo().keySet()) {
			Iterator<Date> i = new DateIterator(startDate, endDate);
			while (i.hasNext()) {
				Date date = i.next();
				// yyyy-MM-dd
				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isCountryCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_country]", CommonUtil.formatDate(date), station));
					Country country = cqfcService.getCountry(station, date);
					rdbService.insertCountry(country);
				}
			}
		}
		logger.info("[end] [t_country] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_branch(startDate, endDate);
	}

	/**
	 * 区域分彩种统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_country_lottery(Date startDate, Date endDate) {
		logger.info("[start] [t_country_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : cqfcService.getCountryInfo().keySet()) {
				for (Map.Entry<String, String> entry : cqfcService
						.getLotteryItemInfo().entrySet()) {
					String lotterName = entry.getValue();
					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService.isCountryLotteryCalculated(
							station, lotterName, time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_country_lottery]",
								CommonUtil.formatDate(date), lotterName,
								station));
						CountryLottery countryLottery = cqfcService
								.getCountryLottery(station, lotterName, date);
						rdbService.insertCountryLottery(countryLottery);
					}
				}
			}
		}
		logger.info("[end] [t_country_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_branch_lottery(startDate, endDate);
	}

	/**
	 * 分中心统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_branch(Date startDate, Date endDate) {
		logger.info("[start] [t_branch] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		for (String station : cqfcService.getBranchInfo().keySet()) {
			Iterator<Date> i = new DateIterator(startDate, endDate);
			while (i.hasNext()) {
				Date date = i.next();
				// yyyy-MM-dd
				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isBranchCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_branch]", CommonUtil.formatDate(date), station));
					Branch branch = cqfcService.getBranch(station, date);
					rdbService.insertBranch(branch);
				}
			}
		}
		logger.info("[end] [t_branch] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_center(startDate, endDate);
	}

	/**
	 * 分中心分彩种统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_branch_lottery(Date startDate, Date endDate) {
		logger.info("[start] [t_branch_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		for (String station : cqfcService.getBranchInfo().keySet()) {
			for (Map.Entry<String, String> entry : cqfcService
					.getLotteryItemInfo().entrySet()) {
				String lotterName = entry.getValue();
				Iterator<Date> i = new DateIterator(startDate, currentDate);
				while (i.hasNext()) {
					Date date = i.next();
					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService.isBranchLotteryCalculated(
							station, lotterName, time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_branch_lottery]",
								CommonUtil.formatDate(date), lotterName,
								station));
						BranchLottery branchLottery = cqfcService
								.getBranchLottery(station, lotterName, date);
						rdbService.insertBranchLottery(branchLottery);
					}
				}
			}
		}
		logger.info("[end] [t_branch_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_center_lottery(startDate, endDate);
	}

	/**
	 * 分中心到当前时间的统计信息
	 * 
	 * @param currentDate
	 * @param currentDate2
	 */
	public void t_branch_sum(Date startDate, Date endDate) {
		logger.info("[start] [t_branch_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		for (String station : cqfcService.getBranchInfo().keySet()) {
			Iterator<Date> i = new DateIterator(startDate, currentDate);
			while (i.hasNext()) {
				Date date = i.next();
				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isBranchSumCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_branch_sum]", CommonUtil.formatDate(date),
							station));
					BranchSum branchSum = cqfcService.getBranchSum(station,
							date);
					rdbService.insertBranchSum(branchSum);
				}
			}
		}
		logger.info("[end] [t_branch_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_center_sum(startDate, endDate);
	}

	/**
	 * 分中心到当前时间的统计信息分彩种
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_branch_lottery_sum(Date startDate, Date endDate) {
		logger.info("[start] [t_branch_lottery_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		for (String station : cqfcService.getBranchInfo().keySet()) {
			for (Map.Entry<String, String> entry : cqfcService
					.getLotteryItemInfo().entrySet()) {
				String lotterName = entry.getValue();
				Iterator<Date> i = new DateIterator(startDate, currentDate);
				while (i.hasNext()) {
					Date date = i.next();
					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService
							.isBranchLotterySumCalculated(station, lotterName,
									time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_branch_lottery_sum]",
								CommonUtil.formatDate(date), lotterName,
								station));
						BranchLotterySum branchLotterySum = cqfcService
								.getBranchLotterySum(station, lotterName, date);
						rdbService.insertBranchLotterySum(branchLotterySum);
					}
				}
			}
		}
		logger.info("[end] [t_branch_lottery_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		t_center_lottery_sum(startDate, endDate);
	}

	/**
	 * 中心统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_center(Date startDate, Date endDate) {
		logger.info("[start] [t_center] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Iterator<Date> i = new DateIterator(startDate, endDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : cqfcService.getCenterInfo().keySet()) {
				// yyyy-MM-dd
				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isCenterCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_center]", CommonUtil.formatDate(date), station));
					Center center = cqfcService.getCenter(station, date);
					rdbService.insertCenter(center);
				}
			}
		}
		logger.info("[end] [t_center] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	/**
	 * 中心分彩种统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_center_lottery(Date startDate, Date endDate) {
		logger.info("[start] [t_center_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : cqfcService.getCenterInfo().keySet()) {
				for (Map.Entry<String, String> entry : cqfcService
						.getLotteryItemInfo().entrySet()) {
					String lotterName = entry.getValue();
					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService.isCenterLotteryCalculated(
							station, lotterName, time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_stat_lottery]",
								CommonUtil.formatDate(date), lotterName,
								station));
						CenterLottery centerLottery = cqfcService
								.getCenterLottery(station, lotterName, date);
						rdbService.insertCenterLottery(centerLottery);
					}
				}
			}
		}
		logger.info("[end] [t_center_lottery] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	/**
	 * 分中心到当前时间的统计信息
	 * 
	 * @param currentDate
	 * @param currentDate2
	 */
	public void t_center_sum(Date startDate, Date endDate) {
		logger.info("[start] [t_center_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;
		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {
			Date date = i.next();
			for (String station : cqfcService.getCenterInfo().keySet()) {

				long time = CommonUtil.getRdbTime(date);
				boolean calculated = rdbService.isCenterSumCalculated(station,
						time);
				if (!calculated) {
					logger.info(String.format("%22s date=%s bet_station=%s",
							"[t_center_sum]", CommonUtil.formatDate(date),
							station));
					CenterSum centerSum = cqfcService.getCenterSum(station,
							date);
					rdbService.insertCenterSum(centerSum);
				}
			}
		}
		logger.info("[end] [t_center_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	/**
	 * 中心到当前时间的统计信息分彩种
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_center_lottery_sum(Date startDate, Date endDate) {
		logger.info("[start] [t_center_lottery_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = endDate;

		Iterator<Date> i = new DateIterator(startDate, currentDate);
		while (i.hasNext()) {

			Date date = i.next();
			for (String station : cqfcService.getCenterInfo().keySet()) {
				for (Map.Entry<String, String> entry : cqfcService
						.getLotteryItemInfo().entrySet()) {
					String lotterName = entry.getValue();

					long time = CommonUtil.getRdbTime(date);
					boolean calculated = rdbService
							.isCenterLotterySumCalculated(station, lotterName,
									time);
					if (!calculated) {
						logger.info(String.format(
								"%22s date=%s lotteryId=%s bet_station=%s",
								"[t_center_lottery_sum]",
								CommonUtil.formatDate(date), lotterName,
								station));
						CenterLotterySum centerLotterySum = cqfcService
								.getCenterLotterySum(station, lotterName, date);
						rdbService.insertCenterLotterySum(centerLotterySum);
					}
				}
			}
		}
		logger.info("[end] [t_center_lottery_sum] startDate="
				+ CommonUtil.formatDate(startDate) + " endDate="
				+ CommonUtil.formatDate(endDate));
	}

	/**
	 * 201411投票活动数据统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_stat_tpActivity(Date endDate) {
		logger.info("[start] [t_stat_activity] Date="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = new Date();

		for (String bet_station : cqfcService.getStationInfo().keySet()) {

			// long time = CommonUtil.getRdbTime(currentDate);
			logger.info(String.format("%22s date=%s  bet_station=%s",
					"[t_stat_tpActivity]", CommonUtil.formatDate(currentDate),
					bet_station));
			StatTpActivity statTpActivity = cqfcService.getStatTpActivity(
					bet_station, currentDate);
			rdbService.insertStatTpActivity(statTpActivity);
		}
		logger.info("[end] [t_stat_activity] Date="
				+ CommonUtil.formatDate(endDate));
		t_stat_tpActivitySum(endDate);
	}

	/**
	 * 201411投票活动统计数据求和
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_stat_tpActivitySum(Date endDate) {
		logger.info("[start] [t_stat_tpActivitySum] Date="
				+ CommonUtil.formatDate(endDate));
		Date currentDate = new Date();

		// long time = CommonUtil.getRdbTime(currentDate);
		logger.info(String.format("%22s date=%s  ", "[t_stat_tpActivitySum]",
				CommonUtil.formatDate(currentDate)));
		StatTpActivitySum statTpActivitySum = cqfcService
				.getStatTpActivitySum(currentDate);
		rdbService.insertStatTpActivitySum(statTpActivitySum);
		logger.info("[end] [t_stat_tpActivitySum] Date="
				+ CommonUtil.formatDate(endDate));
	}

	/**
	 * 201411投票活动扫码来源统计
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void t_stat_ptScancode(Date endDate) {
		logger.info("[start] [t_stat_ptScancode] endDate="
				+ CommonUtil.formatDate(endDate));
		for (String station : cqfcService.getStationInfo().keySet()) {
			logger.info(String.format("%22s date=%s bet_station=%s",
					"[t_stat_ptScancode]", CommonUtil.formatDate(endDate),
					station));
			ActivityScancodeSource activityScancodeSource = cqfcService
					.getActivityScancodeSource(station, endDate);
			rdbService.insertActivityScancodeSource(activityScancodeSource);
		}
	}

	/**
	 * 201411投票活动用户相关信息
	 * 
	 */
	public void t_stat_ptActivityInfo() {

		Date currentDate = new Date();
		logger.info("[start] [t_stat_ptActivityInfo] Date="
				+ CommonUtil.formatDate(currentDate));
		for (String station : cqfcService.getStationInfo().keySet()) {
			logger.info(String.format("%22s date=%s bet_station=%s",
					"[t_stat_ptActivityInfo]",
					CommonUtil.formatDate(currentDate), station));
			List<StatTpActivityInfo> activityInfos = cqfcService
					.getTpActivityInfo(station);
			rdbService.insertActivityInfo(activityInfos);
		}
	}

}
