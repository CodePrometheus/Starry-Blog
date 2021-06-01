package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 评论点赞记录
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:31
 */
@Data
@TableName("tb_comment_like")
public class CommentLike {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞用户
     */
    private Integer userId;

    /**
     * 点赞评论
     */
    private Integer commentId;


}
