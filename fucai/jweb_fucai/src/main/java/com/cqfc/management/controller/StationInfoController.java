package com.cqfc.management.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.EntityObj;
import com.cqfc.management.model.LotterySale;
import com.cqfc.management.model.PcEntityObj;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.StationMap;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.util.dateUtils.DateUtils;

@Controller
@RequestMapping("/stationInfo")
public class StationInfoController {

	@Autowired
	IStationInfoService stationInfoService;

	@RequestMapping(value = "/insert/branch", method = RequestMethod.POST)
	public void insertBranch(StationInfo stationInfo) {

		int id = 85;
		StationInfo si = stationInfoService.getStationInfoById(id);
		stationInfoService.insertBranch(si);
	}

	@RequestMapping(value = "/insert/bettingShop", method = RequestMethod.POST)
	public void insertBettingShop(StationInfo stationInfo) {

		int id = 85;
		StationInfo si = stationInfoService.getStationInfoById(id);
		stationInfoService.insertBettingShop(si);
	}

	@RequestMapping(value = "/getBranchInfo.json")
	@ResponseBody
	public PcEntityObj getBranchInfo(int id) {

		id = 80;

		PcEntityObj pcEntityObj = new PcEntityObj();

		EntityObj entityObj = new EntityObj();

		Map<String, Object> map = stationInfoService.getBranchInfo(id);

		entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
		entityObj.setRecordTotal((Integer) map
				.get(IStationInfoService.PARAM_RECORDTOTAL));

		pcEntityObj.setEntity(entityObj);
		pcEntityObj.setMsgCode("1");
		pcEntityObj.setMsg("1");
		return pcEntityObj;

	}

	@RequestMapping(value = "/getBettingShopInfo.json")
	@ResponseBody
	public PcEntityObj getBettingShopInfo(int id) {

		id = 85;
		PcEntityObj pcEntityObj = new PcEntityObj();

		EntityObj entityObj = new EntityObj();

		Map<String, Object> map = stationInfoService.getBettingShopInfo(id);

		entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
		entityObj.setRecordTotal((Integer) map
				.get(IStationInfoService.PARAM_RECORDTOTAL));

		pcEntityObj.setEntity(entityObj);
		pcEntityObj.setMsgCode("1");
		pcEntityObj.setMsg("1");
		return pcEntityObj;

	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void updateBranch(int id, StationInfo stationInfo) {

		id = 80;
		stationInfo = new StationInfo();
		stationInfoService.updateStation(id, stationInfo);
	}

	@RequestMapping(value = "/delete")
	public void deleteStation(int id) {

		stationInfoService.deleteStation(id);

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

	// ================================测试===================================

	/**
	 * 中心默认首页数据详情（图形化）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/centerSales.json")
	@ResponseBody
	public List<LotterySale> getCenterSales(int id, String fromDate,
			String toDate) {

		id = 16;

		StationInfo si = stationInfoService.getStationInfoById(id);
		int pageNum = 1;

		int pageSize = 15;
		String[] date = DateUtils.getMonthFristAndLastDay();
		return null;

	}

	/**
	 * 非中心默认首页数据详情（图形化）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/lotterySales.json")
	@ResponseBody
	public List<LotterySale> getLotterySales(int id, String fromDate,
			String toDate) {

		id = 85;

		StationInfo si = stationInfoService.getStationInfoById(id);
		int pageNum = 1;

		int pageSize = 15;
		String[] date = DateUtils.getMonthFristAndLastDay();
		return null;

	}

	/**
	 * 中心默认首页数据详情
	 */
	// @RequestMapping(value = "/centerSaleDetails.json")
	//
	@RequestMapping(value = "/saleDetails.json")
	@ResponseBody
	public PcEntityObj getCenterSaleDetails(int id, String fromDate,
			String toDate, int pageNum, int pageSize) {

		if (!"".equals(id) && id == 0) {

			id = 16;
		}

		StationInfo si = stationInfoService.getStationInfoById(id);

		String[] date = DateUtils.getMonthFristAndLastDay();

		pageNum = 1;

		pageSize = 15;

		PcEntityObj pcEntityObj = new PcEntityObj();

		EntityObj entityObj = new EntityObj();

		Map<String, Object> map = stationInfoService.getLotterySaleDetail(si,
				date[0], date[1], pageNum, pageSize);

		entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
		entityObj.setRecordTotal((Integer) map
				.get(IStationInfoService.PARAM_RECORDTOTAL));

		pcEntityObj.setEntity(entityObj);
		pcEntityObj.setMsgCode("1");
		pcEntityObj.setMsg("成功");
		return pcEntityObj;
	}

	/**
	 * 非中心默认首页数据详情
	 */
	// @RequestMapping(value = "/lotterySaleDetails.json")
	// @ResponseBody
	// public PcEntityObj getLotterySaleDetails(int id) {
	//
	// id = 85;
	//
	// StationInfo si = stationInfoService.getStationInfoById(id);
	// int pageNum = 1;
	//
	// int pageSize = 15;
	// String[] date = DateUtils.getMonthFristAndLastDay();
	//
	// PcEntityObj pcEntityObj = new PcEntityObj();
	//
	// EntityObj entityObj = new EntityObj();
	// Map<String, Object> map = stationInfoService.getLotterySaleDetail(si,
	// date[0], date[1], pageNum, pageSize);
	//
	// entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
	// entityObj.setRecordTotal((Integer) map
	// .get(IStationInfoService.PARAM_RECORDTOTAL));
	//
	// pcEntityObj.setEntity(entityObj);
	// pcEntityObj.setMsgCode("1");
	// pcEntityObj.setMsg("成功");
	//
	// return pcEntityObj;
	// }

	/**
	 * 中心管理：获取中心下所有分中心的基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/center/branches.json")
	@ResponseBody
	public PcEntityObj getCenterManage(String keyword, int pageNum, int pageSize) {

		pageNum = 1;
		pageSize = 15;

		StationInfo si = new StationInfo();
		PcEntityObj pcEntityObj = new PcEntityObj();
		EntityObj entityObj = new EntityObj();
		si = stationInfoService.getStationInfoById(16);
		String keyWord = "";

		Map<String, Object> map = stationInfoService.getCenterManage(si,
				keyWord, pageNum, pageSize);

		entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
		entityObj.setRecordTotal((Integer) map
				.get(IStationInfoService.PARAM_RECORDTOTAL));

		pcEntityObj.setEntity(entityObj);
		pcEntityObj.setMsgCode("1");
		pcEntityObj.setMsg("成功");

		return pcEntityObj;

	}

	/**
	 * 获取分中心下所有投注站的基本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/branch/bettingShopes.json")
	@ResponseBody
	public PcEntityObj getBranchManage(int id, String addOne, String keyword,
			int pageNum, int pageSize) {

		StationInfo si = new StationInfo();

		id = 85;

		pageNum = 1;

		pageSize = 15;

		PcEntityObj pcEntityObj = new PcEntityObj();
		EntityObj entityObj = new EntityObj();
		si = stationInfoService.getStationInfoById(id);

		Map<String, Object> map = stationInfoService.getBranchManage(si,
				addOne, keyword, pageNum, pageSize);

		entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
		entityObj.setRecordTotal((Integer) map
				.get(IStationInfoService.PARAM_RECORDTOTAL));

		pcEntityObj.setEntity(entityObj);
		pcEntityObj.setMsgCode("1");
		pcEntityObj.setMsg("成功");

		return pcEntityObj;
	}

	/**
	 * 获取中心下所有投注站信息
	 * String addOne,
			String keyword, int pageNum, int pageSize
	 * @return
	 */
	@RequestMapping(value = "/center/bettingShopes.json")
	@ResponseBody
	public PcEntityObj getBettingInfoInCenter( String addOne, String keyword,
			int pageNum, int pageSize) {

		StationInfo si = new StationInfo();

		si.setId(16);
		pageNum = 1;

		pageSize = 15;

		PcEntityObj pcEntityObj = new PcEntityObj();
		EntityObj entityObj = new EntityObj();
		si = stationInfoService.getStationByWhereAnd(si).get(0);

		Map<String, Object> map = stationInfoService.getAllBettingInfoInCenter(
				si, "", "", pageNum, pageSize,IStationInfoService.BETTINGSHOP_FLAG);

		entityObj.setObjects(map.get(IStationInfoService.PARAM_LIST));
		entityObj.setRecordTotal((Integer) map
				.get(IStationInfoService.PARAM_RECORDTOTAL));

		pcEntityObj.setEntity(entityObj);
		pcEntityObj.setMsgCode("1");
		pcEntityObj.setMsg("成功");

		return pcEntityObj;
	}

}
