package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.cqfc.common.dao.BaseMapper;
import com.cqfc.management.model.RoleFunction;

public interface RoleFunctionMapper extends BaseMapper{

	
	@Select("select *  from t_role_function where roleId = #{0} and url = #{1}")
	public List<RoleFunction> getUrlByRoleIdAndPath(int id ,String path); 
	
}
