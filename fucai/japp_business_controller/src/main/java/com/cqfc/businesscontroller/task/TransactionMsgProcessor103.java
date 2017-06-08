package com.cqfc.businesscontroller.task;

import java.util.List;

import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.partnerorder.IssueSaleAndReward;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader103;
import com.cqfc.xmlparser.TransactionMsgLoader703;
import com.cqfc.xmlparser.transactionmsg703.Body;
import com.cqfc.xmlparser.transactionmsg703.Headtype;
import com.cqfc.xmlparser.transactionmsg703.Msg;
import com.cqfc.xmlparser.transactionmsg703.Querytype;
import com.cqfc.xmlparser.transactionmsg703.Stat;
import com.cqfc.xmlparser.transactionmsg703.Stats;
import com.jami.util.Log;

public class TransactionMsgProcessor103 {


	private static String returnCode = "703";

	/**
	 * 103接口为销售统计接口，在期结束后可通过该接口查询当期的销售统计， 在期兑奖结束后可通过该接口查询可的销售统计和中奖统计信息
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理103消息");
		TransResponse response = new TransResponse();
		String xml103 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg103.Msg msg103 = TransactionMsgLoader103
				.xmlToMsg(xml103);

		com.cqfc.xmlparser.transactionmsg103.Headtype head103 = msg103
				.getHead();

		com.cqfc.xmlparser.transactionmsg103.Body body103 = msg103.getBody();

		String partnerId = head103.getPartnerid();
		String version = head103.getVersion();

		com.cqfc.xmlparser.transactionmsg103.Querytype querytype103 = body103
				.getQueryprizenotice();

		String lotteryId = querytype103.getGameid();
		String issueNo = querytype103.getIssue();

		com.cqfc.xmlparser.transactionmsg703.Msg msg703 = new Msg();

		com.cqfc.xmlparser.transactionmsg703.Body body703 = new Body();
		com.cqfc.xmlparser.transactionmsg703.Headtype head703 = new Headtype();

		head703.setPartnerid(partnerId);
		head703.setTime(DateUtil.getCurrentDateTime());
		head703.setVersion(version);
		head703.setTranscode(returnCode);

		com.cqfc.xmlparser.transactionmsg703.Querytype querytype703 = new Querytype();

		com.cqfc.xmlparser.transactionmsg703.Stats stats703 = new Stats();

		List<Stat> stats = stats703.getStat();
		Stat stat = null;
		long totalSale = 0;
		long totalBouns = 0;


		Log.fucaibiz.info("开始调用partnerOrder中的getIssueSaleAndReward方法,参数:partnerId="+partnerId+",lotteryId="+lotteryId+",issueNo="+issueNo);
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getIssueSaleAndReward",
						partnerId,lotteryId,issueNo);
		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)&&reMsg
				.getObj()!=null) {

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

					stat.setBigprizebonus(MoneyUtil.toYuanStr(temp
							.getBigPrizeMoney()));
					stat.setBigprizenum(String.valueOf(temp.getBigPrizeNum()));

					stat.setSmallprize(MoneyUtil.toYuanStr(temp
							.getSmallPrizeMoney()));
					stat.setSmallprizenum(String.valueOf(temp
							.getSmallPrizeNum()));

					stat.setType(DataUtil.changeOrderType(String.valueOf(temp.getOrderType())));

					totalSale += temp.getSucMoney()+temp.getFailMoney();
					totalBouns += temp.getSmallPrizeMoney()
							+ temp.getBigPrizeMoney();

					stats.add(stat);
				}
			} else {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				response.setData("未查询到数据");
				Log.fucaibiz.info("未查询到数据");
				return response;

			}

			querytype703.setTotalbouns(MoneyUtil.toYuanStr(totalBouns));
			querytype703.setTotalsale(MoneyUtil.toYuanStr(totalSale));
			querytype703.setGameid(lotteryId);
			querytype703.setIssue(issueNo);
			querytype703.setStats(stats703);
			body703.setStatinfo(querytype703);
			msg703.setBody(body703);
			msg703.setHead(head703);
			response.setData(TransactionMsgLoader703.msgToXml(msg703));
			response.setResponseTransCode(returnCode);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			Log.fucaibiz.info("703消息体成功生成");
		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("返回状态的错误消息:"+reMsg.getMsg());
			return response;
		}

		return response;
	}
}
