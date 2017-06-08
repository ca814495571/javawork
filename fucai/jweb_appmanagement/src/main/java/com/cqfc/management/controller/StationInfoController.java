package com.cqfc.management.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.StationMap;
import com.cqfc.management.model.UserInfo;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IUserInfoService;
import com.cqfc.management.util.dateUtils.DateUtils;

@Controller
@RequestMapping("/stationInfo")
public class StationInfoController {

	@Autowired
	IStationInfoService stationInfoService;

	@Autowired
	IUserInfoService userInfoService;

	@RequestMapping(value = "/insert")
	@ResponseBody
	public PcResultObj insertStation(HttpServletRequest request,
			StationInfo stationInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		return stationInfoService.insertStation(user, stationInfo);
	}

	@RequestMapping(value = "/getStationInfo.json")
	@ResponseBody
	public PcResultObj getStationInfo(HttpServletRequest request,
			StationInfo stationInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		
		int id = stationInfo.getId();
		if(id ==0 ){
			
			StationInfo si = userInfoService.getStationByUser(user);
			if(si == null){
				
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");
				return pcResultObj;
			}
			id = si.getId();
		}else{
			
			pcResultObj = userInfoService.checkStationId(user, id);

			if ((Integer) pcResultObj.getEntity() == 2) {

				return pcResultObj;

			}
			
		}
		
		
		return stationInfoService.getStationInfo(id);

	}

	@RequestMapping(value = "/update")
	@ResponseBody
	public PcResultObj updateStation(HttpServletRequest request,
			StationInfo stationInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		
		int id = stationInfo.getId();
		if(id == 0 ){
			
			StationInfo si = userInfoService.getStationByUser(user);
			if(si == null){
				
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");
				return pcResultObj;
			}
			
			stationInfo.setId(si.getId());
			
		}else{
			
			
			pcResultObj = userInfoService.checkUpdateAndDel(user,
					stationInfo.getId());
			if ((Integer) pcResultObj.getEntity() == 2) {

				return pcResultObj;

			}
		}
		
		return stationInfoService.updateStation(user,stationInfo);
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public PcResultObj deleteStation(HttpServletRequest request,
			StationInfo stationInfo) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		pcResultObj = userInfoService.checkUpdateAndDel(user,
				stationInfo.getId());

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}
		return stationInfoService.deleteStation(stationInfo.getId());

	}

	/**
	 * 查询站点地图信息（Json 格式）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/map.json")
	@ResponseBody
	public List<StationMap> stationMaplists() {
		return stationInfoService.getStationMaps();
	}

	/**
	 * 中心，分中心，投注站数据详情
	 */
	@RequestMapping(value = "/saleDetails.json")
	@ResponseBody
	public PcResultObj getSaleDetails(HttpServletRequest request,
			StationInfo station, String fromDate, String toDate) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}
		
		pcResultObj = DateUtils.checkDate(fromDate, toDate);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;
		}
		UserInfo user = (UserInfo) request.getSession().getAttribute("user");
		
		int id = station.getId();
		if(id == 0 ){
			
			StationInfo si = userInfoService.getStationByUser(user);
			if(si == null){
				
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");
				return pcResultObj;
			}
			
			id =si.getId();
			
		}else{
			
			pcResultObj = userInfoService.checkStationId(user, id);

			if ((Integer) pcResultObj.getEntity() == 2) {
				return pcResultObj;
			}

		}
	
		StationInfo si = stationInfoService.getStationInfoById(id);

		return stationInfoService.getLotterySaleDetail(si, fromDate, toDate);
	}

	/**
	 * 中心管理：获取中心下所有分中心列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/branches.json")
	@ResponseBody
	public PcResultObj getBranches(HttpServletRequest request, String keyword,
			int pageNum, int pageSize) {

		PcResultObj pcResultObj = userInfoService.authenticate(request);

		if ((Integer) pcResultObj.getEntity() == 2) {

			return pcResultObj;

		}

		UserInfo user = (UserInfo) request.getSession().getAttribute("user");

		int id = IStationInfoService.CENTER_ID;

		if (pageNum < 1) {
			pageNum = 1;
		}

		if (pageSize > 200) {
			pageSize = 15;
		}

		boolean b = userInfoService.ifInsertBettingShop(user);

		if (!b) {
			
			pcResultObj.setMsg("没有权限");
			pcResultObj.setMsgCode("2");
			return pcResultObj;
		}

		StationInfo si = stationInfoService.getStationInfoById(id);

		return stationInfoService.getBranchInCenter(si, keyword, pageNum,
				pageSize);

	}

	/**
	 * 获取分中心或者中心下所有投注站列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bettingShopes.json")
	@ResponseBody
	public PcResultObj getBettingShopes(HttpServletRequest request,
			StationInfo station, String keyword, int pageNum, int pageSize) {

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
		
		int id = station.getId();
		
		if(id == 0 ){
			
			StationInfo si = userInfoService.getStationByUser(user);
			if(si == null){
				
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("该用户没有操作权限");
				return pcResultObj;
			}
			
			id =si.getId();
			
		}else{

			pcResultObj = userInfoService.checkGetBettingShop(user, id);
			if ((Integer) pcResultObj.getEntity() == 2) {
				return pcResultObj;
			}
			
		}

		StationInfo si = stationInfoService.getStationInfoById(id);

		if (si.getStationFlag() == 1) {

			return stationInfoService.getBettingInfoInCenter(si, "", keyword,
					pageNum, pageSize, IStationInfoService.BETTINGSHOP_FLAG);

		} else if (si.getStationFlag() == 2) {
  
			return stationInfoService.getBettingInfoInBranch(si, "", keyword,
					pageNum, pageSize);

		} else {

			pcResultObj.setEntity("");
			pcResultObj.setMsgCode("3");
			pcResultObj.setMsg("没有权限!");
			return pcResultObj;

		}

	}

}
