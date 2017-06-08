package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.List;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.partnerorder.DailySaleAndCharge;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader149;
import com.cqfc.xmlparser.TransactionMsgLoader749;
import com.cqfc.xmlparser.transactionmsg749.Body;
import com.cqfc.xmlparser.transactionmsg749.Headtype;
import com.cqfc.xmlparser.transactionmsg749.Msg;
import com.cqfc.xmlparser.transactionmsg749.Partnerdatebean;
import com.cqfc.xmlparser.transactionmsg749.Partnerdatebeans;
import com.cqfc.xmlparser.transactionmsg749.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor149 {

	private static String returnCode = "749";

	/**
	 * 149接口为渠道日销售结果的查询接口，
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		
		Log.fucaibiz.info("开始处理149消息体");
		
		TransResponse response = new TransResponse();

		String xml149 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg149.Msg msg149 = TransactionMsgLoader149
				.xmlToMsg(xml149);

		com.cqfc.xmlparser.transactionmsg149.Body body149 = msg149.getBody();
		com.cqfc.xmlparser.transactionmsg149.Headtype head149 = msg149
				.getHead();
		com.cqfc.xmlparser.transactionmsg149.Querytype querytype149 = body149
				.getPartnerDateQuery();

		String headPartnerId = head149.getPartnerid();
		String version = head149.getVersion();
		String countTime = querytype149.getDate();
		String partnerId = querytype149.getPartnerid();

		if(partnerId==null || "".equals(partnerId)){
			partnerId = headPartnerId;
		}
		com.cqfc.xmlparser.transactionmsg749.Msg msg749 = new Msg();
		com.cqfc.xmlparser.transactionmsg749.Body body749 = new Body();
		com.cqfc.xmlparser.transactionmsg749.Headtype head749 = new Headtype();

		head749.setTime(DateUtil.getCurrentDateTime());
		head749.setPartnerid(partnerId);
		head749.setTranscode(returnCode);
		head749.setVersion(version);

		com.cqfc.xmlparser.transactionmsg749.Querytype querytype749 = new Querytype();
		com.cqfc.xmlparser.transactionmsg749.Partnerdatebeans partnerdatasbeans = new Partnerdatebeans();
		List<com.cqfc.xmlparser.transactionmsg749.Partnerdatebean> partnerdatasbeanList = partnerdatasbeans
				.getPartnerdatebean();
		com.cqfc.xmlparser.transactionmsg749.Partnerdatebean partnerdatasbean = null;

		String  countTimeTemp = "";
		try {
			countTimeTemp = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.convertStringToDate("yyyyMMdd", countTime));
		} catch (ParseException e) {
			Log.fucaibiz.error("时间格式错误",e);
		}
		Log.fucaibiz.info("调用partnerOrder中的getDailySaleAndCharge方法,参数partnerId="+partnerId+",countTime="+countTimeTemp);
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getDailySaleAndCharge",
						partnerId , countTimeTemp);

		String statusCode = reMsg.getStatusCode();

		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			List<DailySaleAndCharge> dailySaleCounts = (List<DailySaleAndCharge>) reMsg
					.getObj();

			if (dailySaleCounts.size() > 0) {

				for (DailySaleAndCharge temp : dailySaleCounts) {

					partnerdatasbean = new Partnerdatebean();

					partnerdatasbean.setPartnerId(temp.getPartnerId());
					partnerdatasbean.setGameId(temp.getLotteryId());
					partnerdatasbean.setSaleAccount(MoneyUtil
							.toYuanStr(temp.getTotalMoney()));
					partnerdatasbean.setPrizeAccount(MoneyUtil
							.toYuanStr(temp.getAwardPrizeMoney()));
					partnerdatasbean.setEncachMoney(MoneyUtil.toYuanStr(temp.getEncashTotalMoney()));
					partnerdatasbean.setChargeMoney(MoneyUtil.toYuanStr(temp.getChargeTotalMoney()));
					partnerdatasbean.setStatTime(countTime);
//					partnerdatasbean.setChargeMoney(MoneyUtil
//							.toYuanStr(temp.getChargeTotalMoney()));
//					partnerdatasbean.setEncachMoney(MoneyUtil
//							.toYuanStr(temp.getEncashTotalMoney()));
					partnerdatasbean.setProxyAccount("0");
					partnerdatasbeanList.add(partnerdatasbean);

				}
			} else {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				response.setData("未查询到数据");
				Log.fucaibiz.info("未查询到数据");
				return response;

			}

			querytype749.setPartnerid(partnerId);
			querytype749.setPartnerdatebeans(partnerdatasbeans);
			body749.setPartnerdatas(querytype749);
			msg749.setBody(body749);
			msg749.setHead(head749);
			response.setData(TransactionMsgLoader749.msgToXml(msg749));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			
			Log.fucaibiz.error("错误的消息体:"+reMsg.getMsg());
			return response;
		}

		Log.fucaibiz.info("749消息生成成功");
		return response;
	}
}
