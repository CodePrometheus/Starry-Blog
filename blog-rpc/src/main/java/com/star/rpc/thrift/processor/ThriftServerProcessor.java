package com.star.rpc.thrift.processor;

import com.star.rpc.thrift.annotation.ThriftServer;
import jakarta.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Thrift 服务端注解处理器
 *
 * @Author: Starry
 * @Date: 02-17-2023
 */
@Component
public class ThriftServerProcessor implements BeanPostProcessor, ApplicationContextAware {

    @Value("${thrift.server.port}")
    private String port;

    @Resource
    private Map<Integer, Map<String, Object>> serviceMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.serviceMap = (Map<Integer, Map<String, Object>>) applicationContext.getBean("serviceMap");
    }

    /**
     * bean 初始化之前执行
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        ThriftServer annotation = bean.getClass().getAnnotation(ThriftServer.class);
        if (null != annotation) {
            String serviceName = annotation.serviceName();
            // 全部的服务都放在一个map中，key为端口号，value为一个map，key为服务名，value为服务实现类
            Map<String, Object> serviceContentMap = this.serviceMap.get(port);
            if (null == serviceContentMap) {
                serviceContentMap = new HashMap<>();
            }
            serviceContentMap.put(serviceName, bean);
            serviceMap.put(Integer.parseInt(port), serviceContentMap);
        }
        return bean;
    }

}
