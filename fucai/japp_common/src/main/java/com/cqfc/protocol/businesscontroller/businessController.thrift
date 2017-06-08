namespace java com.cqfc.protocol.businesscontroller
include "../appendtask/appendTask.thrift"

struct Message{
    1:i32 transCode, //接口码
    2:string transMsg, //接口内容
    3:string partnerId //渠道ID
}

struct TransResponse{
    1:string statusCode, //状态码
    2:string data, //说明信息
    3:string responseTransCode, //响应接口码
    4:map<string,string> extra //额外信息
}

struct VoteTicket{
	1:string ticketId,
	2:i32 multiple,
	3:string issueNo,
	4:string playType,
	5:i32 money,
	6:string ball
}

struct OrderMsg{
	1:string partnerId,
	2:string version,
	3:string time,
	4:string lotteryId,
	5:i32 ticketsNum,
	6:i32 totalMoney,
	7:string province,
	8:string machine,
	9:i64 userId,
	10:string realName,
	11:string idCard,
	12:string phone,
	13:string planId,
	14:string wareId,
	15:list<VoteTicket> ticketList
}

struct SportDetail{
	1:string matchId,	//赛事ID
	2:string transferId,	//赛事转换ID
	3:string matchNo,	//赛事编码
	4:string rq,	//让球（让分）
	5:string orderContent	//投注内容
}

struct PrintMatch{
	1:string matchId,	//赛事ID
	2:string transferId,	//赛事转换ID
	3:string rq,	//让球（让分）
	4:string sp	//赔率
}

struct SportPrint{
	1:i64 orderNo,	//订单编号
	2:i32 printStatus,	//出票状态：1失败 2成功 3出票中 4订单不存在
	3:string ticketNo, //彩票号
	4:string printTime,	//出票时间
	5:list<PrintMatch> matchList	//赛事对应赔率
}

service BusinessControllerService {
	//处理业务
    TransResponse ProcessMessage(1:Message message);
    //获取queueSize
    i32 getQueueSize();
    //出票回调resultNumber(0:出票失败 1:出票成功)
    bool isTicketSuccess(1:string orderNoStr, 2:i32 resultNumber);
    //追号明细创建订单
    i32 createAppendOrder(1:appendTask.AppendTaskDetail appendTaskDetail);
    //104投注接口
    TransResponse createOrderProcess(1:OrderMsg orderMsg);
    //105查询接口
    TransResponse findOrderProcess(1:Message message);
    //205查询接口
    TransResponse batchFindOrderProcess(1:Message message);
    //竞技彩出票回调接口
    bool sportOrderAfterPrint(1:SportPrint sportPrint);
}
