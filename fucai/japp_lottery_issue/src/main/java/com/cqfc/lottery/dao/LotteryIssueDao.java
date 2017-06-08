package com.cqfc.lottery.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.lottery.dao.mapper.LotteryIssueMapper;
import com.cqfc.protocol.lotteryissue.DrawResultData;
import com.cqfc.protocol.lotteryissue.IssueSport;
import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.lotteryissue.LotteryItem;
import com.cqfc.protocol.lotteryissue.LotteryTaskComplete;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveData;
import com.cqfc.protocol.lotteryissue.MatchCompetivePlay;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.protocol.lotteryissue.MatchFootballDate;
import com.cqfc.protocol.lotteryissue.ReturnData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.util.SportIssueConstant;
import com.jami.util.DbGenerator;
import com.jami.util.Log;
import com.jami.util.LotteryIssueConstant;
import com.jami.util.ScanLog;

/**
 * @author liwh
 */
@Repository
public class LotteryIssueDao {

	@Autowired
	private LotteryIssueMapper lotteryIssueMapper;

	@Autowired
	private MemcachedClient memcachedClient;

	/**
	 * 新增彩种
	 * 
	 * @param lotteryItem
	 * @return
	 */
	public int addLotteryItem(LotteryItem lotteryItem) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.addLotteryItem(lotteryItem);
		} catch (DuplicateKeyException e) {
			Log.run.error("新增彩种发生唯一键冲突,lotteryId=" + lotteryItem.getLotteryId(), e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error("新增彩种发生异常", e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 修改彩种(更新彩种缓存)
	 * 
	 * @param lotteryItem
	 * @return
	 */
	public int updateLotteryItem(LotteryItem lotteryItem) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateLotteryItem(lotteryItem);
			if (returnValue == 1) {
				String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryItem.getLotteryId();
				memcachedClient.set(memcachedKey, 0, lotteryItem);
			}
		} catch (DuplicateKeyException e) {
			Log.run.error("更新彩种发生唯一键冲突,lotteryId=" + lotteryItem.getLotteryId(), e);
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			Log.run.error("更新彩种发生异常", e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 查询彩种信息（查询后更新缓存）
	 * 
	 * @param lotteryId
	 * @return
	 */
	public LotteryItem findLotteryItemByLotteryId(String lotteryId) {
		LotteryItem lotteryItem = null;
		String memKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId;
		try {
			DbGenerator.setDynamicSlaveSource();
			lotteryItem = memcachedClient.get(memKey);
			if (null == lotteryItem || "".equals(lotteryItem)) {
				lotteryItem = lotteryIssueMapper.findLotteryItemByLotteryId(lotteryId);
				if (null != lotteryItem) {
					memcachedClient.set(memKey, 0, lotteryItem);
				}
			}
		} catch (Exception e) {
			Log.run.error("查询彩种信息发生异常", e);
			return null;
		}
		return lotteryItem;
	}

	/**
	 * 分页搜索彩种期号信息
	 * 
	 * @param lotteryIssue
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public ReturnData getLotteryIssueList(LotteryIssue lotteryIssue, int currentPage, int pageSize) {
		DbGenerator.setDynamicSlaveSource();
		ReturnData returnData = new ReturnData();
		StringBuffer conditions = new StringBuffer();
		conditions.append(" 1=1");
		if (null != lotteryIssue.getLotteryId() && !"".equals(lotteryIssue.getLotteryId())) {
			conditions.append(" and lotteryId='" + lotteryIssue.getLotteryId() + "'");
		}
		if (null != lotteryIssue.getIssueNo() && !"".equals(lotteryIssue.getIssueNo())) {
			conditions.append(" and issueNo='" + lotteryIssue.getIssueNo() + "'");
		}
		if (lotteryIssue.getState() > 0) {
			conditions.append(" and state=" + lotteryIssue.getState());
		}
		int totalSize = countTotalSize(conditions.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		conditions.append(" order by issueNo desc");
		if (totalPage >= currentPage) {
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
		}
		List<LotteryIssue> list = lotteryIssueMapper.getLotteryIssueList(conditions.toString());

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 计算彩种期号总记录数
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	public int countTotalSize(String conditions) {
		DbGenerator.setDynamicSlaveSource();
		return lotteryIssueMapper.countTotalSize(conditions);
	}

	/**
	 * 查询彩种期号信息（从数据库查询成功后设置缓存）
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public LotteryIssue findLotteryIssue(String lotteryId, String issueNo) {
		String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId + issueNo;
		LotteryIssue issue = null;
		StringBuffer conditions = new StringBuffer();
		conditions.append(" lotteryId='" + lotteryId + "'");
		List<LotteryIssue> issueList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			if (null != issueNo && !"".equals(issueNo)) {
				conditions.append(" and issueNo='" + issueNo + "'");
			} else {
				String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
				conditions.append(" and beginTime<='" + currentTime + "' and compoundEndTime>'" + currentTime + "'");
			}
			String issueId = (String) memcachedClient.get(memcachedKey);
			String issueKey = "";
			if (null != issueId && !"".equals(issueId)) {
				issue = (LotteryIssue) memcachedClient.get(issueId);
				if (null == issue || "".equals(issue)) {
					issueList = lotteryIssueMapper.findLotteryIssue(conditions.toString());
					if (null != issueList && issueList.size() > 0) {
						issue = issueList.get(0);
						if (null != issue && null != issueNo && !"".equals(issueNo)) {
							memcachedClient.set(memcachedKey, 0, String.valueOf(issue.getIssueId()));
						}
					}
				}
			} else {
				issueList = lotteryIssueMapper.findLotteryIssue(conditions.toString());
				if (null != issueList && issueList.size() > 0) {
					issue = issueList.get(0);
				}
				if (null != issue) {
					issueKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + String.valueOf(issue.getIssueId());
					if (null != issue && null != issueNo && !"".equals(issueNo)) {
						memcachedClient.set(memcachedKey, 0, issueKey);
					}
					memcachedClient.set(issueKey, 0, issue);
				}
			}
		} catch (Exception e) {
			Log.run.error("查询彩种期号信息发生异常,彩种ID：" + lotteryId + ",期号：" + issueNo, e);
			return null;
		}
		return issue;
	}

	/**
	 * 查询最大期号彩种期号信息
	 * 
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	public LotteryIssue getMaxIssueNo(String lotteryId) {
		LotteryIssue lotteryIssue = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			lotteryIssue = lotteryIssueMapper.getMaxIssueNo(lotteryId);
		} catch (Exception e) {
			Log.run.error("查询最大期号彩种期号信息发生异常,彩种ID：" + lotteryId, e);
			return null;
		}
		return lotteryIssue;
	}

	/**
	 * 更新彩种期号状态(更新彩种缓存)
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @param state
	 * @return
	 */
	public int updateLotteryIssueState(int issueId, int state) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateLotteryIssueState(issueId, state);
			if (returnValue == 1) {
				String issueIdKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + String.valueOf(issueId);
				memcachedClient.delete(issueIdKey);
			}
			Log.run.debug("更新彩种期号状态,issueId=%d,state=%d,returnValue=%d", issueId, state, returnValue);
		} catch (Exception e) {
			Log.run.error("更新彩种期号状态发生异常,期号ID：" + issueId + ",状态：" + state, e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 查询开奖结果
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public LotteryDrawResult findLotteryDrawResult(String lotteryId, String issueNo) {
		LotteryDrawResult drawResult = null;
		String memDrawResultKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + "drawResult" + lotteryId + issueNo;
		try {
			DbGenerator.setDynamicSlaveSource();
			drawResult = memcachedClient.get(memDrawResultKey);
			if (null == drawResult || "".equals(drawResult)) {
				drawResult = lotteryIssueMapper.findLotteryDrawResult(lotteryId, issueNo);
				if (null != drawResult) {
					memcachedClient.set(memDrawResultKey, 0, drawResult);
				}
			}
		} catch (Exception e) {
			Log.run.error("查询开奖结果发生异常,彩种ID：" + lotteryId + ",期号：" + issueNo, e);
			return null;
		}
		return drawResult;
	}

	/**
	 * 新增开奖结果
	 * 
	 * @param lotteryDrawResult
	 * @return
	 */
	public int addLotteryDrawResult(LotteryDrawResult lotteryDrawResult) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.addLotteryDrawResult(lotteryDrawResult);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			String lotteryId = lotteryDrawResult.getLotteryId();
			String issueNo = lotteryDrawResult.getIssueNo();
			Log.run.error("新增开奖结果发生唯一键冲突,彩种ID：" + lotteryId + ",期号：" + issueNo, e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("新增开奖结果发生异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 分页查询开奖结果
	 * 
	 * @param lotteryDrawResult
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public DrawResultData getLotteryDrawResultList(LotteryDrawResult lotteryDrawResult, int currentPage, int pageSize) {
		DbGenerator.setDynamicSlaveSource();
		DrawResultData drawResultData = new DrawResultData();
		StringBuffer conditions = new StringBuffer();
		conditions.append(" 1=1");

		if (null != lotteryDrawResult) {
			if (null != lotteryDrawResult.getLotteryId() && !"".equals(lotteryDrawResult.getLotteryId())) {
				conditions.append(" and lotteryId='" + lotteryDrawResult.getLotteryId() + "'");
			}
		}

		int totalSize = countResultTotalSize(conditions.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
		}
		List<LotteryDrawResult> list = lotteryIssueMapper.getLotteryDrawResultList(conditions.toString());

		drawResultData.setCurrentPage(currentPage);
		drawResultData.setPageSize(pageSize);
		drawResultData.setTotalSize(totalSize);
		drawResultData.setResultList(list);
		return drawResultData;
	}

	/**
	 * 计算开奖结果总记录数
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	public int countResultTotalSize(String conditions) {
		DbGenerator.setDynamicSlaveSource();
		return lotteryIssueMapper.countResultTotalSize(conditions);
	}

	/**
	 * 创建彩种期号
	 * 
	 * @param lotteryIssue
	 * @return
	 */
	public int addLotteryIssue(LotteryIssue lotteryIssue) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.addLotteryIssue(lotteryIssue);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			String lotteryId = lotteryIssue.getLotteryId();
			String issueNo = lotteryIssue.getIssueNo();
			Log.run.error("创建彩种期号发生唯一键冲突,彩种ID：" + lotteryId + ",期号：" + issueNo, e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("创建彩种期号发生异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 查询开奖结果奖级列表
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public List<LotteryDrawLevel> findLotteryDrawLevelList(String lotteryId, String issueNo) {
		String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + "drawLevel" + lotteryId + issueNo;
		List<LotteryDrawLevel> drawLevelList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			drawLevelList = memcachedClient.get(memcachedKey);
			if (null == drawLevelList || drawLevelList.size() == 0) {
				drawLevelList = lotteryIssueMapper.findLotteryDrawLevelList(lotteryId, issueNo);
				if (null != drawLevelList && drawLevelList.size() > 0) {
					memcachedClient.set(memcachedKey, 0, drawLevelList);
				}
			}
		} catch (Exception e) {
			Log.run.error("查询开奖结果奖级列表发生异常", e);
			return null;
		}
		return drawLevelList;
	}

	/**
	 * 查询所有彩种信息
	 * 
	 * @return
	 */
	public List<LotteryItem> getLotteryItemList() {
		DbGenerator.setDynamicSlaveSource();
		return lotteryIssueMapper.getLotteryItemList();
	}

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
	public List<LotteryIssue> getLotteryIssueListByState(int state, int currentPage, int pageSize) {
		List<LotteryIssue> lotteryIssueList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer strBuff = new StringBuffer();
			// if (state == IssueConstant.ISSUE_STATUS_NOT_SELL) {
			// strBuff.append(" and drawTime >= now() ");
			// }
			strBuff.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			lotteryIssueList = lotteryIssueMapper.getLotteryIssueListByState(state, strBuff.toString());
		} catch (Exception e) {
			ScanLog.run.error("分页查询期号错误,期状态：" + state + ",页码：" + currentPage + ",页大小：" + pageSize, e);
		}
		return lotteryIssueList;
	}

	/**
	 * 根据状态查询期号列表(后台管理平台使用)state值对应：1：中奖审核未审核13 2：中奖审核通过15、16、17 3： 派奖审核未审核 15
	 * 4：派奖审核通过17
	 * 
	 * @param state
	 * @return
	 */
	public ReturnData getLotteryIssueByParam(LotteryIssue lotteryIssue, int currentPage, int pageSize) {
		ReturnData returnData = new ReturnData();

		DbGenerator.setDynamicSlaveSource();
		StringBuffer conditions = new StringBuffer();
		conditions.append(" 1=1");
		if (lotteryIssue.getState() > 0) {
			if (lotteryIssue.getState() == 1) {
				conditions.append(" and state=" + IssueConstant.ISSUE_STATUS_HASCAL_WAITAUDIT);
			}
			if (lotteryIssue.getState() == 2) {
				conditions.append(" and (state=" + IssueConstant.ISSUE_STATUS_CAN_SEND + " or state="
						+ IssueConstant.ISSUE_STATUS_SENDING + " or state="
						+ IssueConstant.ISSUE_STATUS_SENDPRIZE_ONLINE + ")");
			}
			if (lotteryIssue.getState() == 3) {
				conditions.append(" and state=" + IssueConstant.ISSUE_STATUS_CAN_SEND);
			}
			if (lotteryIssue.getState() == 4) {
				conditions.append(" and state=" + IssueConstant.ISSUE_STATUS_SENDPRIZE_ONLINE);
			}
		}
		if (null != lotteryIssue.getLotteryId() && !"".equals(lotteryIssue.getLotteryId())) {
			conditions.append(" and lotteryId='" + lotteryIssue.getLotteryId() + "'");
		}
		if (null != lotteryIssue.getIssueNo() && !"".equals(lotteryIssue.getIssueNo())) {
			conditions.append(" and issueNo='" + lotteryIssue.getIssueNo() + "'");
		}
		int totalSize = countTotalSize(conditions.toString());
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		conditions.append(" order by issueNo desc");
		if (totalPage >= currentPage) {
			conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
		}
		List<LotteryIssue> list = lotteryIssueMapper.getLotteryIssueByParam(conditions.toString());

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);

		return returnData;
	}

	/**
	 * 新增期状态扫描任务完成记录
	 * 
	 * @param lotteryTaskComplete
	 * @return
	 */
	public int createLotteryTaskComplete(LotteryTaskComplete lotteryTaskComplete) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			if (lotteryTaskComplete.getTaskType() == IssueConstant.TASK_COMPLETE_PARTNER_WINNINGCAL) {
				lotteryTaskComplete.setStatus(LotteryIssueConstant.TaskStatus.DEALING.getValue());
			} else {
				lotteryTaskComplete.setStatus(LotteryIssueConstant.TaskStatus.COMPLETE.getValue());
			}
			returnValue = lotteryIssueMapper.createLotteryTaskComplete(lotteryTaskComplete);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			String lotteryId = lotteryTaskComplete.getLotteryId();
			String issueNo = lotteryTaskComplete.getIssueNo();
			int taskType = lotteryTaskComplete.getTaskType();
			String setNo = lotteryTaskComplete.getSetNo();
			ScanLog.run.error("新增期状态扫描任务完成记录发生唯一 键冲突,lotteryId=%s,issueNo=%s,taskType=%d,setNo=%s", lotteryId, issueNo,
					taskType, setNo);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			ScanLog.run.error("新增期状态扫描任务完成记录发生异常");
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;

	}

	/**
	 * 获取期状态扫描任务完成记录
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @param type
	 * @return
	 */
	public List<LotteryTaskComplete> getLotteryTaskCompleteByType(String lotteryId, String issueNo, int type) {
		List<LotteryTaskComplete> taskList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			taskList = lotteryIssueMapper.getLotteryTaskCompleteByType(lotteryId, issueNo, type);
		} catch (Exception e) {
			Log.run.error("获取期状态扫描任务完成记录发生异常", e);
		}
		return taskList;
	}

	/**
	 * 更新期号状态
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @param state
	 * @return
	 */
	public int updateLotteryIssueStateByParam(String lotteryId, String issueNo, int state) {
		int isSuccess = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			isSuccess = lotteryIssueMapper.updateLotteryIssueStateByParam(lotteryId, issueNo, state);
			if (isSuccess > 0) {
				String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId + issueNo;
				String issueId = (String) memcachedClient.get(memcachedKey);

				// 更新期号状态之后,删除memcached
				if (null != issueId && !"".equals(issueId)) {
					memcachedClient.delete(issueId);
				}
				memcachedClient.delete(memcachedKey);
			}
			Log.run.debug("更新期号状态,lotteryId=%s,issueNo=%s,state=%d,returnValue=%d", lotteryId, issueNo, state,
					isSuccess);
		} catch (Exception e) {
			Log.run.error("更新期号状态发生异常,彩种ID：" + lotteryId + ",期号：" + issueNo + ",更新状态：" + state, e);
			return 0;
		}
		return isSuccess;
	}

	/**
	 * 创建奖级信息
	 * 
	 * @param lotteryDrawLevel
	 * @return
	 */
	public int createLotteryDrawLevel(LotteryDrawLevel lotteryDrawLevel) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.createLotteryDrawLevel(lotteryDrawLevel);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (Exception e) {
			Log.run.error("创建奖级信息发生异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 更新期号开奖结果信息
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @param drawResult
	 * @param prizePool
	 * @param salesVolume
	 * @return
	 */
	public int updateIssueDrawResult(String lotteryId, String issueNo, String drawResult, long prizePool,
			long salesVolume) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateIssueDrawResult(lotteryId, issueNo, drawResult, prizePool,
					salesVolume);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId + issueNo;
			memcachedClient.delete(memcachedKey);
			Log.run.debug("更新期号开奖结果信息,memcachedKey=%s", memcachedKey);
		} catch (Exception e) {
			Log.run.error("更新期号开奖结果信息发生异常", e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 删除期号
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int deleteIssueNo(String lotteryId, String issueNo) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.deleteIssueNo(lotteryId, issueNo);
			if (returnValue != 1) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId + issueNo;
			String issueId = (String) memcachedClient.get(memcachedKey);
			if (null != issueId && !"".equals(issueId)) {
				memcachedClient.delete(issueId);
				memcachedClient.delete(memcachedKey);
			}
		} catch (Exception e) {
			Log.run.error("删除期号发生异常,彩种：" + lotteryId + ",期号：" + issueNo, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 修改期号
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int updateIssueNo(String lotteryId, String oldIssueNo, String newIssueNo) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateIssueNo(lotteryId, oldIssueNo, newIssueNo);
			if (returnValue != 1) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId + oldIssueNo;
			String issueId = (String) memcachedClient.get(memcachedKey);
			if (null != issueId && !"".equals(issueId)) {
				memcachedClient.delete(issueId);
				memcachedClient.delete(memcachedKey);
			}
		} catch (Exception e) {
			Log.run.error("修改期号发生异常,彩种：" + lotteryId + ",期号：" + oldIssueNo, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 计算期号数
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int countIssue(String lotteryId, String beginTime, String endTime) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicSlaveSource();
			returnValue = lotteryIssueMapper.countIssue(lotteryId, beginTime, endTime);
		} catch (Exception e) {
			Log.run.error("计算期号数发生异常,彩种：" + lotteryId + ",开始时间：" + beginTime + ",结束时间：" + endTime, e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	/**
	 * 更新任务完成状态
	 * 
	 * @param lotteryTaskComplete
	 * @return
	 */
	public int updateTaskCompleteStatus(LotteryTaskComplete lotteryTaskComplete) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateTaskCompleteStatus(lotteryTaskComplete);
			Log.run.debug("更新任务完成状态,lotteryId=%s,issueNo=%s,taskType=%d,setNo=%s,returnValue=%d",
					lotteryTaskComplete.getLotteryId(), lotteryTaskComplete.getIssueNo(),
					lotteryTaskComplete.getTaskType(), lotteryTaskComplete.getSetNo(), returnValue);
		} catch (Exception e) {
			Log.run.error(
					"更新任务完成状态发生异常,彩种：" + lotteryTaskComplete.getLotteryId() + ",期号：" + lotteryTaskComplete.getIssueNo()
							+ ",类型：" + lotteryTaskComplete.getTaskType() + ",机器编码：" + lotteryTaskComplete.getSetNo(), e);
		}
		return returnValue;
	}

	/**
	 * 根据任务状态查询任务记录
	 * 
	 * @param taskType
	 * @param status
	 * @return
	 */
	public List<LotteryTaskComplete> getLotteryTaskCompleteByStatus(int taskType, int status, int currentPage,
			int pageSize) {
		List<LotteryTaskComplete> taskList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer strBuff = new StringBuffer();
			strBuff.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			taskList = lotteryIssueMapper.getLotteryTaskCompleteByStatus(taskType, status, strBuff.toString());
		} catch (Exception e) {
			Log.run.error("根据任务状态查询任务记录发生异常", e);
		}
		return taskList;
	}

	/**
	 * 获取能取开奖结果的期号列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LotteryIssue> getDrawIssueList(int currentPage, int pageSize) {
		List<LotteryIssue> issueList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer strBuff = new StringBuffer();
			strBuff.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			issueList = lotteryIssueMapper.getDrawIssueList(strBuff.toString());
		} catch (Exception e) {
			Log.run.error("获取能取开奖结果的期号列表发生异常", e);
		}
		return issueList;
	}

	/**
	 * 获取能出票的期号列表
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LotteryIssue> getCanPrintIssueList(int currentPage, int pageSize) {
		List<LotteryIssue> issueList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer strBuff = new StringBuffer();
			strBuff.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			issueList = lotteryIssueMapper.getCanPrintIssueList(strBuff.toString());
		} catch (Exception e) {
			Log.run.error("获取能出票的期号列表发生异常", e);
		}
		return issueList;
	}

	public int updateLotteryIssue(LotteryIssue lotteryIssue) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		String lotteryId = "";
		String issueNo = "";
		try {
			lotteryId = lotteryIssue.getLotteryId();
			issueNo = lotteryIssue.getIssueNo();
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateLotteryIssue(lotteryIssue, lotteryId, issueNo);
			if (returnValue != 1) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
			String memcachedKey = ConstantsUtil.MODULENAME_LOTTERY_ISSUE + lotteryId + issueNo;
			String issueId = (String) memcachedClient.get(memcachedKey);
			if (null != issueId && !"".equals(issueId)) {
				memcachedClient.delete(issueId);
				memcachedClient.delete(memcachedKey);
			}
		} catch (Exception e) {
			Log.run.error("修改期号时间发生异常,lotteryId=" + lotteryId + ",issueNo" + issueNo, e);
			throw new DaoLevelException("0");
		}
		return returnValue;
	}

	/**
	 * 创建竞技彩期号
	 * 
	 * @param issueSport
	 * @return
	 */
	public int createIssueSport(IssueSport issueSport) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.createIssueSport(issueSport);
		} catch (Exception e) {
			Log.run.error("创建竞技彩期号发生异常,lotteryId=" + issueSport.getLotteryId(), e);
		}
		return returnValue;
	}

	/**
	 * 更新竞技彩期号状态
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param wareState
	 * @return
	 */
	public int updateIssueSport(String lotteryId, String wareIssue, int wareState) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateIssueSport(lotteryId, wareIssue, wareState);
			if (returnValue == 1) {
				String issueSportMemKey = getIssueSportMemKey(lotteryId, wareIssue);
				memcachedClient.delete(issueSportMemKey);
			}
			Log.run.debug("更新竞技彩期号状态,lotteryId=%s,wareIssue=%s,wareState=%d,returnValue=%d", lotteryId, wareIssue,
					wareState, returnValue);
		} catch (Exception e) {
			Log.run.error("更新竞技彩期号状态发生异常,lotteryId=" + lotteryId + ",wareIssue=" + wareIssue + ",wareState="
					+ wareState, e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 查询竞技彩期号
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	public IssueSport findIssueSport(String lotteryId, String wareIssue) {
		IssueSport issue = null;
		try {
			String memcachedKey = getIssueSportMemKey(lotteryId, wareIssue);
			issue = memcachedClient.get(memcachedKey);
			if (null == issue || "".equals(issue)) {
				StringBuffer str = new StringBuffer();
				str.append(" lotteryId='" + lotteryId + "'");
				if (null == wareIssue || "".equals(wareIssue)) {
					String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
					str.append(" and beginSellTime<='" + currentTime + "' and endSellTime>'" + currentTime + "'");
				} else {
					str.append(" and wareIssue='" + wareIssue + "'");
				}
				DbGenerator.setDynamicSlaveSource();
				List<IssueSport> issueSportList = lotteryIssueMapper.findIssueSport(str.toString());
				if (null != issueSportList && issueSportList.size() > 0) {
					issue = issueSportList.get(0);
					memcachedClient.set(memcachedKey, 0, issue);
				}
			}
		} catch (Exception e) {
			Log.run.error("查询竞技彩期号发生异常,lotteryId=" + lotteryId + ",wareIssue=" + wareIssue, e);
		}
		return issue;
	}

	/**
	 * 竞技彩期号memcacheKey
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	private String getIssueSportMemKey(String lotteryId, String wareIssue) {
		return ConstantsUtil.MODULENAME_LOTTERY_ISSUE + "issueSport" + lotteryId + wareIssue;
	}

	/**
	 * 创建竞足竞篮赛事
	 * 
	 * @param matchCompetive
	 * @return
	 */
	public int createMatchCompetive(MatchCompetive matchCompetive) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = lotteryIssueMapper.createMatchCompetive(matchCompetive);
		} catch (Exception e) {
			Log.run.error("创建竞足竞篮赛事发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 创建竞足竞篮赛事玩法
	 * 
	 * @param matchCompetivePlay
	 * @return
	 */
	public int createMatchCompetivePlay(MatchCompetivePlay matchCompetivePlay) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = lotteryIssueMapper.createMatchCompetivePlay(matchCompetivePlay);
		} catch (Exception e) {
			Log.run.error("创建竞足竞篮赛事玩法发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 删除赛事玩法
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	public int deleteMatchCompetivePlay(String wareIssue, String transferId) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = lotteryIssueMapper.deleteMatchCompetivePlay(wareIssue, transferId);
		} catch (Exception e) {
			Log.run.error("删除赛事玩法发生异常,wareIssue=" + wareIssue + ",transferId=" + transferId, e);
		}
		return returnValue;
	}

	/**
	 * 获取赛事赛果
	 * 
	 * @param lotteryId
	 * @param transferId
	 * @return
	 */
	public List<MatchCompetiveResult> getMatchCompetiveResultList(String wareIssue, String lotteryId, String transferId) {
		List<MatchCompetiveResult> resultList = null;
		String sql = "1=1";
		try {
			DbGenerator.setDynamicSlaveSource();
			if (null != lotteryId && !"".equals(lotteryId)) {
				sql += " and wareIssue='" + wareIssue + "' and transferId='" + transferId + "' and lotteryId='"
						+ lotteryId + "'";
			} else {
				sql += " and wareIssue='" + wareIssue + "' and transferId='" + transferId + "'";
			}
			resultList = lotteryIssueMapper.getMatchCompetiveResultList(sql);
		} catch (Exception e) {
			Log.run.error("获取赛事赛果发生异常,lotteryId=" + lotteryId + ",transferId=" + transferId, e);
		}
		return resultList;
	}

	/**
	 * 获取竞足竞篮北单赛事信息
	 * 
	 * @param transferId
	 * @return
	 */
	public MatchCompetive getMatchCompetive(String wareIssue, String transferId) {
		MatchCompetive matchCompetive = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			matchCompetive = lotteryIssueMapper.getMatchCompetive(wareIssue, transferId);
			if (null != matchCompetive && !"".equals(matchCompetive)) {
				matchCompetive.setMatchCompetivePlayList(lotteryIssueMapper.getMatchCompetivePlayList(wareIssue,
						transferId));
			}
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮赛事信息法发生异常,transferId=" + transferId, e);
		}
		return matchCompetive;
	}

	/**
	 * 获取竞足竞篮赛事玩法信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @param lotteryId
	 * @return
	 */
	public List<MatchCompetivePlay> getMatchCompetivePlayList(String wareIssue, String transferId, String lotteryId) {
		List<MatchCompetivePlay> playList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			if (null != lotteryId && !"".equals(lotteryId)) {
				playList = lotteryIssueMapper.getMatchCompetivePlay(wareIssue, transferId, lotteryId);
			} else {
				playList = lotteryIssueMapper.getMatchCompetivePlayList(wareIssue, transferId);
			}
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮赛事玩法信息发生异常,transferId=" + transferId, e);
		}
		return playList;
	}

	/**
	 * 根据赛事类型获取竞足竞篮所有在售赛事
	 * 
	 * @param matchType
	 * @return
	 */
	public List<MatchCompetive> getMatchCompetiveListByMatchType(int matchType) {
		List<MatchCompetive> matchCompetiveList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			matchCompetiveList = lotteryIssueMapper.getMatchCompetiveListByMatchType(matchType);
		} catch (Exception e) {
			Log.run.error("根据赛事类型获取竞足竞篮所有在售赛事发生异常,matchType=" + matchType, e);
		}
		return matchCompetiveList;
	}

	/**
	 * 更新竞足竞篮赛事开奖结果
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @param drawResult
	 * @param matchStatus
	 * @return
	 */
	public int updateMatchCompetiveDrawResult(String wareIssue, String transferId, String drawResult, int matchStatus) {
		int returnValue = 0;
		try {
			returnValue = lotteryIssueMapper.updateMatchCompetiveDrawResult(wareIssue, transferId, drawResult,
					matchStatus);
		} catch (Exception e) {
			Log.run.error("更新竞足竞篮赛事开奖结果发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 创建竞足竞篮赛事入库
	 * 
	 * @param matchCompetiveResult
	 * @return
	 */
	public int createMatchCompetiveResult(MatchCompetiveResult matchCompetiveResult) {
		int returnValue = 0;
		try {
			returnValue = lotteryIssueMapper.createMatchCompetiveResult(matchCompetiveResult);
		} catch (Exception e) {
			Log.run.error("创建竞足竞篮赛事入库发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 根据条件搜索竞足竞篮赛事列表
	 * 
	 * @param matchCompetive
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public MatchCompetiveData getMatchCompetiveList(MatchCompetive matchCompetive, int currentPage, int pageSize) {
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer conditions = new StringBuffer();
			MatchCompetiveData returnData = new MatchCompetiveData();
			conditions.append(" 1=1");
			if (null != matchCompetive.getBeginMatchDate() && !"".equals(matchCompetive.getBeginMatchDate())) {
				conditions.append(" and matchDate>='" + matchCompetive.getBeginMatchDate() + "'");
			}
			if (null != matchCompetive.getEndMatchDate() && !"".equals(matchCompetive.getEndMatchDate())) {
				conditions.append(" and matchDate<='" + matchCompetive.getEndMatchDate() + "'");
			}
			
			if(StringUtils.isNotEmpty(matchCompetive.getMatchDate())){
				conditions.append(" and matchDate='" + matchCompetive.getMatchDate()+"'");
			}
			
			if(StringUtils.isNotEmpty(matchCompetive.getWareIssue())){
				conditions.append(" and wareIssue='" + matchCompetive.getWareIssue()+"'");
			}
			
			if(StringUtils.isNotEmpty(matchCompetive.getMatchNo())){
				conditions.append(" and matchNo='" + matchCompetive.getMatchNo()+"'");
			}
			if(StringUtils.isNotEmpty(matchCompetive.getTransferId())){
				conditions.append(" and transferId='" + matchCompetive.getTransferId()+"'");
			}
//			if(matchCompetive.getMatchStatus()!=100){
//			conditions.append(" and matchStatus =" + matchCompetive.getMatchStatus());
//			}
			if (matchCompetive.getMatchType() > 0) {
				conditions.append(" and matchType=" + matchCompetive.getMatchType());
			}
			int totalSize = countMatchTotalSize(conditions.toString());
			int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
			conditions.append(" order by transferId desc, matchNo desc");
			if (totalPage >= currentPage) {
				conditions.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			}
			List<MatchCompetive> list = lotteryIssueMapper.getMatchCompetiveList(conditions.toString());
			if (null != list && list.size() > 0) {
				for (MatchCompetive match : list) {
					String wareIssue = match.getWareIssue();
					String transferId = match.getTransferId();
					List<MatchCompetivePlay> playList = lotteryIssueMapper.getMatchCompetivePlayList(wareIssue,
							transferId);
					match.setMatchCompetivePlayList(playList);
					List<MatchCompetiveResult> competiveResults = new ArrayList<MatchCompetiveResult>();
					match.setMatchCompetiveResultList(competiveResults);
					if(playList!=null && playList.size()>0){
						List<MatchCompetiveResult> competiveResultsTemp = null;
						for (MatchCompetivePlay competivePlay:playList) {
							
							competiveResultsTemp = getMatchCompetiveResultList(competivePlay.getWareIssue(), competivePlay.getLotteryId(), competivePlay.getTransferId());
								if(competiveResultsTemp!=null && competiveResultsTemp.size()>0){
									competiveResults.add(competiveResultsTemp.get(0));
								}
						}
						
					}
					
				}
			}
			returnData.setCurrentPage(currentPage);
			returnData.setPageSize(pageSize);
			returnData.setTotalSize(totalSize);
			returnData.setMatchCompetiveList(list);
			return returnData;
		} catch (Exception e) {
			Log.run.error("根据条件搜索竞足竞篮赛事列表发生异常", e);
			return null;
		}
	}

	/**
	 * 计算竞足竞篮北单赛事总记录数
	 * 
	 * @param conditions
	 *            搜索条件字符串
	 * @return
	 */
	public int countMatchTotalSize(String conditions) {
		DbGenerator.setDynamicSlaveSource();
		return lotteryIssueMapper.countMatchTotalSize(conditions);
	}

	/**
	 * 修改竞足竞篮赛事信息
	 * 
	 * @param matchCompetive
	 * @return
	 */
	public int updateMatchCompetive(MatchCompetive matchCompetive) {
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateMatchCompetive(matchCompetive);
		} catch (Exception e) {
			Log.run.error("修改竞足竞篮赛事信息发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 删除竞足竞篮赛事信息
	 * 
	 * @param wareIssue
	 * @param transferId
	 * @return
	 */
	public int deleteMatchCompetive(String wareIssue, String transferId) {
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.deleteMatchCompetive(wareIssue, transferId);
		} catch (Exception e) {
			Log.run.error("删除竞足竞篮赛事信息发生异常,transferId=" + transferId, e);
		}
		return returnValue;
	}

	/**
	 * 老足彩获取彩种销售已截止的最大期号
	 * 
	 * @param lotteryId
	 * @return
	 */
	public LotteryIssue getMaxLotteryIssueByLotteryId(String lotteryId) {
		LotteryIssue lotteryIssue = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			lotteryIssue = lotteryIssueMapper.getMaxLotteryIssueByLotteryId(lotteryId);
		} catch (Exception e) {
			Log.run.error("老足彩获取彩种最大期号发生异常,lotteryId=" + lotteryId, e);
		}
		return lotteryIssue;
	}

	/**
	 * 创建老足彩赛事信息
	 * 
	 * @param matchFootball
	 * @return
	 */
	public int createMatchFootball(MatchFootball matchFootball) {
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.createMatchFootball(matchFootball);
		} catch (Exception e) {
			Log.run.error("创建老足彩赛事信息发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 查询老足彩赛事信息
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param matchNo
	 * @return
	 */
	public MatchFootball getMatchFootballByParams(String lotteryId, String wareIssue, String matchNo) {
		MatchFootball matchFootball = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			matchFootball = lotteryIssueMapper.getMatchFootballByParams(lotteryId, wareIssue, matchNo);
		} catch (Exception e) {
			Log.run.error("查询老足彩赛事信息发生异常", e);
		}
		return matchFootball;
	}

	/**
	 * 计算老足彩赛事数据
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	public int countMatchFootball(String lotteryId, String wareIssue) {
		int returnValue = 0;
		try {
			DbGenerator.setDynamicSlaveSource();
			returnValue = lotteryIssueMapper.countMatchFootball(lotteryId, wareIssue);
		} catch (Exception e) {
			Log.run.error("计算老足彩赛事数据发生异常", e);
		}
		return returnValue;
	}

	/**
	 * 获取老足彩赛事信息
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @return
	 */
	public List<MatchFootball> getMatchFootballList(String lotteryId, String wareIssue) {
		List<MatchFootball> matchFootballList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			matchFootballList = lotteryIssueMapper.getMatchFootballList(lotteryId, wareIssue);
		} catch (Exception e) {
			Log.run.error("获取老足彩赛事信息发生异常,wareIssue=" + wareIssue + ",lotteryId=" + lotteryId, e);
		}
		return matchFootballList;
	}

	/**
	 * 查询老足彩期号信息
	 * 
	 * @param lotteryId
	 * @return
	 */
	public IssueSport findLZCIssueSport(String lotteryId) {
		IssueSport issueSport = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			issueSport = lotteryIssueMapper.findLZCIssueSport(lotteryId);
		} catch (Exception e) {
			Log.run.error("查询老足彩期号信息发生异常,lotteryId=" + lotteryId, e);
		}
		return issueSport;
	}

	/**
	 * 查询老足彩上一期的期号
	 * 
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public LotteryIssue getMaxLotteryIssueByIssue(String lotteryId, String issueNo) {
		LotteryIssue lotteryIssue = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			lotteryIssue = lotteryIssueMapper.getMaxLotteryIssueByIssue(lotteryId, issueNo);
		} catch (Exception e) {
			Log.run.error("老足彩获取彩种最大期号发生异常,lotteryId=" + lotteryId, e);
		}
		return lotteryIssue;
	}

	/**
	 * 分页获取竞足竞篮北单算奖赛事数据
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<MatchCompetive> getMatchCompetiveListForCalPrize(int currentPage, int pageSize) {
		List<MatchCompetive> matchCompetiveList = null;
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer strBuff = new StringBuffer();
			strBuff.append("where matchStatus=" + SportIssueConstant.CompetiveMatchStatus.MATCH_HAS_DRAW.getValue()
					+ " or matchStatus=" + SportIssueConstant.CompetiveMatchStatus.MATCH_IN_CALING.getValue());
			strBuff.append(" limit " + (currentPage - 1) * pageSize + "," + pageSize);
			matchCompetiveList = lotteryIssueMapper.getMatchCompetiveListForCalPrize(strBuff.toString());
		} catch (Exception e) {
			ScanLog.run.error("分页获取竞足竞篮北单算奖赛事数据发生异常,currentPage=" + currentPage + ",pageSize=" + pageSize, e);
		}
		return matchCompetiveList;
	}

	/**
	 * 更新竞足竞篮北单赛事状态
	 * 
	 * @param transferId
	 * @param matchStatus
	 * @return
	 */
	public int updateMatchCompetiveStatus(String transferId, int matchStatus) {
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateMatchCompetiveStatus(transferId, matchStatus);
		} catch (Exception e) {
			Log.run.error("更新竞足竞篮北单赛事状态发生异常,transferId=" + transferId, e);
		}
		return returnValue;
	}


	
	/**
	 * 分页获取老足彩赛事信息
	 * @param matchFootball
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public MatchFootballDate getMatchFootballDate(MatchFootball matchFootball,int currentPage,int pageSize){
		MatchFootballDate matchFootballDate = new MatchFootballDate();
		try {
			DbGenerator.setDynamicSlaveSource();
			StringBuffer sbf = new StringBuffer();
			sbf.append(" where 1=1 ");
			if(StringUtils.isNotBlank(matchFootball.getLotteryId())){
				sbf.append(" and lotteryId= '"+matchFootball.getLotteryId()+"' ");
			}
			if(StringUtils.isNotBlank(matchFootball.getWareIssue())){
				sbf.append(" and wareIssue= '"+matchFootball.getWareIssue()+"' ");
			}
			
			int totalSize = getMatchFootballTotalSize(sbf.toString());
			
			sbf.append(" order by cast(matchNo as SIGNED) ");
			if(currentPage !=0 && pageSize != 0){
				
				sbf.append(" limit " + pageSize*(currentPage -1)+"," + pageSize);
			}
			List<MatchFootball> matchFootballs = lotteryIssueMapper.getMatchFootballs(sbf.toString());
			matchFootballDate.setCurrentPage(currentPage);
			matchFootballDate.setTotalSize(totalSize);
			matchFootballDate.setMatchFootballList(matchFootballs);
		} catch (Exception e) {
			Log.run.error("分页获取老足彩信息列表异常:" + matchFootball, e);
		}
		
		return matchFootballDate;
	}
	
	/**
	 * 获取老足彩记录数带条件
	 * @param sql
	 * @return
	 */
	public int getMatchFootballTotalSize(String sql){
		int total = 0;
		try {
			total = lotteryIssueMapper.getMatchFootballTotalSize(sql);
		} catch (Exception e) {
			Log.run.error("根据条件获取老足彩记录数异常:" + sql, e);
		}
		return total;
	}
	
	/**
	 * 修改老足彩信息
	 * @param matchFootball
	 * @return
	 */
	public int updateMatchFootBall(MatchFootball matchFootball){
		
		int flag = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			flag =lotteryIssueMapper.updateMatchFootball(matchFootball);
		} catch (Exception e) {
			Log.run.error("修改老足彩信息异常:" + matchFootball, e);
		}
		return flag;
	}
	
	/**
	 * 修改赛事结果
	 * @param competiveResult
	 * @return
	 */
	public int updateMatchCompetiveResult(MatchCompetiveResult competiveResult){

		int flag = 0;
		try {
			flag = lotteryIssueMapper.updateMatchCompetiveResult(competiveResult);
		} catch (Exception e) {
			Log.run.error("修改竞彩赛事结果异常:" + competiveResult, e);
		}
		return flag;
	}
	
	/**
	 * 更新竞技彩期号结束时间
	 * 
	 * @param lotteryId
	 * @param wareIssue
	 * @param endSellTime
	 * @return
	 */
	public int updateIssueSportEndTime(String lotteryId, String wareIssue, String endSellTime) {
		int returnValue = 0;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = lotteryIssueMapper.updateIssueSportEndTime(lotteryId, wareIssue, endSellTime);
		} catch (Exception e) {
			Log.run.error("更新竞技彩期号结束时间发生异常,lotteryId=" + lotteryId + ",wareIssue=" + wareIssue, e);
		}
		return returnValue;
	}

}
