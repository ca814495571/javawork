package com.cqfc.user.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.cqfc.user.order.dao.mapper.RecoveryIndexMapper;
import com.jami.util.Log;
@Repository
public class RecoveryIndexDao {

	
	@Autowired
	RecoveryIndexMapper recoveryIndexMapper;
	
	
	public RecoverOrderIndex getIndexByDbName(String dbName){
		
		RecoverOrderIndex recoverOrderIndex = null;
		try {
			
			recoverOrderIndex = recoveryIndexMapper.getIndexByDbName(dbName);
		} catch (Exception e) {
			Log.run.error("获取同步订单索引表异常",e);
			Log.error("获取同步订单索引表异常",e);
		}
		
		return recoverOrderIndex;
	}
	
	
	public int updateFlag(String dbName,int flag){
		
		return recoveryIndexMapper.updateFlag(dbName, flag);
	}
}
