package com.star.rpc.thrift.processor;

import com.star.rpc.thrift.pool.TSocketPool;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Thrift客户端动态代理
 * cglib.proxy
 *
 * @Author: Starry
 * @Date: 02-17-2023
 */
public class ThriftClientProxy implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ThriftClientProxy.class);

    /**
     * 真正的客户端
     */
    private TServiceClient realClient;

    /**
     * 协议
     */
    private TProtocol protocol;

    /**
     * 连接池
     */
    private TSocketPool pool;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 绑定
     *
     * @param clazz
     * @param protocol
     * @param serviceName
     * @param pool
     * @return
     * @throws Exception
     */
    public Object bind(Class clazz, TProtocol protocol, String serviceName, TSocketPool pool) throws Exception {
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, serviceName);
        Constructor<TServiceClient> constructor = clazz.getDeclaredConstructor(TProtocol.class);
        this.realClient = constructor.newInstance(multiplexedProtocol);
        this.protocol = protocol;
        this.pool = pool;
        this.serviceName = serviceName;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.realClient.getClass());
        enhancer.setCallback(this);
        return enhancer.create(new Class[]{TProtocol.class}, new Object[]{this.protocol});
    }

    /**
     * 拦截器
     *
     * @param o
     * @param method
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 真正绑定客户端
        TSocket socket = pool.borrowObject();
        TProtocol protocol = new TBinaryProtocol(socket);
        TMultiplexedProtocol multiplexedProtocol = new TMultiplexedProtocol(protocol, serviceName);
        Constructor<TServiceClient> constructor = (Constructor<TServiceClient>) this.realClient.getClass().getDeclaredConstructor(TProtocol.class);
        this.realClient = constructor.newInstance(multiplexedProtocol);
        try {
            // 调用方法
            return methodProxy.invoke(this.realClient, objects);
        } catch (Exception e) {
            logger.error("Thrift RPC 调用发生异常: ", e);
            return null;
        } finally {
            pool.returnObject(socket);
        }
    }

}
