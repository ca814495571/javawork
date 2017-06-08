package com.cqfc.access.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.apache.thrift.TException;

import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.util.ClassPathUtil;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.Digit;
import com.cqfc.util.SchemaValidator;
import com.cqfc.xmlparser.util.ErrorMsgXmlHelper;
import com.jami.util.Log;


@WebServlet(value = "/verification/verify", asyncSupported = true)
public class VerificationController extends HttpServlet {

	static {
		MDC.put("ip", "192.168.1.137");
		MDC.put("component", "japp_access");
	}

	/**
	 * 鉴权 验证 分发
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	final String transcode = req.getParameter("transcode");
    	final String msg = req.getParameter("msg");
    	final String key = req.getParameter("key");
    	final String partnerid = req.getParameter("partnerid");

		Log.fucaibiz.info(
				"access message,transcode=%s,partnerid=%s,msg=%s",
				transcode, partnerid, msg);
		Boolean flag = false;
		flag = verifySigRight(transcode + msg, key);
		if (flag) {
			// 验证msg是否符合xsd格式
			flag = SchemaValidator.validate(ClassPathUtil.getClassPathInputStream("transdef"
				+ transcode + ".xsd"), msg);
		} else {
			Log.run.error("Verify sign right failed.");
			res.getWriter().write(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "加密信息是不一致的",
					ConstantsUtil.STATUS_CODE_SIG_INCONSISTENT));
		}

		if (!flag) {
			Log.run.error("Xml format error.");
			res.getWriter().write(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "XML格式化错误",
					ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT));
		}
		String partneridSingleQuote = "partnerid='"+partnerid+"'";
		String partneridDoubleQuote = "partnerid=\""+partnerid+"\"";
		if (msg.contains(partneridSingleQuote) || msg.contains(partneridDoubleQuote)) {
			ReturnMessage retMsg = TransactionProcessor.dynamicInvoke("partner", "verifyPartnerIsExist", partnerid);
			if(retMsg.getStatusCode().equals(ConstantsUtil.STATUS_CODE_RETURN_SUCCESS)){				
				if(!(Boolean) retMsg.getObj()){
					Log.run.error("partnerid不存在 .");
					res.getWriter().write(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "不存在的合作商",
							ConstantsUtil.STATUS_CODE_PARTNER_NOTEXIST));
				}
			}
			else{
				Log.run.info("VerificationController(verify): %s", retMsg.getMsg());
				res.getWriter().write(ErrorMsgXmlHelper.getErrorMsgXml(partnerid,
						retMsg.getMsg(), retMsg.getStatusCode()));
			}
		} else {
			Log.run.error("传入partnerid和消息头中partnerid不一致 .");
			res.getWriter().write(ErrorMsgXmlHelper.getErrorMsgXml(partnerid, "参数与xml文件中的不一致",
					ConstantsUtil.STATUS_CODE_PARTNER_ID_INEXISTENCE_OR_PARM_INCONSISTENT));
		}
		
		int code = Integer.valueOf(transcode);
		if (code==105) {
		      res.setContentType("text/html;charset=UTF-8");
		      final AsyncContext asyncContext = req.startAsync();
		      asyncContext.start(new Runnable() {
		          public void run() {
		        	  System.out.println("async task started on " + new Date() + "\r\n");
		        	  
		        	  ServletResponse response = asyncContext.getResponse();
		        	  try {
		        		  Message message105 = new Message();
		        		  message105.setPartnerId(partnerid);
		        		  message105.setTransCode(Integer.valueOf(transcode));
		        		  message105.setTransMsg(msg);
		        		  PrintWriter writer = response.getWriter();
		        		  writer.write(dispatchMessage(message105));
		        	  } catch (IOException e) {
		        		  e.printStackTrace();
		        	  }
		        	  asyncContext.complete();
		        	  System.out.println("async task completed on " + new Date() + "\r\n");
		          }
		      });
		} else if (code==110){
		      res.setContentType("text/html;charset=UTF-8");
		      final AsyncContext asyncContext = req.startAsync();
		      asyncContext.start(new Runnable() {
		          public void run() {
		        	  System.out.println("async task started on " + new Date() + "\r\n");
		        	  
		        	  ServletResponse response = asyncContext.getResponse();
		        	  try {
		        		  Message message110 = new Message();
		        		  message110.setPartnerId(partnerid);
		        		  message110.setTransCode(Integer.valueOf(transcode));
		        		  message110.setTransMsg(msg);
		        		  PrintWriter writer = response.getWriter();
		        		  writer.write(dispatchMessage(message110));
		        	  } catch (IOException e) {
		        		  e.printStackTrace();
		        	  }
		        	  asyncContext.complete();
		        	  System.out.println("async task completed on " + new Date() + "\r\n");
		          }
		      });
		} else {
			// 构造message
			Message message = new Message();
			message.setPartnerId(partnerid);
			message.setTransCode(Integer.valueOf(transcode));
			message.setTransMsg(msg);
			Log.run.debug("Dispatch the message.");
			res.getWriter().write(dispatchMessage(message));
		}
		System.out.println("doPost() completed on " + new Date() + "\r\n");
	}

	/**
	 * 校验签名是否一致
	 * 
	 * @param txt
	 * @param key
	 * @return
	 */
	@SuppressWarnings("restriction")
	private Boolean verifySigRight(String txt, String key) {
		Boolean isRight = false;
		InputStream in = ClassPathUtil.getClassPathInputStream(ConstantsUtil.PUBLIC_SCRECT_NAME);
		byte[] sigedText = null;
		sun.misc.BASE64Decoder base64 = new sun.misc.BASE64Decoder();

		try {
			sigedText = base64.decodeBuffer(key);
			isRight = Digit.veriSig(txt.getBytes("utf-8"), sigedText, in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isRight;
	}

	/**
	 * 寻找节点，进行分发
	 * 
	 * @param message
	 */
	private String dispatchMessage(Message message) {
		TransResponse response = null;
		try {
			response = doProcess(message);
			if (response.getStatusCode().equals(
					ConstantsUtil.STATUS_CODE_SUCCESS)) {
				InputStream in = ClassPathUtil.getClassPathInputStream("transdef" + response.getResponseTransCode() + ".xsd");
				if (SchemaValidator.validate(in, response.getData())) {
					Log.run.debug("Process message succeeded. response : "
							+ response.getData());
					return response.getData();
				} else {
					Log.run.error("The format of returnXml is error.");
					response.setData("生成的xml文件用对应xsd校验不合法");
					response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_ERROR);
				}
			}
		} catch (Exception e) {
			Log.run.debug("error=", e);
			response = new TransResponse();
			response.setData("调用接口发生异常");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_ERROR);
			Log.run.error("Call service error.errMsg=%s", e.toString());
		} 
		return ErrorMsgXmlHelper.getErrorMsgXml(message.getPartnerId(),
				response.getData(), response.getStatusCode());
	}

	private TransResponse doProcess(Message message) throws TException {
		String xmlstr = message.getTransMsg();
		TransResponse processMsg = null;
		com.cqfc.xmlparser.transactionmsg704.Msg msgXml = null;
		switch (message.getTransCode()) {
		case 104:
			// 请求消息对象
			com.cqfc.xmlparser.transactionmsg104.Msg msg104 = com.cqfc.xmlparser.TransactionMsgLoader104
					.xmlToMsg(xmlstr);
			com.cqfc.xmlparser.transactionmsg104.Tickets tickets = msg104
					.getBody().getTicketorder().getTickets();
			if (tickets.getTicket() == null) {
				processMsg = new TransResponse();
				processMsg.setData("xml格式错误，获取不到ticket");
				processMsg
						.setStatusCode(ConstantsUtil.STATUS_CODE_SCHEMA_VALIDATE_INCORRECT);
				Log.run.debug("doProcess: xml格式错误，获取不到ticket");
				return processMsg;
			}
			for (com.cqfc.xmlparser.transactionmsg104.Ticket ticket : tickets
					.getTicket()) {
				Log.run.debug("ticketId is %s", ticket.getId());
				com.cqfc.xmlparser.transactionmsg104.Msg tmpMsg = new com.cqfc.xmlparser.transactionmsg104.Msg();
				tmpMsg.copy(msg104);
				tmpMsg.getBody().getTicketorder().setTicketsnum("1");
				tmpMsg.getBody().getTicketorder()
						.setTotalmoney(ticket.getMoney());
				tmpMsg.getBody().getTicketorder().getTickets().getTicket()
						.add(ticket);
				Message tmpMessage = new Message(message);
				tmpMessage.setTransMsg(tmpMsg.toString());
				TransResponse tmpProcessMsg = processMsg(tmpMessage,
						ticket.getId(), ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS);
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
						.addAll(msg704.getBody().getTicketorder().getTickets()
								.getTicket());
			}
			processMsg.setData(com.cqfc.xmlparser.TransactionMsgLoader704
					.msgToXml(msgXml));
			break;
		case 105:
			com.cqfc.xmlparser.transactionmsg105.Msg msg105 = com.cqfc.xmlparser.TransactionMsgLoader105
					.xmlToMsg(xmlstr);
			processMsg = processMsg(message, msg105.getBody()
					.getQueryticket().getId(), ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS);
			break;
		case 110:
			com.cqfc.xmlparser.transactionmsg110.Msg msg110 = com.cqfc.xmlparser.TransactionMsgLoader110
					.xmlToMsg(xmlstr);
			processMsg = processMsg(message, msg110.getBody()
					.getFloworder().getId(), ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS);
			break;
		case 121:
			com.cqfc.xmlparser.transactionmsg121.Msg msg121 = com.cqfc.xmlparser.TransactionMsgLoader121
					.xmlToMsg(xmlstr);
			processMsg = processMsg(message, msg121.getBody()
					.getFloworder().getId(), ConstantsUtil.DEVICE_TYPE_TICKET_BUSINESS);
			break;
		default:
			processMsg = processMsg(message, "" + System.currentTimeMillis(), ConstantsUtil.DEVICE_TYPE_OTHER_BUSINESS);
		}
		return processMsg;
	}
	
	private TransResponse processMsg(
			Message message, Object key, int moduleType) throws TException {
		TransResponse response = null;
		ReturnMessage retMsg = TransactionProcessor.dynamicInvoke(
				ConstantsUtil.MODULENAME_BUSINESS_CONTROLLER,
				ConstantsUtil.METHODNAME_PROCESSMESSAGE,
				key,
				moduleType, message);
		String statusCode = retMsg.getStatusCode();
		Log.fucaibiz.info("返回状态码为:"+statusCode);
		if (ConstantsUtil.STATUS_CODE_RETURN_SUCCESS.equals(statusCode)) {
			response = (TransResponse) retMsg.getObj();
		} else {
			response = new TransResponse();
			response.setStatusCode(String.valueOf(statusCode));
			response.setData(retMsg.getMsg());
			Log.fucaibiz.error("返回状态码为:"+statusCode+",返回消息:"+retMsg.getMsg());
			return response;
		}
		return response;

	}
}
