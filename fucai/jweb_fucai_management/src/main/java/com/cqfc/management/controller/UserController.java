package com.cqfc.management.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.management.model.AccountInfo;
import com.cqfc.management.model.AccountLog;
import com.cqfc.management.model.PcResultObj;
import com.cqfc.management.model.UserDepositCheck;
import com.cqfc.management.model.UserWithDrawCheck;
import com.cqfc.management.service.IFuserService;
import com.cqfc.management.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final int DEFAUT_PAGE_SIZE = 10;
	private static final int MAX_PAGE_SIZE = 100;
	
	
	@Autowired
	IUserService userService;
	
	
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
//		return userService.getOrderByWhere(orderInfo);
//
//	}
	/**
	 * 根据用户Id查询用户信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/getUserAccount")
	@ResponseBody
	public PcResultObj getUserAccount(String id){
		
		PcResultObj pcResultObj = new PcResultObj();
		//验证权限（session 和 url）
		
		
		long userId = 0L;
		try {
			
			userId = Long.parseLong(id);
		} catch (Exception e) {
			
			pcResultObj.setMsg("用户的帐号格式错误");
			pcResultObj.setMsgCode("2");
			return pcResultObj;
		}
		pcResultObj = userService.getUserById(userId);
		return pcResultObj;
		
		
	}
	
	/**
	 * 查询用户提现信息
	 * @param userWithDrawCheck
	 * @return
	 */
	@RequestMapping("/getUserWithDrawInfo")
	@ResponseBody
	public PcResultObj getUserWithDrawInfo(UserWithDrawCheck userWithDrawCheck,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = userService.getUserWithdrawApply(userWithDrawCheck,pageNum,pageSize);
		
		return pcResultObj;
	}
	
	/**
	 * 审核提现记录（同意）
	 * @param applyId
	 * @return
	 */
	@RequestMapping("/userWithDrawCheck/agree")
	@ResponseBody
	public PcResultObj userWithDrawCheckSuc(HttpServletRequest request,UserWithDrawCheck userWithDrawCheck){
		PcResultObj pcResultObj = new PcResultObj();
		
		if(userWithDrawCheck.getApplyId()==null||userWithDrawCheck.getApplyId()<0){
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("提现记录异常");
			return pcResultObj;
		}
		
		pcResultObj = userService.checkWithdrawSuc(userWithDrawCheck.getApplyId());
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "审核提现记录:"+userWithDrawCheck.toString()+" 审核成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "审核提现记录:"+userWithDrawCheck.toString()+"审核失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	/**
	 * 审核提现记录（不同意）
	 * @param applyId
	 * @return
	 */
	@RequestMapping("/userWithDrawCheck/disAgree")
	@ResponseBody
	public PcResultObj userWithDrawCheckFail(HttpServletRequest request,UserWithDrawCheck userWithDrawCheck){
		
		PcResultObj pcResultObj = new PcResultObj();		
		if(userWithDrawCheck.getApplyId()==null||userWithDrawCheck.getApplyId()<0){
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("提现记录异常");
			return pcResultObj;
		}
		pcResultObj = userService.checkWithdrawFail(userWithDrawCheck.getApplyId());
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "审核提现记录:"+userWithDrawCheck.toString()+" 审核未通过成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "审核提现记录:"+userWithDrawCheck.toString()+" 审核未通过失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
	}
	
	/**
	 * 根据条件查询预存款信息
	 * @param userDepositCheck
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getPreUserDeposit")
	@ResponseBody
	public PcResultObj getUserPreDeposit(UserDepositCheck userDepositCheck,Integer pageNum ,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = userService.getUserPreSave(userDepositCheck, pageNum, pageSize);
		
		return pcResultObj;
		
	}
	
	/**
	 * 审核预存款信息(同意)
	 * @param applyId
	 * @return
	 */
	@RequestMapping("/preUserDepositCheck/agree")
	@ResponseBody
	public PcResultObj userPreDepositCheckAgree(HttpServletRequest request,UserDepositCheck userDepositCheck){
		PcResultObj pcResultObj = new PcResultObj();
		if(userDepositCheck.getId()==null||userDepositCheck.getId()<0){
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("预存款记录异常");
			return pcResultObj;
		}
		pcResultObj = userService.CheckUserPreSaveAgree(userDepositCheck.getId());
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "审核预存款记录:"+userDepositCheck.toString()+" 审核通过成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "审核预存款记录:"+userDepositCheck.toString()+" 审核通过失败:"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		
		return pcResultObj;
		
	}
	
	/**
	 * 审核预存款信息(不同意)
	 * @param applyId
	 * @return
	 */
	@RequestMapping("/preUserDepositCheck/disAgree")
	@ResponseBody
	public PcResultObj userPreDepositCheckDisAgree(HttpServletRequest request,UserDepositCheck userDepositCheck){
		PcResultObj pcResultObj = new PcResultObj();
		if(userDepositCheck.getId()==null||userDepositCheck.getId()<0){
			pcResultObj.setMsgCode(PcResultObj.FAIL_CODE);
			pcResultObj.setMsg("预存款记录异常");
			return pcResultObj;
		}
		pcResultObj = userService.CheckUserPreSaveDisAgree(userDepositCheck.getId());
		
		if(pcResultObj.getMsgCode().equals(PcResultObj.SUCCESS_CODE)){
			fuserService.addOperateLog(request, "审核预存款记录:"+userDepositCheck.toString()+" 审核未通过成功", IFuserService.OPERATE_FINANCE_MANAGE);
		}else{
			fuserService.addOperateLog(request, "审核预存款记录:"+userDepositCheck.toString()+" 审核未通过失败"+pcResultObj.getMsg(), IFuserService.OPERATE_FINANCE_MANAGE);
		}
		return pcResultObj;
		
	}
	
	/**
	 * 根据条件查询账户信息
	 * @param accountInfo
	 * @return
	 */
	@RequestMapping("/getAccountInfo")
	@ResponseBody
	public PcResultObj getAccountInfo(AccountInfo accountInfo){
		
		PcResultObj pcResultObj = new PcResultObj();
		
		pcResultObj = userService.getAccountInfoByWhere(accountInfo);
		
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
	@RequestMapping("/getAccountLog")
	@ResponseBody
	public PcResultObj getAccountLog(AccountLog accountLog,String fromTime,String toTime,Integer pageNum,Integer pageSize){
		
		PcResultObj pcResultObj = new PcResultObj();
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || (pageSize <= 0 || pageSize > MAX_PAGE_SIZE)) {
			pageSize = DEFAUT_PAGE_SIZE;
		}
		pcResultObj = userService.getLogInfoByWhere(accountLog, fromTime, toTime, pageNum, pageSize);
		
		return pcResultObj;
		
	}
	
	
}
