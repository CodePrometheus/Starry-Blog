package com.star.rpc.thrift.processor;

import com.star.rpc.thrift.annotation.ThriftClient;
import com.star.rpc.thrift.pool.TSocketPool;
import jakarta.annotation.Resource;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Thrift 客户端注解处理器
 *
 * @Author: Starry
 * @Date: 02-17-2023
 */
@Component
public class ThriftClientProcessor implements BeanPostProcessor, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ThriftClientProcessor.class);

    private ApplicationContext applicationContext;

    // @Resource
    private Map<String, TSocketPool> connPoolMap;

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
        List<Field> fields = scanTargetClass(bean.getClass());
        fields.forEach(field -> {
            Object target = applicationContext.getBean(beanName);
            // 设置字段可访问性
            ReflectionUtils.makeAccessible(field);
            try {
                TServiceClient client = createClient(field.getType(), field.getAnnotation(ThriftClient.class));
                field.set(target, client);
            } catch (Exception e) {
                logger.error("ThriftClientProcessor postProcessBeforeInitialization error: ", e);
            }
        });
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取哪些类中哪些成员变量带有目标注解 ThriftClient
     *
     * @param clazz
     * @return
     */
    private List<Field> scanTargetClass(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            ThriftClient hasAnno = field.getAnnotation(ThriftClient.class);
            if (hasAnno != null) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }


    /**
     * 根据注解生成ThriftClient对象
     *
     * @param clazz
     * @param anno
     * @return
     * @throws Exception
     */
    private TServiceClient createClient(Class clazz, ThriftClient anno) throws Exception {
        String host = anno.host();
        String port = anno.port();
        int timeout = anno.timeout();
        // connPoolMap为连接池键值映射Map（全局），主要考虑不同主机、端口、超时场景下的连接池
        String key = host + "-" + port + "-" + timeout;
        TSocketPool pool = connPoolMap.get(key);
        if (pool == null) {
            pool = new TSocketPool(host, Integer.parseInt(port), timeout);
            logger.info("关于{}的连接池已创建", key);
            connPoolMap.put(key, pool);
        }
        // 创建客户端代理
        ThriftClientProxy clientProxy = new ThriftClientProxy();
        TTransport fakeSocket = new TSocket("127.0.0.1", 0, 0);
        TProtocol protocol = new TBinaryProtocol(fakeSocket);
        logger.info("Starry-Thrift-Client 已启动, 端口: {}", port);
        return (TServiceClient) clientProxy.bind(clazz, protocol, anno.serviceName(), pool);
    }

}
