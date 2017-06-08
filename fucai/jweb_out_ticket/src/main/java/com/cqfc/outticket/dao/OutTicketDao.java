package com.cqfc.outticket.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import com.cqfc.outticket.dao.mapper.OutTicketMapper;
import com.cqfc.outticket.model.OutTicket;
import com.cqfc.outticket.model.Statis;
import com.cqfc.outticket.util.DbGenerator;
import com.cqfc.util.DaoLevelException;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Repository
public class OutTicketDao {

	@Autowired
	OutTicketMapper outTicketMapper;
	
	/**
	 * 添加出票信息
	 * @param outTicket
	 * @return
	 * @throws DaoLevelException
	 */
	public int addOutTicket(OutTicket outTicket) throws DaoLevelException {
		int returnValue = ServiceStatusCodeUtil.STATUS_CODE_DB_ERROR;
		try {
			DbGenerator.setDynamicMasterSource();
			returnValue = outTicketMapper.addOutTicket(outTicket);
			if (returnValue == 0) {
				throw new DaoLevelException(String.valueOf(returnValue));
			}
		} catch (DuplicateKeyException e) {
			Log.run.error("t_out_ticket,orderNo=%s,errorMsg=%s", outTicket.getOrderNo(), e);
			throw new DaoLevelException(String.valueOf(ServiceStatusCodeUtil.STATUS_CODE_INSERT_ISEXIST));
		} catch (Exception e) {
			Log.run.error("t_out_ticket,orderNo=%s,errorMsg=%s",outTicket.getOrderNo(), e);
			throw new DaoLevelException(String.valueOf(returnValue));
		}
		return returnValue;
	}

	public OutTicket findOrderStaByOrderNo(String orderNo) {
		if (null != orderNo && !"".equals(orderNo)) {
			DbGenerator.setDynamicSlaveSource();
			return outTicketMapper.findOrderStaByOrderNo(orderNo);
		}
		return null;
	}
	
	/**
	 * 根据lotteryId和issueNo查询投注统计
	 */
	public Statis statisOrder(String lotteryId, String issueNo, String tableName) {
		StringBuffer conditions = new StringBuffer(" 1=1 ");
		Statis statis = new Statis();
		
		try {
			if (null != lotteryId && !"".equals(lotteryId)) {
				conditions.append(" and lotteryId ='" + lotteryId + "'");
			}
			if (null != issueNo && !"".equals(issueNo)) {
				conditions.append(" and issueNo ='" + issueNo + "'");
			}
			DbGenerator.setDynamicSlaveSource();
			statis = outTicketMapper.statisOrder(conditions.toString(),
					tableName);
		} catch (Exception e) {
			Log.run.error("countTotalSizeDao(exception=%s)", e);
			return statis;
		}

		return statis;
	}


}
