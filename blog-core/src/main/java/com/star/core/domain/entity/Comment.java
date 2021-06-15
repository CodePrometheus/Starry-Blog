package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Integer replyId;

    /**
     * 评论文章id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 状态码
     */
    private Integer isDelete;

}
