package com.cqfc.management.service;

import com.cqfc.management.model.PcResultObj;

public interface ICqUserService {

	
	/**
	 * 获取福彩中心的账户金额
	 * @return
	 */
	public PcResultObj getCqUserAccountInfo(String paramType,String value);
	
}
