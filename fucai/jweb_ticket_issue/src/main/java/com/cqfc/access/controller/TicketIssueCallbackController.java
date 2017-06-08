package com.cqfc.access.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cqfc.access.task.TicketIssueCallbackTask;
import com.jami.util.Log;

@Controller
@RequestMapping("/ticketissue")
public class TicketIssueCallbackController {

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@RequestMapping(value = "/result", produces = { "text/xml;charset=UTF-8" })
	@ResponseBody
	public String ticketIssueResult(String transcode, String msg, String key, String partnerid)
			throws FileNotFoundException, UnsupportedEncodingException {
		Log.run.info("ticketIssueCallBack msg=%s, transcode=%s, partnerid=%s, key=%s", msg, transcode, partnerid, key);
		try {
			TicketIssueCallbackTask ticketIssueTask = new TicketIssueCallbackTask(msg);
			threadPoolTaskExecutor.submit(ticketIssueTask);
			Log.run.debug("ticketIssueCallBack poolsize=%d", threadPoolTaskExecutor.getThreadPoolExecutor().getQueue()
					.size());
		} catch (RejectedExecutionException e) {
			Log.run.error("ticketIssueCallBack回调请求线程池已满,msg=%s, exception=%s", msg, e);
		} catch (Exception e) {
			Log.run.error("ticketIssueCallBack回调请求submit线程池发生异常,msg=%s, exception=%s", msg, e);
		}
		return "";
	}
}
