package com.star.admin.rpc.hello;

import com.star.rpc.thrift.annotation.ThriftServer;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

/**
 * @Author: Starry
 * @Date: 02-18-2023
 */
@Service
@ThriftServer(port = "${thrift.server.port}", serviceName = "helloService")
public class HelloServiceImpl implements com.star.admin.rpc.HelloService.Iface {

    @Override
    public com.star.admin.rpc.HelloResp Hello(com.star.admin.rpc.HelloReq req) throws TException {
        return new com.star.admin.rpc.HelloResp("Java Hello...");
    }

}
