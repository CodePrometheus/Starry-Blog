package com.star.inf.search;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: zzStar
 * @Date: 06-02-2021 23:29
 */
@Component
public class InitializationRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(InitializationRunner.class);

    @Resource
    private ElasticSearchUtil elasticSearchUtil;
    // @BlogResource
    // private SolrSearchUtil solrSearchUtil;
    // @BlogResource
    // private LuceneSearchUtil luceneSearchUtil;

    @Override
    public void run(ApplicationArguments args) {
        try {
            elasticSearchUtil.async();
            log.info("ES更新完毕");
            // solrSearchUtil.async();
            // log.info("Solr更新完毕");
            // luceneSearchUtil.async();
            log.info("Lucene更新完毕");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
