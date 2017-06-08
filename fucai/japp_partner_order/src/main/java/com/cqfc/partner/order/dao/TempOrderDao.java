package com.cqfc.partner.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.TempOrderMapper;
import com.cqfc.protocol.partnerorder.DailyAwardCount;
import com.cqfc.protocol.partnerorder.DailySaleCount;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.jami.util.Log;

@Repository
public class TempOrderDao {

	@Autowired
	private TempOrderMapper tempOrderMapper;

	
	public int cleanTempOrder(String time,int type){
		int flag = 0 ;
		
		try {
			flag = tempOrderMapper.deleteTempOrder(time,type);
		} catch (Exception e) {
			Log.run.error("定时删除临时订单库订单数据库出现异常",e);
			flag = -100;
		}
		
		return flag;
	}
	
	public int cleanTempJcOrderDetail(String time){
		int flag = 0 ;
		
		try {
			flag = tempOrderMapper.deleteTempJcOrderDetail(time);
		} catch (Exception e) {
			Log.run.error("定时删除临时订单库竞技彩订单数据库出现异常",e);
			flag = -100;
		}
		
		return flag;
	}

	public long getTotalTicknumByDay(String startTime, String endTime) {
		
		Long num = 0L;
		try {
			
			
			num = tempOrderMapper.getTotalTicknumByDay(startTime, endTime);
		} catch (Exception e) {
			Log.run.error("获取出票总数数据库出现异常",e);
			Log.error("获取出票总数数据库出现异常",e);
		}
		return num;
	}

	
	public List<LotteryIssueSale> getCurrentIssueSale(LotteryIssueSale lotteryIssueSale,String startTime, String endTime) {
		
		List<LotteryIssueSale> lotteryIssueSales = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" 1 =1");
			
			if(StringUtils.isNotEmpty(lotteryIssueSale.getIssueNo())){
				sb.append(" and issueNo = '");
				sb.append(lotteryIssueSale.getIssueNo());
				sb.append("'");
			}
			
			if(StringUtils.isNotEmpty(lotteryIssueSale.getLotteryId())){
				sb.append(" and lotteryId = '");
				sb.append(lotteryIssueSale.getLotteryId());
				sb.append("'");
			}
			
			if(StringUtils.isNotEmpty(lotteryIssueSale.getPartnerId())){
				sb.append(" and partnerId = '");
				sb.append(lotteryIssueSale.getPartnerId());
				sb.append("'");
			}
			if(StringUtils.isNotEmpty(startTime)){
				sb.append(" and createTime >= '");
				sb.append(startTime);
				sb.append("'");
			}
			
			if(StringUtils.isNotEmpty(endTime)){
				sb.append(" and createTime <='");
				sb.append(endTime);
				sb.append("'");
			}
			
			sb.append(" group by partnerId ");
			
			lotteryIssueSales = tempOrderMapper.getCurrentIssueSale(sb.toString());
		
			if(lotteryIssueSales.size() == 1 && lotteryIssueSales.get(0)==null){
				lotteryIssueSales.remove(0);
			}
		} catch (Exception e) {
			Log.run.error("获取当前期销量出现异常",e);
			Log.error("获取当前期销量出现异常",e);
		}
		
		
		return lotteryIssueSales;
	}
	
	
	
	public List<DailySaleCount> getDailyReportSale(String countTime,String startTime,String endTime) {

		List<DailySaleCount> dailySaleCounts = new ArrayList<DailySaleCount>();
		try {

			StringBuffer sb = new StringBuffer();

			sb.append(" createTime >='");
			sb.append(startTime);
			sb.append("' and createTime<'");
			sb.append(endTime);
			sb.append("'");
			sb.append(" group by partnerId,lotteryId");
			dailySaleCounts = tempOrderMapper.getDailyReportSale(
					sb.toString());
		} catch (Exception e) {
			Log.run.error("获取日报数据库异常", e);
			Log.error("获取日报数据库异常", e);
		}
		return dailySaleCounts;
	}
	
	
	
	public List<DailyAwardCount> getDailyReportAward(String countTime,String startTime,String endTime) {

		List<DailyAwardCount> dailySaleCounts = new ArrayList<DailyAwardCount>();
		try {

			StringBuffer sb = new StringBuffer();

			sb.append(" createTime >='");
			sb.append(startTime);
			sb.append("' and createTime<'");
			sb.append(endTime);
			sb.append("'");
			sb.append(" group by partnerId,lotteryId");
			dailySaleCounts = tempOrderMapper.getDailyReportAward(
					sb.toString());
		} catch (Exception e) {
			Log.run.error("获取日报数据库异常", e);
			Log.error("获取日报数据库异常", e);
		}
		return dailySaleCounts;
	}
}
