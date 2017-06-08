package com.cqfc.ticketwinning.task;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.ticketwinning.model.Winning;
import com.cqfc.ticketwinning.model.WinningOrder;
import com.cqfc.ticketwinning.service.IWinningOrderService;
import com.cqfc.ticketwinning.service.impl.WinningOrderServiceImpl;
import com.cqfc.ticketwinning.service.impl.WinningServiceImpl;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.AppendTaskConstant;
import com.cqfc.util.Configuration;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.OrderConstant;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

public class SendPrizeTask implements Runnable{

	private String lotteryId;
	private String issueNo;
	
	
	public SendPrizeTask(String lotteryId, String issueNo) {
		this.lotteryId = lotteryId;
		this.issueNo = issueNo;
	}


	@Override
	public void run() {
		Log.fucaibiz.info("SendPrizeTask: start, (lotteryId=%s, issueNo=%s)", lotteryId, issueNo);
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		WinningServiceImpl winningService = applicationContext.getBean("winningServiceImpl", WinningServiceImpl.class);
		IWinningOrderService orderService = applicationContext.getBean("winningOrderServiceImpl", WinningOrderServiceImpl.class);
		/**
		 * 1.从中奖结果表中读取对应信息
		 * 2.根据渠道信息派发到渠道用户，还是普通用户
		 */
		DBSourceUtil.setMasterDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);
		int winningTotalCount = winningService.countTotalSize(lotteryId, issueNo, DBSourceUtil.WINNING_RESULT_TABLENAME);
		int pageSize = TicketWinningConstantsUtil.ORDER_TABLE_PAGE_SIZE;
		int totalPage = (winningTotalCount % pageSize == 0) ? (winningTotalCount / pageSize) : (winningTotalCount / pageSize + 1);
		List<Winning> list = null;
		ReturnMessage msg = null;
		Winning winning = null;
		long winningTotalMoney = 0L;
		int returnValue = 0;
		int prizeLevel = AppendTaskConstant.OrderPrizeLevel.SMALLPRIZE.getValue();//中奖级别，1 小奖 2 大奖		
		
		try{			
			for(int i = 0; i < totalPage; i++){
				list = winningService.getUnSendPrizeWinningList(lotteryId, issueNo, i+1, pageSize, DBSourceUtil.WINNING_RESULT_TABLENAME);
				
				for(int j = 0, size = list.size(); j < size; j++){
					winning = list.get(j);
					winningTotalMoney = winning.getWinningAmount() * winning.getMultiple();
					//大于一万为大奖
					if(winningTotalMoney > TicketWinningConstantsUtil.BIG_PRIZE_AMOUNT){
						prizeLevel = AppendTaskConstant.OrderPrizeLevel.BIGPRIZE.getValue();
					}
					else{
						prizeLevel = AppendTaskConstant.OrderPrizeLevel.SMALLPRIZE.getValue();
					}
					ScanLog.scan.debug("sendPrize(lotteryId=%s, issueNo=%s, orderNo=%s, winningTotalMoney=%d",  lotteryId, issueNo, winning.getOrderNo(), winningTotalMoney);
					// 大奖不派奖
					if(prizeLevel == AppendTaskConstant.OrderPrizeLevel.SMALLPRIZE.getValue()){	
						msg = TransactionProcessor.dynamicInvoke("partner","isPartnerAccount", winning.getPartnerId());
						//只有partnerId类型是3才派奖
						if(!(Boolean) msg.getObj()){							
							//派奖
							msg = TransactionProcessor.dynamicInvoke("userAccount",
									"sendPrize", winning.getUserId(), OrderConstant.SEND_PRIZE__PREFIX + winning.getOrderNo(), winningTotalMoney);
							if(!ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
									.equals(msg.getStatusCode())){
								Log.run.error("SendPrizeTask(userAccount),  %s", msg.getMsg());
								throw new DaoLevelException();
							}
							//调用更新追号接口
							if(winning.getOrderType() == Winning.OrderType.AFTER_ORDER.getValue()){
								msg = TransactionProcessor.dynamicInvoke("appendTask",
										"updateAppendAfterOrderPrize", winning.getOrderNo(), prizeLevel, winning.getUserId());
								if(!ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
										.equals(msg.getStatusCode())){
									Log.run.error("SendPrizeTask(appendTask),  %s", msg.getMsg());
									throw new DaoLevelException();
								}
							}
							// 更新全部数据库中订单状态
							DBSourceUtil.setMasterDataSourceType(winning.getDbName());
							returnValue = orderService.updateWinningOrderByOrderNo(WinningOrder.OrderStatus.HAS_PRIZE.getValue(), winning.getWinningAmount(), winning.getOrderNo(), winning.getTableName());
							// 更新中奖结果表中派奖状态
							if(returnValue > 0){
								DBSourceUtil.setMasterDataSourceType(DBSourceUtil.WINNING_RESULT_DBNAME);							
								returnValue = winningService.updateSendPrizeState(winning.getId(), Winning.SendPrizeState.HAS_SENDPRIZE.getValue(), DBSourceUtil.WINNING_RESULT_TABLENAME);										
								ScanLog.scan.debug("sendPrize(lotteryId=%s, issueNo=%s, userId=%s, orderNo=%s, winningTotalMoney=%d",  lotteryId, issueNo, winning.getUserId(), winning.getOrderNo(), winningTotalMoney);
							}
							else{
								throw new DaoLevelException();
							}
						}
					}
					else{
						msg = TransactionProcessor.dynamicInvoke("partner","isPartnerAccount", winning.getPartnerId());
						if(!(Boolean) msg.getObj()){
							//调用更新追号接口
							if(winning.getOrderType() == Winning.OrderType.AFTER_ORDER.getValue()){
								msg = TransactionProcessor.dynamicInvoke("appendTask",
										"updateAppendAfterOrderPrize", winning.getOrderNo(), prizeLevel, winning.getUserId());
								if(!ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
										.equals(msg.getStatusCode())){
									Log.run.error("SendPrizeTask(appendTask),  %s", msg.getMsg());
									throw new DaoLevelException();
								}
							}
						}
					}
				}
			}
			Log.fucaibiz.info("sendPrizeTask(createLotteryTaskComplete): start, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
			LotteryTaskComplete task = new LotteryTaskComplete();
			task.setLotteryId(lotteryId);
			task.setIssueNo(issueNo);
			task.setTaskType(IssueConstant.TASK_COMPLETE_SENDPRIZE);
			task.setSetNo(Configuration.getConfigValue("setNo"));
			
			ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
			ActivemqProducer activemqProducer = ctx.getBean("activemqProducer", ActivemqProducer.class);

			ActivemqSendObject sendObject = new ActivemqSendObject();
			sendObject.setLotteryId(lotteryId);
			sendObject.setIssueNo(issueNo);
			sendObject.setObj(task);
			activemqProducer.send(sendObject, ActivemqMethodUtil.MQ_TASKCOMPLETE_METHODID);
			
			Log.fucaibiz.info("sendPrizeTask(createLotteryTaskComplete): complete, lotteryId=%s, issueNo=%s", lotteryId, issueNo);
		} catch (DaoLevelException e) {
			orderService.updateWinningOrderByOrderNo(WinningOrder.OrderStatus.WAIT_AWARD.getValue(), winning.getWinningAmount(), winning.getOrderNo(), winning.getTableName());
			winningService.updateSendPrizeState(winning.getId(), Winning.SendPrizeState.NO_SENDPRIZE.getValue(), DBSourceUtil.WINNING_RESULT_TABLENAME);
			Log.run.error("", e);
			Log.run.error("sendPrizeTask(lotteryId=%s, issueNo=%s, DaoLevelException=%s)", lotteryId, issueNo, e);
			Log.error("sendPrizeTask(lotteryId=%s, issueNo=%s, DaoLevelException=%s)", lotteryId, issueNo, e);			
		}	
		Log.fucaibiz.info("SendPrizeTask: end, (lotteryId=%s, issueNo=%s)", lotteryId, issueNo);
	}

}
