package com.cqfc.statistics.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.dao.baseDao.IStationDao;
import com.cqfc.statistics.dao.baseDao.TemplateBase;
import com.cqfc.statistics.model.Branch;
import com.cqfc.statistics.model.Center;
import com.cqfc.statistics.model.Country;
import com.cqfc.statistics.model.Stat;

@Repository
public class StationDao extends TemplateBase implements IStationDao {

	private static final Logger logger = LoggerFactory
			.getLogger(StationDao.class);

	@Override
	public void stationStatics(Date time, Stat station) {

		try {
			String start = CommonUtil.getDateStart(time);
			String end = CommonUtil.getDateEnd(time);
			long rdbTime = CommonUtil.getRdbTime(time);
			String queryPay = "";
			String queryNewUser = "";
			Map<String, Object> pay = new HashMap<String, Object>();
			Long new_user = null ;
			if (VIRTUAL_STATION.equals(station.getBet_station())) {

				queryPay = "SELECT sum(total_money) as pay_money,sum(action_times) as pay_cnt,lottery FROM rdb.t_user_action  WHERE action_type = ? AND action_time >=? AND action_time<? AND (bet_station =? or bet_station is NULL or bet_station ='')";
				queryNewUser = "select count(user_id) as user_amount from rdb.t_user_info where action_type =? and action_time >=? and action_time<?  AND (bet_station =? or bet_station is NULL or bet_station ='') ";

				pay = rdbTemplate.queryForMap(queryPay,new Object[]{CONST_ONE,start, end,station.getBet_station()});
				new_user = rdbTemplate.queryForObject(queryNewUser,
						new Object[]{CONST_SIX,start, end,station.getBet_station()}, Long.class);
			} else {
				queryPay = "SELECT sum(total_money) as pay_money ,sum(action_times) as pay_cnt FROM rdb.t_user_action  WHERE action_type = ? AND action_time >=? AND action_time<? AND bet_station =?";
				queryNewUser = "select count(1) as user_amount from (select * from (select user_id,bet_station  from rdb.t_user_action where action_type =? and action_time >=? and action_time<?  and int_3 in(1,2) order by action_time desc ) t group by t.user_id) t2 where bet_station = ?";
				pay = rdbTemplate.queryForMap(queryPay,new Object[]{CONST_ONE,start, end,station.getBet_station()});
				new_user = rdbTemplate.queryForObject(queryNewUser,
						new Object[]{CONST_NINE,start, end,station.getBet_station()}, Long.class);
			}

			String sql = "replace into t_stat (time, pay_money, pay_cnt, new_user, bet_station, parent_code, station_name,address,business_type) VALUES (?, ?, ?, ?, ?, ?,?,?,?)";
		
			BigDecimal pay_money = (BigDecimal) pay.get("pay_money");
			BigDecimal pay_cnt = (BigDecimal) pay.get("pay_cnt");
			if (pay_money == null)
				pay_money = new BigDecimal(0); // 如果没有投注记录，则设置为0
			if (pay_cnt == null)
				pay_cnt = new BigDecimal(0); // 如果没有投注记录，则设置为0
			if (new_user == null)
				new_user = 0L;

			rdbTemplate.update(
					sql,
					new Object[] { rdbTime, pay_money, pay_cnt,
							new_user,
							station.getBet_station(), station.getParentCode(),
							station.getStationName(), station.getAddress(),
							station.getBusiness_type()});
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	
	@Override
	public void stationLotteryStatics(Date time, Stat station,
			Map<String, String> lotteryMap) {

		try {
			String start = CommonUtil.getDateStart(time);
			String end = CommonUtil.getDateEnd(time);
			long rdbTime = CommonUtil.getRdbTime(time);
			String queryPay = "";

			if (VIRTUAL_STATION.equals(station.getBet_station())) {

				queryPay = "SELECT sum(total_money) as pay_money,sum(action_times) as pay_cnt,lottery FROM rdb.t_user_action  WHERE action_type = ? AND action_time >=? AND action_time<? AND (bet_station =? or bet_station is NULL or bet_station ='') and lottery=?";

			} else {
				queryPay = "SELECT sum(total_money) as pay_money ,sum(action_times) as pay_cnt FROM rdb.t_user_action  WHERE action_type = ? AND action_time >=? AND action_time<? AND bet_station =? and lottery=?";
			}

			String sqlLottery = "replace into t_stat_lottery (time, lottery, pay_money, pay_cnt, bet_station, parent_code,station_name,address,business_type) VALUES (?, ?, ?, ?, ?,?,?,?,?)";

			List<Object[]> objects = new ArrayList<Object[]>();
			List<Object> list = new ArrayList<Object>();
			BigDecimal moneyTotal = new BigDecimal(0);
			BigDecimal cntTotal = new BigDecimal(0);

			for (String lottery : lotteryMap.values()) {

				Map<String, Object> pay = rdbTemplate.queryForMap(
						queryPay,
						new Object[] { CONST_ONE, start, end,
								station.getBet_station(), lottery });
				list.add(rdbTime);
				list.add(lottery);
				list.add((BigDecimal) (pay.get("pay_money") == null ? new BigDecimal(
						0) : pay.get("pay_money")));
				list.add((BigDecimal) (pay.get("pay_cnt") == null ? new BigDecimal(
						0) : pay.get("pay_cnt")));
				list.add(station.getBet_station());
				list.add(station.getParentCode());
				list.add(station.getStationName());
				list.add(station.getAddress());
				list.add(station.getBusiness_type());
				moneyTotal = moneyTotal
						.add((BigDecimal) (pay.get("pay_money") == null ? new BigDecimal(
								0) : pay.get("pay_money")));
				cntTotal = cntTotal
						.add((BigDecimal) (pay.get("pay_cnt") == null ? new BigDecimal(
								0) : pay.get("pay_cnt")));
				objects.add(list.toArray());
				list.clear();
			}

			rdbTemplate.batchUpdate(sqlLottery, objects);
			objects.clear();

		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	@Override
	public void countryStatics(Date time, Country station) {
		try {

			long rdbTime = CommonUtil.getRdbTime(time);
			Object[] params = new Object[] { station.getCountry_code(), rdbTime };
			String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_stat where parent_code=? and time=?";
			Map<String, Object> pay = rdbTemplate.queryForMap(queryPay, params);
			String queryNewUser = "select sum(new_user) from t_stat where parent_code=? and  time=?";
			Long new_user = rdbTemplate.queryForObject(queryNewUser, params,
					Long.class);

			String insertSql = "replace into t_country (time, pay_money, pay_cnt, new_user, bet_station,station_name,parent_code) VALUES (?, ?, ?, ?, ?, ?,?)";
			rdbTemplate.update(
					insertSql,
					new Object[] { rdbTime, pay.get("pay_money")==null?0:pay.get("pay_money"),
							pay.get("pay_cnt")==null?0:pay.get("pay_cnt"), new_user==null?0:new_user,
							station.getCountry_code(), station.getCountry_name(),
							station.getParentCode() });
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	@Override
	public void branchStatics(Date time, Branch station) {
		try {
			long rdbTime = CommonUtil.getRdbTime(time);
			Object[] params = new Object[] { station.getBranchCode(), rdbTime };
			String queryPay = "";
			String queryNewUser = "";
			if (station.getBusiness_type() == 0) {

				queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_country where parent_code=? and time=?";
				queryNewUser = "select sum(new_user) from t_country where parent_code=? and  time=?";
			} else {
				queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_stat where parent_code=? and time=?";
				queryNewUser = "select sum(new_user) from t_stat where parent_code=? and  time=?";
			}

			Map<String, Object> pay = rdbTemplate.queryForMap(queryPay, params);
			Long new_user = rdbTemplate.queryForObject(queryNewUser, params,
					Long.class);
			String insertSql = "replace into t_branch (time, pay_money, pay_cnt, new_user, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
			rdbTemplate.update(
					insertSql,
					new Object[] { rdbTime, pay.get("pay_money")==null?0:pay.get("pay_money"),
							pay.get("pay_cnt")==null?0:pay.get("pay_cnt"), new_user==null?0:new_user,
							station.getBranchCode(), station.getBranchName()

					});
		} catch (Exception e) {
			logger.error(e.toString());
		}

	}

	@Override
	public void centerStatics(Date time, Center station) {
		try {

			long rdbTime = CommonUtil.getRdbTime(time);
			Object[] params = new Object[] { rdbTime };
			String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt  from t_branch where  time=?";
			Map<String, Object> pay = rdbTemplate.queryForMap(queryPay, params);
			String queryNewUser = "select sum(new_user) from t_branch where time=?";
			Long new_user = rdbTemplate.queryForObject(queryNewUser, params,
					Long.class);
			String insertSql = "replace into t_center (time, pay_money, pay_cnt, new_user, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
			rdbTemplate
					.update(insertSql,
							new Object[] { rdbTime, pay.get("pay_money")==null?0: pay.get("pay_money"),
									pay.get("pay_cnt")==null?0: pay.get("pay_cnt"), new_user==null?0:new_user,
									station.getCenterCode(),
									station.getCenterName() });
		} catch (Exception e) {
			logger.error(e.toString());
		}

	}

	@Override
	public void countryLotteryStatics(Date time, Country station) {
		try {
			long rdbTime = CommonUtil.getRdbTime(time);
			Object[] params = new Object[] { station.getCountry_code(), rdbTime };
			String queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt,lottery  from t_stat_lottery where parent_code=? and time=? group by lottery";
			List<Map<String, Object>> pays = rdbTemplate.queryForList(queryPay,
					params);

			String insertSql = "replace into t_country_lottery (time, lottery, pay_money, pay_cnt, bet_station,station_name,parent_code) VALUES (?, ?, ?, ?, ?,?,?)";
			for (int i = 0; i < pays.size(); i++) {
				rdbTemplate.update(
						insertSql,
						new Object[] { rdbTime, pays.get(i).get("lottery"),
								pays.get(i).get("pay_money"),
								pays.get(i).get("pay_cnt"),
								station.getCountry_code(),
								station.getCountry_name(),
								station.getParentCode() });
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}

	}

	@Override
	public void branchLotteryStatics(Date time, Branch station) {
		try {

			long rdbTime = CommonUtil.getRdbTime(time);
			Object[] params = new Object[] { station.getBranchCode(), rdbTime };

			String queryPay = "";
			if (station.getBusiness_type() == 0) {
				queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt,lottery  from t_country_lottery where parent_code=? and time=? group by lottery";
			} else {
				queryPay = "select sum(pay_money) as pay_money, sum(pay_cnt) as pay_cnt,lottery  from t_stat_lottery where parent_code=? and time=? group by lottery";
			}

			List<Map<String, Object>> pays = rdbTemplate.queryForList(queryPay,
					params);

			String insertSql = "replace into t_branch_lottery (time, lottery, pay_money, pay_cnt, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
			for (int i = 0; i < pays.size(); i++) {
				rdbTemplate.update(
						insertSql,
						new Object[] { rdbTime, pays.get(i).get("lottery"),
								pays.get(i).get("pay_money"),
								pays.get(i).get("pay_cnt"),
								station.getBranchCode(),
								station.getBranchName() });
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}

	}

	@Override
	public void centerLotteryStatics(Date time, Center station) {
		try {

			long rdbTime = CommonUtil.getRdbTime(time);
			Object[] params = new Object[] { rdbTime };

			String queryPay = "select sum(pay_money) as pay_money,sum(pay_cnt) as pay_cnt,lottery  from t_branch_lottery where time=? GROUP BY lottery";
			List<Map<String, Object>> pays = rdbTemplate.queryForList(queryPay,
					params);

			String insertSql = "replace into t_center_lottery (time, lottery, pay_money, pay_cnt, bet_station,station_name) VALUES (?, ?, ?, ?, ?,?)";
			for (int i = 0; i < pays.size(); i++) {
				rdbTemplate.update(
						insertSql,
						new Object[] { rdbTime, pays.get(i).get("lottery"),
								pays.get(i).get("pay_money"),
								pays.get(i).get("pay_cnt"),
								station.getCenterCode(),
								station.getCenterName()});
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}


	@Override
	public void stationUserStatics(Date time, Stat station) {
		
		try {
			String start = CommonUtil.getDateStart(time);
			String end = CommonUtil.getDateEnd(time);
			Object object =null;
			String querySql = "select unique_id as user_id,nick,action_time from t_user_info where  action_type = ? and bet_station=? and last_update_time>=? and last_update_time <? order by action_time desc";
			
			List<Map<String, Object>> users = rdbTemplate.queryForList(querySql, new Object[]{CONST_SIX,station.getBet_station(),start,end});
			
			String insertSql = "replace into t_stat_user(user_id,bet_station,nick,bind_time,business_type,parent_code,station_name,address) values (?,?,?,?,?,?,?,?)";
			for (int i = 0; i < users.size(); i++) {
				
				object =  rdbTemplate.queryForMap("select max(action_time) as 'bindTime' from t_user_action where action_type=? and user_id=? and bet_station=? and action_time>=? and action_time<?", new Object[]{CONST_NINE,users.get(i).get("user_id"),station.getBet_station(),start,end}).get("bindTime");
				
				if(object!=null){
					users.get(i).put("action_time", ((Timestamp)object)) ;
				}
				
				rdbTemplate.update(insertSql, new Object[]{
						
						users.get(i).get("user_id"),
						station.getBet_station(),
						users.get(i).get("nick"),
						users.get(i).get("action_time"),
						station.getBusiness_type(),
						station.getParentCode(),
						station.getStationName(),
						station.getAddress()
						
				});
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
	}


	@Override
	public void stationDealStatics(Date time, Stat station) {
		
		try {
			
			String start = CommonUtil.getDateStart(time);
			String end = CommonUtil.getDateEnd(time);
			long rdbTime = CommonUtil.getRdbTime(time);
			String querySql = "select  action_time, nick, lottery, total_money, user_id, action_id from  rdb.t_user_action where action_type = ?  and bet_station =? and action_time>=? and action_time<?   order by action_time desc";
			
			List<Map<String, Object>> deals = rdbTemplate.queryForList(querySql, new Object[]{CONST_ONE,station.getBet_station(),start,end});
			
			String insertSql = "replace into t_stat_deal(time,buy_time,nick,lottery,total_amount,user_id,deal_id,business_type,bet_station,station_name,address,parent_code) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			for (int i = 0; i < deals.size(); i++) {
				
				rdbTemplate.update(insertSql, new Object[]{
						rdbTime,
						deals.get(i).get("action_time"),
						deals.get(i).get("nick"),
						deals.get(i).get("lottery"),
						deals.get(i).get("total_money"),
						deals.get(i).get("user_id"),
						deals.get(i).get("action_id"),
						station.getBusiness_type(),
						station.getBet_station(),
						station.getStationName(),
						station.getAddress(),
						station.getParentCode()
				});
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
	}

}
