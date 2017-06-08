package com.cqfc.ipquery.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.ipquery.dao.mapper.IpQueryMapper;
import com.cqfc.ipquery.model.IpSec;
@Repository
public class IpQueryDao {
	
	@Autowired
	private IpQueryMapper mapper;
	
	public IpSec getIpSecById(Long index){
		return mapper.getIpSecById(index);
	}
	
	public List<Long> getAllStartIndex(){
		return mapper.getAllStartIndex();
	}
	
	public int createIpSec(IpSec ipSec){
		return mapper.createIpSec(ipSec);
	}
}
