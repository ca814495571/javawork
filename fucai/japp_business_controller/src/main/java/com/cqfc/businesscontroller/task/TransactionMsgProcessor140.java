package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.List;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.partnerorder.LotteryIssueSale;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader140;
import com.cqfc.xmlparser.TransactionMsgLoader740;
import com.cqfc.xmlparser.transactionmsg740.Body;
import com.cqfc.xmlparser.transactionmsg740.Headtype;
import com.cqfc.xmlparser.transactionmsg740.Msg;
import com.cqfc.xmlparser.transactionmsg740.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor140 {

	private static String returnCode = "740";

	/**
	 * 140接口为渠道期销售结果的查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理140消息");
		TransResponse response = new TransResponse();

		String xml140 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg140.Msg msg140 = TransactionMsgLoader140
				.xmlToMsg(xml140);

		com.cqfc.xmlparser.transactionmsg140.Body body140 = msg140.getBody();
		com.cqfc.xmlparser.transactionmsg140.Headtype head140 = msg140
				.getHead();
		com.cqfc.xmlparser.transactionmsg140.Querytype querytype140 = body140
				.getPartnerIssueQuery();

		String partnerId = head140.getPartnerid();
		String version = head140.getVersion();
		String lotteryId = querytype140.getGameid();
		String issueNo = querytype140.getIssueno();

		com.cqfc.xmlparser.transactionmsg740.Msg msg740 = new Msg();
		com.cqfc.xmlparser.transactionmsg740.Body body740 = new Body();
		com.cqfc.xmlparser.transactionmsg740.Headtype head740 = new Headtype();

		head740.setTime(DateUtil.getCurrentDateTime());
		head740.setPartnerid(partnerId);
		head740.setTranscode(returnCode);
		head740.setVersion(version);

		com.cqfc.xmlparser.transactionmsg740.Querytype querytype740 = new Querytype();

		Log.fucaibiz
				.info("调用partnerOrder中getIssueSaleAndRewardByGroup方法，参数partnerId="
						+ partnerId
						+ ",lotteryId="
						+ lotteryId
						+ ",issueNo="
						+ issueNo);

		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getIssueSaleAndRewardByGroup",
						partnerId, lotteryId, issueNo);

		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:" + statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			List<LotteryIssueSale> issueSaleCounts = (List<LotteryIssueSale>) reMsg
					.getObj();

			if (issueSaleCounts.size() > 0) {

				LotteryIssueSale issueSaleAndReward = issueSaleCounts.get(0);

				querytype740.setGameId(issueSaleAndReward.getLotteryId());
				querytype740.setIssueNo(issueSaleAndReward.getIssueNo());
				querytype740.setPartnerId(issueSaleAndReward.getPartnerId());
				querytype740.setStatType("0");
				querytype740.setBigPrizeStakes(String
						.valueOf(issueSaleAndReward.getBigPrizeNum()));
				querytype740.setTotalBigBonus(MoneyUtil
						.toYuanStr(issueSaleAndReward.getBigPrizeMoney()));
				querytype740.setSmallPrizeStakes(String
						.valueOf(issueSaleAndReward.getSmallPrizeNum()));
				querytype740.setTotalSmallBonus(MoneyUtil
						.toYuanStr(issueSaleAndReward.getSmallPrizeMoney()));
				querytype740.setTotalSucStakes(String
						.valueOf(issueSaleAndReward.getSucNum()));
				querytype740.setTotalSucSale(MoneyUtil
						.toYuanStr(issueSaleAndReward.getSucMoney()));
				querytype740.setTotalFailStakes(String
						.valueOf(issueSaleAndReward.getFailNum()));
				querytype740.setTotalFailSale(MoneyUtil
						.toYuanStr(issueSaleAndReward.getFailMoney()));
				querytype740.setTotalSale(MoneyUtil
						.toYuanStr(issueSaleAndReward.getSucMoney()
								+ issueSaleAndReward.getFailMoney()));
				querytype740.setTotalBonus(MoneyUtil
						.toYuanStr(issueSaleAndReward.getBigPrizeMoney()
								+ issueSaleAndReward.getSmallPrizeMoney()));
				try {
					querytype740.setCreateTime(DateUtil
							.formatStringFour(issueSaleAndReward
									.getCreateTime()));
				} catch (ParseException e) {
					Log.run.error(e);
				}
			} else {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTEXIST_ISSUENO);
				response.setData("不存在的期次");
				return response;

			}
			body740.setPartnersalestat(querytype740);
			msg740.setBody(body740);
			msg740.setHead(head740);
			response.setData(TransactionMsgLoader740.msgToXml(msg740));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);
		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());

			Log.fucaibiz.error("错误的消息:" + reMsg.getMsg());

			return response;
		}

		Log.fucaibiz.info("740消息生成成功");
		return response;
	}

}
