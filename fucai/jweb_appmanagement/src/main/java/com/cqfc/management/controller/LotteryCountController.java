package com.cqfc.management.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.service.ILotteryCountService;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.util.dateUtils.DateUtils;

@Controller
@RequestMapping("/lotteryCount")
public class LotteryCountController {

	@Autowired
	ILotteryCountService lotteryCountService;

	@Autowired
	IStationInfoService stationInfoService;

	/**
	 * 模拟数据插入
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/insert")
	@ResponseBody
	public void insertLotteryCount() throws ParseException {

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

		Date date = DateUtils.stringToDateTwo("2014-6-4");
		List<StationInfo> stations = new ArrayList<StationInfo>();
		StationInfo station = null;
		StationInfo station2 = null;
		LotteryCount lc = new LotteryCount();
		String lotteryType = "";
		for (int i = 0; i < 5; i++) {
			
			if(i==0){
				
				lotteryType = "双色球";
			}
			if(i==1){
							
							lotteryType = "福彩3D";
						}
			if(i==2){
				
				lotteryType = "七乐彩";
			}if(i==3){
				
				lotteryType = "幸运农场";
			}if(i==4){
				
				lotteryType = "时时彩";
			}

			for (int j = 0; j < fenzhongxin.size(); j++) {

				station = fenzhongxin.get(j);
				stations = stationInfoService.getChildStations(station);

				for (int k = 0; k < stations.size(); k++) {

					station2 = stations.get(k);
					insertLotteryCount(station2, lc, 100, date, lotteryType);
					fenzhongxinDailyAddNum += lc.getLotteryDailyNum();

				}

				insertLotteryCount(station, lc, fenzhongxinDailyAddNum, date,
						lotteryType);

				fenzhongxinDailyAddNum = 0;
				zhongxinDailyAddNum += lc.getLotteryDailyNum();

			}

			insertLotteryCount(zhongxin, lc, zhongxinDailyAddNum, date,
					lotteryType);

			zhongxinDailyAddNum = 0;

		}

	}

	public void insertLotteryCount(StationInfo station,
			LotteryCount lotteryCount, int dailyAdd, Date date,
			String lotteryType) {

		lotteryCount.setLotteryDailyNum(dailyAdd);
		lotteryCount.setStationId(station.getId());
		lotteryCount.setYear(DateUtils.getYear(date));
		lotteryCount.setMonth(DateUtils.getMonth(date));
		lotteryCount.setLotteryType(lotteryType);
		lotteryCount.setCountTime(DateUtils.formatDateTwo(date));
		List<LotteryCount> preDayLotteryCount = lotteryCountService
				.getPreDayLotteryCount(station, date, lotteryType);
		if (preDayLotteryCount.size() > 0) {

			lotteryCount.setLotteryMonthNum(preDayLotteryCount.get(0)
					.getLotteryMonthNum() + dailyAdd);
		} else {

			lotteryCount.setLotteryMonthNum(dailyAdd);
		}
		int a = lotteryCountService.insertLotteryCount(lotteryCount);
		System.out.println(a);
	}

}
