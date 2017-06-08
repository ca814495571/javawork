package com.cqfc.partneraccount.task;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.cqfc.partneraccount.dao.mapper.PartnerAccountMapper;
import com.cqfc.partneraccount.datacenter.PartnerAccountBuffer;
import com.cqfc.protocol.partneraccount.PartnerAccount;
import com.cqfc.protocol.partneraccount.PartnerAccountLog;
import com.cqfc.util.PartnerAccountLogUtil;
import com.jami.util.Log;
import com.jami.util.PartnerLogger;

public class InitAccountTask implements Runnable {

	private PartnerAccountMapper partnerAccountMapper;
	private ApplicationContext applicationContext;

	public InitAccountTask(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void run() {
		if (partnerAccountMapper == null) {
			partnerAccountMapper = applicationContext.getBean(
					"partnerAccountMapper", PartnerAccountMapper.class);
		}
		int lessChar = 2;
		List<PartnerAccount> list = partnerAccountMapper
				.getPartnerAccountList("1=1");
		RandomAccessFile randomFileReader = null;
		for (PartnerAccount account : list) {
			String partnerId = account.getPartnerId();
			String filePath = PartnerLogger.getDynamicLogger(partnerId)
					.getFilePath();
			File logFile = new File(filePath);
			if (!logFile.exists() || logFile.length() <= 0) {
				continue;
			}
			try {
				randomFileReader = new RandomAccessFile(filePath, "r");
				long start = randomFileReader.getFilePointer();
				long cur = start + randomFileReader.length() - 1;
				int c = -1;
				int readNum = 0;

				// StringBuffer sb = new StringBuffer();
				while (cur >= start) {
					randomFileReader.seek(cur);
					c = randomFileReader.read();
					if (c == '\n' || c == '\r') {
						if (readNum > lessChar) {
							break;
						} else {
							readNum--;
						}
					}
					// sb.insert(0, (char)c);
					cur--;
					readNum++;
				}
				String result = "";// sb.toString();
				byte[] readBytes = new byte[readNum];
				// randomFileReader.seek(cur);
				// 此处读字节会少读第一个字节，原因未知，因前面是一个时间不影响结果因此不处理，但有风险存在。
				randomFileReader.read(readBytes);
				result = new String(readBytes, "UTF-8");
				PartnerAccountLog accountLog = PartnerAccountLogUtil
						.convertStr2Log(result);
				if (!partnerId.equals(accountLog.getPartnerId())){
					Log.run.warn(
							"partner log in error format, partnerId=%s",
							partnerId);
					continue;
				}
				if (accountLog.getRemainAmount() != account.getUsableAmount()) {
					Log.run.error(
							"Account money not same,change it to same as log, partnerId=%s",
							partnerId);
					long amount = account.getUsableAmount()
							- accountLog.getRemainAmount();
					partnerAccountMapper.updateDeductAccount(partnerId, amount);
					account.setTotalAmount(account.getTotalAmount() - amount);
					account.setUsableAmount(account.getUsableAmount() - amount);
				}
			} catch (Exception e) {
				Log.run.warn("read partner file failed, partnerId=&s",
						partnerId);
			} finally {
				if (randomFileReader != null)
					try {
						randomFileReader.close();
					} catch (IOException e) {
						Log.run.debug("close partner file failed");
					}
			}
		}
		PartnerAccountBuffer.initBuffer(list);
	}
}
