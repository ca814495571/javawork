package com.cqfc.statistics.serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.model.ActivityTpPhoneInfo;
import com.cqfc.statistics.serviceImpl.service.IUserActionAnalyzeService;

/**
 * 
 * @author an
 *
 */
@Service
public class UserActionAnalyzeService implements IUserActionAnalyzeService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserActionAnalyzeService.class);


	@Autowired
	RdbService rdbService;

	@Autowired
	private JdbcTemplate rdbJdbcTemplate;

	@Autowired
	private JdbcTemplate cqfcJdbcTemplate;

	@Autowired
	private JdbcTemplate cqfcfinanceJdbcTemplate;

	/**
	 * 分析20150115签到活动数据入库
	 */
	public void statistics_0115() {

		String sql = "select * from t_lottery_user_checkin";
		List<Map<String, Object>> rows = cqfcJdbcTemplate.queryForList(sql);

		if (rows.size() > 0) {
			rdbJdbcTemplate.execute("delete from t_statistics_checkin");
		}
		for (Map<String, Object> row : rows) {
			String userId = (String) row.get("Fuser_id");
			String nick = (String) row.get("Fnick");
			String month = (String) row.get("Fmonth");
			String reward = (String) row.get("Fdaily_reward");
			String checkin = (String) row.get("Fdaily_checkin");
			String Freceive_name = (String) row.get("Freceive_name");
			String Freceive_phone = (String) row.get("Freceive_phone");
			String Freceive_addr = (String) row.get("Freceive_addr");
			String Freceive_iden = (String) row.get("Freceive_iden");

			String[] checkinDates = checkin.split(",");

			Set<String> checkInDate = new HashSet<String>();
			for (int i = 0; i < checkinDates.length; i++) {

				if (checkValidateTime(month + checkinDates[i])) {

					checkInDate.add(month + checkinDates[i]);
				}
			}

			Set<String> hanselDate = new HashSet<String>();
			Set<String> jifenDate = new HashSet<String>();
			List<Map<String, Object>> rewards = (List<Map<String, Object>>) JSONObject
					.parse(reward);

			Map<String, Object> map = new HashMap<String, Object>();

			Long handselAmount = 0L;
			Long jiFenAmount = 0L;
			for (int i = 0; i < rewards.size(); i++) {

				map = rewards.get(i);

				if (checkValidateTime(month + map.get("day"))) {

					if (("CAIJIN").equals(map.get("model"))
							&& "1".equals(map.get("send"))) {

						hanselDate.add(month + map.get("day"));
						handselAmount += Long.parseLong(map.get("value")
								.toString());
					}
					if (("JIFEN").equals(map.get("model"))
							&& "1".equals(map.get("send"))) {

						jiFenAmount += Long.parseLong(map.get("value")
								.toString());
						jifenDate.add(month + map.get("day"));
					}

				}
			}

			Map<String, Object> mapBefore = cqfcJdbcTemplate
					.queryForMap(
							"select sum(Fbuy_count) as beforeTimes,sum(Ftotal_amount) as beforeAmount from t_lottery_deal_recent where Fuser_id = ? and Fbuy_time>='2015-01-01 00:00:00' and Fbuy_time<'2015-01-15 00:00:00' and Fplan_type!=3 and Fdeal_status in (3,4,5,24)",
							new Object[] { userId });

			Map<String, Object> mapAfter = cqfcJdbcTemplate
					.queryForMap(
							"select sum(Fbuy_count) as afterTimes,sum(Ftotal_amount) as afterAmount from t_lottery_deal_recent where Fuser_id = ? and Fbuy_time>='2015-01-15 00:00:00' and Fplan_type!=3 and Fdeal_status in (3,4,5,24)",
							new Object[] { userId });

			BigDecimal beforeTimes = mapBefore.get("beforeTimes") == null ? new BigDecimal(
					0) : (BigDecimal) mapBefore.get("beforeTimes");
			BigDecimal beforeAmount = mapBefore.get("beforeAmount") == null ? new BigDecimal(
					0) : (BigDecimal) mapBefore.get("beforeAmount");

			BigDecimal afterTimes = mapAfter.get("afterTimes") == null ? new BigDecimal(
					0) : (BigDecimal) mapAfter.get("afterTimes");
			BigDecimal afterAmount = mapAfter.get("afterAmount") == null ? new BigDecimal(
					0) : (BigDecimal) mapAfter.get("afterAmount");

			try {

				rdbJdbcTemplate
						.update("insert into t_statistics_checkin (user_id,nick,checkIn_date,checkIn_times,handsel_times,handsel_total,score_times,"
								+ "score_total,handsel_date,score_date,before_amount,before_times,after_amount,after_times,receive,Freceive_name,Freceive_phone,Freceive_addr,Freceive_iden,Faddr_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
								new Object[] {

								userId, nick, transferCheckInDate(checkInDate),
										getCheckinTimes(checkInDate),
										getCheckinTimes(hanselDate),
										handselAmount,
										getCheckinTimes(jifenDate),
										jiFenAmount,
										transferCheckInDate(hanselDate),
										transferCheckInDate(jifenDate),
										beforeAmount, beforeTimes, afterAmount,
										afterTimes ,row.get("Freceive"),Freceive_name,Freceive_phone,Freceive_addr,Freceive_iden,row.get("Faddr_time")});

			} catch (Exception e) {

				
				if (e instanceof DuplicateKeyException) {

					Map<String, Object> mapOld = rdbJdbcTemplate
							.queryForMap(
									"select * from t_statistics_checkin where user_id=?",
									new Object[] { userId });

					addOldDate((String) mapOld.get("checkIn_date"), checkInDate);
					addOldDate((String) mapOld.get("handsel_date"), hanselDate);
					addOldDate((String) mapOld.get("score_date"), jifenDate);

					handselAmount = handselAmount
							+ (Long) mapOld.get("handsel_total");
					jiFenAmount = jiFenAmount
							+ (Long) mapOld.get("score_total");

					rdbJdbcTemplate
							.update("update t_statistics_checkin set checkIn_date=?,checkIn_times=?,"
									+ "handsel_times=?,handsel_total=?,score_times=?,score_total=?,handsel_date=?,score_date=?,"
									+ "before_amount=?,before_times=?,after_amount=?,after_times=? where user_id=?",
									new Object[] {

									transferCheckInDate(checkInDate),
											getCheckinTimes(checkInDate),
											getCheckinTimes(hanselDate),
											handselAmount,
											getCheckinTimes(jifenDate),
											jiFenAmount,
											transferCheckInDate(hanselDate),
											transferCheckInDate(jifenDate),
											beforeAmount, beforeTimes,
											afterAmount, afterTimes, userId });

				}else{
					logger.error(e.toString());
				}
			}

		}

	}

	/**
	 * 分析至今用户使用真实现金购买彩票的总次数,总金额以及各个彩种的比例
	 */
	public void statics_1_19() {

		String sqlOriginal = "select Fnick,Fuser_id,Flottery_name,sum(Factual_amount) as money,COUNT(1) as frequency,MIN(Fbuy_time) as first_time,MAX(Fbuy_time) as last_time from  t_lottery_deal_recent where Factual_amount>0 and  Fplan_type!=3 and Fdeal_status in (3,4,5,24) and Fbuy_time>='2014-11-1 00:00:00' GROUP BY Fuser_id,Flottery_name";

		List<Map<String, Object>> originalRows = cqfcJdbcTemplate
				.queryForList(sqlOriginal);

		rdbJdbcTemplate.execute("delete from t_statistics_1_19");

		for (Map<String, Object> row : originalRows) {

			rdbJdbcTemplate
					.update("insert into t_statistics_1_19 (Fnick,Fuser_id,Flottery_name,money,frequency,first_time,last_time)values(?,?,?,?,?,?,?)",
							new Object[] { row.get("Fnick"),
									row.get("Fuser_id"),
									row.get("Flottery_name"), row.get("money"),
									row.get("frequency"),
									row.get("first_time"), row.get("last_time") });
		}

		String sql = "select Fuser_id, Fnick from t_statistics_1_19 group by Fuser_id";

		List<Map<String, Object>> rows = rdbJdbcTemplate.queryForList(sql);
		DecimalFormat df = new DecimalFormat("#0.00");
		Map<String, Object> map = null;
		ArrayList<String> columns = new ArrayList<String>();
		ArrayList<Object> parms = new ArrayList<Object>();
		for (Map<String, Object> row : rows) {

			sql = "select sum(money) as total_money,sum(frequency) as total_frequency,min(first_time) as first_time,max(last_time) as last_time from t_statistics_1_19 where Fuser_id = ?";

			Map<String, Object> mapTotal = rdbJdbcTemplate.queryForMap(sql,
					new Object[] { row.get("Fuser_id") });

			sql = "select * from t_statistics_1_19 where Fuser_id = ?";

			// sbf.append("insert into t_static_actual_buy where (nick,user_id,total_frequency,total_money,first_time,last_time,");
			columns.add("nick");
			parms.add(row.get("Fnick"));
			columns.add("user_id");
			parms.add(row.get("Fuser_id"));
			columns.add("total_frequency");
			parms.add(mapTotal.get("total_frequency"));
			columns.add("total_money");
			parms.add(mapTotal.get("total_money"));
			columns.add("first_time");
			parms.add(mapTotal.get("first_time"));
			columns.add("last_time");
			parms.add(mapTotal.get("last_time"));

			List<Map<String, Object>> maps = rdbJdbcTemplate.queryForList(sql,
					new Object[] { row.get("Fuser_id") });
			for (int i = 0; i < maps.size(); i++) {
				map = maps.get(i);

				if (StringUtils.isNotBlank((String) map.get("Flottery_name"))) {
					columns.add("lottery_" + (i + 1));
					parms.add(map.get("Flottery_name"));

					columns.add("lottery_money_" + (i + 1));
					parms.add((Long) map.get("money"));
					columns.add("lottery_rate_" + (i + 1));
					parms.add(df.format(Double.parseDouble(((Integer) map
							.get("frequency")).toString())
							/ Double.parseDouble((((BigDecimal) mapTotal
									.get("total_frequency")).toString()))));
				}
			}

			sql = CommonUtil.getInsertSql(columns, "t_static_actual_buy");

			Object[] objects = parms.toArray();
			try {

				rdbJdbcTemplate.update(sql, objects);
			} catch (Exception e) {
				if (e instanceof DuplicateKeyException) {
					logger.error("记录相同userid:" + row.get("Fnick")
							+ e.toString());
					System.out.println("记录相同userid:" + row.get("Fnick"));
				}
			}
			columns.clear();
			parms.clear();
		}

	}

	/**
	 * 投票活动手机机型比例,数量统计
	 */
	public void activityTpPhoneAnalyze() {

		// 获取所有手机的总数量,即总记录数

		int sum = rdbService.activityTpPhoneVersionSum();
		// 分组统计所有机型的数量,并算出比例入库

		Map<String, Long> mapVersion = rdbService.activityTpPhoneVersionGroup();

		Double versionNum = 0.00;
		Double sysNum = 0.00;
		Double netNum = 0.00;

		DecimalFormat df = new DecimalFormat("#0.0000");

		for (String version : mapVersion.keySet()) {

			versionNum = Double.parseDouble(mapVersion.get(version).toString());

			rdbService.activityTpPhoneAnalyzeInsert(version,
					mapVersion.get(version), df.format(versionNum / sum) + "",
					1);

		}
		mapVersion.clear();

		Map<String, Long> mapSys = rdbService.activityTpPhoneSysGroup();

		for (String system : mapSys.keySet()) {

			sysNum = Double.parseDouble(mapSys.get(system).toString());

			rdbService.activityTpPhoneAnalyzeInsert(system, mapSys.get(system),
					df.format(sysNum / sum) + "", 2);

		}
		mapSys.clear();
		Map<String, Long> mapNet = rdbService.activityTpPhoneNetGroup();

		for (String netType : mapNet.keySet()) {

			netNum = Double.parseDouble(mapNet.get(netType).toString());
			rdbService.activityTpPhoneAnalyzeInsert(netType,
					mapNet.get(netType), df.format(netNum / sum) + "", 3);

		}
	}



	/**
	 * 分析CGI日志
	 * 
	 * @param file
	 */
	public void readTpLog(File file) {

		BufferedReader br = null;

		try {
			String str = "";

			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), "UTF-8"));
			ActivityTpPhoneInfo phoneInfo = null;
			while ((str = br.readLine()) != null) {

				if (!(str.contains("GGL_TP_201411") && str
						.contains("queryPartIn"))) {
					continue;
				}
				System.out.println(str);
				phoneInfo = new ActivityTpPhoneInfo();
				phoneInfo.setLogTime(str.substring(0, 19));
				phoneInfo.setActivityNo("GGL_TP_201411");
				if (str.contains("(Linux; Android")) {
					// (Linux; Android 4.4.4; MI 3 Build/KTU84P)
					// AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0
					// Chrome/33.0.0.0 Mobile Safari/537.36
					// MicroMessenger/6.0.0.50_r844973.501 NetType/WIFI cqfc_001
					String[] phones = str.substring(
							str.indexOf("(Linux; Android")).split(";");

					if (phones.length == 3) {

						phoneInfo.setPhoneInfo1(phones[2].substring(1,
								phones[2].indexOf(")")));
						phoneInfo.setPhoneInfo2(phones[1].substring(1));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						if (phones[2].indexOf("NetType/") != -1) {

							if (phones[2].substring(
									phones[2].indexOf("NetType/"),
									phones[2].indexOf("\t")).split("/").length == 1) {
								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[2].substring(
										phones[2].indexOf("NetType/"),
										phones[2].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					} else

					if (phones.length == 4) {

						phoneInfo.setPhoneInfo1(phones[3].substring(1,
								phones[3].indexOf(")")));
						phoneInfo.setPhoneInfo2(phones[1].substring(1));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						phoneInfo.setPhoneInfo4(phones[2].substring(1));
						if (phones[3].indexOf("NetType/") != -1) {

							if (phones[3].substring(
									phones[3].indexOf("NetType/"),
									phones[3].indexOf("\t")).split("/").length == 1) {

								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[3].substring(
										phones[3].indexOf("NetType/"),
										phones[3].indexOf("\t")).split("/")[1]);
							}
						} else {
							phoneInfo.setNetType("");
						}
					} else {
						continue;
					}

				} else

				if (str.contains("(iPhone;")) {
					String[] phones = str.substring(str.indexOf("(iPhone;"))
							.split(";");

					if (phones.length == 2) {

						phoneInfo.setPhoneInfo1(phones[0].substring(1));
						phoneInfo.setPhoneInfo2(phones[1].substring(1,
								phones[1].indexOf(")")));
						if (phones[1].indexOf("NetType/") != -1) {

							if (phones[1].substring(
									phones[1].indexOf("NetType/"),
									phones[1].indexOf("\t")).split("/")[1]
									.length() == 1) {

								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[1].substring(
										phones[1].indexOf("NetType/"),
										phones[1].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					}

					if (phones.length == 4) {

						phoneInfo.setPhoneInfo1(phones[0].substring(1));
						phoneInfo.setPhoneInfo2(phones[2].substring(1));
						phoneInfo.setPhoneInfo4(phones[3].substring(1,
								phones[3].indexOf(")")));
						phoneInfo.setPhoneInfo5(phones[1].substring(1));
						if (phones[3].indexOf("NetType/") != -1) {

							if (phones[3].substring(
									phones[3].indexOf("NetType/"),
									phones[3].indexOf("\t")).split("/")[1]
									.length() == 1) {

								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[3].substring(
										phones[3].indexOf("NetType/"),
										phones[3].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}

					}

				} else

				if (str.contains("(iPad;")) {
					String[] phones = str.substring(str.indexOf("(iPad;"))
							.split(";");

					phoneInfo.setPhoneInfo1(phones[0].substring(1));
					phoneInfo.setPhoneInfo2(phones[1].substring(1,
							phones[1].indexOf(")")));
					if (phones[1].indexOf("NetType/") != -1) {

						if (phones[1].substring(phones[1].indexOf("NetType/"),
								phones[1].indexOf("\t")).split("/").length == 1) {
							phoneInfo.setNetType("");
						} else {

							phoneInfo.setNetType(phones[1].substring(
									phones[1].indexOf("NetType/"),
									phones[1].indexOf("\t")).split("/")[1]);
						}

					} else {
						phoneInfo.setNetType("");
					}

				} else

				if (str.contains("(Linux; U;")) {
					String[] phones = str.substring(str.indexOf("(Linux; U;"))
							.split(";");

					if (phones.length == 7) {

						phoneInfo.setPhoneInfo1(phones[4].substring(1));
						phoneInfo.setPhoneInfo2(phones[2].substring(1));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						phoneInfo.setPhoneInfo4(phones[3].substring(1));
						phoneInfo.setPhoneInfo6(phones[1].substring(1));
						phoneInfo.setPhoneInfo7(phones[5].substring(1));
						phoneInfo.setPhoneInfo5(phones[6].substring(1,
								phones[6].indexOf(")")));
						if (phones[6].indexOf("NetType/") != -1) {

							if (phones[6].substring(
									phones[6].indexOf("NetType/"),
									phones[6].indexOf("\t")).split("/").length == 1) {
								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[6].substring(
										phones[6].indexOf("NetType/"),
										phones[6].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					}

					if (phones.length == 5) {

						phoneInfo.setPhoneInfo1(phones[4].substring(1,
								phones[4].indexOf(")")));
						phoneInfo.setPhoneInfo2(phones[2].substring(1));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						phoneInfo.setPhoneInfo4(phones[3].substring(1));
						phoneInfo.setPhoneInfo5(phones[1].substring(1));
						if (phones[4].indexOf("NetType/") != -1) {

							if (phones[4].substring(
									phones[4].indexOf("NetType/"),
									phones[4].indexOf("\t")).split("/").length == 1) {
								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[4].substring(
										phones[4].indexOf("NetType/"),
										phones[4].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					}

					if (phones.length == 6) {

						phoneInfo.setPhoneInfo1(phones[4].substring(1));
						phoneInfo.setPhoneInfo2(phones[2].substring(1));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						phoneInfo.setPhoneInfo4(phones[3].substring(1));
						phoneInfo.setPhoneInfo5(phones[1].substring(1));
						phoneInfo.setPhoneInfo6(phones[5].substring(1,
								phones[5].indexOf(")")));
						if (phones[5].indexOf("NetType/") != -1) {

							if (phones[5].substring(
									phones[5].indexOf("NetType/"),
									phones[5].indexOf("\t")).split("/").length == 1) {
								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[5].substring(
										phones[5].indexOf("NetType/"),
										phones[5].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					}

					if (phones.length == 3) {

						phoneInfo.setPhoneInfo1("");
						phoneInfo.setPhoneInfo2(phones[2].substring(1,
								phones[2].indexOf(")")));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						phoneInfo.setPhoneInfo5(phones[1].substring(1));
						if (phones[2].indexOf("NetType/") != -1) {

							if (phones[2].substring(
									phones[2].indexOf("NetType/"),
									phones[2].indexOf("\t")).split("/").length == 1) {
								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[2].substring(
										phones[2].indexOf("NetType/"),
										phones[2].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					}

					if (phones.length == 4) {

						phoneInfo.setPhoneInfo1("");
						phoneInfo.setPhoneInfo2(phones[2].substring(1));
						phoneInfo.setPhoneInfo3(phones[0].substring(1));
						phoneInfo.setPhoneInfo5(phones[1].substring(1));
						if (phones[3].indexOf("NetType/") != -1) {

							if (phones[3].substring(
									phones[3].indexOf("NetType/"),
									phones[3].indexOf("\t")).split("/").length == 1) {
								phoneInfo.setNetType("");
							} else {

								phoneInfo.setNetType(phones[3].substring(
										phones[3].indexOf("NetType/"),
										phones[3].indexOf("\t")).split("/")[1]);
							}

						} else {
							phoneInfo.setNetType("");
						}
					}

				} else {

					continue;
				}
				rdbService.insertActivityTpPhoneInfo(phoneInfo);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Set<String> addOldDate(String str, Set<String> set) {

		String[] strs = str.split(",");

		for (int i = 0; i < strs.length; i++) {
			set.add(strs[i]);
		}

		return set;
	}

	public String transferCheckInDate(Set<String> set) {

		String str = "";
		Object[] a = set.toArray();
		Arrays.sort(a);
		for (int i = 0; i < a.length; i++) {
			str += (String) a[i];
			if (i < a.length - 1) {
				str += ",";
			}
		}

		return str;
	}

	public boolean checkValidateTime(String str) {

		long base = 20150115;
		if (Long.parseLong(str) >= base) {

			return true;
		}
		return false;
	}

	public int getCheckinTimes(Set<String> set) {

		int time = 0;
		Object[] strs = set.toArray();
		for (int i = 0; i < strs.length; i++) {

			if (StringUtils.isNotEmpty((String) strs[i])
					&& checkValidateTime((String) strs[i])) {
				time++;
			}
		}

		return time;
	}
}
