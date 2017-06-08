namespace java com.cqfc.protocol.ticketissue
include "../lotteryissue/lotteryIssue.thrift"
include "../businesscontroller/businessController.thrift"

struct ResultMessage{
	1:i32 statusCode, //状态码
    2:string msg, //状态码说明信息
    3:businessController.SportPrint sportPrint
}

struct FucaiCount{
    1:string lotteryId,	//彩种Id
    2:string issueNo,	//期号
    3:i32 ticketNum,    //出票数
    4:i64 totalBuy,    //总买金额
    5:i64 totalWinning    //中奖总金额
}

struct OutTicketOrder{
	1:string orderNo,
	2:string lotteryId
	3:string issueNo,
	4:string orderContent,
	5:i64 totalMoney,
	6:i32 multiple,
	7:string playType,
	8:string partnerId,
	9:string batchId,
	10:string wareId
}
struct UserAccountInfo{
  1:i64 userId; // 用户id
  2:string realName; // 用户姓名
  3:string idCardNo; // 证件号
  4:string phone; // 电话
  5:string status; // 状态
  6:string puserkey; // 平台用户id
  7:i64 cash; // 余额
  8:i64 giftcash; // 礼金
  9:i64 forzencash; // 冻结金额
}

struct FucaiPartnerInfo{
	1:string partnerId;	// 合作商Id
	2:string serverUrl;	// 福彩url
	7:string aliasKey;	// 别名
	8:string keyStorePass; // 密码key
	9:string privateSecretKey; // 密钥	
	10:string userId;// 用户Id	
	11:string realName;// 姓名	
	12:string idCard;// 身份证	
	13:string phone;// 电话	
	14:string version;// 版本	
	15:string province;// 省份
	16:string partnerType;// 渠道商类型
}


service TicketIssueService{
	//出票
	ResultMessage sendTicket(1:OutTicketOrder outTicketOrder);
	
    //测试http请求
	ResultMessage testHttpRequest(1:string queryString);
	
	//查询出票情况
	ResultMessage checkTicket(1:OutTicketOrder outTicketOrder);
	
	//查询开奖信息
	lotteryIssue.LotteryDrawResult findLotteryDrawResult(1:string lotteryId,2:string issueNo);
	
	FucaiCount getFucaiCount(1:string lotteryId,2:string issueNo);
	
	FucaiCount getFucaiCountByDay(1:string date);
	
	//cancelOrder使用(同步)
	ResultMessage queryTicket(1:OutTicketOrder outTicketOrder);
	
	//查询用户账户信息
	UserAccountInfo getUserAccountInfo(1:string parmType, 2:string value);
	
	//获取福彩合作商账号
	list<FucaiPartnerInfo> getFucaiPartnerInfoList();
	
	//推送大乐透，七星彩，排列三，排列五，浙江十一选五开奖结果
	string pushDrawResult(1:string lotteryId, 2:string issueNo, 3:string msg);
}
