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

    @Override
    public void run(ApplicationArguments args) {
        elasticSearchUtil.async();
        log.info("ES更新完毕");
    }

}
