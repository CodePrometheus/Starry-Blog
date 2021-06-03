package com.star.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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

    public final static String es_queue = "starry_article_queue";
    public final static String es_change = "starry_article_change";
    public final static String es_bind_key = "starry_article_bind_key";

    @Bean
    public Queue queue() {
        return new Queue(es_queue);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(es_change);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(es_bind_key);
    }

}
