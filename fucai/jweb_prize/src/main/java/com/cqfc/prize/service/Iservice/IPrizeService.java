package com.cqfc.prize.service.Iservice;

import java.util.List;

import com.cqfc.prize.model.Prize;

public interface IPrizeService {

	/**
	 * 兑奖信息入库
	 * @param prize
	 * @return
	 */
	public abstract boolean insert(Prize prize);
	
	//public abstract boolean delete(Prize prize);
	/**
	 * 修改兑奖信息
	 * @param prize
	 * @return
	 */
	public abstract boolean update(Prize prize);
	
	/**
	 * 查询单个兑奖信息
	 * @param prize
	 * @return
	 */
	public abstract Prize queryOne(Prize prize);
	
	
	/**
	 * 分页查询兑奖信息总记录数
	 * @param prize
	 * @return
	 */
	public abstract int querySum(Prize prize);
	/**
	 * 分页查询兑奖信息
	 * @param prize
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public abstract List<Prize> queryList(Prize prize,int pageNum,int pageSize);
	
	
	
}
