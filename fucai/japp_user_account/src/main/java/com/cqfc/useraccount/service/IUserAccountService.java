package com.cqfc.useraccount.service;

import java.util.List;
import java.util.Map;

import com.cqfc.protocol.useraccount.UserAccount;
import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserAccountLogData;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.protocol.useraccount.UserHandselCount;
import com.cqfc.protocol.useraccount.UserHandselData;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.UserInfoData;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.cqfc.protocol.useraccount.UserPreApplyData;
import com.cqfc.protocol.useraccount.UserRecharge;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.protocol.useraccount.WithdrawApplyData;

/**
 * @author liwh
 */
public interface IUserAccountService {

	/**
	 * 检查用户是否已存在，存在则返回true
	 * 
	 * @param userInfo
	 *            用户信息
	 * @return 返回是否存在
	 */
	public boolean checkUserExist(UserInfo userInfo);

	/**
	 * 生成 用户ID
	 * 
	 * @param userInfo
	 *            用户信息
	 * @return
	 * @throws Exception
	 */
	public long genUserId(UserInfo userInfo) throws Exception;

	/**
	 * 创建用户资料并创建账户
	 * 
	 * @param userInfo
	 * @return
	 */
	public long createUserInfo(UserInfo userInfo);

	/**
	 * 分页查询用户信息
	 * 
	 * @param userInfo
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public UserInfoData getUserInfoList(UserInfo userInfo, int currentPage, int pageSize);

	/**
	 * 根据用户ID查询用户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public UserInfo findUserInfoById(long userId);

	/**
	 * 根据用户ID查询账户信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public UserAccount findUserAccountByUserId(long userId);

	/**
	 * 冻结、解冻账户
	 * 
	 * @param userId
	 *            用户ID
	 * @param state
	 *            状态：1正常 2冻结
	 * @return
	 */
	public int modifyUserAccountState(long userId, int state);

	/**
	 * 彩金赠送(写流水日志)
	 * 
	 * @param userHandsel
	 * @return
	 */
	public int createUserHandsel(UserHandsel userHandsel);

	/**
	 * 分页查询彩金列表
	 * 
	 * @param userHandsel
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public UserHandselData getUserHandselList(UserHandsel userHandsel, int currentPage, int pageSize);

	/**
	 * 通过用户ID查询彩金列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param state
	 *            状态：1有效 2无效 0查询所有
	 * @return
	 */
	public List<UserHandsel> getUserHandselListByUserId(long userId, int state);

	/**
	 * 彩金状态修改(判断时间是否失效)
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public int modifyUserHandselState(long userId);

	/**
	 * 查询用户可使用彩金列表（按过期时间升序）
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<UserHandsel> getUsableUserHandselList(long userId);

	/**
	 * 创建提款帐号
	 * 
	 * @param withdrawAccount
	 * @return
	 */
	public int createWithdrawAccount(WithdrawAccount withdrawAccount);

	/**
	 * 修改提款帐号
	 * 
	 * @param withdrawAccount
	 * @return
	 */
	@Deprecated
	public int updateWithdrawAccount(WithdrawAccount withdrawAccount);

	/**
	 * 查询用户提款帐号列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<WithdrawAccount> getWithdrawAccountListByUserId(long userId);

	/**
	 * 创建提现记录
	 * 
	 * @param withdrawApply
	 * @return
	 */
	public int createWithdrawApply(WithdrawApply withdrawApply);

	/**
	 * 根据提现ID查询提现记录
	 * 
	 * @param applyId
	 *            申请提现ID
	 * @return
	 */
	public WithdrawApply findWithdrawApplyByApplyId(int applyId);

	/**
	 * 根据用户ID、流水号查询提现记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            提现流水号
	 * @return
	 */
	public WithdrawApply findWithdrawApply(long userId, String serialNumber);

	/**
	 * 根据用户ID查询提现列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<WithdrawApply> getWithdrawApplyListByUserId(long userId);

	/**
	 * 提现审核（用户ID、流水号、审核状态、审核人ID、审核人、审核备注）、成功:处理金额
	 * 
	 * @param withdrawApply
	 * @return
	 */
	public int auditWithdrawApply(WithdrawApply withdrawApply);

	/**
	 * 创建充值记录(金额、流水处理)
	 * 
	 * @param userRecharge
	 * @return
	 */
	public int createUserRecharge(UserRecharge userRecharge);

	/**
	 * 查询用户充值记录列表
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	public List<UserRecharge> getUserRechargeList(long userId);

	/**
	 * 分页查询流水日志
	 * 
	 * @param userAccountLog
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public UserAccountLogData getUserAccountLogList(UserAccountLog userAccountLog, int currentPage, int pageSize);

	/**
	 * 退款
	 * 
	 * @param userId
	 *            用户ID
	 * @param paySerialNumber
	 *            支付流水号
	 * @param refundSerialNumber
	 *            退款流水号
	 * @param amount
	 *            退款金额
	 * @return
	 */
	public int modifyRefund(long userId, String paySerialNumber, String refundSerialNumber, long amount);

	/**
	 * 支付（优先支付彩金,根据过期时间顺序,然后账户）
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @param amount
	 *            支付金额
	 * @return
	 */
	public int payUserAccount(long userId, String serialNumber, long amount);

	/**
	 * 派奖(直接增加到用户帐户、写流水日志)
	 * 
	 * @param userId
	 *            用户ID
	 * @param serialNumber
	 *            流水号
	 * @param amount
	 *            派奖金额
	 * @return
	 */
	public int sendPrize(long userId, String serialNumber, long amount);

	/**
	 * 查询用户充值记录
	 * 
	 * @param partnerId
	 *            渠道ID
	 * @param partnerChargeId
	 *            合作商充值ID
	 * @return
	 */
	public UserRecharge findUserRecharge(String partnerId, String partnerChargeId);

	/**
	 * 分页查询提现申请记录
	 * 
	 * @param withdrawApply
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public WithdrawApplyData getWithdrawApplyList(WithdrawApply withdrawApply, int currentPage, int pageSize);

	/**
	 * 根据彩金交易在合作商的id查询
	 * 
	 * @param partnerId
	 *            合作商ID
	 * @param partnerHandselId 彩金交易ID
	 * @return
	 */
	public UserHandsel findUserHandselByPartnerId(String partnerId, String partnerHandselId);

	/**
	 * 冻结用户账户资金(追号使用)
	 * 
	 * @param userId
	 *            用户ID
	 * @param amount
	 *            冻结金额
	 * @param freezeSerialNumber
	 *            冻结流水号
	 * @return
	 */
	public int freezeUserAccount(long userId, long amount, String freezeSerialNumber);

	/**
	 * 扣除冻结资金（追号详情完成一项）
	 * 
	 * @param userId
	 *            用户ID
	 * @param amount
	 *            扣除冻结金额
	 * @param paySerialNumber
	 *            扣除冻结金额流水号
	 * @return
	 */
	public int deductFreezeMoney(long userId, long amount, String paySerialNumber);

	/**
	 * 退还冻结资金（直接退还到可用资金账户）---追号订单追号失败退款
	 * 
	 * @param userId
	 *            用户ID
	 * @param amount
	 *            退还金额
	 * @param freezeSerialNumber
	 *            扣除冻结金额流水号
	 * @param refundSerialNumber
	 *            退还冻结金额流水号
	 * @return
	 */
	public int refundFreezeMoney(long userId, long amount, String freezeSerialNumber, String refundSerialNumber);

	/**
	 * 修改用户账户兑奖密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param oldPasswd
	 *            旧密码
	 * @param newPasswd
	 *            新密码
	 * @return
	 */
	public int updatePrizePassword(long userId, String oldPasswd, String newPasswd);

	/**
	 * 创建用户预申请记录
	 * 
	 * @param userPreApply
	 * @return
	 */
	public int createUserPreApply(UserPreApply userPreApply);

	/**
	 * 查询预存款申请记录
	 * 
	 * @param userId
	 *            用户ID
	 * @param partnerUniqueNo
	 *            预存款合作商交易ID
	 * @return
	 */
	public UserPreApply findUserPreApply(String partnerId, String partnerUniqueNo);

	/**
	 * 分页查询预存款申请记录
	 * 
	 * @param userPreApply
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public UserPreApplyData getUserPreApplyList(UserPreApply userPreApply, int currentPage, int pageSize);

	/**
	 * 通过预存款申请ID查询信息
	 * 
	 * @param preApplyId
	 * @return
	 */
	public UserPreApply findUserPreApplyByPreApplyId(long preApplyId);

	
	/**
	 * 审核预存款申请
	 * 
	 * @param preApplyId
	 *            预申请ID
	 * @param status
	 *            状态: 0 待审核 1 审核通过 2 审核未通过
	 * @return
	 */
	public int auditUserPreApply(long preApplyId, int status);

	/**
	 * 增加账户预存款金额
	 * 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public int addUserPreApplyAmount(long userId, long amount);

	/**
	 * 统计充值金额
	 * @param date 要统计的时间，格式为 yyyy-MM-dd
	 * @return 返回Map,key为渠道商Id(partnerId),value为金额
	 * @throws org.apache.thrift.TException
	 */
    public Map<String,Long> statisticRecharge(String date);

    /**
	 * 统计提现金额
	 * @param date 要统计的时间，格式为 yyyy-MM-dd
	 * @return 返回Map,key为渠道商Id(partnerId),value为金额
     * @throws org.apache.thrift.TException
     */
    public Map<String,Long> statisticWithdraw(String date);

    /**
     * 创建流水日志
     * @param userId
     * @param nickName
     * @param partnerId
     * @param logType
     * @param totalAmount
     * @param accountAmount
     * @param handselAmount
     * @param serialNumber
     * @param remark
     * @return
     */
	int createUserAccountLog(long userId, String nickName, String partnerId,
			int logType, long totalAmount, long accountAmount,
			long handselAmount, String serialNumber, String remark);

	/**
	 * 更新彩金状态
	 */
	void updateHandselState();

	/**
	 * 冻结资金，不写日志，Handler调用接口
	 * @param withdrawApply 提现申请
	 * @return 返回更新结果
	 */
	int freezeUserAccoutByWithdraw(WithdrawApply withdrawApply);

	/**
	 * 审核提现申请后更新帐号信息
	 * @param withdrawApply
	 * @return 返回更新结果
	 */
	int updateUserAccountByWithdraw(WithdrawApply withdrawApply);

	/**
	 * 审核预存款后更新帐号信息
	 * @param userPreApply
	 * @return
	 */
	int updateUserAccountByPreapply(UserPreApply userPreApply);

	/**
	 * 根据userId判断用户是否存在
	 * @param userId
	 * @return
	 */
	public boolean checkUserExist(long userId);

	/**
	 * 初始化Handsel统计表插入一条彩金统计数据
	 * @param day
	 * @return
	 */
	public int initHandselCount(String day);

	/**
	 * 增加彩金统计金额
	 * @param userHandsel
	 */
	public void increaseHandselCount(UserHandsel userHandsel);

	/**
	 * 查询彩金统计数据
	 * @param date
	 * @return
	 */
	public UserHandselCount getUserHandselCount(String date);

	/**
	 * 当前所有帐户金额
	 * @return
	 */
	public long totalAccountMoney();

	/**
	 * 查询一天的支付流水数
	 * @param date
	 * @return
	 */
	public long totalPaylogNum(String date);

	WithdrawAccount findWithdrawAccountByNo(String accountNo, long userId);
	
	int createAccountAndGetId(WithdrawAccount account);
	
	int deleteWithdrawApply(long userId, String serialNumber);

	public int rollbackWithdrawApply(int applyId);

	public int rollbackPreApply(long preApplyId);
}
