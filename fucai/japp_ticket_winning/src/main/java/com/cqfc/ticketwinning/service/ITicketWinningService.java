package com.cqfc.ticketwinning.service;

import java.util.List;
import java.util.Map;

import com.cqfc.ticketwinning.model.Prize;

public interface ITicketWinningService {
	
	/**
	 * 校验投注内容是否合法
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	public boolean validateContent(String orderContent, String playType);
	
	/**
	 * 计算一张彩票中奖金额
	 * @param orderContent
	 * @param playType
	 * @param winningBallContent
	 * @param prizeLevelMap
	 * @return
	 */
	public Prize calTicketWinningAmount(String orderContent, String playType, String winningBallContent, Map<Integer, Long> prizeLevelMap);
	
	/**
	 * 计算投注总注数
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	public int calBallCounts(String orderContent, String playType);
	
	/**
	 * 计算每注详细内容
	 * @param orderContent
	 * @param playType
	 * @return
	 */
	public List<String> listTicketDetails(String orderContent, String playType);
}
