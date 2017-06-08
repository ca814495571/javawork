package com.cqfc.riskcontrol.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cqfc.protocol.riskcontrol.StatisticDataByDay;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.riskcontrol.StatisticDayPageData;
import com.cqfc.protocol.riskcontrol.StatisticPageData;
import com.cqfc.riskcontrol.dao.mapper.RiskControlMapper;

@Repository
public class RiskControlDao {
	@Autowired
	private RiskControlMapper riskControlMapper;

	public StatisticDataByGame getStatisticByGame(String gameId, String issue) {
		return riskControlMapper.getStatisticDataByGame(gameId, issue);
	}

	public int createTickStatisticByGame(String gameId, String issue) {
		return riskControlMapper.createTickStatisticByGame(gameId, issue);
	}

	public int updateStatisticDataByGame(StatisticDataByGame data) {
		return riskControlMapper.updateStatisticDataByGame(data);
	}

	public int updateFucaiCountByGame(StatisticDataByGame data){
		return riskControlMapper.updateFucaiCountByGame(data);
	}
	public int updateStatisticTicketNumByGame(StatisticDataByGame data) {
		return riskControlMapper.updateStatisticTicketNumByGame(data);
	}

	public int updateStatisticWinningByGame(StatisticDataByGame data) {
		return riskControlMapper.updateStatisticWinningByGame(data);
	}

	public int createTickStatisticByDay(String day) {
		return riskControlMapper.createTickStatisticByDay(day);
	}

	public StatisticDataByDay getStatisticDataByDay(String day) {
		return riskControlMapper.getStatisticDataByDay(day);
	}

	public int updateStatisticDataByDay(StatisticDataByDay data) {
		return riskControlMapper.updateStatisticDataByDay(data);
	}

	public StatisticPageData queryStatisticByGame(String gameId,
			int currentPage, int pageSize) {
		StatisticPageData returnData = new StatisticPageData();
		String conditions = "1=1";

		// 搜索参数
		if (!StringUtils.isEmpty(gameId)) {
			conditions += " and gameId='" + gameId + "'";
		}
		int totalSize = riskControlMapper.getCount(conditions);
		int totalPage = (totalSize - 1) / pageSize + 1;
		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		conditions += " order by issue desc";
		if (totalPage >= currentPage) {
			conditions += " limit " + (currentPage - 1) * pageSize + ","
					+ pageSize;
		} else {
			returnData.setTotalSize(totalSize);
			returnData.setResultList(new ArrayList<StatisticDataByGame>());
			return returnData;
		}
		List<StatisticDataByGame> list = riskControlMapper
				.queryStatisticDataByGame(conditions);

		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	public StatisticDayPageData queryStatisticByDay(String day, int currentPage,
			int pageSize) {
		StatisticDayPageData returnData = new StatisticDayPageData();
		String conditions = "1=1";

		// 搜索参数
		if (!StringUtils.isEmpty(day)) {
			conditions += " and day='" + day + "'";
		}
		int totalSize = riskControlMapper.getDayCount(conditions);
		int totalPage = (totalSize - 1) / pageSize + 1;
		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		conditions += " order by day desc";
		if (totalPage >= currentPage) {
			conditions += " limit " + (currentPage - 1) * pageSize + ","
					+ pageSize;
		} else {
			returnData.setTotalSize(totalSize);
			returnData.setResultList(new ArrayList<StatisticDataByDay>());
			return returnData;
		}
		List<StatisticDataByDay> list = riskControlMapper
				.queryStatisticDataByDay(conditions);

		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	public int updateFucaiCountByDay(StatisticDataByDay data) {
		return riskControlMapper.updateFucaiCountByDay(data);
	}
}
