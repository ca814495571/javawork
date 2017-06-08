package com.cqfc.idgenerate.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.protocol.idgenerate.IdGenerate;
import com.jami.common.BaseMapper;

/**
 * @author liwh
 */
public interface IdGenerateMapper extends BaseMapper {

	/**
	 * 更新ID
	 * 
	 * @param idName
	 * @param currentId
	 * @return
	 */
	@Update("update t_lottery_id_generate set currentId=currentId+offset where idName=#{idName} and currentId=#{currentId}")
	public int updateId(@Param("idName") String idName, @Param("currentId") long currentId);

	/**
	 * 查询信息
	 * 
	 * @param idName
	 * @return
	 */
	@Select("select * from t_lottery_id_generate where idName=#{idName}")
	public List<IdGenerate> queryByCondition(@Param("idName") String idName);
}
