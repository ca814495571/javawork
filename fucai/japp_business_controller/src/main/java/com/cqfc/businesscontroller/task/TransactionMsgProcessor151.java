package com.cqfc.businesscontroller.task;

import java.text.ParseException;

import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.xmlparser.TransactionMsgLoader151;
import com.cqfc.xmlparser.TransactionMsgLoader751;
import com.cqfc.xmlparser.transactionmsg151.Msg;
import com.cqfc.xmlparser.transactionmsg151.Querytype;
import com.cqfc.xmlparser.transactionmsg751.Body;
import com.cqfc.xmlparser.transactionmsg751.Headtype;
import com.jami.util.Log;

public class TransactionMsgProcessor151 {

	private static String returnCode = "751";

	/**
	 * 该接口是彩票期次信息的交易接口，该接口包含了彩种期次、交易的开始时间和 结束时间，以及平台系统接收订单的最后时间和期次状态，当前期次状态。
	 * 如果该彩种已经获得开奖公告，返回开奖号码。
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		
		Log.fucaibiz.info("开始处理151消息");
		
		TransResponse response = new TransResponse();
		String xml751 = "";
		String xml151 = message.getTransMsg();
		Msg msg151 = TransactionMsgLoader151.xmlToMsg(xml151);


		com.cqfc.xmlparser.transactionmsg151.Headtype head151 = msg151
				.getHead();
		com.cqfc.xmlparser.transactionmsg151.Body body151 = msg151.getBody();
		Querytype queryType151 = body151.getQueryissue();

		String lotteryId = queryType151.getGameid();
		String issueNo = queryType151.getIssueno();
		// ==================================以上是151相关数据处理，下面是751数据处理========================
		Headtype head751 = new Headtype();
		Body body751 = new Body();
		com.cqfc.xmlparser.transactionmsg751.Msg msg751 = new com.cqfc.xmlparser.transactionmsg751.Msg();
		com.cqfc.xmlparser.transactionmsg751.Querytype quertType751 = new com.cqfc.xmlparser.transactionmsg751.Querytype();
		LotteryIssue lotteryResult = null;
		// 目前考虑到的异常：数据库连接异常，查询结果null异常
		ReturnMessage retMsg = null;
		
		Log.fucaibiz.info("调用lotteryIssue中的findLotteryIssue方法,参数:lotteryId="+lotteryId+"，issueNo="+issueNo);
		retMsg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"findLotteryIssue", lotteryId, issueNo);

		String statusCode = retMsg.getStatusCode();

		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			lotteryResult = (LotteryIssue) retMsg.getObj();

		} else {

			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			
			Log.fucaibiz.info("返回状态的错误消息体："+retMsg.getMsg());
			return response;
		}

		head751.setPartnerid(head151.getPartnerid());
		head751.setTime(DateUtil.getCurrentDateTime());
		head751.setTranscode(returnCode);
		head751.setVersion(head151.getVersion());

		quertType751.setIssue(lotteryResult.getIssueNo());
		quertType751.setGameid(lotteryResult.getLotteryId());
		quertType751.setPrizeball(lotteryResult.getDrawResult());
		try {
			quertType751.setStarttime(DateUtil.formatStringFour(lotteryResult.getOfficialBeginTime()));
		
			quertType751.setEndtime(DateUtil.formatStringFour(lotteryResult.getOfficialEndTime()));
			quertType751.setPalmtime(DateUtil.formatStringFour(lotteryResult.getCompoundEndTime()));
			quertType751.setPrizetime(DateUtil.formatStringFour(lotteryResult.getDrawTime()));
			quertType751
				.setUnionendtime(DateUtil.formatStringFour(lotteryResult.getCompoundTogetherEndTime()));
		// 0为预销售
		// 1为销售中
		// 2为已结期
		// 3为已获得开奖公告
		// 4 为已经兑奖完毕
		quertType751.setStatus(DataUtil.transferState(lotteryResult.getState()));
		} catch (ParseException e) {
			quertType751.setStarttime("");
			quertType751.setEndtime("");
			quertType751.setPalmtime("");
			quertType751.setPrizetime("");
			quertType751
			.setUnionendtime("");
			Log.fucaibiz.error("时间格式不正确");
		}
		body751.setIssueinfo(quertType751);

		msg751.setBody(body751);
		msg751.setHead(head751);

		xml751 = TransactionMsgLoader751.msgToXml(msg751);

		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setData(xml751);
		response.setResponseTransCode(returnCode);
		Log.fucaibiz.info("751消息生成成功");
		return response;
	}
	
}
