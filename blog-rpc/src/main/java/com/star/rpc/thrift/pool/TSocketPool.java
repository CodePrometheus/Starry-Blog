package com.star.rpc.thrift.pool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.thrift.transport.TSocket;

import java.util.NoSuchElementException;

/**
 * TSocket连接池
 *
 * @Author: Starry
 * @Date: 02-17-2023
 */
public class TSocketPool implements ObjectPool<TSocket> {

    private final GenericObjectPool<TSocket> pool;

    public TSocketPool(String host, int port, int timeout) {
        TSocketPoolFactory factory = new TSocketPoolFactory(host, port, timeout);
        pool = new GenericObjectPool<>(factory, new TSocketPoolConfig(timeout));
    }

    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
        pool.addObject();
    }

    @Override
    public TSocket borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        return pool.borrowObject();
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {
        pool.clear();
    }

    @Override
    public void close() {
        pool.close();
    }

    @Override
    public int getNumActive() {
        return pool.getNumActive();
    }

    @Override
    public int getNumIdle() {
        return pool.getNumIdle();
    }

    @Override
    public void invalidateObject(TSocket tSocket) throws Exception {
        pool.invalidateObject(tSocket);
    }

    @Override
    public void returnObject(TSocket tSocket) throws Exception {
        pool.returnObject(tSocket);
    }

}
