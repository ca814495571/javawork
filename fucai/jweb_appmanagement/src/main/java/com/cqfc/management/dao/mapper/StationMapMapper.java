package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.cqfc.management.model.StationMap;
import com.cqfc.common.dao.BaseMapper;

public interface StationMapMapper extends BaseMapper {
	/**
	 * private int id; //
	private String code; // 编号
	private String address; // 地址
	private String phone; // 联系方式
	private String lon; // 经度
	private String lat; // 纬度
	
	
	
	private String id ; //编号
	private String address;  //地址
	private String phone;	//联系方式
	private String lon ;	// 经度
	private String lat;	
	 * @param userCount
	 * @return
	 */
	@Insert("insert into t_station_map "
			+ "(id,address,phone,lon,lat) "
			+ "values (#{id},#{address},#{phone},#{lon},#{lat})"
			)
	public int insertStationMap(StationMap stationMap);


	@Select("select *  from t_station_map ")
	public List<StationMap> getAllStationMap();
	
	
	
	
	
	
	@Select("select *  from t_station_map where lon <> '' and lat <> ''")
	public List<StationMap> getAllUseAbleStationMap();
}
