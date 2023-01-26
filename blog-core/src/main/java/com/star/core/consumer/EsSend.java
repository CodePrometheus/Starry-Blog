package com.star.core.consumer;

import com.rabbitmq.client.Channel;
import com.star.common.constant.RabbitmqConst;
import com.star.inf.search.ArticleMqMessage;
import com.star.inf.search.ElasticSearchUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: zzStar
 * @Date: 06-03-2021 13:36
 */
@Component
@RabbitListener(queues = RabbitmqConst.ES_QUEUE)
public class EsSend {

    private static final Logger log = LoggerFactory.getLogger(EsSend.class);

    @Resource
    private ElasticSearchUtil elasticSearchUtil;

    @RabbitHandler
    public void handle(ArticleMqMessage mqMessage, Channel channel, Message message) throws IOException {
        log.info("mq get a msg : {}", mqMessage.toString());
        switch (mqMessage.getType()) {
            case ArticleMqMessage.CREATE_OR_UPDATE:
                elasticSearchUtil.createOrUpdateIndex(mqMessage);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                break;
            case ArticleMqMessage.REMOVE:
                elasticSearchUtil.removeIndex(mqMessage);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                break;
            default:
                log.error("未找到对应消息类型 --> {}", mqMessage);
                break;
        }
    }

}
