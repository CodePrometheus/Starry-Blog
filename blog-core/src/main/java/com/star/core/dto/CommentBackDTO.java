package com.star.core.dto;


import lombok.Data;

import java.util.Date;

/**
 * 后台评论列表
 *
 * @Author: zzStar
 * @Date: 12-20-2020 17:04
 */
@Data
public class CommentBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 被回复用户昵称
     */
    private String replyNickname;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 发表时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private Integer isDelete;

}
