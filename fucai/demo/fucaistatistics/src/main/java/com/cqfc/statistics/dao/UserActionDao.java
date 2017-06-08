package com.cqfc.statistics.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.common.DataSyncBean;
import com.cqfc.statistics.common.IConstantUtil;
import com.cqfc.statistics.dao.baseDao.IUserActionDao;
import com.cqfc.statistics.dao.baseDao.TemplateBase;
/**
 * 用户操作行为
 * @author an
 *
 */
@Repository
public class UserActionDao extends TemplateBase implements IUserActionDao{

	private static final Logger logger = LoggerFactory
			.getLogger(UserActionDao.class);
	@Override
	public void userAction(Date givenDate, DataSyncBean dataSyncBean,int flag) {

		try {
			List<Map<String, Object>> rows = queryForList(
					transferSelectSql(dataSyncBean.getSelectSql()),
					setParam(dataSyncBean.getSelectSql(), givenDate,flag,
							dataSyncBean.getSelectSqlParam()),
					dataSyncBean.getSourceUrl(), dataSyncBean.getSourceDataBase());
			logger.info("action:"
					+ IConstantUtil.ActionType.getDesc((Integer) dataSyncBean
							.getInsertSqlParam().get("action_type"))
					+ ";activity_no:"
					+ dataSyncBean.getInsertSqlParam().get("activity_no")
					+ " total num is :" + rows.size());
			String[] columns = CommonUtil.getInsertColumn(dataSyncBean
					.getInsertSql());
			Map<String, Object> insertParamMap = dataSyncBean.getInsertSqlParam();
			String[] updateColumns = null;
			if (dataSyncBean.getExceptionHandler() == CONST_THREE) {

				updateColumns = CommonUtil.getUpdateColumn(dataSyncBean
						.getExceptionSql());
			}
			for (Map<String, Object> row : rows) {

				Object[] insertParams = new Object[columns.length];

				for (int i = 0; i < insertParams.length; i++) {
					columns[i] = columns[i].trim();

					// 配置需要手动添加数据的字段 map中含有对应的手动字段则取出值

					if (insertParamMap.get(columns[i]) == null) {
						insertParams[i] = row.get(columns[i]);

					} else {

						insertParams[i] = insertParamMap.get(columns[i]);
					}
				}

				try {
					
					rdbTemplate.update(dataSyncBean.getInsertSql(), insertParams);
				} catch (Exception e) {
					// 根据操作类型不同,违反唯一键的做出不同的处理
					if (e instanceof DuplicateKeyException) {
						try {
							// 根据配置的异常常量来处理
							if (dataSyncBean.getExceptionHandler() == CONST_ONE) {
								// 不做处理
							}

							if (dataSyncBean.getExceptionHandler() == CONST_TWO) {
								// 执行配置的异常处理sql,replace into
								rdbTemplate.update(dataSyncBean.getExceptionSql(),insertParams);
							}

							if (dataSyncBean.getExceptionHandler() == CONST_THREE) {
								// UPDATE

								Object[] updateParams = new Object[updateColumns.length];

								for (int i = 0; i < updateColumns.length; i++) {

									if (row.get(updateParams[i]) != null) {
										updateParams[i] = row.get(updateParams[i]);
										continue;
									}

									if (insertParamMap.get(updateParams[i]) != null) {
										updateParams[i] = insertParamMap
												.get(updateParams[i]);
										continue;
									}
								}

								rdbTemplate.update( dataSyncBean.getExceptionSql(),updateParams);
							}
						} catch (Exception e2) {
							logger.error("action:"
							+ IConstantUtil.ActionType
									.getDesc((Integer) dataSyncBean
											.getInsertSqlParam().get("action_type"))
							+ ";activity_no:"
							+ dataSyncBean.getInsertSqlParam().get("activity_no")
							+"exceptionSql excute fail!"
									+ e2.toString());
						}
					} else {
						logger.error("action:"
								+ IConstantUtil.ActionType
								.getDesc((Integer) dataSyncBean
										.getInsertSqlParam().get("action_type"))
						+ ";activity_no:"
						+ dataSyncBean.getInsertSqlParam().get("activity_no")
						+"exceptionSql excute fail!" + e.toString());
					}
				}
			}
			
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
	}

	
	
	/**
	 * 转化配置的selectSql成为带？的sql
	 * 
	 * @param selectSql
	 * @return
	 */
	public String transferSelectSql(String selectSql) {

		StringBuffer sbf = new StringBuffer();

		if (StringUtils.isNotEmpty(selectSql) && selectSql.indexOf("@") > 0) {
			String[] strs = selectSql.split("@");

			for (int i = 0; i < strs.length; i++) {

				if (i != 0) {

					strs[i] = "?" + strs[i].substring(strs[i].indexOf("}") + 1);
				}
				sbf.append(strs[i]);
			}

			return sbf.toString();
		} else {

			return selectSql;
		}

	}

	/**
	 * 将配置的参数根据selectSql顺序设置参数的值,设置的值来源于配置
	 * 
	 * @param givenDate
	 * @return
	 */
	public Object[] setParam(String selectSql, Date givenDate,int flag,
			Map<String, Object> map) {
		
		String start = "";
		String end = "";
		//按天
		start = CommonUtil.getOffset(givenDate, 0, 0, -1, -5);
		end = CommonUtil.formatTimestamp(givenDate);
		if(flag == 1){
			start = CommonUtil.getDateStart(givenDate);
			end = CommonUtil.getDateEnd(givenDate);
		}
		
		List<Object> objects = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(selectSql) && selectSql.indexOf("@") > 0) {
			String[] strs = selectSql.split("@");

			for (int i = 1; i < strs.length; i++) {

				String str = strs[i].substring(strs[i].indexOf("{") + 1,
						strs[i].indexOf("}"));
				// 如果没有给selectParam设置,则sql会报错,参数数量不匹配
				Object object = null;
				if (map != null) {
					object = map.get(str);
				}

				if (object == null || "null".equals(object)
						|| "".equals(object)) {

					if ("start".equals(str)) {
						objects.add(start);
					}

					if ("end".equals(str)) {
						objects.add(end);
					}
				} else {
					objects.add(object);
				}
			}
		}

		return objects.toArray();
	}

	@Override
	public void cleanUserActionTemp(String date) {

		try {

			int num = rdbTemplate.update(
					"delete from t_user_action where action_time<=?",
					new Object[] { date });
			if (num >= 0) {
				logger.info("before " + date + " datas clean successfully!");
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}

	}
	
	
	public synchronized List<Map<String, Object>> queryForList(String sql,
			Object[] params, String url, String database) {
		
		return	CommonUtil.getJdbcTemplate(url, database).queryForList(sql,
							params);
	}

}
