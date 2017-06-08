package com.cqfc.management.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cqfc.management.model.StationInfo;
import com.cqfc.common.dao.BaseMapper;

public interface StationInfoMapper extends BaseMapper {
	/*
	 * private int id; private StationInfo stationInfo; // 父站点 private String
	 * stationName; // 站点名称 private String stationLinkman; // 站点联系人 private
	 * String stationTel; // 站点联系方式 private String stationAddOne; //
	 * 站点所属区地址（例：渝中区） private String stationAddTwo; // 站点所属区地址后面的详细地址 private
	 * String stationCode; // 站点站号 private String stationOrgLevel; // 站点组织级别
	 * private String stationOrg; // 站点所属组织 private String stationLongitude; //
	 * 站点百度地图经度 private String stationLatitude; // 站点百度地图纬度 private String
	 * stationAccountNum; // 站点帐号 private String stationPassword; // 站点密码
	 * private String stationCreateTime; // 站点创建时间 private int stationFlag; //
	 * 站点标识 private String lastTime; // 站点最后修改时间
	 */
	/**
	 * 指定列名id为自动生成主键的列
	 * 
	 * @param stationInfo
	 * @return
	 */
	@Insert("insert into t_station_info "
			+ "(parentId,stationName,stationLinkman,stationTel,stationAddOne,stationAddTwo,"
			+ "stationCode,stationOrgLevel,stationOrg,stationLongitude,stationLatitude,"
			+ "stationAccountNum,stationPassword,stationCreateTime,stationFlag,lastTime,parentStationName,enterTag) "
			+ "values (#{parentId},#{stationName},#{stationLinkman},#{stationTel},#{stationAddOne},"
			+ "#{stationAddTwo},#{stationCode},#{stationOrgLevel},#{stationOrg},#{stationLongitude},#{stationLatitude},"
			+ "#{stationAccountNum},#{stationPassword},now(),#{stationFlag},#{lastTime},#{parentStationName},#{enterTag})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public int insertStationInfo(StationInfo stationInfo);

	@Select("SELECT * FROM t_station_info where id= #{id} and active =1 ")
	public StationInfo getStationInfoById(@Param("id") int id);

	@Select("select *  from  t_station_info where active =1")
	public List<StationInfo> getStationInfoAll();

	@Update("update t_station_info set "
			+ "parentId=#{parentId},stationName=#{stationName},stationLinkman=#{stationLinkman},stationTel=#{stationTel},"
			+ "stationAddOne=#{stationAddOne},stationAddTwo=#{stationAddTwo},stationCode=#{stationCode},"
			+ "stationOrgLevel=#{stationOrgLevel},stationOrg=#{stationOrg},stationLongitude=#{stationLongitude},"
			+ "stationLatitude=#{stationLatitude},stationAccountNum=#{stationAccountNum},stationPassword=#{stationPassword},"
			+ "stationCreateTime=#{stationCreateTime},stationFlag=#{stationFlag},parentStationName=#{parentStationName}, "
			+ "enterTag = #{enterTag},active = #{active} where id = #{id}")
	public int updateStationInfo(StationInfo stationInfo);
	
	
	@Select("select *  from t_station_info where ${where} ")
	public List<StationInfo> getStationInfoByWhereAnd(@Param("where") String where);


	@Select("select count(*)  from t_station_info where parentId =#{id} and id <> #{parentId} and active = 1")
    public int getChildStationNum(StationInfo stationInfo);

	
//	@Select("select *  from t_station_info where parentId = #{param1.id} and stationName like '%${keyword}%'  and active = 1 limit #{2},#{3}")
//	public List<StationInfo> getStationInfoByKeyword(StationInfo stationInfo ,@Param("stationName") String keyword , int preSize , int pageSize);
//
//	@Select("select count(*) from t_station_info where parentId = #{param1.id} and stationName like '%${keyword}%' and active = 1")
//	public int getRecordTotalByKeyword(StationInfo stationInfo ,@Param("stationName") String keyword );
//	
//	
	@Select("select *  from t_station_info where ${where}")
	public List<StationInfo> getStationInfoByWhere(@Param("where") String where);
	
	@Select("select count(*)  from t_station_info where ${where} ")
	public int getRecordTotalByWhere(@Param("where") String where);
	
	@Select("select *  from t_station_info where stationName = #{1} and stationName <> #{0} ")
	public List<StationInfo> checkStationName(String name , String updateName );
	
	@Select("select *  from t_station_info where stationCode = #{1} and stationCode <> #{0} ")
	public List<StationInfo> checkStationCode(String code , String updateCode );
	
	
	@Update("update t_station_info set parentStationName = #{1} where parentId =#{0}")
	public int updateByParentId(int parentId ,String name); 
}
