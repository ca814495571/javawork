package com.cqfc.management.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.RoleFunctionMapper;
import com.cqfc.management.model.RoleFunction;

@Repository
public class RoleFunctionDao {

	@Autowired
	private RoleFunctionMapper roleFunctionMapper; 
	
	
	
	public List<RoleFunction>  getUrlByRoleIdAndPath(int id , String path){
		
		return roleFunctionMapper.getUrlByRoleIdAndPath(id,path);
	}
	
}
