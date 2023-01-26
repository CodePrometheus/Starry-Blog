package com.star.core.consumer;

import com.rabbitmq.client.Channel;
import com.star.common.constant.RabbitmqConst;
import com.star.common.tool.SensitiveUtils;
import com.star.inf.entity.Comment;
import com.star.inf.mapper.CommentMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @RabbitListener(queues = RabbitmqConst.REVIEW_QUEUE_DEAD)
    @Transactional(rollbackFor = Exception.class)
    public void autoReview(Integer commentId, Channel channel, Message message) throws IOException {
        long tagId = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(tagId, false);
        try {
            Comment comment = commentMapper.selectById(commentId);
            if (comment.getIsReview() == 1) {
                logger.info("该评论已经审核");
                channel.basicReject(tagId, false);
                return;
            }
            // 我是木立十机器人，自动审核程序开始，利用前缀树对评论进行过滤
            String filteredComment = SensitiveUtils.filter(comment.getCommentContent());
            comment.setCommentContent(filteredComment);
            comment.setIsReview(1);
            commentMapper.updateById(comment);
        } catch (Exception e) {
            logger.error(e.getMessage());
            channel.basicNack(tagId, false, true);
        }
    }

}
