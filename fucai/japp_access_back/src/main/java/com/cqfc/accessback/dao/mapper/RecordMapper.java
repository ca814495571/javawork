package com.cqfc.accessback.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.accessback.TicknumRecord;
import com.jami.common.BaseMapper;

public interface RecordMapper extends BaseMapper {
	@Select("select gameId, issue, sum(successNum) as successNum, sum(failedNum)as failNum from t_lottery_ticket_num where gameId=#{gameId} and issue=#{issue}")
	TicknumRecord getTicknumRecord(@Param("gameId") String gameId, @Param("issue") String issue);
}
