package com.cqfc.chupiao.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cqfc.protocol.chupiao.Chupiao;
import com.jami.common.BaseMapper;

/**
 * @author zwt
 */
public interface ChupiaoMapper extends BaseMapper {

	@Insert("insert into t_chu_piao (orderNo,reasult)"
			+ "values (#{orderNo},#{reasult})")
	public int addChupiao(Chupiao chupiao);

	@Select("select count(*) from t_chu_piao where orderNo=#{orderNo}")
	public int findChupiaoByOrderNo(@Param("orderNo") String orderNo);

}
