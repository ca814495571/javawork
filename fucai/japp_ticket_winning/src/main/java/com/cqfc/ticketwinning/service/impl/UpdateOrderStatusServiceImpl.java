package com.cqfc.ticketwinning.service.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.ticketwinning.model.Winning;
import com.cqfc.ticketwinning.model.WinningOrder;
import com.cqfc.ticketwinning.service.IWinningOrderService;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.Configuration;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.IssueConstant;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;

@Service
public class UpdateOrderStatusServiceImpl {

	/**
	 * 算奖后更新订单记录表
	 * @param lotteryId
	 * @param issueNo
	 */
	public int updateOrderStatus(String lotteryId, String issueNo){
		Log.fucaibiz.info("updateOrderStatus(lotteryId=%s, issueNo=%s)", lotteryId, issueNo);
        int returnValue = 0;
    	ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		WinningServiceImpl winningService = ctx.getBean("winningServiceImpl", WinningServiceImpl.class);
		IWinningOrderService orderService = ctx.getBean("winningOrderServiceImpl", WinningOrderServiceImpl.class);	
		try{
			returnValue = updateWinningOrder(lotteryId, issueNo, winningService, orderService);
			if(returnValue == 1){
				returnValue = updateUnWinningOrder(lotteryId, issueNo, orderService);
			}
			else{
				Log.run.error("updateWinningOrder(lotteryId=%s, issueNo=%s, returnValue=%d)", lotteryId, issueNo, returnValue);
				return returnValue;
			}
			Log.fucaibiz.info("UpdateOrderStatusServiceImpl(createLotteryTaskComplete): start, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
			LotteryTaskComplete task = new LotteryTaskComplete();
			task.setLotteryId(lotteryId);
			task.setIssueNo(issueNo);
			task.setTaskType(IssueConstant.TASK_COMPLETE_CAN_SEND);
			task.setSetNo(Configuration.getConfigValue("setNo"));
			
			ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

			ActivemqSendObject sendObject = new ActivemqSendObject();
			sendObject.setLotteryId(lotteryId);
			sendObject.setIssueNo(issueNo);
			sendObject.setObj(task);
			activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
		}catch(Exception e){
			Log.run.error("updateOrderStatus: lotteryId=%s, issueNo=%s, exception=%s",  lotteryId, issueNo, e);
			Log.error("updateOrderStatus: lotteryId=%s, issueNo=%s, exception=%s",  lotteryId, issueNo, e);
			return returnValue;
		}		
		Log.fucaibiz.info("UpdateOrderStatusServiceImpl(createLotteryTaskComplete): complete, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
		return returnValue;
	}
	
	/**
	 * 更新中奖记录订单
	 * @param lotteryId
	 * @param issueNo
	 */
	private int updateWinningOrder(String lotteryId, String issueNo, WinningServiceImpl winningService, IWinningOrderService orderService){
		/**
		 * 1.读取中奖结果表中记录
		 * 2.更新对应订单表中记录
		 * 3.读取所有中奖结果后，一次性更新订单表未中奖记录
		 */
		int returnValue = 0;	
		// 总记录数
		DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
		int winningTotalCount = winningService.countTotalSize(lotteryId, issueNo, DBSourceUtil.WINNING_RESULT_TABLENAME);
		int pageSize = TicketWinningConstantsUtil.ORDER_TABLE_PAGE_SIZE;
		int totalPage = (winningTotalCount - 1) / pageSize + 1;
		Winning winning = null;
		List<Winning> list = null;
		try{
			if(winningTotalCount == 0){
				return 1;
			}
			for(int i = 0; i < totalPage; i++){
				DBSourceUtil.setSlaveDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
				list = winningService.getWinningList(lotteryId, issueNo, i+1, pageSize, DBSourceUtil.WINNING_RESULT_TABLENAME);
				for(int j = 0, size = list.size(); j < size; j++){
					winning = list.get(j);
					//更新订单状态
					DBSourceUtil.setMasterDataSourceType(winning.getDbName());
					returnValue = orderService.updateWinningOrderByOrderNo(WinningOrder.OrderStatus.WAIT_AWARD.getValue(), winning.getWinningAmount(), winning.getOrderNo(), winning.getTableName());
				}
			}
		} catch (DaoLevelException e) {
			Log.run.error("updateWinningOrder(lotteryId=%s, issueNo=%s, exception=%s)", lotteryId, issueNo, e);
			return -1;
		}	
		Log.run.info("updateWinningOrder: complete, (lotteryId=%s, issueNo=%s, returnVaue=%d)", lotteryId, issueNo, returnValue);
		return returnValue;
	}
	
	/**
	 * 更新未中奖记录订单
	 * @param lotteryId
	 * @param issueNo
	 */
	private int updateUnWinningOrder(String lotteryId, String issueNo, IWinningOrderService orderService){
		Log.run.info("updateUnWinningOrder(lotteryId=%s, issueNo=%s)", lotteryId, issueNo);
		int returnValue = 0;
		String dbName = "";
		String tableName = "";
		int dbStartNum = Integer.valueOf(Configuration.getConfigValue("DB_START_NUM"));
		int dbEndNum = Integer.valueOf(Configuration.getConfigValue("DB_END_NUM"));
		int pageSize = TicketWinningConstantsUtil.ORDER_TABLE_PAGE_SIZE;
		int totalPage = 0;
		int totalCount = 0;
		int count = 0;
		
		try{
			for(int i = dbStartNum; i < dbEndNum; i++){
				dbName = DBSourceUtil.getDbName(i);
				for(int j = 0; j < TicketWinningConstantsUtil.ORDER_DB_TABLE_SIZE; j ++){
					//更新订单状态
					tableName = DBSourceUtil.getTableName(dbName, j);
					DBSourceUtil.setSlaveDataSourceType(dbName);
					totalCount = orderService.getUnWinningOrderCount(lotteryId, issueNo, tableName);
					totalPage = (totalCount % pageSize == 0) ? (totalCount / pageSize) : (totalCount / pageSize + 1); 
					for(int k = 0; k < totalPage; k++){
						if(k == (totalPage - 1)){
							count = totalCount - k * pageSize;
						}
						else{
							count = pageSize;
						}
						DBSourceUtil.setMasterDataSourceType(dbName);
						returnValue = orderService.updateUnWinningOrder(lotteryId, issueNo, tableName, count);
					}
				}
			}
		} catch (DaoLevelException e) {
			Log.run.error("updateUnWinningOrder(dbName=%s, tableName=%s, lotteryId=%s, issueNo=%s, exception=%s)", dbName, tableName, lotteryId, issueNo, e);
			return -1;
		}	
		Log.run.info("updateUnWinningOrder: complete, (lotteryId=%s, issueNo=%s, returnValue=%d)", lotteryId, issueNo, returnValue);
		return returnValue;
	}

}
