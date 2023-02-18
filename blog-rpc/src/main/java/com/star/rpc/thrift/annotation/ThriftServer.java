package com.star.rpc.thrift.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Thrift 服务端注解
 * 作用在类上
 *
 * @Author: Starry
 * @Date: 02-17-2023 23:33
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThriftServer {

    // 服务名
    String serviceName();

    // 端口
    int port();

}
