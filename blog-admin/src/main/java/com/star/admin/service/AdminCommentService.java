package com.star.admin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.CommentBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Comment;
import com.star.inf.mapper.CommentMapper;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.ReviewVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminCommentService extends ServiceImpl<CommentMapper, Comment> {

    @Resource
    private CommentMapper commentMapper;

    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateCommentsReview(ReviewVO reviewVO) {
        List<Comment> commentList = reviewVO.getIdList().stream().map(v -> Comment.builder()
                        .id(v).isReview(reviewVO.getIsReview()).build())
                .collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论列表
     */
    public PageData<CommentBackDTO> listCommentBackDTO(ConditionVO condition) {
        Long count = commentMapper.countCommentBack(condition);
        if (count == 0) {
            return new PageData<>();
        }
        List<CommentBackDTO> commentBack = commentMapper.listCommentBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(commentBack, count);
    }

}
