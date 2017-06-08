package com.cqfc.channel.servece.impl;

import javax.annotation.Resource;

import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import com.cqfc.channel.dao.ChannelCooperationDao;
import com.cqfc.channel.protocol.ChannelCooperation;
import com.cqfc.channel.protocol.ChannelCooperationService;
import com.cqfc.channel.protocol.ReturnData;

@Service("channelCooperationService")
public class ChannelCooperationServiceImpl implements ChannelCooperationService.Iface {

	@Resource
	ChannelCooperationDao channelCooperationDao;

	@Override
	public ReturnData getChannelCooperationList(ChannelCooperation channelCooperation,
			int currentPage, int pageSize) throws TException {
		return channelCooperationDao.getCooperateKeyList(channelCooperation, currentPage, pageSize);
	}

	@Override
	public boolean addChannelCooperation(ChannelCooperation channelCooperation) throws TException {
		boolean flag = false;
		int isSuccess = channelCooperationDao.addChannelCooperation(channelCooperation);
		if (isSuccess > 0) {
			flag = true;
		}
		return flag;
	}

}
