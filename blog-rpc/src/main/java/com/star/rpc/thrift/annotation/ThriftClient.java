package com.star.rpc.thrift.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Thrift 客户端注解
 * 作用在属性上
 *
 * @Author: Starry
 * @Date: 02-17-2023
 */
@Component
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThriftClient {

    // 主机
    String host();

    // 端口
    String port();

    // 超时时间
    int timeout() default 3000;

    // 服务名
    String serviceName() default "";

}
