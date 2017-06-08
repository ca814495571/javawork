package com.cqfc.management.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.management.dao.LotteryCountDao;
import com.cqfc.management.dao.StationInfoDao;
import com.cqfc.management.dao.UserCountDao;
import com.cqfc.management.model.BettingShopBaseInfo;
import com.cqfc.management.model.BettingShopInBranch;
import com.cqfc.management.model.BettingShopInCenter;
import com.cqfc.management.model.BranchBaseInfo;
import com.cqfc.management.model.BranchInCenterManage;
import com.cqfc.management.model.BranchManage;
import com.cqfc.management.model.CenterManage;
import com.cqfc.management.model.LotteryCount;
import com.cqfc.management.model.LotterySale;
import com.cqfc.management.model.LotterySaleDetail;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.StationMap;
import com.cqfc.management.model.UserCount;
import com.cqfc.management.service.IStationInfoService;

@Service
public class StationInfoServiceImpl implements IStationInfoService {

	@Autowired
	private StationInfoDao stationInfoDao;

	@Autowired
	private UserCountDao userCountDao;

	@Autowired
	private LotteryCountDao lotteryCountDao;

	@Override
	public int insertBranch(StationInfo stationInfo) {
		stationInfo.setStationFlag(BRANCH_FLAG);
		stationInfo.setParentId(CENTER_ID);
		return stationInfoDao.insertStationInfo(stationInfo);
	}

	@Override
	public int insertBettingShop(StationInfo stationInfo) {

		if (!"".equals(stationInfo.getParentId())) {

			int parentId = stationInfo.getParentId();
			stationInfo.setParentStationName(stationInfoDao.getStationInfoById(
					parentId).getStationName());
		}

		stationInfo.setStationFlag(BETTINGSHOP_FLAG);
		return stationInfoDao.insertStationInfo(stationInfo);
	}

	@Override
	public int updateStation(int id, StationInfo si) {

		StationInfo stationInfo = getStationInfoById(id);

		if (!"".equals(si.getStationName()) && si.getStationName() != null) {

			stationInfo.setStationName(si.getStationName());

		}

		if (!"".equals(si.getParentId())) {

			int parentId = si.getParentId();
			stationInfo.setParentStationName(stationInfoDao.getStationInfoById(
					parentId).getStationName());
		}

		if (!"".equals(si.getStationAddOne()) && si.getStationAddOne() != null) {

			stationInfo.setStationAddOne(si.getStationAddOne());

		}

		if (!"".equals(si.getStationAddTwo()) && si.getStationAddTwo() != null) {

			stationInfo.setStationAddTwo(si.getStationAddTwo());

		}

		if (!"".equals(si.getStationCode()) && si.getStationCode() != null) {

			stationInfo.setStationCode(si.getStationCode());
		}

		if (!"".equals(si.getStationLinkman())
				&& si.getStationLinkman() != null) {

			stationInfo.setStationLinkman(si.getStationLinkman());
		}

		if (!"".equals(si.getStationTel()) && si.getStationTel() != null) {

			stationInfo.setStationTel(si.getStationTel());
		}

		if (!"".equals(si.getStationLongitude())
				&& si.getStationLongitude() != null) {

			stationInfo.setStationLongitude(si.getStationLongitude());
		}

		if (!"".equals(si.getStationLatitude())
				&& si.getStationLatitude() != null) {

			stationInfo.setStationLatitude(si.getStationLatitude());
		}

		return stationInfoDao.updateStationInfo(stationInfo);
	}

	@Override
	public int deleteStation(int id) {

		StationInfo stationInfo = getStationInfoById(id);

		stationInfo.setActive(2);

		return stationInfoDao.updateStationInfo(stationInfo);
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
	public int updateStationInfo(StationInfo stationInfo) {
		return stationInfoDao.updateStationInfo(stationInfo);
	}

	@Override
	public List<StationInfo> getStationByWhereAnd(StationInfo stationInfo) {

		return stationInfoDao.getStationInfoByWhereAnd(stationInfo);
	}

	@Override
	public Map<String, Object> getBranchInfo(int id) {

		Map<String, Object> map = new HashMap<String, Object>();

		BranchBaseInfo branchBaseInfo = new BranchBaseInfo();

		StationInfo stationInfo = getStationInfoById(id);

		branchBaseInfo.setBranchCode(stationInfo.getStationCode());
		branchBaseInfo.setBranchLinkman(stationInfo.getStationLinkman());
		branchBaseInfo.setBranchName(stationInfo.getStationName());
		branchBaseInfo.setBranchTel(stationInfo.getStationTel());

		map.put(PARAM_LIST, branchBaseInfo);
		map.put(PARAM_RECORDTOTAL, 1);

		return map;
	}

	@Override
	public Map<String, Object> getBettingShopInfo(int id) {

		Map<String, Object> map = new HashMap<String, Object>();

		StationInfo stationInfo = getStationInfoById(id);
		BettingShopBaseInfo bettingShopBaseInfo = new BettingShopBaseInfo();

		bettingShopBaseInfo.setBettingShopName(stationInfo.getStationName());
		bettingShopBaseInfo.setBettingShopCode(stationInfo.getStationCode());
		bettingShopBaseInfo.setBettingShopTel(stationInfo.getStationTel());
		bettingShopBaseInfo.setBranchId(stationInfo.getParentId());
		bettingShopBaseInfo.setBranchName(stationInfo.getParentStationName());
		bettingShopBaseInfo.setAddOne(stationInfo.getStationAddOne());
		bettingShopBaseInfo.setAddTwo(stationInfo.getStationAddTwo());
		bettingShopBaseInfo.setLon(stationInfo.getStationLongitude());
		bettingShopBaseInfo.setLat(stationInfo.getStationLatitude());

		map.put(PARAM_LIST, bettingShopBaseInfo);
		map.put(PARAM_RECORDTOTAL, 1);
		return map;
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
	public Map<String, Object> getLotterySaleByDate(StationInfo stationInfo,
			String fromDate, String toDate, int pageNum, int pageSize) {

		Map<String, Object> map = new HashMap<String, Object>();

		List<LotterySale> sscb = new ArrayList<LotterySale>();

		List<LotteryCount> lotteryCounts = lotteryCountDao
				.getLotteryCountByDate(stationInfo, fromDate, toDate, pageNum,
						pageSize);

		LotteryCount lc = null;

		for (int i = 0; i < lotteryCounts.size(); i++) {

			LotterySale ls = new LotterySale();
			lc = lotteryCounts.get(i);

			ls.setDay(Integer.parseInt(lc.getCountTime().split("-")[2]));
			ls.setSaleNum(lc.getLotteryDailyNum());
			ls.setLotteryType(lc.getLotteryType());

			sscb.add(ls);
		}

		map.put(PARAM_RECORDTOTAL,
				lotteryCountDao.getRecordTotal(stationInfo, fromDate, toDate));
		map.put(PARAM_LIST, sscb);
		return map;
	}

	@Override
	public Map<String, Object> getLotterySaleDetail(StationInfo stationInfo,
			String fromDate, String toDate, int pageNum, int pageSize) {

		Map<String, Object> map = new HashMap<String, Object>();

		List<LotterySaleDetail> centerSaleDetails = new ArrayList<LotterySaleDetail>();

		List<UserCount> userCounts = userCountDao.getUserCountByDate(
				stationInfo, fromDate, toDate, pageNum, pageSize);

		List<LotteryCount> lotteryCounts = null;

		LotteryCount lc = new LotteryCount();
		lc.setStationId(stationInfo.getId());
		UserCount uc = null;
		String date = "";

		for (int i = 0; i < userCounts.size(); i++) {

			LotterySaleDetail centerSaleDetail = new LotterySaleDetail();
			List<LotterySale> lotterySales = new ArrayList<LotterySale>();
			uc = userCounts.get(i);
			date = uc.getCountTime();

			lc.setCountTime(date);

			lotteryCounts = lotteryCountDao.getLotteryCountByWhereAnd(lc);

			for (int j = 0; j < lotteryCounts.size(); j++) {

				LotteryCount lotteryCount = lotteryCounts.get(j);
				LotterySale ls = new LotterySale();

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

		map.put(PARAM_RECORDTOTAL,
				userCountDao.getRecordTotal(stationInfo, fromDate, toDate));
		map.put(PARAM_LIST, centerSaleDetails);
		return map;
	}

	@Override
	public Map<String, Object> getCenterManage(StationInfo stationInfo,
			String keyword, int pageNum, int pageSize) {

		Map<String, Object> map = new HashMap<String, Object>();
		CenterManage centerManage = new CenterManage();

		List<StationInfo> stationInfos = stationInfoDao.getStationInfoByWhere(
				stationInfo, "", keyword, pageNum, pageSize, 0);
		int[] num = getChildStationNum(stationInfo);

		centerManage.setAllBranchNum(num[0]);
		centerManage.setAllbettingShopNum(num[1]);

		map.put(PARAM_RECORDTOTAL, stationInfos.size());
		centerManage.setBranchInfos(getBranchInfos(stationInfos));
		map.put(PARAM_LIST, centerManage);
		return map;
	}

	@Override
	public Map<String, Object> getBranchManage(StationInfo stationInfo,
			String addressOne, String keyword, int pageNum, int pageSize) {

		Map<String, Object> map = new HashMap<String, Object>();

		BranchManage branchManage = new BranchManage();
		List<StationInfo> stationInfos = stationInfoDao.getStationInfoByWhere(
				stationInfo, addressOne, keyword, pageNum, pageSize, 0);

		branchManage.setBettingShopNum(getChildStationNum(stationInfo)[1]);
		branchManage.setBranchName(stationInfo.getStationName());

		map.put(PARAM_RECORDTOTAL, stationInfoDao.getRecordTotalByWhere(
				stationInfo, addressOne, keyword, 0));

		branchManage.setBettingShopInfos(getBettingShopInfos(stationInfo,
				stationInfos));
		map.put(PARAM_LIST, branchManage);
		return map;

	}

	@Override
	public Map<String, Object> getAllBettingInfoInCenter(
			StationInfo stationInfo, String addressOne, String keyword,
			int pageNum, int pageSize, int flag) {

		Map<String, Object> map = new HashMap<String, Object>();
		BettingShopInCenter allBettingInfoInCenter = new BettingShopInCenter();

		StationInfo si = null;
		int[] num = getChildStationNum(stationInfo);

		List<StationInfo> bettingShopInCenter = stationInfoDao
				.getStationInfoByWhere(stationInfo, addressOne, keyword,
						pageNum, pageSize, flag);

		List<BettingShopInBranch> bettingShopInfos = new ArrayList<BettingShopInBranch>();
		for (int i = 0; i < bettingShopInCenter.size(); i++) {

			BettingShopInBranch bsi = new BettingShopInBranch();
			si = bettingShopInCenter.get(i);
			bsi.setAddress(si.getStationAddOne() + si.getStationAddTwo());
			bsi.setBettingShopName(si.getStationName());
			bsi.setCode(si.getStationCode());
			bsi.setLinkMan(si.getStationLinkman());
			bsi.setPhone(si.getStationTel());
			bsi.setBranchName(si.getParentStationName());
			bsi.setBettingShopId(si.getId());
			bettingShopInfos.add(bsi);
		}

		allBettingInfoInCenter.setBettingShopInfos(bettingShopInfos);

		allBettingInfoInCenter.setBettingShopNum(num[0]);
		allBettingInfoInCenter.setBranchNum(num[1]);

		map.put(PARAM_RECORDTOTAL, stationInfoDao.getRecordTotalByWhere(
				stationInfo, addressOne, keyword, flag));

		map.put(PARAM_LIST, allBettingInfoInCenter);
		return map;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public Map<String, Object> getAllBettingInfoInCenter(
	// StationInfo stationInfo, String addressOne, String keyword,
	// int pageNum, int pageSize) {
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// BettingShopInCenter allBettingInfoInCenter = new BettingShopInCenter();
	//
	// List<StationInfo> branchs = getChildStations(stationInfo);
	//
	// List<BettingShopInBranch> bettingShopInBranch = new
	// ArrayList<BettingShopInBranch>();
	// List<BettingShopInBranch> bettingShopInCenter = new
	// ArrayList<BettingShopInBranch>();
	// StationInfo si = null;
	// int[] num = getChildStationNum(stationInfo);
	//
	// for (int i = 0; i < branchs.size(); i++) {
	//
	// si = branchs.get(i);
	// bettingShopInBranch = getBettingShopInfos(si,
	// stationInfoDao.getStationInfoByWhere(si, addressOne,
	// keyword,0,0));
	//
	// for (int j = 0; j < bettingShopInBranch.size(); j++) {
	//
	// bettingShopInCenter.add(bettingShopInBranch.get(j));
	// }
	//
	// }
	// allBettingInfoInCenter.setBettingShopNum(num[0]);
	// allBettingInfoInCenter.setBranchNum(num[1]);
	//
	// map.put(PARAM_RECORDTOTAL, bettingShopInCenter.size());
	//
	// bettingShopInCenter = (List<BettingShopInBranch>) getListsByPageNum(
	// bettingShopInCenter, pageNum, pageSize);
	// allBettingInfoInCenter.setBettingShopInfos(bettingShopInCenter);
	// map.put(PARAM_LIST, allBettingInfoInCenter);
	// return map;
	// }

	// ==============================公用部分===============================//

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
	public List<BranchInCenterManage> getBranchInfos(
			List<StationInfo> stationInfos) {

		List<BranchInCenterManage> branchInfos = new ArrayList<BranchInCenterManage>();
		StationInfo si = new StationInfo();
		int[] num = new int[2];

		for (int i = 0; i < stationInfos.size(); i++) {

			BranchInCenterManage bi = new BranchInCenterManage();
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
			bsi.setPhone(si.getStationTel());
			bsi.setBranchName(stationInfo.getStationName());
			bsi.setBettingShopId(si.getId());
			bettingShopInfos.add(bsi);
		}

		return bettingShopInfos;
	}

	/**
	 * 通用的web分页
	 * 
	 * @param lists
	 * @param pageNum
	 * @return
	 */
	// public List<?> getListsByPageNum(List<?> lists, int pageNum, int
	// pageSize) {
	//
	// if (pageNum < 1) {
	//
	// pageNum = 1;
	// }
	// if (pageSize < 1 || pageSize > 200) {
	//
	// pageSize = 15;
	// }
	// int startNum = (pageNum - 1) * pageSize;
	// List<Object> objects = new ArrayList<Object>();
	//
	// if (pageSize > lists.size()) {
	//
	// for (int i = startNum; i < lists.size(); i++) {
	//
	// objects.add(lists.get(i));
	// }
	// } else {
	//
	// for (int i = startNum; i < pageSize; i++) {
	//
	// objects.add(lists.get(i));
	// }
	// }
	// return objects;
	//
	// }

}
