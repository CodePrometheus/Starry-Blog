package com.star.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目启动
 * 奔跑吧 zzStar ;)
 *
 * @Author: zzStar
 * @Date: 12-16-2020 19:37
 */
@EnableCaching
@EnableScheduling
@SpringBootApplication
@MapperScan({"com.star.admin", "com.star.common", "com.star.core", "com.star.inf", "com.star.rpc"})
@ComponentScan(basePackages = {"com.star.admin", "com.star.common", "com.star.core", "com.star.inf", "com.star.rpc"})
@EnableElasticsearchRepositories(basePackages = "com.star.inf.mapper")
public class StarryBlogRunning {

    public static void main(String[] args) {
        SpringApplication.run(StarryBlogRunning.class, args);
    }

}
