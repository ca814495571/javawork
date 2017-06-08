package com.cqfc.accessback.record;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.jami.util.Log;
import com.jami.util.RecordLogger;

public class InitRecordTask implements Runnable {

	@Override
	public void run() {
		String logDir = RecordLogger.getLogDir();
		File logDirFile = new File(logDir);
		for (File logFile : logDirFile.listFiles()) {
			String name = logFile.getName();
			if (!name.endsWith(RecordLogger.LOG_FILE_PATH_SUBBIX)
					|| logFile.length() <= 0) {
				continue;
			}
			String gameId = name.substring(0, name.length()
					- RecordLogger.LOG_FILE_PATH_SUBBIX.length());
			readLastLineFromFile(gameId, logFile);
		}
	}

	private void readLastLineFromFile(String gameId, File file) {
		RandomAccessFile randomFileReader = null;
		int lessChar = 2;
		try {
			randomFileReader = new RandomAccessFile(file, "r");
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
			TicketCallbackRecordBuffer.addLog(gameId, result);
		} catch (Exception e) {
			Log.run.warn("read record file failed, gameId=&s", gameId);
		} finally {
			if (randomFileReader != null)
				try {
					randomFileReader.close();
				} catch (IOException e) {
					Log.run.debug("close record file failed");
				}
		}
	}
}
