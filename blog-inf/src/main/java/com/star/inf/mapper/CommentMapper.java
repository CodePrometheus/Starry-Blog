package com.star.inf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.inf.dto.*;
import com.star.inf.entity.Comment;
import com.star.inf.vo.CommentVO;
import com.star.inf.vo.ConditionVO;
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
     * @param current   当前页码
     * @param size      大小
     * @param commentVO 评论信息
     * @return 评论集合
     */
    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size, @Param("commentVO") CommentVO commentVO);

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

    /**
     * 根据动态id获取评论量
     *
     * @param momentIdList 动态id列表
     * @return {@link List< CommentCountDTO >}动态评论量
     */
    List<CommentCountDTO> listMomentCountByMomentIds(List<Integer> momentIdList);

}
