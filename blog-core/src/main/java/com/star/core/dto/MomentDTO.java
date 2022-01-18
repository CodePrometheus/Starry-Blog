package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:33 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomentDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 内容
     */
    private String momentContent;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 状态码
     */
    private Integer isDelete;

    /**
     * 文章状态 1.公开 2.私密
     */
    private Integer status;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
