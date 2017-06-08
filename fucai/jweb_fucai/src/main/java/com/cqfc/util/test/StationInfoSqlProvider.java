package com.cqfc.util.test;

import org.apache.ibatis.jdbc.SQL;

import com.cqfc.management.model.StationInfo;

public class StationInfoSqlProvider {
	/**
	 * 插入数据的第一种写法 SQL 支持的方法如下： SELECT(String) SELECT_DISTINCT(String)
	 * FROM(String) JOIN(String) INNER_JOIN(String) LEFT_OUTER_JOIN(String)
	 * RIGHT_OUTER_JOIN(String) WHERE(String) OR() AND() GROUP_BY(String)
	 * HAVING(String) ORDER_BY(String) DELETE_FROM(String) INSERT_INTO(String)
	 * SET(String) UPDATE(String) VALUES(String, String)
	 * 具体参考：http://mybatis.github.io/mybatis-3/statement-builders.html
	 * 
	 * @param stationInfo
	 * @return
	 */
	public String insertSatationInfoOne(StationInfo stationInfo) {

		SQL sql = new SQL().INSERT_INTO("t_station_info");
		sql.VALUES("parentId", "'" + stationInfo.getId() + "'");
		if (stationInfo.getStationName() == null) {

			sql.VALUES("stationName", "''");
		} else {
			sql.VALUES("stationName", "'" + stationInfo.getStationName() + "'");
		}

		if (stationInfo.getStationLinkman() == null) {

			sql.VALUES("stationLinkman", "''");
		} else {
			sql.VALUES("stationLinkman", "'" + stationInfo.getStationLinkman()
					+ "'");
		}

		if (stationInfo.getStationTel() == null) {
			sql.VALUES("stationTel", "''");
		} else {
			sql.VALUES("stationTel", "'" + stationInfo.getStationTel() + "'");
		}

		if (stationInfo.getStationAddOne() == null) {
			sql.VALUES("stationAddOne", "''");
		} else {
			sql.VALUES("stationAddOne", "'" + stationInfo.getStationAddOne()
					+ "'");
		}

		if (stationInfo.getStationAddTwo() == null) {
			sql.VALUES("stationAddTwo", "''");
		} else {
			sql.VALUES("stationAddTwo", "'" + stationInfo.getStationAddTwo()
					+ "'");
		}

		if (stationInfo.getStationCode() == null) {
			sql.VALUES("stationCode", "''");
		} else {
			sql.VALUES("stationCode", "'" + stationInfo.getStationCode() + "'");
		}

		if (stationInfo.getStationOrgLevel() == null) {
			sql.VALUES("stationOrgLevel", "''");
		} else {
			sql.VALUES("stationOrgLevel",
					"'" + stationInfo.getStationOrgLevel() + "'");
		}

		if (stationInfo.getStationOrg() == null) {
			sql.VALUES("stationOrg", "''");
		} else {
			sql.VALUES("stationOrg", "'" + stationInfo.getStationOrg() + "'");
		}

		if (stationInfo.getStationLongitude() == null) {
			sql.VALUES("stationLongitude", "''");
		} else {
			sql.VALUES("stationLongitude",
					"'" + stationInfo.getStationLongitude() + "'");
		}

		if (stationInfo.getStationLatitude() == null) {
			sql.VALUES("stationLatitude", "''");
		} else {
			sql.VALUES("stationLatitude",
					"'" + stationInfo.getStationLatitude() + "'");
		}

		if (stationInfo.getStationAccountNum() == null) {
			sql.VALUES("stationAccountNum", "''");
		} else {
			sql.VALUES("stationAccountNum",
					"'" + stationInfo.getStationAccountNum() + "'");
		}

		if (stationInfo.getStationPassword() == null) {
			sql.VALUES("stationPassword", "''");
		} else {
			sql.VALUES("stationPassword",
					"'" + stationInfo.getStationPassword() + "'");
		}
		if (stationInfo.getStationCreateTime() == null) {
			sql.VALUES("stationCreateTime", "''");
		} else {
			sql.VALUES(
					"stationCreateTime",
					"'"
							+ 
							stationInfo.getStationCreateTime() + "'");
		}

		sql.VALUES("stationFlag", "'" + stationInfo.getStationFlag() + "'");

		return sql.toString();

	}

	/**
	 * 插入数据第二种写法
	 * 
	 * @param stationInfo
	 * @return
	 */
	public String insertSatationInfoTwo(final StationInfo stationInfo) {

		SQL sql = new SQL() {
			{
				INSERT_INTO("t_station_info");
				VALUES("parentId", "'" + stationInfo.getParentId() + "'");
				if (stationInfo.getStationName() == null) {
					VALUES("stationName", "''");
				} else {
					VALUES("stationName", "'" + stationInfo.getStationName()
							+ "'");
				}

				if (stationInfo.getStationLinkman() == null) {
					VALUES("stationLinkman", "''");
				} else {
					VALUES("stationLinkman",
							"'" + stationInfo.getStationLinkman() + "'");
				}

				if (stationInfo.getStationTel() == null) {
					VALUES("stationTel", "''");
				} else {
					VALUES("stationTel", "'" + stationInfo.getStationTel()
							+ "'");
				}

				if (stationInfo.getStationAddOne() == null) {
					VALUES("stationAddOne", "''");
				} else {
					VALUES("stationAddOne",
							"'" + stationInfo.getStationAddOne() + "'");
				}

				if (stationInfo.getStationAddTwo() == null) {
					VALUES("stationAddTwo", "''");
				} else {
					VALUES("stationAddTwo",
							"'" + stationInfo.getStationAddTwo() + "'");
				}

				if (stationInfo.getStationCode() == null) {
					VALUES("stationCode", "''");
				} else {
					VALUES("stationCode", "'" + stationInfo.getStationCode()
							+ "'");
				}

				if (stationInfo.getStationOrgLevel() == null) {
					VALUES("stationOrgLevel", "''");
				} else {
					VALUES("stationOrgLevel",
							"'" + stationInfo.getStationOrgLevel() + "'");
				}

				if (stationInfo.getStationOrg() == null) {
					VALUES("stationOrg", "''");
				} else {
					VALUES("stationOrg", "'" + stationInfo.getStationOrg()
							+ "'");
				}

				if (stationInfo.getStationLongitude() == null) {
					VALUES("stationLongitude", "''");
				} else {
					VALUES("stationLongitude",
							"'" + stationInfo.getStationLongitude() + "'");
				}

				if (stationInfo.getStationLatitude() == null) {
					VALUES("stationLatitude", "''");
				} else {
					VALUES("stationLatitude",
							"'" + stationInfo.getStationLatitude() + "'");
				}

				if (stationInfo.getStationAccountNum() == null) {
					VALUES("stationAccountNum", "''");
				} else {
					VALUES("stationAccountNum",
							"'" + stationInfo.getStationAccountNum() + "'");
				}

				if (stationInfo.getStationPassword() == null) {
					VALUES("stationPassword", "''");
				} else {
					VALUES("stationPassword",
							"'" + stationInfo.getStationPassword() + "'");
				}

				if (stationInfo.getStationCreateTime() == null) {
					VALUES("stationCreateTime", "''");
				} else {
					VALUES("stationCreateTime",
							"'"
									+ stationInfo
											.getStationCreateTime() + "'");
				}

				VALUES("stationFlag", "'" + stationInfo.getStationFlag() + "'");

			}
		};
		
		return sql.toString();
	}

	/**
	 * 插入数据第三种写法 执行的语句： INSERT INTO t_station_info (parentId, stationName,
	 * stationLinkman, stationTel, stationAddOne, stationAddTwo, stationCode,
	 * stationOrgLevel, stationOrg, stationLongitude, stationLatitude,
	 * stationAccountNum, stationPassword, stationCreateTime, stationFlag)
	 * VALUES (#{parentId}, #{stationName}, '', '', '', '', '', '', '', '', '',
	 * #{stationAccountNum}, #{stationPassword}, #{stationCreateTime},
	 * #{stationFlag})
	 * 
	 * @param stationInfo
	 * @return
	 */
	public String insertSatationInfoThree(final StationInfo stationInfo) {

		SQL sql = new SQL() {
			{
				INSERT_INTO("t_station_info");
				VALUES("parentId", "#{stationInfo.id}");
				if (stationInfo.getStationName() == null) {
					VALUES("stationName", "''");
				} else {
					VALUES("stationName", "#{stationName}");
				}

				if (stationInfo.getStationLinkman() == null) {
					VALUES("stationLinkman", "''");
				} else {
					VALUES("stationLinkman", "#{stationLinkman}");
				}
				if (stationInfo.getStationTel() == null) {
					VALUES("stationTel", "''");
				} else {
					VALUES("stationTel", "#{stationTel}");
				}

				if (stationInfo.getStationAddOne() == null) {
					VALUES("stationAddOne", "''");
				} else {
					VALUES("stationAddOne", "#{stationAddOne}");
				}

				if (stationInfo.getStationAddTwo() == null) {
					VALUES("stationAddTwo", "''");
				} else {
					VALUES("stationAddTwo", "#{stationAddTwo}");
				}

				if (stationInfo.getStationCode() == null) {
					VALUES("stationCode", "''");
				} else {
					VALUES("stationCode", "#{stationCode}");
				}

				if (stationInfo.getStationOrgLevel() == null) {
					VALUES("stationOrgLevel", "''");
				} else {
					VALUES("stationOrgLevel", "#{stationOrgLevel}");
				}

				if (stationInfo.getStationOrg() == null) {
					VALUES("stationOrg", "''");
				} else {
					VALUES("stationOrg", "#{stationOrg}");
				}

				if (stationInfo.getStationLongitude() == null) {
					VALUES("stationLongitude", "''");
				} else {
					VALUES("stationLongitude", "#{sstationLongitude}");
				}

				if (stationInfo.getStationLatitude() == null) {
					VALUES("stationLatitude", "''");
				} else {
					VALUES("stationLatitude", "#{stationLatitude}");
				}

				if (stationInfo.getStationAccountNum() == null) {
					VALUES("stationAccountNum", "''");
				} else {
					VALUES("stationAccountNum", "#{stationAccountNum}");
				}

				if (stationInfo.getStationPassword() == null) {
					VALUES("stationPassword", "''");
				} else {
					VALUES("stationPassword", "#{stationPassword}");
				}

				if (stationInfo.getStationCreateTime() == null) {
					VALUES("stationCreateTime", "0000-00-00");
				} else {
					VALUES("stationCreateTime", "#{stationCreateTime}");
				}

				VALUES("stationFlag", "#{stationFlag}");

			}
		};
		return sql.toString();
	}

}
