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
import com.cqfc.xmlparser.TransactionMsgLoader148;
import com.cqfc.xmlparser.TransactionMsgLoader748;
import com.cqfc.xmlparser.transactionmsg748.Body;
import com.cqfc.xmlparser.transactionmsg748.Headtype;
import com.cqfc.xmlparser.transactionmsg748.Msg;
import com.cqfc.xmlparser.transactionmsg748.Querytype;
import com.jami.util.Log;

public class TransactionMsgProcessor148 {

	private static String returnCode = "748";

	/**
	 * 接口为渠道期销售结果的查询接口
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理148消息");
		
		TransResponse response = new TransResponse();
		String xml148 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg148.Msg msg148 = TransactionMsgLoader148
				.xmlToMsg(xml148);

		com.cqfc.xmlparser.transactionmsg148.Body body148 = msg148.getBody();
		com.cqfc.xmlparser.transactionmsg148.Headtype head148 = msg148
				.getHead();

		String headPartnerId = head148.getPartnerid();
		String version = head148.getVersion();

		com.cqfc.xmlparser.transactionmsg148.Querytype querytype148 = body148
				.getPartnerIssueQuery();

		String lotteryId = querytype148.getGameid();
		String issueNo = querytype148.getIssueno();
		String partnerId = querytype148.getPartnerid();

		if(partnerId!=null && !"".equals(partnerId)){
			
				if(!partnerId.equals(headPartnerId)){
					
					response.setStatusCode(ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
					response.setData("合作商id不存在或者参数与xml文件中的不一致");
					Log.fucaibiz.info("合作商id不存在或者参数与xml文件中的不一致");
					return response;
				}
			
		}else{
			
			partnerId = headPartnerId;
		}
		
		Msg msg748 = new Msg();

		com.cqfc.xmlparser.transactionmsg748.Body body748 = new Body();
		com.cqfc.xmlparser.transactionmsg748.Querytype querytype748 = new Querytype();
		com.cqfc.xmlparser.transactionmsg748.Headtype head748 = new Headtype();

		head748.setPartnerid(partnerId);
		head748.setTime(DateUtil.getCurrentDateTime());
		head748.setVersion(version);
		head748.setTranscode(returnCode);
		Log.fucaibiz.info("调用partnerOrder中getIssueSaleAndRewardByGroup方法，参数partnerId="+partnerId+",lotteryId="+lotteryId+",issueNo="+issueNo);
		
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("partnerOrder", "getIssueSaleAndRewardByGroup",
						partnerId,lotteryId,issueNo);

		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			List<LotteryIssueSale> issueSaleCounts = (List<LotteryIssueSale>) reMsg
					.getObj();

			if (issueSaleCounts.size() > 0) {

				LotteryIssueSale issueSaleAndReward = issueSaleCounts.get(0);

				querytype748.setGameId(issueSaleAndReward.getLotteryId());
				querytype748.setIssueNo(issueSaleAndReward.getIssueNo());
				querytype748.setPartnerId(issueSaleAndReward.getPartnerId());
				querytype748.setStatType("0");
				querytype748.setTotalSucSale(MoneyUtil
						.toYuanStr(issueSaleAndReward.getSucMoney()));
				querytype748.setTotalSucStakes(String.valueOf(issueSaleAndReward
						.getSucNum()));
				querytype748.setTotalFailSale(MoneyUtil
						.toYuanStr(issueSaleAndReward.getFailMoney()));
				querytype748.setTotalFailStakes(String.valueOf(issueSaleAndReward
						.getFailNum()));
				querytype748.setTotalSale(MoneyUtil
						.toYuanStr(issueSaleAndReward.getSucMoney()+issueSaleAndReward.getFailMoney()));
				querytype748.setTotalBigBonus(MoneyUtil
						.toYuanStr(issueSaleAndReward.getBigPrizeMoney()));
				querytype748.setBigPrizeStakes(String
						.valueOf(issueSaleAndReward.getBigPrizeNum()));
				querytype748.setTotalSmallBonus(MoneyUtil
						.toYuanStr(issueSaleAndReward.getSmallPrizeMoney()));
				querytype748.setSmallPrizeStakes(String.valueOf(issueSaleAndReward
						.getSmallPrizeNum()));
				querytype748.setProxySale("0");
				querytype748.setTotalBonus(MoneyUtil
						.toYuanStr(issueSaleAndReward.getBigPrizeMoney()
								+ issueSaleAndReward.getSmallPrizeMoney()));
				try {
					querytype748.setCreateTime(DateUtil.formatStringFour(issueSaleAndReward.getCreateTime()));
				} catch (ParseException e) {
					Log.fucaibiz.error("时间格式不正确");
				}

			} else {

				response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
				response.setData("未查询到数据");
				Log.fucaibiz.info("未查询到数据");
				return response;

			}

			body748.setPartnersalestat(querytype748);
			msg748.setBody(body748);
			msg748.setHead(head748);

			response.setData(TransactionMsgLoader748.msgToXml(msg748));
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			response.setResponseTransCode(returnCode);

		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.info("错误消息体:"+reMsg.getMsg());
			return response;
		}
		
		Log.fucaibiz.info("748返回消息生成成功 ");
		
		return response;
	}
}
