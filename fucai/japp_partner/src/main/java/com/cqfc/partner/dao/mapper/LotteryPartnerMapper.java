package com.cqfc.partner.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
public interface LotteryPartnerMapper extends BaseMapper {

	@Select("select * from t_lottery_partner where ${conditions}")
	public List<LotteryPartner> getLotteryPartnerList(@Param("conditions") String conditions);

	@Select("select count(*) from t_lottery_partner where ${conditions}")
	public int countTotalSize(@Param("conditions") String conditions);

	@Insert("insert into t_lottery_partner(partnerId,partnerName,partnerType,secretKey,state,minBalance,callbackUrl,ext,registrationTime,publicKey,aliasKey,keyStore)"
			+ "values (#{partnerId},#{partnerName},#{partnerType},#{secretKey},#{state},#{minBalance},#{callbackUrl},#{ext},now(),#{publicKey},#{aliasKey},#{keyStore})")
	public int addLotteryPartner(LotteryPartner lotteryPartner);

	@Select("select * from t_lottery_partner where partnerId=#{partnerId}")
	public LotteryPartner findLotteryPartnerById(@Param("partnerId") String partnerId);

	@Update("update t_lottery_partner set state=#{stateUpdate} where partnerId=#{partnerId} and state = #{state}")
	public int updateLotteryPartnerState(@Param("partnerId") String partnerId, @Param("state") int state,
			@Param("stateUpdate") int stateUpdate);

	@Select("select * from t_lottery_partner_ip where partnerId=#{partnerId}")
	public List<PartnerIpAddress> getPartnerIpAddressList(@Param("partnerId") String partnerId);

	@Insert("insert into t_lottery_partner_ip(ipAddress,partnerId) values(#{ipAddress},#{partnerId})")
	public int addPartnerIpAddress(PartnerIpAddress partnerIpAddress);

	@Update("update t_lottery_partner_ip set ipAddress=#{ipAddress},partnerId=#{partnerId} where id=#{id}")
	public int updatePartnerIpAddress(PartnerIpAddress partnerIpAddress);

	@Delete("delete from t_lottery_partner_ip where id=#{id}")
	public int deletePartnerIpAddressById(@Param("id") int id);

	@Select("select * from t_lottery_partner_ip where id=#{id}")
	public PartnerIpAddress findIpAddressById(@Param("id") int id);

	@Select("select * from t_lottery_partner_ip where ${conditions}")
	public PartnerIpAddress findPartnerIpAddress(@Param("conditions") String conditions);

	@Update("update t_lottery_partner set partnerId=#{param1.partnerId},partnerName=#{param1.partnerName},partnerType=#{param1.partnerType},"
			+ "secretKey=#{param1.secretKey},minBalance=#{param1.minBalance},callbackUrl=#{param1.callbackUrl},"
			+ "ext=#{param1.ext},publicKey=#{param1.publicKey},aliasKey=#{param1.aliasKey},keyStore=#{param1.keyStore} where partnerId=#{oldPartnerId}")
	public int updateLotteryPartner(LotteryPartner lotteryPartner, @Param("oldPartnerId") String oldPartnerId);

	@Delete("delete from t_lottery_partner_ip where partnerId=#{partnerId}")
	public int deleteIpAddressByPartnerId(@Param("partnerId") String partnerId);

	@Delete("delete from t_lottery_partner where partnerId=#{partnerId}")
	public int deletePartnerByPartnerId(@Param("partnerId") String partnerId);
}
