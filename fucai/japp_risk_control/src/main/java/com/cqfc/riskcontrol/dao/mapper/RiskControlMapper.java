package com.cqfc.riskcontrol.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.riskcontrol.StatisticDataByDay;
import com.cqfc.protocol.riskcontrol.StatisticDataByGame;
import com.jami.common.BaseMapper;

public interface RiskControlMapper extends BaseMapper {
	@Insert("replace into t_lottery_ticket_statistic_day(day) values(#{day})")
	int createTickStatisticByDay(@Param("day") String day);
	
	@Select("select * from t_lottery_ticket_statistic_day where day=#{day}")
	StatisticDataByDay getStatisticDataByDay(@Param("day") String day);
	
	@Update("update t_lottery_ticket_statistic_day set totalRecharge=#{data.totalRecharge},totalPartnerAccount=#{data.totalPartnerAccount},totalUserAccount=#{data.totalUserAccount},"
			+ "totalWinningMoney=#{data.totalWinningMoney},totalHansel=#{data.totalHansel},invalidHansel=#{data.invalidHansel},"
			+ "fucaiTotalBuy=#{data.fucaiTotalBuy},totalWithdraw=#{data.totalWithdraw},totalTicketNum=#{data.totalTicketNum},paylogNum=#{data.paylogNum}"
			+ " where day=#{data.day}")
	int updateStatisticDataByDay(@Param("data")StatisticDataByDay data);
	
	@Update("update t_lottery_ticket_statistic_day set fucaiTotalBuy=#{data.fucaiTotalBuy} where day=#{data.day}")
	int updateFucaiCountByDay(@Param("data") StatisticDataByDay data);
	
	@Insert("insert into t_lottery_ticket_statistic(gameId,issue) values(#{gameId}, #{issue})")
	int createTickStatisticByGame(@Param("gameId") String gameId, @Param("issue") String issue);
	
	@Select("select * from t_lottery_ticket_statistic where gameId=#{gameId} and issue=#{issue}")
	StatisticDataByGame getStatisticDataByGame(@Param("gameId") String gameId, @Param("issue") String issue);
	
	
	@Update("update t_lottery_ticket_statistic set outTicketSuccessNum=#{data.outTicketSuccessNum},successTicketNum=#{data.successTicketNum},"
			+ "successTotalMoney=#{data.successTotalMoney}, cancelAndSuccessNum=#{data.cancelAndSuccessNum},totalWinningMoney=#{data.totalWinningMoney}, fucaiTicketNum=#{data.fucaiTicketNum},"
			+ "fucaiTotalBuy=#{data.fucaiTotalBuy},fucaiTotalWinning=#{data.fucaiTotalWinning}"
			+ " where gameId=#{data.gameId} and issue=#{data.issue}")
	int updateStatisticDataByGame(@Param("data")StatisticDataByGame data);
	
	@Update("update t_lottery_ticket_statistic set fucaiTicketNum=#{data.fucaiTicketNum},"
			+ "fucaiTotalBuy=#{data.fucaiTotalBuy},fucaiTotalWinning=#{data.fucaiTotalWinning}"
			+ " where gameId=#{data.gameId} and issue=#{data.issue}")
	int updateFucaiCountByGame(@Param("data")StatisticDataByGame data);
	
	@Update("update t_lottery_ticket_statistic set outTicketSuccessNum=#{data.outTicketSuccessNum},successTicketNum=#{data.successTicketNum},"
			+ "successTotalMoney=#{data.successTotalMoney}, cancelAndSuccessNum=#{data.cancelAndSuccessNum}"
			+ " where gameId=#{data.gameId} and issue=#{data.issue}")
	int updateStatisticTicketNumByGame(@Param("data")StatisticDataByGame data);
	
	
	@Update("update t_lottery_ticket_statistic set totalWinningMoney=#{data.totalWinningMoney}, fucaiTicketNum=#{data.fucaiTicketNum},"
			+ "fucaiTotalBuy=#{data.fucaiTotalBuy},fucaiTotalWinning=#{data.fucaiTotalWinning}"
			+ " where gameId=#{data.gameId} and issue=#{data.issue}")
	int updateStatisticWinningByGame(@Param("data")StatisticDataByGame data);
	
	@Select("select count(*) from t_lottery_ticket_statistic where ${conditions}")
	int getCount(@Param("conditions") String conditions);
	
	@Select("select * from t_lottery_ticket_statistic where ${conditions}")
	List<StatisticDataByGame> queryStatisticDataByGame(@Param("conditions") String conditions);
	

	@Select("select count(*) from t_lottery_ticket_statistic_day where ${conditions}")
	int getDayCount(@Param("conditions") String conditions);
	
	@Select("select * from t_lottery_ticket_statistic_day where ${conditions}")
	List<StatisticDataByDay> queryStatisticDataByDay(@Param("conditions") String conditions);


}
