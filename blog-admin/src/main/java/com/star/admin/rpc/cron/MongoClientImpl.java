package com.star.admin.rpc.cron;

import com.alibaba.fastjson2.JSON;
import com.star.rpc.thrift.annotation.ThriftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @Author: Starry
 * @Date: 02-25-2023
 */
@Service("mongoCron")
public class MongoClientImpl {

    private static final Logger log = LoggerFactory.getLogger(MongoClientImpl.class);

    @ThriftClient(host = "${thrift.client.host}", port = "${thrift.client.port}",
            timeout = 300, serviceName = "MongoCronService")
    public com.star.admin.rpc.cron.MongoCronService.Client mongoCronClient;

    public void mongoCronClient() {
        try {
            com.star.admin.rpc.cron.MongoCronReq mongoCronZhihu = new com.star.admin.rpc.cron.MongoCronReq();
            com.star.admin.rpc.cron.MongoCronReq mongoCronWeibo = new com.star.admin.rpc.cron.MongoCronReq();
            System.out.println("mongoCronClient = " + JSON.toJSONString(mongoCronClient));
            mongoCronZhihu.setTag("zhihu");
            mongoCronWeibo.setTag("weibo");
            mongoCronClient.mongoCronService(mongoCronZhihu);
            mongoCronClient.mongoCronService(mongoCronWeibo);
        } catch (Exception e) {
            log.error("mongoCronClient failed: ", e);
        }
    }

}
