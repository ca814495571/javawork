package com.cqfc.management.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.model.CancelOrderInfo;
import com.cqfc.management.model.Issue;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.PcWinPrizeCheck;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.model.WinPrizeCheck;
import com.cqfc.management.service.ILotteryService;
import com.cqfc.management.service.IPartnerService;
import com.cqfc.management.util.ReturnCodeConstansUtil;
import com.cqfc.management.util.dateUtils.DateUtils;
import com.cqfc.management.util.dateUtils.OrderUtils;
import com.cqfc.management.util.scheduledTask.ScheduledTask;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.cancelorder.CancelOrder;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveData;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.protocol.lotteryissue.MatchFootballDate;
import com.cqfc.protocol.lotteryissue.ReturnData;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.PcPartnerOrder;
import com.cqfc.protocol.ticketwinning.WinningAmountStat;
import com.cqfc.protocol.ticketwinning.WinningAmountStatData;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.userorder.PcUserOrder;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderUtil;
import com.jami.util.Log;

@Service
public class LotteryServiceImpl implements ILotteryService {

	@Autowired
	IPartnerService partnerService;

	/**
	 * 根据状态查询彩种期号列表（13 已算奖待审核 14已算奖审核中 15算奖已审核）
	 * 
	 * @param status
	 * @return
	 */
	public List<LotteryIssue> getIssueByStatus(int status) {

		LotteryIssue lotteryIssue = new LotteryIssue();
		List<LotteryIssue> lotteryIssues = new ArrayList<LotteryIssue>();
		lotteryIssue.setState(status);
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "getLotteryIssueByParam",
						lotteryIssue);

		if (reMsg.getObj() != null) {

			lotteryIssues = (List<LotteryIssue>) reMsg.getObj();
		}

		return lotteryIssues;
	}

	@Override
	public PcResultObj updateIssueStatus(int issueId, int status) {

		PcResultObj pcResultObj = new PcResultObj();
		int flag = 0;
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "updateLotteryIssueState",
						issueId, status);

		if (reMsg.getObj() != null) {

			flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				pcResultObj.setMsg("审核成功");
				pcResultObj.setMsgCode("1");

				return pcResultObj;
			}

		}

		pcResultObj.setMsg("审核失败");
		pcResultObj.setMsgCode("2");

		return pcResultObj;
	}

	@Override
	public PcResultObj getWinningCheck(WinPrizeCheck winPrizeCheck,
			int pageNum, int pageSize) {

		ResultObj resultObj = new ResultObj();

		PcResultObj pcResultObj = new PcResultObj();

		Integer status = winPrizeCheck.getStatus();

		WinningAmountStat winningSum = null;

		List<WinningAmountStat> winningSums = new ArrayList<WinningAmountStat>();

		WinningAmountStatData winningSumData = new WinningAmountStatData();

		LotteryIssue lotteryIssue = new LotteryIssue();
		// 彩种期号审核的记录,里面包含各个合作商中奖详情
		Map<String, PcWinPrizeCheck> pcWinPrizeChecksMap = new HashMap<String, PcWinPrizeCheck>();

		// 返回的json数据
		List<PcWinPrizeCheck> pcwinPrizeChecks = new ArrayList<PcWinPrizeCheck>();

		List<Object> winPrizeChecks = null;
		Map<String, WinPrizeCheck> tempWinPrizeChecks = null;
		String key = "";
		PcWinPrizeCheck pcWinPrizeCheck = null;

		if (status == null) {
			// 1中奖审核未审核13
			status = 1;
		}

		lotteryIssue.setLotteryId(winPrizeCheck.getLotteryId());
		lotteryIssue.setState(status);
		lotteryIssue.setIssueNo(winPrizeCheck.getIssueNo());
		com.cqfc.processor.ReturnMessage returnMessage = null;
		if(status == 1){
			returnMessage = TransactionProcessor
					.dynamicInvoke("lotteryIssue", "getLotteryIssueByParam",
							lotteryIssue,1,100000);
		}
		if(status == 2){
			returnMessage = TransactionProcessor
					.dynamicInvoke("lotteryIssue", "getLotteryIssueByParam",
							lotteryIssue,pageNum,pageSize);
		}
		
		ReturnData returnData = (ReturnData) returnMessage.getObj();
		List<LotteryIssue> lotteryIssues = returnData.getResultList();
		
		if (lotteryIssues != null && lotteryIssues.size() > 0) {

			
			com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
					.dynamicInvoke("partner", "getLotteryPartnerList",
							new LotteryPartner(), 1, 10000);
			List<LotteryPartner> partners = new ArrayList<LotteryPartner>();
			if (reMsg.getObj() != null) {
				com.cqfc.protocol.partner.ReturnData re = (com.cqfc.protocol.partner.ReturnData) reMsg
						.getObj();
				partners = re.getResultList();
				
			}
			
			for (int i = 0; i < lotteryIssues.size(); i++) {

				lotteryIssue = lotteryIssues.get(i);

				winningSum = new WinningAmountStat();
				winningSum.setIssueNo(lotteryIssue.getIssueNo());
				winningSum.setLotteryId(lotteryIssue.getLotteryId());
				winningSum.setPartnerId(winPrizeCheck.getPartnerId());

				pcWinPrizeCheck = new PcWinPrizeCheck();
				pcWinPrizeCheck.setPrizeResult(lotteryIssue.getDrawResult());
				tempWinPrizeChecks = new HashMap<String, WinPrizeCheck>();

				initTempWinPrizeChecks(partners,lotteryIssue.getLotteryId(),
						lotteryIssue.getIssueNo(), tempWinPrizeChecks, status);
				winPrizeChecks = new ArrayList<Object>();
				winPrizeChecks.add(tempWinPrizeChecks);
				pcWinPrizeCheck.setIssueNo(lotteryIssue.getIssueNo());
				pcWinPrizeCheck.setLotteryId(lotteryIssue.getLotteryId());
				pcWinPrizeCheck.setWinPrizeChecks(winPrizeChecks);
				pcWinPrizeCheck.setStatus(status);
				pcWinPrizeChecksMap.put(lotteryIssue.getLotteryId()
						+ lotteryIssue.getIssueNo(), pcWinPrizeCheck);

				winningSums.add(winningSum);
			}

			returnMessage = TransactionProcessor.dynamicInvoke("ticketWinning",
					"getWinningAmountStat", winningSums, 1, 100000);

			if (returnMessage.getObj() != null) {

				winningSumData = (WinningAmountStatData) returnMessage.getObj();

				winningSums = winningSumData.getResultList();

				for (int i = 0; i < winningSums.size(); i++) {

					winningSum = winningSums.get(i);

					if (winningSum.getPartnerId() == null) {
						continue;
					}

					key = winningSum.getPartnerId() + winningSum.getLotteryId()
							+ winningSum.getIssueNo();
					tempWinPrizeChecks = (Map) (pcWinPrizeChecksMap
							.get(winningSum.getLotteryId()
									+ winningSum.getIssueNo())
							.getWinPrizeChecks().get(0));

					if (tempWinPrizeChecks.containsKey(key)) {
						tempWinPrizeChecks.get(key).setTotalMoney(
								MoneyUtil.toYuanStr(winningSum.getSum()));
					}
				}
			}

			Iterator<String> iterator = pcWinPrizeChecksMap.keySet().iterator();
			String key_ = "";

			Map<String, WinPrizeCheck> map = new HashMap<String, WinPrizeCheck>();
			List<Object> objects = new ArrayList<Object>();
			while (iterator.hasNext()) {

				key_ = iterator.next();
				objects = pcWinPrizeChecksMap.get(key_).getWinPrizeChecks();
				map = (Map) objects.get(0);

				Set<Entry<String, WinPrizeCheck>> entries = map.entrySet();
				for (Entry<String, WinPrizeCheck> entry : entries) {

					objects.add(entry.getValue());
				}
				objects.remove(0);
				pcwinPrizeChecks.add(pcWinPrizeChecksMap.get(key_));
			}
			sort(pcwinPrizeChecks);
			resultObj.setObjects(pcwinPrizeChecks);
			resultObj.setRecordTotal(returnData.getTotalSize());

			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode("1");
			return pcResultObj;

		} else {
			resultObj.setObjects(pcwinPrizeChecks);
			resultObj.setRecordTotal(0);
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询无记录");
			pcResultObj.setMsgCode("1");
			return pcResultObj;
		}

	}

	@Override
	public PcResultObj getAwardCheck(WinPrizeCheck winPrizeCheck, int pageNum,
			int pageSize) {

		ResultObj resultObj = new ResultObj();

		PcResultObj pcResultObj = new PcResultObj();

		Integer status = winPrizeCheck.getStatus();

		WinningAmountStat winningSum = null;

		List<WinningAmountStat> winningSums = new ArrayList<WinningAmountStat>();

		WinningAmountStatData winningSumData = new WinningAmountStatData();


		LotteryIssue lotteryIssue = new LotteryIssue();

		Map<String, PcWinPrizeCheck> pcWinPrizeChecksMap = new HashMap<String, PcWinPrizeCheck>();

		List<PcWinPrizeCheck> pcwinPrizeChecks = new ArrayList<PcWinPrizeCheck>();
		Map<String, WinPrizeCheck> tempWinPrizeChecks = null;

		List<Object> winPrizeChecks = null;
		String key = "";
		PcWinPrizeCheck pcWinPrizeCheck = null;
		if (status == null) {
			// 3： 派奖审核未审核 15
			status = 3;
		}

		lotteryIssue.setLotteryId(winPrizeCheck.getLotteryId());
		lotteryIssue.setState(status);
		lotteryIssue.setIssueNo(winPrizeCheck.getIssueNo());

		com.cqfc.processor.ReturnMessage returnMessage = null;
		if(status == 3){
			 returnMessage = TransactionProcessor
						.dynamicInvoke("lotteryIssue", "getLotteryIssueByParam",
								lotteryIssue,1,100000);
		}
		if(status == 4){
			 returnMessage = TransactionProcessor
						.dynamicInvoke("lotteryIssue", "getLotteryIssueByParam",
								lotteryIssue,pageNum,pageSize);
		}
		
		ReturnData returnData = (ReturnData) returnMessage.getObj();
		List<LotteryIssue> lotteryIssues = returnData.getResultList();
		if (lotteryIssues != null && lotteryIssues.size() > 0) {

			
			com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
					.dynamicInvoke("partner", "getLotteryPartnerList",
							new LotteryPartner(), 1, 10000);
			List<LotteryPartner> partners = new ArrayList<LotteryPartner>();
			if (reMsg.getObj() != null) {
				com.cqfc.protocol.partner.ReturnData re = (com.cqfc.protocol.partner.ReturnData) reMsg
						.getObj();
				partners = re.getResultList();
				
			}
			for (int i = 0; i < lotteryIssues.size(); i++) {

				lotteryIssue = lotteryIssues.get(i);

				winningSum = new WinningAmountStat();
				winningSum.setIssueNo(lotteryIssue.getIssueNo());
				winningSum.setLotteryId(lotteryIssue.getLotteryId());
				winningSum.setPartnerId(winPrizeCheck.getPartnerId());

				pcWinPrizeCheck = new PcWinPrizeCheck();
				pcWinPrizeCheck.setPrizeResult(lotteryIssue.getDrawResult());
				tempWinPrizeChecks = new HashMap<String, WinPrizeCheck>();
				winPrizeChecks = new ArrayList<Object>();
				winPrizeChecks.add(tempWinPrizeChecks);
				initTempWinPrizeChecks(partners,lotteryIssue.getLotteryId(),
						lotteryIssue.getIssueNo(), tempWinPrizeChecks, status);
				pcWinPrizeCheck.setIssueNo(lotteryIssue.getIssueNo());
				pcWinPrizeCheck.setLotteryId(lotteryIssue.getLotteryId());
				pcWinPrizeCheck.setWinPrizeChecks(winPrizeChecks);
				pcWinPrizeCheck.setStatus(status);
				pcWinPrizeChecksMap.put(lotteryIssue.getLotteryId()
						+ lotteryIssue.getIssueNo(), pcWinPrizeCheck);

				winningSums.add(winningSum);
			}

			returnMessage = TransactionProcessor.dynamicInvoke("ticketWinning",
					"getWinningAmountStat", winningSums, 1, 100000);

			if (returnMessage.getObj() != null) {

				winningSumData = (WinningAmountStatData) returnMessage.getObj();

				winningSums = winningSumData.getResultList();

				for (int i = 0; i < winningSums.size(); i++) {

					winningSum = winningSums.get(i);

					if (winningSum.getPartnerId() == null) {
						continue;
					}

					key = winningSum.getPartnerId() + winningSum.getLotteryId()
							+ winningSum.getIssueNo();
					tempWinPrizeChecks = (Map) (pcWinPrizeChecksMap
							.get(winningSum.getLotteryId()
									+ winningSum.getIssueNo())
							.getWinPrizeChecks().get(0));

					if (tempWinPrizeChecks.containsKey(key)) {
						tempWinPrizeChecks.get(key).setTotalMoney(
								MoneyUtil.toYuanStr(winningSum.getSum()));
					}
				}
			}

			Iterator<String> iterator = pcWinPrizeChecksMap.keySet().iterator();
			String key_ = "";
			Map<String, WinPrizeCheck> map = new HashMap<String, WinPrizeCheck>();
			List<Object> objects = new ArrayList<Object>();
			while (iterator.hasNext()) {

				key_ = iterator.next();
				objects = pcWinPrizeChecksMap.get(key_).getWinPrizeChecks();
				map = (Map) objects.get(0);

				Set<Entry<String, WinPrizeCheck>> entries = map.entrySet();
				for (Entry<String, WinPrizeCheck> entry : entries) {

					objects.add(entry.getValue());
				}
				objects.remove(0);
				pcwinPrizeChecks.add(pcWinPrizeChecksMap.get(key_));
			}
			sort(pcwinPrizeChecks);
			resultObj.setObjects(pcwinPrizeChecks);
			resultObj.setRecordTotal(returnData.getTotalSize());

			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode("1");
			return pcResultObj;

		} else {
			resultObj.setObjects(pcwinPrizeChecks);
			resultObj.setRecordTotal(0);
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询无记录");
			pcResultObj.setMsgCode("1");
			return pcResultObj;
		}

	}

	@Override
	public PcResultObj checkWinPrize(WinPrizeCheck winPrizeCheck) {

		PcResultObj pcResultObj = new PcResultObj();
		int flag = 0;

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "findLotteryIssue",
						winPrizeCheck.getLotteryId(),
						winPrizeCheck.getIssueNo());

		if (reMsg.getObj() != null) {

			LotteryIssue lotteryIssue = (LotteryIssue) reMsg.getObj();
			if (IssueConstant.ISSUE_STATUS_HASCAL_WAITAUDIT != lotteryIssue
					.getState()) {

				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("彩种该期号状态不是待审核,期号状态:"+lotteryIssue
						.getState());
				return pcResultObj;
			}
		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("不存在该彩种的期号");
			return pcResultObj;
		}

		reMsg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"updateLotteryIssueStateByParam", winPrizeCheck.getLotteryId(),
				winPrizeCheck.getIssueNo(),
				IssueConstant.ISSUE_STATUS_AUDITTING);

		if (reMsg.getObj() != null) {

			flag = (Integer) reMsg.getObj();
		}

		if (flag == 1) {
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("审核成功");
			return pcResultObj;
		}

		pcResultObj.setMsgCode("2");
		pcResultObj.setMsg("审核失败");
		return pcResultObj;
	}

	@Override
	public PcResultObj checkAwardPrize(WinPrizeCheck winPrizeCheck) {

		PcResultObj pcResultObj = new PcResultObj();
		int flag = 0;
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "findLotteryIssue",
						winPrizeCheck.getLotteryId(),
						winPrizeCheck.getIssueNo());
		if (reMsg.getObj() != null) {

			LotteryIssue lotteryIssue = (LotteryIssue) reMsg.getObj();
			if (IssueConstant.ISSUE_STATUS_CAN_SEND != lotteryIssue.getState()) {

				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("彩种该期号状态不是待审核");
				return pcResultObj;
			}
		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("不存在该彩种的期号");
			return pcResultObj;
		}
		reMsg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"updateLotteryIssueStateByParam", winPrizeCheck.getLotteryId(),
				winPrizeCheck.getIssueNo(), IssueConstant.ISSUE_STATUS_SENDING);

		if (reMsg.getObj() != null) {

			flag = (Integer) reMsg.getObj();
		}

		if (flag == 1) {

			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("审核成功");
			return pcResultObj;
		}

		pcResultObj.setMsgCode("2");
		pcResultObj.setMsg("审核失败");
		return pcResultObj;
	}

	@Override
	public PcResultObj getOrderByWhere(OrderInfo orderInfo,int pageNum ,int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage reMsg = new ReturnMessage();
		com.cqfc.protocol.partnerorder.Order partnerOrder = new Order();
		com.cqfc.protocol.userorder.Order userOrder = new com.cqfc.protocol.userorder.Order();
		try {
			
			if (StringUtils.isBlank(orderInfo.getUserId())) {
			
				partnerOrder.setTradeId(orderInfo.getTicketId());
				if(StringUtils.isNotBlank(orderInfo.getPartnerId())){
					partnerOrder.setPartnerId(orderInfo.getPartnerId());
				}
				
				if(StringUtils.isNotBlank(orderInfo.getOrderNo())){
					partnerOrder.setOrderNo(orderInfo.getOrderNo());
				}
				
				reMsg = TransactionProcessor.dynamicInvoke("partnerOrder",
						"getPartnerOrderByWhere", partnerOrder,pageNum,pageSize);
			
				if (reMsg.getObj() != null) {
					PcPartnerOrder pcPartnerOrder = (PcPartnerOrder) reMsg.getObj();
					List<Order> partenrOrders = pcPartnerOrder.getPartnerOrders();
					List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
					if(partenrOrders!=null){
						  
						for (Order orderTemp :partenrOrders) {
							orderInfos.add(OrderUtils.packPartnerOrder(orderTemp));
						}
					}
					ResultObj resultObj = new ResultObj();
					resultObj.setObjects(orderInfos);
					resultObj.setRecordTotal(pcPartnerOrder.getTotalNum());
					pcResultObj.setEntity(resultObj);
					pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
					pcResultObj.setMsg("查询成功");
	
					return pcResultObj;
	
					}
		
				}
					
				
			if (StringUtils.isNotBlank(orderInfo.getUserId())) {
				
				userOrder.setUserId(Long.parseLong(orderInfo.getUserId()));
		
				if(StringUtils.isNotBlank(orderInfo.getOrderNo())){
					userOrder.setOrderNo(orderInfo.getOrderNo());
				}
				
				if(StringUtils.isNotBlank(orderInfo.getTicketId())){
					userOrder.setTradeId(orderInfo.getTicketId());
				}
				
				
				reMsg = TransactionProcessor.dynamicInvoke("userOrder",
						"getUserOrder",
						userOrder,pageNum,pageSize);
				
				if(reMsg.getObj()!=null){
					
					PcUserOrder pcUserOrder = (PcUserOrder) reMsg.getObj();
					List<com.cqfc.protocol.userorder.Order> orders = pcUserOrder.getUserOrders();
					List<OrderInfo> orderInfos = new ArrayList<OrderInfo>();
					
					if(orders!=null){
						reMsg = TransactionProcessor.dynamicInvoke("userAccount",
								"findUserInfoById", userOrder.getUserId());

						if (reMsg.getObj() != null) {

							UserInfo userInfo = (UserInfo) reMsg.getObj();
							orderInfo.setUserName(userInfo.getUserName());
						}
						
						for (com.cqfc.protocol.userorder.Order tempOrder:orders) {
							
						orderInfos.add(OrderUtils.packUserOrder(tempOrder,orderInfo.getUserName()));
						}
						
					}
					
					ResultObj resultObj = new ResultObj();
					resultObj.setObjects(orderInfos);
					resultObj.setRecordTotal(pcUserOrder.totalSize);
					pcResultObj.setEntity(resultObj);
					pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
					pcResultObj.setMsg("查询成功");

					return pcResultObj;
				}
			}
		} catch (java.lang.NumberFormatException e) {

			pcResultObj.setMsg("用户帐号格式错误");
			pcResultObj.setMsgCode("2");
			return pcResultObj;
		}
		ResultObj resultObj = new ResultObj();
		resultObj.setObjects(new OrderInfo());
		resultObj.setRecordTotal(0);
		pcResultObj.setEntity(resultObj);
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setMsg("查询无记录");
		return pcResultObj;
	}
	
	
	@Override
	public PcResultObj getWinningOrderByWhere(WinningOrderInfo winningOrderInfo, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage res = TransactionProcessor.dynamicInvoke("ticketWinning", "getWinningOrderList", winningOrderInfo, pageNum, pageSize);
		
		if (res.getObj() != null) {
			com.cqfc.protocol.ticketwinning.ReturnData ret = (com.cqfc.protocol.ticketwinning.ReturnData) res.getObj();
			
			if(ret.getResultListSize() > 0){
				ResultObj resultObj = new ResultObj();
				resultObj.setObjects(ret.getResultList());
				resultObj.setRecordTotal(ret.getTotalSize());
				pcResultObj.setEntity(resultObj);
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("查询成功");
			}
			else{
				ResultObj resultObj = new ResultObj();
				resultObj.setObjects(ret.getResultList());
				resultObj.setRecordTotal(0);
				pcResultObj.setEntity(resultObj);
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("查询无记录");
			}
		}
		else{
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj getLotteryIssue(Issue issue, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();
		ResultObj resultObj = new ResultObj();
		LotteryIssue lotteryIssue = new LotteryIssue();
		
		if(StringUtils.isBlank(issue.getLotteryId())){
			lotteryIssue.setLotteryId("");
		}else{
			lotteryIssue.setLotteryId(issue.getLotteryId());
		}
		if(StringUtils.isBlank(issue.getIssueNo())){
			lotteryIssue.setIssueNo("");
		}else{
			lotteryIssue.setIssueNo(issue.getIssueNo());
		}
		if(issue.getState()==null){
			lotteryIssue.setState(0);
		}else{
			try {
				lotteryIssue.setState(Integer.parseInt(issue.getState()));
			} catch (NumberFormatException e) {
				pcResultObj.setMsg("期号状态格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
			
		}
		com.cqfc.processor.ReturnMessage res = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "getLotteryIssueList", lotteryIssue,
						pageNum, pageSize);
		if (res.getObj() != null) {
			ReturnData returnDate = (ReturnData) res.getObj();
			List<LotteryIssue> lotteryIssues = returnDate.getResultList();
			List<Issue> issues = new ArrayList<Issue>();
		
			if(lotteryIssues.size()>0){
				
				for(int i=0;i<lotteryIssues.size();i++){
					
					issue = new Issue();
					issue.setIssueId(lotteryIssues.get(i).getIssueId());
					issue.setLotteryId(lotteryIssues.get(i).getLotteryId());
					issue.setIssueNo(lotteryIssues.get(i).getIssueNo());
					issue.setDrawResult(lotteryIssues.get(i).getDrawResult());
					issue.setState(Integer.toString(lotteryIssues.get(i).getState()));
					issue.setDrawTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getDrawTime()));
					issue.setBeginTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getBeginTime()));
					issue.setSingleEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getSingleEndTime()));
					issue.setCompoundEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getCompoundEndTime()));
					issue.setSingleTogetherEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getSingleTogetherEndTime()));
					issue.setCompoundTogetherEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getCompoundTogetherEndTime()));
					issue.setSingleUploadEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getSingleUploadEndTime()));
					issue.setPrintBeginTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getPrintBeginTime()));
					issue.setPrintEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getPrintEndTime()));
					issue.setOfficialBeginTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getOfficialBeginTime()));
					issue.setOfficialEndTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getOfficialEndTime()));
					issue.setPrizePool(MoneyUtil.toYuanStr(lotteryIssues.get(i).getPrizePool()));
					issue.setExt(lotteryIssues.get(i).getExt());
					issue.setCreateTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getCreateTime()));
					issue.setLastUpdateTime(DateUtil.getSubstringDateTime(lotteryIssues.get(i).getLastUpdateTime()));
					issues.add(issue);
				}
								
			}
			resultObj.setObjects(issues);
			resultObj.setRecordTotal(returnDate.getTotalSize());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
					
 		} else {
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		return pcResultObj;
	}

	@Override
	public PcResultObj findLotteryIssue(String lotteryId, String issueNo) {
		PcResultObj pcResultObj = new PcResultObj();
		if(StringUtils.isBlank(lotteryId)){
			lotteryId ="";
		}
		if(StringUtils.isBlank(issueNo)){
			issueNo = "";
		}
		com.cqfc.processor.ReturnMessage res = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId,issueNo);
		if(res.getObj()!=null){
			LotteryIssue ls = (LotteryIssue)res.getObj();
			Issue issue = new Issue();
			issue.setIssueId(ls.getIssueId());
			issue.setLotteryId(ls.getLotteryId());
			issue.setIssueNo(ls.getIssueNo());
			issue.setDrawResult(ls.getDrawResult());
			issue.setState(Integer.toString(ls.getState()));
			issue.setDrawTime(DateUtil.getSubstringDateTime(ls.getDrawTime()));
			issue.setBeginTime(DateUtil.getSubstringDateTime(ls.getBeginTime()));
			issue.setSingleEndTime(DateUtil.getSubstringDateTime(ls.getSingleEndTime()));
			issue.setCompoundEndTime(DateUtil.getSubstringDateTime(ls.getCompoundEndTime()));
			issue.setSingleTogetherEndTime(DateUtil.getSubstringDateTime(ls.getSingleTogetherEndTime()));
			issue.setCompoundTogetherEndTime(DateUtil.getSubstringDateTime(ls.getCompoundTogetherEndTime()));
			issue.setSingleUploadEndTime(DateUtil.getSubstringDateTime(ls.getSingleUploadEndTime()));
			issue.setPrintBeginTime(DateUtil.getSubstringDateTime(ls.getPrintBeginTime()));
			issue.setPrintEndTime(DateUtil.getSubstringDateTime(ls.getPrintEndTime()));
			issue.setOfficialBeginTime(DateUtil.getSubstringDateTime(ls.getOfficialBeginTime()));
			issue.setOfficialEndTime(DateUtil.getSubstringDateTime(ls.getOfficialEndTime()));
			issue.setPrizePool(MoneyUtil.toYuanStr(ls.getPrizePool()));
			issue.setExt(ls.getExt());
			issue.setCreateTime(DateUtil.getSubstringDateTime(ls.getCreateTime()));
			issue.setLastUpdateTime(DateUtil.getSubstringDateTime(ls.getLastUpdateTime()));
			pcResultObj.setEntity(issue);
			pcResultObj.setMsg("查询详情成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		}else{
			pcResultObj.setMsg("查询详情失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		return pcResultObj;
	}
	@Override
	public PcResultObj deleteLotteryIssue(Issue issue) {
		PcResultObj pcResultObj = new PcResultObj();

		if (StringUtils.isBlank(issue.getLotteryId()) || StringUtils.isBlank(issue.getIssueNo())) {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("数据错误");
			return pcResultObj;
		}

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE,
						"deleteIssueNo", issue.getLotteryId(),
						issue.getIssueNo());

		if (reMsg.getObj() != null) {

			int flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				pcResultObj.setMsgCode("1");
				pcResultObj.setMsg("删除成功");
				return pcResultObj;
			}

		}

		pcResultObj.setMsgCode("2");
		pcResultObj.setMsg("删除失败");
		return pcResultObj;
	}
	
	@Override
	public PcResultObj updateLotteryIssue(Issue issue) {
		PcResultObj pcResultObj = new PcResultObj();
		LotteryIssue lotteryIssue = new LotteryIssue();
		if(StringUtils.isBlank(issue.getDrawTime())||
		   StringUtils.isBlank(issue.getBeginTime())||
		   StringUtils.isBlank(issue.getSingleEndTime())||
		   StringUtils.isBlank(issue.getCompoundEndTime())||
		   StringUtils.isBlank(issue.getSingleTogetherEndTime())||
		   StringUtils.isBlank(issue.getCompoundTogetherEndTime())||
		   StringUtils.isBlank(issue.getSingleUploadEndTime())||
		   StringUtils.isBlank(issue.getPrintBeginTime())||
		   StringUtils.isBlank(issue.getPrintEndTime())||
		   StringUtils.isBlank(issue.getOfficialBeginTime())||
		   StringUtils.isBlank(issue.getOfficialEndTime())){
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("修改数据为空");
				return pcResultObj;
		}
		   lotteryIssue.setLotteryId(issue.getLotteryId());
		   lotteryIssue.setIssueNo(issue.getIssueNo());
		   lotteryIssue.setDrawTime(issue.getDrawTime());
		   lotteryIssue.setBeginTime(issue.getBeginTime());
		   lotteryIssue.setSingleEndTime(issue.getSingleEndTime());
		   lotteryIssue.setCompoundEndTime(issue.getCompoundEndTime());
		   lotteryIssue.setSingleTogetherEndTime(issue.getSingleTogetherEndTime());
		   lotteryIssue.setCompoundTogetherEndTime(issue.getCompoundTogetherEndTime());
		   lotteryIssue.setSingleUploadEndTime(issue.getSingleUploadEndTime());
		   lotteryIssue.setPrintBeginTime(issue.getPrintBeginTime());
		   lotteryIssue.setPrintEndTime(issue.getPrintEndTime());
		   lotteryIssue.setOfficialBeginTime(issue.getOfficialBeginTime());
		   lotteryIssue.setOfficialEndTime(issue.getOfficialEndTime());
		
			com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
					.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE,
							"updateLotteryIssue", lotteryIssue);
			
			if(reMsg!=null){
				int flag = (Integer)reMsg.getObj();
				if(flag==1){
					pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
					pcResultObj.setMsg("修改成功");
					return pcResultObj;
				}
			}
		
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		pcResultObj.setMsg("修改失败");
		return pcResultObj;
	}

	public void initTempWinPrizeChecks(List<LotteryPartner> partners,String lotteryId, String issueNo,
			Map<String, WinPrizeCheck> tempWinPrizeChecks, int status) {

		WinPrizeCheck winPrizeCheck = null;
		if(partners!=null){
			
			for (LotteryPartner partner : partners) {
				winPrizeCheck = new WinPrizeCheck();
			//	winPrizeCheck.setIssueNo(issueNo);
			//	winPrizeCheck.setLotteryId(lotteryId);
				winPrizeCheck.setPartnerId(partner.getPartnerId());
				winPrizeCheck.setName(partner.getPartnerName());
			//	winPrizeCheck.setStatus(status);
				winPrizeCheck.setTotalMoney("0");
				tempWinPrizeChecks.put(partner.getPartnerId() + lotteryId
						+ issueNo, winPrizeCheck);
			}
		}
			
	}


	@Override
	public PcResultObj ifAutoCheckWinPrize(boolean flag) {

		PcResultObj pcResultObj = new PcResultObj();
		ScheduledTask.setCheckWinPrize(flag);
		if (flag) {

			pcResultObj.setMsg("设置自动审核成功");
		} else {
			pcResultObj.setMsg("取消自动审核成功");
		}

		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		return pcResultObj;
	}

	@Override
	public PcResultObj ifAutoCheckAwardPrize(boolean flag) {

		PcResultObj pcResultObj = new PcResultObj();
		ScheduledTask.setCheckAwardPrize(flag);
		if (flag) {

			pcResultObj.setMsg("设置自动审核成功");
		} else {
			pcResultObj.setMsg("取消自动审核成功");
		}

		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		return pcResultObj;
	}

	
	
	public LotteryIssue getLotteryIssue(LotteryIssue lotteryIssue){
		
	
		
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryIssue.getLotteryId(),lotteryIssue.getIssueNo());
		
		if(returnMessage.getObj()!=null){
			

			return (LotteryIssue)returnMessage.getObj();
		} else {

			return null;
		}

	}

	@Override
	public PcResultObj getCancelOrder(CancelOrderInfo cancelOrderInfo, int currentPage, int pageSize) {
		PcResultObj pcResultObj = new PcResultObj();
		ResultObj resultObj = new ResultObj();
		CancelOrder cancelOrder = new CancelOrder();
		if(StringUtils.isBlank(cancelOrderInfo.getUserId())){
			cancelOrder.setUserId(0);
		}else{
			try {
				cancelOrder.setUserId(Long.parseLong(cancelOrderInfo.getUserId()));
			} catch (NumberFormatException e) {
				pcResultObj.setMsg("用户Id格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
			
		}
		if(StringUtils.isBlank(cancelOrderInfo.getOrderNo())){
			cancelOrder.setOrderNo("");
		}else{
			cancelOrder.setOrderNo(cancelOrderInfo.getOrderNo());
		}
		if(StringUtils.isBlank(cancelOrderInfo.getPartnerId())){
			cancelOrder.setPartnerId("");
		}else{
			cancelOrder.setPartnerId(cancelOrderInfo.getPartnerId());
		}
		if(StringUtils.isBlank(cancelOrderInfo.getLotteryId())){
			cancelOrder.setLotteryId("");
		}else{
			cancelOrder.setLotteryId(cancelOrderInfo.getLotteryId());
		}
		if(StringUtils.isBlank(cancelOrderInfo.getIssueNo())){
			cancelOrder.setIssueNo("");
		}else{
			cancelOrder.setIssueNo(cancelOrderInfo.getIssueNo());
		}
		
		com.cqfc.processor.ReturnMessage res = TransactionProcessor
				.dynamicInvoke("cancelOrder", "getCancelOrder",cancelOrder,currentPage,pageSize);
		if (res.getObj() != null) {
			com.cqfc.protocol.cancelorder.ReturnData returnData = (com.cqfc.protocol.cancelorder.ReturnData) res
					.getObj();
			List<CancelOrder> cancelOrderList = returnData.getResultList();
			
			List<CancelOrderInfo> cancelOrderInfos = new ArrayList<CancelOrderInfo>();
			if(cancelOrderList.size()>0){
				for(int i=0;i<cancelOrderList.size();i++){
					cancelOrderInfo = new CancelOrderInfo();
					cancelOrderInfo.setOrderNo(cancelOrderList.get(i).getOrderNo()); 
					cancelOrderInfo.setPartnerId(cancelOrderList.get(i).getPartnerId());
					cancelOrderInfo.setUserId(Long.toString(cancelOrderList.get(i).getUserId()));
					cancelOrderInfo.setLotteryId(cancelOrderList.get(i).getLotteryId());
					cancelOrderInfo.setIssueNo(cancelOrderList.get(i).getIssueNo());
					cancelOrderInfo.setOutTicketStatus(cancelOrderList.get(i).getOutTicketStatus());
					String totalAmount = MoneyUtil.toYuanStr(cancelOrderList.get(i).getTotalAmount());
					cancelOrderInfo.setTotalAmount(totalAmount);
					cancelOrderInfo.setOrderContent(cancelOrderList.get(i).getOrderContent());
					cancelOrderInfo.setPlayType(cancelOrderList.get(i).getPlayType());
					cancelOrderInfo.setMultiple(cancelOrderList.get(i).getMultiple());
					cancelOrderInfo.setCreateTime(DateUtil.getSubstringDateTime(cancelOrderList.get(i).getCreateTime()));
					cancelOrderInfo.setLastUpdateTime(DateUtil.getSubstringDateTime(cancelOrderList.get(i).getLastUpdateTime()));
					cancelOrderInfos.add(cancelOrderInfo);
					}
				
			}
			resultObj.setObjects(cancelOrderInfos);
			resultObj.setRecordTotal(returnData.getTotalSize());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		} else {
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}

		return pcResultObj;
	}

	/**
	 * 重新全部算奖
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public Integer restartAllPrize(String lotteryId, String issueNo){
		//判断期次状态是否符合要求
		ReturnMessage message = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId, issueNo);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			LotteryIssue lotteryIssue = (LotteryIssue) message.getObj();
			if(lotteryIssue == null){
				return ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_EXIST;
			}
			if(lotteryIssue.getState() < 13 || lotteryIssue.getState() > 15){
				return ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_CORRECT;
			}
		} else if(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(message
				.getStatusCode())){
			return ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_EXIST;
		} else{
			return null;
		}
		message = TransactionProcessor.dynamicInvoke("ticketWinning", "restartCalPrizeAll", lotteryId, issueNo);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			Integer result = (Integer) message.getObj();
			return result;
		} else {
			Log.run.error("restartAllPrize failed.");
		}
		return null;
	}
	
	/**
	 * 重新部分算奖
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public Integer restartPartPrize(String lotteryId, String issueNo){
		//判断期次状态是否符合要求
		ReturnMessage message = TransactionProcessor.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId, issueNo);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			LotteryIssue lotteryIssue = (LotteryIssue) message.getObj();
			if(lotteryIssue == null){
				return ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_EXIST;
			}
			if(lotteryIssue.getState() < 13 || lotteryIssue.getState() > 15){
				return ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_CORRECT;
			}
		} else if(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA.equals(message
				.getStatusCode())){
			return ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_EXIST;
		} else{
			return null;
		}
		message = TransactionProcessor.dynamicInvoke("ticketWinning", "restartCalPrizePart", lotteryId, issueNo);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(message
				.getStatusCode())) {
			Integer result = (Integer) message.getObj();
			return result;
		} else {
			Log.run.error("restartPartPrize failed.");
		}
		return null;
	}

	
	public void sort(List<PcWinPrizeCheck> pcwinPrizeChecks){
		Collections.sort(pcwinPrizeChecks, new Comparator<PcWinPrizeCheck>() {

			@Override
			public int compare(PcWinPrizeCheck o1, PcWinPrizeCheck o2) {
				return o1.getLotteryId().compareTo(o2.getLotteryId());
			}
			
		});
		Collections.sort(pcwinPrizeChecks, new Comparator<PcWinPrizeCheck>() {

			@Override
			public int compare(PcWinPrizeCheck o1, PcWinPrizeCheck o2) {
				return o2.getIssueNo().compareTo(o1.getIssueNo());
			}
			
		});
	}

	@Override
	public PcResultObj getJcMacth(MatchCompetive competive, int pageNum,
			int pageSize) {
		PcResultObj pcResultObj =  new PcResultObj();
		List<MatchCompetive> matchCompetives = new ArrayList<MatchCompetive>();
		
		
		
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "getMatchCompetiveList", competive,pageNum,pageSize);
		
		if(returnMessage.getObj()!=null){
			
			MatchCompetiveData matchCompetiveData = (MatchCompetiveData) returnMessage.getObj();
			matchCompetives = matchCompetiveData.getMatchCompetiveList();
			
			for (MatchCompetive matchCompetive :matchCompetives) {
				matchCompetive.setMatchDate(DateUtils.toHtmlTime(matchCompetive.getMatchDate(), 2));
				matchCompetive.setMatchBeginTime(DateUtils.toHtmlTime(matchCompetive.getMatchBeginTime(), 1));
				matchCompetive.setMatchEndTime(DateUtils.toHtmlTime(matchCompetive.getMatchEndTime(), 1));
				matchCompetive.setBettingDeadline(DateUtils.toHtmlTime(matchCompetive.getBettingDeadline(), 1));
			}
			
			ResultObj resultObj = new ResultObj();
			
			resultObj.setObjects(matchCompetives);
			resultObj.setRecordTotal(matchCompetiveData.getTotalSize());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			 
		}else{
			
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj updateJcMatch(MatchCompetive competive) {
		
		PcResultObj pcResultObj =  new PcResultObj();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "updateMatchCompetive", competive);
		
		if(returnMessage.getObj()!=null){
			int flag = (Integer) returnMessage.getObj();
			if(flag == 1){
				
				pcResultObj.setMsg("修改成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			}else{
				
				pcResultObj.setMsg("修改失败");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			}
		}else{
			
			pcResultObj.setMsg("修改失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj delJcMatch(MatchCompetive competive) {
		PcResultObj pcResultObj =  new PcResultObj();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "deleteMatchCompetive", competive.getWareIssue(),competive.getTransferId());
		
		if(returnMessage.getObj()!=null){
			
			int flag = (Integer) returnMessage.getObj();
			if(flag == 1){
				
				pcResultObj.setMsg("删除成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			}else{
				
				pcResultObj.setMsg("删除失败");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			}
		}else{
			
			pcResultObj.setMsg("删除失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj getJcMatchById(MatchCompetive competive) {
		PcResultObj pcResultObj =  new PcResultObj();
		MatchCompetive matchCompetive = new MatchCompetive();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "getMatchCompetive", competive.getWareIssue(),competive.getTransferId());
		
		if(returnMessage.getObj()!=null){
			
			matchCompetive = (MatchCompetive) returnMessage.getObj();
			matchCompetive.setMatchBeginTime(DateUtils.toHtmlTime(matchCompetive.getMatchBeginTime(), 1));
			matchCompetive.setBettingDeadline(DateUtils.toHtmlTime(matchCompetive.getBettingDeadline(), 1));
			matchCompetive.setMatchDate(DateUtils.toHtmlTime(matchCompetive.getMatchDate(), 2));
			pcResultObj.setEntity(matchCompetive);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			 
		}else{
			
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj getJcMatchResultById(MatchCompetive competive,String lotteryId) {
		PcResultObj pcResultObj =  new PcResultObj();
		MatchCompetive matchCompetive = new MatchCompetive();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "getMatchCompetiveResult", competive.getWareIssue(),lotteryId,competive.getTransferId());
		
		if(returnMessage.getObj()!=null){
			
			matchCompetive = (MatchCompetive) returnMessage.getObj();
			if(matchCompetive.getMatchCompetiveResultList().size()>0){
				
				pcResultObj.setEntity(matchCompetive.getMatchCompetiveResultList().get(0));
			}
			
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			 
		}else{
			
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	/**
	 * 创建修改开奖结果（北单 北单胜负过关 竞足竞篮）
	 */
	@Override
	public PcResultObj createOrUpdateJcMatchResult(
			MatchCompetiveResult competiveResult) {
		PcResultObj pcResultObj =  new PcResultObj();
		
		competiveResult.setMatchType(OrderUtil.getJcCategoryDetail(competiveResult.getLotteryId()));
		competiveResult.setLotteryName(IssueConstant.SportLotteryType.getEnum(competiveResult.getLotteryId()).getText());
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "saveOrUpdateMatchCompetiveResult",competiveResult);
		
		if(returnMessage.getObj()!=null){
			int flag = (Integer) returnMessage.getObj();
			
			if(flag == 1){
				
				pcResultObj.setMsg("成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			}else{
				
				pcResultObj.setMsg("失败");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			}
			 
		}else{
			
			pcResultObj.setMsg("失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj getLzcMactches(MatchFootball matchFootball) {
		PcResultObj pcResultObj =  new PcResultObj();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "getMatchFootballDate",matchFootball,1,20);
		
		if(returnMessage.getObj()!=null){
			MatchFootballDate matchFootballDate = (MatchFootballDate) returnMessage.getObj();
			List<MatchFootball> matchFootballs = matchFootballDate.getMatchFootballList();
			if(matchFootballs!=null){
				
				for (MatchFootball football:matchFootballs) {
					football.setMatchBeginTime(DateUtils.toHtmlTime(football.getMatchBeginTime(), 1));
				}
			}
			
			pcResultObj.setEntity(matchFootballs);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			 
		}else{
			
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj getJcCancelOrder(SportCancelOrder sportCancelOrder,
			int pageNum, int pageSize) {
		PcResultObj pcResultObj =  new PcResultObj();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "", sportCancelOrder);
		
		if(returnMessage.getObj()!=null){
			
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			 
		}else{
			
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj udpateLzcMactche(MatchFootball matchFootball) {
		PcResultObj pcResultObj =  new PcResultObj();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "updateMatchFootball", matchFootball);
		
		if(returnMessage.getObj()!=null){
			int flag = (Integer) returnMessage.getObj();
			
			if(flag == 1){
				
				pcResultObj.setMsg("成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			}else{
				
				pcResultObj.setMsg("失败");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			}
			 
		}else{
			
			pcResultObj.setMsg("失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj getFootballMatchById(MatchFootball matchFootball) {

		PcResultObj pcResultObj =  new PcResultObj();
		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor.dynamicInvoke(ConstantsUtil.MODULENAME_LOTTERY_ISSUE, "getMatchFootball", matchFootball.getLotteryId(),matchFootball.getWareIssue(),matchFootball.getMatchNo());
		
		if(returnMessage.getObj()!=null){
			
			
			MatchFootball football = (MatchFootball)returnMessage.getObj();
			football.setMatchBeginTime(DateUtils.toHtmlTime(football.getMatchBeginTime(), 1));
			pcResultObj.setEntity(football);
			pcResultObj.setMsg("成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			 
		}else{
			
			pcResultObj.setMsg("失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		return pcResultObj;
	
	}
	
	
}
