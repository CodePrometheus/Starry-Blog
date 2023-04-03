package com.star.rpc.thrift.processor;

import com.star.rpc.thrift.pool.TSocketPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Thrift 自动配置类
 *
 * @Author: Starry
 * @Date: 02-18-2023
 */
// @Configuration
public class ThriftConfig {

    // @Bean(name = "serviceMap")
    // public Map<Integer, Map<String, Object>> creatServiceMap() {
    //     return new HashMap<>();
    // }
    //
    // @Bean(destroyMethod = "destroy")
    // public ThriftRunner createRunner() {
    //     return new ThriftRunner();
    // }
    //
    // @Bean(name = "connPoolMap")
    // public Map<String, TSocketPool> createTransportMap() {
    //     return new HashMap<>();
    // }

}
