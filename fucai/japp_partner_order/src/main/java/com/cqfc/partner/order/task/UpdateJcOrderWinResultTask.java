package com.cqfc.partner.order.task;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;

import com.cqfc.partner.order.dataCache.PartnerOrderBuffer;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.jami.util.CountLog;
import com.jami.util.DataSourceUtil;
import com.jami.util.Log;

public class UpdateJcOrderWinResultTask implements Runnable {

	private static final List<BlockingQueue<WinningOrderInfo>> listQueue = PartnerOrderBuffer
			.getWinResultUpdateQueue();

	
	private BlockingQueue<WinningOrderInfo> queue;

	private static final int MAX_NUM = 500;

	private AtomicBoolean running;

	private ApplicationContext applicationContext;

	private int num;

	public UpdateJcOrderWinResultTask(ApplicationContext applicationContext,
			AtomicBoolean running, int num) {

		this.queue = listQueue.get(num);
		this.num = num;
		this.applicationContext = applicationContext;
		this.running = running;

	}

	public void stop() {
		running.set(false);
	}

	@Override
	public void run() {

		String dbName = DataSourceUtil.getDateSourceName(num);
		String masterDbName = DataSourceUtil.MASTER + dbName;
		DataSource dataSource = null;

		List<WinningOrderInfo> list = new ArrayList<WinningOrderInfo>();
		// 全局库的数据连接
		Connection conn = null;
		Statement statement = null;
		
		try {

			dataSource = applicationContext.getBean(masterDbName,
					PooledDataSource.class);
			while (running.get()) {

				if (queue.size() == 0) {
					try {
						Thread.sleep(500);

					} catch (InterruptedException e) {
						CountLog.run.error(Thread.currentThread().getName()
								+ ",sleep异常", e);
					}
					continue;
				}
				queue.drainTo(list, MAX_NUM);

				try {

					conn = dataSource.getConnection();
					statement = conn.createStatement();
					conn.setAutoCommit(false);

					for (WinningOrderInfo order : list) {

						if (!running.get()) {
							return;
						}
						statement.addBatch(getUpdateJcWinResultSql(order,
								DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId())));
					}

					try {

						statement.executeBatch();
						conn.commit();
						conn.setAutoCommit(true);
					} catch (Exception e) {
						CountLog.run.warn(masterDbName
								+ "竞彩批量修改订单中奖金额及状态出现异常", e);
						try {

							conn.rollback();
							DataSourceUtil.closeConn(conn, statement);
							conn = dataSource.getConnection();
							statement = conn.createStatement();
							conn.setAutoCommit(true);
							for (WinningOrderInfo order : list) {
									
									statement.execute(getUpdateJcWinResultSql(order,
											DataSourceUtil.getTableName(DataSourceUtil.PARTNERORDER_TABLENAME,order.getTradeId())));
							}
							CountLog.run.info("逐个修改竞彩订单中奖金额及状态数据库成功!");
						} catch (Exception e2) {
							CountLog.addPartnerOrder.error(masterDbName
									+ "逐个修改竞彩订单中奖金额及状态出现异常,需及时处理", e2);
							CountLog.run.error(masterDbName
									+ "逐个修改竞彩订单中奖金额及状态出现异常,需及时处理", e2);
							Log.error(masterDbName
									+ "逐个修改竞彩订单中奖金额及状态出现异常,需及时处理", e2);
							running.set(false);
							return;
						}
					}

				} catch (Exception e) {

					CountLog.run.error("竞彩算奖修改全局订单状态和金额时系统异常,需及时处理", e);
					running.set(false);
					return;

				} finally {

						if (running.get()) {

						}
					list.clear();
					DataSourceUtil.closeConn(conn, statement);
				}

			}
		} catch (Exception e) {
			CountLog.run.error("竞彩算奖修改全局订单状态和金额时系统异常,需及时处理", e);
		}
	}

	
	public String getUpdateJcWinResultSql(WinningOrderInfo order ,String tableName){
		
		
		return "update "+ tableName 
				+" set winPrizeMoney = '" +order.getWinningAmount()
				+"',orderStatus =" +order.getOrderStatus()+" where orderNo='"+order.getOrderNo()+"'";
		
	}

}
