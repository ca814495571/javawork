package com.cqfc.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.StationMap;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IStationMapService;
import com.cqfc.management.util.test.TestPoi;

@Controller
@RequestMapping("/station")
public class StationMapController {

	@Autowired
	IStationMapService stationMapService;

	@Autowired
	IStationInfoService stationInfoService;

	@RequestMapping(value = "/insert")
	public void insertStationMap() {


		   TestPoi tp = new TestPoi();
		   
		   String[] mapInfos = tp.getExcelDate("d:/map.xls");
		   
		   String []  map = null ;
		   int j  = 0 ;
		   for (int i = 1; i < mapInfos.length; i++) {
			
			   map = mapInfos[i].split(",") ;
			   
			   StationMap sm = new StationMap();
			   
			   j = map.length ;
				
				if(j ==0){
					
				}  else if(j == 1){
					
					sm.setId(map[0]);
				}else if(j == 2){
					sm.setId(map[0]);
					sm.setAddress(map[1]);
					
				} else if(j == 3){
					sm.setId(map[0]);
					sm.setAddress(map[1]);
					sm.setLon(map[2]);
					
				} else if(j == 4){
					
					sm.setId(map[0]);
					sm.setAddress(map[1]);
					sm.setLon(map[2]);
					sm.setLat(map[3]);
				} 
				   
			
			   int a =stationMapService.insertStationMap(sm);
			   System.out.println(a);
		}
		   
		   
	}

	@RequestMapping(value = "/map")
	@ResponseBody
	public List<StationMap> getAllUseAbleStationMap() {
		
		return stationMapService.getAllUseAbleStationMap();
		
	}

}
