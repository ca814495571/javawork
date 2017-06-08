package com.cqfc.channel.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.channel.dao.mapper.ChannelCooperationMapper;
import com.cqfc.channel.protocol.ChannelCooperation;
import com.cqfc.channel.protocol.ReturnData;

@Repository
public class ChannelCooperationDao {

	@Autowired
	private ChannelCooperationMapper channelCooperationMapper;

	/**
	 * 获取合作渠道信息列表
	 * 
	 * @return
	 */
	public ReturnData getCooperateKeyList(ChannelCooperation channelCooperation, int currentPage, int pageSize) {
		ReturnData returnData = new ReturnData();
		String conditions = "1=1";

		//搜索参数
		if (null != channelCooperation) {
			if (null != channelCooperation.getChannelName() && !"".equals(channelCooperation.getChannelName())) {
				conditions += " and channelName='" + channelCooperation.getChannelName() + "'";
			}
		}

		int totalSize = countTotalSize();
		int totalPage = (int) Math.ceil((double) totalSize / (double) pageSize);
		if (totalPage >= currentPage) {
			conditions += " limit " + (currentPage - 1) * pageSize + "," + pageSize;
		}
		List<ChannelCooperation> list = channelCooperationMapper.getChannelCooperationList(conditions);

		returnData.setCurrentPage(currentPage);
		returnData.setPageSize(pageSize);
		returnData.setTotalSize(totalSize);
		returnData.setResultList(list);
		return returnData;
	}

	/**
	 * 新增合作渠道信息
	 * 
	 * @param channelCooperation
	 * @return
	 */
	public int addChannelCooperation(ChannelCooperation channelCooperation) {
		return channelCooperationMapper.addChannelCooperation(channelCooperation);
	}

	/**
	 * 计算总记录数
	 * 
	 * @return
	 */
	public int countTotalSize() {
		return channelCooperationMapper.countTotalSize();
	}

}
