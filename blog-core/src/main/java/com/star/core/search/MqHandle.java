package com.star.core.search;

import com.star.core.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 06-03-2021 13:36
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitConfig.es_queue)
public class MqHandle {

    @Resource
    private ElasticSearchUtil elasticSearchUtil;

    @RabbitHandler
    public void handle(ArticleMqMessage mqMessage) {
        log.info("mq get a msg : {}", mqMessage.toString());
        switch (mqMessage.getType()) {
            case ArticleMqMessage.CREATE_OR_UPDATE:
                elasticSearchUtil.createOrUpdateIndex(mqMessage);
                break;
            case ArticleMqMessage.REMOVE:
                elasticSearchUtil.removeIndex(mqMessage);
                break;
            default:
                log.error("未找到对应消息类型 --> {}", mqMessage.toString());
                break;
        }
    }

}
