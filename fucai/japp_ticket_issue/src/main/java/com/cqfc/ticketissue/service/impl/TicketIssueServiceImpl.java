package com.cqfc.ticketissue.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.SportPrint;
import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.ticketissue.FucaiCount;
import com.cqfc.protocol.ticketissue.FucaiPartnerInfo;
import com.cqfc.protocol.ticketissue.OutTicketOrder;
import com.cqfc.protocol.ticketissue.ResultMessage;
import com.cqfc.protocol.ticketissue.TicketIssueService;
import com.cqfc.protocol.ticketissue.UserAccountInfo;
import com.cqfc.ticketissue.task.CheckTicketTask;
import com.cqfc.ticketissue.task.LotteryDrawResultTask;
import com.cqfc.ticketissue.task.LotteryDrawResultTask103;
import com.cqfc.ticketissue.task.LotteryDrawResultTask106;
import com.cqfc.ticketissue.task.LotteryDrawResultTask141;
import com.cqfc.ticketissue.task.QueryTicketTask;
import com.cqfc.ticketissue.task.SendTicketTask;
import com.cqfc.ticketissue.task.TestHttpClientTask;
import com.cqfc.ticketissue.util.TicketUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.FucaiPartnerInfoUtil;
import com.cqfc.util.LotteryType;
import com.cqfc.util.LotteryUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.ReqPrintXmlUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.TicketIssueConstant;
import com.cqfc.xmlparser.TransactionMsgLoader605;
import com.cqfc.xmlparser.TransactionMsgLoader702;
import com.cqfc.xmlparser.TransactionMsgLoader706;
import com.cqfc.xmlparser.transactionmsg702.Levelinfo;
import com.cqfc.xmlparser.transactionmsg702.Querytype;
import com.cqfc.xmlparser.transactionmsg702.Saleinfo;
import com.cqfc.xmlparser.transactionmsg703.Stat;
import com.cqfc.xmlparser.transactionmsg741content.Partnerdatebean;
import com.cqfc.xmlparser.transactionmsg741content.Partnerdatebeans;
import com.jami.util.Log;

@Service
public class TicketIssueServiceImpl implements TicketIssueService.Iface {

	@Autowired
	private ThreadPoolTaskExecutor sendTicketThreadPool;

	@Autowired
	private ThreadPoolTaskExecutor checkTicketThreadPool;

	/**
	 * 请求出票
	 * 
	 * @param outTicketOrder
	 * @return
	 * @throws TException
	 */
	@Override
	public ResultMessage sendTicket(OutTicketOrder outTicketOrder) throws TException {
		ResultMessage message = new ResultMessage();
		String orderNo = "";
		try {
			orderNo = outTicketOrder.getOrderNo();
			boolean isMapExist = TicketUtil.isSendTicketMapExist(orderNo);

			Log.run.debug("sendTicket校验当前订单是否在请求出票MAP,orderNo=%s,isMapExist=%b", orderNo, isMapExist);
			if (!isMapExist) {
				try {
					sendTicketThreadPool.submit(new SendTicketTask(outTicketOrder));
					int poolSize = sendTicketThreadPool.getThreadPoolExecutor().getQueue().size();
					Log.run.debug("sendTicket提交到线程池成功,orderNo=%s,poolSize=%d", orderNo, poolSize);

					message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_OK);
					message.setMsg("success");
				} catch (RejectedExecutionException e) {
					TicketUtil.sendTicketMap.remove(orderNo);
					Log.run.error("出票请求线程池已满,orderNo=%s, exception=%s", orderNo, e);
					Log.error("出票请求线程池已满,orderNo=%s, exception=%s", orderNo, e);
					message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_ERROR);
					message.setMsg("threadPool full");
					return message;
				} catch (Exception e) {
					TicketUtil.sendTicketMap.remove(orderNo);
					Log.run.error("出票请求submit线程池发生异常,orderNo=%s, exception=%s", orderNo, e);
					Log.error("出票请求submit线程池发生异常,orderNo=%s, exception=%s", orderNo, e);
					message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_ERROR);
					message.setMsg("submit threadPool error");
					return message;
				}
			} else {
				message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_ERROR);
				message.setMsg("checkTicket is exist");
			}
		} catch (Exception e) {
			Log.run.error("请求出票发生异常,orderNo=%s, exception=%s", orderNo, e);
			Log.error("请求出票发生异常,orderNo=%s, exception=%s", orderNo, e);
		}
		return message;
	}

	/**
	 * 查询出票（异步）
	 * 
	 * @param outTicketOrder
	 * @return
	 * @throws TException
	 */
	@Override
	public ResultMessage checkTicket(OutTicketOrder outTicketOrder) throws TException {
		ResultMessage message = new ResultMessage();
		String orderNo = "";
		try {
			orderNo = outTicketOrder.getOrderNo();

			boolean isMapExist = TicketUtil.isCheckTicketMapExist(orderNo);

			Log.run.debug("checkTicket校验当前订单是否在查询出票MAP,orderNo=%s,isMapExist=%b", orderNo, isMapExist);
			if (!isMapExist) {
				String issueNo = LotteryUtil.convertIssueNo(outTicketOrder.getLotteryId(), outTicketOrder.getIssueNo());
				Log.run.debug("checkTicket查询出票期次,orderNo=%s,issueNo=%s", orderNo, issueNo);
				try {
					checkTicketThreadPool.submit(new CheckTicketTask(outTicketOrder));
					int poolSize = checkTicketThreadPool.getThreadPoolExecutor().getQueue().size();
					Log.run.debug("checkTicket提交到线程池成功,orderNo=%s,poolSize=%d", orderNo, poolSize);

					message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_OK);
					message.setMsg("success");
				} catch (RejectedExecutionException e) {
					TicketUtil.checkTicketMap.remove(orderNo);
					Log.run.error("出票查询线程池已满,orderNo=" + orderNo, e);
					Log.error("出票查询线程池已满,orderNo=" + orderNo, e);
					message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_ERROR);
					message.setMsg("threadPool full");
				} catch (Exception e) {
					TicketUtil.checkTicketMap.remove(orderNo);
					Log.run.error("出票查询submit线程池发生异常,orderNo=" + orderNo, e);
					Log.error("出票查询submit线程池发生异常,orderNo=" + orderNo, e);
					message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_ERROR);
					message.setMsg("submit threadPool error");
				}
			} else {
				message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_ERROR);
				message.setMsg("checkTicket is exist");
			}
		} catch (Exception e) {
			Log.run.error("查询出票发生异常,orderNo=%s, exception=%s", orderNo, e);
			Log.error("查询出票发生异常,orderNo=%s, exception=%s", orderNo, e);
		}
		return message;
	}

	/**
	 * 获取开奖公告信息
	 */
	@Override
	public LotteryDrawResult findLotteryDrawResult(String lotteryId, String issueNo) throws TException {
		Log.run.debug("findLotteryDrawResult request,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
		LotteryDrawResult result = null;
		try {
			// 获取开奖公告
			String xmlStr = "";
			if(lotteryId.equals(LotteryType.SSQ.getText()) || lotteryId.equals(LotteryType.QLC.getText())
				|| lotteryId.equals(LotteryType.FC3D.getText()) || lotteryId.equals(LotteryType.SSC.getText())
				|| lotteryId.equals(LotteryType.XYNC.getText())){
				xmlStr = LotteryDrawResultTask.findLotteryDrawResult(lotteryId, issueNo);
			}
//			else if(lotteryId.equals(LotteryType.ZJSYXW.getText())){
//				xmlStr = ZJSYXWSnatchDrawResultTask.findLotteryDrawResult(issueNo);
//			}
//			else{
//				xmlStr = TCLotteryDrawResultTask.snatchLotteryDrawResult(lotteryId, issueNo);
//			}
			
			if(xmlStr == null || xmlStr.equals("")){
				return result;
			}
			com.cqfc.xmlparser.transactionmsg702.Msg msg702 = TransactionMsgLoader702.xmlToMsg(xmlStr);
			// 返回结果判断
			if (TicketIssueConstant.TRANSCODE702.equals(msg702.getHead().getTranscode())) {
				result = new LotteryDrawResult();
				Querytype prizeInfo = msg702.getBody().getPrizeinfo();
				String prizeBall = prizeInfo.getPrizeball();
				if (prizeBall == null || prizeBall.equals("")) {
					return null;
				}
				result.setLotteryId(lotteryId);
				result.setIssueNo(issueNo);
				result.setDrawResult(prizeBall);
				// 幸运农场奖池处理
				result.setPrizePool(!LotteryType.XYNC.getText().equals(lotteryId) ? MoneyUtil.toCent(Long
						.valueOf(prizeInfo.getPrizepool())) : 0);
				result.setState(Integer.valueOf(prizeInfo.getStatus()));
				List<Levelinfo> levelInfos = msg702.getBody().getPrizeinfo().getLevelinfos().getLevelinfo();
				if (levelInfos == null || levelInfos.size() == 0) {
					return null;
				}
				List<LotteryDrawLevel> lotteryDrawList = new ArrayList<LotteryDrawLevel>();
				LotteryDrawLevel lotteryDraw = null;
				for (Levelinfo info : levelInfos) {
					lotteryDraw = new LotteryDrawLevel();
					lotteryDraw.setLotteryId(lotteryId);
					lotteryDraw.setIssueNo(issueNo);
					lotteryDraw.setLevel(Integer.valueOf(info.getLevel()));
					lotteryDraw.setMoney(MoneyUtil.toCent(Long.valueOf(info.getMoney())));
					lotteryDraw.setLevelName(info.getName());
					lotteryDraw.setTotalCount(Long.valueOf(info.getCount()));
					lotteryDrawList.add(lotteryDraw);
				}
				result.setLotteryDrawLevelList(lotteryDrawList);
				List<Saleinfo> saleinfo = msg702.getBody().getPrizeinfo().getSaleinfos().getSaleinfo();
				if (saleinfo == null || saleinfo.size() == 0) {
					result.setSalesVolume(0);
				}
				for (Saleinfo sale : saleinfo) {
					if(sale.getTypename().equals("cq")){
						result.setSalesVolume(Long.valueOf(sale.getMoney()) * 100);
						break;
					}
				}
				Log.run.debug("findLotteryDrawResult response success,lotteryId=%s,issueNo=%s,drawResult=%s",
						lotteryId, issueNo, prizeBall);
			} else {
				if (TicketIssueConstant.TRANSCODE000.equals(msg702.getHead().getTranscode())) {
					if (xmlStr.contains(ConstantsUtil.STATUS_CODE_NO_WINNINGMSG)) {
						Log.run.debug("findLotteryDrawResult info,lotteryId=%s,issueNo=%s,xml=%s", lotteryId, issueNo,
								xmlStr);
					} else {
						Log.run.error("findLotteryDrawResult error,lotteryId=%s,issueNo=%s,xml=%s", lotteryId, issueNo,
								xmlStr);
					}
				}
			}
		} catch (Exception e) {
			Log.run.error("获取开奖公告信息发生异常,lotteryId=%s,issueNo=%s,exception=%s", lotteryId, issueNo, e);
			Log.error("获取开奖公告信息发生异常,lotteryId=%s,issueNo=%s,exception=%s", lotteryId, issueNo, e);
		}
		return result;
	}

	@Override
	public FucaiCount getFucaiCount(String lotteryId, String issueNo) throws TException {
		FucaiCount count = new FucaiCount();
		count.setLotteryId(lotteryId);
		count.setIssueNo(issueNo);
		try {
			List<FucaiPartnerInfo> partnerInfoList = FucaiPartnerInfoUtil.getFucaiPartnerInfoList();
			for (FucaiPartnerInfo partnerInfo : partnerInfoList) {
				com.cqfc.xmlparser.transactionmsg703.Querytype statinfo = null;
				if (partnerInfo.getPartnerId() != null && !partnerInfo.getPartnerId().equals(ConstantsUtil.TEST_PARTNERID)) {
					statinfo = LotteryDrawResultTask103.getFucaiCount(
							lotteryId, issueNo, partnerInfo);					
				
					if (statinfo == null) {
						count.setTotalBuy(ServiceStatusCodeUtil.STATUS_LIST_IS_NULL);
						break;
					} else {
						count.setTotalWinning(count.getTotalWinning() + transferFucaiMoney(statinfo.getTotalbouns()));
						List<Stat> statList = statinfo.getStats().getStat();
						int totalSucNum = 0;
						long totalBuy = 0;
						for (Stat stat : statList) {
							totalSucNum += Integer.parseInt(stat.getSucnum());
							totalBuy += transferFucaiMoney(stat.getSucmoney());
						}
						count.setTotalBuy(count.getTotalBuy() + totalBuy);
						count.setTicketNum(count.getTicketNum() + totalSucNum);
					}
				}
			}
			Log.run.debug("103统计,lotteryId=%s,issueNo=%s,totalWinningPrize=%d,totalBuy=%d,totalTicketNum=%d",
					lotteryId, issueNo, count.getTotalWinning(), count.getTotalBuy(), count.getTicketNum());
		} catch (Exception e) {
			Log.run.error("103统计发生异常,lotteryId=%s,issueNo=%s,errorMsg=%s", lotteryId, issueNo, e.toString());
		}

		return count;
	}

	private long transferFucaiMoney(String money) {
		String[] split = money.split("\\.");
		long result = Long.parseLong(split[0]) * 100;
		if (split.length > 1) {
			if (split[1].length() < 2) {
				split[1] += "0";
			} else if (split[1].length() > 2) {
				split[1] = split[1].substring(0, 2);
			}
			result += Integer.parseInt(split[1]);
		}
		return result;
	}

	@Override
	public FucaiCount getFucaiCountByDay(String date) throws TException {
		FucaiCount count = new FucaiCount();
		count.setLotteryId(date);
		try {
			List<FucaiPartnerInfo> partnerInfoList = FucaiPartnerInfoUtil.getFucaiPartnerInfoList();
			for (FucaiPartnerInfo partnerInfo : partnerInfoList) {
				if (partnerInfo.getPartnerId() != null && !partnerInfo.getPartnerId().equals(ConstantsUtil.TEST_PARTNERID)) {
					Partnerdatebeans statinfo = LotteryDrawResultTask141.getFucaiCount(date, partnerInfo);
					if (statinfo != null) {
						long saleCount = 0, prizeCount = 0;
						List<Partnerdatebean> list = statinfo.getPartnerdatebean();
						if (list.isEmpty()) {
							count.setTotalBuy(ServiceStatusCodeUtil.STATUS_LIST_IS_NULL);
						} else {
							for (Partnerdatebean bean : list) {
								saleCount += transferFucaiMoney(bean.getSaleAccount());
								prizeCount += transferFucaiMoney(bean.getPrizeAccount());
							}
							count.setTotalBuy(saleCount);
							count.setTotalWinning(prizeCount);
						}
					}
				}
			}
			Log.run.debug("141统计,date=%s,totalWinningPrize=%d,totalBuy=%d,totalTicketNum=%d", date,
					count.getTotalWinning(), count.getTotalBuy(), count.getTicketNum());
		} catch (Exception e) {
			Log.run.error("141统计发生异常,date=%s,errorMsg=%s", date, e.toString());
		}
		return count;
	}

	/**
	 * 出票查询（同步）
	 */
	@Override
	public ResultMessage queryTicket(OutTicketOrder outTicketOrder) throws TException {
		ResultMessage message = null;
		try {
			String orderNo = outTicketOrder.getOrderNo();
			String xmlStr = "";
			String statusCode = "";

			xmlStr = QueryTicketTask.queryTicketChuPiao(outTicketOrder);
			Log.run.debug("cancel order queryTicket ticket,orderNo=%s, msg=%s", orderNo, xmlStr);
			int lotteryType = OrderUtil.getLotteryCategory(outTicketOrder.getLotteryId());
			if (null != xmlStr && !"".equals(xmlStr)) {
				com.cqfc.xmlparser.transactionmsg605.Msg msg605 = TransactionMsgLoader605.xmlToMsg(xmlStr);
				message = new ResultMessage();

				if (TicketIssueConstant.TRANSCODE605.equals(msg605.getHead().getTranscode())) {
					statusCode = msg605.getBody().getTicketresult().getStatuscode();
					if (statusCode.equals(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS)) {
						message.setStatusCode(TicketIssueConstant.SEND_TICKET_RESULT_SUCCESS);
						message.setMsg("success");
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_RRADE_FAIL)) {
						message.setStatusCode(TicketIssueConstant.SEND_TICKET_RESULT_FAIL);
						message.setMsg("fail");
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_WAIT_TRADE)) {
						message.setStatusCode(TicketIssueConstant.SEND_TICKET_RESULT_PROCESSING);
						message.setMsg("processing");
					} else if (statusCode.equals(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST)) {
						message.setStatusCode(TicketIssueConstant.SEND_TICKET_RESULT_NOEXIST_ORDER);
						message.setMsg("605 order not exist");
					} else {
						Log.run.error("cancel order queryTicket,xml", xmlStr);
					}
					if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
						SportPrint sportResult = OrderUtil.convertMsg2result(msg605, message.getStatusCode());
						message.setSportPrint(sportResult);
					} 
				} else {
					if (TicketIssueConstant.TRANSCODE000.equals(msg605.getHead().getTranscode())) {
						if (xmlStr.contains(ConstantsUtil.STATUS_CODE_ORDER_NOTEXIST)) {
							message.setStatusCode(TicketIssueConstant.SEND_TICKET_RESULT_NOEXIST_ORDER);
							message.setMsg("000 cancel order queryTicket not exist");
							if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
								SportPrint sportResult = new SportPrint();
								sportResult.setPrintStatus(message.getStatusCode());
								message.setSportPrint(sportResult);
							} 
						}
					}
				}
			} else {
				message = new ResultMessage();
				message.setStatusCode(TicketIssueConstant.SEND_TICKET_RESULT_PROCESSING);
				message.setMsg("checkTicket is null");
				if (lotteryType == OrderStatus.LotteryType.SPORTS_GAME.getType()) {
					SportPrint sportResult = new SportPrint();
					sportResult.setPrintStatus(message.getStatusCode());
					message.setSportPrint(sportResult);
				} 
			}
			Log.run.debug("cancel order queryTicket response,orderNo=%s,statusCode=%s,msg=%s", orderNo, statusCode,
					message.getMsg());
		} catch (Exception e) {
			Log.run.error("撤单订单查询出票发生异常", e);
		}
		return message;
	}

	@Override
	public UserAccountInfo getUserAccountInfo(String parmType, String value) throws TException {
		UserAccountInfo userInfo = new UserAccountInfo();

		String xmlStr = LotteryDrawResultTask106.findUserInfo106(parmType, value);
		if (null != xmlStr && !"".equals(xmlStr)) {
			com.cqfc.xmlparser.transactionmsg706.Msg msg706 = TransactionMsgLoader706.xmlToMsg(xmlStr);

			userInfo.setUserId(Long.valueOf(msg706.getBody().getUser().getUserid()));
			userInfo.setIdCardNo(msg706.getBody().getUser().getIdcardno());
			userInfo.setPhone(msg706.getBody().getUser().getPhone());
			userInfo.setPuserkey(msg706.getBody().getUser().getPuserkey());
			userInfo.setRealName(msg706.getBody().getUser().getRealname());
			userInfo.setStatus(msg706.getBody().getUser().getStatus());
			userInfo.setCash((long) (Float.valueOf(msg706.getBody().getUser().getAccount().getCash()) * 1));
			userInfo.setGiftcash((long) (Float.valueOf(msg706.getBody().getUser().getAccount().getGiftcash()) * 1));
			if (msg706.getBody().getUser().getAccount().getForzencash() == null
					|| msg706.getBody().getUser().getAccount().getForzencash().equals("")) {
				userInfo.setForzencash(0L);
			} else {
				userInfo.setForzencash((long) (Float.valueOf(msg706.getBody().getUser().getAccount().getForzencash()) * 100));
			}
		}
		return userInfo;
	}

	/**
	 * 获取福彩合作商
	 */
	@Override
	public List<FucaiPartnerInfo> getFucaiPartnerInfoList() throws TException {

		return FucaiPartnerInfoUtil.getFucaiPartnerInfoList();
	}

	@Override
	public ResultMessage testHttpRequest(String queryString) throws TException {
		ResultMessage message = new ResultMessage();
		
		try {			
			sendTicketThreadPool.submit(new TestHttpClientTask());
			int poolSize = sendTicketThreadPool.getThreadPoolExecutor().getQueue().size();
			Log.run.debug("sendTicket提交到线程池成功,poolSize=%d", poolSize);

			message.setStatusCode(TicketIssueConstant.STATUS_SEND_TICKET_OK);
			message.setMsg("success");
			
		} catch (Exception e) {
			Log.run.error("请求出票发生异常, exception=%s", e);
			Log.error("请求出票发生异常, exception=%s", e);
		}
		return message;
	}

	@Override
	public String pushDrawResult(String lotteryId, String issueNo, String msg)
			throws TException {
		Log.run.debug("pushDrawResult response,lotteryId=%s,issueNo=%s,msg=%s", lotteryId, issueNo, msg);
		LotteryDrawResult result = null;
		try {			
			com.cqfc.xmlparser.transactionmsg702.Msg msg702 = TransactionMsgLoader702.xmlToMsg(msg);
			// 返回结果判断
			if (TicketIssueConstant.TRANSCODE702.equals(msg702.getHead().getTranscode())) {
				result = new LotteryDrawResult();
				Querytype prizeInfo = msg702.getBody().getPrizeinfo();
				String prizeBall = prizeInfo.getPrizeball();
				if (prizeBall == null || prizeBall.equals("")) {
					return null;
				}
				result.setLotteryId(lotteryId);
				result.setIssueNo(issueNo);
				result.setDrawResult(prizeBall);
				// 幸运农场奖池处理
				result.setPrizePool(!LotteryType.XYNC.getText().equals(lotteryId) ? MoneyUtil.toCent(Long
						.valueOf(prizeInfo.getPrizepool())) : 0);
				result.setState(Integer.valueOf(prizeInfo.getStatus()));
				List<Levelinfo> levelInfos = msg702.getBody().getPrizeinfo().getLevelinfos().getLevelinfo();
				if (levelInfos == null || levelInfos.size() == 0) {
					return null;
				}
				List<LotteryDrawLevel> lotteryDrawList = new ArrayList<LotteryDrawLevel>();
				LotteryDrawLevel lotteryDraw = null;
				for (Levelinfo info : levelInfos) {
					lotteryDraw = new LotteryDrawLevel();
					lotteryDraw.setLotteryId(lotteryId);
					lotteryDraw.setIssueNo(issueNo);
					lotteryDraw.setLevel(Integer.valueOf(info.getLevel()));
					lotteryDraw.setMoney(MoneyUtil.toCent(Long.valueOf(info.getMoney())));
					lotteryDraw.setLevelName(info.getName());
					lotteryDraw.setTotalCount(Long.valueOf(info.getCount()));
					lotteryDrawList.add(lotteryDraw);
				}
				result.setLotteryDrawLevelList(lotteryDrawList);
				List<Saleinfo> saleinfo = msg702.getBody().getPrizeinfo().getSaleinfos().getSaleinfo();
				if (saleinfo == null || saleinfo.size() == 0) {
					result.setSalesVolume(0);
				}
				for (Saleinfo sale : saleinfo) {
					if(sale.getTypename().equals("cq")){
						result.setSalesVolume(Long.valueOf(sale.getMoney()) * 100);
						break;
					}
				}
				Log.run.debug("pushDrawResult response success,lotteryId=%s,issueNo=%s,drawResult=%s",
						lotteryId, issueNo, prizeBall);
			} else {
				if (TicketIssueConstant.TRANSCODE000.equals(msg702.getHead().getTranscode())) {
					if (msg.contains(ConstantsUtil.STATUS_CODE_NO_WINNINGMSG)) {
						Log.run.debug("pushDrawResult info,lotteryId=%s,issueNo=%s,xml=%s", lotteryId, issueNo,
								msg);
					} else {
						Log.run.error("pushDrawResult error,lotteryId=%s,issueNo=%s,xml=%s", lotteryId, issueNo,
								msg);
					}
				}
			}
			ReturnMessage retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryDrawResult", lotteryId, issueNo);
			if(retMsg.getObj() == null){
				retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue", "createLotteryDrawResult", result);
				if(retMsg != null && retMsg.getObj() != null){
					if((Integer)retMsg.getObj() < 0){
						Log.run.error("推送开奖公告信息发生错误,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
					}
					else{
						Log.run.debug("推送开奖公告信息发生成功,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
					}
				}
			}
		} catch (Exception e) {
			Log.run.error("推送开奖公告信息发生异常,lotteryId=%s,issueNo=%s,exception=%s", lotteryId, issueNo, e);
			Log.error("推送开奖公告信息发生异常,lotteryId=%s,issueNo=%s,exception=%s", lotteryId, issueNo, e);
		}
		return "";
	}

}
