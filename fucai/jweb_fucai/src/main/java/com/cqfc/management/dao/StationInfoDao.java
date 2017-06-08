package com.cqfc.management.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.management.dao.mapper.StationInfoMapper;
import com.cqfc.management.model.StationInfo;
import com.cqfc.management.service.IStationInfoService;

@Repository
public class StationInfoDao {

	@Autowired
	private StationInfoMapper stationInfoMapper;

	/**
	 * 插入记录
	 * 
	 * @param stationInfo
	 * @return
	 */
	public int insertStationInfo(StationInfo stationInfo) {

		filterNullColumn(stationInfo);
		return stationInfoMapper.insertStationInfo(stationInfo);
	}

	/**
	 * 根据Id查询记录
	 * 
	 * @param id
	 * @return
	 */
	public StationInfo getStationInfoById(int id) {

		return stationInfoMapper.getStationInfoById(id);
	};

	/**
	 * 查询所有记录
	 * 
	 * @return
	 */
	public List<StationInfo> getStationAll() {
		return stationInfoMapper.getStationInfoAll();
	}

	/**
	 * 修改记录
	 * 
	 * @param stationInfo
	 * @return
	 */
	public int updateStationInfo(StationInfo stationInfo) {

		filterNullColumn(stationInfo);
		return stationInfoMapper.updateStationInfo(stationInfo);
	}

	/**
	 * SELECT语句（条件格式为 ：1=1 and fieldName1 = value1 and fieldName2 = value2
	 * .....）
	 * 
	 * @param stationInfo
	 * @return
	 */
	public List<StationInfo> getStationInfoByWhereAnd(StationInfo stationInfo) {

		List<StationInfo> stationInfos = new ArrayList<StationInfo>();
		filterNullColumn(stationInfo);

		StringBuffer where = new StringBuffer(" 1=1");

		if (!"".equals(stationInfo.getId()) && stationInfo.getId() != 0) {
			where.append(" and id = '");
			where.append(stationInfo.getId());
			where.append("'");
		}
		if (!"".equals(stationInfo.getParentId())
				&& stationInfo.getParentId() != 0) {
			where.append(" and parentId = '");
			where.append(stationInfo.getParentId());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationName())) {
			where.append(" and stationName = '");
			where.append(stationInfo.getStationName());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationLinkman())) {
			where.append(" and stationLinkman = '");
			where.append(stationInfo.getStationLinkman());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationTel())) {
			where.append(" and stationTel = '");
			where.append(stationInfo.getStationTel());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationAddOne())) {
			where.append(" and stationAddOne = '");
			where.append(stationInfo.getStationAddOne());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationAddTwo())) {
			where.append(" and stationAddTwo = '");
			where.append(stationInfo.getStationAddTwo());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationCode())) {
			where.append(" and stationCode = '");
			where.append(stationInfo.getStationCode());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationOrgLevel())) {
			where.append(" and stationOrgLevel = '");
			where.append(stationInfo.getStationOrgLevel());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationOrg())) {
			where.append(" and stationOrg = '");
			where.append(stationInfo.getStationOrg());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationLongitude())) {
			where.append(" and stationLongitude = '");
			where.append(stationInfo.getStationLongitude());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationLatitude())) {
			where.append(" and stationLatitude = '");
			where.append(stationInfo.getStationLatitude());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationAccountNum())) {
			where.append(" and stationAccountNum = '");
			where.append(stationInfo.getStationAccountNum());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationPassword())) {
			where.append(" and stationPassword = '");
			where.append(stationInfo.getStationPassword());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationCreateTime())
				&& !"0000-00-00".equals(stationInfo.getStationCreateTime())
				&& stationInfo.getStationCreateTime() != null) {
			where.append(" and stationCreateTime = '");
			where.append(stationInfo.getStationCreateTime());
			where.append("'");
		}
		if (!"".equals(stationInfo.getStationFlag())
				&& stationInfo.getStationFlag() != 0) {
			where.append(" and stationFlag = '");
			where.append(stationInfo.getStationFlag());
			where.append("'");
		}
		if (!"".equals(stationInfo.getEnterTag())
				&& stationInfo.getEnterTag() != null) {
			where.append(" and enterTag = '");
			where.append(stationInfo.getEnterTag());
			where.append("'");
		}

		System.out.println(where.toString());
		stationInfos = stationInfoMapper.getStationInfoByWhereAnd(where
				.toString());
		// return where.toString();
		return stationInfos;
	}

	/**
	 * 过滤掉null值的字段设置默认值
	 * 
	 * @param stationInfo
	 */
	public void filterNullColumn(StationInfo stationInfo) {
		if (stationInfo.getStationName() == null) {

			stationInfo.setStationName("");
		}

		if (stationInfo.getStationLinkman() == null) {

			stationInfo.setStationLinkman("");
		}

		if (stationInfo.getStationTel() == null) {

			stationInfo.setStationTel("");
		}

		if (stationInfo.getStationAddOne() == null) {

			stationInfo.setStationAddOne("");
		}

		if (stationInfo.getStationAddTwo() == null) {

			stationInfo.setStationAddTwo("");
		}

		if (stationInfo.getStationCode() == null) {

			stationInfo.setStationCode("");
		}

		if (stationInfo.getStationOrgLevel() == null) {

			stationInfo.setStationOrgLevel("");
		}

		if (stationInfo.getStationOrg() == null) {

			stationInfo.setStationOrg("");

		}
		if (stationInfo.getStationLongitude() == null) {

			stationInfo.setStationLongitude("");
		}
		if (stationInfo.getStationLatitude() == null) {

			stationInfo.setStationLatitude("");
		}
		if (stationInfo.getStationAccountNum() == null) {

			stationInfo.setStationAccountNum("");
		}
		if (stationInfo.getStationPassword() == null) {

			stationInfo.setStationPassword("");
		}
		if (stationInfo.getParentStationName() == null) {

			stationInfo.setParentStationName("");
		}
		if (stationInfo.getEnterTag() == null) {

			stationInfo.setEnterTag("");
		}

	}

	/**
	 * 查询子站点数目
	 * 
	 * @param stationInfo
	 * @return
	 */
	public int getChildStationNum(StationInfo stationInfo) {

		return stationInfoMapper.getChildStationNum(stationInfo);

	}


	/**
	 * 根据条件查询该站点下一层站点信息
	 * @param stationInfo
	 * @param addressOne
	 * @param fieldValue
	 * @return
	 */
	public List<StationInfo> getStationInfoByWhere(
			StationInfo stationInfo, String addressOne, String keyword , int pageNum , int pageSize , int flag) {

		StringBuffer sb = new StringBuffer(" 1=1 ");
		
		if(!"".equals(stationInfo.getId()) && stationInfo.getId()!=IStationInfoService.CENTER_ID){
			sb.append("parentId = '") ;
			sb.append(stationInfo.getId());
			sb.append("'");
		}
		
		
		if (addressOne != null && !"".equals(addressOne)) {
			
			sb.append(" and stationAddOne = '");
			sb.append(addressOne);
			sb.append("'");
		}
		
		if (flag != 0 && !"".equals(flag)) {
			
			sb.append(" and stationFlag = '");
			sb.append(flag);
			sb.append("'");
		}

		if (keyword != null && !"".equals(keyword)) {
			
			sb.append(" and ( stationCode like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(" or stationName like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(" or stationLinkman like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(")");
		}
		
		sb.append(" and active = 1 ") ;
		
		if(pageNum != 0 && pageSize!=0){
			
			sb.append(" limit ");
			sb.append((pageNum-1)*pageSize) ;
			sb.append(",");
			sb.append(pageSize) ;
			
		}
		System.out.println(sb.toString());
		
		
		return stationInfoMapper.getStationInfoByWhere(sb.toString());

	}
	
	
	public int getRecordTotalByWhere(StationInfo stationInfo, String addressOne, String keyword,int flag){
		
		StringBuffer sb = new StringBuffer(" 1=1 ");
		
		if(!"".equals(stationInfo.getId()) && stationInfo.getId()!=IStationInfoService.CENTER_ID){
			sb.append("parentId = '") ;
			sb.append(stationInfo.getId());
			sb.append("'");
		}
		
		if (addressOne != null && !"".equals(addressOne)) {
			
			sb.append(" and stationAddOne ='");
			sb.append(addressOne);
			sb.append("'");
		}
		
		if (flag != 0 && !"".equals(flag)) {
			
			sb.append(" and stationFlag = '");
			sb.append(flag);
			sb.append("'");
		}
	
		if (keyword != null && !"".equals(keyword)) {
			
			sb.append(" and ( stationCode like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(" or stationName like '%");
			sb.append(keyword);
			sb.append("%'");
			sb.append(")");
		}
		
		sb.append(" and active = 1 ");
		
		System.out.println(sb.toString());
		
		return stationInfoMapper.getRecordTotalByWhere(sb.toString());
	}
	
	

}
