package com.cqfc.management.model;

import java.io.Serializable;

/**
 * 站点地图json数据格式
 * @author Administrator
 *
 */
public class StationMap implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4240946305714428372L;
	private String id ; //编号
	private String address;  //地址
	private String phone;	//联系方式
	private String lon ;	// 经度
	private String lat;	//纬度
	public StationMap() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StationMap(String id, String address, String phone, String lon,
			String lat) {
		super();
		this.id = id;
		this.address = address;
		this.phone = phone;
		this.lon = lon;
		this.lat = lat;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	
	
}
