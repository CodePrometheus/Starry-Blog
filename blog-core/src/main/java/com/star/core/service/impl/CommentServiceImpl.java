package com.star.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtil;
import com.star.core.config.RabbitConfig;
import com.star.core.dto.*;
import com.star.core.entity.Comment;
import com.star.core.mapper.ArticleMapper;
import com.star.core.mapper.CommentMapper;
import com.star.core.mapper.UserInfoMapper;
import com.star.core.service.BlogInfoService;
import com.star.core.service.CommentService;
import com.star.core.util.HTMLUtil;
import com.star.core.util.PageUtils;
import com.star.core.util.UserUtil;
import com.star.core.vo.CommentVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.ReviewVO;
import com.star.core.vo.WebsiteConfigVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.*;
import static com.star.common.constant.RedisConst.COMMENT_LIKE_COUNT;
import static com.star.common.constant.RedisConst.COMMENT_USER_LIKE;

/**
 * 评论业务
 *
 * @Author: zzStar
 * @Date: 12-20-2020 15:03
 */
@Service
@SuppressWarnings("unchecked")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private BlogInfoService blogInfoService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public PageData<CommentDTO> listComments(Integer articleId) {
        // 查询文章评论量
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.isNull(articleId), Comment::getArticleId, articleId)
                .isNull(Objects.isNull(articleId), Comment::getArticleId)
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, TRUE));
        if (commentCount == 0) {
            return new PageData<>();
        }

        // 查询评论集合
        List<CommentDTO> commentList = commentMapper.listComments(PageUtils.getLimitCurrent(), PageUtils.getSize(), articleId);
        if (CollectionUtils.isEmpty(commentList)) {
            return new PageData<>();
        }
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisUtil.hGetAll(COMMENT_LIKE_COUNT);
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

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId, Long current) {
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyList = commentMapper.listRepliesByCommentId(PageUtils.getLimitCurrent(), PageUtils.getSize(), commentId);
        // 查询redis的评论点赞数据
        Map<String, Object> likeCountMap = redisUtil.hGetAll(COMMENT_LIKE_COUNT);
        // 封装点赞数据
        replyList.forEach(v ->
                v.setLikeCount((Integer) likeCountMap.get(v.getId().toString())));
        return replyList;
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveComment(CommentVO commentVO) {
        // 判断是否需要审核
        WebsiteConfigVO websiteConfig = blogInfoService.getWebsiteConfig();
        Integer isReward = websiteConfig.getIsReward();
        // 过滤html标签
        commentVO.setCommentContent(HTMLUtil.deleteCommentTag(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtil.getLoginUser().getUserInfoId())
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
        Integer commentId = comment.getId();
        // 判断是回复用户还是评论作者
        Integer authorId = articleMapper.selectById(comment.getArticleId()).getUserId();
        Integer userId = Optional.ofNullable(comment.getReplyUserId()).orElse(authorId);

        // 查询邮箱号
        String emailStr = userInfoMapper.selectById(userId).getEmail();
        if (StringUtils.isBlank(emailStr)) {
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
            rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(email),
                            new MessageProperties()));
        } else {
            // 管理员审核提醒
            EmailDTO email = new EmailDTO();
            String adminEmail = userInfoMapper.selectById(authorId).getEmail();
            email.setEmail(adminEmail);
            email.setSubject("审核提醒");
            email.setContent("您收到了一条新的回复，请前往后台管理页面审核，如果一天之内没有做出处理，本系统将会将其推送自动审核机制");
            rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(email),
                            new MessageProperties()));
            // 如果评论在规定时间内未处理，将交付给自动评审机制
            rabbitTemplate.convertAndSend(RabbitConfig.REVIEW_QUEUE_DELAY, commentId, msg -> {
                // 毫秒值 1min * 60 * 1000
                msg.getMessageProperties().setExpiration("60000");
                return msg;
            });
        }

    }


    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveCommentLike(Integer commentId) {
        String commentLikeKey = COMMENT_USER_LIKE + UserUtil.getUserInfoId();
        if (redisUtil.sIsMember(commentLikeKey, commentId)) {
            redisUtil.sRemove(commentLikeKey, commentId);
            redisUtil.hDecr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        } else {
            redisUtil.sAdd(commentLikeKey, commentId);
            redisUtil.hIncr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        }
    }


    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateCommentsReview(ReviewVO reviewVO) {
        List<Comment> commentList = reviewVO.getIdList().stream().map(v -> Comment.builder()
                        .id(v).isReview(reviewVO.getIsReview()).build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public PageData<CommentBackDTO> listCommentBackDTO(ConditionVO condition) {
        Integer count = commentMapper.countCommentBack(condition);
        if (count == 0) {
            return new PageData<>();
        }
        List<CommentBackDTO> commentBack = commentMapper.listCommentBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(commentBack, count);
    }

}
