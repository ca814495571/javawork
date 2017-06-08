package com.cqfc.ipquery.test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;

import com.cqfc.ipquery.IpUtil;

public class FileConvert {
	private static final String insertStart = "insert into t_lottery_ipinfo(startIndex,endIndex,startIp,endIp,detail,province,used) values";
	private static Set<String> autonomousRegions = new HashSet<String>();
	private static Map<String,String> universitys = new HashMap<String,String>();
	static {
		autonomousRegions.add("广西");
		autonomousRegions.add("宁夏");
		autonomousRegions.add("新疆");
		autonomousRegions.add("西藏");
		autonomousRegions.add("内蒙古");
		universitys.put("北京化工大学", "北京市");
		universitys.put("北方工业大学", "北京市");
		universitys.put("北京工业大学", "北京市");
		universitys.put("北京体育大学", "北京市");
		universitys.put("中国农业大学", "北京市");
		universitys.put("北京联合大学", "北京市");
		universitys.put("首都师范大学", "北京市");
		universitys.put("首都经贸大学", "北京市");
		universitys.put("首都科技大学", "北京市");
		universitys.put("北京理工大学", "北京市");
		universitys.put("北京科技大学", "北京市");
		universitys.put("北京中医药大学", "北京市");
		universitys.put("对外经济贸易大学", "北京市");
		universitys.put("北京信息科技大学", "北京市");
		universitys.put("北京大学", "北京市");
		universitys.put("清华大学", "北京市");
		universitys.put("中央财经大学", "北京市");
		universitys.put("中国人民大学", "北京市");
		universitys.put("中国农业科学院", "北京市");
		universitys.put("北京石油化工学院", "北京市");
		universitys.put("北京科技职业学院", "北京市");
		universitys.put("北京人民警察学院", "北京市");
		universitys.put("华北科技学院", "北京市");
		universitys.put("武汉大学", "湖北省");
		universitys.put("华中科技大学", "湖北省");
		universitys.put("华中农业大学", "湖北省");
		universitys.put("中南财经政法大学", "湖北省");
		universitys.put("长江大学", "湖北省");
		universitys.put("武汉科技大学", "湖北省");
		universitys.put("湖北第二师范学院", "湖北省");
		universitys.put("华南理工大学", "广东省");
		universitys.put("华南农业大学", "广东省");
		universitys.put("中山大学", "广东省");
		universitys.put("山东大学", "山东省");
		universitys.put("青岛科技大学", "山东省");
		universitys.put("青岛大学", "山东省");
		universitys.put("中北大学", "山西省");
		universitys.put("太原科技大学", "山西省");
		universitys.put("长沙理工大学", "湖南省");
		universitys.put("中南大学", "湖南省");
		universitys.put("湖南师范大学", "湖南省");
		universitys.put("湖南农业大学", "湖南省");
		universitys.put("湖南长沙财经学院", "湖南省");
		universitys.put("长沙电力职业技术学院", "湖南省");
		universitys.put("湖南商学院", "湖南省");
		universitys.put("长沙交通学院", "湖南省");
		universitys.put("四川师范大学", "四川省");
		universitys.put("四川大学", "四川省");
		universitys.put("四川西南科技大学", "四川省");
		universitys.put("西华大学", "四川省");
		universitys.put("四川农业大学", "四川省");
		universitys.put("成都理工大学", "四川省");
		universitys.put("成都中医药大学", "四川省");
		universitys.put("成都工业学院", "四川省");
		universitys.put("四川行政学院", "四川省");
		universitys.put("成都信息工程学院", "四川省");
		universitys.put("四川理工学院", "四川省");
		universitys.put("东北农业大学", "黑龙江");
		universitys.put("哈尔滨师范大学", "黑龙江");
		universitys.put("佳木斯大学", "黑龙江");
		universitys.put("大庆职工大学", "黑龙江");
		universitys.put("黑龙江大学", "黑龙江");
		universitys.put("哈尔滨工程大学", "黑龙江");
		universitys.put("齐齐哈尔医学院", "黑龙江");
		universitys.put("黑龙江东方学院", "黑龙江");
		universitys.put("大连理工大学", "辽宁省");
		universitys.put("东北大学", "辽宁省");
		universitys.put("长春工业大学", "吉林省");
		universitys.put("长春职业技术学院", "吉林省");
		universitys.put("吉林大学", "吉林省");
		universitys.put("河北大学", "河北省");
		universitys.put("兰州大学", "甘肃省");
		universitys.put("郑州大学", "河南省");
		universitys.put("黄河科技大学", "河南省");
		universitys.put("西安建筑科技大学", "陕西省");
		universitys.put("西安石油大学", "陕西省");
		universitys.put("西安科技大学", "陕西省");
		universitys.put("西北工业大学", "陕西省");
		universitys.put("西安联合大学", "陕西省");
		universitys.put("西安交通大学", "陕西省");
		universitys.put("西安思源学院", "陕西省");
		universitys.put("西安外事学院", "陕西省");
		universitys.put("汉中理工学院", "陕西省");
		universitys.put("安徽财经大学", "安徽省");
		universitys.put("合肥工业大学", "安徽省");
		universitys.put("安徽农业大学", "安徽省");
		universitys.put("安徽大学", "安徽省");
		universitys.put("安徽建筑工业学院", "安徽省");
		universitys.put("南京大学", "江苏省");
		universitys.put("南京理工大学", "江苏省");
		universitys.put("南京化工大学", "江苏省");
		universitys.put("东南大学", "江苏省");
		universitys.put("江西财经大学", "江西省");
		universitys.put("江西师范大学", "江西省");
		universitys.put("江西财经学院", "江西省");
		universitys.put("南昌理工学院", "江西省");
		universitys.put("江西九江学院", "江西省");
		universitys.put("江西财经学院九江分院", "江西省");
		universitys.put("青海大学", "青海省");
		universitys.put("青海建筑职业技术学院", "青海省");
		universitys.put("贵州工业大学", "贵州省");
		universitys.put("台湾大学", "台湾省");
		universitys.put("浙江广播电视大学", "浙江省");
		universitys.put("宁波大学", "浙江省");
		universitys.put("浙江大学", "浙江省");
		universitys.put("浙江工业大学", "浙江省");
		universitys.put("浙江科技学院", "浙江省");
		universitys.put("浙江轻纺学院", "浙江省");
		universitys.put("集美大学", "福建省");
		universitys.put("福建工程学院", "福建省");
		universitys.put("云南大学", "云南省");
		universitys.put("重庆大学", "重庆市");
		universitys.put("重庆工学院", "重庆市");
		universitys.put("重庆信息工程专修学院", "重庆市");
		universitys.put("上海交通大学", "上海市");
		universitys.put("上海财经大学", "上海市");
		universitys.put("东华大学", "上海市");
		universitys.put("上海大学", "上海市");
		universitys.put("华东理工大学", "上海市");
		universitys.put("华东师范大学", "上海市");
		universitys.put("上海理工大学", "上海市");
		universitys.put("南开大学", "天津市");
	}

	private static void convert2csv(){
		String filePath="";
		try {
			ClassPathResource resource = new ClassPathResource("ip.csv");
			filePath = resource.getFile().getParent();
		} catch (Exception e) {
			filePath = FileConvert.class.getResource("/").getPath();
			System.out.println("ip.csv file not found, use / as home path.");
		}
		String fileName = filePath +"/ip.csv";
		String newFileName = filePath + "/ipInfo.csv";
		RandomAccessFile randomFileReader = null;
		// FileOutputStream fos = null;
		OutputStreamWriter fw = null;
		try {
			randomFileReader = new RandomAccessFile(fileName, "r");
			// fos = new FileOutputStream(newFileName);
			fw = new FileWriter(newFileName);
			String readLine = "", writeLine = "";
			// StringBuffer sb = new StringBuffer();
			int count = 0;
			while (true) {
				readLine = randomFileReader.readLine();
				if (readLine == null) {
					break;
				}
				readLine = new String(readLine.getBytes("ISO-8859-1"), "GBK");
				String[] split = readLine.split(",");
				writeLine = "";
				for (int i = 0; i < 3; i++) {
					writeLine += split[i] + ",";
				}
				String other = "";
				if (split[2].indexOf("IANA") >= 0) {
					writeLine += ",";
					other = split[2];
				} else {
					if (split[2].length() > 3) {
						boolean isRegion = false;
						for (String autonomousRegionName : autonomousRegions) {
							if (split[2].startsWith(autonomousRegionName)) {
								writeLine += autonomousRegionName + ",";
								isRegion = true;
								break;
							}
						}
						if (!isRegion) {
							int indexProvince = split[2].indexOf("省"), indexCity = split[2]
									.indexOf("市");
							if (((indexProvince > 0) && (indexProvince <= 3))
									|| ((indexCity > 0) && (indexCity < 3))) {
								writeLine += split[2].substring(0, 3) + ",";
							} else {
								int indexUniversity = split[2].indexOf("大学");
								if (indexUniversity <= 0){
									indexUniversity = split[2].indexOf("学院");
								}
								if (indexUniversity > 0){
									String university = split[2].substring(0,indexUniversity+2);
									if (universitys.containsKey(university)){
										writeLine += universitys.get(university) + ",";
									} else {
										writeLine += split[2] + ",";
									}
								}else{
									writeLine += split[2] + ",";
								}
							}
						}
					} else {
						writeLine += split[2] + ",";
					}
				}

				for (int i = 3; i < split.length; i++) {
					other += split[i] + " ";
				}
				writeLine += other.trim()
						+ System.getProperty("line.separator");
				fw.write(writeLine);
				count ++;
				if(count % 10000 == 0){
					System.out.println("parse " + count + " records.");
				}
			}
			System.out.println("parse " + count + " records.");
		} catch (Exception e) {
		} finally {
			if (randomFileReader != null) {
				try {
					randomFileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void convert2sql(){
		String filePath="";
		try {
			ClassPathResource resource = new ClassPathResource("ip.csv");
			filePath = resource.getFile().getParent();
		} catch (Exception e) {
			filePath = FileConvert.class.getResource("/").getPath();
			System.out.println("ip.csv file not found, use / as home path.");
		}
		String fileName = filePath +"/ip.csv";
		String newFileName = filePath + "/ipInfo.sql";
		RandomAccessFile randomFileReader = null;
		// FileOutputStream fos = null;
		OutputStreamWriter fw = null;
		try {
			randomFileReader = new RandomAccessFile(fileName, "r");
			// fos = new FileOutputStream(newFileName);
			fw = new FileWriter(newFileName);
			String readLine = "", writeLine = "";
			// StringBuffer sb = new StringBuffer();
			fw.write(insertStart + System.getProperty("line.separator"));
			boolean first = true;
			int count = 0;
			while (true) {
				readLine = randomFileReader.readLine();
				if (readLine == null) {
					break;
				}
				if (first){
					first = false;
				} else {
					if(count % 5000 == 0){
						fw.write(";" + System.getProperty("line.separator"));
						fw.write(insertStart + System.getProperty("line.separator"));
						System.out.println("parse " + count + " records.");
					} else {
						fw.write("," + System.getProperty("line.separator"));
					}
				}
				readLine = new String(readLine.getBytes("ISO-8859-1"), "GBK");
				String[] split = readLine.split(",");
				writeLine = "(";
				String startIp = split[0];
				String endIp = split[1];
				String detail = split[2];
				long startIndex = IpUtil.convertIp(startIp);
				long endIndex = IpUtil.convertIp(endIp);
				writeLine += startIndex + "," + endIndex + ",'" + startIp + "','" + endIp + "','" + detail + "','";
				String other = "";
				if (split[2].indexOf("IANA") >= 0) {
					writeLine += "','";
					other = split[2];
				} else {
					if (split[2].length() > 3) {
						boolean isRegion = false;
						for (String autonomousRegionName : autonomousRegions) {
							if (split[2].startsWith(autonomousRegionName)) {
								writeLine += autonomousRegionName + "','";
								isRegion = true;
								break;
							}
						}
						if (!isRegion) {
							int indexProvince = split[2].indexOf("省"), indexCity = split[2]
									.indexOf("市");
							if (((indexProvince > 0) && (indexProvince <= 3))
									|| ((indexCity > 0) && (indexCity < 3))) {
								writeLine += split[2].substring(0, 3) + "','";
							} else {
								int indexUniversity = split[2].indexOf("大学");
								if (indexUniversity <= 0){
									indexUniversity = split[2].indexOf("学院");
								}
								if (indexUniversity > 0){
									String university = split[2].substring(0,indexUniversity+2);
									if (universitys.containsKey(university)){
										writeLine += universitys.get(university) + "','";
									} else {
										writeLine += split[2] + "','";
									}
								}else{
									writeLine += split[2] + "','";
								}
							}
						}
					} else {
						writeLine += split[2] + "','";
					}
				}
				
				for (int i = 3; i < split.length; i++) {
					other += split[i] + " ";
				}
				writeLine += other.trim().replace("'", "\\'") + "')";
				fw.write(writeLine);
				count ++;
			}
			System.out.println("parse " + count + " records.");
		} catch (Exception e) {
		} finally {
			if (randomFileReader != null) {
				try {
					randomFileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		if(args.length == 0){
			convert2csv();
		} else {
			convert2sql();
		}
	}
}
