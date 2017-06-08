package com.cqfc.ipquery.service.impl;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.cqfc.ipquery.IpUtil;
import com.cqfc.ipquery.dao.IpQueryDao;
import com.cqfc.ipquery.datacenter.IpInfoBuffer;
import com.cqfc.ipquery.model.IpSec;
import com.cqfc.ipquery.service.IIpQueryService;
import com.cqfc.ipquery.test.FileConvert;
import com.cqfc.protocol.ipquery.IpInfo;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.jami.util.Log;

@Service
public class IpQueryServiceImpl implements IIpQueryService {

	@Resource
	private IpQueryDao ipDao;
	
	@Resource
	private DataSource dataSource;
	
	@Override
	public IpInfo queryIpAttribution(String ipAddr) {
		IpInfo info = new IpInfo();
		info.setAddress(ipAddr);
		Long index = IpInfoBuffer.getInstance().locateIndex(ipAddr);
		if (index == null){
			info.setDetail("找不到Ip信息");
		} else {
			IpSec ipSec = ipDao.getIpSecById(index);
			info.setProvince(ipSec.getProvince());
			info.setDetail(ipSec.getDetail());
			info.setUsed(ipSec.getUsed());
		}
		return info;
	}

	@Override
	public int parseIp2DB() {
		parseIp();
		return ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS;
	}

	private void parseIp() {
		Connection connection = null;
		boolean autoCommit = false;
		PreparedStatement insertPs = null;
		Statement deleteSt = null;
		String filePath="";
		try {
			ClassPathResource resource = new ClassPathResource("ipInfo.csv");
			filePath = resource.getFile().getParent();
		} catch (Exception e) {
			filePath = FileConvert.class.getResource("/").getPath();
			System.out.println("ip.csv file not found, use / as home path.");
		}
		String newFileName = filePath + "/ipInfo.csv";
		RandomAccessFile randomFileReader = null;
		List<Long> indexs = new ArrayList<Long>();
		try {
			connection = dataSource.getConnection();
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			
			insertPs = connection.prepareStatement("insert into t_lottery_ipinfo(startIndex,endIndex,startIp,endIp,province,detail,used) values(?,?,?,?,?,?,?)");
			deleteSt = connection.createStatement();
			deleteSt.executeUpdate("delete from t_lottery_ipinfo");
			randomFileReader = new RandomAccessFile(newFileName, "r");
			// fos = new FileOutputStream(newFileName);
			String readLine = "";
			// StringBuffer sb = new StringBuffer();
			int count = 0, i=0;
			while (true) {
				readLine = randomFileReader.readLine();
				if (readLine == null) {
					break;
				}
				readLine = new String(readLine.getBytes("ISO-8859-1"), "UTF-8");
				String[] split = readLine.split(",");
				
				if(split.length < 4){
					System.out.println("skip line=" + readLine);
					continue;
				}
				String startIp = split[0];
				String endIp = split[1];
				long startIndex = IpUtil.convertIp(startIp);
				long endIndex = IpUtil.convertIp(endIp);
				String used = "";
				if(split.length > 4){
					used = split[4];
				}
				insertPs.setLong(1, startIndex);
				insertPs.setLong(2, endIndex);
				insertPs.setString(3, startIp);
				insertPs.setString(4, endIp);
				insertPs.setString(5, split[3]);
				insertPs.setString(5, split[3]);
				insertPs.setString(6, split[2]);
				insertPs.setString(7, used);
				insertPs.addBatch();
				count ++;
				if (count>=1000){
					insertPs.executeBatch();
					connection.commit();
					insertPs.clearBatch();
					i+=count;
					count = 0;
					System.out.println("解析了"+i + "条数据入库");
				}
				indexs.add(startIndex);
			}
			insertPs.executeBatch();
			connection.commit();
			insertPs.clearBatch();
			i +=count;
			System.out.println("解析了"+ i + "条数据入库");
			IpInfoBuffer.getInstance().initDatas(indexs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (randomFileReader != null) {
				try {
					randomFileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			closeConnection(insertPs, deleteSt, connection, autoCommit);
		}
	}

	private void closeConnection(PreparedStatement insertPs, Statement deleteSt, Connection connection,
			boolean autoCommit) {
		try {
			if (insertPs != null) {
				insertPs.close();
			}
			if (deleteSt != null) {
				deleteSt.close();
			}
			if (connection != null) {
				connection.setAutoCommit(autoCommit);
				connection.close();
			}
		} catch (Exception e) {
			Log.run.debug("", e);
		}
	}
}
