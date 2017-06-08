package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 站点信息对象
 * 
 * @author chen_an
 *
 */
public class StationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3506972649587818533L;
	/**
	 * 
	 */
	private int id;
	private int parentId; // 父站点
	private String stationName; // 站点名称
	private String stationLinkman; // 站点联系人
	private String stationTel; // 站点联系方式
	private String stationAddOne; // 站点所属区地址（例：渝中区）
	private String stationAddTwo; // 站点所属区地址后面的详细地址
	private String stationCode; // 站点站号
	private String stationOrgLevel; // 站点组织级别
	private String stationOrg; // 站点所属组织
	private String stationLongitude; // 站点百度地图经度
	private String stationLatitude; // 站点百度地图纬度
	private String stationAccountNum; // 站点帐号
	private String stationPassword; // 站点密码
	private String stationCreateTime; // 站点创建时间
	private int stationFlag; // 站点标识
	private String lastTime; // 站点最后修改时间
	private String parentStationName; // 父站点名称
	private String enterTag; // 入口标识符
	private int active;

	public StationInfo() {
		super();
	}

	public StationInfo(int id, int parentId, String stationName,
			String stationLinkman, String stationTel, String stationAddOne,
			String stationAddTwo, String stationCode, String stationOrgLevel,
			String stationOrg, String stationLongitude, String stationLatitude,
			String stationAccountNum, String stationPassword,
			String stationCreateTime, int stationFlag, String lastTime,
			String parentStationName, String enterTag, int active) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.stationName = stationName;
		this.stationLinkman = stationLinkman;
		this.stationTel = stationTel;
		this.stationAddOne = stationAddOne;
		this.stationAddTwo = stationAddTwo;
		this.stationCode = stationCode;
		this.stationOrgLevel = stationOrgLevel;
		this.stationOrg = stationOrg;
		this.stationLongitude = stationLongitude;
		this.stationLatitude = stationLatitude;
		this.stationAccountNum = stationAccountNum;
		this.stationPassword = stationPassword;
		this.stationCreateTime = stationCreateTime;
		this.stationFlag = stationFlag;
		this.lastTime = lastTime;
		this.parentStationName = parentStationName;
		this.enterTag = enterTag;
		this.active = active;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationLinkman() {
		return stationLinkman;
	}

	public void setStationLinkman(String stationLinkman) {
		this.stationLinkman = stationLinkman;
	}

	public String getStationTel() {
		return stationTel;
	}

	public void setStationTel(String stationTel) {
		this.stationTel = stationTel;
	}

	public String getStationAddOne() {
		return stationAddOne;
	}

	public void setStationAddOne(String stationAddOne) {
		this.stationAddOne = stationAddOne;
	}

	public String getStationAddTwo() {
		return stationAddTwo;
	}

	public void setStationAddTwo(String stationAddTwo) {
		this.stationAddTwo = stationAddTwo;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationOrgLevel() {
		return stationOrgLevel;
	}

	public void setStationOrgLevel(String stationOrgLevel) {
		this.stationOrgLevel = stationOrgLevel;
	}

	public String getStationOrg() {
		return stationOrg;
	}

	public void setStationOrg(String stationOrg) {
		this.stationOrg = stationOrg;
	}

	public String getStationLongitude() {
		return stationLongitude;
	}

	public void setStationLongitude(String stationLongitude) {
		this.stationLongitude = stationLongitude;
	}

	public String getStationLatitude() {
		return stationLatitude;
	}

	public void setStationLatitude(String stationLatitude) {
		this.stationLatitude = stationLatitude;
	}

	public String getStationAccountNum() {
		return stationAccountNum;
	}

	public void setStationAccountNum(String stationAccountNum) {
		this.stationAccountNum = stationAccountNum;
	}

	public String getStationPassword() {
		return stationPassword;
	}

	public void setStationPassword(String stationPassword) {
		this.stationPassword = stationPassword;
	}

	public String getStationCreateTime() {
		return stationCreateTime;
	}

	public void setStationCreateTime(String stationCreateTime) {
		this.stationCreateTime = stationCreateTime;
	}

	public int getStationFlag() {
		return stationFlag;
	}

	public void setStationFlag(int stationFlag) {
		this.stationFlag = stationFlag;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getParentStationName() {
		return parentStationName;
	}

	public void setParentStationName(String parentStationName) {
		this.parentStationName = parentStationName;
	}

	public String getEnterTag() {
		return enterTag;
	}

	public void setEnterTag(String enterTag) {
		this.enterTag = enterTag;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

}
