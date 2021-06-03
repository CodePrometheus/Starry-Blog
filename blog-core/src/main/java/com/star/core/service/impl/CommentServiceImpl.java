package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.DeleteConst;
import com.star.common.constant.UserConst;
import com.star.common.exception.StarryException;
import com.star.core.domain.entity.Comment;
import com.star.core.domain.mapper.CommentMapper;
import com.star.core.domain.vo.CommentVO;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.DeleteVO;
import com.star.core.service.CommentService;
import com.star.core.service.dto.*;
import com.star.core.util.HTMLUtil;
import com.star.core.util.UserUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 评论业务
 *
 * @Author: zzStar
 * @Date: 12-20-2020 15:03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CommentService commentService;

    @Override
    public PageDTO<CommentDTO> listComments(Integer articleId, Long current) {
        //查询文章评论量
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(articleId != null, "article_id", articleId)
                .isNull(articleId == null, "article_id").isNull("parent_id")
                .eq("is_delete", DeleteConst.NORMAL);
        Integer commentCount = commentMapper.selectCount(queryWrapper);
        if (commentCount == 0) {
            return new PageDTO<>();
        }
        //查询评论集合
        List<CommentDTO> commentDTOList = commentMapper.listComments(articleId, (current - 1) * 10);
        //查询redis的评论点赞数据
        Map<String, Integer> likeCountMap = (Map<String, Integer>) redisTemplate.boundHashOps("comment_like_count").entries();
        //提取评论id集合
        List<Integer> commentIdList = new ArrayList<>();
        for (CommentDTO commentDTO : commentDTOList) {
            commentIdList.add(commentDTO.getId());
            //封装评论点赞量
            commentDTO.setLikeCount(likeCountMap.get(commentDTO.getId().toString()));
        }
        //根据评论id集合查询所有分页回复数据
        List<ReplyDTO> replyDTOList = commentMapper.listReplies(commentIdList);
        for (ReplyDTO replyDTO : replyDTOList) {
            //封装回复点赞量
            replyDTO.setLikeCount(likeCountMap.get(replyDTO.getId().toString()));
        }
        //根据评论id查询回复量
        List<ReplyCountDTO> replyCountDTOList = commentMapper.listReplyCountByCommentId(commentIdList);
        //将回复量封装成评论id对应回复量的map
        Map<Integer, Integer> replyCountMap = new HashMap<>(16);
        for (ReplyCountDTO replyCountDTO : replyCountDTOList) {
            replyCountMap.put(replyCountDTO.getCommentId(), replyCountDTO.getReplyCount());
        }
        //将分页回复数据和回复量封装进对应的评论
        for (CommentDTO commentDTO : commentDTOList) {
            List<ReplyDTO> replyList = new ArrayList<>();
            for (ReplyDTO replyDTO : replyDTOList) {
                if (replyDTO.getParentId().equals(commentDTO.getId())) {
                    replyList.add(replyDTO);
                }
            }
            commentDTO.setReplyDTOList(replyList);
            commentDTO.setReplyCount(replyCountMap.get(commentDTO.getId()));
        }
        return new PageDTO<CommentDTO>(commentDTOList, commentCount);
    }

    @Override
    public List<ReplyDTO> listRepliesByCommentId(Integer commentId, Long current) {
        //转换页码查询评论下的回复
        List<ReplyDTO> replyDTOList = commentMapper.listRepliesByCommentId(commentId, (current - 1) * 5);
        //查询redis的评论点赞数据
        Map<String, Integer> likeCountMap = (Map<String, Integer>) redisTemplate.boundHashOps("comment_like_count").entries();
        for (ReplyDTO replyDTO : replyDTOList) {
            //封装点赞数据
            replyDTO.setLikeCount(likeCountMap.get(replyDTO.getId().toString()));
        }
        return replyDTOList;
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void saveComment(CommentVO commentVO) {
        if (UserUtil.getLoginUser().getIsSilence() == UserConst.SILENCE) {
            throw new StarryException("您已被禁言");
        }
        //过滤html标签
        commentVO.setCommentContent(HTMLUtil.deleteCommentTag(commentVO.getCommentContent()));
        commentMapper.insert(new Comment(commentVO));
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void saveCommentLike(Integer commentId) {
        //查询当前用户点赞过的评论id集合
        Set<Integer> commentLikeSet = (Set<Integer>) redisTemplate.boundHashOps("comment_user_like").get(UserUtil.getLoginUser().getUserInfoId().toString());
        //第一次点赞则创建
        if (commentLikeSet == null) {
            commentLikeSet = new HashSet();
        }
        //判断是否点赞
        if (commentLikeSet.contains(commentId)) {
            //点过赞则删除评论id
            commentLikeSet.remove(commentId);
            //评论点赞量-1
            redisTemplate.boundHashOps("comment_like_count").increment(commentId.toString(), -1);
        } else {
            //未点赞则增加评论id
            commentLikeSet.add(commentId);
            //评论点赞量+1
            redisTemplate.boundHashOps("comment_like_count").increment(commentId.toString(), 1);
        }
        //保存点赞记录
        redisTemplate.boundHashOps("comment_user_like").put(UserUtil.getLoginUser().getUserInfoId().toString(), commentLikeSet);
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateCommentDelete(DeleteVO deleteVO) {
        //修改评论逻辑删除状态
        List<Comment> commentList = new ArrayList<>();
        for (Integer commentId : deleteVO.getIdList()) {
            commentList.add(new Comment(commentId, deleteVO.getIsDelete()));
        }
        commentService.updateBatchById(commentList);
    }

    @Override
    public PageDTO<CommentBackDTO> listCommentBackDTO(ConditionVO condition) {
        //转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        //统计后台评论量
        Integer count = commentMapper.countCommentBack(condition);
        if (count == 0) {
            return new PageDTO<>();
        }
        //查询后台评论集合
        List<CommentBackDTO> commentBackDTOList = commentMapper.listCommentBackDTO(condition);
        //获取评论点赞量
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps("comment_like_count").entries();
        //封装点赞量
        for (CommentBackDTO commentBackDTO : commentBackDTOList) {
            commentBackDTO.setLikeCount(likeCountMap.get(commentBackDTO.getId()));
        }
        return new PageDTO<CommentBackDTO>(commentBackDTOList, count);
    }

}
