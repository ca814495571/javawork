package com.cqfc.management.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.StationMapMapper;
import com.cqfc.management.model.StationMap;

@Repository
public class StationMapDao {

	@Autowired
	private StationMapMapper stationMapMapper;

	/**
	 * 插入记录
	 * 
	 * @param StationMap
	 * @return
	 */
	public int insertStationMap(StationMap stationMap) {

		filterNullColumn(stationMap);
		return stationMapMapper.insertStationMap(stationMap);
	}

	/**
	 * 过滤掉null值的字段设置默认值
	 * 
	 * @param StationMap
	 */
	public void filterNullColumn(StationMap StationMap) {
		if (StationMap.getId() == null) {
			StationMap.setId("");
		}

		if (StationMap.getAddress() == null) {
			StationMap.setAddress("");
		}
		if (StationMap.getLat() == null) {
			StationMap.setLat("");
		}
		if (StationMap.getLon() == null) {
			StationMap.setLon("");
		}
		if (StationMap.getPhone() == null) {
			StationMap.setPhone("");
		}
	}

	public List<StationMap> getAllStationMap() {

		return stationMapMapper.getAllStationMap();

	}

	public List<StationMap> getAllUseAbleStationMap() {

		return stationMapMapper.getAllUseAbleStationMap();

	}

}
