package com.cqfc.samplemodule.client;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import com.cqfc.samplemodule.protocol.SampleService.AsyncClient.doService_call;


/**
 * @author: giantspider@126.com
 */

public class MyCallback implements AsyncMethodCallback<doService_call> {

    // 返回结果
    @Override
    public void onComplete(doService_call response) {
        //System.out.println("onComplete");
        try {
            System.out.println("retval: " + response.getResult().toString());
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    // 返回异常
    @Override
    public void onError(Exception exception) {
        System.out.println("onError");
    }

}
