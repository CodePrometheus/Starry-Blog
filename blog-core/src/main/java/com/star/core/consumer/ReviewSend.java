package com.star.core.consumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.star.core.config.RabbitConfig;
import com.star.core.entity.Comment;
import com.star.core.mapper.CommentMapper;
import com.star.core.util.SensitiveUtils;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author: zzStar
 * @Date: 08-29-2021 10:33
 */
@Component
public class ReviewSend {

    private static final Logger logger = LoggerFactory.getLogger(ReviewSend.class);

    @Resource
    private CommentMapper commentMapper;

    @RabbitListener(queues = RabbitConfig.REVIEW_QUEUE_DEAD)
    @Transactional(rollbackFor = Exception.class)
    public void autoReview(Integer commentId, Channel channel, Message message) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        try {
            Comment comment = commentMapper.selectById(commentId);
            // 我是木里十机器人，自动审核程序开始，利用前缀树对评论进行过滤
            String filteredComment = SensitiveUtils.filter(comment.getCommentContent());
            comment.setCommentContent(filteredComment);
            comment.setIsReview(1);
            commentMapper.updateById(comment);
        } catch (Exception e) {
            logger.error(e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

}
