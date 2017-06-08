package com.cqfc.user.order.service.impl;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cqfc.protocol.userorder.Order;
import com.cqfc.protocol.userorder.PcUserOrder;
import com.cqfc.protocol.userorder.UserOrderService;
import com.cqfc.user.order.service.IUserOrderService;
@Service
public class UserOrderServiceHandler  implements UserOrderService.Iface {

	
	@Autowired
	IUserOrderService userOrderService;
	
	@Override
	public int addUserOrder(Order order) throws TException {

		return userOrderService.addUserOrder(order);
	}


	@Override
	public Order getUserOrderByUserId(long userId, String orderNo)
			throws TException {
		// TODO Auto-generated method stub
		return userOrderService.getUserOrderByUserId(userId, orderNo);
	}

	@Override
	public PcUserOrder getUserOrder(Order order, int pageNum, int pageSize)
			throws TException {
		// TODO Auto-generated method stub
		return userOrderService.getUserOrder(order, pageNum, pageSize);
	}

	@Override
	public Order getUserPrizeStatus(long userId, String partnerId,
			String orderNo, String ticketId) throws TException {
		// TODO Auto-generated method stub
		return userOrderService.getUserPrizeStatus(userId, partnerId, orderNo, ticketId);
	}



}
