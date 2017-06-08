package com.cqfc.chupiao.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.chupiao.dao.mapper.ChupiaoMapper;
import com.cqfc.protocol.chupiao.Chupiao;
import com.cqfc.util.ServiceStatusCodeUtil;

/**
 * @author liwh
 */
@Repository
public class ChupiaoDao {

	@Autowired
	private ChupiaoMapper chupiaoMapper;

	
	public int addChupiao(Chupiao chupiao) {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			returnValue = chupiaoMapper.addChupiao(chupiao);
		} catch (DuplicateKeyException e) {
			return ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST;
		} catch (Exception e) {
			return returnValue;
		}
		return returnValue;
	}

	/**
	 * 查询出票结果信息
	 * 
	 * @param lotteryId
	 * @return
	 */
	public int findChupiaoByOrderNo(String orderNo) {
		int returnValue = 0;
		returnValue = chupiaoMapper.findChupiaoByOrderNo(orderNo);
		return returnValue;
	}

	

}
