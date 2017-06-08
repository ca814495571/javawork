package com.cqfc.management.service;

import java.util.List;

import com.cqfc.management.model.StationMap;

public interface IStationMapService {
	/**
	 * 
	 * @param stationMap
	 * @return
	 */
	public int insertStationMap(StationMap stationMap);

	/**
	 * @param stationMap
	 * @return
	 */
	public List<StationMap> getAllStationMap();

	
	/**
	 * 查询可用的站点信息 （暂定lon or lat 不能为''）
	 * @return
	 */
	public List<StationMap> getAllUseAbleStationMap();
}
