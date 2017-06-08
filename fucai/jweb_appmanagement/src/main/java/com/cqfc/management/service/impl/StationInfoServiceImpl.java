package com.cqfc.management.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.LotteryCountDao;
import com.cqfc.management.dao.StationInfoDao;
import com.cqfc.management.dao.UserCountDao;
import com.cqfc.management.dao.UserInfoDao;
import com.cqfc.management.model.BettingShopBaseInfo;
import com.cqfc.management.model.BettingShopInBranch;
import com.cqfc.management.model.Branch;
import com.cqfc.management.model.BranchBaseInfo;
import com.cqfc.management.model.BranchInCenter;
import com.cqfc.management.model.Center;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.LotterySale;
import com.cqfc.management.model.LotterySaleDetail;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.StationMap;
import com.cqfc.management.model.UserCount;
import com.cqfc.management.model.UserInfo;
import com.cqfc.management.service.IStationInfoService;
import com.cqfc.management.service.IUserInfoService;

@Service
public class StationInfoServiceImpl implements IStationInfoService {

	@Autowired
	private StationInfoDao stationInfoDao;

	@Autowired
	private UserCountDao userCountDao;

	@Autowired
	private LotteryCountDao lotteryCountDao;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private UserInfoDao userInfoDao;
	
	@Override
	public PcResultObj insertStation(UserInfo user, StationInfo stationInfo) {

		PcResultObj pcResultObj = new PcResultObj();
		int a = 0;
		// parentId不为0时,中心添加投注站
		if (stationInfo.getParentId() != 0) {

			if (userInfoService.ifInsertBettingShop(user)) {

				stationInfo.setParentStationName(stationInfoDao
						.getStationInfoById(stationInfo.getParentId())
						.getStationName());
				stationInfo.setStationFlag(BETTINGSHOP_FLAG);

				pcResultObj = ifStationExist(stationInfo);
				
				if((Integer)pcResultObj.getEntity() == 2){
					return pcResultObj;
				}
				
			}
		} else if (stationInfo.getParentId() == 0) {
			//parentId为0时，分中心添加投注站 或者 中心添加分中心

			StationInfo siTemp = userInfoService.getStationByUser(user); 
			//站点表查询不到该用户所属的站点
			if(siTemp == null){
				
				pcResultObj.setMsgCode("2");
				pcResultObj.setMsg("没有权限！");
				return pcResultObj;
			}
			
			//用户是中心的则添加分中心
			if(siTemp.getStationFlag() == 1){
				
				stationInfo.setParentStationName(stationInfoDao
						.getStationInfoById(CENTER_ID).getStationName());
				stationInfo.setStationFlag(BRANCH_FLAG);
				stationInfo.setParentId(CENTER_ID);
				pcResultObj = ifStationExist(stationInfo);
				
				if((Integer)pcResultObj.getEntity() == 2){
					return pcResultObj;
				}
				
			}
			//用户是分中心的则添加投注站
			if(siTemp.getStationFlag() == 2){
				
				stationInfo.setStationFlag(BETTINGSHOP_FLAG);
				
				stationInfo.setParentId(siTemp.getId());
				
				stationInfo.setParentStationName(siTemp.getStationName());
				
				pcResultObj = ifStationExist(stationInfo);
				
				if((Integer)pcResultObj.getEntity() == 2){
					return pcResultObj;
				}
			}
			
		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("没有权限！");
			return pcResultObj;
		}

		a = stationInfoDao.insertStationInfo(stationInfo);
		
		if (a == 1) {

			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("添加成功");
		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("添加失败");

		}

		return pcResultObj;
	}

	@Override
	public PcResultObj updateStation(UserInfo user ,StationInfo si) {

		PcResultObj pcResultObj = new PcResultObj();

		StationInfo stationInfo = new StationInfo();
//1修改了分中心名称    修改了投注站站号2
		int flag = 0 ;
		
		String OriginalSiName = "";
		String OriginalSiCode = "";
		stationInfo.setId(si.getId());
		//根据Id查询自己的信息,对传递进来的参数有值的进行替换
		List<StationInfo> stationInfos = stationInfoDao
				.getStationInfoByWhereAnd(stationInfo);
		if (stationInfos.size() > 0) {

			stationInfo = stationInfos.get(0);
		} else {
			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("查询失败！");
			return pcResultObj;
		}

		if (stationInfo.getStationFlag() == 3) {
			
			stationInfo.setStationName(si.getStationName());

		}
		
		//修改分中心的名称，其下面的投注站的父节点名称需修改  需对分中心的name校验唯一性
		if (!"".equals(si.getStationName()) && stationInfo.getStationFlag() == 2) {

			
			
			List<StationInfo> stationInfosTemp = stationInfoDao.checkStationName(stationInfo.getStationName(), si.getStationName());
			
			if(stationInfosTemp.size() > 0){
				
				pcResultObj.setMsg("名称已存在");
				pcResultObj.setMsgCode("2");
				
				return pcResultObj;
			}
			flag = 1;
			OriginalSiName = stationInfo.getStationName();
			stationInfo.setStationName(si.getStationName());
			
		}
		
		if (!"".equals(si.getParentId()) && stationInfo.getStationFlag() == 3 && si.getParentId()!= 0) {

			int parentId = si.getParentId();
			// 验证父节点是否存在
			StationInfo stationInfoTemp = new StationInfo();
			stationInfoTemp.setId(parentId);

			if (stationInfoDao.getStationInfoByWhereAnd(stationInfoTemp).size() < 1
					|| parentId == CENTER_ID) {

				return checkStation(null);
			}

			stationInfo.setParentId(parentId);
			stationInfo.setParentStationName(stationInfoDao.getStationInfoById(
					parentId).getStationName());

		}

		if ( si.getStationAddOne() != null) {

			stationInfo.setStationAddOne(si.getStationAddOne());

		}

		if ( si.getStationAddTwo() != null) {

			stationInfo.setStationAddTwo(si.getStationAddTwo());

		}
		
		
		
		if (stationInfo.getStationFlag() == 2) {
			
			stationInfo.setStationCode(si.getStationCode());

		}
		//对code进行校验唯一性（目前针对投注站）
		if (!"".equals(si.getStationCode()) && stationInfo.getStationFlag() == 3) {

			
				//检查修改之后的code是否存在
				List<StationInfo> stationInfosTemp = stationInfoDao.checkStationCode(stationInfo.getStationCode(), si.getStationCode());
				
				if(stationInfosTemp.size() > 0){
					
					pcResultObj.setMsg("站号已存在");
					pcResultObj.setMsgCode("2");
					
					return pcResultObj;
				}
				
			
			flag = 2;
			OriginalSiCode = stationInfo.getStationCode();
			
			stationInfo.setStationCode(si.getStationCode());
		}

		if (si.getStationLinkman() != null) {

			stationInfo.setStationLinkman(si.getStationLinkman());
		}

		if ( si.getStationTel() != null) {

			stationInfo.setStationTel(si.getStationTel());
		}

		if (si.getStationLongitude() != null) {

			stationInfo.setStationLongitude(si.getStationLongitude());
		}

		if (si.getStationLatitude() != null) {

			stationInfo.setStationLatitude(si.getStationLatitude());
		}

		if (!"".equals(si.getActive()) && si.getActive() != 0) {

			stationInfo.setActive(si.getActive());
		}


		int a = stationInfoDao.updateStationInfo(stationInfo);

		if (a == 1) {

			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("修改成功");
			
			//分中心下的投注站修改父节点名称    用户所属的name也要修改
			if(flag == 1){
				
			
				stationInfoDao.updateByParentId(stationInfo.getId(), stationInfo.getStationName());
				
				userInfoDao.updateByCode(OriginalSiName, stationInfo.getStationName());
				
				if(OriginalSiName.equals(user.getStationCode())){
					
					
					user.setStationCode(stationInfo.getStationName());
				}
			}
			//投注站所属的用户也要修改code
			if(flag == 2){
				
				userInfoDao.updateByCode(OriginalSiCode, stationInfo.getStationCode());
				
				if(OriginalSiCode.equals(user.getStationCode())){
					
					user.setStationCode(stationInfo.getStationCode());
					
				}
			}
			
			
		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("修改失败");

		}

		return pcResultObj;
	}

	@Override
	public PcResultObj deleteStation(int id) {

		PcResultObj pcResultObj = new PcResultObj();
		StationInfo stationInfo = getStationInfoById(id);

		stationInfo.setActive(2);

		int a = stationInfoDao.updateStationInfo(stationInfo);

		if (a == 1) {

			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("删除成功");
			// if(stationInfo.getStationFlag() == 2){
			// List<StationInfo> stationInfos = getChildStations(stationInfo);
			// for (int i = 0; i < stationInfos.size(); i++) {
			//
			// StationInfo si = stationInfos.get(i);
			// si.setActive(2);
			// stationInfoDao.updateStationInfo(si);
			// }
			//
			// }
			// 需删除分中心或者投注站管理员?

			String stationCode = stationInfo.getStationCode();

		} else {

			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("删除失败");

		}

		return pcResultObj;
	}

	@Override
	public StationInfo getStationInfoById(int id) {
		return stationInfoDao.getStationInfoById(id);
	}

	@Override
	public List<StationInfo> getStationInfoAll() {
		return stationInfoDao.getStationAll();
	}



	@Override
	public List<StationInfo> getStationByWhereAnd(StationInfo stationInfo) {

		return stationInfoDao.getStationInfoByWhereAnd(stationInfo);
	}

	@Override
	public PcResultObj getStationInfo(int id) {

		PcResultObj pcResultObj = new PcResultObj();

		BranchBaseInfo branchBaseInfo = new BranchBaseInfo();
		BettingShopBaseInfo bettingShopBaseInfo = new BettingShopBaseInfo();
		StationInfo stationInfo = getStationInfoById(id);

		if (stationInfo == null) {
			return checkStation(stationInfo);
		}

		if (stationInfo.getStationFlag() == 2) {

			branchBaseInfo.setBranchCode(stationInfo.getStationCode());
			branchBaseInfo.setLinkMan(stationInfo.getStationLinkman());
			branchBaseInfo.setBranchName(stationInfo.getStationName());
			branchBaseInfo.setPhone(stationInfo.getStationTel());
			branchBaseInfo.setBranchId(stationInfo.getId());
			pcResultObj.setEntity(branchBaseInfo);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("查询成功");

		} else if (stationInfo.getStationFlag() == 3) {

			bettingShopBaseInfo
					.setBettingShopName(stationInfo.getStationName());
			bettingShopBaseInfo
					.setBettingShopCode(stationInfo.getStationCode());
			bettingShopBaseInfo.setBettingShopTel(stationInfo.getStationTel());
			bettingShopBaseInfo.setBranchId(stationInfo.getParentId());
			bettingShopBaseInfo.setBranchName(stationInfo
					.getParentStationName());
			bettingShopBaseInfo.setAddOne(stationInfo.getStationAddOne());
			bettingShopBaseInfo.setAddTwo(stationInfo.getStationAddTwo());
			bettingShopBaseInfo.setLon(stationInfo.getStationLongitude());
			bettingShopBaseInfo.setLat(stationInfo.getStationLatitude());
			bettingShopBaseInfo.setBettingShopId(stationInfo.getId());
			bettingShopBaseInfo.setLinkMan(stationInfo.getStationLinkman());

			pcResultObj.setEntity(bettingShopBaseInfo);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("查询成功");

		}

		return pcResultObj;
	}

	@Override
	public List<StationMap> getStationMaps() {

		List<StationMap> stationMaps = new ArrayList<StationMap>();
		StationInfo si = new StationInfo();
		si.setStationFlag(3);
		List<StationInfo> stationInfos = stationInfoDao
				.getStationInfoByWhereAnd(si);

		for (int i = 0; i < stationInfos.size(); i++) {

			si = stationInfos.get(i);
			StationMap stationMap = new StationMap();

			stationMap.setId(si.getStationCode().trim());
			stationMap.setAddress((si.getStationAddOne() + si
					.getStationAddTwo()).trim());
			stationMap.setPhone(si.getStationTel().trim());
			stationMap.setLon(si.getStationLongitude().trim());
			stationMap.setLat(si.getStationLatitude().trim());
			stationMaps.add(stationMap);
		}

		return stationMaps;
	}

	@Override
	public ResultObj getLotterySaleByDate(StationInfo stationInfo,
			String fromDate, String toDate, int pageNum, int pageSize) {

		ResultObj resultObj = new ResultObj();

		List<LotterySaleDetail> sscb = new ArrayList<LotterySaleDetail>();

		List<LotteryCount> lotteryCounts = lotteryCountDao
				.getLotteryCountByDate(stationInfo, fromDate, toDate, pageNum,
						pageSize);

		LotteryCount lc = null;

		for (int i = 0; i < lotteryCounts.size(); i++) {

			LotterySaleDetail ls = new LotterySaleDetail();
			lc = lotteryCounts.get(i);

			ls.setDay(Integer.parseInt(lc.getCountTime().split("-")[2]));
			ls.setSaleNum(lc.getLotteryDailyNum());
			ls.setLotteryType(lc.getLotteryType());

			sscb.add(ls);
		}

		resultObj.setObjects(sscb);
		resultObj.setRecordTotal(lotteryCountDao.getRecordTotal(stationInfo,
				fromDate, toDate));
		return resultObj;
	}

	@Override
	public PcResultObj getLotterySaleDetail(StationInfo stationInfo,
			String fromDate, String toDate) {

		ResultObj resultObj = new ResultObj();

		if (stationInfo == null) {
			return checkStation(stationInfo);
		}

		List<LotterySale> centerSaleDetails = new ArrayList<LotterySale>();

		List<UserCount> userCounts = userCountDao.getUserCountByDate(
				stationInfo, fromDate, toDate);

		List<LotteryCount> lotteryCounts = null;

		LotteryCount lc = new LotteryCount();
		lc.setStationId(stationInfo.getId());
		UserCount uc = null;
		String date = "";

		for (int i = 0; i < userCounts.size(); i++) {

			LotterySale centerSaleDetail = new LotterySale();
			List<LotterySaleDetail> lotterySales = new ArrayList<LotterySaleDetail>();
			uc = userCounts.get(i);
			date = uc.getCountTime();

			lc.setCountTime(date);

			lotteryCounts = lotteryCountDao.getLotteryCountByWhereAnd(lc);

			for (int j = 0; j < lotteryCounts.size(); j++) {

				LotteryCount lotteryCount = lotteryCounts.get(j);
				LotterySaleDetail ls = new LotterySaleDetail();

				ls.setDay(Integer.parseInt(lotteryCount.getCountTime().split(
						"-")[2]));
				ls.setSaleNum(lotteryCount.getLotteryDailyNum());
				ls.setLotteryType(lotteryCount.getLotteryType());

				lotterySales.add(ls);
			}

			centerSaleDetail.setLotterySales(lotterySales);
			centerSaleDetail.setDate(date);
			centerSaleDetail.setUserAddNum(uc.getUserDailyAddNum());
			centerSaleDetails.add(centerSaleDetail);
		}

		resultObj.setRecordTotal(userCountDao.getRecordTotal(stationInfo,
				fromDate, toDate));
		resultObj.setObjects(centerSaleDetails);
		return pcResultObj(resultObj);
	}

	@Override
	public PcResultObj getBranchInCenter(StationInfo stationInfo,
			String keyword, int pageNum, int pageSize) {

		ResultObj resultObj = new ResultObj();
		Center centerManage = new Center();
		if (stationInfo == null) {
			return checkStation(stationInfo);
		}
		List<StationInfo> stationInfos = stationInfoDao.getStationInfoByWhere(
				stationInfo, "", keyword, pageNum, pageSize, 0);
		int[] num = getChildStationNum(stationInfo);

		centerManage.setAllBranchNum(num[0]);
		centerManage.setAllbettingShopNum(num[1]);

		resultObj.setRecordTotal(stationInfoDao.getRecordTotalByWhere(
				stationInfo, "", keyword, 0));
		centerManage.setBranchInfos(getBranchInfos(stationInfos));
		resultObj.setObjects(centerManage);

		return pcResultObj(resultObj);
	}

	@Override
	public PcResultObj getBettingInfoInBranch(StationInfo stationInfo,
			String addressOne, String keyword, int pageNum, int pageSize) {

		ResultObj resultObj = new ResultObj();
		Branch branchManage = new Branch();

		if (stationInfo == null) {
			return checkStation(stationInfo);
		}

		List<StationInfo> stationInfos = stationInfoDao.getStationInfoByWhere(
				stationInfo, addressOne, keyword, pageNum, pageSize, 0);

		branchManage.setBettingShopNum(getChildStationNum(stationInfo)[1]);
		branchManage.setBranchName(stationInfo.getStationName());

		resultObj.setRecordTotal(stationInfoDao.getRecordTotalByWhere(
				stationInfo, addressOne, keyword, 0));

		branchManage.setBettingShopInfos(getBettingShopInfos(stationInfo,
				stationInfos));
		resultObj.setObjects(branchManage);

		return pcResultObj(resultObj);

	}

	@Override
	public PcResultObj getBettingInfoInCenter(StationInfo stationInfo,
			String addressOne, String keyword, int pageNum, int pageSize,
			int flag) {

		ResultObj resultObj = new ResultObj();
		Branch branchManage = new Branch();

		if (stationInfo == null) {
			return checkStation(stationInfo);
		}

		int[] num = getChildStationNum(stationInfo);

		List<StationInfo> bettingShopInCenter = stationInfoDao
				.getStationInfoByWhere(stationInfo, addressOne, keyword,
						pageNum, pageSize, flag);

		// List<BettingShopInBranch> bettingShopInfos = new
		// ArrayList<BettingShopInBranch>();
		// for (int i = 0; i < bettingShopInCenter.size(); i++) {
		//
		// BettingShopInBranch bsi = new BettingShopInBranch();
		// si = bettingShopInCenter.get(i);
		// bsi.setAddress(si.getStationAddOne() + si.getStationAddTwo());
		// bsi.setBettingShopName(si.getStationName());
		// bsi.setCode(si.getStationCode());
		// bsi.setLinkMan(si.getStationLinkman());
		// bsi.setPhone(si.getStationTel());
		// bsi.setBranchName(si.getParentStationName());
		// bsi.setBettingShopId(si.getId());
		// bettingShopInfos.add(bsi);
		// }

		branchManage.setBettingShopInfos(getBettingShopInfos(stationInfo,
				bettingShopInCenter));

		branchManage.setBettingShopNum(num[0]);
		branchManage.setBranchName(stationInfo.getStationName());

		resultObj.setRecordTotal(stationInfoDao.getRecordTotalByWhere(
				stationInfo, addressOne, keyword, flag));

		resultObj.setObjects(branchManage);
		return pcResultObj(resultObj);
	}

	@Override
	public int[] getStationSummarize(StationInfo stationInfo, String date) {

		int[] dailySaleAndUserAddNum = new int[4];
		UserCount uc = new UserCount();
		LotteryCount lc = new LotteryCount();
		List<UserCount> userCounts = new ArrayList<UserCount>();
		List<LotteryCount> lotteryCounts = new ArrayList<LotteryCount>();

		uc.setCountTime(date);
		uc.setStationId(stationInfo.getId());
		userCounts = userCountDao.getUserCountByWhereAnd(uc);

		if (userCounts.size() > 0) {
			dailySaleAndUserAddNum[0] = userCounts.get(0).getUserDailyAddNum();
			dailySaleAndUserAddNum[1] = userCounts.get(0).getUserTotalNum();
		}

		lc.setCountTime(date);
		lc.setStationId(stationInfo.getId());
		lotteryCounts = lotteryCountDao.getLotteryCountByWhereAnd(lc);

		for (int i = 0; i < lotteryCounts.size(); i++) {
			dailySaleAndUserAddNum[2] += lotteryCounts.get(i)
					.getLotteryDailyNum();
			dailySaleAndUserAddNum[3] += lotteryCounts.get(i)
					.getLotteryMonthNum();
		}

		return dailySaleAndUserAddNum;
	}

	@Override
	public int[] getChildStationNum(StationInfo stationInfo) {
		int[] childStationNum = new int[2];

		if (stationInfo.getStationFlag() == 1) {

			int grandChildNum = 0;
			childStationNum[0] = stationInfoDao.getChildStationNum(stationInfo);

			List<StationInfo> stationInfos = getChildStations(stationInfo);

			for (int i = 0; i < stationInfos.size(); i++) {

				grandChildNum += stationInfoDao.getChildStationNum(stationInfos
						.get(i));

			}
			childStationNum[1] = grandChildNum;

		} else if (stationInfo.getStationFlag() == 2) {

			childStationNum[0] = 0;
			childStationNum[1] = stationInfoDao.getChildStationNum(stationInfo);

		} else {

			childStationNum[0] = 0;
			childStationNum[1] = 0;
		}

		return childStationNum;
	}

	@Override
	public List<StationInfo> getChildStations(StationInfo stationInfo) {
		StationInfo si = new StationInfo();
		si.setParentId(stationInfo.getId());

		List<StationInfo> childStations = getStationByWhereAnd(si);

		for (int i = 0; i < childStations.size(); i++) {

			if (childStations.get(i).getId() == (stationInfo.getId())) {

				childStations.remove(i);
			}
		}

		return childStations;
	}

	/**
	 * 中心下分中心信息封装到分中心信息列表BranchInfos中
	 * 
	 * @param stationInfos
	 * @return
	 */
	public List<BranchInCenter> getBranchInfos(List<StationInfo> stationInfos) {

		List<BranchInCenter> branchInfos = new ArrayList<BranchInCenter>();
		StationInfo si = new StationInfo();
		int[] num = new int[2];

		for (int i = 0; i < stationInfos.size(); i++) {

			BranchInCenter bi = new BranchInCenter();
			si = stationInfos.get(i);
			num = getChildStationNum(si);

			bi.setAddress(si.getStationAddOne() + si.getStationAddTwo());
			bi.setBranchName(si.getStationName());
			bi.setLinkMan(si.getStationLinkman());
			bi.setPhone(si.getStationTel());
			bi.setBettingShopNum(num[1]);
			bi.setBranchId(si.getId());
			branchInfos.add(bi);
		}

		return branchInfos;
	}

	/**
	 * 封装分中心下所有投注站的信息列表
	 * 
	 * @param stationInfo
	 * @param stationInfos
	 * @return
	 */
	public List<BettingShopInBranch> getBettingShopInfos(
			StationInfo stationInfo, List<StationInfo> stationInfos) {

		StationInfo si = new StationInfo();

		List<BettingShopInBranch> bettingShopInfos = new ArrayList<BettingShopInBranch>();
		for (int i = 0; i < stationInfos.size(); i++) {

			si = stationInfos.get(i);
			BettingShopInBranch bsi = new BettingShopInBranch();

			bsi.setAddress(si.getStationAddOne() + si.getStationAddTwo());
			bsi.setBettingShopName(si.getStationName());
			bsi.setCode(si.getStationCode());
			bsi.setLinkMan(si.getStationLinkman());
			bsi.setBranchId(si.getParentId());
			bsi.setPhone(si.getStationTel());
			bsi.setBranchName(si.getParentStationName());
			bsi.setBettingShopId(si.getId());
			bettingShopInfos.add(bsi);
		}

		return bettingShopInfos;
	}

	public PcResultObj checkStation(StationInfo stationInfo) {
		PcResultObj pcResultObj = new PcResultObj();

		if (stationInfo == null) {

			pcResultObj.setMsg("没有权限!");
			pcResultObj.setMsgCode("2");
		}
		return pcResultObj;
	}

	public PcResultObj pcResultObj(ResultObj resultObj) {

		PcResultObj pcResultObj = new PcResultObj();

		if (resultObj.getRecordTotal() >= 0) {

			pcResultObj.setEntity(resultObj);
			pcResultObj.setMsgCode("1");
			pcResultObj.setMsg("查询成功");

		} else {

			pcResultObj.setMsgCode("2");
			pcResultObj.setMsg("查询失败");

		}

		return pcResultObj;

	}

	public PcResultObj ifStationExist(StationInfo stationInfo){
		
		PcResultObj pcResultObj = new PcResultObj();
		StationInfo si = new StationInfo();
		
		if(stationInfo.getStationFlag() == BRANCH_FLAG){
			
			si.setStationName(stationInfo.getStationName());
			si.setStationFlag(BRANCH_FLAG);


			List<StationInfo> stationInfos = stationInfoDao
					.getStationInfoByWhereAnd(si);

			if (stationInfos.size() > 0) {
			

				pcResultObj.setEntity(2);
				pcResultObj.setMsg("名称已存在");
				pcResultObj.setMsgCode("2");

				return pcResultObj;
			}else{
				pcResultObj.setEntity(1);
				pcResultObj.setMsg("名称不存在");
				pcResultObj.setMsgCode("1");
				
				
			}
		}
		
		if(stationInfo.getStationFlag() == BETTINGSHOP_FLAG){
			
			si.setStationCode(stationInfo.getStationCode());
			si.setStationFlag(stationInfo.getStationFlag());

			List<StationInfo> stationInfos = stationInfoDao
					.getStationInfoByWhereAnd(si);

			if (stationInfos.size() > 0) {

				pcResultObj.setEntity(2);
				pcResultObj.setMsg("站号已存在");
				pcResultObj.setMsgCode("2");

			}else{
				
				pcResultObj.setEntity(1);
				pcResultObj.setMsg("站号不存在");
				pcResultObj.setMsgCode("1");
				
			}
			
		}
		
		return pcResultObj;
	}
	
	
	
}
