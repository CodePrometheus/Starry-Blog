package com.star.core.config;

import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zzStar
 * @Date: 03-31-2021 15:06
 */
@Configuration
public class ElasticSearchConfig {

    @Resource
    private RestClientBuilder restClientBuilder;

    @Value("${spring.elasticsearch.host}")
    private String host;

    @Value("${spring.elasticsearch.port}")
    private String port;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 长时间停止ES访问后，与es服务器建立的HTTP连接死掉，需要重新建立连接
        return new RestHighLevelClient(restClientBuilder.setHttpClientConfigCallback(requestConfig ->
                requestConfig.setKeepAliveStrategy((response, context) ->
                        TimeUnit.MINUTES.toMillis(3))));
    }

}
