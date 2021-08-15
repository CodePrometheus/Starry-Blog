package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.entity.Comment;
import com.star.core.vo.CommentVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.dto.CommentBackDTO;
import com.star.core.dto.CommentDTO;
import com.star.core.dto.PageData;
import com.star.core.dto.ReplyDTO;
import com.star.core.vo.ReviewVO;

import java.util.List;

/**
 * @author zzStar
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查看评论
     *
     * @param articleId 文章id
     * @return CommentListDTO
     */
    PageData<CommentDTO> listComments(Integer articleId);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @return 回复列表
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId, Long current);

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    void saveComment(CommentVO commentVO);

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    void saveCommentLike(Integer commentId);

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论列表
     */
    PageData<CommentBackDTO> listCommentBackDTO(ConditionVO condition);

    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     */
    void updateCommentsReview(ReviewVO reviewVO);
}
