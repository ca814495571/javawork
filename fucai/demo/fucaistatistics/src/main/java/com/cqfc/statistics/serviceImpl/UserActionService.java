package com.cqfc.statistics.serviceImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cqfc.statistics.common.CommonUtil;
import com.cqfc.statistics.common.DataSyncBean;
import com.cqfc.statistics.common.DataSyncConfig;
import com.cqfc.statistics.common.DateIterator;
import com.cqfc.statistics.common.IConstantUtil;
import com.cqfc.statistics.dao.UserActionDao;
import com.cqfc.statistics.serviceImpl.service.IUserActionService;

@Service
public class UserActionService implements IUserActionService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserActionService.class);

	@Autowired
	private UserActionDao userActionDao;

	@Autowired
	private DataSyncConfig DataSyncConfig;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 根据时间和更新时间间隔决策进行数据同步(flag 0: 按小时  1 : 按天)  默认0
	 * @param date
	 * @param flag
	 */
	public void userAction(Date date,int flag) {

		List<DataSyncBean> dataSyncBeans = DataSyncConfig.getDataSyncBeans();
		org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor poolTask = DataSyncConfig
				.getThreadPool();
		CountDownLatch threadNum = new CountDownLatch(dataSyncBeans.size());
		logger.info(CommonUtil.formatDate(date) +"dataSync start !");
		for (int i = 0; i < dataSyncBeans.size(); i++) {
			poolTask.submit(new DataSync(dataSyncBeans.get(i), threadNum,date,flag));
		}
		try {
			threadNum.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info(CommonUtil.formatDate(date) +"dataSync has done all!");
	}

	class DataSync implements Runnable {

		private CountDownLatch threadNum;

		private DataSyncBean dataSyncBean;

		private Date date;
		
		private int flag;
		public DataSync(DataSyncBean dataSyncBean, CountDownLatch threadNum,Date date,int flag) {
			super();
			this.dataSyncBean = dataSyncBean;
			this.threadNum = threadNum;
			this.date = date;
			this.flag = flag;
		}

		@Override
		public void run() {
			try {
//				logger.info("action:"
//						+ IConstantUtil.ActionType
//								.getDesc((Integer) dataSyncBean
//										.getInsertSqlParam().get("action_type"))
//						+ ";activity_no:"
//						+ dataSyncBean.getInsertSqlParam().get("activity_no")
//						+ " is start!");
				userActionDao.userAction(date, dataSyncBean,flag);

			} catch (Exception e) {
				logger.error(e.toString());
				logger.info("action:"
						+ IConstantUtil.ActionType
								.getDesc((Integer) dataSyncBean
										.getInsertSqlParam().get("action_type"))
						+ ";activity_no:"
						+ dataSyncBean.getInsertSqlParam().get("activity_no")
						+ " has failed.");
			} finally {
				threadNum.countDown();
			}
		}

	}
	

	@Override
	public void userActionHistory(String actionName, Date dateFrom, Date dateTo) {
	
		logger.info("[start] ["+actionName+"] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		try {
			DataSyncBean userAction = applicationContext.getBean(actionName,
					DataSyncBean.class);
			Iterator<Date> i = new DateIterator(dateFrom, dateTo);
			while (i.hasNext()) {
				Date date = i.next();
				userActionDao.userAction(date, userAction,1);
				logger.info(String.format("%22s date=%s ", "["+actionName+"] end",
						CommonUtil.formatDate(date)));
			}
			
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		logger.info("[end] ["+actionName+"] startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		
	}
	
	

	public void userActionHistory(Date dateFrom, Date dateTo) {
	
		try {
			Iterator<Date> i = new DateIterator(dateFrom, dateTo);
			while (i.hasNext()) {
				Date date = i.next();
				userAction(date,1);
				logger.info(String.format("date=%s", 
						CommonUtil.formatDate(date)));
			}
			
		} catch (Exception e) {
			logger.error(e.toString());
		}
		
		logger.info("[end]  startDate="
				+ CommonUtil.formatDate(dateFrom) + " endDate="
				+ CommonUtil.formatDate(dateTo));
		
	}
	
	@Override
	public void cleanUserActionTemp(String time) {

		userActionDao.cleanUserActionTemp(time);

	}
	

}
