namespace java com.cqfc.protocol.appendtask

struct AppendTaskDetail{
    1:i64 detailId,	//追号任务详情ID(自增)
    2:string appendTaskId,	//追号任务ID
    3:string partnerId,	//渠道ID
    4:string issueNo,	//期号
    5:string lotteryId,	//彩种ID
    6:i64 userId,	//用户ID
    7:string playType,	//玩法
    8:i64 totalMoney,	//投注总金额
    9:i32 multiple,	//倍数
    10:i32 noteNumber,	//注数
    11:string lotteryNumber,	//当期开奖号码
    12:i64 afterTaxMoney,	//税后奖金
    13:i32 appendDetailStatus,	//追号详情状态：0等待交易  1交易中  2交易成功  3交易失败 4交易取消 
    14:i32 createOrderTimes,	//下单重复次数
    15:string orderNo,	//订单编号
    16:string ball,	//投注内容
    17:string createTime,	//生成时间
    18:string lastUpdateTime	//最后更新时间
}
struct AppendTask{
    1:string appendTaskId,	//追号任务ID
    2:string partnerId	//渠道商ID
    3:string lotteryId,	//彩种ID
    4:string ball,	//投注内容
    5:string beginIssueNo,	//开始期号
    6:i32 appendQuantity,	//追号期数
    7:i32 remainingQuantity,	//剩余期数
    8:i32 finishedNum,	//完成期数
    9:i32 cancelNum,	//取消期数
    10:i32 stopFlag,	//终止条件：0不停追  1中任意奖停追  2中大奖停追
    11:i32 appendStatus,	//追号任务状态：1追号正常  2追号完成 3追号取消
    12:i64 appendTotalMoney,	//追号总金额
    13:i64 finishedMoney,	//完成金额
    14:i64 cancelMoney,	//取消金额
    15:i64 winningTotalMoney,	//总中奖金额
    16:i32 perNoteNumber,	//每次注数
    17:i64 userId,	//用户ID
    18:string playType,	//玩法
    19:string newAppendIssueNo,	//最新追过的期号
    20:string freezeSerialNumber,	//冻结账户金额流水号
    21:string createTime,	//发起时间
    22:string lastUpdateTime,	//最后更新时间
    23:list<AppendTaskDetail> appendTaskDetailList
}
struct AppendTaskIndex{
	1:string partnerId,	//渠道ID
	2:string partnerTradeId,	//合作商交易ID
	3:string appendTaskId,	//追号任务ID
	4:i64 userId,	//用户ID
	5:string createTime	//创建时间
}
service AppendTaskService {
    //新增追号任务
    i32 addAppendTask(1:AppendTask appendTask);
    
    //查询追号任务信息
    AppendTask findAppendTaskById(1:string appendTaskId);
    
    //查询最小期号追号任务明细
    AppendTaskDetail findMinAppendTaskDetail(1:string partnerId, 2:string partnerTradeId);
    
    //停止追号
    list<AppendTaskDetail> stopAppendTask(1:string appendTaskId,2:list<string> issueNoList);
    
    //获取追号任务状态
    i32 getAppendTaskStatus(1:string appendTaskId);
    
    //创建追号明细订单后更新追号任务信息
    i32 updateAppendAfterOrder(1:i64 appendTaskDetailId,2:string orderNo);
    
    //通过订单编号获取冻结金额序列号（userId用于分库分表）
    string getRefundSerialNumberByOrderNo(1:string orderNo,2:string userId);
    
    //取消追号任务(冻结追号金额失败时使用)
    i32 cancelAppendTask(1:string appendTaskId);
    
    //追号明细订单出票回调后,更新追号明细状态
    i32 updateDetailAfterPrint(1:string orderNo,2:bool isPrintSuccess);
    
    //订单中奖后更新追号信息
    i32 updateAppendAfterOrderPrize(1:string orderNo,2:i32 prizeLevel,3:i64 userId);
    
}