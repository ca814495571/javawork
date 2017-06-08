package com.cqfc.businesscontroller.task;

import java.util.Date;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.UserHandsel;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.IdWorker;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.ServiceStatusCodeUtil;
import com.cqfc.xmlparser.TransactionMsgLoader111;
import com.cqfc.xmlparser.TransactionMsgLoader711;
import com.cqfc.xmlparser.transactionmsg711.Msg;
import com.cqfc.xmlparser.transactionmsg711.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor111 {

	/**
	 * 彩金赠送接口为合作商提供了彩金赠送的功能， 彩金的赠送是平台从合作商账户的资金账户中扣除添加到增加的用户的彩金账户，
	 * 彩金账户的资金一旦添加用户只能用来购彩不能提现。彩金的赠送需要合作商在
	 * 平台的管理平台提交彩金活动申请，由平台审核通过后，合作商获得该次活动的活动id，
	 * 在赠送请求中发送该id，以便平台控制活动的金额和便于平台对活动的数据统计
	 */

	private static String retrunCode = "711";

	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理111消息体");
		
		TransResponse response = new TransResponse();

		String xml111 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg111.Msg msg111 = TransactionMsgLoader111
				.xmlToMsg(xml111);
		com.cqfc.xmlparser.transactionmsg111.Headtype head111 = msg111
				.getHead();
		com.cqfc.xmlparser.transactionmsg111.Body body111 = msg111.getBody();
		com.cqfc.xmlparser.transactionmsg111.Querytype queryType111 = body111
				.getGiftinfo();

		String partnerId = head111.getPartnerid();
		String version = head111.getVersion();
		String activityId = queryType111.getActivityid();
		// 该彩金交易在合作商的彩金id，不能重复
		String giftId = queryType111.getGiftid();
		String money = queryType111.getGiftmoney();
		String userId = queryType111.getUserid();
		
		com.cqfc.xmlparser.transactionmsg711.Msg msg711 = new Msg();
		com.cqfc.xmlparser.transactionmsg711.Body body711 = new com.cqfc.xmlparser.transactionmsg711.Body();
		com.cqfc.xmlparser.transactionmsg711.Headtype head711 = new com.cqfc.xmlparser.transactionmsg711.Headtype();
		com.cqfc.xmlparser.transactionmsg711.Querytype bodyInfo711 = new Querytype();

		int flag = 0;
		Date date = new Date();
		String palmgiftid;

		
		try {
			
			

			
			head711.setTime(DateUtil.getCurrentDateTime());
			head711.setPartnerid(partnerId);
			head711.setVersion(version);
			head711.setTranscode(retrunCode);
			msg711.setHead(head711);
			response.setResponseTransCode(retrunCode);

			bodyInfo711.setActivityid(activityId);
			bodyInfo711.setGiftid(giftId);
			bodyInfo711.setGiftmoney(money);
			bodyInfo711.setUserid(userId);
			bodyInfo711.setPalmgiftid("");
			if("".equals(giftId)){
				
				bodyInfo711
				.setStatuscode(ConstantsUtil.STATUS_CODE_PARAM_NULL);
				bodyInfo711.setMsg("参数为空");
				msg711.setBody(body711);

				response.setData(TransactionMsgLoader711.msgToXml(msg711));
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
				
				return response;
			}
			
		
			long userIdTemp = Integer.parseInt(userId);
			if(!CheckUser.validateUser(userIdTemp, partnerId)){
				
				response.setData("用户不存在");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
				Log.fucaibiz.info("合作商"+partnerId+"用户不存在userId"+userId);
				return response;
				
			}
			
			UserHandsel userHandsel = new UserHandsel();
			
			userHandsel.setUserId(userIdTemp);
			userHandsel.setPartnerId(partnerId);
			userHandsel.setActivityId(activityId);
			userHandsel
					.setPresentAmount(MoneyUtil.toCent(Long.parseLong(money)));
			palmgiftid = IdWorker.getSerialNumber();
			bodyInfo711.setPalmgiftid(palmgiftid);
			userHandsel.setSerialNumber(palmgiftid);
			userHandsel
					.setUsableAmount(MoneyUtil.toCent(Long.parseLong(money)));
			userHandsel.setValidTime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", date));
			userHandsel.setFailureTime(DateUtil.yearAdd(date, 100));
			userHandsel.setPresentTime(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", date));
			userHandsel.setPartnerHandselId(giftId);



			Log.fucaibiz.info("调用userAccount模块createUserHandsel方法,参数:"+userHandsel);
			
			ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
					"userAccount", "createUserHandsel", userHandsel);

			String statusCode = retMsg.getStatusCode();
			Log.fucaibiz.info("返回的状态码:"+statusCode);
			// 只对添加数据返回结果惊醒处理，没对插入相同数据的异常处理


			
			if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)&&retMsg.getObj()!=null) {

				flag = (Integer) retMsg.getObj();

				if (flag == ServiceStatusCodeUtil.STATUS_CODE_OPERATE_SUCCESS) {

					bodyInfo711
							.setStatuscode(ConstantsUtil.STATUS_CODE_TRADE_SUCCESS);
					bodyInfo711.setMsg("赠送成功");

				} else {

					bodyInfo711
							.setStatuscode(ConstantsUtil.STATUS_CODE_RRADE_FAIL);
					bodyInfo711.setMsg("彩金id重复,赠送失败");
				}

			} else {

				bodyInfo711.setStatuscode(statusCode);
				bodyInfo711.setMsg("系统错误");

			}

		} catch (Exception e) {

			if(e instanceof NumberFormatException){
				
				Log.fucaibiz.error("money或者userId格式不正确");
				
			}
			Log.fucaibiz.error(e);
			bodyInfo711.setStatuscode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			bodyInfo711.setMsg("非法数字");
		}
		
		body711.setGiftinfo(bodyInfo711);
		msg711.setBody(body711);

		response.setData(TransactionMsgLoader711.msgToXml(msg711));
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		
		Log.fucaibiz.info("711消息体返回成功");
		return response;
	}
}
