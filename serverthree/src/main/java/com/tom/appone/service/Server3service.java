package com.tom.appone.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.tom.common.servertwo.api.ServertwoService;

@Service
public class Server3service implements ServertwoService{

    public String print(String name) {
        return "这是服务3的数据"+name;
    }
}
