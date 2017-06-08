package com.cqfc.partneraccount.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import com.cqfc.partneraccount.datacenter.PartnerAccountBuffer;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.util.PartnerAccountConstant.PartnerAccountLogType;
import com.jami.util.Log;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

//@Component
public class SqlExecuteTask implements Runnable {
	private static final BlockingQueue<PartnerAccountLog> queue = PartnerAccountBuffer
			.getQueue();
	private static final int MAX_NUM = 500;

	// @Autowired
	DataSource dataSource;

	private AtomicBoolean running = new AtomicBoolean(true);

	private ApplicationContext applicationContext;

	public SqlExecuteTask(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void stop() {
		running.set(false);
	}

	// @Scheduled(cron = "0/5 * * * * ?")
	public void run() {
		Connection connection = null;
		boolean autoCommit = true;
		try {
			dataSource = applicationContext.getBean("dataSource",
					PooledDataSource.class);
			List<PartnerAccountLog> list = new ArrayList<PartnerAccountLog>();
			Map<String, Long> partnerAmount = new HashMap<String, Long>();
//			DataSourceTransactionManager txManager = applicationContext
//					.getBean("txManager", DataSourceTransactionManager.class);
//			DefaultTransactionDefinition def = null;
//			TransactionStatus status = null;
			while (running.get()) {
				try {
					if (queue.size() == 0) {
						closeConnection(connection, autoCommit);
						connection = null;
						Thread.sleep(5000);
						continue;
					}
					queue.drainTo(list, MAX_NUM);
					if (connection == null) {
						connection = dataSource.getConnection();
						autoCommit = connection.getAutoCommit();
						connection.setAutoCommit(false);
					}
//					 def = new DefaultTransactionDefinition();
//					 def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//					 status = txManager.getTransaction(def);
					PreparedStatement psLog = connection
							.prepareStatement("insert into t_lottery_partner_account_log(partnerId,state,totalAmount,accountAmount,remainAmount,serialNumber,ext,remark,createTime) values(?,?,?,?,?,?,?,?,now())");
					for (PartnerAccountLog log : list) {
						String partnerId = log.getPartnerId();
						if (partnerAmount.containsKey(partnerId)) {
							partnerAmount.put(
									partnerId,
									partnerAmount.get(partnerId)
											+ log.getAccountAmount());
						} else {
							partnerAmount
									.put(partnerId, log.getAccountAmount());
						}
						psLog.setString(1, log.getPartnerId());
						psLog.setShort(2, (short) log.getState());
						psLog.setLong(3, log.getTotalAmount());
						psLog.setLong(4, log.getAccountAmount());
						psLog.setLong(5, log.getRemainAmount());
						psLog.setString(6, log.getSerialNumber());
						psLog.setString(7, log.getExt());
						psLog.setString(8, log.getRemark());
						psLog.addBatch();
					}
					PreparedStatement psAccount = connection
							.prepareStatement("update t_lottery_partner_account set totalAmount=totalAmount+?,usableAmount=usableAmount+? where partnerId=?");
					for (Entry<String, Long> entry : partnerAmount.entrySet()) {
						psAccount.setLong(1, entry.getValue());
						psAccount.setLong(2, entry.getValue());
						psAccount.setString(3, entry.getKey());
						psAccount.addBatch();
					}
					if (list.size() > 0) {
						psAccount.executeBatch();
						psLog.executeBatch();
//						txManager.commit(status);
						connection.commit();
						psAccount.close();
						psLog.close();
					}
					list.clear();
					partnerAmount.clear();
					// Thread.sleep(50);// 这个sleep是为了防止CPU过高
				} catch (Exception e) {
					Log.run.debug("", e);
					Log.run.warn("batch insert to database failed,try insert one by one.");
					connection.rollback();
//					txManager.rollback(status);
					Statement st = null;
					for (PartnerAccountLog log : list) {
						try {
//							 def = new DefaultTransactionDefinition();
//							 def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//							 status = txManager.getTransaction(def);
							st = connection.createStatement();

							long amount = log.getAccountAmount();
							String partnerId = log.getPartnerId();
							st.execute("insert into t_lottery_partner_account_log(partnerId,state,totalAmount,accountAmount,remainAmount,serialNumber,ext,remark,createTime) values('"
									+ partnerId
									+ "',"
									+ log.getState()
									+ ","
									+ log.getTotalAmount()
									+ ","
									+ amount
									+ ","
									+ log.getRemainAmount()
									+ ",'"
									+ log.getSerialNumber()
									+ "','"
									+ log.getExt()
									+ "','"
									+ log.getRemark()
									+ "',now())");
							st.execute("update t_lottery_partner_account set totalAmount=totalAmount+"
									+ amount
									+ ",usableAmount=usableAmount+"
									+ amount
									+ " where partnerId="
									+ partnerId
									+ "");
							connection.commit();
//							txManager.commit(status);							
						} catch (DuplicateKeyException e1) {
							connection.rollback();
//							txManager.rollback(status);
							int state = log.getState();
							String remark = "流水号重复";
							if (PartnerAccountLogType.PAYMENT.getValue() == state) {
								remark += "，回滚重复支付";
							} else if (PartnerAccountLogType.REFUND.getValue() == state) {
								remark += "，回滚重复退款";
							} else if (PartnerAccountLogType.PRIZE.getValue() == state) {
								remark += "，回滚重复派奖";
							}

							PartnerAccountBuffer.updateAccount(
									log.getPartnerId(),
									0 - log.getAccountAmount(),
									log.getSerialNumber(),
									PartnerAccountLogType.ROLLBACK, remark);
							Log.run.error("%s.serialNumber=%s", remark,
									log.getSerialNumber());
						} catch(MySQLIntegrityConstraintViolationException e2){
							connection.rollback();
//							txManager.rollback(status);
							int state = log.getState();
							String remark = "流水号重复";
							if (PartnerAccountLogType.PAYMENT.getValue() == state) {
								remark += "，回滚重复支付";
							} else if (PartnerAccountLogType.REFUND.getValue() == state) {
								remark += "，回滚重复退款";
							} else if (PartnerAccountLogType.PRIZE.getValue() == state) {
								remark += "，回滚重复派奖";
							}

							PartnerAccountBuffer.updateAccount(
									log.getPartnerId(),
									0 - log.getAccountAmount(),
									log.getSerialNumber(),
									PartnerAccountLogType.ROLLBACK, remark);
							Log.run.error("%s.serialNumber=%s", remark,
									log.getSerialNumber());
						}catch (Exception e2) {
							connection.rollback();
//							txManager.rollback(status);
							Log.run.fatal(
									"Other error happen when insert, please check, error=%s",
									e.toString());
							Log.run.debug("", e);
						} finally {
							if (st != null) {
								st.close();
							}
						}

					}

					list.clear();
					partnerAmount.clear();
				}
			}
		} catch (Exception e) {
			Log.run.debug("", e);
		} finally {
			// running.set(false);
			closeConnection(connection, autoCommit);
			connection = null;
		}
	}

	private void closeConnection(Connection connection, boolean autoCommit) {
		try {
			if (connection != null) {
				connection.setAutoCommit(autoCommit);
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("", e);
		}
	}
}
