package com.star.core.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zzStar
 * @Date: 03-31-2021 15:06
 */
@Configuration
public class ElasticsearchConfig implements RestClientBuilderCustomizer {

    @Value("${spring.elasticsearch.host}")
    private String host;

    @Value("${spring.elasticsearch.port}")
    private String port;

    @Bean
    public ElasticsearchClient esClient() {
        RestClient clientBuilder = RestClient.builder(new HttpHost(host, Integer.parseInt(port), "http")).build();
        RestClientTransport transport = new RestClientTransport(clientBuilder, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    @Override
    public void customize(RestClientBuilder builder) {
        // es默认 connectTimeout 1s，socketTimeout 30s
        builder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                .setConnectTimeout(5000)
                .setSocketTimeout(60000));
    }

}
