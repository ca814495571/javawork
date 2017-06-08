package com.cqfc.businesscontroller.task;

import java.text.ParseException;

import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader617;
import com.cqfc.xmlparser.transactionmsg617.Body;
import com.cqfc.xmlparser.transactionmsg617.Headtype;
import com.cqfc.xmlparser.transactionmsg617.Msg;
import com.cqfc.xmlparser.transactionmsg617.Querytype;
import com.cqfc.xmlparser.transactionmsg617.User;
import com.jami.util.Log;

public class TransactionMsgProcessor617 {

	private static String returnCode = "617";

	public static TransResponse process(int applyId) {

		Log.fucaibiz.info("开始处理617");
		TransResponse response = new TransResponse();

		Msg msg617 = new Msg();

		com.cqfc.xmlparser.transactionmsg617.Headtype head617 = new Headtype();
		com.cqfc.xmlparser.transactionmsg617.Body body617 = new Body();

		head617.setTime(DateUtil.getCurrentDateTime());
		head617.setVersion("");

		com.cqfc.xmlparser.transactionmsg617.Querytype querytype617 = new Querytype();
		com.cqfc.xmlparser.transactionmsg617.User user = new User();

		Log.fucaibiz.info("调用userAccount中的findWithdrawApplyByApplyId方法,参数applyId="+applyId);
		com.cqfc.processor.ReturnMessage reMsg = TransactionProcessor
				.dynamicInvoke("userAccount", "findWithdrawApplyByApplyId",
						applyId);

		String statusCode = reMsg.getStatusCode();
		Log.fucaibiz.info("返回的状态码："+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {

			WithdrawApply withdrawApply = (WithdrawApply) reMsg.getObj();

			WithdrawAccount withdrawAccount = withdrawApply
					.getWithdrawAccount();
			querytype617.setBankname(withdrawAccount.getBankName());
			querytype617.setBranch(withdrawAccount.getAccountAddress());
			querytype617.setCardno(withdrawAccount.getAccountNo());
			querytype617.setMoney(MoneyUtil.toYuanStr(withdrawApply
					.getWithdrawAmount()));
			if (withdrawApply.getAuditState() == 1) {

				querytype617.setMsg("审核中");
			}

			if (withdrawApply.getAuditState() == 2) {

				querytype617.setMsg("提现成功");
			}
			if (withdrawApply.getAuditState() == 3) {

				querytype617.setMsg("提现失败");
			}
			querytype617.setStatuscode(statusCode);
			try {
				querytype617.setTradetime(DateUtil.formatStringFour(withdrawApply.getCreateTime()));
			} catch (ParseException e) {
				Log.fucaibiz.error("时间格式不正确");
			}
			user.setUserid(String.valueOf(withdrawApply.getUserId()));
			querytype617.setUser(user);
			querytype617.setWithdrawid(withdrawApply.getSerialNumber());

			body617.setWithdrawresult(querytype617);
			msg617.setBody(body617);
			msg617.setHead(head617);

			response.setData(TransactionMsgLoader617.msgToXml(msg617));
			response.setResponseTransCode(returnCode);
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);

		} else {

			response.setStatusCode(statusCode);
			response.setData(reMsg.getMsg());
			Log.fucaibiz.error("返回的错误消息:"+reMsg.getMsg());
			return response;
		}
		Log.fucaibiz.info("617消息体生成成功");
		return response;

	}
}
