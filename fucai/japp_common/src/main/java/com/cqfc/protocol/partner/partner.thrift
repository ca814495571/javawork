namespace java com.cqfc.protocol.partner

struct PartnerIpAddress{
    1:i32 id,	//ID(自增)
    2:string ipAddress,	//渠道商IP地址
    3:string partnerId	//渠道商id
}

struct LotteryPartner{
    1:string partnerId,	//渠道商id
    2:string partnerName,	//渠道商名称
    3:i16 partnerType,	//渠道商类型：1纯B2B，2平台管理用户帐号，3平台管理用户帐号、用户账户
    4:string secretKey,	//私钥文件名称
    5:i16 state,	//渠道商状态: 1正常，2锁定
    6:i64 minBalance,	//余额告警值
    7:string callbackUrl,	//回调url
    8:string ext,	//扩展信息
    9:i64 userId,	//用户ID
    10:string registrationTime,	//创建时间
    11:string lastUpdateTime,	//最后更新时间
    12:list<PartnerIpAddress> partnerIpAddressList,
    13:string publicKey,   //公钥文件名称
    14:string aliasKey,      //私钥别名
    15:string keyStore   //私钥密码
}

struct ReturnData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<LotteryPartner> resultList
}

service PartnerService {
	//查询渠道商信息列表
    ReturnData getLotteryPartnerList(1:LotteryPartner lotteryPartner,2:i32 currentPage,3:i32 pageSize);
    //新增渠道商信息
    i32 addLotteryPartner(1:LotteryPartner lotteryPartner);
    //查询渠道商信息
    LotteryPartner findLotteryPartnerById(1:string partnerId);
    //修改渠道商状态
    i32 updateState(1:string partnerId,2:i32 state);
    //校验是否为渠道账户
    bool isPartnerAccount(1:string partnerId);
    //更新渠道商信息
    i32 updateLotteryPartner(1:LotteryPartner lotteryPartner,2:string oldPartnerId);
    //校验渠道商是否存在
    bool verifyPartnerIsExist(1:string partnerId);
    //判断渠道商是否可以绑定用户
    bool verifyCanBindUser(1:string partnerId);
    //查询渠道商信息(校验使用)
    LotteryPartner findPartnerForCheck(1:string partnerId);
    //获取合作商回调url
    list<string> getCallBackUrlListByPartnerId(1:string partnerId);
    
    
    //根据渠道商ID查询所有IP地址
    list<PartnerIpAddress> getPartnerIpAddressList(1:string partnerId);
	//查询渠道商IP地址是否存在
    bool isExistIpAddress(1:string partnerId,2:string ipAddress);
    //删除合作商信息与ip地址
    i32 deletePartnerByPartnerId(1:string partnerId);
        
	//新增渠道商IP地址
	//i32 addPartnerIpAddress(1:PartnerIpAddress partnerIpAddress);
	//修改渠道商IP地址
	//i32 updatePartnerIpAddress(1:PartnerIpAddress partnerIpAddress);
	//删除渠道商IP地址
	//i32 deletePartnerIpAddressByIp(1:i32 id,2:string partnerId);
	//查询渠道商IP地址
	//PartnerIpAddress findPartnerIpAddressById(1:i32 id);
	//修改合作商IP地址
    //i32 updateIpAddress(1:string partnerId,2:list<PartnerIpAddress> ipList);
}