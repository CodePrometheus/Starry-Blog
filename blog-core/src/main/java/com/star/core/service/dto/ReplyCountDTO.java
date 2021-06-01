package com.star.core.service.dto;

import lombok.Data;

/**
 * 回复数量
 *
 * @Author: zzStar
 * @Date: 12-20-2020 16:08
 */
@Data
public class ReplyCountDTO {

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复数量
     */
    private Integer replyCount;

}
