namespace java com.cqfc.protocol.useraccount

//用户彩金
struct UserHandsel{
	1:i64 handselId,	//彩金ID(自增)
	2:i64 userId,	//用户ID
	3:string partnerId,	//渠道ID
	4:string activityId,	//活动ID
	5:string partnerHandselId,	//彩金交易在合作商的id
	6:string serialNumber,	//流水号
	7:i64 presentAmount,	//赠送金额
	8:i64 usableAmount,	//可使用金额
	9:i64 usedAmount,	//已使用金额
	10:string presentTime,	//赠送时间
	11:string validTime,	//有效时间
	12:string failureTime,	//失效时间
	13:i32 state,	//状态：1有效  2无效
	14:i64 version, //版本号
	15:string createTime,	//创建时间
	16:string lastUpdateTime	//最后更新时间
}
//用户账户
struct UserAccount{
    1:i64 userId,	//用户ID
    2:i64 totalAmount,	//总金额=可用金额+冻结金额
    3:i64 freezeAmount,	//冻结金额
    4:i32 state,	//状态: 1正常 2冻结
    5:i64 usableAmount,	//可用金额
    6:string createTime,	//创建时间
    7:string lastUpdateTime	//最后更新时间
    8:list<UserHandsel> userHandselList	//用户彩金
}
//用户资料
struct UserInfo{
    1:i64 userId,	//渠道id
    2:string nickName,	//用户昵称
    3:i32 cardType,	//证件类型:1身份证，2护照，3军官证，4港台同胞证
    4:string cardNo,	//证件号码
    5:string mobile,	//手机号
    6:string email,	//email
    7:string userName,	//姓名
    8:i32 sex,	//性别:1男 2女
    9:string birthday,	//生日：由身份证号计算得出
    10:i32 age,	//年龄：由身份证号计算得出
    11:i32 userType,	//用户类型
    12:i32 registerTerminal,	//注册终端:1：手机 2：PC
    13:i32 accountType,	//帐号类型:0/1：qq 2：wx
    14:string partnerId,	//来源渠道ID
    15:string partnerUserId,	//渠道用户ID
    16:string prizePassword,	//用户账户兑奖密码
    17:string createTime,	//创建时间
    18:string lastUpdateTime,	//最后更新时间
    19:UserAccount userAccount,	//用户账户
    20:list<UserHandsel> userHandselList	//用户彩金
}
//提款帐号
struct WithdrawAccount{
	1:i32 withdrawAccountId,	//提款帐号ID(自增)
	2:i64 userId,	//用户ID
	3:string realName,	//用户姓名
	4:i32 accountType,	//支付帐号类型：1:银行卡 2:cft 3:支付宝
	5:string accountNo,	//帐号
	6:string bankType,	//银行类型
	7:string bankName,	//银行名称
	8:i32 bankCardType,	//银行卡类型：1:信用卡 2:借记卡
	9:i32 payAccountState,	//支付帐号状态：1:未绑定 2:已绑定
	10:string accountAddress,	//帐号地址信息
	11:string createTime,	//创建时间
	12:string lastUpdateTime	//最后更新时间
}
//提现申请
struct WithdrawApply{
	1:i32 applyId,	//提现申请ID(自增)
	2:i64 userId,	//用户ID
	3:string realName,	//用户姓名
	4:string partnerId,	//渠道ID
	5:string serialNumber,//流水号
	6:i32 withdrawAccountId,	//提款帐号ID
	7:i64 withdrawAmount,	//提款金额
	8:i64 totalAmount,	//提款前总金额
	9:i32 auditState,	//审核状态: 1未审核 2审核通过 3审核不通过
	10:string auditRemark,	//审核备注
	11:string auditTime,	//审核时间
	12:i32 auditId,	//审核人ID
	13:string auditName,	//审核人昵称
	14:string partnerApplyId,	//提现请求合作商id
	15:string withdrawMsgId,	//提现任务信息id
	16:string createTime,	//创建时间
	17:string lastUpdateTime,	//最后更新时间
	18:string searchBeginTime,	//搜索开始时间
	19:string searchEndTime,	//搜索结束时间
	20:WithdrawAccount withdrawAccount
}
//用户充值
struct UserRecharge{
	1:i64 rechargeId,	//充值ID(自增)
	2:i64 userId,	//用户ID
	3:string nickName,	//用户昵称
	4:string partnerId,	//渠道ID
	5:string serialNumber,	//流水号
	6:string partnerRechargeId,	//合作商充值ID
	7:i64 rechargeAmount,	//充值金额
	8:string rechargeType,	//充值方式
	9:string remark,	//备注
	10:string createTime,	//创建时间
	11:string lastUpdateTime	//最后更新时间
}
//账户流水
struct UserAccountLog{
	1:i64 logId,	//流水ID
	2:i64 userId,	//用户ID
	3:string nickName,	//用户昵称
	4:string partnerId,	//渠道ID
	5:i32 logType,	//日志类型：1充值  2支付  3提现  4退款  5派奖  6彩金赠送 7彩金失效
	6:i64 totalAmount,	//总金额=账户金额+彩金金额
	7:i64 accountAmount,	//账户金额
	8:i64 handselAmount,	//彩金金额
	9:string serialNumber,	//流水号
	10:string ext,	//扩展信息
	11:string remark,	//备注
	12:string handselRemark,	//格式：彩金ID=金额，彩金ID=金额...
	13:string createTime,	//创建时间
	14:string searchBeginTime,	//搜索开始时间
	15:string searchEndTime,	//搜索结束时间
}
//用户预申请表
struct UserPreApply{
	1:i64 preApplyId,	//预申请ID(自增)
	2:string partnerId,	//渠道ID
	3:i64 userId,	//用户ID
	4:string partnerUniqueNo,	//渠道订单唯一号
	5:i64 preMoney,	//预存款金额
	6:i32 status,	//申请状态: 0 待审核 1 审核通过 2 审核未通过
	7:string createTime,	//创建时间
	8:string lastUpdateTime	//最后更新时间
}
struct PartnerUser{
	1:i32 id,	//ID
	2:i32 userId,	//用户ID
	3:string partnerId,	//渠道ID
	4:string partnerSecond,	//二级渠道
	5:string partnerThird,	//三级渠道
	6:string partnerFourth	//四级渠道
}
struct UserInfoData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<UserInfo> resultList
}
struct UserHandselData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<UserHandsel> resultList
}
struct UserAccountLogData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<UserAccountLog> resultList
}
struct WithdrawApplyData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<WithdrawApply> resultList
}
struct UserPreApplyData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<UserPreApply> resultList
}

struct UserHandselCount{
    1:string day,
    2:i64    totalMoney,
    3:i64    totalInvalidMoney
}

service UserAccountService {

	//新增用户资料并创建账户
	i64 createUserInfo(1:UserInfo userInfo);
	//修改用户资料
//	i32 updateUserInfo(1:UserInfo userInfo);
	//分页查询用户信息
	UserInfoData getUserInfoList(1:UserInfo userInfo,2:i32 currentPage,3:i32 pageSize);
	//根据用户ID查询用户信息
	UserInfo findUserInfoById(1:i64 userId);
	//冻结、解冻账户（1正常 2冻结）
	i32 modifyUserAccountState(1:i64 userId,2:i32 state);
	//根据用户ID查询用户账本（用户可用彩金）
	UserAccount findUserAccountByUserId(1:i64 userId);
	//冻结用户账户资金(追号使用)
	i32 freezeUserAccount(1:i64 userId,2:i64 amount,3:string freezeSerialNumber);
	//扣除冻结资金（追号详情完成一项）
	i32 deductFreezeMoney(1:i64 userId,2:i64 amount,3:string paySerialNumber);
	//退还冻结资金
	i32 refundFreezeMoney(1:i64 userId,2:i64 amount,3:string freezeSerialNumber,4:string refundSerialNumber);
	//修改用户账户兑奖密码
	i32 updatePrizePassword(1:i64 userId,2:string oldPasswd,3:string newPasswd);
	//创建用户预申请记录
	i32 createUserPreApply(1:UserPreApply userPreApply);
	
	
	
	//彩金赠送(写流水日志)
	i32 createUserHandsel(1:UserHandsel userHandsel);
	//分页查询彩金列表
	UserHandselData getUserHandselList(1:UserHandsel userHandsel,2:i32 currentPage,3:i32 pageSize);
	//通过用户ID查询彩金列表
	list<UserHandsel> getUserHandselListByUserId(1:i64 userId,2:i32 state);
	//彩金状态修改(判断时间是否失效)
	i32 modifyUserHandselState(1:i64 userId);
	//查询用户可使用彩金列表（按过期时间升序）
	list<UserHandsel> getUsableUserHandselList(1:i64 userId);
	//根据彩金交易在合作商的id查询
	UserHandsel findUserHandselByPartnerId(1:string partnerId, 2:string partnerHandselId);
	
	
	//创建提款帐号
	i32 createWithdrawAccount(1:WithdrawAccount withdrawAccount);
	//查询用户提款帐号列表
	list<WithdrawAccount> getWithdrawAccountListByUserId(1:i64 userId);
	
	
	//创建提现申请
	i32 createWithdrawApply(1:WithdrawApply withdrawApply);
	//修改提现申请
//	i32 updateWithdrawApply(1:WithdrawApply withdrawApply);
	//根据提现ID查询提现记录
	WithdrawApply findWithdrawApplyByApplyId(1:i32 applyId);
	//根据用户ID、流水号查询提现记录
	WithdrawApply findWithdrawApply(1:i64 userId,2:string serialNumber);
	//根据用户ID查询提现列表
	list<WithdrawApply> getWithdrawApplyListByUserId(1:i64 userId);
	//提现审核（用户ID、流水号、审核状态、审核人ID、审核人、审核备注）、成功:处理金额
	i32 auditWithdrawApply(1:WithdrawApply withdrawApply);
	//分页查询提现记录
	WithdrawApplyData getWithdrawApplyList(1:WithdrawApply withdrawApply,2:i32 currentPage,3:i32 pageSize);
	//查询存款预申请记录
	UserPreApply findUserPreApply(1:string partnerId,2:string partnerUniqueNo);
	//分页查询预存款申请记录
	UserPreApplyData getUserPreApplyList(1:UserPreApply userPreApply,2:i32 currentPage,3:i32 pageSize);
	//预存款申请审核
	i32 auditUserPreApply(1:i64 preApplyId,2:i32 status);
	//判断用户是否存在
	bool checkUserExist(1:i64 userId);
	
	
	//创建充值记录(金额、流水处理)
	i32 createUserRecharge(1:UserRecharge userRecharge);
	//查询用户充值记录列表
	list<UserRecharge> getUserRechargeList(1:i64 userId);
	//查询用户充值记录(渠道ID、合作商充值ID)
	UserRecharge findUserRecharge(1:string partnerId,2:string partnerChargeId);
	
	
	//分页查询流水日志
	UserAccountLogData getUserAccountLogList(1:UserAccountLog userAccountLog,2:i32 currentPage,3:i32 pageSize);

	//退款
	i32 modifyRefund(1:i64 userId,2:string paySerialNumber,3:string refundSerialNumber,4:i64 amount);
	//支付（优先支付彩金,根据过期时间顺序,然后账户）
	i32 payUserAccount(1:i64 userId,2:string serialNumber,3:i64 amount);
    //派奖
    i32 sendPrize(1:i64 userId,2:string serialNumber,3:i64 amount);
    
    //初始化彩金统计数据
    i32 initHandselCount(1:string day);
    
    //获取彩金统计数据
    UserHandselCount getUserHandselCount(1:string date);
    
	//统计充值金额
	map<string,i64> statisticRecharge(1:string date);
	//统计提现金额
	map<string,i64> statisticWithdraw(1:string date);
	
	//当前所有用户帐户总金额
	i64 totalAccountMoney();
	
	//统计当天的支付流水
	i64 totalPaylogNum(1:string date);
}