package com.cqfc.accessback.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cqfc.accessback.dao.RecordDao;
import com.cqfc.accessback.record.TicketCallbackRecordBuffer;
import com.cqfc.accessback.task.AccessBackTask;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.accessback.AccessBackService;
import com.cqfc.protocol.accessback.ResultMessage;
import com.cqfc.protocol.accessback.TicknumRecord;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.Pair;
import com.cqfc.xmlparser.util.ErrorMsgXmlHelper;
import com.jami.util.Log;

@Service
public class AccessBackServiceImpl implements AccessBackService.Iface{
	
	private static Map<String, LotteryPartner> partnerMap = null;
	private static final AtomicBoolean isInitialization = new AtomicBoolean(
			false);
	
	static {
		initPartners();
	}

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Resource
	private RecordDao recordDao;
	
	@Override
	public ResultMessage sendAccessBackMessage(String partnerId, String xmlStr)
			throws TException {
		Log.run.info("sendAccessBackMessage(parnterId: %s, xmlStr: %s)", partnerId, xmlStr);
		ResultMessage message = new ResultMessage();
		LotteryPartner partner = null;
		Pair<LotteryPartner, String> checkPartnerResult = checkPartner(partnerId, xmlStr);
		if (checkPartnerResult.second() != null) {
			Log.run.error("sendAccessBackMessage: %s", checkPartnerResult.second());
			return message;
		}
		partner = checkPartnerResult.first();
		try {
			threadPoolTaskExecutor.submit(new AccessBackTask(partner, xmlStr));
			message.setStatusCode(200);
			message.setMsg("OK");
			return message;
		} catch (Exception e) {
			Log.run.error(e.toString());
			message.setStatusCode(404);
			message.setMsg(e.toString());
		}finally{
			TicketCallbackRecordBuffer.addRecord(xmlStr);
		}

		return message;
	}

	@Override
	public TicknumRecord getTicknumRecord(String gameId, String issue)
			throws TException {
		TicknumRecord ticknumRecord = null;
		try {
			ticknumRecord = recordDao.getTicknumRecord(gameId, issue);
		} catch (Exception e) {
			Log.run.debug("", e);
		}
		if(ticknumRecord == null){
			ticknumRecord = new TicknumRecord();
			ticknumRecord.setGameId(gameId);
			ticknumRecord.setIssue(issue);
			ticknumRecord.setSuccessNum(0);
			ticknumRecord.setFailNum(0);
		}
		return ticknumRecord;
	}	
	
	/**
	 * 初始获取合作商列表
	 */
	private static boolean initPartners() {
		// 该方法基于目前partner数量不会很多的情况下直接去partner模块将所有partner(10000以内)一次性取过来，假如后续partner较多，这里需要分多次循环去取。
		if (isInitialization.compareAndSet(false, true)) {
			try {
				List<LotteryPartner> partners = new ArrayList<LotteryPartner>();
				Map<String, LotteryPartner> map = new ConcurrentHashMap<String, LotteryPartner>();
				ReturnMessage reMsg = TransactionProcessor.dynamicInvoke(
						"partner", "getLotteryPartnerList",
						new LotteryPartner(), 1, 10000);
				if (reMsg.getObj() != null) {
					ReturnData re = (ReturnData) reMsg.getObj();
					partners = re.getResultList();
					for (LotteryPartner partner : partners) {
						map.put(partner.getPartnerId(), partner);
					}
					partnerMap = map;
				}
				return true;
			} catch (Exception e) {
				Log.run.error("sendAccessBackMessage,get partner failed.errMsg=%s", e.toString());
			} finally {
				isInitialization.set(false);
			}
		}
		return false;
	}
	
	/**
	 * 校验合作商id是否存在
	 * 
	 * @param partnerid
	 * @param msg
	 * @return
	 */
	private Pair<LotteryPartner, String> checkPartner(String partnerid, String msg) {
		Pair<LotteryPartner, String> result = new Pair<LotteryPartner, String>();
		if (partnerMap == null) {
			if (initPartners()){
				if (partnerMap == null || !partnerMap.containsKey(partnerid)){
					Log.run.error("sendAccessBackMessage,get all partner, but partnerid不存在 ,partnerid: %s, msg: %s",
							partnerid, msg);
					result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
					return result;
				}
				result.first(partnerMap.get(partnerid));
				return result;
			} else {
				ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
						"partner", "findLotteryPartnerById", partnerid);

				if (retMsg.getObj() == null) {
					Log.run.error("sendAccessBackMessage,get one partner when map is null, partnerid不存在 .partnerid: %s, msg: %s",
							partnerid, msg);
					result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
					return result;
				} else {
					LotteryPartner partner = (LotteryPartner) retMsg.getObj();
					partnerMap.put(partner.getPartnerId(), partner);
					Log.run.info(
							"sendAccessBackMessage,find lottery partner success, partnerid: %s, msg: %s",
							partnerid, msg);
					result.first(partner);
					return result;
				}
			}
		} else {
			if (partnerMap.containsKey(partnerid)) {
				result.first(partnerMap.get(partnerid));
				return result;
			}
			ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
					"partner", "findLotteryPartnerById", partnerid);

			if (retMsg.getObj() == null) {
				Log.run.error("sendAccessBackMessage,get one partner when map not contain it, partnerid不存在 .partnerid: %s, msg: %s",
						partnerid, msg);
				result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
						ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
				return result;
			} else {
				LotteryPartner partner = (LotteryPartner) retMsg.getObj();
				partnerMap.put(partner.getPartnerId(), partner);
				Log.run.info(
						"sendAccessBackMessage,find lottery partner success, partnerid: %s, msg: %s",
						partnerid, msg);
				result.first(partner);
				return result;
			}

		}
	}

	public static void updatePartnerMap(LotteryPartner lotteryPartner) {
		if(lotteryPartner != null && partnerMap != null){
			partnerMap.put(lotteryPartner.getPartnerId(), lotteryPartner);
		}		
	}
}
