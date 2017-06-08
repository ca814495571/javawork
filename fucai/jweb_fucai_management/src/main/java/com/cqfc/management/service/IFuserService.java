package com.cqfc.management.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cqfc.management.model.Function;
import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.OperateLog;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.Role;

public interface IFuserService {

	public static final String PRE_PASS = "cq_lottery";
	public static final String CONST_PASS = "cq_pwd_";

	public static final String SESSION_USER = "fuser";
	public static final String COOKIE_KEY = "userId";
	
	public static final String ORIGINAL_PASS = "000000";
	
	public static final int CONST_ZERO = 0;
	
	public static final int CONST_ONE = 1;

	public static final int CONST_TWO = 2;

	public static final int CONST_THREE = 3;

	public static final int CONST_FOUR = 4;

	public static final int CONST_FIVE = 5;
	
	/**
	 * 系统管理员指定fuserId
	 */
	public static final Integer SYSTEM_ADMIN_FUSERID = CONST_ONE;
	/**
	 * 系统管理员指定roleId
	 */
	public static final Integer SYSTEM_ADMIN_ROLEID = CONST_ONE;
	// 用户登录常量 1 成功 2 用户不存在 3 密码错误 4 已锁住 5 验证码错误
	public static final int LOGIN_CODE_ONE = CONST_ONE;

	public static final int LOGIN_CODE_TWO = CONST_TWO;

	public static final int LOGIN_CODE_THREE = CONST_THREE;

	public static final int LOGIN_CODE_FOUR = CONST_FOUR;

	public static final int LOGIN_CODE_FIVE = CONST_FIVE;

	
	//菜单级别 1 一级菜单  2 二级菜单 0根节点
	
	public static final int FUNCTION_LEVEL_ZERO = CONST_ZERO;
	
	public static final int FUNCTION_LEVEL_ONE = CONST_ONE;
	
	public static final int FUNCTION_LEVEL_TWO = CONST_TWO;
	
	//资源类型 1 菜单   2 功能
	public static final int FUNCTION_TYPE_ONE = CONST_ONE;
	
	public static final int FUNCTION_TYPE_TWO = CONST_TWO;
	/**
	 * 登录失败最大次数常量设置
	 */
	public static final int LOGIN_MAX_NUM = CONST_FIVE;
	
	/**
	 * 后台操作日志类型 1 用户管理 2 彩票管理 3 财务管理 4系统管理
	 */
	public static final int OPERATE_USER_MANAGE  = CONST_ONE;
	
	public static final int OPERATE_LOTTERY_MANAGE = CONST_TWO;
	
	public static final int OPERATE_FINANCE_MANAGE = CONST_THREE;
	
	public static final int OPERATE_SYSTEM_MANAGE  = CONST_FOUR;
	
	

	public PcResultObj addFuser(Fuser fuser);

	public PcResultObj deleteFuser(Fuser fuser);

	public PcResultObj updateFuser(Fuser fuser);

	
	/**
	 * 根据用户Id查询
	 * 
	 * @param fuser
	 * @return
	 */
	public PcResultObj getFuserById(Fuser fuser);

	/**
	 * 根据调教分页查询用户信息
	 * 
	 * @param fuser
	 * @return
	 */
	public PcResultObj getFusers(Fuser fuser, int pageNum, int pageSize);

	/**
	 * 多条件查询单个用户信息
	 * 
	 * @param fuser
	 * @return
	 */
	public Fuser getFuser(Fuser fuser);
	
	
	
	
	/**
	 * 获取角色列表
	 * @param fuser
	 * @return
	 */
	public PcResultObj getRoles(Role role, int pageNum,int pageSize);
	
	
	/**
	 * 获取单个角色信息
	 * @param fuser
	 * @return
	 */
	public PcResultObj getRole(int roleId);
	
	
	
	/**
	 * 修改单个角色信息
	 * @param fuser
	 * @return
	 */
	public PcResultObj updateRole(Role role);
	
	/**
	 * 添加单个角色信息
	 * @param fuser
	 * @return
	 */
	public PcResultObj addRole(Role role);
	
	
	/**
	 * 给角色分配资源
	 * @param role
	 * @return
	 */
	public PcResultObj updateRoleAndFunction(Role role);
	/**
	 * 删除单个角色信息
	 * @param fuser
	 * @return
	 */
	public PcResultObj deleteRole(Role role);
	
	
	/**
	 * 获取所有菜单
	 * @param fuser
	 * @return
	 */
	public List<Function> getMenus();
	
	
	/**
	 * 获取所有菜单
	 * @param fuser
	 * @return
	 */
	public PcResultObj getMenuTree(Role role);
	
	
	/**
	 * 根据角色获取菜单
	 * @param fuser
	 * @return
	 */
	public PcResultObj getMenusByRole(HttpServletRequest request,String roleId);
	/**
	 * 鉴权
	 * @param fuser
	 * @return
	 */
	public boolean Authenticate(Fuser fuser);
	

	
	/**
	 * 是否系统管理员
	 */
	public boolean ifSystemAdmin(HttpServletRequest request);
	
	/**
	 * 判断用户是否成功登录 1 成功 2 用户不存在 3 密码错误 4 已锁住
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public int login(HttpServletRequest request, String name, String password,
			String code) throws Exception;

	/**
	 * 修改用户的密码
	 * 
	 * @return
	 */
	public PcResultObj updatePassword(HttpServletRequest request,Fuser fuser, String newPassword);
	
	
	
	/**
	 * 重置用户的密码
	 * 
	 * @return
	 */
	public PcResultObj resetPassword(HttpServletRequest request,Fuser fuser);
	
	
	
	/**
	 * 退出系统
	 * @param request
	 * @return
	 */
	public PcResultObj quit(HttpServletRequest request,HttpServletResponse response);
	
	
	
	
	/**
	 * 增加操作日志
	 * @param operateLog
	 * @return
	 */
	public int addOperateLog(HttpServletRequest request , String action ,int type);
	
	
	/**
	 * 分页查询操作日志
	 * @param operateLog
	 * @return
	 */
	public PcResultObj getOperateLogByWhere(OperateLog operateLog,String from,String to,int pageNum,int pageSize);
	
	/**
	 * 删除当前时间90天前的操作日志
	 * @return
	 */
	public int deleteOperate();
}
