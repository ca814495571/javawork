package com.tom.appone.test;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tom.common.servertwo.api.ServertwoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceTest {

    @Reference
    private ServertwoService servertwoService;

    @Test
    public void testA(){
        System.out.printf(servertwoService.print("123"));
    }

}
