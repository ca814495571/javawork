package com.tom.appone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@org.springframework.boot.autoconfigure.SpringBootApplication
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

