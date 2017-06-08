package com.cqfc.management.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cqfc.management.dao.FuserDao;
import com.cqfc.management.model.Function;
import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.FuserAndRole;
import com.cqfc.management.model.Menu;
import com.cqfc.management.model.MenuTree;
import com.cqfc.management.model.OperateLog;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.model.Role;
import com.cqfc.management.model.RoleFunction;
import com.cqfc.management.model.RoleMenus;
import com.cqfc.management.model.UserForView;
import com.cqfc.management.service.IFuserService;
import com.cqfc.management.util.dateUtils.DateUtils;
import com.cqfc.management.util.md5Utils.MdFiveUtils;
import com.cqfc.util.DateUtil;
import com.google.code.kaptcha.Constants;
import com.jami.util.Log;

@Service
public class FuserService implements IFuserService {

	@Autowired
	FuserDao fuserDao;

	@Transactional
	@Override
	public PcResultObj addFuser(Fuser fuser) {
		int flag = 0;
		PcResultObj pcResultObj = new PcResultObj();

		fuser.setPassword(MdFiveUtils.getMD5(CONST_PASS+MdFiveUtils.getMD5(PRE_PASS+ORIGINAL_PASS)));
		flag = fuserDao.addFuser(fuser);

		if (flag == 1) {

			// 添加用户的角色
			List<Role> roles = fuser.getRoles();

			FuserAndRole fuserAndRole = null;
			if(roles!=null&& roles.size()>0){
				
				for (Role role : roles) {

					fuserAndRole = new FuserAndRole();
					fuserAndRole.setFuserId(fuserDao.getNextId());
					fuserAndRole.setRoleId(role.getRoleId());
					int ifsuc = fuserDao.addFuserAndRole(fuserAndRole);
					if (ifsuc != 1) {
						TransactionAspectSupport.currentTransactionStatus()
								.setRollbackOnly();
						pcResultObj.setMsg("用户添加失败!");
						pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
						return pcResultObj;
					}
				}
			}

			pcResultObj.setMsg("用户添加成功!");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);

		} else if (flag == -100) {

			pcResultObj.setMsg("用户名称重复!");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		} else {
			pcResultObj.setMsg("用户添加失败!");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj deleteFuser(Fuser fuser) {
		int flag = 0;
		PcResultObj pcResultObj = new PcResultObj();
		flag = fuserDao.updateFuserActive(fuser);

		if (flag == 1) {

//			int ifsuc = fuserDao.deleteFuserAndRole(fuser.getFuserId());
//
//			if (ifsuc < 0) {
//				TransactionAspectSupport.currentTransactionStatus()
//						.setRollbackOnly();
//				pcResultObj.setMsg("用户删除失败!");
//				pcResultObj.setMsgCode("2");
//				return pcResultObj;
//			}

			pcResultObj.setMsg("操作成功!");
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);

		} else {

			pcResultObj.setMsg("操作失败!");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}

		return pcResultObj;
	}

	
	@Transactional
	@Override
	public PcResultObj updateFuser(Fuser fuser) {
		int flag = 0;
		PcResultObj pcResultObj = new PcResultObj();

		Fuser old = new Fuser();
		
		if(fuser.getFuserId()==null){
			
			pcResultObj.setMsg("用户id不存在!");
			pcResultObj.setMsgCode("2");
			return pcResultObj;
		}
		
		old.setFuserId(fuser.getFuserId());
		old = fuserDao.getFuser(old);

		if (old == null) {

			pcResultObj.setMsg("用户不存在!");
			pcResultObj.setMsgCode("2");
			return pcResultObj;
		}

		if (StringUtils.isNotBlank(fuser.getName())) {

			old.setName(fuser.getName());

		}

		if (StringUtils.isNotBlank(fuser.getPassword())) {

			old.setPassword(fuser.getPassword());
		}

		if (fuser.getLoginFailCount()!=null && fuser.getLoginFailCount()!=0) {

			old.setLoginFailCount(fuser.getLoginFailCount());

		}

		if(StringUtils.isBlank(old.getLoginTime())){
			old.setLoginTime("0000-00-00 00:00:00");
		}
		
		if (StringUtils.isNotBlank(fuser.getLoginTime())) {

			old.setLoginTime(fuser.getLoginTime());
		}

		if (StringUtils.isNotBlank(fuser.getPartnerId())) {

			old.setPartnerId(fuser.getPartnerId());
		}
		
		flag = fuserDao.updateFuser(old);

		if (flag != -100) {

			int ifsusc = fuserDao.deleteFuserAndRole(fuser.getFuserId());

			if (ifsusc != -100) {
				List<Role> roles = fuser.getRoles();
				FuserAndRole fuserAndRole = null;

				if(roles!=null){
					for (Role role : roles) {
						fuserAndRole = new FuserAndRole();

						fuserAndRole.setFuserId(fuser.getFuserId());
						fuserAndRole.setRoleId(role.getRoleId());
						ifsusc = fuserDao.addFuserAndRole(fuserAndRole);

						if (ifsusc != 1) {

							TransactionAspectSupport.currentTransactionStatus()
									.setRollbackOnly();
							pcResultObj.setMsg("用户修改失败!");
							pcResultObj.setMsgCode("2");
							return pcResultObj;
						}
					}
				}

			} else {

				TransactionAspectSupport.currentTransactionStatus()
						.setRollbackOnly();
				pcResultObj.setMsg("用户修改失败!");
				pcResultObj.setMsgCode("2");
				return pcResultObj;
			}
			pcResultObj.setMsg("用户修改成功!");
			pcResultObj.setMsgCode("1");

		} else {

			pcResultObj.setMsg("用户修改失败");
			pcResultObj.setMsgCode("2");
		}

		return pcResultObj;
	}

	@Override
	public Fuser getFuser(Fuser fuser) {

		fuser = fuserDao.getFuser(fuser);
		if (fuser != null) {

			List<Role> roles = new ArrayList<Role>();
			List<FuserAndRole> fuserAndRoles = fuserDao.getFuserAndRoles(fuser
					.getFuserId());
			for (FuserAndRole fuserAndRole : fuserAndRoles) {

				roles.add(fuserDao.getRole(fuserAndRole.getRoleId()));
			}

		}

		return fuser;
	}

	/**
	 * 1 成功 2 用户不存在 3 密码错误 4 已锁住 5 验证码错误
	 * 
	 * @throws ParseException
	 */
	@Override
	public int login(HttpServletRequest request, String name, String password,
			String code) throws Exception {

		 String generateCode = (String) request.getSession()
		 .getAttribute(Constants.KAPTCHA_SESSION_KEY);
		
		 if( generateCode==null||(generateCode!=null &&
		 !generateCode.equals(code)) ){
			 	return IFuserService.LOGIN_CODE_FIVE;
		 }

		Fuser fuser = new Fuser();
		int flag = 0;
		fuser.setName(name);
		fuser = fuserDao.getFuser(fuser);
		// 系统存在该账户
		if (fuser != null) {

			int loginFailCount = fuser.getLoginFailCount();
			Date date = new Date();

			// 失败次数小于3
			if (loginFailCount < IFuserService.LOGIN_MAX_NUM) {

				// 验证密码成功
				if (MdFiveUtils.getMD5(CONST_PASS + password).equals(
						fuser.getPassword())) {

					flag = IFuserService.LOGIN_CODE_ONE;

					fuser.setLoginFailCount(0);
					fuser.setLoginTime(DateUtils.formatDateOne(date));
					fuserDao.updateFuser(fuser);

					// 获取用户的角色
					initRoleAndResources(fuser);
					request.getSession().setAttribute(SESSION_USER, fuser);
					request.getSession().setMaxInactiveInterval(7200);//设置session过期时间为2个小时
				} else {
					// 密码错误
					flag = IFuserService.LOGIN_CODE_THREE;

					if (DateUtils.ifPreDay(DateUtils.stringToDateOne(fuser
							.getLoginTime()))) {

						loginFailCount = 0;
					}

					loginFailCount++;
					fuser.setLoginFailCount(loginFailCount);
					fuser.setLoginTime(DateUtils.formatDateOne(date));
					if (loginFailCount == IFuserService.LOGIN_MAX_NUM) {
						fuser.setLoginTime(DateUtils.addDateHour(date, 3));

					}

					fuserDao.updateFuser(fuser);

				}
			} else {

				String lastLoginTime = fuser.getLoginTime();

				if (DateUtils.compareDate(new Date(),
						DateUtils.stringToDateOne(lastLoginTime))) {

					if (MdFiveUtils.getMD5(CONST_PASS + password).equals(
							fuser.getPassword())) {

						flag = IFuserService.LOGIN_CODE_ONE;

						fuser.setLoginFailCount(0);
						fuser.setLoginTime(DateUtils.formatDateOne(date));
						fuserDao.updateFuser(fuser);

						// 获取角色
						initRoleAndResources(fuser);
						request.getSession().setAttribute(SESSION_USER, fuser);

					} else {

						flag = IFuserService.LOGIN_CODE_THREE; // 密码错误

						loginFailCount = 1;
						fuser.setLoginFailCount(loginFailCount);
						fuser.setLoginTime(DateUtils.formatDateOne(date));
						fuserDao.updateFuser(fuser);

					}

				} else {

					flag = IFuserService.LOGIN_CODE_FOUR; // 登陆失败3次后,被锁住了
				}

			}

		} else {

			flag = IFuserService.LOGIN_CODE_TWO;
		}

		return flag;
	}

	public void initRoleAndResources(Fuser fuser) {
		// 获取 用户绑定的角色
		List<FuserAndRole> fuserAndRoles = fuserDao.getFuserAndRoles(fuser
				.getFuserId());

		Set<String> urls = new HashSet<String>();
		List<RoleFunction> roleFunctions = new ArrayList<RoleFunction>();

		List<Role> roles = new ArrayList<Role>();

		List<Function> functions = new ArrayList<Function>();
		for (FuserAndRole fuserAndRole : fuserAndRoles) {

			// 获取角色具体信息
			roles.add(fuserDao.getRole(fuserAndRole.getRoleId()));
			// 获取角色绑定的资源
			roleFunctions = fuserDao.getRoleFunctions(fuserAndRole.getRoleId());
			
			if(roleFunctions!=null){
				
				for (RoleFunction roleFunction : roleFunctions) {

					functions = fuserDao.getChildren(roleFunction.getFunctionId(),IFuserService.FUNCTION_TYPE_TWO);
					for (Function functionTemp : functions) {
						urls.add(functionTemp.getUrl());
					}

				}
			}
			
		}
		fuser.setRoles(roles);
		fuser.setResources(urls);
	}

	
	
	
	@Override
	public PcResultObj getFuserById(Fuser fuser) {

		PcResultObj pcResultObj = new PcResultObj();

		if (fuser.getFuserId() != null && !"".equals(fuser.getFuserId())) {

			fuser = fuserDao.getFuser(fuser);
			if(fuser==null){
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("用户查询失败,请检查用户是否冻结");
				return pcResultObj;
			}
			
			List<FuserAndRole> fuserAndRoles = fuserDao.getFuserAndRoles(fuser
					.getFuserId());

			Set<Integer> roleIds = new HashSet<Integer>();
			
			
			for (FuserAndRole fuserAndRole : fuserAndRoles) {

				roleIds.add(fuserAndRole.getRoleId());
			}
			
			
			List<Role> allRoles = fuserDao.getRoles(new Role(), 1, 10000);
			RoleMenus roleMenus = new RoleMenus();
			RoleMenus roleMenusTemp = new RoleMenus();
			List<RoleMenus> roleMenusList = new ArrayList<RoleMenus>();
			roleMenus.setName(fuser.getName());
			roleMenus.setPartnerId(fuser.getPartnerId());
			for (Role role : allRoles) {
				roleMenusTemp = new RoleMenus();
				
				if(roleIds.contains(role.getRoleId())){
					
					roleMenusTemp.setChecked(true);
				}else{
					roleMenusTemp.setChecked(false);
				}
				roleMenusTemp.setName(role.getName());
				roleMenusTemp.setRemark(role.getRemark());
				roleMenusTemp.setRoleId(role.getRoleId());
				roleMenusList.add(roleMenusTemp);
			}
			roleMenus.setRoleMenus(roleMenusList);
			pcResultObj.setEntity(roleMenus);
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("查询成功");
		} else {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("用户id为空,查询失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj getFusers(Fuser fuser, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		List<Fuser> fusers = fuserDao.getFuserByWhere(fuser, pageNum, pageSize);

		int totalNum = fuserDao.getFuserSumByWhere(fuser);

		UserForView userForView = null;
		
		List<UserForView> forViews = new ArrayList<UserForView>();
		if(fusers!=null){
			
			for (Fuser fuserTemp:fusers) {
				
				userForView = new UserForView();
				userForView.setUserName(fuserTemp.getName());
				userForView.setActive(fuserTemp.getActive());
				userForView.setFuserId(fuserTemp.getFuserId());
				userForView.setCreateTime(DateUtil.getSubstringDateTime(fuserTemp.getCreateTime()));
				forViews.add(userForView);
			}
		}
		
		ResultObj resultObj = new ResultObj();

		resultObj.setObjects(forViews);
		resultObj.setRecordTotal(totalNum);
		pcResultObj.setMsg("查询成功");
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setEntity(resultObj);
		return pcResultObj;
	}

	@Override
	public PcResultObj updatePassword(HttpServletRequest request,Fuser fuser, String newPassword) {

		PcResultObj pcResultObj = new PcResultObj();
		Fuser sessionFuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		
		fuser.setFuserId(sessionFuser.getFuserId());
		fuser.setPassword(MdFiveUtils.getMD5(CONST_PASS+fuser.getPassword()));

		int flag = fuserDao.updateFuserPassword(fuser,
				MdFiveUtils.getMD5(IFuserService.CONST_PASS + newPassword));

		if (flag == 1) {

			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("修改成功");
		} else {
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("原密码错误");
		}

		return pcResultObj;

	}

	
	@Override
	public PcResultObj resetPassword(HttpServletRequest request, Fuser fuser) {
		PcResultObj pcResultObj = new PcResultObj();
		
		if(ifSystemAdmin(request)){
			fuser.setPassword(MdFiveUtils.getMD5(CONST_PASS+MdFiveUtils.getMD5(PRE_PASS+ORIGINAL_PASS)));
			int flag = fuserDao.resetFuserPassword(fuser);
			
			if(flag == 1){
				pcResultObj.setMsg("密码重置成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			}else{
				pcResultObj.setMsg("密码重置失败");
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			}
			
		}else{
			pcResultObj.setMsg("权限不足");
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
		}
		
		
		return pcResultObj;
	}
	
	
	@Override
	public PcResultObj quit(HttpServletRequest request,
			HttpServletResponse response) {
		PcResultObj pcResultObj = new PcResultObj();
		try {
			
			
			HttpSession session = request.getSession();
			
			if(session!=null){
				request.getSession().removeAttribute(IFuserService.SESSION_USER);
				Cookie[] cookies = request.getCookies();
				if(cookies!=null){
					for (Cookie cookie : cookies) {
						cookie.setMaxAge(0);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				
			}
		} catch (Exception e) {

			Log.run.error("退出异常", e);
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("系统异常");
			return pcResultObj;
		}

		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setMsg("成功退出");
		return pcResultObj;
	}

	@Override
	public boolean Authenticate(Fuser fuser) {
		return false;
	}

//	@Override
//	@Transactional
//	public PcResultObj addRole(Role role) {
////functions[0].functionId
//		PcResultObj pcResultObj = new PcResultObj();
//
//		if (!StringUtils.isNotBlank(role.getName())) {
//
//			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
//			pcResultObj.setMsg("角色名不能为空");
//			return pcResultObj;
//		}
//		
//		if(!StringUtils.isNotBlank(role.getRemark())){
//			role.setRemark("");
//		}
//
//		int flag = fuserDao.addRole(role);
//
//		RoleFunction roleFunction = null;
//		if (flag == 1) {
//			List<Function> functions = role.getFunctions();
//			if(functions!=null){
//				for (Function function : functions) {
//					roleFunction = new RoleFunction();
//					roleFunction.setRoleId(fuserDao.getNextId());
//					roleFunction.setFunctionId(function.getFunctionId());
//					flag = fuserDao.addRoleFunction(roleFunction);
//					
//					if(flag !=1){
//						pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
//						pcResultObj.setMsg("添加角色失败");
//						TransactionAspectSupport.currentTransactionStatus()
//						.setRollbackOnly();
//						return pcResultObj;
//					}
//				}
//			}
//
//			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
//			pcResultObj.setMsg("添加角色成功");
//		} else if(flag == -100){
//			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
//			pcResultObj.setMsg("角色名称重复");
//		}else{
//			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
//			pcResultObj.setMsg("添加角色失败");
//			
//		}
//
//		return pcResultObj;
//	}
	
	
	
	@Override
	public PcResultObj addRole(Role role) {
//functions[0].functionId
		PcResultObj pcResultObj = new PcResultObj();

		if (!StringUtils.isNotBlank(role.getName())) {

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("角色名不能为空");
			return pcResultObj;
		}
		
		if(!StringUtils.isNotBlank(role.getRemark())){
			role.setRemark("");
		}

		int flag = fuserDao.addRole(role);

		if (flag == 1) {
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("添加角色成功");
		} else if(flag == -100){
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("角色名称重复");
		}else{
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("添加角色失败");
			
		}

		return pcResultObj;
	}
	
	
	@Override
	@Transactional
	public PcResultObj updateRoleAndFunction(Role role) {
		
		PcResultObj pcResultObj =new PcResultObj();
		RoleFunction roleFunction = null;
		int flag = 0;
		if(role.getRoleId()==0){
			
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("角色Id为空");
			return pcResultObj;
		}
		List<Function> functions = role.getFunctions();
		
		flag = fuserDao.deleteRoleAndFunction(role.getRoleId());
		
		if(flag == -100){
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("删除角色资源异常");
			TransactionAspectSupport.currentTransactionStatus()
			.setRollbackOnly();
			return pcResultObj;
		}
		
		
		if(functions!=null&&functions.size()>0){
			Function functionTemp =null;
			for (Function function : functions) {
				roleFunction = new RoleFunction();
				roleFunction.setRoleId(role.getRoleId());
				
				functionTemp = fuserDao.getFuntionByType(function.getFunctionId(), IFuserService.FUNCTION_TYPE_ONE);
				if(functionTemp == null){
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					pcResultObj.setMsg("添加的资源不存在");
					TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
					return pcResultObj;
				}
				roleFunction.setFunctionId(function.getFunctionId());
				
				flag = fuserDao.addRoleFunction(roleFunction);
				
				if(flag !=1){
					pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
					pcResultObj.setMsg("角色添加资源失败");
					TransactionAspectSupport.currentTransactionStatus()
					.setRollbackOnly();
					return pcResultObj;
				}
			}
		}
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setMsg("分配角色成功");
		
		return pcResultObj;
	}
	

	@Override
	public PcResultObj getRoles(Role role, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();

		List<Role> roles = fuserDao.getRoles(role, pageNum, pageSize);
		for(Role r:roles){
			r.setCreateTime(DateUtil.getSubstringDateTime(r.getCreateTime()));
			r.setLastUpdateTime(DateUtil.getSubstringDateTime(r.getLastUpdateTime()));
		}

		int total = fuserDao.getRolesNum(role);

		ResultObj resultObj = new ResultObj();
		resultObj.setObjects(roles);
		resultObj.setRecordTotal(total);
		pcResultObj.setEntity(resultObj);
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setMsg("查询成功");

		return pcResultObj;
	}

	@Override
	public PcResultObj getRole(int roleId) {

		PcResultObj pcResultObj = new PcResultObj();

		List<Function> functions = new ArrayList<Function>();
		Function function = new Function();
		List<RoleFunction> roleFunctions = fuserDao.getRoleFunctions(roleId);
		Role role = fuserDao.getRole(roleId);

		if (role != null) {
			for (RoleFunction roleFunction : roleFunctions) {

				// 获取具体的资源信息
				function = fuserDao.getFunction(roleFunction.getFunctionId());
				functions.add(function);
				// 获取资源的父节点信息
			}

			role.setFunctions(functions);
			pcResultObj.setEntity(role);
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("查询成功");
		} else {
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("查询失败");

		}

		return pcResultObj;
	}

	@Override
	public PcResultObj updateRole(Role role) {

		PcResultObj pcResultObj = new PcResultObj();

		
		if(!StringUtils.isNotBlank(role.getName())){

			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("角色名不能为空");
			return pcResultObj;
		}
		if(!StringUtils.isNotBlank(role.getRemark())){
			role.setRemark("");
		}
		
		int flag = fuserDao.updateRole(role);
//		RoleFunction roleFunction = null;

		if (flag == 1) {

//			fuserDao.deleteRoleAndFunction(role.getRoleId());
//			List<Function> functions = role.getFunctions();
//			if(functions!=null){
//			for (Function function : functions) {
//				roleFunction = new RoleFunction();
//				roleFunction.setRoleId(role.getRoleId());
//				roleFunction.setFunctionId(function.getFunctionId());
//				fuserDao.addRoleFunction(roleFunction);
//			}}
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("修改角色成功");
		} else {
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("修改角色失败");
		}

		return pcResultObj;
	}

	@Override
	@Transactional
	public PcResultObj deleteRole(Role role) {

		PcResultObj pcResultObj = new PcResultObj();

		int flag = fuserDao.deleteRole(role.getRoleId());

		if (flag == 1) {

			flag = fuserDao.deleteRoleAndFunction(role.getRoleId());

			if(flag == -100){
				
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("删除角色资源时异常");
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return pcResultObj;
			}
			flag = fuserDao.deleteRoleAndFuser(role.getRoleId());
			
			if(flag == -100){
				
				pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
				pcResultObj.setMsg("删除用户角色时异常");
				TransactionAspectSupport.currentTransactionStatus()
				.setRollbackOnly();
				return pcResultObj;
			}
			
			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("删除角色成功");

		} else {

			pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
			pcResultObj.setMsg("删除角色失败");
		}

		return pcResultObj;
	}

	@Override
	public List<Function> getMenus() {
		
		//获取一级菜单
		List<Function> functions = fuserDao.getChildren(IFuserService.FUNCTION_LEVEL_ZERO,IFuserService.FUNCTION_TYPE_ONE);
		
		List<Function> children =  new ArrayList<Function>();
		for (Function function :functions) {
			//获取二级菜单
			children = fuserDao.getChildren(function.getFunctionId(),IFuserService.FUNCTION_TYPE_ONE);
			function.setChildren(children);
			
		}
		return functions;
	}

	@Override
	public PcResultObj getMenusByRole(HttpServletRequest request ,String roleId) {
		
		PcResultObj pcResultObj = new  PcResultObj();
		
		List<Menu> menus = new ArrayList<Menu>();
		
		Fuser fuser =(Fuser)request.getSession().getAttribute(IFuserService.SESSION_USER);
		boolean flag = false;
		
		if(fuser==null||roleId==null){
			pcResultObj.setMsg("登陆超时");
			pcResultObj.setMsgCode(PcResultObj.NO_RIGHT_CODE);
			return pcResultObj;
		}else{
			
			String [] roleIds = roleId.split("@");
			List<Role> roles = fuser.getRoles();
			
			for (Role role :roles) {
				
				for (int i = 0; i < roleIds.length; i++) {
					
					if(String.valueOf(role.getRoleId()).equals(roleIds[i])){
						
						flag = true;
						break;
					}
				}
			}
			
			if(flag){
				
				Set<Integer> functionIds = new HashSet<Integer>();
				List<RoleFunction> roleFunctions =  new ArrayList<RoleFunction>();
				
				for (Role role :roles) {
					
					roleFunctions =fuserDao.getRoleFunctions(role.getRoleId());
					
					for (RoleFunction roleFunction: roleFunctions) {
						
						functionIds.add(roleFunction.getFunctionId());
					}
				}
				
				
				Menu menu = null;
				List<Function> functions = getMenus();
				List<Function> childFunctions = new ArrayList<Function>();
				for (Function function :functions) {
					
					childFunctions = function.getChildren();
					for (Function childFunction:childFunctions) {
						
						  if(functionIds.contains(childFunction.getFunctionId())){
							  
							  menu =new Menu();
							  menu.setSecond(childFunction.getRemark());
							  menu.setRole(0);
							  menu.setUrl(childFunction.getUrl());
							  menu.setFirst(function.getRemark());
							  menus.add(menu);
						  }
					}
					
				}
				
				pcResultObj.setEntity(menus);
				pcResultObj.setMsg("查询成功");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				
			}else{
				
				pcResultObj.setMsg("用户无任何权限");
				pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
				return pcResultObj;
			}
			
			
		}
		
		return pcResultObj;
	}

	@Override
	public PcResultObj getMenuTree(Role role) {
		PcResultObj pcResultObj = new PcResultObj();
		
		role = fuserDao.getRole(role.getRoleId());
		
		if(role == null){
			
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("角色不存在");
			return pcResultObj;
		}
		MenuTree menuTree = null;
		List<MenuTree> menuTrees = new ArrayList<MenuTree>();
		List<Function> functions = getMenus();
		List<Function> children = new ArrayList<Function>();
		
		Set<Integer> set  = new  HashSet<Integer>();
		int partnerId = 0;
		try {
			List<RoleFunction> roleFunctions =fuserDao.getRoleFunctions(role.getRoleId());
			for (RoleFunction roleFunction : roleFunctions) {
				set.add(roleFunction.getFunctionId());
				partnerId = roleFunction.getFunctionId();
				while(partnerId!=IFuserService.FUNCTION_LEVEL_ZERO){
					
					partnerId = fuserDao.getFuntionByType(partnerId, FUNCTION_TYPE_ONE).getParentId();
					set.add(partnerId);
					
				}
			}
			
			for (Function parent :functions) {
				
				menuTree = new MenuTree();
				
				menuTree.setpId(parent.getParentId());
				menuTree.setId(parent.getFunctionId());
				menuTree.setName(parent.getRemark());
				
				if(set.contains(parent.getFunctionId())){
					menuTree.setChecked(true);
					menuTree.setOpen(true);
				}else{
					menuTree.setChecked(false);
					menuTree.setOpen(false);
				}
				menuTrees.add(menuTree);
				children = parent.getChildren();
				for (Function child : children) {
					
					menuTree = new MenuTree();
					
					menuTree.setpId(child.getParentId());
					menuTree.setId(child.getFunctionId());
					menuTree.setName(child.getRemark());
					if(set.contains(child.getFunctionId())){
						menuTree.setChecked(true);
						menuTree.setOpen(true);
					}else{
						menuTree.setChecked(false);
						menuTree.setOpen(false);
					}
					menuTrees.add(menuTree);
					
				}
			}
		} catch (Exception e) {
			Log.run.error("获取资源异常", e);
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("获取资源成功");
			return pcResultObj;
		}
		
		
		pcResultObj.setEntity(menuTrees);
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setMsg("获取资源成功");
		
		return pcResultObj;
	}

	
	
	public boolean ifSystemAdmin(HttpServletRequest request){
		Fuser fuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		List<FuserAndRole> fuserAndRoles = fuserDao.getFuserAndRoles(fuser.getFuserId());
		
		Set<Integer> set = new HashSet<Integer>();
		
		for (FuserAndRole fuserAndRole :fuserAndRoles) {
			
			set.add(fuserAndRole.getRoleId());
		}
		
		 if(set.contains(IFuserService.SYSTEM_ADMIN_ROLEID)){
			 return true;
		 }else{
			 return false;
		 }
		
	}

	@Override
	public int addOperateLog(HttpServletRequest request , String action ,int type) {
		int flag = 0;
		OperateLog operateLog = new OperateLog();
		
		Fuser operator = (Fuser)request.getSession().getAttribute(IFuserService.SESSION_USER);
		
		if(StringUtils.isBlank(operator.getName())){
			operateLog.setOperator("");
		}else{
			operateLog.setOperator(operator.getName());
		}
		
		
		if(operator.getFuserId()!=null){
			operateLog.setFuserId(operator.getFuserId());
		}else{
			operateLog.setFuserId(0);
		}
		
		
		if(StringUtils.isBlank(action)){
			operateLog.setAction("");
		}else{
			operateLog.setAction(action);
		}
		
		operateLog.setType(type);
		flag = fuserDao.addOperateLog(operateLog);
		
		return flag;
	}

	@Override
	public  PcResultObj getOperateLogByWhere(OperateLog operateLog,String from ,String to, int pageNum,
			int pageSize) {
		PcResultObj pcResultObj = new PcResultObj();
		
		List<OperateLog> operateLogs =new ArrayList<OperateLog>();
		
		
		operateLogs = fuserDao.getOperateLogByWhere(operateLog, from, to, pageNum, pageSize);
		for(OperateLog o :operateLogs){
			o.setOperateTime(DateUtil.getSubstringDateTime(o.getOperateTime()));
		}
		
		int sum = fuserDao.getOperateLogSumByWhere(operateLog, from, to);
		
		ResultObj resultObj = new ResultObj();
		resultObj.setObjects(operateLogs);
		resultObj.setRecordTotal(sum);
		pcResultObj.setEntity(resultObj);
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		pcResultObj.setMsg("查询成功");
		return pcResultObj;
	}

	/**
	 * 删除当前时间90天前的操作日志
	 */
	@Override
	public int deleteOperate() {
		int flag = 0;
		
		flag = fuserDao.deleteOperateLog();
		
		return flag;
	}


	
}
