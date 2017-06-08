package com.cqfc.statistics.serviceImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cqfc.statistics.History;
import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.model.ActivityScancodeSource;
import com.cqfc.statistics.model.ActivityTpPhoneInfo;
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
public class RdbService {

	private static final Logger logger = LoggerFactory
			.getLogger(RdbService.class);

	@Resource(name = "rdbJdbcTemplate")
	private JdbcTemplate rdbJdbcTemplate;

	public int insertStat(Stat stat) {
		String sql = "replace into t_stat (time, pay_money, pay_cnt, new_user, bet_station, parent_code, station_name,address,business_type) VALUES (?, ?, ?, ?, ?, ?,?,?,?)";
		Object[] params = new Object[] { stat.getTime(), stat.getPay_money(),
				stat.getPay_cnt(), stat.getNew_user(), stat.getBet_station(),
				stat.getParentCode(), stat.getStationName(), stat.getAddress(),
				stat.getBusiness_type() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isStatCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_stat", where);
	}

	public int insertCountry(Country country) {

		String sql = "replace into t_country (time, pay_money, pay_cnt, new_user, bet_station,station_name,parent_code) VALUES (?, ?, ?, ?, ?, ?,?)";

		Object[] params = new Object[] { country.getTime(),
				country.getPay_money(), country.getPay_cnt(),
				country.getNew_user(), country.getCountry_code(),
				country.getCountry_name(), country.getParentCode() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isCountryCalculated(String country, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				country, time);
		return judge("t_country", where);
	}

	public boolean isCountryLotteryCalculated(String bet_station,
			String lotterName, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lotterName, time);
		return judge("t_country_lottery", where);
	}

	public int insertCountryLottery(CountryLottery countryLottery) {
		String sql = "replace into t_country_lottery (time, lottery, pay_money, pay_cnt, bet_station,station_name,parent_code) VALUES (?, ?, ?, ?, ?,?,?)";
		Object[] params = new Object[] { countryLottery.getTime(),
				countryLottery.getLottery(), countryLottery.getPay_money(),
				countryLottery.getPay_cnt(), countryLottery.getCountry_code(),
				countryLottery.getCountry_name(),
				countryLottery.getParentCode() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public int insertStatLottery(StatLottery statLottery) {
		String sql = "replace into t_stat_lottery (time, lottery, pay_money, pay_cnt, bet_station, parent_code,station_name,address,business_type) VALUES (?, ?, ?, ?, ?,?,?,?,?)";
		Object[] params = new Object[] { statLottery.getTime(),
				statLottery.getLottery(), statLottery.getPay_money(),
				statLottery.getPay_cnt(), statLottery.getBet_station(),
				statLottery.getParentCode(), statLottery.getStationName(),
				statLottery.getAddress(), statLottery.getBusiness_type() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isStatLotteryCalculated(String bet_station,
			String lotterName, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lotterName, time);
		return judge("t_stat_lottery", where);
	}

	public int insertUser(User user) {
		String sql = "replace into t_user (content, bet_station, parent_code,station_name,address) VALUES (?, ?,?,?,?)";
		Object[] params = new Object[] { user.getContent(),
				user.getBet_station(), user.getParentCode(),
				user.getStationName(), user.getAddress() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public int insertStatUser(StatUser statUser) {
		String sql = "replace into t_stat_user (bet_station, nick, bind_time,business_type,user_id) VALUES (?, ?,?,?,?)";
		Object[] params = new Object[] { statUser.getBet_station(),
				statUser.getNick(), statUser.getBind_time(),
				statUser.getBusiness_type(), statUser.getUser_id() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public int insertStatUser(List<StatUser> statUsers) {
		String sql = "replace into t_stat_user (bet_station, nick, bind_time,business_type,user_id) VALUES (?, ?,?,?,?)";

		final List<StatUser> rows = statUsers;

		return rdbJdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {

						ps.setString(1, rows.get(i).getBet_station());
						ps.setString(2, rows.get(i).getNick());
						ps.setString(3, rows.get(i).getBind_time());
						ps.setInt(4, rows.get(i).getBusiness_type());
						ps.setString(5, rows.get(i).getUser_id());
					}

					@Override
					public int getBatchSize() {
						return rows.size();
					}
				}).length;
	}

	public int insertDeal(Deal deal) {
		String sql = "replace into t_deal (time, content, bet_station, parent_code,station_name,address) VALUES (?, ?, ?,?,?,?)";
		Object[] params = new Object[] { deal.getTime(), deal.getContent(),
				deal.getBet_station(), deal.getParentCode(),
				deal.getStationName(), deal.getAddress() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isDealCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_deal", where);
	}

	public void insertStatDeal(List<StatDeal> statDeal) {
		final List<StatDeal> statDealList = statDeal;
		String sql = "replace into t_stat_deal (time, buy_time, nick, lottery,total_amount,user_id,deal_id,business_type,bet_station) VALUES (?, ?, ?,?,?,?, ?,?,?)";
		rdbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {

				ps.setLong(1, statDealList.get(i).getTime());
				ps.setString(2, statDealList.get(i).getBuyTime());
				ps.setString(3, statDealList.get(i).getNick());
				ps.setString(4, statDealList.get(i).getLottery());
				ps.setLong(5, statDealList.get(i).getTotalAmount());
				ps.setString(6, statDealList.get(i).getUserId());
				ps.setLong(7, statDealList.get(i).getDealId());
				ps.setInt(8, statDealList.get(i).getBusiness_type());
				ps.setString(9, statDealList.get(i).getBet_station());
			}

			@Override
			public int getBatchSize() {
				return statDealList.size();
			}
		});

	}

	public boolean isStatDealCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_stat_deal", where);
	}

	public int insertStatSum(StatSum statSum) {
		String sql = "replace into t_stat_sum (time, pay_money, pay_cnt, new_user, bet_station, parent_code,station_name,address) VALUES (?, ?, ?, ?, ?,?,?,?)";
		Object[] params = new Object[] { statSum.getTime(),
				statSum.getPay_money(), statSum.getPay_cnt(),
				statSum.getNew_user(), statSum.getBet_station(),
				statSum.getParentCode(), statSum.getStationName(),
				statSum.getAddress() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isStatSumCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_stat_sum ", where);
	}

	public int insertStatLotterySum(StatLotterySum statLotterySum) {
		String sql = "replace into t_stat_lottery_sum (time, lottery, pay_money, pay_cnt, bet_station, parent_code,station_name,address) VALUES (?, ?, ?, ?, ?,?,?,?)";
		Object[] params = new Object[] { statLotterySum.getTime(),
				statLotterySum.getLottery(), statLotterySum.getPay_money(),
				statLotterySum.getPay_cnt(), statLotterySum.getBet_station(),
				statLotterySum.getParentCode(),
				statLotterySum.getStationName(), statLotterySum.getAddress() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isStatLotterySumCalculated(String bet_station,
			String lottery, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lottery, time);
		return judge("t_stat_lottery_sum ", where);
	}

	public boolean isBranchCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_branch", where);
	}

	public int insertBranch(Branch branch) {
		String sql = "replace into t_branch (time, pay_money, pay_cnt, new_user, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { branch.getTime(),
				branch.getPay_money(), branch.getPay_cnt(),
				branch.getNew_user(), branch.getBranchCode(),
				branch.getBranchName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isBranchLotteryCalculated(String bet_station,
			String lotterName, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lotterName, time);
		return judge("t_branch_lottery", where);
	}

	public int insertBranchLottery(BranchLottery branchLottery) {
		String sql = "replace into t_branch_lottery (time, lottery, pay_money, pay_cnt, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { branchLottery.getTime(),
				branchLottery.getLottery(), branchLottery.getPay_money(),
				branchLottery.getPay_cnt(), branchLottery.getBet_station(),
				branchLottery.getBranchName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isBranchSumCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_branch_sum ", where);
	}

	public int insertBranchSum(BranchSum branchSum) {
		String sql = "replace into t_branch_sum (time, pay_money, pay_cnt, new_user, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { branchSum.getTime(),
				branchSum.getPay_money(), branchSum.getPay_cnt(),
				branchSum.getNew_user(), branchSum.getBet_station(),
				branchSum.getBranchName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isBranchLotterySumCalculated(String bet_station,
			String lottery, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lottery, time);
		return judge("t_branch_lottery_sum ", where);
	}

	public int insertBranchLotterySum(BranchLotterySum branchLotterySum) {
		String sql = "replace into t_branch_lottery_sum (time, lottery, pay_money, pay_cnt, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { branchLotterySum.getTime(),
				branchLotterySum.getLottery(), branchLotterySum.getPay_money(),
				branchLotterySum.getPay_cnt(),
				branchLotterySum.getBet_station(),
				branchLotterySum.getBranchName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isCenterCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_center", where);
	}

	public int insertCenter(Center center) {
		String sql = "replace into t_center (time, pay_money, pay_cnt, new_user, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { center.getTime(),
				center.getPay_money(), center.getPay_cnt(),
				center.getNew_user(), center.getCenterCode(),
				center.getCenterName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isCenterLotteryCalculated(String bet_station,
			String lotterName, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lotterName, time);
		return judge("t_center_lottery", where);
	}

	public int insertCenterLottery(CenterLottery centerLottery) {
		String sql = "replace into t_center_lottery (time, lottery, pay_money, pay_cnt, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { centerLottery.getTime(),
				centerLottery.getLottery(), centerLottery.getPay_money(),
				centerLottery.getPay_cnt(), centerLottery.getCenterCode(),
				centerLottery.getCenterName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isCenterSumCalculated(String bet_station, long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_center_sum ", where);
	}

	public boolean isActivityScancodeSourceCalculated(String bet_station,
			long time) {
		String where = String.format("where bet_station='%s' and time=%s",
				bet_station, time);
		return judge("t_activity_scancode_source", where);
	}

	public int insertActivityScancodeSource(ActivityScancodeSource acSource) {
		String sql = "replace into t_activity_scancode_source (time, scancode_entrance_number, station_name, address, bet_station,if_join_activity,station_type,business_type) VALUES (?, ?, ?, ?, ?,?,?,?)";
		Object[] params = new Object[] { acSource.getTime(),
				acSource.getScancodeEntranceNumber(),
				acSource.getBetStationName(), acSource.getBetStationAddress(),
				acSource.getBetStation(), acSource.getIfJoin(),
				acSource.getStation_type(), acSource.getBusiness_type() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public int insertCenterSum(CenterSum centerSum) {
		String sql = "replace into t_center_sum (time, pay_money, pay_cnt, new_user, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { centerSum.getTime(),
				centerSum.getPay_money(), centerSum.getPay_cnt(),
				centerSum.getNew_user(), centerSum.getBet_station(),
				centerSum.getCenterName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public boolean isCenterLotterySumCalculated(String bet_station,
			String lottery, long time) {
		String where = String.format(
				"where bet_station='%s' and lottery='%s' and time=%s",
				bet_station, lottery, time);
		return judge("t_center_lottery_sum ", where);
	}

	public int insertCenterLotterySum(CenterLotterySum centerLotterySum) {
		String sql = "replace into t_center_lottery_sum (time, lottery, pay_money, pay_cnt, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
		Object[] params = new Object[] { centerLotterySum.getTime(),
				centerLotterySum.getLottery(), centerLotterySum.getPay_money(),
				centerLotterySum.getPay_cnt(),
				centerLotterySum.getBet_station(),
				centerLotterySum.getCenterName() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public int insertStatTpActivity(StatTpActivity statTpActivity) {
		String sql = "replace into t_activity_record (time, scratch5_receive_number, scratch10_receive_number, scratch_receive_totalMoney, actual_scratch5_exchange_number,actual_scratch10_exchange_number,actual_scratch_exchange_totalMoney,scratch10_handsel_receive_number,scratch10_handsel_receive_money,address,station_name,bet_station,if_join_activity,station_type,business_type) VALUES (?, ?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?,?,?)";
		Object[] params = new Object[] { statTpActivity.getTime(),
				statTpActivity.getScr5RecNum(),
				statTpActivity.getScr10RecNum(),
				statTpActivity.getScrRecTotal(),
				statTpActivity.getActScr5ExcNum(),
				statTpActivity.getActScr10ExcNum(),
				statTpActivity.getActScrExcTotal(),
				statTpActivity.getScr10HandRecNum(),
				statTpActivity.getScr10HandRecMoney(),
				statTpActivity.getAddress(), statTpActivity.getName(),
				statTpActivity.getBet_station(), statTpActivity.getIf_Join(),
				statTpActivity.getStation_type(),
				statTpActivity.getBusiness_type() };
		return rdbJdbcTemplate.update(sql, params);
	}

	public int insertStatTpActivitySum(StatTpActivitySum statTpActivitySum) {
		String sql = "replace into t_activity_summary (time, join_exchange_bet_station_number,all_scratch5_receive_count,all_scratch10_receive_count,all_bet_station_receive_total_money, all_bet_station_scratch5_exchange_count, all_bet_station_scratch10_exchange_count, all_bet_station_exchange_total_money,handsel10_receive_total_count, handsel10_receive_total_money)  VALUES (?, ?, ?, ?, ?,?,?,?,?,?)";
		Object[] params = new Object[] { statTpActivitySum.getTime(),
				statTpActivitySum.getStaJoinNum(),
				statTpActivitySum.getStatRec5ExcNum(),
				statTpActivitySum.getStatRec10ExcNum(),
				statTpActivitySum.getStatRecTotalMoney(),
				statTpActivitySum.getStatSca5ExcNum(),
				statTpActivitySum.getStatSca10ExcNum(),
				statTpActivitySum.getStatExcTotalMoney(),
				statTpActivitySum.getHand10recNum(),
				statTpActivitySum.getHand10recTotalMoney() };
		return rdbJdbcTemplate.update(sql, params);
	}

	// public int insertActivityInfo(StatTpActivityInfo sti) {
	// String sql =
	// "replace into t_stat_tp_info (nick, user_id,vote_time,bet_station,open_id)  VALUES (?, ?, ?, ?, ?)";
	// Object[] params = new Object[]
	// {sti.getNick(),sti.getUserId(),sti.getVoteTime(),sti.getBetStation(),sti.getOpenId()};
	// return rdbJdbcTemplate.update(sql, params);
	// }

	public int insertActivityInfo(List<StatTpActivityInfo> stis) {
		String sql = "replace into t_stat_tp_info (nick, user_id,vote_time,bet_station,open_id,verify,handsel_charge)  VALUES (?, ?, ?, ?, ?,?,?)";

		final List<StatTpActivityInfo> rows = stis;

		return rdbJdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {

						ps.setString(1, rows.get(i).getNick());
						ps.setString(2, rows.get(i).getUserId());
						ps.setString(3, rows.get(i).getVoteTime());
						ps.setString(4, rows.get(i).getBetStation());
						ps.setString(5, rows.get(i).getOpenId());
						ps.setInt(6, rows.get(i).getVerify());
						ps.setInt(7, rows.get(i).getHandselCharge());
					}

					@Override
					public int getBatchSize() {
						return rows.size();
					}
				}).length;
	}

	public int insertActivityTpPhoneInfo(ActivityTpPhoneInfo stpi) {
		try {
			String sql = "insert into t_activity_phone (logTime, activityNo,phoneInfo1,phoneInfo2,phoneInfo3,phoneInfo4,phoneInfo5,phoneInfo6,phoneInfo7,phoneInfo8,phoneInfo9,phoneInfo10,netType)  VALUES (?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?)";
			Object[] params = new Object[] { stpi.getLogTime(),
					stpi.getActivityNo(), stpi.getPhoneInfo1(),
					stpi.getPhoneInfo2(), stpi.getPhoneInfo3(),
					stpi.getPhoneInfo4(), stpi.getPhoneInfo5(),
					stpi.getPhoneInfo6(), stpi.getPhoneInfo7(),
					stpi.getPhoneInfo8(), stpi.getPhoneInfo9(),
					stpi.getPhoneInfo10(), stpi.getNetType(), };
			return rdbJdbcTemplate.update(sql, params);

		} catch (DuplicateKeyException e) {
			return 1;
		}
	}

	public int updateStatParentCode(String bet_station, String parent_code,
			int business_type, long time) {

		String sql = "update t_stat set parent_code = ?, business_type=? where bet_station=? and time=?";
		return rdbJdbcTemplate.update(sql, new Object[] { parent_code,
				business_type, bet_station, time });

	}

	public int updateStatLotteryParentCode(String bet_station,
			String parent_code, int business_type, long time) {

		String sql = "update t_stat_lottery set parent_code = ?, business_type=? where bet_station=? and time=?";
		return rdbJdbcTemplate.update(sql, new Object[] { parent_code,
				business_type, bet_station, time });

	}

	public int updateStatParentCode(String bet_station, String parent_code,
			int business_type) {

		String sql = "update t_stat set parent_code = ?, business_type=? where bet_station=?";
		return rdbJdbcTemplate.update(sql, new Object[] { parent_code,
				business_type, bet_station });

	}

	public int updateStatLotteryParentCode(String bet_station,
			String parent_code, int business_type) {

		String sql = "update t_stat_lottery set parent_code = ?, business_type=? where bet_station=?";
		return rdbJdbcTemplate.update(sql, new Object[] { parent_code,
				business_type, bet_station });

	}

	public int activityTpPhoneVersionSum() {

		String sql = "select count(*) from t_activity_phone";

		return rdbJdbcTemplate.queryForObject(sql, Integer.class);
	}

	public Map<String, Long> activityTpPhoneVersionGroup() {

		Map<String, Long> result = new HashMap<String, Long>();
		String sql = "select phoneInfo1 as version,count(*) as num from t_activity_phone group by phoneInfo1 order by phoneInfo1";

		List<Map<String, Object>> list = rdbJdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : list) {

			result.put((String) row.get("version"),
					(Long) row.get("num") == null ? 0L : (Long) row.get("num"));
		}
		return result;

	}

	public int activityTpPhoneAnalyzeInsert(String name, Long num,
			String ratio, int type) {

		String sql = "insert into t_activity_phone_analyze (name,num,ratio,type) values (?,?,?,?)";

		return rdbJdbcTemplate.update(sql, new Object[] { name, num, ratio,
				type });

	}

	public Map<String, Long> activityTpPhoneSysGroup() {

		Map<String, Long> result = new HashMap<String, Long>();
		String sql = "select phoneInfo2 as systemName,count(*) as num from t_activity_phone group by phoneInfo2 order by phoneInfo2";

		List<Map<String, Object>> list = rdbJdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : list) {

			result.put((String) row.get("systemName"),
					(Long) row.get("num") == null ? 0L : (Long) row.get("num"));
		}
		return result;

	}

	public Map<String, Long> activityTpPhoneNetGroup() {

		Map<String, Long> result = new HashMap<String, Long>();
		String sql = "select netType as net,count(*) as num from t_activity_phone group by netType order by netType";

		List<Map<String, Object>> list = rdbJdbcTemplate.queryForList(sql);

		for (Map<String, Object> row : list) {

			result.put((String) row.get("net"),
					(Long) row.get("num") == null ? 0L : (Long) row.get("num"));
		}
		return result;

	}

	// 判断是否已统计
	public boolean judge(String tableName, String whereClause) {
		boolean ret = false;
		// 跳过判断,进行覆盖统计记录
		if (History.isOverwrite()) {
			return false;
		}
		String sql = "select time, last_update_time from " + tableName + " "
				+ whereClause;
		List<Map<String, Object>> queryList = rdbJdbcTemplate.queryForList(sql);
		if (queryList.size() == 1) {
			Map<String, Object> map = queryList.get(0);
			String statsDate = String.valueOf(map.get("time"));
			String lastUpdateTime = CommonUtil
					.formatStatisticsTime((Timestamp) map
							.get("last_update_time"));
			if (lastUpdateTime.compareTo(statsDate) > 0) {
				ret = true;
			}
		} /*
		 * else { logger.info("judge=false || queryList.size()!=1 sql=" + sql);
		 * //possible means somthing wrong in sql statement }
		 */
		return ret;
	}

	public static void main(String[] args) {
		String statsDate = "20141201";
		String lastUpdateTime = "20141207";
		int r = lastUpdateTime.compareTo(statsDate);
		System.out.println(r);
	}

}
