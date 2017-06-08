package com.cqfc.businesscontroller.task;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.userorder.Order;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.util.OrderStatus;
import com.cqfc.xmlparser.TransactionMsgLoader134;
import com.cqfc.xmlparser.TransactionMsgLoader734;
import com.cqfc.xmlparser.transactionmsg734.Body;
import com.cqfc.xmlparser.transactionmsg734.Headtype;
import com.cqfc.xmlparser.transactionmsg734.Msg;
import com.cqfc.xmlparser.transactionmsg734.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor134 {

	private static String returnCode = "734";

	/**
	 * 中奖订单查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		
		Log.fucaibiz.info("开始处理134消息");
		
		TransResponse response = new TransResponse();

		String xml134 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg134.Msg msg = TransactionMsgLoader134
				.xmlToMsg(xml134);

		com.cqfc.xmlparser.transactionmsg134.Body body = msg.getBody();
		com.cqfc.xmlparser.transactionmsg134.Headtype head = msg.getHead();
		com.cqfc.xmlparser.transactionmsg134.Querytype querytype = body
				.getPrizequery();

		String partnerId = head.getPartnerid();
		String version = head.getVersion();
		String pid = querytype.getPid();
		String orderNo = querytype.getSeriesno();
		String userId = querytype.getUserid();

		com.cqfc.xmlparser.transactionmsg734.Msg msg734 = new Msg();
		com.cqfc.xmlparser.transactionmsg734.Body body734 = new Body();
		com.cqfc.xmlparser.transactionmsg734.Headtype head734 = new Headtype();

		head734.setTime(DateUtil.getCurrentDateTime());
		head734.setPartnerid(partnerId);
		head734.setTranscode(returnCode);
		head734.setVersion(version);

		com.cqfc.xmlparser.transactionmsg734.Querytype querytype734 = new Querytype();

		Order order = new Order();
		long userIdTemp = 0;
		try {
			userIdTemp = Long.parseLong(userId);
		} catch (Exception e) {

			response.setStatusCode(ConstantsUtil.STATUS_CODE_USERID_ERROR);
			response.setData("用户Id格式不正确");
			Log.fucaibiz.error("用户Id格式不正确");
			return response;
		}
		
		
		if(!CheckUser.validateUser(userIdTemp, partnerId)){
			
			response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
			response.setData("用户不存在");
			Log.fucaibiz.error(partnerId+"合作商没有该用户"+userIdTemp);
			return response;
		}
		
		
		// 1:i64 userId,2:string partnerId,3:string orderNo,4:string ticketId
		
		Log.fucaibiz.info("调用userOrder中的getUserPrizeStatus方法，参数userId="+userIdTemp
				+",partnerId="+partnerId+",orderNo="+orderNo+",pid="+pid);
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("userOrder", "getUserPrizeStatus", userIdTemp,
						partnerId, orderNo, pid);

		String statusCode = reMsg.getStatusCode();

		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			order = (Order) reMsg.getObj();

			if (order != null) {

				querytype734.setBalls(order.getOrderContent());
				querytype734.setGameid(order.getLotteryId());
				querytype734.setIssueno(order.getIssueNo());
				querytype734.setPid(order.getTradeId());

				// 1待付款 2已付款 3出票中 4已出票待开奖 5出票失败 6未中奖 7待领奖 8已领奖 9退款中 10退款成功
				// 11订单取消',
				if (order.getOrderStatus() == OrderStatus.NOT_WIN_PRIZE) {

					querytype734.setPrizestatus("1");

				} else if (order.getOrderStatus() == OrderStatus.WAIT_TO_AWARD_PRIZE) {

					querytype734.setPrizestatus("2");
				} else if (order.getOrderStatus() == OrderStatus.FINISH_AWARD_PRIZE ) {

					querytype734.setPrizestatus("3");
				} else {

					querytype734.setPrizestatus("0");
				}

				querytype734.setPrizemoney(MoneyUtil
						.toYuanStr(order.getWinPrizeMoney()*order.getMultiple()));

				querytype734.setSeriesno(order.getOrderNo());

				querytype734.setUserid(String.valueOf(order.getUserId()));
			} else {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				response.setData("未查询到数据");
				return response;

			}

			body734.setResult(querytype734);
			msg734.setBody(body734);
			msg734.setHead(head734);
			response.setData(TransactionMsgLoader734.msgToXml(msg734));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("返回的错误消息体："+reMsg.getMsg());
			return response;

		}
		Log.fucaibiz.info("734消息体生成成功");
		return response;
	}
}
