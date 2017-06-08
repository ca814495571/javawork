package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.FtpFileLog;

public interface FtpFileLogMapper extends BaseMapper{

	
	@Select("select * from t_ftp_file_log")
	public List<FtpFileLog> getAll();
	
	@Insert("insert into t_ftp_file_log (fileName,createTime,downTime) "
			+ "values (#{fileName},now(),#{downTime})")
	public int insert(FtpFileLog ftpFileLog);
	
	@Update("update t_ftp_file_log set flag =#{flag} where fileName = #{fileName}")
	public int update(FtpFileLog ftpFileLog);
	
	@Select("select *  from t_ftp_file_log where fileName = #{0}")
	public List<FtpFileLog> getByFileName(String fileName);
	
	@Select("select *  from t_ftp_file_log where downTime = #{0}")
	public List<FtpFileLog> getByDownTime(String downTime);
	
	@Select("select *  from t_ftp_file_log where fileName = #{0} and flag = #{1}")
	public List<FtpFileLog> getByFileNameAndFlag(String fileName , int flag);
}
