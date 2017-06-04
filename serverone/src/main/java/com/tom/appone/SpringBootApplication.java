package com.tom.appone;

import com.alibaba.dubbo.rpc.Invoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@org.springframework.boot.autoconfigure.SpringBootApplication
//@ConditionalOnClass(Invoker.class)
@ImportResource("classpath:dubbo/consumer.xml")
public class SpringBootApplication {

    static Log logger =  LogFactory.getLog(SpringBootApplication.class);
    public static void main(String[] args){
        ApplicationContext ctx = new SpringApplicationBuilder()
                .sources(SpringBootApplication.class)
                .web(true) // 没错，把项目设置成web环境
                .run(args);
        logger.info(ctx.getApplicationName()+"1231232");
    }
}

