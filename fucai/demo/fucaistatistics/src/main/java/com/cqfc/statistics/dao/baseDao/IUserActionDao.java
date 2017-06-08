package com.cqfc.statistics.dao.baseDao;

import java.util.Date;

import com.cqfc.statistics.common.DataSyncBean;

public interface IUserActionDao {

	/**
	 * 对指定日期进行数据同步
	 * @param givenDate
	 * @param dataSyncBean
	 * @param flag (默认0:按小时更新;1:按天更新)
	 */
	void userAction(Date givenDate, DataSyncBean dataSyncBean,int flag);

	
	
	/**
	 * 清理t_user_action 6个月前的数据
	 * @param date
	 */
	void cleanUserActionTemp(String date);

}
