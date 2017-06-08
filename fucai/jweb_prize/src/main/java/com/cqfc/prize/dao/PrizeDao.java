package com.cqfc.prize.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.prize.dao.mapper.PrizeMapper;
import com.cqfc.prize.model.Prize;
import com.jami.util.Log;


@Repository
public class PrizeDao {
	
	@Autowired
	private PrizeMapper prizeMapper;
	
	
	public int insert(Prize prize,String tableName){
		
		
		if(prize.getUserId()==null) prize.setUserId("");
		if(prize.getTicketId()==null) prize.setTicketId("");
		if(prize.getGameCode()==null) prize.setGameCode("");
		if(prize.getGameSerial()==null) prize.setGameSerial("");
		if(prize.getPackageNum()==null) prize.setPackageNum("");
		if(prize.getTicketNum()==null) prize.setTicketNum("");
		if(prize.getMessage()==null) prize.setMessage("");
		
		int flag = 0;
		try {
			
			flag = prizeMapper.insert(prize, tableName);
		} catch (Exception e) {
			Log.run.error("insert exception:"+e);
			return 0;
		}
		
		return flag;
	}
	
	public int update(Prize prize,String tableName){
		if(prize.getMessage()==null) prize.setMessage("");
		int flag = 0;
		try {
			
			flag = prizeMapper.update(prize, tableName);
		} catch (Exception e) {
			Log.run.error("update exception:"+e);
		}
		
		return flag;
	}
	
	
	public Prize queryOne(Prize prize,String tableName){
		
		Prize result = null;
		try {
			
			result = prizeMapper.queryOne(prize, tableName);
			
		} catch (Exception e) {
			Log.run.error("querytOne exception:"+e);
		}
		return result;
	}
	
	
	
	public int queryForSum(Prize prize,String tableName){
		
		
		int sum = 0 ;
		try {
			
			sum = prizeMapper.queryForSum(prize, tableName);
			
		} catch (Exception e) {
			Log.run.error("queryForSum exception:"+e);
		}
		
		return sum;
	}
	
	
	public List<Prize> queryList(Prize prize,int pageNum,int pageSize,String tableName){
		
		List<Prize> prizes = null;
		
		try {
			
			StringBuffer sbf = new StringBuffer();
			sbf.append(" userId=");
			sbf.append(prize.getUserId());
			sbf.append(" limit ");
			sbf.append((pageNum-1)*pageSize);
			sbf.append(",");
			sbf.append(pageSize);
			
			prizes = prizeMapper.queryList(tableName,sbf.toString());
			
		} catch (Exception e) {
			Log.run.error("querytList exception:"+e);
		}
		
		return prizes;
	}
	
	
}
