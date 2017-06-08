namespace java com.cqfc.protocol.accessback

struct ResultMessage{
	1:i32 statusCode, //状态码
	2:string msg //状态码说明信息
}

struct TicknumRecord{
    1:string gameId,	//彩种
    2:string issue,	//期号
    3:i64 successNum,	//平台成功出票数
    4:i64 failNum,	//平台失败出票数
}
service AccessBackService{
	//入口回调通知
	ResultMessage sendAccessBackMessage(1:string parnterId, 2:string xmlStr);
	
	TicknumRecord getTicknumRecord(1:string gameId, 2:string issue);
}