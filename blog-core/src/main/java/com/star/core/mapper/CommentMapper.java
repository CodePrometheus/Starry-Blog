package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.dto.CommentBackDTO;
import com.star.core.dto.CommentDTO;
import com.star.core.dto.ReplyCountDTO;
import com.star.core.dto.ReplyDTO;
import com.star.core.entity.Comment;
import com.star.core.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 评论
 * @Author: zzStar
 * @Date: 12-19-2020 16:31
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 评论列表
     * 查看评论
     *
     * @param articleId 文章id
     * @param current   当前页码
     * @param size      大小
     * @return 评论集合
     */
    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size, @Param("articleId") Integer articleId);

    /**
     * 查看评论id集合下的回复
     *
     * @param commentIdList 评论id集合
     * @return 回复集合
     */
    List<ReplyDTO> listReplies(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 根据评论id查询回复总量
     *
     * @param commentIdList 评论id集合
     * @return 回复数量
     */
    List<ReplyCountDTO> listReplyCountByCommentId(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 查看当条评论下的回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @param size      大小
     * @return 回复集合
     */
    List<ReplyDTO> listRepliesByCommentId(@Param("current") Long current, @Param("size") Long size, @Param("commentId") Integer commentId);

    /**
     * 统计后台评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    Long countCommentBack(@Param("condition") ConditionVO condition);

    /**
     * 查询后台评论
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentBackDTO> listCommentBack(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}
