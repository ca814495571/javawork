package com.cqfc.accessback.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.accessback.dao.mapper.RecordMapper;
import com.cqfc.protocol.accessback.TicknumRecord;

@Repository
public class RecordDao {

	@Autowired
	private RecordMapper recordMapper;
	
	public TicknumRecord getTicknumRecord(String gameId, String issue) {
		return recordMapper.getTicknumRecord(gameId, issue);
	}

}
