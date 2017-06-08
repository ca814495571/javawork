package com.cqfc.partner.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.partner.dao.LotteryPartnerDao;
import com.cqfc.partner.service.ILotteryPartnerService;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.util.PartnerConstant;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class LotteryPartnerServiceImpl implements ILotteryPartnerService {

	@Resource
	LotteryPartnerDao lotteryPartnerDao;

	@Override
	public ReturnData getLotteryPartnerList(LotteryPartner lotteryPartner, int currentPage, int pageSize) {
		ReturnData returnData = null;
		try {
			returnData = lotteryPartnerDao.getLotteryPartnerList(lotteryPartner, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("查询渠道商信息列表发生异常", e);
		}
		return returnData;
	}

	@Override
	@Transactional
	public int addLotteryPartner(LotteryPartner lotteryPartner) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerDao.addLotteryPartner(lotteryPartner);
			List<PartnerIpAddress> ipList = lotteryPartner.getPartnerIpAddressList();
			if (null != ipList && ipList.size() > 0) {
				for (PartnerIpAddress ipAddress : ipList) {
					lotteryPartnerDao.addPartnerIpAddress(ipAddress);
				}
			}
		} catch (Exception e) {
			Log.run.error("新增渠道商信息发生异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return isSuccess;
	}

	@Override
	public LotteryPartner findLotteryPartnerById(String partnerId) {
		LotteryPartner lotteryPartner = null;
		try {
			lotteryPartner = lotteryPartnerDao.findPartnerByPartnerId(partnerId);
			if (null != lotteryPartner || "".equals(lotteryPartner)) {
				List<PartnerIpAddress> ipList = lotteryPartnerDao.getPartnerIpAddressList(partnerId);
				lotteryPartner.setPartnerIpAddressList(ipList);
				Log.run.debug("findPartner partnerIpAddress,partnerId=%s,ipList=%s", partnerId, ipList);
			}
		} catch (Exception e) {
			Log.run.error("查询渠道商信息发生异常", e);
		}
		return lotteryPartner;
	}

	@Override
	public int updateState(String partnerId, int state) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerDao.updateLotteryPartnerState(partnerId, state);
		} catch (Exception e) {
			Log.run.error("修改渠道商状态发生异常", e);
		}
		return isSuccess;
	}

	@Override
	public List<PartnerIpAddress> getPartnerIpAddressList(String partnerId) {
		List<PartnerIpAddress> partnerIpAddressList = null;
		try {
			if (null != partnerId && !"".equals(partnerId)) {
				partnerIpAddressList = lotteryPartnerDao.getPartnerIpAddressList(partnerId);
			}
		} catch (Exception e) {
			Log.run.error("根据渠道商ID查询所有IP地址发生异常", e);
		}
		return partnerIpAddressList;
	}

	@Override
	public int addPartnerIpAddress(PartnerIpAddress partnerIpAddress) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerDao.addPartnerIpAddress(partnerIpAddress);
		} catch (Exception e) {
			Log.run.error("新增渠道商IP地址发生异常", e);
		}
		return isSuccess;
	}

	@Override
	public int updatePartnerIpAddress(PartnerIpAddress partnerIpAddress) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerDao.updatePartnerIpAddress(partnerIpAddress);
		} catch (Exception e) {
			Log.run.error("修改渠道商IP地址发生异常", e);
		}
		return isSuccess;
	}

	@Override
	public int deletePartnerIpAddressByIp(int id, String partnerId) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerDao.deletePartnerIpAddressByIp(id, partnerId);
		} catch (Exception e) {
			Log.run.error("删除渠道商IP地址发生异常", e);
		}
		return isSuccess;
	}

	@Override
	public boolean isExistIpAddress(String partnerId, String ipAddress) {
		boolean flag = false;
		try {
			PartnerIpAddress partnerIpAddress = null;
			if (null != partnerId && !"".equals(partnerId) && null != ipAddress && !"".equals(ipAddress)) {
				partnerIpAddress = lotteryPartnerDao.isExistIpAddress(partnerId, ipAddress);
			}
			if (null != partnerIpAddress) {
				flag = true;
			}
		} catch (Exception e) {
			Log.run.error("查询渠道商IP地址是否存在发生异常", e);
		}
		return flag;
	}

	@Override
	public PartnerIpAddress findPartnerIpAddressById(int id) {
		PartnerIpAddress partnerIpAddress = null;
		try {
			if (id > 0) {
				partnerIpAddress = lotteryPartnerDao.findPartnerIpAddressById(id);
			}
		} catch (Exception e) {
			Log.run.error("查询渠道商IP地址发生异常", e);
		}
		return partnerIpAddress;
	}

	@Override
	public boolean isPartnerAccount(String partnerId) {
		boolean flag = true;
		try {
			LotteryPartner partner = lotteryPartnerDao.findPartnerByPartnerId(partnerId);
			if (partner.getPartnerType() == PartnerConstant.PartnerType.USERPARTNER.getValue()) {
				flag = false;
			}
		} catch (Exception e) {
			Log.run.error("校验渠道商类型发生异常", e);
		}
		return flag;
	}

	@Override
	@Transactional
	public int updateLotteryPartner(LotteryPartner lotteryPartner, String oldPartnerId) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerDao.updateLotteryPartner(lotteryPartner, oldPartnerId);
			lotteryPartnerDao.deleteIpAddress(oldPartnerId);
			List<PartnerIpAddress> ipList = lotteryPartner.getPartnerIpAddressList();
			for (PartnerIpAddress ipAddress : ipList) {
				lotteryPartnerDao.addPartnerIpAddress(ipAddress);
			}
		} catch (Exception e) {
			Log.run.error("修改渠道商信息发生异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public boolean verifyPartnerIsExist(String partnerId) {
		boolean flag = false;
		try {
			LotteryPartner partner = lotteryPartnerDao.findPartnerByPartnerId(partnerId);
			if (null != partner && !"".equals(partner)
					&& PartnerConstant.PartnerState.NORMAL.getValue() == partner.getState()) {
				flag = true;
			}
		} catch (Exception e) {
			Log.run.error("校验渠道商是否存在发生异常", e);
		}
		return flag;
	}

	@Override
	public boolean verifyCanBindUser(String partnerId) {
		boolean flag = true;
		try {
			LotteryPartner partner = lotteryPartnerDao.findPartnerByPartnerId(partnerId);
			if (null != partner && !"".equals(partner)
					&& partner.getPartnerType() == PartnerConstant.PartnerType.B2B.getValue()) {
				flag = false;
			}
		} catch (Exception e) {
			Log.run.error("判断渠道商是否可以绑定用户发生异常", e);
		}
		return flag;
	}

	@Override
	public LotteryPartner findPartnerForCheck(String partnerId) {
		LotteryPartner lotteryPartner = null;
		try {
			lotteryPartner = lotteryPartnerDao.findPartnerByPartnerId(partnerId);
		} catch (Exception e) {
			Log.run.error("查询渠道商信息(校验使用)发生异常", e);
			return null;
		}
		return lotteryPartner;
	}

	@Override
	public List<String> getCallBackUrlListByPartnerId(String partnerId) {
		List<String> urlList = null;
		try {
			LotteryPartner lotteryPartner = lotteryPartnerDao.findPartnerByPartnerId(partnerId);
			String callBackUrl = lotteryPartner.getCallbackUrl();
			if (null != callBackUrl && !"".equals(callBackUrl)) {
				urlList = new ArrayList<String>();
				urlList.add(callBackUrl);
			}
		} catch (Exception e) {
			Log.run.error("获取回调url发生异常,partnerId=" + partnerId, e);
		}
		return urlList;
	}

	@Override
	@Transactional
	public int updateIpAddress(String partnerId, List<PartnerIpAddress> ipList) {
		try {
			lotteryPartnerDao.deleteIpAddress(partnerId);
			for (PartnerIpAddress ipAddress : ipList) {
				lotteryPartnerDao.addPartnerIpAddress(ipAddress);
			}
			return 1;
		} catch (Exception e) {
			Log.run.error("updateIpAddress error", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return -1;
		}
	}

	@Override
	@Transactional
	public int deletePartnerByPartnerId(String partnerId) {
		try {
			lotteryPartnerDao.deleteIpAddress(partnerId);
			lotteryPartnerDao.deletePartnerByPartnerId(partnerId);
			return 1;
		} catch (Exception e) {
			Log.run.error("deletePartnerByPartnerId error", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return -1;
		}
	}
}
