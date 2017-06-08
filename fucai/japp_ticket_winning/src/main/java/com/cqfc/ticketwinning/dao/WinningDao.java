package com.cqfc.ticketwinning.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.protocol.ticketwinning.ReturnData;
import com.cqfc.protocol.ticketwinning.WinningAmountStat;
import com.cqfc.protocol.ticketwinning.WinningAmountStatData;
import com.cqfc.protocol.ticketwinning.WinningNumStat;
import com.cqfc.protocol.ticketwinning.WinningNumStatData;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;
import com.cqfc.ticketwinning.dao.mapper.WinningMapper;
import com.cqfc.ticketwinning.model.Winning;
import com.cqfc.ticketwinning.util.TicketWinningConstantsUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.DBSourceUtil;
import com.jami.util.Log;
import com.jami.util.ScanLog;

@Repository
public class WinningDao {

	@Autowired
	private WinningMapper winningMapper;

	/**
	 * 根据lotteryId和issueNo查询投注中奖结果
	 * 
	 * @param conditions
	 * @return
	 */
	public List<Winning> getWinningList(String lotteryId, String issueNo,
			int currentPage, int pageSize, String tableName) {
		List<Winning> winningList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			if (null != lotteryId && !"".equals(lotteryId)) {
				conditions.append(" and lotteryId ='" + lotteryId + "'");
			}
			if (null != issueNo && !"".equals(issueNo)) {
				conditions.append(" and issueNo ='" + issueNo + "'");
			}
			conditions.append(" limit " + (currentPage - 1) * pageSize + ","
					+ pageSize);
			winningList = winningMapper.getWinningList(conditions.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error("getWinningListDao(exception=%s)", e);
			return null;
		}

		return winningList;
	}
	
	
	/**
	 *查询投注中奖订单
	 * 
	 * @param conditions
	 * @return
	 */
	public ReturnData getWinningOrderList(WinningOrderInfo winningOrderInfo,
			int currentPage, int pageSize, String tableName) {
		ReturnData returnData = new ReturnData();
		List<WinningOrderInfo> winningList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			if (null != winningOrderInfo.getLotteryId() && !"".equals(winningOrderInfo.getLotteryId())) {
				conditions.append(" and lotteryId ='" + winningOrderInfo.getLotteryId() + "'");
			}
			if (null != winningOrderInfo.getIssueNo() && !"".equals(winningOrderInfo.getIssueNo())) {
				conditions.append(" and issueNo ='" + winningOrderInfo.getIssueNo() + "'");
			}
			if (null != winningOrderInfo.getPartnerId() && !"".equals(winningOrderInfo.getPartnerId())) {
				conditions.append(" and partnerId ='" + winningOrderInfo.getPartnerId() + "'");
			}
			if (null != winningOrderInfo.getOrderNo() && !"".equals(winningOrderInfo.getOrderNo())) {
				conditions.append(" and orderNo ='" + winningOrderInfo.getOrderNo() + "'");
			}
			int totalSize = countSumTotalSize(conditions.toString(), tableName);
			conditions.append(" order by lastUpdateTime desc limit " + (currentPage - 1) * pageSize + ", "
					+ pageSize);
			winningList = winningMapper.getWinningOrderList(conditions.toString(),
					tableName);
			returnData.setCurrentPage(currentPage);
			returnData.setPageSize(pageSize);
			returnData.setTotalSize(totalSize);
			returnData.setResultList(winningList);
		} catch (Exception e) {
			Log.run.error("getWinningOrderList(exception=%s)", e);
			return null;
		}

		return returnData;
	}

	/**
	 * 根据lotteryId和issueNo查询未派奖记录
	 * 
	 * @param conditions
	 * @return
	 */
	public List<Winning> getUnSendPrizeWinningList(String lotteryId,
			String issueNo, int currentPage, int pageSize, String tableName) {
		List<Winning> winningList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			if (null != lotteryId && !"".equals(lotteryId)) {
				conditions.append(" and lotteryId ='" + lotteryId + "'");
			}
			if (null != issueNo && !"".equals(issueNo)) {
				conditions.append(" and issueNo ='" + issueNo + "'");
			}
			conditions
					.append(" and sendPrizeState = "
							+ Winning.SendPrizeState.NO_SENDPRIZE.getValue()
							+ " limit " + (currentPage - 1) * pageSize + ","
							+ pageSize);
			winningList = winningMapper.getWinningList(conditions.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error("getUnSendPrizeWinningListDao(exception=%s)", e);
			return null;
		}

		return winningList;
	}

	/**
	 * 根据lotteryId和issueNo查询投注中奖结果
	 * 
	 * @param conditions
	 * @return
	 */
	public List<Winning> getWinningAmountZeroList(String lotteryId,
			String issueNo, String tableName) {
		List<Winning> winningList = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			if (null != lotteryId && !"".equals(lotteryId)) {
				conditions.append(" and lotteryId ='" + lotteryId + "'");
			}
			if (null != issueNo && !"".equals(issueNo)) {
				conditions.append(" and issueNo ='" + issueNo + "'");
			}
			conditions.append(" and winningAmount = 0 ");
			winningList = winningMapper.getWinningList(conditions.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error("getWinningAmountZeroListDao(exception=%s)", e);
			return null;
		}

		return winningList;
	}

	/**
	 * 根据lotteryId和issueNo查询投注中奖结果总数
	 * 
	 * @return
	 */
	public int countTotalSize(String lotteryId, String issueNo, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			if (null != lotteryId && !"".equals(lotteryId)) {
				conditions.append(" and lotteryId ='" + lotteryId + "'");
			}
			if (null != issueNo && !"".equals(issueNo)) {
				conditions.append(" and issueNo ='" + issueNo + "'");
			}
			returnValue = winningMapper.countTotalSize(conditions.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error("countTotalSizeDao(exception=%s)", e);
			return returnValue;
		}

		return returnValue;
	}

	/**
	 * 统计中奖金额
	 * 
	 * @param conditions
	 * @return
	 */
	public WinningAmountStatData getWinningAmountStat(
			List<WinningAmountStat> winningAmountStatList, int currentPage,
			int pageSize, String tableName) {
		WinningAmountStatData winningAmountStatData = new WinningAmountStatData();
		WinningAmountStat winningAmountStat = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			// 搜索参数
			for (int i = 0, size = winningAmountStatList.size(); i < size; i++) {
				winningAmountStat = winningAmountStatList.get(i);

				if (null != winningAmountStat.getLotteryId()
						&& !"".equals(winningAmountStat.getLotteryId())) {
					if (i == 0) {
						conditions.append(" and ");
					} else {
						conditions.append(" or ");
					}
					conditions.append(" (lotteryId ='"
							+ winningAmountStat.getLotteryId() + "'");
				}
				if (null != winningAmountStat.getIssueNo()
						&& !"".equals(winningAmountStat.getIssueNo())) {
					conditions.append(" and issueNo ='"
							+ winningAmountStat.getIssueNo() + "')");
				}
				if (null != winningAmountStat.getPartnerId()
						&& !"".equals(winningAmountStat.getPartnerId())) {
					conditions.append(" and partnerId ='"
							+ winningAmountStat.getPartnerId() + "'");
				}
			}

			int totalSize = countSumTotalSize(conditions.toString(), tableName);
			int totalPage = (int) Math.ceil((double) totalSize
					/ (double) pageSize);
			if (totalPage >= currentPage) {
				conditions
						.append(" group by lotteryId, issueNo, partnerId limit "
								+ (currentPage - 1) * pageSize + "," + pageSize);
			}

			List<WinningAmountStat> list = winningMapper.getWinningAmountStat(
					conditions.toString(), tableName);
			if (list.size() == 1 && list.get(0) == null) {
				list.remove(0);
			}
			winningAmountStatData.setCurrentPage(currentPage);
			winningAmountStatData.setPageSize(pageSize);
			winningAmountStatData.setTotalSize(totalSize);
			winningAmountStatData.setResultList(list);
		} catch (Exception e) {
			Log.run.error("getWinningAmountStatDao(exception=%s)", e);
			return null;
		}
		return winningAmountStatData;
	}

	/**
	 * 统计中奖总数
	 * 
	 * @param conditions
	 * @return
	 */
	public int countSumTotalSize(String conditions, String tableName) {
		return winningMapper.countTotalSize(conditions.toString(), tableName);
	}

	/**
	 * 统计中奖大小奖个数与金额
	 * 
	 * @param winningNumStatList
	 * @param currentPage
	 * @param pageSize
	 * @param tableName
	 * @return
	 */
	public WinningNumStatData getWinningNumStat(WinningNumStat winningNumStat,
			int currentPage, int pageSize, String tableName) {
		WinningNumStatData winningNumStatData = null;
		StringBuffer conditions = new StringBuffer(" 1=1 ");

		try {
			if (winningNumStat != null) {
				if (null != winningNumStat.getLotteryId()
						&& !"".equals(winningNumStat.getLotteryId())) {
					conditions.append(" and lotteryId ='"
							+ winningNumStat.getLotteryId() + "'");
				}
				if (null != winningNumStat.getIssueNo()
						&& !"".equals(winningNumStat.getIssueNo())) {
					conditions.append(" and issueNo ='"
							+ winningNumStat.getIssueNo() + "'");
				}
				if (null != winningNumStat.getPartnerId()
						&& !"".equals(winningNumStat.getPartnerId())) {
					conditions.append(" and partnerId ='"
							+ winningNumStat.getPartnerId() + "'");
				}
				if (!"".equals(winningNumStat.getOrderType())
						&& winningNumStat.getOrderType() != 0) {
					conditions.append(" and orderType = "
							+ winningNumStat.getOrderType() + "");
				}
			}
			int totalSize = countSumTotalSize(conditions.toString(), tableName);
			int totalPage = (int) Math.ceil((double) totalSize
					/ (double) pageSize);
			if (totalPage >= currentPage) {
				conditions.append(" group by partnerId, orderType limit "
						+ (currentPage - 1) * pageSize + "," + pageSize);
			}
			List<WinningNumStat> winningNumStatList = winningMapper
					.getWinningNumStat(conditions.toString(),
							TicketWinningConstantsUtil.BIG_PRIZE_AMOUNT,
							tableName);

			if (winningNumStatList.size() == 1
					&& winningNumStatList.get(0) == null) {
				winningNumStatList.remove(0);
			}
			winningNumStatData = new WinningNumStatData();
			winningNumStatData.setCurrentPage(currentPage);
			winningNumStatData.setPageSize(pageSize);
			winningNumStatData.setTotalSize(totalSize);
			winningNumStatData.setResultList(winningNumStatList);
		} catch (Exception e) {
			Log.run.error("getWinningNumStatDao(exception=%s)", e);
			return null;
		}
		return winningNumStatData;
	}

	/**
	 * 更新中奖订单派奖状态
	 * 
	 * @param id
	 * @param sendPrizeState
	 * @param tableName
	 * @return
	 */
	public int updateSendPrizeState(long id, int sendPrizeState,
			String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		StringBuffer conditions = new StringBuffer(" 1 = 1 ");
		try {
			conditions.append(" and id = " + id);
			returnValue = winningMapper.updateSendPrizeState(sendPrizeState,
					conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("updateSendPrizeStateDao(exception=%s)", e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 删除中奖结果记录
	 * 
	 * @param tableName
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	public int deleteWinningRecordsByLotteryIdAndIssueNo(String lotteryId,
			String issueNo, String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = winningMapper
					.deleteWinningRecordsByLotteryIdAndIssueNo(lotteryId,
							issueNo, tableName);
			ScanLog.scan
					.debug("删除中奖结果记录(deleteWinningRecordsByLotteryIdAndIssueNo),tableName=%s,lotteryId=%s,issueNo=%s,deleteReturnValues=%d",
							tableName, lotteryId, issueNo, returnValue);
		} catch (Exception e) {
			Log.run.error("deleteWinningRecordsByLotteryIdAndIssueNoDao(exception=%s)", e);
			return returnValue;
		}
		return returnValue;
	}
	
	/**
	 * 删除竞彩中奖结果记录
	 * @param transferId
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	public int deleteJCWinningRecordsByTransferId(String transferId,
			String tableName, String jcTableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = winningMapper
					.deleteJCWinningRecordsByorderNo(transferId, tableName, jcTableName);
			ScanLog.scan
					.debug("删除中奖结果记录(deleteJCWinningRecordsByTransferId),transferId=%s,tableName=%s,jcTableName=%s,deleteReturnValues=%d",
							transferId, tableName, jcTableName, returnValue);
		} catch (Exception e) {
			Log.run.error("deleteJCWinningRecordsByTransferId(exception=%s)", e);
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 更新中奖金额
	 * 
	 * @param winningAmount
	 * @param conditions
	 * @param tableName
	 * @return
	 */
	public int updateWinningAmount(long winningAmount, long winningId,
			String tableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		StringBuffer conditions = new StringBuffer(" 1 = 1 ");
		try {
			conditions.append(" and id = " + winningId);
			returnValue = winningMapper.updateWinningAmount(winningAmount,
					conditions.toString(), tableName);
		} catch (Exception e) {
			Log.run.error("updateWinningAmountDao(exception=%s)", e);
			return returnValue;
		}
		return returnValue;
	}

	public long getTotalWinningMoneyByGame(String lotteryId, String issueNo) {
		Long money = winningMapper.getTotalWinningMoneyByGame(lotteryId, issueNo,
				DBSourceUtil.WINNING_RESULT_TABLENAME);
		if(money == null){
			return 0;
		}
		return money;
	}

	public long getTotalWinningMoneyByDay(String start, String end) {
		Long money =  winningMapper.getTotalWinningMoneyByDay(start, end,
				DBSourceUtil.WINNING_RESULT_TABLENAME);
		if(money == null){
			return 0;
		}
		return money;
	}

	/**
	 * 删除北单中奖结果记录
	 * @param wareIssue
	 * @param matchNo
	 * @param matchType
	 * @param tableName
	 * @param jcTableName
	 * @return
	 */
	public int deleteBDWinningRecordsByWareIssueAndMatchNoAndMatchType(
			String wareIssue, String matchNo, int matchType, String tableName,
			String jcTableName) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = winningMapper
					.deleteBDWinningRecordsByorderNo(wareIssue, matchNo, matchType, tableName, jcTableName);
			ScanLog.scan
					.debug("删除中奖结果记录(deleteBDWinningRecordsByWareIssueAndMatchNoAndMatchType),wareIssue=%s,matchNo=%s,matchType=%d,tableName=%s,jcTableName=%s,deleteReturnValues=%d",
							wareIssue, matchNo, matchType, tableName, jcTableName, returnValue);
		} catch (Exception e) {
			Log.run.error("deleteJCWinningRecordsByTransferId(exception=%s)", e);
			return returnValue;
		}
		return returnValue;
	}
}
