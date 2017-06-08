namespace java com.cqfc.protocol.ticketwinning

//中奖订单
struct WinningOrderInfo{	
	1:i64 id, //中奖订单id
    2:string lotteryId, //彩种Id
    3:string partnerId, // 渠道id
    4:i64 userId, // 用户id
    5:string issueNo, //期号
    6:i32 orderType, //订单类型：1直投订单 2追号订单
	7:string orderNo,//订单编号
	8:i64 winningAmount, //中奖金额
	9:string orderContent, //投注内容
	10:i32 multiple, //倍数
	11:string playType, //玩法
	12:string createTime, //创建时间
	13:string lastUpdateTime, //最后更新时间
	14:i32 sendPrizeState, //派奖状态:1未派奖 2已派奖
	15:string tradeId, // 交易Id
	16:i32 orderStatus // 订单状态
}

//中奖金额返回
struct WinningAmountReturnMessage{
	1:string statusCode,
    2:string msg,
    3:i64 amount,
    4:bool isPrize
}

//投注注数返回
struct BallCountReturnMessage{
	1:string statusCode,
    2:string msg,
    3:i32 count
}

//投注详细内容返回
struct TicketDetailReturnMessage{
	1:string statusCode,
    2:string msg,
    3:list<string> details
}

//中奖金额统计
struct WinningAmountStat{
	1:string lotteryId,
	2:string issueNo,
	3:string partnerId,
	4:i64 sum
}

//大小奖个数和金额统计
struct WinningNumStat{
	1:string lotteryId,
	2:string issueNo,
	3:string partnerId,
	4:i32 orderType,
	5:i32 smallPrizeNum,
	6:i64 smallPrizeMoney,
	7:i32 bigPrizeNum,
	8:i64 bigPrizeMoney
}

//中奖金额统计返回
struct WinningAmountStatData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<WinningAmountStat> resultList
}

//大小奖个数和金额统计返回
struct WinningNumStatData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<WinningNumStat> resultList
}

//中奖订单返回
struct ReturnData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<WinningOrderInfo> resultList
}

service TicketWinningService{

	//计算订单中奖总金额
	WinningAmountReturnMessage calTicketWinningAmount(1:string lotteryId, 2:string playType, 3:string orderContent, 4:string issueNo);
	
	//计算订单总注数
	BallCountReturnMessage calBallCount(1:string lotteryId, 2:string playType, 3:string orderContent);
	
	//计算竞彩订单总注数
	BallCountReturnMessage calJCBallCount(1:string lotteryId, 2:string playType, 3:string orderContent);
	
	//计算订单每注内容
	TicketDetailReturnMessage calTicketDetail(1:string lotteryId, 2:string playType, 3:string orderContent);
	
	//统计中奖金
	WinningAmountStatData getWinningAmountStat(1:list<WinningAmountStat> winningAmountList, 2:i32 currentPage, 3:i32 pageSize);
	
	//统计大小奖中奖个数和金额
	WinningNumStatData getWinningNumStat(1:WinningNumStat winningNumStat, 2:i32 currentPage, 3:i32 pageSize);
	
	//全部重新算奖
	i32 restartCalPrizeAll(1:string lotteryId, 2:string issueNo);
	
    //部分重新算奖
	i32 restartCalPrizePart(1:string lotteryId, 2:string issueNo);
	
	//查询中奖订单
	ReturnData getWinningOrderList(1:WinningOrderInfo winningOrderInfo, 2:i32 currentPage, 3:i32 pageSize);	
	
    //根据彩种期号获取中奖金额
	i64 getTotalWinningMoneyByGame(1:string lotteryId, 2:string issueNo);
	
	//根据日期获取中奖金额
	i64 getTotalWinningMoneyByDay(1:string date);
}
