package com.cqfc.samplemodule.service.impl;


import com.cqfc.samplemodule.protocol.SampleService;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;


@Service
public class SampleServiceImpl implements SampleService.Iface {

    public String doService(String param) throws TException {
        return "hi " + param;
    }

}
