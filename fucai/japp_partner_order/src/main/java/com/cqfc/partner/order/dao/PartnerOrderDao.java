package com.cqfc.partner.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cqfc.partner.order.dao.mapper.DailyAwardCountMapper;
import com.cqfc.partner.order.dao.mapper.DailySaleCountMapper;
import com.cqfc.partner.order.dao.mapper.IssueRewardCountMapper;
import com.cqfc.partner.order.dao.mapper.PartnerOrderMapper;
import com.cqfc.protocol.partnerorder.DailyAwardCount;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.protocol.partnerorder.IssueRewardCount;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.protocol.partnerorder.LotteryDaySale;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.protocol.partnerorder.Order;
import com.cqfc.protocol.partnerorder.PcDayAwardDetails;
import com.cqfc.protocol.partnerorder.PcDaySaleDetails;
import com.cqfc.protocol.partnerorder.PcLotteryIssueSale;
import com.cqfc.protocol.partnerorder.PcPartnerOrder;
import com.cqfc.util.LotteryType;
import com.jami.util.Log;

@Repository
public class PartnerOrderDao {

	@Autowired
	private PartnerOrderMapper partnerOrderMapper;

	@Autowired
	private DailySaleCountMapper dailySaleCountMapper;
	
	@Autowired
	private DailyAwardCountMapper dailyAwardCountMapper;
	
	@Autowired
	private IssueRewardCountMapper issueRewardCountMapper ;

	public PcPartnerOrder getPartnerOrderByWhere(Order order, String tableName,
			int pageNum, int pageSize) {
		PcPartnerOrder pcPartnerOrder = new PcPartnerOrder();
		List<Order> partnerOrders = new ArrayList<Order>();
		int total = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(" 1=1 ");

		try {
			
			if (order.getPartnerId() != null && !"".equals(order.getPartnerId())) {

				sb.append(" and partnerId = '");
				sb.append(order.getPartnerId());
				sb.append("'");
			}
			
			
			if (order.getIssueNo() != null && !"".equals(order.getIssueNo())) {

				sb.append(" and issueNo = '");
				sb.append(order.getIssueNo());
				sb.append("'");
			}

			if (order.getLotteryId() != null && !"".equals(order.getLotteryId())) {

				sb.append(" and lotteryId = '");
				sb.append(order.getLotteryId());
				sb.append("'");

			}

			if (order.getOrderNo() != null && !"".equals(order.getOrderNo())) {

				sb.append(" and orderNo = '");
				sb.append(order.getOrderNo());
				sb.append("'");

			}
			if (order.getTradeId() != null && !"".equals(order.getTradeId())) {
				
				sb.append(" and tradeId = '");
				sb.append(order.getTradeId());
				sb.append("'");
				
			}

			if (order.getCreateTime() != null && !"".equals(order.getCreateTime())) {

				sb.append(" and createTime between '");
				sb.append(order.getCreateTime() + " 00:00:00");
				sb.append("' and '");
				sb.append(order.getCreateTime() + " 23:59:59");
				sb.append("'");
			}
			
			sb.append(" order by createTime desc ");
			
			total = partnerOrderMapper.getPartnerOrderNumByWhere(sb.toString(),
					tableName);
			
			

			if (pageNum != 0 && pageSize != 0) {

				sb.append(" limit ");
				sb.append((pageNum - 1) * pageSize);
				sb.append(",");
				sb.append(pageSize);

			}
			
			partnerOrders = partnerOrderMapper.getPartnerOrderByWhere(sb.toString(),
					tableName);
			
			pcPartnerOrder.setTotalNum(total);
			pcPartnerOrder.setPartnerOrders(partnerOrders);
		} catch (Exception e) {
			Log.run.error("根据条件获取订单信息数据库异常", e);
			Log.error("根据条件获取订单信息数据库异常", e);
		}
		return pcPartnerOrder;

	}

	
	public List<IssueSaleAndReward> getIssueSaleAndReward(String partnerId,
			String lotteryId, String issueNo){
		List<IssueSaleAndReward> issueSaleAndRewards = new ArrayList<IssueSaleAndReward>();
		
		try {
			issueSaleAndRewards = partnerOrderMapper.getIssueSaleAndReward(partnerId,lotteryId,issueNo);
		} catch (Exception e) {
			Log.run.error("查询合作商期销量中奖信息数据库异常",e);
			Log.error("查询合作商期销量中奖信息数据库异常",e);
		}
		
		return issueSaleAndRewards;
	}
	
	public List<LotteryIssueSale> getIssueSaleAndRewardByGroup(String partnerId,
			String lotteryId, String issueNo){
		List<LotteryIssueSale> issueSaleAndRewards = new ArrayList<LotteryIssueSale>();
		
		try {
			issueSaleAndRewards = partnerOrderMapper.getIssueSaleAndRewardByGroup(partnerId,lotteryId,issueNo);
		} catch (Exception e) {
			Log.run.error("查询合作商期销量中奖信息数据库异常",e);
			Log.error("查询合作商期销量中奖信息数据库异常",e);
		}
		
		return issueSaleAndRewards;
	}

	public List<LotteryIssueSale> getAllLotteryIssueSale(
			String lotteryId, String issueNo){
		Log.run.debug("调用getAllIssueSale方法...");
		List<LotteryIssueSale> issueSaleAndRewards = new ArrayList<LotteryIssueSale>();
		
		try {
			issueSaleAndRewards = partnerOrderMapper.getAllLotteryIssueSale(lotteryId,issueNo);
		} catch (Exception e) {
			Log.run.error("查询合作商期销量中奖信息数据库异常",e);
			Log.error("查询合作商期销量中奖信息数据库异常",e);
		}
		
		return issueSaleAndRewards;
	}
	
	
	
	public List<DailySaleAndCharge> getDailySaleAndCharge(
			String partnerId, String countTime) {
		List<DailySaleAndCharge> dailySaleAndCharges = new ArrayList<DailySaleAndCharge>();
		
		try {
			dailySaleAndCharges = partnerOrderMapper.getDailySaleAndCharge(partnerId,countTime);
		} catch (Exception e) {
			Log.run.error("获取日销量充值信息数据库异常","e");
			Log.error("获取日销量充值信息数据库异常","e");
		}
		
		return dailySaleAndCharges;
	}
	
		
	public PcLotteryIssueSale getLotteryIssueSaleByWhere(
			LotteryIssueSale lotteryIssueSale ,int pageNum ,int pageSize) {
		
		PcLotteryIssueSale pcLotteryIssueSale = new PcLotteryIssueSale(); 
		List<LotteryIssueSale> lotteryIssueSales = new ArrayList<LotteryIssueSale>();
		StringBuffer sb = new StringBuffer();
		sb.append(" 1=1 ");
		
		if(lotteryIssueSale.getIssueNo()!=null && !"".equals(lotteryIssueSale.getIssueNo())){
			
			sb.append(" and issueNo='");
			sb.append(lotteryIssueSale.getIssueNo());
			sb.append("'");
		}
		if(lotteryIssueSale.getLotteryId()!=null && !"".equals(lotteryIssueSale.getLotteryId())){
			
			sb.append(" and lotteryId='");
			sb.append(lotteryIssueSale.getLotteryId());
			sb.append("'");
		}
		
		if(lotteryIssueSale.getPartnerId()!=null && !"".equals(lotteryIssueSale.getPartnerId())){
			
			sb.append(" and partnerId='");
			sb.append(lotteryIssueSale.getPartnerId());
			sb.append("'");
		}
		
		sb.append(" group by partnerId,lotteryId,issueNo");
		
		int totalNum = 0;
		try {
			
			totalNum = partnerOrderMapper.getLotteryIssueSaleSum(sb.toString());
		} catch (Exception e) {
			Log.run.error("获取期销量总销量数据库异常",e);
			Log.error("获取期销量总销量数据库异常",e);
		}
		
		
		if(pageNum!=0 && pageSize!=0){
			
			
			sb.append(" limit ");
			sb.append((pageNum-1)* pageSize);
			sb.append(",");
			sb.append(pageSize );
		}
		
		try {
			lotteryIssueSales = partnerOrderMapper.getLotteryIssueSale(sb.toString());
	
			
			if (lotteryIssueSales!=null && lotteryIssueSales.size()>0) {
				IssueRewardCount issueRewardCount = null;
				
					for (LotteryIssueSale issueSale :lotteryIssueSales) {
						
						issueRewardCount = issueRewardCountMapper.getIssueRewardGroup(issueSale.getPartnerId(), issueSale.getLotteryId(), issueSale.getIssueNo());
						if(issueRewardCount!=null){
							issueSale.setBigPrizeMoney(issueRewardCount.getBigPrizeMoney());
							issueSale.setBigPrizeNum(issueRewardCount.getBigPrizeNum());
							issueSale.setSmallPrizeMoney(issueRewardCount.getSmallPrizeMoney());
							issueSale.setSmallPrizeNum(issueRewardCount.getSmallPrizeNum());
						}
					}
				
			}
		
		} catch (Exception e) {
			Log.run.error("获取合作商销售中奖情况数据库异常",e);
			Log.error("获取合作商销售中奖情况数据库异常",e);
		}
		
		
		pcLotteryIssueSale.setTotalNum(totalNum);
		pcLotteryIssueSale.setLotteryIssueSale(lotteryIssueSales);
		return pcLotteryIssueSale;
	}

	public List<LotteryDaySale> getLotteryDaySaleByWhere(
			LotteryDaySale lotteryDaySale , int pageNum , int pageSize) {
		List<LotteryDaySale> lotteryDaySales = new ArrayList<LotteryDaySale>();
		
		StringBuffer sb = new StringBuffer();

		sb.append(" 1=1 ");

		if (lotteryDaySale.getPartnerId() != null
				&& !"".equals(lotteryDaySale.getPartnerId())) {

			sb.append(" and t1.partnerId = '");
			sb.append(lotteryDaySale.getPartnerId());
			sb.append("'");
		}

		if (lotteryDaySale.getCountTime() != null
				&& !"".equals(lotteryDaySale.getCountTime())) {

			sb.append(" and t1.countTime = '");
			sb.append(lotteryDaySale.getCountTime());
			sb.append("'");

		}

		sb.append(" group by t1.partnerId");
		
		
		try {
			
		//	int totalNum = dailySaleCountMapper.getLotteryDaySalesSum(sb.toString());
		} catch (Exception e) {
			Log.run.debug(e);
		}
//		if(pageNum!=0 && pageSize!=0){
//			
//			sb.append(" limit ");
//			sb.append( (pageNum-1) * pageSize);
//			sb.append(" , ");
//			sb.append(pageSize);
//		}
		
		try {
			
			lotteryDaySales = partnerOrderMapper.getLotteryDaySales(sb.toString());
			
		} catch (Exception e) {
			Log.run.error("获取彩种日销量兑奖提现充值信息数据库异常",e);
			Log.error("获取彩种日销量兑奖提现充值信息数据库异常",e);
		}
		
		return lotteryDaySales;
	}
	

	public PcDaySaleDetails getDailySaleByWhere(DailySaleAndCharge dailySaleAndCharge ,int pageNum, int pageSize){
		PcDaySaleDetails pcDaySaleDetails = new PcDaySaleDetails();
		List<DailySaleAndCharge> daySaleDetails = new ArrayList<DailySaleAndCharge>();
		StringBuffer sb = new StringBuffer();
		int totalNum = 0;
		try {
			

			sb.append(" 1=1 ");

			if (StringUtils.isNotBlank(dailySaleAndCharge.getPartnerId())) {

				sb.append(" and t1.partnerId = '");
				sb.append(dailySaleAndCharge.getPartnerId());
				sb.append("'");
			}
			
			if (StringUtils.isNotBlank(dailySaleAndCharge.getLotteryId())) {

				
				if("JZ".equalsIgnoreCase(dailySaleAndCharge.getLotteryId())){
					sb.append(" and t1.lotteryId in (");
					sb.append("'"+LotteryType.JCZQBF.getText()+"',");
					sb.append("'"+LotteryType.JCZQBQC.getText()+"',");
					sb.append("'"+LotteryType.JCZQHHGG.getText()+"',");
					sb.append("'"+LotteryType.JCZQJQS.getText()+"',");
					sb.append("'"+LotteryType.JCZQRQSPF.getText()+"',");
					sb.append("'"+LotteryType.JCZQSPF.getText()+"'");
					sb.append(")");
				}else if("JL".equalsIgnoreCase(dailySaleAndCharge.getLotteryId())){
					
					sb.append(" and t1.lotteryId in (");
					sb.append("'"+LotteryType.JCLQDXF.getText()+"',");
					sb.append("'"+LotteryType.JCLQHHGG.getText()+"',");
					sb.append("'"+LotteryType.JCLQRFSF.getText()+"',");
					sb.append("'"+LotteryType.JCLQSF.getText()+"',");
					sb.append("'"+LotteryType.JCLQSFC.getText()+"'");
					sb.append(")");
				}else if("BD".equalsIgnoreCase(dailySaleAndCharge.getLotteryId())){
					
					sb.append(" and t1.lotteryId in (");
					sb.append("'"+LotteryType.BDBF.getText()+"',");
					sb.append("'"+LotteryType.BDBQCSPC.getText()+"',");
					sb.append("'"+LotteryType.BDSFGG.getText()+"',");
					sb.append("'"+LotteryType.BDSPF.getText()+"',");
					sb.append("'"+LotteryType.BDSXDS.getText()+"',");
					sb.append("'"+LotteryType.BDXBC.getText()+"',");
					sb.append("'"+LotteryType.BDZJQS.getText()+"'");
					sb.append(")");
				}else if("NUM".equalsIgnoreCase(dailySaleAndCharge.getLotteryId())){
					
					sb.append(" and t1.lotteryId in (");
					sb.append("'"+LotteryType.SSQ.getText()+"',");
					sb.append("'"+LotteryType.QLC.getText()+"',");
					sb.append("'"+LotteryType.FC3D.getText()+"',");
					sb.append("'"+LotteryType.XYNC.getText()+"',");
					sb.append("'"+LotteryType.SSC.getText()+"',");
					sb.append("'"+LotteryType.DLT.getText()+"',");
					sb.append("'"+LotteryType.QXC.getText()+"',");
					sb.append("'"+LotteryType.PLS.getText()+"',");
					sb.append("'"+LotteryType.PLW.getText()+"',");
					sb.append("'"+LotteryType.ZCSFC.getText()+"',");
					sb.append("'"+LotteryType.ZCRX9.getText()+"',");
					sb.append("'"+LotteryType.ZC4CJQ.getText()+"',");
					sb.append("'"+LotteryType.ZC6CBQC.getText()+"'");
					sb.append(")");
				}else{
				
					sb.append(" and t1.lotteryId = '");
					sb.append(dailySaleAndCharge.getLotteryId());
					sb.append("'");
				}
			}

			if (StringUtils.isNotBlank(dailySaleAndCharge.getCountTime())) {

				sb.append(" and t1.countTime = '");
				sb.append(dailySaleAndCharge.getCountTime());
				sb.append("'");
			}
			
			totalNum = dailySaleCountMapper.getDailySaleByWhereSum(sb.toString());
			
			
			if(pageNum != 0 && pageSize!=0){
				
				sb.append(" limit ");
				sb.append((pageNum-1)*pageSize);
				sb.append(",");
				sb.append(pageSize);
			}
			
			
			daySaleDetails = dailySaleCountMapper.getDailySaleByWhere(sb.toString());
		} catch (Exception e) {
			Log.run.error(e);
		}
		
		pcDaySaleDetails.setDaySaleDetails(daySaleDetails);
		pcDaySaleDetails.setTotalNum(totalNum);
		return pcDaySaleDetails;
		
	}
	
	public PcDayAwardDetails getDailyAwardByWhere(DailyAwardCount dailyAwardCount,int pageNum, int pageSize){
		PcDayAwardDetails pcDayAwardDetails = new PcDayAwardDetails();
		List<DailyAwardCount> dailyAwards = new ArrayList<DailyAwardCount>();
		StringBuffer sb = new StringBuffer();
		int totalNum = 0;
		try {
			

			sb.append(" 1=1 ");

			if (StringUtils.isNotBlank(dailyAwardCount.getPartnerId())) {

				sb.append(" and partnerId = '");
				sb.append(dailyAwardCount.getPartnerId());
				sb.append("'");
			}

			if (StringUtils.isNotBlank(dailyAwardCount.getLotteryId())) {

				sb.append(" and lotteryId = '");
				sb.append(dailyAwardCount.getLotteryId());
				sb.append("'");
			}
			
			
			if (StringUtils.isNotBlank(dailyAwardCount.getCountTime())) {

				sb.append(" and countTime = '");
				sb.append(dailyAwardCount.getCountTime());
				sb.append("'");
			}
			
			totalNum = dailyAwardCountMapper.getDailyAwardByWhereSum(sb.toString());
			
			if(pageNum != 0 && pageSize!=0){
				
				sb.append(" limit ");
				sb.append((pageNum-1)*pageSize);
				sb.append(",");
				sb.append(pageSize);
			}
			dailyAwards = dailyAwardCountMapper.getDailyAwardByWhere(sb.toString());
		} catch (Exception e) {
			Log.run.error(e);
		}
		
		pcDayAwardDetails.setDayAwardDetails(dailyAwards);
		pcDayAwardDetails.setTotalNum(totalNum);
		return pcDayAwardDetails;
		
	}
	
}
