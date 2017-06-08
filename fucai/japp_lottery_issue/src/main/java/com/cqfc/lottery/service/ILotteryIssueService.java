package com.cqfc.lottery.service;

import java.util.List;

import org.apache.thrift.TException;

import com.cqfc.protocol.lotteryissue.DrawResultData;
import com.cqfc.protocol.lotteryissue.IssueSport;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryItem;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveData;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.protocol.lotteryissue.MatchFootballDate;
import com.cqfc.protocol.lotteryissue.ReturnData;

/**
 * @author liwh
 */
public interface ILotteryIssueService {

	/**
	 * 新增/修改彩种信息
	 * 
	 * @param lotteryItem
	 * @return
	 */
	public int addOrUpdateLotteryItem(LotteryItem lotteryItem);

	/**
	 * 获取所有彩种信息
	 * 
	 * @return
	 */
	public List<LotteryItem> getLotteryItemList();

	/**
	 * 查询彩种信息
	 * 
	 * @param lotteryItem
	 * @return
	 */
	public LotteryItem findLotteryItemByLotteryId(String lotteryId);

	/**
	 * 查询彩种期号信息
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @param issueNo
	 *            期号
	 * @return
	 */
	public LotteryIssue findLotteryIssue(String lotteryId, String issueNo);

	/**
	 * 更新彩种期号状态
	 * 
	 * @param issueId
	 *            期号ID
	 * @param state
	 *            状态
	 * @return
	 */
	public int updateLotteryIssueState(int issueId, int state);

	/**
	 * 查询开奖结果
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @param issueNo
	 *            期号
	 * @return
	 */
	public LotteryDrawResult findLotteryDrawResult(String lotteryId, String issueNo);

	/**
	 * 新增开奖结果 (1创建开奖结果记录 2创建奖级信息 3更新期信息--奖池、开奖结果)
	 * 
	 * @param lotteryDrawResult
	 * @return
	 */
	public int addLotteryDrawResult(LotteryDrawResult lotteryDrawResult);

	/**
	 * 分页查询开奖结果
	 * 
	 * @param lotteryDrawResult
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public DrawResultData getLotteryDrawResultList(LotteryDrawResult lotteryDrawResult, int currentPage, int pageSize);

	/**
	 * 分页查询期号信息
	 * 
	 * @param lotteryIssue
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public ReturnData getLotteryIssueList(LotteryIssue lotteryIssue, int currentPage, int pageSize);

	/**
	 * 创建彩种期号
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param lotteryId
	 * @return
	 */
	public int createLotteryIssue(String beginTime, String endTime, String lotteryId);

	/**
	 * 获取彩种最大期号
	 * 
	 * @param lotteryId
	 * @return
	 */
	public LotteryIssue getMaxLotteryIssue(String lotteryId);

	/**
	 * 按期号状态分页查询期号信息(定时任务使用)
	 * 
	 * @param state
	 *            期号状态
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 */
	public List<LotteryIssue> getLotteryIssueListByState(int state, int currentPage, int pageSize);

	/**
	 * 根据状态查询期号列表
	 * 
	 * @param lotteryIssue
	 * @return
	 */
	public ReturnData getLotteryIssueByParam(LotteryIssue lotteryIssue, int currentPage, int pageSize);

	/**
	 * 新增期状态扫描任务完成记录
	 * 
	 * @param lotteryTaskComplete
	 * @return
	 */
	public int createLotteryTaskComplete(LotteryTaskComplete lotteryTaskComplete);

	/**
	 * 获取期状态扫描任务完成记录
	 * 
	 * @param lotteryId
	 * @param issueNoint
	 * @param type
	 * @return
	 */
	public List<LotteryTaskComplete> getLotteryTaskCompleteByType(String lotteryId, String issueNo, int type);

	/**
	 * 更新期号状态
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @param state
	 *            期号状态:1未销售 2销售中 3销售截止 4已保底 5已出票 6已撤单 7已转移 8已开奖 9过关中 10已过关
	 *            11过关已审核 12算奖中 13已算奖待审核 14已算奖审核中 15算奖已审核 16派奖中 17已派奖
	 * @return
	 */
	public int updateLotteryIssueStateByParam(String lotteryId, String issueNo, int state);

	/**
	 * 删除期号
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int deleteIssueNo(String lotteryId, String issueNo);

	/**
	 * 更新任务完成状态
	 * 
	 * @param lotteryTaskComplete
	 * @return
	 */
	public int updateTaskCompleteStatus(LotteryTaskComplete lotteryTaskComplete);

	/**
	 * 根据任务状态查询任务记录
	 * 
	 * @param taskType
	 * @param status
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LotteryTaskComplete> getLotteryTaskCompleteByStatus(int taskType, int status, int currentPage,
			int pageSize);

	/**
	 * 获取能取开奖结果的期号列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LotteryIssue> getDrawIssueList(int currentPage, int pageSize);

	/**
	 * 获取能出票的期号列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LotteryIssue> getCanPrintIssueList(int currentPage, int pageSize);

	/**
	 * 更新期号时间
	 * 
	 * @param lotteryIssue
	 * @return
	 */
	public int updateLotteryIssue(LotteryIssue lotteryIssue);

	/**
	 * 创建竞技彩期号
	 * 
	 * @param issueSportList
	 * @return
	 */
	public int createIssueSport(List<IssueSport> issueSportList);

	/**
	 * 更新竞技彩期号状态
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param wareState
	 * @return
	 */
	public int updateIssueSport(String lotteryId, String wareIssue, int wareState);

	/**
	 * 查询竞技彩期号
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	public IssueSport findIssueSport(String lotteryId, String wareIssue);

	/**
	 * 创建竞足竞篮赛事、玩法
	 * 
	 * @param matchCompetive
	 * @return
	 */
	public int createMatchCompetiveAndPlay(MatchCompetive matchCompetive);

	/**
	 * 创建竞足竞篮赛事、玩法列表
	 * 
	 * @param matchCompetiveList
	 * @return
	 */
	public int createMatchCompetiveAndPlayList(List<MatchCompetive> matchCompetiveList);

	/**
	 * 获取该赛事所有赛果
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	public MatchCompetive getMatchCompetiveResultList(String wareIssue, String transferId);

	/**
	 * 获取该赛事的彩种赛果
	 * 
	 * @param wareIssue
	 * @param lotteryId
	 * @param transferId
	 * @return
	 */
	public MatchCompetive getMatchCompetiveResult(String wareIssue, String lotteryId, String transferId);

	/**
	 * 获取竞足竞篮赛事信息
	 * 
	 * @param transferId
	 * @return
	 */
	public MatchCompetive getMatchCompetive(String wareIssue, String transferId);

	/**
	 * 根据赛事类型获取竞足竞篮所有在售赛事
	 * 
	 * @param lotteryId
	 * @param matchType
	 * @return
	 */
	public List<MatchCompetive> getMatchCompetiveListByMatchType(String lotteryId, int matchType);

	/**
	 * 竞足竞篮赛事开奖结果入库
	 * 
	 * @param matchCompetiveList
	 * @return
	 */
	public int createMatchCompetiveResultList(List<MatchCompetive> matchCompetiveList);

	/**
	 * 根据条件搜索竞足竞篮赛事列表
	 * 
	 * @param matchCompetive
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public MatchCompetiveData getMatchCompetiveList(MatchCompetive matchCompetive, int currentPage, int pageSize);

	/**
	 * 修改竞足竞篮赛事信息
	 * 
	 * @param matchCompetive
	 * @return
	 */
	public int updateMatchCompetive(MatchCompetive matchCompetive);

	/**
	 * 删除竞足竞篮赛事信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	public int deleteMatchCompetive(String wareIssue, String transferId);

	/**
	 * 获取竞足竞篮北单赛事彩种信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @param lotteryId
	 * @return
	 */
	public MatchCompetive getMatchCompetiveByLotteryId(String wareIssue, String transferId, String lotteryId);

	/**
	 * 创建老足彩赛事信息
	 * 
	 * @param matchFootballList
	 * @return
	 */
	public int createMatchFootball(List<MatchFootball> matchFootballList);

	/**
	 * 计算老足彩赛事数据
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	public int countMatchFootball(String lotteryId, String wareIssue);

	/**
	 * 获取老足彩赛事信息
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	public List<MatchFootball> getMatchFootballList(String lotteryId, String wareIssue);

	/**
	 * 创建老足彩期号信息
	 * 
	 * @param lotteryIssue
	 * @return
	 */
	public int createLZCLotteryIssue(LotteryIssue lotteryIssue);

	/**
	 * 分页获取竞足竞篮北单算奖赛事数据
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<MatchCompetive> getMatchCompetiveListForCalPrize(int currentPage, int pageSize);

	/**
	 * 更新竞足竞篮北单赛事状态
	 * 
	 * @param transferId
	 * @param matchStatus
	 * @return
	 */
	public int updateMatchCompetiveStatus(String transferId, int matchStatus);
	
	/**
	 * 分页查询老足彩赛事信息(管理平台)
	 * @param matchFootball
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public MatchFootballDate getMatchFootballDate(MatchFootball matchFootball,
			int currentPage, int pageSize);
	/**
	 * 查询单个老足彩赛事(管理平台)
	 * @param lotteryId
	 * @param wareIssue
	 * @param matchNo
	 * @return
	 */
	public MatchFootball getMatchFootball(String lotteryId, String wareIssue,
			String matchNo);
	/**
	 * 修改老足彩赛事(管理平台)
	 * @param matchFootball
	 * @return
	 */
	public int updateMatchFootball(MatchFootball matchFootball);
	
	/**
	 * 创建或修改赛事结果(管理平台)
	 * @param matchCompetiveResult
	 * @return
	 */
	public int saveOrUpdateMatchCompetiveResult(
			MatchCompetiveResult matchCompetiveResult);
}
