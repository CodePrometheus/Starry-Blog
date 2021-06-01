package com.star.core.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.service.dto.CommentBackDTO;
import com.star.core.service.dto.CommentDTO;
import com.star.core.service.dto.ReplyCountDTO;
import com.star.core.service.dto.ReplyDTO;
import com.star.core.domain.entity.Comment;
import com.star.core.domain.vo.ConditionVO;
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
     * 查看评论
     *
     * @param articleId 文章id
     * @param current   当前页码
     * @return 评论集合
     */
    List<CommentDTO> listComments(Integer articleId, Long current);

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
     * 查看当条评论下的所有回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @return 回复集合
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId, long current);

    /**
     * 统计后台评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    Integer countCommentBack(@Param("condition") ConditionVO condition);

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentBackDTO> listCommentBackDTO(@Param("condition") ConditionVO condition);

}
