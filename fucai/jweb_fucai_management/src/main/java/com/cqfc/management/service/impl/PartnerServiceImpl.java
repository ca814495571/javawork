package com.cqfc.management.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cqfc.management.model.DaySaleOrAwardDetail;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PartnerCharge;
import com.cqfc.management.model.PartnerDayReport;
import com.cqfc.management.model.PartnerDaySale;
import com.cqfc.management.model.PartnerInfo;
import com.cqfc.management.model.PartnerIssueCount;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.service.ILotteryService;
import com.cqfc.management.service.IPartnerService;
import com.cqfc.management.util.dateUtils.DateUtils;
import com.cqfc.management.util.dateUtils.OrderUtils;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerRecharge;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.LotteryDaySale;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.PcDailyReport;
import com.cqfc.protocol.partnerorder.PcDaySaleDetails;
import com.cqfc.protocol.partnerorder.PcLotteryIssueSale;
import com.cqfc.protocol.partnerorder.PcPartnerOrder;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IdWorker;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.PartnerConstant;
import com.jami.common.ActivemqProducer;
import com.jami.common.ApplicationContextProvider;
import com.jami.util.Log;

@Service
public class PartnerServiceImpl implements IPartnerService {

	@Autowired
	ILotteryService lotteryIssueService;

	@Override
	public PcResultObj addPartnerInfo(PartnerInfo partnerInfo) {

		PcResultObj pcResultObj = new PcResultObj();
		int flag = 0;
		double alarmValue = 0;
		double creditLimit = 0;
		LotteryPartner lotteryPartner = new LotteryPartner();

		if (StringUtils.isNotBlank(partnerInfo.getCallbackUrl())) {
			lotteryPartner.setCallbackUrl(partnerInfo.getCallbackUrl());
		} else {
			lotteryPartner.setCallbackUrl("");
		}

		if (StringUtils.isNotBlank(partnerInfo.getPartnerId())) {

			lotteryPartner.setPartnerId(partnerInfo.getPartnerId());
		} else {
			pcResultObj.setMsg("合作商Id为必填项");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		if (StringUtils.isNotBlank(partnerInfo.getPartnerName())) {

			lotteryPartner.setPartnerName(partnerInfo.getPartnerName());
		} else {
			lotteryPartner.setPartnerName("");
		}

		if (StringUtils.isNotBlank(partnerInfo.getAlarmValue())) {
			try {
				
				alarmValue = Double.parseDouble(partnerInfo.getAlarmValue());
				if(alarmValue<0.01&&alarmValue!=0){
					pcResultObj.setMsg("告警值金额不正确");
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					return pcResultObj;
				}
				lotteryPartner.setMinBalance(MoneyUtil.toCent(alarmValue));
				
			} catch (Exception e) {
				pcResultObj.setMsg("告警值格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
		}
		}else {
			
			lotteryPartner.setMinBalance(0);
		}
		
		if (StringUtils.isNotBlank(partnerInfo.getCreditLimit())) {
			try {
				creditLimit = Double.parseDouble(partnerInfo
						.getCreditLimit());
				if(creditLimit<0.01&&creditLimit!=0){
					pcResultObj.setMsg("信用额度金额不正确");
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					return pcResultObj;
				}
			} catch (Exception e) {
				pcResultObj.setMsg("信用额度格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
		}
		
		if (partnerInfo.getPartnerType() == 0) {
			lotteryPartner.setPartnerType(Short.parseShort("1"));
		} else {
			lotteryPartner.setPartnerType(partnerInfo.getPartnerType());
		}
		
		if (partnerInfo.getSecretKey() == null) {
			lotteryPartner.setSecretKey("");
		} else {
			lotteryPartner.setSecretKey(partnerInfo.getSecretKey());
		}
		if (partnerInfo.getPublicKey() == null) {
			lotteryPartner.setPublicKey("");
		} else {
			lotteryPartner.setPublicKey(partnerInfo.getPublicKey());
		}
		if (partnerInfo.getAliasKey() == null) {
			lotteryPartner.setAliasKey("");
		} else {
			lotteryPartner.setAliasKey(partnerInfo.getAliasKey());
		}
		if (partnerInfo.getKeyStore() == null) {
			lotteryPartner.setKeyStore("");
		} else {
			lotteryPartner.setKeyStore(partnerInfo.getKeyStore());
		}
		if (partnerInfo.getUserId() == null
				|| "".equals(partnerInfo.getUserId())) {
			lotteryPartner.setUserId(0);
		} else {

			try {

				lotteryPartner
						.setUserId(Long.parseLong(partnerInfo.getUserId()));
			} catch (NumberFormatException e) {

				pcResultObj.setMsg("用户Id格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
		}

		lotteryPartner.setState(Short.parseShort("1"));
		if (getPartnerType(partnerInfo.getPartnerId()) != -1) {
			pcResultObj.setMsg("合作商id已存在");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		PartnerIpAddress partnerIpAddress = null;
		List<PartnerIpAddress> partnerIpAddresses = new ArrayList<PartnerIpAddress>();
		if (StringUtils.isNotBlank(partnerInfo.getIpAddress())) {

			String[] ipAdsresses = partnerInfo.getIpAddress().split(",");
			
			for (int i = 0; i < ipAdsresses.length; i++) {
				partnerIpAddress = new PartnerIpAddress();
				partnerIpAddress.setPartnerId(partnerInfo.getPartnerId());
				partnerIpAddress.setIpAddress(ipAdsresses[i]);
				partnerIpAddresses.add(partnerIpAddress);
			}
			
		} 
		lotteryPartner.setPartnerIpAddressList(partnerIpAddresses);
				
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor.dynamicInvoke("partner",
					"addLotteryPartner", lotteryPartner);

		if (reMsg.getObj() != null) {
			flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				PartnerAccount partnerAccount = new PartnerAccount();
				partnerAccount.setPartnerId(partnerInfo.getPartnerId());
				partnerAccount.setAlarmValue(MoneyUtil.toCent(alarmValue));
				partnerAccount.setCreditLimit(MoneyUtil.toCent(creditLimit));
				reMsg = TransactionProcessor.dynamicInvoke(
						"partnerAccount", "addPartnerAccount",
						partnerAccount);

				if (reMsg.getObj() != null) {

					flag = (Integer) reMsg.getObj();

					if(flag !=1){
						reMsg = TransactionProcessor.dynamicInvoke(
								"partner", "deletePartnerByPartnerId",
								partnerInfo.getPartnerId());
					}
				} 
			}
		}
		if (flag == 1) {

			pcResultObj.setMsg("添加成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			
			sendPartnerMQ(lotteryPartner);
		} else {
			pcResultObj.setMsg("添加合作商信息失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

		}
		return pcResultObj;
	}

	@Override
	public PcResultObj getPartnerInfo(LotteryPartner partner, int pageNum,
			int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();
		PartnerInfo partnerInfo = null;

		List<PartnerInfo> partnerInfos = new ArrayList<PartnerInfo>();

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partner", "getLotteryPartnerList", partner,
						pageNum, pageSize);

		if (reMsg.getObj() != null) {

			ReturnData reDate = (ReturnData) reMsg.getObj();

			List<LotteryPartner> lotteryPartners = reDate.getResultList();
			if (lotteryPartners != null) {
				for (int i = 0; i < lotteryPartners.size(); i++) {

					partner = lotteryPartners.get(i);

					partnerInfo = new PartnerInfo();

					reMsg = TransactionProcessor.dynamicInvoke(
							"partnerAccount", "findPartnerAccountByPartnerId",
							partner.getPartnerId());

					if (reMsg.getObj() != null) {

						PartnerAccount partnerAccount = (PartnerAccount) reMsg
								.getObj();
						partnerInfo.setAccountState(partnerAccount.getState());
						partnerInfo.setFreezeMoney(MoneyUtil
								.toYuanStr(partnerAccount.getFreezeAmount()));
						partnerInfo.setUsableMoney(MoneyUtil
								.toYuanStr(partnerAccount.getUsableAmount()));
					}

					partnerInfo.setCreateTime(DateUtil.getSubstringDateTime(partner.getRegistrationTime()));
					partnerInfo.setPartnerName(partner.getPartnerName());
					partnerInfo.setPartnerId(partner.getPartnerId());
					partnerInfo.setPartnerType(partner.getPartnerType());
					partnerInfo.setPartnerState(partner.getState());
					partnerInfo.setSecretKey(partner.getSecretKey());
					partnerInfo.setPublicKey(partner.getPublicKey());
					partnerInfo.setAliasKey(partner.getAliasKey());
					partnerInfo.setKeyStore(partner.getKeyStore());
					partnerInfos.add(partnerInfo);

				}
			} else {
				pcResultObj.setMsg("查询失败");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}

			ResultObj resultObj = new ResultObj();
			resultObj.setObjects(partnerInfos);
			resultObj.setRecordTotal(reDate.getTotalSize());

			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			return pcResultObj;

		} else {

			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj getPartnerById(PartnerInfo partnerInfo) {
		PcResultObj pcResultObj = new PcResultObj();

		LotteryPartner partner = null;

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partner", "findLotteryPartnerById",
						partnerInfo.getPartnerId());

		if (reMsg.getObj() != null) {

			partner = (LotteryPartner) reMsg.getObj();

			partnerInfo = new PartnerInfo();

			reMsg = TransactionProcessor.dynamicInvoke("partnerAccount",
					"findPartnerAccountByPartnerId", partner.getPartnerId());

			if (reMsg.getObj() != null) {

				PartnerAccount partnerAccount = (PartnerAccount) reMsg.getObj();

				partnerInfo.setAccountState(partnerAccount.getState());
				partnerInfo.setUsableMoney(MoneyUtil.toYuanStr(partnerAccount.getUsableAmount()));
				partnerInfo.setCreditLimit(MoneyUtil.toYuanStr(partnerAccount
						.getCreditLimit()));
				partnerInfo.setFreezeMoney(MoneyUtil
						.toYuanStr(partnerAccount.getFreezeAmount()));
				
			}

			partnerInfo.setPartnerName(partner.getPartnerName());
			partnerInfo.setPartnerId(partner.getPartnerId());
			partnerInfo.setPartnerType(partner.getPartnerType());
			partnerInfo.setPartnerState(partner.getState());
			partnerInfo.setCreateTime(DateUtil.getSubstringDateTime(partner.getRegistrationTime()));
			partnerInfo.setAlarmValue(MoneyUtil.toYuanStr(partner
					.getMinBalance()));
			partnerInfo.setSecretKey(partner.getSecretKey());
			partnerInfo.setPublicKey(partner.getPublicKey());
			partnerInfo.setAliasKey(partner.getAliasKey());
			partnerInfo.setKeyStore(partner.getKeyStore());			
			partnerInfo.setCallbackUrl(partner.getCallbackUrl());
			partnerInfo.setUserId(String.valueOf(partner.getUserId()));
			List<PartnerIpAddress> ipAddresses = partner
					.getPartnerIpAddressList();
			String ip = "";
			if (ipAddresses != null) {
				for (PartnerIpAddress ipAddress : ipAddresses) {
					ip += ipAddress.getIpAddress() + ",";
				}
			}

			if (!"".equals(ip)) {

				partnerInfo.setIpAddress(ip.substring(0, ip.lastIndexOf(",")));
			} else {

				partnerInfo.setIpAddress("");
			}
			pcResultObj.setEntity(partnerInfo);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			return pcResultObj;

		} else {

			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj updatePartnerById(PartnerInfo partnerInfo) {
		PcResultObj pcResultObj = new PcResultObj();

		int flag = 0;
		double alarmValue = 0;
		double creditLimit = 0;
		if (!isLockPartner(partnerInfo.getPartnerId())) {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("合作商被冻结或者查询不到");
			return pcResultObj;
		}

		LotteryPartner lotteryPartner = new LotteryPartner();

		if (StringUtils.isNotBlank(partnerInfo.getCallbackUrl())) {
			lotteryPartner.setCallbackUrl(partnerInfo.getCallbackUrl());
		} else {
			lotteryPartner.setCallbackUrl("");
		}

		if (StringUtils.isNotBlank(partnerInfo.getPartnerId()) ) {
			lotteryPartner.setPartnerId(partnerInfo.getPartnerId());

		} else {
			pcResultObj.setMsg("合作商Id为必填项");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		if (StringUtils.isNotBlank(partnerInfo.getPartnerName())) {

			lotteryPartner.setPartnerName(partnerInfo.getPartnerName());
		} else {
			lotteryPartner.setPartnerName("");
		}


		if (StringUtils.isNotBlank(partnerInfo.getAlarmValue())) {
			try {
				
				alarmValue = Double.parseDouble(partnerInfo.getAlarmValue());
				if(alarmValue<0.01&&alarmValue!=0){
					pcResultObj.setMsg("告警值金额不正确");
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					return pcResultObj;
				}
				lotteryPartner.setMinBalance(MoneyUtil.toCent(alarmValue));
				
			} catch (Exception e) {
				pcResultObj.setMsg("告警值金额格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
		}
		}else {
			
			lotteryPartner.setMinBalance(0);
		}
		
		if (StringUtils.isNotBlank(partnerInfo.getCreditLimit())) {
			try {
				creditLimit = Double.parseDouble(partnerInfo
						.getCreditLimit());
				if(creditLimit<0.01&&creditLimit!=0){
					pcResultObj.setMsg("信用额度金额不正确");
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					return pcResultObj;
				}
			} catch (Exception e) {
				pcResultObj.setMsg("信用额度格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
		}
		
		if (partnerInfo.getPartnerType() == null) {
			lotteryPartner.setPartnerType(Short.parseShort("1"));
		} else {
			lotteryPartner.setPartnerType(partnerInfo.getPartnerType());

		}
		if (partnerInfo.getSecretKey() == null) {
			lotteryPartner.setSecretKey("");
		} else {
			lotteryPartner.setSecretKey(partnerInfo.getSecretKey());
		}
		if (partnerInfo.getPublicKey() == null) {
			lotteryPartner.setPublicKey("");
		} else {
			lotteryPartner.setPublicKey(partnerInfo.getPublicKey());
		}
		if (partnerInfo.getAliasKey() == null) {
			lotteryPartner.setAliasKey("");
		} else {
			lotteryPartner.setAliasKey(partnerInfo.getAliasKey());
		}
		if (partnerInfo.getKeyStore() == null) {
			lotteryPartner.setKeyStore("");
		} else {
			lotteryPartner.setKeyStore(partnerInfo.getKeyStore());
		}
		if (partnerInfo.getUserId() == null
				|| "".equals(partnerInfo.getUserId())) {
			lotteryPartner.setUserId(0);
		} else {
			try {

				lotteryPartner
						.setUserId(Long.parseLong(partnerInfo.getUserId()));
			} catch (NumberFormatException e) {

				pcResultObj.setMsg("用户Id格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}
		}

		if (partnerInfo.getPartnerState() == null) {
			lotteryPartner.setState(Short.parseShort("1"));
		} else {
			lotteryPartner.setState(partnerInfo.getPartnerState());
		}
		lotteryPartner.setExt("");
		
		
		PartnerIpAddress partnerIpAddress = null;
		List<PartnerIpAddress> partnerIpAddresses = new ArrayList<PartnerIpAddress>();
		if (StringUtils.isNotBlank(partnerInfo.getIpAddress())) {

			String[] ipAdsresses = partnerInfo.getIpAddress().split(",");
			
			for (int i = 0; i < ipAdsresses.length; i++) {
				partnerIpAddress = new PartnerIpAddress();
				partnerIpAddress.setPartnerId(partnerInfo.getPartnerId());
				partnerIpAddress.setIpAddress(ipAdsresses[i]);
				partnerIpAddresses.add(partnerIpAddress);
			}
			
		} 
		lotteryPartner.setPartnerIpAddressList(partnerIpAddresses);
		
		// 根据Id查询原对象
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partner", "updateLotteryPartner",
						lotteryPartner, partnerInfo.getPartnerId());
		
		if (reMsg.getObj() != null) {

			flag = (Integer)reMsg.getObj();

			if(flag == 1){
				if(partnerInfo.getAccountState()==null){
					partnerInfo.setAccountState(1);
				}
				reMsg = TransactionProcessor.dynamicInvoke(
						"partnerAccount", "updatePartnerAccountCreditLimit",
						partnerInfo.getPartnerId(),
						MoneyUtil.toCent(creditLimit));
				if(reMsg.getObj() != null){
					flag = (Integer)reMsg.getObj();
					if(flag ==1){
						
					}
				}else{
					flag = -1;
				}
			}
		}
		if (flag == 1) {

			pcResultObj.setMsg("修改成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			
			sendPartnerMQ(lotteryPartner);
		} else {

			pcResultObj.setMsg("修改失败");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		return pcResultObj;
	}

	private void sendPartnerMQ(LotteryPartner lotteryPartner) {
		ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
		ActivemqProducer activemqProducer = ctx.getBean("activemqProducer",
				ActivemqProducer.class);
		
		ActivemqSendObject partnerObject = new ActivemqSendObject();
		partnerObject.setObj(lotteryPartner);
		activemqProducer.send(partnerObject, ActivemqMethodUtil.MQ_PARTNERINFO_CHANGE_METHODID);
		Log.run.debug("sendPartnerMQ, partnerId: %s, secretKey: %s, publicKey: %s, alias: %s, keyStore: %s",  lotteryPartner.getPartnerId(), lotteryPartner.getSecretKey(), lotteryPartner.getPublicKey(), lotteryPartner.getAliasKey(), lotteryPartner.getKeyStore());
	}

	@Override
	public PcResultObj addPartnerRecharge(PartnerCharge partnerCharge) {
		PcResultObj pcResultObj = new PcResultObj();
		String partnerId = partnerCharge.getPartnerId();

		// 流水号问题
		if (getPartnerType(partnerId) != 0) {
			pcResultObj.setMsg("合作商类型错误或者不存在");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		if (!isLockPartner(partnerId)) {
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("合作商被冻结");
			return pcResultObj;
		}

		if (partnerCharge.getRechargeAmount() < 0.01) {
			pcResultObj.setMsg("充值金额数据错误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		PartnerRecharge partnerRecharge = new PartnerRecharge();

		partnerRecharge.setPartnerId(partnerId);
		partnerRecharge.setRechargeAmount(MoneyUtil.toCent(partnerCharge
				.getRechargeAmount()));
		partnerRecharge.setRechargeType("后台管理充值");
		partnerRecharge.setRemark(partnerRecharge.getRemark());
		partnerRecharge.setSerialNumber(IdWorker.getSerialNumber());
		com.cqfc.processor.ReturnMessage res = TransactionProcessor
				.dynamicInvoke("partnerAccount", "addPartnerRecharge",
						partnerRecharge);

		if (res.getObj() != null) {

			int flag = (Integer) res.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("充值成功");
			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("充值失败");
			}
		}
		return pcResultObj;
	}

	@Override
	public PcResultObj getSaleByIssue(PartnerIssueCount partnerIssueCount,
			int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		List<LotteryIssueSale> lotteryIssueSales = new ArrayList<LotteryIssueSale>();
		LotteryIssueSale lotteryIssueSale = new LotteryIssueSale();
		LotteryPartner lotteryPartner = new LotteryPartner();

		List<PartnerIssueCount> partnerIssueCounts = new ArrayList<PartnerIssueCount>();

		if (partnerIssueCount.getPartnerId() != null
				&& !"".equals(partnerIssueCount.getPartnerId())) {

			lotteryIssueSale.setPartnerId(partnerIssueCount.getPartnerId());
		}

		if (partnerIssueCount.getLotteryId() != null
				&& !"".equals(partnerIssueCount.getLotteryId())) {

			lotteryIssueSale.setLotteryId(partnerIssueCount.getLotteryId());
		}

		if (partnerIssueCount.getIssueNo() != null
				&& !"".equals(partnerIssueCount.getIssueNo())) {

			lotteryIssueSale.setIssueNo(partnerIssueCount.getIssueNo());
		}

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getLotteryIssueSaleByWhere",
						lotteryIssueSale, pageNum, pageSize);

		if (reMsg.getObj() != null) {

			PcLotteryIssueSale pcLotteryIssueSale = (PcLotteryIssueSale) reMsg
					.getObj();
			lotteryIssueSales = pcLotteryIssueSale.getLotteryIssueSale();
			for (int i = 0; i < lotteryIssueSales.size(); i++) {

				lotteryIssueSale = lotteryIssueSales.get(i);

				partnerIssueCount = new PartnerIssueCount();

				partnerIssueCount.setPartnerId(lotteryIssueSale.getPartnerId());
				partnerIssueCount.setIssueNo(lotteryIssueSale.getIssueNo());
				partnerIssueCount.setLotteryId(lotteryIssueSale.getLotteryId());

				reMsg = TransactionProcessor.dynamicInvoke("partner",
						"findLotteryPartnerById",
						lotteryIssueSale.getPartnerId());

				if (reMsg.getObj() != null) {
					lotteryPartner = (LotteryPartner) reMsg.getObj();
				}

				partnerIssueCount.setPartnerType(lotteryPartner
						.getPartnerType());

				partnerIssueCount.setSucMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSucMoney()));
				partnerIssueCount.setSucNum(lotteryIssueSale.getSucNum());
				partnerIssueCount.setFailMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getFailMoney()));
				partnerIssueCount.setFailNum(lotteryIssueSale.getFailNum());
				partnerIssueCount.setBigPrizeMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getBigPrizeMoney()));
				partnerIssueCount.setBigPrizeNum(lotteryIssueSale
						.getBigPrizeNum());
				partnerIssueCount.setSmallPrizeMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSmallPrizeMoney()));
				partnerIssueCount.setSmallPrizeNum(lotteryIssueSale
						.getSmallPrizeNum());
				partnerIssueCount.setPrizeTotalMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getBigPrizeMoney()
								+ lotteryIssueSale.getSmallPrizeMoney()));
				partnerIssueCount
						.setPrizeTotalNum(lotteryIssueSale.getBigPrizeNum()
								+ lotteryIssueSale.getSmallPrizeNum());
				partnerIssueCount.setSaleTotalMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSucMoney()
								+ lotteryIssueSale.getFailMoney()));
				partnerIssueCount.setSaleTotalNum(lotteryIssueSale.getSucNum()
						+ lotteryIssueSale.getFailNum());
				partnerIssueCounts.add(partnerIssueCount);

			}

			ResultObj resultObj = new ResultObj();

			resultObj.setObjects(partnerIssueCounts);
			resultObj.setRecordTotal(pcLotteryIssueSale.getTotalNum());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);

			return pcResultObj;
		}

		pcResultObj.setMsg("查询失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

		return pcResultObj;
	}

	@Override
	public PcResultObj getSaleByDay(PartnerDaySale partnerDaySale, int pageNum,
			int pageSize) {
		PcResultObj pcResultObj = new PcResultObj();

		LotteryDaySale lotteryDaySale = new LotteryDaySale();

		List<LotteryDaySale> lotteryDaySales = new ArrayList<LotteryDaySale>();

		List<PartnerDaySale> partnerDaySales = new ArrayList<PartnerDaySale>();

		LotteryPartner lotteryPartner = new LotteryPartner();

		if (partnerDaySale.getCountTime() != null
				&& !"".equals(partnerDaySale.getCountTime())) {

			lotteryDaySale.setCountTime(partnerDaySale.getCountTime());
		} else {

			lotteryDaySale.setCountTime(DateUtils.getPreDay(new Date(), -1));
		}

		if (partnerDaySale.getPartnerId() != null
				&& !"".equals(partnerDaySale.getPartnerId())) {

			lotteryDaySale.setPartnerId(partnerDaySale.getPartnerId());

		}

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getLotteryDaySaleByWhere",
						lotteryDaySale, 1, 10000);

		if (reMsg.getObj() != null) {
			lotteryDaySales = (List<LotteryDaySale>) reMsg.getObj();

			for (int i = 0; i < lotteryDaySales.size(); i++) {

				lotteryDaySale = lotteryDaySales.get(i);

				partnerDaySale = new PartnerDaySale();

				partnerDaySale.setAwardPrizeTotalMoney(MoneyUtil
						.toYuanStr(lotteryDaySale.getAwardPrizeTotalMoney()));
				partnerDaySale.setChargeTotalMoney(MoneyUtil
						.toYuanStr(lotteryDaySale.getChargeTotalMoney()));
				partnerDaySale.setSaleTotalMoney(MoneyUtil
						.toYuanStr(lotteryDaySale.getSaleTotalMoney()));
				partnerDaySale.setCountTime(lotteryDaySale.getCountTime());
		
				partnerDaySale.setWithDrawTotalMoney(MoneyUtil
						.toYuanStr(lotteryDaySale.getEncashTotalMoney()));
				partnerDaySale.setPartnerId(lotteryDaySale.getPartnerId());
//				reMsg = TransactionProcessor
//						.dynamicInvoke("partner", "findLotteryPartnerById",
//								lotteryDaySale.getPartnerId());
//
//				if (reMsg.getObj() != null) {
//					lotteryPartner = (LotteryPartner) reMsg.getObj();
//				}
//
//				partnerDaySale.setPartnerType(lotteryPartner.getPartnerType());

				partnerDaySales.add(partnerDaySale);
			}

			ResultObj resultObj = new ResultObj();
			resultObj.setObjects(partnerDaySales);
			resultObj.setRecordTotal(partnerDaySales.size());

			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			return pcResultObj;
		}
		pcResultObj.setMsg("查询失败");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);

		return pcResultObj;
	}

	@Override
	public int getPartnerType(String partnerId) {

		LotteryPartner partner = null;

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partner", "findLotteryPartnerById", partnerId);

		if (reMsg.getObj() != null) {
			partner = (LotteryPartner) reMsg.getObj();
		} else {
			return -1;
		}

		if (partner.getPartnerType() == PartnerConstant.PartnerType.B2B
				.getValue()
				|| partner.getPartnerType() == PartnerConstant.PartnerType.B2B2C
						.getValue()) {

			return 0;
		}

		if (partner.getPartnerType() == PartnerConstant.PartnerType.USERPARTNER
				.getValue()) {
			return 1;

		}

		return -1;
	}

	@Override
	public PcResultObj updatePartnerState(PartnerInfo partnerInfo) {

		PcResultObj pcResultObj = new PcResultObj();

		if (getPartnerType(partnerInfo.getPartnerId()) == -1) {
			pcResultObj.setMsg("合作商id不存在");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}
		if (partnerInfo.getPartnerState() == 0) {
			partnerInfo.setPartnerState(Short.parseShort("1"));
		}

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partner", "updateState",
						partnerInfo.getPartnerId(),
						(int) partnerInfo.getPartnerState());

		if (reMsg.getObj() != null) {

			int flag = (Integer) reMsg.getObj();

			if (flag == 1) {
				int normal = PartnerConstant.PartnerState.NORMAL.getValue();
				int lock = PartnerConstant.PartnerState.LOCK.getValue();
				int state = 1;
				if((int) partnerInfo.getPartnerState()==normal){
					state = lock;
				}else{
					state = normal;
				}
				reMsg = TransactionProcessor
						.dynamicInvoke("partnerAccount", "updatePartnerAccountState",
								partnerInfo.getPartnerId(),
								state);
				if(reMsg.getObj()!=null){
					
					flag = (Integer) reMsg.getObj();
					
					if(flag!=1){
						TransactionProcessor
						.dynamicInvoke("partner", "updateState",
								partnerInfo.getPartnerId(),
								state);
					}
				}
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("修改成功");
			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("修改失败");
			}

		}

		return pcResultObj;
	}

	@Override
	public PcResultObj issueSaleCount(String lotteryId, String issueNo) {
		PcResultObj pcResultObj = new PcResultObj();

		LotteryIssue lotteryIssue = new LotteryIssue();
		lotteryIssue.setLotteryId(lotteryId);
		lotteryIssue.setIssueNo(issueNo);

		lotteryIssue = lotteryIssueService.getLotteryIssue(lotteryIssue);
		if (lotteryIssue == null) {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("彩种期号不存在");
			return pcResultObj;
		} else {

			Date date = null;
			Date currentTime = new Date();
			try {
				date = DateUtils.stringToDateTwo(lotteryIssue.getDrawTime());

				if (DateUtils.ifPreDayOrToday(date, DateUtils
						.stringToDateTwo(DateUtils.getPreDay(currentTime, -6)))) {

					pcResultObj.setMsg("5天前的数据已被清理,无法统计");
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					return pcResultObj;
				}

			} catch (ParseException e) {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("系统错误");
				Log.run.error("期号开奖时间异常", e);
				return pcResultObj;
			}
		}

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerIssueSaleCount",
						lotteryId, issueNo);
		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj issueRewardCount(String lotteryId, String issueNo) {
		PcResultObj pcResultObj = new PcResultObj();
		LotteryIssue lotteryIssue = new LotteryIssue();
		lotteryIssue.setLotteryId(lotteryId);
		lotteryIssue.setIssueNo(issueNo);

		lotteryIssue = lotteryIssueService.getLotteryIssue(lotteryIssue);
		if (lotteryIssue == null) {
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("彩种期号不存在");
			return pcResultObj;
		}

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerIssueRewardCount",
						lotteryId, issueNo);
		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj dailySaleCount(String countTime) {
		PcResultObj pcResultObj = new PcResultObj();

		Date paramTime = new Date();
		Date currentTime = new Date();
		try {

			if (countTime == null || "".equals(countTime)) {

				pcResultObj.setMsg("统计时间格式错误");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;
			}

			paramTime = DateUtils.stringToDateTwo(countTime);
		} catch (ParseException e) {
			pcResultObj.setMsg("统计时间格式错误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		try {

			if (DateUtils.ifPreDayOrToday(paramTime, DateUtils
					.stringToDateTwo(DateUtils.getPreDay(currentTime, -6)))) {

				pcResultObj.setMsg("5天前的数据已被清理,无法统计");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				return pcResultObj;

			}

		} catch (ParseException e) {
			pcResultObj.setMsg("系统错误");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			return pcResultObj;
		}

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerDailySaleCount",
						countTime);

		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj dailyEncashCount(String countTime) {
		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerDailyEncashCount",
						countTime);
		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj dailyRewardCount(String countTime) {
		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerDailyAwardCount",
						countTime);
		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj dailyChargeCount(String countTime) {
		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerDailyChargeCount",
						countTime);
		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}

	
	
	@Override
	public PcResultObj dailyReport(String countTime) {
		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partnerOrder", "partnerDailyReport",
						countTime);
		if (returnMessage.getObj() != null) {

			int flag = (Integer) returnMessage.getObj();

			if (flag == 1) {
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				pcResultObj.setMsg("统计成功");

			} else {
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("统计失败");

			}

		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("统计失败");
		}

		return pcResultObj;
	}
	

	public static void main(String[] args) {
		System.out.println(MoneyUtil.toCent(Double.parseDouble("0.2356")));
	}

	public boolean isLockPartner(String partnerId) {

		boolean flag = true;

		com.cqfc.processor.ReturnMessage returnMessage = TransactionProcessor
				.dynamicInvoke("partner", "verifyPartnerIsExist", partnerId);

		if (returnMessage.getObj() != null) {

			flag = (Boolean) returnMessage.getObj();
		} else {
			flag = false;
		}

		return flag;
	}

	@Override
	public PcResultObj getOrderByWhere(OrderInfo orderInfo) {
		
		PcResultObj pcResultObj = new PcResultObj();
		

		return pcResultObj;
	}

	@Override
	public PcResultObj getHistoryIssueCount(PartnerIssueCount partnerIssueCount,int pageNum,int pageSize) {
	
		PcResultObj pcResultObj = new PcResultObj();
		
		LotteryIssueSale lotteryIssueSale = new LotteryIssueSale();
		
		
		lotteryIssueSale.setLotteryId(partnerIssueCount.getLotteryId());
		lotteryIssueSale.setIssueNo(partnerIssueCount.getIssueNo());
		lotteryIssueSale.setPartnerId(partnerIssueCount.getPartnerId());
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getLotteryIssueSaleByWhere",
						lotteryIssueSale, pageNum, pageSize);
		List<PartnerIssueCount> partnerIssueCounts = new ArrayList<PartnerIssueCount>();
		if (reMsg.getObj() != null) {

			PcLotteryIssueSale pcLotteryIssueSale = (PcLotteryIssueSale) reMsg
					.getObj();
			List<LotteryIssueSale> lotteryIssueSales = pcLotteryIssueSale.getLotteryIssueSale();
			for (int i = 0; i < lotteryIssueSales.size(); i++) {

				lotteryIssueSale = lotteryIssueSales.get(i);

				partnerIssueCount = new PartnerIssueCount();

				partnerIssueCount.setPartnerId(lotteryIssueSale.getPartnerId());
				partnerIssueCount.setIssueNo(lotteryIssueSale.getIssueNo());
				partnerIssueCount.setLotteryId(lotteryIssueSale.getLotteryId());

//				reMsg = TransactionProcessor.dynamicInvoke("partner",
//						"findLotteryPartnerById",
//						lotteryIssueSale.getPartnerId());
//
//				if (reMsg.getObj() != null) {
//					lotteryPartner = (LotteryPartner) reMsg.getObj();
//				}
//
//				partnerIssueCount.setPartnerType(lotteryPartner
//						.getPartnerType());

				partnerIssueCount.setSucMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSucMoney()));
				partnerIssueCount.setSucNum(lotteryIssueSale.getSucNum());
				partnerIssueCount.setFailMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getFailMoney()));
				partnerIssueCount.setFailNum(lotteryIssueSale.getFailNum());
				partnerIssueCount.setBigPrizeMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getBigPrizeMoney()));
				partnerIssueCount.setBigPrizeNum(lotteryIssueSale
						.getBigPrizeNum());
				partnerIssueCount.setSmallPrizeMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSmallPrizeMoney()));
				partnerIssueCount.setSmallPrizeNum(lotteryIssueSale
						.getSmallPrizeNum());
				partnerIssueCount.setPrizeTotalMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getBigPrizeMoney()
								+ lotteryIssueSale.getSmallPrizeMoney()));
				partnerIssueCount
						.setPrizeTotalNum(lotteryIssueSale.getBigPrizeNum()
								+ lotteryIssueSale.getSmallPrizeNum());
				partnerIssueCount.setAfterPrizeMoney(MoneyUtil
						.toYuanStr(Math.round(lotteryIssueSale.getBigPrizeMoney()*0.8
								+ lotteryIssueSale.getSmallPrizeMoney())));
				partnerIssueCount.setSaleTotalMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSucMoney()
								+ lotteryIssueSale.getFailMoney()));
				partnerIssueCount.setSaleTotalNum(lotteryIssueSale.getSucNum()
						+ lotteryIssueSale.getFailNum());
				partnerIssueCounts.add(partnerIssueCount);

			}
			
			ResultObj resultObj = new ResultObj();
			resultObj.setObjects(partnerIssueCounts);
			resultObj.setRecordTotal(pcLotteryIssueSale.getTotalNum());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功!");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			return pcResultObj;
			
		}
		
		pcResultObj.setMsg("查询失败!");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		return pcResultObj;
		
	}

	@Override
	public PcResultObj getCurrentIssueCount(PartnerIssueCount partnerIssueCount,String fromTime,String toTime) {
		PcResultObj pcResultObj = new  PcResultObj();
		
		
		LotteryIssueSale lotteryIssueSale = new LotteryIssueSale();
		
		
		com.cqfc.processor.ReturnMessage msg = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "findLotteryIssue",
						partnerIssueCount.getLotteryId(),"");
		LotteryIssue lotteryIssue = (LotteryIssue) msg.getObj();
		
		lotteryIssueSale.setLotteryId(partnerIssueCount.getLotteryId());
		lotteryIssueSale.setIssueNo(lotteryIssue.getIssueNo());
		lotteryIssueSale.setPartnerId(partnerIssueCount.getPartnerId());
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getCurrentIssueSaleByWhere",
						lotteryIssueSale, fromTime, toTime);
		List<PartnerIssueCount> partnerIssueCounts = new ArrayList<PartnerIssueCount>();
		
		if (reMsg.getObj() != null) {

			List<LotteryIssueSale> lotteryIssueSales = (List<LotteryIssueSale>) reMsg
					.getObj();
			for (int i = 0; i < lotteryIssueSales.size(); i++) {

				lotteryIssueSale = lotteryIssueSales.get(i);

				partnerIssueCount = new PartnerIssueCount();

				partnerIssueCount.setPartnerId(lotteryIssueSale.getPartnerId());
				partnerIssueCount.setIssueNo(lotteryIssueSale.getIssueNo());
				partnerIssueCount.setLotteryId(lotteryIssueSale.getLotteryId());

//				reMsg = TransactionProcessor.dynamicInvoke("partner",
//						"findLotteryPartnerById",
//						lotteryIssueSale.getPartnerId());
//
//				if (reMsg.getObj() != null) {
//					lotteryPartner = (LotteryPartner) reMsg.getObj();
//				}
//
//				partnerIssueCount.setPartnerType(lotteryPartner
//						.getPartnerType());

				partnerIssueCount.setSucMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSucMoney()));
				partnerIssueCount.setSucNum(lotteryIssueSale.getSucNum());
				partnerIssueCount.setFailMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getFailMoney()));
				partnerIssueCount.setFailNum(lotteryIssueSale.getFailNum());
				partnerIssueCount.setBigPrizeMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getBigPrizeMoney()));
				partnerIssueCount.setBigPrizeNum(lotteryIssueSale
						.getBigPrizeNum());
				partnerIssueCount.setSmallPrizeMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSmallPrizeMoney()));
				partnerIssueCount.setSmallPrizeNum(lotteryIssueSale
						.getSmallPrizeNum());
				partnerIssueCount.setPrizeTotalMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getBigPrizeMoney()
								+ lotteryIssueSale.getSmallPrizeMoney()));
				partnerIssueCount
						.setPrizeTotalNum(lotteryIssueSale.getBigPrizeNum()
								+ lotteryIssueSale.getSmallPrizeNum());
				partnerIssueCount.setSaleTotalMoney(MoneyUtil
						.toYuanStr(lotteryIssueSale.getSucMoney()
								+ lotteryIssueSale.getFailMoney()));
				partnerIssueCount.setSaleTotalNum(lotteryIssueSale.getSucNum()
						+ lotteryIssueSale.getFailNum());
				partnerIssueCounts.add(partnerIssueCount);

			}
			
			ResultObj resultObj = new ResultObj();
			resultObj.setObjects(partnerIssueCounts);
			resultObj.setRecordTotal(partnerIssueCounts.size());
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsg("查询成功!");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			return pcResultObj;
			
		}
		
		pcResultObj.setMsg("查询失败!");
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		return pcResultObj;
		
	}

	@Override
	public PcResultObj getDailyReport(DailySaleAndCharge dailySaleAndCharge,
			int pageNum, int pageSize) {
		
		PcResultObj pcResultObj = new PcResultObj();
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getDailyReportByWhere",
						dailySaleAndCharge, pageNum, pageSize);
		
		List<PartnerDayReport> partnerDayReports = new ArrayList<PartnerDayReport>();
		PartnerDayReport partnerDayReport = null;
		if(reMsg.getObj()!=null){
			
			PcDailyReport pcDailyReport  = (PcDailyReport) reMsg.getObj();
			
			ResultObj resultObj = new ResultObj();
			
			List<DailySaleAndCharge> dailySaleAndCharges = pcDailyReport.getDailySaleAndCharges();
			if(dailySaleAndCharges!=null){
				for (DailySaleAndCharge saleAndCharge: dailySaleAndCharges) {
					
					partnerDayReport = new PartnerDayReport();
					
					partnerDayReport.setLotteryId(saleAndCharge.getLotteryId());
					partnerDayReport.setTotalMoney(MoneyUtil.toYuanStr(saleAndCharge.getTotalMoney()));
					partnerDayReport.setCountTime(saleAndCharge.getCountTime());
					partnerDayReport.setAwardPrizeMoney(MoneyUtil.toYuanStr(saleAndCharge.getAwardPrizeMoney()));
					partnerDayReport.setAfterPrizeMoney(MoneyUtil.toYuanStr(saleAndCharge.getChargeTotalMoney()));;
					partnerDayReports.add(partnerDayReport);
				}
			}
			
			resultObj.setObjects(partnerDayReports);
			resultObj.setRecordTotal(pcDailyReport.getTotalNum());;
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
	public PcResultObj getOrder(OrderInfo orderInfo, int pageNum, int pageSize) {
		
		PcResultObj pcResultObj = new PcResultObj();

		com.cqfc.processor.ReturnMessage reMsg = new ReturnMessage();
		com.cqfc.protocol.partnerorder.Order partnerOrder = new Order();
	//	com.cqfc.protocol.userorder.Order userOrder = new com.cqfc.protocol.userorder.Order();
		try {
			
			if (StringUtils.isNotBlank(orderInfo.getTicketId())&&StringUtils.isBlank(orderInfo.getUserId())) {
			
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
					
					for (OrderInfo orderInfo2 :orderInfos) {
						orderInfo2.setOrderType(0);
						orderInfo2.setOrderNo("");
					}
					resultObj.setObjects(orderInfos);
					resultObj.setRecordTotal(pcPartnerOrder.getTotalNum());
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
	public PcResultObj getDaySaleDetails(DailySaleAndCharge dailySaleAndCharge,int pageNum,int pageSize) {
		PcResultObj pcResultObj = new PcResultObj();
		
		ResultObj resultObj = new ResultObj();
		ReturnMessage reMsg = TransactionProcessor.dynamicInvoke("partnerOrder",
				"getDailySaleDetails", dailySaleAndCharge,pageNum,pageSize);
		List<DaySaleOrAwardDetail> daySaleOrAwardDetails = new ArrayList<DaySaleOrAwardDetail>();
		if(reMsg.getObj()!=null){
			
			PcDaySaleDetails pcDaySaleDetails = (PcDaySaleDetails) reMsg.getObj();
			List<DailySaleAndCharge> dailySaleCounts = pcDaySaleDetails.getDaySaleDetails();
			DaySaleOrAwardDetail saleOrAwardDetail = null;
			for (DailySaleAndCharge dailySaleCount :dailySaleCounts) {
				saleOrAwardDetail = new DaySaleOrAwardDetail();
				saleOrAwardDetail.setLotteryId(dailySaleCount.getLotteryId());
				saleOrAwardDetail.setTotalMoney((MoneyUtil.toYuanStr(dailySaleCount.getTotalMoney())));
				saleOrAwardDetail.setAwardMoney((MoneyUtil.toYuanStr(dailySaleCount.getAwardPrizeMoney())));
				
				daySaleOrAwardDetails.add(saleOrAwardDetail);
			}
			
			
			resultObj.setObjects(daySaleOrAwardDetails);
			resultObj.setRecordTotal(pcDaySaleDetails.getTotalNum());
			
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("查询成功");
			return pcResultObj;
		}
		pcResultObj.setEntity(resultObj);
		pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		pcResultObj.setMsg("查询无记录");
		return pcResultObj;
	}


}
