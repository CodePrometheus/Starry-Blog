package com.star.inf.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.RabbitmqConst;
import com.star.common.exception.StarryException;
import com.star.common.tool.HtmlUtils;
import com.star.common.tool.PageUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.*;
import com.star.inf.entity.Comment;
import com.star.inf.mapper.ArticleMapper;
import com.star.inf.mapper.CommentMapper;
import com.star.inf.mapper.UserInfoMapper;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.CommentVO;
import com.star.inf.vo.WebsiteConfigVO;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.*;
import static com.star.common.constant.RedisConst.COMMENT_LIKE_COUNT;
import static com.star.common.constant.RedisConst.COMMENT_USER_LIKE;
import static com.star.common.constant.UserConst.BLOGGER_ID;
import static com.star.common.enums.CommentTypeEnum.*;

/**
 * 评论业务
 *
 * @Author: zzStar
 * @Date: 12-20-2020 15:03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements IService<Comment> {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private BlogInfoServiceImpl blogInfoServiceImpl;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private CommentMapper commentMapper;

    public PageData<CommentDTO> listComments(CommentVO commentVO) {
        // 查询文章评论量
        Long commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(ARTICLE.getType().equals(commentVO.getType()), Comment::getArticleId, commentVO.getArticleId())
                .eq(LINK.getType().equals(commentVO.getType()), Comment::getType, commentVO.getType())
                .eq(MOMENT.getType().equals(commentVO.getType()), Comment::getMomentId, commentVO.getMomentId())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, TRUE));
        if (commentCount == 0) {
            return new PageData<>();
        }

        // 查询评论集合
        List<CommentDTO> commentList = commentMapper.listComments(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentVO);
        if (CollectionUtils.isEmpty(commentList)) {
            return new PageData<>();
        }

        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisUtils.hGetAll(COMMENT_LIKE_COUNT);
        // 提取评论id集合
        List<Integer> commentIdList = commentList.stream().map(CommentDTO::getId)
                .collect(Collectors.toList());

        // 根据评论id集合查询所有分页回复数据
        List<ReplyDTO> replyList = commentMapper.listReplies(commentIdList);

        // 封装回复点赞量
        replyList.forEach(v -> v.setLikeCount((Integer) likeCountMap.get(v.getId().toString())));

        // 根据ParentId分组回复数据
        Map<Integer, List<ReplyDTO>> replyMap = replyList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));

        // 封装评论id对应的回复数量
        Map<Integer, Integer> replyCountMap = commentMapper.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId
                        , ReplyCountDTO::getReplyCount));

        // 封装评论数据
        commentList.forEach(v -> {
            v.setReplyList(replyMap.get(v.getId()));
            v.setReplyCount(replyCountMap.get(v.getId()));
            v.setLikeCount((Integer) likeCountMap.get(v.getId().toString()));
        });
        return new PageData<>(commentList, commentCount);
    }

    public List<ReplyDTO> listRepliesByCommentId(Integer commentId) {
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyList = commentMapper.listRepliesByCommentId(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentId);
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisUtils.hGetAll(COMMENT_LIKE_COUNT);
        // 封装点赞数据
        replyList.forEach(v ->
                v.setLikeCount((Integer) likeCountMap.get(v.getId().toString())));
        return replyList;
    }

    @Transactional(rollbackFor = StarryException.class)
    public void saveComment(CommentVO commentVO) {
        // 判断是否需要审核
        WebsiteConfigVO websiteConfig = blogInfoServiceImpl.getWebsiteConfig();
        Integer isReward = websiteConfig.getIsReward();
        // 过滤html标签
        commentVO.setCommentContent(HtmlUtils.deleteCommentTag(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .type(commentVO.getType())
                .momentId(commentVO.getMomentId())
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .replyUserId(commentVO.getReplyUserId())
                .articleId(commentVO.getArticleId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .isReview(isReward == TRUE ? FALSE : TRUE).build();
        commentMapper.insert(comment);
        // 判断是否开启邮箱通知,通知用户
        if (websiteConfig.getIsEmailNotice().equals(TRUE)) {
            notice(comment);
        }
    }

    /**
     * 通知评论用户
     *
     * @param comment 评论信息
     */
    @Async
    public void notice(Comment comment) {
        Integer authorId;
        if (Objects.isNull(comment.getArticleId())) {
            authorId = BLOGGER_ID;
        } else {
            authorId = articleMapper.selectById(comment.getArticleId()).getUserId();
        }
        Integer commentId = comment.getId();
        // 判断是回复用户还是评论作者
        Integer userId = Optional.ofNullable(comment.getReplyUserId()).orElse(authorId);

        // 查询邮箱号
        String emailStr = userInfoMapper.selectById(userId).getEmail();
        if (Objects.isNull(emailStr)) {
            return;
        }
        // 判断页面路径
        String url = Objects.nonNull(comment.getArticleId()) ? URL + ARTICLE_PATH + comment.getArticleId() : URL + LINK_PATH;
        // 发送消息
        if (comment.getIsReview().equals(TRUE)) {
            EmailDTO email = new EmailDTO();
            email.setEmail(emailStr);
            email.setSubject("评论提醒");
            email.setContent("你收到了一条新的回复，请前往 " + url + "页面查看");
            rabbitTemplate.convertAndSend(RabbitmqConst.EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(email),
                            new MessageProperties()));
        } else {
            // 管理员审核提醒
            EmailDTO email = new EmailDTO();
            String adminEmail = userInfoMapper.selectById(authorId).getEmail();
            email.setEmail(adminEmail);
            email.setSubject("审核提醒");
            email.setContent("您收到了一条新的回复，请前往后台管理页面审核，如果一天之内没有做出处理，本系统将会将其推送自动审核机制");
            rabbitTemplate.convertAndSend(RabbitmqConst.EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(email),
                            new MessageProperties()));
            // 如果评论在规定时间内未处理，将交付给自动评审机制
            rabbitTemplate.convertAndSend(RabbitmqConst.REVIEW_QUEUE_DELAY, commentId, msg -> {
                // 毫秒值 1min * 60 * 1000
                msg.getMessageProperties().setExpiration("60000");
                return msg;
            });
        }
    }

    @Transactional(rollbackFor = StarryException.class)
    public void saveCommentLike(Integer commentId) {
        String commentLikeKey = COMMENT_USER_LIKE + UserUtils.getUserInfoId();
        if (redisUtils.sIsMember(commentLikeKey, commentId)) {
            redisUtils.sRemove(commentLikeKey, commentId);
            redisUtils.hDecr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        } else {
            redisUtils.sAdd(commentLikeKey, commentId);
            redisUtils.hIncr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        }
    }

}
