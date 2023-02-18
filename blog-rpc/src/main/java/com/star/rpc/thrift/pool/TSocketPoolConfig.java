package com.star.rpc.thrift.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.thrift.transport.TSocket;

/**
 * @Author: Starry
 * @Date: 02-17-2023
 */
public class TSocketPoolConfig extends GenericObjectPoolConfig<TSocket> {

    public TSocketPoolConfig(int timeout) {
        setMaxTotal(20);
        setTestOnCreate(true);
        setTestOnBorrow(true);
        setMinEvictableIdleTimeMillis(timeout);
        setTimeBetweenEvictionRunsMillis(1000L);
    }

}
