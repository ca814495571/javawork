package com.cqfc.ticketissue.testClient;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cqfc.ticketissue.model.TempOrder;

public class DataCenter {
	private static final LinkedBlockingQueue<TempOrder> queue = new LinkedBlockingQueue<TempOrder>(50000);
	private static AtomicBoolean end = new AtomicBoolean(false);
	public static  void add(TempOrder order){
		try {
			queue.put(order);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static  TempOrder get(){
		return queue.poll();
	}
	
	public static  boolean set2End(){
		return end.compareAndSet(false, true);
	}
	
	public static  boolean isEnd(){
		return end.get();
	}
}
