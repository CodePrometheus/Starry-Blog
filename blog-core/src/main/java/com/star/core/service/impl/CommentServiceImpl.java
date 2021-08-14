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
import com.star.core.mapper.CommentMapper;
import com.star.core.mapper.UserInfoMapper;
import com.star.core.vo.CommentVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.service.CommentService;
import com.star.core.util.HTMLUtil;
import com.star.core.util.UserUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
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
    private RedisUtil redisUtil;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private CommentMapper commentMapper;

    @Override
    public PageDTO<CommentDTO> listComments(Integer articleId, Long current) {
        // 查询文章评论量
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.isNull(articleId), Comment::getArticleId, articleId)
                .isNull(Objects.isNull(articleId), Comment::getArticleId)
                .isNull(Comment::getParentId)
                .eq(Comment::getIsDelete, FALSE));
        if (commentCount == 0) {
            return new PageDTO<>();
        }

        // 查询评论集合
        List<CommentDTO> commentDTOList = commentMapper.listComments(articleId, (current - 1) * 10);
        // 查询redis的评论点赞数据
        Map<String, Integer> likeCountMap = (Map<String, Integer>) redisUtil.hGetAll(COMMENT_LIKE_COUNT);
        // 提取评论id集合
        List<Integer> commentIdList = new ArrayList<>();
        for (CommentDTO commentDTO : commentDTOList) {
            commentIdList.add(commentDTO.getId());
            // 封装评论点赞量
            commentDTO.setLikeCount(Objects.requireNonNull(likeCountMap).get(commentDTO.getId().toString()));
        }

        // 根据评论id集合查询所有分页回复数据
        List<ReplyDTO> replyDTOList = commentMapper.listReplies(commentIdList);
        for (ReplyDTO replyDTO : replyDTOList) {
            // 封装回复点赞量
            replyDTO.setLikeCount(Objects.requireNonNull(likeCountMap).get(replyDTO.getId().toString()));
        }

        // 根据ParentId分组回复数据
        Map<Integer, List<ReplyDTO>> replyMap = replyDTOList.stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));

        // 封装评论id对应的评论数量
        Map<Integer, Integer> replyCountMap = commentMapper.listReplyCountByCommentId(commentIdList)
                .stream().collect(Collectors.toMap(ReplyCountDTO::getCommentId
                        , ReplyCountDTO::getReplyCount));

        // 将分页回复数据和回复量封装进对应的评论
        for (CommentDTO commentDTO : commentDTOList) {
            commentDTO.setReplyDTOList(replyMap.get(commentDTO.getId()));
            commentDTO.setReplyCount(replyCountMap.get(commentDTO.getId()));
        }
        return new PageDTO<>(commentDTOList, commentCount);
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId, Long current) {
        // 转换页码查询评论下的回复
        List<ReplyDTO> replyDTOList = commentMapper.listRepliesByCommentId(commentId, (current - 1) * 5);
        // 查询redis的评论点赞数据
        Map<String, Integer> likeCountMap = (Map<String, Integer>) redisUtil.hGetAll(COMMENT_LIKE_COUNT);
        for (ReplyDTO replyDTO : replyDTOList) {
            // 封装点赞数据
            replyDTO.setLikeCount(Objects.requireNonNull(likeCountMap).get(replyDTO.getId().toString()));
        }
        return replyDTOList;
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveComment(CommentVO commentVO) {
        // 过滤html标签
        commentVO.setCommentContent(HTMLUtil.deleteCommentTag(commentVO.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtil.getLoginUser().getUserInfoId())
                .replyId(commentVO.getReplyId())
                .articleId(commentVO.getArticleId())
                .commentContent(commentVO.getCommentContent())
                .parentId(commentVO.getParentId())
                .createTime(new Date()).build();
        commentMapper.insert(comment);
        // 通知用户
        notice(commentVO);
    }

    /**
     * 通知评论用户
     *
     * @param commentVO
     */
    @Async
    public void notice(CommentVO commentVO) {
        // 判断是回复用户还是评论作者
        Integer userId = Objects.nonNull(commentVO.getReplyId()) ? commentVO.getReplyId() : BLOGGER_ID;
        // 查询邮箱号
        String email = userInfoMapper.selectById(userId).getEmail();
        if (StringUtils.isNotBlank(email)) {
            // 判断页面路径
            String url = Objects.nonNull(commentVO.getArticleId()) ? URL + ARTICLE_PATH + commentVO.getArticleId() : URL + LINK_PATH;
            // 发送消息
            EmailDTO emailDTO = EmailDTO.builder()
                    .email(email)
                    .subject("评论提醒")
                    .content("你收到了一条新的回复，请前往 " + url + "页面查看").build();
            rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_EXCHANGE, "*",
                    new Message(JSON.toJSONBytes(emailDTO),
                            new MessageProperties()));
        }
    }


    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveCommentLike(Integer commentId) {
        // 查询当前用户点赞过的评论id集合
        Set<Integer> commentLikeSet = (HashSet<Integer>) redisUtil.hGet(COMMENT_USER_LIKE, UserUtil.getUserInfoId().toString());
        // 第一次点赞则创建
        if (CollectionUtils.isEmpty(commentLikeSet)) {
            commentLikeSet = new HashSet<>();
        }
        // 判断是否点赞
        if (commentLikeSet.contains(commentId)) {
            // 点过赞则删除评论id
            commentLikeSet.remove(commentId);
            // 评论点赞量-1
            redisUtil.hDecr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        } else {
            // 未点赞则增加评论id
            commentLikeSet.add(commentId);
            // 评论点赞量+1
            redisUtil.hIncr(COMMENT_LIKE_COUNT, commentId.toString(), 1L);
        }
        // 保存点赞记录
        redisUtil.hSet(COMMENT_USER_LIKE, UserUtil.getUserInfoId().toString(), commentLikeSet);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateCommentDelete(DeleteVO deleteVO) {
        // 修改评论逻辑删除状态
        List<Comment> commentList = deleteVO.getIdList().stream()
                .map(id -> Comment.builder()
                        .id(id)
                        .isDelete(deleteVO.getIsDelete()).build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    @Override
    public PageDTO<CommentBackDTO> listCommentBackDTO(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 统计后台所有评论量
        Integer count = commentMapper.countCommentBack(condition);
        if (count == 0) {
            return new PageDTO<>();
        }
        // 查询后台评论集合
        List<CommentBackDTO> commentBackDTOList = commentMapper.listCommentBackDTO(condition);
        // 获取评论点赞量
        Map<String, Integer> likeCountMap = redisUtil.hGetAll(COMMENT_LIKE_COUNT);
        // 封装点赞量
        for (CommentBackDTO commentBackDTO : commentBackDTOList) {
            commentBackDTO.setLikeCount(Objects.requireNonNull(likeCountMap)
                    .get(commentBackDTO.getId().toString()));
        }
        return new PageDTO<>(commentBackDTOList, count);
    }

}
