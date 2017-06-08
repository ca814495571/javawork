package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.protocol.partnerorder.IssueSaleCount;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader603;
import com.cqfc.xmlparser.transactionmsg603.Body;
import com.cqfc.xmlparser.transactionmsg603.Headtype;
import com.cqfc.xmlparser.transactionmsg603.Msg;
import com.cqfc.xmlparser.transactionmsg603.Querytype;
import com.cqfc.xmlparser.transactionmsg603.Stat;
import com.jami.util.Log;

public class TransactionMsgProcessor603 {

	private static String returnCode = "603";

	public static TransResponse process(String partnerId, String lotteryId,
			String issueNo) {
		
		Log.fucaibiz.info("开始处理603");
		
		TransResponse response = new TransResponse();

		Msg msg603 = new Msg();

		com.cqfc.xmlparser.transactionmsg603.Headtype head603 = new Headtype();
		com.cqfc.xmlparser.transactionmsg603.Body body603 = new Body();

		head603.setPartnerid(partnerId);
		head603.setTime(DateUtil.getCurrentDateTime());
		head603.setVersion("");

		com.cqfc.xmlparser.transactionmsg603.Querytype querytype603 = new Querytype();

		com.cqfc.xmlparser.transactionmsg603.Stats stats603 = querytype603
				.getStats();

		List<Stat> stats = stats603.getStat();
		Stat stat = null;
		IssueSaleCount issueSaleCount = new IssueSaleCount();

		long totalSale = 0;
		long totalBouns = 0;

		issueSaleCount.setLotteryId(lotteryId);
		issueSaleCount.setPartnerId(partnerId);
		issueSaleCount.setIssueNo(issueNo);

		Log.fucaibiz.info("调用partnerOrder中getIssueSaleAndRewardByWhere方法,参数issueSaleCount="+issueSaleCount);
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getIssueSaleAndRewardByWhere",
						issueSaleCount);

		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			List<IssueSaleAndReward> issueSaleCounts = (List<IssueSaleAndReward>) reMsg
					.getObj();

			if (issueSaleCounts.size() > 0) {

				for (IssueSaleAndReward temp : issueSaleCounts) {

					stat = new Stat();

					stat.setSucmoney(MoneyUtil.toYuanStr(temp
							.getSucMoney()));
					stat.setSucnum(String.valueOf(temp.getSucNum()));

					stat.setFailmoney(MoneyUtil.toYuanStr(temp
							.getFailMoney()));
					stat.setFailnum(String.valueOf(temp.getFailNum()));

					stat.setBigprize(MoneyUtil.toYuanStr(temp
							.getBigPrizeMoney()));
					stat.setBigprizenum(String.valueOf(temp.getBigPrizeNum()));

					stat.setSmallprize(MoneyUtil.toYuanStr(temp
							.getSmallPrizeMoney()));
					stat.setSmallprizenum(String.valueOf(temp
							.getSmallPrizeNum()));

					stat.setType(String.valueOf(temp.getOrderType()));

					totalSale += temp.getSucMoney();
					totalBouns += temp.getSmallPrizeMoney()
							+ temp.getBigPrizeMoney();

					stats.add(stat);
				}
			}else{
				
				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				response.setData("期号未统计完成");
				Log.fucaibiz.info("期号未统计完成");
				
				return response;
			}

			querytype603.setTotalbouns(MoneyUtil.toYuanStr(totalBouns));
			querytype603.setTotalsale(MoneyUtil.toYuanStr(totalSale));
			querytype603.setGameid(lotteryId);
			querytype603.setIssue(issueNo);
			querytype603.setStats(stats603);
			body603.setStatinfo(querytype603);
			msg603.setBody(body603);
			msg603.setHead(head603);
			
			response.setData(TransactionMsgLoader603.msgToXml(msg603));
			response.setResponseTransCode(returnCode);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);

		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.info("返回的消息体:"+reMsg.getMsg());
			return response;
		}
		
		Log.fucaibiz.info("603消息生成成功");
		return response;

	}
}
