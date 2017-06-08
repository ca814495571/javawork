package com.cqfc.management.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.FtpFileLogMapper;
import com.cqfc.management.model.FtpFileLog;

@Repository
public class FtpFileLogDao {

	@Autowired
	private FtpFileLogMapper ftpFileLogMapper;
	
	
	public List<FtpFileLog> getAll(){
		
		return ftpFileLogMapper.getAll();
	}
	
	
	public int insert(FtpFileLog ftpFileLog){
		
		filterColumn(ftpFileLog);
		return ftpFileLogMapper.insert(ftpFileLog);
	}
	
	public int update(FtpFileLog ftpFileLog){
		
		filterColumn(ftpFileLog);
		return ftpFileLogMapper.update(ftpFileLog);
	}
	
	public List<FtpFileLog> getByFileName(String fileName){
		
		return ftpFileLogMapper.getByFileName(fileName);
	}
	
	public List<FtpFileLog> getByDownTime(String downTime){
		
		return ftpFileLogMapper.getByDownTime(downTime);
	}
	
	
	public List<FtpFileLog> getByFileNameAndFlag(String fileName ,int flag){
		
		return ftpFileLogMapper.getByFileNameAndFlag(fileName,flag);
	}
	
	
	public void filterColumn(FtpFileLog ftpFileLog){
		
		if(ftpFileLog.getFileName() == null){
		
			ftpFileLog.setFileName("");
		}
	}
}
