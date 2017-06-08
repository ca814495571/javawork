package com.cqfc.management.service.impl;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.FtpFileLogDao;
import com.cqfc.management.dao.LotteryPlanDao;
import com.cqfc.management.dao.UserInfoOfFTPFileDao;
import com.cqfc.management.model.FtpFileLog;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.LotteryPlan;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.UserCount;
import com.cqfc.management.model.UserInfoOfFTPFile;
import com.cqfc.management.service.ILotteryCountService;
import com.cqfc.management.service.IReadFTPFileService;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IUserCountService;
import com.cqfc.management.util.dateUtils.DateUtils;
import com.cqfc.management.util.propertiesUtils.ReadProperties;

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

	@Autowired
	private FtpFileLogDao ftpFileDao;

	@Override
	public void insertUserInfo(File file) {

		BufferedReader br = null;

		try {

			String str = "";

			// br = new BufferedReader(new FileReader(filePath));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));

			while ((str = br.readLine()) != null) {
				
				if (!"none".equals(str)) {
					String[] strs = str.split("\\t");

					UserInfoOfFTPFile userInfoOfFTP = new UserInfoOfFTPFile();
					userInfoOfFTP.setUserId(strs[0]);
//{"actualAmount":"200","cftPayId":"1214526201201403179508336977","payId":"201403170013304115","vb2ctag":"4_2013_3_1780"}
					str = strs[22];
					if (str != null && !"".equals(str) && !"NULL".equals(str)
							&& !"null".equals(str)) {
						
						str = str.substring(str.indexOf("vb2ctag") - 2);
						str = str.replace("\"", "=");
						userInfoOfFTP.setExt(str.split("=")[3]);
					}


					userInfoOfFTP.setRegistTime(strs[23]);

					if (userInfoOfFTPDao.getByUserId(userInfoOfFTP).size() < 1) {

						userInfoOfFTPDao.insertUserInfoOfFTP(userInfoOfFTP);

					}
				}
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
	public void insertPlanInfo(File file) {

		BufferedReader br = null;

		try {

			String str = "";

			// br = new BufferedReader(new FileReader(filePath));
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));

			while ((str = br.readLine()) != null) {

				if (!"none".equals(str)) {
					String[] strs = str.split("\\t");

					LotteryPlan lotteryPlan = new LotteryPlan();

					lotteryPlan.setPlanId(strs[0]);
					lotteryPlan.setLotteryId(strs[4]);
					lotteryPlan.setUserId(strs[7]);
					lotteryPlan.setLotteryName(strs[5]);
					lotteryPlan.setTotalAmount(strs[25]);
					lotteryPlan.setCreateTime(strs[36]);
					lotteryPlan.setCharOne(strs[59]);
					lotteryPlan.setCharTwo(strs[60]);
					lotteryPlan.setExtInfo(strs[67]);

					if (lotteryPlanDao.getByPlanId(lotteryPlan).size() < 1) {
						lotteryPlanDao.inertLotteryPlan(lotteryPlan);
					}

				}
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
	public void ftpDateTransfer() {

		FTPClient client = new FTPClient();
		String downTime = DateUtils.formatDateTwo(new Date());
		try {
			client.connect("203.195.182.109", 21);
			client.login("chongqingfucai", "FuCai4Tencent");
			client.setPassive(false);
			String dir = client.currentDirectory();
//			client.changeDirectory("/fc_data/" + downTime.replace("-", ""));
			FTPFile[] list = client.list("*.txt");
			System.out.println(list.length);
			client.setCharset("utf-8");
//			List<FtpFileLog> ftpFileLogs = ftpFileDao.getByDownTime(downTime);
			List<FtpFileLog> ftpFileLogs = new ArrayList<FtpFileLog>();
			FtpFileLog ftpFileLog = new FtpFileLog();
			boolean flag = false;
			// 遍历FTP上的文件
			for (int i = 0; i < list.length; i++) {

				String fileName = list[i].getName();
				
				ftpFileLogs = ftpFileDao.getByFileNameAndFlag(fileName, 1);
					if (ftpFileLogs.size() < 1) {

						flag = false;
					}else{
						
						flag = true ;
					}
				
				// log中没有被分析的fileName才进行下载读取
				if (!flag) {
 

					File file = new File(new ReadProperties().readProperties("conf.properties").get("ftpDownLoad") + fileName);
				
					client.download(fileName, file);
					
					ftpFileLog.setFileName(fileName);
					ftpFileLog.setDownTime(downTime);
					ftpFileLog.setFlag(2);
					try {
						ftpFileDao.insert(ftpFileLog);
					} catch (Exception e) {
						ftpFileDao.update(ftpFileLog);

					}

					if (fileName.lastIndexOf("planinfo") != -1) {

						try {

							insertPlanInfo(file);
							ftpFileLog.setFlag(1);
							ftpFileDao.update(ftpFileLog);
						} catch (Exception e) {

							ftpFileLog.setFlag(2);
							ftpFileDao.update(ftpFileLog);
						}

					}

					if (fileName.lastIndexOf("userinfo") != -1) {

						try {

							insertUserInfo(file);
							ftpFileLog.setFlag(1);
							ftpFileDao.update(ftpFileLog);
						} catch (Exception e) {

							ftpFileLog.setFlag(2);
							ftpFileDao.update(ftpFileLog);
						}

					}

				}

			}
			// client.download("hello.ext", new java.io.File("F:/A.txt"), new
			// MyTransferListener());

			try{
				
				analyzeUserInfo(DateUtils.stringToDateTwo(downTime));
			
			}catch(Exception e){
				
				userCountService.deleteAll(downTime);
				try {
					analyzeUserInfo(DateUtils.stringToDateTwo(downTime));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			try{
				
				analyzePlanInfo(DateUtils.stringToDateTwo(downTime));
			
			}
			catch(Exception e){
				
				lotteryCountService.deleteAll(downTime);
				try {
					analyzePlanInfo(DateUtils.stringToDateTwo(downTime));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}

			client.disconnect(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPListParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void analyzeUserInfo(Date date) {
	
		List<StationInfo> bettingShops = new ArrayList<StationInfo>();
		List<StationInfo> branches = new ArrayList<StationInfo>();
		String enterTag = "";
		UserCount uc = new UserCount();

		StationInfo bettingShop = null;
		StationInfo branch = null;
		int bettingUserAddNum = 0;
		int branchUserAddNum = 0;
		int centerUserAddNum = 0;
		String time = DateUtils.formatDateTwo(date);
		
		
		StationInfo si = new StationInfo();
		si.setStationFlag(IStationInfoService.BETTINGSHOP_FLAG);
		
		bettingShops = stationInfoService.getStationByWhereAnd(si);
		UserCount ucTemp = new UserCount();
		for (int i = 0; i < bettingShops.size(); i++) {
			
			
			bettingShop = bettingShops.get(i);
			enterTag = bettingShop.getEnterTag();

			bettingUserAddNum = userInfoOfFTPDao.getBettingShopUserAddNum(
					enterTag, time);
  
			insertUserCount(uc, bettingUserAddNum, bettingShop, enterTag,
					date);
			
		}
		
		si.setStationFlag(IStationInfoService.BRANCH_FLAG);
		
		branches = stationInfoService.getStationByWhereAnd(si);
		
		for (int i = 0; i < branches.size(); i++) {
			
			branch = branches.get(i);
			bettingShops = stationInfoService.getChildStations(branch);
			
			for (int j = 0; j < bettingShops.size(); j++) {
				
				bettingShop = bettingShops.get(j);
				
				ucTemp.setStationId(bettingShop.getId());
				ucTemp.setCountTime(time);
				branchUserAddNum += userCountService.getUserCountByWhereAnd(ucTemp).get(0).getUserDailyAddNum();
			}
			insertUserCount(uc, branchUserAddNum, branch, "", date);
			
			centerUserAddNum += branchUserAddNum;
			branchUserAddNum = 0 ;
		}
		
		si.setStationFlag(IStationInfoService.CENTER_ID);
		
		StationInfo center = stationInfoService.getStationInfoById(IStationInfoService.CENTER_ID);
		
		
		insertUserCount(uc, centerUserAddNum, center, "", date);
		
	}

	@Override
	public void analyzeUserInfoTest(Date date) {

		StationInfo stationInfo = stationInfoService
				.getStationInfoById(IStationInfoService.CENTER_ID);

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
		uc.setCountTime(DateUtils.formatDateTwo(date));

		List<UserCount> preDayUserCount = userCountService.getpreDayUserCount(
				uc, date);

		if (preDayUserCount.size() > 0) {

			uc.setUserTotalNum(preDayUserCount.get(0).getUserTotalNum()
					+ dailyAddNum);
		} else {
			
			int total = userCountService.getTotalNum(uc.getStationId(), DateUtils.formatDateTwo(date));
			
			if(total > 0){
				
				uc.setUserTotalNum(total);
			}else{
				
				uc.setUserTotalNum(dailyAddNum);
			}
		}

		int a = userCountService.insertUserCount(uc);
		System.out.println(a);
	}

	@Override
	public void analyzePlanInfo(Date date) {

		
		List<StationInfo> branches = new ArrayList<StationInfo>();

		List<StationInfo> bettingShops = new ArrayList<StationInfo>();
		
		List<StationInfo> bettingShopsInBranch = new ArrayList<StationInfo>();
		
		StationInfo center = null ;
		StationInfo branch = null;
		StationInfo bettingShop = null;
		LotteryCount lc = new LotteryCount();

		String time = DateUtils.formatDateTwo(date);
		int centerSaleNum = 0;
		int branchSaleNum = 0;
		int bettingShopSaleNum = 0;
		String enterTag = "";
		
		
		StationInfo si = new StationInfo();
		si.setStationFlag(IStationInfoService.BETTINGSHOP_FLAG);
		
		bettingShops = stationInfoService.getStationByWhereAnd(si);
		
		si.setStationFlag(IStationInfoService.BRANCH_FLAG);
		
		branches = stationInfoService.getStationByWhereAnd(si);
		
		si.setStationFlag(IStationInfoService.CENTER_ID);
		
		center = stationInfoService.getStationInfoById(IStationInfoService.CENTER_ID);
		
		for (int i = 0; i < LOTTERY_TYPE.length; i++) {
			
			for (int j = 0; j < bettingShops.size(); j++) {
				
				bettingShop = bettingShops.get(j);
				enterTag = bettingShop.getEnterTag();

				bettingShopSaleNum = lotteryPlanDao.getLotterySaleByType(
						enterTag, time, LOTTERY_TYPE[i]);

				insertLotteryCount(bettingShop, lc, bettingShopSaleNum,
						date, LOTTERY_TYPE[i], enterTag);

			}
			
			
			for (int j = 0; j < branches.size(); j++) {
				
				branch = branches.get(j);
				bettingShopsInBranch = stationInfoService.getChildStations(branch);
				
				for (int k = 0; k < bettingShopsInBranch.size(); k++) {
					
					bettingShop = bettingShopsInBranch.get(k);
					
					lc.setStationId(bettingShop.getId());
					lc.setCountTime(time);
					lc.setLotteryType(LOTTERY_TYPE[i]);
					branchSaleNum += lotteryCountService.getLotteryCountByWhereAnd(lc).get(0).getLotteryDailyNum();
				
				}
				
				insertLotteryCount(branch, lc, branchSaleNum,
						date, LOTTERY_TYPE[i], "");
				
				centerSaleNum += branchSaleNum;
				branchSaleNum = 0 ;
			}
			
			
			insertLotteryCount(center, lc, centerSaleNum,
					date, LOTTERY_TYPE[i], "");
			
			centerSaleNum = 0 ;
		}
		
		
	}
	
	
	@Override
	public void analyzePlanInfoTest(Date date) {

		StationInfo stationInfo = stationInfoService
				.getStationInfoById(IStationInfoService.CENTER_ID);

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
		lotteryCount.setCountTime(DateUtils.formatDateTwo(date));
		
		int totalSaleNum =  lotteryCountService.getTotalSaleNum(station,lotteryType,DateUtils.getMonthFristAndLastDay()[0],DateUtils.formatDateTwo(date));
		
		if(totalSaleNum >0){
			lotteryCount.setLotteryMonthNum(totalSaleNum);
			
		}else{
			
			lotteryCount.setLotteryMonthNum(dailyAdd);
		}
		
		int a = lotteryCountService.insertLotteryCount(lotteryCount);
		System.out.println(a);
	}
}
