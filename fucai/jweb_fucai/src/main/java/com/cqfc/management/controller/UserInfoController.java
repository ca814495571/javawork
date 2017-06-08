package com.cqfc.management.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cqfc.management.model.UserInfo;
import com.cqfc.management.service.IUserInfoService;
import com.cqfc.util.dateUtils.DateUtils;


@Controller
@RequestMapping("/user")
public class UserInfoController {

	@Autowired
	IUserInfoService userInfoService;
	
	
	public void login(HttpServletRequest request ,String name ,String password){
		
		 name ="admin" ;
		 password = "admin";
		 HttpSession session = request.getSession();
		
		 UserInfo userInfo =  new UserInfo();
		
		 List<UserInfo> userInfos = new ArrayList<UserInfo>();
		 
		 boolean flag = false ;
		 
		 userInfo.setName(name);
		 
		 userInfos = userInfoService.getUserInfoByWhereAnd(userInfo);
		 
		 //系统存在该账户
		 if(userInfos.size() > 0){
			 
			 flag = true ;
			 
			 int loginFailCount = userInfos.get(0).getLoginFailCount();
			 Date date = new Date();
			 
			 //失败次数小于3
			 if(loginFailCount < 3){
				 
				 userInfo.setPassword(password);
				 
				 List<UserInfo> users = userInfoService.getUserInfoByWhereAnd(userInfo);
				 
				 //验证成功
				 if(users.size() > 0){
					    
					 flag = true ;
					 
					 userInfo = users.get(0);
					 userInfo.setLoginFailCount(0);
					 userInfo.setLoginTime(DateUtils.formatDateOne(date));
					 userInfoService.update(userInfo);
					 
				 }else{
					 
					 flag = false;
					 loginFailCount ++ ;
					 userInfo = userInfos.get(0);
					 userInfo.setLoginFailCount(loginFailCount) ;
					 userInfo.setLoginTime(DateUtils.formatDateOne(date));
					 if(loginFailCount == 3){
						 userInfo.setLoginTime(DateUtils.addDateHour(date, 3));
						 
					 }
					 
					 userInfoService.update(userInfo);
					 
				 }
			 }else{
				 
				  
				  String lastLoginTime = userInfos.get(0).getLastUpdateTime();
				  
				  if(DateUtils.compareDate(new Date(), DateUtils.stringToDateOne(lastLoginTime))){
					  
					  userInfo.setPassword(password);
						 
					  List<UserInfo> users = userInfoService.getUserInfoByWhereAnd(userInfo);
					  
					  if(users.size() > 0){
						    
							 flag = true ;
							 
							 userInfo = users.get(0);
							 userInfo.setLoginFailCount(0);
							 userInfo.setLoginTime(DateUtils.formatDateOne(date));
							 userInfoService.update(userInfo);
					  
					  }else {
						  
						  
						  	 flag = false;
							 userInfo = userInfos.get(0);
							 userInfo.setLoginFailCount(loginFailCount) ;
							 userInfo.setLoginTime(DateUtils.addDateHour(date, 3));
							 userInfoService.update(userInfo);
						  
					  }
					  
				  }else{
					  
					  flag = false;
				  }
				 
			 }
			 
		 }else{
			 
			 flag = false ;
		 }
		 
		 
		
	}
	
	
}
