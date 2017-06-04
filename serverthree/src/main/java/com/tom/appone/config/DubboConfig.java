package com.tom.appone.config;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.tom.appone.service.Server3service;
import com.tom.common.servertwo.api.ServertwoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

@Configuration
@PropertySource(value = "classpath:/dubbo.properties")
public class DubboConfig {
//
//
//
//    @Value("${dubbo.application.name}")
//    private String applicationName;
//
////    @Value("${dubbo.registr.protocol}")
////    private String protocol;
//
////    @Value("${dubbo.registry.address}")
////    private String registryAddress;
//
//    @Value("${dubbo.protocol.name}")
//    private String protocolName;
//
//    @Value("${dubbo.protocol.port}")
//    private int protocolPort;
////
////    @Value("${dubbo.provider.timeout}")
////    private int timeout;
//////
////    @Value("${dubbo.provider.retries}")
////    private int retries;
////
////    @Value("${dubbo.provider.delay}")
////    private int delay;
//
//
//    /**
//     * 设置dubbo扫描包
//     * @param packageName
//     * @return
//     */
//    @Bean
//    public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package}") String packageName) {
//        AnnotationBean annotationBean = new AnnotationBean();
//        annotationBean.setPackage(packageName);
//        return annotationBean;
//    }
//
//    /**
//     * 注入dubbo上下文
//     *
//     * @return
//     */
//    @Bean
//    public ApplicationConfig applicationConfig() {
//        // 当前应用配置
//        ApplicationConfig applicationConfig = new ApplicationConfig();
//        applicationConfig.setName(this.applicationName);
//        return applicationConfig;
//    }
//
//
//    /**
//     * 注入dubbo注册中心配置,基于zookeeper
//     *
//     * @return
//     */
////    @Bean
////    public RegistryConfig registryConfig() {
////        // 连接注册中心配置
////        RegistryConfig registry = new RegistryConfig();
////        registry.setProtocol(protocol);
////        registry.setAddress(registryAddress);
////        registry.s
////        return registry;
////    }
//
//    /**
//     * 默认基于dubbo协议提供服务
//     *
//     * @return
//     */
//    @Bean
//    public ProtocolConfig protocolConfig() {
//        // 服务提供者协议配置
//        ProtocolConfig protocolConfig = new ProtocolConfig();
//        protocolConfig.setName(protocolName);
//        protocolConfig.setPort(protocolPort);
//        protocolConfig.setThreads(10);
//        System.out.println("默认protocolConfig：" + protocolConfig.hashCode());
//        return protocolConfig;
//    }
//
//    /**
//     * dubbo服务提供,没zookeeper关闭
//     *
//     * @param applicationConfig
//     * @param registryConfig
//     * @param protocolConfig
//     * @return
//     */
////    @Bean(name="myProvider")
////    public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig, ProtocolConfig protocolConfig) {
////        ProviderConfig providerConfig = new ProviderConfig();
////        providerConfig.setTimeout(timeout);
////        providerConfig.setRetries(retries);
////        providerConfig.setDelay(delay);
////        providerConfig.setApplication(applicationConfig);
////        providerConfig.setRegistry(registryConfig);
////        providerConfig.setProtocol(protocolConfig);
////        return providerConfig;
////    }
//
//
//    @Bean
//    public ServiceConfig serviceConfig(ProtocolConfig protocolConfig,RegistryConfig registryConfig) {
//        // 服务提供者协议配置
//        ServiceBean serviceBean = new ServiceBean();
////        serviceConfig.setRegister(false);
//        serviceBean.setRegistry(registryConfig);
//        serviceBean.setBeanName("server3service");serviceBean.setId("serviceBean");
//        serviceBean.setRegister(Boolean.FALSE);
//        serviceBean.setInterface("com.tom.common.servertwo.api.ServertwoService");
////        serviceConfig.setRef(new Server3service());
////        serviceConfig.setProtocol(protocolConfig);
////        serviceConfig.setRegistry(new RegistryConfig());
////        serviceConfig.setRegistry(RegistryConfig.NO_AVAILABLE);
//        return serviceBean;
//    }
//
//
//    public String getApplicationName() {
//        return applicationName;
//    }
//
//    public void setApplicationName(String applicationName) {
//        this.applicationName = applicationName;
//    }
//
//    public String getProtocolName() {
//        return protocolName;
//    }
//
//    public void setProtocolName(String protocolName) {
//        this.protocolName = protocolName;
//    }
//
//    public int getProtocolPort() {
//        return protocolPort;
//    }
//
//    public void setProtocolPort(int protocolPort) {
//        this.protocolPort = protocolPort;
//    }

}




