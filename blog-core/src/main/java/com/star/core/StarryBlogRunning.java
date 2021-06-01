package com.star.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * 项目启动
 * 奔跑吧 zzStar :)
 *
 * @Author: zzStar
 * @Date: 12-16-2020 19:37
 */
@EnableScheduling
@SpringBootApplication
public class StarryBlogRunning {

    public static void main(String[] args) {
        SpringApplication.run(StarryBlogRunning.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
