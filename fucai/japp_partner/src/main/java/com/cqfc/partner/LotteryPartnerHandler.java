package com.cqfc.partner;

import java.util.List;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.partner.service.ILotteryPartnerService;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.cqfc.protocol.partner.PartnerService;
import com.cqfc.protocol.partner.ReturnData;
import com.jami.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class LotteryPartnerHandler implements PartnerService.Iface {

	@Resource(name = "lotteryPartnerServiceImpl")
	private ILotteryPartnerService lotteryPartnerService;

	@Override
	public ReturnData getLotteryPartnerList(LotteryPartner lotteryPartner, int currentPage, int pageSize)
			throws TException {
		ReturnData returnData = null;
		try {
			setDynamic(DbGenerator.SLAVE);
			returnData = lotteryPartnerService.getLotteryPartnerList(lotteryPartner, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("查询渠道商信息列表发生异常", e);
			return null;
		}
		return returnData;
	}

	@Override
	public int addLotteryPartner(LotteryPartner lotteryPartner) throws TException {
		int isSuccess = 0;
		try {
			setDynamic(DbGenerator.MASTER);
			isSuccess = lotteryPartnerService.addLotteryPartner(lotteryPartner);
		} catch (Exception e) {
			Log.run.error("新增渠道商信息发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public LotteryPartner findLotteryPartnerById(String partnerId) throws TException {
		LotteryPartner partner = null;
		try {
			setDynamic(DbGenerator.SLAVE);
			partner = lotteryPartnerService.findLotteryPartnerById(partnerId);
			Log.run.debug("查询渠道商信息,partner=%s", partner);
		} catch (Exception e) {
			Log.run.error("查询渠道商信息发生异常", e);
			return null;
		}
		return partner;
	}

	@Override
	public int updateState(String partnerId, int state) throws TException {
		int isSuccess = 0;
		try {
			setDynamic(DbGenerator.MASTER);
			isSuccess = lotteryPartnerService.updateState(partnerId, state);
		} catch (Exception e) {
			Log.run.error("修改渠道商状态发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public boolean isPartnerAccount(String partnerId) throws TException {
		boolean flag = false;
		try {
			setDynamic(DbGenerator.SLAVE);
			flag = lotteryPartnerService.isPartnerAccount(partnerId);
		} catch (Exception e) {
			Log.run.error("校验是否为渠道账户发生异常", e);
			return false;
		}
		return flag;
	}

	@Override
	public int updateLotteryPartner(LotteryPartner lotteryPartner, String oldPartnerId) throws TException {
		int isSuccess = 0;
		try {
			setDynamic(DbGenerator.MASTER);
			isSuccess = lotteryPartnerService.updateLotteryPartner(lotteryPartner, oldPartnerId);
		} catch (Exception e) {
			Log.run.error("更新渠道商信息发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public List<PartnerIpAddress> getPartnerIpAddressList(String partnerId) throws TException {
		List<PartnerIpAddress> ipAddressList = null;
		try {
			setDynamic(DbGenerator.SLAVE);
			ipAddressList = lotteryPartnerService.getPartnerIpAddressList(partnerId);
			Log.run.debug("getPartnerIpAddressList,partnerId=%s,ipList=%s", partnerId, ipAddressList);
		} catch (Exception e) {
			Log.run.error("根据渠道商ID查询所有IP地址发生异常", e);
		}
		return ipAddressList;
	}

	@Override
	public boolean isExistIpAddress(String partnerId, String ipAddress) throws TException {
		boolean flag = false;
		try {
			setDynamic(DbGenerator.SLAVE);
			flag = lotteryPartnerService.isExistIpAddress(partnerId, ipAddress);
		} catch (Exception e) {
			Log.run.error("查询渠道商IP地址是否存在发生异常", e);
			return false;
		}
		return flag;
	}

	@Override
	public boolean verifyPartnerIsExist(String partnerId) throws TException {
		boolean flag = false;
		try {
			setDynamic(DbGenerator.SLAVE);
			flag = lotteryPartnerService.verifyPartnerIsExist(partnerId);
		} catch (Exception e) {
			Log.run.error("校验渠道商是否存在发生异常", e);
			return false;
		}
		return flag;
	}

	@Override
	public boolean verifyCanBindUser(String partnerId) throws TException {
		boolean flag = false;
		try {
			setDynamic(DbGenerator.SLAVE);
			flag = lotteryPartnerService.verifyCanBindUser(partnerId);
		} catch (Exception e) {
			Log.run.error("判断渠道商是否可以绑定用户发生异常", e);
			return false;
		}
		return flag;
	}

	@Override
	public LotteryPartner findPartnerForCheck(String partnerId) throws TException {
		LotteryPartner partner = null;
		try {
			setDynamic(DbGenerator.SLAVE);
			partner = lotteryPartnerService.findPartnerForCheck(partnerId);
		} catch (Exception e) {
			Log.run.error("查询渠道商信息(校验使用)发生异常", e);
			return null;
		}
		return partner;
	}

	private void setDynamic(String masterOrSlave) {
		DbGenerator.setDynamicDataSource(masterOrSlave);
	}

	@Override
	public List<String> getCallBackUrlListByPartnerId(String partnerId) throws TException {
		setDynamic(DbGenerator.SLAVE);
		return lotteryPartnerService.getCallBackUrlListByPartnerId(partnerId);
	}

	@Override
	public int deletePartnerByPartnerId(String partnerId) throws TException {
		setDynamic(DbGenerator.MASTER);
		return lotteryPartnerService.deletePartnerByPartnerId(partnerId);
	}

	// @Override
	// public int addPartnerIpAddress(PartnerIpAddress partnerIpAddress) throws
	// TException {
	// int isSuccess = 0;
	// try {
	// setDynamic(DbGenerator.MASTER);
	// isSuccess = lotteryPartnerService.addPartnerIpAddress(partnerIpAddress);
	// } catch (Exception e) {
	// Log.run.error("新增渠道商IP地址发生异常", e);
	// return 0;
	// }
	// return isSuccess;
	// }

	// @Override
	// public int updatePartnerIpAddress(PartnerIpAddress partnerIpAddress)
	// throws TException {
	// int isSuccess = 0;
	// try {
	// setDynamic(DbGenerator.MASTER);
	// isSuccess =
	// lotteryPartnerService.updatePartnerIpAddress(partnerIpAddress);
	// } catch (Exception e) {
	// Log.run.error("修改渠道商IP地址发生异常", e);
	// return 0;
	// }
	// return isSuccess;
	// }

	// @Override
	// public int deletePartnerIpAddressByIp(int id, String partnerId) throws
	// TException {
	// int isSuccess = 0;
	// try {
	// setDynamic(DbGenerator.MASTER);
	// isSuccess = lotteryPartnerService.deletePartnerIpAddressByIp(id,
	// partnerId);
	// } catch (Exception e) {
	// Log.run.error("删除渠道商IP地址发生异常", e);
	// return 0;
	// }
	// return isSuccess;
	// }

	// @Override
	// public PartnerIpAddress findPartnerIpAddressById(int id) throws
	// TException {
	// PartnerIpAddress ipAddress = null;
	// try {
	// setDynamic(DbGenerator.SLAVE);
	// ipAddress = lotteryPartnerService.findPartnerIpAddressById(id);
	// } catch (Exception e) {
	// Log.run.error("查询渠道商IP地址发生异常", e);
	// return null;
	// }
	// return ipAddress;
	// }

	// @Override
	// public int updateIpAddress(String partnerId, List<PartnerIpAddress>
	// ipList) throws TException {
	// setDynamic(DbGenerator.MASTER);
	// return lotteryPartnerService.updateIpAddress(partnerId, ipList);
	// }

}
