package com.cqfc.businesscontroller.task;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partneraccount.PartnerPreApply;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader156;
import com.cqfc.xmlparser.TransactionMsgLoader756;
import com.cqfc.xmlparser.transactionmsg756.Body;
import com.jami.util.Log;

public class TransactionMsgProcessor156 {

	private static String returnCode = "756";

	/**
	 * 预存款申请订单查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理156消息");
		TransResponse response = new TransResponse();

		String xml156 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg156.Msg msg156 = TransactionMsgLoader156
				.xmlToMsg(xml156);

		com.cqfc.xmlparser.transactionmsg156.Body body156 = msg156.getBody();
		com.cqfc.xmlparser.transactionmsg156.Headtype head156 = msg156
				.getHead();
		com.cqfc.xmlparser.transactionmsg156.Querytype querytype156 = body156.getPreview();

		String partnerId = head156.getPartnerid();
		String version = head156.getVersion();

		String pid = querytype156.getPid();

		com.cqfc.xmlparser.transactionmsg756.Msg msg756 = new com.cqfc.xmlparser.transactionmsg756.Msg();
		com.cqfc.xmlparser.transactionmsg756.Body body756 = new Body();
		com.cqfc.xmlparser.transactionmsg756.Headtype head756 = new com.cqfc.xmlparser.transactionmsg756.Headtype();

		head756.setTime(DateUtil.getCurrentDateTime());
		head756.setPartnerid(partnerId);
		head756.setTranscode(returnCode);
		head756.setVersion(version);

		com.cqfc.xmlparser.transactionmsg756.Querytype querytype756 = new com.cqfc.xmlparser.transactionmsg756.Querytype();

		Log.fucaibiz.info("调用partner中的findLotteryPartnerById方法，参数partnerId="+partnerId);
		ReturnMessage partnerMsg = TransactionProcessor.dynamicInvoke(
				"partner", "findLotteryPartnerById", partnerId);
		LotteryPartner lotteryPartner = (LotteryPartner) partnerMsg.getObj();
		if (lotteryPartner == null) {

			response.setStatusCode(ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
			response.setData("合作商id不存在或者参数与xml文件中的不一致");
			Log.fucaibiz.info("合作商id不存在或者参数与xml文件中的不一致");
			return response;
		}

		boolean isPartnerAccount = lotteryPartner.getPartnerType() == 3 ? false
				: true;

		com.cqfc.processor.ReturnMessage reMsg = new ReturnMessage();
	
		
		String statusCode = "";
		// 平台用户
		if (!isPartnerAccount) {

			UserPreApply userPreApply = new UserPreApply();
			
			Log.fucaibiz.info("平台用户调用userAccount中的findUserPreApply方法,参数partnerId="+partnerId+"，pid="+pid);
			reMsg = TransactionProcessor.dynamicInvoke("userAccount",
					"findUserPreApply", partnerId,pid);
			statusCode = reMsg.getStatusCode();
			
			Log.fucaibiz.info("返回的状态码:"+statusCode);
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

				userPreApply = (UserPreApply) reMsg.getObj();
				
				querytype756.setPid(pid);
				querytype756.setMoney(MoneyUtil.toYuanStr(userPreApply.getPreMoney()));
				querytype756.setStatus(getAuditState(userPreApply.getStatus()));

				body756.setPreview(querytype756);
				
				
			} else {

				response.setStatusCode(statusCode);
				response.setData(reMsg.getMsg());
				Log.fucaibiz.error("错误的消息体："+reMsg.getMsg());
				return response;
			}
			

		} else {

			Log.fucaibiz.info("调用partnerAccount中的findPartnerPreApply方法,参数partnerId="+partnerId+",pid="+pid);
			PartnerPreApply partnerPreApply = new PartnerPreApply();
			reMsg = TransactionProcessor.dynamicInvoke("partnerAccount",
					"findPartnerPreApply", partnerId,pid);
			
			statusCode = reMsg.getStatusCode();
			Log.fucaibiz.info("返回的状态码:"+statusCode);
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

				partnerPreApply = (PartnerPreApply) reMsg.getObj();
				
				querytype756.setPid(pid);
				querytype756.setMoney(MoneyUtil.toYuanStr(partnerPreApply.getPreMoney()));
				querytype756.setStatus(String.valueOf(partnerPreApply.getStatus()));
				body756.setPreview(querytype756);
				
			} else {

				response.setStatusCode(statusCode);
				response.setData(reMsg.getMsg());
				Log.fucaibiz.info("返回的错误消息体："+reMsg.getMsg());
				return response;
			}
			
		}
		
		msg756.setBody(body756);
		msg756.setHead(head756);
		response.setData(TransactionMsgLoader756.msgToXml(msg756));
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setResponseTransCode(returnCode);
		
		
		Log.fucaibiz.info("756消息生成成功");
		return response;

	}
	
	public static String getAuditState(int state){
		
		if(state == 1){
			return "0";
		}
		if(state == 2){
			return "1";
		}
		if(state == 3){
			return "2";
		}
	
		return "";
}
}
