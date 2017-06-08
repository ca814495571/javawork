package com.cqfc.queue.service;

import com.cqfc.queue.model.QueueTask;

public interface QueueService {

	public void submit(Runnable runnable);

}
