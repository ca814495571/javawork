package com.cqfc.management.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.Fuser;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PartnerCharge;
import com.cqfc.management.model.PartnerDaySale;
import com.cqfc.management.model.PartnerInfo;
import com.cqfc.management.model.PartnerIssueCount;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.service.IFuserService;
import com.cqfc.management.service.IPartnerService;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.PartnerConstant;

@Controller
@RequestMapping("/partner")
public class PartnerController {

	
	private static final int DEFAUT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 100;
	
	
	@Autowired
	IPartnerService partnerService;
	
	@Autowired
	IFuserService fuserService;
	
	
	
	/**
	 * 根据条件查询订单信息
	 * 
	 * @param orderInfo
	 * @return
	 */
//	@RequestMapping("/getOrderInfo")
//	@ResponseBody
//	public PcResultObj getOrderInfo(OrderInfo orderInfo) {
//
//		return partnerService.getOrderByWhere(orderInfo);
//
//	}
	
	
	
	/**
	 * 添加合作商信息
	 * @param 
	 * @return
	 */
	@RequestMapping("/addPartnerInfo")
	@ResponseBody
	public PcResultObj addPartnerInfo(HttpServletRequest request,PartnerInfo parterInfo){
		
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.addPartnerInfo(parterInfo);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "添加合作商,"+parterInfo.toString()+"操作成功", IFuserService.OPERATE_USER_MANAGE);
		}else{
			fuserService.addOperateLog(request, "添加合作商,"+parterInfo.toString()+"操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_USER_MANAGE);
		}
		
		return pcResultObj;
	}
	
	
	/**
	 * 获取合作商信息
	 * @param 
	 * @return
	 */
	@RequestMapping("/getPartnerInfo")
	@ResponseBody
	public PcResultObj getPartnerInfo(LotteryPartner partnerInfo,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = partnerService.getPartnerInfo(partnerInfo,pageNum,pageSize);
		
		return pcResultObj;
	}
	
	
	
	
	
	/**
	 * 获取单个合作商信息
	 * @param 
	 * @return
	 */
	@RequestMapping("/getPartnerById")
	@ResponseBody
	public PcResultObj getPartnerById(PartnerInfo partnerInfo){
		
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.getPartnerById(partnerInfo);
		
		return pcResultObj;
	}
	
	
	
	/**
	 * 修改合作商信息
	 * @param 
	 * @return
	 */
	@RequestMapping("/updatePartnerById")
	@ResponseBody
	public PcResultObj updatePartnerById(HttpServletRequest request,PartnerInfo partnerInfo){
		
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.updatePartnerById(partnerInfo);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "修改合作商信息,"+partnerInfo.toString()+"操作成功", IFuserService.OPERATE_USER_MANAGE);
		}else{
			fuserService.addOperateLog(request, "修改合作商信息,"+partnerInfo.toString()+"操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_USER_MANAGE);
		}
		
		return pcResultObj;
	}
	
	
	/**
	 * 修改合作商冻结状态
	 * @param 
	 * @return
	 */
	@RequestMapping("/updatePartnerState")
	@ResponseBody
	public PcResultObj updatePartnerState(HttpServletRequest request,PartnerInfo partnerInfo){
		
		
		
		
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.updatePartnerState(partnerInfo);
		
		
		if(partnerInfo.getPartnerState()!=null){
			int normal = PartnerConstant.PartnerState.NORMAL.getValue();
			if(partnerInfo.getPartnerState() == normal){
				if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
					fuserService.addOperateLog(request, "冻结合作商"+partnerInfo.toString()+",操作成功", IFuserService.OPERATE_USER_MANAGE);
				}else{
					fuserService.addOperateLog(request, "冻结合作商"+partnerInfo.toString()+",操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_USER_MANAGE);
				}
				
			}else {
				if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
					fuserService.addOperateLog(request, "解冻合作商"+partnerInfo.toString()+",操作成功", IFuserService.OPERATE_USER_MANAGE);
				}else{
					fuserService.addOperateLog(request, "解冻合作商"+partnerInfo.toString()+",操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_USER_MANAGE);
				}
			}
		}
		
		return pcResultObj;
	}
	/**
	 * 合作商充值充值
	 * @param 
	 * @return
	 */
	@RequestMapping("/recharge")
	@ResponseBody
	public PcResultObj recharge(HttpServletRequest request,PartnerCharge partnerRecharge){
		
		
		
		PcResultObj pcResultObj = new PcResultObj();
		pcResultObj = partnerService.addPartnerRecharge(partnerRecharge);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "合作商充值,"+partnerRecharge.toString()+",操作成功", IFuserService.OPERATE_USER_MANAGE);
		}else{
			fuserService.addOperateLog(request, "合作商充值,"+partnerRecharge.toString()+",操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_USER_MANAGE);

		}
		
		return pcResultObj;
	}
	
	
	
	
	/**
	 * 根据条件查询合作商日销售量
	 * @param partnerDaySale
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getPartnerDaySale")
	@ResponseBody
	public PcResultObj getPartnerDaySale(PartnerDaySale partnerDaySale,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = partnerService.getSaleByDay(partnerDaySale, pageNum, pageSize);
		
		return pcResultObj;
	}
	
	
	
	@RequestMapping("/getDaySaleDetails")
	@ResponseBody
	public PcResultObj getDaySaleDetails(DailySaleAndCharge dailySaleAndCharge,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = partnerService.getDaySaleDetails(dailySaleAndCharge,pageNum,pageSize);
		
		return pcResultObj;
	}
	/**
	 * 根据合作商id 彩种 期号 查询销售量
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getPartnerIssueCount")
	@ResponseBody
	public PcResultObj getPartnerIssueCount(PartnerIssueCount partnerIssueCount,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = partnerService.getSaleByIssue(partnerIssueCount, pageNum, pageSize);
		
		return pcResultObj;
	}
	
	@RequestMapping("/issueSale")
	@ResponseBody
	public PcResultObj issueSale(HttpServletRequest request,String lotteryId ,String issueNo){
		PcResultObj pcResultObj = new PcResultObj();
		pcResultObj = partnerService.issueSaleCount(lotteryId, issueNo);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "彩种"+lotteryId+"期号"+issueNo+"销售重新统计成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "彩种"+lotteryId+"期号"+issueNo+"销售重新统计失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	@RequestMapping("/issueReward")
	@ResponseBody
	public PcResultObj issueReward(HttpServletRequest request,String lotteryId ,String issueNo){
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.issueRewardCount(lotteryId , issueNo);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "彩种"+lotteryId+"期号"+issueNo+"中奖金额重新统计成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "彩种"+lotteryId+"期号"+issueNo+"中奖金额重新统计失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	@RequestMapping("/dailySale")
	@ResponseBody
	public PcResultObj dailySale(HttpServletRequest request,String countTime){
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.dailySaleCount(countTime);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "日销量统计成功,时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "日销量统计失败"+pcResultObj.getMsg()+",时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	@RequestMapping("/dailyReward")
	@ResponseBody
	public PcResultObj dailyReward(HttpServletRequest request,String countTime){
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.dailyRewardCount(countTime);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "日兑奖重新统计成功,时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "日兑奖重新统计失败:"+pcResultObj.getMsg()+",时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	@RequestMapping("/dailyEncash")
	@ResponseBody
	public PcResultObj dailyEncash(HttpServletRequest request,String countTime){
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.dailyEncashCount(countTime);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "日提现重新统计成功,时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "日提现重新统计失败"+pcResultObj.getMsg()+",时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	
	@RequestMapping("/dailyCharge")
	@ResponseBody
	public PcResultObj dailyCharge(HttpServletRequest request,String countTime){
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.dailyChargeCount(countTime);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "日充值重新统计成功,时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "日充值重新统计失败"+pcResultObj.getMsg()+",时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	
	@RequestMapping("/dailyReport")
	@ResponseBody
	public PcResultObj dailyReport(HttpServletRequest request,String countTime){
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = partnerService.dailyReport(countTime);
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "日充值重新统计成功,时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "日充值重新统计失败"+pcResultObj.getMsg()+",时间:"+countTime, IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	/**
	 * 合作商查询(对外开发)
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/user/getHistoryIssueCount")
	@ResponseBody
	public PcResultObj getHistoryIssueCount(HttpServletRequest request,PartnerIssueCount partnerIssueCount,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		Fuser fuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		partnerIssueCount.setPartnerId(fuser.getPartnerId());
		pcResultObj = partnerService.getHistoryIssueCount(partnerIssueCount,pageNum,pageSize);
		
		return pcResultObj;
	}
	
	
	/**
	 * 合作商查询(对外开发)
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/user/getCurrentIssueCount")
	@ResponseBody
	public PcResultObj getCurrentIssueCount(HttpServletRequest request,PartnerIssueCount partnerIssueCount,String startTime,String endTime){
		
		PcResultObj pcResultObj = new PcResultObj();
		Fuser fuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		partnerIssueCount.setPartnerId(fuser.getPartnerId());
		if(StringUtils.isBlank(startTime))
			startTime ="";
	
		if(StringUtils.isBlank(endTime))endTime="";
		pcResultObj = partnerService.getCurrentIssueCount(partnerIssueCount,startTime,endTime);
		
		return pcResultObj;
	}
	
	
	/**
	 * 合作商查询账户(对外开发)
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/user/getAccount")
	@ResponseBody
	public PcResultObj getAccount(HttpServletRequest request){
		
		PcResultObj pcResultObj = new PcResultObj();
		PartnerInfo partnerInfo = new PartnerInfo();
		Fuser fuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		partnerInfo.setPartnerId(fuser.getPartnerId());
		pcResultObj = partnerService.getPartnerById(partnerInfo);
		partnerInfo = (PartnerInfo) pcResultObj.getEntity();
		if(StringUtils.isNotBlank(partnerInfo.getUsableMoney())){
			
			partnerInfo.setUsableMoney(MoneyUtil.toYuanStr(MoneyUtil.toCent(Double.parseDouble(partnerInfo.getUsableMoney()))-5000000000L));
		}
		partnerInfo.setAlarmValue("");
		partnerInfo.setCallbackUrl("");
		partnerInfo.setCreditLimit("");
		partnerInfo.setIpAddress("");
		partnerInfo.setSecretKey("");
		return pcResultObj;
	}
	
	
	/**
	 * 合作商查询日报(对外开发)
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/user/getDailyReport")
	@ResponseBody
	public PcResultObj getDailyReport(HttpServletRequest request,DailySaleAndCharge dailySaleAndCharge,Integer pageNum ,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		Fuser fuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		dailySaleAndCharge.setPartnerId(fuser.getPartnerId());
		
		pcResultObj = partnerService.getDailyReport(dailySaleAndCharge, pageNum, pageSize);
		return pcResultObj;
	}
	
	
	/**
	 * 合作商查询订单(对外开发)
	 * @param partnerIssueCount
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/user/getOrder")
	@ResponseBody
	public PcResultObj getOrder(HttpServletRequest request,OrderInfo orderInfo,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		Fuser fuser = (Fuser) request.getSession().getAttribute(IFuserService.SESSION_USER);
		orderInfo.setPartnerId(fuser.getPartnerId());
		
		pcResultObj = partnerService.getOrder(orderInfo, pageNum, pageSize);
		
		return pcResultObj;
	}
	
	
	/**
	 * 根据条件查询流水日志信息
	 * @param accountLog
	 * @param fromTime
	 * @param toTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
//	@RequestMapping("/getAccountLog")
//	@ResponseBody
//	public PcResultObj getAccountLog(AccountLog accountLog,String fromTime,String toTime,Integer pageNum,Integer pageSize){
//		
//		PcResultObj pcResultObj = new PcResultObj();
//		
//		pcResultObj = partnerService.getLogInfoByWhere(accountLog, fromTime, toTime, pageNum, pageSize);
//		
//		return pcResultObj;
//		
//	}
	
	
	
}
