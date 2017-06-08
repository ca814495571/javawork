namespace java com.cqfc.protocol.lotteryissue

struct LotteryItem{
    1:i32 id,	//id(自增)
    2:string lotteryId,	//彩种id
    3:string lotteryName,	//彩种名称
    4:i32 lotteryType,	//彩种类型: 1双色球，2福彩3D，3七乐彩，4幸运农场，5时时彩
    5:string createTime,	//创建时间
    6:string lastUpdateTime	//最后更新时间
}
struct LotteryIssue{
    1:i32 issueId,	//期号id(自增)
    2:string lotteryId,	//彩种Id
    3:string issueNo,	//期号
    4:string drawResult,	//开奖结果
    5:i32 state,	//期号状态:1未销售 2销售中 3销售截止 4已保底 5已出票 6已撤单 7已转移 8已开奖 9过关中 10已过关 11过关已审核 12算奖中 13已算奖待审核 14已算奖审核中 15算奖已审核 16派奖中 17已派奖
    6:string drawTime,	//开奖时间
    7:string beginTime,	//投注开始时间
    8:string singleEndTime,	//单式自购截止时间
    9:string compoundEndTime,	//复式自购截止时间
    10:string singleTogetherEndTime,	//单式合买截止时间
    11:string compoundTogetherEndTime,	//复式合买截止时间
    12:string singleUploadEndTime,	//单式上传截止时间
    13:string printBeginTime,	//出票开始时间
    14:string printEndTime,	//出票截止时间
    15:string officialBeginTime,	//官方销售开始时间
    16:string officialEndTime,	//官方销售截止时间
    17:i64 prizePool,	//奖池滚存
    18:string ext,	//扩展信息
    19:i64 salesVolume,	//销量
    20:string createTime,	//创建时间
    21:string lastUpdateTime	//最后更新时间
}
struct LotteryDrawLevel{
	1:i32 levelId,	//奖级id(自增)
	2:string lotteryId,	//彩种Id
	3:string issueNo,	//期号
	4:i32 level,	//奖级
	5:string levelName,	//奖级名称
	6:i64 money,	//该奖级的奖金
	7:i64 totalCount,	//该奖级的中奖人数
	8:string createTime,	//创建时间
	9:string lastUpdateTime	//最后更新时间
}
struct LotteryDrawResult{
	1:string lotteryId,	//彩种Id
	2:string issueNo,	//期号
	3:string drawResult,	//开奖结果
	4:i32 state,	//有效状态: 0无效 1有效
	5:i32 priority,	//优先级
	6:string ext,	//扩展信息
	7:string createTime,	//创建时间
	8:string lastUpdateTime,	//最后更新时间
	9:list<LotteryDrawLevel> lotteryDrawLevelList,	//奖级列表
	10:i64 prizePool,	//奖池滚存(获取开奖结果使用)
	11:i64 salesVolume,	//销量(获取开奖结果使用)
	12:LotteryIssue lotteryIssue	//102接口信息调用
}
struct LotteryTaskComplete{
	1:i32 id,	//ID(自增)
	2:string lotteryId,	//彩种id
	3:string issueNo,	//期号
	4:i32 taskType,	//任务类型: 6已撤单 13已算奖待审核 15算奖已审核  17已派奖
	5:string setNo,	//机器编号
	6:i32 status,	//状态：1完成 2处理中
	7:string createTime,	//创建时间
	8:string lastUpdateTime,	//最后更新时间
}
struct ReturnData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<LotteryIssue> resultList
}
struct DrawResultData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<LotteryDrawResult> resultList
}

//竞技彩期号
struct IssueSport{
	1:string lotteryId,	//彩种ID
	2:string wareIssue,	//期号
	3:string wareId,	//商品编号
	4:string beginSellTime,	//销售开始时间
	5:string endSellTime,	//销售结束时间
	6:string beginOfficialTime,	//官方开始时间
	7:string endOfficialTime,	//官方结束时间
	8:i32 wareState,	//期状态：0未销售 1销售中 2已停售
	9:string createTime,	//创建时间
	10:string lastUpdateTime	//最后更新时间
}

//竞足竞篮赛事玩法
struct MatchCompetivePlay{
	1:string lotteryId,	//彩种ID
	2:string wareIssue,	//期号
	3:string transferId,	//赛事转换ID
	4:string matchId,	//赛事ID
	5:i32 dgGdSaleStatus,	//单关固定销售状态：1开售 2停售
	6:i32 dgSaleStatus,	//单关浮动销售状态：1开售 2停售
	7:i32 ggSaleStatus,	//过关销售状态：1开售 2停售
	8:string dgPv,	//单关即时赔率
	9:string ggPv,	//过关即时赔率
	10:string dgRq,	//单关玩法让球（让分）
	11:string ggRq,	//过关玩法让球（让分）
	12:string createTime,	//创建时间
	13:string lastUpdateTime	//最后更新时间
}

//竞足竞篮北单开奖结果
struct MatchCompetiveResult{
	1:string wareIssue,	//期号
	2:string transferId,	//赛事转换ID
	3:string matchId,	//赛事ID
	4:string lotteryId,	//彩种ID
	5:i32 matchType,	//赛事类型：1足球 2篮球 3北单
	6:string sp,	//赔率
	7:string drawResult,	//赛果
	8:string rq,	//让球数（让分数）
	9:string lotteryName,	//彩种名称
	10:string createTime,	//创建时间
	11:string lastUpdateTime	//最后更新时间
}

//竞足竞篮北单赛事
struct MatchCompetive{
	1:string wareIssue,	//期号
	2:string matchId,	//赛事ID
	3:string transferId,	//赛事转换ID
	4:i32 matchType,	//竞彩类型：1竞足 2竞篮 3北单 4北单胜负过关
	5:string matchNo,	//赛事编号
	6:string matchName,	//赛事名称
	7:string homeTeam,	//主队名称
	8:string guestTeam,	//客队名称
	9:string drawResult,	//开奖结果
	10:string matchBeginTime,	//比赛开始时间
	11:string bettingDeadline,	//投注截止时间
	12:string matchEndTime,	//比赛结束时间（大约）
	13:string matchDate,	//赛事日期
	14:i32 isAllowModify,	//是否允许修改赛事信息 1允许 2不允许
	15:i32 matchStatus,		//赛事状态：-3比赛延期 -2比赛取消 -1比赛暂停销售 0比赛未开售 1比赛销售中
	16:list<MatchCompetivePlay> matchCompetivePlayList,	//赛事玩法列表
	17:list<MatchCompetiveResult> matchCompetiveResultList,	//竞足竞篮开奖结果列表
	18:string createTime,	//创建时间
	19:string lastUpdateTime,	//最后更新时间
	20:string beginMatchDate,	//赛事日期搜索开始时间
	21:string endMatchDate		//赛事日期搜索结束时间
}

//竞足竞篮赛事分页查询封装对象
struct MatchCompetiveData{
	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<MatchCompetive> matchCompetiveList
}

//老足彩赛事表
struct MatchFootball{
	1:string lotteryId,	//彩种
	2:string wareIssue,	//期号
	3:string matchNo,	//场次号
	4:string matchId,	//赛事ID
	5:string matchName,	//赛事名称
	6:string homeTeam,	//主队名称
	7:string guestTeam,	//客队名称
	8:string drawResult,	//开奖结果
	9:string matchBeginTime,	//比赛开始时间
	10:string sp,	//即时赔率(胜-平-负)
	11:string createTime,	//创建时间
	12:string lastUpdateTime	//最后更新时间
}

struct MatchFootballDate{

	1:i32 currentPage,
	2:i32 pageSize,
	3:i32 totalSize,
	4:list<MatchFootball> matchFootballList
}

service LotteryIssueService {
	//新增、修改彩种信息
	i32 addOrUpdateLotteryItem(1:LotteryItem lotteryItem);
	//查询彩种信息(列表)
	list<LotteryItem> getLotteryItemList();
	//根据彩种ID查询彩种信息
	LotteryItem findLotteryItemByLotteryId(1:string lotteryId);
	//创建彩种期号
	i32 createLotteryIssue(1:string beginTime,2:string endTime,3:string lotteryId);
	//查询期号信息(分页)
	ReturnData getLotteryIssueList(1:LotteryIssue lotteryIssue,2:i32 currentPage,3:i32 pageSize);
	//根据彩种ID、期号查询期号信息
	LotteryIssue findLotteryIssue(1:string lotteryId,2:string issueNo);
	//获取最小创建时间
	string getCreateMinTime(1:string lotteryId);
	//更新彩种期号状态
	i32 updateLotteryIssueState(1:i32 issueId,2:i32 state);
	//查询开奖结果
	LotteryDrawResult findLotteryDrawResult(1:string lotteryId,2:string issueNo);
	//分页查询开奖结果
	DrawResultData getLotteryDrawResultList(1:LotteryDrawResult lotteryDrawResult,2:i32 currentPage,3:i32 pageSize);
	//根据状态查询期号列表
	ReturnData getLotteryIssueByParam(1:LotteryIssue lotteryIssue,2:i32 currentPage,3:i32 pageSize);
	//更新彩种期号状态
	i32 updateLotteryIssueStateByParam(1:string lotteryId,2:string issueNo,3:i32 state);
	//删除某一期号
	i32 deleteIssueNo(1:string lotteryId,2:string issueNo);
	//修改期号时间
	i32 updateLotteryIssue(1:LotteryIssue lotteryIssue);
	//数字彩新增开奖结果
	i32 createLotteryDrawResult(1:LotteryDrawResult lotteryDrawResult);
	
	//创建老足彩期号信息
	i32 createLZCLotteryIssue(1:LotteryIssue lotteryIssue);
	
	
	//-----------------------竞技彩接口-----------------------
	//获取赛事所有彩种开奖结果
	MatchCompetive getMatchCompetiveResultList(1:string wareIssue,2:string transferId);
	//获取竞足竞篮彩种开奖结果
	MatchCompetive getMatchCompetiveResult(1:string wareIssue,2:string lotteryId,3:string transferId);
	//获取竞足竞篮北单赛事信息
	MatchCompetive getMatchCompetive(1:string wareIssue,2:string transferId);
	//获取竞足竞篮北单赛事彩种信息
	MatchCompetive getMatchCompetiveByLotteryId(1:string wareIssue,2:string transferId,3:string lotteryId);
	//创建竞技彩期号
	i32 createIssueSport(1:list<IssueSport> issueSportList);
	//查询竞技彩期号信息
	IssueSport findIssueSport(1:string lotteryId,2:string wareIssue);
	//获取竞足竞篮北单赛事信息
	list<MatchCompetive> getMatchCompetiveListByMatchType(1:string lotteryId);
	//新增竞足竞篮北单赛事信息入库
	bool createMatchCompetiveList(1:list<MatchCompetive> matchCompetiveList);
	//新增竞足竞篮北单赛事开奖结果入库
	bool createMatchCompetiveResultList(1:list<MatchCompetive> matchCompetiveList);
	//分页查询竞足竞篮赛事信息（管理平台）
	MatchCompetiveData getMatchCompetiveList(1:MatchCompetive matchCompetive,2:i32 currentPage,3:i32 pageSize);
	//修改竞足竞篮赛事信息（管理平台）
	i32 updateMatchCompetive(1:MatchCompetive matchCompetive);
	//删除竞足竞篮赛事信息（管理平台）
	i32 deleteMatchCompetive(1:string wareIssue,2:string transferId);
	//创建老足彩赛事信息
	bool createMatchFootball(1:list<MatchFootball> matchFootballList);
	//获取老足彩赛事信息
	list<MatchFootball> getMatchFootballList(1:string lotteryId,2:string wareIssue);
	
	//分页查询老足彩赛事信息(管理平台)
	MatchFootballDate getMatchFootballDate(1:MatchFootball matchFootball,2:i32 currentPage,3:i32 pageSize)
	//查询单个老足彩赛事(管理平台)
	MatchFootball getMatchFootball(1:string lotteryId,2:string wareIssue ,3:string matchNo)
	//修改老足彩赛事(管理平台)
	i32 updateMatchFootball(1:MatchFootball matchFootball)
	//创建或者修改赛事结果(管理平台)
	i32 saveOrUpdateMatchCompetiveResult(1:MatchCompetiveResult matchCompetiveResult)
	
	
}