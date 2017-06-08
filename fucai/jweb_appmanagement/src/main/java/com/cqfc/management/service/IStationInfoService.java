package com.cqfc.management.service;

import java.util.List;

import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.ResultObj;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.model.StationMap;
import com.cqfc.management.model.UserInfo;

public interface IStationInfoService {

	public final static int CENTER_ID = 16;
	public final static int CENTER_FLAG = 1;
	public final static int BRANCH_FLAG = 2;
	public final static int BETTINGSHOP_FLAG = 3;
	public final static String PARAM_LIST = "list";
	public final static String PARAM_RECORDTOTAL = "recordTotal";

	/**
	 * 根据Id查询数据
	 * 
	 * @param id
	 * @return
	 */
	public StationInfo getStationInfoById(int id);

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	public List<StationInfo> getStationInfoAll();


	/**
	 * 条件查询（格式：1=1 and id =1 and name =2 .....）
	 * 
	 * @param stationInfo
	 * @return
	 */
	public List<StationInfo> getStationByWhereAnd(StationInfo stationInfo);

	/**
	 * 查询站点地图信息（Json 格式）
	 * 
	 * @return
	 */
	public List<StationMap> getStationMaps();

	/**
	 * 查询该子站点数据
	 * 
	 * @param stationInfo
	 * @return
	 */
	public List<StationInfo> getChildStations(StationInfo stationInfo);

	// /**
	// * 根据条件获取(中心或者分中心）下投注站的基本信息
	// *
	// * @param stationInfo
	// * @param addressOne
	// * @param fieldValue
	// * @return
	// */
	// public PcBettingShopInfo getBettingShopInfos(StationInfo stationInfo,
	// String addressOne, String fieldValue);

	/**
	 * 查询站点子站点的数目(中心【分站点数，投注站数目】 ， 分中心【0，投注站数】，投注站【0，0】)
	 * 
	 * @param stationInfo
	 * @return
	 */
	public int[] getChildStationNum(StationInfo stationInfo);

	/**
	 * 获取站点下的总概况格式[日增加人数，总人数，日销售量，月销售量]
	 * 
	 * @param stationInfo
	 * @param Date
	 * @return
	 */
	public int[] getStationSummarize(StationInfo stationInfo, String Date);

	// ============================================================

	/**
	 * 插入分中心和投注站信息
	 * 
	 * @param stationInfo
	 * @return
	 */
	public PcResultObj insertStation(UserInfo user, StationInfo stationInfo);

	/**
	 * 修改分中心和投注站信息
	 * 
	 * @param si
	 * @return
	 */
	public PcResultObj updateStation(UserInfo user, StationInfo si);

	/**
	 * 删除分中心和投注站
	 * 
	 * @param id
	 * @return
	 */
	public PcResultObj deleteStation(int id);

	/**
	 * 根据Id 获取分中心或者投注站的基本信息
	 * 
	 * @param id
	 * @return
	 */

	public PcResultObj getStationInfo(int id);

	/**
	 * 根据时间段查询站点日销售量(用于图形化)
	 * 
	 * @param stationInfo
	 * @param fromDate
	 *            toDate
	 * @return
	 */
	public ResultObj getLotterySaleByDate(StationInfo stationInfo,
			String fromDate, String toDate, int pageNum, int pageSize);

	/**
	 * 根据时间段查询站点详细信息（各彩种销售量和用户增加数量）
	 * 
	 * @param stationInfo
	 * @return
	 */
	public PcResultObj getLotterySaleDetail(StationInfo stationInfo,
			String fromDate, String toDate);

	/**
	 * 获取中心下所有分中心的基本信息
	 * 
	 * @param stationInfo
	 * @return
	 */
	public PcResultObj getBranchInCenter(StationInfo stationInfo,
			String keyWord, int pageNum, int pageSize);

	/**
	 * 获取分中心下所有投注站的基本信息
	 * 
	 * @param stationInfo
	 * @return
	 */
	public PcResultObj getBettingInfoInBranch(StationInfo stationInfo,
			String addressOne, String fieldValue, int pageNum, int pageSize);

	/**
	 * 获取中心下所有投注站信息
	 * 
	 * @return
	 */
	public PcResultObj getBettingInfoInCenter(StationInfo stationInfo,
			String addressOne, String fieldValue, int pageNum, int pageSize,
			int flag);

	
	
	
}
