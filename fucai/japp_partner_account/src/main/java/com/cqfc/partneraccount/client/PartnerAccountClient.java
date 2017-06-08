package com.cqfc.partneraccount.client;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountService;

public class PartnerAccountClient {
	public static void main(String[] args) {
		try {
			TTransport transport;
			transport = new TSocket("localhost", 10008);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			PartnerAccountService.Client partnerAccount = new PartnerAccountService.Client(protocol);

			PartnerAccount account = new PartnerAccount();
			account.setPartnerId("00860009");
			System.out.println(partnerAccount.addPartnerAccount(account));

			/*
			 * System.out.println(partnerAccount.payPartnerAccount("zc1234236455"
			 * ,1,"sssz")); PartnerRecharge partnerRecharge = new
			 * PartnerRecharge(); partnerRecharge.setPartnerId("zc1234236455");
			 * partnerRecharge.setRechargeAmount(1000);
			 * partnerRecharge.setSerialNumber("s332"); //
			 * System.out.println(partnerAccount
			 * .addPartnerRecharge(partnerRecharge)); PartnerPreApply
			 * partnerPreApply = new PartnerPreApply();
			 * partnerPreApply.setPartnerId("00860002"); //
			 * partnerPreApply.setStatus(1); //
			 * partnerPreApply.setPartnerUniqueNo("12312"); //
			 * partnerPreApply.setPreMoney(10000);
			 * 
			 * // System.out.println(partnerAccount.createPartnerPreApply(
			 * partnerPreApply)); //
			 * System.out.println(partnerAccount.getPartnerPreApplyList
			 * (partnerPreApply, 1, 100));
			 */
//			System.out.println(partnerAccount.auditPartnerPreApply(1, 1));

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
