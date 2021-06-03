package com.star.core.search;

import com.star.core.domain.mapper.ElasticsearchMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 06-02-2021 23:29
 */
@Slf4j
@Component
public class InitializationRunner implements ApplicationRunner {

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
