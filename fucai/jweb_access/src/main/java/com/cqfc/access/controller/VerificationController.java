package com.cqfc.access.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.thrift.TException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.OrderMsg;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.businesscontroller.VoteTicket;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.ReturnData;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.Pair;
import com.cqfc.util.SchemaValidator;
import com.cqfc.xmlparser.transactionmsg104.Ticket;
import com.cqfc.xmlparser.transactionmsg104.Ticketordertype;
import com.cqfc.xmlparser.util.ErrorMsgXmlHelper;
import com.jami.util.Log;

@Controller
@RequestMapping("/verification")
public class VerificationController {
	private static Map<String, LotteryPartner> partnerMap = null;
	private static final AtomicBoolean isInitialization = new AtomicBoolean(
			false);
	static {
		initPartners();
	}

	/**
	 * 鉴权 验证 分发
	 * 
	 */
	@RequestMapping(value = "/verify", produces = { "text/xml;charset=UTF-8" })
	@ResponseBody
	public String verify(String transcode, String msg, String key,
			String partnerid) {
		Log.run.info("access message,transcode=%s,partnerid=%s,msg=%s",
				transcode, partnerid, msg);
		Boolean flag = false;
		String retMsg = "";
		String retTranscode = "000";
		try {
			String msgHeadEnd = msg.substring(0,
					msg.indexOf(ConstantsUtil.SEPARATOR_MSG_BODY));
			String transcodeSingleQuote = "transcode='" + transcode + "'";
			String transcodeDoubleQuote = "transcode=\"" + transcode + "\"";
			String partneridSingleQuote = "partnerid='" + partnerid + "'";
			String partneridDoubleQuote = "partnerid=\"" + partnerid + "\"";

			LotteryPartner partner = null;
			if (!(msgHeadEnd.contains(transcodeSingleQuote) || msgHeadEnd
					.contains(transcodeDoubleQuote))) {
				Log.run.error("transcode不一致. transcode: %s, msg: %s",
						transcode, msg);
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "XML格式化错误",
						ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
			}
			if (msgHeadEnd.contains(partneridSingleQuote)
					|| msgHeadEnd.contains(partneridDoubleQuote)) {
				Pair<LotteryPartner, String> checkPartnerResult = checkPartner(partnerid, msg);
				if (checkPartnerResult.second() != null) {
					retMsg = checkPartnerResult.second();
				}
				partner = checkPartnerResult.first();
			} else {
				Log.run.error(
						"传入partnerid和消息头中partnerid不一致 .partnerid: %s, msg: %s",
						partnerid, msg);
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid,
						"合作商id不存在或者参数与xml文件中的不一致",
						ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
			}

			flag = verifySigRight(transcode + msg, key, partner);

			if (flag) {
				// 验证msg是否符合xsd格式
				flag = SchemaValidator.validate(
						ClassPathUtil.getClassPathInputStream("transdef"
								+ transcode + ".xsd"), msg);
				if (!flag) {
					Log.run.error("Xml format error. msg: %s", msg);
					retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "XML格式化错误",
							ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
				}
			} else {
				Log.run.error("Verify sign right failed. msg: %s, key: %s", msg,
						key);
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "加密信息是不一致的",
						ConstantsUtil.STATUS_CODE_SIG_INCONSISTENT);
			}
			
			if (!retMsg.equals("")) {
				Log.run.debug("jweb_access retMsg: %s", retMsg + "	" + Digit.sig((retTranscode+retMsg).getBytes("utf-8"),
						partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey())));
				return retMsg + "\t" + Digit.sig((retTranscode+retMsg).getBytes("utf-8"),
						partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey()));
			}

			// 构造message
			Message message = new Message();
			message.setPartnerId(partnerid);
			message.setTransCode(Integer.valueOf(transcode));
			message.setTransMsg(msg);

			Log.run.debug("Dispatch the message.");
			TransResponse response = dispatchMessage(message);
			
			if (response.getStatusCode().equals(ConstantsUtil.STATUS_CODE_SUCCESS)) {
				retMsg = response.getData();
				retTranscode = response.getResponseTransCode();				
			} else {
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(message.getPartnerId(),
						response.getData(), response.getStatusCode());				
			}
			Log.run.debug("jweb_access retMsg: %s", retMsg + "	" + Digit.sig((retTranscode+retMsg).getBytes("utf-8"),
					partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey())));
			return retMsg + "\t" + Digit.sig((retTranscode+retMsg).getBytes("utf-8"),
					partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey()));
		} catch (Exception e) {
			Log.run.error(
					"jweb_access verify exception, msg: %s, exeception: %s",
					msg, e);
		}
		return "";
	}

	/**
	 * 鉴权 验证 分发
	 * 
	 */
	@RequestMapping(value = "/verify2", produces = { "text/xml;charset=UTF-8" })
	@ResponseBody
	public String verify2(String transcode, String msg, String key,
			String partnerid) {
		Log.run.info("access message,transcode=%s,partnerid=%s,msg=%s",
				transcode, partnerid, msg);
		Boolean flag = false;
		String retMsg = "";
		String retTranscode = "000";
		try {
			String msgHeadEnd = msg.substring(0,
					msg.indexOf(ConstantsUtil.SEPARATOR_MSG_BODY));
			String transcodeSingleQuote = "transcode='" + transcode + "'";
			String transcodeDoubleQuote = "transcode=\"" + transcode + "\"";
			String partneridSingleQuote = "partnerid='" + partnerid + "'";
			String partneridDoubleQuote = "partnerid=\"" + partnerid + "\"";

			LotteryPartner partner = null;
			if (!(msgHeadEnd.contains(transcodeSingleQuote) || msgHeadEnd
					.contains(transcodeDoubleQuote))) {
				Log.run.error("transcode不一致. transcode: %s, msg: %s",
						transcode, msg);
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "XML格式化错误",
						ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
			}
			if (msgHeadEnd.contains(partneridSingleQuote)
					|| msgHeadEnd.contains(partneridDoubleQuote)) {
				Pair<LotteryPartner, String> checkPartnerResult = checkPartner(partnerid, msg);
				if (checkPartnerResult.second() != null) {
					retMsg = checkPartnerResult.second();
				}
				partner = checkPartnerResult.first();
			} else {
				Log.run.error(
						"传入partnerid和消息头中partnerid不一致 .partnerid: %s, msg: %s",
						partnerid, msg);
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid,
						"合作商id不存在或者参数与xml文件中的不一致",
						ConstantsUtil.STATUS_CODE_PARTNERID_OR_XML_ERROR);
			}

			flag = verifySigRight(transcode + msg, key, partner);

			if (flag) {
				// 验证msg是否符合xsd格式
				flag = SchemaValidator.validate(
						ClassPathUtil.getClassPathInputStream("transdef"
								+ transcode + ".xsd"), msg);
				if (!flag) {
					Log.run.error("Xml format error. msg: %s", msg);
					retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "XML格式化错误",
							ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
				}
			} else {
				Log.run.error("Verify sign right failed.msg %s, key %s", msg,
						key);
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "加密信息是不一致的",
						ConstantsUtil.STATUS_CODE_SIG_INCONSISTENT);
			}
			
			if (!retMsg.equals("")) {
				Log.run.debug("jweb_access retMsg: %s", retMsg + "	" + Digit.sig((retTranscode+retMsg).getBytes("utf-8"),
						partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey())));
				return retMsg;
			}

			// 构造message
			Message message = new Message();
			message.setPartnerId(partnerid);
			message.setTransCode(Integer.valueOf(transcode));
			message.setTransMsg(msg);

			Log.run.debug("Dispatch the message.");
			TransResponse response = dispatchMessage(message);
			
			if (response.getStatusCode().equals(ConstantsUtil.STATUS_CODE_SUCCESS)) {
				retMsg = response.getData();
				retTranscode = response.getResponseTransCode();				
			} else {
				retMsg = ErrorMsgXmlHelper.getErrorMsgXml(message.getPartnerId(),
						response.getData(), response.getStatusCode());				
			}
			Log.run.debug("jweb_access retMsg: %s", retMsg + "	" + Digit.sig((retTranscode+retMsg).getBytes("utf-8"),
					partner.getKeyStore(), partner.getAliasKey(), ClassPathUtil.getClassPathInputStream(partner.getSecretKey())));
			return retMsg;
		} catch (Exception e) {
			Log.run.error(
					"jweb_access verify exception, msg: %s, exeception: %s",
					msg, e);
		}
		return "";
	}
	
	
	/**
	 * 校验合作商id是否存在
	 * 
	 * @param partnerid
	 * @param msg
	 * @return
	 */
	private Pair<LotteryPartner, String> checkPartner(String partnerid, String msg) {
		Pair<LotteryPartner, String> result = new Pair<LotteryPartner, String>();
		if (partnerMap == null) {
			if (initPartners()){
				if (partnerMap == null || !partnerMap.containsKey(partnerid)){
					Log.run.error("get all partner, but partnerid不存在 ,partnerid: %s, msg: %s",
							partnerid, msg);
					result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
					return result;
				}
				result.first(partnerMap.get(partnerid));
				return result;
			} else {
				ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
						"partner", "findLotteryPartnerById", partnerid);

				if (retMsg.getObj() == null) {
					Log.run.error("get one partner when map is null, partnerid不存在 .partnerid: %s, msg: %s",
							partnerid, msg);
					result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
					return result;
				} else {
					LotteryPartner partner = (LotteryPartner) retMsg.getObj();
					partnerMap.put(partner.getPartnerId(), partner);
					Log.run.info(
							"find lottery partner success, partnerid: %s, msg: %s",
							partnerid, msg);
					result.first(partner);
					return result;
				}
			}
		} else {
			if (partnerMap.containsKey(partnerid)) {
				result.first(partnerMap.get(partnerid));
				return result;
			}
			ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
					"partner", "findLotteryPartnerById", partnerid);

			if (retMsg.getObj() == null) {
				Log.run.error("get one partner when map not contain it, partnerid不存在 .partnerid: %s, msg: %s",
						partnerid, msg);
				result.second(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
						ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
				return result;
			} else {
				LotteryPartner partner = (LotteryPartner) retMsg.getObj();
				partnerMap.put(partner.getPartnerId(), partner);
				Log.run.info(
						"find lottery partner success, partnerid: %s, msg: %s",
						partnerid, msg);
				result.first(partner);
				return result;
			}

		}
	}

	/**
	 * 初始获取合作商列表
	 */
	private static boolean initPartners() {
		// 该方法基于目前partner数量不会很多的情况下直接去partner模块将所有partner(10000以内)一次性取过来，假如后续partner较多，这里需要分多次循环去取。
		if (isInitialization.compareAndSet(false, true)) {
			try {
				List<LotteryPartner> partners = new ArrayList<LotteryPartner>();
				Map<String, LotteryPartner> map = new ConcurrentHashMap<String, LotteryPartner>();
				ReturnMessage reMsg = TransactionProcessor.dynamicInvoke(
						"partner", "getLotteryPartnerList",
						new LotteryPartner(), 1, 10000);
				if (reMsg.getObj() != null) {
					ReturnData re = (ReturnData) reMsg.getObj();
					partners = re.getResultList();
					for (LotteryPartner partner : partners) {
						map.put(partner.getPartnerId(), partner);
					}
					partnerMap = map;
				}
				return true;
			} catch (Exception e) {
				Log.run.error("get partner failed.errMsg=%s", e.toString());
			} finally {
				isInitialization.set(false);
			}
		}
		return false;
	}

	/**
	 * 校验签名是否一致
	 * 
	 * @param txt
	 * @param key
	 * @return
	 */
	@SuppressWarnings("restriction")
	private Boolean verifySigRight(String txt, String key, LotteryPartner partner) {
		Boolean isRight = false;
		try {
			InputStream in = ClassPathUtil
					.getClassPathInputStream(partner.getPublicKey());
			byte[] sigedText = null;
			sun.misc.BASE64Decoder base64 = new sun.misc.BASE64Decoder();
			sigedText = base64.decodeBuffer(key);
			isRight = Digit.veriSig(txt.getBytes("utf-8"), sigedText, in);
		} catch (IOException e) {
			Log.run.error("jweb_acces verifySigRight exception: %s", e);
		}
		return isRight;
	}

	/**
	 * 寻找节点，进行分发
	 * 
	 * @param message
	 */
	private TransResponse dispatchMessage(Message message) {
		TransResponse response = null;
		try {
			response = doProcess(message);
			if (response.getStatusCode().equals(
					ConstantsUtil.STATUS_CODE_SUCCESS)) {
				InputStream in = ClassPathUtil
						.getClassPathInputStream("transdef"
								+ response.getResponseTransCode() + ".xsd");
				if (!SchemaValidator.validate(in, response.getData())) {
					Log.run.error("The format of returnXml is error.msg=%s", response.getData());
					response.setData("系统错误");
					response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_ERROR);
				}
			}
		} catch (Exception e) {
			Log.run.debug("error=", e);
			response = new TransResponse();
			response.setData("系统错误");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_ERROR);
			Log.run.error("Call service error.errMsg=%s", e.toString());
		}

		return response;
	}

	private TransResponse checkValid(
			com.cqfc.xmlparser.transactionmsg104.Msg msg104) {
		TransResponse processMsg = null;
		Ticketordertype ticketorder = msg104.getBody().getTicketorder();
		List<Ticket> ticketList = ticketorder.getTickets().getTicket();

		if (ticketList == null || ticketList.isEmpty()) {
			processMsg = new TransResponse();
			processMsg.setData("xml格式化错误");
			processMsg
					.setStatusCode(ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
			Log.run.debug("checkValid: xml格式错误，获取不到ticket");
			return processMsg;
		}

		String numStr = ticketorder.getTicketsnum();
		try {
			int ticketsNum = Integer.parseInt(numStr);
			if (ticketsNum != ticketList.size()) {
				processMsg = new TransResponse();
				processMsg.setData("不一致的票数与钱数");
				processMsg
						.setStatusCode(ConstantsUtil.STATUS_CODE_TICKETANDAMOUNT_INCONSISTENT);
				Log.run.debug(
						"checkValid: xml不合法，ticketsnum与订单数不一致，ticketsnum=%s, ticketsSize=%d",
						numStr, ticketList.size());
				return processMsg;
			}
			if (ticketsNum > ConstantsUtil.MAX_TICKET_PER_REQUEST) {
				processMsg = new TransResponse();
				processMsg.setData("超过最大注数");
				processMsg
						.setStatusCode(ConstantsUtil.STATUS_CODE_OVER_LARGESTNUM);
				Log.run.debug("checkValid: xml不合法，ticketsnum太大，ticketsnum=%s",
						numStr);
				return processMsg;
			}
		} catch (NumberFormatException e) {
			processMsg = new TransResponse();
			processMsg.setData("非法数字");
			processMsg.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			Log.run.debug("checkValid: xml格式错误，ticketsnum非整数，ticketsnum=%s",
					numStr);
			return processMsg;
		}
		String totalmoneyStr = ticketorder.getTotalmoney();
		try {
			float totalmoneyF = Float.parseFloat(totalmoneyStr);
			long totalmoneyI = (long) totalmoneyF * 100;
			long moneySum = 0;
			for (com.cqfc.xmlparser.transactionmsg104.Ticket ticket : ticketList) {
				String ticketId = ticket.getId();
				if (ticketId != null && ticketId.length() < 56
						&& ticketId.length() > 0) {
					float moneyF = Float.parseFloat(ticket.getMoney());
					long moneyI = (long) moneyF * 100;
					if (moneyI > ConstantsUtil.MAX_TOTAL_MONEY) {
						processMsg = new TransResponse();
						processMsg.setData("票总金额错误");
						processMsg
								.setStatusCode(ConstantsUtil.STATUS_CODE_TOTALAMOUNT_ERROR);
						Log.run.debug(
								"checkValid: 投注内容有问题，单个方案投注金额不得大于10万元。 money=%d",
								moneyI);
						return processMsg;
					}
					moneySum += moneyI;
				} else {
					processMsg = new TransResponse();
					processMsg.setData("XML格式化错误");
					processMsg
							.setStatusCode(ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
					Log.run.debug(
							"checkValid: 投注内容有问题，ticketId长度不正确，ticketId=%s",
							ticketId);
					return processMsg;
				}
			}
			if (totalmoneyI != moneySum) {
				processMsg = new TransResponse();
				processMsg.setData("不一致的票数与钱数");
				processMsg
						.setStatusCode(ConstantsUtil.STATUS_CODE_TICKETANDAMOUNT_INCONSISTENT);
				Log.run.debug(
						"checkValid: 投注内容有问题，totalmoney不等于各订单金额之和，totalmoney=%s, moneySum=%d",
						totalmoneyStr, moneySum);
				return processMsg;
			}
		} catch (NumberFormatException e) {
			processMsg = new TransResponse();
			processMsg.setData("非法数字");
			processMsg.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			Log.run.debug("checkValid: xml格式错误，ticketsnum非整数，ticketsnum=%s",
					numStr);
			return processMsg;
		}
		return processMsg;
	}

	private TransResponse doProcess(Message message) throws TException {
		TransResponse processMsg = null;
		try {
			String xmlstr = message.getTransMsg();
			com.cqfc.xmlparser.transactionmsg704.Msg msgXml = null;
			switch (message.getTransCode()) {
			case 104:
				// 请求消息对象
				com.cqfc.xmlparser.transactionmsg104.Msg msg104 = com.cqfc.xmlparser.TransactionMsgLoader104
						.xmlToMsg(xmlstr);
				com.cqfc.xmlparser.transactionmsg104.Tickets tickets = msg104
						.getBody().getTicketorder().getTickets();
				processMsg = checkValid(msg104);
				if (processMsg != null) {
					return processMsg;
				}
				OrderMsg orderMsg = getOrderMsg(msg104);
				for (com.cqfc.xmlparser.transactionmsg104.Ticket ticket : tickets
						.getTicket()) {
					Log.run.debug("104 betting order,ticketId=%s",
							ticket.getId());

					List<VoteTicket> ticketList = new ArrayList<VoteTicket>();
					VoteTicket voteTicket = new VoteTicket();
					voteTicket.setTicketId(ticket.getId());
					voteTicket
							.setMultiple(Integer.valueOf(ticket.getMultiple()));
					voteTicket.setIssueNo(ticket.getIssue());
					voteTicket.setPlayType(ticket.getPlaytype());
					voteTicket.setMoney(Integer.valueOf(ticket.getMoney()));
					voteTicket.setBall(ticket.getBall());
					ticketList.add(voteTicket);
					orderMsg.setTicketList(ticketList);

					ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
							ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER,
							ConstantsUtil.METHODNAME_CREATEORDERPROCESS,
							orderMsg);
					String statusCode = retMsg.getStatusCode();
					if (!ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
							.equals(statusCode)) {
						Log.run.error(
								"jweb_access104调用business异常,statusCode=%s,msg=%s",
								statusCode, retMsg.getMsg());
					}
					TransResponse tmpProcessMsg = (TransResponse) retMsg
							.getObj();

					com.cqfc.xmlparser.transactionmsg704.Msg msg704 = com.cqfc.xmlparser.TransactionMsgLoader704
							.xmlToMsg(tmpProcessMsg.getData());
					if (processMsg == null) {
						processMsg = tmpProcessMsg;
						msgXml = new com.cqfc.xmlparser.transactionmsg704.Msg();
						msgXml.copy(msg704);
						msgXml.getBody()
								.getTicketorder()
								.setTicketsnum(
										msg104.getBody().getTicketorder()
												.getTicketsnum());
						msgXml.getBody()
								.getTicketorder()
								.setTotalmoney(
										msg104.getBody().getTicketorder()
												.getTotalmoney());
					}
					msgXml.getBody()
							.getTicketorder()
							.getTickets()
							.getTicket()
							.addAll(msg704.getBody().getTicketorder()
									.getTickets().getTicket());
				}
				processMsg.setData(com.cqfc.xmlparser.TransactionMsgLoader704
						.msgToXml(msgXml));
				break;
			case 105:
				com.cqfc.xmlparser.transactionmsg105.Msg msg105 = com.cqfc.xmlparser.TransactionMsgLoader105
						.xmlToMsg(xmlstr);
				processMsg = processMsg(message, msg105.getBody()
						.getQueryticket().getId(),
						ConstantsUtil.METHODNAME_FINDORDERPROCESS,
						ConstantsUtil.DEVICE_TYPE_QUERY_TICKET_BUSINESS);
				break;
			case 110:
				com.cqfc.xmlparser.transactionmsg110.Msg msg110 = com.cqfc.xmlparser.TransactionMsgLoader110
						.xmlToMsg(xmlstr);
				processMsg = processMsg(message, msg110.getBody()
						.getFloworder().getId(),
						ConstantsUtil.METHODNAME_PROCESSMESSAGE,
						ConstantsUtil.DEVICE_TYPE_OTHER_BUSINESS);
				break;
			case 121:
				com.cqfc.xmlparser.transactionmsg121.Msg msg121 = com.cqfc.xmlparser.TransactionMsgLoader121
						.xmlToMsg(xmlstr);
				processMsg = processMsg(message, msg121.getBody()
						.getFloworder().getId(),
						ConstantsUtil.METHODNAME_PROCESSMESSAGE,
						ConstantsUtil.DEVICE_TYPE_OTHER_BUSINESS);
				break;
			case 205:
				com.cqfc.xmlparser.transactionmsg205.Msg msg205 = com.cqfc.xmlparser.TransactionMsgLoader205
						.xmlToMsg(xmlstr);
				processMsg = processMsg(message, msg205.getBody()
						.getTicketorder().getTickets().getTicket().get(0)
						.getId(),
						ConstantsUtil.METHODNAME_BATCHFINDORDERPROCESS,
						ConstantsUtil.DEVICE_TYPE_QUERY_TICKET_BUSINESS);
				break;
			default:
				processMsg = processMsg(message,
						"" + System.currentTimeMillis(),
						ConstantsUtil.METHODNAME_PROCESSMESSAGE,
						ConstantsUtil.DEVICE_TYPE_OTHER_BUSINESS);
			}
		} catch (Exception e) {
			Log.run.error("doProcess发生异常", e);
		}
		return processMsg;
	}

	/**
	 * 
	 * @param message
	 *            传入的msg
	 * @param key
	 *            用来路由的关键字(ticketId或随机数) (2014.11.19增加zookeeper时废弃该参数)
	 * @param method
	 *            要调用的方法
	 * @param moduleType
	 *            要调用的business类型(区分104 105 和其他) (2014.11.19增加zookeeper时废弃该参数)
	 * @return
	 * @throws TException
	 */
	private TransResponse processMsg(Message message, Object key,
			String method, int moduleType) throws TException {
		TransResponse response = null;
		ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER, method, message);
		String statusCode = retMsg.getStatusCode();
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {
			response = (TransResponse) retMsg.getObj();
		} else {
			response = new TransResponse();
			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			Log.run.error("jweb_access调用business异常,statusCode=%s,msg=%s",
					statusCode, retMsg.getMsg());
			return response;
		}
		return response;
	}

	private OrderMsg getOrderMsg(com.cqfc.xmlparser.transactionmsg104.Msg msg104) {
		OrderMsg orderMsg = null;
		try {
			com.cqfc.xmlparser.transactionmsg104.Headtype head = msg104
					.getHead();
			com.cqfc.xmlparser.transactionmsg104.Body body = msg104.getBody();
			com.cqfc.xmlparser.transactionmsg104.Ticketordertype ticketOrder = body
					.getTicketorder();
			com.cqfc.xmlparser.transactionmsg104.User user = ticketOrder
					.getUser();

			orderMsg = new OrderMsg();

			orderMsg.setPartnerId(head.getPartnerid());
			orderMsg.setVersion(head.getVersion());
			orderMsg.setTime(head.getTime());

			orderMsg.setLotteryId(ticketOrder.getGameid());
			orderMsg.setTicketsNum(Integer.valueOf(ticketOrder.getTicketsnum()));
			orderMsg.setTotalMoney(Integer.valueOf(ticketOrder.getTotalmoney()));
			orderMsg.setProvince(ticketOrder.getProvince());
			orderMsg.setMachine(ticketOrder.getMachine());

			long userId = null != user.getUserid()
					&& !"".equals(user.getUserid()) ? Long.valueOf(
					user.getUserid()).longValue() : 0;
			orderMsg.setUserId(userId);
			orderMsg.setRealName(user.getRealname());
			orderMsg.setIdCard(user.getIdcard());
			orderMsg.setPhone(user.getPhone());
			
			orderMsg.setPlanId(ticketOrder.getBatchid());
			orderMsg.setWareId(ticketOrder.getWareid());

			return orderMsg;
		} catch (Exception e) {
			Log.run.error("104投注拆单时组装对象发生异常", e);
			return null;
		}
	}
	
	
	public static void updatePartnerMap(LotteryPartner lotteryPartner){
		if(lotteryPartner != null && partnerMap != null){
			partnerMap.put(lotteryPartner.getPartnerId(), lotteryPartner);
			Log.run.debug("receivePartnerMQ, partnerId: %s, secretKey: %s, publicKey: %s, alias: %s, keyStore: %s",  lotteryPartner.getPartnerId(), lotteryPartner.getSecretKey(), lotteryPartner.getPublicKey(), lotteryPartner.getAliasKey(), lotteryPartner.getKeyStore());
		}
	}
}
