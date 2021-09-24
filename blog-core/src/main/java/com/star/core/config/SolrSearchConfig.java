package com.star.core.config;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

/**
 * @Author: zzStar
 * @Date: 2021/9/24
 * @Description:
 */
@Configuration
public class SolrSearchConfig {

    @Value("${spring.data.solr.host}")
    private String host;

    @Bean
    public SolrTemplate solrTemplate() {
        HttpSolrClient solrClient = new HttpSolrClient.Builder(host)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000).build();
        return new SolrTemplate(solrClient);
    }

}
