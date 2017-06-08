package com.cqfc.channel.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.channel.protocol.ChannelCooperation;
import com.jami.common.BaseMapper;

public interface ChannelCooperationMapper extends BaseMapper {

	@Select("select * from t_channels_cooperation where ${conditions}")
	public List<ChannelCooperation> getChannelCooperationList(@Param("conditions") String conditions);

	@Select("select count(*) from t_channels_cooperation")
	public int countTotalSize();

	@Insert("insert into t_channels_cooperation "
			+ "(channelName,channelKey,status,balanceAlarm,accountType,accountBalance,minBalanceAlarm,extension3,registTime,lastUpdateTime,channelID,channelAccountID,credit,remark,password)"
			+ "values (#{channelName},#{channelKey},#{status},#{balanceAlarm},#{accountType},#{accountBalance},#{minBalanceAlarm},#{extension3},now(),now(),#{channelID},#{channelAccountID},#{credit},#{remark},#{password})")
	public int addChannelCooperation(ChannelCooperation channelCooperation);
}
