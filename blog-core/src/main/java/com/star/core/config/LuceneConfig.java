package com.star.core.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zzStar
 * @Date: 09-28-2021 14:15
 */
@Configuration
public class LuceneConfig {

    @Bean
    public Analyzer analyzer() {
        return new SmartChineseAnalyzer();
    }


}
