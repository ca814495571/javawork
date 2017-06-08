namespace java com.cqfc.channel.protocol

struct ChannelCooperation{
    1:i32 id,
    2:string channelName,
    3:string channelKey,
    4:i16 status,
    5:i64 balanceAlarm,
    6:i64 accountType,
    7:i64 accountBalance,
    8:string minBalanceAlarm,
    9:string extension3,
    10:string registTime,
    11:string lastUpdateTime,
    12:string channelID,
    13:string channelAccountID,
    14:i32 credit,
    15:string remark,
    16:string password
}

struct ReturnData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<ChannelCooperation> resultList
}

service ChannelCooperationService {
    ReturnData getChannelCooperationList(1:ChannelCooperation channelCooperation,2:i32 currentPage,3:i32 pageSize),
    bool addChannelCooperation(1:ChannelCooperation channelCooperation);
}