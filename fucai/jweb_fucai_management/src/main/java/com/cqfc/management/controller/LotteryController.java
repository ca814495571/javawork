package com.cqfc.management.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.CancelOrderInfo;
import com.cqfc.management.model.Issue;
import com.cqfc.management.model.OrderInfo;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.WinPrizeCheck;
import com.cqfc.management.service.IFuserService;
import com.cqfc.management.service.ILotteryService;
import com.cqfc.management.util.ReturnCodeConstansUtil;
import com.cqfc.management.util.scheduledTask.ScheduledTask;
import com.cqfc.protocol.cancelorder.SportCancelOrder;
import com.cqfc.protocol.lotteryissue.MatchCompetive;
import com.cqfc.protocol.lotteryissue.MatchCompetiveResult;
import com.cqfc.protocol.lotteryissue.MatchFootball;
import com.cqfc.protocol.ticketwinning.WinningOrderInfo;

@Controller
@RequestMapping("/lottery")
public class LotteryController {

	
	private static final int DEFAUT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 100;
	
	@Autowired
	ILotteryService lotteryService;
	
	@Autowired
	IFuserService fuserService; 

	/**
	 * 根据条件查询订单信息
	 * 
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping("/getOrderInfo")
	@ResponseBody
	public PcResultObj getOrderInfo(OrderInfo orderInfo,Integer pageNum,Integer pageSize) {

		
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		
		return lotteryService.getOrderByWhere(orderInfo,pageNum,pageSize);

	}
	
	/**
	 * 根据条件查询订单信息
	 * 
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping("/getWinningOrderInfo")
	@ResponseBody
	public PcResultObj getWinningOrderInfo(WinningOrderInfo winningOrderInfo,
			Integer pageNum, Integer pageSize) {
		
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		return lotteryService.getWinningOrderByWhere(winningOrderInfo, pageNum, pageSize);

	}

	/**
	 * 根据条件查询中奖总金额（中将审核查询）
	 * 
	 * @param winPrizeCheck
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getWinPrizeInfo")
	@ResponseBody
	public PcResultObj getWinPrizeInfo(WinPrizeCheck winPrizeCheck,
			Integer pageNum, Integer pageSize) {
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		return lotteryService.getWinningCheck(winPrizeCheck, pageNum, pageSize);
	}

	/**
	 * 审核中奖(修改期号表状态)
	 * 
	 * @param winPrizeCheck
	 * @return
	 */
	@RequestMapping("/winPrizeAgree")
	@ResponseBody
	public PcResultObj checkWinPrizeAgree(HttpServletRequest request,WinPrizeCheck winPrizeCheck) {
		
		
		PcResultObj pcResultObj = lotteryService.checkWinPrize(winPrizeCheck);
		
		if(PcResultObj.SUCCESS_CODE.equals(pcResultObj.getMsgCode())){
			
			fuserService.addOperateLog(request, "审核中奖信息,"+winPrizeCheck.toString()+",操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "审核中奖信息,"+winPrizeCheck.toString()+",操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		
		
		return pcResultObj;
	}

	/**
	 * 根据条件查询中奖总金额（派奖审核查询）
	 * 
	 * @param winPrizeCheck
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getAwardPrizeInfo")
	@ResponseBody
	public PcResultObj getAwardPrizeInfo(WinPrizeCheck winPrizeCheck,
			Integer pageNum, Integer pageSize) {

		
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		return lotteryService.getAwardCheck(winPrizeCheck, pageNum, pageSize);

	}

	/**
	 * 审核派奖(修改期号表状态)
	 * 
	 * @param winPrizeCheck
	 * @return
	 */
	@RequestMapping("/awardPrizeAgree")
	@ResponseBody
	public PcResultObj awardPrizeAgree(HttpServletRequest request,WinPrizeCheck winPrizeCheck) {
		
		
		PcResultObj pcResultObj = lotteryService.checkAwardPrize(winPrizeCheck);
		
		if(PcResultObj.SUCCESS_CODE.equals(pcResultObj.getMsgCode())){
			
			fuserService.addOperateLog(request, "审核派奖信息,"+winPrizeCheck.toString()+",操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "审核派奖信息,"+winPrizeCheck.toString()+",操作失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		
		return pcResultObj;

	}

	/**
	 * 得到中奖审核自动审核的标示符,页面显示
	 * 
	 * @return
	 */
	@RequestMapping("/getAutoCheckWinPrize")
	@ResponseBody
	public PcResultObj getAutoCheckWinPrize() {

		PcResultObj pcResultObj = new PcResultObj();

		pcResultObj.setEntity(ScheduledTask.getCheckWinPrize());
		pcResultObj.setMsg("成功");
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		return pcResultObj;
	}

	/**
	 * 获取兑奖审核自动审核的标示符
	 * 
	 * @return
	 */
	@RequestMapping("/getAutoCheckAwardPrize")
	@ResponseBody
	public PcResultObj getAutoCheckAwardPrize() {

		PcResultObj pcResultObj = new PcResultObj();

		pcResultObj.setEntity(ScheduledTask.getCheckAwardPrize());
		pcResultObj.setMsg("成功");
		pcResultObj.setMsgCode(PcResultObj.SUCCESS_CODE);
		return pcResultObj;
	}

	/**
	 * 选择是否自动审核中奖
	 * 
	 * @param flag
	 * @return
	 */
	@RequestMapping("/ifAutoCheckWin")
	@ResponseBody
	public PcResultObj ifAutoCheckWin(HttpServletRequest request,boolean flag) {
		
		PcResultObj pcResultObj = lotteryService.ifAutoCheckWinPrize(flag);
		
		if(flag){
			
			if(PcResultObj.SUCCESS_CODE.equals(pcResultObj.getMsgCode())){
				fuserService.addOperateLog(request, "设置中奖自动审核,操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
			}else{
				fuserService.addOperateLog(request, "设置中奖自动审核,操作失败", IFuserService.OPERATE_FINANCE_MANAGE);
			}
		
		}else{
			
			if(PcResultObj.SUCCESS_CODE.equals(pcResultObj.getMsgCode())){
				fuserService.addOperateLog(request, "取消中奖自动审核,操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
			}else{
				fuserService.addOperateLog(request, "取消中奖自动审核,操作失败", IFuserService.OPERATE_FINANCE_MANAGE);
			}
			
		}
		return pcResultObj;
	}

	/**
	 * 选择是否自动审核派奖
	 * 
	 * @param flag
	 * @return
	 */
	@RequestMapping("/ifAutoCheckAward")
	@ResponseBody
	public PcResultObj ifAutoCheckAward(HttpServletRequest request,boolean flag) {
		
		
		
		PcResultObj pcResultObj = lotteryService.ifAutoCheckAwardPrize(flag);
		
		if(flag){
			
			if(PcResultObj.SUCCESS_CODE.equals(pcResultObj.getMsgCode())){
				fuserService.addOperateLog(request, "设置派奖自动审核,操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
			}else{
				fuserService.addOperateLog(request, "设置派奖自动审核,操作失败", IFuserService.OPERATE_FINANCE_MANAGE);
			}
		
		}else{
			
			if(PcResultObj.SUCCESS_CODE.equals(pcResultObj.getMsgCode())){
				fuserService.addOperateLog(request, "取消派奖自动审核,操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
			}else{
				fuserService.addOperateLog(request, "取消派奖自动审核,操作失败", IFuserService.OPERATE_FINANCE_MANAGE);
			}
			
		}
		return pcResultObj;

	}

	
	/***
	 * 查询迁移单
	 * @param partnerId
	 * @param lotteryId
	 * @param issueNo
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getCancelOrder")
	@ResponseBody
	public PcResultObj getCancelOrder(CancelOrderInfo cancelOrderInfo, Integer currentPage, Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		
		if (currentPage == null || currentPage < 1) {
			currentPage = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		
		pcResultObj = lotteryService.getCancelOrder(cancelOrderInfo, currentPage, pageSize);
		
		return pcResultObj;
		
	}


	/**
	 * 重新全部算奖
	 * 
	 * @param flag
	 * @return
	 */
	@RequestMapping("/restartAllPrize")
	@ResponseBody
	public PcResultObj restartAllPrize(HttpServletRequest request,String lotteryId, String issueNo) {
		
		
		
		PcResultObj result = new PcResultObj();
		try {
			
			Integer ret = lotteryService.restartAllPrize(lotteryId, issueNo);
			if (ret == null) {
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("查询失败");
			} else if(ret == ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_EXIST) {
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("彩种期次错误或者不存在");
			} else if(ret == ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_CORRECT){
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("彩种期次状态错误");
			} else {
				result.setEntity(ret);
				result.setMsgCode(PcResultObj.SUCCESS_CODE);
				result.setMsg("查询成功");
			}
		} catch (Exception e) {
			result.setMsgCode(PcResultObj.FAIL_CODE);
			result.setMsg("查询失败," + e.toString());
		}
		
		
		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "对彩种:"+ lotteryId+"期号"+issueNo+"全部重新算奖操作,操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "对彩种:"+ lotteryId+"期号"+issueNo+"全部重新算奖操作,操作失败:"+result.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		
		
		return result;
	}

	/**
	 * 重新部分算奖
	 * 
	 * @param flag
	 * @return
	 */
	@RequestMapping("/restartPartPrize")
	@ResponseBody
	public PcResultObj restartPartPrize(HttpServletRequest request,String lotteryId, String issueNo) {

		PcResultObj result = new PcResultObj();
		try {
			Integer ret = lotteryService.restartPartPrize(lotteryId, issueNo);
			if (ret == null) {
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("查询失败");
			} else if(ret == ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_EXIST) {
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("彩种期次错误或者不存在");
			} else if(ret == ReturnCodeConstansUtil.RETURN_STATUS_CODE_ISSUE_NO_CORRECT){
				result.setMsgCode(PcResultObj.FAIL_CODE);
				result.setMsg("彩种期次状态错误");
			} else {
				result.setEntity(ret);
				result.setMsgCode(PcResultObj.SUCCESS_CODE);
				result.setMsg("查询成功");
			}
		} catch (Exception e) {
			result.setMsgCode(PcResultObj.FAIL_CODE);
			result.setMsg("查询失败," + e.toString());
		}

		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "对彩种:"+ lotteryId+"期号"+issueNo+"部分重新算奖,操作成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "对彩种:"+ lotteryId+"期号"+issueNo+"部分重新算奖,操作失败:"+result.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		
		return result;
	}
	
	/**
	 * 查询彩种期号
	 * @param issue
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getLotteryIssue")
	@ResponseBody
	public PcResultObj getLotteryIssue(Issue issue,int pageNum,int pageSize){

		PcResultObj result = new PcResultObj();
		
		result = lotteryService.getLotteryIssue(issue, pageNum, pageSize);
		
		return result;
	}
	/**
	 * 根据彩种和期号查询彩种期号详情
	 * @param lotteryId
	 * @param issueNo
	 * @return
	 */
	@RequestMapping("/findLotteryIssueDetail")
	@ResponseBody
	public PcResultObj findLotteryIssueDetail(String lotteryId,String issueNo){
		PcResultObj result = new PcResultObj();
		
		result = lotteryService.findLotteryIssue(lotteryId, issueNo);
		
		return result;
	}
	/**
	 * 删除彩种期号
	 * @param issue
	 * @return
	 */
	@RequestMapping("/deleteLotteryIssue")
	@ResponseBody
	public PcResultObj deleteLotteryIssue(HttpServletRequest request ,Issue issue){
		PcResultObj  result = new PcResultObj();
		
		
		result = lotteryService.deleteLotteryIssue(issue);
		
		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "删除彩种期号,"+issue.toString()+",操作成功", IFuserService.OPERATE_LOTTERY_MANAGE);
		}else{
			
			fuserService.addOperateLog(request, "删除彩种期号,"+issue.toString()+",操作失败:"+result.getMsg(), IFuserService.OPERATE_LOTTERY_MANAGE);
		}
		return result;
	}
	
	/**
	 * 修改彩种期号
	 * @param issue
	 * @return
	 */
	@RequestMapping("/updateLotteryIssue")
	@ResponseBody
	public PcResultObj updateLotteryIssue(HttpServletRequest request, Issue issue){
		PcResultObj result = new PcResultObj();
		
		result = lotteryService.updateLotteryIssue(issue);
		
		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "修改彩种期号,"+issue.toString()+",操作成功", IFuserService.OPERATE_LOTTERY_MANAGE);
		}else{
			
			fuserService.addOperateLog(request, "修改彩种期号,"+issue.toString()+",操作失败:"+result.getMsg(), IFuserService.OPERATE_LOTTERY_MANAGE);
		}
		return result;
	}
	
	
	/**
	 * 分页查询竞彩赛事信息列表
	 * @param request
	 * @param issue
	 * @return
	 */
	@RequestMapping("/getJcMatch")
	@ResponseBody
	public PcResultObj getJcMatch(HttpServletRequest request, MatchCompetive competive,Integer pageNum,Integer pageSize){
		PcResultObj result = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		result = lotteryService.getJcMacth(competive, pageNum, pageSize);
		return result;
	}
	
	
	@RequestMapping("/getJcMatchById")
	@ResponseBody
	public PcResultObj getJcMatchById(HttpServletRequest request, MatchCompetive competive){
		PcResultObj result = new PcResultObj();
		result = lotteryService.getJcMatchById(competive);
		return result;
	}
	
	@RequestMapping("/getJcMatchResultById")
	@ResponseBody
	public PcResultObj getJcMatchResultById(HttpServletRequest request, MatchCompetive competive,String lotteryId){
		PcResultObj result = new PcResultObj();
		result = lotteryService.getJcMatchResultById(competive,lotteryId);
		return result;
	}
	/**
	 * 修改竞彩赛事信息
	 */
	@RequestMapping("/updateJcMatch")
	@ResponseBody
	public PcResultObj updateJcMatch(HttpServletRequest request, MatchCompetive competive){
		PcResultObj result = new PcResultObj();
		
		result = lotteryService.updateJcMatch(competive);
		
		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "修改赛事transferId:"+competive.getTransferId()+",操作成功", IFuserService.OPERATE_LOTTERY_MANAGE);
		}else{
			
			fuserService.addOperateLog(request, "修改赛事transferId:"+competive.getTransferId()+",操作失败:"+result.getMsg(), IFuserService.OPERATE_LOTTERY_MANAGE);
		}
		return result;
	}
	
	/**
	 * 删除竞彩赛事信息(只需要修改状态就可以了)
	 */
	@RequestMapping("/delJcMatch")
	@ResponseBody
	public PcResultObj delJcMatch(HttpServletRequest request,MatchCompetive competive){
		PcResultObj result = new PcResultObj();
		result = lotteryService.delJcMatch(competive);
		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "删除赛事transferId:"+competive.getTransferId()+",操作成功", IFuserService.OPERATE_LOTTERY_MANAGE);
		}else{
			
			fuserService.addOperateLog(request, "删除赛事transferId:"+competive.getTransferId()+",操作失败:"+result.getMsg(), IFuserService.OPERATE_LOTTERY_MANAGE);
		}
		return result;
	}
	
	
	/**
	 * 创建或者修改赔率
	 * @param request
	 * @param competiveResult
	 * @return
	 */
	@RequestMapping("/updateMatchResult")
	@ResponseBody
	public PcResultObj updateMatchResult(HttpServletRequest request,MatchCompetiveResult competiveResult){
		PcResultObj result = new PcResultObj();
		result = lotteryService.createOrUpdateJcMatchResult(competiveResult);
		if(PcResultObj.SUCCESS_CODE.equals(result.getMsgCode())){
			
			fuserService.addOperateLog(request, "删除赛事transferId:"+competiveResult.getTransferId()+",操作成功", IFuserService.OPERATE_LOTTERY_MANAGE);
		}else{
			
			fuserService.addOperateLog(request, "删除赛事transferId:"+competiveResult.getTransferId()+",操作失败:"+result.getMsg(), IFuserService.OPERATE_LOTTERY_MANAGE);
		}
		return result;
	}
	
	/**
	 * 获取老足彩赛事
	 * @param request
	 * @param matchFootball
	 * @return
	 */
	@RequestMapping("/getFootballMatch")
	@ResponseBody
	public PcResultObj getFootballMatch(HttpServletRequest request,MatchFootball matchFootball){
		PcResultObj result = new PcResultObj();
		result = lotteryService.getLzcMactches(matchFootball);
		return result;
	}
	
	
	@RequestMapping("/getFootballMatchById")
	@ResponseBody
	public PcResultObj getFootballMatchById(HttpServletRequest request,MatchFootball matchFootball){
		PcResultObj result = new PcResultObj();
		result = lotteryService.getFootballMatchById(matchFootball);
		return result;
	}
	
	
	/**
	 * 修改老足彩赛事
	 * @param request
	 * @param matchFootball
	 * @return
	 */
	@RequestMapping("/updateFootballMatch")
	@ResponseBody
	public PcResultObj updateFootballMatch(HttpServletRequest request,MatchFootball matchFootball){
		PcResultObj result = new PcResultObj();
		result = lotteryService.udpateLzcMactche(matchFootball);
		return result;
	}
	/**
	 * 分页查询竞彩转移订单
	 */
	@RequestMapping("/getJcCancelOrder")
	@ResponseBody
	public PcResultObj getJcCancelOrder(HttpServletRequest request,SportCancelOrder sportCancelOrder,Integer pageNum,Integer pageSize){
		PcResultObj result = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		result = lotteryService.getJcCancelOrder(sportCancelOrder,pageNum,pageSize);
		return result;
	}
}
