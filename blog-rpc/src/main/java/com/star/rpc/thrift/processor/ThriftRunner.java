package com.star.rpc.thrift.processor;

import com.star.rpc.thrift.pool.TSocketPool;
import jakarta.annotation.Resource;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.util.Map;

/**
 * @Author: Starry
 * @Date: 02-17-2023
 */
public class ThriftRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ThriftRunner.class);

    @Resource
    private Map<Integer, Map<String, Object>> serviceMap;

    @Resource
    private Map<String, TSocketPool> connPoolMap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Integer key : serviceMap.keySet()) {
            ServeThread thread = new ServeThread(key, serviceMap.get(key));
            thread.setName("starry-rpc-" + key);
            thread.start();
        }
    }

    @Bean
    public void destroy() {
        for (String key : connPoolMap.keySet()) {
            TSocketPool pool = connPoolMap.get(key);
            pool.close();
            logger.info("连接池{}已关闭", key);
        }
    }

}

class ServeThread extends Thread {

    private int port;
    private Map<String, Object> serviceMap;

    private static final Logger logger = LoggerFactory.getLogger(ServeThread.class);

    public ServeThread(int port, Map<String, Object> serviceMap) {
        this.port = port;
        this.serviceMap = serviceMap;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);
            TServerSocket tServerSocket = new TServerSocket(socket);
            TBinaryProtocol.Factory factory = new TBinaryProtocol.Factory();
            TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
            for (String serviceName : serviceMap.keySet()) {
                Class<?> serviceImplClass = serviceMap.get(serviceName).getClass();
                Class<?>[] interfaces = serviceImplClass.getInterfaces();
                for (Class<?> facade : interfaces) {
                    String facadeName = facade.getName();
                    if (facadeName.contains("$Iface")) {
                        String processorName = facadeName.substring(0, facadeName.indexOf("$")) + "$Processor";
                        Class<?> processorClass = Class.forName(processorName);
                        Constructor tProcessorConstructor = processorClass.getConstructors()[0];
                        TProcessor tProcessorObj = (TProcessor) tProcessorConstructor.newInstance(serviceMap.get(serviceName));
                        multiplexedProcessor.registerProcessor(serviceName, tProcessorObj);
                        // 假设对于每一个Iface，只有一个实现类
                        break;
                    }
                }
            }
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(tServerSocket);
            tArgs.processor(multiplexedProcessor);
            tArgs.protocolFactory(factory);
            TThreadPoolServer tServer = new TThreadPoolServer(tArgs);
            logger.info("Starry-Thrift-Srv 已启动, 端口: {}", port);
            tServer.serve();
        } catch (Exception e) {
            logger.error("Starry-Thrift 服务端启动失败，端口{}：{},", port, e);
        }
    }

}
