namespace java com.cqfc.protocol.cancelorder

struct CancelOrder{
    1:string orderNo,	//订单编号
    2:string partnerId,	//渠道ID
    3:i64 userId,	//用户ID
    4:string lotteryId,	//彩种ID
    5:string issueNo,	//期号
    6:i32 outTicketStatus,	//订单出票状态：3出票中 4已出票待开奖 5出票失败
    7:i64 totalAmount,	//投注总金额
    8:string orderContent, //投注内容
    9:string playType,	//	玩法
    10:i32 multiple,	//倍数
    11:string createTime,	//创建时间
    12:string lastUpdateTime	//最后更新时间
}

struct ReturnData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<CancelOrder> resultList
}

struct SportCancelOrderDetail{
	1:i64 orderNo,
	2:string matchId,
	3:string transferId,
	4:string matchNo,
	5:string rq,
	6:string orderContent,
	7:string sp,
	8:i32 matchStatus,
	9:string createTime,
	10:string lastUpdateTime
}

struct SportCancelOrder{
	1:i64 orderNo,
	2:string lotteryId,
	3:string partnerId,
	4:i64 userId,
	5:string issueNo,
	6:i32 orderType,
	7:i32 outTicketStatus,
	8:i64 totalAmount,
	9:string orderContent,
	10:i32 multiple,
	11:string playType,
	12:string tradeId,
	13:string planId,
	14:string printProvince,
	15:string ticketNo,
	16:string printTime,
	17:string createTime,
	18:string lastUpdateTime,
	19:list<SportCancelOrderDetail> detailList
}

service CancelOrderService {
    //新增数字彩转移订单
    i32 createCancelOrder(1:CancelOrder cancelOrder);
    //获取转移订单中成功出票的订单
    i64 getSuccessTicketCancelOrder(1:string lotteryId, 2:string issueNo)
    //分页获取转移单列表
    ReturnData getCancelOrder(1:CancelOrder cancelOrder,2:i32 currentPage,3:i32 pageSize)
    
    //新增竞技彩转移订单
    i32 createSportCancelOrder(1:SportCancelOrder sportCancelOrder);
    
}