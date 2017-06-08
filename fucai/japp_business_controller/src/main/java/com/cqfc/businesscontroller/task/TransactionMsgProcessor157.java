package com.cqfc.businesscontroller.task;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserPreApply;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader157;
import com.cqfc.xmlparser.TransactionMsgLoader757;
import com.cqfc.xmlparser.transactionmsg757.Body;
import com.jami.util.Log;

public class TransactionMsgProcessor157 {

	private static String returnCode = "757";

	/**
	 * 预存款申请接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理157消息");
		
		TransResponse response = new TransResponse();

		String xml157 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg157.Msg msg157 = TransactionMsgLoader157
				.xmlToMsg(xml157);

		com.cqfc.xmlparser.transactionmsg157.Body body157 = msg157.getBody();
		com.cqfc.xmlparser.transactionmsg157.Headtype head157 = msg157
				.getHead();
		com.cqfc.xmlparser.transactionmsg157.Querytype querytype157 = body157
				.getPreview();

		String partnerId = head157.getPartnerid();
		String version = head157.getVersion();

		String money = querytype157.getMoney();
		String userId = querytype157.getUserId();
		String pid = querytype157.getPid();

		com.cqfc.xmlparser.transactionmsg757.Msg msg757 = new com.cqfc.xmlparser.transactionmsg757.Msg();
		com.cqfc.xmlparser.transactionmsg757.Body body757 = new Body();
		com.cqfc.xmlparser.transactionmsg757.Headtype head757 = new com.cqfc.xmlparser.transactionmsg757.Headtype();

		head757.setTime(DateUtil.getCurrentDateTime());
		head757.setPartnerid(partnerId);
		head757.setTranscode(returnCode);
		head757.setVersion(version);

		com.cqfc.xmlparser.transactionmsg757.Querytype querytype757 = new com.cqfc.xmlparser.transactionmsg757.Querytype();
		
		
		long userIdTemp = 0 ;
		
		
		try {
			userIdTemp = Long.parseLong(userId);
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
		
		if(pid==null||"".equals(pid)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_PARAM_NULL);
			response.setData("参数为空");
			Log.fucaibiz.error("参数"+pid+"为空");
			return response;
		}
		
		
		com.cqfc.processor.ReturnMessage reMsg = new ReturnMessage();
		// 平台用户

		UserPreApply userPreApply = new UserPreApply();

		userPreApply.setUserId(userIdTemp);
		userPreApply.setPartnerId(partnerId);
		userPreApply.setPartnerUniqueNo(pid);
		try {
			
			userPreApply
			.setPreMoney(MoneyUtil.toCent(Double.parseDouble(money)));
			
		} catch (Exception e) {
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_MISTAKE);
			response.setData("For input string: " + money);
			Log.fucaibiz.info("money格式错误");
			return response;
			// TODO: handle exception
		}
		Log.fucaibiz.info("调用userAccount中的createUserPreApply方法,参数:userPreApply="+userPreApply);
		
		reMsg = TransactionProcessor.dynamicInvoke("userAccount",
				"createUserPreApply", userPreApply);


		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			int flag = (Integer) reMsg.getObj();

			if (flag == 1) {

				querytype757.setPid(pid);
				querytype757.setStatus("0");
				querytype757.setDescription("申请成功,待审核");

			} else {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_RRADE_FAIL);
				response.setData("申请失败,请检查订单号是否重复");
				Log.fucaibiz.info("申请失败");
				return response;

			}

			body757.setPreview(querytype757);
			msg757.setBody(body757);
			msg757.setHead(head757);
			response.setData(TransactionMsgLoader757.msgToXml(msg757));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);

		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.info("返回的错误消息"+reMsg.getMsg());
			
			return response;
		}
		
		Log.fucaibiz.info("757消息生成成功");
		return response;
	}

}
