namespace java com.cqfc.protocol.riskcontrol

struct StatisticDataByGame{
    1:string gameId,	//彩种
    2:string issue,	//期号
    3:i64 outTicketSuccessNum,	//平台成功出票数
    4:i64 successTicketNum,	//平台成功订单数
    5:i64 successTotalMoney,	//平台成功交易金额
    6:i64 cancelAndSuccessNum,	//福彩成功但平台失败的订单数
    7:i64 totalWinningMoney,	//平台总中奖金额
    8:i64 fucaiTicketNum,	//福彩出票成功数
    9:i64 fucaiTotalBuy,	//福彩交易总金额
    10:i64 fucaiTotalWinning	//福彩中奖总金额
}
struct StatisticDataByDay{
    1:string day,	//日期
    2:i64 totalPartnerAccount,	//渠道商 帐户总金额
    3:i64 totalUserAccount,	//用户 帐户总金额
    4:i64 totalRecharge,	//用户 +渠道商 充值总金额
    5:i64 totalWinningMoney,	//中奖总金额
    6:i64 totalHansel,	//派发彩金金额
    7:i64 invalidHansel,	//过期彩金金额
    8:i64 fucaiTotalBuy,	//福彩交易总金额
    9:i64 totalWithdraw	//用户 +渠道商 提现金额
    10:i64 totalTicketNum	//按天统计的订单数
    11:i64 paylogNum	//支付流水数据
}
struct StatisticPageData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<StatisticDataByGame> resultList
}
struct StatisticDayPageData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<StatisticDataByDay> resultList
}
service RiskControlService {
    //获取一期的统计数据
    StatisticDataByGame getStatisticByGame(1:string gameId, 2:string issue);
    //分页获取一彩种的统计数据
    StatisticPageData queryStatisticByGame(1:string gameId, 2:i32 currentPage, 3:i32 pageSize);
    //获取一天的统计数据
    StatisticDataByDay getStatisticByDay(1:string day);
    //分页获取按天的统计数据
    StatisticDayPageData queryStatisticByDay(1:string day, 2:i32 currentPage, 3:i32 pageSize);
    //重新统计一天数据(按期统计的如果需要重新统计则通过销量统计触发)
    i32 restatisticByDay(1:string day);
}