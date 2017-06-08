package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.List;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserAccountLog;
import com.cqfc.protocol.useraccount.UserAccountLogData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader132;
import com.cqfc.xmlparser.TransactionMsgLoader732;
import com.cqfc.xmlparser.transactionmsg732.Infolist;
import com.cqfc.xmlparser.transactionmsg732.Msg;
import com.cqfc.xmlparser.transactionmsg732.Useraccountlog;
import com.jami.util.Log;

public class TransactionMsgProcessor132 {

	private static String retrunCode = "732";

	/**
	 * 用户帐户明细查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理132消息");
		TransResponse response = new TransResponse();
		String xml132 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg132.Msg msg132 = TransactionMsgLoader132
				.xmlToMsg(xml132);

		com.cqfc.xmlparser.transactionmsg132.Body body132 = msg132.getBody();
		com.cqfc.xmlparser.transactionmsg132.Headtype head132 = msg132
				.getHead();
		com.cqfc.xmlparser.transactionmsg132.Querytype bodyInfo132 = body132
				.getQueryuseraccount();

		String partnerId = head132.getPartnerid();
		String version = head132.getVersion();
		String from = bodyInfo132.getFrom();
		String fromTime = String.valueOf(bodyInfo132.getFromtime());
		String size = bodyInfo132.getSize();
		String toTime = String.valueOf(bodyInfo132.getTotime());
		String userId = bodyInfo132.getUserid();

		long userIdTemp = 0;
		if(!DateUtil.isValidDate(fromTime) || !DateUtil.isValidDate(toTime)){
			
			response.setData("时间格式不正确");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			
			Log.fucaibiz.error("时间格式不正确fromTime="+fromTime+"---toTime="+toTime);
			return response;
		}
		
		if(!DataUtil.isNumeric(from) || !DataUtil.isNumeric(size)){
			
			response.setData("非法数字");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			Log.fucaibiz.error("非法数字from="+from+",size="+size+",from="+from);
			return response;
		}
		
		int pageNum = Integer.parseInt(from);
		int pageSize = Integer.parseInt(size);
		if(pageNum<=0){
			pageNum = 1 ;
		}
		if(pageSize<0||pageSize>50){
			pageSize = 50;
		}
		try {
			userIdTemp = Long.parseLong(userId);
		} catch (Exception e) {
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USERID_ERROR);
			response.setData("用户id错误");
			Log.fucaibiz.error("数据格式不正确"+userId,e);
			return response;
		}
		
		if(!CheckUser.validateUser(userIdTemp, partnerId)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			Log.fucaibiz.error(partnerId+"合作商没有该用户"+userIdTemp);
			return response;
		}
		
		
		com.cqfc.xmlparser.transactionmsg732.Msg msg732 = new Msg();
		com.cqfc.xmlparser.transactionmsg732.Headtype head732 = new com.cqfc.xmlparser.transactionmsg732.Headtype();
		com.cqfc.xmlparser.transactionmsg732.Body body732 = new com.cqfc.xmlparser.transactionmsg732.Body();
		com.cqfc.xmlparser.transactionmsg732.Querytype bodyInfo732 = new com.cqfc.xmlparser.transactionmsg732.Querytype();

		com.cqfc.xmlparser.transactionmsg732.Infolist infolist = new Infolist();

		List<Useraccountlog> useraccountlogs = infolist.getUseraccountlog();

		UserAccountLogData accountLogData = null;
		UserAccountLog userAccountLog = new UserAccountLog();

		userAccountLog.setPartnerId(partnerId);
		userAccountLog.setUserId(userIdTemp);
		userAccountLog.setSearchBeginTime(fromTime);
		userAccountLog.setSearchEndTime(toTime);
		ReturnMessage retMsg;
		
		
		Log.fucaibiz.info("调用userAccount中的getUserAccountLogList方法，参数userAccountLog="+userAccountLog+",from="+from+",size="+size);
		
		retMsg = TransactionProcessor.dynamicInvoke("userAccount",
				"getUserAccountLogList", userAccountLog,
				pageNum, pageSize);

		String statusCode = retMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码："+statusCode);
		
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			accountLogData = (UserAccountLogData) retMsg.getObj();
			List<UserAccountLog> userAccountLogs = accountLogData
					.getResultList();

			if (userAccountLogs.size() > 0) {

				Useraccountlog useraccount = null;
				for (int i = 0; i < userAccountLogs.size(); i++) {

					useraccount = new Useraccountlog();
					userAccountLog = userAccountLogs.get(i);

					useraccount.setLogId(String.valueOf(userAccountLog
							.getSerialNumber()));
					useraccount.setTotalMoney(MoneyUtil
							.toYuanStr(userAccountLog.getTotalAmount()));
					useraccount.setAccountMoney(MoneyUtil
							.toYuanStr(userAccountLog.getAccountAmount()));
					useraccount.setHandselMoney(MoneyUtil
							.toYuanStr(userAccountLog.getHandselAmount()));
					try {
						useraccount.setCreateTime((DateUtil.formatStringFour(userAccountLog.getCreateTime())));
					} catch (ParseException e) {
						Log.fucaibiz.error("时间格式不正确");
					}
					// 1充值 2支付 3提现 4退款 5派奖 6彩金赠送 7彩金失效
					useraccount.setOptCode(String.valueOf(userAccountLog
							.getLogType()));
					useraccount.setOptDes(userAccountLog.getRemark());
					useraccountlogs.add(useraccount);

				}

				bodyInfo732.setFrom(from);
				bodyInfo732.setSize(size);
				bodyInfo732.setUserid(userId);
				bodyInfo732.setTotalcount(String.valueOf(accountLogData
						.getTotalSize()));

				bodyInfo732.setInfolist(infolist);

				body732.setUseraccounts(bodyInfo732);

				head732.setPartnerid(partnerId);
				head732.setTime(DateUtil.getCurrentDateTime());
				head732.setTranscode(retrunCode);
				head732.setVersion(version);

				msg732.setBody(body732);
				msg732.setHead(head732);

				response.setData(TransactionMsgLoader732.msgToXml(msg732));
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				response.setResponseTransCode(retrunCode);
			} else {

				response.setData("未查询到数据");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				Log.fucaibiz.info("未查询到数据");
			}
		} else {

			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			Log.fucaibiz.error("错误状态码的消息体："+retMsg.getMsg());
			return response;
		}
		
		Log.fucaibiz.info("732消息生成成功");
		return response;
	}
	
	public static String transferCode(int type){
		/**
		 * 
		RECHARGE(1, "充值"), PAYMENT(2, "支付"), WITHDRAW(3, "提现"), REFUND(4, "退款"), PRIZE(5, "派奖"), HANDSEL_PRESENT(6,
				"彩金赠送"), HANDSEL_FAILURE(7, "彩金失效"), FREEZE_AMOUNT(8, "冻结金额"), USERPREAPPLY(9, "用户预存款");
		 */
		
		return "";
	}
	
}
