package com.star.rpc.thrift.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Thrift 客户端注解
 * 作用在属性上
 *
 * @Author: Starry
 * @Date: 02-17-2023
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThriftClient {

    // 主机
    String host();

    // 端口
    int port();

    // 超时时间
    int timeout() default 3000;

    // 服务名
    String serviceName() default "";

}
