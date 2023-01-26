package com.star.common.constant;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbitmq配置
 *
 * @Author: zzStar
 * @Date: 12-18-2020 12:42
 */
@Configuration
public class RabbitmqConst {

    public final static String CANAL_SEARCH_CHANGE = "canal_search_change";
    public final static String CANAL_SEARCH_QUEUE = "canal_search_queue";
    public final static String CANAL_SEARCH_KEY = "canal_search_key";

    public final static String ES_QUEUE = "starry_article_queue";
    public final static String ES_CHANGE = "starry_article_change";
    public final static String ES_BIND_KEY = "starry_article_bind_key";

    public final static String EMAIL_EXCHANGE = "starry_mail_send";
    public final static String EMAIL_QUEUE = "starry_email";

    /**
     * 延迟队列
     */
    public final static String REVIEW_QUEUE_DELAY = "review_queue_delay";

    /**
     * 死信队列
     */
    public final static String REVIEW_QUEUE_DEAD = "review_queue_dead";

    /**
     * 死信交换机
     */
    public final static String REVIEW_EXCHANGE_DEAD = "review_exchange_dead";

    /**
     * 延迟交换机
     */
    public final static String REVIEW_EXCHANGE_DELAY = "review_exchange_delay";


    public final static String REVIEW_KEY_DELAY = "review_key_delay";
    public final static String REVIEW_KEY_DEAD = "review_key_dead";


    @Bean(CANAL_SEARCH_CHANGE)
    public Exchange canalSearchExchange() {
        return ExchangeBuilder.directExchange(CANAL_SEARCH_CHANGE).build();
    }

    @Bean(CANAL_SEARCH_QUEUE)
    public Queue canalSearchQueue() {
        return QueueBuilder.durable(CANAL_SEARCH_QUEUE).build();
    }

    @Bean
    public Binding canalSearchBind(@Qualifier(CANAL_SEARCH_CHANGE) Exchange exchange,
                                   @Qualifier(CANAL_SEARCH_QUEUE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(CANAL_SEARCH_KEY).noargs();
    }

    /**
     * 设置延迟队列并绑定死信
     *
     * @return Queue
     */
    @Bean(REVIEW_QUEUE_DELAY)
    public Queue delayQueue() {
        return QueueBuilder.durable(REVIEW_QUEUE_DELAY)
                //指定死信路由交换机
                .withArgument("x-dead-letter-exchange", REVIEW_EXCHANGE_DEAD)
                .withArgument("x-dead-letter-routing-key", REVIEW_KEY_DEAD).build();
    }

    /**
     * 定义死信队列
     *
     * @return Queue
     */
    @Bean(REVIEW_QUEUE_DEAD)
    public Queue deadQueue() {
        return QueueBuilder.durable(REVIEW_QUEUE_DEAD).build();
    }


    /**
     * 定义死信交换机
     *
     * @return Exchange
     */
    @Bean(REVIEW_EXCHANGE_DEAD)
    public Exchange deadExchange() {
        return ExchangeBuilder.directExchange(REVIEW_EXCHANGE_DEAD).build();
    }

    /**
     * 定义延时交换机
     *
     * @return Exchange
     */
    @Bean(REVIEW_EXCHANGE_DELAY)
    public Exchange delayExchange() {
        return ExchangeBuilder.directExchange(REVIEW_EXCHANGE_DELAY).build();
    }

    /**
     * 绑定死信
     */
    @Bean("deadBinding")
    public Binding deadBinding(@Qualifier(REVIEW_EXCHANGE_DEAD) Exchange exchange,
                               @Qualifier(REVIEW_QUEUE_DEAD) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(REVIEW_KEY_DEAD).noargs();
    }

    /**
     * 绑定延迟
     */
    @Bean("delayBinding")
    public Binding delayBinding(@Qualifier(REVIEW_EXCHANGE_DELAY) Exchange exchange,
                                @Qualifier(REVIEW_QUEUE_DELAY) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(REVIEW_KEY_DELAY).noargs();
    }


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
