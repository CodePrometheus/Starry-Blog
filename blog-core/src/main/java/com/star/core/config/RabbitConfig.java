package com.star.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbitmq配置
 *
 * @Author: zzStar
 * @Date: 12-18-2020 12:42
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue insertDirectQueue() {
        return new Queue("article", true);
    }

    @Bean
    FanoutExchange maxWellExchange() {
        return new FanoutExchange("maxwell", false, false);
    }

    @Bean
    Binding bindingArticleDirect() {
        return BindingBuilder.bind(insertDirectQueue()).to(maxWellExchange());
    }

}
