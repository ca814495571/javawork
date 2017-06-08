package com.tom.appone.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tom.common.servertwo.api.ServertwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Reference(version = "1.0.0")
    private ServertwoService servertwoService;

    @Reference(version = "2.0.0")
    private ServertwoService server3Service;

    @RequestMapping("/a")
    public String testA(){
        return servertwoService.print("是我就是我")+"------------------------------------"+server3Service.print("呵呵你");
    }
}
