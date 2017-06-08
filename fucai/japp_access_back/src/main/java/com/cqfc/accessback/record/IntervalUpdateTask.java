package com.cqfc.accessback.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cqfc.accessback.record.ResultRecord.Record;
import com.cqfc.util.Configuration;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;
import com.mysql.jdbc.MysqlErrorNumbers;

@Component
public class IntervalUpdateTask {
	@Resource(name = "recordDataSource")
	private DataSource recordDataSource;
	private boolean firstTime = true;

	@Scheduled(cron = "0/5 * * * * ?")
	public void executeTask() {
		if (recordDataSource == null) {
			ApplicationContext applicationContext = ApplicationContextProvider
					.getApplicationContext();
			recordDataSource = applicationContext.getBean("recordDataSource",
					PooledDataSource.class);
		}
		Connection connection = null;
		boolean autoCommit = false;
		PreparedStatement insertPs = null;
		PreparedStatement updatePs = null;
		Collection<ResultRecord> resultRecords = TicketCallbackRecordBuffer
				.getResultRecords();
		String setNo = Configuration.getConfigValue("setNo");
		if (StringUtils.isEmpty(setNo)) {
			setNo = "";
		}
		try {
			connection = recordDataSource.getConnection();
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			if (firstTime){
				insertPs = connection.prepareStatement(ResultRecord.getFirstTimeInsertSql());
			} else {
				insertPs = connection.prepareStatement(ResultRecord.getInsertSql());
			}
			updatePs = connection.prepareStatement(ResultRecord.getUpdateSql());

			boolean needInsert = false;
			boolean needUpdate = false;
			for (ResultRecord record : resultRecords) {
				List<Record> records = record.getUpdateRecords();
				for (Record r : records) {
					if (r.hasUpdate) {
						continue;
					}
					if (r.isNew) {
						insertPs.setString(1, r.gameId);
						insertPs.setString(2, r.issue);
						insertPs.setString(3, setNo);
						insertPs.setInt(4, r.successNum);
						insertPs.setInt(5, r.failedNum);
						insertPs.setInt(6, r.successNum);
						insertPs.setInt(7, r.failedNum);
						insertPs.addBatch();
						needInsert = true;
					} else {
						updatePs.setInt(1, r.successNum);
						updatePs.setInt(2, r.failedNum);
						updatePs.setString(3, r.gameId);
						updatePs.setString(4, r.issue);
						updatePs.setString(5, setNo);
						updatePs.addBatch();
						needUpdate = true;
					}
				}
			}
			if (needUpdate) {
				int[] batch = updatePs.executeBatch();
				Log.run.debug("update ticket number, record size=%d", batch.length);
			}
			if (needInsert) {
				int[] batch = insertPs.executeBatch();
				Log.run.debug("insert ticket number, record size=%d", batch.length);
			}
			if (needInsert || needUpdate) {
				connection.commit();
				TicketCallbackRecordBuffer.afterUpdate();
			}
		} catch (Exception e) {
			try {
				connection.rollback();
				if (e instanceof SQLException) {
					if (((SQLException) e).getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
						for (ResultRecord record : resultRecords) {
							List<Record> records = record.getUpdateRecords();
							for (Record r : records) {
								try {
									if (r.hasUpdate) {
										continue;
									}
									if (r.isNew) {
										insertPs.setString(1, r.gameId);
										insertPs.setString(2, r.issue);
										insertPs.setString(3, setNo);
										insertPs.setInt(4, r.successNum);
										insertPs.setInt(5, r.failedNum);
										insertPs.setInt(6, r.successNum);
										insertPs.setInt(7, r.failedNum);
										insertPs.executeUpdate();
										connection.commit();
										ResultRecord.afterUpdate(r);
									} else {
										updatePs.setInt(1, r.successNum);
										updatePs.setInt(2, r.failedNum);
										updatePs.setString(3, r.gameId);
										updatePs.setString(4, r.issue);
										updatePs.setString(5, setNo);
										updatePs.executeUpdate();
										connection.commit();
										ResultRecord.afterUpdate(r);
									}
								} catch (Exception e1) {
									if (e1 instanceof SQLException) {
										try {
											if (((SQLException) e1)
													.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
												updatePs.setInt(1, r.successNum);
												updatePs.setInt(2, r.failedNum);
												updatePs.setString(3, r.gameId);
												updatePs.setString(4, r.issue);
												updatePs.setString(5, setNo);
												updatePs.executeUpdate();
												connection.commit();
												ResultRecord.afterUpdate(r);
											}
										} catch (Exception e2) {
											Log.run.error("execute update failed.", e2);
										}
									}
								}
							}
						}
					}
				}
			} catch (Exception e1) {
				Log.run.error("execute one by one failed.", e1);
			}
		} finally {
			// running.set(false);
			closeConnection(insertPs, updatePs, connection, autoCommit);
			connection = null;
			firstTime = false;
		}
	}

	private void closeConnection(PreparedStatement insertPs,
			PreparedStatement updatePs, Connection connection,
			boolean autoCommit) {
		try {
			if (insertPs != null) {
				insertPs.close();
			}
			if (updatePs != null) {
				updatePs.close();
			}
			if (connection != null) {
				connection.setAutoCommit(autoCommit);
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("", e);
		}
	}
}
