package com.cqfc.management.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.RoleFunctionDao;
import com.cqfc.management.dao.StationInfoDao;
import com.cqfc.management.dao.UserInfoDao;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.model.RoleFunction;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserBaseInfo;
import com.cqfc.management.model.UserInfo;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IUserInfoService;
import com.cqfc.management.util.dateUtils.DateUtils;
import com.cqfc.management.util.md5Utils.MdFiveUtils;

@Service
public class UserInfoServiceImpl implements IUserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;

	@Autowired
	private RoleFunctionDao roleFunctionDao;

	@Autowired
	private StationInfoDao stationInfoDAO;

	@Autowired
	private IStationInfoService stationInfoservice;

	@Override
	public PcResultObj insert(UserInfo userInfo) {

		PcResultObj pcEntityObj = new PcResultObj();

		userInfo.setPassword(MdFiveUtils.getMD5(CONST_PASS
				+ userInfo.getPassword()));
		int a = 0;

		StationInfo si = new StationInfo();

		if (userInfo.getRoleId() == 4) {

			si.setStationCode(userInfo.getStationCode());

			List<StationInfo> stationInfos = stationInfoservice
					.getStationByWhereAnd(si);

			if (stationInfos.size() < 1) {

				pcEntityObj.setEntity("");
				pcEntityObj.setMsgCode("2");
				pcEntityObj.setMsg("站号不存在");

				return pcEntityObj;

			}

		} else if (userInfo.getRoleId() == 3) {

			si.setStationName(userInfo.getStationCode());

			List<StationInfo> stationInfos = stationInfoservice
					.getStationByWhereAnd(si);

			if (stationInfos.size() < 1) {

				pcEntityObj.setEntity("");
				pcEntityObj.setMsgCode("2");
				pcEntityObj.setMsg("名称不存在");

				return pcEntityObj;

			}

		} else if (userInfo.getRoleId() == 1 || userInfo.getRoleId() == 2) {

			userInfo.setStationCode("000000");
		}

		try {

			a = userInfoDao.insert(userInfo);

		} catch (Exception e) {

			pcEntityObj.setEntity("");
			pcEntityObj.setMsgCode("2");
			pcEntityObj.setMsg("用户名已存在");
			return pcEntityObj;
		}
		if (a == 1) {

			pcEntityObj.setEntity("");
			pcEntityObj.setMsgCode("1");
			pcEntityObj.setMsg("添加成功");
		} else {

			pcEntityObj.setEntity("");
			pcEntityObj.setMsgCode("2");
			pcEntityObj.setMsg("添加失败");

		}

		return pcEntityObj;
	}

	@Override
	public PcResultObj getUserInfoById(UserInfo user, int id) {

		PcResultObj pcResultObj = new PcResultObj();

		if (id == 0) {
			id = user.getId();
		}

		UserInfo userInfo = userInfoDao.getUserInfoById(id);

		if (userInfo == null) {
			pcResultObj.setEntity(2);
			pcResultObj.setMsg("查询失败");
			pcResultObj.setMsgCode("2");

		} else {

			UserBaseInfo userBaseInfo = new UserBaseInfo();

			userBaseInfo.setId(userInfo.getId());
			userBaseInfo.setName(userInfo.getName());
			userBaseInfo.setRoleId(userInfo.getRoleId());
			userBaseInfo.setCode(userInfo.getStationCode());
			pcResultObj.setEntity(userBaseInfo);
			pcResultObj.setMsg("查询成功");
			pcResultObj.setMsgCode("1");
		}

		return pcResultObj;
	}

	@Override
	public List<UserInfo> getUserInfoByWhereAnd(UserInfo userInfo) {

		return userInfoDao.getUserInfoByWhereAnd(userInfo);
	}

	/**
	 * 1 成功 2 用户不存在 3 密码错误 4 已锁住
	 * 
	 * @throws ParseException
	 * @throws Exception
	 */
	@Override
	public int login(HttpServletRequest request, String name, String password)
			throws ParseException {

		UserInfo userInfo = new UserInfo();

		List<UserInfo> userInfos = new ArrayList<UserInfo>();

		int flag = 0;

		userInfo.setName(name);

		userInfos = userInfoDao.getUserInfoByWhereAnd(userInfo);

		// 系统存在该账户
		if (userInfos.size() > 0) {

			int loginFailCount = userInfos.get(0).getLoginFailCount();
			Date date = new Date();

			// 失败次数小于3
			if (loginFailCount < 3) {

				userInfo.setPassword(MdFiveUtils.getMD5(CONST_PASS + password));
				List<UserInfo> users = userInfoDao
						.getUserInfoByWhereAnd(userInfo);

				// 验证成功
				if (users.size() > 0) {

					flag = 1;

					userInfo = users.get(0);
					userInfo.setLoginFailCount(0);
					userInfo.setLoginTime(DateUtils.formatDateOne(date));
					userInfoDao.update(userInfo);
					request.getSession().setAttribute("user", userInfo);

				} else {

					flag = 3;
					userInfo = userInfos.get(0);

					if (DateUtils.ifPreDay(DateUtils.stringToDateOne(userInfo
							.getLoginTime()))) {

						loginFailCount = 0;
					}

					loginFailCount++;
					userInfo.setLoginFailCount(loginFailCount);
					userInfo.setLoginTime(DateUtils.formatDateOne(date));
					if (loginFailCount == 3) {
						userInfo.setLoginTime(DateUtils.addDateHour(date, 3));

					}

					userInfoDao.update(userInfo);

				}
			} else {

				String lastLoginTime = userInfos.get(0).getLoginTime();

				if (DateUtils.compareDate(new Date(),
						DateUtils.stringToDateOne(lastLoginTime))) {

					userInfo.setPassword(MdFiveUtils.getMD5(CONST_PASS
							+ password));

					List<UserInfo> users = userInfoDao
							.getUserInfoByWhereAnd(userInfo);

					if (users.size() > 0) {

						flag = 1;

						userInfo = users.get(0);
						userInfo.setLoginFailCount(0);
						userInfo.setLoginTime(DateUtils.formatDateOne(date));
						userInfoDao.update(userInfo);
						request.getSession().setAttribute("user", userInfo);

					} else {

						flag = 3;

					}

				} else {

					flag = 4;
				}

			}

		} else {

			flag = 2;
		}

		return flag;
	}

	@Override
	public PcResultObj getAllUsers(String keyword, int pageNum, int pageSize) {

		PcResultObj pcResultObj = new PcResultObj();
		ResultObj resultObj = new ResultObj();
		List<UserInfo> userInfos = userInfoDao.getUserInfoByWhere(keyword,
				pageNum, pageSize);
		UserInfo userInfo = new UserInfo();

		List<UserBaseInfo> userBaseInfos = new ArrayList<UserBaseInfo>();

		for (int i = 0; i < userInfos.size(); i++) {

			UserBaseInfo user = new UserBaseInfo();
			userInfo = userInfos.get(i);
			user.setRoleId(userInfo.getRoleId());
			user.setName(userInfo.getName());
			user.setCode(userInfo.getStationCode());
			user.setId(userInfo.getId());

			userBaseInfos.add(user);
		}

		resultObj.setRecordTotal(userInfoDao.getAllUserNum());

		if (resultObj.getRecordTotal() > 0) {
			resultObj.setObjects(userBaseInfos);
			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("查询成功");
		} else {

			pcResultObj.setEntity(null);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("查询失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj update(UserInfo user, UserInfo userInfo) {

		PcResultObj pcResultObj = new PcResultObj();

		int a = 0;

		int id = userInfo.getId();

		if (id == 0) {

			id = user.getId();

		}

		user = userInfoDao.getUserInfoById(id);

		if (user == null) {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("修改的用户不存在");

			return pcResultObj;
		} else {

			if (!"".equals(userInfo.getName()) && userInfo.getName() != null) {

				user.setName(userInfo.getName());

			}
			if (!"".equals(userInfo.getRoleId()) && userInfo.getName() != null
					&& userInfo.getRoleId() != 0) {

				user.setRoleId(userInfo.getRoleId());
			}
			if (!"".equals(userInfo.getStationCode())
					&& userInfo.getStationCode() != null) {

				user.setStationCode(userInfo.getStationCode());
			}
			if (!"".equals(userInfo.getPassword())
					&& userInfo.getPassword() != null) {

				user.setPassword(MdFiveUtils.getMD5(CONST_PASS
						+ userInfo.getPassword()));
			}

			StationInfo si = new StationInfo();

			if (user.getRoleId() == 4) {

				si.setStationCode(user.getStationCode());
				si.setStationFlag(IStationInfoService.BETTINGSHOP_FLAG);
				List<StationInfo> stationInfos = stationInfoservice
						.getStationByWhereAnd(si);

				if (stationInfos.size() < 1) {

					pcResultObj.setEntity("");
					pcResultObj.setMsgCode("2");
					pcResultObj.setMsg("站号不存在");

					return pcResultObj;

				}

			} else if (user.getRoleId() == 3) {

				si.setStationName(user.getStationCode());
				si.setStationFlag(IStationInfoService.BRANCH_FLAG);
				List<StationInfo> stationInfos = stationInfoservice
						.getStationByWhereAnd(si);

				if (stationInfos.size() < 1) {

					pcResultObj.setEntity("");
					pcResultObj.setMsgCode("2");
					pcResultObj.setMsg("名称不存在");

					return pcResultObj;

				}
			} else if (user.getRoleId() == 1 || user.getRoleId() == 2) {

				user.setStationCode("000000");
			}

			try {

				a = userInfoDao.update(user);

			} catch (Exception e) {

				pcResultObj.setEntity("");
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("用户名已存在");
				return pcResultObj;
			}

		}

		if (a == 1) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("修改成功");

		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("修改失败");
		}
		return pcResultObj;
	}

	@Override
	public PcResultObj delete(int id) {
		PcResultObj pcResultObj = new PcResultObj();
		UserInfo user = userInfoDao.getUserInfoById(id);

		int a = 0;
		if (user == null) {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("删除的用户不存在");

			return pcResultObj;

		} else {

			user.setActive(2);
			a = userInfoDao.update(user);

		}

		if (a == 1) {

			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("成功");

		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("修改失败");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj authenticate(HttpServletRequest request) {

		PcResultObj pcResultObj = new PcResultObj();
		String path = request.getRequestURI().substring(
				request.getContextPath().length());

		UserInfo userInfo = (UserInfo) request.getSession()
				.getAttribute("user");
		boolean flag = false;

		if (userInfo != null) {
 
			List<RoleFunction> roleFunctions = roleFunctionDao
					.getUrlByRoleIdAndPath(userInfo.getRoleId(), path);

			if (roleFunctions.size() > 0) {

				flag = true;
			}
		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("3");
			pcResultObj.setMsg("用户没登录!");
			return pcResultObj;
		}

		if (flag == false) {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("没有权限访问!");

		} else {

			pcResultObj.setEntity(1);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("有权限访问!");

		}

		return pcResultObj;
	}

	@Override
	public PcResultObj checkStationId(UserInfo user, int id) {

		PcResultObj pcResultObj = new PcResultObj();

		boolean flag = false;
		List<StationInfo> stationInfos = new ArrayList<StationInfo>();

		// 系统,中心
		if (user.getRoleId() == CONST_ONE || user.getRoleId() == CONST_TWO) {

			flag = true;
		}

		// 分中心
		if (user.getRoleId() == CONST_THREE) {

			StationInfo si = new StationInfo();
			si.setStationName(user.getStationCode());

			if (stationInfoDAO.getStationInfoByWhereAnd(si).size() < 1) {

				pcResultObj.setEntity(2);
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");

				return pcResultObj;
			}

			si = stationInfoDAO.getStationInfoByWhereAnd(si).get(0);
			stationInfos = stationInfoservice.getChildStations(si);
			stationInfos.add(si);
			for (int i = 0; i < stationInfos.size(); i++) {

				if (id == stationInfos.get(i).getId()) {

					flag = true;
					break;
				}
			}

		}

		// 投注站
		if (user.getRoleId() == CONST_FOUR) {

			StationInfo si = new StationInfo();
			si.setStationCode(user.getStationCode());

			if (stationInfoDAO.getStationInfoByWhereAnd(si).size() < 1) {

				pcResultObj.setEntity(2);
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");

				return pcResultObj;
			}
			si = stationInfoDAO.getStationInfoByWhereAnd(si).get(0);

			if (si.getId() == id) {
				flag = true;
			}
		}

		if (flag) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("有权限访问!");
		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("没有权限访问!");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj ifSysAdmin(UserInfo userInfo) {

		PcResultObj pcResultObj = new PcResultObj();

		if (userInfo.getRoleId() == CONST_ONE) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsg("验证成功");
			pcResultObj.setMsgCode("1");

		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsg("没有权限");
			pcResultObj.setMsgCode("2");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj checkUserId(UserInfo user, int id) {

		PcResultObj pcResultObj = new PcResultObj();

		boolean flag = false;

		int userId = user.getId();

		if (id == 0) {
			id = userId;
		}

		if (user.getRoleId() == CONST_ONE) {

			flag = true;
		} else {

			if (user.getId() == id) {

				flag = true;
			}

		}

		if (flag) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("有权限访问!");
		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("没有权限访问!");
		}

		return pcResultObj;
	}

	@Override
	public boolean ifInsertBettingShop(UserInfo userInfo) {

		boolean flag = false;

		if (userInfo.getRoleId() == CONST_ONE
				|| userInfo.getRoleId() == CONST_TWO
				|| userInfo.getRoleId() == CONST_THREE) {
			// 添加投注站
			flag = true;
		}

		return flag;
	}

	@Override
	public boolean ifInsertBranch(UserInfo userInfo) {

		boolean flag = false;

		if (userInfo.getRoleId() == CONST_ONE
				|| userInfo.getRoleId() == CONST_TWO) {

			flag = true;
		}

		return flag;
	}

	@Override
	public PcResultObj checkUpdateAndDel(UserInfo user, int id) {

		PcResultObj pcResultObj = new PcResultObj();

		boolean flag = false;

		if (user.getRoleId() == CONST_ONE || user.getRoleId() == CONST_TWO) {

			flag = true;

		}

		if (user.getRoleId() == CONST_THREE) {

			StationInfo si = new StationInfo();
		
			si = getStationByUser(user);
			
			if(si == null){
				
				pcResultObj.setEntity(2);
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("没有权限访问!");
				
				return pcResultObj;
			}
			List<StationInfo> stationInfos = stationInfoservice
					.getChildStations(si);

			for (int i = 0; i < stationInfos.size(); i++) {

				if (stationInfos.get(i).getId() == id) {

					flag = true;
					break;
				}

			}
 
		}
		
		
		if(user.getRoleId() == CONST_FOUR){
			
			StationInfo stationInfo = getStationByUser(user);
			if(stationInfo == null){
				
				pcResultObj.setEntity(2);
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("没有权限访问!");
				
				return pcResultObj;
			}
			
			if(stationInfo.getId() == id){
				
				flag = true;
			}
			
		}
		
		if (flag) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("有权限访问!");
		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("没有权限访问!");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj checkGetBettingShop(UserInfo user, int id) {

		PcResultObj pcResultObj = new PcResultObj();

		boolean flag = false;

		if (user.getRoleId() == CONST_ONE || user.getRoleId() == CONST_TWO) {

			flag = true;

		}

		if (user.getRoleId() == CONST_THREE) {

			StationInfo si = new StationInfo();
			si.setStationName(user.getStationCode());

			if (stationInfoDAO.getStationInfoByWhereAnd(si).size() < 1) {

				pcResultObj.setEntity(2);
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");

				return pcResultObj;
			}
			si = stationInfoDAO.getStationInfoByWhereAnd(si).get(0);

			if (si.getId() == id) {

				flag = true;
			}

		}

		if (flag) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("有权限访问!");
		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("没有权限访问!");
		}

		return pcResultObj;
	}

	@Override
	public PcResultObj checkPassword(UserInfo user, UserInfo userInfo) {

		PcResultObj pcResultObj = new PcResultObj();
		int id = 0;

		if (userInfo.getId() == 0) {

			id = user.getId();

			userInfo.setId(id);
		}
		userInfo.setPassword(MdFiveUtils.getMD5(CONST_PASS
				+ userInfo.getPassword()));
		List<UserInfo> userInfos = userInfoDao.getUserInfoByWhereAnd(userInfo);

		if (userInfos.size() > 0) {

			pcResultObj.setEntity(1);
			pcResultObj.setMsg("用户原密码正确");
			pcResultObj.setMsgCode("1");

		} else {

			pcResultObj.setEntity(2);
			pcResultObj.setMsg("用户原密码错误");
			pcResultObj.setMsgCode("2");
		}

		return pcResultObj;
	}

	@Override
	public StationInfo getStationByUser(UserInfo user) {

		StationInfo si = new StationInfo();
		
		if(user.getRoleId() == CONST_ONE || user.getRoleId() ==  CONST_TWO || user.getRoleId() == CONST_THREE){
			          
			si.setStationName(user.getStationCode());
			List<StationInfo> stationInfos = stationInfoDAO.getStationInfoByWhereAnd(si);
			if(stationInfos.size() > 0){
				
				return stationInfos.get(0);
			}
		}else if(user.getRoleId() == CONST_FOUR){
			
			si.setStationCode(user.getStationCode());
			List<StationInfo> stationInfos = stationInfoDAO.getStationInfoByWhereAnd(si);
			if(stationInfos.size() > 0){
				
				return stationInfos.get(0);
			}
		}
		
		return null;
	}

}
