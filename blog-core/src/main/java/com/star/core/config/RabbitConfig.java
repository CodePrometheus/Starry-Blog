package com.star.core.config;

import org.springframework.amqp.core.*;
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

    public final static String ES_QUEUE = "starry_article_queue";
    public final static String ES_CHANGE = "starry_article_change";
    public final static String ES_BIND_KEY = "starry_article_bind_key";
    public final static String EMAIL_EXCHANGE = "starry_mail_send";
    public final static String EMAIL_QUEUE = "starry_email";

    @Bean
    public Queue queue() {
        return new Queue(ES_QUEUE);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(ES_CHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(ES_BIND_KEY);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

}
