package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.List;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.protocol.useraccount.WithdrawApplyData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.UserAccountConstant;
import com.cqfc.xmlparser.TransactionMsgLoader131;
import com.cqfc.xmlparser.TransactionMsgLoader731;
import com.cqfc.xmlparser.transactionmsg731.Body;
import com.cqfc.xmlparser.transactionmsg731.Encash;
import com.cqfc.xmlparser.transactionmsg731.Headtype;
import com.cqfc.xmlparser.transactionmsg731.Infolist;
import com.cqfc.xmlparser.transactionmsg731.Msg;
import com.cqfc.xmlparser.transactionmsg731.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor131 {

	private static String retrunCode = "731";

	/**
	 * 用户提现明细查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理131消息体");
		
		TransResponse response = new TransResponse();

		String xml131 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg131.Msg msg131 = TransactionMsgLoader131
				.xmlToMsg(xml131);

		com.cqfc.xmlparser.transactionmsg131.Headtype head131 = msg131
				.getHead();
		com.cqfc.xmlparser.transactionmsg131.Body body131 = msg131.getBody();
		com.cqfc.xmlparser.transactionmsg131.Querytype bodyInfo131 = body131
				.getQueryencash();

		String partnerId = head131.getPartnerid();
		String version = head131.getVersion();

		String from = bodyInfo131.getFrom();
		String fromTime = bodyInfo131.getFromtime();
		String size = bodyInfo131.getSize();
		String toTime = bodyInfo131.getTotime();
		String userId = bodyInfo131.getUserid();

		long userIdTemp = 0;
		int fromTemp = 0 , sizeTemp =0;
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
		try {
			userIdTemp = Long.parseLong(userId);
			fromTemp = Integer.parseInt(from);
			if(fromTemp < 1){
				fromTemp =1;
			}
			sizeTemp = Integer.parseInt(size);
			if(sizeTemp>500){
				sizeTemp = 500;
			}
		} catch (Exception e) {
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			response.setData("非法数字");
			Log.fucaibiz.error("数据格式不正确"+userId,e);
			return response;
		}
		
		if(!CheckUser.validateUser(userIdTemp, partnerId)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			Log.fucaibiz.error(partnerId+"合作商没有该用户"+userIdTemp);
			return response;
		}
		com.cqfc.xmlparser.transactionmsg731.Msg msg731 = new Msg();
		com.cqfc.xmlparser.transactionmsg731.Body body731 = new Body();
		com.cqfc.xmlparser.transactionmsg731.Headtype head731 = new Headtype();
		com.cqfc.xmlparser.transactionmsg731.Querytype bodyInfo731 = new Querytype();
		com.cqfc.xmlparser.transactionmsg731.Encash encash = null;
		com.cqfc.xmlparser.transactionmsg731.Infolist infolist = new Infolist();
		List<Encash> encashs = infolist.getEncash();

		WithdrawApplyData withdrawApplyData = null;

		WithdrawApply withdrawApply = new WithdrawApply();

		withdrawApply.setSearchBeginTime(fromTime);
		withdrawApply.setSearchEndTime(toTime);
		withdrawApply.setUserId(userIdTemp);
		ReturnMessage retMsg;
		
		
		Log.fucaibiz.info("调用userAccount中getWithdrawApplyList,参数withdrawApply="+withdrawApply+"from="+from+",size="+size);
		retMsg = TransactionProcessor.dynamicInvoke("userAccount",
				"getWithdrawApplyList", withdrawApply,fromTemp ,sizeTemp
				);

		String statusCode = retMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			withdrawApplyData = (WithdrawApplyData) retMsg.getObj();

			List<WithdrawApply> withdrawReplies = withdrawApplyData
					.getResultList();

			if (withdrawReplies.size() > 0) {

				WithdrawApply reply = null;
				WithdrawAccount account = null;

				for (int i = 0; i < withdrawReplies.size(); i++) {

					encash = new Encash();
					reply = withdrawReplies.get(i);
					account = reply.getWithdrawAccount();

					encash.setUserid(String.valueOf(reply.getUserId()));
					encash.setEncashid(reply.getPartnerApplyId());
					encash.setMoney(MoneyUtil.toYuanStr(reply
							.getWithdrawAmount()));
					try {
						encash.setCreatetime(DateUtil.formatStringFour(reply.getCreateTime()));
					} catch (ParseException e) {
						encash.setCreatetime("");;
						Log.fucaibiz.error("时间格式不正确");
					}
					encash.setStatus(getAuditState(reply.getAuditState()));
					encash.setUsername(reply.getRealName());
					encash.setBankname(account.getBankName());
					encash.setBankcardno(account.getAccountNo());
					encash.setProcesstime(reply.getAuditTime());
					encash.setProcessnote(reply.getAuditRemark());
					encash.setEiid(reply.getWithdrawMsgId());
					encash.setPartnerencashid(reply.getSerialNumber());

					encashs.add(encash);
				}

				bodyInfo731.setInfolist(infolist);
				bodyInfo731.setFrom(from);
				bodyInfo731.setSize(size);
				bodyInfo731.setUserid(userId);
				bodyInfo731.setTotalcount(String.valueOf(withdrawApplyData
						.getTotalSize()));

				body731.setEncashes(bodyInfo731);
				head731.setPartnerid(partnerId);
				head731.setTime(DateUtil.getCurrentDateTime());
				head731.setTranscode(retrunCode);
				head731.setVersion(version);

				msg731.setBody(body731);
				msg731.setHead(head731);

				response.setData(TransactionMsgLoader731.msgToXml(msg731));
				response.setResponseTransCode(retrunCode);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);

			} else {

				response.setData("未查询到数据");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				Log.fucaibiz.error("未查询到数据");
			}
		} else {

			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			Log.fucaibiz.error("返回的错误消息:"+retMsg.getMsg());
			return response;
		}
		
		Log.fucaibiz.info("731消息体生成成功");
		return response;
	}
	
	
	
	public static String getAuditState(int state){
	
			if(state == UserAccountConstant.WithdrawAuditState.NOT_AUDIT.getValue()){
				return "0";
			}
			if(state == UserAccountConstant.WithdrawAuditState.AUDIT_PASS.getValue()){
				return "5";
			}
			if(state == UserAccountConstant.WithdrawAuditState.AUDIT_NOPASS.getValue()){
				return "4";
			}
		
			return "";
	}
	
}
