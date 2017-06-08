package com.cqfc.management.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.OperateLog;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.Role;
import com.cqfc.management.model.UserForView;
import com.cqfc.management.service.IFuserService;
import com.jami.util.Log;

@Controller
@RequestMapping("/fuser")
public class FuserController {

	
	private static final int DEFAUT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 100;
	@Autowired
	IFuserService fuserService;

	@RequestMapping("/add")
	@ResponseBody
	public PcResultObj addUser(HttpServletRequest request ,Fuser fuser) {
		
		PcResultObj pcResultObj = fuserService.addFuser(fuser);
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "添加后台管理用户,"+fuser.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "添加后台管理用户,"+fuser.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;
	}

	@RequestMapping("/update")
	@ResponseBody
	public PcResultObj updateUser(HttpServletRequest request,Fuser fuser) {
		
		
		PcResultObj pcResultObj = fuserService.updateFuser(fuser);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "修改后台管理用户信息,"+fuser.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "修改后台管理用户信息,"+fuser.toString()+",操作失败"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;
		

	}

	@RequestMapping("/delete")
	@ResponseBody
	public PcResultObj deleteUser(HttpServletRequest request,Fuser fuser) {
		PcResultObj pcResultObj = new PcResultObj();
		fuserService.addOperateLog(request, "冻结后台管理用户信息"+fuser.toString(),IFuserService.OPERATE_SYSTEM_MANAGE);
		
		Fuser fuserSession = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		
		
		if(fuserSession.getFuserId()==fuser.getFuserId()){
			
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("权限不足");
			return pcResultObj;
		}
		
		pcResultObj = fuserService.deleteFuser(fuser);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "冻结后台管理用户信息,"+fuser.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "冻结后台管理用户信息,"+fuser.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		
		return pcResultObj;

	}

	@RequestMapping("/getFuserById")
	@ResponseBody
	public PcResultObj getFuserById(Fuser fuser) {

		return fuserService.getFuserById(fuser);

	}

	@RequestMapping("/getFusers")
	@ResponseBody
	public PcResultObj getFusers(Fuser fuser, Integer pageNum, Integer pageSize) {

		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		
		return fuserService.getFusers(fuser, pageNum, pageSize);

	}

	@RequestMapping("/updatePass")
	@ResponseBody
	public PcResultObj updatePass(HttpServletRequest request, Fuser fuser, String newPassword) {
		
		PcResultObj pcResultObj = fuserService.updatePassword(request,fuser, newPassword);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "修改后台用户密码,"+fuser.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "修改后台用户密码,"+fuser.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;

	}
	
	
	
	@RequestMapping("/resetPass")
	@ResponseBody
	public PcResultObj resetPassword(HttpServletRequest request, Fuser fuser) {
		
		
		PcResultObj pcResultObj = fuserService.resetPassword(request,fuser);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "重置后台管理用户密码,"+fuser.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "重置后台管理用户密码,"+fuser.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;

	}
	
	
	
	@RequestMapping("/quit")
	@ResponseBody
	public PcResultObj quit(HttpServletRequest request  , HttpServletResponse response) {
		return  fuserService.quit(request,response);
	}
	
	

	/**
	 * 根据角色获取菜单
	 * @return
	 */
	@RequestMapping("/getMenusByRole")
	@ResponseBody
	public PcResultObj getMenusByRole(HttpServletRequest request ,String roleId) {
		
		
		return  fuserService.getMenusByRole(request,roleId);
	}
	
	
	
	/**
	 * 获取所有的的菜单
	 * @return
	 */
	@RequestMapping("/getMenuTree")
	@ResponseBody
	public PcResultObj getMenuTree(Role role) {
		
		return  fuserService.getMenuTree(role);
	}
	
	@RequestMapping("/addRole")
	@ResponseBody
	public PcResultObj addRole(HttpServletRequest request,Role role) {
		
		PcResultObj pcResultObj = fuserService.addRole(role);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "添加后台角色,"+role.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "添加后台角色,"+role.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;
		
	}
	
	
	@RequestMapping("/updateRoleAndFunction")
	@ResponseBody
	public PcResultObj updateRoleAndFunction(HttpServletRequest request,Role role) {
		
		
		PcResultObj pcResultObj = fuserService.updateRoleAndFunction(role);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "分配角色资源,"+role.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "分配角色资源,"+role.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;
		
	}
	
	@RequestMapping("/getRoles")
	@ResponseBody
	public PcResultObj getRoles(Role role,Integer pageNum,Integer pageSize) {
		
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		
		return  fuserService.getRoles(role, pageNum, pageSize);
	}
	
	
	@RequestMapping("/getRoleById")
	@ResponseBody
	public PcResultObj getRole(Role role) {
		
		
		return  fuserService.getRole(role.getRoleId());
	}
	
	
	@RequestMapping("/updateRole")
	@ResponseBody
	public PcResultObj updateRole(HttpServletRequest request,Role role) {
		
		PcResultObj pcResultObj = fuserService.updateRole(role);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "修改角色信息,"+role.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "修改角色信息,"+role.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;
		
	}
	
	@RequestMapping("/deleteRole")
	@ResponseBody
	public PcResultObj deleteRole(HttpServletRequest request,Role role) {
		
		PcResultObj pcResultObj = fuserService.deleteRole(role);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "删除角色信息,"+role.toString()+",操作成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}else{
			fuserService.addOperateLog(request, "删除角色信息,"+role.toString()+",操作失败:"+pcResultObj.getMsg(),IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		return pcResultObj;
		
	}
	
	
	@RequestMapping("/getOperateLogs")
	@ResponseBody
	public PcResultObj getOperateLogs(OperateLog operateLog,String from ,String to ,Integer pageNum , Integer pageSize) {
		
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}		
		return  fuserService.getOperateLogByWhere(operateLog, from, to, pageNum, pageSize);
	}
	
	
	
	@RequestMapping("/login")
	@ResponseBody
	public PcResultObj login(HttpServletRequest request,
			HttpServletResponse response, String name, String password,
			String code) {

		PcResultObj pcResultObj = new PcResultObj();
		int flag = 0;
		try {
				
			flag = fuserService.login(request, name, password, code);				

		} catch (Exception e) {
			Log.run.error("用户登录异常", e);
			pcResultObj.setMsg("系统错误");
			pcResultObj.setMsgCode("2");
			return pcResultObj;

		}
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(2*60*60);
		Fuser fuser = null;
		if (flag == 1) {
			
			fuser = (Fuser) session.getAttribute("fuser");

			UserForView user = new UserForView();
			user.setUserName(fuser.getName());
			user.setRoles(fuser.getRoles());
			user.setFuserId(fuser.getFuserId());
			String roleId = "";
			
			List<Role> roles = user.getRoles();
			for (Role role :roles) {
				roleId += role.getRoleId()+"@";
			}
			if(roleId.length()>0){
				
				roleId = roleId.substring(0, roleId.lastIndexOf("@"));
			}
			Cookie cookie = new Cookie(IFuserService.COOKIE_KEY, "userId="
					+ String.valueOf(fuser.getFuserId()) + ",userName="
					+ fuser.getName()+",roleId="+roleId);
			cookie.setMaxAge(3600 * 2);
			cookie.setSecure(false);
			cookie.setPath("/");
			response.addCookie(cookie);
			pcResultObj.setEntity(user);
			pcResultObj.setMsg("成功");
			pcResultObj.setMsgCode("1");
		} else if (flag == 2) {

			pcResultObj.setMsg("该账户不存在");
			pcResultObj.setMsgCode("2");
		} else if (flag == 3) {

			pcResultObj.setMsg("密码错误");
			pcResultObj.setMsgCode("2");
		} else if (flag == 4) {

			pcResultObj.setMsg("该用户已被锁住");
			pcResultObj.setMsgCode("2");
		} else if (flag == 5) {
			pcResultObj.setMsg("验证码错误");
			pcResultObj.setMsgCode("2");
		}

		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, fuser.toString()+"登陆系统成功",IFuserService.OPERATE_SYSTEM_MANAGE);
		}
		
		return pcResultObj;

	}
	
	
	
	

}
