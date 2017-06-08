package com.cqfc.management.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;


import com.cqfc.management.dao.mapper.FuserMapper;
import com.cqfc.management.model.Function;
import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.FuserAndRole;
import com.cqfc.management.model.OperateLog;
import com.cqfc.management.model.Role;
import com.cqfc.management.model.RoleFunction;
import com.cqfc.management.service.IFuserService;
import com.jami.util.Log;

@Repository
public class FuserDao {

	@Autowired
	FuserMapper fuserMapper;

	public int addFuser(Fuser fuser) {

		int flag = 0;
		try {

			if(StringUtils.isNotBlank(fuser.getPartnerId())){
				fuser.setPartnerId("0");
			}
			
			flag = fuserMapper.addFuser(fuser);

		} catch (Exception e) {

			if (e instanceof DuplicateKeyException) {
				flag = -100;
			} else {

				flag = -1;
			}
			Log.run.error("添加后台管理用户异常", e);
		}

		return flag;
	}

	public int updateFuserActive(Fuser fuser) {

		int flag = 0;
		int newActive = 1;
		try {
			if (fuser.getActive() == 1) {
				newActive = 2;
			} else if (fuser.getActive() == 2) {
				newActive = 1;
			}
			flag = fuserMapper.updateFuserActive(fuser, newActive);

		} catch (Exception e) {

			Log.run.error("刪除后台管理用户异常", e);
		}

		return flag;
	}

	public int updateFuser(Fuser fuser) {

		int flag = 0;
		try {

			flag = fuserMapper.updateFuser(fuser);

		} catch (Exception e) {

			if (e instanceof DuplicateKeyException) {

			} else {
				flag = -100;

			}
			Log.run.error("修改后台管理用户异常", e);
		}

		return flag;
	}

	public Fuser getFuser(Fuser fuser) {

		try {

			StringBuffer sbf = new StringBuffer();
			sbf.append(" 1 = 1");
			if (null != fuser.getFuserId() && !"".equals(fuser.getFuserId())) {

				sbf.append(" and fuserId = '");
				sbf.append(fuser.getFuserId());
				sbf.append("' ");

			}

			if (null != fuser.getName() && !"".equals(fuser.getName())) {

				sbf.append(" and name = '");
				sbf.append(fuser.getName());
				sbf.append("' ");

			}

			if (null != fuser.getPassword() && !"".equals(fuser.getPassword())) {

				sbf.append(" and password = '");
				sbf.append(fuser.getPassword());
				sbf.append("' ");

			}

			fuser = fuserMapper.getFuser(sbf.toString());

		} catch (Exception e) {

			Log.run.error("查询单个管理用户异常", e);
		}

		return fuser;

	}

	public List<Fuser> getFuserByWhere(Fuser fuser, int pageNum, int pageSize) {

		List<Fuser> fusers = new ArrayList<Fuser>();
		try {

			StringBuffer sbf = new StringBuffer();
			sbf.append(" 1 = 1 and fuserId !=");
			sbf.append(IFuserService.SYSTEM_ADMIN_FUSERID);
			if (null != fuser.getFuserId() && !"".equals(fuser.getFuserId())) {

				sbf.append(" and fuserId = '");
				sbf.append(fuser.getFuserId());
				sbf.append("' ");

			}

			if (null != fuser.getName() && !"".equals(fuser.getName())) {

				sbf.append(" and name like '%");
				sbf.append(fuser.getName());
				sbf.append("%' ");

			}

			if (pageNum < 1) {
				pageNum = 1;
			}

			if (pageSize > 500) {
				pageSize = 500;
			}

			sbf.append(" limit ");
			sbf.append(pageSize * (pageNum - 1));
			sbf.append(" ,");
			sbf.append(pageSize);
			fusers = fuserMapper.getFuserBywhere(sbf.toString());

		} catch (Exception e) {

			Log.run.error("分页查询后台用户异常", e);

		}

		return fusers;
	}

	public int getFuserSumByWhere(Fuser fuser) {

		int sum = 0;
		try {

			StringBuffer sbf = new StringBuffer();
			sbf.append(" 1 = 1 and fuserId !=");
			sbf.append(IFuserService.SYSTEM_ADMIN_FUSERID);

			if (null != fuser.getFuserId() && !"".equals(fuser.getFuserId())) {

				sbf.append(" and fuserId = '");
				sbf.append(fuser.getFuserId());
				sbf.append("' ");

			}

			if (null != fuser.getName() && !"".equals(fuser.getName())) {

				sbf.append(" and name = '");
				sbf.append(fuser.getName());
				sbf.append("' ");

			}

			sum = fuserMapper.getFuserSumBywhere(sbf.toString());

		} catch (Exception e) {

			Log.run.error("分页查询后台用户异常", e);
		}

		return sum;
	}

	public int addFuserAndRole(FuserAndRole fuserAndRole) {

		int flag = 0;
		try {

			flag = fuserMapper.addFuserAndRole(fuserAndRole);

		} catch (Exception e) {
			Log.run.error("添加用户角色异常", e);
		}

		return flag;
	}

	public int deleteFuserAndRole(int fuseId) {

		int flag = 0;
		try {

			flag = fuserMapper.deleteFuserAndRole(fuseId);

		} catch (Exception e) {
			flag = -100;
			Log.run.error("删除用户角色异常", e);
		}

		return flag;
	}

	public int deleteRoleAndFuser(int roleId) {
		int flag = 0;
		try {

			flag = fuserMapper.deleteRoleAndFuser(roleId);

		} catch (Exception e) {
			flag = -100;
			Log.run.error("删除用户角色异常", e);
		}

		return flag;
	}

	public List<FuserAndRole> getFuserAndRoles(int fuserId) {

		List<FuserAndRole> fuserAndRoles = new ArrayList<FuserAndRole>();

		try {
			fuserAndRoles = fuserMapper.getFuserAndRoles(fuserId);

		} catch (Exception e) {

			Log.run.error("获取用户的角色异常", e);

		}
		return fuserAndRoles;
	}

	public Role getRole(int roleId) {

		Role role = new Role();

		try {
			role = fuserMapper.getRole(roleId);
		} catch (Exception e) {
			Log.run.error("获取用户角色详细异常", e);
		}

		return role;

	}

	public List<Role> getRoles(Role role, int pageNum, int pageSize) {

		List<Role> roles = new ArrayList<Role>();

		StringBuffer sb = new StringBuffer();

		sb.append(" 1=1 and roleId !=");
		sb.append(IFuserService.SYSTEM_ADMIN_ROLEID);
		try {

			if (StringUtils.isNotBlank(role.getName())) {
				sb.append(" and name like '%");
				sb.append(role.getName());
				sb.append("%'");
			}

			sb.append(" limit ");
			sb.append((pageNum - 1) * pageSize);
			sb.append(",");
			sb.append(pageSize);
			roles = fuserMapper.getRoles(sb.toString());
		} catch (Exception e) {
			Log.run.error("分页获取角色列表", e);
		}
		return roles;
	}

	public int getRolesNum(Role role) {

		int totalNum = 0;

		StringBuffer sb = new StringBuffer();

		sb.append(" 1=1 and roleId !=");
		sb.append(IFuserService.SYSTEM_ADMIN_ROLEID);
		try {

			if (StringUtils.isNotBlank(role.getName())) {
				sb.append(" and name like '%");
				sb.append(role.getName());
				sb.append("%'");
			}

			totalNum = fuserMapper.getRolesNumBywhere(sb.toString());
		} catch (Exception e) {
			Log.run.error("分页获取角色列表", e);
		}
		return totalNum;
	}

	public int addRole(Role role) {

		int flag = 0;

		try {

			flag = fuserMapper.addRole(role);

		} catch (Exception e) {

			if (e instanceof DuplicateKeyException) {
				flag = -100;
			}

			Log.run.error("添加角色异常", e);
		}

		return flag;

	}

	public int updateRole(Role role) {
		int flag = 0;

		try {

			flag = fuserMapper.updateRole(role);

		} catch (Exception e) {
			Log.run.error("修改角色信息异常", e);
		}

		return flag;
	}

	public int deleteRole(int roleId) {
		int flag = 0;

		try {

			flag = fuserMapper.deleteRole(roleId);

		} catch (Exception e) {
			Log.run.error("删除角色信息异常", e);
		}

		return flag;
	}

	public int deleteRoleAndFunction(int roleId) {
		int flag = 0;

		try {

			flag = fuserMapper.deleteRoleAndFunction(roleId);

		} catch (Exception e) {
			flag = -100;
			Log.run.error("删除角色对应的菜单信息异常", e);
		}

		return flag;
	}

	public List<RoleFunction> getRoleFunctions(int roleId) {

		List<RoleFunction> roleFunctions = new ArrayList<RoleFunction>();

		try {
			roleFunctions = fuserMapper.getRoleFunctions(roleId);
		} catch (Exception e) {
			Log.run.error("获取角色url资源异常", e);
		}

		return roleFunctions;

	}

	public Function getFunction(int functionId) {

		Function fucntion = new Function();

		try {
			fucntion = fuserMapper.getFunction(functionId);
		} catch (Exception e) {
			Log.run.error("获取用户资源详细异常", e);
		}

		return fucntion;

	}

	public Function getParent(int parentId) {

		Function fucntion = new Function();

		try {
			fucntion = fuserMapper.getParent(parentId);
		} catch (Exception e) {
			Log.run.error("获取资源父节点异常", e);
		}

		return fucntion;

	}

	public List<Function> getChildren(int fucntionId, int type) {

		List<Function> fucntions = new ArrayList<Function>();

		try {
			fucntions = fuserMapper.getChildren(fucntionId, type);
		} catch (Exception e) {
			Log.run.error("获取资源子节点异常", e);
		}

		return fucntions;

	}

	public Function getFuntionByType(int funtionId, int type) {

		Function fucntion = new Function();

		try {
			fucntion = fuserMapper.getFuntionsByType(funtionId, type);
		} catch (Exception e) {
			Log.run.error("根据类型和主键查询资源异常", e);
		}

		return fucntion;
	}

	public int addRoleFunction(RoleFunction roleFunction) {

		int flag = 0;

		try {

			flag = fuserMapper.addRoloFunction(roleFunction);
		} catch (Exception e) {
			Log.run.error("添加角色功能异常", e);
		}

		return flag;

	}

	public int updateFuserPassword(Fuser fuser, String newPassword) {

		int flag = 0;

		try {

			flag = fuserMapper.updateFuserPassword(fuser, newPassword);

		} catch (Exception e) {
			Log.run.error("修改用户密码异常", e);
		}

		return flag;
	}

	public int resetFuserPassword(Fuser fuser) {

		int flag = 0;

		try {

			flag = fuserMapper.resetFuserPassword(fuser);

		} catch (Exception e) {
			Log.run.error("重置用户密码异常", e);
		}

		return flag;
	}

	public Function getFunctionByLevel(int functionId, int level) {

		Function function = new Function();

		try {
			function = fuserMapper.getFunctionByLevel(functionId, level);

		} catch (Exception e) {
			Log.run.error("根据级别和主键获取资源", e);
		}

		return function;

	}

	public int getNextId() {

		return fuserMapper.getCurrentInsertRoleId();
	}

	public int addOperateLog(OperateLog operateLog) {

		int flag = 0;

		try {

			flag = fuserMapper.addOperate(operateLog);

		} catch (Exception e) {

			Log.run.error("添加操作日志异常", e);
		}

		return flag;
	}

	public List<OperateLog> getOperateLogByWhere(OperateLog operateLog,
			String from, String to, int pageNum, int pageSize) {

		List<OperateLog> operateLogs = new ArrayList<OperateLog>();
		StringBuffer sb = new StringBuffer();

		try {

			sb.append(" 1=1 ");

			if (StringUtils.isNotBlank(operateLog.getOperator())) {

				sb.append(" and operator");
				sb.append(" like '%");
				sb.append(operateLog.getOperator());
				sb.append("%'");
			}

			if (StringUtils.isNotBlank(from)) {

				sb.append(" and operateTime >='");
				sb.append(from);
				sb.append(" 00:00:00'");
			}

			if (StringUtils.isNotBlank(to)) {

				sb.append(" and operateTime <='");
				sb.append(to);
				sb.append(" 23:59:59'");
			}

			if (operateLog.getType() != null) {

				sb.append(" and type='");
				sb.append(operateLog.getType());
				sb.append("'");
			}

			if (operateLog.getFuserId() != null) {

				sb.append(" and fuserId='");
				sb.append(operateLog.getFuserId());
				sb.append("'");
			}

				
			if(StringUtils.isNotBlank(operateLog.getAction())){
				
				sb.append(" and action like '%");
				sb.append(operateLog.getAction());
				sb.append("%'");
			}
			
			if(pageNum <0){
				pageNum = 1; 
			}
			if (pageSize > 50 || pageSize < 0) {
				pageSize = 50;
			}

			sb.append(" order by operateTime desc");

			sb.append(" limit ");
			sb.append((pageNum - 1) * pageSize);
			sb.append(" ,");
			sb.append(pageSize);

			operateLogs = fuserMapper.getOperateBywhere(sb.toString());

		} catch (Exception e) {

			Log.run.error("添加操作日志异常", e);
		}

		return operateLogs;
	}

	public int getOperateLogSumByWhere(OperateLog operateLog, String from,
			String to) {

		int sum = 0;
		StringBuffer sb = new StringBuffer();

		try {

			sb.append(" 1=1 ");

			if (StringUtils.isNotBlank(operateLog.getOperator())) {

				sb.append(" and operator");
				sb.append(" like '%");
				sb.append(operateLog.getOperator());
				sb.append("%'");
			}

			if (StringUtils.isNotBlank(from)) {

				sb.append(" and operateTime >='");
				sb.append(from);
				sb.append(" 00:00:00'");
			}

			if (StringUtils.isNotBlank(to)) {

				sb.append(" and operateTime <='");
				sb.append(to);
				sb.append(" 23:59:59'");
			}

			if (operateLog.getType() != null) {

				sb.append(" and type='");
				sb.append(operateLog.getType());
				sb.append("'");
			}

			if (operateLog.getFuserId() != null) {

				sb.append(" and fuserId='");
				sb.append(operateLog.getFuserId());
				sb.append("'");
			}
			sum = fuserMapper.getOperateSumBywhere(sb.toString());

		} catch (Exception e) {

			Log.run.error("获取日志总数", e);
		}

		return sum;
	}

	/**
	 * 删除当前时间前90天前的操作日志
	 * @param log
	 * @return
	 */
	public int deleteOperateLog() {
		int flag = 0;
		try {
			flag = fuserMapper.deleteOperateLog();
		} catch (Exception e) {
			Log.run.error("删除操作日志数据库异常", e);
			Log.error("删除操作日志数据库异常", e);
			flag = -100;
		}
		
		return flag;
	}
}
