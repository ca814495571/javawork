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
import com.cqfc.xmlparser.TransactionMsgLoader141;
import com.cqfc.xmlparser.TransactionMsgLoader741;
import com.cqfc.xmlparser.transactionmsg741.Body;
import com.cqfc.xmlparser.transactionmsg741.Headtype;
import com.cqfc.xmlparser.transactionmsg741.Msg;
import com.cqfc.xmlparser.transactionmsg741.Partnerdatesbean;
import com.cqfc.xmlparser.transactionmsg741.Partnerdatesbeans;
import com.cqfc.xmlparser.transactionmsg741.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor141 {

	
	private static String returnCode = "741";

	/**
	 * 141接口为渠道日销售结果的查询接口
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理141消息");
		
		TransResponse response = new TransResponse();
		
		String xml141 = message.getTransMsg();
		
		com.cqfc.xmlparser.transactionmsg141.Msg msg141 = TransactionMsgLoader141.xmlToMsg(xml141); 
		
		com.cqfc.xmlparser.transactionmsg141.Body body141 = msg141.getBody();
		com.cqfc.xmlparser.transactionmsg141.Headtype head141 = msg141.getHead();
		com.cqfc.xmlparser.transactionmsg141.Querytype querytype141 = body141.getPartnerDateQuery();
		
		String partnerId = head141.getPartnerid();
		String version = head141.getVersion();
		String countTime = querytype141.getDate();
		
		com.cqfc.xmlparser.transactionmsg741.Msg msg741= new Msg();
		com.cqfc.xmlparser.transactionmsg741.Body body741= new Body();
		com.cqfc.xmlparser.transactionmsg741.Headtype head741= new Headtype();
		
		head741.setTime(DateUtil.getCurrentDateTime());
		head741.setPartnerid(partnerId);
		head741.setTranscode(returnCode);
		head741.setVersion(version);
		
		com.cqfc.xmlparser.transactionmsg741.Querytype querytype741= new Querytype();
		com.cqfc.xmlparser.transactionmsg741.Partnerdatesbeans partnerdatasbeans = new Partnerdatesbeans();
		
		List<com.cqfc.xmlparser.transactionmsg741.Partnerdatesbean> partnerdatasbeanList  =  partnerdatasbeans.getPartnerdatesbean();
		com.cqfc.xmlparser.transactionmsg741.Partnerdatesbean partnerdatasbean = null;
		
		String countTimeTemp ="";
		try {
			countTimeTemp = DateUtil.getDateTime("yyyy-MM-dd", DateUtil.convertStringToDate("yyyyMMdd", countTime));
		} catch (ParseException e) {
			Log.fucaibiz.error("时间格式错误",e);
		}
		
		Log.fucaibiz.info("调用partnerOrder中的getDailySaleAndCharge方法,参数:partnerId"+partnerId+",countTimeTemp:"+countTimeTemp);
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor.dynamicInvoke("partnerOrder", "getDailySaleAndCharge",partnerId,countTimeTemp );
		
		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)){
			
			List<DailySaleAndCharge> dailySaleCounts  = (List<DailySaleAndCharge>) reMsg.getObj();
			
			if(dailySaleCounts.size()>0){
				
				for(DailySaleAndCharge temp :dailySaleCounts){
					
					partnerdatasbean = new Partnerdatesbean();
					
					partnerdatasbean.setPartnerId(temp.getPartnerId());
					partnerdatasbean.setGameId(temp.getLotteryId());
					partnerdatasbean.setSaleAccount(MoneyUtil.toYuanStr(temp.getTotalMoney()));
					partnerdatasbean.setPrizeAccount(MoneyUtil.toYuanStr(temp.getAwardPrizeMoney()));
					partnerdatasbean.setProxyAccount("0");
					partnerdatasbean.setEncachMoney(MoneyUtil.toYuanStr(temp.getEncashTotalMoney()));
					partnerdatasbean.setChargeMoney(MoneyUtil.toYuanStr(temp.getChargeTotalMoney()));
					partnerdatasbean.setStatTime(countTime);
					partnerdatasbeanList.add(partnerdatasbean);
					
				}
			}else{
				
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				response.setData("未查询到数据");
				Log.fucaibiz.info("未查询到数据");
				return response;
				
			}
			
			querytype741.setPartnerid(partnerId);
			
			querytype741.setPartnerdatesbeans(partnerdatasbeans);
			body741.setPartnerdatas(querytype741);
			
			msg741.setBody(body741);
			msg741.setHead(head741);
			
			response.setData(TransactionMsgLoader741.msgToXml(msg741));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		}else{
			
			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("产生的错误消息:"+reMsg.getMsg());
			return response;
			
		}
		
		Log.fucaibiz.info("生成741消息成功");
		return response;
	}
}
