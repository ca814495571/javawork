package com.cqfc.statistics.serviceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cqfc.statistics.common.CommonUtil;
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
public class CqfcService {

	private static final Logger logger = LoggerFactory
			.getLogger(CqfcService.class);

	/**
	 * 没有绑定投注站的编码
	 */
	public static final String ISNOT_STATION = "0";

	/**
	 * 没有绑定投注站的名称
	 */
	public static final String ISNOT_STATION_NAME = "网络用户";
	/**
	 * 没有绑定投注站的分中心编码
	 */
	public static final String ISNOT_BRANCH = "0";

	/**
	 * 没有绑定投注站的名称
	 */
	public static final String ISNOT_BRANCH_NAME = "网络用户";

	public static final Integer AMOUNT_1000 = new Integer(1000);

	public static final Integer AMOUNT_500 = new Integer(500);

	public static final int AMOUNT_0 = 0;

	public static final int AMOUNT_1 = 1;
	
	public static final int AMOUNT_2 = 2;
	// private boolean IS_SELECT_STAT = true;

	// private boolean IS_SELECT_BRANCH = true;

	@Autowired
	private JdbcTemplate cqfcJdbcTemplate;

	@Autowired
	private JdbcTemplate cqfcfinanceJdbcTemplate;

	@Autowired
	private JdbcTemplate rdbJdbcTemplate;

	// 获取所有的投注站: Map<station_code, parent_code>
	private static Map<String, Stat> stationMap = null;
	private long lastOpTime = 0L;

	public Map<String, Stat> getStationInfo() {
		long delta = System.currentTimeMillis() - lastOpTime;
		if ((stationMap != null && delta < 14400 * 1000))
			return stationMap;
		lastOpTime = System.currentTimeMillis();
		stationMap = new HashMap<String, Stat>();
		String queryBetStation = "select distinct station_code, parent_code, station_name,address,station_type,business_type from t_lottery_betting_station where station_type = 2 order by station_code asc";
		List<Map<String, Object>> rows = cqfcfinanceJdbcTemplate
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
			station.setStation_type((Integer) row.get("station_type") == null ? AMOUNT_2
					: (Integer) row.get("station_type"));
			station.setBusiness_type((Integer) row.get("business_type") == null ? AMOUNT_0
					: (Integer) row.get("business_type"));
			stationMap.put((String) row.get("station_code"), station);
		}
		station = new Stat();
		station.setBet_station(ISNOT_STATION);
		station.setParentCode(ISNOT_BRANCH);
		station.setStationName(ISNOT_STATION_NAME);
		station.setBusiness_type(AMOUNT_1);
		station.setStation_type(AMOUNT_2);
		station.setAddress("");
		// IS_SELECT_STAT = false;
		stationMap.put(ISNOT_STATION, station); // 添加没有绑定投注站的编码0
		logger.info("total bet stations: " + stationMap.size());
		return stationMap;
	}

	//获取所有区域
	private long getCountryLastOpTime = 0L;
	private static Map<String, Country> countryMap = null;
	public Map<String, Country> getCountryInfo(){
		long delta = System.currentTimeMillis() - getCountryLastOpTime;
		if((countryMap != null && delta < 14400 * 1000)) return countryMap;
		String queryCountry = "select distinct station_code,station_name,parent_code from t_lottery_betting_station where station_type = 4 order by station_code asc";
		countryMap = new HashMap<String, Country>();
		List<Map<String, Object>> rows = cqfcfinanceJdbcTemplate.queryForList(queryCountry);
		
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
	// 获取所有分中心
	private static Map<String, Branch> branchMap = null;

	public Map<String, Branch> getBranchInfo() {
		long delta = System.currentTimeMillis() - getBranchLastOpTime;
		if ((branchMap != null && delta < 14400 * 1000))
			return branchMap;
		String queryBranch = "select distinct station_code,station_name,business_type from t_lottery_betting_station where station_type = 1 order by station_code asc";
		branchMap = new HashMap<String, Branch>();
		List<Map<String, Object>> rows = cqfcfinanceJdbcTemplate
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
		branch.setBranchCode(ISNOT_BRANCH);
		branch.setBranchName(ISNOT_BRANCH_NAME);
		branch.setBusiness_type(AMOUNT_1);
		branchMap.put(ISNOT_BRANCH, branch);
		getBranchLastOpTime = System.currentTimeMillis();
		// IS_SELECT_BRANCH = false;
		logger.info("total branches: " + branchMap.size());
		return branchMap;
	}

	private long getCenterLastOpTime = 0L;
	// 获取所有中心的Map
	private static Map<String, Center> centerMap = null;

	public Map<String, Center> getCenterInfo() {
		long delta = System.currentTimeMillis() - getCenterLastOpTime;
		if ((centerMap != null && delta < 14400 * 1000))
			return centerMap;
		String queryBranch = "select distinct station_code,station_name from t_lottery_betting_station where station_type = 0 order by station_code asc";
		centerMap = new HashMap<String, Center>();
		List<Map<String, Object>> rows = cqfcfinanceJdbcTemplate
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

	// 获取所有的彩种 Map<lotteryId, lotteryName>
	private static Map<String, String> lotteryItemMap = null;

	public Map<String, String> getLotteryItemInfo() {
		if (lotteryItemMap != null)
			return lotteryItemMap;
		lotteryItemMap = new HashMap<String, String>();
		String queryLottery = "select Flottery_id, Flottery_name from t_lottery_item";
		for (Map<String, Object> lotteryItem : cqfcJdbcTemplate
				.queryForList(queryLottery)) {
			lotteryItemMap.put((String) lotteryItem.get("Flottery_id"),
					(String) lotteryItem.get("Flottery_name"));
		}
		logger.info("total lottery:" + lotteryItemMap.size());
		return lotteryItemMap;
	}

	// 获取某投注站在给定日期的统计信息
	public Stat getStat(String bet_station, Date givenDate) {
		String start = CommonUtil.getDateStart(givenDate);
		String end = CommonUtil.getDateEnd(givenDate);
		Object[] params = new Object[] { bet_station, start, end };
		String queryPay = "";
		String queryNewUser = "";
		if (ISNOT_STATION.equals(bet_station)) {
			
			queryPay = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where (Fbet_station=? or Fbet_station is null or Fbet_station='') and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
			queryNewUser = "select count(Fuser_id) from t_lottery_userinfo_all where (Fbet_station=? or Fbet_station is null or Fbet_station='') and Fregist_time>=? and Fregist_time<?";
		} else {
			queryPay = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
			queryNewUser = "select count(Fuser_id) from t_lottery_userinfo_all where Fbet_station=? and Fregist_time>=? and Fregist_time<?";
		
		}

		Map<String, Object> pay = cqfcJdbcTemplate
				.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("totalAmount");
		BigDecimal pay_cnt = (BigDecimal) pay.get("totalCount");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Long new_user = cqfcfinanceJdbcTemplate.queryForObject(queryNewUser,
				params, Long.class);
		if (new_user == null)
			new_user = 0L;
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Stat stat = getStationInfo().get(bet_station);
		
		return new Stat(rdbTime, pay_money, pay_cnt, new_user, bet_station,
				stat.getParentCode(), stat.getStationName(), stat.getAddress(),stat.getStation_type(),stat.getBusiness_type());
	}
	
	

	// 获取在某给定日期，某个彩种在某投注站的统计信息
	public StatLottery getStatLottery(String bet_station, String lotteryId,
			Date givenDate) {
		String start = CommonUtil.getDateStart(givenDate);
		String end = CommonUtil.getDateEnd(givenDate);
		Object[] params = new Object[] { bet_station, lotteryId, start, end };

		String queryPay = "";
		if (ISNOT_STATION.equals(bet_station)) {
			queryPay = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where (Fbet_station=? or Fbet_station is null or Fbet_station='') and  Flottery_id=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
		} else {
			queryPay = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and  Flottery_id=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
		}

		Map<String, Object> pay = cqfcJdbcTemplate
				.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("totalAmount");
		BigDecimal pay_cnt = (BigDecimal) pay.get("totalCount");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Stat stat = getStationInfo().get(bet_station);
		return new StatLottery(rdbTime, getLotteryItemInfo().get(lotteryId),
				pay_money, pay_cnt, bet_station, stat.getParentCode(),
				stat.getStationName(), stat.getAddress(),stat.getBusiness_type());
	}

	public User getUser(String bet_station, Date startDate, Date givenDate) {
		String content = getUserContent(bet_station, startDate, givenDate);
		Stat stat = getStationInfo().get(bet_station);
		return new User(content, bet_station, stat.getParentCode(),
				stat.getStationName(), stat.getAddress());
	}

	// 获取给定时间段的某投注站所有用户信息，按约定格式拼接成字符串
	private String getUserContent(String bet_station, Date startDate,
			Date givenDate) {
		List<User> ret = new ArrayList<User>();
		// Object[] params = new Object[] {bet_station,
		// CommonUtil.getDateStart(startDate),
		// CommonUtil.getDateEnd(givenDate)};
		// String queryUser =
		// "select Fnick, Fregist_time from t_lottery_userinfo_all where Fbet_station=? and Fregist_time>=? and Fregist_time<?";
		Object[] params = new Object[] { bet_station };
		String queryUser = "select Fnick, Fregist_time,Fuser_id from t_lottery_userinfo_all where Fbet_station=? order by Flast_update_time desc";
		List<Map<String, Object>> userInfoList = cqfcfinanceJdbcTemplate
				.queryForList(queryUser, params);
		StringBuilder contentBuilder = new StringBuilder();
		Long registTime = 0L;
		Object object = null;
		for (Map<String, Object> userinfo : userInfoList) {
			registTime = 0L;
			registTime = ((Timestamp) userinfo.get("Fregist_time")).getTime();

			object =  cqfcfinanceJdbcTemplate.queryForMap("select max(Fcreate_time) as 'bindTime' from t_lottery_station_bind_record_all where Fuser_id=? and Fstation_code=?", new Object[]{userinfo.get("Fuser_id"),bet_station}).get("bindTime");
			
			if(object!=null){
				registTime = ((Timestamp)object).getTime();
			}
			
			// try {
			// //Date date = CommonUtil.timestampFormat.parse((String)
			// userinfo.get("Fregist_time"));
			//
			// } catch (ParseException e) {
			// e.printStackTrace();
			// }
			contentBuilder.append(registTime).append("##")
					.append(userinfo.get("Fnick")).append("||");
		}
		if (contentBuilder.length() > 2)
			contentBuilder.setLength(contentBuilder.length() - 2); // remove the
																	// trailing
																	// "||"
		return contentBuilder.toString();
	}

	
	public List<StatUser> getStatUser(String bet_station) {
		List<StatUser> statUsers = new ArrayList<StatUser>();
		Object[] params = new Object[] { bet_station ,bet_station};
		String queryUser = "select Fnick, Fregist_time,Fuser_id from cqfcfinance.t_lottery_userinfo_all t1  where t1.Fbet_station=? and t1.Fuser_id  not in (select t2.user_id from rdb.t_stat_user t2 where t2.bet_station=?) ";
		List<Map<String, Object>> userInfoList = cqfcfinanceJdbcTemplate
				.queryForList(queryUser, params );
		
		StatUser statUser = null;
		Stat stat = getStationInfo().get(bet_station);
		for (Map<String, Object> userinfo : userInfoList) {
			
			statUser = new StatUser(bet_station, (String)userinfo.get("Fnick"), ((Timestamp)userinfo.get("Fregist_time")).toString(), stat.getBusiness_type(),(String)userinfo.get("Fuser_id"));
			statUsers.add(statUser);
		}
		return statUsers;
	}
	
	
	
	
	// 获取某投注站给定日期段内的订单信息
	public Deal getDeal(String bet_station, Date givenDate) {
		String start = CommonUtil.getDateStart(givenDate);
		String end = CommonUtil.getDateEnd(givenDate);
		Object[] params = new Object[] { bet_station, start, end };
		String queryDeal = "select Fbuy_time, Fnick, Flottery_name, Ftotal_amount, Fuser_id, Fdeal_id from t_lottery_deal_recent where Fbet_station=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>=? and Fbuy_time<?";
		List<Map<String, Object>> dealInfoList = cqfcJdbcTemplate.queryForList(
				queryDeal, params);
		StringBuilder contentBuilder = new StringBuilder();
		// Long buyTime = 0L;
		for (Map<String, Object> dealinfo : dealInfoList) {
			contentBuilder
					.append(((Timestamp) dealinfo.get("Fbuy_time")).getTime())
					.append("##").append(dealinfo.get("Fnick")).append("##")
					.append(dealinfo.get("Flottery_name")).append("##")
					.append(dealinfo.get("Ftotal_amount")).append("##")
					.append(dealinfo.get("Fuser_id")).append("##")
					.append(dealinfo.get("Fdeal_id")).append("||");
		}
		if (contentBuilder.length() > 2)
			contentBuilder.setLength(contentBuilder.length() - 2); // remove the
																	// trailing
																	// "||"
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Stat stat = getStationInfo().get(bet_station);
		return new Deal(rdbTime, contentBuilder.toString(), bet_station,
				stat.getParentCode(), stat.getStationName(), stat.getAddress());
	}
	
	
	// 获取某投注站给定日期段内的订单信息（只统计绑定投注站的）
		public List<StatDeal> getStatDeal(String bet_station, Date givenDate) {
			List<Map<String, Object>> dealInfoList =new ArrayList<Map<String,Object>>();
			String start = CommonUtil.getDateStart(givenDate);	
			String end = CommonUtil.getDateEnd(givenDate);
			Object[] params = new Object[] { bet_station,bet_station, start, end };
			String queryDeal = "select t.Fbuy_time, t.Fnick, t.Flottery_name, t.Ftotal_amount, t.Fuser_id, t.Fdeal_id from cqfcdb.t_lottery_deal_recent t where Fbet_station=? and t.Fdeal_id not in (select deal_id from rdb.t_stat_deal  where bet_station=?) and t.Fplan_type!=3 and t.Fdeal_status in (3,4,5,24) and t.Fbuy_time>=? and t.Fbuy_time<?";
			dealInfoList = cqfcJdbcTemplate.queryForList(
					queryDeal, params);
			
			StatDeal statDeals = null;
			List<StatDeal> statdeals = new ArrayList<StatDeal>();
			for (Map<String, Object> dealinfo : dealInfoList) {
				Stat stat = getStationInfo().get(bet_station);
				statDeals = new StatDeal();

				String buy_Time = ((Timestamp) dealinfo
						.get("Fbuy_time")).toString();
				String nick = (String) dealinfo.get("Fnick");
				String lottery = (String) dealinfo.get("Flottery_name");
				Long totalAmount = (Long) dealinfo.get("Ftotal_amount");
				String userId = (String) dealinfo.get("Fuser_id");
				Long dealId = (Long) dealinfo.get("Fdeal_id");

				statDeals.setTime(CommonUtil.getRdbTime(givenDate));
				statDeals.setBuyTime(buy_Time);
				statDeals.setNick(nick);
				statDeals.setLottery(lottery);
				statDeals.setTotalAmount(totalAmount);
				statDeals.setUserId(userId);
				statDeals.setDealId(dealId);
				statDeals.setBusiness_type(stat.getBusiness_type());
				statDeals.setBet_station(bet_station);
				statdeals.add(statDeals);
				
			}
			
			return statdeals;
			
		}
		
	

	// 获取到给定日期为止的某投注统计信息
	public StatSum getStatSum(String bet_station, Date givenDate) {
		String end = CommonUtil.getDateEnd(givenDate);
		Object[] params = new Object[] { bet_station, end };

		String queryDeal = "";
		String queryNewUser = "";
		if (ISNOT_STATION.equals(bet_station)) {
			queryDeal = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where (Fbet_station=? or Fbet_station is null or Fbet_station='') and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time<?";
			queryNewUser = "select count(Fuser_id) from t_lottery_userinfo_all where (Fbet_station=? or Fbet_station is null or Fbet_station='') and  Fregist_time<?";
		} else {
			queryDeal = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=? and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time<?";
			queryNewUser = "select count(Fuser_id) from t_lottery_userinfo_all where Fbet_station=? and  Fregist_time<?";
		}

		Map<String, Object> dealInfo = cqfcJdbcTemplate.queryForMap(queryDeal,
				params);
		BigDecimal pay_money = (BigDecimal) dealInfo.get("totalAmount");
		BigDecimal pay_cnt = (BigDecimal) dealInfo.get("totalCount");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Long new_user = cqfcfinanceJdbcTemplate.queryForObject(queryNewUser,
				params, Long.class);
		if (new_user == null)
			new_user = 0L;
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Stat stat = getStationInfo().get(bet_station);
		return new StatSum(rdbTime, pay_money, pay_cnt, new_user, bet_station,
				stat.getParentCode(), stat.getStationName(), stat.getAddress());
	}

	// 获取到给定日期为止的某投注某彩种的统计信息
	public StatLotterySum getStatLotterySum(String bet_station,
			String lotteryId, Date givenDate) {
		String end = CommonUtil.getDateEnd(givenDate);
		Object[] params = new Object[] { bet_station, lotteryId, end };

		String queryStatLotterySum = "";
		if (ISNOT_STATION.equals(bet_station)) {
			queryStatLotterySum = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where (Fbet_station=? or Fbet_station is null or Fbet_station='')  and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Flottery_id=? and Fbuy_time<?";
		} else {
			queryStatLotterySum = "select sum(Ftotal_amount) as totalAmount, sum(Fbuy_count) as totalCount from t_lottery_deal_recent where Fbet_station=?  and Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Flottery_id=? and Fbuy_time<?";
		}

		Map<String, Object> dealInfo = cqfcJdbcTemplate.queryForMap(
				queryStatLotterySum, params);
		String lotteryName = getLotteryItemInfo().get(lotteryId);
		BigDecimal pay_money = (BigDecimal) dealInfo.get("totalAmount");
		BigDecimal pay_cnt = (BigDecimal) dealInfo.get("totalCount");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Stat stat = getStationInfo().get(bet_station);
		return new StatLotterySum(rdbTime, lotteryName, pay_money, pay_cnt,
				bet_station, stat.getParentCode(), stat.getStationName(),
				stat.getAddress());
	}
	
	
	/**
	 * 获得某区域给定日期的统计信息
	 * @param countryCode
	 * @param givenDate
	 * @return
	 */
	public Country getCountry(String bet_station, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station, rdbTime };
		String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_stat where parent_code=? and time=?";
		Map<String, Object> pay = rdbJdbcTemplate.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		String queryNewUser = "select sum(new_user) from t_stat where parent_code=? and  time=?";
		Long new_user = rdbJdbcTemplate.queryForObject(queryNewUser, params,
				Long.class);
		if (new_user == null)
			new_user = 0L;
		Country country = getCountryInfo().get(bet_station);
		return new Country(rdbTime, pay_money, pay_cnt, new_user, bet_station, country.getCountry_name(),country.getParentCode());
	}
	
	/**
	 * 获取在某给定日期，某个彩种在某分中心的统计信息
	 * 
	 * @param bet_station
	 * @param lotteryId
	 * @param givenDate
	 * @return
	 */
	public CountryLottery getCountryLottery(String bet_station,
			String lotteryName, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station, lotteryName, rdbTime };
		String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt from t_stat_lottery where parent_code=? and lottery=? and time=? ";
		Map<String, Object> pay = rdbJdbcTemplate.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Country country = getCountryInfo().get(bet_station);
		return new CountryLottery(rdbTime, lotteryName, pay_money, pay_cnt,
				bet_station, country.getCountry_name(),country.getParentCode());
	}

	/**
	 * 获取某分中心在给定日期的统计信息
	 * 
	 * @param bet_station
	 * @param givenDate
	 * @return
	 */
	public Branch getBranch(String bet_station, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station, rdbTime };
		Branch branch = getBranchInfo().get(bet_station);
		String queryPay = "";
		String queryNewUser = "";
		if(branch.getBusiness_type() == 0){
			
			queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_country where parent_code=? and time=?";
			queryNewUser = "select sum(new_user) from t_country where parent_code=? and  time=?";
		}else {
			queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_stat where parent_code=? and time=?";
			queryNewUser = "select sum(new_user) from t_stat where parent_code=? and  time=?";
		}
		
		Map<String, Object> pay = rdbJdbcTemplate.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Long new_user = rdbJdbcTemplate.queryForObject(queryNewUser, params,
				Long.class);
		if (new_user == null)
			new_user = 0L;
		return new Branch(rdbTime, pay_money, pay_cnt, new_user, bet_station,
				branch.getBranchName(),branch.getBusiness_type());
	}

	/**
	 * 获取在某给定日期，某个彩种在某分中心的统计信息
	 * 
	 * @param bet_station
	 * @param lotteryId
	 * @param givenDate
	 * @return
	 */
	public BranchLottery getBranchLottery(String bet_station,
			String lotteryName, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station, lotteryName, rdbTime };
		Branch branch = getBranchInfo().get(bet_station);
		String queryPay = "";
		if(branch.getBusiness_type() == 0){
			 queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt from t_country_lottery where parent_code=? and lottery=? and time=? ";

		}else{
			 queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt from t_stat_lottery where parent_code=? and lottery=? and time=? ";
		}
		
		Map<String, Object> pay = rdbJdbcTemplate.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		return new BranchLottery(rdbTime, lotteryName, pay_money, pay_cnt,
				bet_station, branch.getBranchName());
	}

	/**
	 * 获取到给定日期为止的某分中心统计信息
	 * 
	 * @param bet_station
	 * @param givenDate
	 * @return
	 */
	public BranchSum getBranchSum(String bet_station, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station, rdbTime };
		String queryDeal = "select sum(pay_money) as pay_money , sum(pay_cnt) as pay_cnt from t_stat_sum where parent_code=? and time=?";
		Map<String, Object> dealInfo = rdbJdbcTemplate.queryForMap(queryDeal,
				params);
		BigDecimal pay_money = (BigDecimal) dealInfo.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) dealInfo.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		String queryNewUser = "select sum(new_user) from t_stat_sum where parent_code=? and time=?";
		Long new_user = rdbJdbcTemplate.queryForObject(queryNewUser, params,
				Long.class);
		if (new_user == null)
			new_user = 0L;
		Branch branch = getBranchInfo().get(bet_station);
		return new BranchSum(rdbTime, pay_money, pay_cnt, new_user,
				bet_station, branch.getBranchName());
	}

	/**
	 * 获取到给定日期为止的某投注彩分中心的统计信息
	 * 
	 * @param bet_station
	 * @param lotteryId
	 * @param givenDate
	 * @return
	 */
	public BranchLotterySum getBranchLotterySum(String bet_station,
			String lotteryName, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station, lotteryName, rdbTime };
		String queryStatLotterySum = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt from t_stat_lottery_sum where parent_code=? and lottery =? and time=?";
		Map<String, Object> dealInfo = rdbJdbcTemplate.queryForMap(
				queryStatLotterySum, params);
		BigDecimal pay_money = (BigDecimal) dealInfo.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) dealInfo.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Branch branch = getBranchInfo().get(bet_station);
		return new BranchLotterySum(rdbTime, lotteryName, pay_money, pay_cnt,
				bet_station, branch.getBranchName());
	}

	/**
	 * 获取中心在给定日期的统计信息
	 * 
	 * @param bet_station
	 * @param givenDate
	 * @return
	 */
	public Center getCenter(String bet_station, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { rdbTime };
		String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_branch where  time=?";
		Map<String, Object> pay = rdbJdbcTemplate.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		String queryNewUser = "select sum(new_user) from t_branch where time=?";
		Long new_user = rdbJdbcTemplate.queryForObject(queryNewUser, params,
				Long.class);
		if (new_user == null)
			new_user = 0L;
		Center center = getCenterInfo().get(bet_station);
		return new Center(rdbTime, pay_money, pay_cnt, new_user, bet_station,
				center.getCenterName());
	}

	/**
	 * 获取在某给定日期，某个彩种在中心的统计信息
	 * 
	 * @param bet_station
	 * @param lotteryId
	 * @param givenDate
	 * @return
	 */
	public CenterLottery getCenterLottery(String bet_station,
			String lotteryName, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { lotteryName, rdbTime };
		String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt from t_branch_lottery where  lottery=? and time=? ";
		Map<String, Object> pay = rdbJdbcTemplate.queryForMap(queryPay, params);
		BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Center center = getCenterInfo().get(bet_station);
		return new CenterLottery(rdbTime, lotteryName, pay_money, pay_cnt,
				bet_station, center.getCenterName());
	}

	/**
	 * 获取到给定日期为止的某中心统计信息
	 * 
	 * @param bet_station
	 * @param givenDate
	 * @return
	 */
	public CenterSum getCenterSum(String bet_station, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { rdbTime };
		String queryDeal = "select sum(pay_money) as pay_money , sum(pay_cnt) as pay_cnt from t_branch_sum where time=?";
		Map<String, Object> dealInfo = rdbJdbcTemplate.queryForMap(queryDeal,
				params);
		BigDecimal pay_money = (BigDecimal) dealInfo.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) dealInfo.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		String queryNewUser = "select sum(new_user) from t_branch_sum where time=?";
		Long new_user = rdbJdbcTemplate.queryForObject(queryNewUser, params,
				Long.class);
		if (new_user == null)
			new_user = 0L;
		Center center = getCenterInfo().get(bet_station);
		return new CenterSum(rdbTime, pay_money, pay_cnt, new_user,
				bet_station, center.getCenterName());
	}

	/**
	 * 获取到给定日期为止的某投注彩分中心的统计信息
	 * 
	 * @param bet_station
	 * @param lotteryId
	 * @param givenDate
	 * @return
	 */
	public CenterLotterySum getCenterLotterySum(String bet_station,
			String lotteryName, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { lotteryName, rdbTime };
		String queryStatLotterySum = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt from t_branch_lottery_sum where lottery =? and time=?";
		Map<String, Object> dealInfo = rdbJdbcTemplate.queryForMap(
				queryStatLotterySum, params);
		BigDecimal pay_money = (BigDecimal) dealInfo.get("pay_money");
		BigDecimal pay_cnt = (BigDecimal) dealInfo.get("pay_cnt");
		if (pay_money == null)
			pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
		if (pay_cnt == null)
			pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
		Center center = getCenterInfo().get(bet_station);
		return new CenterLotterySum(rdbTime, lotteryName, pay_money, pay_cnt,
				bet_station, center.getCenterName());
	}

	/**
	 * 获取到给定日期为止的某投注站的TP活动统计信息
	 * 
	 * @param bet_station
	 * @param givenDate
	 * @return
	 */
	public StatTpActivity getStatTpActivity(String bet_station, Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { bet_station };

		String queryGetByAmount = "";
		if (ISNOT_STATION.equals(bet_station)) {

			queryGetByAmount = "SELECT count(*) as getNum,Famount as getAmount from cqfcdb.t_lottery_activity_guaguale  where  (Fverify_betStation =? OR Fverify_betStation='' OR Fverify_betStation IS NULL)  GROUP BY Famount";
		} else {
			queryGetByAmount = "SELECT count(*) as getNum,Famount as getAmount from cqfcdb.t_lottery_activity_guaguale  where  Fverify_betStation =?  GROUP BY Famount";
		}

		List<Map<String, Object>> rows = cqfcJdbcTemplate.queryForList(
				queryGetByAmount, params);
		Stat stat = getStationInfo().get(bet_station);
		StatTpActivity statTpActivity = new StatTpActivity();
		statTpActivity.setBet_station(bet_station);
		statTpActivity.setAddress(stat.getAddress());
		statTpActivity.setName(stat.getStationName());
		statTpActivity.setBusiness_type(stat.getBusiness_type());
		statTpActivity.setStation_type(stat.getStation_type());
		statTpActivity.setTime(rdbTime);
		if (rows.size() > 0) {
			statTpActivity.setIf_Join(AMOUNT_1);
		}
		for (Map<String, Object> row : rows) {

			Integer getAmount = (Integer) row.get("getAmount");
			Long getNum = (Long) row.get("getNum");

			if (getAmount == null)
				getAmount = new Integer(0);
			if (getNum == null)
				getNum = new Long(0);

			if (getAmount.intValue() == AMOUNT_1000) {
				statTpActivity.setScr10RecNum(getNum);
			} else if (getAmount.intValue() == AMOUNT_500) {
				statTpActivity.setScr5RecNum(getNum);
			}
		}

		if (statTpActivity.getIf_Join() == AMOUNT_1) {

			String queryActByAmount = "";
			String queryHandByAmount = "";
			if (ISNOT_STATION.equals(bet_station)) {
				queryActByAmount = "select count(*) as actNum,Famount as getAmount from cqfcdb.t_lottery_activity_guaguale where  (Fverify_betStation =? OR Fverify_betStation='' OR Fverify_betStation IS NULL) AND Fverify = 1 GROUP BY Famount";
				queryHandByAmount = "SELECT count(*) as handNum  from cqfcdb.t_lottery_activity_guaguale  where  (Fverify_betStation =? OR Fverify_betStation='' OR Fverify_betStation IS NULL) and Fhandsel_charge = 1 and  Fhandsel_amount = 1000";
			} else {
				queryActByAmount = "select count(*) as actNum,Famount as getAmount from cqfcdb.t_lottery_activity_guaguale where  Fverify_betStation =? AND Fverify = 1 GROUP BY Famount";
				queryHandByAmount = "SELECT count(*) as handNum  from cqfcdb.t_lottery_activity_guaguale  where  Fverify_betStation =? and Fhandsel_charge = 1 and  Fhandsel_amount = 1000";
			}

			List<Map<String, Object>> actRows = cqfcJdbcTemplate.queryForList(
					queryActByAmount, params);

			for (Map<String, Object> row : actRows) {

				Long actNum = (Long) row.get("actNum");
				Integer getAmount = (Integer) row.get("getAmount");

				if (getAmount == null)
					getAmount = new Integer(0);
				if (actNum == null)
					actNum = new Long(0);
				if (getAmount.intValue() == AMOUNT_1000) {
					statTpActivity.setActScr10ExcNum(actNum);
				} else if (getAmount.intValue() == AMOUNT_500) {
					statTpActivity.setActScr5ExcNum(actNum);
				}
			}

			Integer handNum = cqfcJdbcTemplate.queryForObject(
					queryHandByAmount, params, Integer.class);
			if (handNum == null)
				handNum = new Integer(0);
			statTpActivity.setScr10HandRecNum(handNum);

			statTpActivity
					.setScrRecTotal(statTpActivity.getScr10RecNum()
							* AMOUNT_1000 + statTpActivity.getScr5RecNum()
							* AMOUNT_500);
			statTpActivity.setActScrExcTotal(statTpActivity.getActScr10ExcNum()
					* (AMOUNT_1000) + statTpActivity.getActScr5ExcNum()
					* AMOUNT_500);
			statTpActivity.setScr10HandRecMoney(statTpActivity
					.getScr10HandRecNum() * AMOUNT_1000);
		}
		return statTpActivity;
	}

	/**
	 * 获取到给定日期为止的所有投注站TP活动的统计信息
	 * 
	 * @param bet_station
	 * @param lotteryId
	 * @param givenDate
	 * @return
	 */
	public StatTpActivitySum getStatTpActivitySum(Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		Object[] params = new Object[] { rdbTime };
		String query = "select count(*) as jionNum,sum(scratch5_receive_number) as rec5,sum(scratch10_receive_number) as rec10,sum(actual_scratch5_exchange_number) as act5,sum(actual_scratch10_exchange_number) as act10,sum(scratch10_handsel_receive_number) as scr10 from t_activity_record where time =?  and if_join_activity= 1";
		Map<String, Object> row = rdbJdbcTemplate.queryForMap(query, params);
		Long jionNum = (Long) row.get("jionNum");
		BigDecimal rec5 = (BigDecimal) row.get("rec5");
		BigDecimal rec10 = (BigDecimal) row.get("rec10");
		BigDecimal act5 = (BigDecimal) row.get("act5");
		BigDecimal act10 = (BigDecimal) row.get("act10");
		BigDecimal scr10 = (BigDecimal) row.get("scr10");

		if (rec5 == null)
			rec5 = new BigDecimal(0);
		if (rec10 == null)
			rec10 = new BigDecimal(0);
		if (jionNum == null)
			jionNum = new Long(0);
		if (act5 == null)
			act5 = new BigDecimal(0); // 如果没有记录，则设置为0
		if (act10 == null)
			act10 = new BigDecimal(0); // 如果没有记录，则设置为0
		if (scr10 == null)
			scr10 = new BigDecimal(0); // 如果没有记录，则设置为0

		return new StatTpActivitySum(rdbTime, jionNum, rec5, rec10, rec5
				.multiply(new BigDecimal(AMOUNT_500)).add(
						rec10.multiply(new BigDecimal(AMOUNT_1000))), act5,
				act10, act5.multiply(new BigDecimal(AMOUNT_500)).add(
						act10.multiply(new BigDecimal(AMOUNT_1000))), scr10,
				scr10.multiply(new BigDecimal(AMOUNT_1000)));
	}

	/**
	 * 各个投注站扫描二维码次数
	 * 
	 * @param bet_station
	 * @param givenDate
	 * @return
	 */
	public ActivityScancodeSource getActivityScancodeSource(String bet_station,
			Date givenDate) {
		long rdbTime = CommonUtil.getRdbTime(givenDate);
		String queryPay = "";
		if (ISNOT_STATION.equals(bet_station)) {

			queryPay = "select count(Fid) as totalCount from t_lottery_station_bind_record_all  where (Fstation_code=? or Fstation_code='' or Fstation_code is null)";
		} else {

			queryPay = "select count(Fid) as totalCount from t_lottery_station_bind_record_all  where Fstation_code=?";
		}

		Long totalcount = cqfcfinanceJdbcTemplate.queryForObject(queryPay,
				new Object[] { bet_station }, Long.class);

		ActivityScancodeSource activityScancodeSource = new ActivityScancodeSource();
		if (totalcount == null) {
			totalcount = 0L;
		} else if (totalcount > 0) {
			activityScancodeSource.setIfJoin(AMOUNT_1);
		}

		Stat stat = getStationInfo().get(bet_station);
		activityScancodeSource.setBetStation(stat.getBet_station());
		activityScancodeSource.setBetStationAddress(stat.getAddress());
		activityScancodeSource.setBetStationName(stat.getStationName());
		activityScancodeSource.setScancodeEntranceNumber(totalcount);
		activityScancodeSource.setStation_type(stat.getStation_type());
		activityScancodeSource.setBusiness_type(stat.getBusiness_type());
		activityScancodeSource.setTime(rdbTime);
		return activityScancodeSource;
	}
	
	
	
	public List<StatTpActivityInfo> getTpActivityInfo(String bet_station){
		
		List<StatTpActivityInfo> activityInfos = new ArrayList<StatTpActivityInfo>();
		
		String query ="";
		
		if(ISNOT_STATION.equals(bet_station)){
			
			query = "select  Fuser_id,Fnick,Fvote_time,Fhandsel_charge,Fverify from t_lottery_activity_guaguale where (Fverify_betStation =? or Fverify_betStation='' or Fverify_betStation is null)";
		}else{
			query = "select  Fuser_id,Fnick,Fvote_time,Fhandsel_charge,Fverify from t_lottery_activity_guaguale where Fverify_betStation = ?";
		}
		List<Map<String, Object>> rows = cqfcJdbcTemplate.queryForList(query,new Object[]{bet_station});
		StatTpActivityInfo statTpActivityInfo = null;
		for (Map<String, Object> row :rows) {
			
				statTpActivityInfo =  new StatTpActivityInfo();
				statTpActivityInfo.setNick((String)row.get("Fnick"));
				statTpActivityInfo.setBetStation(bet_station);
				statTpActivityInfo.setUserId((String)row.get("Fuser_id"));
				statTpActivityInfo.setHandselCharge((Integer)row.get("Fhandsel_charge")==null?0:(Integer)row.get("Fhandsel_charge"));
				statTpActivityInfo.setVerify((Integer)row.get("Fverify")==null?0:(Integer)row.get("Fverify"));
				statTpActivityInfo.setVoteTime(((Timestamp)row.get("Fvote_time")).toString());
		
				String openId =cqfcfinanceJdbcTemplate.queryForObject("select  Fopen_id from t_lottery_wx_user_token_recent where Fuser_id='"+statTpActivityInfo.getUserId()+"' limit 1", String.class);
				if(openId==null)openId="";
				statTpActivityInfo.setOpenId(openId);
				activityInfos.add(statTpActivityInfo);
		}
		
		return activityInfos;
		
	}
	
	
	

}