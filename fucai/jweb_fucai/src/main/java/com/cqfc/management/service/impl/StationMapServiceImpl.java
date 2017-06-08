package com.cqfc.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.StationMapDao;
import com.cqfc.management.model.StationMap;
import com.cqfc.management.service.IStationMapService;

@Service
public class StationMapServiceImpl implements IStationMapService {

	@Autowired
	private StationMapDao stationMapDao;

	@Override
	public int insertStationMap(StationMap stationMap) {

		return stationMapDao.insertStationMap(stationMap);
	}

	List<StationMap> cache = null;

	@Override
	public List<StationMap> getAllStationMap() {
		if (cache == null) {
			cache = stationMapDao.getAllStationMap();
		}
		return cache;
	}

	@Override
	public List<StationMap> getAllUseAbleStationMap() {
		if (cache == null) {
			cache = stationMapDao.getAllUseAbleStationMap();
		}
		return cache;
	}

}
