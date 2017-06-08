namespace java com.cqfc.protocol.userorder
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
	23:string tradeId //合作商唯一订单号
}

struct PcUserOrder{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<Order> userOrders
}


service UserOrderService{
	//添加用户订单(return -1 数据库异常 1 添加成功 -2 用户不存在 -100 违反唯一性约束)
	i32 addUserOrder(1:Order order);
	
	//根据用户id查询订单信息（查询失败或没记录return null）
	Order getUserOrderByUserId(1:i64 userId,2:string orderNo);
	
	//根据用户id，订单类型，彩种查询订单信息(记录总数为-1 或者 用户订单列表为null 则为数据库异常)
	PcUserOrder getUserOrder(1:Order order, 2:i32 pageNum , 3:i32 pageSize);
	
	//根据用户Id，合作商Id,流水号ID(平台orderNo)，pid(ticketId)查询订单中奖状态
	Order getUserPrizeStatus(1:i64 userId,2:string partnerId,3:string orderNo,4:string ticketId);
	
}
