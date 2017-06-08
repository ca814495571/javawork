package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.Date;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserRecharge;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IdWorker;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader119;
import com.cqfc.xmlparser.TransactionMsgLoader619;
import com.cqfc.xmlparser.transactionmsg619.Body;
import com.jami.util.Log;

public class TransactionMsgProcessor119 {

	private static String returnCode = "619";

	/**
	 * 合作商为用户充值接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理119消息");
		
		TransResponse response = new TransResponse();

		String xml119 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg119.Msg msg119 = TransactionMsgLoader119
				.xmlToMsg(xml119);

		com.cqfc.xmlparser.transactionmsg119.Body body119 = msg119.getBody();
		com.cqfc.xmlparser.transactionmsg119.Headtype head119 = msg119
				.getHead();
		com.cqfc.xmlparser.transactionmsg119.Querytype querytype119 = body119
				.getChargeinfo();

		String partnerId = head119.getPartnerid();
		String version = head119.getVersion();

		String id = querytype119.getId();
		String money = querytype119.getMoney();
		String userId = querytype119.getUserid();
		String degist = querytype119.getDegist();
		com.cqfc.xmlparser.transactionmsg619.Msg msg619 = new com.cqfc.xmlparser.transactionmsg619.Msg();
		com.cqfc.xmlparser.transactionmsg619.Body body619 = new Body();
		com.cqfc.xmlparser.transactionmsg619.Headtype head619 = new com.cqfc.xmlparser.transactionmsg619.Headtype();

		head619.setTime(DateUtil.getCurrentDateTime());
		head619.setPartnerid(partnerId);
		head619.setTranscode(returnCode);
		head619.setVersion(version);

		com.cqfc.xmlparser.transactionmsg619.Querytype querytype619 = new com.cqfc.xmlparser.transactionmsg619.Querytype();
		long chargeMoney =0 ;
		String serialNumber = "";
		long userIdTemp = 0;
		
		if("".equals(id)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_PARAM_NULL);
			response.setData("参数为空");
			Log.fucaibiz.error("id为空");
			return response;
		}
		
		
		try {
			userIdTemp = Long.parseLong(userId);
			chargeMoney = MoneyUtil.toCent(Double.parseDouble(money));
			if(chargeMoney<=0){
				
				response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
				response.setData("非法数字");
				Log.fucaibiz.error("充值金额数据错误money="+money);
				return response;
				
			}
			serialNumber = IdWorker.getSerialNumber();
		} catch (Exception e) {
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			response.setData("非法数字");
			Log.fucaibiz.error("流水号生成失败或者数据格式不正确",e);
			return response;
		}
		
		
		
		
		if(!CheckUser.validateUser(userIdTemp, partnerId)){
			
			
			querytype619.setUserid(userId);
			querytype619.setMoney(money);
			querytype619.setChargeid(id);
			querytype619.setId("");
			querytype619.setTradetime("");
			querytype619.setStatuscode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			querytype619.setMsg("用户不存在");
			body619.setChargeinfo(querytype619);
			msg619.setBody(body619);
			msg619.setHead(head619);
			response.setData(TransactionMsgLoader619.msgToXml(msg619));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			return response;
			
		}
		
		com.cqfc.processor.ReturnMessage reMsg = null;
		UserRecharge userRecharge = new UserRecharge();
		userRecharge.setUserId(userIdTemp);
		userRecharge.setPartnerId(partnerId);
		userRecharge.setPartnerRechargeId(id);
		userRecharge.setRechargeAmount(chargeMoney);
		userRecharge.setRemark(degist);
		userRecharge.setSerialNumber(serialNumber);
		Log.fucaibiz.info("调用userAccount中的createUserRecharge,参数:"+userRecharge);
		
		reMsg = TransactionProcessor
				.dynamicInvoke("userAccount", "createUserRecharge",
						userRecharge);
		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码为:"+statusCode);
		
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			int flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				querytype619.setMsg("充值成功");
				querytype619.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);

			} else {
				
				response.setData("充值订单号重复");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_RECHARGE_ORDERNOREPEAT);
				return  response;
			}

			querytype619.setUserid(userId);
			querytype619.setMoney(money);
			querytype619.setId(serialNumber);
			querytype619.setChargeid(id);
			
			try {
				querytype619.setTradetime(DateUtil.formatStringFour(new Date()));
			} catch (ParseException e) {
				Log.fucaibiz.error("时间格式不正确");
			}

			body619.setChargeinfo(querytype619);
			msg619.setBody(body619);
			msg619.setHead(head619);
			response.setData(TransactionMsgLoader619.msgToXml(msg619));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("错误的msg:"+reMsg.getMsg());
			
			return response;
		}

		Log.fucaibiz.info("返回619消息成功");
		return response;
	}
}
