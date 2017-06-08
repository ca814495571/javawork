package com.cqfc.partner.service;

import java.util.List;

import org.apache.thrift.TException;

import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.cqfc.protocol.partner.ReturnData;

/**
 * @author liwh
 */
public interface ILotteryPartnerService {

	/**
	 * 查询渠道商信息列表
	 * 
	 * @param lotteryPartner
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public ReturnData getLotteryPartnerList(LotteryPartner lotteryPartner, int currentPage, int pageSize);

	/**
	 * 新增渠道商信息
	 * 
	 * @param lotteryPartner
	 * @return
	 */
	public int addLotteryPartner(LotteryPartner lotteryPartner);

	/**
	 * 查询渠道商信息
	 * 
	 * @param partnerId
	 * @return
	 */
	public LotteryPartner findLotteryPartnerById(String partnerId);

	/**
	 * 修改渠道商状态
	 * 
	 * @param partnerId
	 * @param state
	 * @return
	 */
	public int updateState(String partnerId, int state);

	/**
	 * 根据渠道商ID查询所有IP地址
	 * 
	 * @param partnerId
	 * @return
	 */
	public List<PartnerIpAddress> getPartnerIpAddressList(String partnerId);

	/**
	 * 新增渠道商IP地址
	 * 
	 * @param partnerIpAddress
	 * @return
	 */
	public int addPartnerIpAddress(PartnerIpAddress partnerIpAddress);

	/**
	 * 修改渠道商IP地址
	 * 
	 * @param partnerIpAddress
	 * @return
	 */
	public int updatePartnerIpAddress(PartnerIpAddress partnerIpAddress);

	/**
	 * 删除渠道商IP地址
	 * 
	 * @param id
	 * @param partnerId
	 * @return
	 */
	public int deletePartnerIpAddressByIp(int id, String partnerId);

	/**
	 * 查询渠道商IP地址是否存在
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param ipAddress
	 *            ip地址
	 * @return
	 */
	public boolean isExistIpAddress(String partnerId, String ipAddress);

	/**
	 * 查询渠道商IP地址
	 * 
	 * @param id
	 *            ip地址ID
	 * @return
	 */
	public PartnerIpAddress findPartnerIpAddressById(int id);

	/**
	 * 校验渠道商类型：1、B2B合作商 2、B2B2C合作商 3、行业合作商
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @return
	 */
	public boolean isPartnerAccount(String partnerId);

	/**
	 * 修改渠道商信息
	 * 
	 * @param lotteryPartner
	 * @param oldPartnerId
	 *            原渠道ID
	 * @return
	 */
	public int updateLotteryPartner(LotteryPartner lotteryPartner, String oldPartnerId);

	/**
	 * 校验渠道商是否存在
	 * 
	 * @param partnerId
	 * @return
	 */
	public boolean verifyPartnerIsExist(String partnerId);

	/**
	 * 判断渠道商是否可以绑定用户
	 * 
	 * @param partnerId
	 * @return
	 */
	public boolean verifyCanBindUser(String partnerId);

	/**
	 * 查询渠道商信息(校验使用)
	 * 
	 * @param partnerId
	 * @return
	 * @throws TException
	 */
	public LotteryPartner findPartnerForCheck(String partnerId);

	public List<String> getCallBackUrlListByPartnerId(String partnerId);

	public int updateIpAddress(String partnerId, List<PartnerIpAddress> ipList);
	
	public int deletePartnerByPartnerId(String partnerId);

}
