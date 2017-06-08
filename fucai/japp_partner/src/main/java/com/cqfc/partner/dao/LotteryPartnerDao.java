package com.cqfc.partner.dao;

import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.dao.mapper.LotteryPartnerMapper;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.PartnerConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Repository
public class LotteryPartnerDao {

	@Autowired
	private LotteryPartnerMapper lotteryPartnerMapper;

	@Autowired
	private MemcachedClient memcachedClient;

	public ReturnData getLotteryPartnerList(LotteryPartner lotteryPartner, int currentPage, int pageSize) {
		ReturnData returnData = new ReturnData();

		try {
			StringBuffer strBuff = new StringBuffer();
			strBuff.append(" 1=1");

			if (null != lotteryPartner) {
				if (null != lotteryPartner.getPartnerName() && !"".equals(lotteryPartner.getPartnerName())) {
					strBuff.append(" and partnerName='" + lotteryPartner.getPartnerName() + "'");
				}
				if (null != lotteryPartner.getPartnerId() && !"".equals(lotteryPartner.getPartnerId())) {
					strBuff.append(" and partnerId='" + lotteryPartner.getPartnerId() + "'");
				}
				if (lotteryPartner.getPartnerType() == PartnerConstant.PartnerType.B2B.getValue()
						|| lotteryPartner.getPartnerType() == PartnerConstant.PartnerType.B2B2C.getValue()) {
					strBuff.append(" and (partnerType=" + PartnerConstant.PartnerType.B2B.getValue()
							+ " or partnerType=" + PartnerConstant.PartnerType.B2B2C.getValue() + ")");
				} else if (lotteryPartner.getPartnerType() == PartnerConstant.PartnerType.USERPARTNER.getValue()) {
					strBuff.append(" and partnerType=" + lotteryPartner.getPartnerType());
				}
			}

			int totalSize = countTotalSize(strBuff.toString());
			int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
			if (totalPage >= currentPage) {
				strBuff.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			}
			List<LotteryPartner> partnerList = lotteryPartnerMapper.getLotteryPartnerList(strBuff.toString());
			if (null != partnerList && partnerList.size() > 0) {
				for (LotteryPartner partner : partnerList) {
					List<PartnerIpAddress> ipAddressList = lotteryPartnerMapper.getPartnerIpAddressList(partner
							.getPartnerId());
					partner.setPartnerIpAddressList(ipAddressList);
				}
			}
			returnData.setCurrentPage(currentPage);
			returnData.setPageSize(pageSize);
			returnData.setTotalSize(totalSize);
			returnData.setResultList(partnerList);
		} catch (Exception e) {
			Log.run.error("获取合作渠道信息列表发生异常", e);
		}
		return returnData;
	}

	public int countTotalSize(String conditions) {
		return lotteryPartnerMapper.countTotalSize(conditions);
	}

	public int addLotteryPartner(LotteryPartner lotteryPartner) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = lotteryPartnerMapper.addLotteryPartner(lotteryPartner);
			if (returnValue != 1) {
				throw new DaoLevelException("-1");
			}
		} catch (DuplicateKeyException e) {
			Log.run.error("新增合作渠道信息发生唯一约束冲突", e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("新增合作渠道信息发生异常", e);
			throw new DaoLevelException("-1");
		}
		return returnValue;
	}

	public LotteryPartner findPartnerByPartnerId(String partnerId) {
		LotteryPartner lotteryPartner = null;
		try {
			String memKey = ConstantsUtil.MODULENAME_PARTNER + partnerId;
			lotteryPartner = memcachedClient.get(memKey);
			if (null == lotteryPartner || "".equals(lotteryPartner)) {
				lotteryPartner = lotteryPartnerMapper.findLotteryPartnerById(partnerId);
				if (null != lotteryPartner) {
					memcachedClient.set(memKey, 0, lotteryPartner);
				}
			}
		} catch (Exception e) {
			Log.run.error("根据渠道ID查询渠道合作商信息发生异常,partnerId=" + partnerId, e);
		}
		return lotteryPartner;
	}

	public int updateLotteryPartnerState(String partnerId, int state) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		int stateUpdate = PartnerConstant.PartnerState.NORMAL.getValue();
		try {
			if (state == PartnerConstant.PartnerState.NORMAL.getValue()) {
				stateUpdate = PartnerConstant.PartnerState.LOCK.getValue();
			} else if (state == PartnerConstant.PartnerState.LOCK.getValue()) {
				stateUpdate = PartnerConstant.PartnerState.NORMAL.getValue();
			}
			returnValue = lotteryPartnerMapper.updateLotteryPartnerState(partnerId, state, stateUpdate);
			if (returnValue == 1) {
				String memKey = ConstantsUtil.MODULENAME_PARTNER + partnerId;
				LotteryPartner lotterPartner = memcachedClient.get(memKey);
				if (null != lotterPartner) {
					lotterPartner.setState((short) stateUpdate);
					memcachedClient.set(memKey, 0, lotterPartner);
				}
			}
		} catch (Exception e) {
			Log.run.error("更新渠道商信息状态发生异常,partnerId=" + partnerId, e);
			return returnValue;
		}
		return returnValue;
	}

	public List<PartnerIpAddress> getPartnerIpAddressList(String partnerId) {
		List<PartnerIpAddress> ipAddressList = null;
		try {
			String memKey = ConstantsUtil.MODULENAME_PARTNER + "ipAddress" + partnerId;
			ipAddressList = memcachedClient.get(memKey);
			if (null == ipAddressList || "".equals(ipAddressList)) {
				ipAddressList = lotteryPartnerMapper.getPartnerIpAddressList(partnerId);
				if (null != ipAddressList) {
					memcachedClient.set(memKey, 0, ipAddressList);
				}
			}
		} catch (Exception e) {
			Log.run.error("根据partnerId查询所有IP地址发生异常", e);
		}
		return ipAddressList;
	}

	public int addPartnerIpAddress(PartnerIpAddress partnerIpAddress) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = lotteryPartnerMapper.addPartnerIpAddress(partnerIpAddress);
			if (returnValue != 1) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			String partnerId = partnerIpAddress.getPartnerId();
			String memKey = ConstantsUtil.MODULENAME_PARTNER + "ipAddress" + partnerId;
			boolean del = memcachedClient.delete(memKey);
			Log.run.debug("新增渠道商IP地址,partnerId=%s,memcachDel=%b", partnerId, del);
		} catch (Exception e) {
			Log.run.error("新增渠道商IP地址发生异常", e);
			throw new DaoLevelException("0");
		}
		return returnValue;
	}

	public int updatePartnerIpAddress(PartnerIpAddress partnerIpAddress) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerMapper.updatePartnerIpAddress(partnerIpAddress);
			if (isSuccess > 0) {
				String partnerId = partnerIpAddress.getPartnerId();
				String memKey = ConstantsUtil.MODULENAME_PARTNER + "ipAddress" + partnerId;
				boolean del = memcachedClient.delete(memKey);
				Log.run.debug("修改渠道商IP地址,partnerId=%s,memcachDel=%b", partnerId, del);
			}
		} catch (Exception e) {
			Log.run.error("新增渠道商IP地址发生异常", e);
		}
		return isSuccess;
	}

	public int deletePartnerIpAddressByIp(int id, String partnerId) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerMapper.deletePartnerIpAddressById(id);
			if (isSuccess > 0) {
				String memKey = ConstantsUtil.MODULENAME_PARTNER + "ipAddress" + partnerId;
				memcachedClient.delete(memKey);
			}
		} catch (Exception e) {
			Log.run.error("删除渠道商IP地址发生异常", e);
		}
		return isSuccess;
	}

	public PartnerIpAddress isExistIpAddress(String partnerId, String ipAddress) {
		String conditions = " partnerId = '" + partnerId + "' and ipAddress = '" + ipAddress + "'";
		return lotteryPartnerMapper.findPartnerIpAddress(conditions);
	}

	public PartnerIpAddress findPartnerIpAddressById(int id) {
		PartnerIpAddress ipAddress = null;
		try {
			ipAddress = lotteryPartnerMapper.findIpAddressById(id);
		} catch (Exception e) {
			Log.run.error("查询渠道商IP地址发生异常", e);
		}
		return ipAddress;
	}

	public int updateLotteryPartner(LotteryPartner lotteryPartner, String oldPartnerId) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = lotteryPartnerMapper.updateLotteryPartner(lotteryPartner, oldPartnerId);
			if (returnValue != 1) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			String memKey = ConstantsUtil.MODULENAME_PARTNER + oldPartnerId;
			memcachedClient.delete(memKey);
			Log.run.debug("updatePartner,partnerId=%s,returnValue=%d", oldPartnerId, returnValue);
		} catch (DuplicateKeyException e) {
			Log.run.error("修改渠道商信息发生主键冲突,被修改渠道ID：" + oldPartnerId + ",异常信息：" + e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("修改渠道商信息发生异常,被修改渠道ID：" + oldPartnerId + ",异常信息：" + e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	public int deleteIpAddress(String partnerId) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerMapper.deleteIpAddressByPartnerId(partnerId);
			if (isSuccess > 0) {
				String memKey = ConstantsUtil.MODULENAME_PARTNER + "ipAddress" + partnerId;
				memcachedClient.delete(memKey);
			}
		} catch (Exception e) {
			Log.run.error("deleteIpAddressByPartnerId error,partnerId=" + partnerId, e);
			throw new DaoLevelException("0");
		}
		return isSuccess;
	}

	public int deletePartnerByPartnerId(String partnerId) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryPartnerMapper.deletePartnerByPartnerId(partnerId);
			if (isSuccess != 1) {
				throw new DaoLevelException(String.valueOf(-1));
			}
			String memKey = ConstantsUtil.MODULENAME_PARTNER + partnerId;
			memcachedClient.delete(memKey);
		} catch (Exception e) {
			Log.run.error("deletePartnerByPartnerId error,partnerId=" + partnerId, e);
			throw new DaoLevelException("0");
		}
		return isSuccess;
	}
}
