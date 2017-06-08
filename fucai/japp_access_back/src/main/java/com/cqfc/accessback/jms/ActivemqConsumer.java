package com.cqfc.accessback.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.cqfc.accessback.service.impl.AccessBackServiceImpl;
import com.cqfc.protocol.partner.LotteryPartner;
import com.cqfc.util.ActivemqMethodUtil;
import com.cqfc.util.ActivemqSendObject;
import com.cqfc.util.SwitchUtil;
import com.jami.util.Log;


public class ActivemqConsumer implements MessageListener {
	
	@Override
	public void onMessage(Message message) {
	
		if (!SwitchUtil.mqConsumerIsOpen()) {
			return;
		}
		ObjectMessage msg = (ObjectMessage) message;
		try {
			String methodId = msg.getStringProperty("methodId");

			// 触发更新合作商信息
			if (ActivemqMethodUtil.MQ_PARTNERINFO_CHANGE_METHODID.equals(methodId)) {
				ActivemqSendObject obj = (ActivemqSendObject) msg.getObject();
				
				AccessBackServiceImpl.updatePartnerMap((LotteryPartner)obj.getObj());
			}
		} catch (Exception e) {
			Log.run.error("japp_access_back消息监听发生异常", e);
		}
	}
}
