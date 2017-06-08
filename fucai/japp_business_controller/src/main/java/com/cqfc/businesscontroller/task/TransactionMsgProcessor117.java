package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.List;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.protocol.useraccount.WithdrawApplyData;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader117;
import com.cqfc.xmlparser.TransactionMsgLoader717;
import com.cqfc.xmlparser.transactionmsg717.Body;
import com.cqfc.xmlparser.transactionmsg717.Encash;
import com.cqfc.xmlparser.transactionmsg717.Infolist;
import com.cqfc.xmlparser.transactionmsg717.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor117 {

	private static String returnCode = "717";

	/**
	 * 提现记录查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理117消息体");
		
		TransResponse response = new TransResponse();

		String xml117 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg117.Msg msg117 = TransactionMsgLoader117
				.xmlToMsg(xml117);

		com.cqfc.xmlparser.transactionmsg117.Body body117 = msg117.getBody();
		com.cqfc.xmlparser.transactionmsg117.Headtype head117 = msg117
				.getHead();
		com.cqfc.xmlparser.transactionmsg117.Querytype querytype117 = body117
				.getQuerywithdraw();

		String headPartnerId = head117.getPartnerid();
		String version = head117.getVersion();

		String from = querytype117.getFrom();
		String size = querytype117.getSize();
		String id = querytype117.getId();
		String userId = querytype117.getUserid();
		String fromTime = querytype117.getFromtime();
		long userIdTemp = 0;
		try {
			
			 userIdTemp = Long.parseLong(userId);
		} catch (Exception e) {
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			response.setData("用户id错误");
			Log.fucaibiz.error("用户id错误",e);
			return response;
		}
		
		if(!CheckUser.validateUser(userIdTemp, headPartnerId)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			Log.fucaibiz.error(headPartnerId+"合作商没有该用户"+userIdTemp);
			return response;
		}
		
		if (!DateUtil.isValidDate(fromTime)) {

			response.setData("时间格式不正确");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			Log.fucaibiz.error("时间格式不正确");
			return response;
		}

		if (!DataUtil.isNumeric(from) || !DataUtil.isNumeric(size)
				) {

			response.setData("非法数字");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			Log.fucaibiz.error("非法数字");
			return response;
		}

		int fromTemp = Integer.parseInt(from);
		int sizeTemp = Integer.parseInt(size);
		if(fromTemp < 1 ){
			fromTemp = 1;
		}
		if(sizeTemp<0||sizeTemp>50){
			sizeTemp = 50;
		}
		
		
		com.cqfc.xmlparser.transactionmsg717.Msg msg717 = new com.cqfc.xmlparser.transactionmsg717.Msg();
		com.cqfc.xmlparser.transactionmsg717.Body body717 = new Body();
		com.cqfc.xmlparser.transactionmsg717.Headtype head717 = new com.cqfc.xmlparser.transactionmsg717.Headtype();
		com.cqfc.xmlparser.transactionmsg717.Querytype bodyInfo717 = new Querytype();
		com.cqfc.xmlparser.transactionmsg717.Encash encash = null;
		com.cqfc.xmlparser.transactionmsg717.Infolist infolist = new Infolist();
		List<Encash> encashs = infolist.getEncash();

		head717.setTime(DateUtil.getCurrentDateTime());
		head717.setPartnerid(headPartnerId);
		head717.setTranscode(returnCode);
		head717.setVersion(version);

		WithdrawApply withdrawApply = new WithdrawApply();

		WithdrawApplyData withdrawApplyData = null;
		withdrawApply.setUserId(Long.parseLong(userId));
		withdrawApply.setPartnerId(headPartnerId);
		withdrawApply.setPartnerApplyId(id);
		withdrawApply.setSearchBeginTime(fromTime);
		
		Log.fucaibiz.info("调用userAccount中的getWithdrawApplyList接口，参数withdrawApply="+withdrawApply+"，from="+from+",size="+size);
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("userAccount", "getWithdrawApplyList",
						withdrawApply, fromTemp,
						sizeTemp);

		String statusCode = reMsg.getStatusCode();

		Log.fucaibiz.info("返回的状态码"+statusCode);
		
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			withdrawApplyData = (WithdrawApplyData) reMsg.getObj();

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
							encash.setStatus(DataUtil.getAuditState(reply.getAuditState()));
							encash.setUsername(reply.getRealName());
							encash.setBankName(account.getBankName());
							encash.setBankCardno(account.getAccountNo());
							if(reply.getAuditRemark()==null){
								reply.setAuditRemark("");
							}
							if(reply.getWithdrawMsgId()==null){
								reply.setWithdrawMsgId("");
							}
							encash.setProcessnote(reply.getAuditRemark());
							encash.setEiid(reply.getWithdrawMsgId());
							encash.setPartnerEncashid(reply.getSerialNumber());
					try {
							encash.setCreatetime(DateUtil.formatStringFour(reply.getCreateTime()));
							
							if(reply.getAuditTime()!=null && !"".equals(reply.getAuditTime())){
								encash.setProcesstime(DateUtil.formatStringFour(reply.getAuditTime()));
								
							}else{
								encash.setProcesstime("");
							}
							
					} catch (ParseException e) {
						
						Log.fucaibiz.error("时间格式不正确",e);
						encash.setCreatetime("");
						encash.setProcesstime("");
					}
					encashs.add(encash);
				}

				bodyInfo717.setInfolist(infolist);
				bodyInfo717.setFrom(from);
				bodyInfo717.setSize(size);
				bodyInfo717.setUserid(userId);
				bodyInfo717.setTotalcount(String.valueOf(withdrawApplyData
						.getTotalSize()));

				body717.setEncashes(bodyInfo717);
				head717.setPartnerid(headPartnerId);
				head717.setTime(DateUtil.getCurrentDateTime());
				head717.setTranscode(returnCode);
				head717.setVersion(version);

				msg717.setBody(body717);
				msg717.setHead(head717);

				response.setData(TransactionMsgLoader717.msgToXml(msg717));
				response.setResponseTransCode(returnCode);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);

			} else {

				response.setData("未查询到数据");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				Log.fucaibiz.info("未查询到数据");
			}
		} else {

			response.setStatusCode(String.valueOf(statusCode));
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("返回的错误消息:"+reMsg.getMsg());
			return response;
		}

		Log.fucaibiz.info("返回717消息成功");
		
		return response;
	}

}
