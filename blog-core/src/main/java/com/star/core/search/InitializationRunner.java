package com.star.core.search;

import com.star.core.mapper.ElasticsearchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 06-02-2021 23:29
 */
@Component
public class InitializationRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(InitializationRunner.class);

    @Resource
    private ElasticSearchUtil elasticSearchUtil;

    @Resource
    private ElasticsearchMapper elasticsearchMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        long count = elasticsearchMapper.count();
        if (count == 0) {
            elasticSearchUtil.async();
            log.info("es 异步导入完成");
        } else {
            log.info("es 已存在索引");
        }
    }
}
