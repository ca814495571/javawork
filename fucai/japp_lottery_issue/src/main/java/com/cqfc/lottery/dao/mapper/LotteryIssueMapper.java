package com.cqfc.lottery.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.lotteryissue.IssueSport;
import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryItem;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetivePlay;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
/**
 * @author liwh
 * 
 */
public interface LotteryIssueMapper extends BaseMapper {

	@Insert("insert into t_lottery_item (lotteryId,lotteryName,lotteryType,createTime)"
			+ "values (#{lotteryId},#{lotteryName},#{lotteryType},#{createTime})")
	public int addLotteryItem(LotteryItem lotteryItem);

	@Update("update t_lottery_item set lotteryId=#{lotteryId},lotteryName=#{lotteryName},lotteryType=#{lotteryType} where id=#{id}")
	public int updateLotteryItem(LotteryItem lotteryItem);

	@Select("select * from t_lottery_item where lotteryId=#{lotteryId}")
	public LotteryItem findLotteryItemByLotteryId(@Param("lotteryId") String lotteryId);

	@Select("select * from t_lottery_issue where ${conditions}")
	public List<LotteryIssue> getLotteryIssueList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_issue where ${conditions}")
	public int countTotalSize(@Param("conditions") String conditions);

	@Select("select * from t_lottery_issue where ${conditions}")
	public List<LotteryIssue> findLotteryIssue(@Param("conditions") String conditions);

	@Select("select * from t_lottery_issue where issueId=(select max(issueId) from t_lottery_issue where lotteryId=#{lotteryId})")
	public LotteryIssue getMaxIssueNo(@Param("lotteryId") String lotteryId);

	@Update("update t_lottery_issue set state=#{state} where issueId=#{issueId} and state<=#{state}")
	public int updateLotteryIssueState(@Param("issueId") int issueId, @Param("state") int state);

	@Select("select * from t_lottery_draw_result where lotteryId=#{lotteryId} and issueNo=#{issueNo}")
	public LotteryDrawResult findLotteryDrawResult(@Param("lotteryId") String lotteryId,
			@Param("issueNo") String issueNo);

	@Insert("insert into t_lottery_draw_result (lotteryId,issueNo,drawResult,priority,ext,createTime)"
			+ "values (#{lotteryId},#{issueNo},#{drawResult},#{priority},#{ext},now())")
	public int addLotteryDrawResult(LotteryDrawResult lotteryDrawResult);

	@Select("select * from t_lottery_draw_result where ${conditions}")
	public List<LotteryDrawResult> getLotteryDrawResultList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_draw_result where ${conditions}")
	public int countResultTotalSize(@Param("conditions") String conditions);

	@Insert("insert into t_lottery_issue "
			+ "(lotteryId,issueNo,drawTime,beginTime,singleEndTime,compoundEndTime,singleTogetherEndTime,compoundTogetherEndTime,singleUploadEndTime,printBeginTime,printEndTime,officialBeginTime,officialEndTime,prizePool,createTime)"
			+ "values (#{lotteryId},#{issueNo},#{drawTime},#{beginTime},#{singleEndTime},#{compoundEndTime},#{singleTogetherEndTime},#{compoundTogetherEndTime},#{singleUploadEndTime},#{printBeginTime},#{printEndTime},#{officialBeginTime},#{officialEndTime},#{prizePool},now())")
	public int addLotteryIssue(LotteryIssue lotteryIssue);

	@Select("select * from t_lottery_draw_level where lotteryId=#{lotteryId} and issueNo=#{issueNo}")
	public List<LotteryDrawLevel> findLotteryDrawLevelList(@Param("lotteryId") String lotteryId,
			@Param("issueNo") String issueNo);

	@Select("select * from t_lottery_item")
	public List<LotteryItem> getLotteryItemList();

	@Select("select * from t_lottery_issue where state=#{state} ${conditions}")
	public List<LotteryIssue> getLotteryIssueListByState(@Param("state") int state,
			@Param("conditions") String conditions);

	@Select("select * from t_lottery_issue where ${conditions}")
	public List<LotteryIssue> getLotteryIssueByParam(@Param("conditions") String conditions);

	@Insert("insert into t_lottery_task_complete(lotteryId,issueNo,taskType,setNo,status,createTime) "
			+ "values(#{lotteryId},#{issueNo},#{taskType},#{setNo},#{status},now())")
	public int createLotteryTaskComplete(LotteryTaskComplete lotteryTaskComplete);

	@Select("select * from t_lottery_task_complete where lotteryId=#{lotteryId} and issueNo=#{issueNo} and taskType=#{taskType}")
	public List<LotteryTaskComplete> getLotteryTaskCompleteByType(@Param("lotteryId") String lotteryId,
			@Param("issueNo") String issueNo, @Param("taskType") int taskType);

	@Update("update t_lottery_issue set state=#{state} where lotteryId=#{lotteryId} and issueNo=#{issueNo} and state<=#{state}")
	public int updateLotteryIssueStateByParam(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo,
			@Param("state") int state);

	@Insert("insert into t_lottery_draw_level(lotteryId,issueNo,level,levelName,money,totalCount,createTime) "
			+ "values(#{lotteryId},#{issueNo},#{level},#{levelName},#{money},#{totalCount},now())")
	public int createLotteryDrawLevel(LotteryDrawLevel lotteryDrawLevel);

	@Update("update t_lottery_issue set drawResult=#{drawResult},prizePool=#{prizePool},salesVolume=#{salesVolume}"
			+ " where lotteryId=#{lotteryId} and issueNo=#{issueNo}")
	public int updateIssueDrawResult(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo,
			@Param("drawResult") String drawResult, @Param("prizePool") long prizePool,
			@Param("salesVolume") long salesVolume);

	@Delete("delete from t_lottery_issue where lotteryId=#{lotteryId} and issueNo=#{issueNo} and state=1")
	public int deleteIssueNo(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo);

	@Update("update t_lottery_issue set issueNo=#{newIssueNo} where lotteryId=#{lotteryId} and issueNo=#{oldIssueNo}")
	public int updateIssueNo(@Param("lotteryId") String lotteryId, @Param("oldIssueNo") String oldIssueNo,
			@Param("newIssueNo") String newIssueNo);

	@Select("select count(*) from t_lottery_issue where lotteryId=#{lotteryId} and drawTime>#{beginTime} and drawTime<=#{endTime}")
	public int countIssue(@Param("lotteryId") String lotteryId, @Param("beginTime") String beginTime,
			@Param("endTime") String endTime);

	@Select("select * from t_lottery_task_complete where taskType=#{taskType} and status=#{status} ${conditions}")
	public List<LotteryTaskComplete> getLotteryTaskCompleteByStatus(@Param("taskType") int taskType,
			@Param("status") int status, @Param("conditions") String conditions);

	@Update("update t_lottery_task_complete set status=#{status} where lotteryId=#{lotteryId} and"
			+ " issueNo=#{issueNo} and setNo=#{setNo} and taskType=#{taskType}")
	public int updateTaskCompleteStatus(LotteryTaskComplete lotteryTaskComplete);

	@Select("SELECT * FROM t_lottery_issue WHERE drawTime<=NOW() AND LENGTH(trim(drawResult))<1 ${conditions};")
	public List<LotteryIssue> getDrawIssueList(@Param("conditions") String conditions);

	@Select("SELECT * FROM t_lottery_issue WHERE printBeginTime<=NOW() AND printEndTime>NOW() ${conditions};")
	public List<LotteryIssue> getCanPrintIssueList(@Param("conditions") String conditions);

	@Update("update t_lottery_issue set drawTime=#{param1.drawTime},beginTime=#{param1.beginTime},singleEndTime=#{param1.singleEndTime}"
			+ ",compoundEndTime=#{param1.compoundEndTime},singleTogetherEndTime=#{param1.singleTogetherEndTime}"
			+ ",compoundTogetherEndTime=#{param1.compoundTogetherEndTime},singleUploadEndTime=#{param1.singleUploadEndTime}"
			+ ",printBeginTime=#{param1.printBeginTime},printEndTime=#{param1.printEndTime} "
			+ ",officialBeginTime=#{param1.officialBeginTime},officialEndTime=#{param1.officialEndTime} "
			+ " where lotteryId=#{lotteryId} and issueNo=#{issueNo}")
	public int updateLotteryIssue(LotteryIssue lotteryIssue, @Param("lotteryId") String lotteryId,
			@Param("issueNo") String issueNo);

	/**
	 * 创建竞技彩期号
	 * 
	 * @param issueSport
	 * @return
	 */
	@Insert("insert into t_lottery_issue_sport(lotteryId,wareIssue,wareId,beginSellTime,endSellTime,beginOfficialTime,endOfficialTime,wareState,createTime) "
			+ "values(#{lotteryId},#{wareIssue},#{wareId},#{beginSellTime},#{endSellTime},#{beginOfficialTime},#{endOfficialTime},#{wareState},now())")
	public int createIssueSport(IssueSport issueSport);

	/**
	 * 更新竞技彩期号状态
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param wareState
	 * @return
	 */
	@Update("update t_lottery_issue_sport set wareState=#{wareState} where lotteryId=#{lotteryId} and"
			+ " wareIssue=#{wareIssue}")
	public int updateIssueSport(@Param("lotteryId") String lotteryId, @Param("wareIssue") String wareIssue,
			@Param("wareState") int wareState);

	/**
	 * 更新竞技彩期号结束时间
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param endSellTime
	 * @return
	 */
	@Update("update t_lottery_issue_sport set endSellTime=#{endSellTime},endOfficialTime=#{endSellTime} where lotteryId=#{lotteryId} and"
			+ " wareIssue=#{wareIssue}")
	public int updateIssueSportEndTime(@Param("lotteryId") String lotteryId, @Param("wareIssue") String wareIssue,
			@Param("endSellTime") String endSellTime);

	/**
	 * 查询竞技彩期号信息
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_issue_sport where ${conditions}")
	public List<IssueSport> findIssueSport(@Param("conditions") String conditions);

	/**
	 * 创建竞足竞篮赛事
	 * 
	 * @param matchCompetive
	 * @return
	 */
	@Insert("replace into t_lottery_match_competive(wareIssue,matchId,transferId,matchType,matchNo,"
			+ "matchName,homeTeam,guestTeam,matchBeginTime,bettingDeadline,matchEndTime,matchDate,createTime) "
			+ "values(#{wareIssue},#{matchId},#{transferId},#{matchType},#{matchNo},"
			+ "#{matchName},#{homeTeam},#{guestTeam},#{matchBeginTime},#{bettingDeadline},#{matchEndTime},#{matchDate},now())")
	public int createMatchCompetive(MatchCompetive matchCompetive);

	/**
	 * 创建竞足竞篮赛事玩法
	 * 
	 * @param matchCompetivePlay
	 * @return
	 */
	@Insert("replace into t_lottery_match_competive_play(wareIssue,lotteryId,transferId,matchId,dgGdSaleStatus,dgSaleStatus,"
			+ "ggSaleStatus,dgPv,ggPv,dgRq,ggRq,createTime) "
			+ "values(#{wareIssue},#{lotteryId},#{transferId},#{matchId},#{dgGdSaleStatus},#{dgSaleStatus},"
			+ "#{ggSaleStatus},#{dgPv},#{ggPv},#{dgRq},#{ggRq},now())")
	public int createMatchCompetivePlay(MatchCompetivePlay matchCompetivePlay);

	/**
	 * 删除赛事玩法
	 * 
	 * @param lotteryId
	 * @param transferId
	 * @return
	 */
	@Delete("delete from t_lottery_match_competive_play where wareIssue=#{wareIssue} and transferId=#{transferId}")
	public int deleteMatchCompetivePlay(@Param("wareIssue") String wareIssue, @Param("transferId") String transferId);

	/**
	 * 获取竞足竞篮赛果
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_match_competive_result where ${conditions}")
	public List<MatchCompetiveResult> getMatchCompetiveResultList(@Param("conditions") String conditions);

	/**
	 * 获取竞足竞篮赛事信息
	 * 
	 * @param transferId
	 * @return
	 */
	@Select("select * from t_lottery_match_competive where wareIssue=#{wareIssue} and transferId=#{transferId}")
	public MatchCompetive getMatchCompetive(@Param("wareIssue") String wareIssue, @Param("transferId") String transferId);

	/**
	 * 获取竞足竞篮赛事玩法信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	@Select("select * from t_lottery_match_competive_play where wareIssue=#{wareIssue} and transferId=#{transferId}")
	public List<MatchCompetivePlay> getMatchCompetivePlayList(@Param("wareIssue") String wareIssue,
			@Param("transferId") String transferId);

	/**
	 * 获取竞足竞篮赛事玩法信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	@Select("select * from t_lottery_match_competive_play where wareIssue=#{wareIssue} and transferId=#{transferId} and lotteryId=#{lotteryId}")
	public List<MatchCompetivePlay> getMatchCompetivePlay(@Param("wareIssue") String wareIssue,
			@Param("transferId") String transferId, @Param("lotteryId") String lotteryId);

	/**
	 * 根据赛事类型获取竞足竞篮所有在售赛事
	 * 
	 * @param matchType
	 * @return
	 */
	@Select("select * from t_lottery_match_competive where matchType=#{matchType} and bettingDeadline>now()")
	public List<MatchCompetive> getMatchCompetiveListByMatchType(@Param("matchType") int matchType);

	/**
	 * 查询赛事列表
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_match_competive where ${conditions}")
	public List<MatchCompetive> getMatchCompetiveList(@Param("conditions") String conditions);

	/**
	 * 更新竞足竞篮赛事开奖结果
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @param drawResult
	 * @return
	 */
	@Update("update t_lottery_match_competive set drawResult=#{drawResult},matchStatus=#{matchStatus} where wareIssue=#{wareIssue} and transferId=#{transferId}")
	public int updateMatchCompetiveDrawResult(@Param("wareIssue") String wareIssue,
			@Param("transferId") String transferId, @Param("drawResult") String drawResult,
			@Param("matchStatus") int matchStatus);

	/**
	 * 创建竞足竞篮赛事入库
	 * 
	 * @param matchCompetiveResult
	 * @return
	 */
	@Insert("insert into t_lottery_match_competive_result(wareIssue,transferId,lotteryId,"
			+ "matchId,matchType,sp,drawResult,rq,lotteryName,createTime)"
			+ " values(#{wareIssue},#{transferId},#{lotteryId},#{matchId},#{matchType},"
			+ "#{sp},#{drawResult},#{rq},#{lotteryName},now())")
	public int createMatchCompetiveResult(MatchCompetiveResult matchCompetiveResult);

	/**
	 * 修改竞足竞篮赛事信息
	 * 
	 * @param matchCompetive
	 * @return
	 */
	@Update("update t_lottery_match_competive set matchNo=#{matchNo},matchName=#{matchName},homeTeam=#{homeTeam},drawResult=#{drawResult},"
			+ "guestTeam=#{guestTeam},matchBeginTime=#{matchBeginTime},bettingDeadline=#{bettingDeadline},"
			+ "matchEndTime=#{matchEndTime},matchDate=#{matchDate},isAllowModify=#{isAllowModify},matchStatus=#{matchStatus}"
			+ " where transferId=#{transferId}")
	public int updateMatchCompetive(MatchCompetive matchCompetive);

	/**
	 * 删除竞足竞篮赛事信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	@Delete("delete from t_lottery_match_competive where wareIssue=#{wareIssue} and transferId=#{transferId}")
	public int deleteMatchCompetive(@Param("wareIssue") String wareIssue, @Param("transferId") String transferId);

	/**
	 * 计算竞足竞篮北单赛事总记录数
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select count(*) from t_lottery_match_competive where ${conditions}")
	public int countMatchTotalSize(@Param("conditions") String conditions);

	/**
	 * 获取老足彩最大期号
	 * 
	 * @param lotteryId
	 * @return
	 */
	@Select("select * from t_lottery_issue where issueNo=(select max(issueNo) from t_lottery_issue where lotteryId=#{lotteryId} and compoundEndTime<=now())")
	public LotteryIssue getMaxLotteryIssueByLotteryId(@Param("lotteryId") String lotteryId);

	/**
	 * 创建老足彩赛事信息
	 * 
	 * @param matchFootball
	 * @return
	 */
	@Insert("replace into t_lottery_match_football(lotteryId,wareIssue,matchNo,matchId,matchName,homeTeam,guestTeam,matchBeginTime,sp,createTime)"
			+ " values(#{lotteryId},#{wareIssue},#{matchNo},#{matchId},#{matchName},#{homeTeam},#{guestTeam},#{matchBeginTime},#{sp},now())")
	public int createMatchFootball(MatchFootball matchFootball);

	/**
	 * 查询老足彩赛事信息
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param matchNo
	 * @return
	 */
	@Select("select * from t_lottery_match_football where lotteryId=#{lotteryId} and wareIssue=#{wareIssue} and matchNo=#{matchNo}")
	public MatchFootball getMatchFootballByParams(@Param("lotteryId") String lotteryId,
			@Param("wareIssue") String wareIssue, @Param("matchNo") String matchNo);

	/**
	 * 计算老足彩赛事数据
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	@Select("select count(*) from t_lottery_match_football where lotteryId=#{lotteryId} and wareIssue=#{wareIssue}")
	public int countMatchFootball(@Param("lotteryId") String lotteryId, @Param("wareIssue") String wareIssue);

	/**
	 * 获取老足彩赛事信息
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	@Select("select * from t_lottery_match_football where lotteryId=#{lotteryId} and wareIssue=#{wareIssue} order by cast(matchNo as signed) ASC")
	public List<MatchFootball> getMatchFootballList(@Param("lotteryId") String lotteryId,
			@Param("wareIssue") String wareIssue);

	/**
	 * 查询老足彩期号信息
	 * 
	 * @param lotteryId
	 * @return
	 */
	@Select("select * from t_lottery_issue_sport where lotteryId=#{lotteryId} and wareIssue=(select min(wareIssue) from t_lottery_issue_sport where now()<endSellTime and lotteryId=#{lotteryId})")
	public IssueSport findLZCIssueSport(@Param("lotteryId") String lotteryId);

	/**
	 * 查询老足彩上一期的期号
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@Select("select * from t_lottery_issue where lotteryId=#{lotteryId} and issueNo<#{issueNo} order by issueNo desc limit 1")
	public LotteryIssue getMaxLotteryIssueByIssue(@Param("lotteryId") String lotteryId, @Param("issueNo") String issueNo);

	/**
	 * 分页获取竞足竞篮北单算奖赛事数据
	 * 
	 * @param conditions
	 * @return
	 */
	@Select("select * from t_lottery_match_competive ${conditions}")
	public List<MatchCompetive> getMatchCompetiveListForCalPrize(@Param("conditions") String conditions);

	/**
	 * 更新竞足竞篮北单赛事状态
	 * 
	 * @param transferId
	 * @param matchStatus
	 * @return
	 */
	@Update("update t_lottery_match_competive set matchStatus=#{matchStatus} where transferId=#{transferId}")
	public int updateMatchCompetiveStatus(@Param("transferId") String transferId, @Param("matchStatus") int matchStatus);


	/**
	 * 根据条件获取老足彩记录数
	 * @param where
	 * @return
	 */
	@Select("select count(*) from t_lottery_match_football ${where}")
	public int getMatchFootballTotalSize(@Param("where") String where);
	
	/**
	 * 根据条件获取老足彩信息列表
	 * @param where
	 * @return
	 */
	@Select("select * from t_lottery_match_football ${where}")
	public List<MatchFootball> getMatchFootballs(@Param("where") String where);

	/**
	 * 修改老足彩信息
	 * @param matchFootball
	 * @return
	 */
	@Update("update t_lottery_match_football set matchBeginTime =#{matchBeginTime} where wareIssue=#{wareIssue} and lotteryId=#{lotteryId} and matchNo=#{matchNo}")
	public int updateMatchFootball(MatchFootball matchFootball);

	/**
	 * 修改竞彩赛事结果
	 */
	@Update("update t_lottery_match_competive_result set sp=#{sp} ,drawResult=#{drawResult} where wareIssue=#{wareIssue} and lotteryId=#{lotteryId} and transferId=#{transferId}")
	public int updateMatchCompetiveResult(MatchCompetiveResult matchCompetiveResult);

}
