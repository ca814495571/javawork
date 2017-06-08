package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.lotteryissue.LotteryDrawLevel;
import com.cqfc.protocol.lotteryissue.LotteryDrawResult;
import com.cqfc.protocol.lotteryissue.LotteryIssue;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.LotteryType;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader602;
import com.cqfc.xmlparser.transactionmsg602.Body;
import com.cqfc.xmlparser.transactionmsg602.Headtype;
import com.cqfc.xmlparser.transactionmsg602.Levelinfo;
import com.cqfc.xmlparser.transactionmsg602.Levelinfos;
import com.cqfc.xmlparser.transactionmsg602.Msg;
import com.cqfc.xmlparser.transactionmsg602.Querytype;
import com.cqfc.xmlparser.transactionmsg602.Saleinfo;
import com.cqfc.xmlparser.transactionmsg602.Saleinfos;
import com.jami.util.Log;

public class TransactionMsgProcessor602 {

	private static String retrunCode = "602";

	/**
	 * 
	 * 
	 * zwt test
	 * 
	 * @param 待定
	 * @return
	 */
//	public static TransResponse process(Message message) {
    public static TransResponse process (String partnerId ,String lotteryId,
			String issueNo){
    	
    	Log.fucaibiz.info("开始处理602");
    	
		TransResponse response = new TransResponse();
		String xml602 = "";
//		String transMsg = message.getTransMsg();
//		System.out.println("入口发来的102消息：" + transMsg);
//		com.cqfc.xmlparser.transactionmsg102.Msg msg102 = TransactionMsgLoader102
//				.xmlToMsg(transMsg);
//		com.cqfc.xmlparser.transactionmsg102.Headtype head102 = msg102
//				.getHead();
//		com.cqfc.xmlparser.transactionmsg102.Querytype queryType102 = msg102
//				.getBody().getQueryprizenotice();

//		String lotteryId = queryType102.getGameid();
//		String issueNo = queryType102.getIssue();

		com.cqfc.xmlparser.transactionmsg602.Msg msg602 = new Msg();
		Headtype head602 = new Headtype();
		Body body602 = new Body();
		Querytype querytype602 = new Querytype();

		Levelinfo levelinfo602 = null;
		Levelinfos levelinfos602 = new Levelinfos();
		List<Levelinfo> levelinfo602s = null;
		List<LotteryDrawLevel> lotteryDrawLevels = null;
		LotteryDrawLevel lotteryDrawLevel = null;

		Saleinfo saleinfo602 = null;
		Saleinfos saleinfos602 = new Saleinfos();
		List<Saleinfo> saleinfo602s = null;
		LotteryDrawResult drawResult = null;
		LotteryIssue lotteryResult = null;
		String prizePool = "";

		Log.fucaibiz.info("调用lotteryIssue中的findLotteryDrawResult方法，参数：lotteryId="+lotteryId+",issueNo="+issueNo);
		
		com.cqfc.processor.ReturnMessage reMsg;
		reMsg = TransactionProcessor.dynamicInvoke("lotteryIssue",
				"findLotteryDrawResult", lotteryId, issueNo);

		
		Log.fucaibiz.info("返回的状态码:"+reMsg
				.getStatusCode());
		
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(reMsg
				.getStatusCode())) {

			drawResult = (LotteryDrawResult) reMsg.getObj();
		} else {

			response.setData(reMsg.getMsg());
			response.setStatusCode(reMsg.getStatusCode());
			Log.fucaibiz.error("返回的错误的消息："+reMsg.getMsg());
			return response;
		}
		
		Log.fucaibiz.info("调用lotteryIssue中的findLotteryIssue方法，参数lotteryId="+lotteryId+"，issueNo="+issueNo);
		com.cqfc.processor.ReturnMessage msg = TransactionProcessor
				.dynamicInvoke("lotteryIssue", "findLotteryIssue", lotteryId,
						issueNo);

		Log.fucaibiz.info("返回的状态码:"+msg
				.getStatusCode());
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(msg
				.getStatusCode())) {

			lotteryResult = (LotteryIssue) msg.getObj();
			
			prizePool = String.valueOf(lotteryResult.getPrizePool());
		}

		lotteryDrawLevels = drawResult.getLotteryDrawLevelList();

		for (int i = 0; i < lotteryDrawLevels.size(); i++) {

			lotteryDrawLevel = lotteryDrawLevels.get(i);
			levelinfo602 = new Levelinfo();

			
			
			levelinfo602.setLevel(String.valueOf(lotteryDrawLevel.getLevel()));
			levelinfo602.setMoney(MoneyUtil.toYuanStr(lotteryDrawLevel.getMoney()));
			levelinfo602.setCount(String.valueOf(lotteryDrawLevel
					.getTotalCount()));
			levelinfo602.setName(lotteryDrawLevel.getLevelName());

			levelinfo602s = levelinfos602.getLevelinfo();
			levelinfo602s.add(levelinfo602);
		}
		Log.fucaibiz.info("开奖公告信息获取成功..");
		
		LotteryIssueSale lotteryIssueSale = new LotteryIssueSale();
		
		lotteryIssueSale.setIssueNo(issueNo);
		lotteryIssueSale.setLotteryId(lotteryId);
		msg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getLotteryIssueSaleByWhere",lotteryIssueSale, 0,0);
		List<LotteryIssueSale> lotteryIssueSales = (List<LotteryIssueSale>) msg.getObj();
		if(lotteryIssueSales.size() > 0 ){
			
			lotteryIssueSale = lotteryIssueSales.get(0);
			saleinfo602 = new Saleinfo();

			saleinfo602.setMoney(MoneyUtil.toYuanStr(lotteryIssueSale.getSucMoney()));
			saleinfo602.setType(lotteryId);
			saleinfo602.setTypename(LotteryType.getLotteryName(lotteryId));
			saleinfo602s = saleinfos602.getSaleinfo();
			saleinfo602s.add(saleinfo602);
		}else{
			
			saleinfo602 = new Saleinfo();

			saleinfo602.setMoney("0");
			saleinfo602.setType(lotteryId);
			saleinfo602.setTypename(LotteryType.getLotteryName(lotteryId));
			saleinfo602s = saleinfos602.getSaleinfo();
			saleinfo602s.add(saleinfo602);
		}
		
		Log.fucaibiz.info("销售信息获取成功..");
		querytype602.setLevelinfos(levelinfos602);
		querytype602.setSaleinfos(saleinfos602);
		querytype602.setGameid(drawResult.getLotteryId());
		querytype602.setIssue(drawResult.getIssueNo());
		querytype602.setPricepool(prizePool);
		querytype602.setPrizeball(drawResult.getDrawResult());
//		querytype602.setProvince(queryType102.getProvince());
		querytype602.setStatus(String.valueOf(drawResult.getState()));

		body602.setPrizeinfo(querytype602);

//		head602.setPartnerid(head102.getPartnerid()); 
		head602.setPartnerid(partnerId);
//		head602.setTime(head102.getTime());
		head602.setTime(DateUtil.getCurrentDateTime());
		head602.setTranscode(retrunCode);
//		head602.setVersion(head102.getVersion());

		msg602.setHead(head602);
		msg602.setBody(body602);

		xml602 = TransactionMsgLoader602.msgToXml(msg602);

		response.setData(xml602);
		response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
		response.setResponseTransCode(retrunCode);
		Log.fucaibiz.info("602消息返回成功");
		return response;
	}
}
