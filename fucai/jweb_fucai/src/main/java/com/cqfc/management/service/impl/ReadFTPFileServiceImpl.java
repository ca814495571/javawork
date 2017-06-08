package com.cqfc.management.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.LotteryPlanDao;
import com.cqfc.management.dao.UserInfoOfFTPFileDao;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.LotteryPlan;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserCount;
import com.cqfc.management.model.UserInfoOfFTPFile;
import com.cqfc.management.service.ILotteryCountService;
import com.cqfc.management.service.IReadFTPFileService;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IUserCountService;
import com.cqfc.util.dateUtils.DateUtils;

@Service
public class ReadFTPFileServiceImpl implements IReadFTPFileService {

	@Autowired
	private UserInfoOfFTPFileDao userInfoOfFTPDao;

	@Autowired
	private LotteryPlanDao lotteryPlanDao;

	@Autowired
	private IStationInfoService stationInfoService;

	@Autowired
	private IUserCountService userCountService;

	@Autowired
	private ILotteryCountService lotteryCountService;

	@Override
	public void insertUserInfo(String filePath) {

		BufferedReader br = null;

		try {

			String str = "";

			// br = new BufferedReader(new FileReader(filePath));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(filePath)), "utf-8"));

			while ((str = br.readLine()) != null) {

				String[] strs = str.split("\\t");

				UserInfoOfFTPFile userInfoOfFTP = new UserInfoOfFTPFile();
				userInfoOfFTP.setUserId(strs[0]);

				str = strs[22].replace("\"", "=");

				if (str != null && !"".equals(str) && !"NULL".equals(str)
						&& !"null".equals(str)) {

					userInfoOfFTP.setExt(str.split("=")[3]);
				}

				userInfoOfFTP.setRegistTime(strs[23]);

				userInfoOfFTPDao.insertUserInfoOfFTP(userInfoOfFTP);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void insertPlanInfo(String filePath) {

		BufferedReader br = null;

		try {

			String str = "";

			// br = new BufferedReader(new FileReader(filePath));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					new File(filePath)), "utf-8"));

			while ((str = br.readLine()) != null) {

				String[] strs = str.split("\\t");

				LotteryPlan lotteryPlan = new LotteryPlan();

				lotteryPlan.setPlanId(strs[0]);
				lotteryPlan.setLotteryId(strs[4]);
				lotteryPlan.setUserId(strs[7]);
				lotteryPlan.setLotteryName(strs[5]);
				lotteryPlan.setTotalAmount(strs[23]);
				lotteryPlan.setCreateTime(strs[36]);
				lotteryPlan.setCharOne(strs[59]);
				lotteryPlan.setCharTwo(strs[60]);
				lotteryPlan.setExtInfo(strs[67]);

				lotteryPlanDao.inertLotteryPlan(lotteryPlan);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void analyzeUserInfo(Date date) {

		StationInfo stationInfo = stationInfoService.getStationInfoById(16);

		List<StationInfo> branchs = stationInfoService
				.getChildStations(stationInfo);

		List<StationInfo> bettingShops = new ArrayList<StationInfo>();

		String enterTag = "";
		UserCount uc = new UserCount();

		StationInfo bettingShop = null;
		StationInfo branch = null;
		int bettingUserAddNum = 0;
		int branchUserAddNum = 0;
		int centerUserAddNum = 0;
		String time = DateUtils.formatDateTwo(date);

		for (int i = 0; i < branchs.size(); i++) {

			branch = branchs.get(i);
			bettingShops = stationInfoService.getChildStations(branch);

			for (int j = 0; j < bettingShops.size(); j++) {

				bettingShop = bettingShops.get(j);
				enterTag = bettingShop.getEnterTag();

				bettingUserAddNum = userInfoOfFTPDao.getBettingShopUserAddNum(
						enterTag, time);

				insertUserCount(uc, bettingUserAddNum, bettingShop, enterTag,
						date);

				branchUserAddNum += bettingUserAddNum;

			}

			insertUserCount(uc, branchUserAddNum, branch, "", date);

			centerUserAddNum += branchUserAddNum;
			branchUserAddNum = 0;

		}

		insertUserCount(uc, centerUserAddNum, stationInfo, "", date);
	}

	public void insertUserCount(UserCount uc, int dailyAddNum,
			StationInfo station, String enterTag, Date date) {

		uc.setEnterTag(enterTag);
		uc.setUserDailyAddNum(dailyAddNum);
		uc.setYear(DateUtils.getYear(date));
		uc.setMonth(DateUtils.getMonth(date));
		uc.setStationId(station.getId());
		List<UserCount> preDayUserCount = userCountService.getpreDayUserCount(
				uc, date);

		if (preDayUserCount.size() > 0) {

			uc.setUserTotalNum(preDayUserCount.get(0).getUserTotalNum()
					+ dailyAddNum);
		} else {

			uc.setUserTotalNum(dailyAddNum);
		}

		int a = userCountService.insertUserCount(uc);
		System.out.println(a);
	}

	@Override
	public void analyzePlanInfo(Date date) {

		StationInfo stationInfo = stationInfoService.getStationInfoById(16);

		List<StationInfo> braches = stationInfoService
				.getChildStations(stationInfo);

		List<StationInfo> bettingShops = new ArrayList<StationInfo>();

		StationInfo branch = null;
		StationInfo bettingShop = null;
		LotteryCount lc = new LotteryCount();

		String time = DateUtils.formatDateTwo(date);
		int centerSaleNum = 0;
		int branchSaleNum = 0;
		int bettingShopSaleNum = 0;
		String enterTag = "";

		for (int i = 0; i < LOTTERY_TYPE.length; i++) {

			for (int j = 0; j < braches.size(); j++) {

				branch = braches.get(j);
				bettingShops = stationInfoService.getChildStations(branch);

				for (int k = 0; k < bettingShops.size(); k++) {

					bettingShop = bettingShops.get(k);
					enterTag = bettingShop.getEnterTag();

					bettingShopSaleNum = lotteryPlanDao.getLotterySaleByType(
							enterTag, time, LOTTERY_TYPE[i]);

					insertLotteryCount(bettingShop, lc, bettingShopSaleNum,
							date, LOTTERY_TYPE[i], enterTag);

					branchSaleNum += lc.getLotteryDailyNum();

				}

				insertLotteryCount(branch, lc, branchSaleNum, date,
						LOTTERY_TYPE[i], "");

				centerSaleNum += branchSaleNum;
				branchSaleNum = 0;

			}

			insertLotteryCount(stationInfo, lc, centerSaleNum, date,
					LOTTERY_TYPE[i], "");

			centerSaleNum = 0;
		}
	}

	public void insertLotteryCount(StationInfo station,
			LotteryCount lotteryCount, int dailyAdd, Date date,
			String lotteryType, String enterTag) {

		lotteryCount.setLotteryDailyNum(dailyAdd);
		lotteryCount.setStationId(station.getId());
		lotteryCount.setYear(DateUtils.getYear(date));
		lotteryCount.setMonth(DateUtils.getMonth(date));
		lotteryCount.setLotteryType(lotteryType);
		lotteryCount.setEnterTag(enterTag);
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
