package com.cqfc.statistics.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 使用实例:
 * 
 * import org.springframework.context.ApplicationContext;
 * import com.jami.common.ApplicationContextProvider;
 *
 * ApplicationContext ctx = ApplicationContextProvider.getApplicationContext();
 * ctx.getBean("BeanId", Bean.class);
 *
 * 
 * @author giantspider@126.com
 *
 */

public class ApplicationContextProvider implements ApplicationContextAware {
    
    private static ApplicationContext context;
 
    public static ApplicationContext getApplicationContext() {
        return context;
    }
 
    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
    }
    
}
