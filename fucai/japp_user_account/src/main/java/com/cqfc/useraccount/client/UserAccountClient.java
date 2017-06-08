package com.cqfc.useraccount.client;

import java.util.Date;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.cqfc.protocol.useraccount.UserAccountService;
import com.cqfc.protocol.useraccount.UserInfo;
import com.cqfc.protocol.useraccount.WithdrawAccount;
import com.cqfc.protocol.useraccount.WithdrawApply;
import com.cqfc.util.DateUtil;
import com.cqfc.util.UserAccountConstant;

public class UserAccountClient {
	public static void main(String[] args) {
		TTransport transport = null;
		try {
			transport = new TSocket("127.0.0.1", 10009);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			UserAccountService.Client userAccount = new UserAccountService.Client(protocol);
			// System.out.println(userAccount.findUserInfoById(3000));
			
			  UserInfo userInfo = new UserInfo(); userInfo.setCardType(1);
			  userInfo.setRegisterTerminal(0); userInfo.setAccountType(0);
			  userInfo.setPartnerId("00860001");
			  userInfo.setPartnerUserId("123456z2");
			  userInfo.setAge(14);
			  userInfo.setMobile("13452688522");
			  userInfo.setCardNo("5952456891227878");
			  userInfo.setAccountType(UserAccountConstant.UserAccountType.QQ.getValue());
			  userInfo.setCardType(UserAccountConstant.UserCardType.IDENTIFICATION_CARD.getValue());
			  userInfo.setBirthday(DateUtil.dateToString(new Date(), DateUtil.DATE_FORMAT_DATETIME));
//			  System.out.println(userAccount.createUserInfo(userInfo));
			 

//			int i = userAccount.payUserAccount(48, "BP001#00860003#SSQ20141021915", 200);
//			System.out.println(i);
			// 创建彩金
//			UserHandsel userHandsel = new UserHandsel();
//			userHandsel.setUserId(3);
//			userHandsel.setPartnerId("00860001");
//			userHandsel.setActivityId("test");
//			userHandsel.setSerialNumber("z1222223007");
//			userHandsel.setPresentAmount(10000);
//			userHandsel.setUsableAmount(10000);
//			userHandsel.setUsedAmount(0);
//			userHandsel.setPresentTime("2014-06-18 16:00:00");
//			userHandsel.setValidTime("2014-06-19 15:00:00");
//			userHandsel.setFailureTime("2014-06-28 13:50:00");
//			userHandsel.setState(1);
//			userHandsel.setPartnerHandselId("123132-111-222");
			// System.out.println(userAccount.createUserHandsel(userHandsel));
			// System.out.println(userAccount.updatePrizePassword(3,"123","123456TEST"));
//			UserPreApply userPreApply = new UserPreApply();
//			userPreApply.setPartnerId("00860001");
//			userPreApply.setUserId(3);
//			userPreApply.setStatus(1);
			// userPreApply.setPartnerUniqueNo("TEST00112");
			// userPreApply.setPreMoney(10000);
			// System.out.println(userAccount.findUserInfoById(3));
//			System.out.println(userAccount.getUserPreApplyList(userPreApply, 1, 100));
			// System.out.println(userAccount.createUserPreApply(userPreApply));
			// System.out.println(userAccount.modifyUserHandselState(3));
			// System.out.println(userAccount.getUsableUserHandselList(3));
			// System.out.println(userAccount.getUserInfoList(userInfo,1,3));

			WithdrawApply apply = new WithdrawApply();
			WithdrawAccount withdrawAccount = new WithdrawAccount();

			withdrawAccount.setUserId(3);
			withdrawAccount.setRealName("haha");
			withdrawAccount.setBankName("cmb");
			withdrawAccount.setAccountAddress("aaa");
			withdrawAccount.setAccountNo("383848");

			apply.setUserId(3);
			apply.setRealName("haha");
			apply.setPartnerId("0087002");
			apply.setPartnerApplyId("testtest");
			apply.setSerialNumber("kklsekkdk");
			apply.setWithdrawAccount(withdrawAccount);
			apply.setWithdrawAmount(100000);
			System.out.println(userAccount.totalAccountMoney());
//			System.out.println(userAccount.createWithdrawApply(apply));
//			 System.out.println(userAccount.freezeUserAccount(3, 51000,
//			 "1231200000224"));

		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			if (e instanceof TApplicationException
					&& ((TApplicationException) e).getType() == TApplicationException.MISSING_RESULT) {
				System.out.println("The result of function is NULL");
			}
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}
}
