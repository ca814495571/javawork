package com.cqfc.management.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserCount;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IUserCountService;
import com.cqfc.util.dateUtils.DateUtils;

@Controller
@RequestMapping("/userCount")
public class UserCountController {

	@Autowired
	IUserCountService userCountService;

	@Autowired
	IStationInfoService stationInfoService;

	@RequestMapping(value = "/insert")
	@ResponseBody
	public void insertUserCount() {

		StationInfo zhongxin = new StationInfo();
		zhongxin.setId(16);

		List<StationInfo> fenzhongxin = stationInfoService
				.getChildStations(zhongxin);

		for (int i = 0; i < fenzhongxin.size(); i++) {

			if (fenzhongxin.get(i).getId() == (zhongxin.getId())) {

				fenzhongxin.remove(i);
			}
		}

		int zhongxinDailyAddNum = 0;
		int fenzhongxinDailyAddNum = 0;
		Date date = new Date();
		List<StationInfo> stations = new ArrayList<StationInfo>();
		StationInfo station = null;
		StationInfo station2 = null;
		UserCount uc = new UserCount();
		for (int i = 0; i < fenzhongxin.size(); i++) {

			station = fenzhongxin.get(i);
			stations = stationInfoService.getChildStations(station);

			for (int j = 0; j < stations.size(); j++) {

				station2 = stations.get(j);
				insertUser(station2, uc, 20, date);
				fenzhongxinDailyAddNum += uc.getUserDailyAddNum();

			}

			insertUser(station, uc, fenzhongxinDailyAddNum, date);

			fenzhongxinDailyAddNum = 0;
			zhongxinDailyAddNum += uc.getUserDailyAddNum();

		}

		insertUser(zhongxin, uc, zhongxinDailyAddNum, date);

	}

	public void insertUser(StationInfo station, UserCount userCount,
			int dailyAdd, Date date) {

		userCount.setUserDailyAddNum(dailyAdd);
		userCount.setStationId(station.getId());
		userCount.setYear(DateUtils.getYear(date));
		userCount.setMonth(DateUtils.getMonth(date));
		List<UserCount> preDayUserCount = userCountService.getpreDayUserCount(
				userCount, date);
		if (preDayUserCount.size() > 0) {

			userCount.setUserTotalNum(preDayUserCount.get(0).getUserTotalNum()
					+ dailyAdd);
		} else {

			userCount.setUserTotalNum(dailyAdd);
		}
		int a = userCountService.insertUserCount(userCount);
		System.out.println(a);

	}

}
