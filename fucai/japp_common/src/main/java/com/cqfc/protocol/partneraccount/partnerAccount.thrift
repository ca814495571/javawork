namespace java com.cqfc.protocol.partneraccount

//渠道账户
struct PartnerAccount{
    1:string partnerId,	//渠道id
    2:i64 totalAmount,	//总金额
    3:i64 freezeAmount,	//冻结金额
    4:i64 usableAmount,	//可用金额
    5:i64 creditLimit,	//信用额度
    6:i64 alarmValue,	//告警值
    7:i32 state,	//状态：1正常 2冻结
    8:string createTime,	//创建时间
    9:string lastUpdateTime	//最后更新时间
}
//渠道账户流水
struct PartnerAccountLog{
    1:i32 logId,	//自增
    2:string partnerId,	//渠道ID
    3:i32 state,	//收支状态：1充值  2支付  4退款  5派奖
    4:i64 totalAmount,	//总金额
    5:i64 accountAmount,	//操作金额
    6:i64 remainAmount,	//剩余金额
    7:string serialNumber,	//流水号
    8:string ext,	//扩展信息
    9:string remark,	//备注
    10:string createTime,	//创建时间
    11:string searchBeginTime,	//搜索开始时间
    12:string searchEndTime	//搜索结束时间
}
//渠道充值记录
struct PartnerRecharge{
	1:i32 rechargeId,	//充值ID（自增）
	2:string partnerId,	//渠道ID
	3:string serialNumber,	//流水号
	4:i64 rechargeAmount,	//充值金额
	5:string rechargeType,	//充值方式
	6:string remark,	//备注
	7:string createTime,	//创建时间
    8:string lastUpdateTime	//最后更新时间
}
//渠道预申请表
struct PartnerPreApply{
	1:i64 preApplyId,	//预申请ID(自增)
	2:string partnerId,	//渠道ID
	4:string partnerUniqueNo,	//渠道订单唯一号
	5:i64 preMoney,	//预存款金额
	6:i32 status,	//申请状态: 0 待审核 1 审核通过 2 审核未通过
	7:string createTime,	//创建时间
	8:string lastUpdateTime	//最后更新时间
}
struct PartnerAccountData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<PartnerAccount> resultList
}
struct PartnerAccountLogData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<PartnerAccountLog> resultList
}
struct PartnerPreApplyData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<PartnerPreApply> resultList
}

service PartnerAccountService {
	//新增渠道账户
	i32 addPartnerAccount(1:PartnerAccount partnerAccount);
	//查询渠道账户列表
    PartnerAccountData getPartnerAccountList(1:PartnerAccount partnerAccount,2:i32 currentPage,3:i32 pageSize);
	//根据渠道ID查询账户信息
	PartnerAccount findPartnerAccountByPartnerId(1:string partnerId);
	//更新渠道账户状态(冻结、解冻)
	i32 updatePartnerAccountState(1:string partnerId,2:i32 state);
	//更新渠道账户信用额度
	i32 updatePartnerAccountCreditLimit(1:string partnerId,2:i64 creditLimit);
	//支付
	i32 payPartnerAccount(1:string partnerId,2:i64 amount,3:string serialNumber);
	//查询渠道账户流水日志列表
    PartnerAccountLogData getPartnerAccountLogList(1:PartnerAccountLog partnerAccountLog,2:i32 currentPage,3:i32 pageSize);
    //新增充值
    i32 addPartnerRecharge(1:PartnerRecharge partnerRecharge);
    //退款
    i32 modifyRefund(1:string partnerId,2:i64 amount,3:string paySerialNumber,4:string refundSerialNumber);
    //派奖
    i32 sendPrize(1:string partnerId,2:i64 amount,3:string serialNumber);
    //计算渠道商充值记录
    i64 countPartnerRechargeByDate(1:string partnerId,2:string dateTime);
    //创建渠道存款预申请记录
    i32 createPartnerPreApply(1:PartnerPreApply partnerPreApply);
    //查询渠道存款预申请记录
	PartnerPreApply findPartnerPreApply(1:string partnerId,2:string partnerUniqueNo);
	//分页查询预存款申请记录
	PartnerPreApplyData getPartnerPreApplyList(1:PartnerPreApply partnerPreApply,2:i32 currentPage,3:i32 pageSize);
	//预存款申请审核
	i32 auditPartnerPreApply(1:i64 preApplyId,2:i32 status);
	//统计充值金额
	map<string,i64> statisticRecharge(1:string date);
	
	//当前所有渠道商帐户总金额
	i64 totalAccountMoney();
	
	//统计当天的支付流水
	i64 totalPaylogNum(1:string date);
}