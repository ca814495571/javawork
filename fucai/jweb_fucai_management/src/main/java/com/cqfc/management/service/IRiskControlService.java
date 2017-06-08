package com.cqfc.management.service;

import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.cqfc.protocol.riskcontrol.StatisticDayPageData;
import com.cqfc.protocol.riskcontrol.StatisticPageData;

public interface IRiskControlService {
	/**
	 * 根据彩种期号查询统计信息
	 * @param gameId
	 * @param issue
	 * @return
	 */
    public StatisticDataByGame getStatisticByGame(String gameId, String issue);

    /**
     * 根据彩种查询一页的统计信息
     * @param gameId
     * @param currentPage
     * @param pageSize
     * @return
     */
    public StatisticPageData queryStatisticByGame(String gameId, int currentPage, int pageSize);

    /**
     * 根据日期统计
     * @param day
     * @return
     */
    public StatisticDayPageData getStatisticByDay(String day, int currentPage, int pageSize);

}
