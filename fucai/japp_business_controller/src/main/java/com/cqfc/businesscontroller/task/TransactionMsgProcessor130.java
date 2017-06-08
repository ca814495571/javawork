package com.cqfc.businesscontroller.task;

import java.text.ParseException;
import java.util.List;

import com.cqfc.businesscontroller.util.CheckUser;
import com.cqfc.businesscontroller.util.DataUtil;
import com.cqfc.processor.ReturnMessage;
import com.cqfc.processor.TransactionProcessor;
import com.cqfc.protocol.appendtask.AppendTask;
import com.cqfc.protocol.businesscontroller.Message;
import com.cqfc.protocol.businesscontroller.TransResponse;
import com.cqfc.protocol.userorder.Order;
import com.cqfc.protocol.userorder.PcUserOrder;
import com.cqfc.util.ConstantsUtil;
import com.cqfc.util.DateUtil;
import com.cqfc.util.MoneyUtil;
import com.cqfc.xmlparser.TransactionMsgLoader130;
import com.cqfc.xmlparser.TransactionMsgLoader730;
import com.cqfc.xmlparser.transactionmsg730.Body;
import com.cqfc.xmlparser.transactionmsg730.Bookorder;
import com.cqfc.xmlparser.transactionmsg730.Bookorders;
import com.cqfc.xmlparser.transactionmsg730.Headtype;
import com.cqfc.xmlparser.transactionmsg730.Joinorders;
import com.cqfc.xmlparser.transactionmsg730.Msg;
import com.cqfc.xmlparser.transactionmsg730.Querytype;
import com.cqfc.xmlparser.transactionmsg730.Unionorders;
import com.cqfc.xmlparser.transactionmsg730.Userorders;
import com.cqfc.xmlparser.transactionmsg730.Userordertype;
import com.jami.util.Log;

public class TransactionMsgProcessor130 {


	private static String retrunCode = "730";

	/**
	 * 130接口为彩票交易结果的查询接口，合作商根据用户ID查询用户投注信息
	 * 
	 * @param message
	 * @return
	 */
	public static TransResponse process(Message message) {

		Log.fucaibiz.info("开始处理130消息");
		
		TransResponse response = new TransResponse();
		String xml130 = message.getTransMsg();

		com.cqfc.xmlparser.transactionmsg130.Msg msg130 = TransactionMsgLoader130
				.xmlToMsg(xml130);

		com.cqfc.xmlparser.transactionmsg130.Body body130 = msg130.getBody();
		com.cqfc.xmlparser.transactionmsg130.Headtype head130 = msg130
				.getHead();
		com.cqfc.xmlparser.transactionmsg130.Querytype bodyInfo130 = body130
				.getQueryorder();

		String partnerId = head130.getPartnerid();
		String version = head130.getVersion();

		String from = bodyInfo130.getFrom();
		String gameId = bodyInfo130.getGameid();
		String orderType = bodyInfo130.getOrdertype();
		String size = bodyInfo130.getSize();
		String userId = bodyInfo130.getUserid();
		
		
		if(!DataUtil.isNumeric(from) || !DataUtil.isNumeric(size)){
			
			response.setData("非法数字");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
			Log.fucaibiz.error("非法数字from="+from+",size="+size+",from="+from);
			return response;
		}
		
		int pageNum = Integer.parseInt(from);
		int pageSize = Integer.parseInt(size);
		if(pageNum<0){
			pageNum = 0 ;
		}
		if(pageSize<0||pageSize>50){
			pageSize = 50;
		}
		

		com.cqfc.xmlparser.transactionmsg730.Msg msg730 = new Msg();
		com.cqfc.xmlparser.transactionmsg730.Body body730 = new Body();
		com.cqfc.xmlparser.transactionmsg730.Headtype head730 = new Headtype();
		com.cqfc.xmlparser.transactionmsg730.Querytype bodyInfo730 = new Querytype();

		// 直投
		com.cqfc.xmlparser.transactionmsg730.Userorders userorders = new Userorders();
		//合买
		 com.cqfc.xmlparser.transactionmsg730.Unionorders unionorders = new Unionorders();
		//追号
		com.cqfc.xmlparser.transactionmsg730.Bookorders bookorders = new Bookorders();
		// 参与合买
		com.cqfc.xmlparser.transactionmsg730.Joinorders joinorders = new Joinorders();
		
		List<Bookorder> bookordersList = bookorders.getBookorder();
		com.cqfc.xmlparser.transactionmsg730.Bookorder bookorder = null;

		List<Userordertype> userOrderList = userorders.getUserorder();
		com.cqfc.xmlparser.transactionmsg730.Userordertype userordertype = null;

		Order order = new Order();
		long userIdTemp = 0;
		try {

			userIdTemp = Long.parseLong(userId);
			if(!CheckUser.validateUser(userIdTemp, partnerId)){
				
				response.setStatusCode(ConstantsUtil.STATUS_CODE_USER_NOTEXIST);
				response.setData("用户不存在");
				Log.fucaibiz.error(partnerId+"合作商没有该用户"+userIdTemp);
				return response;
			}
			
			order.setUserId(userIdTemp);
			//文档0 为 直投  文档1 为追号  其他就默认为查询所有类型
			
			if("0".equals(orderType)){
				order.setOrderType(1);
			}else if("1".equals(orderType)){
				order.setOrderType(2);
			}else{
				
				response.setData("参数不正确");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_RETURN_ARGUMENT_ERROR);
				return response;
			}
			order.setLotteryId(gameId);

			Log.fucaibiz.info("调用userOrder中的getUserOrder方法,参数order="+order+",from="+from+",size="+size);
			
			ReturnMessage reMsg = TransactionProcessor.dynamicInvoke(
					"userOrder", "getUserOrder", order, Integer.parseInt(from),
					Integer.parseInt(size));

			String statusCode = reMsg.getStatusCode();

			Log.fucaibiz.info("返回的状态码:"+statusCode);
			
			if (!"".equals(statusCode)
					&& ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
							.equals(statusCode)) {

				PcUserOrder pcUserOrder = (PcUserOrder) reMsg.getObj();

				List<Order> orders = pcUserOrder.getUserOrders();

				head730.setPartnerid(partnerId);
				head730.setTime(DateUtil.getCurrentDateTime());
				head730.setTranscode(retrunCode);
				head730.setVersion(version);

				bodyInfo730.setFrom(from);
				bodyInfo730.setSize(size);
				bodyInfo730
						.setTotal(String.valueOf(pcUserOrder.getTotalSize()));
				bodyInfo730.setOrdertype(orderType);
				bodyInfo730.setUserid(userId);
				bodyInfo730.setBookorders(bookorders);
				bodyInfo730.setUserorders(userorders);
				bodyInfo730.setJoinorders(joinorders);
				bodyInfo730.setUnionorders(unionorders);
				
				if (orders.size() > 0) {

					Order orderTemp = new Order();

					for (int i = 0; i < orders.size(); i++) {
						orderTemp = orders.get(i);
						// 1直投
						if (orderTemp.getOrderType() == 1) {
							Log.fucaibiz.info("直投订单处理:"+orderTemp);
							// 平台Id
							userordertype = new Userordertype();
							userordertype.setUorderid(String.valueOf(orderTemp
									.getOrderId()));
							userordertype
									.setPid(orderTemp.getPaySerialNumber()); // 合作商平台Id
							userordertype.setCreatetime(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss",orderTemp
									.getCreateTime()).toString()); // 创建时间
							userordertype.setIssueno(orderTemp.getIssueNo()); // 其次
							userordertype.setGameid(orderTemp.getLotteryId()); // 彩种
							userordertype.setMoney(MoneyUtil
									.toYuanStr(orderTemp.getTotalAmount())); // 购买金额

							userordertype.setStatus(DataUtil.getTradeStatus(orderTemp
									.getOrderStatus())); // 订单状态0 等待交易 1 交易中
							// 2交易成功 3 交易失败
							userordertype.setPlaytype(orderTemp.getPlayType());// 玩法
							userordertype.setUserid(String.valueOf(orderTemp
									.getUserId())); // 用户Id
							userordertype.setTradetime(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss",orderTemp
									.getCreateTime()).toString());// 交易时间
							// userordertype.setInitprovinceid(value); //初始化分配省份
							// userordertype.setRealprovinceid(value); //最终交易省份
							userordertype.setTicketid(orderTemp.getOrderNo()); // 投注订单ID
							userordertype.setBalls(orderTemp.getOrderContent()); // 所选球
							userordertype.setMultiple(String.valueOf(orderTemp
									.getMultiple())); // 倍数
							userordertype.setStake(String.valueOf(orderTemp.getStakeNum())); // 注数
							userordertype.setPrizemoney(MoneyUtil.toYuanStr(orderTemp
											.getWinPrizeMoney())); // 奖金
							userordertype
									.setPartnerid(orderTemp.getPartnerId()); // 合作商ID
							userordertype
									.setPrizestatus(DataUtil.getPrizeStatus(orderTemp
											.getOrderStatus()));
							// 中将类型 0 默认 1 小奖2大奖
							userordertype.setPrizetype("0");
							//如果中奖则判断大小奖
							if (userordertype.getPrizestatus() != null
									&& "2".equals(userordertype
											.getPrizestatus())) {

								if (orderTemp
										.getWinPrizeMoney()*orderTemp
										.getMultiple() > 1000000) {
									userordertype.setPrizetype("2");
								} else {

									userordertype.setPrizetype("1");
								}

							}
							userOrderList.add(userordertype);
						}
						// 2追号
						if (orderTemp.getOrderType() == 2) {

							Log.fucaibiz.info("追号订单处理:"+orderTemp);
							
							String[] orderNos = orderTemp.getOrderNo().split(
									"#");
							String appendTaskId = orderNos[1] + "#"
									+ orderNos[2];
							
							Log.fucaibiz.info("调用appendTask中的findAppendTaskById方法，参数appendTaskId="+appendTaskId);
							
							ReturnMessage appendMsg = TransactionProcessor
									.dynamicInvoke("appendTask",
											"findAppendTaskById", appendTaskId);

							statusCode = appendMsg.getStatusCode();
							
							Log.fucaibiz.info("返回的状态码："+statusCode);
							
							if (!"".equals(statusCode)
									&& ConstantsUtil.STATUS_CODE_RETURN_SUCCESS
											.equals(statusCode)) {

								AppendTask appendTask = (AppendTask) appendMsg
										.getObj();

								if (appendTask != null) {
									bookorder = new Bookorder();
									bookorder.setBorderid(appendTask
											.getAppendTaskId()); // 追号ID
									bookorder.setUserid(String
											.valueOf(appendTask.getUserId()));// 用户ID
									bookorder.setAnteid("");// 投注内容ID
									bookorder.setMoney(MoneyUtil
											.toYuanStr(appendTask
													.getAppendTotalMoney()));// 购买金额
									bookorder.setStatus(String
											.valueOf(appendTask
													.getAppendStatus()));// 状态
																			// 1追号正常2追号完成
									bookorder.setPlaytype(appendTask
											.getPlayType());// 玩法
									bookorder.setStoptype(String
											.valueOf(appendTask.getStopFlag()));// 停追类型
																				// :0不停追1小奖停追2大奖停追
									bookorder.setCancelmoney(MoneyUtil.toYuanStr(appendTask.getCancelMoney()));// 取消金额
									bookorder
											.setPrizemoney(MoneyUtil.toYuanStr(appendTask
													.getWinningTotalMoney()));// 奖金
									bookorder.setStake(String
											.valueOf(appendTask
													.getPerNoteNumber()));// 注数
									bookorder.setCreatetime(DateUtil.convertStringToDate("yyyy-MM-dd HH:mm:ss",appendTask
											.getCreateTime()).toString());// 创建时间
									bookorder.setPartnerid(appendTask
											.getPartnerId());// 合作商
									bookorder.setStartissue(appendTask
											.getBeginIssueNo());// 开始 期次
									bookorder.setCurrentissue(orderTemp
											.getIssueNo());// 当前期次
									bookorder.setIssuenum(String
											.valueOf(appendTask
													.getAppendQuantity()));// 期次总数
									bookorder.setGameid(appendTask
											.getLotteryId());// 彩种
									bookorder.setCancelnum(String.valueOf(appendTask.getCancelNum()));// 取消期次数
									bookorder
											.setFinishednum(String.valueOf(appendTask
													.getAppendQuantity()
													- appendTask
															.getRemainingQuantity()));// 完成期次数
									bookorder.setFinishedsum(MoneyUtil.toYuanStr(appendTask.getFinishedMoney()));// 完成追号金额
									bookorder.setPid(orderNos[2]);// 合作商追号ID
									bookorder.setAnteBall(appendTask.getBall());// 投注内容

									bookordersList.add(bookorder);
								}

							} else {

								response.setData(reMsg.getMsg());
								response.setStatusCode(reMsg.getStatusCode());
								Log.fucaibiz.error("追号查询返回的错误信息："+reMsg.getMsg());
								return response;

							}

						}

					}

				}else{
					
					response.setData("未查询到数据");
					response.setStatusCode(ConstantsUtil.STATUS_CODE_NOTQUERY_DATA);
					return response;
				}

				body730.setOrders(bodyInfo730);

				msg730.setBody(body730);
				msg730.setHead(head730);
				response.setData(TransactionMsgLoader730.msgToXml(msg730));
				response.setResponseTransCode(retrunCode);
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SUCCESS);
			} else {

				response.setData(reMsg.getMsg());
				response.setStatusCode(reMsg.getStatusCode());
				return response;
			}

		}catch(Exception e){
			if(e instanceof NumberFormatException){
				response.setData("非法数字");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_ILLEGAL_DIGITAL);
				Log.fucaibiz.error("非法数字" , e);
				return response;
			}
			if(e instanceof ParseException){
				response.setData("时间格式转换出错");
				response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
				Log.fucaibiz.error("时间格式转换出错",e);
				return response;
			}
			
			response.setData("系统错误");
			response.setStatusCode(ConstantsUtil.STATUS_CODE_SYSTEM_FAIL);
			Log.fucaibiz.error(e);
			return response;
		}
		
		Log.fucaibiz.info("730消息返回成功");
		return response;
	}



}
