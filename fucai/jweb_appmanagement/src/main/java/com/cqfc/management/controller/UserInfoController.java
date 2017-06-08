package com.cqfc.management.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.UserBaseInfo;
import com.cqfc.management.model.UserInfo;
import com.cqfc.management.service.IUserInfoService;

@Controller
@RequestMapping("/user")
public class UserInfoController {

	@Autowired
	IUserInfoService userInfoService;

	@RequestMapping(value = "/insert")
	@ResponseBody
	public PcResultObj insert(HttpServletRequest request, UserInfo userInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		pcResultObj = userInfoService.ifSysAdmin(user);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		return userInfoService.insert(userInfo);
	}

	@RequestMapping(value = "/lists.json")
	@ResponseBody
	public PcResultObj getAllUser(HttpServletRequest request,String keyword ,int pageNum ,int pageSize) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}
		
		if (pageNum < 1) {
			pageNum = 1;
		}

		if (pageSize > 200) {
			pageSize = 15;
		}
		

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		pcResultObj = userInfoService.ifSysAdmin(user);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		return userInfoService.getAllUsers(keyword,pageNum , pageSize);
	}

	@RequestMapping(value = "/getUserInfo.json")
	@ResponseBody
	public PcResultObj getUserInfo(HttpServletRequest request, UserInfo userInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		pcResultObj = userInfoService.checkUserId(user, userInfo.getId());

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;
		}

		return userInfoService.getUserInfoById(user, userInfo.getId());
	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public PcResultObj update(HttpServletRequest request, UserInfo userInfo, String newPassword) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}
		
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		pcResultObj = userInfoService.checkUserId(user, userInfo.getId());

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;
		}
		
		if(userInfo.getPassword() != null && !"".equals(userInfo.getPassword())){
			
			pcResultObj = userInfoService.checkPassword(user, userInfo);
			if ((Integer) pcResultObj.getEntity() == 2) {

				return pcResultObj;
			}
			
			
		}
		if(newPassword != null && !"".equals(newPassword)){
			
			userInfo.setPassword(newPassword);
			
		}
	
		return userInfoService.update(user, userInfo);

	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public PcResultObj delete(HttpServletRequest request, UserInfo userInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		pcResultObj = userInfoService.ifSysAdmin(user);
		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		return userInfoService.delete(userInfo.getId());
	}

	@RequestMapping(value = "/login")
	@ResponseBody
	public PcResultObj login(HttpServletRequest request, String name,
			String password) throws Exception {

		PcResultObj pcResultObj = new PcResultObj();

		int flag = userInfoService.login(request, name, password);
		HttpSession session = request.getSession();
		if (flag == 1) {

			UserInfo userInfo = (UserInfo) session.getAttribute("user");

			UserBaseInfo user = new UserBaseInfo();
			user.setName(userInfo.getName());
			user.setRoleId(userInfo.getRoleId());
			user.setCode(userInfo.getStationCode());
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
		}

		return pcResultObj;
	}

}
