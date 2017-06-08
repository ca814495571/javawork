namespace java com.cqfc.protocol.partnerorder
include "../ticketwinning/ticketWinning.thrift"

//竞彩订单详情
struct OrderDetail{
	
	1:string orderNo, //订单编号
	2:string transferId, //赛事id
	3:string matchNo,	//赛事编号
	4:string rq, //让球
	5:string content, //投注内容
	6:string odds, //赔率
	7:i32  matchStatus, //赛事状态
	8:string createTime //创建时间
	9:string winOdds, //中奖赔率
	10:string lotteryId //彩种
	11:string issueNo //期号
	12:i32 matchType //赛事类型
}

//订单
struct Order{
	
	1:i64 orderId, //订单id
    2:string lotteryId, //彩种Id
    3:string partnerId, // 渠道id
    4:i64 userId, // 用户id
    5:string issueNo, //期号
	6:string orderNo,//订单编号
	7:i32 orderStatus, //订单状态
	8:i64 totalAmount, //投注总金额
	9:i64 winPrizeMoney, //中奖金额
	10:i64 prizeAfterTax, //税后奖金
	11:string orderContent, //投注内容
	12:i32 stakeNum, //注数
	13:i32 multiple, //倍数
	14:string playType, //玩法
	15:string paySerialNumber,//支付流水号
	16:string realName, //真实姓名
	17:string cardNo, //身份证号
	18:string mobile,//手机号
	19:string createTime, //创建时间
	20:string lastUpdateTime, //最后更新时间
	21:string ext ,//扩展字段
	22:i32 orderType, //订单类型
	23:string tradeId, //合作商唯一订单号
	24:string planId,	//彩票id
	25:string province,	//出票省份
	26:string lotteryMark, //彩票号
	27:string ticketTime, //出票时间
	28:string endTime, //竞彩截止时间
	29:string drawTime, //竞彩开奖时间
	30:list<OrderDetail> orderDetails
}


//渠道商期销售统计表
struct IssueSaleCount{

	1:i64 id , //主键
    2:string issueNo,  //期号
    3:string lotteryId, //彩种Id
    4:string partnerId, // 渠道id
    5:i32 orderType , //订单类型
	6:i64 sucNum,		//成功交易的数量
	7:i64 sucMoney,		//成功交易的金额
	8:i64 failNum,		//交易失败的笔数
	9:i64 failMoney,	//交易失败金额
	10:string createTime ,//创建时间 	
 	11:string lastUpdateTime //最后修改时间

}



//渠道商期兑奖统计表
struct IssueRewardCount{

	1:i64 id , //主键
    2:string issueNo,  //期号
    3:string lotteryId, //彩种Id
    4:string partnerId, // 渠道id
    5:i32 orderType , //订单类型
	6:i64 smallPrizeNum,	//小奖中奖总数
	7:i64 smallPrizeMoney,	//小奖中奖总金额
	8:i64 bigPrizeNum,		//中大奖的总数
	9:i64 bigPrizeMoney,	//中大奖的总金额
	10:string createTime ,//创建时间 	
 	11:string lastUpdateTime //最后修改时间
}

//渠道商彩种日销售统计表
struct DailySaleCount{ 
    1:i64 id , //主键
	2:string partnerId , //渠道Id
	3:string lotteryId , //彩种Id
	4:i64 totalMoney,		//销售总额
	5:string countTime, //统计时间
	6:string createTime ,//创建时间 	
 	7:string lastUpdateTime //最后修改时间
}


//渠道商彩种日兑奖统计表
struct DailyAwardCount{ 
    1:i64 id , //主键
	2:string partnerId , //渠道Id
	3:string lotteryId , //彩种Id
	4:i64 awardPrizeMoney, //兑奖金额
	5:i64 afterPrizeMoney, //税后兑奖金额
	6:string countTime, //统计时间
	7:string createTime ,//创建时间 	
 	8:string lastUpdateTime //最后修改时间
}

//渠道商日充值统计表
struct DailyChargeCount{ 
    1:i64 id , //主键
	2:string partnerId , //渠道Id
	3:i64 chargeTotalMoney, //充值总金额
	4:string countTime, //统计时间
	5:string createTime ,//创建时间 	
 	6:string lastUpdateTime //最后修改时间
}

//渠道商日提现统计表
struct DailyEncashCount{ 
    1:i64 id , //主键
	2:string partnerId , //渠道Id
	3:i64 encashTotalMoney, //提现总金额
	4:string countTime, //统计时间
	5:string createTime ,//创建时间 	
 	6:string lastUpdateTime //最后修改时间
}

//风控日统计数据
struct DailyRiskCount{
    1:string day, //统计时间
    2:i64 saleTotalMoney,		//销售总额
	3:i64 rechargeTotalMoney, //充值总金额
	4:i64 withdrawTotalMoney, //提现总金额
	5:i64 awardPrizeMoney //兑奖金额
}

//风控期统计数据
struct IssueRiskCount{
    1:string lotteryId, //彩种Id
    2:string issueNo,  //期号
	3:i64 successNum,		//成功交易的数量
	4:i64 successMoney,		//成功交易的金额
	5:i64 failNum,		//交易失败的笔数
	6:i64 failMoney,	//交易失败金额
}

//日志恢复索引表
struct RecoverOrderIndex{
	1:string dbName;//数据库名称
	2:string orderNo;//订单号
	3:i32 flag; //标示是否可修改
	4:string createTime;//创建时间
	5:string lastUpdateTime //最后修改时间
}

//供文档接口查询渠道商期销量兑奖信息
struct IssueSaleAndReward{

    1:string issueNo,  //期号
    2:string lotteryId, //彩种Id
    3:string partnerId, // 渠道id
    4:i32 orderType , //订单类型
    5:i64 sucNum,		//成功交易的数量
	6:i64 sucMoney,		//成功交易的金额
	7:i64 failNum,		//交易失败的笔数
	8:i64 failMoney,	//交易失败金额
	9:i64 smallPrizeNum,	//小奖中奖总数
	10:i64 smallPrizeMoney,	//小奖中奖总金额
	11:i64 bigPrizeNum,		//中大奖的总数
	12:i64 bigPrizeMoney,	//中大奖的总金额
	13:string createTime //创建时间 	
}


//供文档接口查询日销售和充值信息
struct DailySaleAndCharge{
 	
	1:string partnerId , //渠道Id
	2:string lotteryId , //彩种Id
	3:i64 totalMoney,		//销售总额
	4:i64 awardPrizeMoney, //兑奖金额
	5:i64 chargeTotalMoney, //充值总金额
	6:i64 encashTotalMoney, //提现总金额
	7:string countTime //统计时间

}


//后台渠道商日销售所有彩种销量
struct LotteryDaySale
{

	1:string partnerId , //渠道Id
	2:i64 awardPrizeTotalMoney,
	3:i64 encashTotalMoney,
	4:i64 chargeTotalMoney,
	5:i64 saleTotalMoney,
	6:string countTime,

}
//后台彩种期号销售中奖统计信息
struct LotteryIssueSale{

    1:string issueNo,  //期号
    2:string lotteryId, //彩种Id
    3:string partnerId, // 渠道id
	4:i64 sucNum,		//成功交易的数量
	5:i64 sucMoney,		//成功交易的金额
	6:i64 failNum,		//交易失败的笔数
	7:i64 failMoney,	//交易失败金额
	8:i64 smallPrizeNum,	//小奖中奖总数
	9:i64 smallPrizeMoney,	//小奖中奖总金额
	10:i64 bigPrizeNum,		//中大奖的总数
	11:i64 bigPrizeMoney	//中大奖的总金额
	12:string createTime //创建时间
}


//扫描全局库时统计销量信息对象，处理之后放入日销售兑奖统计表
//struct IssueSale{
//    1:string partnerId, // 渠道id
//    2:string lotteryId, //彩种Id
//    3:string issueNo,  //期号
//    4:i32 orderType , //订单类型
//    5:i64 sucNum,		//成功交易的数量
//	6:i64 sucMoney,		//成功交易的金额
//	7:i64 failNum,		//交易失败的笔数
//	8:i64 failMoney	//交易失败金额
//}

//扫描全局库时渠道商彩种日销售兑奖对象,处理之后放入渠道商期销售统计表中
//struct DailySaleReward{ 
//	1:string partnerId , //渠道Id
//	2:string lotteryId , //彩种Id
//	3:i64 totalMoney,		//销售总额
//	4:i64 awardPrizeMoney //兑奖金额
//}


struct PcLotteryIssueSale{

	1:list<LotteryIssueSale> lotteryIssueSale,
 	2:i32 totalNum 
}

struct PcPartnerOrder{

	1:list<Order> partnerOrders,
	2:i32 totalNum 
}


struct PcDailyReport{

	1:list<DailySaleAndCharge> dailySaleAndCharges,
	2:i32 totalNum 
}

struct PcDaySaleDetails{

	1:list<DailySaleAndCharge> daySaleDetails,
	2:i32 totalNum 
}


service PartnerOrderService{
	//添加合作商订单信息(返回值 -1 数据库异常 1 添加成功 -7 订单号不存在 -100 违反唯一性约束)
	i32 addPartnerOrder(1:Order order);
	
	//根据多条件查询该合作商订单信息
 	PcPartnerOrder getPartnerOrderByWhere(1:Order order,2:i32 pageNum,3:i32 pageSize);
 	
	//根据渠道商id 彩种id 期号 查询渠道商期销售兑奖统计信息(区分了订单类型)
	list<IssueSaleAndReward> getIssueSaleAndReward(1:string partnerId,2:string lotteryId,3:string issueNo);
	
	//根据渠道商id 彩种id 期号 查询渠道商期销售兑奖统计信息(分组)
	list<LotteryIssueSale> getIssueSaleAndRewardByGroup(1:string partnerId,2:string lotteryId,3:string issueNo);
	
	//根据渠道商id 时间 查询渠道商所有彩种日销售充值统计信息
	list<DailySaleAndCharge> getDailySaleAndCharge(1:string partnerId,2:string countTime);
	
	//根据彩种id 期号查询所有合作商的销售量
	list<LotteryIssueSale> getAllLotteryIssueSale(1:string lotteryId,2:string issueNo);
	
	//根据条件查询渠道商期销售统计信息(partnerid 彩种id 期号分组)
	PcLotteryIssueSale getLotteryIssueSaleByWhere(1:LotteryIssueSale lotteryIssueSale,2:i32 pageNum,3:i32 pageSize);
	
	
	//根据条件查询渠道商当前期期销售统计信息
	list<LotteryIssueSale> getCurrentIssueSaleByWhere(1:LotteryIssueSale lotteryIssueSale,2:string fromTime,3:string toTime);
	
	//根据渠道商id 时间 查询渠道商所有彩种日销售统计信息
	list<LotteryDaySale> getLotteryDaySaleByWhere(1:LotteryDaySale lotteryDaySale, 2:i32 pageNum, 3:i32 pageSize);
	
	
	//根据渠道商id 时间 查询渠道商所有彩种日报信息
	PcDailyReport getDailyReportByWhere(1:DailySaleAndCharge dailySaleAndCharge, 2:i32 pageNum, 3:i32 pageSize);
	
	
	//查询风控日统计数据
	DailyRiskCount getDailyRiskCount(1:string day);
		
	//查询风控期统计数据
	IssueRiskCount getIssueRiskCount(1:string lotteryId,2:string issueNo);
	
	//统计期销量
	i32 partnerIssueSaleCount(1:string lotteryId, 2:string issueNo);
	
	//统计期中奖金额
	i32 partnerIssueRewardCount(1:string lotteryId, 2:string issueNo);
	//统计日销量
	i32 partnerDailySaleCount(1:string countTime);
	//统计日兑奖
	i32 partnerDailyAwardCount(1:string countTime);
	//统计日充值
	i32 partnerDailyChargeCount(1:string countTime);
	//统计日提现
	i32 partnerDailyEncashCount(1:string countTime);
	//统计合作商日报
	i32 partnerDailyReport(1:string countTime);	
	
	i64 getTotalTicknumByDay(1:string date);
	
	//修改全局竞彩订单中奖金额以及是否中奖状态
	i32 updateWinResultInfo(1:list<ticketWinning.WinningOrderInfo> winningOrderInfos);

	//日销量详情
	PcDaySaleDetails getDailySaleDetails(1:DailySaleAndCharge dailySaleAndCharge,2:i32 pageNum,3:i32 pageSize);

}
