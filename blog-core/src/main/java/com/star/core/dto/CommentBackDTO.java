package com.star.core.dto;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台评论列表
 *
 * @Author: zzStar
 * @Date: 12-20-2020 17:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("后台评论")
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
     * 是否审核
     */
    private Integer isReview;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

}
