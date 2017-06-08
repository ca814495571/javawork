package com.cqfc.management.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserInfo;

public interface IUserInfoService {

	public static final int CONST_ONE = 1;
	public static final int CONST_TWO = 2;
	public static final int CONST_THREE = 3;
	public static final int CONST_FOUR = 4;
	public static final String CONST_PASS = "Cq_Fucai_";

	public PcResultObj insert(UserInfo userInfo);

	public PcResultObj delete(int id);

	public PcResultObj getUserInfoById(UserInfo user, int id);

	public List<UserInfo> getUserInfoByWhereAnd(UserInfo userInfo);

	/**
	 * 判断用户是否成功登录 1 成功 2 用户不存在 3 密码错误 4 已锁住
	 * 
	 * @param name
	 * @param password
	 * @return
	 */
	public int login(HttpServletRequest request, String name, String password)
			throws Exception;

	/**
	 * 获取所有用户
	 * 
	 * @return
	 */
	public PcResultObj getAllUsers(String keyword , int pageNum , int pageSize);

	/**
	 * 修改帐号
	 * 
	 * @param id
	 * @param userInfo
	 * @return
	 */
	public PcResultObj update(UserInfo user, UserInfo userInfo);

	/**
	 * 验证访问user和路径是否有效
	 * 
	 * @param path
	 * @param userInfo
	 * @return
	 */
	public PcResultObj authenticate(HttpServletRequest request);

	/***
	 * 验证站点id是否是登录用户能访问的站点Id
	 * 
	 * @param userInfo
	 * @param id
	 * @return
	 */
	public PcResultObj checkStationId(UserInfo userInfo, int id);

	/**
	 * 验证分中心和站点修改/删除权限
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public PcResultObj checkUpdateAndDel(UserInfo user, int id);

	/**
	 * 验证用户id是否是自己的Id
	 * 
	 * @param userInfo
	 * @param id
	 * @return
	 */
	public PcResultObj checkUserId(UserInfo userInfo, int id);

	/**
	 * 验证是否是系统管理员
	 * 
	 * @param userInfo
	 * @return
	 */
	public PcResultObj ifSysAdmin(UserInfo userInfo);

	/**
	 * 验证是否能对分中心进行添加操作
	 * 
	 * @param userInfo
	 * @return
	 */
	public boolean ifInsertBranch(UserInfo userInfo);
	
	/**
	 * 验证是否能对站点进行添加操作
	 * 
	 * @param userInfo
	 * @return
	 */
	public boolean ifInsertBettingShop(UserInfo userInfo);

	/**
	 * 验证获取投注站的权限
	 * 
	 * @param user
	 * @param id
	 * @return
	 */
	public PcResultObj checkGetBettingShop(UserInfo user, int id);
	
	
	/**
	 * 修改密码验证密码的正确性
	 * @param user
	 * @return
	 */
	public PcResultObj checkPassword(UserInfo user ,UserInfo userInfo);
	
	
	
	/**
	 * 根据用户查询该拥挤所属的投注站
	 * @return
	 */
	public StationInfo getStationByUser(UserInfo user);
	
	
	
}
