package com.cqfc.businesscontroller.task;

import com.cqfc.protocol.businesscontroller.Message;

/**
 * @author: giantspider@126.com
 */

public class TransTask implements Runnable {

	/*
	 * Map<String, String> taskProperties;
	 * 
	 * public TransTask(Map<String, String> properties) { taskProperties =
	 * properties; }
	 * 
	 * @Override public void run() { try {
	 * System.out.println(Thread.currentThread().getName() + " processing task:"
	 * + taskProperties); // TODO: // save transaction details to database ...
	 * // generate Plan for the transaction request ... // forward issue request
	 * to 3rd platform ... // any other steps if required ...
	 * Thread.sleep(10000000); } catch (InterruptedException ex) {
	 * ex.printStackTrace(); } }
	 */

	Message message;

	public TransTask(Message message) {
		this.message = message;
	}

	@Override
	public void run() {
		switch (message.getTransCode()) {
		case 104:
			TransactionMsgProcessor104.process(message);
			break;
		}
	}

}
