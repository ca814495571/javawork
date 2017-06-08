package com.cqfc.partner.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.protocol.partner.PartnerIpAddress;
import com.cqfc.protocol.partner.PartnerService;

public class PartnerClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10005);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			PartnerService.Client partner = new PartnerService.Client(protocol);

			LotteryPartner lotteryPartner = new LotteryPartner();
			lotteryPartner.setPartnerId("00860007");
			lotteryPartner.setPartnerName("test_good_user");
			lotteryPartner.setPartnerType((short) 3);
			lotteryPartner.setSecretKey("testzxcvuser002");
			lotteryPartner.setState((short) 1);
			lotteryPartner.setCallbackUrl("/test/list/getList");
			lotteryPartner.setMinBalance(23000);

			List<PartnerIpAddress> ipList = new ArrayList<PartnerIpAddress>();
			PartnerIpAddress ip = new PartnerIpAddress();
			ip.setIpAddress("test");
			ip.setPartnerId("123");
			ipList.add(ip);
			// System.out.println(partner.updateIpAddress("123", ipList));
			// System.out.println(partner.deletePartnerByPartnerId("123"));

			// System.out.println(partner.updateLotteryPartner(lotteryPartner,
			// "00860003"));
			System.out.println(partner.addLotteryPartner(lotteryPartner));
			// System.out.println(partner.findPartnerForCheck("00860007"));
			// System.out.println(partner.findLotteryPartnerById("00860008"));
			// System.out.println(partner.getLotteryPartnerList(lotteryPartner,
			// 1, 5));
			// System.out.println(partner.updateState("zc1234236455", 2));

			// PartnerIpAddress partnerIpAddress = new PartnerIpAddress();
			// partnerIpAddress.setIpAddress("127.0.0.1");
			// partnerIpAddress.setId(4);
			// partnerIpAddress.setIpAddress("192.210.23.215");
			// partnerIpAddress.setPartnerId("zc1234236455");

			// System.out.println(partner.getPartnerIpAddressList("zc1234236452"));
			// System.out.println(partner.addPartnerIpAddress(partnerIpAddress));
			// System.out.println(partner.updatePartnerIpAddress(partnerIpAddress));
			// System.out.println(partner.deletePartnerIpAddressByIp(2));
			// System.out.println(partner.isExistIpAddress("zc1234236455","192.210.23.215"));
			// System.out.println(partner.findPartnerIpAddressById(3));

			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException
					&& ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
				System.out.println("The result of function is NULL");
			}
		}
	}
}
