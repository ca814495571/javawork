package com.cqfc.management.service;

import java.util.List;

import com.cqfc.management.model.AccountInfo;
import com.cqfc.management.model.AccountLog;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.UserDepositCheck;
import com.cqfc.management.model.UserWithDrawCheck;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.userorder.Order;

public interface IUserService {

	/**
	 * 根据条件查询用户列表信息
	 * 
	 * @param userInfo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PcResultObj getUsers(UserInfo userInfo, int pageNum, int pageSize);

	/**
	 * 根据用户Id查询用户信息
	 * 
	 * @param id
	 * @return
	 */
	PcResultObj getUserById(long id);

	/**
	 * 查询用户提现信息
	 * 
	 * @return
	 */
	PcResultObj getUserWithdrawApply(UserWithDrawCheck userWithDrawCheck,int pageNum ,int pageSize);

	
	/**
	 * 审核提现记录（同意）
	 * @param id
	 * @return
	 */
	PcResultObj checkWithdrawSuc(int id);
	
	/**
	 * 审核提现记录（不同意）
	 * @param id
	 * @return
	 */
	PcResultObj checkWithdrawFail(int id);
	

	
	/**
	 * 根据条件查询账户信息
	 * @return
	 */
	PcResultObj getAccountInfoByWhere(AccountInfo userAccount);
	
	
	
	/**
	 * 根据条件查询流水日志信息
	 */
	PcResultObj getLogInfoByWhere(AccountLog userAccountLog ,String fromTime, String toTime ,int pageNum ,int pageSize);
	
	
	
	/**
	 * 根据条件查询预存款信息
	 * @param status
	 * @return
	 */
	PcResultObj getUserPreSave(UserDepositCheck userPreApply,int pageNum ,int pageSize);


	/**
	 * 审核预存款信息(同意)
	 * @param status
	 * @return
	 */
	PcResultObj CheckUserPreSaveAgree(int applyId);
	
	/**
	 * 审核预存款信息(不同意)
	 * @param status
	 * @return
	 */
	PcResultObj CheckUserPreSaveDisAgree(int applyId);
	
	/**
	 * 根据条件查询订单
	 * @param orderInfo
	 * @return
	 */
	public PcResultObj getOrderByWhere(OrderInfo orderInfo);
}
