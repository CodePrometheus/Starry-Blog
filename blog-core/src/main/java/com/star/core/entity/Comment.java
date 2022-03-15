package com.star.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 评论
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_comment")
public class Comment {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论用户Id
     */
    private Integer userId;

    /**
     * 回复用户id
     */
    private Integer replyUserId;

    /**
     * 评论文章id
     */
    private Integer articleId;

    /**
     * 评论动态id
     */
    private Integer momentId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 评论类型 1.文章 2.友链 3.动态
     */
    private Integer type;

    /**
     * 是否审核
     */
    private Integer isReview;

    /**
     * 评论时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
