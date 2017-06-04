package com.tom.appone.service.impl;

import com.tom.common.servertwo.api.ServertwoService;

public class ServertwoServiceImpl implements ServertwoService{
    public String print(String name) {
        return "这是服务2的数据..."+ name;
    }
}
