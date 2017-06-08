
package com.cqfc.queue.controller;

import java.util.List;
import java.util.Random;
import java.util.concurrent.RejectedExecutionException;

import com.cqfc.queue.model.Message;
import com.cqfc.queue.model.QueueTask;
import com.cqfc.queue.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/queue")
public class QueueController {

	@Autowired
    private QueueService queueService;

    @RequestMapping(value = "/puttask")
    @ResponseBody
    public String putTask() {
        Random random = new Random();
        int msgId = random.nextInt(100);
        String ret;
        try {
            Message msg = new Message(msgId, "msg body for msg id:" + msgId);
            QueueTask queueTask = new QueueTask(msg);
            queueService.submit(queueTask);
            ret = "task submitted successfully: msgId=" + msgId;
        } catch (RejectedExecutionException e) {
            ret = "fail to submit task(msgId= +" + msgId + "): queue is *FULL*";
        }
        return ret;
    }

}
