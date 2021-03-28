package com.star;

import org.mybatis.spring.annotation.MapperScan;
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
@SpringBootApplication
@EnableScheduling
@MapperScan("com.star.domain.mapper")
public class StarryBlogRunning {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(StarryBlogRunning.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
