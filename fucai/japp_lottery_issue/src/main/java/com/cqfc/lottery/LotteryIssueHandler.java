package com.cqfc.lottery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.lottery.service.ILotteryIssueService;
import com.cqfc.protocol.lotteryissue.DrawResultData;
import com.cqfc.protocol.lotteryissue.IssueSport;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryIssueService;
import com.cqfc.protocol.lotteryissue.LotteryItem;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveData;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.protocol.lotteryissue.MatchFootballDate;
import com.cqfc.protocol.lotteryissue.ReturnData;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.OrderStatus;
import com.cqfc.util.OrderUtil;
import com.cqfc.util.SportIssueConstant;
import com.jami.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class LotteryIssueHandler implements LotteryIssueService.Iface {

	@Resource
	private ILotteryIssueService lotteryIssueService;

	/**
	 * 新增/修改彩种信息
	 * 
	 * @param lotteryItem
	 * @return
	 * @throws TException
	 */
	@Override
	public int addOrUpdateLotteryItem(LotteryItem lotteryItem) throws TException {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueService.addOrUpdateLotteryItem(lotteryItem);
		} catch (Exception e) {
			Log.run.error("操作彩种信息发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 获取所有彩种信息
	 * 
	 * @return
	 * @throws TException
	 */
	@Override
	public List<LotteryItem> getLotteryItemList() throws TException {
		List<LotteryItem> itemList = null;
		try {
			itemList = lotteryIssueService.getLotteryItemList();
		} catch (Exception e) {
			Log.run.error("获取所有彩种信息发生异常", e);
			return null;
		}
		return itemList;
	}

	/**
	 * 根据参数查询彩种信息
	 * 
	 * @param lotteryId
	 * @return
	 * @throws TException
	 */
	@Override
	public LotteryItem findLotteryItemByLotteryId(String lotteryId) throws TException {
		LotteryItem item = null;
		try {
			item = lotteryIssueService.findLotteryItemByLotteryId(lotteryId);
		} catch (Exception e) {
			Log.run.error("根据参数查询彩种信息发生异常", e);
			return null;
		}
		return item;
	}

	/**
	 * 创建彩种期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 * @throws TException
	 */
	@Override
	public int createLotteryIssue(String beginTime, String endTime, String lotteryId) throws TException {
		int isSuccess = 0;
		try {
			String createMinTime = getCreateMinTime(lotteryId);
			int compareValue = DateUtil.compareDate(createMinTime, beginTime);
			int beginEndValue = DateUtil.compareDate(beginTime, endTime);
			if (compareValue == 0 && beginEndValue >= 0) {
				DbGenerator.setDynamicMasterSource();
				isSuccess = lotteryIssueService.createLotteryIssue(beginTime, endTime, lotteryId);
			}
		} catch (Exception e) {
			Log.run.error("创建彩种期号发生异常,彩种：" + lotteryId + ",开始时间：" + beginTime + ",结束时间：" + endTime, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 分页查询期号信息
	 * 
	 * @param lotteryIssue
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @return
	 * @throws TException
	 */
	@Override
	public ReturnData getLotteryIssueList(LotteryIssue lotteryIssue, int currentPage, int pageSize) throws TException {
		ReturnData returnData = null;
		try {
			returnData = lotteryIssueService.getLotteryIssueList(lotteryIssue, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("分页查询期号信息发生异常", e);
			return null;
		}
		return returnData;
	}

	/**
	 * 查询彩种期号信息
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @param issueNo
	 *            彩种期号
	 * @return
	 * @throws TException
	 */
	@Override
	public LotteryIssue findLotteryIssue(String lotteryId, String issueNo) throws TException {
		LotteryIssue issue = null;
		try {
			// if
			// (lotteryId.equals(IssueConstant.SportLotteryType.LZC_R9.getValue()))
			// {
			// lotteryId = IssueConstant.SportLotteryType.LZC_SF.getValue();
			// }
			issue = lotteryIssueService.findLotteryIssue(lotteryId, issueNo);
		} catch (Exception e) {
			Log.run.error("查询彩种期号信息发生异常,彩种：" + lotteryId + ",期号：" + issueNo, e);
			return null;
		}
		return issue;
	}

	/**
	 * 获取最小创建期号时间
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 * @throws TException
	 */
	@Override
	public String getCreateMinTime(String lotteryId) throws TException {
		String createMinTime = "";
		try {
			LotteryIssue lotteryIssue = lotteryIssueService.getMaxLotteryIssue(lotteryId);
			if (null != lotteryIssue) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = simpleDateFormat.parse(lotteryIssue.getDrawTime());
				if (IssueConstant.LOTTERYID_SSC.equals(lotteryId)
						&& "00:00:00".equals(DateUtil.getDateTime("HH:mm:ss", date))) {
					createMinTime = DateUtil.getDateTime("yyyy-MM-dd", date);
				} else {
					createMinTime = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.getOffsetDate(date, 1));
				}
			}
		} catch (Exception e) {
			Log.run.error("获取最小创建期号时间发生异常,彩种：" + lotteryId, e);
			return "";
		}
		return createMinTime;
	}

	/**
	 * 更新彩种期号状态
	 * 
	 * @param issueId
	 *            期号ID
	 * @param state
	 *            期号状态:1未销售 2销售中 3销售截止 4已保底 5已出票 6已撤单 7已转移 8已开奖 9过关中 10已过关
	 *            11过关已审核 12算奖中 13已算奖待审核 14已算奖审核中 15算奖已审核 16派奖中 17已派奖
	 * 
	 * @return
	 * @throws TException
	 */
	@Override
	public int updateLotteryIssueState(int issueId, int state) throws TException {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueService.updateLotteryIssueState(issueId, state);
		} catch (Exception e) {
			Log.run.error("更新彩种期号状态发生异常,期号信息ID：" + issueId, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 查询开奖结果
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @param issueNo
	 *            彩种期号
	 * @return
	 * @throws TException
	 */
	@Override
	public LotteryDrawResult findLotteryDrawResult(String lotteryId, String issueNo) throws TException {
		LotteryDrawResult drawResult = null;
		try {
			drawResult = lotteryIssueService.findLotteryDrawResult(lotteryId, issueNo);
		} catch (Exception e) {
			Log.run.error("查询开奖结果发生异常,彩种：" + lotteryId + ",期号：" + issueNo, e);
			return null;
		}
		return drawResult;
	}

	/**
	 * 分页查询开奖结果
	 * 
	 * @param lotteryDrawResult
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws TException
	 */
	@Override
	public DrawResultData getLotteryDrawResultList(LotteryDrawResult lotteryDrawResult, int currentPage, int pageSize)
			throws TException {
		DrawResultData drawResultData = null;
		try {
			drawResultData = lotteryIssueService.getLotteryDrawResultList(lotteryDrawResult, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("分页查询开奖结果发生异常", e);
			return null;
		}
		return drawResultData;
	}

	/**
	 * 根据状态查询期号列表（管理平台审核使用）state值对应：1：中奖审核未审核13 2：中奖审核通过15、16、17 3： 派奖审核未审核 15
	 * 4：派奖审核通过17
	 * 
	 * @param state
	 * @return
	 * @throws TException
	 */
	@Override
	public ReturnData getLotteryIssueByParam(LotteryIssue lotteryIssue, int currentPage, int pageSize)
			throws TException {
		ReturnData returnData = null;
		try {
			returnData = lotteryIssueService.getLotteryIssueByParam(lotteryIssue, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("根据状态查询期号列表（管理平台审核使用）发生异常", e);
			return null;
		}
		return returnData;
	}

	/**
	 * 更新期号状态
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @param state
	 * @return
	 * @throws TException
	 */
	@Override
	public int updateLotteryIssueStateByParam(String lotteryId, String issueNo, int state) throws TException {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueService.updateLotteryIssueStateByParam(lotteryId, issueNo, state);
		} catch (Exception e) {
			Log.run.error("更新期号状态发生异常,彩种：" + lotteryId + ",期号：" + issueNo + ",更新状态：" + state, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 删除期号
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 * @throws TException
	 */
	@Override
	public int deleteIssueNo(String lotteryId, String issueNo) throws TException {
		int isSuccess = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			isSuccess = lotteryIssueService.deleteIssueNo(lotteryId, issueNo);
		} catch (Exception e) {
			Log.run.error("删除期号发生异常,彩种：" + lotteryId + ",期号：" + issueNo, e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public int updateLotteryIssue(LotteryIssue lotteryIssue) throws TException {
		int isSuccess = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			isSuccess = lotteryIssueService.updateLotteryIssue(lotteryIssue);
		} catch (Exception e) {
			return 0;
		}
		return isSuccess;
	}

	// ------------------------------------------竞技彩IDL接口--------------------------------------------------

	@Override
	public MatchCompetive getMatchCompetiveResultList(String wareIssue, String transferId) throws TException {
		// TODO Auto-generated method stub
		MatchCompetive matchCompetiveresult = null;
		try {
			matchCompetiveresult = lotteryIssueService.getMatchCompetiveResultList(wareIssue, transferId);
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮北单赛事的所有赛果发生异常,transferId=" + transferId, e);
		}
		return matchCompetiveresult;
	}

	@Override
	public MatchCompetive getMatchCompetiveResult(String wareIssue, String lotteryId, String transferId)
			throws TException {
		// TODO Auto-generated method stub
		MatchCompetive result = null;
		try {
			result = lotteryIssueService.getMatchCompetiveResult(wareIssue, lotteryId, transferId);
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮赛事的赛果发生异常,transferId=" + transferId + ",lotteryId=" + lotteryId, e);
		}
		return result;
	}

	@Override
	public MatchCompetive getMatchCompetive(String wareIssue, String transferId) throws TException {
		// TODO Auto-generated method stub
		MatchCompetive matchCompetive = null;
		try {
			matchCompetive = lotteryIssueService.getMatchCompetive(wareIssue, transferId);
			Log.run.debug("获取赛事信息,matchCompetive=" + matchCompetive);
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮赛事的赛果发生异常,transferId=" + transferId, e);
		}
		return matchCompetive;
	}

	@Override
	public int createIssueSport(List<IssueSport> issueSportList) throws TException {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			int len = issueSportList.size();
			Log.run.debug("创建竞技彩期号入库,listSize=%d", len);
			if (len == 0) {
				return returnValue;
			}
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueService.createIssueSport(issueSportList);
		} catch (Exception e) {
			Log.run.error("创建竞技彩期号发生异常", e);
			return 0;
		}
		return returnValue;
	}

	@Override
	public IssueSport findIssueSport(String lotteryId, String wareIssue) throws TException {
		// TODO Auto-generated method stub
		IssueSport issueSport = null;
		try {
			// if
			// (lotteryId.equals(IssueConstant.SportLotteryType.LZC_R9.getValue()))
			// {
			// lotteryId = IssueConstant.SportLotteryType.LZC_SF.getValue();
			// }
			issueSport = lotteryIssueService.findIssueSport(lotteryId, wareIssue);
		} catch (Exception e) {
			Log.run.error("查询竞技彩期号发生异常", e);
		}
		return issueSport;
	}

	@Override
	public List<MatchCompetive> getMatchCompetiveListByMatchType(String lotteryId) throws TException {
		// TODO Auto-generated method stub
		List<MatchCompetive> matchCompetiveList = null;
		int matchType = 0;
		try {
			int type = OrderUtil.getJcCategoryDetail(lotteryId);

			if (type == OrderStatus.LotteryType.JJZC_GAME.getType()) {
				matchType = SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue();
			} else if (type == OrderStatus.LotteryType.JJLC_GAME.getType()) {
				matchType = SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue();
			} else if (type == OrderStatus.LotteryType.JJBD_GAME.getType()) {
				matchType = SportIssueConstant.CompetitionMatchType.BEIDAN.getValue();
			} else if (type == OrderStatus.LotteryType.JJBDSFGG_GAME.getType()) {
				matchType = SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue();
			}
			matchCompetiveList = lotteryIssueService.getMatchCompetiveListByMatchType(lotteryId, matchType);
		} catch (Exception e) {
			Log.run.error("根据赛事类型获取竞足竞篮所有在售赛事发生异常,matchType=" + matchType, e);
		}
		return matchCompetiveList;
	}

	@Override
	public int createLotteryDrawResult(LotteryDrawResult lotteryDrawResult) throws TException {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			String lotteryId = lotteryDrawResult.getLotteryId();
			String issueNo = lotteryDrawResult.getIssueNo();
			LotteryIssue issue = lotteryIssueService.findLotteryIssue(lotteryId, issueNo);
			if (null != issue && !"".equals(issue)) {
				String drawResult = issue.getDrawResult();
				if (null != drawResult && !"".equals(drawResult)) {
					return returnValue;
				}
			}
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueService.addLotteryDrawResult(lotteryDrawResult);
		} catch (Exception e) {
			Log.run.error("数字彩新增开奖结果发生异常", e);
		}
		return returnValue;
	}

	@Override
	public boolean createMatchCompetiveList(List<MatchCompetive> matchCompetiveList) throws TException {
		// TODO Auto-generated method stub
		boolean returnValue = false;
		try {
			Log.run.debug("竞足竞篮北单赛事信息请求入库,listSize=%d", matchCompetiveList.size());
			if (null == matchCompetiveList || matchCompetiveList.size() == 0) {
				return returnValue;
			}
			DbGenerator.setDynamicMasterSource();
			int value = lotteryIssueService.createMatchCompetiveAndPlayList(matchCompetiveList);
			returnValue = value > 0 ? true : false;
		} catch (Exception e) {
			Log.run.error("创建竞足竞篮赛事和玩法列表发生异常", e);
		}
		return returnValue;
	}

	@Override
	public boolean createMatchCompetiveResultList(List<MatchCompetive> matchCompetiveList) throws TException {
		// TODO Auto-generated method stub
		boolean returnValue = false;
		try {
			Log.run.debug("竞足竞篮北单赛事开奖结果请求入库,listSize=%d", matchCompetiveList.size());
			DbGenerator.setDynamicMasterSource();
			int value = lotteryIssueService.createMatchCompetiveResultList(matchCompetiveList);
			returnValue = value > 0 ? true : false;
		} catch (Exception e) {
			Log.run.error("竞足竞篮赛事开奖结果入库发生异常", e);
		}
		return returnValue;
	}

	@Override
	public MatchCompetiveData getMatchCompetiveList(MatchCompetive matchCompetive, int currentPage, int pageSize)
			throws TException {
		// TODO Auto-generated method stub
		MatchCompetiveData matchCompetiveData = null;
		try {
			matchCompetiveData = lotteryIssueService.getMatchCompetiveList(matchCompetive, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("根据条件搜索竞足竞篮赛事列表发生异常", e);
		}
		return matchCompetiveData;
	}

	@Override
	public int updateMatchCompetive(MatchCompetive matchCompetive) throws TException {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			returnValue = lotteryIssueService.updateMatchCompetive(matchCompetive);
		} catch (Exception e) {
			Log.run.error("修改竞足竞篮赛事信息发生异常", e);
		}
		return returnValue;
	}

	@Override
	public int deleteMatchCompetive(String wareIssue, String transferId) throws TException {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueService.deleteMatchCompetive(wareIssue, transferId);
		} catch (Exception e) {
			Log.run.error("删除竞足竞篮赛事信息发生异常", e);
		}
		return returnValue;
	}

	@Override
	public MatchCompetive getMatchCompetiveByLotteryId(String wareIssue, String transferId, String lotteryId)
			throws TException {
		// TODO Auto-generated method stub
		MatchCompetive matchCompetive = null;
		try {
			matchCompetive = lotteryIssueService.getMatchCompetiveByLotteryId(wareIssue, transferId, lotteryId);
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮北单赛事彩种信息发生异常,wareIssue=" + wareIssue + ",transferId=" + transferId + ",lotteryId="
					+ lotteryId, e);
		}
		return matchCompetive;
	}

	/**
	 * 创建老足彩赛事信息
	 * 
	 * @param matchFootball
	 * @return
	 * @throws TException
	 */
	@Override
	public boolean createMatchFootball(List<MatchFootball> matchFootballList) throws TException {
		// TODO Auto-generated method stub
		boolean returnValue = false;
		try {
			int size = matchFootballList.size();
			Log.run.debug("老足彩赛事信息请求入库,listSize=%d", size);
			if (null == matchFootballList || size == 0) {
				return returnValue;
			}
			// 判断老足彩赛事信息是否存在,存在则不再进行入库.
			MatchFootball match = matchFootballList.get(0);
			String lotteryId = match.getLotteryId();
			String wareIssue = match.getWareIssue();
			int matchSize = lotteryIssueService.countMatchFootball(lotteryId, wareIssue);
			if (matchSize > 0 && matchSize != size) {
				return returnValue;
			}
			DbGenerator.setDynamicMasterSource();
			int value = lotteryIssueService.createMatchFootball(matchFootballList);
			returnValue = value > 0 ? true : false;
		} catch (Exception e) {
			Log.run.error("创建老足彩赛事信息发生异常", e);
		}
		return returnValue;
	}

	@Override
	public List<MatchFootball> getMatchFootballList(String lotteryId, String wareIssue) throws TException {
		// TODO Auto-generated method stub
		List<MatchFootball> matchFootballList = null;
		try {
			// if
			// (lotteryId.equals(IssueConstant.SportLotteryType.LZC_R9.getValue()))
			// {
			// lotteryId = IssueConstant.SportLotteryType.LZC_SF.getValue();
			// }
			matchFootballList = lotteryIssueService.getMatchFootballList(lotteryId, wareIssue);
		} catch (Exception e) {
			Log.run.error("获取老足彩赛事信息发生异常,wareIssue=" + wareIssue + ",lotteryId=" + lotteryId, e);
		}
		return matchFootballList;
	}

	@Override
	public int createLZCLotteryIssue(LotteryIssue lotteryIssue) throws TException {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			returnValue = lotteryIssueService.createLZCLotteryIssue(lotteryIssue);
		} catch (Exception e) {
			Log.run.error("创建老足彩期号信息发生异常", e);
		}
		return returnValue;
	}

	@Override
	public MatchFootballDate getMatchFootballDate(MatchFootball matchFootball,
			int currentPage, int pageSize) throws TException {
		MatchFootballDate matchFootballDate = null;
		
		try {
			matchFootballDate = lotteryIssueService.getMatchFootballDate(matchFootball, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("后台分页查询老足彩赛事出现异常", e);
		}
		
		return matchFootballDate;
	}

	@Override
	public MatchFootball getMatchFootball(String lotteryId, String wareIssue,
			String matchNo) throws TException {
		MatchFootball matchFootball = null;	
		try {
			matchFootball = lotteryIssueService.getMatchFootball(lotteryId, wareIssue, matchNo);
		} catch (Exception e) {
			Log.run.error("获取老足彩赛事信息发生异常,wareIssue=" + wareIssue + ",lotteryId=" + lotteryId, e);
		}
		
		return matchFootball;
	}

	@Override
	public int updateMatchFootball(MatchFootball matchFootball)
			throws TException {
		int flag = 0;
		try {
			if(StringUtils.isEmpty(matchFootball.getWareIssue()) || StringUtils.isEmpty(matchFootball.getLotteryId())
					|| StringUtils.isEmpty(matchFootball.getMatchNo())){
				return flag;
			}
			
			flag = lotteryIssueService.updateMatchFootball(matchFootball);
		} catch (Exception e) {
			Log.run.error("修改老足彩信息异常:" + matchFootball, e);
		}
		return flag;
	}

	@Override
	public int saveOrUpdateMatchCompetiveResult(
			MatchCompetiveResult matchCompetiveResult) throws TException {
		
		int flag = 0;
		try {
			if(StringUtils.isEmpty(matchCompetiveResult.getWareIssue()) || StringUtils.isEmpty(matchCompetiveResult.getLotteryId())
					|| StringUtils.isEmpty(matchCompetiveResult.getTransferId())){
				return flag;
			}
			flag = lotteryIssueService.saveOrUpdateMatchCompetiveResult(matchCompetiveResult);
		} catch (Exception e) {
			Log.run.error("创建或修改赛事结果:" + matchCompetiveResult, e);
		}
		return flag;
	}

}
