package com.cqfc.lottery.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.lottery.dao.LotteryIssueDao;
import com.cqfc.lottery.service.ILotteryIssueService;
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
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IssueConstant;
import com.cqfc.util.IssueUtil;
import com.cqfc.util.SportIssueConstant;
import com.jami.util.DbGenerator;
import com.jami.util.Log;

/**
 * @author liwh
 */
@Service
public class LotteryIssueServiceImpl implements ILotteryIssueService {

	@Resource
	private LotteryIssueDao lotteryIssueDao;

	@Override
	public int addOrUpdateLotteryItem(LotteryItem lotteryItem) {
		int isSuccess = 0;
		try {
			if (lotteryItem.getId() > 0) {
				isSuccess = lotteryIssueDao.updateLotteryItem(lotteryItem);
			} else {
				if (!isExistLotteryId(lotteryItem.getLotteryId())) {
					isSuccess = lotteryIssueDao.addLotteryItem(lotteryItem);
				}
			}
		} catch (Exception e) {
			Log.run.error("操作彩种信息发生异常", e);
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public List<LotteryItem> getLotteryItemList() {
		List<LotteryItem> itemList = null;
		try {
			itemList = lotteryIssueDao.getLotteryItemList();
		} catch (Exception e) {
			Log.run.error("获取所有彩种信息发生异常", e);
			return null;
		}
		return itemList;
	}

	@Override
	public LotteryItem findLotteryItemByLotteryId(String lotteryId) {
		LotteryItem lotteryItem = null;
		try {
			lotteryItem = lotteryIssueDao.findLotteryItemByLotteryId(lotteryId);
		} catch (Exception e) {
			Log.run.error("查询彩种信息发生异常", e);
			return null;
		}
		return lotteryItem;
	}

	@Override
	public LotteryIssue findLotteryIssue(String lotteryId, String issueNo) {
		LotteryIssue issue = null;
		try {
			issue = lotteryIssueDao.findLotteryIssue(lotteryId, issueNo);
		} catch (Exception e) {
			Log.run.error("查询彩种期号信息发生异常", e);
			return null;
		}
		return issue;
	}

	@Override
	public int updateLotteryIssueState(int issueId, int state) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueDao.updateLotteryIssueState(issueId, state);
		} catch (Exception e) {
			Log.run.error("更新彩种期号状态发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public LotteryDrawResult findLotteryDrawResult(String lotteryId, String issueNo) {
		LotteryDrawResult drawResult = null;
		try {
			drawResult = lotteryIssueDao.findLotteryDrawResult(lotteryId, issueNo);
			if (null != drawResult && !"".equals(drawResult)) {
				List<LotteryDrawLevel> drawLevelList = lotteryIssueDao.findLotteryDrawLevelList(lotteryId, issueNo);
				drawResult.setLotteryDrawLevelList(drawLevelList);
				LotteryIssue issue = lotteryIssueDao.findLotteryIssue(lotteryId, issueNo);
				drawResult.setLotteryIssue(issue);
			}
		} catch (Exception e) {
			Log.run.error("查询开奖结果发生异常", e);
			return null;
		}
		return drawResult;
	}

	@Override
	@Transactional
	public int addLotteryDrawResult(LotteryDrawResult lotteryDrawResult) {
		int isSuccess = 0;
		try {
			String lotteryId = lotteryDrawResult.getLotteryId();
			String issueNo = lotteryDrawResult.getIssueNo();
			// 创建开奖结果记录
			isSuccess = lotteryIssueDao.addLotteryDrawResult(lotteryDrawResult);
			// 创建奖级信息
			List<LotteryDrawLevel> drawLevelList = lotteryDrawResult.getLotteryDrawLevelList();
			if (null == drawLevelList || drawLevelList.equals("") || drawLevelList.size() < 1) {
				Log.run.debug("奖级信息不存在,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
				throw new Exception("奖级信息不存在！");
			}
			for (LotteryDrawLevel lotteryDrawLevel : drawLevelList) {
				lotteryIssueDao.createLotteryDrawLevel(lotteryDrawLevel);
			}
			// 更新期信息--奖池、开奖结果、期销售量
			long prizePool = lotteryDrawResult.getPrizePool();
			String drawResult = lotteryDrawResult.getDrawResult();
			long salesVolume = lotteryDrawResult.getSalesVolume();
			isSuccess = lotteryIssueDao.updateIssueDrawResult(lotteryId, issueNo, drawResult, prizePool, salesVolume);
			Log.run.debug("开奖结果写入成功,lotteryId=%s,issueNo=%s,drawResult=%s,returnValue=%d", lotteryId, issueNo,
					drawResult, isSuccess);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			String lotteryId = lotteryDrawResult.getLotteryId();
			String issueNo = lotteryDrawResult.getIssueNo();
			Log.run.error("新增开奖结果发生异常,彩种：" + lotteryId + ",期号：" + issueNo, e);
			return -1;
		}
		return isSuccess;
	}

	@Override
	public DrawResultData getLotteryDrawResultList(LotteryDrawResult lotteryDrawResult, int currentPage, int pageSize) {
		DrawResultData drawResultData = null;
		try {
			drawResultData = lotteryIssueDao.getLotteryDrawResultList(lotteryDrawResult, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("分页查询开奖结果发生异常", e);
			return null;
		}
		return drawResultData;
	}

	@Override
	public ReturnData getLotteryIssueList(LotteryIssue lotteryIssue, int currentPage, int pageSize) {
		ReturnData returnData = null;
		try {
			returnData = lotteryIssueDao.getLotteryIssueList(lotteryIssue, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("分页查询期号信息发生异常", e);
			return null;
		}
		return returnData;
	}

	@Override
	@Transactional
	public int createLotteryIssue(String beginTime, String endTime, String lotteryId) {
		int createFlag = 0;
		try {
			if (IssueConstant.LOTTERYID_SSQ.equals(lotteryId)) {
				createFlag = createLotteryIssueUnionLotto(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_SSC.equals(lotteryId)) {
				createFlag = createLotteryIssueSsc(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_XYNC.equals(lotteryId)) {
				createFlag = createLotteryIssueLuckyFarm(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_QLC.equals(lotteryId)) {
				createFlag = createLotteryIssueQlc(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_FC3D.equals(lotteryId)) {
				createFlag = createLotteryIssueFc3d(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_DLT.equals(lotteryId)) {
				createFlag = createLotteryIssueDlt(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_QXC.equals(lotteryId)) {
				createFlag = createLotteryIssueQxc(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_PLS.equals(lotteryId)) {
				createFlag = createLotteryIssuePls(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_PLW.equals(lotteryId)) {
				createFlag = createLotteryIssuePlw(beginTime, endTime, lotteryId);
			} else if (IssueConstant.LOTTERYID_ZJSYXW.equals(lotteryId)) {
				createFlag = createLotteryIssueZJSYXW(beginTime, endTime, lotteryId);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("创建彩种期号发生异常", e);
			return Integer.valueOf(e.getMessage());
		}
		return createFlag;
	}

	/**
	 * 创建双色球期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	private int createLotteryIssueUnionLotto(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
		try {
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				String week = DateUtil.getWeekOfDate(currentDate);

				if ("Tuesday".equals(week) || "Thursday".equals(week) || "Sunday".equals(week)) { // 生成期号
					// 获取当前最大期号
					LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
					// 上一期开奖时间
					Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
					// 投注开始时间、官方销售开始时间
					String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);
					// 出票开始时间
					String printBeginTime = DateUtil.dateToString(DateUtil.getOffsetDate(previousDrawTime, 1),
							"yyyy-MM-dd");

					int issueNoInt = 0;
					// 当前期数年份
					String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
					// 数据库中最大期数日期年份
					String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

					if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
						issueNoInt = Integer.parseInt(currentDateYear + "001");
					} else {
						String issueNo = lotteryIssue.getIssueNo();
						issueNoInt = Integer.parseInt(issueNo) + 1;
					}

					LotteryIssue lotteryIssueTemp = new LotteryIssue();
					lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
					lotteryIssueTemp.setLotteryId(lotteryId);
					// 开奖时间=开奖日的21:30-->从2015-03-02开始,修改为开奖日的21:15
					lotteryIssueTemp.setDrawTime(dateStr + " 21:15:00");
					// 投注开始时间=上一期开奖日的19:45
					lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
					// 单式自购截止时间=开奖日的19:45（改成19:30）
					lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
					// 复式自购截止时间=开奖日的19:45（改成19:30）
					lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
					// 单式合买截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
					// 复式合买截止时间=开奖日的19:45（改成19:30）
					lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
					// 单式上传截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
					// 出票开始时间=上一期开奖日的第二天凌晨3点（改成凌晨00:00:10点）
					lotteryIssueTemp.setPrintBeginTime(printBeginTime + " 00:00:10");
					// 出票截止时间=开奖日的19:55（改成19:40）
					lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
					// 官方销售开始时间=上一期开奖日的20:00（改成19:45）
					lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 19:45:00");
					// 官方销售截止时间=开奖日的20:00（改成19:30）
					lotteryIssueTemp.setOfficialEndTime(dateStr + " 19:30:00");

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);
				}
			}
		} catch (Exception e) {
			Log.run.error("创建双色球期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 创建七乐彩期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	private int createLotteryIssueQlc(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		try {
			int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				String week = DateUtil.getWeekOfDate(currentDate);

				if ("Monday".equals(week) || "Wednesday".equals(week) || "Friday".equals(week)) { // 生成期号
					// 获取当前最大期号
					LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
					// 上一期开奖时间
					Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
					// 投注开始时间、官方销售开始时间
					String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);
					// 出票开始时间
					String printBeginTime = DateUtil.dateToString(DateUtil.getOffsetDate(previousDrawTime, 1),
							"yyyy-MM-dd");

					int issueNoInt = 0;
					// 当前期数年份
					String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
					// 数据库中最大期数日期年份
					String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

					if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
						issueNoInt = Integer.parseInt(currentDateYear + "001");
					} else {
						String issueNo = lotteryIssue.getIssueNo();
						issueNoInt = Integer.parseInt(issueNo) + 1;
					}

					LotteryIssue lotteryIssueTemp = new LotteryIssue();
					lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
					lotteryIssueTemp.setLotteryId(lotteryId);
					// 开奖时间=开奖日的21:30-->从2015-03-02开始,修改为开奖日的21:15
					lotteryIssueTemp.setDrawTime(dateStr + " 21:15:00");
					// 投注开始时间=上一期开奖日的19:45
					lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
					// 单式自购截止时间=开奖日的19:45（改成19:30）
					lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
					// 复式自购截止时间=开奖日的19:45（改成19:30）
					lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
					// 单式合买截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
					// 复式合买截止时间=开奖日的19:45（改成19:30）
					lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
					// 单式上传截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
					// 出票开始时间=上一期开奖日的第二天0点（改成00:00:10）
					lotteryIssueTemp.setPrintBeginTime(printBeginTime + " 00:00:10");
					// 出票截止时间=开奖日的19:50（改成19:40）
					lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
					// 官方销售开始时间=上一期开奖日的20:00（改成19:45）
					lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 19:45:00");
					// 官方销售截止时间=开奖日的20:00（改成19:30）
					lotteryIssueTemp.setOfficialEndTime(dateStr + " 19:30:00");

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);

				}
			}
		} catch (Exception e) {
			Log.run.error("创建七乐彩期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 创建福彩3D期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	private int createLotteryIssueFc3d(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		try {
			int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				// 日期累加
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				// 获取当前最大期号
				LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
				// 上一期开奖时间
				Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
				// 投注开始时间、官方销售开始时间
				String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);

				int issueNoInt = 0;
				// 当前期数年份
				String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
				// 数据库中最大期数日期年份
				String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

				if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
					issueNoInt = Integer.parseInt(currentDateYear + "001");
				} else {
					String issueNo = lotteryIssue.getIssueNo();
					issueNoInt = Integer.parseInt(issueNo) + 1;
				}

				LotteryIssue lotteryIssueTemp = new LotteryIssue();
				lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
				lotteryIssueTemp.setLotteryId(lotteryId);
				// 开奖时间=开奖日的20:30
				lotteryIssueTemp.setDrawTime(dateStr + " 20:30:00");
				// 投注开始时间=上一期开奖日的19:45
				lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
				// 单式自购截止时间=开奖日的19:45（改成19:30）
				lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
				// 复式自购截止时间=开奖日的19:45（改成19:30）
				lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
				// 单式合买截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
				// 复式合买截止时间=开奖日的19:45（改成19:30）
				lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
				// 单式上传截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
				// 出票开始时间=开奖日的08:00:00（改成00:00:11）
				lotteryIssueTemp.setPrintBeginTime(dateStr + " 00:00:11");
				// 出票截止时间=开奖日的19:50（改成19:40）
				lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
				// 官方销售开始时间=上一期开奖日的20:00（改成19:45）
				lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 19:45:00");
				// 官方销售截止时间=开奖日的20:00（改成19:30）
				lotteryIssueTemp.setOfficialEndTime(dateStr + " 19:30:00");

				isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);
			}
		} catch (Exception e) {
			Log.run.error("创建福彩3D期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 创建时时彩期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	private int createLotteryIssueSsc(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		try {
			int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				// 初始期号
				String issueNoStr = DateUtil.dateToString(currentDate, "yyyyMMdd");
				int issueNoInt = 1;

				// 时时彩每天120期(白天72期10分钟开奖一次，夜间48期5分钟开奖一次)
				for (int z = 0; z < 120; z++) {
					// 初始时间
					String initTime = z < 23 ? dateStr + " 00:00:00" : (z > 95 ? dateStr + " 21:55:00" : dateStr
							+ " 09:50:00");
					// 开奖间隔时间
					int offset = z < 23 ? 5 : (z > 95 ? 5 : 10);
					// 当天开始销售时间
					String beginSaleTime = z < 23 ? DateUtil.addDateMinut(initTime, "MINUTE", z * offset)
							: (z > 95 ? DateUtil.addDateMinut(initTime, "MINUTE", (z - 95) * offset) : DateUtil
									.addDateMinut(initTime, "MINUTE", (z - 23) * offset));
					String printBeginTime = DateUtil.addDateMinut(beginSaleTime, "SECOND", 10);
					// 开奖时间=白天73期10分钟开奖一次，夜间48期5分钟开奖一次（白天销售时间是09:50-22:00,73期,024-096，夜间销售时间是00:00-01:55,23期,001-023;22:00-00:00,24期,097-120;）
					String drawTime = DateUtil.addDateMinut(beginSaleTime, "MINUTE", offset);
					// 投注开始时间=上期开奖时间-3分钟
					String saleBeginTime = DateUtil.addDateMinut(beginSaleTime, "MINUTE", -3);
					if (z == 23) {
						saleBeginTime = dateStr + " 01:52:00";
					}
					// 单式自购截止时间=开奖时间-3分钟（改成-90s）
					String singleEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 复式自购截止时间=开奖时间-3分钟（改成-90s）
					String compoundEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 单式合买截止时间=开奖时间-3分钟（改成-90s）
					String singleTogetherEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 复式合买截止时间=开奖时间-3分钟（改成-90s）
					String compoundTogetherEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 单式上传截止时间=开奖时间-3分钟（改成-90s）
					String singleUploadEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 出票开始时间=上一期官方销售截止时间（改成开始销售时间+3分钟）
					if (z == 23) {
						// 期号为024出票开始时间为06:00:00
						printBeginTime = dateStr + " 06:00:10";
					}
					// 出票截止时间=开奖时间-50秒(改成-1分钟)
					String printEndTime = DateUtil.addDateMinut(drawTime, "MINUTE", -1);
					// 官方销售开始时间=上一期开奖时间-3分钟
					String officialBeginTime = DateUtil.addDateMinut(drawTime, "MINUTE", -(offset + 3));
					if (z == 23) {
						officialBeginTime = dateStr + " 06:00:00";
					}
					// 官方销售截止时间=开奖时间-3分钟（改成-90s）
					String officialEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);

					String loop = transferIssueNoTail(String.valueOf(issueNoInt + z));

					LotteryIssue lotteryIssue = new LotteryIssue();
					lotteryIssue.setIssueNo(issueNoStr + loop);
					lotteryIssue.setLotteryId(lotteryId);
					lotteryIssue.setDrawTime(drawTime);
					lotteryIssue.setBeginTime(saleBeginTime);
					lotteryIssue.setSingleEndTime(singleEndTime);
					lotteryIssue.setCompoundEndTime(compoundEndTime);
					lotteryIssue.setSingleTogetherEndTime(singleTogetherEndTime);
					lotteryIssue.setCompoundTogetherEndTime(compoundTogetherEndTime);
					lotteryIssue.setSingleUploadEndTime(singleUploadEndTime);
					lotteryIssue.setPrintBeginTime(printBeginTime);
					lotteryIssue.setPrintEndTime(printEndTime);
					lotteryIssue.setOfficialBeginTime(officialBeginTime);
					lotteryIssue.setOfficialEndTime(officialEndTime);

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssue);
				}
			}
		} catch (Exception e) {
			Log.run.error("创建时时彩期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 创建幸运农场期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	private int createLotteryIssueLuckyFarm(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		try {
			int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				// 初始期号
				String issueNoStr = DateUtil.dateToString(currentDate, "yyyyMMdd");
				int issueNoInt = 1;

				for (int z = 0; z < 97; z++) { // 幸运农场每天97期
					String currentDateStr = DateUtil
							.dateToString(DateUtil.getOffsetDate(currentDate, -1), "yyyy-MM-dd");
					// 初始时间
					String initTime = z < 13 ? currentDateStr + " 23:53:00" : dateStr + " 09:53:00";
					// 开奖间隔时间
					int offset = 10;
					// 当天开始销售时间
					String beginSaleTime = z < 13 ? DateUtil.addDateMinut(initTime, "MINUTE", z * offset) : DateUtil
							.addDateMinut(initTime, "MINUTE", (z - 13) * offset);
					String printBeginTime = DateUtil.addDateMinut(beginSaleTime, "SECOND", 10);

					// 开奖时间=开奖时间00:03--02:03(13期)，10:03--23:53(84期).
					String drawTime = DateUtil.addDateMinut(beginSaleTime, "MINUTE", offset);
					// 投注开始时间=上期开奖时间-2分钟
					String saleBeginTime = DateUtil.addDateMinut(beginSaleTime, "MINUTE", -2);
					if (z == 13) {
						saleBeginTime = dateStr + " 02:01:00";
					}
					// 单式自购截止时间=开奖时间-2分钟（改成-90s）
					String singleEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 复式自购截止时间=开奖时间-2分钟（改成-90s）
					String compoundEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 单式合买截止时间=开奖时间-2分钟（改成-90s）
					String singleTogetherEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 复式合买截止时间=开奖时间-2分钟（改成-90s）
					String compoundTogetherEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 单式上传截止时间=开奖时间-2分钟（改成-90s）
					String singleUploadEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 出票开始时间=上一期开奖时间（改成当期开始销售时间+2分钟）
					if (z == 13) {
						printBeginTime = dateStr + " 06:00:10";
					}
					// 出票截止时间=开奖时间-1分钟
					String printEndTime = DateUtil.addDateMinut(drawTime, "MINUTE", -1);
					if (z == 0) {
						// 第一期出票时间特殊处理(第一期出票时间只有2分钟)
						printBeginTime = dateStr + " 00:00:05";
						printEndTime = dateStr + " 00:02:00";
					}
					// 官方销售开始时间=上一期开奖时间-3分钟
					String officialBeginTime = DateUtil.addDateMinut(drawTime, "MINUTE", -(offset + 3));
					if (z == 13) {
						officialBeginTime = dateStr + " 02:00:00";
					}
					// 官方销售截止时间=开奖时间-2分钟（改成-90s）
					String officialEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);

					String loop = transferIssueNoTail(String.valueOf(issueNoInt + z));

					LotteryIssue lotteryIssue = new LotteryIssue();
					lotteryIssue.setIssueNo(issueNoStr + loop);
					lotteryIssue.setLotteryId(lotteryId);
					lotteryIssue.setDrawTime(drawTime);
					lotteryIssue.setBeginTime(saleBeginTime);
					lotteryIssue.setSingleEndTime(singleEndTime);
					lotteryIssue.setCompoundEndTime(compoundEndTime);
					lotteryIssue.setSingleTogetherEndTime(singleTogetherEndTime);
					lotteryIssue.setCompoundTogetherEndTime(compoundTogetherEndTime);
					lotteryIssue.setSingleUploadEndTime(singleUploadEndTime);
					lotteryIssue.setPrintBeginTime(printBeginTime);
					lotteryIssue.setPrintEndTime(printEndTime);
					lotteryIssue.setOfficialBeginTime(officialBeginTime);
					lotteryIssue.setOfficialEndTime(officialEndTime);

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssue);
				}
			}
		} catch (Exception e) {
			Log.run.error("创建幸运农场期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 大乐透
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param lotteryId
	 * @return
	 */
	private int createLotteryIssueDlt(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
		try {
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				String week = DateUtil.getWeekOfDate(currentDate);

				if ("Monday".equals(week) || "Wednesday".equals(week) || "Saturday".equals(week)) {
					// 获取当前最大期号
					LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
					// 上一期开奖时间
					Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
					// 投注开始时间、官方销售开始时间
					String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);
					// 出票开始时间
					String printBeginTime = DateUtil.dateToString(DateUtil.getOffsetDate(previousDrawTime, 1),
							"yyyy-MM-dd");

					int issueNoInt = 0;
					// 当前期数年份
					String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
					// 数据库中最大期数日期年份
					String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

					if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
						issueNoInt = Integer.parseInt(currentDateYear + "001");
					} else {
						String issueNo = lotteryIssue.getIssueNo();
						issueNoInt = Integer.parseInt(issueNo) + 1;
					}

					LotteryIssue lotteryIssueTemp = new LotteryIssue();
					lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
					lotteryIssueTemp.setLotteryId(lotteryId);
					// 开奖时间=开奖日的20:30
					lotteryIssueTemp.setDrawTime(dateStr + " 20:30:00");
					// 投注开始时间=上一期开奖日的19:45
					lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
					// 单式自购截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
					// 复式自购截止时间=开奖日的19:30
					lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
					// 单式合买截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
					// 复式合买截止时间=开奖日的19:30
					lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
					// 单式上传截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
					// 出票开始时间=上一期开奖日的24点
					lotteryIssueTemp.setPrintBeginTime(printBeginTime + " 00:00:00");
					// 出票截止时间=开奖日的19:40
					lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
					// 官方销售开始时间=上一期开奖日的20:00
					lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 20:00:00");
					// 官方销售截止时间=开奖日的20:00
					lotteryIssueTemp.setOfficialEndTime(dateStr + " 20:00:00");

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);
				}
			}
		} catch (Exception e) {
			Log.run.error("创建大乐透期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 七星彩
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param lotteryId
	 * @return
	 */
	private int createLotteryIssueQxc(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
		try {
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				String week = DateUtil.getWeekOfDate(currentDate);

				if ("Tuesday".equals(week) || "Friday".equals(week) || "Sunday".equals(week)) {
					// 获取当前最大期号
					LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
					// 上一期开奖时间
					Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
					// 投注开始、官方销售开始日期
					String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);

					// 出票开始日期
					String printBeginTime = DateUtil.dateToString(DateUtil.getOffsetDate(previousDrawTime, 1),
							"yyyy-MM-dd");

					int issueNoInt = 0;
					// 当前期数年份
					String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
					// 数据库中最大期数日期年份
					String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

					if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
						issueNoInt = Integer.parseInt(currentDateYear + "001");
					} else {
						String issueNo = lotteryIssue.getIssueNo();
						issueNoInt = Integer.parseInt(issueNo) + 1;
					}

					LotteryIssue lotteryIssueTemp = new LotteryIssue();
					lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
					lotteryIssueTemp.setLotteryId(lotteryId);
					// 开奖时间=开奖日的20:30
					lotteryIssueTemp.setDrawTime(dateStr + " 20:30:00");
					// 投注开始时间=上一期开奖日的19:45
					lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
					// 单式自购截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
					// 复式自购截止时间=开奖日的19:30
					lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
					// 单式合买截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
					// 复式合买截止时间=开奖日的19:30
					lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
					// 单式上传截止时间=开奖日的19:30
					lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
					// 出票开始时间=上一期开奖时间的24:00
					lotteryIssueTemp.setPrintBeginTime(printBeginTime + " 00:00:00");
					// 出票截止时间=开奖日的19:40
					lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
					// 官方销售开始时间=上一期开奖日的20:00
					lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 20:00:00");
					// 官方销售截止时间=开奖日的20:00
					lotteryIssueTemp.setOfficialEndTime(dateStr + " 20:00:00");

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);
				}
			}
		} catch (Exception e) {
			Log.run.error("创建七星彩期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 排列三
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param lotteryId
	 * @return
	 */
	private int createLotteryIssuePls(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
		try {
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}

				// 获取当前最大期号
				LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
				// 上一期开奖时间
				Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
				// 投注开始、官方销售开始、出票开始日期
				String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);

				int issueNoInt = 0;
				// 当前期数年份
				String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
				// 数据库中最大期数日期年份
				String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

				if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
					issueNoInt = Integer.parseInt(currentDateYear + "001");
				} else {
					String issueNo = lotteryIssue.getIssueNo();
					issueNoInt = Integer.parseInt(issueNo) + 1;
				}

				LotteryIssue lotteryIssueTemp = new LotteryIssue();
				lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
				lotteryIssueTemp.setLotteryId(lotteryId);
				// 开奖时间=开奖日的20:30
				lotteryIssueTemp.setDrawTime(dateStr + " 20:30:00");
				// 投注开始时间=上一期开奖日的19:45
				lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
				// 单式自购截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
				// 复式自购截止时间=开奖日的19:30
				lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
				// 单式合买截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
				// 复式合买截止时间=开奖日的19:30
				lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
				// 单式上传截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
				// 出票开始时间=开奖日的08:30
				lotteryIssueTemp.setPrintBeginTime(dateStr + " 08:30:00");
				// 出票截止时间=开奖日的19:40
				lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
				// 官方销售开始时间=上一期开奖日的20:00
				lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 20:00:00");
				// 官方销售截止时间=开奖日的20:00
				lotteryIssueTemp.setOfficialEndTime(dateStr + " 20:00:00");

				isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);
			}
		} catch (Exception e) {
			Log.run.error("创建排列三期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 排列五
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param lotteryId
	 * @return
	 */
	private int createLotteryIssuePlw(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
		try {
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}

				// 获取当前最大期号
				LotteryIssue lotteryIssue = lotteryIssueDao.getMaxIssueNo(lotteryId);
				// 上一期开奖时间
				Date previousDrawTime = DateUtil.convertStringToDate("yyyy-MM-dd", lotteryIssue.getDrawTime());
				// 投注开始、官方销售开始、出票开始日期
				String previousDrawTimeStr = DateUtil.getDateTime("yyyy-MM-dd", previousDrawTime);

				int issueNoInt = 0;
				// 当前期数年份
				String currentDateYear = DateUtil.getDateTime("yyyy", currentDate);
				// 数据库中最大期数日期年份
				String previousDrawTimeYear = DateUtil.getDateTime("yyyy", previousDrawTime);

				if (Integer.parseInt(currentDateYear) > Integer.parseInt(previousDrawTimeYear)) {
					issueNoInt = Integer.parseInt(currentDateYear + "001");
				} else {
					String issueNo = lotteryIssue.getIssueNo();
					issueNoInt = Integer.parseInt(issueNo) + 1;
				}

				LotteryIssue lotteryIssueTemp = new LotteryIssue();
				lotteryIssueTemp.setIssueNo(String.valueOf(issueNoInt));
				lotteryIssueTemp.setLotteryId(lotteryId);
				// 开奖时间=开奖日的20:30
				lotteryIssueTemp.setDrawTime(dateStr + " 20:30:00");
				// 投注开始时间=上一期开奖日的19:45
				lotteryIssueTemp.setBeginTime(previousDrawTimeStr + " 19:45:00");
				// 单式自购截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleEndTime(dateStr + " 19:30:00");
				// 复式自购截止时间=开奖日的19:30
				lotteryIssueTemp.setCompoundEndTime(dateStr + " 19:30:00");
				// 单式合买截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleTogetherEndTime(dateStr + " 19:30:00");
				// 复式合买截止时间=开奖日的19:30
				lotteryIssueTemp.setCompoundTogetherEndTime(dateStr + " 19:30:00");
				// 单式上传截止时间=开奖日的19:30
				lotteryIssueTemp.setSingleUploadEndTime(dateStr + " 19:30:00");
				// 出票开始时间=开奖日的08:30
				lotteryIssueTemp.setPrintBeginTime(dateStr + " 08:30:00");
				// 出票截止时间=开奖日的19:40
				lotteryIssueTemp.setPrintEndTime(dateStr + " 19:40:00");
				// 官方销售开始时间=上一期开奖日的20:00
				lotteryIssueTemp.setOfficialBeginTime(previousDrawTimeStr + " 20:00:00");
				// 官方销售截止时间=开奖日的20:00
				lotteryIssueTemp.setOfficialEndTime(dateStr + " 20:00:00");

				isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssueTemp);
			}
		} catch (Exception e) {
			Log.run.error("创建排列五期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	/**
	 * 创建浙江11选5期号
	 * 
	 * @param beginTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lotteryId
	 *            彩种ID
	 * @return
	 */
	private int createLotteryIssueZJSYXW(String beginTime, String endTime, String lotteryId) {
		int isSuccess = 0;
		try {
			int days = DateUtil.getTimeDifference(beginTime, endTime) + 1;
			for (int i = 0; i < days; i++) {
				Date currentDate = DateUtil.convertStringToDate("yyyy-MM-dd", beginTime);
				currentDate = DateUtil.getOffsetDate(currentDate, i);

				String dateStr = DateUtil.dateToString(currentDate, "yyyy-MM-dd");
				// 判断是否为年初一至初七这段时间
				String checkDateTime = dateStr + " 00:00:00";
				if (DateUtil.checkIsHoliday(checkDateTime)) {
					continue;
				}
				// 初始期号
				String issueNoStr = DateUtil.dateToString(currentDate, "yyyyMMdd");
				int issueNoInt = 1;

				// 每天80期,从08:50到22:00
				for (int z = 0; z < 80; z++) {
					// 开奖间隔时间
					int offset = 10;
					// 当天开始销售时间
					String beginSaleTime = DateUtil.addDateMinut(dateStr + " 08:40:00", "MINUTE", z * offset);

					// 开奖时间=开奖时间08:50--22:00(80期)
					String drawTime = DateUtil.addDateMinut(beginSaleTime, "MINUTE", offset);
					// 投注开始时间=上期开奖时间-2分钟
					String saleBeginTime = DateUtil.addDateMinut(beginSaleTime, "MINUTE", -2);
					// 单式自购截止时间=开奖时间-90s
					String singleEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 复式自购截止时间=开奖时间-90s
					String compoundEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 单式合买截止时间=开奖时间-90s
					String singleTogetherEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 复式合买截止时间=开奖时间-90s
					String compoundTogetherEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 单式上传截止时间=开奖时间-90s
					String singleUploadEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);
					// 出票开始时间=上一期开奖时间
					String printBeginTime = DateUtil.addDateMinut(beginSaleTime, "SECOND", 10);
					// 出票截止时间=开奖时间-1分钟
					String printEndTime = DateUtil.addDateMinut(drawTime, "MINUTE", -1);
					// 官方销售开始时间=上一期开奖时间-3分钟
					String officialBeginTime = DateUtil.addDateMinut(drawTime, "MINUTE", -(offset + 3));
					// 官方销售截止时间=开奖时间-90s
					String officialEndTime = DateUtil.addDateMinut(drawTime, "SECOND", -90);

					String issueNo = String.valueOf(issueNoInt + z);
					String loop = issueNo.length() == 1 ? "0" + issueNo : issueNo;

					LotteryIssue lotteryIssue = new LotteryIssue();
					lotteryIssue.setIssueNo(issueNoStr + loop);
					lotteryIssue.setLotteryId(lotteryId);
					lotteryIssue.setDrawTime(drawTime);
					lotteryIssue.setBeginTime(saleBeginTime);
					lotteryIssue.setSingleEndTime(singleEndTime);
					lotteryIssue.setCompoundEndTime(compoundEndTime);
					lotteryIssue.setSingleTogetherEndTime(singleTogetherEndTime);
					lotteryIssue.setCompoundTogetherEndTime(compoundTogetherEndTime);
					lotteryIssue.setSingleUploadEndTime(singleUploadEndTime);
					lotteryIssue.setPrintBeginTime(printBeginTime);
					lotteryIssue.setPrintEndTime(printEndTime);
					lotteryIssue.setOfficialBeginTime(officialBeginTime);
					lotteryIssue.setOfficialEndTime(officialEndTime);

					isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssue);
				}
			}
		} catch (Exception e) {
			Log.run.error("创建浙江11选5期号发生异常", e);
			throw new DaoLevelException("-1");
		}
		return isSuccess;
	}

	@Override
	public LotteryIssue getMaxLotteryIssue(String lotteryId) {
		LotteryIssue issue = null;
		try {
			issue = lotteryIssueDao.getMaxIssueNo(lotteryId);
		} catch (Exception e) {
			Log.run.error("获取彩种最大期号发生异常,彩种：" + lotteryId, e);
			return null;
		}
		return issue;
	}

	@Override
	public List<LotteryIssue> getLotteryIssueListByState(int state, int currentPage, int pageSize) {
		List<LotteryIssue> issueList = null;
		try {
			issueList = lotteryIssueDao.getLotteryIssueListByState(state, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("按期号状态分页查询期号信息(定时任务使用)发生异常,期状态：" + state, e);
			return null;
		}
		return issueList;
	}

	@Override
	public ReturnData getLotteryIssueByParam(LotteryIssue lotteryIssue, int currentPage, int pageSize) {
		ReturnData returnData = null;
		try {
			returnData = lotteryIssueDao.getLotteryIssueByParam(lotteryIssue, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("根据状态查询期号列表发生异常", e);
			return null;
		}
		return returnData;
	}

	@Override
	public int createLotteryTaskComplete(LotteryTaskComplete lotteryTaskComplete) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueDao.createLotteryTaskComplete(lotteryTaskComplete);
		} catch (Exception e) {
			// ScanLog.run.error("新增期状态扫描任务完成记录发生异常", e);
			return Integer.valueOf(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public List<LotteryTaskComplete> getLotteryTaskCompleteByType(String lotteryId, String issueNo, int type) {
		List<LotteryTaskComplete> taskCompleteList = null;
		try {
			taskCompleteList = lotteryIssueDao.getLotteryTaskCompleteByType(lotteryId, issueNo, type);
		} catch (Exception e) {
			Log.run.error("获取期状态扫描任务完成记录发生异常", e);
			return null;
		}
		return taskCompleteList;
	}

	@Override
	public int updateLotteryIssueStateByParam(String lotteryId, String issueNo, int state) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueDao.updateLotteryIssueStateByParam(lotteryId, issueNo, state);
		} catch (Exception e) {
			Log.run.error("更新期号状态发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	@Transactional
	public int deleteIssueNo(String lotteryId, String issueNo) {
		int isSuccess = 0;
		try {
			LotteryIssue issue = lotteryIssueDao.findLotteryIssue(lotteryId, issueNo);

			String deleteDrawTime = issue.getDrawTime();

			isSuccess = lotteryIssueDao.deleteIssueNo(lotteryId, issueNo);

			Date deleteDrawDate = DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss", deleteDrawTime);
			String endYear = DateUtil.getDateTime("yyyy", deleteDrawDate);
			String deleteIssueNo = issueNo;

			int issueNoTail = Integer.valueOf(deleteIssueNo.substring(deleteIssueNo.length() - 3));
			String issueNoPrefix = deleteIssueNo.substring(0, deleteIssueNo.length() - 3);
			String compareDateStr = DateUtil.getDateTime("yyyy-MM-dd", deleteDrawDate);

			int issueNos = 0;
			String countBeginTime = "";
			String countEndTime = "";

			if (IssueConstant.LOTTERYID_SSC.equals(lotteryId)) {
				countBeginTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", deleteDrawDate);
				if (DateUtil.compareDateTime(deleteDrawTime, compareDateStr + " 00:00:00") <= 0
						&& DateUtil.compareDateTime(deleteDrawTime, compareDateStr + " 02:00:00") >= 0) {
					countEndTime = compareDateStr + " 02:00:00";
				} else {
					countEndTime = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.getOffsetDate(deleteDrawDate, 1))
							+ " 02:00:00";
				}
			} else if (IssueConstant.LOTTERYID_XYNC.equals(lotteryId)) {
				countBeginTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", deleteDrawDate);
				countEndTime = compareDateStr + " 23:59:59";
			} else {
				countBeginTime = deleteDrawTime;
				countEndTime = endYear + "-12-31 23:59:59";
			}
			issueNos = lotteryIssueDao.countIssue(lotteryId, countBeginTime, countEndTime);

			for (int i = 1; i <= issueNos; i++) {
				String oldIssueNo = issueNoPrefix + transferIssueNoTail(String.valueOf(issueNoTail + 1));
				String newIssueNo = issueNoPrefix + transferIssueNoTail(String.valueOf(issueNoTail));

				lotteryIssueDao.updateIssueNo(lotteryId, oldIssueNo, newIssueNo);
				issueNoTail++;
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("删除期号发生异常", e);
			return 0;
		}
		return isSuccess;
	}

	@Override
	public int updateTaskCompleteStatus(LotteryTaskComplete lotteryTaskComplete) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueDao.updateTaskCompleteStatus(lotteryTaskComplete);
		} catch (Exception e) {
			Log.run.error("更新任务完成状态发生异常", e);
		}
		return isSuccess;
	}

	@Override
	public List<LotteryTaskComplete> getLotteryTaskCompleteByStatus(int taskType, int status, int currentPage,
			int pageSize) {
		List<LotteryTaskComplete> taskList = null;
		try {
			taskList = lotteryIssueDao.getLotteryTaskCompleteByStatus(taskType, status, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("根据任务状态查询任务记录发生异常", e);
		}
		return taskList;
	}

	/**
	 * 判断彩种ID是否存在
	 * 
	 * @param lotteryId
	 * @return
	 */
	private boolean isExistLotteryId(String lotteryId) {
		boolean flag = false;
		LotteryItem lotteryItem = lotteryIssueDao.findLotteryItemByLotteryId(lotteryId);
		if (null != lotteryItem && !"".equals(lotteryItem)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 期号尾数转换
	 * 
	 * @param issueNoTail
	 * @return
	 */
	private String transferIssueNoTail(String issueNoTail) {
		String transferIssueNoTail = issueNoTail.length() == 1 ? "00" + issueNoTail : (issueNoTail.length() == 2 ? "0"
				+ issueNoTail : issueNoTail);
		return transferIssueNoTail;
	}

	@Override
	public List<LotteryIssue> getDrawIssueList(int currentPage, int pageSize) {
		List<LotteryIssue> issueList = null;
		try {
			issueList = lotteryIssueDao.getDrawIssueList(currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("获取能取开奖结果的期号列表发生异常", e);
		}
		return issueList;
	}

	@Override
	public List<LotteryIssue> getCanPrintIssueList(int currentPage, int pageSize) {
		List<LotteryIssue> issueList = null;
		try {
			issueList = lotteryIssueDao.getCanPrintIssueList(currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("获取能出票的期号列表发生异常", e);
		}
		return issueList;
	}

	@Override
	@Transactional
	public int updateLotteryIssue(LotteryIssue lotteryIssue) {
		int isSuccess = 0;
		try {
			isSuccess = lotteryIssueDao.updateLotteryIssue(lotteryIssue);
		} catch (Exception e) {
			Log.run.error("更新期号时间发生异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return isSuccess;
	}

	@Override
	@Transactional
	public int createIssueSport(List<IssueSport> issueSportList) {
		int returnValue = 0;
		try {
			for (IssueSport issueSport : issueSportList) {
				String lotteryId = issueSport.getLotteryId();
				String wareIssue = issueSport.getWareIssue();
				String endSellTime = issueSport.getEndSellTime();

				Log.run.debug("创建竞技彩期号,lotteryId=%s,wareIssue=%s", lotteryId, issueSport.getWareIssue());
				IssueSport issueTemp = lotteryIssueDao.findIssueSport(lotteryId, wareIssue);
				if (null != issueTemp && !"".equals(issueTemp)) {
					String endTime = issueTemp.getEndSellTime();
					Log.run.debug("sport issue time,lotteryId=%s,wareIssue=%s,endTime=%s,endSellTime=%s", lotteryId,
							issueTemp.getWareIssue(), endTime, endSellTime);
					if (DateUtil.compareDateTime(endSellTime, endTime) != 0) {
						// 结束时间与数据库中的结束时间不同,更新数据库中时间
						Log.run.debug("竞技彩期号,更新数据库中结束时间,lotteryId=%s,wareIssue=%s", lotteryId, wareIssue);
						returnValue = lotteryIssueDao.updateIssueSportEndTime(lotteryId, wareIssue, endSellTime);
						return returnValue;
					} else {
						Log.run.debug("竞技彩期号已存在,不创建,lotteryId=%s,wareIssue=%s", lotteryId, wareIssue);
						continue;
					}
				}

				issueSport.setBeginOfficialTime(issueSport.getBeginSellTime());
				issueSport.setEndOfficialTime(endSellTime);
				issueSport.setWareState(SportIssueConstant.SportIssueStatus.IN_SALE.getValue());
				returnValue = lotteryIssueDao.createIssueSport(issueSport);

				Log.run.debug("创建竞技彩期号完成,lotteryId=%s,wareIssue=%s,returnValue=%d", lotteryId,
						issueSport.getWareIssue(), returnValue);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("创建竞技彩期号发生异常", e);
		}
		return returnValue;
	}

	@Override
	public int updateIssueSport(String lotteryId, String wareIssue, int wareState) {
		int returnValue = 0;
		try {
			returnValue = lotteryIssueDao.updateIssueSport(lotteryId, wareIssue, wareState);
			Log.run.debug("更新竞技彩期号状态完成,lotteryId=%s,wareIssue=%s,wareState=%d,returnValue=%d", lotteryId, wareIssue,
					wareState, returnValue);
		} catch (Exception e) {
			Log.run.error("创建竞技彩期号发生异常,lotteryId=" + lotteryId, e);
		}
		return returnValue;
	}

	@Override
	public IssueSport findIssueSport(String lotteryId, String wareIssue) {
		IssueSport issueSport = null;
		try {
			if ((null == wareIssue || "".equals(wareIssue))
					&& (lotteryId.equals(IssueConstant.SportLotteryType.LZC_SF.getValue())
							|| lotteryId.equals(IssueConstant.SportLotteryType.LZC_R9.getValue())
							|| lotteryId.equals(IssueConstant.SportLotteryType.LZC_6BQC.getValue())
							|| lotteryId.equals(IssueConstant.SportLotteryType.LZC_4JQS.getValue()) || lotteryId
								.equals(IssueConstant.MATCHPLAY_BEIDAN_ALL))) {
				issueSport = lotteryIssueDao.findLZCIssueSport(lotteryId);
			} else {
				issueSport = lotteryIssueDao.findIssueSport(lotteryId, wareIssue);
			}
		} catch (Exception e) {
			Log.run.error("查询竞技彩期号发生异常,lotteryId=" + lotteryId + ",wareIssue=" + wareIssue, e);
		}
		return issueSport;
	}

	@Override
	@Transactional
	public int createMatchCompetiveAndPlay(MatchCompetive matchCompetive) {
		int returnValue = 0;
		try {
			String matchBeginTime = matchCompetive.getMatchBeginTime();
			String matchNo = matchCompetive.getMatchNo();
			int matchType = matchCompetive.getMatchType();
			String transferId = IssueUtil.getTransferId(matchBeginTime, matchNo, matchType);
			int offset = matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue() ? 2 : 3;
			String matchDate = matchCompetive.getMatchDate() + " 00:00:00";
			String wareIssue = matchCompetive.getWareIssue();

			matchCompetive.setWareIssue(IssueConstant.SPORT_ISSUE_CONSTANT);
			matchCompetive.setTransferId(transferId);
			matchCompetive.setBettingDeadline(IssueUtil.getPrintDeadlineTime(matchBeginTime));
			matchCompetive.setMatchEndTime(DateUtil.addDateMinut(matchBeginTime, "HOUR", offset));
			matchCompetive.setMatchDate(matchDate);

			MatchCompetive match = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
			if (null != match && !"".equals(match)) {
				String result = match.getDrawResult();
				if (null != result && !result.equals("")) {
					return 0;
				}
			}

			// 1、创建赛事
			returnValue = lotteryIssueDao.createMatchCompetive(matchCompetive);
			if (returnValue <= 0) {
				throw new Exception("创建竞足竞篮赛事发生异常,competiveValue=" + returnValue);
			}
			// 2、创建赛事玩法
			List<MatchCompetivePlay> playList = matchCompetive.getMatchCompetivePlayList();
			for (MatchCompetivePlay play : playList) {
				play.setTransferId(transferId);
				int playValue = lotteryIssueDao.createMatchCompetivePlay(play);
				if (playValue <= 0) {
					throw new Exception("创建赛事玩法发生异常,playValue=" + playValue);
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("创建竞足竞篮赛事发生异常", e);
			return 0;
		}
		return returnValue;
	}

	@Override
	@Transactional
	public int createMatchCompetiveAndPlayList(List<MatchCompetive> matchCompetiveList) {
		int returnValue = 0;
		try {
			for (MatchCompetive matchCompetive : matchCompetiveList) {
				String matchBeginTime = matchCompetive.getMatchBeginTime();
				String matchNo = matchCompetive.getMatchNo();
				int matchType = matchCompetive.getMatchType();
				String matchDate = matchCompetive.getMatchDate();
				String transferId = "";
				int offset = matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue() ? 2 : 3;
				String wareIssue = matchCompetive.getWareIssue();

				if (matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue()
						|| matchType == SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue()) {
					transferId = IssueUtil.getTransferId(matchDate, matchNo, matchType);
					wareIssue = IssueConstant.SPORT_ISSUE_CONSTANT;
				} else if (matchType == SportIssueConstant.CompetitionMatchType.BEIDAN.getValue()
						|| matchType == SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue()) {
					transferId = IssueUtil.getBeiDanTransferId(wareIssue, matchNo, matchType);
				}

				matchCompetive.setWareIssue(wareIssue);
				matchCompetive.setTransferId(transferId);
				matchCompetive.setBettingDeadline(IssueUtil.getPrintDeadlineTime(matchBeginTime));
				matchCompetive.setMatchEndTime(DateUtil.addDateMinut(matchBeginTime, "HOUR", offset));
				matchCompetive.setMatchDate(matchDate + " 00:00:00");

				Log.run.debug("create sport match,wareIssue=%s,matchType=%d", wareIssue, matchType);
				MatchCompetive match = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
				int updateMatchFlag = 0;
				if (null != match && !"".equals(match)) {
					String result = match.getDrawResult();
					int isAllowModify = match.getIsAllowModify();
					if (null != result && !result.equals("")) {
						continue;
					}
					if (isAllowModify == SportIssueConstant.CompetiveMatchIsAllowModify.MODIFY_NOTALLOW.getValue()) {
						updateMatchFlag = -1;
					}
				}

				// 1、创建赛事
				if (updateMatchFlag != -1) {
					returnValue = lotteryIssueDao.createMatchCompetive(matchCompetive);
					if (returnValue <= 0) {
						throw new Exception("创建竞足竞篮赛事列表发生异常,competiveValue=" + returnValue);
					}
				}
				// 2、创建赛事玩法
				List<MatchCompetivePlay> playList = matchCompetive.getMatchCompetivePlayList();
				for (MatchCompetivePlay play : playList) {
					play.setTransferId(transferId);
					play.setWareIssue(wareIssue);
					int playValue = lotteryIssueDao.createMatchCompetivePlay(play);
					if (playValue <= 0) {
						throw new Exception("创建赛事玩法列表发生异常,playValue=" + playValue);
					}
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("创建竞足竞篮赛事列表发生异常", e);
			return 0;
		}
		return returnValue;
	}

	@Override
	public MatchCompetive getMatchCompetiveResultList(String wareIssue, String transferId) {
		// TODO Auto-generated method stub
		MatchCompetive matchCompetive = null;
		try {
			matchCompetive = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
			if (null != matchCompetive && !"".equals(matchCompetive)) {
				// 竞足竞篮北单赛果
				matchCompetive.setMatchCompetiveResultList(lotteryIssueDao.getMatchCompetiveResultList(wareIssue, null,
						transferId));
			}
		} catch (Exception e) {
			Log.run.error("获取竞足竞北单篮赛事所有赛果发生异常,transferId=" + transferId, e);
		}
		return matchCompetive;
	}

	@Override
	public MatchCompetive getMatchCompetiveResult(String wareIssue, String lotteryId, String transferId) {
		// TODO Auto-generated method stub
		MatchCompetive result = null;
		try {
			result = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
			if (null != result && !"".equals(result)) {
				List<MatchCompetiveResult> resultList = lotteryIssueDao.getMatchCompetiveResultList(wareIssue,
						lotteryId, transferId);
				result.setMatchCompetiveResultList(resultList);
			}
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮赛事彩种赛果发生异常,transferId=" + transferId, e);
		}
		return result;
	}

	@Override
	public MatchCompetive getMatchCompetive(String wareIssue, String transferId) {
		// TODO Auto-generated method stub
		MatchCompetive matchCompetive = null;
		try {
			matchCompetive = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
			matchCompetive.setMatchCompetivePlayList(lotteryIssueDao.getMatchCompetivePlayList(wareIssue, transferId,
					null));
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮赛事信息发生异常,transferId=" + transferId, e);
		}
		return matchCompetive;
	}

	@Override
	public List<MatchCompetive> getMatchCompetiveListByMatchType(String lotteryId, int matchType) {
		// TODO Auto-generated method stub
		List<MatchCompetive> matchCompetiveList = null;
		try {
			matchCompetiveList = lotteryIssueDao.getMatchCompetiveListByMatchType(matchType);
			if (null != matchCompetiveList && matchCompetiveList.size() > 0) {
				if (lotteryId.equals(IssueConstant.SportLotteryType.JCLQ_HHGG.getValue())
						|| lotteryId.equals(IssueConstant.SportLotteryType.JCZQ_HHGG.getValue())
						|| lotteryId.equals(IssueConstant.MATCHPLAY_BEIDAN_ALL)) {
					lotteryId = null;
				}
				for (MatchCompetive matchCompetive : matchCompetiveList) {
					String wareIssue = matchCompetive.getWareIssue();
					String transferId = matchCompetive.getTransferId();
					List<MatchCompetivePlay> playList = lotteryIssueDao.getMatchCompetivePlayList(wareIssue,
							transferId, lotteryId);
					matchCompetive.setMatchCompetivePlayList(playList);
				}
			}
		} catch (Exception e) {
			Log.run.error("根据赛事类型获取竞足竞篮所有在售赛事发生异常,matchType=" + matchType, e);
		}
		return matchCompetiveList;
	}

	@Override
	@Transactional
	public int createMatchCompetiveResultList(List<MatchCompetive> matchCompetiveList) {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			for (MatchCompetive matchCompetive : matchCompetiveList) {
				String matchNo = matchCompetive.getMatchNo();
				int matchType = matchCompetive.getMatchType();
				String drawResult = matchCompetive.getDrawResult();
				String matchDate = matchCompetive.getMatchDate();

				String transferId = "";
				String wareIssue = matchCompetive.getWareIssue();

				if (matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue()
						|| matchType == SportIssueConstant.CompetitionMatchType.BASKETBALL.getValue()) {
					transferId = IssueUtil.getTransferId(matchDate, matchNo, matchType);
					wareIssue = IssueConstant.SPORT_ISSUE_CONSTANT;
				} else if (matchType == SportIssueConstant.CompetitionMatchType.BEIDAN.getValue()
						|| matchType == SportIssueConstant.CompetitionMatchType.BEIDAN_SFGG.getValue()) {
					transferId = IssueUtil.getBeiDanTransferId(wareIssue, matchNo, matchType);
				}
				Log.run.debug("sport match result 入库,wareIssue=%s,transferId=%s,drawResult=%s", wareIssue, transferId,
						drawResult);
				MatchCompetive match = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
				if (null != match && !"".equals(match)) {
					String result = match.getDrawResult();
					if (null != result && !result.equals("")) {
						continue;
					}
				} else {
					continue;
				}
				// 1、更新赛事表开奖结果字段
				returnValue = lotteryIssueDao.updateMatchCompetiveDrawResult(wareIssue, transferId, drawResult,
						SportIssueConstant.CompetiveMatchStatus.MATCH_HAS_DRAW.getValue());
				if (returnValue <= 0) {
					throw new Exception("竞足竞篮赛事开奖结果更新发生异常,returnValue=" + returnValue);
				}
				// 2、insert开奖结果表
				for (MatchCompetiveResult matchCompetiveResult : matchCompetive.getMatchCompetiveResultList()) {
					matchCompetiveResult.setTransferId(transferId);
					matchCompetiveResult.setWareIssue(wareIssue);
					returnValue = lotteryIssueDao.createMatchCompetiveResult(matchCompetiveResult);
				}
				if (returnValue <= 0) {
					throw new Exception("竞足竞篮赛事开奖结果入库发生异常,returnValue=" + returnValue);
				}
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("竞足竞篮赛事开奖结果入库发生异常", e);
			return 0;
		}
		return returnValue;
	}

	@Override
	public MatchCompetiveData getMatchCompetiveList(MatchCompetive matchCompetive, int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		MatchCompetiveData returnData = null;
		try {
			returnData = lotteryIssueDao.getMatchCompetiveList(matchCompetive, currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("根据条件搜索竞足竞篮赛事列表发生异常", e);
			return null;
		}
		return returnData;
	}

	@Override
	public int updateMatchCompetive(MatchCompetive matchCompetive) {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			String matchBeginTime = matchCompetive.getMatchBeginTime();
			int matchType = matchCompetive.getMatchType();
			int offset = matchType == SportIssueConstant.CompetitionMatchType.FOOTBALL.getValue() ? 2 : 3;
			String matchDate = matchCompetive.getMatchDate();
			int matchStatus = matchCompetive.getMatchStatus();

			matchCompetive.setBettingDeadline(IssueUtil.getPrintDeadlineTime(matchBeginTime));
			matchCompetive.setMatchEndTime(DateUtil.addDateMinut(matchBeginTime, "HOUR", offset));
			matchCompetive.setMatchDate(matchDate);
			matchCompetive.setIsAllowModify(SportIssueConstant.CompetiveMatchIsAllowModify.MODIFY_NOTALLOW.getValue());
			matchCompetive.setMatchStatus(matchStatus);
			matchCompetive.setDrawResult(matchCompetive.getDrawResult());
			// 更新赛事信息,但并不更新赛事玩法信息
			returnValue = lotteryIssueDao.updateMatchCompetive(matchCompetive);
		} catch (Exception e) {
			Log.run.error("修改竞足竞篮赛事信息发生异常,transferId=" + matchCompetive.getTransferId(), e);
		}
		return returnValue;
	}

	@Override
	@Transactional
	public int deleteMatchCompetive(String wareIssue, String transferId) {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			// 1、删除赛事信息
			returnValue = lotteryIssueDao.deleteMatchCompetive(wareIssue, transferId);
			if (returnValue <= 0) {
				throw new Exception("删除竞足竞篮北单赛事信息发生异常,returnValue=" + returnValue);
			}
			// 2、删除赛事玩法信息
			int playValue = lotteryIssueDao.deleteMatchCompetivePlay(wareIssue, transferId);
			if (playValue <= 0) {
				throw new Exception("删除竞足竞篮北单赛事玩法信息发生异常,returnValue=" + playValue);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("删除竞足竞篮北单赛事信息发生异常,wareIssue=" + wareIssue + "transferId=" + transferId, e);
		}
		return returnValue;
	}

	@Override
	public MatchCompetive getMatchCompetiveByLotteryId(String wareIssue, String transferId, String lotteryId) {
		// TODO Auto-generated method stub
		MatchCompetive matchCompetive = null;
		try {
			matchCompetive = lotteryIssueDao.getMatchCompetive(wareIssue, transferId);
			if (null != matchCompetive && !"".equals(matchCompetive)) {
				matchCompetive.setMatchCompetivePlayList(lotteryIssueDao.getMatchCompetivePlayList(wareIssue,
						transferId, lotteryId));
			}
		} catch (Exception e) {
			Log.run.error("获取竞足竞篮北单赛事彩种信息发生异常,transferId=" + transferId, e);
		}
		return matchCompetive;
	}

	@Override
	@Transactional
	public int createMatchFootball(List<MatchFootball> matchFootballList) {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			for (MatchFootball matchFootball : matchFootballList) {
				String lotteryId = matchFootball.getLotteryId();
				String wareIssue = matchFootball.getWareIssue();
				String matchNo = matchFootball.getMatchNo();
				MatchFootball football = lotteryIssueDao.getMatchFootballByParams(lotteryId, wareIssue, matchNo);
				if (null != football && !"".equals(football)) {
					String drawResult = football.getDrawResult();
					if (null != drawResult && !"".equals(drawResult)) {
						continue;
					}
				}
				Log.run.debug("create football match,lotteryId=%s,wareIssue=%s,matchNo=%s", lotteryId, wareIssue,
						matchNo);
				returnValue = lotteryIssueDao.createMatchFootball(matchFootball);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			Log.run.error("创建竞足竞篮赛事列表发生异常", e);
			return 0;
		}
		return returnValue;
	}

	@Override
	public int countMatchFootball(String lotteryId, String wareIssue) {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			returnValue = lotteryIssueDao.countMatchFootball(lotteryId, wareIssue);
		} catch (Exception e) {
			Log.run.error("计算老足彩赛事数据发生异常,wareIssue=" + wareIssue + ",lotteryId=" + lotteryId, e);
		}
		return returnValue;
	}

	@Override
	public List<MatchFootball> getMatchFootballList(String lotteryId, String wareIssue) {
		// TODO Auto-generated method stub
		List<MatchFootball> matchFootballList = null;
		try {
			matchFootballList = lotteryIssueDao.getMatchFootballList(lotteryId, wareIssue);
		} catch (Exception e) {
			Log.run.error("获取老足彩赛事信息发生异常,wareIssue=" + wareIssue + ",lotteryId=" + lotteryId, e);
		}
		return matchFootballList;
	}

	@Override
	public int createLZCLotteryIssue(LotteryIssue lotteryIssue) {
		// TODO Auto-generated method stub
		int isSuccess = 0;
		try {
			String lotteryId = lotteryIssue.getLotteryId();
			String issueNo = lotteryIssue.getIssueNo();
			String printBeginTime = lotteryIssue.getPrintBeginTime();
			String printEndTime = lotteryIssue.getPrintEndTime();

			LotteryIssue issueTemp = lotteryIssueDao.findLotteryIssue(lotteryId, issueNo);

			if (null != issueTemp && !"".equals(issueTemp)) {
				String compoundEndTime = issueTemp.getCompoundEndTime();
				Log.run.debug("LZC sport issue create,lotteryId=%s,issueNo=%s,compoundEndTime=%s,printEndTime=%s",
						lotteryId, issueNo, compoundEndTime, printEndTime);
				if (DateUtil.compareDateTime(compoundEndTime, printEndTime) != 0) {
					// 结束时间与数据库中的结束时间不同,更新数据库中时间
					Log.run.debug("更新数据库中期号结束时间,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
					issueTemp.setDrawTime(DateUtil.addDateMinut(printEndTime, "DAY", 1));
					issueTemp.setSingleEndTime(printEndTime);
					issueTemp.setCompoundEndTime(printEndTime);
					issueTemp.setSingleTogetherEndTime(printEndTime);
					issueTemp.setCompoundTogetherEndTime(printEndTime);
					issueTemp.setSingleUploadEndTime(printEndTime);
					issueTemp.setPrintEndTime(printEndTime);
					issueTemp.setOfficialEndTime(printEndTime);
					isSuccess = lotteryIssueDao.updateLotteryIssue(issueTemp);
					return isSuccess;
				} else {
					Log.run.debug("该老足彩期号已存在,不再进行创建,lotteryId=%s,issueNo=%s", lotteryId, issueNo);
					return isSuccess;
				}
			}
			LotteryIssue issue = null;
			String currentTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", new Date());
			if (DateUtil.compareDateTime(currentTime, printEndTime) < 0) {
				issue = lotteryIssueDao.getMaxLotteryIssueByIssue(lotteryId, issueNo);
			} else {
				issue = lotteryIssueDao.getMaxLotteryIssueByLotteryId(lotteryId);

			}
			String beginTime = "";
			if (null != issue && !"".equals(issue)) {
				beginTime = issue.getCompoundEndTime();
			} else {
				beginTime = printBeginTime;
			}
			lotteryIssue.setOfficialBeginTime(beginTime);
			lotteryIssue.setPrintBeginTime(beginTime);
			lotteryIssue.setBeginTime(beginTime);
			lotteryIssue.setDrawTime(DateUtil.addDateMinut(printEndTime, "DAY", 1));
			lotteryIssue.setSingleEndTime(printEndTime);
			lotteryIssue.setCompoundEndTime(printEndTime);
			lotteryIssue.setSingleTogetherEndTime(printEndTime);
			lotteryIssue.setCompoundTogetherEndTime(printEndTime);
			lotteryIssue.setSingleUploadEndTime(printEndTime);
			lotteryIssue.setPrintEndTime(printEndTime);
			lotteryIssue.setOfficialEndTime(printEndTime);

			isSuccess = lotteryIssueDao.addLotteryIssue(lotteryIssue);
			Log.run.debug("创建老足彩期号完成,lotteryId=%s,issueNo=%s,returnValue=%d", lotteryId, issueNo, isSuccess);
		} catch (Exception e) {
			Log.run.error("创建老足彩期号信息发生异常", e);
		}
		return isSuccess;
	}

	@Override
	public List<MatchCompetive> getMatchCompetiveListForCalPrize(int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		List<MatchCompetive> matchCompetiveList = null;
		try {
			matchCompetiveList = lotteryIssueDao.getMatchCompetiveListForCalPrize(currentPage, pageSize);
		} catch (Exception e) {
			Log.run.error("分页获取竞足竞篮北单算奖赛事数据发生异常", e);
		}
		return matchCompetiveList;
	}

	@Override
	public int updateMatchCompetiveStatus(String transferId, int matchStatus) {
		// TODO Auto-generated method stub
		int returnValue = 0;
		try {
			returnValue = lotteryIssueDao.updateMatchCompetiveStatus(transferId, matchStatus);
		} catch (Exception e) {
			Log.run.error("更新竞足竞篮北单赛事状态发生异常,transferId=" + transferId, e);
		}
		return returnValue;
	}

	@Override
	public MatchFootballDate getMatchFootballDate(MatchFootball matchFootball,
			int currentPage, int pageSize) {
		MatchFootballDate matchFootballDate = null;
		try {
			
			matchFootballDate = lotteryIssueDao.getMatchFootballDate(matchFootball, currentPage,pageSize);
		} catch (Exception e) {
			Log.run.error("分页获取老足彩信息列表异常:" + matchFootball, e);
		}
		
		return matchFootballDate;
	}

	@Override
	public MatchFootball getMatchFootball(String lotteryId, String wareIssue,
			String matchNo) {
		MatchFootball matchFootball = null;
		try {
			matchFootball = lotteryIssueDao.getMatchFootballByParams(lotteryId, wareIssue, matchNo);
		} catch (Exception e) {
			Log.run.error("获取老足彩信息异常:" + lotteryId+","+wareIssue+","+matchNo, e);
		}
		return matchFootball;
	}

	@Override
	public int updateMatchFootball(MatchFootball matchFootball) {
		int flag = 0;
		
		try {
			
			if(matchFootball.getMatchBeginTime()==null)matchFootball.setMatchBeginTime("0000-00-00 00:00:00");
			if(matchFootball.getSp()==null)matchFootball.setSp("");
			
			
			flag = lotteryIssueDao.updateMatchFootBall(matchFootball);
		} catch (Exception e) {
			Log.run.error("修改老足彩信息异常:" + matchFootball, e);
		}
		return flag;
	}

	@Override
	public int saveOrUpdateMatchCompetiveResult(
			MatchCompetiveResult matchCompetiveResult) {
		
		int flag = 0;
		try {
			if(matchCompetiveResult.getDrawResult()==null)matchCompetiveResult.setDrawResult("");
			if(matchCompetiveResult.getSp()==null)matchCompetiveResult.setSp("");
			if(matchCompetiveResult.getMatchId()==null)matchCompetiveResult.setMatchId("");
			if(matchCompetiveResult.getRq()==null)matchCompetiveResult.setRq("");
			matchCompetiveResult.setLotteryName(IssueConstant.SportLotteryType.getEnum(matchCompetiveResult.getLotteryId()).getText());
			List<MatchCompetiveResult> competiveResults =  lotteryIssueDao.getMatchCompetiveResultList(matchCompetiveResult.getWareIssue(),  matchCompetiveResult.getLotteryId(),matchCompetiveResult.getTransferId());
			DbGenerator.setDynamicMasterSource();
			if(competiveResults!=null &&competiveResults.size()>0){
				flag = lotteryIssueDao.updateMatchCompetiveResult(matchCompetiveResult);
			}else{
				flag = lotteryIssueDao.createMatchCompetiveResult(matchCompetiveResult);
			}
			
		} catch (Exception e) {
			Log.run.error("创建或修改赛事结果异常:" + matchCompetiveResult, e);
		}
		
		return flag;
	}

}
