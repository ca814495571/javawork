package com.cqfc.partner.order.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.RecoveryIndexMapper;
import com.cqfc.protocol.partnerorder.RecoverOrderIndex;
import com.jami.util.Log;
@Repository
public class RecoveryIndexDao {

	
	@Autowired
	RecoveryIndexMapper recoveryIndexMapper;
	
	
	public RecoverOrderIndex getIndexByDbName(String dbName,String setNo){
		
		RecoverOrderIndex recoverOrderIndex = new RecoverOrderIndex();
		
		try {
			recoverOrderIndex =recoveryIndexMapper.getIndexByDbName(dbName,setNo);
		} catch (Exception e) {
			Log.run.error("获取订单索引表信息数据库异常",e);
			Log.error("获取订单索引表信息数据库异常",e);
		}
		return recoverOrderIndex;
	}
	
	
	public int updateFlag(String dbName,int flag){
		
		int b  = -1;
		try {
			b = recoveryIndexMapper.updateFlag(dbName, flag);
		} catch (Exception e) {
			Log.run.error("修改订单索引表标示符数据库异常", e);
			Log.error("修改订单索引表标示符数据库异常", e);
		}
		return b;
	}
}
