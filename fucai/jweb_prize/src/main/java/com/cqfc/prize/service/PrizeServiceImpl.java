package com.cqfc.prize.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.prize.dao.PrizeDao;
import com.cqfc.prize.model.Prize;
import com.cqfc.prize.service.Iservice.IPrizeService;
import com.cqfc.prize.util.DataSourceContextHolder;
import com.cqfc.prize.util.DataSourceUtil;
import com.jami.util.Log;
@Service
public class PrizeServiceImpl implements IPrizeService{

	
	@Autowired
	PrizeDao prizeDao;
	
	
	@Override
	public boolean insert(Prize prize) {
		
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.getDbName(prize.getUserId()));
		
		int flag = prizeDao.insert(prize,DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId()));
		
		if(flag!=0){
			
			
			DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.DB_NAME_TEMP_PRIZE);
			
			prize.setMessage(prize.getMessage()+"||"+DataSourceUtil.getDbName(prize.getUserId())+":"+DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId()));
			flag = prizeDao.insert(prize,DataSourceUtil.TABLE_NAME_TEMP_PRIZE);
			
			if(flag==0){
				Log.run.error("兑奖结果入临时表异常："+prize.toString());
				Log.error(prize.toString());
				return false;
			}
			
			return true;
		}else{
			Log.run.error("兑奖结果入库异常："+DataSourceUtil.getDbName(prize.getUserId())+"--"+DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId())+"--"+prize.toString());
			Log.error(prize.toString());
			return false;
		}
		
	}

	@Override
	public boolean update(Prize prize) {
		
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.getDbName(prize.getUserId()));
		
		int flag = prizeDao.update(prize,DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId()));
		
		if(flag == 1){
			
			return true;
		}
		
		return false;
	}

	@Override
	public Prize queryOne(Prize prize) {
		
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.getDbName(prize.getUserId()));
//		DataSourceContextHolder.setDataSourceType(DataSourceUtil.SLAVE+DataSourceUtil.getDbName(prize.getUserId()));
		return prizeDao.queryOne(prize, DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId()));
	}

	
	@Override
	public List<Prize> queryList(Prize prize, int pageNum, int pageSize) {
		
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.getDbName(prize.getUserId()));
		return prizeDao.queryList(prize, pageNum, pageSize,  DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId()));
	}

	@Override
	public int querySum(Prize prize) {
		// TODO Auto-generated method stub
		DataSourceContextHolder.setDataSourceType(DataSourceUtil.MASTER+DataSourceUtil.getDbName(prize.getUserId()));
		return prizeDao.queryForSum(prize,DataSourceUtil.getTableName(DataSourceUtil.TABLE_NAME_PRIZE, prize.getUserId()));
	}

	
}
